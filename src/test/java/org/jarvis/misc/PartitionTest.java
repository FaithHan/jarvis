package org.jarvis.misc;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class PartitionTest {

    @Test
    void test() {
        List<Integer> collect = IntStream.rangeClosed(1, 105).boxed().collect(Collectors.toList());
        Partition<Integer> partition = Partition.create(collect, 10);
        for (List<Integer> integers : partition) {
            System.out.println(integers);
        }
        assertEquals(11, partition.size());
    }
}