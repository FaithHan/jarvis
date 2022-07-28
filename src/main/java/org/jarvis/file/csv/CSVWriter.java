package org.jarvis.file.csv;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.jarvis.file.csv.CSVBeanUtils.FieldDefinition;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class CSVWriter {

    public static CSVFormat DEFAULT_CSV_FORMAT;

    static {
        DEFAULT_CSV_FORMAT = CSVFormat.DEFAULT
                .builder()
                .setNullString("-")
                .build();
    }

    public static String createCSVString(List<String> headers, List<List<Object>> columns) {
        StringWriter writer = new StringWriter();

        CSVFormat csvFormat = DEFAULT_CSV_FORMAT.builder()
                .setHeader(headers.toArray(new String[0]))
                .build();

        try {
            CSVPrinter csvPrinter = new CSVPrinter(writer, csvFormat);
            if (columns != null) {
                columns.forEach(column -> {
                    try {
                        csvPrinter.printRecord(column.toArray());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return writer.toString();
    }

    public static String createCSVString(List<?> beanList) {
        if (beanList == null || beanList.isEmpty()) {
            return "";
        }
        Class<?> classValue = beanList.get(0).getClass();
        List<FieldDefinition> fieldDefinitionList = CSVBeanUtils.getFieldDefinition(classValue);
        List<List<Object>> columns = beanList.stream().map(bean ->
                        fieldDefinitionList.stream().map(fieldDefinition ->
                                fieldDefinition.getBeanValue(bean)).collect(Collectors.toList())
                ).collect(Collectors.toList());
        return createCSVString(CSVBeanUtils.getHeader(classValue), columns);

    }

}
