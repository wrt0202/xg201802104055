package cn.edu.sdjzu.xg.bysj.Filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

//*表示对所有的web资源有效
@WebFilter(filterName = "Filter 10",urlPatterns = "/*")
public class Filter10 implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}
    //获得请求时间
    private String getTime() {
        //将日期格式转化为字符串格式
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日HH:mm");
        return df.format(date);
    }
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //打印提示信息
        System.out.println("ContentFilter begins");
        //强制类型转换成HttpServletRequest类型
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        //打印被请求的资源名称和请求时间
        System.out.println(request.getRequestURI() + " @ " + this.getTime());
        //执行其他过滤器，若过滤器已经执行完毕，则执行原请求
        filterChain.doFilter(servletRequest,servletResponse);
        //打印提示信息
        System.out.println("ContentFilter ends");
    }

    @Override
    public void destroy() {}
}