package org.jarvis.security.crypto;

import lombok.SneakyThrows;
import org.jarvis.misc.Assert;

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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <h2>本例采用RSA加密，ECB加密模式，PKCS1Padding填充</h2>
 */
public abstract class RSAUtils {

    private static final String CIPHER_MODE = "RSA/ECB/PKCS1Padding";

    private static final String ALGORITHM = "RSA";

    private static final List<Integer> VALID_KEY_LENGTH = Arrays.asList(1024, 2048, 3072, 4096);

    private static final int DEFAULT_KEY_LENGTH = 2048;

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

    public static KeyPair genKeyPair() {
        return genKeyPair(DEFAULT_KEY_LENGTH);
    }

    public static KeyPair genKeyPair(int keyLength) {
        Assert.isTrue(VALID_KEY_LENGTH.contains(keyLength),
                String.format("%d is not a valid RSA key length, valid is %s", keyLength, VALID_KEY_LENGTH));
        try {
            //KeyPairGenerator类用于生成公钥和密钥对，基于RSA算法生成对象
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(ALGORITHM);
            //初始化密钥对生成器(1024,2048,3072,4096)
            keyPairGen.initialize(keyLength, new SecureRandom());
            //生成一个密钥对，保存在keyPair中
            return keyPairGen.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<Integer, byte[]> genKeyPairMap() {
        return genKeyPairMap(DEFAULT_KEY_LENGTH);
    }

    public static Map<Integer, byte[]> genKeyPairMap(int keyLength) {
        KeyPair keyPair = genKeyPair(keyLength);
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
        return KeyFactory.getInstance(ALGORITHM).generatePrivate(new PKCS8EncodedKeySpec(privateKeyByteArray));
    }

    @SneakyThrows
    public static PublicKey toPublicKey(byte[] publicKeyByteArray) {
        return KeyFactory.getInstance(ALGORITHM).generatePublic(new X509EncodedKeySpec(publicKeyByteArray));
    }

}
