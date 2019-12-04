package cn.edu.sdjzu.xg.bysj.Filter;


import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.Data;
import java.io.IOException;
import java.util.Date;

@WebFilter(filterName = "Filter 0",urlPatterns = {"/*"}/*对所有资源进行过滤*/)
public class Filter00 implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        //轻质类型转换
        HttpServletRequest request = (HttpServletRequest)req;
        HttpServletResponse response = (HttpServletResponse)resp;
        //获得path
        String path = request.getRequestURI();
        // response.setContentType("text/html;charset=UTF-8");

        //如果没有字串"/login"，则考虑设置字符编码
        if (!path.contains("/login")){
            System.out.println("set response");
            //设置响应字符编码为UTF-8
            response.setContentType("text/html;charset=UTF-8");
            //获得请求方法
            String method=request.getMethod();
            //如果方法是POST或者PUT
            if ("POST-PUT".contains(method)){
                //设置请求字符编码为UTF-8
                //设置请求字符集
                request.setCharacterEncoding("UTF-8");
            }
        }
        System.out.println(path+"@"+new Date());
        chain.doFilter(req, resp);//执行其他过滤器，如果过滤器已经执行完毕，则执行原请求

    }

    public void init(FilterConfig config) throws ServletException {

    }

}
