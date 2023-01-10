package servletAgents;

import com.google.gson.Gson;
import dataWeb.DataContestAndTeamForAgent;
import dataWeb.DataTeamsWeb;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.Ally;
import logic.BattleField;
import logic.DmManagerNew;
import users.AlliesManager;
import users.BattleFieldManager;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import static constants.ConstantsWeb.USERNAME;

@WebServlet(name = "AgentsContestAndTeamDataServlet", urlPatterns = {"/agentsContestAndTeamData"})
public class AgentsContestAndTeamDataServlet extends HttpServlet {



    Object object = new Object();
    private  HashMap<String, BattleField> allBattleFieldsMap;
    private BattleField battleField;
    private boolean isBattleInAlly;
    private String team;
    private int agentsAmount;
    private int taskSize;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //returning JSON objects, not HTML
        response.setContentType("application/json");
        synchronized (object) {
            try (PrintWriter out = response.getWriter()) {
                Gson gson = new Gson();
                isBattleInAlly=false;
                BattleFieldManager battleFieldManager = ServletUtils.getAllBattleFieldsManager(getServletContext());
                //Set<String> usersList = userManager.getUsers();
               // HashMap<String, BattleField> allBattleFieldsMap = battleFieldManager.getAllBattleFields();
                AlliesManager alliesManager = ServletUtils.getAllAlliesManager(getServletContext());
                HashMap<String, Ally>allyHashMap=alliesManager.getAllAllies();
                allyHashMap.forEach((s, ally) -> {
                    if(ally.getAgentsForCurrentCompetition().contains(request.getParameter(USERNAME))){
                        if(!ally.getAllyBattle().equals("")){
                            allBattleFieldsMap = battleFieldManager.getAllBattleFields();
                            battleField = allBattleFieldsMap.get(ally.getAllyBattle());
                            isBattleInAlly=true;
                        }
                        team=ally.getName();
                        agentsAmount=ally.getAgentsForCurrentCompetition().size();
                        taskSize=ally.getSizeOfTask();
                    }
                });

                DataContestAndTeamForAgent dataContestAndTeamForAgent=new DataContestAndTeamForAgent();
                if(isBattleInAlly){
                    dataContestAndTeamForAgent.setBattleFieldName(battleField.getBattleField());
                    dataContestAndTeamForAgent.setuBoatName(battleField.getUBoatName());
                    dataContestAndTeamForAgent.setStatus(battleField.isStatus());
                    dataContestAndTeamForAgent.setLevel(battleField.getLevel());
                    dataContestAndTeamForAgent.setAlliesMax(battleField.getAllies());
                    dataContestAndTeamForAgent.setAlliesLogged(battleField.getAlliesLogged());
                    dataContestAndTeamForAgent.setTeamName(team);
                    dataContestAndTeamForAgent.setNumberOfAgents(agentsAmount);
                    dataContestAndTeamForAgent.setSizeOfTask(taskSize);
                }else{

                    dataContestAndTeamForAgent.setBattleFieldName("");
                    dataContestAndTeamForAgent.setuBoatName("");
                    dataContestAndTeamForAgent.setStatus(false);
                    dataContestAndTeamForAgent.setLevel("");
                    dataContestAndTeamForAgent.setAlliesMax(0);
                    dataContestAndTeamForAgent.setAlliesLogged(0);
                    dataContestAndTeamForAgent.setTeamName(team);
                    dataContestAndTeamForAgent.setNumberOfAgents(agentsAmount);
                    dataContestAndTeamForAgent.setSizeOfTask(taskSize);

                }
                ArrayList<DataContestAndTeamForAgent>dataContestAndTeamForAgentArrayList=new ArrayList<>();
                dataContestAndTeamForAgentArrayList.add(dataContestAndTeamForAgent);
                String json = gson.toJson(dataContestAndTeamForAgentArrayList);
                out.println(json);
                out.flush();
            }
        }
    }

}
