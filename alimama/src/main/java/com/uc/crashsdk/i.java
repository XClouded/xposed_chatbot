package com.uc.crashsdk;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;
import com.uc.crashsdk.a.a;
import com.uc.crashsdk.a.g;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;

/* compiled from: ProGuard */
public final class i {
    private static PendingIntent a;

    public static void a() {
        if (!e.x() && !b.D() && a == null && h.i() >= 0) {
            try {
                Context a2 = g.a();
                Intent launchIntentForPackage = a2.getPackageManager().getLaunchIntentForPackage(a2.getPackageName());
                launchIntentForPackage.addFlags(335544320);
                a = PendingIntent.getActivity(a2, 0, launchIntentForPackage, 0);
            } catch (Throwable th) {
                g.a(th);
            }
        }
    }

    static boolean b() {
        if (a == null) {
            a.c("Restart intent is null!");
            return false;
        }
        try {
            a.b("restarting ...");
            ((AlarmManager) g.a().getSystemService(NotificationCompat.CATEGORY_ALARM)).set(1, System.currentTimeMillis() + 200, a);
            return true;
        } catch (Throwable th) {
            g.a(th);
            return false;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:28:0x0087 A[SYNTHETIC, Splitter:B:28:0x0087] */
    /* JADX WARNING: Removed duplicated region for block: B:34:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static void a(android.content.Context r11) {
        /*
            java.lang.String r0 = "restartBrowser"
            com.uc.crashsdk.a.a.a(r0)
            if (r11 != 0) goto L_0x0008
            return
        L_0x0008:
            r11 = 0
            int r0 = c()
            long r1 = java.lang.System.currentTimeMillis()
            r3 = 1000(0x3e8, double:4.94E-321)
            long r1 = r1 / r3
            int r5 = com.uc.crashsdk.h.i()
            r6 = 1
            if (r5 < 0) goto L_0x0064
            if (r0 <= 0) goto L_0x0029
            long r7 = (long) r0
            long r7 = r1 - r7
            int r5 = com.uc.crashsdk.h.i()
            long r9 = (long) r5
            int r5 = (r7 > r9 ? 1 : (r7 == r9 ? 0 : -1))
            if (r5 <= 0) goto L_0x0064
        L_0x0029:
            long r7 = java.lang.System.currentTimeMillis()
            long r7 = r7 / r3
            com.uc.crashsdk.b.u()
            java.io.File r11 = new java.io.File
            java.lang.String r3 = com.uc.crashsdk.b.d()
            r11.<init>(r3)
            r3 = 0
            java.io.FileOutputStream r4 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x0057 }
            r4.<init>(r11)     // Catch:{ Exception -> 0x0057 }
            java.lang.String r11 = java.lang.String.valueOf(r7)     // Catch:{ Exception -> 0x0052, all -> 0x004f }
            byte[] r11 = r11.getBytes()     // Catch:{ Exception -> 0x0052, all -> 0x004f }
            r4.write(r11)     // Catch:{ Exception -> 0x0052, all -> 0x004f }
            com.uc.crashsdk.a.g.a((java.io.Closeable) r4)
            goto L_0x005e
        L_0x004f:
            r11 = move-exception
            r3 = r4
            goto L_0x0060
        L_0x0052:
            r11 = move-exception
            r3 = r4
            goto L_0x0058
        L_0x0055:
            r11 = move-exception
            goto L_0x0060
        L_0x0057:
            r11 = move-exception
        L_0x0058:
            com.uc.crashsdk.a.g.b((java.lang.Throwable) r11)     // Catch:{ all -> 0x0055 }
            com.uc.crashsdk.a.g.a((java.io.Closeable) r3)
        L_0x005e:
            r11 = 1
            goto L_0x0064
        L_0x0060:
            com.uc.crashsdk.a.g.a((java.io.Closeable) r3)
            throw r11
        L_0x0064:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            java.lang.String r4 = "restartBrowser, lastTime: "
            r3.<init>(r4)
            r3.append(r0)
            java.lang.String r0 = ", currentTime: "
            r3.append(r0)
            r3.append(r1)
            java.lang.String r0 = ", needRestart: "
            r3.append(r0)
            r3.append(r11)
            java.lang.String r0 = r3.toString()
            com.uc.crashsdk.a.a.a(r0)
            if (r11 == 0) goto L_0x0092
            com.uc.crashsdk.d.a((boolean) r6)     // Catch:{ Throwable -> 0x008b }
            goto L_0x008f
        L_0x008b:
            r11 = move-exception
            com.uc.crashsdk.a.g.a((java.lang.Throwable) r11)
        L_0x008f:
            b()
        L_0x0092:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.crashsdk.i.a(android.content.Context):void");
    }

    private static int c() {
        FileInputStream fileInputStream;
        Exception e;
        File file = new File(b.d());
        int i = -1;
        if (!file.exists()) {
            return -1;
        }
        try {
            fileInputStream = new FileInputStream(file);
            try {
                byte[] bArr = new byte[((int) file.length())];
                if (fileInputStream.read(bArr) > 0) {
                    i = Integer.parseInt(new String(bArr));
                }
            } catch (Exception e2) {
                e = e2;
                try {
                    g.b((Throwable) e);
                    g.a((Closeable) fileInputStream);
                    return i;
                } catch (Throwable th) {
                    th = th;
                    g.a((Closeable) fileInputStream);
                    throw th;
                }
            }
        } catch (Exception e3) {
            fileInputStream = null;
            e = e3;
            g.b((Throwable) e);
            g.a((Closeable) fileInputStream);
            return i;
        } catch (Throwable th2) {
            fileInputStream = null;
            th = th2;
            g.a((Closeable) fileInputStream);
            throw th;
        }
        g.a((Closeable) fileInputStream);
        return i;
    }
}
