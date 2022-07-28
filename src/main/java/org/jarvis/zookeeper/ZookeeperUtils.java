package org.jarvis.zookeeper;


import io.swagger.models.auth.In;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.jarvis.clock.TimeUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * https://blog.csdn.net/java_66666/article/details/81015302
 */
public class ZookeeperUtils {

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        ZooKeeper zooKeeper = new ZooKeeper("localhost:2181", 5000, watchedEvent -> {});
        String s = zooKeeper.create("/hanfei", "韩斐然".getBytes(StandardCharsets.UTF_8), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println(s);

    }
}
