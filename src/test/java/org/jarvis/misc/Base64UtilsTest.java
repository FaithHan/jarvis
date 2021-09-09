package org.jarvis.misc;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Base64UtilsTest {

    @Test
    void encodeAndDecodeTest() {
        byte[] bytes = new byte[]{1, 2, 0, -4, 5, 127};
        String encode = Base64Utils.encode(bytes);
        byte[] decode = Base64Utils.decode(encode);
        Assertions.assertArrayEquals(bytes,decode);
    }

}