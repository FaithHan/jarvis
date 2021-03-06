package org.jarvis.misc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * json字符串字段值脱敏，支持嵌套
 */
public final class JsonDesensitizedUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonDesensitizedUtils.class);
    private static final char MASK_CHAR = '*';

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final List<String> sensitiveKeyList;


    public JsonDesensitizedUtils(List<String> sensitiveKeyList) {
        this.sensitiveKeyList = sensitiveKeyList;
    }


    public String desensitize(String json) {
        try {
            return desensitize(objectMapper.readTree(json));
        } catch (JsonProcessingException e) {
            LOGGER.error("JsonDesensitizedUtils desensitize error", e);
            return json;
        }
    }

    public String desensitize(JsonNode jsonNode) {
        traverse(jsonNode);
        return jsonNode.toString();
    }

    public void traverse(JsonNode root) {
        if (root.isObject()) {
            ObjectNode node = (ObjectNode) root;
            Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                if (!field.getValue().isValueNode()) {
                    traverse(field.getValue());
                } else {
                    node.set(field.getKey(), change(field));
                }
            }
        } else if (root.isArray()) {
            ArrayNode arrayNode = (ArrayNode) root;
            for (int i = 0; i < arrayNode.size(); i++) {
                JsonNode node = arrayNode.get(i);
                if (!node.isValueNode()) {
                    traverse(node);
                }
            }
        }
    }

    private JsonNode change(Map.Entry<String, JsonNode> field) {
        String key = field.getKey();
        JsonNode jsonNode = field.getValue();
        if (jsonNode.isNull()) {
            return jsonNode;
        }
        if (isSensitiveKey(key)) {
            if (jsonNode.isTextual()) {
                String value = field.getValue().asText();
                if (org.apache.commons.lang3.StringUtils.isNotBlank(value)) {
                    return JsonNodeFactory.instance.textNode(StringUtils.replace(value, MASK_CHAR, value.length() >> 1));
                }
            }
        }
        return field.getValue();
    }


    private boolean isSensitiveKey(String key) {
        return this.sensitiveKeyList.contains(key);
    }

}

/*
Gson 版本
public final class JsonDesensitizedUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonDesensitizedUtils.class);

    private static final char MASK_CHAR = '*';

    private final List<String> sensitiveKeyList;

    private final int maxSize;

    public JsonDesensitizedUtils(List<String> sensitiveKeyList) {
        this(sensitiveKeyList, Integer.MAX_VALUE);
    }

    public JsonDesensitizedUtils(List<String> sensitiveKeyList, int maxSize) {
        this.sensitiveKeyList = sensitiveKeyList == null ? Collections.emptyList() : sensitiveKeyList;
        this.maxSize = maxSize;
    }

    public String changeSensitiveMsg(String json) {
        try {
            JsonElement jsonElement = GsonUtils.parseAsElement(json);
            if (jsonElement.isJsonObject()) {
                return GsonUtils.toJson(changeSensitiveMsg(jsonElement.getAsJsonObject()));
            } else if (jsonElement.isJsonArray()) {
                return GsonUtils.toJson(changeSensitiveMsg(jsonElement.getAsJsonArray()));
            }
            return json;
        } catch (JsonSyntaxException e) {
            return json;
        } catch (Exception e) {
            LOGGER.error("change sensitive field error", e);
            return json;
        }
    }

    private JsonObject changeSensitiveMsg(JsonObject jsonObject) {
        for (String key : jsonObject.keySet()) {
            JsonElement jsonElement = jsonObject.get(key);
            if (jsonElement.isJsonObject()) {
                jsonObject.add(key, changeSensitiveMsg(jsonElement.getAsJsonObject()));
            } else if (jsonElement.isJsonArray()) {
                jsonObject.add(key, changeSensitiveMsg(jsonElement.getAsJsonArray()));
            } else if (isSensitiveKey(key) && jsonElement.isJsonPrimitive()) {
                String value = jsonElement.getAsString();
                if (value.length() > maxSize) {
                    value = value.substring(0, maxSize);
                }
                jsonObject.addProperty(key, StringUtils.replace(value, MASK_CHAR, value.length() >> 1));
            }
        }
        return jsonObject;
    }

    private JsonArray changeSensitiveMsg(JsonArray jsonArray) {
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonElement jsonElement = jsonArray.get(i);
            if (jsonElement.isJsonArray()) {
                jsonArray.set(i, changeSensitiveMsg(jsonElement.getAsJsonArray()));
            } else if (jsonElement.isJsonObject()) {
                jsonArray.set(i, changeSensitiveMsg(jsonElement.getAsJsonObject()));
            }
        }
        return jsonArray;
    }


    private boolean isSensitiveKey(String key) {
        return this.sensitiveKeyList.contains(key);
    }

}
*/