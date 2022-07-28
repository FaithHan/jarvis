package org.jarvis.file.csv;


import com.fasterxml.jackson.core.type.TypeReference;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CSVJsonMappingRegistry {

    private static final Map<Class<?>, Map<String, TypeReference<?>>> registry = new HashMap<>();


    public static void register(Class<?> classValue, String propertyName, TypeReference<?> typeReference) {
        Map<String, TypeReference<?>> map = registry.computeIfAbsent(classValue, key -> new HashMap<>());
        map.put(propertyName, typeReference);
    }

    public static TypeReference<?> getTypeReference(Class<?> classValue, String propertyName) {
        return Optional.ofNullable(registry.get(classValue))
                .map(map -> map.get(propertyName))
                .orElse(null);
    }
}
