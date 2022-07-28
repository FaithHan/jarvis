package org.jarvis.file.csv;

import lombok.SneakyThrows;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.jarvis.file.csv.CSVBeanUtils.FieldDefinition;

import java.io.Reader;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CSVReader {

    public static CSVFormat DEFAULT_CSV_FORMAT;

    static {
        DEFAULT_CSV_FORMAT = CSVFormat.DEFAULT
                .builder()
                // 不设置null string 区分不出null和空字符串
                .setNullString("-")
                .build();
    }

    @SneakyThrows
    public static List<Map<String, String>> readCSV(Reader reader) {
        CSVParser csvParser = DEFAULT_CSV_FORMAT
                .builder()
                .build()
                .parse(reader);

        // 不支持动态header，这里先取出第一行作为header
        List<String> headers = csvParser.iterator().next()
                .stream().collect(Collectors.toList());

        return csvParser.stream().map(record -> {
            LinkedHashMap<String, String> map = new LinkedHashMap<>();
            for (int i = 0; i < headers.size(); i++) {
                map.put(headers.get(i), record.get(i));
            }
            return map;
        }).collect(Collectors.toList());
    }

    @SneakyThrows
    public static <T> List<T> readCSV(Reader reader, Class<T> classValue) {
        List<Map<String, String>> beanMapList = readCSV(reader);

        return beanMapList.stream().map(map -> {
            T instance;
            try {
                instance = classValue.newInstance();
                Map<String, FieldDefinition> headerDefinitionMap =
                        CSVBeanUtils.getHeaderDefinitionMap(classValue);
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    String value = entry.getValue();
                    FieldDefinition fieldDefinition = headerDefinitionMap.get(entry.getKey());
                    if (fieldDefinition != null) {
                        fieldDefinition.setBeanValue(instance, value);
                    }
                }
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
            return instance;
        }).collect(Collectors.toList());
    }
}
