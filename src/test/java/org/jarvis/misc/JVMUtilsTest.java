package org.jarvis.misc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JVMUtilsTest {

    @Test
    void availableProcessors() {
        System.out.println(JVMUtils.availableProcessors());
    }

    @Test
    void line_separator() {
        System.out.println(JVMUtils.LINE_SEPARATOR);
        System.out.println(JVMUtils.USER_DIR);
        System.out.println(JVMUtils.USER_HOME);
        System.out.println(JVMUtils.JAVA_IO_TMPDIR);
    }

    @Test
    void memory_size() {
        long totalMemory = JVMUtils.totalMemory();
        StorageSize storageSize = StorageSize.ofBytes(totalMemory);
        System.out.println(storageSize.toBytes() + "Bytes");
        System.out.println(storageSize.toKilobytes() + "KB");
        System.out.println(storageSize.toMegabytes() + "MB");
        System.out.println(storageSize.toGigabytes() + "GB");
    }
}