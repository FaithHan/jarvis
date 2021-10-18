package org.jarvis.id;


import java.util.Date;

/**
 * Twitter的Snowflake 算法<br>
 * 分布式系统中，有一些需要使用全局唯一ID的场景，有些时候我们希望能使用一种简单一些的ID，并且希望ID能够按照时间有序生成。
 *
 * <p>
 * snowflake的结构如下(每部分用-分开):<br>
 *
 * <pre>
 * 符号位（1bit）- 时间戳相对值（41bit）- 数据中心标志（5bit）- 机器标志（5bit）- 递增序号（12bit）
 * 0 - 0000000000 0000000000 0000000000 0000000000 0 - 00000 - 00000 - 000000000000
 * </pre>
 * <p>
 * 第一位为未使用(符号位表示正数)，接下来的41位为毫秒级时间(41位的长度可以使用69年)<br>
 * 然后是5位datacenterId和5位workerId(10位的长度最多支持部署1024个节点）<br>
 * 最后12位是毫秒内的计数（12位的计数顺序号支持每个节点每毫秒产生4096个ID序号）
 * <p>
 * 并且可以通过生成的id反推出生成时间,datacenterId和workerId
 * <p>
 * 参考：http://www.cnblogs.com/relucent/p/4955340.html<br>
 * 关于长度是18还是19的问题见：https://blog.csdn.net/unifirst/article/details/80408050
 */
public class SnowflakeIdGenerator implements IdGenerator<Long> {

    /**
     * 默认的起始时间: 2020-12-12 00:00:00
     */
    private static final long DEFAULT_TWEPOCH = 1607702400000L;

    /**
     * 默认回拨时间，1.5S
     */
    private static final long DEFAULT_TIME_OFFSET = 1500L;

    private static final long WORKER_ID_BITS = 5L;

    // 最大支持机器节点数0~31，一共32个
    @SuppressWarnings({"PointlessBitwiseExpression", "FieldCanBeLocal"})
    private static final long MAX_WORKER_ID = -1L ^ (-1L << WORKER_ID_BITS);

    private static final long DATA_CENTER_ID_BITS = 5L;

    // 最大支持数据中心节点数0~31，一共32个
    @SuppressWarnings({"PointlessBitwiseExpression", "FieldCanBeLocal"})
    private static final long MAX_DATA_CENTER_ID = -1L ^ (-1L << DATA_CENTER_ID_BITS);

    // 序列号12位（表示只允许workId的范围为：0-4095）
    private static final long SEQUENCE_BITS = 12L;

    // 机器节点左移12位
    private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;

    // 数据中心节点左移17位
    private static final long DATA_CENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;

    // 时间毫秒数左移22位
    private static final long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATA_CENTER_ID_BITS;

    // 序列掩码，用于限定序列最大值不能超过4095
    @SuppressWarnings("FieldCanBeLocal")
    private static final long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);// 4095

    private static final Clock DEFAULT_CLOCK = new SystemClock();

    private final long twepoch;

    private final long workerId;

    private final long dataCenterId;

    // 允许的时钟回拨数
    private final long timeOffset;

    private final Clock clock;

    private long sequence = 0L;

    private long lastTimestamp;


    /**
     * 构造
     */
    public SnowflakeIdGenerator() {
        this(0L, 0L);
    }

    /**
     * 构造
     *
     * @param workerId     终端ID
     * @param dataCenterId 数据中心ID
     */
    public SnowflakeIdGenerator(long workerId, long dataCenterId) {
        this(null, workerId, dataCenterId);
    }

    /**
     * @param epochDate    初始化时间起点（null表示默认起始日期）,后期修改会导致id重复,如果要修改连workerId dataCenterId，慎用
     * @param workerId     工作机器节点id
     * @param dataCenterId 数据中心id
     * @since 5.1.3
     */
    public SnowflakeIdGenerator(Date epochDate, long workerId, long dataCenterId) {
        this(epochDate, workerId, dataCenterId, DEFAULT_TIME_OFFSET, DEFAULT_CLOCK);
    }

    /**
     * @param epochDate    初始化时间起点（null表示默认起始日期）,后期修改会导致id重复,如果要修改连workerId dataCenterId，慎用
     * @param workerId     工作机器节点id
     * @param dataCenterId 数据中心id
     * @param timeOffset   允许时间回拨的毫秒数
     * @param clock        读取时间戳的时钟
     * @since 5.7.3
     */
    public SnowflakeIdGenerator(Date epochDate, long workerId, long dataCenterId, long timeOffset, Clock clock) {
        this.lastTimestamp = this.twepoch = epochDate != null ? epochDate.getTime() : DEFAULT_TWEPOCH;
        if (workerId > MAX_WORKER_ID || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %s or less than 0", MAX_WORKER_ID));
        }
        if (dataCenterId > MAX_DATA_CENTER_ID || dataCenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %s or less than 0", MAX_DATA_CENTER_ID));
        }
        this.workerId = workerId;
        this.dataCenterId = dataCenterId;
        this.timeOffset = timeOffset;
        this.clock = clock;
    }

    /**
     * 根据Snowflake的ID，获取机器id
     *
     * @param id snowflake算法生成的id
     * @return 所属机器的id
     */
    public long getWorkerId(long id) {
        return id >> WORKER_ID_SHIFT & ~(-1L << WORKER_ID_BITS);
    }

    /**
     * 根据Snowflake的ID，获取数据中心id
     *
     * @param id snowflake算法生成的id
     * @return 所属数据中心
     */
    public long getDataCenterId(long id) {
        return id >> DATA_CENTER_ID_SHIFT & ~(-1L << DATA_CENTER_ID_BITS);
    }

    /**
     * 根据Snowflake的ID，获取生成时间
     *
     * @param id snowflake算法生成的id
     * @return 生成的时间
     */
    public long getGenerateDateTime(long id) {
        return (id >> TIMESTAMP_LEFT_SHIFT & ~(-1L << 41L)) + twepoch;
    }

    /**
     * 下一个ID
     *
     * @return ID
     */
    public synchronized Long nextId() {
        long timestamp = currentTimestamp();
        if (timestamp < this.lastTimestamp) {
            if (this.lastTimestamp - timestamp < timeOffset) {
                // 容忍指定的回拨，避免NTP校时造成的异常
                timestamp = lastTimestamp;
            } else {
                // 如果服务器时间有问题(时钟后退) 报错。
                throw new IllegalStateException(String.format("Clock moved backwards. Refusing to generate id for %s ms", lastTimestamp - timestamp));
            }
        }

        if (timestamp == this.lastTimestamp) {
            final long sequence = (this.sequence + 1) & SEQUENCE_MASK;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
            this.sequence = sequence;
        } else {
            sequence = 0L;
        }

        lastTimestamp = timestamp;

        return ((timestamp - twepoch) << TIMESTAMP_LEFT_SHIFT)
                | (dataCenterId << DATA_CENTER_ID_SHIFT)
                | (workerId << WORKER_ID_SHIFT)
                | sequence;
    }


    /*  private method start  */

    /**
     * 循环等待下一个时间
     *
     * @param lastTimestamp 上次记录的时间
     * @return 下一个时间
     */
    private long tilNextMillis(long lastTimestamp) {
        long timestamp = currentTimestamp();
        // 循环直到操作系统时间戳变化
        while (timestamp == lastTimestamp) {
            timestamp = currentTimestamp();
        }
        if (timestamp < lastTimestamp) {
            // 如果发现新的时间戳比上次记录的时间戳数值小，说明操作系统时间发生了倒退，报错
            throw new IllegalStateException(
                    String.format("Clock moved backwards. Refusing to generate id for %s ms", lastTimestamp - timestamp));
        }
        return timestamp;
    }

    /**
     * 生成时间戳
     *
     * @return 时间戳
     */
    private long currentTimestamp() {
        return clock.now();
    }

    /*  clock to read current timestapm  */
    interface Clock {

        /**
         * 单位 millisecond
         *
         * @return 当前时间戳
         */
        long now();

    }

    static class SystemClock implements Clock {
        @Override
        public long now() {
            return System.currentTimeMillis();
        }
    }


}
