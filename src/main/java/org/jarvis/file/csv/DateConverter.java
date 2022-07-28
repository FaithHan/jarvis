package org.jarvis.file.csv;

import org.jarvis.date.DateUtils;

import java.util.Date;

class DateConverter implements Converter<Date> {

    private final String pattern;

    private DateConverter(String pattern) {
        this.pattern = pattern;
    }

    public static DateConverter ofPattern(String pattern) {
        return new DateConverter(pattern);
    }

    @Override
    public String toCSVText(Date property) {
        if (property == null) {
            return null;
        }
        return DateUtils.format(property, pattern);

    }

    @Override
    public Date toProperty(String text) {
        if (text == null) {
            return null;
        }
        return DateUtils.parse(text, pattern);
    }
}