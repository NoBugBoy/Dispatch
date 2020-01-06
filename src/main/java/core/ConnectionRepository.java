package core;

import beans.RemoteChannel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Sets;
import config.DispatchConfig;
import io.netty.channel.Channel;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.IncrUtils;
import utils.IpUtils;
import utils.JsonUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 连接仓库
 * @author yujian
 */

public class ConnectionRepository extends DefaultChannelGroup{
    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionRepository.class);
    private  Integer number = 0 ;
    private final static ConnectionRepository CONNECTION_REPOSITORY = new ConnectionRepository();
    //标识label和channel
    private  Map<String,Set<Channel>> labelMaps = new ConcurrentHashMap<>();
    //远程节点
    private  Map<String,Set<String>> remoteMaps = new ConcurrentHashMap<>();
    //远程待删除节点
    private  Map<Channel,String> deleteNodes = new ConcurrentHashMap<>();
    //响应map
    private  Map<String,String> resultMap = new ConcurrentHashMap<>();
    private  final Random r = new Random();
    public static ConnectionRepository get(){
        return CONNECTION_REPOSITORY;
    }
    public ConnectionRepository() {
        super(GlobalEventExecutor.INSTANCE);
    }
    public  String addJob(){
            String s = UUID.randomUUID().toString().replaceAll("-","");
            LOGGER.info("add task for id :" + s);
            resultMap.put(s,"");
            return s;
    }
    public Integer keys(){
        return resultMap.keySet().size();
    }
    public Integer values(){
        int size = 0;
        for (Set<Channel> value : labelMaps.values()) {
            size += value.size();
        }
        return size;
    }
    public  boolean checkJob(String uuid){
        String s = resultMap.get(uuid);
        return "".equals(s);
    }
    public  String getJob(String uuid){
        return resultMap.get(uuid);
    }
    public void asyncJob(String uuid,String msg){
        resultMap.put(uuid,msg);
    }
    public synchronized void removeJob(String uuid){
        boolean b = resultMap.containsKey(uuid);
        if(b){
            LOGGER.info("remove task for id :{}",uuid);
            resultMap.remove(uuid);
        }
    }
    public synchronized String getRemoteChannelByLabel(String label){
        Set<String> remoteIps = remoteMaps.get(label);
        if(CollectionUtils.isEmpty(remoteIps)){
            return null;
        }else{
            int i = r.nextInt(remoteIps.size());
            return (String)remoteIps.toArray()[i];
        }
    }
    public synchronized void addRemoteChannel(List<String> labels,String ip){
        for (String label : labels) {
            Set<String> ips = remoteMaps.get(label.toUpperCase());
            if(CollectionUtils.isNotEmpty(ips)){
                ips.add(ip);
            }else{
                ips = Sets.newHashSet();
                ips.add(ip);
                remoteMaps.put(label.toUpperCase(),ips);
            }
        }
    }
    /**
     *
     * @param labels 多个职能
     * @param channel 通道
     */
    public synchronized boolean addChannel(List<String> labels,Channel channel){
        try {
            if(DispatchConfig.ZOOKEEPER_ENABLE){
                //将自己所拥有的节点 暴露出去
                RemoteChannel remoteChannel = RemoteChannel
                        .builder()
                        .ip(IpUtils.getRealIp())
                        .label(labels)
                        .build();
                boolean zoo_add ;
                String incrIp = IpUtils.getRealIp() + IncrUtils.incr();
                zoo_add = ZookeeperClient.createChannel("/" + incrIp, JsonUtils.get().writeValueAsString(remoteChannel));
                if(!zoo_add){
                    return false;
                }
                deleteNodes.put(channel,incrIp);

            }
            add(channel);
            LOGGER.info("new channel join labels:{}", JsonUtils.get().writeValueAsString(labels));
        } catch (JsonProcessingException e) {
            LOGGER.error(e.getMessage());
        }
        for (String label : labels) {
            Set<Channel> channels = labelMaps.get(label.toUpperCase());
            if(CollectionUtils.isNotEmpty(channels)){
                channels.add(channel);
            }else{
                channels = Sets.newHashSet();
                channels.add(channel);
                labelMaps.put(label.toUpperCase(),channels);
            }
        }
        return true;
    }
    public Channel randomChannel(Set<Channel> channels){
        int i = r.nextInt(channels.size());
        return (Channel)channels.toArray()[i];
    }
    //本地获取channel如果没有去找暴露的节点上是否有
    public  Channel switchChannel(String label,boolean jobWeight){
        Set<Channel> channels = labelMaps.get(label.toUpperCase());
        if(CollectionUtils.isNotEmpty(channels)){
            if(jobWeight){
                synchronized(number){
                    if(number > channels.size() -1 ){
                        number = 0;
                    }
                    Channel channel = (Channel) channels.toArray()[number];
                    if(channel == null){
                        number = 0;
                        return  randomChannel(channels);
                    }
                    number++;
                    return channel;
                }
            }else{
                //非公平
                return  randomChannel(channels);
            }
        }else{
            LOGGER.warn("====== The label not found {} ======",label);
            return null;
        }
    }
    @Override
    public boolean add(Channel channel) {
        return super.add(channel);
    }
    @Override
    public boolean remove(Object o) {
        synchronized (labelMaps){
            Iterator<Set<Channel>> iterator = labelMaps.values().iterator();
            while(iterator.hasNext()){
                Set<Channel> next = iterator.next();
                Iterator<Channel> innerSet = next.iterator();
                while(innerSet.hasNext()){
                    Channel ch = innerSet.next();
                    if (ch.equals(o)) {
                        innerSet.remove();
                        if(DispatchConfig.ZOOKEEPER_ENABLE){
                            ZookeeperClient.deleteNode("/" + deleteNodes.get(ch));
                        }
                    }
                }
            }
        }
        return super.remove(o);
    }

}
