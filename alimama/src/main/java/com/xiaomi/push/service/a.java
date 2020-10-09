package com.xiaomi.push.service;

import android.content.Context;
import android.text.TextUtils;

public class a {
    private static volatile a a;

    /* renamed from: a  reason: collision with other field name */
    private Context f831a;

    /* renamed from: a  reason: collision with other field name */
    private final Object f832a = new Object();

    /* renamed from: a  reason: collision with other field name */
    private final String f833a = "mipush_region";
    private final Object b = new Object();

    /* renamed from: b  reason: collision with other field name */
    private final String f834b = "mipush_country_code";
    private final String c = "mipush_region.lock";
    private final String d = "mipush_country_code.lock";
    private volatile String e;
    private volatile String f;

    public a(Context context) {
        this.f831a = context;
    }

    public static a a(Context context) {
        if (a == null) {
            synchronized (a.class) {
                if (a == null) {
                    a = new a(context);
                }
            }
        }
        return a;
    }

    /* JADX WARNING: Removed duplicated region for block: B:34:0x006c A[SYNTHETIC, Splitter:B:34:0x006c] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.String a(android.content.Context r4, java.lang.String r5, java.lang.String r6, java.lang.Object r7) {
        /*
            r3 = this;
            java.io.File r0 = new java.io.File
            java.io.File r1 = r4.getFilesDir()
            r0.<init>(r1, r5)
            boolean r1 = r0.exists()
            r2 = 0
            if (r1 != 0) goto L_0x0025
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r6 = "No ready file to get data from "
            r4.append(r6)
            r4.append(r5)
            java.lang.String r4 = r4.toString()
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.String) r4)
            return r2
        L_0x0025:
            monitor-enter(r7)
            java.io.File r5 = new java.io.File     // Catch:{ Exception -> 0x0064, all -> 0x0061 }
            java.io.File r4 = r4.getFilesDir()     // Catch:{ Exception -> 0x0064, all -> 0x0061 }
            r5.<init>(r4, r6)     // Catch:{ Exception -> 0x0064, all -> 0x0061 }
            com.xiaomi.push.y.a((java.io.File) r5)     // Catch:{ Exception -> 0x0064, all -> 0x0061 }
            java.io.RandomAccessFile r4 = new java.io.RandomAccessFile     // Catch:{ Exception -> 0x0064, all -> 0x0061 }
            java.lang.String r6 = "rw"
            r4.<init>(r5, r6)     // Catch:{ Exception -> 0x0064, all -> 0x0061 }
            java.nio.channels.FileChannel r5 = r4.getChannel()     // Catch:{ Exception -> 0x005e, all -> 0x005c }
            java.nio.channels.FileLock r5 = r5.lock()     // Catch:{ Exception -> 0x005e, all -> 0x005c }
            java.lang.String r6 = com.xiaomi.push.y.a((java.io.File) r0)     // Catch:{ Exception -> 0x005a }
            if (r5 == 0) goto L_0x0055
            boolean r0 = r5.isValid()     // Catch:{ all -> 0x0092 }
            if (r0 == 0) goto L_0x0055
            r5.release()     // Catch:{ IOException -> 0x0051 }
            goto L_0x0055
        L_0x0051:
            r5 = move-exception
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.Throwable) r5)     // Catch:{ all -> 0x0092 }
        L_0x0055:
            com.xiaomi.push.y.a((java.io.Closeable) r4)     // Catch:{ all -> 0x0092 }
            monitor-exit(r7)     // Catch:{ all -> 0x0092 }
            return r6
        L_0x005a:
            r6 = move-exception
            goto L_0x0067
        L_0x005c:
            r6 = move-exception
            goto L_0x0081
        L_0x005e:
            r6 = move-exception
            r5 = r2
            goto L_0x0067
        L_0x0061:
            r6 = move-exception
            r4 = r2
            goto L_0x0081
        L_0x0064:
            r6 = move-exception
            r4 = r2
            r5 = r4
        L_0x0067:
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.Throwable) r6)     // Catch:{ all -> 0x007f }
            if (r5 == 0) goto L_0x007a
            boolean r6 = r5.isValid()     // Catch:{ all -> 0x0092 }
            if (r6 == 0) goto L_0x007a
            r5.release()     // Catch:{ IOException -> 0x0076 }
            goto L_0x007a
        L_0x0076:
            r5 = move-exception
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.Throwable) r5)     // Catch:{ all -> 0x0092 }
        L_0x007a:
            com.xiaomi.push.y.a((java.io.Closeable) r4)     // Catch:{ all -> 0x0092 }
            monitor-exit(r7)     // Catch:{ all -> 0x0092 }
            return r2
        L_0x007f:
            r6 = move-exception
            r2 = r5
        L_0x0081:
            if (r2 == 0) goto L_0x0094
            boolean r5 = r2.isValid()     // Catch:{ all -> 0x0092 }
            if (r5 == 0) goto L_0x0094
            r2.release()     // Catch:{ IOException -> 0x008d }
            goto L_0x0094
        L_0x008d:
            r5 = move-exception
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.Throwable) r5)     // Catch:{ all -> 0x0092 }
            goto L_0x0094
        L_0x0092:
            r4 = move-exception
            goto L_0x0098
        L_0x0094:
            com.xiaomi.push.y.a((java.io.Closeable) r4)     // Catch:{ all -> 0x0092 }
            throw r6     // Catch:{ all -> 0x0092 }
        L_0x0098:
            monitor-exit(r7)     // Catch:{ all -> 0x0092 }
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.service.a.a(android.content.Context, java.lang.String, java.lang.String, java.lang.Object):java.lang.String");
    }

    /* JADX WARNING: Removed duplicated region for block: B:30:0x004f A[SYNTHETIC, Splitter:B:30:0x004f] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void a(android.content.Context r4, java.lang.String r5, java.lang.String r6, java.lang.String r7, java.lang.Object r8) {
        /*
            r3 = this;
            monitor-enter(r8)
            r0 = 0
            java.io.File r1 = new java.io.File     // Catch:{ Exception -> 0x0048, all -> 0x0045 }
            java.io.File r2 = r4.getFilesDir()     // Catch:{ Exception -> 0x0048, all -> 0x0045 }
            r1.<init>(r2, r7)     // Catch:{ Exception -> 0x0048, all -> 0x0045 }
            com.xiaomi.push.y.a((java.io.File) r1)     // Catch:{ Exception -> 0x0048, all -> 0x0045 }
            java.io.RandomAccessFile r7 = new java.io.RandomAccessFile     // Catch:{ Exception -> 0x0048, all -> 0x0045 }
            java.lang.String r2 = "rw"
            r7.<init>(r1, r2)     // Catch:{ Exception -> 0x0048, all -> 0x0045 }
            java.nio.channels.FileChannel r1 = r7.getChannel()     // Catch:{ Exception -> 0x0043 }
            java.nio.channels.FileLock r1 = r1.lock()     // Catch:{ Exception -> 0x0043 }
            java.io.File r0 = new java.io.File     // Catch:{ Exception -> 0x0040, all -> 0x003d }
            java.io.File r4 = r4.getFilesDir()     // Catch:{ Exception -> 0x0040, all -> 0x003d }
            r0.<init>(r4, r6)     // Catch:{ Exception -> 0x0040, all -> 0x003d }
            com.xiaomi.push.y.a((java.io.File) r0, (java.lang.String) r5)     // Catch:{ Exception -> 0x0040, all -> 0x003d }
            if (r1 == 0) goto L_0x0039
            boolean r4 = r1.isValid()     // Catch:{ all -> 0x0072 }
            if (r4 == 0) goto L_0x0039
            r1.release()     // Catch:{ IOException -> 0x0035 }
            goto L_0x0039
        L_0x0035:
            r4 = move-exception
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.Throwable) r4)     // Catch:{ all -> 0x0072 }
        L_0x0039:
            com.xiaomi.push.y.a((java.io.Closeable) r7)     // Catch:{ all -> 0x0072 }
            goto L_0x005e
        L_0x003d:
            r4 = move-exception
            r0 = r1
            goto L_0x0061
        L_0x0040:
            r4 = move-exception
            r0 = r1
            goto L_0x004a
        L_0x0043:
            r4 = move-exception
            goto L_0x004a
        L_0x0045:
            r4 = move-exception
            r7 = r0
            goto L_0x0061
        L_0x0048:
            r4 = move-exception
            r7 = r0
        L_0x004a:
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.Throwable) r4)     // Catch:{ all -> 0x0060 }
            if (r0 == 0) goto L_0x0039
            boolean r4 = r0.isValid()     // Catch:{ all -> 0x0072 }
            if (r4 == 0) goto L_0x0039
            r0.release()     // Catch:{ IOException -> 0x0059 }
            goto L_0x0039
        L_0x0059:
            r4 = move-exception
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.Throwable) r4)     // Catch:{ all -> 0x0072 }
            goto L_0x0039
        L_0x005e:
            monitor-exit(r8)     // Catch:{ all -> 0x0072 }
            return
        L_0x0060:
            r4 = move-exception
        L_0x0061:
            if (r0 == 0) goto L_0x0074
            boolean r5 = r0.isValid()     // Catch:{ all -> 0x0072 }
            if (r5 == 0) goto L_0x0074
            r0.release()     // Catch:{ IOException -> 0x006d }
            goto L_0x0074
        L_0x006d:
            r5 = move-exception
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.Throwable) r5)     // Catch:{ all -> 0x0072 }
            goto L_0x0074
        L_0x0072:
            r4 = move-exception
            goto L_0x0078
        L_0x0074:
            com.xiaomi.push.y.a((java.io.Closeable) r7)     // Catch:{ all -> 0x0072 }
            throw r4     // Catch:{ all -> 0x0072 }
        L_0x0078:
            monitor-exit(r8)     // Catch:{ all -> 0x0072 }
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.service.a.a(android.content.Context, java.lang.String, java.lang.String, java.lang.String, java.lang.Object):void");
    }

    public String a() {
        if (TextUtils.isEmpty(this.e)) {
            this.e = a(this.f831a, "mipush_region", "mipush_region.lock", this.f832a);
        }
        return this.e;
    }

    public void a(String str) {
        if (!TextUtils.equals(str, this.e)) {
            this.e = str;
            a(this.f831a, this.e, "mipush_region", "mipush_region.lock", this.f832a);
        }
    }

    public String b() {
        if (TextUtils.isEmpty(this.f)) {
            this.f = a(this.f831a, "mipush_country_code", "mipush_country_code.lock", this.b);
        }
        return this.f;
    }

    public void b(String str) {
        if (!TextUtils.equals(str, this.f)) {
            this.f = str;
            a(this.f831a, this.f, "mipush_country_code", "mipush_country_code.lock", this.b);
        }
    }
}
