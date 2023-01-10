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

@WebServlet(name = "ContestFinishedAlliesServlet", urlPatterns = {"/contestFinishedAllies"})
public class ContestFinishedAlliesServlet extends HttpServlet {

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

                AlliesManager alliesManager = ServletUtils.getAllAlliesManager(getServletContext());
                Ally ally=alliesManager.getAllAllies().get(nameOfUser);
                String battle=request.getParameter("battle");

                ally.contestRealyStarted(false);
                String winner="";
                if(allBattleFieldsMap.get(battle).isContestOver()){
                    winner=allBattleFieldsMap.get(battle).getWinnerOfContest();
                    //את זה הוספתי
                    allBattleFieldsMap.forEach((s, battleField) -> {
                        if(s.equals(battle)){
                                battleField.removeDmFromAlly(ally);
                                battleField.setStatus(false);
                        }
                    });
                    //עד כאן

                }
                String[]results=new String[2];
                results[0]= String.valueOf(allBattleFieldsMap.get(battle).isContestOver());
                results[1]=winner;
                response.setStatus(HttpServletResponse.SC_OK);
                String json = gson.toJson(results);
                out.println(json);
                out.flush();
            }
        }
    }

}

