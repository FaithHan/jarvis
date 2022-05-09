package org.jarvis.misc;

import org.jarvis.json.JsonUtils;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JsonDesensitizedUtilsTest {

    @Test
    void desensitize() {
        Map<String, Object> map = new HashMap<>();
        map.put("key1", "123456");
        map.put("key2", "123456");
        map.put("key3", "123456");
        ArrayList<Object> list = new ArrayList<>();
        list.add("11");
        list.add("22");
        Map<String, Object> subMap = new HashMap<>();
        subMap.put("key1", "123456");
        subMap.put("key2", "123456");
        list.add(subMap);
        map.put("key4", list);
        map.put("key5", null);
        JsonDesensitizedUtils jsonDesensitizedUtils = new JsonDesensitizedUtils(Arrays.asList("key1","key2"));
        String json = JsonUtils.toJson(map);
        System.out.println(jsonDesensitizedUtils.desensitize(json));
    }
}