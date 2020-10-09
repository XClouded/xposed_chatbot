package com.ali.telescope.util;

import android.database.Cursor;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.zip.ZipFile;

public class IOUtils {
    public static void write(String str, OutputStream outputStream, Charset charset) throws IOException {
        if (str != null) {
            outputStream.write(str.getBytes(toCharset(charset)));
        }
    }

    public static long copy(InputStream inputStream, Writer writer, Charset charset) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, toCharset(charset));
        char[] cArr = new char[4096];
        long j = 0;
        while (true) {
            int read = inputStreamReader.read(cArr);
            if (-1 == read) {
                return j;
            }
            writer.write(cArr, 0, read);
            j += (long) read;
        }
    }

    public static String toString(InputStream inputStream, Charset charset) throws IOException {
        StringWriter stringWriter = new StringWriter();
        copy(inputStream, stringWriter, charset);
        return stringWriter.toString();
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                TelescopeLog.v("IOUtils", e);
            }
        }
    }

    public static void closeQuietly(Cursor cursor) {
        if (cursor != null) {
            try {
                cursor.close();
            } catch (Exception unused) {
            }
        }
    }

    public static void closeQuietly(Socket socket) {
        if (socket != null) {
            try {
                socket.close();
            } catch (Exception unused) {
            }
        }
    }

    public static void closeQuietly(ZipFile zipFile) {
        if (zipFile != null) {
            try {
                zipFile.close();
            } catch (Exception unused) {
            }
        }
    }

    public static Charset toCharset(Charset charset) {
        return charset == null ? Charset.defaultCharset() : charset;
    }
}
