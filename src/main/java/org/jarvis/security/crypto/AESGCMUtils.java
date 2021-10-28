package org.jarvis.security.crypto;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * <h2>本例采用AES加密，GCM加密模式，PKCS5Padding填充</h2>
 * <p>
 * https://docs.oracle.com/javase/7/docs/api/javax/crypto/spec/GCMParameterSpec.html
 * https://github.com/patrickfav/armadillo/blob/master/armadillo/src/main/java/at/favre/lib/armadillo/AesGcmEncryption.java
 * <b>GCM模式:</b><br>
 *  * https://cloud.tencent.com/developer/article/1161339 <br>
 */
@Slf4j
public class AESGCMUtils {

    private static final String ALGORITHM = "AES";
    /**
     * 加密算法/加密模式/填充类型
     * 本例采用AES加密，GCM加密模式，PKCS5Padding填充
     */
    private static final String CIPHER_MODE = "AES/GCM/NoPadding";

    private static final int TAG_LENGTH_BIT = 128;
    private static final int IV_LENGTH_BYTE = 12;

    public static byte[] generateAESKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
            // 128 or 192 or 256
            keyGenerator.init(128);
            SecretKey secretKey = keyGenerator.generateKey();
            return secretKey.getEncoded();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    public static byte[] encrypt(byte[] rawData, byte[] rawEncryptionKey, byte[] associatedData) {
        if (rawEncryptionKey.length < 16) {
            throw new IllegalArgumentException("key length must be longer than 16 bytes");
        }
        byte[] iv = null;
        byte[] encrypted = null;
        iv = new byte[IV_LENGTH_BYTE];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(iv);

        Cipher cipherEnc = Cipher.getInstance(CIPHER_MODE);
        cipherEnc.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(rawEncryptionKey, "AES"), new GCMParameterSpec(TAG_LENGTH_BIT, iv));

        if (associatedData != null) {
            cipherEnc.updateAAD(associatedData);
        }

        encrypted = cipherEnc.doFinal(rawData);

        ByteBuffer byteBuffer = ByteBuffer.allocate(1 + iv.length + encrypted.length);
        byteBuffer.put((byte) iv.length);
        byteBuffer.put(iv);
        byteBuffer.put(encrypted);
        return byteBuffer.array();

    }

    @SneakyThrows
    public static byte[] decrypt(byte[] encryptedData, byte[] rawEncryptionKey, byte[] associatedData) {
        int initialOffset = 1;
        int ivLength = encryptedData[0];

        if (ivLength != 12 && ivLength != 16) {
            throw new IllegalStateException("Unexpected iv length");
        }

        Cipher cipherDec = Cipher.getInstance(CIPHER_MODE);
        cipherDec.init(Cipher.DECRYPT_MODE, new SecretKeySpec(rawEncryptionKey, "AES"),
                new GCMParameterSpec(TAG_LENGTH_BIT, encryptedData, initialOffset, ivLength));

        if (associatedData != null) {
            cipherDec.updateAAD(associatedData);
        }

        return cipherDec.doFinal(encryptedData, initialOffset + ivLength, encryptedData.length - (initialOffset + ivLength));
    }
}

