package servlets;

import data.DataFromEngineToUiEncryptedString;
import data.DataFromEngineToUiShowingTheMachine;
import data.DataFromUiToEngineStringToEncrypt;
import exceptionFromUserInput.InputStringToEncryptException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import logic.BattleField;
import logic.Engine;
import sun.misc.IOUtils;
import users.UserUBoatManager;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

import static constants.ConstantsWeb.USERNAME;

@WebServlet(name = "EncryptStringServlet", urlPatterns = {"/encrypt"})
public class EncryptStringServlet extends HttpServlet {
    Object lock=new Object();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/plain;charset=UTF-8");
        synchronized (lock) {
            UserUBoatManager userUBoatManager = ServletUtils.getUserUBoatManager(getServletContext());
            String usernameFromSession = SessionUtils.getUsername(request);
            String usernameFromParameter = request.getParameter(USERNAME);
            String textToEncrypt = request.getParameter("encrypt");
            BattleField battleField = (BattleField) ServletUtils.getBattleField(usernameFromParameter, getServletContext());
            Engine engine = battleField.getEngine();
            DataFromUiToEngineStringToEncrypt dataEncrypt = new DataFromUiToEngineStringToEncrypt(textToEncrypt.toUpperCase());
            DataFromEngineToUiEncryptedString resultedString = new DataFromEngineToUiEncryptedString("");

            try {
                resultedString = engine.inputProcessing(dataEncrypt);
                //System.out.println("The encrypted string: "+resultedString.getEncryptedString());
                resultedString = new DataFromEngineToUiEncryptedString(resultedString.getEncryptedString());
                try (PrintWriter out = response.getWriter()) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    out.print(resultedString.getEncryptedString());
                    out.flush();
                }

            } catch (InputStringToEncryptException e) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                resultedString.setEncryptedString(e.getAnswer());
                // return resultedString;
                try (PrintWriter out = response.getWriter()) {
                    out.print(resultedString.getEncryptedString());
                    out.flush();
                }
            }
        }

    }



}
