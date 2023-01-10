package servletAllies;

import com.google.gson.Gson;
import dataWeb.DataContestsWeb;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.BattleField;
import users.BattleFieldManager;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

@WebServlet(name = "ContestsDataServlet", urlPatterns = {"/contestsData"})
public class ContestsDataServlet extends HttpServlet {
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
                ArrayList<DataContestsWeb> dataContestsWebArrayList = new ArrayList<>();
                allBattleFieldsMap.forEach((s, battleField) -> {
                    DataContestsWeb dataContestsWeb = new DataContestsWeb();
                    dataContestsWeb.setBattleFieldName(s);
                    dataContestsWeb.setuBoatName(battleField.getUBoatName());
                    dataContestsWeb.setStatus(battleField.isStatus());
                    dataContestsWeb.setLevel(battleField.getLevel());
                    dataContestsWeb.setAlliesMax(battleField.getAllies());
                    dataContestsWeb.setAlliesLogged(battleField.getAlliesLogged());
                    dataContestsWebArrayList.add(dataContestsWeb);
                });
                String json = gson.toJson(dataContestsWebArrayList);
                out.println(json);
                out.flush();
            }
        }
    }
}
