package servlets;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.Agent;
import logic.Ally;
import users.AlliesManager;
import users.BattleFieldManager;
import users.UserUBoatManager;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.util.ArrayList;

import static constants.ConstantsWeb.USERNAME;

public class LoginServletUBoat extends HttpServlet {


    Object key=new Object();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");
        UserUBoatManager userUBoatManager = ServletUtils.getUserUBoatManager(getServletContext());
        String usernameFromSession = SessionUtils.getUsername(request);
        if(request.getParameter("type").equals("uboat")) {
            BattleFieldManager battleFieldManager = ServletUtils.getAllBattleFieldsManager(getServletContext());
        } else if (request.getParameter("type").equals("allies")) {
                AlliesManager alliesManager = ServletUtils.getAllAlliesManager(getServletContext());
                if(usernameFromSession==null){
                    if(request.getParameter(USERNAME)!=null &&!request.getParameter(USERNAME).isEmpty()){
                        synchronized (this){
                            if(!userUBoatManager.isUserExists(request.getParameter(USERNAME))){
                                alliesManager.addAlly(request.getParameter(USERNAME));
                            }
                        }
                    }
                }

        }else if(request.getParameter("type").equals("agents")){
            if(usernameFromSession==null){
                if(request.getParameter(USERNAME)!=null &&!request.getParameter(USERNAME).isEmpty()){
                    synchronized (this){
                        if(!userUBoatManager.isUserExists(request.getParameter(USERNAME))){
                            AlliesManager alliesManager=ServletUtils.getAllAlliesManager(getServletContext());
                            Ally ally=alliesManager.getAllAllies().get(request.getParameter("team"));
                            Agent agent=new Agent(request.getParameter(USERNAME),Integer.valueOf(request.getParameter("numberOfThreads")),Integer.valueOf(request.getParameter("sizeOfTasks")),ally.getName());
                            ally.addAgent(request.getParameter(USERNAME),agent);
                        }
                    }
                }
            }

        }

        if (usernameFromSession == null) { //user is not logged in yet


            String usernameFromParameter = request.getParameter(USERNAME);
            if (usernameFromParameter == null || usernameFromParameter.isEmpty()) {
                //no username in session and no username in parameter - not standard situation. it's a conflict

                // stands for conflict in server state
                response.setStatus(HttpServletResponse.SC_CONFLICT);
            } else {
                //normalize the username value
                usernameFromParameter = usernameFromParameter.trim();

                synchronized (this) {
                    if (userUBoatManager.isUserExists(usernameFromParameter)) {
                        String errorMessage = "Username " + usernameFromParameter + " already exists.\n Please enter a different username.";

                        // stands for unauthorized as there is already such user with this name
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getOutputStream().print(errorMessage);
                    }
                    else {
                        //add the new user to the users list
                        userUBoatManager.addUser(usernameFromParameter);
                        //ServletUtils.setBattleField(usernameFromParameter,getServletContext());
                        //set the username in a session so it will be available on each request
                        //the true parameter means that if a session object does not exists yet
                        //create a new one
                        request.getSession(true).setAttribute(USERNAME, usernameFromParameter);

                        //redirect the request to the chat room - in order to actually change the URL
                        System.out.println("On login, request URI is: " + request.getRequestURI());
                        response.setStatus(HttpServletResponse.SC_OK);
                    }
                }
            }
        } else {
            //user is already logged in
            response.setStatus(HttpServletResponse.SC_OK);

        }
    }

}
