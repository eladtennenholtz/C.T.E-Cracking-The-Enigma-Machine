package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.BattleField;
import logic.Engine;
import users.BattleFieldManager;
import users.UserUBoatManager;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static constants.ConstantsWeb.USERNAME;
//logoutUBoat
@WebServlet(name = "LogOutUBoatServlet", urlPatterns = {"/logoutUBoat"})
public class LogOutUBoatServlet extends HttpServlet {

    Object lock=new Object();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/plain;charset=UTF-8");

        synchronized (lock) {
            UserUBoatManager userUBoatManager = ServletUtils.getUserUBoatManager(getServletContext());
            String usernameFromSession = SessionUtils.getUsername(request);
            String usernameFromParameter = request.getParameter(USERNAME);
            BattleField battleField = (BattleField) ServletUtils.getBattleField(usernameFromParameter, getServletContext());
            battleField.getAlliesInBattleMap().forEach((ally, dmManagerNew) -> {

            });
            battleField.removeAll();
            String battle=battleField.getBattleField();
            userUBoatManager.removeUser(usernameFromParameter);
            userUBoatManager.removeBattle(battle,usernameFromParameter);
            //UserUBoatManager.getAllBattleFieldsMap().remove(battle);
            //UserUBoatManager.getAllBattleFields().remove(battle);
            BattleFieldManager battleFieldManager = ServletUtils.getAllBattleFieldsManager(getServletContext());
            battleFieldManager.removeBattle(battle);

            ServletUtils.eraseBattleField(usernameFromParameter,getServletContext());
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }





}
