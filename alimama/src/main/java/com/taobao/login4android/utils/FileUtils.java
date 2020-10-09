package com.taobao.login4android.utils;

public class FileUtils {
    /* JADX WARNING: Removed duplicated region for block: B:14:0x0021 A[SYNTHETIC, Splitter:B:14:0x0021] */
    /* JADX WARNING: Removed duplicated region for block: B:24:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void writeFileData(android.content.Context r2, java.lang.String r3, java.lang.String r4) {
        /*
            r0 = 0
            r1 = 0
            java.io.FileOutputStream r2 = r2.openFileOutput(r3, r0)     // Catch:{ Exception -> 0x0025, all -> 0x001d }
            java.lang.String r3 = "UTF-8"
            java.nio.charset.Charset r3 = java.nio.charset.Charset.forName(r3)     // Catch:{ Exception -> 0x001b, all -> 0x0019 }
            byte[] r3 = r4.getBytes(r3)     // Catch:{ Exception -> 0x001b, all -> 0x0019 }
            r2.write(r3)     // Catch:{ Exception -> 0x001b, all -> 0x0019 }
            if (r2 == 0) goto L_0x0029
        L_0x0015:
            r2.close()     // Catch:{ Exception -> 0x0029 }
            goto L_0x0029
        L_0x0019:
            r3 = move-exception
            goto L_0x001f
        L_0x001b:
            goto L_0x0026
        L_0x001d:
            r3 = move-exception
            r2 = r1
        L_0x001f:
            if (r2 == 0) goto L_0x0024
            r2.close()     // Catch:{ Exception -> 0x0024 }
        L_0x0024:
            throw r3
        L_0x0025:
            r2 = r1
        L_0x0026:
            if (r2 == 0) goto L_0x0029
            goto L_0x0015
        L_0x0029:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.login4android.utils.FileUtils.writeFileData(android.content.Context, java.lang.String, java.lang.String):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:?, code lost:
        r3.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x002d, code lost:
        if (r3 != null) goto L_0x001c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x001a, code lost:
        if (r3 != null) goto L_0x001c;
     */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x0028 A[SYNTHETIC, Splitter:B:16:0x0028] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String readFileData(android.content.Context r3, java.lang.String r4) {
        /*
            java.lang.String r0 = ""
            r1 = 0
            java.io.FileInputStream r3 = r3.openFileInput(r4)     // Catch:{ Exception -> 0x002c, all -> 0x0024 }
            int r4 = r3.available()     // Catch:{ Exception -> 0x0022, all -> 0x0020 }
            if (r4 <= 0) goto L_0x001a
            byte[] r4 = new byte[r4]     // Catch:{ Exception -> 0x0022, all -> 0x0020 }
            r3.read(r4)     // Catch:{ Exception -> 0x0022, all -> 0x0020 }
            java.lang.String r1 = new java.lang.String     // Catch:{ Exception -> 0x0022, all -> 0x0020 }
            java.lang.String r2 = "UTF-8"
            r1.<init>(r4, r2)     // Catch:{ Exception -> 0x0022, all -> 0x0020 }
            r0 = r1
        L_0x001a:
            if (r3 == 0) goto L_0x0030
        L_0x001c:
            r3.close()     // Catch:{ Exception -> 0x0030 }
            goto L_0x0030
        L_0x0020:
            r4 = move-exception
            goto L_0x0026
        L_0x0022:
            goto L_0x002d
        L_0x0024:
            r4 = move-exception
            r3 = r1
        L_0x0026:
            if (r3 == 0) goto L_0x002b
            r3.close()     // Catch:{ Exception -> 0x002b }
        L_0x002b:
            throw r4
        L_0x002c:
            r3 = r1
        L_0x002d:
            if (r3 == 0) goto L_0x0030
            goto L_0x001c
        L_0x0030:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.login4android.utils.FileUtils.readFileData(android.content.Context, java.lang.String):java.lang.String");
    }
}
