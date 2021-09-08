package org.jarvis.email;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class MailUtils {

    private static final JavaMailSender javaMailSender;

    static {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("mail.baidu.com");
        mailSender.setPort(25);
        mailSender.setUsername("xxxx");
        mailSender.setPassword("xxxx");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        javaMailSender = mailSender;
    }

    public void sendSimpleMail(String[] to, String[] cc, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("xxxx@baidu.com");
        message.setTo(to);
        message.setCc(cc);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
    }

    public static void sendMailWithAttachment(String[] to, String[] cc, String subject,
                                              String text, List<File> attachments, Map<String, File> inlineMap) {

        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true, StandardCharsets.UTF_8.displayName());
            helper.setFrom("xxxx@baidu.com");
            helper.setTo(to);
            helper.setCc(cc);
            helper.setSubject(subject);
            helper.setText(text, true);
            attachments.forEach(file -> {
                try {
                    helper.addAttachment(file.getName(), file);
                } catch (MessagingException ignored) {

                }
            });
            inlineMap.forEach((cid,file) -> {
                try {
                    helper.addInline(cid, file);
                } catch (MessagingException ignored) {

                }
            });
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        javaMailSender.send(message);
    }

}
