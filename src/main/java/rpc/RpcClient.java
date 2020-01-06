package rpc;

import beans.BaseRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import utils.JsonUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class RpcClient {
    private static final HttpClient HTTP_CLIENT = HttpClients.createDefault();
    private RpcClient(){};
    public static String send(String url, BaseRequest params, Long timeOut){
        HttpPost post = new HttpPost(url);
        try {
            HttpEntity entity = new StringEntity(JsonUtils.get().writeValueAsString(params));
            post.setEntity(entity);
            post.setConfig(RequestConfig.custom()
                    .setConnectTimeout(timeOut.intValue())
                    .setSocketTimeout(10000)
                    .setConnectionRequestTimeout(10000)
                    .build());

        } catch (UnsupportedEncodingException | JsonProcessingException e) {
            e.printStackTrace();
        }
        try {
            HttpResponse execute = HTTP_CLIENT.execute(post);
            String response = EntityUtils.toString(execute.getEntity(),"UTF-8");
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
