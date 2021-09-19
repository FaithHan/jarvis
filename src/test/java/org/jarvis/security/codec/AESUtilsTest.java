package org.jarvis.security.codec;

import org.jarvis.security.crypto.AESUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AESUtilsTest {

    @Test
    void generateAESKey() {
        byte[] bytes = AESUtils.generateAESKey();
        assertEquals(16, bytes.length);
    }

    @Test
    void encrypt_decrypt_test() {
        byte[] bytes = AESUtils.generateAESKey();
        byte[] cipherTextBytes = AESUtils.encrypt("你好".getBytes(StandardCharsets.UTF_8), bytes);
        byte[] clearTextBytes = AESUtils.decrypt(cipherTextBytes, bytes);
        String clearText = new String(clearTextBytes, StandardCharsets.UTF_8);
        Assertions.assertEquals("你好",clearText);

    }

}