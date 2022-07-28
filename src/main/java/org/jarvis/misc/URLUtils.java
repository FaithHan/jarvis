package org.jarvis.misc;


import lombok.SneakyThrows;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public abstract class URLUtils {
    @SneakyThrows
    public static void main(String[] args) {
//        https://www.baidu.com/s?wd=%E6%88%91%E7%9F%A5%E9%81%93
        String encode = URLEncoder.encode("https://www.baidu.com/s?wd=我知道12231", StandardCharsets.UTF_8.name());
        System.out.println(encode);
    }
}
