package servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.BattleField;
import users.BattleFieldManager;
import users.UserUBoatManager;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.util.HashMap;

import static constants.ConstantsWeb.USERNAME;

@WebServlet(name = "FoundTheWinnerServlet", urlPatterns = {"/foundTheWinnerUBoat"})
public class FoundTheWinnerServlet extends HttpServlet {
    Object object = new Object();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //returning JSON objects, not HTML
        response.setContentType("application/json");
        synchronized (object) {

            UserUBoatManager userUBoatManager = ServletUtils.getUserUBoatManager(getServletContext());
            String usernameFromSession = SessionUtils.getUsername(request);
            String usernameFromParameter = request.getParameter(USERNAME);
            String team = request.getParameter("teamname");
            BattleField battleField = (BattleField) ServletUtils.getBattleField(usernameFromParameter, getServletContext());
            battleField.setWinnerOfContest(team);
            battleField.setContestOver(true);

            //System.out.println("winner!"+team);

            response.setStatus(HttpServletResponse.SC_OK);

        }
    }

}
