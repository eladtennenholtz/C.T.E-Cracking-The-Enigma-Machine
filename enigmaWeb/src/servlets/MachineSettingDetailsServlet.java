package servlets;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.BattleField;
import logic.CurrentMachineDetails;
import logic.Engine;
import rotor.Rotor;
import users.UserUBoatManager;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import static constants.ConstantsWeb.USERNAME;

@WebServlet(name = "MachineSettingDetailsServlet", urlPatterns = {"/machineSettingDetails"})
public class MachineSettingDetailsServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/plain;charset=UTF-8");
        UserUBoatManager userUBoatManager = ServletUtils.getUserUBoatManager(getServletContext());
        String usernameFromSession = SessionUtils.getUsername(request);
        String usernameFromParameter = request.getParameter(USERNAME);
        BattleField battleField= (BattleField) ServletUtils.getBattleField(usernameFromParameter,getServletContext());
        Engine engine=battleField.getEngine();
        CurrentMachineDetails machineDetails = engine.getDetails();

        Gson gson=new Gson();
        String jsonResponse=gson.toJson(machineDetails);
        JsonParser jsonParser=new JsonParser();
        JsonElement jsonElement=jsonParser.parse(jsonResponse);
        JsonObject jsonObject =jsonElement.getAsJsonObject();
        try (PrintWriter out = response.getWriter()) {
            out.print(jsonResponse);
            out.flush();
        }

    }




}
