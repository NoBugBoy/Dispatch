package http;

import beans.BaseRequest;
import com.sun.net.httpserver.HttpExchange;
import config.DispatchConfig;
import core.ConnectionRepository;
import io.netty.channel.Channel;
import io.netty.util.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rpc.RpcClient;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author yujian
 */
public class Service {
    private static final Logger LOGGER = LoggerFactory.getLogger(Service.class);
    private final static ConnectionRepository CHANNELS = ConnectionRepository.get();
    public static Object getChannelOrRpc(BaseRequest baseRequest, HttpExchange httpExchange) throws IOException {
        Channel channel = CHANNELS.switchChannel(baseRequest.getLabel().get(0),baseRequest.getAvgDispatch());
        if(channel == null){
            if(DispatchConfig.ZOOKEEPER_ENABLE){
                String remoteChannelByLabel = CHANNELS.getRemoteChannelByLabel(baseRequest.getLabel().get(0));
                if(StringUtil.isNullOrEmpty(remoteChannelByLabel)){
                    Responses.sendResponse(httpExchange,Responses.errorMap(null));
                    httpExchange.close();
                }
                return remoteChannelByLabel;
            }else{
                Responses.sendResponse(httpExchange,Responses.errorMap(null));
                httpExchange.close();
            }
        }
        return channel;
    }

    /**
     * 本地发送
     */
    public static void localSend(Channel channel,BaseRequest baseRequest,HttpExchange httpExchange,Long timeOut) throws IOException {
        String uuid = "";
        if(channel!=null && channel.isActive()){
            uuid = CHANNELS.addJob();
            channel.writeAndFlush(uuid + "&" + baseRequest.getMsg());
        }else{
            Responses.sendResponse(httpExchange,Responses.errorMap("Connection not found"));
        }

        long startTime = System.currentTimeMillis();

        while (CHANNELS.checkJob(uuid)){
            try {
                if(System.currentTimeMillis() - startTime > timeOut){
                    //轮....训超过5S就中断
                    LOGGER.error("response time out");
                    CHANNELS.removeJob(uuid);
                    Responses.sendResponse(httpExchange,Responses.errorMap("timeout"));
                }
                TimeUnit.MILLISECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        LOGGER.info("Task complete of id is {}",uuid);
        String job = CHANNELS.getJob(uuid);
        CHANNELS.removeJob(uuid);
        Responses.sendResponse(httpExchange,Responses.successMap(job));
    }

    /**
     * 远程调用
     */
    public static void rpcSend(String host, BaseRequest baseRequest,HttpExchange httpExchange, Long timeOut) throws IOException {
        String msg = RpcClient.send(host, baseRequest, timeOut);
        if(null == msg){
            Responses.sendResponse(httpExchange,Responses.errorMap("调用失败！"));
        }else{
            Responses.sendResponse(httpExchange,msg);
        }





    }
}
