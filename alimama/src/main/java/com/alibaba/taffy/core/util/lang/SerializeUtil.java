package com.alibaba.taffy.core.util.lang;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class SerializeUtil {
    public static byte[] toBytes(Object obj) throws IOException {
        if (obj == null) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        new ObjectOutputStream(byteArrayOutputStream).writeObject(obj);
        byteArrayOutputStream.flush();
        return byteArrayOutputStream.toByteArray();
    }

    public static void writeTo(Object obj, OutputStream outputStream) throws IOException {
        if (obj != null) {
            new ObjectOutputStream(outputStream).writeObject(obj);
        }
    }

    public static <T> T parseObject(InputStream inputStream, Class<T> cls) throws IOException, ClassNotFoundException {
        if (inputStream != null) {
            return cls.cast(new ObjectInputStream(inputStream).readObject());
        }
        throw new IllegalArgumentException("The InputStream must not be null");
    }

    public static <T> T parseObject(byte[] bArr, Class<T> cls) throws IOException, ClassNotFoundException {
        if (bArr != null) {
            return cls.cast(new ObjectInputStream(new ByteArrayInputStream(bArr)).readObject());
        }
        throw new IllegalArgumentException("The data must not be null");
    }
}
