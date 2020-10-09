package com.alibaba.aliweex.interceptor.utils;

import androidx.annotation.Nullable;
import com.alibaba.aliweex.interceptor.NetworkEventReporterProxy;
import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterOutputStream;

public class RequestBodyUtil {
    protected static final String DEFLATE_ENCODING = "deflate";
    protected static final String GZIP_ENCODING = "gzip";
    private ByteArrayOutputStream mDeflatedOutput;
    private CountingOutputStream mDeflatingOutput;
    private final NetworkEventReporterProxy mEventReporter;
    private final String mRequestId;

    public RequestBodyUtil(NetworkEventReporterProxy networkEventReporterProxy, String str) {
        this.mEventReporter = networkEventReporterProxy;
        this.mRequestId = str;
    }

    public OutputStream createBodySink(@Nullable String str) throws IOException {
        OutputStream outputStream;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if ("gzip".equals(str)) {
            outputStream = GZipOutputStream.create(byteArrayOutputStream);
        } else {
            outputStream = DEFLATE_ENCODING.equals(str) ? new InflaterOutputStream(byteArrayOutputStream) : byteArrayOutputStream;
        }
        this.mDeflatingOutput = new CountingOutputStream(outputStream);
        this.mDeflatedOutput = byteArrayOutputStream;
        return this.mDeflatingOutput;
    }

    public byte[] getDisplayBody() {
        throwIfNoBody();
        return this.mDeflatedOutput.toByteArray();
    }

    public boolean hasBody() {
        return this.mDeflatedOutput != null;
    }

    public void reportDataSent() {
        throwIfNoBody();
        this.mEventReporter.dataSent(this.mRequestId, this.mDeflatedOutput.size(), (int) this.mDeflatingOutput.getCount());
    }

    private void throwIfNoBody() {
        if (!hasBody()) {
            throw new IllegalStateException("No body found; has createBodySink been called?");
        }
    }

    private class CountingOutputStream extends FilterOutputStream {
        private long mCount;

        public CountingOutputStream(OutputStream outputStream) {
            super(outputStream);
        }

        public long getCount() {
            return this.mCount;
        }

        public void write(int i) throws IOException {
            this.out.write(i);
            this.mCount++;
        }

        public void write(byte[] bArr) throws IOException {
            write(bArr, 0, bArr.length);
        }

        public void write(byte[] bArr, int i, int i2) throws IOException {
            this.out.write(bArr, i, i2);
            this.mCount += (long) i2;
        }
    }

    static class GZipOutputStream extends FilterOutputStream {
        private static final ExecutorService sExecutor = Executors.newCachedThreadPool();
        private final Future<Void> mCopyFuture;

        public static GZipOutputStream create(OutputStream outputStream) throws IOException {
            PipedInputStream pipedInputStream = new PipedInputStream();
            return new GZipOutputStream(new PipedOutputStream(pipedInputStream), sExecutor.submit(new GunzippingCallable(pipedInputStream, outputStream)));
        }

        private GZipOutputStream(OutputStream outputStream, Future<Void> future) throws IOException {
            super(outputStream);
            this.mCopyFuture = future;
        }

        public void close() throws IOException {
            try {
                super.close();
                try {
                } catch (IOException e) {
                    throw e;
                }
            } finally {
                try {
                    getAndRethrow(this.mCopyFuture);
                } catch (IOException unused) {
                }
            }
        }

        /* JADX WARNING: Can't wrap try/catch for region: R(3:0|1|2) */
        /* JADX WARNING: Code restructure failed: missing block: B:3:0x0005, code lost:
            r0 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:4:0x0006, code lost:
            r0 = r0.getCause();
            propagateIfInstanceOf(r0, java.io.IOException.class);
            propagate(r0);
         */
        /* JADX WARNING: Missing exception handler attribute for start block: B:0:0x0000 */
        /* JADX WARNING: Removed duplicated region for block: B:0:0x0000 A[LOOP:0: B:0:0x0000->B:4:0x0006, LOOP_START, MTH_ENTER_BLOCK, SYNTHETIC, Splitter:B:0:0x0000] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private static <T> T getAndRethrow(java.util.concurrent.Future<T> r2) throws java.io.IOException {
            /*
            L_0x0000:
                java.lang.Object r0 = r2.get()     // Catch:{ InterruptedException -> 0x0000, ExecutionException -> 0x0005 }
                return r0
            L_0x0005:
                r0 = move-exception
                java.lang.Throwable r0 = r0.getCause()
                java.lang.Class<java.io.IOException> r1 = java.io.IOException.class
                propagateIfInstanceOf(r0, r1)
                propagate(r0)
                goto L_0x0000
            */
            throw new UnsupportedOperationException("Method not decompiled: com.alibaba.aliweex.interceptor.utils.RequestBodyUtil.GZipOutputStream.getAndRethrow(java.util.concurrent.Future):java.lang.Object");
        }

        private static class GunzippingCallable implements Callable<Void> {
            private final InputStream mIn;
            private final OutputStream mOut;

            public GunzippingCallable(InputStream inputStream, OutputStream outputStream) {
                this.mIn = inputStream;
                this.mOut = outputStream;
            }

            /* JADX INFO: finally extract failed */
            public Void call() throws IOException {
                GZIPInputStream gZIPInputStream = new GZIPInputStream(this.mIn);
                try {
                    GZipOutputStream.copy(gZIPInputStream, this.mOut, new byte[1024]);
                    gZIPInputStream.close();
                    this.mOut.close();
                    return null;
                } catch (Throwable th) {
                    gZIPInputStream.close();
                    this.mOut.close();
                    throw th;
                }
            }
        }

        private static <T extends Throwable> void propagateIfInstanceOf(Throwable th, Class<T> cls) throws Throwable {
            if (cls.isInstance(th)) {
                throw th;
            }
        }

        private static RuntimeException propagate(Throwable th) {
            propagateIfInstanceOf(th, Error.class);
            propagateIfInstanceOf(th, RuntimeException.class);
            throw new RuntimeException(th);
        }

        /* access modifiers changed from: private */
        public static void copy(InputStream inputStream, OutputStream outputStream, byte[] bArr) throws IOException {
            while (true) {
                int read = inputStream.read(bArr);
                if (read != -1) {
                    outputStream.write(bArr, 0, read);
                } else {
                    return;
                }
            }
        }
    }
}
