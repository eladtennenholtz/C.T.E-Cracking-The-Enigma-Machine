package servletAllies;

import com.google.gson.Gson;
import data.DataAgentsProgressToAllies;
import data.DataCandidatesToAllies;
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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import static constants.ConstantsWeb.USERNAME;
@WebServlet(name = "TeamsAgentsProgressDataServlet", urlPatterns = {"/progressOfAgents"})
public class TeamsAgentsProgressDataServlet extends HttpServlet {
    private int index;

    Object object=new Object();
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //returning JSON objects, not HTML
        response.setContentType("application/json");
        synchronized (object) {
            try (PrintWriter out = response.getWriter()) {
                Gson gson = new Gson();
                BattleFieldManager battleFieldManager = ServletUtils.getAllBattleFieldsManager(getServletContext());
                HashMap<String, BattleField> allBattleFieldsMap = battleFieldManager.getAllBattleFields();
                if (allBattleFieldsMap.size() == 0) {
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    return;
                }
                String nameOfUser=request.getParameter(USERNAME);
                //String battle=request.getParameter("battle");
                // BattleField battleField=allBattleFieldsMap.get(battle);
                AlliesManager alliesManager = ServletUtils.getAllAlliesManager(getServletContext());
                Ally ally=alliesManager.getAllAllies().get(nameOfUser);
                ArrayList<DataCandidatesToAllies> dataCandidatesToAllies=ally.getCandidatesToAllies();
                HashMap<String, Agent>agents=ally.getAgentsMapForCurrentCompetition();
                DataAgentsProgressToAllies[]arrayOfAgentsProgress=new DataAgentsProgressToAllies[agents.size()];
                index=0;
                agents.forEach((s, agent) -> {
                    DataAgentsProgressToAllies data=new DataAgentsProgressToAllies(agent.getNameAgent(),agent.getTaskPreformed(),agent.getTaskRemain(),agent.getCandidates());
                    arrayOfAgentsProgress[index]=data;
                    index++;
                });



                    response.setStatus(HttpServletResponse.SC_OK);
                    String json = gson.toJson(arrayOfAgentsProgress);
                    out.println(json);
                    out.flush();

            }
        }
    }

}
