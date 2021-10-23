package org.jarvis.security.crypto;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;

@Slf4j
public abstract class DESUtils {

    private static final String ALGORITHM = "DES";
    /**
     * 加密算法/加密模式/填充类型
     * 本例采用DES加密，ECB加密模式，PKCS5Padding填充
     */
    private static final String CIPHER_MODE = "DES/ECB/PKCS5Padding";

    /**
     * 密钥为 8 Bytes
     * @return
     */
    public static byte[] generateAESKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
            keyGenerator.init(56);
            SecretKey secretKey = keyGenerator.generateKey();
            return secretKey.getEncoded();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] encrypt(byte[] clearTextBytes, byte[] keyBytes) {
        try {
            // 1 获取加密密钥
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, ALGORITHM);
            // 2 获取Cipher实例
            Cipher cipher = Cipher.getInstance(CIPHER_MODE);
            // 3 初始化Cipher实例。设置执行模式以及加密密钥
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            // 4 执行并返回密文字符集
            return cipher.doFinal(clearTextBytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static byte[] decrypt(byte[] cipherTextBytes, byte[] keyBytes) {
        try {
            // 1 获取解密密钥
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, ALGORITHM);

            // 2 获取Cipher实例
            Cipher cipher = Cipher.getInstance(CIPHER_MODE);

            // 3 初始化Cipher实例。设置执行模式以及加密密钥
            cipher.init(Cipher.DECRYPT_MODE, keySpec);

            //4 执行并返回明文字符集
            return cipher.doFinal(cipherTextBytes);

        } catch (Exception e) {
            log.error("DES解密错误", e);
        }
        return null;
    }
}
