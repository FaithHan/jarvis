package org.jarvis.security.crypto.crc;

import org.jarvis.io.FileCopyUtils;
import org.jarvis.io.StreamCopyUtils;
import org.junit.jupiter.api.Test;
import org.springframework.util.StreamUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class CRC32UtilsTest {

    @Test
    void checksumCRC32() throws IOException {
        long crc32 = CRC32Utils.checksumCRC32(new File(""));
        System.out.println(crc32);
    }

    @Test
    void testChecksumCRC32() throws IOException {
        byte[] bytes = FileCopyUtils.copyToByteArray(new File(""));
        long crc32 = CRC32Utils.checksumCRC32(new ByteArrayInputStream(bytes));
        System.out.println(crc32);
    }

}