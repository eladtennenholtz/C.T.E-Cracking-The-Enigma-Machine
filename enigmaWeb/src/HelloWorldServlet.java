import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.Engine;
import logic.EngineFirst;

import java.io.IOException;

@WebServlet (name = "Hello Servlet",urlPatterns = "/hello")
public class HelloWorldServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
        response.getWriter().println("Hello world! get request");

    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
        response.getWriter().println("Hello world! post request");
    }









}
