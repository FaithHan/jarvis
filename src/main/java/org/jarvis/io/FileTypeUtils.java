package org.jarvis.io;

import org.jarvis.codec.HexUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class FileTypeUtils {

    public final static Map<String, String> FILE_TYPE_MAP;

    static {
        Map<String, String> map = new LinkedHashMap<>();
        //初始化文件类型信息
        map.put("jpg", "FFD8FF"); //JPEG (jpg)
        map.put("jpeg", "FFD8FF"); //JPEG (jpg)
        map.put("png", "89504E47");  //PNG (png)
        map.put("gif", "47494638");  //GIF (gif)
        map.put("tif", "49492A00");  //TIFF (tif)
        map.put("bmp", "424D"); //Windows Bitmap (bmp)
        map.put("dwg", "41433130"); //CAD (dwg)
        map.put("html", "68746D6C3E");  //HTML (html)
        map.put("rtf", "7B5C727466");  //Rich Text Format (rtf)
        map.put("xml", "3C3F786D6C");
        map.put("zip", "504B0304");
        map.put("rar", "52617221");
        map.put("psd", "38425053");  //Photoshop (psd)
        map.put("eml", "44656C69766572792D646174653A");  //Email [thorough only] (eml)
        map.put("dbx", "CFAD12FEC5FD746F");  //Outlook Express (dbx)
        map.put("pst", "2142444E");  //Outlook (pst)
        map.put("xls", "D0CF11E0");  //MS Word
        map.put("doc", "D0CF11E0");  //MS Excel 注意：word 和 excel的文件头一样
        map.put("mdb", "5374616E64617264204A");  //MS Access (mdb)
        map.put("wpd", "FF575043"); //WordPerfect (wpd)
        map.put("eps", "252150532D41646F6265");
        map.put("ps", "252150532D41646F6265");
        map.put("pdf", "255044462D312E");  //Adobe Acrobat (pdf)
        map.put("qdf", "AC9EBD8F");  //Quicken (qdf)
        map.put("pwl", "E3828596");  //Windows Password (pwl)
        map.put("wav", "57415645");  //Wave (wav)
        map.put("avi", "41564920");
        map.put("ram", "2E7261FD");  //Real Audio (ram)
        map.put("rm", "2E524D46");  //Real Media (rm)
        map.put("mpg", "000001BA");  //
        map.put("mov", "6D6F6F76");  //Quicktime (mov)
        map.put("asf", "3026B2758E66CF11"); //Windows Media (asf)
        map.put("mid", "4D546864");  //MIDI (mid)
        FILE_TYPE_MAP = Collections.unmodifiableMap(map);
    }

    public static String getFileType(File file) {
        try {
            return getFileType(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getFileType(InputStream inputStream) {
        try {
            byte[] headerBytes = new byte[20];
            inputStream.read(headerBytes);
            return getFileType(headerBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getFileType(byte[] fileBytes) {
        if (fileBytes.length > 20) {
            fileBytes = Arrays.copyOf(fileBytes, 20);
        }
        String fileHexHeader = HexUtils.toHexString(fileBytes).toUpperCase();
        return FILE_TYPE_MAP.entrySet().stream()
                .filter(fileTypeItem -> fileHexHeader.startsWith(fileTypeItem.getValue()))
                .findFirst()
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    public static boolean isFileTypeMatch(File file, String fileExtension) {
        if (!isFileTypeSupported(fileExtension)) {
            throw new RuntimeException("不支持的文件扩展名");
        }
        return fileExtension.equalsIgnoreCase(getFileType(file));
    }

    public static boolean isFileTypeMatch(InputStream inputStream, String fileExtension) {
        if (!isFileTypeSupported(fileExtension)) {
            throw new RuntimeException("不支持的文件扩展名");
        }
        return fileExtension.equalsIgnoreCase(getFileType(inputStream));
    }

    public static boolean isFileTypeMatch(byte[] fileBytes, String fileExtension) {
        if (!isFileTypeSupported(fileExtension)) {
            throw new RuntimeException("不支持的文件扩展名");
        }
        return fileExtension.equalsIgnoreCase(getFileType(fileBytes));
    }

    public static boolean isFileTypeSupported(String fileExtension) {
        return FILE_TYPE_MAP.containsKey(fileExtension.toLowerCase());
    }

}  