package http;

import beans.BaseRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import io.netty.channel.Channel;
import org.apache.commons.collections.CollectionUtils;
import utils.JsonUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * Http入口
 */
public class DispatchHandler implements HttpHandler {
    private final Long TIME_OUT;
    public DispatchHandler(Long timeOut) {
        if(timeOut > 50000L){
            this.TIME_OUT = 50000L;
        }else{
            this.TIME_OUT = 10000L;
        }
    }
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String requestMethod = httpExchange.getRequestMethod();
        if("GET".equalsIgnoreCase(requestMethod)){
            Responses.sendResponse(httpExchange,Responses.errorMap("Method not allowed"));
        }else{
            InputStream is = httpExchange.getRequestBody();
            String response = Responses.toBufferString(is);
            BaseRequest baseRequest = null;
            try{
                baseRequest = JsonUtils.get().readValue(response, BaseRequest.class);
            }catch (JsonProcessingException exception){
                Responses.sendResponse(httpExchange,Responses.errorMap("JSON format exception"));
                is.close();
                httpExchange.close();
            }

            if(baseRequest == null){
                Responses.sendResponse(httpExchange,Responses.errorMap(null));
                is.close();
                httpExchange.close();
            }

            if(CollectionUtils.isEmpty(baseRequest.getLabel())){
                Responses.sendResponse(httpExchange,Responses.errorMap(null));
                is.close();
                httpExchange.close();
            }
            //群发暂时不支持，还没写
            if(baseRequest.getLabel().size() != 1){
                Responses.sendResponse(httpExchange,Responses.errorMap(null));
                is.close();
                httpExchange.close();
            }
            //发现一个channel

            Object channel = Service.getChannelOrRpc(baseRequest, httpExchange);
            if(channel instanceof Channel){
                Service.localSend((Channel)channel,baseRequest,httpExchange,TIME_OUT);
            }else if(channel instanceof String){
                Service.rpcSend( buildHttpUrl((String)channel),baseRequest,httpExchange,TIME_OUT);
            }

            is.close();

        }
        httpExchange.close();
    }
    public String buildHttpUrl(String url){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("http://");
        stringBuilder.append(url);
        stringBuilder.append("/get");
        return stringBuilder.toString();
    }

}