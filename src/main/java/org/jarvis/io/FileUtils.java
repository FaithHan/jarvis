package org.jarvis.io;

import lombok.SneakyThrows;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;

public abstract class FileUtils {

    public static String getExtension(String filename) {
        int index = filename.lastIndexOf('.');
        if (index == -1) {
            return "";
        } else {
            return filename.substring(index + 1);
        }
    }

    @SneakyThrows
    public static List<String> readLines(File file, Charset charset) {
        return Files.readAllLines(file.toPath(), charset);
    }
}
