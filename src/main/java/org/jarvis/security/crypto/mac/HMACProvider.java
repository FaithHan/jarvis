package org.jarvis.security.crypto.mac;

import org.apache.commons.codec.binary.StringUtils;
import org.jarvis.codec.HexUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * 消息认证码工具类
 */
public class HMACProvider {

    private static final int STREAM_BUFFER_LENGTH = 1024;

    private final Mac mac;

    public HMACProvider(HmacAlgorithm algorithm, String key) {
        this(algorithm.getName(), StringUtils.getBytesUtf8(key));
    }

    public HMACProvider(HmacAlgorithm algorithm, byte[] key) {
        this(algorithm.getName(), key);
    }

    public HMACProvider(String algorithm, byte[] key) {
        this(getInitializedMac(algorithm, key));
    }

    private HMACProvider(Mac mac) {
        this.mac = mac;
    }

    public byte[] hmac(byte[] valueToDigest) {
        return mac.doFinal(valueToDigest);
    }

    public byte[] hmac(InputStream valueToDigest) throws IOException {
        byte[] buffer = new byte[STREAM_BUFFER_LENGTH];
        int read;

        while ((read = valueToDigest.read(buffer, 0, STREAM_BUFFER_LENGTH)) > -1) {
            mac.update(buffer, 0, read);
        }
        return mac.doFinal();
    }

    public String hmacHex(byte[] valueToDigest) {
        return HexUtils.toHexString(hmac(valueToDigest));
    }

    public String hmacHex(InputStream valueToDigest) throws IOException {
        return HexUtils.toHexString(hmac(valueToDigest));
    }

    private static Mac getInitializedMac(String algorithm, byte[] key) {

        if (key == null) {
            throw new IllegalArgumentException("Null key");
        }

        try {
            SecretKeySpec keySpec = new SecretKeySpec(key, algorithm);
            Mac mac = Mac.getInstance(algorithm);
            mac.init(keySpec);
            return mac;
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        } catch (InvalidKeyException e) {
            throw new IllegalArgumentException(e);
        }
    }

}
