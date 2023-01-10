package servletAgents;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.BattleField;
import users.BattleFieldManager;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import static constants.ConstantsWeb.USERNAME;

@WebServlet(name = "ContestFinishedAgentsServlet", urlPatterns = {"/contestFinishedAgent"})
public class ContestFinishedAgentsServlet extends HttpServlet {

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
                String battle=request.getParameter("battle");
                String winner="";
                if(allBattleFieldsMap.get(battle).isContestOver()){
                    winner=allBattleFieldsMap.get(battle).getWinnerOfContest();
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
