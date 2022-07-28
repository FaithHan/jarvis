package org.jarvis.zookeeper;

import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.jarvis.clock.TimeUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ZookeeperUtilsTest {

    @Test
    void name() throws IOException {
        ZooKeeper zooKeeper = new ZooKeeper("localhost:2181", 5000, watchedEvent -> {
            if (watchedEvent.getType() == Watcher.Event.EventType.NodeDataChanged) {
                System.out.println(watchedEvent.getPath());
                System.out.println(watchedEvent.getState());
            }
        });
        TimeUtils.sleep(Integer.MAX_VALUE);
    }
}