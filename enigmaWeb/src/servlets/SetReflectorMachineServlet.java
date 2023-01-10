package servlets;

import data.DataFromEngineToUiReflector;
import data.DataFromEngineToUiRotors;
import data.DataFromUiToEngineInputRotors;
import data.DataFromUiToEngineReflector;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.BattleField;
import logic.Engine;
import users.UserUBoatManager;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;

import static constants.ConstantsWeb.USERNAME;

@WebServlet(name = "SetReflectorMachineServlet", urlPatterns = {"/setReflectorMachine"})
public class SetReflectorMachineServlet extends HttpServlet {

    Object lock=new Object();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/plain;charset=UTF-8");

        synchronized (lock) {
            UserUBoatManager userUBoatManager = ServletUtils.getUserUBoatManager(getServletContext());
            String usernameFromSession = SessionUtils.getUsername(request);
            String usernameFromParameter = request.getParameter(USERNAME);
            String reflector = request.getParameter("reflector");
            BattleField battleField = (BattleField) ServletUtils.getBattleField(usernameFromParameter, getServletContext());
            Engine engine = battleField.getEngine();
            DataFromUiToEngineReflector dataReflector=new DataFromUiToEngineReflector(reflector);
            DataFromEngineToUiReflector resultData=engine.settingReflectors(dataReflector);
            if(resultData.getAnswer().charAt(0)=='G'){
                response.setStatus(HttpServletResponse.SC_OK);
            }else{
                response.setStatus(HttpServletResponse.SC_CONFLICT);
            }
        }
    }

}
