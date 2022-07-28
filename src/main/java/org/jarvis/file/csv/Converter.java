package org.jarvis.file.csv;

public interface Converter<T> {

    String toCSVText(T property);

    T toProperty(String text);

}
