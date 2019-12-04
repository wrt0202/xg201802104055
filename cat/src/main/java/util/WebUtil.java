package util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

public class WebUtil {
    public static void setUTF8(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        //请求数据编码为UTF-8
        request.setCharacterEncoding("UTF-8");
        //响应数据编码为UTF-8
        response.setContentType("text/html;charset=UTF-8");
    }
}
