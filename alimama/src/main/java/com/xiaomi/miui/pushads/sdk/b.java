package com.xiaomi.miui.pushads.sdk;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

class b {
    /* JADX WARNING: Removed duplicated region for block: B:19:0x004b A[SYNTHETIC, Splitter:B:19:0x004b] */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0056 A[SYNTHETIC, Splitter:B:26:0x0056] */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x0061  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static int a(android.content.Context r2, java.io.File r3, java.lang.String r4, com.xiaomi.miui.pushads.sdk.h r5) {
        /*
            java.lang.String r0 = a((java.lang.String) r4)
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r3 = r3.getAbsolutePath()
            r1.append(r3)
            java.lang.String r3 = "/"
            r1.append(r3)
            r1.append(r0)
            java.lang.String r3 = r1.toString()
            r0 = 0
            java.io.File r1 = new java.io.File     // Catch:{ Exception -> 0x0054, all -> 0x0048 }
            r1.<init>(r3)     // Catch:{ Exception -> 0x0054, all -> 0x0048 }
            boolean r1 = r1.exists()     // Catch:{ Exception -> 0x0054, all -> 0x0048 }
            if (r1 == 0) goto L_0x002b
            r2 = 0
            r4 = r0
            goto L_0x0038
        L_0x002b:
            java.lang.String r1 = "从sever 下载文件 debug 模式"
            com.xiaomi.miui.pushads.sdk.d.a(r1)     // Catch:{ Exception -> 0x0054, all -> 0x0048 }
            java.io.InputStream r4 = a((java.lang.String) r4)     // Catch:{ Exception -> 0x0054, all -> 0x0048 }
            int r2 = a(r2, r3, r4)     // Catch:{ Exception -> 0x0046, all -> 0x0043 }
        L_0x0038:
            if (r4 == 0) goto L_0x005f
            r4.close()     // Catch:{ IOException -> 0x003e }
            goto L_0x005f
        L_0x003e:
            r4 = move-exception
            r4.printStackTrace()
            goto L_0x005f
        L_0x0043:
            r2 = move-exception
            r0 = r4
            goto L_0x0049
        L_0x0046:
            r0 = r4
            goto L_0x0054
        L_0x0048:
            r2 = move-exception
        L_0x0049:
            if (r0 == 0) goto L_0x0053
            r0.close()     // Catch:{ IOException -> 0x004f }
            goto L_0x0053
        L_0x004f:
            r3 = move-exception
            r3.printStackTrace()
        L_0x0053:
            throw r2
        L_0x0054:
            if (r0 == 0) goto L_0x005e
            r0.close()     // Catch:{ IOException -> 0x005a }
            goto L_0x005e
        L_0x005a:
            r2 = move-exception
            r2.printStackTrace()
        L_0x005e:
            r2 = -1
        L_0x005f:
            if (r2 != 0) goto L_0x0064
            r5.a((java.lang.String) r3)
        L_0x0064:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.miui.pushads.sdk.b.a(android.content.Context, java.io.File, java.lang.String, com.xiaomi.miui.pushads.sdk.h):int");
    }

    /* JADX WARNING: Missing exception handler attribute for start block: B:40:0x007a */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x006f A[Catch:{ IOException -> 0x0073 }] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:18:0x004a=Splitter:B:18:0x004a, B:40:0x007a=Splitter:B:40:0x007a} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static int a(android.content.Context r7, java.lang.String r8, java.io.InputStream r9) {
        /*
            r0 = -1
            if (r9 != 0) goto L_0x0004
            return r0
        L_0x0004:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append(r8)
            java.lang.String r2 = "_"
            r1.append(r2)
            long r2 = java.lang.System.currentTimeMillis()
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            r2 = 0
            java.io.File r3 = new java.io.File     // Catch:{ Exception -> 0x0078, all -> 0x0063 }
            r3.<init>(r1)     // Catch:{ Exception -> 0x0078, all -> 0x0063 }
            java.io.FileOutputStream r1 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x0061, all -> 0x005e }
            r1.<init>(r3)     // Catch:{ Exception -> 0x0061, all -> 0x005e }
            int r2 = com.xiaomi.miui.pushads.sdk.i.a     // Catch:{ Exception -> 0x007a, all -> 0x005c }
            byte[] r2 = new byte[r2]     // Catch:{ Exception -> 0x007a, all -> 0x005c }
        L_0x002b:
            boolean r4 = com.xiaomi.miui.pushads.sdk.f.a((android.content.Context) r7)     // Catch:{ Exception -> 0x007a, all -> 0x005c }
            int r5 = r9.read(r2)     // Catch:{ Exception -> 0x007a, all -> 0x005c }
            r6 = 0
            if (r5 == r0) goto L_0x003c
            if (r4 == 0) goto L_0x003c
            r1.write(r2, r6, r5)     // Catch:{ Exception -> 0x007a, all -> 0x005c }
            goto L_0x002b
        L_0x003c:
            r1.flush()     // Catch:{ Exception -> 0x007a, all -> 0x005c }
            if (r5 != r0) goto L_0x004a
            java.io.File r7 = new java.io.File     // Catch:{ Exception -> 0x007a, all -> 0x005c }
            r7.<init>(r8)     // Catch:{ Exception -> 0x007a, all -> 0x005c }
            r3.renameTo(r7)     // Catch:{ Exception -> 0x007a, all -> 0x005c }
            r0 = 0
        L_0x004a:
            r1.close()     // Catch:{ IOException -> 0x0057 }
            boolean r7 = r3.exists()     // Catch:{ IOException -> 0x0057 }
            if (r7 == 0) goto L_0x0086
            r3.delete()     // Catch:{ IOException -> 0x0057 }
            goto L_0x0086
        L_0x0057:
            r7 = move-exception
            r7.printStackTrace()
            goto L_0x0086
        L_0x005c:
            r7 = move-exception
            goto L_0x0066
        L_0x005e:
            r7 = move-exception
            r1 = r2
            goto L_0x0066
        L_0x0061:
            r1 = r2
            goto L_0x007a
        L_0x0063:
            r7 = move-exception
            r1 = r2
            r3 = r1
        L_0x0066:
            r1.close()     // Catch:{ IOException -> 0x0073 }
            boolean r8 = r3.exists()     // Catch:{ IOException -> 0x0073 }
            if (r8 == 0) goto L_0x0077
            r3.delete()     // Catch:{ IOException -> 0x0073 }
            goto L_0x0077
        L_0x0073:
            r8 = move-exception
            r8.printStackTrace()
        L_0x0077:
            throw r7
        L_0x0078:
            r1 = r2
            r3 = r1
        L_0x007a:
            r1.close()     // Catch:{ IOException -> 0x0057 }
            boolean r7 = r3.exists()     // Catch:{ IOException -> 0x0057 }
            if (r7 == 0) goto L_0x0086
            r3.delete()     // Catch:{ IOException -> 0x0057 }
        L_0x0086:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.miui.pushads.sdk.b.a(android.content.Context, java.lang.String, java.io.InputStream):int");
    }

    private static InputStream a(String str) {
        try {
            return ((HttpURLConnection) new URL(str).openConnection()).getInputStream();
        } catch (IOException unused) {
            return null;
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    private static String m76a(String str) {
        int lastIndexOf = str.lastIndexOf("/");
        return str.substring(lastIndexOf < 0 ? 0 : lastIndexOf + 1);
    }
}
