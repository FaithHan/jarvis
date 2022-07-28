package org.jarvis.file.csv;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jarvis.date.DatePattern;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person {

//    static {
//        CSVJsonMappingRegistry.register(Person.class, "cityList", new TypeReference<LinkedList<City>>() {
//        });
//    }

    @CSVField(order = 0)
    Long id;

    @CSVField(order = 0)
    String name;

    @CSVField(name = "年龄")
    Integer age;

    @CSVField(format = DatePattern.DATE_FORMAT)
    LocalDateTime birthday;

    @CSVField(name = "第二天堂",format = DatePattern.TIME_FORMAT)
    Date birthday2;

    @CSVField(json = true)
    LinkedList<String> list;

    @CSVField(name = "城市列表", json = true)
    List<City> cityList;


    static class IdConverter implements Converter<Long>{
        @Override
        public String toCSVText(Long property) {
            if (property == null) {
                return null;
            }
            return Double.valueOf(property).toString();
        }

        @Override
        public Long toProperty(String text) {
            if (text == null) {
                return null;
            }
            return Double.valueOf(text).longValue();
        }
    }


}