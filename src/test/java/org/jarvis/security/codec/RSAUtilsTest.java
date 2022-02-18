package org.jarvis.security.codec;

import org.jarvis.codec.Base64Utils;
import org.jarvis.security.crypto.RSAUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.util.Map;

class RSAUtilsTest {

    @Test
    void key_gen_test() {
        Map<Integer, byte[]> keyPairMap = RSAUtils.genKeyPairMap(1024);
        byte[] publicKeyByteArray = keyPairMap.get(0);
        byte[] privateKeyByteArray = keyPairMap.get(1);
        System.out.println("publicKey:"+ Base64Utils.encode(publicKeyByteArray));
        System.out.println("privateKey:"+ Base64Utils.encode(privateKeyByteArray));
    }

    @Test
    void public_encrypt_private_decrypt() throws Exception {
        Map<Integer, byte[]> keyPairMap = RSAUtils.genKeyPairMap();
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
        KeyPair keyPairMap = RSAUtils.genKeyPair();
        String text = "测试字符串";
        byte[] encrypt = RSAUtils.encrypt(text.getBytes(StandardCharsets.UTF_8), keyPairMap.getPrivate());
        byte[] decrypt = RSAUtils.decrypt(encrypt, keyPairMap.getPublic());
        String result = new String(decrypt, StandardCharsets.UTF_8);
        Assertions.assertEquals(text, result);
    }
}