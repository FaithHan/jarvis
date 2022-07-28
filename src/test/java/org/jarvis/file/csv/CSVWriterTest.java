package org.jarvis.file.csv;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

class CSVWriterTest {


    List<Person> personList = Arrays.asList(
            new Person(1L, null, 20, LocalDateTime.now().plusDays(1), new Date(), new LinkedList<>(Arrays.asList("hanfei", "张杰")), null),
            new Person(2L, null, 20, LocalDateTime.now(), new Date(), null, null),
            new Person(3L, "", 20, null, new Date(), null, null),
            new Person(4L, "张某人", 20, LocalDateTime.now(), new Date(), null, null),
            new Person(5L, "Tom", 20, LocalDateTime.now(), new Date(), null, Arrays.asList(new City("北京",10000)))
    );


    @Test
    void createCSVString() {
        String csvString = CSVWriter.createCSVString(personList);
        System.out.println(csvString);
    }
}