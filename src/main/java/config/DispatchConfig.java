package config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DispatchConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(DispatchConfig.class);
    public static Boolean ZOOKEEPER_ENABLE = false;
    public static String ZOOKEEPER_HOST = null ;
    public static Integer DISPATCH_PORT = null ;
    public static Integer NETTY_PORT = null ;
    public static Long WAIT_TIME = null ;
    static{
        Properties properties = new Properties();
        InputStream resourceAsStream = null;
        try{
            resourceAsStream= DispatchConfig.class.getResourceAsStream("/dispatch.properties");
            properties.load(resourceAsStream);
            ZOOKEEPER_ENABLE =Boolean.valueOf(properties.getProperty(Prop.ZK_ENABLE.getValue(),Prop.DEFAULT_ZK_ENABLE.getValue()));
            ZOOKEEPER_HOST =  properties.getProperty(Prop.ZK_HOST.getValue(),Prop.DEFAULT_ZK_HOST.getValue());
            DISPATCH_PORT = Integer.parseInt(properties.getProperty(Prop.DISPATCH_PORT.getValue(),Prop.DEFAULT_DISPATCH_PORT.getValue()));
            NETTY_PORT =  Integer.parseInt(properties.getProperty(Prop.NETTY_PORT.getValue(),Prop.DEFAULT_NETTY_PORT.getValue()));
            WAIT_TIME =  Long.valueOf(properties.getProperty(Prop.WAIT_TIME.getValue(),Prop.DEFAULT_WAIT_TIME.getValue()));
            LOGGER.info("load properties successful !");
        } catch (IOException e) {
            LOGGER.error("load properties fail !" + e.getMessage());
        }finally {
            try {
                resourceAsStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
enum Prop{
    /**
     * zk是否启用，不启用为单机
     */
    ZK_ENABLE("dispatch.zookeeper.enable"),
    ZK_HOST("dispatch.zookeeper.host"),
    DISPATCH_PORT("dispatch.zookeeper.start.port"),
    NETTY_PORT("dispatch.zookeeper.netty.port"),
    WAIT_TIME("dispatch.wait.timeout"),
    /**
     * 默认属性
     */
    DEFAULT_ZK_ENABLE("false"),
    DEFAULT_ZK_HOST("127.0.0.1:1881"),
    DEFAULT_DISPATCH_PORT("8990"),
    DEFAULT_WAIT_TIME("3000"),
    DEFAULT_NETTY_PORT("8880");
    private String propName;
    Prop(String s) {
        propName = s;
    }
    public String getValue(){
        return propName;
    }

}
