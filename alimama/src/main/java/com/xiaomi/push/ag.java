package com.xiaomi.push;

import android.content.Context;
import android.os.Environment;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;

public class ag {
    private static final String a = (Environment.getExternalStorageDirectory().getPath() + "/mipush/");
    private static final String b = (a + "lcfp");
    private static final String c = (a + "lcfp.lock");

    public static boolean a(Context context, String str, long j) {
        RandomAccessFile randomAccessFile;
        FileLock fileLock = null;
        try {
            File file = new File(c);
            y.a(file);
            randomAccessFile = new RandomAccessFile(file, "rw");
            try {
                FileLock lock = randomAccessFile.getChannel().lock();
                try {
                    boolean b2 = b(context, str, j);
                    if (lock != null && lock.isValid()) {
                        try {
                            lock.release();
                        } catch (IOException unused) {
                        }
                    }
                    y.a((Closeable) randomAccessFile);
                    return b2;
                } catch (IOException e) {
                    e = e;
                    fileLock = lock;
                    try {
                        e.printStackTrace();
                        try {
                            fileLock.release();
                        } catch (IOException unused2) {
                        }
                        y.a((Closeable) randomAccessFile);
                        return true;
                    } catch (Throwable th) {
                        th = th;
                        if (fileLock != null && fileLock.isValid()) {
                            try {
                                fileLock.release();
                            } catch (IOException unused3) {
                            }
                        }
                        y.a((Closeable) randomAccessFile);
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    fileLock = lock;
                    fileLock.release();
                    y.a((Closeable) randomAccessFile);
                    throw th;
                }
            } catch (IOException e2) {
                e = e2;
                e.printStackTrace();
                if (fileLock != null && fileLock.isValid()) {
                    fileLock.release();
                }
                y.a((Closeable) randomAccessFile);
                return true;
            }
        } catch (IOException e3) {
            e = e3;
            randomAccessFile = null;
            e.printStackTrace();
            fileLock.release();
            y.a((Closeable) randomAccessFile);
            return true;
        } catch (Throwable th3) {
            th = th3;
            randomAccessFile = null;
            fileLock.release();
            y.a((Closeable) randomAccessFile);
            throw th;
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(3:28|29|34) */
    /* JADX WARNING: Code restructure failed: missing block: B:35:?, code lost:
        r3.clear();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x00bb, code lost:
        r0 = th;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:34:0x00b4 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static boolean b(android.content.Context r16, java.lang.String r17, long r18) {
        /*
            int r0 = android.os.Build.VERSION.SDK_INT
            r1 = 1
            r2 = 23
            if (r0 < r2) goto L_0x0012
            java.lang.String r0 = "android.permission.WRITE_EXTERNAL_STORAGE"
            r2 = r16
            boolean r0 = com.xiaomi.push.g.c(r2, r0)
            if (r0 != 0) goto L_0x0014
            return r1
        L_0x0012:
            r2 = r16
        L_0x0014:
            java.io.File r0 = new java.io.File
            java.lang.String r3 = b
            r0.<init>(r3)
            java.util.ArrayList r3 = new java.util.ArrayList
            r3.<init>()
            long r4 = java.lang.System.currentTimeMillis()
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            r7 = r17
            r6.append(r7)
            java.lang.String r8 = ":"
            r6.append(r8)
            java.lang.String r8 = r16.getPackageName()
            r6.append(r8)
            java.lang.String r8 = ","
            r6.append(r8)
            r6.append(r4)
            java.lang.String r6 = r6.toString()
            boolean r8 = r0.exists()
            r9 = 0
            if (r8 == 0) goto L_0x00c0
            java.io.BufferedReader r8 = new java.io.BufferedReader     // Catch:{ Exception -> 0x00b3, all -> 0x00b0 }
            java.io.FileReader r10 = new java.io.FileReader     // Catch:{ Exception -> 0x00b3, all -> 0x00b0 }
            r10.<init>(r0)     // Catch:{ Exception -> 0x00b3, all -> 0x00b0 }
            r8.<init>(r10)     // Catch:{ Exception -> 0x00b3, all -> 0x00b0 }
        L_0x0057:
            java.lang.String r10 = r8.readLine()     // Catch:{ Exception -> 0x00b4 }
            if (r10 == 0) goto L_0x00b7
            java.lang.String r11 = ":"
            java.lang.String[] r11 = r10.split(r11)     // Catch:{ Exception -> 0x00b4 }
            int r12 = r11.length     // Catch:{ Exception -> 0x00b4 }
            r13 = 2
            if (r12 == r13) goto L_0x0068
            goto L_0x0057
        L_0x0068:
            r12 = 0
            r14 = r11[r12]     // Catch:{ Exception -> 0x00b4 }
            java.lang.String r15 = java.lang.String.valueOf(r17)     // Catch:{ Exception -> 0x00b4 }
            boolean r14 = android.text.TextUtils.equals(r14, r15)     // Catch:{ Exception -> 0x00b4 }
            if (r14 == 0) goto L_0x00ac
            r10 = r11[r1]     // Catch:{ Exception -> 0x00b4 }
            java.lang.String r11 = ","
            java.lang.String[] r10 = r10.split(r11)     // Catch:{ Exception -> 0x00b4 }
            int r11 = r10.length     // Catch:{ Exception -> 0x00b4 }
            if (r11 == r13) goto L_0x0081
            goto L_0x0057
        L_0x0081:
            r11 = r10[r1]     // Catch:{ Exception -> 0x00b4 }
            long r13 = java.lang.Long.parseLong(r11)     // Catch:{ Exception -> 0x00b4 }
            r10 = r10[r12]     // Catch:{ Exception -> 0x00b4 }
            java.lang.String r11 = r16.getPackageName()     // Catch:{ Exception -> 0x00b4 }
            boolean r10 = android.text.TextUtils.equals(r10, r11)     // Catch:{ Exception -> 0x00b4 }
            if (r10 != 0) goto L_0x0057
            long r10 = r4 - r13
            long r10 = java.lang.Math.abs(r10)     // Catch:{ Exception -> 0x00b4 }
            float r10 = (float) r10
            r13 = 1000(0x3e8, double:4.94E-321)
            long r13 = r13 * r18
            float r11 = (float) r13
            r13 = 1063675494(0x3f666666, float:0.9)
            float r11 = r11 * r13
            int r10 = (r10 > r11 ? 1 : (r10 == r11 ? 0 : -1))
            if (r10 >= 0) goto L_0x0057
            com.xiaomi.push.y.a((java.io.Closeable) r8)
            return r12
        L_0x00ac:
            r3.add(r10)     // Catch:{ Exception -> 0x00b4 }
            goto L_0x0057
        L_0x00b0:
            r0 = move-exception
            r8 = r9
            goto L_0x00bc
        L_0x00b3:
            r8 = r9
        L_0x00b4:
            r3.clear()     // Catch:{ all -> 0x00bb }
        L_0x00b7:
            com.xiaomi.push.y.a((java.io.Closeable) r8)
            goto L_0x00c7
        L_0x00bb:
            r0 = move-exception
        L_0x00bc:
            com.xiaomi.push.y.a((java.io.Closeable) r8)
            throw r0
        L_0x00c0:
            boolean r2 = com.xiaomi.push.y.a((java.io.File) r0)
            if (r2 != 0) goto L_0x00c7
            return r1
        L_0x00c7:
            r3.add(r6)
            java.io.BufferedWriter r2 = new java.io.BufferedWriter     // Catch:{ IOException -> 0x00fa }
            java.io.FileWriter r4 = new java.io.FileWriter     // Catch:{ IOException -> 0x00fa }
            r4.<init>(r0)     // Catch:{ IOException -> 0x00fa }
            r2.<init>(r4)     // Catch:{ IOException -> 0x00fa }
            java.util.Iterator r0 = r3.iterator()     // Catch:{ IOException -> 0x00f4, all -> 0x00f2 }
        L_0x00d8:
            boolean r3 = r0.hasNext()     // Catch:{ IOException -> 0x00f4, all -> 0x00f2 }
            if (r3 == 0) goto L_0x00ee
            java.lang.Object r3 = r0.next()     // Catch:{ IOException -> 0x00f4, all -> 0x00f2 }
            java.lang.String r3 = (java.lang.String) r3     // Catch:{ IOException -> 0x00f4, all -> 0x00f2 }
            r2.write(r3)     // Catch:{ IOException -> 0x00f4, all -> 0x00f2 }
            r2.newLine()     // Catch:{ IOException -> 0x00f4, all -> 0x00f2 }
            r2.flush()     // Catch:{ IOException -> 0x00f4, all -> 0x00f2 }
            goto L_0x00d8
        L_0x00ee:
            com.xiaomi.push.y.a((java.io.Closeable) r2)
            goto L_0x0105
        L_0x00f2:
            r0 = move-exception
            goto L_0x0106
        L_0x00f4:
            r0 = move-exception
            r9 = r2
            goto L_0x00fb
        L_0x00f7:
            r0 = move-exception
            r2 = r9
            goto L_0x0106
        L_0x00fa:
            r0 = move-exception
        L_0x00fb:
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x00f7 }
            com.xiaomi.channel.commonutils.logger.b.d(r0)     // Catch:{ all -> 0x00f7 }
            com.xiaomi.push.y.a((java.io.Closeable) r9)
        L_0x0105:
            return r1
        L_0x0106:
            com.xiaomi.push.y.a((java.io.Closeable) r2)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.ag.b(android.content.Context, java.lang.String, long):boolean");
    }
}
