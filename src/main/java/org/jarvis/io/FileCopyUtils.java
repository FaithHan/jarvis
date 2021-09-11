package org.jarvis.io;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @see org.springframework.util.FileCopyUtils
 */
public abstract class FileCopyUtils {

    public static void copy(File in, File out) throws IOException {
        org.springframework.util.FileCopyUtils.copy(in, out);
    }

    public static void copy(byte[] bytes, File out) throws IOException {
        org.springframework.util.FileCopyUtils.copy(bytes, out);
    }

    public static byte[] copyToByteArray(File in) throws IOException {
        return org.springframework.util.FileCopyUtils.copyToByteArray(in);
    }

    public static String copyToString(File in, Charset charset) {
        try {
            return StreamCopyUtils.copyToString(new FileInputStream(in), charset);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
