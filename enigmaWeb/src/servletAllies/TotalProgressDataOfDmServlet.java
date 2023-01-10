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

@WebServlet(name = "TotalProgressDataOfDmServlet", urlPatterns = {"/totalProgressOfDm"})
public class TotalProgressDataOfDmServlet extends HttpServlet {
    private int taskPushedToQueue;
    private int totalTasks;
    Object object=new Object();
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //returning JSON objects, not HTML
        response.setContentType("application/json");
        synchronized (object) {
            try (PrintWriter out = response.getWriter()) {
                Gson gson = new Gson();
                BattleFieldManager battleFieldManager = ServletUtils.getAllBattleFieldsManager(getServletContext());
                //Set<String> usersList = userManager.getUsers();
                HashMap<String, BattleField> allBattleFieldsMap = battleFieldManager.getAllBattleFields();
                if(allBattleFieldsMap.size()==0){
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    return;
                }
                String result1=request.getParameter("battle");
                String teamName=request.getParameter("username");
                BattleField battleField = allBattleFieldsMap.get(request.getParameter("battle"));


                HashMap<Ally, DmManagerNew> allAllies = battleField.getAlliesInBattleMap();
                allAllies.forEach(((ally, dmManagerNew) -> {
                    if(ally.getName().equals(teamName)){

                        taskPushedToQueue=dmManagerNew.getTaskedPushedToQueue();
                        totalTasks=ally.getTotalTasks();
                    }

                }));

                int[]toSend=new int[2];
                toSend[0]=taskPushedToQueue;
                toSend[1]=totalTasks;
                String json = gson.toJson(toSend);
                response.setStatus(HttpServletResponse.SC_OK);
                out.println(json);
                out.flush();
            }
        }
    }




}
