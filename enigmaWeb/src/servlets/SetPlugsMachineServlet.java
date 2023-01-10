package servlets;

import data.DataFromEngineToUiPlugBoard;
import data.DataFromEngineToUiReflector;
import data.DataFromUiToEngineInputPlugBoard;
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
@WebServlet(name = "SetPlugsMachineServlet", urlPatterns = {"/setPlugsMachine"})
public class SetPlugsMachineServlet extends HttpServlet {

    Object lock=new Object();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/plain;charset=UTF-8");

        synchronized (lock) {
            UserUBoatManager userUBoatManager = ServletUtils.getUserUBoatManager(getServletContext());
            String usernameFromSession = SessionUtils.getUsername(request);
            String usernameFromParameter = request.getParameter(USERNAME);
            String plugs = request.getParameter("plugs");
            BattleField battleField = (BattleField) ServletUtils.getBattleField(usernameFromParameter, getServletContext());
            Engine engine = battleField.getEngine();
            DataFromUiToEngineInputPlugBoard dataPlugBoard = new DataFromUiToEngineInputPlugBoard(plugs);
            DataFromEngineToUiPlugBoard resultData= engine.settingPlugBoard(dataPlugBoard);
            if(resultData.getAnswer().isEmpty()){
                response.setStatus(HttpServletResponse.SC_OK);
            }
            else {
                if (resultData.getAnswer().charAt(0) == 'G') {
                    response.setStatus(HttpServletResponse.SC_OK);
                } else {
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                }
            }
        }
    }

}
