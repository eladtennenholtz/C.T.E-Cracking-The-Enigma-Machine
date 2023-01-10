package servletAllies;

import com.google.gson.Gson;
import dataWeb.DataContestsWeb;
import dataWeb.DataTeamsWeb;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.Ally;
import logic.BattleField;
import logic.DmManagerNew;
import users.BattleFieldManager;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

@WebServlet(name = "AllTeamsDataForContestServlet", urlPatterns = {"/allTeamsDataForContest"})
public class AllTeamsDataForContestServlet extends HttpServlet {


    Object object = new Object();

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
                String result1=request.getParameter("battle");
                BattleField battleField = allBattleFieldsMap.get(request.getParameter("battle"));
                ArrayList<DataTeamsWeb> dataTeamsWebsArrayList = new ArrayList<>();
                HashMap<Ally, DmManagerNew> allAllies = battleField.getAlliesInBattleMap();

                allAllies.forEach(((ally, dmManagerNew) -> {
                    DataTeamsWeb dataTeamsWeb = new DataTeamsWeb();
                    dataTeamsWeb.setTeamName(ally.getName());
                    dataTeamsWeb.setNumberOfAgents(ally.getAgentsForCurrentCompetition().size());
                    dataTeamsWeb.setSizeOfTask(ally.getSizeOfTask());
                    dataTeamsWebsArrayList.add(dataTeamsWeb);
                }));
                String json = gson.toJson(dataTeamsWebsArrayList);
                out.println(json);
                out.flush();
            }
        }
    }

}

