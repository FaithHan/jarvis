package org.jarvis.file.excel;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

public abstract class ExcelUtils {

    private static Logger logger = LoggerFactory.getLogger(ExcelUtils.class);

    public static Workbook getWorkbook(InputStream inputStream) throws IOException {
        return new XSSFWorkbook(inputStream);
    }

    public static void main(String[] args) {
        logger.error("1",new RuntimeException());
    }


}
