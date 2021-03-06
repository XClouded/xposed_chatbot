package com.facebook.imagepipeline.platform;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.MemoryFile;
import com.facebook.common.internal.ByteStreams;
import com.facebook.common.internal.Closeables;
import com.facebook.common.internal.Throwables;
import com.facebook.common.memory.PooledByteBuffer;
import com.facebook.common.memory.PooledByteBufferInputStream;
import com.facebook.common.references.CloseableReference;
import com.facebook.common.streams.LimitedInputStream;
import com.facebook.imagepipeline.image.EncodedImage;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import javax.annotation.Nullable;

public class GingerbreadPurgeableDecoder extends DalvikPurgeableDecoder {
    private static Method sGetFileDescriptorMethod;

    public /* bridge */ /* synthetic */ CloseableReference decodeFromEncodedImage(EncodedImage encodedImage, Bitmap.Config config, @Nullable Rect rect) {
        return super.decodeFromEncodedImage(encodedImage, config, rect);
    }

    public /* bridge */ /* synthetic */ CloseableReference decodeJPEGFromEncodedImage(EncodedImage encodedImage, Bitmap.Config config, @Nullable Rect rect, int i) {
        return super.decodeJPEGFromEncodedImage(encodedImage, config, rect, i);
    }

    public /* bridge */ /* synthetic */ CloseableReference pinBitmap(Bitmap bitmap) {
        return super.pinBitmap(bitmap);
    }

    /* access modifiers changed from: protected */
    public Bitmap decodeByteArrayAsPurgeable(CloseableReference<PooledByteBuffer> closeableReference, BitmapFactory.Options options) {
        return decodeFileDescriptorAsPurgeable(closeableReference, closeableReference.get().size(), (byte[]) null, options);
    }

    /* access modifiers changed from: protected */
    public Bitmap decodeJPEGByteArrayAsPurgeable(CloseableReference<PooledByteBuffer> closeableReference, int i, BitmapFactory.Options options) {
        return decodeFileDescriptorAsPurgeable(closeableReference, i, endsWithEOI(closeableReference, i) ? null : EOI, options);
    }

    private static MemoryFile copyToMemoryFile(CloseableReference<PooledByteBuffer> closeableReference, int i, @Nullable byte[] bArr) throws IOException {
        OutputStream outputStream;
        PooledByteBufferInputStream pooledByteBufferInputStream;
        LimitedInputStream limitedInputStream;
        LimitedInputStream limitedInputStream2 = null;
        MemoryFile memoryFile = new MemoryFile((String) null, (bArr == null ? 0 : bArr.length) + i);
        memoryFile.allowPurging(false);
        try {
            pooledByteBufferInputStream = new PooledByteBufferInputStream(closeableReference.get());
            try {
                limitedInputStream = new LimitedInputStream(pooledByteBufferInputStream, i);
            } catch (Throwable th) {
                th = th;
                outputStream = null;
                CloseableReference.closeSafely((CloseableReference<?>) closeableReference);
                Closeables.closeQuietly((InputStream) pooledByteBufferInputStream);
                Closeables.closeQuietly((InputStream) limitedInputStream2);
                Closeables.close(outputStream, true);
                throw th;
            }
            try {
                outputStream = memoryFile.getOutputStream();
                try {
                    ByteStreams.copy(limitedInputStream, outputStream);
                    if (bArr != null) {
                        memoryFile.writeBytes(bArr, 0, i, bArr.length);
                    }
                    CloseableReference.closeSafely((CloseableReference<?>) closeableReference);
                    Closeables.closeQuietly((InputStream) pooledByteBufferInputStream);
                    Closeables.closeQuietly((InputStream) limitedInputStream);
                    Closeables.close(outputStream, true);
                    return memoryFile;
                } catch (Throwable th2) {
                    th = th2;
                    limitedInputStream2 = limitedInputStream;
                    CloseableReference.closeSafely((CloseableReference<?>) closeableReference);
                    Closeables.closeQuietly((InputStream) pooledByteBufferInputStream);
                    Closeables.closeQuietly((InputStream) limitedInputStream2);
                    Closeables.close(outputStream, true);
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                outputStream = null;
                limitedInputStream2 = limitedInputStream;
                CloseableReference.closeSafely((CloseableReference<?>) closeableReference);
                Closeables.closeQuietly((InputStream) pooledByteBufferInputStream);
                Closeables.closeQuietly((InputStream) limitedInputStream2);
                Closeables.close(outputStream, true);
                throw th;
            }
        } catch (Throwable th4) {
            th = th4;
            pooledByteBufferInputStream = null;
            outputStream = null;
            CloseableReference.closeSafely((CloseableReference<?>) closeableReference);
            Closeables.closeQuietly((InputStream) pooledByteBufferInputStream);
            Closeables.closeQuietly((InputStream) limitedInputStream2);
            Closeables.close(outputStream, true);
            throw th;
        }
    }

    private synchronized Method getFileDescriptorMethod() {
        if (sGetFileDescriptorMethod == null) {
            try {
                sGetFileDescriptorMethod = MemoryFile.class.getDeclaredMethod("getFileDescriptor", new Class[0]);
            } catch (Exception e) {
                throw Throwables.propagate(e);
            }
        }
        return sGetFileDescriptorMethod;
    }

    private FileDescriptor getMemoryFileDescriptor(MemoryFile memoryFile) {
        try {
            return (FileDescriptor) getFileDescriptorMethod().invoke(memoryFile, new Object[0]);
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x002d  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.graphics.Bitmap decodeFileDescriptorAsPurgeable(com.facebook.common.references.CloseableReference<com.facebook.common.memory.PooledByteBuffer> r2, int r3, byte[] r4, android.graphics.BitmapFactory.Options r5) {
        /*
            r1 = this;
            r0 = 0
            android.os.MemoryFile r2 = copyToMemoryFile(r2, r3, r4)     // Catch:{ IOException -> 0x0025 }
            java.io.FileDescriptor r3 = r1.getMemoryFileDescriptor(r2)     // Catch:{ IOException -> 0x001f, all -> 0x001d }
            com.facebook.common.webp.WebpBitmapFactory r4 = com.facebook.common.webp.WebpSupportStatus.sWebpBitmapFactory     // Catch:{ IOException -> 0x001f, all -> 0x001d }
            android.graphics.Bitmap r3 = r4.decodeFileDescriptor(r3, r0, r5)     // Catch:{ IOException -> 0x001f, all -> 0x001d }
            java.lang.String r4 = "BitmapFactory returned null"
            java.lang.Object r3 = com.facebook.common.internal.Preconditions.checkNotNull(r3, r4)     // Catch:{ IOException -> 0x001f, all -> 0x001d }
            android.graphics.Bitmap r3 = (android.graphics.Bitmap) r3     // Catch:{ IOException -> 0x001f, all -> 0x001d }
            if (r2 == 0) goto L_0x001c
            r2.close()
        L_0x001c:
            return r3
        L_0x001d:
            r3 = move-exception
            goto L_0x002b
        L_0x001f:
            r3 = move-exception
            r0 = r2
            goto L_0x0026
        L_0x0022:
            r3 = move-exception
            r2 = r0
            goto L_0x002b
        L_0x0025:
            r3 = move-exception
        L_0x0026:
            java.lang.RuntimeException r2 = com.facebook.common.internal.Throwables.propagate(r3)     // Catch:{ all -> 0x0022 }
            throw r2     // Catch:{ all -> 0x0022 }
        L_0x002b:
            if (r2 == 0) goto L_0x0030
            r2.close()
        L_0x0030:
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.imagepipeline.platform.GingerbreadPurgeableDecoder.decodeFileDescriptorAsPurgeable(com.facebook.common.references.CloseableReference, int, byte[], android.graphics.BitmapFactory$Options):android.graphics.Bitmap");
    }
}
