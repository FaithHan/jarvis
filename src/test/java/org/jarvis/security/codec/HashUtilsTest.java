package org.jarvis.security.codec;

import org.jarvis.codec.HexUtils;
import org.jarvis.security.crypto.digest.HashUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

class HashUtilsTest {

    @Test
    void md5() {
    }

    @Test
    void md5Hex() {
        byte[] bytes = HashUtils.md5("123".getBytes(StandardCharsets.UTF_8));
        String md5Hex = HashUtils.md5Hex("123".getBytes(StandardCharsets.UTF_8));
        System.out.println(md5Hex);
        String hexString = HexUtils.toHexString(bytes);
        System.out.println(hexString);
        Assertions.assertEquals(hexString, md5Hex);
    }
}