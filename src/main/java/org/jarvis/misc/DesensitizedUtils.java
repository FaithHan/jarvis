package org.jarvis.misc;


import java.util.stream.IntStream;

public abstract class DesensitizedUtils {

    private static final char MASK_CHAR = '*';

    public static String mobilePhone(String phoneNumber) {
        return StringUtils.replace(phoneNumber, MASK_CHAR, 3, -4);
    }

    public static String password(String password) {
        Assert.notNull(password, "password can not be empty");
        if (password.isEmpty()) {
            return password;
        }
        StringBuilder sb = new StringBuilder(password.length());
        IntStream.range(0, password.length()).forEach(v -> sb.append(MASK_CHAR));
        return sb.toString();
    }
}
