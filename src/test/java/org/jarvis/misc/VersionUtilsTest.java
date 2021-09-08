package org.jarvis.misc;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class VersionUtilsTest {

    @Test
    void should_return_result_when_version_test() {
        Assertions.assertTrue(VersionUtils.isValid("1"));
        Assertions.assertTrue(VersionUtils.isValid("1.2"));
        Assertions.assertTrue(VersionUtils.isValid("1.2.3"));
        Assertions.assertTrue(VersionUtils.isValid("1.0.2"));

        Assertions.assertFalse(VersionUtils.isValid("1.02.3"));
        Assertions.assertFalse(VersionUtils.isValid("1.02.3-"));
        Assertions.assertFalse(VersionUtils.isValid("1.2.3.4"));
        Assertions.assertFalse(VersionUtils.isValid(null));
        Assertions.assertFalse(VersionUtils.isValid(""));
    }

    @Test
    void should_return_result_when_compare_version() {
        Assertions.assertEquals(0, VersionUtils.compare("1.2.3", "1.2.3"));
        Assertions.assertEquals(0, VersionUtils.compare("4", "4.0.0"));
        Assertions.assertEquals(0, VersionUtils.compare("5.0", "5.0.0"));

        Assertions.assertEquals(1, VersionUtils.compare("4.2", "1.2.3"));

        Assertions.assertEquals(-1, VersionUtils.compare("1.2.3", "2"));
        Assertions.assertEquals(-1, VersionUtils.compare("1.2.3", "1.2.31"));

        Assertions.assertThrows(IllegalArgumentException.class, () -> VersionUtils.compare("1.2.3.4", "1.2.31"));

    }

}