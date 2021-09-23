package org.jarvis.misc;

import org.jarvis.http.HttpSiteFactory;
import org.jarvis.io.FileCopyUtils;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class QRCodeUtilsTest {

    @Test
    void encode() throws Exception {
        File out = new File("/Users/hanfei08/IdeaProjects/jarvis/a.png");
        FileCopyUtils.copy(QRCodeUtils.encode("https://www.baidu.com"
                , HttpSiteFactory.create().get("https://img1.baidu.com/it/u=4247529099,4093963290&fm=26&fmt=auto",byte[].class).getData()
                ), out);

        System.out.println(QRCodeUtils.decode(out));
    }
}