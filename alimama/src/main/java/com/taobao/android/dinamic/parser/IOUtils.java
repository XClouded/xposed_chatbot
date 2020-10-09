package com.taobao.android.dinamic.parser;

import java.io.IOException;
import java.io.OutputStream;

public class IOUtils {
    public static boolean write(OutputStream outputStream, byte[] bArr) throws IOException {
        try {
            outputStream.write(bArr);
            return true;
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:31:0x0044 A[SYNTHETIC, Splitter:B:31:0x0044] */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x004e A[SYNTHETIC, Splitter:B:36:0x004e] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static byte[] read(java.io.InputStream r5) throws java.io.IOException {
        /*
            r0 = 0
            if (r5 != 0) goto L_0x000e
            if (r5 == 0) goto L_0x000d
            r5.close()     // Catch:{ IOException -> 0x0009 }
            goto L_0x000d
        L_0x0009:
            r5 = move-exception
            r5.printStackTrace()
        L_0x000d:
            return r0
        L_0x000e:
            java.io.ByteArrayOutputStream r1 = new java.io.ByteArrayOutputStream     // Catch:{ all -> 0x003e }
            r2 = 2048(0x800, float:2.87E-42)
            r1.<init>(r2)     // Catch:{ all -> 0x003e }
            r0 = 1024(0x400, float:1.435E-42)
            byte[] r0 = new byte[r0]     // Catch:{ all -> 0x003c }
        L_0x0019:
            int r2 = r5.read(r0)     // Catch:{ all -> 0x003c }
            r3 = -1
            if (r2 == r3) goto L_0x0025
            r3 = 0
            r1.write(r0, r3, r2)     // Catch:{ all -> 0x003c }
            goto L_0x0019
        L_0x0025:
            byte[] r0 = r1.toByteArray()     // Catch:{ all -> 0x003c }
            r1.close()     // Catch:{ IOException -> 0x002d }
            goto L_0x0031
        L_0x002d:
            r1 = move-exception
            r1.printStackTrace()
        L_0x0031:
            if (r5 == 0) goto L_0x003b
            r5.close()     // Catch:{ IOException -> 0x0037 }
            goto L_0x003b
        L_0x0037:
            r5 = move-exception
            r5.printStackTrace()
        L_0x003b:
            return r0
        L_0x003c:
            r0 = move-exception
            goto L_0x0042
        L_0x003e:
            r1 = move-exception
            r4 = r1
            r1 = r0
            r0 = r4
        L_0x0042:
            if (r1 == 0) goto L_0x004c
            r1.close()     // Catch:{ IOException -> 0x0048 }
            goto L_0x004c
        L_0x0048:
            r1 = move-exception
            r1.printStackTrace()
        L_0x004c:
            if (r5 == 0) goto L_0x0056
            r5.close()     // Catch:{ IOException -> 0x0052 }
            goto L_0x0056
        L_0x0052:
            r5 = move-exception
            r5.printStackTrace()
        L_0x0056:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.dinamic.parser.IOUtils.read(java.io.InputStream):byte[]");
    }
}
