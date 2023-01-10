package servlets;

import exceptionsFromXml.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import logic.BattleField;
import logic.Engine;
import logic.EngineFirst;
import users.BattleFieldManager;
import users.UserUBoatManager;
import utils.ServletUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Scanner;

@WebServlet("/upload-file")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class FileUploadServlet extends HttpServlet {
    Object lock=new Object();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        InputStream inputStream=null;
        String currentUser="";

        Collection<Part> parts = request.getParts();
        int count=0;
        for (Part part : parts) {
            if(count==0) {
                inputStream = part.getInputStream();
                count++;
            }else{
                currentUser=part.getName();
            }
        }

        synchronized (lock) {
            //BattleField battleField = ServletUtils.getBattleField(currentUser, getServletContext());
            Engine engine = new EngineFirst();

            try {
                engine.readTheMachineXmlFile(inputStream, currentUser);
                BattleField battleField=new BattleField();
                inputStream.reset();
                String xml=new Scanner(inputStream).useDelimiter("\\Z").next();
                battleField.setMyXml(xml);
                battleField.setBattleFieldName(UserUBoatManager.getAllBattleFieldsMap().get(currentUser));
                battleField.setUBoatName(currentUser);
                battleField.setEngine(engine);
                battleField.setAllies(engine.getDetails().getNumberOfAllies());
                battleField.setLevel(engine.getDetails().getLevel());
                battleField.setStatus(false);
                battleField.setAlliesLogged(0);
                ServletUtils.setBattleField(currentUser,getServletContext(),battleField);
                BattleFieldManager battleFieldManager=ServletUtils.getAllBattleFieldsManager(getServletContext());
                boolean res=battleFieldManager.addBattle(battleField.getBattleField(),battleField);
               // UserUBoatManager userUBoatManager = ServletUtils.getUserUBoatManager(getServletContext());
                //battleField.setBattleFieldName(userUBoatManager.getAllBattleFieldsMap().get(currentUser));
                //ServletUtils.setBattleField(currentUser, getServletContext());
                response.setStatus(HttpServletResponse.SC_OK);
            } catch (FileDoesNotExistException e) {
                //alert.setContentText("File Does Not Exist");
                //alert.showAndWait();
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                response.getWriter().println("File Does Not Exist");
            } catch (RotorsCountException e) {
                //alert.setContentText(e.getAnswer());
                //alert.showAndWait();
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                response.getWriter().println(e.getAnswer());
            } catch (AlphabetSizeException e) {
                //alert.setContentText(e.getAnswer());
                //alert.showAndWait();
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                response.getWriter().println(e.getAnswer());
            } catch (IdRotorException e) {
                //alert.setContentText("Id Rotor problem");
                //alert.showAndWait();
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                response.getWriter().println("Id Rotor problem");
            } catch (MappingRotorException e) {
                //alert.setContentText("Rotor mapped wrong");
                //alert.showAndWait();
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                response.getWriter().println("Rotor mapped wrong");
            } catch (NotchException e) {
                //alert.setContentText("Notch problem");
                //alert.showAndWait();
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                response.getWriter().println("Notch problem");
            } catch (IdReflectorException e) {
                //alert.setContentText(e.getAnswer());
                //alert.showAndWait();
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                response.getWriter().println(e.getAnswer());
            } catch (MappingReflectorException e) {
                //alert.setContentText("Reflector mapped to the same place");
                //alert.showAndWait();
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                response.getWriter().println("Reflector mapped to the same place");
            } catch (ExceptionAll e) {
                //alert.setContentText(e.getResult());
                response.setStatus(HttpServletResponse.SC_CONFLICT);
               response.getWriter().println(e.getResult());

            } catch (Exception e) {
                //alert.setContentText("That is not a legal Xml schema for this specific exam");

            }
        }


    }








}
