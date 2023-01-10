package servletAgents;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dataWeb.DataToAgent;
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

@WebServlet(name = "XmlDataToAgentServlet", urlPatterns = {"/xmlDataToAgent"})
public class XmlDataToAgentServlet extends HttpServlet {
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
                BattleField battleField=allBattleFieldsMap.get(request.getParameter("battle"));
                String xml=battleField.getMyXml();
                String encrypted=battleField.getEncryptedStringForContest();
                DataToAgent dataToAgent=new DataToAgent();
                dataToAgent.setXml(xml);
                dataToAgent.setEncryptedString(encrypted);
                dataToAgent.setTotalTasks(ServletUtils.getAllAlliesManager(getServletContext()).getAllAllies().get(request.getParameter("team")).getTotalTasks());
                //כל זה היה פה
                //JsonParser jsonParser=new JsonParser();
                //String jsonResponse=gson.toJson(dataToAgent);
                //JsonElement jsonElement=jsonParser.parse(jsonResponse);
                //JsonObject jsonObject =jsonElement.getAsJsonObject();


                 String jsonString=gson.toJson(dataToAgent);
                response.setStatus(HttpServletResponse.SC_OK);
                out.println(jsonString);
                out.flush();
            }
        }
    }
}
