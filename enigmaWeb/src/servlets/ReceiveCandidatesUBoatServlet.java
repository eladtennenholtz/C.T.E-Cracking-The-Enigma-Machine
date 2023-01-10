package servlets;

import com.google.gson.Gson;
import data.DataCandidatesToUBoat;
import dataWeb.DataContestsWeb;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.BattleField;
import users.BattleFieldManager;
import users.UserUBoatManager;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import static constants.ConstantsWeb.USERNAME;

@WebServlet(name = "ReceiveCandidatesUBoatServlet", urlPatterns = {"/receiveCandidatesUBoatServlet"})
public class ReceiveCandidatesUBoatServlet extends HttpServlet {


    Object object=new Object();
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //returning JSON objects, not HTML
        response.setContentType("application/json");
        synchronized (object) {
            try (PrintWriter out = response.getWriter()) {
                Gson gson = new Gson();
                UserUBoatManager userUBoatManager = ServletUtils.getUserUBoatManager(getServletContext());
                String usernameFromSession = SessionUtils.getUsername(request);
                String usernameFromParameter = request.getParameter(USERNAME);
                BattleField battleField = (BattleField) ServletUtils.getBattleField(usernameFromParameter, getServletContext());
                ArrayList<DataCandidatesToUBoat>dataCandidatesToUBoats=battleField.getCandidatesToUBoats();
                int candidates=battleField.getCandidatePositionInArray();
                //ArrayList<DataCandidatesToUBoat>toSend=new ArrayList<>();
                DataCandidatesToUBoat[]toSend=new DataCandidatesToUBoat[dataCandidatesToUBoats.size()];
                int j=dataCandidatesToUBoats.size();
                for (int i=candidates;i<dataCandidatesToUBoats.size();i++){
                    System.out.println("candidate "+candidates+"  "+dataCandidatesToUBoats.get(candidates));
                  //  toSend.add(dataCandidatesToUBoats.get(candidates));
                    toSend[candidates]=dataCandidatesToUBoats.get(candidates);
                    candidates++;
                    j--;
                }
                if(j==dataCandidatesToUBoats.size()){
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                }else {
                    response.setStatus(HttpServletResponse.SC_OK);
                    battleField.setCandidatePositionInArray(candidates);
                    String json = gson.toJson(toSend);
                    out.println(json);
                    out.flush();
                }
            }
        }
    }


}
