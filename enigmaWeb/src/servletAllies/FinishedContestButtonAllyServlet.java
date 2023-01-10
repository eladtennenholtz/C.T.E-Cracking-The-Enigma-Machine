package servletAllies;

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
import java.util.HashMap;

import static constants.ConstantsWeb.USERNAME;
@WebServlet(name = "FinishedContestButtonAllyServlet", urlPatterns = {"/finishedContestButtonPressedPage"})
public class FinishedContestButtonAllyServlet extends HttpServlet {

    Object object = new Object();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //returning JSON objects, not HTML
        response.setContentType("application/json");
        synchronized (object) {

            String nameOfUser = request.getParameter(USERNAME);
            AlliesManager alliesManager = ServletUtils.getAllAlliesManager(getServletContext());
            Ally ally = alliesManager.getAllAllies().get(nameOfUser);
            ally.setFinishedPressed(true);
            ally.setPositionInArray(0);
            ally.setTotalTasks(0);
            ally.setReadyToStartCompetition(false);
            ally.setSizeOfTask(0);
            ally.setAllyBattle("");
            //ally.contestRealyStarted(false);


            ally.getCandidatesToAllies().clear();
            ally.getAgentsMapForCurrentCompetition().forEach((s, agent) -> {
                agent.setTaskRemain(0);
                agent.setTaskPreformed(0);
                agent.setCandidates(0);
            });
            BattleFieldManager battleFieldManager = ServletUtils.getAllBattleFieldsManager(getServletContext());
            HashMap<String, BattleField> allBattleFieldsMap = battleFieldManager.getAllBattleFields();
            if (allBattleFieldsMap.size() == 0) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                return;
            }
           String battle=request.getParameter("battle");
            if(allBattleFieldsMap.containsKey(battle)){
                allBattleFieldsMap.get(battle).getAlliesInBattleMap().forEach((ally1, dmManagerNew) -> {
                    if(ally1.getName().equals(nameOfUser)){
                        allBattleFieldsMap.get(battle).setStatus(false);
                        allBattleFieldsMap.get(battle).removeAlly(ally1);

                    }
                });
            }
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }

}
