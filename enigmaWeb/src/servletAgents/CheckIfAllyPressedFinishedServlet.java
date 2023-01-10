package servletAgents;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.Agent;
import logic.Ally;
import users.AlliesManager;
import utils.ServletUtils;

import java.io.IOException;

@WebServlet(name = "CheckIfAllyPressedFinishedServlet", urlPatterns = {"/checkIfAllyPressedFinished"})
public class CheckIfAllyPressedFinishedServlet extends HttpServlet {
    Object object=new Object();
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //returning JSON objects, not HTML
        response.setContentType("application/json");
        synchronized (object) {

            String team = request.getParameter("team");

            AlliesManager alliesManager = ServletUtils.getAllAlliesManager(getServletContext());
            Ally ally = alliesManager.getAllAllies().get(team);
            if(ally.isFinishedPressed()){
                response.setStatus(HttpServletResponse.SC_OK);
            }else{
                response.setStatus(HttpServletResponse.SC_CONFLICT);
            }
        }
    }

}
