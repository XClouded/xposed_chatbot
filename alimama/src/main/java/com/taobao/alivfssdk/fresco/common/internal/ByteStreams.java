package com.taobao.alivfssdk.fresco.common.internal;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public final class ByteStreams {
    private static final int BUF_SIZE = 4096;

    private static class BytesBuffer {
        private static final int MAX_RECYCLED = 2;
        private static final Object RECYCLER_LOCK = new Object();
        private static BytesBuffer sFirstRecycledEvent;
        private static int sRecycledCount;
        byte[] buf = new byte[4096];
        private BytesBuffer mNextRecycledEvent;

        private void reset() {
        }

        public static BytesBuffer obtain() {
            synchronized (RECYCLER_LOCK) {
                if (sFirstRecycledEvent == null) {
                    return new BytesBuffer();
                }
                BytesBuffer bytesBuffer = sFirstRecycledEvent;
                sFirstRecycledEvent = bytesBuffer.mNextRecycledEvent;
                bytesBuffer.mNextRecycledEvent = null;
                sRecycledCount--;
                return bytesBuffer;
            }
        }

        private BytesBuffer() {
        }

        public void recycle() {
            synchronized (RECYCLER_LOCK) {
                if (sRecycledCount < 2) {
                    reset();
                    sRecycledCount++;
                    if (sFirstRecycledEvent != null) {
                        this.mNextRecycledEvent = sFirstRecycledEvent;
                    }
                    sFirstRecycledEvent = this;
                }
            }
        }
    }

    private ByteStreams() {
    }

    public static long copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        Preconditions.checkNotNull(inputStream);
        Preconditions.checkNotNull(outputStream);
        BytesBuffer obtain = BytesBuffer.obtain();
        try {
            byte[] bArr = obtain.buf;
            long j = 0;
            while (true) {
                int read = inputStream.read(bArr);
                if (read == -1) {
                    return j;
                }
                outputStream.write(bArr, 0, read);
                j += (long) read;
            }
        } finally {
            obtain.recycle();
        }
    }

    public static int read(InputStream inputStream, byte[] bArr, int i, int i2) throws IOException {
        Preconditions.checkNotNull(inputStream);
        Preconditions.checkNotNull(bArr);
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

    public static byte[] toByteArray(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        copy(inputStream, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static byte[] toByteArray(InputStream inputStream, int i) throws IOException {
        byte[] bArr = new byte[i];
        int i2 = i;
        while (i2 > 0) {
            int i3 = i - i2;
            int read = inputStream.read(bArr, i3, i2);
            if (read == -1) {
                return Arrays.copyOf(bArr, i3);
            }
            i2 -= read;
        }
        int read2 = inputStream.read();
        if (read2 == -1) {
            return bArr;
        }
        FastByteArrayOutputStream fastByteArrayOutputStream = new FastByteArrayOutputStream();
        fastByteArrayOutputStream.write(read2);
        copy(inputStream, fastByteArrayOutputStream);
        byte[] bArr2 = new byte[(bArr.length + fastByteArrayOutputStream.size())];
        System.arraycopy(bArr, 0, bArr2, 0, bArr.length);
        fastByteArrayOutputStream.writeTo(bArr2, bArr.length);
        return bArr2;
    }

    private static final class FastByteArrayOutputStream extends ByteArrayOutputStream {
        private FastByteArrayOutputStream() {
        }

        /* access modifiers changed from: package-private */
        public void writeTo(byte[] bArr, int i) {
            System.arraycopy(this.buf, 0, bArr, i, this.count);
        }
    }

    public static void readFully(InputStream inputStream, byte[] bArr, int i, int i2) throws IOException {
        int read = read(inputStream, bArr, i, i2);
        if (read != i2) {
            throw new EOFException("reached end of stream after reading " + read + " bytes; " + i2 + " bytes expected");
        }
    }
}
