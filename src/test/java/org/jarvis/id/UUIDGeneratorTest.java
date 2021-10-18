package org.jarvis.id;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UUIDGeneratorTest {

    @Test
    void nextId() {
        UUIDGenerator uuidGenerator = new UUIDGenerator();
        System.out.println(uuidGenerator.nextId());
    }
}