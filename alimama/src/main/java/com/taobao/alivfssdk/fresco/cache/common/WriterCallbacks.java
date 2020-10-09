package com.taobao.alivfssdk.fresco.cache.common;

import com.taobao.alivfssdk.fresco.common.internal.ByteStreams;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class WriterCallbacks {
    public static WriterCallback from(final InputStream inputStream) {
        return new WriterCallback() {
            public OutputStream write(OutputStream outputStream) throws IOException {
                ByteStreams.copy(inputStream, outputStream);
                return outputStream;
            }
        };
    }

    public static WriterCallback from(final byte[] bArr) {
        return new WriterCallback() {
            public OutputStream write(OutputStream outputStream) throws IOException {
                outputStream.write(bArr);
                return outputStream;
            }
        };
    }
}
