package org.jarvis.id;

import org.jarvis.date.DatePattern;
import org.jarvis.date.LocalDateTimeUtils;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class SnowflakeIdGeneratorTest {

    @Test
    void nextId() {
        SnowflakeIdGenerator idGenerator = new SnowflakeIdGenerator(0, 0);
        CopyOnWriteArraySet<Long> set = new CopyOnWriteArraySet<Long>();
        IntStream.range(0,1000).parallel().mapToLong(i -> idGenerator.nextId()).forEach(set::add);
        System.out.println(set.size());
    }

    @Test
    void currentTimestamp() {
        LocalDateTime now = LocalDateTime.of(2020, 12, 12, 0, 0, 0);
        System.out.println(LocalDateTimeUtils.format(now, DatePattern.DATETIME_FORMAT));
        System.out.println(LocalDateTimeUtils.toDate(now).getTime());
    }
}