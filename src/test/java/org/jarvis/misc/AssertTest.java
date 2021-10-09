package org.jarvis.misc;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AssertTest {

    @Test
    void isTrue() {
        Assert.isTrue(true, "not true");
    }

    @Test
    void isTrueNotValid() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> Assert.isTrue(false, "not true"));
    }
}