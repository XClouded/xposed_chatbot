package com.huawei.hianalytics.f.g;

import android.content.Context;
import android.taobao.windvane.jsbridge.api.WVFile;
import com.huawei.hianalytics.util.b;
import java.io.File;

public class d {
    private static final Object a = new Object();

    /* JADX WARNING: Removed duplicated region for block: B:43:0x0067 A[SYNTHETIC, Splitter:B:43:0x0067] */
    /* JADX WARNING: Removed duplicated region for block: B:58:0x007e A[SYNTHETIC, Splitter:B:58:0x007e] */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x008e A[SYNTHETIC, Splitter:B:68:0x008e] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String a(android.content.Context r9, java.lang.String r10) {
        /*
            boolean r0 = c(r9, r10)
            r1 = 0
            if (r0 != 0) goto L_0x0008
            return r1
        L_0x0008:
            java.lang.Object r0 = a
            monitor-enter(r0)
            com.huawei.hianalytics.util.a r2 = new com.huawei.hianalytics.util.a     // Catch:{ FileNotFoundException -> 0x0074, IOException -> 0x005d, all -> 0x005a }
            r3 = 2048(0x800, float:2.87E-42)
            r2.<init>(r3)     // Catch:{ FileNotFoundException -> 0x0074, IOException -> 0x005d, all -> 0x005a }
            java.lang.String r10 = d(r9, r10)     // Catch:{ FileNotFoundException -> 0x0074, IOException -> 0x005d, all -> 0x005a }
            java.io.FileInputStream r9 = r9.openFileInput(r10)     // Catch:{ FileNotFoundException -> 0x0074, IOException -> 0x005d, all -> 0x005a }
            byte[] r10 = new byte[r3]     // Catch:{ FileNotFoundException -> 0x0075, IOException -> 0x005e }
        L_0x001c:
            int r3 = r9.read(r10)     // Catch:{ FileNotFoundException -> 0x0075, IOException -> 0x005e }
            long r4 = (long) r3     // Catch:{ FileNotFoundException -> 0x0075, IOException -> 0x005e }
            r6 = -1
            int r8 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r8 == 0) goto L_0x002b
            r2.a(r10, r3)     // Catch:{ FileNotFoundException -> 0x0075, IOException -> 0x005e }
            goto L_0x001c
        L_0x002b:
            int r10 = r2.a()     // Catch:{ FileNotFoundException -> 0x0075, IOException -> 0x005e }
            if (r10 != 0) goto L_0x0040
            if (r9 == 0) goto L_0x003e
            r9.close()     // Catch:{ IOException -> 0x0037 }
            goto L_0x003e
        L_0x0037:
            java.lang.String r9 = "StorageUtil"
            java.lang.String r10 = "IOException happened when getInfoFromFile's FileOutputStream close"
            com.huawei.hianalytics.g.b.c(r9, r10)     // Catch:{ all -> 0x0092 }
        L_0x003e:
            monitor-exit(r0)     // Catch:{ all -> 0x0092 }
            return r1
        L_0x0040:
            java.lang.String r10 = new java.lang.String     // Catch:{ FileNotFoundException -> 0x0075, IOException -> 0x005e }
            byte[] r2 = r2.b()     // Catch:{ FileNotFoundException -> 0x0075, IOException -> 0x005e }
            java.lang.String r3 = "UTF-8"
            r10.<init>(r2, r3)     // Catch:{ FileNotFoundException -> 0x0075, IOException -> 0x005e }
            if (r9 == 0) goto L_0x0058
            r9.close()     // Catch:{ IOException -> 0x0051 }
            goto L_0x0058
        L_0x0051:
            java.lang.String r9 = "StorageUtil"
            java.lang.String r1 = "IOException happened when getInfoFromFile's FileOutputStream close"
            com.huawei.hianalytics.g.b.c(r9, r1)     // Catch:{ all -> 0x0092 }
        L_0x0058:
            monitor-exit(r0)     // Catch:{ all -> 0x0092 }
            return r10
        L_0x005a:
            r10 = move-exception
            r9 = r1
            goto L_0x008c
        L_0x005d:
            r9 = r1
        L_0x005e:
            java.lang.String r10 = "StorageUtil"
            java.lang.String r2 = "getInfoFromFile(): IOException"
            com.huawei.hianalytics.g.b.c(r10, r2)     // Catch:{ all -> 0x008b }
            if (r9 == 0) goto L_0x0072
            r9.close()     // Catch:{ IOException -> 0x006b }
            goto L_0x0072
        L_0x006b:
            java.lang.String r9 = "StorageUtil"
            java.lang.String r10 = "IOException happened when getInfoFromFile's FileOutputStream close"
            com.huawei.hianalytics.g.b.c(r9, r10)     // Catch:{ all -> 0x0092 }
        L_0x0072:
            monitor-exit(r0)     // Catch:{ all -> 0x0092 }
            return r1
        L_0x0074:
            r9 = r1
        L_0x0075:
            java.lang.String r10 = "StorageUtil"
            java.lang.String r2 = "getInfoFromFile(): is not found file"
            com.huawei.hianalytics.g.b.c(r10, r2)     // Catch:{ all -> 0x008b }
            if (r9 == 0) goto L_0x0089
            r9.close()     // Catch:{ IOException -> 0x0082 }
            goto L_0x0089
        L_0x0082:
            java.lang.String r9 = "StorageUtil"
            java.lang.String r10 = "IOException happened when getInfoFromFile's FileOutputStream close"
            com.huawei.hianalytics.g.b.c(r9, r10)     // Catch:{ all -> 0x0092 }
        L_0x0089:
            monitor-exit(r0)     // Catch:{ all -> 0x0092 }
            return r1
        L_0x008b:
            r10 = move-exception
        L_0x008c:
            if (r9 == 0) goto L_0x009b
            r9.close()     // Catch:{ IOException -> 0x0094 }
            goto L_0x009b
        L_0x0092:
            r9 = move-exception
            goto L_0x009c
        L_0x0094:
            java.lang.String r9 = "StorageUtil"
            java.lang.String r1 = "IOException happened when getInfoFromFile's FileOutputStream close"
            com.huawei.hianalytics.g.b.c(r9, r1)     // Catch:{ all -> 0x0092 }
        L_0x009b:
            throw r10     // Catch:{ all -> 0x0092 }
        L_0x009c:
            monitor-exit(r0)     // Catch:{ all -> 0x0092 }
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.huawei.hianalytics.f.g.d.a(android.content.Context, java.lang.String):java.lang.String");
    }

    public static void a(File file) {
        b.a(file);
    }

    public static void b(Context context, String str) {
        synchronized (a) {
            context.deleteFile(d(context, str));
        }
    }

    private static boolean c(Context context, String str) {
        File file = new File(context.getFilesDir(), d(context, str));
        if (!file.exists()) {
            com.huawei.hianalytics.g.b.b("HiAnalytics/event", "cached file not found");
            return false;
        }
        long length = file.length();
        if (length <= WVFile.FILE_MAX_SIZE) {
            return true;
        }
        com.huawei.hianalytics.g.b.b("HiAnalytics/event", "v1 cached file size overlarge - file len: %d limitedSize: %d", Long.valueOf(length), Long.valueOf(WVFile.FILE_MAX_SIZE));
        return false;
    }

    private static String d(Context context, String str) {
        String packageName = context.getPackageName();
        return "hianalytics_" + str + "_" + packageName;
    }
}
