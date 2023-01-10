package servlets;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import data.DataFromEngineToUiShowingTheMachine;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.BattleField;
import logic.CurrentMachineDetails;
import logic.Engine;
import users.UserUBoatManager;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;

import static constants.ConstantsWeb.USERNAME;

@WebServlet(name = "DisplayMachineSettingsServlet", urlPatterns = {"/displayMachineSettings"})
public class DisplayMachineSettingsServlet extends HttpServlet {
    Object lock=new Object();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/plain;charset=UTF-8");
        synchronized (lock) {
            UserUBoatManager userUBoatManager = ServletUtils.getUserUBoatManager(getServletContext());
            String usernameFromSession = SessionUtils.getUsername(request);
            String usernameFromParameter = request.getParameter(USERNAME);
            BattleField battleField = (BattleField) ServletUtils.getBattleField(usernameFromParameter, getServletContext());
            Engine engine = battleField.getEngine();
            DataFromEngineToUiShowingTheMachine machineSetting = engine.displayingTheMachineSetting();
            String jsonResponse = machineSetting.getCurrentStage();
            try (PrintWriter out = response.getWriter()) {
                out.print(jsonResponse);
                out.flush();
            }
        }
    }
}
