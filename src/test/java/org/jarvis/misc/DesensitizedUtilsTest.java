package org.jarvis.misc;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DesensitizedUtilsTest {

    @Test
    void mobilePhone() {
        Assertions.assertEquals("139****6938", DesensitizedUtils.mobilePhone("13920266938"));
    }

    @Test
    void password() {
        Assertions.assertEquals("", DesensitizedUtils.password(""));
        Assertions.assertEquals("***", DesensitizedUtils.password("123"));
        Assertions.assertEquals("*******", DesensitizedUtils.password("123hdds"));
    }
}