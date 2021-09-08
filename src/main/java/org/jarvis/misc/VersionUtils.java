package org.jarvis.misc;

import java.util.regex.Pattern;

/**
 * 版本号工具
 * 1. 主版本号：当你做了不兼容的 API 修改，
 * 2. 次版本号：当你做了向下兼容的功能性新增，
 * 3. 修订号：当你做了向下兼容的问题修正。
 * MAJOR.MINOR.PATCH
 */
public class VersionUtils {

    public final static Pattern VERSION_PATTERN = Pattern.compile("[1-9]\\d*(\\.([1-9]\\d*|0)){0,2}");

    public static boolean isValid(String version) {
        return version != null && VERSION_PATTERN.matcher(version).matches();
    }

    /**
     * 比较两个版本间的大小
     *
     * 1.2.1 > 1.2.0
     * 1.2.3 == 1.2.3
     * 1 == 1.0.0
     * 2.1 == 2.1.0
     * 3.2.1 > 3.2
     * @param version1
     * @param version2
     * @return
     */
    public static int compare(String version1, String version2) {
        if (!isValid(version1) || !isValid(version2)) {
            throw new IllegalArgumentException("版本号格式不正确");
        }

        if (version1.equals(version2)) {
            return 0;
        }

        String[] arr1 = version1.split("\\.");
        String[] arr2 = version2.split("\\.");

        int result = 0;
        for (int i = 0; i < 3; i++) {
            int value1 = i < arr1.length ? Integer.parseInt(arr1[i]) : 0;
            int value2 = i < arr2.length ? Integer.parseInt(arr2[i]) : 0;
            if (value1 != value2) {
                result =  value1 > value2 ? 1 : -1;
                break;
            }
        }
        return result;
    }

}
