package org.jarvis.misc;

import java.io.File;

public class JVMUtils {

    private static final String USER_DIR_KEY = "user.dir";

    private static final String USER_HOME_KEY = "user.home";

    private static final String JAVA_IO_TMPDIR_KEY = "java.io.tmpdir";

    public static final String PATH_SEPARATOR = File.pathSeparator;

    public static final String LINE_SEPARATOR = System.lineSeparator();

    public static final String USER_DIR = System.getProperty(USER_DIR_KEY);

    public static final String USER_HOME = System.getProperty(USER_HOME_KEY);

    public static final String JAVA_IO_TMPDIR = System.getProperty(JAVA_IO_TMPDIR_KEY);

    public static final String JAVA_SPECIFICATION_VERSION = System.getProperty("java.specification.version");

    public static int availableProcessors() {
        return Runtime.getRuntime().availableProcessors();
    }

    /**
     * unit bytes
     * @return
     */
    public static long freeMemory() {
        return Runtime.getRuntime().freeMemory();
    }

    /**
     * unit bytes
     * @return
     */
    public static long totalMemory() {
        return Runtime.getRuntime().totalMemory();
    }

    /**
     * unit bytes
     * @return
     */
    public static long maxMemory() {
        return Runtime.getRuntime().maxMemory();
    }

}
