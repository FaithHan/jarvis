package org.jarvis.misc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NumberUtilsTest {

    @Test
    void min() {
        double min = NumberUtils.min(1, -2, 3);
        assertEquals(-2, min);
    }
}