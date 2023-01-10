package servletAgents;

import com.google.gson.Gson;
import data.DataCandidatesToUBoat;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.Agent;
import logic.Ally;
import logic.BattleField;
import users.AlliesManager;
import users.UserUBoatManager;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import static constants.ConstantsWeb.USERNAME;

@WebServlet(name = "SendFromAgentToServerProgressServlet", urlPatterns = {"/sendAgentsProgressToServer"})
public class SendFromAgentToServerProgressServlet extends HttpServlet {

    Object object=new Object();
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //returning JSON objects, not HTML
        response.setContentType("application/json");
        synchronized (object) {
                Gson gson = new Gson();
                String userName=request.getParameter("username");
                String team=request.getParameter("team");
                 String candidates=request.getParameter("candidates");
                String taskedPreformed=request.getParameter("taskedPreformed");
                String taskRemain=request.getParameter("taskRemain");
                AlliesManager alliesManager = ServletUtils.getAllAlliesManager(getServletContext());
                Ally ally=alliesManager.getAllAllies().get(team);
                Agent agent=ally.getAgentsMapForCurrentCompetition().get(userName);
                if(candidates.equals("")){
                    agent.setCandidates(0);
                }else {
                    agent.setCandidates(Integer.parseInt(candidates));
                }
                if(taskedPreformed.equals("")){
                    agent.setTaskPreformed(0);
                }else {
                    agent.setTaskPreformed(Integer.parseInt(taskedPreformed));
                }
                if(taskRemain.equals("")){
                    agent.setTaskRemain(0);
                }else {
                    agent.setTaskRemain(Integer.parseInt(taskRemain));
                }
                response.setStatus(HttpServletResponse.SC_OK);
        }
    }

}
