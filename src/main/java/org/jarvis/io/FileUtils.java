package org.jarvis.io;

import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @see org.springframework.util.FileCopyUtils
 */
public abstract class FileUtils {

    public static void copy(File in, File out) throws IOException {
        FileCopyUtils.copy(in, out);
    }

    public static byte[] copyToByteArray(File in) throws IOException {
        return FileCopyUtils.copyToByteArray(in);
    }

    public static String copyToString(File in, Charset charset) {
        try {
            return IOCopyUtils.copyToString(new FileInputStream(in), charset);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
