package org.jarvis.network;

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
        long actual = IPUtils.ipToLong("255.255.255.255");
        System.out.println(actual);
        Assertions.assertEquals(4294967295L, actual);
    }

    @Test
    void ipToIntTest() {
        int ip1 = IPUtils.ipToInt("172.24.240.161");
        int ip2 = IPUtils.ipToInt("189.210.140.255");
        int ip3 = IPUtils.ipToInt("255.255.255.255");

        System.out.println(ip1);
        System.out.println(ip2);
        System.out.println(ip3);

        Assertions.assertEquals("172.24.240.161", IPUtils.ipToString(ip1));
        Assertions.assertEquals("189.210.140.255", IPUtils.ipToString(ip2));
        Assertions.assertEquals("255.255.255.255", IPUtils.ipToString(ip3));
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