package org.jarvis.image;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.jarvis.io.IOUtils;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class CaptchaUtilsTest {

    @Test
    void getDefaultKaptcha() throws IOException {
        DefaultKaptcha defaultKaptcha = CaptchaUtils.getDefaultKaptcha();
        String text = defaultKaptcha.createText();
        BufferedImage bufferedImage = defaultKaptcha.createImage(text);
        ImageIO.write(bufferedImage, "jpg", new File("/Users/hanfei08/Desktop/a.jpg"));
    }
}