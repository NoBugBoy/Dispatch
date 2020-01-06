package core;

import beans.RemoteChannel;
import config.DispatchConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.IpUtils;
import utils.JsonUtils;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

import static org.apache.zookeeper.ZooDefs.Ids.OPEN_ACL_UNSAFE;

/**
 * zk客户端
 */
public class ZookeeperClient  {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperClient.class);
    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
    /**
     * 客户端节点
     */
    private static final String PATH = "/channels";
    /**
     * 服务端节点
     */
    private static final String REMOTES_PATH = "/remotes";
    private static final ConnectionRepository CHANNEL = ConnectionRepository.get();
    private static ZooKeeper zooKeeper = null;
    private static final Stat stat = new Stat();

    public ZookeeperClient() { LOGGER.info("load ZookeeperInstance ..."); }

    public static void init(String host){
        LOGGER.info("wait zk callback ...");
        try {
            zooKeeper = new ZooKeeper(host,500000,watchedEvent -> {
                List<String> children = null;
                if(Watcher.Event.KeeperState.SyncConnected == watchedEvent.getState()){
                    if(Watcher.Event.EventType.None == watchedEvent.getType() && null == watchedEvent.getPath()){
                        connectedSemaphore.countDown();
                        try {
                            //每次都新加一个watch监听channel下节点变化
                            children = zooKeeper.getChildren(PATH, true);
                        } catch (KeeperException | InterruptedException e) {
                            e.printStackTrace();
                        }
                        if(CollectionUtils.isNotEmpty(children)){
                            LOGGER.info("add old channel size {}",children.size());
                            addRemoteNodeChannels(children);
                        }
                    }else if(Watcher.Event.EventType.NodeChildrenChanged == watchedEvent.getType()){
                        try {
                            //每次都新加一个watch监听channel下节点变化
                            children = zooKeeper.getChildren(PATH, true);
                        } catch (KeeperException | InterruptedException e) {
                            e.printStackTrace();
                        }
                        if(Objects.isNull(children)){
                            LOGGER.warn("children node is null  .." );
                            return;
                        }
                        LOGGER.info("add node changes remote size {} ..", children.size());
                        addRemoteNodeChannels(children);
                    }
                }
            });

            connectedSemaphore.await();
            LOGGER.info("zookeeper successful connected !");

            Stat rootExists = zooKeeper.exists(PATH, false);
            if(Objects.isNull(rootExists)){
                LOGGER.info("[  init create channels root  node !  ]");
                zooKeeper.create(PATH,"".getBytes(), OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
            }
            assert IpUtils.getRealIp() != null;
            Stat remoteExists = zooKeeper.exists(REMOTES_PATH, false);
            if(Objects.isNull(remoteExists)){
                LOGGER.info("[  init create remotes root node!  ]");
                zooKeeper.create(REMOTES_PATH , "".getBytes(), OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
                Thread.sleep(100);
                createZkRemotesNode();
            }else{
                Stat exists = zooKeeper.exists(REMOTES_PATH + "/" + IpUtils.getRealIp(), false);
                if(Objects.isNull(exists)){
                    createZkRemotesNode();
                }else{
                    LOGGER.warn("{} already exists !",REMOTES_PATH + "/" + IpUtils.getRealIp());
                }
            }


        } catch (Exception e) {
            LOGGER.error("zookeeper connection exception !" + e.getMessage());
        }
    }
    public static void addRemoteNodeChannels(List<String>  children){
        for (String child : children) {
            try {
                byte[] data = zooKeeper.getData(PATH+"/"+child, false ,stat);
                RemoteChannel remoteChannel = JsonUtils.get().readValue(data, RemoteChannel.class);
                List<String> label = remoteChannel.getLabel();
                // 127.0.0.0:8080
                CHANNEL.addRemoteChannel(label,child.split("#")[0] + ":" + DispatchConfig.DISPATCH_PORT);
            } catch (KeeperException | InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void createZkRemotesNode(){
        try {
            zooKeeper.create(REMOTES_PATH + "/" + IpUtils.getRealIp(), IpUtils.getRealIp().getBytes(), OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static boolean createChannel(String remoteIp, String info){
        try {
            synchronized (zooKeeper){
                zooKeeper.create(PATH + remoteIp , info.getBytes(), OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            }
            //    /channel/127.0.0.1#0
        } catch (KeeperException | InterruptedException e) {
            LOGGER.error("create remote channel error !");
            e.printStackTrace();
        }
        return true;
    }
    public static void deleteLocal(){
        try {
            zooKeeper.delete(REMOTES_PATH + "/" + IpUtils.getRealIp(),stat.getVersion());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }

    }
    public static void deleteNode(String ip) {
        try {
            zooKeeper.delete(PATH + ip,0);
            LOGGER.info("Delete remote ip : {}",ip);
        } catch (InterruptedException | KeeperException e) {
            e.printStackTrace();
        }
    }
}
