package com.huawei.updatesdk.sdk.a.d;

import com.huawei.updatesdk.sdk.a.c.a.a.a;
import java.io.Closeable;
import java.io.IOException;

public abstract class c {
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x0081, code lost:
        if (r1 != null) goto L_0x002f;
     */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x0053 A[Catch:{ NoSuchAlgorithmException -> 0x0078, FileNotFoundException -> 0x006c, IOException -> 0x0060, IllegalArgumentException -> 0x0054, IndexOutOfBoundsException -> 0x0048, all -> 0x0045, all -> 0x0085 }] */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x005f A[Catch:{ NoSuchAlgorithmException -> 0x0078, FileNotFoundException -> 0x006c, IOException -> 0x0060, IllegalArgumentException -> 0x0054, IndexOutOfBoundsException -> 0x0048, all -> 0x0045, all -> 0x0085 }] */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x006b A[Catch:{ NoSuchAlgorithmException -> 0x0078, FileNotFoundException -> 0x006c, IOException -> 0x0060, IllegalArgumentException -> 0x0054, IndexOutOfBoundsException -> 0x0048, all -> 0x0045, all -> 0x0085 }] */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x0077 A[Catch:{ NoSuchAlgorithmException -> 0x0078, FileNotFoundException -> 0x006c, IOException -> 0x0060, IllegalArgumentException -> 0x0054, IndexOutOfBoundsException -> 0x0048, all -> 0x0045, all -> 0x0085 }] */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x0088 A[SYNTHETIC, Splitter:B:55:0x0088] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String a(java.lang.String r8, java.lang.String r9) {
        /*
            r0 = 0
            if (r8 != 0) goto L_0x0004
            return r0
        L_0x0004:
            java.security.MessageDigest r9 = java.security.MessageDigest.getInstance(r9)     // Catch:{ NoSuchAlgorithmException -> 0x0078, FileNotFoundException -> 0x006c, IOException -> 0x0060, IllegalArgumentException -> 0x0054, IndexOutOfBoundsException -> 0x0048, all -> 0x0045 }
            java.io.FileInputStream r1 = new java.io.FileInputStream     // Catch:{ NoSuchAlgorithmException -> 0x0078, FileNotFoundException -> 0x006c, IOException -> 0x0060, IllegalArgumentException -> 0x0054, IndexOutOfBoundsException -> 0x0048, all -> 0x0045 }
            r1.<init>(r8)     // Catch:{ NoSuchAlgorithmException -> 0x0078, FileNotFoundException -> 0x006c, IOException -> 0x0060, IllegalArgumentException -> 0x0054, IndexOutOfBoundsException -> 0x0048, all -> 0x0045 }
            r8 = 1024(0x400, float:1.435E-42)
            byte[] r8 = new byte[r8]     // Catch:{ NoSuchAlgorithmException -> 0x0043, FileNotFoundException -> 0x0041, IOException -> 0x003f, IllegalArgumentException -> 0x003d, IndexOutOfBoundsException -> 0x003b }
            r2 = 0
            r4 = r2
        L_0x0014:
            int r6 = r1.read(r8)     // Catch:{ NoSuchAlgorithmException -> 0x0043, FileNotFoundException -> 0x0041, IOException -> 0x003f, IllegalArgumentException -> 0x003d, IndexOutOfBoundsException -> 0x003b }
            r7 = -1
            if (r6 == r7) goto L_0x0022
            r7 = 0
            r9.update(r8, r7, r6)     // Catch:{ NoSuchAlgorithmException -> 0x0043, FileNotFoundException -> 0x0041, IOException -> 0x003f, IllegalArgumentException -> 0x003d, IndexOutOfBoundsException -> 0x003b }
            long r6 = (long) r6     // Catch:{ NoSuchAlgorithmException -> 0x0043, FileNotFoundException -> 0x0041, IOException -> 0x003f, IllegalArgumentException -> 0x003d, IndexOutOfBoundsException -> 0x003b }
            long r4 = r4 + r6
            goto L_0x0014
        L_0x0022:
            int r8 = (r4 > r2 ? 1 : (r4 == r2 ? 0 : -1))
            if (r8 <= 0) goto L_0x002f
            byte[] r8 = r9.digest()     // Catch:{ NoSuchAlgorithmException -> 0x0043, FileNotFoundException -> 0x0041, IOException -> 0x003f, IllegalArgumentException -> 0x003d, IndexOutOfBoundsException -> 0x003b }
            java.lang.String r8 = com.huawei.updatesdk.sdk.a.d.b.a(r8)     // Catch:{ NoSuchAlgorithmException -> 0x0043, FileNotFoundException -> 0x0041, IOException -> 0x003f, IllegalArgumentException -> 0x003d, IndexOutOfBoundsException -> 0x003b }
            r0 = r8
        L_0x002f:
            r1.close()     // Catch:{ IOException -> 0x0033 }
            goto L_0x0084
        L_0x0033:
            java.lang.String r8 = "FileUtil"
            java.lang.String r9 = "Close FileInputStream failed!"
            com.huawei.updatesdk.sdk.a.c.a.a.a.d(r8, r9)
            goto L_0x0084
        L_0x003b:
            r8 = move-exception
            goto L_0x004a
        L_0x003d:
            r8 = move-exception
            goto L_0x0056
        L_0x003f:
            r8 = move-exception
            goto L_0x0062
        L_0x0041:
            r8 = move-exception
            goto L_0x006e
        L_0x0043:
            r8 = move-exception
            goto L_0x007a
        L_0x0045:
            r8 = move-exception
            r1 = r0
            goto L_0x0086
        L_0x0048:
            r8 = move-exception
            r1 = r0
        L_0x004a:
            java.lang.String r9 = "FileUtil"
            java.lang.String r2 = "getFileHashData IndexOutOfBoundsException"
            com.huawei.updatesdk.sdk.a.c.a.a.a.a(r9, r2, r8)     // Catch:{ all -> 0x0085 }
            if (r1 == 0) goto L_0x0084
            goto L_0x002f
        L_0x0054:
            r8 = move-exception
            r1 = r0
        L_0x0056:
            java.lang.String r9 = "FileUtil"
            java.lang.String r2 = "getFileHashData IllegalArgumentException"
            com.huawei.updatesdk.sdk.a.c.a.a.a.a(r9, r2, r8)     // Catch:{ all -> 0x0085 }
            if (r1 == 0) goto L_0x0084
            goto L_0x002f
        L_0x0060:
            r8 = move-exception
            r1 = r0
        L_0x0062:
            java.lang.String r9 = "FileUtil"
            java.lang.String r2 = "getFileHashData IOException"
            com.huawei.updatesdk.sdk.a.c.a.a.a.a(r9, r2, r8)     // Catch:{ all -> 0x0085 }
            if (r1 == 0) goto L_0x0084
            goto L_0x002f
        L_0x006c:
            r8 = move-exception
            r1 = r0
        L_0x006e:
            java.lang.String r9 = "FileUtil"
            java.lang.String r2 = "getFileHashData FileNotFoundException"
            com.huawei.updatesdk.sdk.a.c.a.a.a.a(r9, r2, r8)     // Catch:{ all -> 0x0085 }
            if (r1 == 0) goto L_0x0084
            goto L_0x002f
        L_0x0078:
            r8 = move-exception
            r1 = r0
        L_0x007a:
            java.lang.String r9 = "FileUtil"
            java.lang.String r2 = "getFileHashData NoSuchAlgorithmException"
            com.huawei.updatesdk.sdk.a.c.a.a.a.a(r9, r2, r8)     // Catch:{ all -> 0x0085 }
            if (r1 == 0) goto L_0x0084
            goto L_0x002f
        L_0x0084:
            return r0
        L_0x0085:
            r8 = move-exception
        L_0x0086:
            if (r1 == 0) goto L_0x0093
            r1.close()     // Catch:{ IOException -> 0x008c }
            goto L_0x0093
        L_0x008c:
            java.lang.String r9 = "FileUtil"
            java.lang.String r0 = "Close FileInputStream failed!"
            com.huawei.updatesdk.sdk.a.c.a.a.a.d(r9, r0)
        L_0x0093:
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.huawei.updatesdk.sdk.a.d.c.a(java.lang.String, java.lang.String):java.lang.String");
    }

    public static void a(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                a.a("FileUtil", "Closeable exception", e);
            }
        }
    }
}
