package org.jarvis.email;

import lombok.extern.slf4j.Slf4j;
import org.jarvis.io.StreamCopyUtils;
import org.jarvis.misc.Assert;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import static org.jarvis.email.PriorityEnum.NORMAL;

/**
 * https://www.baeldung.com/spring-email
 * https://javaee.github.io/javamail/
 * https://www.javatpoint.com/java-mail-api-tutorial
 * https://www.baeldung.com/java-email
 */
@Slf4j
public class MailUtils {

    private static final String DEFAULT_CONTENT_TYPE = "application/octet-stream";

    private static final String[] EMPTY_ARRAY = new String[]{};

    private static final JavaMailSender SENDER;

    public static String from = "hanfei08@baidu.com";

    static {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("mail.baidu.com");
        mailSender.setPort(25);
        mailSender.setUsername("hanfei08");
        mailSender.setPassword("******");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        SENDER = mailSender;
    }

    public static void sendMail(Email email) {
        if (email.isHtml) {
            sendHtmlMail(email.to, email.cc, email.bcc, email.subject, email.content, email.priority, email.attachmentMap, email.inlineMap);
        } else {
            sendTextMail(email.to, email.cc, email.bcc, email.subject, email.content, email.priority, email.attachmentMap);
        }
    }

    public static void sendTextMail(String[] to, String[] cc, String[] bcc,
                                    String subject, String content, PriorityEnum priority,
                                    Map<String, InputStream> attachmentMap) {
        sendMail(to, cc, bcc, subject, content, priority, attachmentMap, null, false);
    }

    public static void sendHtmlMail(String[] to, String[] cc, String[] bcc,
                                    String subject, String content, PriorityEnum priority,
                                    Map<String, InputStream> attachmentMap, Map<String, InputStream> inlineMap) {
        sendMail(to, cc, bcc, subject, content, priority, attachmentMap, inlineMap, true);
    }

    /**
     * send isHTML email
     *
     * @param to
     * @param cc
     * @param bcc
     * @param subject
     * @param content
     * @param priority      PriorityEnum
     * @param attachmentMap 附件 文件名-输入流
     * @param inlineMap     资源 cuid - 输入流 例：<img class="avator" src="cid:img1">
     * @param isHTML        is HTML mode?
     */
    private static void sendMail(String[] to, String[] cc, String[] bcc,
                                 String subject, String content, PriorityEnum priority,
                                 Map<String, InputStream> attachmentMap, Map<String, InputStream> inlineMap,
                                 boolean isHTML) {
        Assert.notNull(to, "to array can not be null");
        Assert.notNull(subject, "subject can not be null");
        Assert.notNull(content, "content can not be null");
        try {
            MimeMessage message = SENDER.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
            helper.setFrom(from);
            helper.setTo(to);
            helper.setCc(cc != null ? cc : EMPTY_ARRAY);
            helper.setCc(bcc != null ? bcc : EMPTY_ARRAY);
            helper.setSubject(subject);
            helper.setText(content, isHTML);
            helper.setSentDate(new Date());
            helper.setEncodeFilenames(true); // 解决附件中文名称乱码
            priority = priority != null ? priority : NORMAL;
            switch (priority) {
                case LOW:
                    helper.setPriority(5);
                    break;
                case HIGH:
                    helper.setPriority(1);
                    break;
                case NORMAL:
                default:
            }
            if (attachmentMap != null) {
                for (Map.Entry<String, InputStream> entry : attachmentMap.entrySet()) {
                    String name = entry.getKey();
                    InputStream inputStream = entry.getValue();
                    helper.addAttachment(name, new ByteArrayResource(StreamCopyUtils.copyToByteArray(inputStream)));
                }
            }
            if (isHTML && inlineMap != null) {
                for (Map.Entry<String, InputStream> entry : inlineMap.entrySet()) {
                    String cuid = entry.getKey();
                    InputStream inputStream = entry.getValue();
                    helper.addInline(cuid, new ByteArrayResource(StreamCopyUtils.copyToByteArray(inputStream)), DEFAULT_CONTENT_TYPE);
                }
            }
            sendMessage(message);
        } catch (MessagingException | IOException e) {
            log.error("send mail error", e);
            throw new RuntimeException(e);
        }
    }

    private static void sendMessage(MimeMessage mimeMessage) {
        SENDER.send(mimeMessage);
    }

    private static void sendMessage(SimpleMailMessage simpleMailMessage) {
        SENDER.send(simpleMailMessage);
    }

     /*public void sendSimpleMail(String[] to, String[] cc, String[] bcc, String subject, String content) {
        Assert.notNull(to, "to array can not be null");
        Assert.notNull(subject, "subject can not be null");
        Assert.notNull(content, "content can not be null");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setCc(cc != null ? cc : EMPTY_ARRAY);
        message.setBcc(bcc != null ? bcc : EMPTY_ARRAY);
        message.setSubject(subject);
        message.setText(content);
        SENDER.send(message);
    }*/


}
