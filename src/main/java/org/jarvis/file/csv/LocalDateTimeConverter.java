package org.jarvis.file.csv;

import org.jarvis.date.DateUtils;
import org.jarvis.date.LocalDateTimeUtils;

import java.time.LocalDateTime;

class LocalDateTimeConverter implements Converter<LocalDateTime> {

        private final String pattern;

        private LocalDateTimeConverter(String pattern) {
            this.pattern = pattern;
        }

        public static LocalDateTimeConverter ofPattern(String pattern) {
            return new LocalDateTimeConverter(pattern);
        }

        @Override
        public String toCSVText(LocalDateTime property) {
            if (property == null) {
                return null;
            }
            return LocalDateTimeUtils.format(property, pattern);

        }

        @Override
        public LocalDateTime toProperty(String text) {
            if (text == null) {
                return null;
            }
            try {
                return LocalDateTimeUtils.parse(text, pattern);
            } catch (Exception e) {
                return DateUtils.toLocalDateTime(DateUtils.parse(text, pattern));
            }
        }
    }