package org.jarvis.misc;

import java.util.stream.IntStream;

public abstract class StringUtils {

    public static String capitalize(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    /**
     * user_profile -> userProfile
     *
     * @param string
     * @return
     */
    public static String toCamelCase(String string) {
        StringBuilder sb = new StringBuilder(string.length());
        boolean nextCharToUpperCase = false;
        for (char charValue : string.toCharArray()) {
            if (charValue == '_') {
                nextCharToUpperCase = true;
            } else if (nextCharToUpperCase) {
                sb.append(Character.toUpperCase(charValue));
            } else {
                sb.append(charValue);
            }
        }
        return sb.toString();
    }

    /**
     * userProfile -> user_profile
     * UserProfile -> user_profile
     *
     * @param string
     * @return
     */
    public static String toUnderline(String string) {
        if (string.isEmpty()) {
            return string;
        }
        StringBuilder sb = new StringBuilder(string.length());
        char firstChar = sb.charAt(0);
        if (Character.isUpperCase(firstChar)) {
            sb.setCharAt(0, Character.toLowerCase(firstChar));
        }
        for (char charValue : string.toCharArray()) {
            if (Character.isUpperCase(charValue)) {
                sb.append('_');
                sb.append(Character.toLowerCase(charValue));
            } else {
                sb.append(charValue);
            }
        }
        return sb.toString();
    }

    /**
     * 12345678, *, 0 --> *******
     * 12345678, *, 1 --> 1******
     * 12345678, *, 2 --> 12*****
     */
    public static String replace(String string, char letter, int beginInclusive) {
        return replace(string, letter, beginInclusive, string.length());
    }


    /**
     * 12345678, *, 0, -1 --> ******8
     * 12345678, *, 1, -1 --> 1*****8
     * 12345678, *, 1, 7 --> 1*****8
     */
    public static String replace(String string, char letter, int beginInclusive, int endExclusive) {
        int length = string.length();
        if (length == 0) {
            return string;
        }
        beginInclusive = beginInclusive >= 0 ? beginInclusive : length + beginInclusive;
        endExclusive = endExclusive >= 0 ? endExclusive : length + endExclusive;

        if (beginInclusive < 0 || endExclusive > length || endExclusive <= beginInclusive) {
            throw new IllegalArgumentException("beginInclusive or endExclusive error");
        }
        StringBuilder sb = new StringBuilder(length);
        sb.append(string, 0, beginInclusive);
        IntStream.range(beginInclusive, endExclusive).forEach(v -> sb.append(letter));
        sb.append(string, endExclusive, length);
        return sb.toString();
    }
}
