package com.taobao.zcache.util;

public class FileUtils {
    /* JADX WARNING: Removed duplicated region for block: B:51:0x00f1 A[SYNTHETIC, Splitter:B:51:0x00f1] */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x00f9 A[Catch:{ IOException -> 0x00f5 }] */
    /* JADX WARNING: Removed duplicated region for block: B:58:0x00fe A[Catch:{ IOException -> 0x00f5 }] */
    /* JADX WARNING: Removed duplicated region for block: B:62:0x010e A[SYNTHETIC, Splitter:B:62:0x010e] */
    /* JADX WARNING: Removed duplicated region for block: B:67:0x0116 A[Catch:{ IOException -> 0x0112 }] */
    /* JADX WARNING: Removed duplicated region for block: B:69:0x011b A[Catch:{ IOException -> 0x0112 }] */
    /* JADX WARNING: Removed duplicated region for block: B:80:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String unzip(java.io.InputStream r9, java.lang.String r10) {
        /*
            java.lang.String r0 = "/"
            boolean r0 = r10.endsWith(r0)
            if (r0 != 0) goto L_0x0019
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            r0.append(r10)
            java.lang.String r10 = "/"
            r0.append(r10)
            java.lang.String r10 = r0.toString()
        L_0x0019:
            r0 = 0
            java.util.zip.ZipInputStream r1 = new java.util.zip.ZipInputStream     // Catch:{ Exception -> 0x00cf, all -> 0x00cc }
            r1.<init>(r9)     // Catch:{ Exception -> 0x00cf, all -> 0x00cc }
            r2 = 1024(0x400, float:1.435E-42)
            byte[] r3 = new byte[r2]     // Catch:{ Exception -> 0x00ca }
            java.lang.StringBuffer r4 = new java.lang.StringBuffer     // Catch:{ Exception -> 0x00ca }
            r5 = 200(0xc8, float:2.8E-43)
            r4.<init>(r5)     // Catch:{ Exception -> 0x00ca }
        L_0x002a:
            java.util.zip.ZipEntry r5 = r1.getNextEntry()     // Catch:{ Exception -> 0x00ca }
            if (r5 == 0) goto L_0x009a
            java.lang.String r6 = r5.getName()     // Catch:{ Exception -> 0x00ca }
            r4.append(r6)     // Catch:{ Exception -> 0x00ca }
            java.lang.String r6 = r4.toString()     // Catch:{ Exception -> 0x00ca }
            java.lang.String r7 = "../"
            boolean r6 = r6.contains(r7)     // Catch:{ Exception -> 0x00ca }
            if (r6 == 0) goto L_0x0044
            goto L_0x002a
        L_0x0044:
            java.io.File r6 = new java.io.File     // Catch:{ Exception -> 0x00ca }
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00ca }
            r7.<init>()     // Catch:{ Exception -> 0x00ca }
            r7.append(r10)     // Catch:{ Exception -> 0x00ca }
            java.lang.String r8 = r4.toString()     // Catch:{ Exception -> 0x00ca }
            r7.append(r8)     // Catch:{ Exception -> 0x00ca }
            java.lang.String r7 = r7.toString()     // Catch:{ Exception -> 0x00ca }
            r6.<init>(r7)     // Catch:{ Exception -> 0x00ca }
            int r7 = r4.length()     // Catch:{ Exception -> 0x00ca }
            r8 = 0
            r4.delete(r8, r7)     // Catch:{ Exception -> 0x00ca }
            boolean r5 = r5.isDirectory()     // Catch:{ Exception -> 0x00ca }
            if (r5 == 0) goto L_0x006e
            r6.mkdirs()     // Catch:{ Exception -> 0x00ca }
            goto L_0x002a
        L_0x006e:
            java.io.File r5 = r6.getParentFile()     // Catch:{ Exception -> 0x00ca }
            boolean r5 = r5.exists()     // Catch:{ Exception -> 0x00ca }
            if (r5 != 0) goto L_0x007f
            java.io.File r5 = r6.getParentFile()     // Catch:{ Exception -> 0x00ca }
            r5.mkdirs()     // Catch:{ Exception -> 0x00ca }
        L_0x007f:
            java.io.FileOutputStream r5 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x00ca }
            r5.<init>(r6)     // Catch:{ Exception -> 0x00ca }
        L_0x0084:
            int r0 = r1.read(r3, r8, r2)     // Catch:{ Exception -> 0x0097, all -> 0x0093 }
            if (r0 <= 0) goto L_0x008e
            r5.write(r3, r8, r0)     // Catch:{ Exception -> 0x0097, all -> 0x0093 }
            goto L_0x0084
        L_0x008e:
            r5.close()     // Catch:{ Exception -> 0x0097, all -> 0x0093 }
            r0 = r5
            goto L_0x002a
        L_0x0093:
            r10 = move-exception
            r0 = r5
            goto L_0x010c
        L_0x0097:
            r10 = move-exception
            r0 = r5
            goto L_0x00d1
        L_0x009a:
            java.lang.String r10 = "SUCCESS"
            if (r0 == 0) goto L_0x00a4
            r0.close()     // Catch:{ IOException -> 0x00a2 }
            goto L_0x00a4
        L_0x00a2:
            r9 = move-exception
            goto L_0x00ad
        L_0x00a4:
            r1.close()     // Catch:{ IOException -> 0x00a2 }
            if (r9 == 0) goto L_0x010a
            r9.close()     // Catch:{ IOException -> 0x00a2 }
            goto L_0x010a
        L_0x00ad:
            java.lang.String r10 = "ZCache3.0"
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
        L_0x00b4:
            java.lang.String r1 = "close Stream Exception:"
            r0.append(r1)
            java.lang.String r9 = r9.getMessage()
            r0.append(r9)
            java.lang.String r9 = r0.toString()
            com.taobao.zcache.log.ZLog.e(r10, r9)
            java.lang.String r10 = "unzip: IO close exception"
            goto L_0x010a
        L_0x00ca:
            r10 = move-exception
            goto L_0x00d1
        L_0x00cc:
            r10 = move-exception
            r1 = r0
            goto L_0x010c
        L_0x00cf:
            r10 = move-exception
            r1 = r0
        L_0x00d1:
            java.lang.String r2 = "ZCache3.0"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x010b }
            r3.<init>()     // Catch:{ all -> 0x010b }
            java.lang.String r4 = "unzip: IOException:"
            r3.append(r4)     // Catch:{ all -> 0x010b }
            java.lang.String r4 = r10.getMessage()     // Catch:{ all -> 0x010b }
            r3.append(r4)     // Catch:{ all -> 0x010b }
            java.lang.String r3 = r3.toString()     // Catch:{ all -> 0x010b }
            com.taobao.zcache.log.ZLog.e(r2, r3)     // Catch:{ all -> 0x010b }
            java.lang.String r10 = r10.getMessage()     // Catch:{ all -> 0x010b }
            if (r0 == 0) goto L_0x00f7
            r0.close()     // Catch:{ IOException -> 0x00f5 }
            goto L_0x00f7
        L_0x00f5:
            r9 = move-exception
            goto L_0x0102
        L_0x00f7:
            if (r1 == 0) goto L_0x00fc
            r1.close()     // Catch:{ IOException -> 0x00f5 }
        L_0x00fc:
            if (r9 == 0) goto L_0x010a
            r9.close()     // Catch:{ IOException -> 0x00f5 }
            goto L_0x010a
        L_0x0102:
            java.lang.String r10 = "ZCache3.0"
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            goto L_0x00b4
        L_0x010a:
            return r10
        L_0x010b:
            r10 = move-exception
        L_0x010c:
            if (r0 == 0) goto L_0x0114
            r0.close()     // Catch:{ IOException -> 0x0112 }
            goto L_0x0114
        L_0x0112:
            r9 = move-exception
            goto L_0x011f
        L_0x0114:
            if (r1 == 0) goto L_0x0119
            r1.close()     // Catch:{ IOException -> 0x0112 }
        L_0x0119:
            if (r9 == 0) goto L_0x0139
            r9.close()     // Catch:{ IOException -> 0x0112 }
            goto L_0x0139
        L_0x011f:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "close Stream Exception:"
            r0.append(r1)
            java.lang.String r9 = r9.getMessage()
            r0.append(r9)
            java.lang.String r9 = r0.toString()
            java.lang.String r0 = "ZCache3.0"
            com.taobao.zcache.log.ZLog.e(r0, r9)
        L_0x0139:
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.zcache.util.FileUtils.unzip(java.io.InputStream, java.lang.String):java.lang.String");
    }
}
