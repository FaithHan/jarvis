package org.jarvis.misc;

import org.junit.jupiter.api.Test;

class RandomUtilsTest {
    @Test
    void randomNumberCode() {
        System.out.println(RandomUtils.randomNumberCode(6));
        System.out.println(RandomUtils.randomAlphabet(6));
        System.out.println(RandomUtils.randomPassword(6));
    }
}