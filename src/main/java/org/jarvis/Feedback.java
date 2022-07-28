package org.jarvis;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.jarvis.io.StreamCopyUtils;
import org.jarvis.jdbc.JDBCUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Feedback {

    static ObjectMapper objectMapper = new ObjectMapper();

    static DataSource datasource = JDBCUtils.getHikariDatasource("jdbc:mysql://10.24.8.209:8080/feedback?characterEncoding=UTF-8&tinyInt1isBit=false&autoReconnect=true&useSSL=true&serverTimezone=UTC&jdbcCompliantTruncation=false",
            "feedback_r", "3kQ_CVnqQsc8EEljA2");

    public static void main(String[] args) throws IOException, SQLException {

        HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream("/Users/hanfei08/Downloads/投诉信息商户号1509658231时间3.1-5.12.xls"));
        Sheet sheetAt = workbook.getSheetAt(0);

        JdbcTemplate jdbcTemplate = new JdbcTemplate(datasource);
        String complainId = null;

        List<String> strings = Files.readAllLines(Paths.get("/Users/hanfei08/Library/Application Support/JetBrains/IntelliJIdea2022.1/scratches/scratch_186.txt"));

        /*for (String line : strings) {
            complainId = line;
            try {

                Map<String, Object> stringObjectMap = jdbcTemplate.queryForMap("select * from channel_feedback where channel_complain_id = ?", complainId);
                Long channelFeedbackId = (Long) stringObjectMap.get("id");
                Map<String, Object> map = jdbcTemplate.queryForMap("select * from feedback_info where channel_feedback_id = ?", channelFeedbackId);
                String ssoId = (String) map.get("sso_id");
                UFOService ufoService = new UFOService();
                String data = ufoService.getData(ssoId);
                String status = objectMapper.readTree(data).get("data").get(0).get("status").asText();
                String productType = objectMapper.readTree(data).get("data").get(0).get("product_type").asText();
                print(complainId + "\t" + ssoId + "\t" + status  + "\t" + mapping.get(productType));
            } catch (Exception e) {
                print(complainId + "error");
            }
        }*/

        /*for (Row row : sheetAt) {
            try {
                complainId = row.getCell(1).getStringCellValue();
                String date = row.getCell(11).getStringCellValue();

                Map<String, Object> stringObjectMap = jdbcTemplate.queryForMap("select * from channel_feedback where channel_complain_id = ?", complainId);
                Long channelFeedbackId = (Long) stringObjectMap.get("id");
                Map<String, Object> map = jdbcTemplate.queryForMap("select * from feedback_info where channel_feedback_id = ?", channelFeedbackId);
                String ssoId = (String) map.get("sso_id");
                UFOService ufoService = new UFOService();
                String data = ufoService.getData(ssoId);
                String status = objectMapper.readTree(data).get("data").get(0).get("status").asText();
                String productType = objectMapper.readTree(data).get("data").get(0).get("product_type").asText();
                print(complainId + "\t" + ssoId + "\t" + status + "\t" + date + "\t" + mapping.get(productType));
            } catch (Exception e) {
                print(complainId + "error");
            }
        }*/
    }

    static PrintWriter printWriter;

    static Map<String, String> mapping = new HashMap<>();

    static {
        try {
            String s = StreamCopyUtils.copyToString(new FileInputStream("/Users/hanfei08/Library/Application Support/JetBrains/IntelliJIdea2022.1/scratches/scratch_188.txt"));
            Arrays.stream(s.split("\\R"))
                    .forEach(line -> {
                        String[] split = line.split("\\|");
                        mapping.put(split[0], split[1]);
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            printWriter = new PrintWriter("/Users/hanfei08/IdeaProjects/jarvis/a.csv");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    static void print(String line) {
        System.out.println(line);
//        printWriter.println(line);
    }


}
