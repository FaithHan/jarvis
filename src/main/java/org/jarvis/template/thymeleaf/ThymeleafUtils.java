package org.jarvis.template.thymeleaf;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.IContext;
import org.thymeleaf.spring5.dialect.SpringStandardDialect;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.Writer;
import java.nio.charset.StandardCharsets;

/**
 * 官方文档：https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html
 * 官网：https://www.thymeleaf.org/
 * baeldung：https://www.baeldung.com/thymeleaf-in-spring-mvc
 * wiki：https://en.wikipedia.org/wiki/Thymeleaf
 */
public class ThymeleafUtils {

    private static TemplateEngine templateEngine;

    public static void init(String prefix, String suffix, TemplateMode templateMode) {
        TemplateEngine templateEngine = new TemplateEngine();
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix(prefix);
        templateResolver.setSuffix(suffix);
        templateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
        templateResolver.setTemplateMode(templateMode);
        templateEngine.setTemplateResolver(templateResolver);
        templateEngine.setDialect(new SpringStandardDialect());
        ThymeleafUtils.templateEngine = templateEngine;
    }

    public static void render(String templateName, IContext context, Writer writer) {
        ThymeleafUtils.templateEngine.process(templateName, context, writer);
    }
}
