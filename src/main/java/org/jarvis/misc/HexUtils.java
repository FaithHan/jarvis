package org.jarvis.misc;

public abstract class HexUtils {

    private static final String HEX_STRING = "0123456789ABCDEF";

    private static final char[] HEX_CHAR_ARRAY = HEX_STRING.toCharArray();

    public static String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length >> 1);
        for (byte byteValue : bytes) {
            int intValue = byteValue & 0xFF;
            sb.append(HEX_CHAR_ARRAY[intValue >>> 4]);
            sb.append(HEX_CHAR_ARRAY[intValue & 0xF]);
        }
        return sb.toString();
    }

    public static byte[] toBytes(String hexString) {
        int length = hexString.length() / 2;
        byte[] bytes = new byte[length];
        for (int i = 0; i < length; i++) {
            int index = i << 1;
            bytes[i] = (byte) ((HEX_STRING.indexOf(hexString.charAt(index)) << 4) + HEX_STRING.indexOf(hexString.charAt(index + 1)));
        }
        return bytes;
    }

}
