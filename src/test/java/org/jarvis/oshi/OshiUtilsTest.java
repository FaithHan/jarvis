package org.jarvis.oshi;

import org.junit.jupiter.api.Test;
import oshi.software.os.OperatingSystem;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class OshiUtilsTest {

    @Test
    void getOs() {
        OperatingSystem os = OshiUtils.getOs();
        System.out.println(os.getFamily());
        System.out.println(os.getProcessId());
        System.out.println(os.getVersionInfo());
        System.out.println(LocalDateTime.ofEpochSecond(os.getSystemBootTime(),0, ZoneOffset.ofHours(8)));
    }
}