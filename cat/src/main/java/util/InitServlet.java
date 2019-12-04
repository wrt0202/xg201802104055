package util;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns={"/initServlet"},loadOnStartup=0)

public class InitServlet extends HttpServlet {
    @Override
    public void init() throws ServletException {
        System.out.println("HEREEEE");
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getServletContext().setAttribute("root",request.getContextPath());
        System.out.println("In initServlet, request.getContextPath()=" + request.getContextPath());
    }
}
