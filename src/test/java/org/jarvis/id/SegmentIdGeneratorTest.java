package org.jarvis.id;

import org.jarvis.jdbc.JDBCUtils;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

class SegmentIdGeneratorTest {

    @Test
    void nextId() {
        DataSource datasource = JDBCUtils.getHikariDatasource("jdbc:mysql://127.0.0.1:3306/test", "root", "123456");
        SegmentIdGenerator idGenerator1 = new SegmentIdGenerator(datasource, "test",10);
        SegmentIdGenerator idGenerator2 = new SegmentIdGenerator(datasource, "test",10);
        SegmentIdGenerator idGenerator3 = new SegmentIdGenerator(datasource, "test",10);
        SegmentIdGenerator idGenerator4 = new SegmentIdGenerator(datasource, "test",10);
        SegmentIdGenerator idGenerator5 = new SegmentIdGenerator(datasource, "test",10);

        List<SegmentIdGenerator> incrementIdGenerators = Arrays.asList(
                idGenerator1,
                idGenerator2,
                idGenerator3,
                idGenerator4,
                idGenerator5
        );
        IntStream.range(0,100).parallel().forEach(i -> System.out.println(incrementIdGenerators.get(i % 5).nextId()));
    }
}