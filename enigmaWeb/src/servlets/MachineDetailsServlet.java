package servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import data.DataFromEngineToUiShowingTheMachine;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import logic.BattleField;
import logic.Engine;
import users.UserUBoatManager;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static constants.ConstantsWeb.CURRENT_USER;
import static constants.ConstantsWeb.USERNAME;

@WebServlet(name = "MachineDetailsServlet", urlPatterns = {"/machineDetails"})
public class MachineDetailsServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/plain;charset=UTF-8");
        UserUBoatManager userUBoatManager = ServletUtils.getUserUBoatManager(getServletContext());
        String usernameFromSession = SessionUtils.getUsername(request);
        String usernameFromParameter = request.getParameter(USERNAME);
        BattleField battleField= (BattleField) ServletUtils.getBattleField(usernameFromParameter,getServletContext());
        Engine engine=battleField.getEngine();
        DataFromEngineToUiShowingTheMachine result= engine.displayingTheMachineSetting();

        Gson gson = new Gson();
       String jsonResponse=gson.toJson(result);
        try (PrintWriter out = response.getWriter()) {
            out.print(jsonResponse);
            out.flush();
        }

    }
}
