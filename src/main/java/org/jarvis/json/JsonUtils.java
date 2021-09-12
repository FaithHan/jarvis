package org.jarvis.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

public abstract class JsonUtils {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    public static String toJson(Object obj) {
        return objectMapper.writeValueAsString(obj);
    }

    @SneakyThrows
    public static <T> T toObj(String json, Class<T> valueType) {
        return objectMapper.readValue(json, valueType);
    }

    @SneakyThrows
    public static <T> T toObj(String json, TypeReference<T> typeReference)  {
        return objectMapper.readValue(json,typeReference );
    }

    
}
