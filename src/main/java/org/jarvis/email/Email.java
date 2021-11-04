package org.jarvis.email;

import org.apache.commons.lang3.StringUtils;
import org.jarvis.misc.Assert;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

public class Email {

    String[] to;
    String[] cc;
    String[] bcc;
    String subject;
    String content;
    PriorityEnum priority;
    Map<String, InputStream> attachmentMap;
    Map<String, InputStream> inlineMap;
    boolean isHtml;

    public static class EmailBuilder {
        private String[] to;
        private String[] cc;
        private String[] bcc;
        private String subject;
        private String content;
        private PriorityEnum priority;
        private final Map<String, InputStream> attachmentMap = new LinkedHashMap<>();
        private final Map<String, InputStream> inlineMap = new LinkedHashMap<>();
        private boolean isHtml;

        public EmailBuilder to(String... to) {
            this.to = to;
            return this;
        }

        public EmailBuilder cc(String... cc) {
            this.cc = cc;
            return this;
        }

        public EmailBuilder bcc(String... bcc) {
            this.bcc = bcc;
            return this;
        }

        public EmailBuilder subject(String subject) {
            this.subject = subject;
            return this;
        }

        public EmailBuilder content(String content) {
            this.content = content;
            return this;
        }

        public EmailBuilder priority(PriorityEnum priority) {
            this.priority = priority;
            return this;
        }

        public EmailBuilder addAttachment(String fileName, InputStream attachment) {
            Assert.isTrue(StringUtils.isNotBlank(fileName),"fileName can not be blank");
            this.attachmentMap.put(fileName, attachment);
            return this;
        }

        public EmailBuilder addInline(String cid, InputStream inline) {
            if (!this.isHtml) {
                throw new IllegalArgumentException("plain text mail can not add inline");
            }
            Assert.isTrue(StringUtils.isNotBlank(cid),"cid can not be blank");
            this.inlineMap.put(cid, inline);
            return this;
        }

        public Email build() {
            Email email = new Email();
            email.to = to;
            email.cc = cc;
            email.bcc = bcc;
            email.subject = subject;
            email.content = content;
            email.priority = priority;
            email.attachmentMap = attachmentMap;
            email.inlineMap = inlineMap;
            email.isHtml = isHtml;
            return email;
        }

    }


    public static EmailBuilder html() {
        EmailBuilder emailBuilder = new EmailBuilder();
        emailBuilder.isHtml = true;
        return emailBuilder;
    }

    public static EmailBuilder text() {
        return new EmailBuilder();
    }
}
