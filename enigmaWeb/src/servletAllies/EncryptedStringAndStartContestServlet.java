package servletAllies;

import com.google.gson.Gson;
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

@WebServlet(name = "EncryptedStringAndStartContestServlet", urlPatterns = {"/encryptedStringAndStartContestData"})
public class EncryptedStringAndStartContestServlet extends HttpServlet {

    Object object = new Object();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //returning JSON objects, not HTML
        response.setContentType("application/json");
        synchronized (object) {
            try (PrintWriter out = response.getWriter()) {
                Gson gson = new Gson();
                BattleFieldManager battleFieldManager = ServletUtils.getAllBattleFieldsManager(getServletContext());
                //Set<String> usersList = userManager.getUsers();
                HashMap<String, BattleField> allBattleFieldsMap = battleFieldManager.getAllBattleFields();
                if (allBattleFieldsMap.size() == 0) {
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    return;
                }
                String result1=request.getParameter("battle");
                BattleField battleField = allBattleFieldsMap.get(request.getParameter("battle"));
                ArrayList<String> dataOfEncryptedAndIfStart = new ArrayList<>();
                dataOfEncryptedAndIfStart.add(battleField.getEncryptedStringForContest());
                dataOfEncryptedAndIfStart.add(String.valueOf(battleField.isContestStarted()));
                String json = gson.toJson(dataOfEncryptedAndIfStart);
                out.println(json);
                out.flush();
            }
        }
    }

}
