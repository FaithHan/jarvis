package org.jarvis.id;

import javax.sql.DataSource;

/**
 * 分布式自增ID
 *
 * CREATE TABLE id_generator
 * (
 * id       int unsigned PRIMARY KEY AUTO_INCREMENT comment '主键',
 * max_id   bigint unsigned NOT NULL COMMENT '当前最大id',
 * biz_type varchar(32)     NOT NULL COMMENT '业务类型',
 * version  bigint unsigned NOT NULL COMMENT '版本号'
 * );
 *
 */
public class IncrementIdGenerator implements IdGenerator<Long> {

    private final SegmentIdGenerator segmentIdGenerator;

    public IncrementIdGenerator(DataSource dataSource, String bizType) {
        this.segmentIdGenerator =
                new SegmentIdGenerator(dataSource, bizType, 1, 0, false);
    }

    @Override
    public Long nextId() {
        return segmentIdGenerator.nextId();
    }

}
