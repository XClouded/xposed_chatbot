package com.taobao.downloader.util;

import com.alibaba.taffy.core.util.codec.MessageDigestAlgorithms;
import java.security.MessageDigest;

public class Md5Util {
    private static final char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /* JADX WARNING: Removed duplicated region for block: B:42:0x0073 A[SYNTHETIC, Splitter:B:42:0x0073] */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x007d A[SYNTHETIC, Splitter:B:47:0x007d] */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x008a A[SYNTHETIC, Splitter:B:55:0x008a] */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x0094 A[SYNTHETIC, Splitter:B:60:0x0094] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean isMd5Same(java.lang.String r6, java.lang.String r7) {
        /*
            boolean r0 = android.text.TextUtils.isEmpty(r6)
            if (r0 == 0) goto L_0x0008
            r6 = 1
            return r6
        L_0x0008:
            java.lang.String r6 = r6.toLowerCase()
            r0 = 0
            if (r7 != 0) goto L_0x0010
            return r0
        L_0x0010:
            r1 = 0
            java.lang.String r2 = "MD5"
            java.security.MessageDigest r2 = java.security.MessageDigest.getInstance(r2)     // Catch:{ Exception -> 0x006c, all -> 0x0068 }
            java.io.FileInputStream r3 = new java.io.FileInputStream     // Catch:{ Exception -> 0x006c, all -> 0x0068 }
            r3.<init>(r7)     // Catch:{ Exception -> 0x006c, all -> 0x0068 }
            java.nio.channels.FileChannel r7 = r3.getChannel()     // Catch:{ Exception -> 0x0064, all -> 0x0061 }
            r1 = 102400(0x19000, float:1.43493E-40)
            java.nio.ByteBuffer r1 = java.nio.ByteBuffer.allocate(r1)     // Catch:{ Exception -> 0x005f, all -> 0x005d }
        L_0x0027:
            int r4 = r7.read(r1)     // Catch:{ Exception -> 0x005f, all -> 0x005d }
            r5 = -1
            if (r4 == r5) goto L_0x003e
            byte[] r5 = r1.array()     // Catch:{ Exception -> 0x005f, all -> 0x005d }
            r2.update(r5, r0, r4)     // Catch:{ Exception -> 0x005f, all -> 0x005d }
            r1.position(r0)     // Catch:{ Exception -> 0x005f, all -> 0x005d }
            r4 = 1
            java.lang.Thread.sleep(r4)     // Catch:{ Exception -> 0x005f, all -> 0x005d }
            goto L_0x0027
        L_0x003e:
            byte[] r1 = r2.digest()     // Catch:{ Exception -> 0x005f, all -> 0x005d }
            java.lang.String r1 = byteToHexString(r1)     // Catch:{ Exception -> 0x005f, all -> 0x005d }
            boolean r6 = r1.equals(r6)     // Catch:{ Exception -> 0x005f, all -> 0x005d }
            r3.close()     // Catch:{ IOException -> 0x004e }
            goto L_0x0052
        L_0x004e:
            r0 = move-exception
            r0.printStackTrace()
        L_0x0052:
            if (r7 == 0) goto L_0x005c
            r7.close()     // Catch:{ IOException -> 0x0058 }
            goto L_0x005c
        L_0x0058:
            r7 = move-exception
            r7.printStackTrace()
        L_0x005c:
            return r6
        L_0x005d:
            r6 = move-exception
            goto L_0x0088
        L_0x005f:
            r6 = move-exception
            goto L_0x0066
        L_0x0061:
            r6 = move-exception
            r7 = r1
            goto L_0x0088
        L_0x0064:
            r6 = move-exception
            r7 = r1
        L_0x0066:
            r1 = r3
            goto L_0x006e
        L_0x0068:
            r6 = move-exception
            r7 = r1
            r3 = r7
            goto L_0x0088
        L_0x006c:
            r6 = move-exception
            r7 = r1
        L_0x006e:
            r6.printStackTrace()     // Catch:{ all -> 0x0086 }
            if (r1 == 0) goto L_0x007b
            r1.close()     // Catch:{ IOException -> 0x0077 }
            goto L_0x007b
        L_0x0077:
            r6 = move-exception
            r6.printStackTrace()
        L_0x007b:
            if (r7 == 0) goto L_0x0085
            r7.close()     // Catch:{ IOException -> 0x0081 }
            goto L_0x0085
        L_0x0081:
            r6 = move-exception
            r6.printStackTrace()
        L_0x0085:
            return r0
        L_0x0086:
            r6 = move-exception
            r3 = r1
        L_0x0088:
            if (r3 == 0) goto L_0x0092
            r3.close()     // Catch:{ IOException -> 0x008e }
            goto L_0x0092
        L_0x008e:
            r0 = move-exception
            r0.printStackTrace()
        L_0x0092:
            if (r7 == 0) goto L_0x009c
            r7.close()     // Catch:{ IOException -> 0x0098 }
            goto L_0x009c
        L_0x0098:
            r7 = move-exception
            r7.printStackTrace()
        L_0x009c:
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.downloader.util.Md5Util.isMd5Same(java.lang.String, java.lang.String):boolean");
    }

    public static String getTextMd5(String str) {
        try {
            return byteToHexString(MessageDigest.getInstance(MessageDigestAlgorithms.MD5).digest(str.getBytes()));
        } catch (Throwable unused) {
            return "";
        }
    }

    private static final String byteToHexString(byte[] bArr) {
        char[] cArr = new char[32];
        int i = 0;
        for (int i2 = 0; i2 < 16; i2++) {
            byte b = bArr[i2];
            int i3 = i + 1;
            cArr[i] = hexDigits[(b >>> 4) & 15];
            i = i3 + 1;
            cArr[i3] = hexDigits[b & 15];
        }
        return new String(cArr);
    }
}
