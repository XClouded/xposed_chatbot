package com.xiaomi.push;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.xiaomi.channel.commonutils.logger.b;

public class i {
    private static String a = null;

    /* renamed from: a  reason: collision with other field name */
    private static volatile boolean f580a = false;
    private static String b = "";
    private static String c;
    private static String d;
    private static String e;

    @TargetApi(17)
    public static int a() {
        Object a2;
        if (Build.VERSION.SDK_INT >= 17 && (a2 = at.a("android.os.UserHandle", "myUserId", new Object[0])) != null) {
            return Integer.class.cast(a2).intValue();
        }
        return -1;
    }

    /* renamed from: a  reason: collision with other method in class */
    public static String m420a() {
        if (Build.VERSION.SDK_INT > 8 && Build.VERSION.SDK_INT < 26) {
            return Build.SERIAL;
        }
        if (Build.VERSION.SDK_INT >= 26) {
            return (String) at.a("android.os.Build", "getSerial", (Object[]) null);
        }
        return null;
    }

    public static String a(Context context) {
        String e2 = e(context);
        return "a-" + ay.b(null + e2 + null);
    }

    public static String a(Context context, boolean z) {
        if (c == null) {
            String str = "";
            if (!l.d()) {
                str = z ? f(context) : o(context);
            }
            String e2 = e(context);
            String a2 = a();
            StringBuilder sb = new StringBuilder();
            sb.append("a-");
            sb.append(ay.b(str + e2 + a2));
            c = sb.toString();
        }
        return c;
    }

    /* JADX WARNING: Removed duplicated region for block: B:30:0x0090  */
    /* JADX WARNING: Removed duplicated region for block: B:34:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void a(android.content.Context r3, java.lang.String r4) {
        /*
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "update vdevid = "
            r0.append(r1)
            r0.append(r4)
            java.lang.String r0 = r0.toString()
            com.xiaomi.channel.commonutils.logger.b.c(r0)
            boolean r0 = android.text.TextUtils.isEmpty(r4)
            if (r0 == 0) goto L_0x001b
            return
        L_0x001b:
            e = r4
            r4 = 0
            boolean r0 = a((android.content.Context) r3)     // Catch:{ IOException -> 0x0071 }
            if (r0 == 0) goto L_0x0059
            java.io.File r0 = new java.io.File     // Catch:{ IOException -> 0x0071 }
            java.io.File r1 = android.os.Environment.getExternalStorageDirectory()     // Catch:{ IOException -> 0x0071 }
            java.lang.String r2 = "/Xiaomi/"
            r0.<init>(r1, r2)     // Catch:{ IOException -> 0x0071 }
            boolean r1 = r0.exists()     // Catch:{ IOException -> 0x0071 }
            if (r1 == 0) goto L_0x003e
            boolean r1 = r0.isFile()     // Catch:{ IOException -> 0x0071 }
            if (r1 == 0) goto L_0x003e
            r0.delete()     // Catch:{ IOException -> 0x0071 }
        L_0x003e:
            java.io.File r1 = new java.io.File     // Catch:{ IOException -> 0x0071 }
            java.lang.String r2 = ".vdevid"
            r1.<init>(r0, r2)     // Catch:{ IOException -> 0x0071 }
            com.xiaomi.push.u r0 = com.xiaomi.push.u.a(r3, r1)     // Catch:{ IOException -> 0x0071 }
            com.xiaomi.push.y.a((java.io.File) r1)     // Catch:{ IOException -> 0x0056, all -> 0x0053 }
            java.lang.String r4 = e     // Catch:{ IOException -> 0x0056, all -> 0x0053 }
            com.xiaomi.push.y.a((java.io.File) r1, (java.lang.String) r4)     // Catch:{ IOException -> 0x0056, all -> 0x0053 }
            r4 = r0
            goto L_0x0059
        L_0x0053:
            r3 = move-exception
            r4 = r0
            goto L_0x008e
        L_0x0056:
            r3 = move-exception
            r4 = r0
            goto L_0x0072
        L_0x0059:
            java.io.File r0 = new java.io.File     // Catch:{ IOException -> 0x0071 }
            java.io.File r3 = r3.getFilesDir()     // Catch:{ IOException -> 0x0071 }
            java.lang.String r1 = ".vdevid"
            r0.<init>(r3, r1)     // Catch:{ IOException -> 0x0071 }
            java.lang.String r3 = e     // Catch:{ IOException -> 0x0071 }
            com.xiaomi.push.y.a((java.io.File) r0, (java.lang.String) r3)     // Catch:{ IOException -> 0x0071 }
            if (r4 == 0) goto L_0x008d
        L_0x006b:
            r4.a()
            goto L_0x008d
        L_0x006f:
            r3 = move-exception
            goto L_0x008e
        L_0x0071:
            r3 = move-exception
        L_0x0072:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x006f }
            r0.<init>()     // Catch:{ all -> 0x006f }
            java.lang.String r1 = "update vdevid failure :"
            r0.append(r1)     // Catch:{ all -> 0x006f }
            java.lang.String r3 = r3.getMessage()     // Catch:{ all -> 0x006f }
            r0.append(r3)     // Catch:{ all -> 0x006f }
            java.lang.String r3 = r0.toString()     // Catch:{ all -> 0x006f }
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.String) r3)     // Catch:{ all -> 0x006f }
            if (r4 == 0) goto L_0x008d
            goto L_0x006b
        L_0x008d:
            return
        L_0x008e:
            if (r4 == 0) goto L_0x0093
            r4.a()
        L_0x0093:
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.i.a(android.content.Context, java.lang.String):void");
    }

    /* renamed from: a  reason: collision with other method in class */
    private static boolean m421a(Context context) {
        boolean z = false;
        if (!m.a(context, "android.permission.WRITE_EXTERNAL_STORAGE") || l.a()) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= 26) {
            z = true;
        }
        return !z ? t.a(context) : z;
    }

    private static boolean a(String str) {
        return !TextUtils.isEmpty(str) && str.length() <= 15 && str.length() >= 14 && ay.b(str) && !ay.c(str);
    }

    public static String b(Context context) {
        try {
            return j.a(context).a();
        } catch (Exception e2) {
            b.a("failure to get gaid:" + e2.getMessage());
            return null;
        }
    }

    /* renamed from: b  reason: collision with other method in class */
    private static boolean m422b(Context context) {
        return context.getPackageManager().checkPermission("android.permission.READ_PHONE_STATE", context.getPackageName()) == 0;
    }

    /* JADX WARNING: Removed duplicated region for block: B:31:0x007c  */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x0084  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String c(android.content.Context r4) {
        /*
            boolean r0 = a((android.content.Context) r4)
            r1 = 0
            if (r0 != 0) goto L_0x0008
            return r1
        L_0x0008:
            java.lang.String r0 = e
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            if (r0 != 0) goto L_0x0013
            java.lang.String r4 = e
            return r4
        L_0x0013:
            java.io.File r0 = new java.io.File
            java.io.File r2 = r4.getFilesDir()
            java.lang.String r3 = ".vdevid"
            r0.<init>(r2, r3)
            java.lang.String r0 = com.xiaomi.push.y.a((java.io.File) r0)
            e = r0
            java.lang.String r0 = e
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            if (r0 != 0) goto L_0x002f
            java.lang.String r4 = e
            return r4
        L_0x002f:
            java.io.File r0 = new java.io.File     // Catch:{ IOException -> 0x0061 }
            java.io.File r2 = android.os.Environment.getExternalStorageDirectory()     // Catch:{ IOException -> 0x0061 }
            java.lang.String r3 = "/Xiaomi/"
            r0.<init>(r2, r3)     // Catch:{ IOException -> 0x0061 }
            java.io.File r2 = new java.io.File     // Catch:{ IOException -> 0x0061 }
            java.lang.String r3 = ".vdevid"
            r2.<init>(r0, r3)     // Catch:{ IOException -> 0x0061 }
            com.xiaomi.push.u r4 = com.xiaomi.push.u.a(r4, r2)     // Catch:{ IOException -> 0x0061 }
            java.lang.String r0 = ""
            e = r0     // Catch:{ IOException -> 0x005c, all -> 0x0059 }
            java.lang.String r0 = com.xiaomi.push.y.a((java.io.File) r2)     // Catch:{ IOException -> 0x005c, all -> 0x0059 }
            if (r0 == 0) goto L_0x0051
            e = r0     // Catch:{ IOException -> 0x005c, all -> 0x0059 }
        L_0x0051:
            java.lang.String r0 = e     // Catch:{ IOException -> 0x005c, all -> 0x0059 }
            if (r4 == 0) goto L_0x0058
            r4.a()
        L_0x0058:
            return r0
        L_0x0059:
            r0 = move-exception
            r1 = r4
            goto L_0x0082
        L_0x005c:
            r0 = move-exception
            r1 = r4
            goto L_0x0062
        L_0x005f:
            r0 = move-exception
            goto L_0x0082
        L_0x0061:
            r0 = move-exception
        L_0x0062:
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x005f }
            r4.<init>()     // Catch:{ all -> 0x005f }
            java.lang.String r2 = "getVDevID failure :"
            r4.append(r2)     // Catch:{ all -> 0x005f }
            java.lang.String r0 = r0.getMessage()     // Catch:{ all -> 0x005f }
            r4.append(r0)     // Catch:{ all -> 0x005f }
            java.lang.String r4 = r4.toString()     // Catch:{ all -> 0x005f }
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.String) r4)     // Catch:{ all -> 0x005f }
            if (r1 == 0) goto L_0x007f
            r1.a()
        L_0x007f:
            java.lang.String r4 = e
            return r4
        L_0x0082:
            if (r1 == 0) goto L_0x0087
            r1.a()
        L_0x0087:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.i.c(android.content.Context):java.lang.String");
    }

    /* JADX WARNING: Removed duplicated region for block: B:19:0x0061  */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x006b  */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x00be  */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x00c9  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String d(android.content.Context r6) {
        /*
            boolean r0 = a((android.content.Context) r6)
            r1 = 0
            if (r0 == 0) goto L_0x00cd
            boolean r0 = f580a
            if (r0 == 0) goto L_0x000d
            goto L_0x00cd
        L_0x000d:
            r0 = 1
            f580a = r0
            java.io.File r0 = new java.io.File
            java.io.File r2 = r6.getFilesDir()
            java.lang.String r3 = ".vdevid"
            r0.<init>(r2, r3)
            java.lang.String r0 = com.xiaomi.push.y.a((java.io.File) r0)
            java.io.File r2 = new java.io.File     // Catch:{ IOException -> 0x0045, all -> 0x0041 }
            java.io.File r3 = android.os.Environment.getExternalStorageDirectory()     // Catch:{ IOException -> 0x0045, all -> 0x0041 }
            java.lang.String r4 = "/Xiaomi/"
            r2.<init>(r3, r4)     // Catch:{ IOException -> 0x0045, all -> 0x0041 }
            java.io.File r3 = new java.io.File     // Catch:{ IOException -> 0x0045, all -> 0x0041 }
            java.lang.String r4 = ".vdevid"
            r3.<init>(r2, r4)     // Catch:{ IOException -> 0x0045, all -> 0x0041 }
            com.xiaomi.push.u r2 = com.xiaomi.push.u.a(r6, r3)     // Catch:{ IOException -> 0x0045, all -> 0x0041 }
            java.lang.String r3 = com.xiaomi.push.y.a((java.io.File) r3)     // Catch:{ IOException -> 0x003f }
            if (r2 == 0) goto L_0x0065
            r2.a()
            goto L_0x0065
        L_0x003f:
            r3 = move-exception
            goto L_0x0047
        L_0x0041:
            r6 = move-exception
            r2 = r1
            goto L_0x00c7
        L_0x0045:
            r3 = move-exception
            r2 = r1
        L_0x0047:
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x00c6 }
            r4.<init>()     // Catch:{ all -> 0x00c6 }
            java.lang.String r5 = "check id failure :"
            r4.append(r5)     // Catch:{ all -> 0x00c6 }
            java.lang.String r3 = r3.getMessage()     // Catch:{ all -> 0x00c6 }
            r4.append(r3)     // Catch:{ all -> 0x00c6 }
            java.lang.String r3 = r4.toString()     // Catch:{ all -> 0x00c6 }
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.String) r3)     // Catch:{ all -> 0x00c6 }
            if (r2 == 0) goto L_0x0064
            r2.a()
        L_0x0064:
            r3 = r1
        L_0x0065:
            boolean r2 = android.text.TextUtils.isEmpty(r0)
            if (r2 != 0) goto L_0x00be
            e = r0
            boolean r2 = android.text.TextUtils.isEmpty(r3)
            if (r2 != 0) goto L_0x0088
            int r2 = r3.length()
            r4 = 128(0x80, float:1.794E-43)
            if (r2 <= r4) goto L_0x007c
            goto L_0x0088
        L_0x007c:
            boolean r6 = android.text.TextUtils.equals(r0, r3)
            if (r6 != 0) goto L_0x009f
            java.lang.String r6 = "vid changed, need sync"
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.String) r6)
            return r3
        L_0x0088:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r4 = "recover vid :"
            r2.append(r4)
            r2.append(r3)
            java.lang.String r2 = r2.toString()
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.String) r2)
            a((android.content.Context) r6, (java.lang.String) r0)
        L_0x009f:
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r0 = "vdevid = "
            r6.append(r0)
            java.lang.String r0 = e
            r6.append(r0)
            java.lang.String r0 = " "
            r6.append(r0)
            r6.append(r3)
            java.lang.String r6 = r6.toString()
            com.xiaomi.channel.commonutils.logger.b.c(r6)
            return r1
        L_0x00be:
            java.lang.String r6 = "empty local vid"
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.String) r6)
            java.lang.String r6 = "F*"
            return r6
        L_0x00c6:
            r6 = move-exception
        L_0x00c7:
            if (r2 == 0) goto L_0x00cc
            r2.a()
        L_0x00cc:
            throw r6
        L_0x00cd:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.i.d(android.content.Context):java.lang.String");
    }

    public static String e(Context context) {
        try {
            return Settings.Secure.getString(context.getContentResolver(), "android_id");
        } catch (Throwable th) {
            b.a(th);
            return null;
        }
    }

    public static String f(Context context) {
        String g = g(context);
        int i = 10;
        while (g == null) {
            int i2 = i - 1;
            if (i <= 0) {
                break;
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException unused) {
            }
            g = g(context);
            i = i2;
        }
        return g;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0024, code lost:
        r1 = com.xiaomi.push.at.a((r1 = com.xiaomi.push.at.a("miui.telephony.TelephonyManager", "getDefault", new java.lang.Object[0])), "getMiuiDeviceId", new java.lang.Object[0]);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String g(android.content.Context r5) {
        /*
            boolean r0 = com.xiaomi.push.l.d()
            if (r0 == 0) goto L_0x0009
            java.lang.String r5 = ""
            return r5
        L_0x0009:
            java.lang.String r0 = a
            if (r0 == 0) goto L_0x0010
            java.lang.String r5 = a
            return r5
        L_0x0010:
            r0 = 0
            boolean r1 = com.xiaomi.push.l.a()     // Catch:{ Throwable -> 0x0088 }
            if (r1 == 0) goto L_0x003b
            java.lang.String r1 = "miui.telephony.TelephonyManager"
            java.lang.String r2 = "getDefault"
            r3 = 0
            java.lang.Object[] r4 = new java.lang.Object[r3]     // Catch:{ Throwable -> 0x0088 }
            java.lang.Object r1 = com.xiaomi.push.at.a((java.lang.String) r1, (java.lang.String) r2, (java.lang.Object[]) r4)     // Catch:{ Throwable -> 0x0088 }
            if (r1 == 0) goto L_0x003b
            java.lang.String r2 = "getMiuiDeviceId"
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch:{ Throwable -> 0x0088 }
            java.lang.Object r1 = com.xiaomi.push.at.a((java.lang.Object) r1, (java.lang.String) r2, (java.lang.Object[]) r3)     // Catch:{ Throwable -> 0x0088 }
            if (r1 == 0) goto L_0x003b
            boolean r2 = r1 instanceof java.lang.String     // Catch:{ Throwable -> 0x0088 }
            if (r2 == 0) goto L_0x003b
            java.lang.Class<java.lang.String> r2 = java.lang.String.class
            java.lang.Object r1 = r2.cast(r1)     // Catch:{ Throwable -> 0x0088 }
            java.lang.String r1 = (java.lang.String) r1     // Catch:{ Throwable -> 0x0088 }
            goto L_0x003c
        L_0x003b:
            r1 = r0
        L_0x003c:
            if (r1 != 0) goto L_0x007c
            boolean r2 = b((android.content.Context) r5)     // Catch:{ Throwable -> 0x0088 }
            if (r2 == 0) goto L_0x007c
            java.lang.String r2 = "phone"
            java.lang.Object r5 = r5.getSystemService(r2)     // Catch:{ Throwable -> 0x0088 }
            android.telephony.TelephonyManager r5 = (android.telephony.TelephonyManager) r5     // Catch:{ Throwable -> 0x0088 }
            int r2 = android.os.Build.VERSION.SDK_INT     // Catch:{ Throwable -> 0x0088 }
            r3 = 26
            if (r2 >= r3) goto L_0x0057
            java.lang.String r1 = r5.getDeviceId()     // Catch:{ Throwable -> 0x0088 }
            goto L_0x007c
        L_0x0057:
            r2 = 1
            int r3 = r5.getPhoneType()     // Catch:{ Throwable -> 0x0088 }
            if (r2 != r3) goto L_0x006b
            java.lang.String r1 = "getImei"
            r2 = r0
            java.lang.Object[] r2 = (java.lang.Object[]) r2     // Catch:{ Throwable -> 0x0088 }
            java.lang.Object r5 = com.xiaomi.push.at.a((java.lang.Object) r5, (java.lang.String) r1, (java.lang.Object[]) r2)     // Catch:{ Throwable -> 0x0088 }
        L_0x0067:
            r1 = r5
            java.lang.String r1 = (java.lang.String) r1     // Catch:{ Throwable -> 0x0088 }
            goto L_0x007c
        L_0x006b:
            r2 = 2
            int r3 = r5.getPhoneType()     // Catch:{ Throwable -> 0x0088 }
            if (r2 != r3) goto L_0x007c
            java.lang.String r1 = "getMeid"
            r2 = r0
            java.lang.Object[] r2 = (java.lang.Object[]) r2     // Catch:{ Throwable -> 0x0088 }
            java.lang.Object r5 = com.xiaomi.push.at.a((java.lang.Object) r5, (java.lang.String) r1, (java.lang.Object[]) r2)     // Catch:{ Throwable -> 0x0088 }
            goto L_0x0067
        L_0x007c:
            boolean r5 = a((java.lang.String) r1)     // Catch:{ Throwable -> 0x0088 }
            if (r5 == 0) goto L_0x0085
            a = r1     // Catch:{ Throwable -> 0x0088 }
            return r1
        L_0x0085:
            java.lang.String r5 = ""
            return r5
        L_0x0088:
            r5 = move-exception
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.Throwable) r5)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.i.g(android.content.Context):java.lang.String");
    }

    public static String h(Context context) {
        String j = j(context);
        int i = 10;
        while (j == null) {
            int i2 = i - 1;
            if (i <= 0) {
                break;
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException unused) {
            }
            j = j(context);
            i = i2;
        }
        return j;
    }

    public static String i(Context context) {
        Object a2;
        if (l.d() || Build.VERSION.SDK_INT < 22) {
            return "";
        }
        if (!TextUtils.isEmpty(b)) {
            return b;
        }
        if (!b(context)) {
            return "";
        }
        g(context);
        if (TextUtils.isEmpty(a)) {
            return "";
        }
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
            Integer num = (Integer) at.a((Object) telephonyManager, "getPhoneCount", new Object[0]);
            if (num == null || num.intValue() <= 1) {
                return "";
            }
            String str = null;
            for (int i = 0; i < num.intValue(); i++) {
                if (Build.VERSION.SDK_INT < 26) {
                    a2 = at.a((Object) telephonyManager, "getDeviceId", Integer.valueOf(i));
                } else if (1 == telephonyManager.getPhoneType()) {
                    a2 = at.a((Object) telephonyManager, "getImei", Integer.valueOf(i));
                } else {
                    if (2 == telephonyManager.getPhoneType()) {
                        a2 = at.a((Object) telephonyManager, "getMeid", Integer.valueOf(i));
                    }
                    if (!TextUtils.isEmpty(str) && !TextUtils.equals(a, str) && a(str)) {
                        b += str + ",";
                    }
                }
                str = (String) a2;
                b += str + ",";
            }
            int length = b.length();
            if (length > 0) {
                b = b.substring(0, length - 1);
            }
            return b;
        } catch (Exception e2) {
            b.d(e2.toString());
            return "";
        }
    }

    public static String j(Context context) {
        i(context);
        if (TextUtils.isEmpty(b)) {
            return "";
        }
        String str = "";
        for (String str2 : b.split(",")) {
            if (a(str2)) {
                str = str + ay.a(str2) + ",";
            }
        }
        int length = str.length();
        return length > 0 ? str.substring(0, length - 1) : str;
    }

    public static synchronized String k(Context context) {
        synchronized (i.class) {
            if (d != null) {
                String str = d;
                return str;
            }
            String e2 = e(context);
            String a2 = a();
            d = ay.b(e2 + a2);
            String str2 = d;
            return str2;
        }
    }

    public static synchronized String l(Context context) {
        String b2;
        synchronized (i.class) {
            String e2 = e(context);
            b2 = ay.b(e2 + null);
        }
        return b2;
    }

    public static String m(Context context) {
        return ((TelephonyManager) context.getSystemService("phone")).getSimOperatorName();
    }

    public static String n(Context context) {
        return "";
    }

    private static String o(Context context) {
        String g = g(context);
        int i = 10;
        while (TextUtils.isEmpty(g)) {
            int i2 = i - 1;
            if (i <= 0) {
                break;
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException unused) {
            }
            g = g(context);
            i = i2;
        }
        return g;
    }
}
