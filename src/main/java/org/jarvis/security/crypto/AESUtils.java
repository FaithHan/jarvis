package org.jarvis.security.crypto;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;

/**
 * <h2>本例采用AES加密，ECB加密模式，PKCS5Padding填充</h2>
 * <br>
 * <b>BouncyCastle:</b><br>
 * https://segmentfault.com/a/1190000023445325 <br>
 * <b>GCM模式:</b><br>
 * https://cloud.tencent.com/developer/article/1161339 <br>
 * <b>随机序列固定:</b><br>
 * SecureRandom random = SecureRandom.getInstance("SHA1PRNG");<br>
 */
@Slf4j
public abstract class AESUtils {

    private static final String ENCRY_ALGORITHM = "AES";
    /**
     * 加密算法/加密模式/填充类型
     * 本例采用AES加密，ECB加密模式，PKCS5Padding填充
     */
    private static final String CIPHER_MODE = "AES/ECB/PKCS5Padding";

    public static byte[] generateAESKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(ENCRY_ALGORITHM);
            // 128 or 192 or 256
            keyGenerator.init(128);
            SecretKey secretKey = keyGenerator.generateKey();
            return secretKey.getEncoded();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] encrypt(byte[] clearTextBytes, byte[] keyBytes) {
        try {
            // 1 获取加密密钥
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, ENCRY_ALGORITHM);
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
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, ENCRY_ALGORITHM);

            // 2 获取Cipher实例
            Cipher cipher = Cipher.getInstance(CIPHER_MODE);

            // 3 初始化Cipher实例。设置执行模式以及加密密钥
            cipher.init(Cipher.DECRYPT_MODE, keySpec);

            //4 执行并返回明文字符集
            return cipher.doFinal(cipherTextBytes);

        } catch (Exception e) {
            log.error("AES解密错误", e);
        }
        return null;
    }
}
