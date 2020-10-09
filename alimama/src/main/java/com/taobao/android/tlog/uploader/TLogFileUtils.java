package com.taobao.android.tlog.uploader;

public class TLogFileUtils {
    /* JADX WARNING: Removed duplicated region for block: B:37:0x0062  */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x006f A[SYNTHETIC, Splitter:B:42:0x006f] */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x009e  */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x00a3  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.io.File copyFile(java.io.File r11, java.io.File r12) throws java.lang.Exception {
        /*
            r0 = 0
        L_0x0001:
            r1 = 3
            r2 = 0
            if (r0 >= r1) goto L_0x00a7
            boolean r1 = r12.exists()     // Catch:{ Exception -> 0x005a, all -> 0x0057 }
            if (r1 != 0) goto L_0x001f
            java.io.File r1 = r12.getParentFile()     // Catch:{ Exception -> 0x005a, all -> 0x0057 }
            boolean r1 = r1.exists()     // Catch:{ Exception -> 0x005a, all -> 0x0057 }
            if (r1 != 0) goto L_0x001c
            java.io.File r1 = r12.getParentFile()     // Catch:{ Exception -> 0x005a, all -> 0x0057 }
            r1.mkdirs()     // Catch:{ Exception -> 0x005a, all -> 0x0057 }
        L_0x001c:
            r12.createNewFile()     // Catch:{ Exception -> 0x005a, all -> 0x0057 }
        L_0x001f:
            java.io.FileInputStream r1 = new java.io.FileInputStream     // Catch:{ Exception -> 0x005a, all -> 0x0057 }
            r1.<init>(r11)     // Catch:{ Exception -> 0x005a, all -> 0x0057 }
            java.nio.channels.FileChannel r1 = r1.getChannel()     // Catch:{ Exception -> 0x005a, all -> 0x0057 }
            java.io.FileOutputStream r3 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x0052, all -> 0x004e }
            r3.<init>(r12)     // Catch:{ Exception -> 0x0052, all -> 0x004e }
            java.nio.channels.FileChannel r9 = r3.getChannel()     // Catch:{ Exception -> 0x0052, all -> 0x004e }
            r4 = 0
            long r6 = r1.size()     // Catch:{ Exception -> 0x0049, all -> 0x0047 }
            r3 = r1
            r8 = r9
            r3.transferTo(r4, r6, r8)     // Catch:{ Exception -> 0x0049, all -> 0x0047 }
            if (r1 == 0) goto L_0x0041
            r1.close()
        L_0x0041:
            if (r9 == 0) goto L_0x0046
            r9.close()
        L_0x0046:
            return r12
        L_0x0047:
            r11 = move-exception
            goto L_0x0050
        L_0x0049:
            r2 = move-exception
            r10 = r2
            r2 = r1
            r1 = r10
            goto L_0x005c
        L_0x004e:
            r11 = move-exception
            r9 = r2
        L_0x0050:
            r2 = r1
            goto L_0x009c
        L_0x0052:
            r3 = move-exception
            r9 = r2
            r2 = r1
            r1 = r3
            goto L_0x005c
        L_0x0057:
            r11 = move-exception
            r9 = r2
            goto L_0x009c
        L_0x005a:
            r1 = move-exception
            r9 = r2
        L_0x005c:
            r12.delete()     // Catch:{ all -> 0x009b }
            r3 = 2
            if (r0 == r3) goto L_0x006f
            if (r2 == 0) goto L_0x0067
            r2.close()
        L_0x0067:
            if (r9 == 0) goto L_0x006c
            r9.close()
        L_0x006c:
            int r0 = r0 + 1
            goto L_0x0001
        L_0x006f:
            java.lang.Exception r0 = new java.lang.Exception     // Catch:{ all -> 0x009b }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x009b }
            r3.<init>()     // Catch:{ all -> 0x009b }
            java.lang.String r4 = "Failed to copy file[src="
            r3.append(r4)     // Catch:{ all -> 0x009b }
            java.lang.String r11 = r11.getAbsolutePath()     // Catch:{ all -> 0x009b }
            r3.append(r11)     // Catch:{ all -> 0x009b }
            java.lang.String r11 = ", dest="
            r3.append(r11)     // Catch:{ all -> 0x009b }
            java.lang.String r11 = r12.getAbsolutePath()     // Catch:{ all -> 0x009b }
            r3.append(r11)     // Catch:{ all -> 0x009b }
            java.lang.String r11 = "]"
            r3.append(r11)     // Catch:{ all -> 0x009b }
            java.lang.String r11 = r3.toString()     // Catch:{ all -> 0x009b }
            r0.<init>(r11, r1)     // Catch:{ all -> 0x009b }
            throw r0     // Catch:{ all -> 0x009b }
        L_0x009b:
            r11 = move-exception
        L_0x009c:
            if (r2 == 0) goto L_0x00a1
            r2.close()
        L_0x00a1:
            if (r9 == 0) goto L_0x00a6
            r9.close()
        L_0x00a6:
            throw r11
        L_0x00a7:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.tlog.uploader.TLogFileUtils.copyFile(java.io.File, java.io.File):java.io.File");
    }
}
