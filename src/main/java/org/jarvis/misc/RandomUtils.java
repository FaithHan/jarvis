package org.jarvis.misc;

import java.util.concurrent.ThreadLocalRandom;

public class RandomUtils {

    private static final String NUMBER = "0123456789";
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String WORD = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final String SYMBOL = "!@#$%&*_+?.";


    /**
     * 获取n位随机数字字符串(开头不为0)
     *
     * @param length
     * @return
     */
    public static String randomNumberCode(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("长度必须大于0");
        }
        StringBuilder sb = new StringBuilder();
        while (sb.length() < length) {
            int number = nextNumber();
            if (number == 0 && sb.length() == 0) {
                continue;
            }
            sb.append(number);
        }
        return sb.toString();
    }

    /**
     * 获取n位字母表字符串
     *
     * @param length
     * @return
     */
    public static String randomAlphabet(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("长度必须大于0");
        }
        StringBuilder sb = new StringBuilder();
        while (sb.length() < length) {
            sb.append(nextAlphabet());
        }
        return sb.toString();

    }

    /**
     * 随机生成n位密码，密码位数大于4位，开头为字符
     *
     * @param length
     * @return
     */
    public static String randomPassword(int length) {
        if (length < 4) {
            throw new IllegalArgumentException("长度必须大于等于4");
        }
        StringBuilder sb = new StringBuilder();
        sb.append(nextAlphabet());
        while (sb.length() <= length - 2) {
            sb.append(nextWord());
        }
        sb.append(nextSymbol());
        return sb.toString();
    }



    /*内部工具方法*/

    private static char nextNumber() {
        return NUMBER.charAt(ThreadLocalRandom.current().nextInt(NUMBER.length()));
    }

    private static char nextAlphabet() {
        return ALPHABET.charAt(ThreadLocalRandom.current().nextInt(ALPHABET.length()));
    }

    private static char nextWord() {
        return WORD.charAt(ThreadLocalRandom.current().nextInt(WORD.length()));
    }

    private static char nextSymbol() {
        return SYMBOL.charAt(ThreadLocalRandom.current().nextInt(SYMBOL.length()));
    }

}
