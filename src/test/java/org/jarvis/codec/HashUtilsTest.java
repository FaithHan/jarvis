package org.jarvis.codec;

import org.jarvis.misc.HexUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

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