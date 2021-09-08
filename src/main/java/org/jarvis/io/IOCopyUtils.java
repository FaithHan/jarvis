package org.jarvis.io;

import org.springframework.lang.Nullable;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public abstract class IOCopyUtils {

    public static byte[] copyToByteArray(@Nullable InputStream in) throws IOException {
        return StreamUtils.copyToByteArray(in);
    }

    public static String copyToString(@Nullable InputStream in) throws IOException {
        return StreamUtils.copyToString(in, StandardCharsets.UTF_8);
    }

    public static String copyToString(@Nullable InputStream in, Charset charset) throws IOException {
        return StreamUtils.copyToString(in, charset);
    }

    public static void copy(byte[] in, OutputStream out) throws IOException {
        StreamUtils.copy(in, out);
    }

    public static void copy(String in, OutputStream out) throws IOException {
        StreamUtils.copy(in, StandardCharsets.UTF_8, out);
    }

    public static void copy(String in, Charset charset, OutputStream out) throws IOException {
        StreamUtils.copy(in, charset, out);
    }

    public static int copy(InputStream in, OutputStream out) throws IOException {
        return StreamUtils.copy(in, out);
    }
}
