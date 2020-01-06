package coded;

import beans.YuSocket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 用不上，因为发送是字符串
 * @author yujian
 */
public class YuEncode extends MessageToByteEncoder<YuSocket> {
    @Override
    protected void encode(ChannelHandlerContext ctx, YuSocket msg, ByteBuf out) {
        out.writeInt(msg.getHead());
        out.writeInt(msg.getContentLength());
        out.writeBytes(msg.getContent());
    }
}
