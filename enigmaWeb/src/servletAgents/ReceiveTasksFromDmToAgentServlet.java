package servletAgents;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.Ally;
import logic.BattleField;
import logic.DmManagerNew;
import logic.Task;
import users.AlliesManager;
import users.BattleFieldManager;
import users.UserUBoatManager;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

@WebServlet(name = "ReceiveTasksFromDmToAgentServlet", urlPatterns = {"/tasksFromDmToAgent"})
public class ReceiveTasksFromDmToAgentServlet extends HttpServlet {

    Object object = new Object();
    Object lock=new Object();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //returning JSON objects, not HTML
        response.setContentType("application/json");
        //synchronized (object) {
            try (PrintWriter out = response.getWriter()) {
                Gson gson = new Gson();
                BattleFieldManager battleFieldManager = ServletUtils.getAllBattleFieldsManager(getServletContext());
                HashMap<String, BattleField> allBattleFieldsMap = battleFieldManager.getAllBattleFields();
                if (allBattleFieldsMap.size() == 0) {
                  response.setStatus(HttpServletResponse.SC_CONFLICT);
                return;
                }
                BattleField battleField=allBattleFieldsMap.get(request.getParameter("battle"));
                HashMap<Ally, DmManagerNew> allyDmManagerNewHashMap=battleField.getAlliesInBattleMap();
                Ally ally= ServletUtils.getAllAlliesManager(getServletContext()).getAllAllies().get(request.getParameter("team"));
                DmManagerNew dmManagerNew=allyDmManagerNewHashMap.get(ally);
                BlockingQueue<Task>taskBlockingQueue= dmManagerNew.getBlockingQueue();
                ArrayList<Task>allTheTasksToSendTheAgent=new ArrayList<>();
                synchronized (object) {
                    for (int i = 0; i < Integer.parseInt(request.getParameter("tasksToPull")); i++) {
                        //if(dmManagerNew.getCounterForQueue()==battleField.getAllConfigurations()){
                        //}else {
                        //synchronized (object) {
                            Task task = taskBlockingQueue.poll();
                            if (task != null) {
                                allTheTasksToSendTheAgent.add(task);
                            }
                        //}
                    }
                }

                //allBattleFieldsMap.forEach((s, battleField) -> {
                // for (int i=0;i<battleField.getAlliesInBattle().size();i++){
                //   allTeams.add(battleField.getAlliesInBattle().get(i));
                //}
                //});


                String json = gson.toJson(allTheTasksToSendTheAgent);
                if(allTheTasksToSendTheAgent.isEmpty()){
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                }else {
                    response.setStatus(HttpServletResponse.SC_OK);
                }
                //out.println(json);
                if(!allTheTasksToSendTheAgent.isEmpty()) {
                    out.println(json);
                    //out.println(allTheTasksToSendTheAgent);
                    out.flush();
                }
            }
        //}
    }
}
