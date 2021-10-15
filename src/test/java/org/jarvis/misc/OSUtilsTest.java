package org.jarvis.misc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OSUtilsTest {

    @Test
    void name() {
        System.out.println(OSUtils.OS_NAME);
        System.out.println(OSUtils.OS_VERSION);
    }

    @Test
    void browse() {
        OSUtils.browse("www.google.com");
    }
}