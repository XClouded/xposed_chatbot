package com.xiaomi.push;

import android.content.Context;
import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.push.service.XMJobService;

public final class ew {
    private static int a = 0;

    /* renamed from: a  reason: collision with other field name */
    private static a f321a;

    /* renamed from: a  reason: collision with other field name */
    private static final String f322a = XMJobService.class.getCanonicalName();

    interface a {
        void a();

        void a(boolean z);

        /* renamed from: a  reason: collision with other method in class */
        boolean m282a();
    }

    public static synchronized void a() {
        synchronized (ew.class) {
            if (f321a != null) {
                f321a.a();
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x005e, code lost:
        if (f322a.equals(java.lang.Class.forName(r5.name).getSuperclass().getCanonicalName()) != false) goto L_0x0048;
     */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x00a5  */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x00cd  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void a(android.content.Context r8) {
        /*
            android.content.Context r8 = r8.getApplicationContext()
            java.lang.String r0 = "com.xiaomi.xmsf"
            java.lang.String r1 = r8.getPackageName()
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x0019
            com.xiaomi.push.ex r0 = new com.xiaomi.push.ex
            r0.<init>(r8)
        L_0x0015:
            f321a = r0
            goto L_0x00d8
        L_0x0019:
            android.content.pm.PackageManager r0 = r8.getPackageManager()
            r1 = 0
            java.lang.String r2 = r8.getPackageName()     // Catch:{ Exception -> 0x0082 }
            r3 = 4
            android.content.pm.PackageInfo r0 = r0.getPackageInfo(r2, r3)     // Catch:{ Exception -> 0x0082 }
            android.content.pm.ServiceInfo[] r2 = r0.services     // Catch:{ Exception -> 0x0082 }
            r3 = 1
            if (r2 == 0) goto L_0x0080
            android.content.pm.ServiceInfo[] r0 = r0.services     // Catch:{ Exception -> 0x0082 }
            int r2 = r0.length     // Catch:{ Exception -> 0x0082 }
            r4 = 0
        L_0x0030:
            if (r1 >= r2) goto L_0x009c
            r5 = r0[r1]     // Catch:{ Exception -> 0x007e }
            java.lang.String r6 = "android.permission.BIND_JOB_SERVICE"
            java.lang.String r7 = r5.permission     // Catch:{ Exception -> 0x007e }
            boolean r6 = r6.equals(r7)     // Catch:{ Exception -> 0x007e }
            if (r6 == 0) goto L_0x0065
            java.lang.String r6 = f322a     // Catch:{ Exception -> 0x007e }
            java.lang.String r7 = r5.name     // Catch:{ Exception -> 0x007e }
            boolean r6 = r6.equals(r7)     // Catch:{ Exception -> 0x007e }
            if (r6 == 0) goto L_0x004a
        L_0x0048:
            r4 = 1
            goto L_0x0062
        L_0x004a:
            java.lang.String r6 = r5.name     // Catch:{ Exception -> 0x0061 }
            java.lang.Class r6 = java.lang.Class.forName(r6)     // Catch:{ Exception -> 0x0061 }
            java.lang.String r7 = f322a     // Catch:{ Exception -> 0x0061 }
            java.lang.Class r6 = r6.getSuperclass()     // Catch:{ Exception -> 0x0061 }
            java.lang.String r6 = r6.getCanonicalName()     // Catch:{ Exception -> 0x0061 }
            boolean r6 = r7.equals(r6)     // Catch:{ Exception -> 0x0061 }
            if (r6 == 0) goto L_0x0062
            goto L_0x0048
        L_0x0061:
        L_0x0062:
            if (r4 != r3) goto L_0x0065
            goto L_0x009c
        L_0x0065:
            java.lang.String r6 = f322a     // Catch:{ Exception -> 0x007e }
            java.lang.String r7 = r5.name     // Catch:{ Exception -> 0x007e }
            boolean r6 = r6.equals(r7)     // Catch:{ Exception -> 0x007e }
            if (r6 == 0) goto L_0x007b
            java.lang.String r6 = "android.permission.BIND_JOB_SERVICE"
            java.lang.String r5 = r5.permission     // Catch:{ Exception -> 0x007e }
            boolean r5 = r6.equals(r5)     // Catch:{ Exception -> 0x007e }
            if (r5 == 0) goto L_0x007b
            r4 = 1
            goto L_0x009c
        L_0x007b:
            int r1 = r1 + 1
            goto L_0x0030
        L_0x007e:
            r0 = move-exception
            goto L_0x0084
        L_0x0080:
            r4 = 0
            goto L_0x009c
        L_0x0082:
            r0 = move-exception
            r4 = 0
        L_0x0084:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "check service err : "
            r1.append(r2)
            java.lang.String r0 = r0.getMessage()
            r1.append(r0)
            java.lang.String r0 = r1.toString()
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.String) r0)
        L_0x009c:
            if (r4 != 0) goto L_0x00cd
            boolean r0 = com.xiaomi.push.t.a((android.content.Context) r8)
            if (r0 != 0) goto L_0x00a5
            goto L_0x00cd
        L_0x00a5:
            java.lang.RuntimeException r8 = new java.lang.RuntimeException
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "Should export service: "
            r0.append(r1)
            java.lang.String r1 = f322a
            r0.append(r1)
            java.lang.String r1 = " with permission "
            r0.append(r1)
            java.lang.String r1 = "android.permission.BIND_JOB_SERVICE"
            r0.append(r1)
            java.lang.String r1 = " in AndroidManifest.xml file"
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            r8.<init>(r0)
            throw r8
        L_0x00cd:
            int r0 = android.os.Build.VERSION.SDK_INT
            r1 = 21
            com.xiaomi.push.ex r0 = new com.xiaomi.push.ex
            r0.<init>(r8)
            goto L_0x0015
        L_0x00d8:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.ew.a(android.content.Context):void");
    }

    public static synchronized void a(Context context, int i) {
        synchronized (ew.class) {
            int i2 = a;
            if (!"com.xiaomi.xmsf".equals(context.getPackageName())) {
                if (i == 2) {
                    a = 2;
                } else {
                    a = 0;
                }
            }
            if (i2 != a && a == 2) {
                a();
                f321a = new ez(context);
            }
        }
    }

    public static synchronized void a(boolean z) {
        synchronized (ew.class) {
            if (f321a == null) {
                b.a("timer is not initialized");
            } else {
                f321a.a(z);
            }
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public static synchronized boolean m281a() {
        synchronized (ew.class) {
            if (f321a == null) {
                return false;
            }
            boolean a2 = f321a.a();
            return a2;
        }
    }
}
