package util;

import cn.edu.sdjzu.xg.bysj.domain.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by STGLJY@126.COM on 2017/12/10.
 */
public class WebHelper {

    public static String getContextPath(HttpServletRequest request) throws ServletException{
        return request.getContextPath();
    }
    public static User getCurrentUser(HttpServletRequest request){
        return getCurrentUser(request.getSession());
    }
    public static User getCurrentUser(HttpSession session){
        return (User)session.getAttribute("user");
    }
    public static void verify(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String url = request.getRequestURL().toString();
        String contextPath = request.getContextPath();
        int indexOfPath = url.lastIndexOf(contextPath);
        String loginUrl = url.substring(0,indexOfPath)+contextPath;
        System.out.println("loginUrl="+loginUrl+"/loginController");
        System.out.println(loginUrl.length());
        HttpSession session = request.getSession();
        if(WebHelper.getCurrentUser(session)==null){
            System.out.println("No login");
            request.setAttribute("msg","请登录后使用");
            request.getRequestDispatcher(loginUrl+"/loginController").forward(request,response);
            return;
        }
    }
}
