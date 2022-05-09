package org.jarvis.id;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jarvis.json.JsonUtils;
import org.jarvis.misc.Assert;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static java.sql.Connection.TRANSACTION_READ_COMMITTED;

/**
 * 分布式自增ID(基于号段模式)
 * <p>
 * CREATE TABLE id_generator
 * (
 * id       int unsigned PRIMARY KEY AUTO_INCREMENT comment '主键',
 * max_id   bigint unsigned NOT NULL COMMENT '当前最大id',
 * biz_type varchar(32)     NOT NULL COMMENT '业务类型',
 * version  bigint unsigned NOT NULL COMMENT '版本号',
 * unique key biz_type_unique_index (biz_type)
 * );
 */
@Slf4j
public class SegmentIdGenerator implements IdGenerator<Long> {

    private final DataSource dataSource;

    private final String bizType;

    private final ArrayBlockingQueue<Long> idQueue;

    private final int threshold;

    private final boolean asyncUpdate;

    private final Semaphore semaphore = new Semaphore(1);
    private final boolean useDbLock;

    public SegmentIdGenerator(DataSource dataSource, String bizType, int capacity) {
        this(dataSource, bizType, capacity, capacity / 4, true);
    }

    public SegmentIdGenerator(DataSource dataSource,
                              String bizType,
                              int capacity,
                              int threshold,
                              boolean asyncUpdate) {
        this(dataSource, bizType, capacity, threshold, asyncUpdate, true);
    }

    public SegmentIdGenerator(DataSource dataSource,
                              String bizType,
                              int capacity,
                              int threshold,
                              boolean asyncUpdate,
                              boolean useDbLock) {

        Assert.notNull(dataSource, "dataSource can not be null");
        Assert.isTrue(bizType != null && bizType.trim().length() > 0, "bizType can not be blank");
        Assert.isTrue(capacity > 0, "capacity must be positive");
        if (asyncUpdate) {
            Assert.isTrue(threshold < capacity, "threshold must less than capacity");
            Assert.isTrue(threshold > 0, "threshold must be positive");
        }

        this.dataSource = dataSource;
        this.bizType = bizType;
        this.idQueue = new ArrayBlockingQueue<>(capacity);
        this.threshold = threshold;
        this.asyncUpdate = asyncUpdate;
        this.useDbLock = useDbLock;
    }


    @Override
    public synchronized Long nextId() {
        Long nextId = idQueue.poll();
        if (nextId != null) {
            if (this.asyncUpdate && shouldFeed()) {
                new Thread(this::feed).start();
            }
        } else {
            feed();
            nextId = idQueue.poll();
        }
        return nextId;
    }

    private boolean shouldFeed() {
        return this.idQueue.size() <= this.threshold;
    }

    private void feed() {
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            return;
        }
        if (!shouldFeed()) {
            semaphore.release();
            return;
        }
        Connection connection = null;
        String querySql = "select id, max_id, biz_type, version from id_generator where biz_type = ?" + (useDbLock ? " for update" : StringUtils.EMPTY);
        String updateSql = "update id_generator set max_id = ?, version = version + 1 where version = ? and biz_type = ?";
        try {
            boolean result = false;
            long maxId = 0;
            long step = 0;
            while (!result) {
                connection = dataSource.getConnection();
                connection.setAutoCommit(false);
                connection.setTransactionIsolation(TRANSACTION_READ_COMMITTED);
                PreparedStatement preparedStatement = connection.prepareStatement(querySql);
                preparedStatement.setString(1, this.bizType);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (!resultSet.next()) {
                    throw new IllegalStateException("This business type has not been configured yet");
                }
                maxId = resultSet.getLong(2);
                String bizType = resultSet.getString(3);
                long version = resultSet.getLong(4);
                preparedStatement = connection.prepareStatement(updateSql);
                step = idQueue.remainingCapacity();
                preparedStatement.setLong(1, maxId + step);
                preparedStatement.setLong(2, version);
                preparedStatement.setString(3, bizType);
                result = preparedStatement.executeUpdate() > 0;
                connection.commit();
            }
            List<Long> idList = LongStream.range(maxId, maxId + step).boxed().collect(Collectors.toList());
            log.info("fetch id list {}", JsonUtils.toJson(idList));
            idQueue.addAll(idList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ignored) {

                }
            }
            this.semaphore.release();
        }
    }

}
