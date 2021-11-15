package org.jarvis.http;

import lombok.Data;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class MultipartEntity {

    Map<String, List<String>> textMap = new HashMap<>();

    Map<String, List<Object>> binaryMap = new HashMap<>();

    public MultipartEntity textBody(String name, String value) {
        textMap.computeIfAbsent(name, key -> new ArrayList<>()).add(value);
        return this;
    }

    public MultipartEntity binaryBody(String name, File file) {
        binaryMap.computeIfAbsent(name, key -> new ArrayList<>()).add(file);
        return this;
    }

    public MultipartEntity binaryBody(String name, InputStream inputStream) {
        binaryMap.computeIfAbsent(name, key -> new ArrayList<>()).add(inputStream);
        return this;
    }

    public MultipartEntity binaryBody(String name, byte[] byteData) {
        binaryMap.computeIfAbsent(name, key -> new ArrayList<>()).add(byteData);
        return this;
    }

    public static MultipartEntity create() {
        return new MultipartEntity();
    }
}
