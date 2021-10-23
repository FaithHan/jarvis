package org.jarvis.security.crypto;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DESUtilsTest {


    @Test
    void encrypt() {
        byte[] bytes = DESUtils.generateDESKey();
        byte[] cipherTextBytes = DESUtils.encrypt("你好".getBytes(StandardCharsets.UTF_8), bytes);
        byte[] clearTextBytes = DESUtils.decrypt(cipherTextBytes, bytes);
        String clearText = new String(clearTextBytes, StandardCharsets.UTF_8);
        assertEquals("你好",clearText);
    }
}