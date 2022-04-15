package org.jarvis.misc;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class StringUtilsTest {

    @Test
    void toUnderline() {
        String underline = StringUtils.toUnderline("userProfile");
        Assertions.assertEquals("user_profile", underline);
    }

    @Test
    void replaceTest() {
        Assertions.assertEquals("********",StringUtils.replace("12345678", '*', 0));
        Assertions.assertEquals("1*******",StringUtils.replace("12345678", '*', 1));
        Assertions.assertEquals("12******",StringUtils.replace("12345678", '*', 2));
        Assertions.assertEquals("1234567*",StringUtils.replace("12345678", '*', -1));
        Assertions.assertEquals("1*******",StringUtils.replace("12345678", '*', -7));


        Assertions.assertEquals("1******8",StringUtils.replace("12345678", '*', 1, -1));
        Assertions.assertEquals("********",StringUtils.replace("12345678", '*', 0, 8));
        Assertions.assertEquals("*******8",StringUtils.replace("12345678", '*', 0, -1));
        Assertions.assertEquals("1*****78",StringUtils.replace("12345678", '*', 1, -2));
        Assertions.assertEquals("1234567*",StringUtils.replace("12345678", '*', 7, 8));
        Assertions.assertEquals("1*******",StringUtils.replace("12345678", '*', 1, 8));
    }
}