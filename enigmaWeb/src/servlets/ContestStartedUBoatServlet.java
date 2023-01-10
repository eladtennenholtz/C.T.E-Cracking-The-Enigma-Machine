package servlets;

import com.google.gson.Gson;
import dataWeb.DataContestsWeb;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.BattleField;
import users.BattleFieldManager;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import static constants.ConstantsWeb.USERNAME;

@WebServlet(name = "ContestStartedUBoatServlet", urlPatterns = {"/contestStartedUBoat"})
public class ContestStartedUBoatServlet extends HttpServlet {

    Object object=new Object();
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //returning JSON objects, not HTML
        response.setContentType("application/json");
        synchronized (object) {
                Gson gson = new Gson();
                BattleFieldManager battleFieldManager = ServletUtils.getAllBattleFieldsManager(getServletContext());
                //Set<String> usersList = userManager.getUsers();
                HashMap<String, BattleField> allBattleFieldsMap = battleFieldManager.getAllBattleFields();
                if(allBattleFieldsMap.size()==0){
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    return;
                }

                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                allBattleFieldsMap.forEach((s, battleField) -> {
                    if(battleField.getUBoatName().equals(request.getParameter(USERNAME))){
                        if(battleField.isContestStarted()){
                           response.setStatus(HttpServletResponse.SC_OK);
                        }
                    }
                });

        }
    }
}
