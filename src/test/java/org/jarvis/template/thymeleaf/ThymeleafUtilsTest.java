package org.jarvis.template.thymeleaf;

import org.junit.jupiter.api.Test;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;

import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;

class ThymeleafUtilsTest {

    @Test
    void render() {
        ThymeleafUtils.init("/templates/thymeleaf/", null, TemplateMode.HTML);
        Context context = new Context();
        context.setVariable("hello", "北京");
        StringWriter writer = new StringWriter();
        ThymeleafUtils.render("test.html", context, writer);
        System.out.println(writer);
    }

    @Test
    void render2() {
        ThymeleafUtils.init("/templates/thymeleaf/", null, TemplateMode.TEXT);
        Context context = new Context();
        context.setVariable("name", "北京");
        StringWriter writer = new StringWriter();
        ThymeleafUtils.render("test.th", context, writer);
        System.out.println(writer);
    }
}