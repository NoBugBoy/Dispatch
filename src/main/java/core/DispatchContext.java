package core;

import beans.BaseRequest;
import beans.YuSocket;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.JsonUtils;

import java.util.Objects;

/**
 * 分发类
 * @author yujian
 */
public class DispatchContext extends SimpleChannelInboundHandler<YuSocket>{
    private static final Logger LOGGER = LoggerFactory.getLogger(DispatchContext.class);
    private final ConnectionRepository REPOSITORY = ConnectionRepository.get();


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, YuSocket o) throws Exception {
        String str = new String(o.getContent());
        if(!str.contains("&")){
            //客户端加入
            BaseRequest baseRequest = JsonUtils.get().readValue(str, BaseRequest.class);
            Channel channel = REPOSITORY.find(channelHandlerContext.channel().id());
            if(Objects.isNull(channel)){
                boolean zoo_check = REPOSITORY.addChannel(baseRequest.getLabel(), channelHandlerContext.channel());
                if(zoo_check){
                    LOGGER.info("send connect successful callback ");
                    channelHandlerContext.channel().writeAndFlush("connect successful !");
                }else{
                    LOGGER.info("zookeeper not connection");
                    channelHandlerContext.channel().writeAndFlush("retry connect !");
                }
            }
        }else{
            //接收客户端消息
            String[] split = str.split("&");
            if(split.length == 2){
                LOGGER.info("Task callback id is =" + split[0]);
                REPOSITORY.asyncJob(split[0],split[1]);
            }
        }
    }
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        LOGGER.info("new channel active {}",ctx.name());
//        System.out.println("add new channel id： " + ctx.channel().id().asShortText());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        LOGGER.info("channel inactive {}",ctx.name());
        REPOSITORY.remove(ctx.channel());
//        System.out.println("removed channel id :" + ctx.channel().id().asShortText());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        REPOSITORY.remove(ctx.channel());
        LOGGER.error(cause.getMessage());
        cause.printStackTrace();
        ctx.close();
    }
}
