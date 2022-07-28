package org.jarvis.misc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RMBUtils {
    //    佰、仟、万、亿
    public static String[] NUMBERS = new String[]{"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖 ", "拾"};
    public static String[] LEVELS = new String[]{"分", "角", "圆*", "拾", "佰", "仟", "万*", "拾", "佰", "仟", "亿*", "拾", "佰", "仟", "万*", "拾", "佰", "仟"};

    public static String convertToBig(long value) {

        String string = Long.toString(value);
        List<String> strings = Arrays.asList(string.split(""));
        if (strings.size() > 18) {
            throw new IllegalArgumentException();
        }
        Collections.reverse(strings);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < strings.size(); i++) {
            sb.append(LEVELS[i]);
            sb.append(NUMBERS[Integer.parseInt(strings.get(i))]);
        }
        sb.reverse();
        String result = sb.toString();
//        result = result.replace("零分", "");
//        result = result.replace("零角", "");
        result = result.replaceAll("(零[\\u4E00-\\u9FA5])", "零");
        result = result.replaceAll("零{2,}", "零");
        result = result.replaceAll("零\\*", "");
        result = result.replaceAll("\\*", "");
        result = result.replaceAll("零$", "");
        result = result.replaceAll("零[万亿]", "");
        if (!result.endsWith("分")) {
            result += "整";
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(convertToBig(12000032101L));
    }

}
