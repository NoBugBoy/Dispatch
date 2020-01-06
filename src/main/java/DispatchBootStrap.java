import com.sun.net.httpserver.HttpServer;
import config.DispatchConfig;
import core.NettyStart;
import core.ZookeeperClient;
import http.DispatchHandler;
import http.MonitorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 万物开始的地方
 */
public class DispatchBootStrap {
    private static final Logger LOGGER = LoggerFactory.getLogger(DispatchBootStrap.class);
    public static void main(String[] args) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 10, 1000, TimeUnit.MINUTES, new LinkedBlockingQueue<>(), r -> {
            Thread thread = new Thread(r);
            thread.setName("dispatch-");
            return thread;
        });
        if(DispatchConfig.ZOOKEEPER_ENABLE){
            ZookeeperClient.init(DispatchConfig.ZOOKEEPER_HOST);
        }
//        ExecutorService threadPoolExecutor = Executors.newCachedThreadPool();
        InetSocketAddress address = new InetSocketAddress(DispatchConfig.DISPATCH_PORT);
        try {
            HttpServer httpServer = HttpServer.create(address, 0);
            httpServer.createContext("/get",new DispatchHandler(DispatchConfig.WAIT_TIME));
            httpServer.createContext("/info",new MonitorHandler());
            httpServer.setExecutor(threadPoolExecutor);
            httpServer.start();
            LOGGER.info("====== HTTP START port is {} ======",DispatchConfig.DISPATCH_PORT);
            threadPoolExecutor.execute(new DispatchNetty());
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                if(LOGGER.isWarnEnabled()){
                    LOGGER.warn("shut down server !");
                }
                ZookeeperClient.deleteLocal();
                threadPoolExecutor.shutdown();
            }));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    static class DispatchNetty implements Runnable{
        @Override
        public void run() {
            LOGGER.info("====== NETTY START port is {} ======",DispatchConfig.NETTY_PORT);
            NettyStart nettyStart = new NettyStart(DispatchConfig.NETTY_PORT);
            nettyStart.start();
        }
    }
}
