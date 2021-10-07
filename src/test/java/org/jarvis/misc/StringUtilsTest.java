package org.jarvis.misc;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class StringUtilsTest {

    @Test
    void toUnderline() {
        String underline = StringUtils.toUnderline("userProfile");
        Assertions.assertEquals("user_profile", underline);
    }
}