package org.jarvis.file.pdf;


import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import org.jarvis.io.StreamCopyUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 中文字体问题：
 * https://stackoverflow.com/questions/29237980/itext-pdf-not-displaying-chinese-characters-when-using-noto-fonts-or-source-hans
 * 字体包：
 * https://www.google.com/get/noto/
 * demo:
 * https://blog.csdn.net/i_will_try/article/details/81220100?utm_medium=distribute.pc_relevant.none-task-blog-2~default~baidujs_baidulandingword~default-0.no_search_link&spm=1001.2101.3001.4242
 * https://www.baeldung.com/pdf-conversions-java
 * https://blog.csdn.net/qq_37137530/article/details/107517045
 */
public class PdfUtils {


    private static final String ORIG = "/Users/hanfei08/IdeaProjects/jarvis/htmlpdf-test.html";
    private static final String OUTPUT_FOLDER = "/Users/hanfei08/IdeaProjects/jarvis/";

    private static final String FONT1 = "/Users/hanfei08/Downloads/Noto-unhinted/NotoSerifCJKkr-Bold.otf";
    private static final String FONT2 = "/Users/hanfei08/Downloads/Noto-unhinted/NotoSerifCJKjp-Light.otf";
    private static final String FONT3 = "/Users/hanfei08/Downloads/Noto-unhinted/NotoSansCJKkr-Black.otf";
    private static final String FONT4 = "/Users/hanfei08/Downloads/Noto-unhinted/NotoSansDisplay-SemiCondensedThinItalic.ttf";

    public static void main(String[] args) throws IOException {
        // https://stackoverflow.com/questions/53760790/how-to-load-resources-from-classpath-in-itext7
        org.apache.catalina.webresources.TomcatURLStreamHandlerFactory.register();

        ConverterProperties props = new ConverterProperties();
        props.setBaseUri("classpath:/templates/");

        DefaultFontProvider defaultFontProvider =
                new DefaultFontProvider(false, false, false);

        defaultFontProvider.addFont(StreamCopyUtils.copyToByteArray(new FileInputStream(FONT1)));
        defaultFontProvider.addFont(StreamCopyUtils.copyToByteArray(new FileInputStream(FONT2)));
        defaultFontProvider.addFont(StreamCopyUtils.copyToByteArray(new FileInputStream(FONT3)));
        defaultFontProvider.addFont(StreamCopyUtils.copyToByteArray(new FileInputStream(FONT4)));

//        defaultFontProvider.addDirectory("/Users/hanfei08/Downloads/Noto-unhinted/");
        props.setFontProvider(defaultFontProvider);
        File htmlSource = new File(ORIG);
        File pdfDest = new File(OUTPUT_FOLDER + "output.pdf");
        HtmlConverter.convertToPdf(new FileInputStream(htmlSource), new FileOutputStream(pdfDest),props);
    }


}
