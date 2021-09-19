package org.jarvis.security.crypto;

import lombok.SneakyThrows;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

public abstract class RSAUtils {

    private static final String CIPHER_MODE = "RSA/ECB/PKCS1Padding";

    private static final String ENCRY_ALGORITHM = "RSA";

    public static byte[] encrypt(byte[] clearTextBytes, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance(CIPHER_MODE);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(clearTextBytes);
    }

    public static byte[] encrypt(byte[] clearTextBytes, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance(CIPHER_MODE);
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return cipher.doFinal(clearTextBytes);
    }

    public static byte[] decrypt(byte[] cipherTextBytes, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance(CIPHER_MODE);
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        return cipher.doFinal(cipherTextBytes);
    }

    public static byte[] decrypt(byte[] cipherTextBytes, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance(CIPHER_MODE);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(cipherTextBytes);
    }

    public static KeyPair getKeyPair() {
        try {
            //KeyPairGenerator类用于生成公钥和密钥对，基于RSA算法生成对象
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(ENCRY_ALGORITHM);
            //初始化密钥对生成器，密钥大小为96-1024位
            keyPairGen.initialize(1024, new SecureRandom());
            //生成一个密钥对，保存在keyPair中
            return keyPairGen.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<Integer, byte[]> getKeyPairMap() {
        KeyPair keyPair = getKeyPair();
        //得到公钥
        PublicKey publicKey = keyPair.getPublic();
        //得到私钥
        PrivateKey privateKey = keyPair.getPrivate();
        //将公钥和私钥保存到Map
        Map<Integer, byte[]> keyPairMap = new HashMap<>();
        //0表示公钥
        keyPairMap.put(0, publicKey.getEncoded());
        //1表示私钥
        keyPairMap.put(1, privateKey.getEncoded());
        return keyPairMap;
    }

    @SneakyThrows
    public static PrivateKey toPrivateKey(byte[] privateKeyByteArray) {
        return KeyFactory.getInstance(ENCRY_ALGORITHM).generatePrivate(new PKCS8EncodedKeySpec(privateKeyByteArray));
    }

    @SneakyThrows
    public static PublicKey toPublicKey(byte[] publicKeyByteArray) {
        return KeyFactory.getInstance(ENCRY_ALGORITHM).generatePublic(new X509EncodedKeySpec(publicKeyByteArray));
    }

}
