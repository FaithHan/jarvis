package org.jarvis.security.crypto;

import org.jarvis.codec.Base64Utils;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class AESGCMUtilsTest {

    @Test
    void generateAESKey() {
        String text = "你好";
        byte[] bytes = AESGCMUtils.generateAESKey();
        byte[] encrypt = AESGCMUtils.encrypt(text.getBytes(StandardCharsets.UTF_8), bytes, null);
        System.out.println(Base64Utils.encode(encrypt));
        byte[] decrypt = AESGCMUtils.decrypt(encrypt, bytes, null);
        String decryptData = new String(decrypt, StandardCharsets.UTF_8);
        System.out.println(decryptData);
        assertEquals(text, decryptData);
    }
}