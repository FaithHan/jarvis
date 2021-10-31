package org.jarvis.email;

import org.jarvis.io.ClassPathUtils;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

class MailUtilsTest {

    @Test
    void sendMailWithAttachment() throws FileNotFoundException {
        MailUtils.sendHtmlMail(
                new String[]{"****@qq.com"},
//                new String[]{"freepuresakura@gmail.com"},
                null,
                null,
                "你好世界",
                ClassPathUtils.copyToString("mail_template.html"),
                MailUtils.PriorityEnum.NORMAL,
                new HashMap<String, InputStream>(){
                    {
                        put("头像哈哈", new FileInputStream("/Users/hanfei08/Desktop/131628246419_.pic_thumb.jpg"));
                    }
                },
                new HashMap<String, InputStream>(){
                    {
                        put("img1", new FileInputStream("/Users/hanfei08/Desktop/131628246419_.pic_thumb.jpg"));
                        put("video1", new FileInputStream("/Users/hanfei08/Downloads/1631602998476450.mp4"));
                    }
                }
        );
    }
}