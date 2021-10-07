package org.jarvis.misc;

public abstract class StringUtils {

    public static String capitalize(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

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

    public static String toUnderline(String string) {
        StringBuilder sb = new StringBuilder(string.length());
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
}
