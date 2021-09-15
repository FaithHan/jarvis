package org.jarvis.codec;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.util.Map;

class RSAUtilsTest {

    @Test
    void public_encrypt_private_decrypt() throws Exception {
        Map<Integer, byte[]> keyPairMap = RSAUtils.getKeyPairMap();
        byte[] publicKeyByteArray = keyPairMap.get(0);
        byte[] privateKeyByteArray = keyPairMap.get(1);
        String text = "测试字符串";
        byte[] encrypt = RSAUtils.encrypt(text.getBytes(StandardCharsets.UTF_8), RSAUtils.toPublicKey(publicKeyByteArray));
        byte[] decrypt = RSAUtils.decrypt(encrypt, RSAUtils.toPrivateKey(privateKeyByteArray));
        String result = new String(decrypt, StandardCharsets.UTF_8);
        Assertions.assertEquals(text, result);
    }

    @Test
    void private_encrypt_public_decrypt() throws Exception {
        KeyPair keyPairMap = RSAUtils.getKeyPair();
        String text = "测试字符串";
        byte[] encrypt = RSAUtils.encrypt(text.getBytes(StandardCharsets.UTF_8), keyPairMap.getPrivate());
        byte[] decrypt = RSAUtils.decrypt(encrypt, keyPairMap.getPublic());
        String result = new String(decrypt, StandardCharsets.UTF_8);
        Assertions.assertEquals(text, result);
    }
}