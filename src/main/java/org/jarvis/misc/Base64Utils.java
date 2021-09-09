package org.jarvis.misc;

import java.util.Base64;

public abstract class Base64Utils {
    /*加密*/
    public static String encode(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }


    /*解密*/
    public static byte[] decode(String base64String) {
        return Base64.getDecoder().decode(base64String);
    }
}
