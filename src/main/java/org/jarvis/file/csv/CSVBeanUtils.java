package org.jarvis.file.csv;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Builder;
import lombok.Data;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@SuppressWarnings({"unchecked", "rawtypes"})
class CSVBeanUtils {

    private static final Map<Class<?>, List<FieldDefinition>> FIELD_DEF_MAP = new ConcurrentHashMap<>();

    static List<String> getHeader(Class<?> classValue) {
        return getFieldDefinition(classValue).stream().map(FieldDefinition::getHeaderName).collect(Collectors.toList());
    }

    static Map<String, FieldDefinition> getHeaderDefinitionMap(Class<?> classValue) {
        return getFieldDefinition(classValue).stream()
                .collect(Collectors.toMap(FieldDefinition::getHeaderName, Function.identity()));
    }

    static List<FieldDefinition> getFieldDefinition(Class<?> classValue) {
        return FIELD_DEF_MAP.computeIfAbsent(classValue, CSVBeanUtils::doGetFieldDefinition);
    }

    private static List<FieldDefinition> doGetFieldDefinition(Class<?> classValue) {
        return Arrays.stream(classValue.getDeclaredFields())
                .filter(field -> Optional.of(field)
                        .map(value -> value.getAnnotation(CSVField.class))
                        .map(CSVField::ignore)
                        .map(ignore -> !ignore)
                        .orElse(true))
                .map(field -> {
                    CSVField csvField = field.getAnnotation(CSVField.class);

                    String headerName = csvField == null ? field.getName() :
                            CSVField.EMPTY.equals(csvField.name()) ? field.getName() : csvField.name();

                    int order = csvField != null ? csvField.order() : CSVField.DEFAULT_ORDER;

                    String format = csvField != null ? csvField.format() : CSVField.EMPTY;

                    boolean isJson = csvField != null && csvField.json();

                    Class<?> fieldType = field.getType();

                    try {
                        Converter<?> converter = csvField == null ? null :
                                void.class == csvField.converter() ? null :
                                        (Converter<?>) csvField.converter().newInstance();

                        if (converter == null && !CSVField.EMPTY.equals(format)) {
                            if (fieldType == Date.class) {
                                converter = DateConverter.ofPattern(format);
                            } else if (fieldType == LocalDate.class) {
                                converter = LocalDateConverter.ofPattern(format);
                            } else if (fieldType == LocalDateTime.class) {
                                converter = LocalDateTimeConverter.ofPattern(format);
                            } else {
                                throw new IllegalArgumentException(String.format("%s###%s只有日期类型才可以设置pattern",
                                        classValue.getName(), fieldType.getName()));
                            }
                        } else if (converter == null && isJson) {
                            String fieldName = field.getName();
                            try {
                                Method method = classValue.getDeclaredMethod("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));
                                Type type = method.getGenericReturnType();
                                converter = new JsonConverter<>(type);
                            } catch (NoSuchMethodException e) {
                                TypeReference<?> typeReference =
                                        CSVJsonMappingRegistry.getTypeReference(classValue, fieldName);
                                if (typeReference != null) {
                                    converter = new JsonConverter<>(typeReference);
                                } else {
                                    converter = new JsonConverter<>(fieldType);
                                }
                            }
                        }

                        field.setAccessible(true);

                        return FieldDefinition.builder()
                                .order(order)
                                .headerName(headerName)
                                .field(field)
                                .converter(converter)
                                .build();

                    } catch (InstantiationException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                })
                .sorted(Comparator.comparing(FieldDefinition::getOrder))
                .collect(Collectors.toList());
    }

    @Data
    @Builder
    static class FieldDefinition {
        private int order;
        private String headerName;
        private Field field;
        private Converter converter;

        Object getBeanValue(Object bean) {
            try {
                Object obj = field.get(bean);
                if (converter != null) {
                    obj = converter.toCSVText(obj);
                }
                return obj;
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        void setBeanValue(Object bean, String text) {
            Object value;
            if (converter != null) {
                value = converter.toProperty(text);
                try {
                    field.set(bean, value);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            } else {
                Class<?> type = field.getType();
                try {
                    if (type == Byte.class || type == byte.class) {
                        field.set(bean, Byte.parseByte(text));
                    } else if (type == Integer.class || type == int.class) {
                        field.set(bean, Integer.parseInt(text));
                    } else if (type == Long.class || type == long.class) {
                        field.set(bean, Long.parseLong(text));
                    } else if (type == Float.class) {
                        field.set(bean, Float.parseFloat(text));
                    } else if (type == Double.class || type == double.class) {
                        field.set(bean, Double.parseDouble(text));
                    } else if (type == BigInteger.class) {
                        field.set(bean, BigInteger.valueOf(Long.parseLong(text)));
                    } else if (type == BigDecimal.class) {
                        field.set(bean, new BigDecimal(text));
                    } else {
                        field.set(bean, text);
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }

}
