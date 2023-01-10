package servlets;

import data.DataFromEngineToUi;
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
@WebServlet(name = "ResetSettingMachineServlet", urlPatterns = {"/reset"})
public class ResetSettingMachineServlet extends HttpServlet {

    Object lock=new Object();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/plain;charset=UTF-8");

        synchronized (lock) {
            UserUBoatManager userUBoatManager = ServletUtils.getUserUBoatManager(getServletContext());
            String usernameFromSession = SessionUtils.getUsername(request);
            String usernameFromParameter = request.getParameter(USERNAME);
            BattleField battleField = (BattleField) ServletUtils.getBattleField(usernameFromParameter, getServletContext());
            Engine engine = battleField.getEngine();
            DataFromEngineToUi data=engine.resetSettings();
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }





}
