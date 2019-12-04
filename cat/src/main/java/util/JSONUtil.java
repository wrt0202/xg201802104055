package util;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class JSONUtil {
    public static String getJSON(HttpServletRequest request)
            throws IOException {
        //获得request的输入流对象
        ServletInputStream requestStream = request.getInputStream();
        //以UTF-8为字符集，获得requestStreamReader
        // （InputStreamReader类型的对象是inputStream和Reader对象转换的桥梁）
        InputStreamReader requestStreamReader =
                new InputStreamReader(requestStream, "UTF-8");
        //获得缓冲Reader对象，以便于高效地按行读取
        BufferedReader streamReader =
                new BufferedReader(requestStreamReader);

        //初始化params_json，以便保存JSON格式的请求参数
        StringBuilder params_json = new StringBuilder();
        String line;
        while ((line = streamReader.readLine()) != null){
            params_json.append(line);
        }
        return params_json.toString();
    }
}
