package servletAllies;

import com.google.gson.Gson;
import dataWeb.DataContestsWeb;
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

@WebServlet(name = "TeamsAgentsDataServlet", urlPatterns = {"/teamsAgentsData"})
public class TeamsAgentsDataServlet extends HttpServlet {

    Object object=new Object();
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //returning JSON objects, not HTML
        response.setContentType("application/json");
        synchronized (object) {
            try (PrintWriter out = response.getWriter()) {
                Gson gson = new Gson();
                AlliesManager alliesManager = ServletUtils.getAllAlliesManager(getServletContext());
                Ally ally=alliesManager.getAllAllies().get(request.getParameter(USERNAME));
                ArrayList<Agent>toSend=new ArrayList<>();
                ally.getAgentsMap().forEach((s, agent) ->toSend.add(agent) );
                String json = gson.toJson(toSend);
                out.println(json);
                out.flush();
            }
        }
    }
}
