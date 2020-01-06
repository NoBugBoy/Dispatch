package testclient;

import beans.BaseRequest;
import beans.YuSocket;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;

/**
 * 模拟手机端
 */
public class SocketClient {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("127.0.0.1",8089);
            socket.setKeepAlive(true);
            boolean connected = socket.isConnected();
            OutputStream outputStream = socket.getOutputStream();
            BaseRequest baseRequest = new BaseRequest();
            baseRequest.setLabel(Arrays.asList("C1"));
            baseRequest.setMsg("");
            ObjectMapper objectMapper = new ObjectMapper();
            String s = objectMapper.writeValueAsString(baseRequest);
            YuSocket yuSocket = new YuSocket();
            //移动端要按照自定义协议发送消息
            yuSocket.setHead(2020);
            yuSocket.setContentLength(s.length());
            yuSocket.setContent(s.getBytes());
            outputStream.write(toByteArray(yuSocket));
            outputStream.flush();

            boolean flag =false;
            String id = "";
            InputStream inputStream = socket.getInputStream();
            while(connected){
                byte[] b = new byte[1024];
                StringBuilder builder = new StringBuilder();
                if (inputStream.read(b)  != -1){
                    String str = new String(b);
                    builder.append(str);

                }
                String[] split = builder.toString().split("&");
                if(split.length == 2){
                    id = split[0];
                    System.out.println("任务id" + split[0]);
                    System.out.println("msg" + split[1]);
                    flag =true;
                }else{
                    System.out.println(builder.toString());
                }

                if(flag){
//                    BaseRequest baseRequestc = new BaseRequest();
//                    baseRequestc.setLable(Arrays.asList("A1"));
//                    baseRequestc.setMsg(id+"&"+"测试11");

                    String sss = "{\"code\":0,\"message\":\"ok\",\"data\":[{\"id\":\"1\",\"name\":\"\\u7537\\u751f\",\"cates\":[{\"id\":\"283\",\"parent_id\":\"0\",\"name\":\"\\u73b0\\u5b9e\",\"cover\":\"http:\\/\\/readstatic.zhulang.com\\/res\\/cover\\/025\\/742\\/25742_180.jpg?1563427434\",\"book_count\":\"72\",\"children\":[],\"level\":1,\"book_type\":0,\"covers\":[\"http:\\/\\/readstatic.zhulang.com\\/res\\/cover\\/025\\/742\\/25742_180.jpg?1563427434\",\"http:\\/\\/readstatic.zhulang.com\\/res\\/cover\\/026\\/791\\/26791_180.jpg?1563427429\"]},{\"id\":\"1\",\"parent_id\":\"0\",\"name\":\"\\u4fee\\u771f\\u6b66\\u4fa0\",\"cover\":\"http:\\/\\/readstatic.zhulang.com\\/res\\/cover\\/030\\/214\\/30214_180.jpg\",\"book_count\":\"102\",\"children\":[],\"level\":1,\"book_type\":0,\"covers\":[\"http:\\/\\/readstatic.zhulang.com\\/res\\/cover\\/030\\/214\\/30214_180.jpg\",\"http:\\/\\/readstatic.zhulang.com\\/res\\/cover\\/012\\/831\\/12831_180.jpg?1563427457\"]},{\"id\":\"6\",\"parent_id\":\"0\",\"name\":\"\\u7384\\u5e7b\\u5c0f\\u8bf4\",\"cover\":\"http:\\/\\/readstatic.zhulang.com\\/res\\/cover\\/038\\/786\\/38786_180.jpg?1563427281\",\"book_count\":\"621\",\"children\":[],\"level\":1,\"book_type\":0,\"covers\":[\"http:\\/\\/readstatic.zhulang.com\\/res\\/cover\\/038\\/786\\/38786_180.jpg?1563427281\",\"http:\\/\\/readstatic.zhulang.com\\/res\\/cover\\/036\\/651\\/36651_180.jpg?1563427304\"]},{\"id\":\"22\",\"parent_id\":\"0\",\"name\":\"\\u5386\\u53f2\\u5c0f\\u8bf4\",\"cover\":\"http:\\/\\/readstatic.zhulang.com\\/res\\/cover\\/135\\/742\\/135742_180.jpg?1563425980\",\"book_count\":\"53\",\"children\":[],\"level\":1,\"book_type\":0,\"covers\":[\"http:\\/\\/readstatic.zhulang.com\\/res\\/cover\\/135\\/742\\/135742_180.jpg?1563425980\",\"http:\\/\\/readstatic.zhulang.com\\/res\\/cover\\/121\\/250\\/121250_180.jpg?1563426127\"]},{\"id\":\"26\",\"parent_id\":\"0\",\"name\":\"\\u519b\\u4e8b\\u5c0f\\u8bf4\",\"cover\":\"http:\\/\\/readstatic.zhulang.com\\/res\\/cover\\/057\\/922\\/57922_180.jpg?1563426947\",\"book_count\":\"19\",\"children\":[],\"level\":1,\"book_type\":0,\"covers\":[\"http:\\/\\/readstatic.zhulang.com\\/res\\/cover\\/057\\/922\\/57922_180.jpg?1563426947\",\"http:\\/\\/readstatic.zhulang.com\\/res\\/cover\\/036\\/703\\/36703_180.jpg?1563427294\"]},{\"id\":\"30\",\"parent_id\":\"0\",\"name\":\"\\u90fd\\u5e02\\u5c0f\\u8bf4\",\"cover\":\"http:\\/\\/readstatic.zhulang.com\\/res\\/cover\\/081\\/588\\/81588_180.jpg?1563426820\",\"book_count\":\"705\",\"children\":[],\"level\":1,\"book_type\":0,\"covers\":[\"http:\\/\\/readstatic.zhulang.com\\/res\\/cover\\/081\\/588\\/81588_180.jpg?1563426820\",\"http:\\/\\/readstatic.zhulang.com\\/res\\/cover\\/028\\/492\\/28492_180.jpg?1563427419\"]},{\"id\":\"37\",\"parent_id\":\"0\",\"name\":\"\\u79d1\\u5e7b\\u5c0f\\u8bf4\",\"cover\":\"http:\\/\\/readstatic.zhulang.com\\/res\\/cover\\/058\\/508\\/58508_180.jpg?1563426930\",\"book_count\":\"31\",\"children\":[],\"level\":1,\"book_type\":0,\"covers\":[\"http:\\/\\/readstatic.zhulang.com\\/res\\/cover\\/058\\/508\\/58508_180.jpg?1563426930\",\"http:\\/\\/readstatic.zhulang.com\\/res\\/cover\\/033\\/570\\/33570_180.jpg?1563427389\"]},{\"id\":\"44\",\"parent_id\":\"0\",\"name\":\"\\u7f51\\u6e38\\u5c0f\\u8bf4\",\"cover\":\"http:\\/\\/readstatic.zhulang.com\\/res\\/cover\\/046\\/032\\/46032_180.jpg?1563427082\",\"book_count\":\"13\",\"children\":[],\"level\":1,\"book_type\":0,\"covers\":[\"http:\\/\\/readstatic.zhulang.com\\/res\\/cover\\/046\\/032\\/46032_180.jpg?1563427082\",\"http:\\/\\/readstatic.zhulang.com\\/res\\/cover\\/094\\/986\\/94986_180.jpg?1563426700\"]},{\"id\":\"50\",\"parent_id\":\"0\",\"name\":\"\\u63a8\\u7406\",\"cover\":\"http:\\/\\/readstatic.zhulang.com\\/res\\/cover\\/119\\/875\\/119875_180.jpg?1563426156\",\"book_count\":\"135\",\"children\":[],\"level\":1,\"book_type\":0,\"covers\":[\"http:\\/\\/readstatic.zhulang.com\\/res\\/cover\\/119\\/875\\/119875_180.jpg?1563426156\",\"http:\\/\\/readstatic.zhulang.com\\/res\\/cover\\/119\\/818\\/119818_180.jpg?1563426162\"]},{\"id\":\"56\",\"parent_id\":\"0\",\"name\":\"\\u7ade\\u6280\\u4f53\\u80b2\",\"cover\":\"http:\\/\\/readstatic.zhulang.com\\/res\\/cover\\/012\\/853\\/12853_180.jpg?1563427457\",\"book_count\":\"2\",\"children\":[],\"level\":1,\"book_type\":0,\"covers\":[\"http:\\/\\/readstatic.zhulang.com\\/res\\/cover\\/012\\/853\\/12853_180.jpg?1563427457\",\"http:\\/\\/readstatic.zhulang.com\\/res\\/cover\\/152\\/827\\/152827_180.jpg?1563425832\"]},{\"id\":\"159\",\"parent_id\":\"0\",\"name\":\"\\u5176\\u4ed6\",\"cover\":\"http:\\/\\/readstatic.zhulang.com\\/res\\/cover\\/026\\/791\\/26791_180.jpg?1563427429\",\"book_count\":\"9\",\"children\":[],\"level\":1,\"book_type\":0,\"covers\":[\"http:\\/\\/readstatic.zhulang.com\\/res\\/cover\\/026\\/791\\/26791_180.jpg?1563427429\",\"http:\\/\\/readstatic.zhulang.com\\/res\\/cover\\/157\\/465\\/157465_180.jpg?1563425732\"]}]},{\"id\":\"2\",\"name\":\"\\u5973\\u751f\",\"cates\":[{\"id\":\"285\",\"parent_id\":\"0\",\"name\":\"\\u73b0\\u5b9e\",\"cover\":\"http:\\/\\/readstatic.zhulang.com\\/res\\/cover\\/103\\/496\\/103496_180.jpg?1563426446\",\"book_count\":\"25\",\"children\":[],\"level\":1,\"book_type\":0,\"covers\":[\"http:\\/\\/readstatic.zhulang.com\\/res\\/cover\\/103\\/496\\/103496_180.jpg?1563426446\",\"http:\\/\\/readstatic.zhulang.com\\/res\\/cover\\/130\\/296\\/130296_180.jpg?1563426037\"]},{\"id\":\"61\",\"parent_id\":\"0\",\"name\":\"\\u73b0\\u4ee3\\u8a00\\u60c5\",\"cover\":\"http:\\/\\/readstatic.zhulang.com\\/res\\/cover\\/033\\/333\\/33333_180.jpg?1563427392\",\"book_count\":\"1104\",\"children\":[],\"level\":1,\"book_type\":0,\"covers\":[\"http:\\/\\/readstatic.zhulang.com\\/res\\/cover\\/033\\/333\\/33333_180.jpg?1563427392\",\"http:\\/\\/readstatic.zhulang.com\\/res\\/cover\\/105\\/556\\/105556_180.jpg?1563426419\"]},{\"id\":\"68\",\"parent_id\":\"0\",\"name\":\"\\u53e4\\u4ee3\\u8a00\\u60c5\",\"cover\":\"http:\\/\\/readstatic.zhulang.com\\/res\\/cover\\/046\\/485\\/46485_180.jpg?1563427061\",\"book_count\":\"645\",\"children\":[],\"level\":1,\"book_type\":0,\"covers\":[\"http:\\/\\/readstatic.zhulang.com\\/res\\/cover\\/046\\/485\\/46485_180.jpg?1563427061\",\"http:\\/\\/readstatic.zhulang.com\\/res\\/cover\\/044\\/262\\/44262_180.jpg?1563427164\"]},{\"id\":\"75\",\"parent_id\":\"0\",\"name\":\"\\u6d6a\\u6f2b\\u9752\\u6625\",\"cover\":\"http:\\/\\/readstatic.zhulang.com\\/res\\/cover\\/044\\/045\\/44045_180.jpg?1563427170\",\"book_count\":\"34\",\"children\":[],\"level\":1,\"book_type\":0,\"covers\":[\"http:\\/\\/readstatic.zhulang.com\\/res\\/cover\\/044\\/045\\/44045_180.jpg?1563427170\",\"http:\\/\\/readstatic.zhulang.com\\/res\\/cover\\/032\\/893\\/32893_180.jpg?1563427397\"]},{\"id\":\"81\",\"parent_id\":\"0\",\"name\":\"\\u7384\\u5e7b\\u5f02\\u80fd\",\"cover\":\"http:\\/\\/readstatic.zhulang.com\\/res\\/cover\\/128\\/834\\/128834_180.jpg?1563426066\",\"book_count\":\"49\",\"children\":[],\"level\":1,\"book_type\":0,\"covers\":[\"http:\\/\\/readstatic.zhulang.com\\/res\\/cover\\/128\\/834\\/128834_180.jpg?1563426066\",\"http:\\/\\/readstatic.zhulang.com\\/res\\/cover\\/043\\/271\\/43271_180.jpg?1563427187\"]},{\"id\":\"84\",\"parent_id\":\"0\",\"name\":\"\\u60ac\\u7591\",\"cover\":\"http:\\/\\/readstatic.zhulang.com\\/res\\/cover\\/117\\/227\\/117227_180.jpg?1563426200\",\"book_count\":\"205\",\"children\":[],\"level\":1,\"book_type\":0,\"covers\":[\"http:\\/\\/readstatic.zhulang.com\\/res\\/cover\\/117\\/227\\/117227_180.jpg?1563426200\",\"http:\\/\\/readstatic.zhulang.com\\/res\\/cover\\/042\\/131\\/42131_180.jpg?1563427215\"]},{\"id\":\"90\",\"parent_id\":\"0\",\"name\":\"\\u6b66\\u4fa0\\u4ed9\\u4fa0\",\"cover\":\"http:\\/\\/readstatic.zhulang.com\\/res\\/cover\\/086\\/881\\/86881_180.jpg?1563426805\",\"book_count\":\"23\",\"children\":[],\"level\":1,\"book_type\":0,\"covers\":[\"http:\\/\\/readstatic.zhulang.com\\/res\\/cover\\/086\\/881\\/86881_180.jpg?1563426805\",\"http:\\/\\/readstatic.zhulang.com\\/res\\/cover\\/041\\/682\\/41682_180.jpg?1563427220\"]},{\"id\":\"175\",\"parent_id\":\"0\",\"name\":\"\\u77ed\\u7bc7\",\"cover\":\"http:\\/\\/readstatic.zhulang.com\\/res\\/cover\\/112\\/136\\/112136_180.jpg?1563426354\",\"book_count\":\"4\",\"children\":[],\"level\":1,\"book_type\":4,\"covers\":[\"http:\\/\\/readstatic.zhulang.com\\/res\\/cover\\/112\\/136\\/112136_180.jpg?1563426354\",\"http:\\/\\/readstatic.zhulang.com\\/res\\/cover\\/118\\/366\\/118366_180.jpg?1563426183\"]},{\"id\":\"161\",\"parent_id\":\"0\",\"name\":\"\\u5176\\u4ed6\",\"cover\":\"http:\\/\\/readstatic.zhulang.com\\/res\\/cover\\/130\\/698\\/130698_180.jpg?1563426029\",\"book_count\":\"3\",\"children\":[],\"level\":1,\"book_type\":4,\"covers\":[\"http:\\/\\/readstatic.zhulang.com\\/res\\/cover\\/130\\/698\\/130698_180.jpg?1563426029\",\"http:\\/\\/readstatic.zhulang.com\\/res\\/cover\\/157\\/404\\/157404_180.jpg?1563425744\"]}]},{\"id\":\"3\",\"name\":\"\\u51fa\\u7248\",\"cates\":[{\"id\":\"263\",\"parent_id\":\"0\",\"name\":\"\\u7ecf\\u5178\\u6b66\\u4fa0\",\"cover\":\"http:\\/\\/readstatic.zhulang.com\\/res\\/publishedbooks\\/\\/5c9f5f3dd98c8\\/cover_180.jpeg\",\"book_count\":\"143\",\"children\":[],\"level\":1,\"book_type\":3,\"covers\":[\"http:\\/\\/readstatic.zhulang.com\\/res\\/publishedbooks\\/\\/5c9f5f3dd98c8\\/cover_180.jpeg\",\"http:\\/\\/readstatic.zhulang.com\\/res\\/publishedbooks\\/\\/5c9f5f3e1d3ec\\/cover_180.jpeg\"]},{\"id\":\"267\",\"parent_id\":\"0\",\"name\":\"\\u7ecf\\u5178\\u8a00\\u60c5\",\"cover\":\"http:\\/\\/readstatic.zhulang.com\\/res\\/publishedbooks\\/\\/5b6bdb60b47f2\\/cover_180.jpeg\",\"book_count\":\"41\",\"children\":[],\"level\":1,\"book_type\":3,\"covers\":[\"http:\\/\\/readstatic.zhulang.com\\/res\\/publishedbooks\\/\\/5b6bdb60b47f2\\/cover_180.jpeg\",\"http:\\/\\/readstatic.zhulang.com\\/res\\/publishedbooks\\/\\/5b6ac51c2b603\\/cover_180.jpeg\"]},{\"id\":\"270\",\"parent_id\":\"0\",\"name\":\"\\u7ecf\\u5178\\u540d\\u8457\",\"cover\":\"http:\\/\\/readstatic.zhulang.com\\/res\\/publishedbooks\\/\\/5c9ce6d5c38e7\\/cover_180.jpeg\",\"book_count\":\"114\",\"children\":[],\"level\":1,\"book_type\":3,\"covers\":[\"http:\\/\\/readstatic.zhulang.com\\/res\\/publishedbooks\\/\\/5c9ce6d5c38e7\\/cover_180.jpeg\",\"http:\\/\\/readstatic.zhulang.com\\/res\\/publishedbooks\\/\\/5c9ce6d561198\\/cover_180.jpeg\"]},{\"id\":\"274\",\"parent_id\":\"0\",\"name\":\"\\u5065\\u5eb7\\u5fc3\\u7075\",\"cover\":\"http:\\/\\/readstatic.zhulang.com\\/res\\/publishedbooks\\/\\/5b90911cee40f\\/cover_180.jpeg\",\"book_count\":\"42\",\"children\":[],\"level\":1,\"book_type\":3,\"covers\":[\"http:\\/\\/readstatic.zhulang.com\\/res\\/publishedbooks\\/\\/5b90911cee40f\\/cover_180.jpeg\",\"http:\\/\\/readstatic.zhulang.com\\/res\\/publishedbooks\\/\\/5b7bad6a67854\\/cover_180.jpeg\"]},{\"id\":\"278\",\"parent_id\":\"0\",\"name\":\"\\u5f53\\u4ee3\\u6587\\u5b66\",\"cover\":\"http:\\/\\/readstatic.zhulang.com\\/res\\/publishedbooks\\/\\/5b839f60bf98b\\/cover_180.jpeg\",\"book_count\":\"244\",\"children\":[],\"level\":1,\"book_type\":3,\"covers\":[\"http:\\/\\/readstatic.zhulang.com\\/res\\/publishedbooks\\/\\/5b839f60bf98b\\/cover_180.jpeg\",\"http:\\/\\/readstatic.zhulang.com\\/res\\/publishedbooks\\/\\/5bc729095a571\\/cover_180.jpeg\"]}]}]}";
                    // id & msg 的格式
                    String a1 = id+"&"+sss;
                    System.out.println(a1);
                    YuSocket ss = new YuSocket();
                    ss.setHead(2020);
                    ss.setContentLength(a1.length());
                    ss.setContent(a1.getBytes());
                    outputStream.write(toByteArray(ss));
                    outputStream.flush();
                    flag = false;
                }

            }


            System.out.println(connected);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static byte[] toByteArray(YuSocket obj) {
        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(obj.getHead());
        buf.writeInt(obj.getContentLength());
        buf.writeBytes(obj.getContent());
        return buf.array();
    }


}
