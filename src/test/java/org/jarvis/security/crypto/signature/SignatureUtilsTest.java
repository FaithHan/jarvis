package org.jarvis.security.crypto.signature;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SignatureUtilsTest {

    static final String text = "百度网讯";

    static final String text2 = "百度网讯有限公司";

    @Test
    void verify_with_RSA_256() {
        byte[] sign = SignatureUtils.sign(text.getBytes(StandardCharsets.UTF_8));
        boolean verifyResult = SignatureUtils.verify(text.getBytes(StandardCharsets.UTF_8), sign);
        assertTrue(verifyResult);
    }

    @Test
    void verify_false_with_RSA_256() {
        byte[] sign = SignatureUtils.sign(text.getBytes(StandardCharsets.UTF_8));
        boolean verifyResult = SignatureUtils.verify(text2.getBytes(StandardCharsets.UTF_8), sign);
        assertFalse(verifyResult);
    }

    @Test
    void verify_with_RSA_512() {
        SignatureUtils.config(SignatureAlgorithm.RS_521);
        byte[] sign = SignatureUtils.sign(text.getBytes(StandardCharsets.UTF_8));
        boolean verifyResult = SignatureUtils.verify(text.getBytes(StandardCharsets.UTF_8), sign);
        assertTrue(verifyResult);
    }
}