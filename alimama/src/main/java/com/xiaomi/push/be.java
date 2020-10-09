package com.xiaomi.push;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;
import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.clientreport.manager.a;
import java.io.File;
import java.util.Arrays;
import java.util.List;

public class be {
    public static String a() {
        return Build.VERSION.RELEASE + "-" + Build.VERSION.INCREMENTAL;
    }

    public static String a(Context context) {
        String a = bh.a(context).a("sp_client_report_status", "sp_client_report_key", "");
        if (!TextUtils.isEmpty(a)) {
            return a;
        }
        String a2 = ay.a(20);
        bh.a(context).a("sp_client_report_status", "sp_client_report_key", a2);
        return a2;
    }

    public static void a(Context context, String str) {
        Intent intent = new Intent("com.xiaomi.xmsf.push.XMSF_UPLOAD_ACTIVE");
        intent.putExtra("pkgname", context.getPackageName());
        intent.putExtra("category", "category_client_report_data");
        intent.putExtra("name", "quality_support");
        intent.putExtra("data", str);
        context.sendBroadcast(intent, "com.xiaomi.xmsf.permission.USE_XMSF_UPLOAD");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:67:0x00f0, code lost:
        if (r7 != null) goto L_0x00f2;
     */
    /* JADX WARNING: Removed duplicated region for block: B:80:0x010f  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void a(android.content.Context r11, java.lang.String r12, java.lang.String r13) {
        /*
            java.io.File r13 = r11.getExternalFilesDir(r13)
            if (r13 == 0) goto L_0x012f
            boolean r0 = r13.exists()
            if (r0 != 0) goto L_0x000f
            r13.mkdirs()
        L_0x000f:
            java.io.File r11 = r11.getExternalFilesDir(r12)
            if (r11 == 0) goto L_0x012f
            boolean r12 = r11.exists()
            if (r12 != 0) goto L_0x001f
            r11.mkdirs()
            return
        L_0x001f:
            com.xiaomi.push.bf r12 = new com.xiaomi.push.bf
            r12.<init>()
            java.io.File[] r11 = r11.listFiles(r12)
            if (r11 == 0) goto L_0x012f
            int r12 = r11.length
            if (r12 > 0) goto L_0x002f
            goto L_0x012f
        L_0x002f:
            long r0 = java.lang.System.currentTimeMillis()
            int r12 = r11.length
            r2 = 0
            r3 = 0
            r4 = r3
            r5 = r4
        L_0x0038:
            if (r2 >= r12) goto L_0x012f
            r6 = r11[r2]
            if (r6 == 0) goto L_0x0113
            java.lang.String r7 = r6.getAbsolutePath()     // Catch:{ Exception -> 0x00d5, all -> 0x00d0 }
            boolean r7 = android.text.TextUtils.isEmpty(r7)     // Catch:{ Exception -> 0x00d5, all -> 0x00d0 }
            if (r7 == 0) goto L_0x004a
            goto L_0x0113
        L_0x004a:
            java.io.File r7 = new java.io.File     // Catch:{ Exception -> 0x00d5, all -> 0x00d0 }
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00d5, all -> 0x00d0 }
            r8.<init>()     // Catch:{ Exception -> 0x00d5, all -> 0x00d0 }
            java.lang.String r9 = r6.getAbsolutePath()     // Catch:{ Exception -> 0x00d5, all -> 0x00d0 }
            r8.append(r9)     // Catch:{ Exception -> 0x00d5, all -> 0x00d0 }
            java.lang.String r9 = ".lock"
            r8.append(r9)     // Catch:{ Exception -> 0x00d5, all -> 0x00d0 }
            java.lang.String r8 = r8.toString()     // Catch:{ Exception -> 0x00d5, all -> 0x00d0 }
            r7.<init>(r8)     // Catch:{ Exception -> 0x00d5, all -> 0x00d0 }
            com.xiaomi.push.y.a((java.io.File) r7)     // Catch:{ Exception -> 0x00ca, all -> 0x00c8 }
            java.io.RandomAccessFile r5 = new java.io.RandomAccessFile     // Catch:{ Exception -> 0x00ca, all -> 0x00c8 }
            java.lang.String r8 = "rw"
            r5.<init>(r7, r8)     // Catch:{ Exception -> 0x00ca, all -> 0x00c8 }
            java.nio.channels.FileChannel r4 = r5.getChannel()     // Catch:{ Exception -> 0x00c3, all -> 0x00c1 }
            java.nio.channels.FileLock r4 = r4.lock()     // Catch:{ Exception -> 0x00c3, all -> 0x00c1 }
            java.lang.String r3 = r13.getAbsolutePath()     // Catch:{ Exception -> 0x00bf }
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00bf }
            r8.<init>()     // Catch:{ Exception -> 0x00bf }
            r8.append(r3)     // Catch:{ Exception -> 0x00bf }
            java.lang.String r3 = java.io.File.separator     // Catch:{ Exception -> 0x00bf }
            r8.append(r3)     // Catch:{ Exception -> 0x00bf }
            java.lang.String r3 = r6.getName()     // Catch:{ Exception -> 0x00bf }
            r8.append(r3)     // Catch:{ Exception -> 0x00bf }
            r8.append(r0)     // Catch:{ Exception -> 0x00bf }
            java.lang.String r3 = r8.toString()     // Catch:{ Exception -> 0x00bf }
            java.io.File r8 = new java.io.File     // Catch:{ Exception -> 0x00bf }
            r8.<init>(r3)     // Catch:{ Exception -> 0x00bf }
            com.xiaomi.push.y.b(r6, r8)     // Catch:{ IOException -> 0x009e }
            goto L_0x00a8
        L_0x009e:
            r3 = move-exception
            r3.printStackTrace()     // Catch:{ Exception -> 0x00bf }
            r6.delete()     // Catch:{ Exception -> 0x00bf }
            r8.delete()     // Catch:{ Exception -> 0x00bf }
        L_0x00a8:
            r6.delete()     // Catch:{ Exception -> 0x00bf }
            if (r4 == 0) goto L_0x00bb
            boolean r3 = r4.isValid()
            if (r3 == 0) goto L_0x00bb
            r4.release()     // Catch:{ IOException -> 0x00b7 }
            goto L_0x00bb
        L_0x00b7:
            r3 = move-exception
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.Throwable) r3)
        L_0x00bb:
            com.xiaomi.push.y.a((java.io.Closeable) r5)
            goto L_0x00f2
        L_0x00bf:
            r3 = move-exception
            goto L_0x00da
        L_0x00c1:
            r11 = move-exception
            goto L_0x00d3
        L_0x00c3:
            r4 = move-exception
            r10 = r4
            r4 = r3
            r3 = r10
            goto L_0x00da
        L_0x00c8:
            r11 = move-exception
            goto L_0x00d2
        L_0x00ca:
            r5 = move-exception
            r10 = r4
            r4 = r3
            r3 = r5
            r5 = r10
            goto L_0x00da
        L_0x00d0:
            r11 = move-exception
            r7 = r5
        L_0x00d2:
            r5 = r4
        L_0x00d3:
            r4 = r3
            goto L_0x00fa
        L_0x00d5:
            r6 = move-exception
            r7 = r5
            r5 = r4
            r4 = r3
            r3 = r6
        L_0x00da:
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.Throwable) r3)     // Catch:{ all -> 0x00f9 }
            if (r4 == 0) goto L_0x00ed
            boolean r3 = r4.isValid()
            if (r3 == 0) goto L_0x00ed
            r4.release()     // Catch:{ IOException -> 0x00e9 }
            goto L_0x00ed
        L_0x00e9:
            r3 = move-exception
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.Throwable) r3)
        L_0x00ed:
            com.xiaomi.push.y.a((java.io.Closeable) r5)
            if (r7 == 0) goto L_0x00f5
        L_0x00f2:
            r7.delete()
        L_0x00f5:
            r3 = r4
            r4 = r5
            r5 = r7
            goto L_0x012b
        L_0x00f9:
            r11 = move-exception
        L_0x00fa:
            if (r4 == 0) goto L_0x010a
            boolean r12 = r4.isValid()
            if (r12 == 0) goto L_0x010a
            r4.release()     // Catch:{ IOException -> 0x0106 }
            goto L_0x010a
        L_0x0106:
            r12 = move-exception
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.Throwable) r12)
        L_0x010a:
            com.xiaomi.push.y.a((java.io.Closeable) r5)
            if (r7 == 0) goto L_0x0112
            r7.delete()
        L_0x0112:
            throw r11
        L_0x0113:
            if (r3 == 0) goto L_0x0123
            boolean r6 = r3.isValid()
            if (r6 == 0) goto L_0x0123
            r3.release()     // Catch:{ IOException -> 0x011f }
            goto L_0x0123
        L_0x011f:
            r6 = move-exception
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.Throwable) r6)
        L_0x0123:
            com.xiaomi.push.y.a((java.io.Closeable) r4)
            if (r5 == 0) goto L_0x012b
            r5.delete()
        L_0x012b:
            int r2 = r2 + 1
            goto L_0x0038
        L_0x012f:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.be.a(android.content.Context, java.lang.String, java.lang.String):void");
    }

    public static void a(Context context, List<String> list) {
        if (list != null && list.size() > 0 && a(context)) {
            for (String next : list) {
                if (!TextUtils.isEmpty(next)) {
                    a(context, next);
                }
            }
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public static boolean m116a(Context context) {
        try {
            return context.getApplicationContext().getPackageManager().getPackageInfo("com.xiaomi.xmsf", 0).versionCode >= 108;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public static boolean m117a(Context context, String str) {
        File file = new File(str);
        long maxFileLength = a.a(context).a().getMaxFileLength();
        if (file.exists()) {
            try {
                if (file.length() > maxFileLength) {
                    return false;
                }
            } catch (Exception e) {
                b.a((Throwable) e);
                return false;
            }
        } else {
            y.a(file);
        }
        return true;
    }

    @TargetApi(9)
    public static byte[] a(String str) {
        byte[] copyOf = Arrays.copyOf(av.a(str), 16);
        copyOf[0] = 68;
        copyOf[15] = 84;
        return copyOf;
    }

    /* renamed from: a  reason: collision with other method in class */
    public static File[] m118a(Context context, String str) {
        File externalFilesDir = context.getExternalFilesDir(str);
        if (externalFilesDir != null) {
            return externalFilesDir.listFiles(new bg());
        }
        return null;
    }
}
