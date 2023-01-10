package servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.Ally;
import logic.BattleField;
import users.AlliesManager;
import users.BattleFieldManager;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import static constants.ConstantsWeb.USERNAME;

@WebServlet(name = "ReadyToStartContestUBoatServlet", urlPatterns = {"/readyToStartContestUBoat"})
public class ReadyToStartContestUBoatServlet extends HttpServlet {

    Object object = new Object();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //returning JSON objects, not HTML
        response.setContentType("application/json");
        synchronized (object) {
                BattleFieldManager battleFieldManager = ServletUtils.getAllBattleFieldsManager(getServletContext());
                HashMap<String, BattleField> allBattleFieldsMap = battleFieldManager.getAllBattleFields();
                if (allBattleFieldsMap.size() == 0) {
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    return;
                }
                String nameOfUser = request.getParameter(USERNAME);

                response.setStatus(HttpServletResponse.SC_CONFLICT);
                allBattleFieldsMap.forEach((s, battleField) -> {
                    if (battleField.getUBoatName().equals(nameOfUser)) {
                        battleField.setEncryptedStringForContest(request.getParameter("encrypted"));
                        battleField.setUBoatReadyToStartContest(true);
                        response.setStatus(HttpServletResponse.SC_OK);
                    }
                });
                if (response.getStatus() != 200) {
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                }

        }
    }







}
