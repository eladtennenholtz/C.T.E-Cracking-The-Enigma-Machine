package servletAllies;

import com.google.gson.Gson;
import data.DataCandidatesToAllies;
import data.DataCandidatesToUBoat;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.Ally;
import logic.BattleField;
import users.AlliesManager;
import users.BattleFieldManager;
import users.UserUBoatManager;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import static constants.ConstantsWeb.USERNAME;
@WebServlet(name = "CandidatesForAlliesServlet", urlPatterns = {"/candidatesForAllies"})
public class CandidatesForAlliesServlet extends HttpServlet {

    Object object=new Object();
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
                String nameOfUser=request.getParameter(USERNAME);
                //String battle=request.getParameter("battle");
               // BattleField battleField=allBattleFieldsMap.get(battle);
                AlliesManager alliesManager = ServletUtils.getAllAlliesManager(getServletContext());
                Ally ally=alliesManager.getAllAllies().get(nameOfUser);
                ArrayList<DataCandidatesToAllies> dataCandidatesToAllies=ally.getCandidatesToAllies();
                //int candidates=battleField.getCandidatePositionInArray();
                //ArrayList<DataCandidatesToUBoat>toSend=new ArrayList<>();
                DataCandidatesToAllies[]toSend=new DataCandidatesToAllies[dataCandidatesToAllies.size()];
                int j=dataCandidatesToAllies.size();
                for (int i=ally.getPositionInArray();i<dataCandidatesToAllies.size();i++){
                  //  System.out.println("candidate "+candidates+"  "+dataCandidatesToAllies.get(candidates));
                    //  toSend.add(dataCandidatesToUBoats.get(candidates));
                    toSend[ally.getPositionInArray()]=dataCandidatesToAllies.get(ally.getPositionInArray());
                    j--;
                    ally.setPositionInArray(i+1);
                }


                if(j==dataCandidatesToAllies.size()){
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                }else {
                    response.setStatus(HttpServletResponse.SC_OK);
                    String json = gson.toJson(toSend);
                    out.println(json);
                    out.flush();
                }
            }
        }
    }
}
