package org.jarvis.file.csv;

import com.fasterxml.jackson.core.type.TypeReference;
import org.jarvis.json.JsonUtils;

import java.lang.reflect.Type;

class JsonConverter<T> implements Converter<T> {

    private TypeReference<T> typeReference;

    private Class<T> classValue;

    private Type type;

    public JsonConverter(Type type) {
        this.type = type;
    }

    public JsonConverter(Class<T> classValue) {
        this.classValue = classValue;
    }

    public JsonConverter(TypeReference<T> typeReference) {
        this.typeReference = typeReference;
    }

    @Override
    public String toCSVText(Object property) {
        if (property == null) {
            return null;
        }
        return JsonUtils.toJson(property);
    }

    @Override
    public T toProperty(String text) {
        if (text == null) {
            return null;
        }
        if (typeReference != null) {
            return JsonUtils.toObj(text, typeReference);
        } else if (type != null) {
            return JsonUtils.toObj(text, type);
        } else {
            return JsonUtils.toObj(text, classValue);
        }
    }

}