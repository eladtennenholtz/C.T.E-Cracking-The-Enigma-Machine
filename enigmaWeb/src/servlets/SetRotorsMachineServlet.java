package servlets;

import data.DataFromEngineToUiRotors;
import data.DataFromUiToEngineInputRotors;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import logic.BattleField;
import logic.Engine;
import users.UserUBoatManager;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Collection;

import static constants.ConstantsWeb.USERNAME;

@WebServlet(name = "SetRotorsMachineServlet", urlPatterns = {"/setRotorsMachine"})
public class SetRotorsMachineServlet extends HttpServlet {

    Object lock=new Object();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/plain;charset=UTF-8");


        synchronized (lock) {
            UserUBoatManager userUBoatManager = ServletUtils.getUserUBoatManager(getServletContext());
            String usernameFromSession = SessionUtils.getUsername(request);
            String usernameFromParameter = request.getParameter(USERNAME);
            String rotors = request.getParameter("rotors");
            BattleField battleField = (BattleField) ServletUtils.getBattleField(usernameFromParameter, getServletContext());
            Engine engine = battleField.getEngine();
            DataFromUiToEngineInputRotors data = new DataFromUiToEngineInputRotors(rotors);
            DataFromEngineToUiRotors resultData = engine.settingRotorsToMachine(data);
            if(resultData.getData().charAt(0)=='G'){
                response.setStatus(HttpServletResponse.SC_OK);
            }else{
                response.setStatus(HttpServletResponse.SC_CONFLICT);
            }
        }
    }


}
