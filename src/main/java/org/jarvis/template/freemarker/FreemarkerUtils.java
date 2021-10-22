package org.jarvis.template.freemarker;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

/**
 * 中文文档: http://freemarker.foofun.cn/
 * 英文文档: https://freemarker.apache.org/
 * Github: https://github.com/apache/freemarker
 * wiki: https://en.wikipedia.org/wiki/FreeMarker
 * Demo: https://www.vogella.com/tutorials/FreeMarker/article.html
 */
public class FreemarkerUtils {

    private static Configuration configuration;

    static {
        initWithClassPath("/templates");
    }

    /**
     *  like /templates
     *
     * @param classPath
     */
    public static void initWithClassPath(String classPath) {
        /* ------------------------------------------------------------------------ */
        /* You should do this ONLY ONCE in the whole application life-cycle:        */

        /* Create and adjust the configuration singleton */
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);

        // classPath路径问题：https://www.cnblogs.com/yejg1212/p/3270152.html
        cfg.setClassForTemplateLoading(FreemarkerUtils.class, classPath);
        cfg.setDefaultEncoding(StandardCharsets.UTF_8.name());
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
        cfg.setTemplateLoader(cfg.getTemplateLoader());
        configuration = cfg;
    }

    public static void initWithFilePath(File dir) throws IOException {
        /* ------------------------------------------------------------------------ */
        /* You should do this ONLY ONCE in the whole application life-cycle:        */

        /* Create and adjust the configuration singleton */
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);

        cfg.setDirectoryForTemplateLoading(dir);
        cfg.setDefaultEncoding(StandardCharsets.UTF_8.name());
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
        cfg.setTemplateLoader(cfg.getTemplateLoader());
        configuration = cfg;
    }

    public void render(String templateName, Object model, OutputStream outputStream) throws TemplateException, IOException {
        Template temp = FreemarkerUtils.configuration.getTemplate(templateName);
        /* Merge data-model with template */
        Writer out = new OutputStreamWriter(outputStream);
        temp.process(model, out);
    }

}
