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

@WebServlet(name = "ReadyForContestAllyServlet", urlPatterns = {"/readyForContestAlly"})
public class ReadyForContestAllyServlet extends HttpServlet {

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
            String nameOfUser=request.getParameter(USERNAME);
            String battle=request.getParameter("battle");
            AlliesManager alliesManager = ServletUtils.getAllAlliesManager(getServletContext());
            Ally ally=alliesManager.getAllAllies().get(nameOfUser);
            if(allBattleFieldsMap.get(battle).calculateAllConfig()<Integer.parseInt(request.getParameter("sizeOfTask"))){
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
            if(ally.getAgents().size()>0) {
                ally.setReadyToStartCompetition(true);
                ally.setSizeOfTask(Integer.parseInt(request.getParameter("sizeOfTask")));
                allBattleFieldsMap.get(battle).setSizeOfTaskAlly(Integer.parseInt(request.getParameter("sizeOfTask")));
                allBattleFieldsMap.get(battle).setAlliesReadyToStartContest(true);
                allBattleFieldsMap.get(battle).setNumberAlliesReadyToStartContest();
                if(allBattleFieldsMap.get(battle).getNumberAlliesReadyToStartContest()==allBattleFieldsMap.get(battle).getAllies()&&allBattleFieldsMap.get(battle).isuBoatReadyToStartContest()&&allBattleFieldsMap.get(battle).getAllies()==allBattleFieldsMap.get(battle).getAlliesLogged()){

                    //ally.contestRealyStarted(true);
                    allBattleFieldsMap.get(battle).setStatus(true);
                    ally.setFinishedPressed(false);
                    allBattleFieldsMap.get(battle).startContest();
                }
                response.setStatus(HttpServletResponse.SC_OK);
            }else{
                response.setStatus(HttpServletResponse.SC_CONFLICT);
            }

        }
    }


}
