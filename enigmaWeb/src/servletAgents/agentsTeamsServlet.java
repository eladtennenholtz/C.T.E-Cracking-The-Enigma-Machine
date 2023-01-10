package servletAgents;


import com.google.gson.Gson;
import dataWeb.DataContestsWeb;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.BattleField;
import users.AlliesManager;
import users.BattleFieldManager;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

@WebServlet(name = "agentsTeamsServlet", urlPatterns = {"/agentsTeams"})
public class agentsTeamsServlet extends HttpServlet {


    Object object = new Object();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //returning JSON objects, not HTML
        response.setContentType("application/json");
        synchronized (object) {
            try (PrintWriter out = response.getWriter()) {
                Gson gson = new Gson();
                //BattleFieldManager battleFieldManager = ServletUtils.getAllBattleFieldsManager(getServletContext());
                //Set<String> usersList = userManager.getUsers();
                //HashMap<String, BattleField> allBattleFieldsMap = battleFieldManager.getAllBattleFields();
                //if (allBattleFieldsMap.size() == 0) {
                  //  response.setStatus(HttpServletResponse.SC_CONFLICT);
                    //return;
                //}
                AlliesManager alliesManager = ServletUtils.getAllAlliesManager(getServletContext());
                //allBattleFieldsMap.forEach((s, battleField) -> {
                  // for (int i=0;i<battleField.getAlliesInBattle().size();i++){
                    //   allTeams.add(battleField.getAlliesInBattle().get(i));
                   //}
                //});
                String json = gson.toJson(alliesManager.getAllAlliesNames());
                out.println(json);
                out.flush();
            }
        }
    }
}
