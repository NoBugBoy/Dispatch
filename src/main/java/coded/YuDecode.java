package coded;

import beans.YuSocket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 自定义解码
 */
public class YuDecode extends ByteToMessageDecoder {
    private static final Logger LOGGER = LoggerFactory.getLogger(YuDecode.class);
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        //head 4字节
        //length 4字节
        int LENGTH = 4 + 4;
        if(in.readableBytes() >= LENGTH){
            //符合协议
            if(in.readableBytes() > 17024){
                LOGGER.warn("Protocol defined byte size exceeded {}" , in.readableBytes());
                //限制数据大小
                in.skipBytes(in.readableBytes());
            }
            int begin;
            while(true){
                //开始的位置
                begin = in.readerIndex();
                //标记
                in.markReaderIndex();
                if(in.readInt() == 2020){
                    //每次读4个字节 读到了包头 符合要求
                    break;
                }
                //没读到包头重置读取位置
                in.resetReaderIndex();
                //略过一个字节
                in.readByte();
                if(in.readableBytes() < LENGTH){
                    return;
                }
            }
            //消息长度
            int contentLength = in.readInt();
            if(in.readableBytes() < contentLength){
                //消息的内容长度没有达到预期设定的长度，还原指针重新读
                in.readerIndex(begin);
                return;
            }
            byte[] content = new byte[contentLength];
            in.readBytes(content);
            YuSocket protocol = new YuSocket();
            protocol.setHead(2020);
            protocol.setContent(content);
            protocol.setContentLength(contentLength);
            out.add(protocol);
        }
    }
}
