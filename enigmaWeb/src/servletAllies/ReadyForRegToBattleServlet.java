package servletAllies;

import com.google.gson.Gson;
import dataWeb.DataContestsWeb;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

@WebServlet(name = "ReadyForRegToBattleServlet", urlPatterns = {"/readyForRegContest"})
public class ReadyForRegToBattleServlet extends HttpServlet {

        Object object = new Object();

        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
            //returning JSON objects, not HTML
            response.setContentType("application/json");
            synchronized (object) {
                Gson gson=new Gson();
                    BattleFieldManager battleFieldManager = ServletUtils.getAllBattleFieldsManager(getServletContext());
                    HashMap<String, BattleField> allBattleFieldsMap = battleFieldManager.getAllBattleFields();
                    if (allBattleFieldsMap.size() == 0) {
                        response.setStatus(HttpServletResponse.SC_CONFLICT);
                        return;
                    }
                    String nameOfUser=request.getParameter(USERNAME);
                    String battle=request.getParameter("battle");
                    AlliesManager alliesManager = ServletUtils.getAllAlliesManager(getServletContext());
                    Ally ally=alliesManager.getAllAllies().get(nameOfUser);
                    boolean res;
                    boolean extra=false;
                    if(!allBattleFieldsMap.get(battle).isContestOver()){
                        res = allBattleFieldsMap.get(battle).addAllieToBattle(ally);

                    }else{
                        extra=true;
                        res=false;
                    }
                    if(res){
                        ally.setAllyBattle(battle);
                     response.setStatus(HttpServletResponse.SC_OK);
                    }else{
                        if(extra==true){
                            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                        }else {
                            response.setStatus(HttpServletResponse.SC_CONFLICT);
                        }
                    }
            }
        }
    }

