package utils;

import io.netty.util.internal.StringUtil;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class IpUtils {
    private static String ip;
    private IpUtils(){}
    public static String getRealIp(){
        if(!StringUtil.isNullOrEmpty(ip)){
            return ip;
        }
        try {
            URL url = new URL("http://www.net.cn/static/customercare/yourip.asp");
            URLConnection urlConnection = url.openConnection();
            Object content = urlConnection.getContent();
            if(content!=null){
                BufferedInputStream bufferedInputStream = new BufferedInputStream((InputStream) content);
                byte[] temp = new byte[1024];
                while (bufferedInputStream.read()!=-1){
                    bufferedInputStream.read(temp,0,temp.length);
                }
                bufferedInputStream.close();
                String gbk = new String(temp, "GBK");
                int before = gbk.indexOf("<h2>");
                int last = gbk.indexOf("</h2>");
                ip = gbk.substring(before + 4 , last);
                return ip;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        String realIp = getRealIp();
        System.out.println(realIp);
    }
}
