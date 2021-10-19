package org.jarvis.security.crypto.mac;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

class HMACProviderTest {

    @Test
    void hmacHex() {
        HMACProvider hmacProvider = new HMACProvider(HmacAlgorithm.HMAC_SHA_256, "nihao");
        String result1 = hmacProvider.hmacHex("百度网讯技术有限公司".getBytes(StandardCharsets.UTF_8));
        String result2 = hmacProvider.hmacHex("百度网讯技术有限公司".getBytes(StandardCharsets.UTF_8));
        Assertions.assertEquals(result1, result2);
    }
}