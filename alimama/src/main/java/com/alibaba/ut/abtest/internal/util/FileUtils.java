package com.alibaba.ut.abtest.internal.util;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

public final class FileUtils {
    private static final String TAG = "FileUtils";

    private FileUtils() {
    }

    public static boolean deleteFile(File file) {
        if (file == null || !file.exists()) {
            return false;
        }
        boolean z = true;
        if (!file.isDirectory()) {
            return file.delete() & true;
        }
        File[] listFiles = file.listFiles();
        if (listFiles != null) {
            for (File deleteFile : listFiles) {
                z &= deleteFile(deleteFile);
            }
        }
        return file.delete() & z;
    }

    public static String toString(File file, Charset charset) throws IOException {
        return new String(toByteArray(file), charset.name());
    }

    public static String toString(InputStream inputStream, Charset charset) throws IOException {
        return new String(toByteArray(inputStream), charset.name());
    }

    public static byte[] toByteArray(File file) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("the file is null.");
        } else if (file.length() > 2147483647L) {
            throw new IllegalArgumentException("the file is bigger than the largest possible byte array.");
        } else if (file.length() == 0) {
            return toByteArray((InputStream) new FileInputStream(file));
        } else {
            byte[] bArr = new byte[((int) file.length())];
            boolean z = true;
            FileInputStream fileInputStream = new FileInputStream(file);
            try {
                readFully(fileInputStream, bArr);
                z = false;
                return bArr;
            } finally {
                close(fileInputStream, z);
            }
        }
    }

    public static byte[] toByteArray(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        copy(inputStream, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static long copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] bArr = new byte[4096];
        long j = 0;
        while (true) {
            int read = inputStream.read(bArr);
            if (read == -1) {
                return j;
            }
            outputStream.write(bArr, 0, read);
            j += (long) read;
        }
    }

    public static void close(Closeable closeable, boolean z) throws IOException {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                if (z) {
                    LogUtils.logW(TAG, "IOException thrown while closing Closeable.", e);
                    return;
                }
                throw e;
            }
        }
    }

    public static void readFully(InputStream inputStream, byte[] bArr) throws IOException {
        readFully(inputStream, bArr, 0, bArr.length);
    }

    public static void readFully(InputStream inputStream, byte[] bArr, int i, int i2) throws IOException {
        if (read(inputStream, bArr, i, i2) != i2) {
            throw new EOFException();
        }
    }

    public static int read(InputStream inputStream, byte[] bArr, int i, int i2) throws IOException {
        if (i2 >= 0) {
            int i3 = 0;
            while (i3 < i2) {
                int read = inputStream.read(bArr, i + i3, i2 - i3);
                if (read == -1) {
                    break;
                }
                i3 += read;
            }
            return i3;
        }
        throw new IndexOutOfBoundsException("len is negative");
    }
}
