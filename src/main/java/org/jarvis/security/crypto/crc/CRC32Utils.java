package org.jarvis.security.crypto.crc;

import org.jarvis.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Objects;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.Checksum;

public class CRC32Utils {

    public static long checksumCRC32(final File file) throws IOException {
        return checksum(file, new CRC32()).getValue();
    }

    public static long checksumCRC32(InputStream inputStream) throws IOException {
        return checksum(inputStream, new CRC32()).getValue();
    }

    public static Checksum checksum(final File file, final Checksum checksum) throws IOException {
        Objects.requireNonNull(file, "file can not be null");
        Objects.requireNonNull(checksum, "checksum can not be null");
        checksum(Files.newInputStream(file.toPath()), checksum);
        return checksum;
    }

    public static Checksum checksum(final InputStream inputStream, final Checksum checksum) throws IOException {
        Objects.requireNonNull(inputStream, "inputStream can not be null");
        Objects.requireNonNull(checksum, "checksum can not be null");
        try (InputStream checkedInputStream = new CheckedInputStream(inputStream, checksum)) {
            IOUtils.consume(checkedInputStream);
        }
        return checksum;
    }
}
