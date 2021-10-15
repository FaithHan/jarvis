package org.jarvis.misc;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.stream.Stream;

@Slf4j
public class OSUtils {

    public static final String OS_NAME = System.getProperty("os.name");

    public static final String OS_VERSION = System.getProperty("os.version");

    public static final boolean IS_OS_WINDOWS = isOSNameMatch("Windows");

    public static final boolean IS_OS_MAC = isOSNameMatch("Mac");

    public static final boolean IS_OS_MAC_OSX = isOSNameMatch("Mac OS X");

    public static final boolean IS_OS_LINUX = isOSNameMatch("Linux") || isOSNameMatch("LINUX");

    private static final Runtime RUNTIME = Runtime.getRuntime();

    public static void browse(String url) {
        try {
            String finalUrl = url.startsWith("http") ? url : "http://" + url;
            if (IS_OS_MAC || IS_OS_MAC_OSX) {
                RUNTIME.exec(new String[]{"open", finalUrl});
            }
            if (IS_OS_WINDOWS) {
                RUNTIME.exec(new String[]{"explore", finalUrl});
            }
            if (IS_OS_LINUX) {
                Stream.of("google-chrome", "chrome", "firefox")
                        .filter(browser -> {
                            try {
                                return RUNTIME.exec(new String[]{"which", browser}).waitFor() == 0;
                            } catch (Exception e) {
                                return false;
                            }
                        })
                        .findFirst()
                        .ifPresent(browser -> {
                            try {
                                RUNTIME.exec(new String[]{browser, finalUrl});
                            } catch (IOException ignored) {

                            }
                        });
            }
        } catch (IOException e) {
            log.error("打开浏览器失败");
        }
    }

    private static boolean isOSNameMatch(final String osNamePrefix) {
        return OSUtils.OS_NAME.startsWith(osNamePrefix);
    }


}
