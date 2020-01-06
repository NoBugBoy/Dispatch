import config.DispatchConfig;
import core.ZookeeperClient;

public class ZookeeperTest {
    public static void main(String[] args) {
        ZookeeperClient.init(DispatchConfig.ZOOKEEPER_HOST);

    }
}
