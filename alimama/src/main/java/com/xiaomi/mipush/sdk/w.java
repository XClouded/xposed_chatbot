package com.xiaomi.mipush.sdk;

import android.content.Context;

final class w implements Runnable {
    final /* synthetic */ Context a;

    /* renamed from: a  reason: collision with other field name */
    final /* synthetic */ boolean f73a;

    w(Context context, boolean z) {
        this.a = context;
        this.f73a = z;
    }

    /* JADX WARNING: Removed duplicated region for block: B:27:0x009b  */
    /* JADX WARNING: Removed duplicated region for block: B:30:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void run() {
        /*
            r7 = this;
            r0 = 0
            android.content.Context r1 = r7.a     // Catch:{ Throwable -> 0x008e }
            java.lang.String r2 = ""
            java.util.HashMap r1 = com.xiaomi.mipush.sdk.ak.a(r1, r2)     // Catch:{ Throwable -> 0x008e }
            boolean r2 = r7.f73a     // Catch:{ Throwable -> 0x008e }
            if (r2 == 0) goto L_0x0018
            android.content.Context r2 = r7.a     // Catch:{ Throwable -> 0x008e }
            java.io.File r2 = r2.getFilesDir()     // Catch:{ Throwable -> 0x008e }
            java.lang.String r2 = r2.getAbsolutePath()     // Catch:{ Throwable -> 0x008e }
            goto L_0x0033
        L_0x0018:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x008e }
            r2.<init>()     // Catch:{ Throwable -> 0x008e }
            android.content.Context r3 = r7.a     // Catch:{ Throwable -> 0x008e }
            java.io.File r3 = r3.getExternalFilesDir(r0)     // Catch:{ Throwable -> 0x008e }
            java.lang.String r3 = r3.getAbsolutePath()     // Catch:{ Throwable -> 0x008e }
            r2.append(r3)     // Catch:{ Throwable -> 0x008e }
            java.lang.String r3 = com.xiaomi.push.dh.f231a     // Catch:{ Throwable -> 0x008e }
            r2.append(r3)     // Catch:{ Throwable -> 0x008e }
            java.lang.String r2 = r2.toString()     // Catch:{ Throwable -> 0x008e }
        L_0x0033:
            java.io.File r3 = com.xiaomi.mipush.sdk.Logger.getLogFile(r2)     // Catch:{ Throwable -> 0x008e }
            if (r3 != 0) goto L_0x003f
            java.lang.String r1 = "log file null"
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.String) r1)     // Catch:{ Throwable -> 0x008e }
            return
        L_0x003f:
            android.content.Context r4 = r7.a     // Catch:{ Throwable -> 0x008e }
            java.lang.String r4 = r4.getPackageName()     // Catch:{ Throwable -> 0x008e }
            java.io.File r5 = new java.io.File     // Catch:{ Throwable -> 0x008e }
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x008e }
            r6.<init>()     // Catch:{ Throwable -> 0x008e }
            r6.append(r4)     // Catch:{ Throwable -> 0x008e }
            java.lang.String r4 = ".zip"
            r6.append(r4)     // Catch:{ Throwable -> 0x008e }
            java.lang.String r4 = r6.toString()     // Catch:{ Throwable -> 0x008e }
            r5.<init>(r2, r4)     // Catch:{ Throwable -> 0x008e }
            com.xiaomi.push.y.a((java.io.File) r5, (java.io.File) r3)     // Catch:{ Throwable -> 0x008c }
            boolean r0 = r5.exists()     // Catch:{ Throwable -> 0x008c }
            if (r0 == 0) goto L_0x0086
            boolean r0 = r7.f73a     // Catch:{ Throwable -> 0x008c }
            if (r0 == 0) goto L_0x006b
            java.lang.String r0 = "https://api.xmpush.xiaomi.com/upload/xmsf_log?file="
            goto L_0x006d
        L_0x006b:
            java.lang.String r0 = "https://api.xmpush.xiaomi.com/upload/app_log?file="
        L_0x006d:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x008c }
            r2.<init>()     // Catch:{ Throwable -> 0x008c }
            r2.append(r0)     // Catch:{ Throwable -> 0x008c }
            java.lang.String r0 = r5.getName()     // Catch:{ Throwable -> 0x008c }
            r2.append(r0)     // Catch:{ Throwable -> 0x008c }
            java.lang.String r0 = r2.toString()     // Catch:{ Throwable -> 0x008c }
            java.lang.String r2 = "file"
            com.xiaomi.push.as.a(r0, r1, r5, r2)     // Catch:{ Throwable -> 0x008c }
            goto L_0x0093
        L_0x0086:
            java.lang.String r0 = "zip log file failed"
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.String) r0)     // Catch:{ Throwable -> 0x008c }
            goto L_0x0093
        L_0x008c:
            r1 = move-exception
            goto L_0x0090
        L_0x008e:
            r1 = move-exception
            r5 = r0
        L_0x0090:
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.Throwable) r1)
        L_0x0093:
            if (r5 == 0) goto L_0x009e
            boolean r0 = r5.exists()
            if (r0 == 0) goto L_0x009e
            r5.delete()
        L_0x009e:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.mipush.sdk.w.run():void");
    }
}
