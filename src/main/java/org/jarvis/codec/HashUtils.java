package org.jarvis.codec;

import org.apache.commons.codec.digest.DigestUtils;
import org.jarvis.misc.HexUtils;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.jarvis.codec.HashAlgorithms.MD5;
import static org.jarvis.codec.HashAlgorithms.SHA_1;
import static org.jarvis.codec.HashAlgorithms.SHA_224;
import static org.jarvis.codec.HashAlgorithms.SHA_256;
import static org.jarvis.codec.HashAlgorithms.SHA_384;
import static org.jarvis.codec.HashAlgorithms.SHA_512;

/**
 * @see DigestUtils
 */
//https://andaily.com/blog/?p=956
public class HashUtils {

    private static final int STREAM_BUFFER_LENGTH = 2048;

    public static byte[] md5(byte[] data) {
        return getDigest(MD5).digest(data);
    }

    public static String md5Hex(byte[] data) {
        return HexUtils.toHexString(getDigest(MD5).digest(data));
    }

    public static byte[] sha1(byte[] data) {
        return getDigest(SHA_1).digest(data);
    }

    public static String sha1Hex(byte[] data) {
        return HexUtils.toHexString(getDigest(SHA_1).digest(data));
    }

    public static byte[] sha224(byte[] data) {
        return getDigest(SHA_224).digest(data);
    }

    public static String sha224Hex(byte[] data) {
        return HexUtils.toHexString(getDigest(SHA_224).digest(data));
    }

    public static byte[] sha256(byte[] data) {
        return getDigest(SHA_256).digest(data);
    }

    public static String sha256Hex(byte[] data) {
        return HexUtils.toHexString(getDigest(SHA_256).digest(data));
    }

    public static byte[] sha384(byte[] data) {
        return getDigest(SHA_384).digest(data);
    }

    public static String sha384Hex(byte[] data) {
        return HexUtils.toHexString(getDigest(SHA_384).digest(data));
    }

    public static byte[] sha512(byte[] data) {
        return getDigest(SHA_512).digest(data);
    }

    public static String sha512Hex(byte[] data) {
        return HexUtils.toHexString(getDigest(SHA_512).digest(data));
    }

    public static byte[] md5(InputStream inputStream) throws IOException{
        return updateDigest(getDigest(MD5), inputStream).digest();
    }

    public static String md5Hex(InputStream inputStream) throws IOException{
        return HexUtils.toHexString(updateDigest(getDigest(MD5), inputStream).digest());
    }

    public static byte[] sha1(InputStream inputStream) throws IOException{
        return updateDigest(getDigest(SHA_1), inputStream).digest();
    }

    public static String sha1Hex(InputStream inputStream) throws IOException{
        return HexUtils.toHexString(updateDigest(getDigest(SHA_1), inputStream).digest());
    }

    public static byte[] sha224(InputStream inputStream) throws IOException{
        return updateDigest(getDigest(SHA_224), inputStream).digest();
    }

    public static String sha224Hex(InputStream inputStream) throws IOException{
        return HexUtils.toHexString(updateDigest(getDigest(SHA_224), inputStream).digest());
    }

    public static byte[] sha256(InputStream inputStream) throws IOException{
        return updateDigest(getDigest(SHA_256), inputStream).digest();
    }

    public static String sha256Hex(InputStream inputStream) throws IOException{
        return HexUtils.toHexString(updateDigest(getDigest(SHA_256), inputStream).digest());
    }

    public static byte[] sha384(InputStream inputStream) throws IOException{
        return updateDigest(getDigest(SHA_384), inputStream).digest();
    }

    public static String sha384Hex(InputStream inputStream) throws IOException{
        return HexUtils.toHexString(updateDigest(getDigest(SHA_384), inputStream).digest());
    }

    public static byte[] sha512(InputStream inputStream) throws IOException{
        return updateDigest(getDigest(SHA_512), inputStream).digest();
    }

    public static String sha512Hex(InputStream inputStream) throws IOException{
        return HexUtils.toHexString(updateDigest(getDigest(SHA_512), inputStream).digest());
    }


    private static MessageDigest getDigest(String hashAlgorithm) {
        try {
            return MessageDigest.getInstance(hashAlgorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static MessageDigest updateDigest(final MessageDigest digest, final InputStream inputStream)
            throws IOException {
        final byte[] buffer = new byte[STREAM_BUFFER_LENGTH];
        int read = inputStream.read(buffer, 0, STREAM_BUFFER_LENGTH);

        while (read > -1) {
            digest.update(buffer, 0, read);
            read = inputStream.read(buffer, 0, STREAM_BUFFER_LENGTH);
        }

        return digest;
    }

}
