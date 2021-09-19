package org.jarvis.misc;

import org.jarvis.codec.HexUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

class HexUtilsTest {

    @Test
    void toHexTest() {
        byte[] bytes = new byte[]{1, 2, 0, -4, 5, 127};
        Assertions.assertEquals("010200fc057f", HexUtils.toHexString(bytes));
    }

    @Test
    void toBytesTest() {
        byte[] bytes = new byte[]{1, 2, 0, -4, 5, 127};
        Assertions.assertArrayEquals(bytes, HexUtils.toBytes("010200fc057f"));
    }

    @Test
    void allTest() {
        String string = "Jarvis开源工具库";
        byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
        String hex = HexUtils.toHexString(bytes);
        Assertions.assertEquals(string,new String(HexUtils.toBytes(hex), StandardCharsets.UTF_8));
    }

    @Test
    void is_hex_string_valid_test() {
        Assertions.assertTrue(HexUtils.isValidHexString("123f"));
        Assertions.assertTrue(HexUtils.isValidHexString("12F3D0"));

        Assertions.assertFalse(HexUtils.isValidHexString("123"));
        Assertions.assertFalse(HexUtils.isValidHexString("123O"));
    }
}