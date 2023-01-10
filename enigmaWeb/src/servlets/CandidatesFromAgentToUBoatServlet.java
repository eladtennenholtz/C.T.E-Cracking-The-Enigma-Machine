package servlets;

import com.google.gson.Gson;
import data.DataFromAgentToDm;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.Agent;
import logic.Ally;
import logic.BattleField;
import users.AlliesManager;
import users.BattleFieldManager;
import utils.ServletUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Properties;

import static constants.ConstantsWeb.USERNAME;
@WebServlet(name = "CandidatesFromAgentToUBoatServlet", urlPatterns = {"/candidatesFromAgentsToUBoat"})
public class CandidatesFromAgentToUBoatServlet extends HttpServlet {
    Object object=new Object();
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        synchronized (object) {
            Gson gson = new Gson();
        //returning JSON objects, not HTML
        response.setContentType("application/json");
        Properties prop=new Properties();
        prop.load(request.getInputStream());
       DataFromAgentToDm[] data= gson.fromJson(prop.getProperty("code"), DataFromAgentToDm[].class);

            BattleFieldManager battleFieldManager = ServletUtils.getAllBattleFieldsManager(getServletContext());
            HashMap<String, BattleField> allBattleFieldsMap = battleFieldManager.getAllBattleFields();
            if (allBattleFieldsMap.size() == 0) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                return;
            }
            BattleField battleField=allBattleFieldsMap.get(request.getHeader("battle"));
        String userName=request.getHeader("username");
        String team=request.getHeader("team");
       // String candidates=request.getHeader("candidates");
        //String taskedPreformed=request.getHeader("taskedPreformed");
        //String taskRemain=request.getHeader("taskRemain");
        //AlliesManager alliesManager = ServletUtils.getAllAlliesManager(getServletContext());
        //Ally ally=alliesManager.getAllAllies().get(team);
        //Agent agent=ally.getAgentsMapForCurrentCompetition().get(userName);
        //agent.setCandidates(Integer.parseInt(candidates));
        //agent.setTaskPreformed(Integer.parseInt(taskedPreformed));
        //agent.setTaskRemain(Integer.parseInt(taskRemain));
            for(int i=0;i<data.length;i++){
                data[i].setTeamName(team);
                data[i].setAgentName(userName);
            }
            battleField.dataCandidatesToSendUBoat(data);

        response.setStatus(HttpServletResponse.SC_OK);

        /*

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

         */

        }
    }
}
