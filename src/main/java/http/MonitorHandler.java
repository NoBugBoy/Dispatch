package http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import core.ConnectionRepository;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yujian
 * 通过接口获取当前状态
 */
public class MonitorHandler implements HttpHandler {
    private final ConnectionRepository channels = ConnectionRepository.get();
    private final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Map<String,Object> map = new HashMap<>();
        map.put("ChannelSize",channels.size());
        map.put("TaskSize",channels.keys());
        map.put("values",channels.values());
        sendResponse(exchange,map);
        exchange.close();
    }
    private void sendResponse(HttpExchange httpExchange, Map<String,Object> msg) throws IOException {
        Headers headers = httpExchange.getResponseHeaders();
        OutputStream out = httpExchange.getResponseBody();
        String response = OBJECT_MAPPER.writeValueAsString(msg);
        httpExchange.sendResponseHeaders(200, response.length());
        headers.set("Content-Type", "application/json; charset=utf8");
        out.write(response.getBytes(StandardCharsets.UTF_8));
        out.close();
    }
}
