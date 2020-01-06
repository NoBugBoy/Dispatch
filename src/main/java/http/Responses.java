package http;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import io.netty.util.internal.StringUtil;
import utils.JsonUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringEscapeUtils;

/**
 * 通用返回值封装
 */
public class Responses {
    public static Map<String,Object> errorMap(String errorMsg){
        Map<String,Object> map = new HashMap<>(2);
        map.put("code",500);
        map.put("msg", StringUtil.isNullOrEmpty(errorMsg)?"ERROR":errorMsg);
        return map;
    }
    public static Map<String,Object> successMap(String msg){
        Map<String,Object> map = new HashMap<>(2);
        map.put("code",200);
        map.put("msg", StringUtil.isNullOrEmpty(msg)?"SUCCESS":msg);
        return map;
    }
    public static void sendResponse(HttpExchange httpExchange, Map<String, Object> msg) throws IOException {
        Headers headers = httpExchange.getResponseHeaders();
        OutputStream out = httpExchange.getResponseBody();
        String response = JsonUtils.get().writeValueAsString(msg);
        httpExchange.sendResponseHeaders(200, response.length());
        headers.set("Content-Type", "application/json; charset=utf8");
        out.write(response.getBytes());
        out.close();
    }
    public static void sendResponse(HttpExchange httpExchange, String msg) throws IOException {
        Headers headers = httpExchange.getResponseHeaders();
        OutputStream out = httpExchange.getResponseBody();
        httpExchange.sendResponseHeaders(200, msg.length());
        headers.set("Content-Type", "application/json; charset=utf8");
        String s = StringEscapeUtils.unescapeJava(new String(msg.getBytes(StandardCharsets.UTF_8)));
        try {
            out.write(s.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.close();
    }
    public static String toBufferString(InputStream is) throws IOException {
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(is, StandardCharsets.UTF_8);
        for (; ; ) {
            int rsz = in.read(buffer, 0, buffer.length);
            if (rsz < 0){
                break;
            }
            out.append(buffer, 0, rsz);
        }
        in.close();
        return out.toString();
    }
}
