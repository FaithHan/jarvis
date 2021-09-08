package org.jarvis.io;

import lombok.SneakyThrows;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

import java.nio.charset.StandardCharsets;

public class ClassPathUtils {

    @SneakyThrows
    public static String copyToString(String classPath) {
        return StreamUtils.copyToString(new ClassPathResource(classPath).getInputStream(), StandardCharsets.UTF_8);
    }
}
