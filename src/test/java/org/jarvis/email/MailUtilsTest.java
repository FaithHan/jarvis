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
                null,
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

    @Test
    void sendMailTest() throws FileNotFoundException {
        Email email = Email.html()
                .to("1975772822@qq.com", "zhaiss1@lenovo.com")
                .subject("你好你好")
                .content("<style>\n" +
                        "    .container {\n" +
                        "        background: gray;\n" +
                        "        margin: 0 auto;\n" +
                        "        width: 70%;\n" +
                        "    }\n" +
                        "</style>\n" +
                        "<div class='container'>\n" +
                        "    <h1>你是谁</h1>\n" +
                        "    <p>我不知道你是谁</p>\n" +
                        "    <img src='cid:img1' alt=''>\n" +
                        "</div>")
                .addInline("img1", new FileInputStream("/Users/hanfei08/IdeaProjects/resourceRepo/img.png"))
                .addAttachment("这是一个.pptx", new FileInputStream("/Users/hanfei08/Documents/密码分享.pptx"))
                .build();
        MailUtils.sendMail(email);

    }
}