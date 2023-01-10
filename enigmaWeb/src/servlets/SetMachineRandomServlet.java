package servlets;

import data.DataFromEngineToUi;
import data.DataFromEngineToUiShowingTheMachine;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.BattleField;
import logic.Engine;
import users.UserUBoatManager;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;

import static constants.ConstantsWeb.USERNAME;

@WebServlet(name = "SetMachineRandomServlet", urlPatterns = {"/setMachineRandom"})
public class SetMachineRandomServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/plain;charset=UTF-8");
        UserUBoatManager userUBoatManager = ServletUtils.getUserUBoatManager(getServletContext());
        String usernameFromSession = SessionUtils.getUsername(request);
        String usernameFromParameter = request.getParameter(USERNAME);
        BattleField battleField= (BattleField) ServletUtils.getBattleField(usernameFromParameter,getServletContext());
        Engine engine=battleField.getEngine();
        DataFromEngineToUi resultedData = engine.selectingAnInitialCodeConfigurationAutomatically();
        StringBuilder stringBuilder=new StringBuilder();
        for(int i=engine.getDetails().getMachine().usedRotors.size()-1;i>=0;i--){
            stringBuilder.append(engine.getDetails().getMachine().usedRotors.get(i).forwardEntry.get(0));
        }
        String jsonResponse=resultedData.getResult()+"\t"+stringBuilder.toString();
        try (PrintWriter out = response.getWriter()) {
            out.print(jsonResponse);
            out.flush();
        }
    }



}
