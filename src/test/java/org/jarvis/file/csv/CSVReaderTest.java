package org.jarvis.file.csv;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

class CSVReaderTest {



    String csvString = "id,name,年龄,birthday,第二天堂,list,城市列表\n" +
            "1,-,20,2021-09-12,13:45:47,\"[\"\"hanfei\"\",\"\"张杰\"\"]\",-\n" +
            "2,-,20,2021-09-11,13:45:47,-,-\n" +
            "3,,20,-,13:45:47,-,-\n" +
            "4,张某人,20,2021-09-11,13:45:47,-,-\n" +
            "5,Tom,20,2021-09-11,13:45:47,-,\"[{\"\"cityName\"\":\"\"北京\"\",\"\"code\"\":10000}]\"";

    @Test
    void readCSV() {
        List<Map<String, String>> maps = CSVReader.readCSV(new StringReader(csvString));
        System.out.println(maps);
    }

    @Test
    void readCSVToBean() {
        List<Person> people = CSVReader.readCSV(new StringReader(csvString), Person.class);
        List<City> cityList = people.get(4).getCityList();
        String cityName = cityList.get(0).getCityName();
        System.out.println(cityName);
    }

}