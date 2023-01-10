package servlets;

import data.DataFromEngineToUiPlugBoard;
import data.DataFromUiToEngineInputPlugBoard;
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
import java.util.ArrayList;

import static constants.ConstantsWeb.USERNAME;

@WebServlet(name = "SetAllManuallyMachineServlet", urlPatterns = {"/setAllManuallyMachine"})
public class SetAllManuallyMachineServlet extends HttpServlet {

    Object lock=new Object();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/plain;charset=UTF-8");

        synchronized (lock) {
            UserUBoatManager userUBoatManager = ServletUtils.getUserUBoatManager(getServletContext());
            String usernameFromSession = SessionUtils.getUsername(request);
            String usernameFromParameter = request.getParameter(USERNAME);
            String rotors=request.getParameter("rotors");
            String positions=request.getParameter("positions");
            String reflector=request.getParameter("reflector");
            String plugs = request.getParameter("plugs");
            ArrayList<String>inputs=new ArrayList<>();
            inputs.add(rotors);
            inputs.add(positions);
            inputs.add(reflector);
            inputs.add(plugs);
            BattleField battleField = (BattleField) ServletUtils.getBattleField(usernameFromParameter, getServletContext());
            Engine engine = battleField.getEngine();
            engine.settingAllManually(inputs);
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }

}
