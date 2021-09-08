package org.jarvis.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

public abstract class IOUtils {


    public void closeSilent(InputStream inputStream) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException ignored) {

            }
        }
    }

    public void closeSilent(OutputStream outputStream) {
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException ignored) {

            }
        }
    }

    public void closeSilent(Writer writer) {
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException ignored) {

            }
        }
    }

    public void closeSilent(Reader reader) {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException ignored) {

            }
        }
    }


}
