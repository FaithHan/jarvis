package org.jarvis.io;

import org.jarvis.http.HttpSite;
import org.jarvis.http.HttpSiteFactory;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.InputStream;

class FileTypeUtilsTest {

    @Test
    void getFileTypeByByteArray() {
        HttpSite httpSite = HttpSiteFactory.create();
        InputStream inputStream = httpSite.get("https://media.wired.com/photos/59326d5344db296121d6aee9/master/pass/8552.gif", InputStream.class).getData();
        String fileType = FileTypeUtils.getFileType(inputStream);
        System.out.println(fileType);
    }

    @Test
    void getFileTypeByFile() {
        String fileType = FileTypeUtils.getFileType(new File("/Users/hanfei08/Documents/书籍/Vim实用技巧(jb51.net).pdf"));
        System.out.println(fileType);
    }
}