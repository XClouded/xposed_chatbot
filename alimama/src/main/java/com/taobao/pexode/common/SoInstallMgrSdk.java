package com.taobao.pexode.common;

import android.content.Context;
import java.io.File;
import java.io.IOException;

public class SoInstallMgrSdk {
    static Context sContext;

    public static void init(Context context) {
        sContext = context;
    }

    public static boolean loadBackup(String str, int i) {
        boolean z;
        String targetSoFile = targetSoFile(str, i);
        File file = new File(targetSoFile);
        if (file.exists()) {
            try {
                System.load(targetSoFile);
                z = true;
            } catch (Throwable unused) {
                file.delete();
            }
            if (!z && !NdkCore.isCpuAbiSupported("mips") && !NdkCore.isCpuAbiSupported("x86")) {
                return unzipTargetAndLoad(str, i);
            }
            return z;
        }
        z = false;
        try {
            return unzipTargetAndLoad(str, i);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static String targetSoFile(String str, int i) {
        String str2;
        if (sContext == null) {
            return "";
        }
        File filesDir = sContext.getFilesDir();
        if (filesDir == null) {
            str2 = "/data/data/" + sContext.getPackageName() + "/files";
        } else {
            str2 = filesDir.getPath();
        }
        return str2 + "/lib" + str + "_bk_" + i + ".so";
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(10:20|21|(2:22|(2:24|25)(1:61))|(2:27|28)|29|30|31|32|(3:35|36|37)|40) */
    /* JADX WARNING: Can't wrap try/catch for region: R(8:42|43|44|(0)|(0)|54|55|56) */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:29:0x0078 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:31:0x007b */
    /* JADX WARNING: Missing exception handler attribute for start block: B:54:0x009e */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x0080 A[SYNTHETIC, Splitter:B:35:0x0080] */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x0094 A[SYNTHETIC, Splitter:B:48:0x0094] */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x009b A[SYNTHETIC, Splitter:B:52:0x009b] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static boolean unzipTargetAndLoad(java.lang.String r6, int r7) throws java.io.IOException {
        /*
            android.content.Context r0 = sContext
            r1 = 0
            if (r0 != 0) goto L_0x0006
            return r1
        L_0x0006:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r2 = "lib/armeabi/lib"
            r0.append(r2)
            r0.append(r6)
            java.lang.String r2 = ".so"
            r0.append(r2)
            java.lang.String r0 = r0.toString()
            java.lang.String r6 = targetSoFile(r6, r7)
            java.lang.String r7 = ""
            android.content.Context r2 = sContext
            android.content.pm.ApplicationInfo r2 = r2.getApplicationInfo()
            if (r2 == 0) goto L_0x002c
            java.lang.String r7 = r2.sourceDir
        L_0x002c:
            java.util.zip.ZipFile r2 = new java.util.zip.ZipFile
            r2.<init>(r7)
            java.util.Enumeration r7 = r2.entries()
        L_0x0035:
            boolean r3 = r7.hasMoreElements()
            if (r3 == 0) goto L_0x00a2
            java.lang.Object r3 = r7.nextElement()
            java.util.zip.ZipEntry r3 = (java.util.zip.ZipEntry) r3
            java.lang.String r4 = r3.getName()
            boolean r4 = r4.startsWith(r0)
            if (r4 == 0) goto L_0x0035
            r7 = 0
            java.io.File r0 = new java.io.File     // Catch:{ all -> 0x0090 }
            r0.<init>(r6)     // Catch:{ all -> 0x0090 }
            boolean r4 = r0.exists()     // Catch:{ all -> 0x0090 }
            if (r4 == 0) goto L_0x005a
            r0.delete()     // Catch:{ all -> 0x0090 }
        L_0x005a:
            java.io.InputStream r3 = r2.getInputStream(r3)     // Catch:{ all -> 0x0090 }
            java.io.FileOutputStream r4 = new java.io.FileOutputStream     // Catch:{ all -> 0x008c }
            r4.<init>(r0)     // Catch:{ all -> 0x008c }
            r7 = 1024(0x400, float:1.435E-42)
            byte[] r7 = new byte[r7]     // Catch:{ all -> 0x008a }
            r0 = 0
        L_0x0068:
            int r5 = r3.read(r7)     // Catch:{ all -> 0x008a }
            if (r5 <= 0) goto L_0x0073
            r4.write(r7, r1, r5)     // Catch:{ all -> 0x008a }
            int r0 = r0 + r5
            goto L_0x0068
        L_0x0073:
            if (r3 == 0) goto L_0x0078
            r3.close()     // Catch:{ Exception -> 0x0078 }
        L_0x0078:
            r4.close()     // Catch:{ Exception -> 0x007b }
        L_0x007b:
            r2.close()     // Catch:{ Exception -> 0x007e }
        L_0x007e:
            if (r0 <= 0) goto L_0x0089
            java.lang.System.load(r6)     // Catch:{ Throwable -> 0x0085 }
            r6 = 1
            return r6
        L_0x0085:
            r6 = move-exception
            r6.printStackTrace()
        L_0x0089:
            return r1
        L_0x008a:
            r6 = move-exception
            goto L_0x008e
        L_0x008c:
            r6 = move-exception
            r4 = r7
        L_0x008e:
            r7 = r3
            goto L_0x0092
        L_0x0090:
            r6 = move-exception
            r4 = r7
        L_0x0092:
            if (r7 == 0) goto L_0x0099
            r7.close()     // Catch:{ Exception -> 0x0098 }
            goto L_0x0099
        L_0x0098:
        L_0x0099:
            if (r4 == 0) goto L_0x009e
            r4.close()     // Catch:{ Exception -> 0x009e }
        L_0x009e:
            r2.close()     // Catch:{ Exception -> 0x00a1 }
        L_0x00a1:
            throw r6
        L_0x00a2:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.pexode.common.SoInstallMgrSdk.unzipTargetAndLoad(java.lang.String, int):boolean");
    }
}
