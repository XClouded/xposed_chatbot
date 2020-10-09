package com.taobao.phenix.common;

import com.taobao.phenix.entity.EncodedData;
import com.taobao.phenix.loader.StreamResultHandler;
import com.taobao.phenix.request.ImageRequest;
import com.taobao.rxm.consume.Consumer;
import com.taobao.tcommon.core.BytesPool;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamUtil {
    /* JADX INFO: finally extract failed */
    public static EncodedData readBytes(InputStream inputStream, BytesPool bytesPool, int[] iArr) throws Exception {
        StreamResultHandler streamResultHandler = new StreamResultHandler((Consumer<?, ImageRequest>) null, iArr[0], 0);
        try {
            readBytes(inputStream, bytesPool, streamResultHandler);
            iArr[0] = streamResultHandler.getReadLength();
            return streamResultHandler.getEncodeData();
        } catch (Throwable th) {
            iArr[0] = streamResultHandler.getReadLength();
            throw th;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x0051 A[Catch:{ all -> 0x009a }] */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x0066 A[Catch:{ all -> 0x009a }, EDGE_INSN: B:57:0x0066->B:27:0x0066 ?: BREAK  
EDGE_INSN: B:58:0x0066->B:27:0x0066 ?: BREAK  ] */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0069 A[Catch:{ all -> 0x009a }] */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x008e  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void readBytes(java.io.InputStream r9, com.taobao.tcommon.core.BytesPool r10, com.taobao.phenix.loader.StreamResultHandler r11) throws java.lang.Exception {
        /*
            r0 = 8192(0x2000, float:1.14794E-41)
            if (r10 == 0) goto L_0x0009
            byte[] r0 = r10.offer(r0)
            goto L_0x000b
        L_0x0009:
            byte[] r0 = new byte[r0]
        L_0x000b:
            int r1 = r11.contentLength
            r2 = 0
            r3 = 1
            r4 = 0
            if (r1 <= 0) goto L_0x0037
            if (r10 == 0) goto L_0x001e
            int r1 = r11.contentLength     // Catch:{ OutOfMemoryError -> 0x0026 }
            byte[] r1 = r10.offer(r1)     // Catch:{ OutOfMemoryError -> 0x0026 }
            r8 = r2
            r2 = r1
            r1 = r8
            goto L_0x0038
        L_0x001e:
            java.io.ByteArrayOutputStream r1 = new java.io.ByteArrayOutputStream     // Catch:{ OutOfMemoryError -> 0x0026 }
            int r5 = r11.contentLength     // Catch:{ OutOfMemoryError -> 0x0026 }
            r1.<init>(r5)     // Catch:{ OutOfMemoryError -> 0x0026 }
            goto L_0x0038
        L_0x0026:
            java.lang.String r1 = "Stream"
            java.lang.String r5 = "allocate byte array OOM with content length=%d"
            java.lang.Object[] r6 = new java.lang.Object[r3]
            int r7 = r11.contentLength
            java.lang.Integer r7 = java.lang.Integer.valueOf(r7)
            r6[r4] = r7
            com.taobao.phenix.common.UnitedLog.e((java.lang.String) r1, (java.lang.String) r5, (java.lang.Object[]) r6)
        L_0x0037:
            r1 = r2
        L_0x0038:
            if (r2 != 0) goto L_0x0044
            if (r1 != 0) goto L_0x0044
            java.io.ByteArrayOutputStream r1 = new java.io.ByteArrayOutputStream
            r5 = 32768(0x8000, float:4.5918E-41)
            r1.<init>(r5)
        L_0x0044:
            int r5 = r9.read(r0)     // Catch:{ all -> 0x009a }
            r6 = -1
            if (r5 == r6) goto L_0x0066
            boolean r7 = r11.inLimit(r5)     // Catch:{ all -> 0x009a }
            if (r7 == 0) goto L_0x0066
            if (r1 == 0) goto L_0x0057
            r1.write(r0, r4, r5)     // Catch:{ all -> 0x009a }
            goto L_0x005e
        L_0x0057:
            int r7 = r11.getReadLength()     // Catch:{ all -> 0x009a }
            java.lang.System.arraycopy(r0, r4, r2, r7, r5)     // Catch:{ all -> 0x009a }
        L_0x005e:
            boolean r7 = r11.onProgressUpdate(r5)     // Catch:{ all -> 0x009a }
            if (r7 != 0) goto L_0x0044
            r7 = 1
            goto L_0x0067
        L_0x0066:
            r7 = 0
        L_0x0067:
            if (r7 != 0) goto L_0x008c
            if (r1 == 0) goto L_0x0070
            byte[] r1 = r1.toByteArray()     // Catch:{ all -> 0x009a }
            goto L_0x0071
        L_0x0070:
            r1 = r2
        L_0x0071:
            r11.setupData(r1)     // Catch:{ all -> 0x009a }
            if (r5 == r6) goto L_0x008b
            java.lang.String r1 = "Stream"
            java.lang.String r5 = "read bytes incorrect, exceed content-length=%d"
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch:{ all -> 0x0088 }
            int r11 = r11.contentLength     // Catch:{ all -> 0x0088 }
            java.lang.Integer r11 = java.lang.Integer.valueOf(r11)     // Catch:{ all -> 0x0088 }
            r3[r4] = r11     // Catch:{ all -> 0x0088 }
            com.taobao.phenix.common.UnitedLog.w(r1, r5, r3)     // Catch:{ all -> 0x0088 }
            goto L_0x008b
        L_0x0088:
            r11 = move-exception
            r3 = 0
            goto L_0x009b
        L_0x008b:
            r3 = 0
        L_0x008c:
            if (r10 == 0) goto L_0x0096
            r10.release(r0)
            if (r3 == 0) goto L_0x0096
            r10.release(r2)
        L_0x0096:
            r9.close()     // Catch:{ Throwable -> 0x0099 }
        L_0x0099:
            return
        L_0x009a:
            r11 = move-exception
        L_0x009b:
            if (r10 == 0) goto L_0x00a5
            r10.release(r0)
            if (r3 == 0) goto L_0x00a5
            r10.release(r2)
        L_0x00a5:
            r9.close()     // Catch:{ Throwable -> 0x00a8 }
        L_0x00a8:
            throw r11
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.phenix.common.StreamUtil.readBytes(java.io.InputStream, com.taobao.tcommon.core.BytesPool, com.taobao.phenix.loader.StreamResultHandler):void");
    }

    public static long copy(InputStream inputStream, OutputStream outputStream, BytesPool bytesPool) throws IOException {
        byte[] bArr;
        long j = 0;
        if (bytesPool != null) {
            bArr = bytesPool.offer(8192);
        } else {
            bArr = new byte[8192];
        }
        while (true) {
            try {
                int read = inputStream.read(bArr, 0, 8192);
                if (read == -1) {
                    break;
                }
                outputStream.write(bArr, 0, read);
                j += (long) read;
            } finally {
                if (bytesPool != null) {
                    bytesPool.release(bArr);
                }
            }
        }
        return j;
    }
}
