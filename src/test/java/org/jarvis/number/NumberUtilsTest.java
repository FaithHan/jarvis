package org.jarvis.number;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NumberUtilsTest {

    @Test
    void min() {
        double min = NumberUtils.min(1, -2, 3);
        assertEquals(-2, min);
    }
}