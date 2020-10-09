package com.xiaomi.push;

import android.content.Context;
import java.io.File;

public abstract class v implements Runnable {
    private Context a;

    /* renamed from: a  reason: collision with other field name */
    private File f945a;

    /* renamed from: a  reason: collision with other field name */
    private Runnable f946a;

    private v(Context context, File file) {
        this.a = context;
        this.f945a = file;
    }

    /* synthetic */ v(Context context, File file, w wVar) {
        this(context, file);
    }

    public static void a(Context context, File file, Runnable runnable) {
        new w(context, file, runnable).run();
    }

    /* access modifiers changed from: protected */
    public abstract void a(Context context);

    /* JADX WARNING: Removed duplicated region for block: B:22:0x0042  */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0048  */
    /* JADX WARNING: Removed duplicated region for block: B:29:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void run() {
        /*
            r5 = this;
            r0 = 0
            java.io.File r1 = r5.f945a     // Catch:{ IOException -> 0x003c }
            if (r1 != 0) goto L_0x0014
            java.io.File r1 = new java.io.File     // Catch:{ IOException -> 0x003c }
            android.content.Context r2 = r5.a     // Catch:{ IOException -> 0x003c }
            java.io.File r2 = r2.getFilesDir()     // Catch:{ IOException -> 0x003c }
            java.lang.String r3 = "default_locker"
            r1.<init>(r2, r3)     // Catch:{ IOException -> 0x003c }
            r5.f945a = r1     // Catch:{ IOException -> 0x003c }
        L_0x0014:
            android.content.Context r1 = r5.a     // Catch:{ IOException -> 0x003c }
            java.io.File r2 = r5.f945a     // Catch:{ IOException -> 0x003c }
            com.xiaomi.push.u r1 = com.xiaomi.push.u.a(r1, r2)     // Catch:{ IOException -> 0x003c }
            java.lang.Runnable r0 = r5.f946a     // Catch:{ IOException -> 0x0035, all -> 0x0030 }
            if (r0 == 0) goto L_0x0025
            java.lang.Runnable r0 = r5.f946a     // Catch:{ IOException -> 0x0035, all -> 0x0030 }
            r0.run()     // Catch:{ IOException -> 0x0035, all -> 0x0030 }
        L_0x0025:
            android.content.Context r0 = r5.a     // Catch:{ IOException -> 0x0035, all -> 0x0030 }
            r5.a(r0)     // Catch:{ IOException -> 0x0035, all -> 0x0030 }
            if (r1 == 0) goto L_0x0045
            r1.a()
            goto L_0x0045
        L_0x0030:
            r0 = move-exception
            r4 = r1
            r1 = r0
            r0 = r4
            goto L_0x0046
        L_0x0035:
            r0 = move-exception
            r4 = r1
            r1 = r0
            r0 = r4
            goto L_0x003d
        L_0x003a:
            r1 = move-exception
            goto L_0x0046
        L_0x003c:
            r1 = move-exception
        L_0x003d:
            r1.printStackTrace()     // Catch:{ all -> 0x003a }
            if (r0 == 0) goto L_0x0045
            r0.a()
        L_0x0045:
            return
        L_0x0046:
            if (r0 == 0) goto L_0x004b
            r0.a()
        L_0x004b:
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.v.run():void");
    }
}
