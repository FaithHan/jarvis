package org.jarvis.email;

import org.jarvis.io.ClassPathUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class MailUtilsTest {

    @Test
    void sendMailWithAttachment() {
        MailUtils.sendMailWithAttachment(new String[]{"xxxx"},
                new String[]{},
                "Hello World", ClassPathUtils.copyToString("template.html"), Collections.emptyList(),
                new HashMap<String, File>(){
                    {
                        put("img1", new File(""));
                    }
                }
        );
    }
}