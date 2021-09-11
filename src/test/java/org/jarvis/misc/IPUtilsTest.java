package org.jarvis.misc;

import org.jarvis.network.IPUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class IPUtilsTest {

    @Test
    void isValid() {
        Assertions.assertTrue(IPUtils.isValidIp("172.24.240.161"));
    }

    @Test
    void ipToLong() {
        Assertions.assertEquals(2887315617L, IPUtils.ipToLong("172.24.240.161"));

    }

    @Test
    void ipToString() {
        Assertions.assertEquals("172.24.240.161", IPUtils.ipToString(2887315617L));

    }

    @Test
    void get_local_ip_test() {
        String localIp = IPUtils.getLocalIp();
        System.out.println(localIp);
        Assertions.assertNotEquals("127.0.0.1", localIp);
    }
}