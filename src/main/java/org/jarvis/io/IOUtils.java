package org.jarvis.io;

import org.apache.commons.io.output.NullOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

public abstract class IOUtils {


    public static void closeSilent(InputStream inputStream) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException ignored) {

            }
        }
    }

    public static void closeSilent(OutputStream outputStream) {
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException ignored) {

            }
        }
    }

    public static void closeSilent(Writer writer) {
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException ignored) {

            }
        }
    }

    public static void closeSilent(Reader reader) {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException ignored) {

            }
        }
    }

    public static void consume(InputStream inputStream) throws IOException {
        StreamCopyUtils.copy(inputStream, new OutputStream() {
            @Override
            public void write(int b) throws IOException {

            }
        });
    }


}
