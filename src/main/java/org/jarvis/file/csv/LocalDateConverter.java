package org.jarvis.file.csv;

import org.jarvis.date.LocalDateUtils;

import java.time.LocalDate;

class LocalDateConverter implements Converter<LocalDate> {

    private final String pattern;

    private LocalDateConverter(String pattern) {
        this.pattern = pattern;
    }

    public static LocalDateConverter ofPattern(String pattern) {
        return new LocalDateConverter(pattern);
    }

    @Override
    public String toCSVText(LocalDate property) {
        if (property == null) {
            return null;
        }
        return LocalDateUtils.format(property, pattern);

    }

    @Override
    public LocalDate toProperty(String text) {
        if (text == null) {
            return null;
        }
        return LocalDateUtils.parse(text, pattern);
    }
}