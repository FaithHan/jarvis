package org.jarvis.misc;

import java.util.regex.Pattern;

public abstract class HexUtils {

    private static final String HEX_STRING = "0123456789abcdef";

    private static final char[] HEX_CHAR_ARRAY = HEX_STRING.toCharArray();

    private static final Pattern HEX_STRING_PATTERN = Pattern.compile("([0-9A-Fa-f]{2})*");

    /**
     * 小写16进制字符串编码
     *
     * @param bytes
     * @return
     */
    public static String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length >> 1);
        for (byte byteValue : bytes) {
            int intValue = byteValue & 0xFF;
            sb.append(HEX_CHAR_ARRAY[intValue >>> 4]);
            sb.append(HEX_CHAR_ARRAY[intValue & 0xF]);
        }
        return sb.toString();
    }

    /**
     * 支持大写小写hex字符串转bytes
     *
     * @param hexString
     * @return
     */
    public static byte[] toBytes(String hexString) {
        hexString = hexString.toLowerCase();
        int length = hexString.length() / 2;
        byte[] bytes = new byte[length];
        for (int i = 0; i < length; i++) {
            int index = i << 1;
            bytes[i] = (byte) ((HEX_STRING.indexOf(hexString.charAt(index)) << 4) + HEX_STRING.indexOf(hexString.charAt(index + 1)));
        }
        return bytes;
    }

    /**
     * 判断hexString合法性
     *
     * @param hex
     * @return
     */
    public static boolean isValidHexString(String hex) {
        return hex != null && HEX_STRING_PATTERN.matcher(hex).matches();
    }

}
