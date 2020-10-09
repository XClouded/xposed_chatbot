package com.vivo.push.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.ComponentInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Process;
import android.text.TextUtils;
import com.alibaba.motu.tbrest.rest.RestUrlWrapper;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/* compiled from: Utility */
public final class z {
    private static String[] a = {"com.vivo.push.sdk.RegistrationReceiver", "com.vivo.push.sdk.service.PushService", "com.vivo.push.sdk.service.CommonJobService"};
    private static String[] b = {"android.permission.INTERNET", "android.permission.READ_PHONE_STATE", "android.permission.ACCESS_NETWORK_STATE", "android.permission.WRITE_SETTINGS", "android.permission.VIBRATE", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.ACCESS_WIFI_STATE", "android.permission.WAKE_LOCK", "android.permission.GET_ACCOUNTS", "com.bbk.account.permission.READ_ACCOUNTINFO", "android.permission.AUTHENTICATE_ACCOUNTS", "android.permission.MOUNT_UNMOUNT_FILESYSTEMS", "android.permission.GET_TASKS"};
    private static Boolean c;
    private static String[] d = {"com.vivo.push.sdk.service.CommandService", "com.vivo.push.sdk.service.CommonJobService"};
    private static String[] e = {"com.vivo.push.sdk.RegistrationReceiver"};
    private static String[] f = new String[0];

    public static boolean a(Context context) {
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses;
        if (c != null) {
            return c.booleanValue();
        }
        String b2 = s.b(context);
        if (context != null && context.getPackageName().equals(b2)) {
            Boolean bool = true;
            c = bool;
            return bool.booleanValue();
        } else if (context == null) {
            p.d("Utility", "isPushProcess context is null");
            return false;
        } else {
            int myPid = Process.myPid();
            ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
            String str = null;
            if (activityManager != null && (runningAppProcesses = activityManager.getRunningAppProcesses()) != null && runningAppProcesses.size() != 0) {
                Iterator<ActivityManager.RunningAppProcessInfo> it = runningAppProcesses.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    ActivityManager.RunningAppProcessInfo next = it.next();
                    if (next.pid == myPid) {
                        str = next.processName;
                        break;
                    }
                }
            }
            if (TextUtils.isEmpty(str)) {
                return false;
            }
            Boolean valueOf = Boolean.valueOf(str.contains(":pushservice"));
            c = valueOf;
            return valueOf.booleanValue();
        }
    }

    public static long b(Context context) {
        String b2 = s.b(context);
        if (!TextUtils.isEmpty(b2)) {
            return a(context, b2);
        }
        p.a("Utility", "systemPushPkgName is null");
        return -1;
    }

    public static long a(Context context, String str) {
        Object b2 = b(context, str, "com.vivo.push.sdk_version");
        if (b2 == null) {
            b2 = b(context, str, RestUrlWrapper.FIELD_SDK_VERSION);
        }
        if (b2 != null) {
            try {
                return Long.parseLong(b2.toString());
            } catch (Exception e2) {
                e2.printStackTrace();
                p.a("Utility", "getSdkVersionCode error ", e2);
                return -1;
            }
        } else {
            p.a("Utility", "getSdkVersionCode sdk version is null");
            return -1;
        }
    }

    public static String b(Context context, String str) {
        Object b2 = b(context, str, "com.vivo.push.app_id");
        if (b2 != null) {
            return b2.toString();
        }
        Object b3 = b(context, str, "app_id");
        return b3 != null ? b3.toString() : "";
    }

    private static Object b(Context context, String str, String str2) {
        if (context == null || TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(str, 128);
            Bundle bundle = applicationInfo != null ? applicationInfo.metaData : null;
            if (bundle != null) {
                return bundle.get(str2);
            }
            return null;
        } catch (PackageManager.NameNotFoundException unused) {
            return null;
        }
    }

    public static Object a(String str, String str2) throws Exception {
        Class<?> cls = Class.forName(str);
        return cls.getField(str2).get(cls);
    }

    public static void c(Context context) throws VivoPushException {
        String str;
        p.d("Utility", "check PushService AndroidManifest declearation !");
        String b2 = s.b(context);
        boolean c2 = s.c(context, context.getPackageName());
        boolean d2 = s.d(context, context.getPackageName());
        boolean b3 = s.b(context, context.getPackageName());
        if (d2) {
            a = new String[]{"com.vivo.push.sdk.RegistrationReceiver", "com.vivo.push.sdk.service.PushService", "com.vivo.push.sdk.service.CommonJobService"};
            b = new String[]{"android.permission.INTERNET", "android.permission.READ_PHONE_STATE", "android.permission.ACCESS_NETWORK_STATE", "android.permission.WRITE_SETTINGS", "android.permission.VIBRATE", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.ACCESS_WIFI_STATE", "android.permission.WAKE_LOCK", "android.permission.GET_ACCOUNTS", "com.bbk.account.permission.READ_ACCOUNTINFO", "android.permission.AUTHENTICATE_ACCOUNTS", "android.permission.MOUNT_UNMOUNT_FILESYSTEMS", "android.permission.GET_TASKS"};
            d = new String[]{"com.vivo.push.sdk.service.CommandService", "com.vivo.push.sdk.service.CommonJobService"};
            e = new String[]{"com.vivo.push.sdk.RegistrationReceiver"};
        } else if (b3 || c2) {
            if (b3) {
                d = new String[]{"com.vivo.push.sdk.service.CommandClientService"};
            } else {
                d = new String[]{"com.vivo.push.sdk.service.CommandService"};
            }
            e = new String[0];
            a = new String[0];
            if (c2) {
                b = new String[]{"android.permission.INTERNET", "android.permission.WRITE_SETTINGS"};
            } else {
                b = new String[]{"android.permission.INTERNET"};
            }
        } else {
            throw new VivoPushException("AndroidManifest.xml中receiver配置项错误，详见接入文档");
        }
        if (c2 || d2) {
            long a2 = a(context, context.getPackageName());
            long j = 293;
            if (context.getPackageName().equals(b2)) {
                j = 1293;
            }
            if (a2 == -1) {
                throw new VivoPushException("AndroidManifest.xml中未配置sdk_version");
            } else if (a2 != j) {
                throw new VivoPushException("AndroidManifest.xml中sdk_version配置项错误，请配置当前sdk_version版本为:" + j);
            }
        }
        g(context);
        e(context, b2);
        c(context, b2);
        d(context, b2);
        String packageName = context.getPackageName();
        Object b4 = b(context, packageName, "com.vivo.push.api_key");
        if (b4 != null) {
            str = b4.toString();
        } else {
            Object b5 = b(context, packageName, "api_key");
            if (b5 != null) {
                str = b5.toString();
            } else {
                str = "";
            }
        }
        if (TextUtils.isEmpty(str)) {
            throw new VivoPushException("com.vivo.push.api_key is null");
        } else if (TextUtils.isEmpty(b(context, context.getPackageName()))) {
            throw new VivoPushException("com.vivo.push.app_id is null");
        } else if ((c2 || d2) && a(context, context.getPackageName()) == -1) {
            throw new VivoPushException("sdkversion is null");
        } else if (d2) {
            a(context, "com.vivo.pushservice.action.METHOD", "com.vivo.push.sdk.RegistrationReceiver", true);
            a(context, "com.vivo.pushservice.action.PUSH_SERVICE", "com.vivo.push.sdk.service.PushService", false);
        }
    }

    private static void c(Context context, String str) throws VivoPushException {
        try {
            if (context.getPackageManager() != null) {
                ServiceInfo[] serviceInfoArr = context.getPackageManager().getPackageInfo(context.getPackageName(), 4).services;
                if (serviceInfoArr != null) {
                    for (String a2 : d) {
                        a(a2, (ComponentInfo[]) serviceInfoArr, str);
                    }
                    return;
                }
                throw new VivoPushException("serviceInfos is null");
            }
            throw new VivoPushException("localPackageManager is null");
        } catch (Exception e2) {
            throw new VivoPushException("error " + e2.getMessage());
        }
    }

    private static void d(Context context, String str) throws VivoPushException {
        if (f.length > 0) {
            try {
                if (context.getPackageManager() != null) {
                    ActivityInfo[] activityInfoArr = context.getPackageManager().getPackageInfo(context.getPackageName(), 1).activities;
                    if (activityInfoArr != null) {
                        for (String a2 : f) {
                            a(a2, (ComponentInfo[]) activityInfoArr, str);
                        }
                        return;
                    }
                    throw new VivoPushException("activityInfos is null");
                }
                throw new VivoPushException("localPackageManager is null");
            } catch (Exception e2) {
                throw new VivoPushException("error " + e2.getMessage());
            }
        }
    }

    private static void g(Context context) throws VivoPushException {
        try {
            PackageManager packageManager = context.getPackageManager();
            if (packageManager != null) {
                String[] strArr = packageManager.getPackageInfo(context.getPackageName(), 4096).requestedPermissions;
                if (strArr != null) {
                    String[] strArr2 = b;
                    int length = strArr2.length;
                    int i = 0;
                    while (i < length) {
                        String str = strArr2[i];
                        int length2 = strArr.length;
                        int i2 = 0;
                        while (i2 < length2) {
                            if (!str.equals(strArr[i2])) {
                                i2++;
                            } else {
                                i++;
                            }
                        }
                        throw new VivoPushException("permission : " + str + "  check fail : " + Arrays.toString(strArr));
                    }
                    return;
                }
                throw new VivoPushException("Permissions is null!");
            }
            throw new VivoPushException("localPackageManager is null");
        } catch (Exception e2) {
            throw new VivoPushException(e2.getMessage());
        }
    }

    private static void a(String str, ComponentInfo[] componentInfoArr, String str2) throws VivoPushException {
        int length = componentInfoArr.length;
        int i = 0;
        while (i < length) {
            ComponentInfo componentInfo = componentInfoArr[i];
            if (!str.equals(componentInfo.name)) {
                i++;
            } else if (componentInfo.enabled) {
                a(componentInfo, str2);
                return;
            } else {
                throw new VivoPushException(componentInfo.name + " module Push-SDK need is illegitmacy !");
            }
        }
        throw new VivoPushException(str + " module Push-SDK need is not exist");
    }

    private static void a(ComponentInfo componentInfo, String str) throws VivoPushException {
        if (!componentInfo.applicationInfo.packageName.equals(str)) {
            String[] strArr = a;
            int length = strArr.length;
            int i = 0;
            while (i < length) {
                if (!strArr[i].equals(componentInfo.name) || componentInfo.processName.contains(":pushservice")) {
                    i++;
                } else {
                    throw new VivoPushException("module : " + componentInfo.name + " process :" + componentInfo.processName + "  check process fail");
                }
            }
        }
    }

    private static void e(Context context, String str) throws VivoPushException {
        try {
            if (context.getPackageManager() != null) {
                ActivityInfo[] activityInfoArr = context.getPackageManager().getPackageInfo(context.getPackageName(), 2).receivers;
                if (activityInfoArr != null) {
                    for (String a2 : e) {
                        a(a2, (ComponentInfo[]) activityInfoArr, str);
                    }
                    return;
                }
                throw new VivoPushException("receivers is null");
            }
            throw new VivoPushException("localPackageManager is null");
        } catch (Exception e2) {
            throw new VivoPushException(e2.getMessage());
        }
    }

    private static void a(Context context, String str, String str2, boolean z) throws VivoPushException {
        Intent intent = new Intent(str);
        intent.setPackage(context.getPackageName());
        try {
            PackageManager packageManager = context.getPackageManager();
            if (packageManager == null) {
                throw new VivoPushException("localPackageManager is null");
            } else if (z) {
                List<ResolveInfo> queryBroadcastReceivers = packageManager.queryBroadcastReceivers(intent, 576);
                if (queryBroadcastReceivers == null || queryBroadcastReceivers.size() <= 0) {
                    throw new VivoPushException("checkModule " + intent + " has no receivers");
                }
                for (ResolveInfo resolveInfo : queryBroadcastReceivers) {
                    if (str2.equals(resolveInfo.activityInfo.name)) {
                        return;
                    }
                }
                throw new VivoPushException(str2 + " is missing");
            } else {
                List<ResolveInfo> queryIntentServices = packageManager.queryIntentServices(intent, 576);
                if (queryIntentServices == null || queryIntentServices.size() <= 0) {
                    throw new VivoPushException("checkModule " + intent + " has no services");
                }
                for (ResolveInfo next : queryIntentServices) {
                    if (str2.equals(next.serviceInfo.name)) {
                        if (!next.serviceInfo.exported) {
                            throw new VivoPushException(next.serviceInfo.name + " exported is false");
                        }
                        return;
                    }
                }
                throw new VivoPushException(str2 + " is missing");
            }
        } catch (Exception e2) {
            p.a("Utility", "error  " + e2.getMessage());
            throw new VivoPushException("checkModule error" + e2.getMessage());
        }
    }

    public static String b(String str, String str2) {
        String str3;
        try {
            str3 = (String) Class.forName("android.os.SystemProperties").getMethod("get", new Class[]{String.class}).invoke((Object) null, new Object[]{str});
        } catch (Exception e2) {
            e2.printStackTrace();
            str3 = str2;
        }
        return (str3 == null || str3.length() == 0) ? str2 : str3;
    }

    public static PublicKey d(Context context) {
        Cursor query = context.getContentResolver().query(com.vivo.push.z.a, (String[]) null, (String) null, (String[]) null, (String) null);
        if (query == null) {
            return null;
        }
        do {
            try {
                if (query.moveToNext()) {
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            } catch (Throwable th) {
                try {
                    query.close();
                } catch (Exception unused) {
                }
                throw th;
            }
            try {
                query.close();
            } catch (Exception unused2) {
            }
            return null;
        } while (!"pushkey".equals(query.getString(query.getColumnIndex("name"))));
        String string = query.getString(query.getColumnIndex("value"));
        p.d("Utility", "result key : " + string);
        PublicKey a2 = t.a(string);
        try {
            query.close();
        } catch (Exception unused3) {
        }
        return a2;
    }

    public static void a(Context context, Intent intent) {
        String b2 = s.b(context);
        String stringExtra = intent.getStringExtra("client_pkgname");
        if (TextUtils.isEmpty(b2)) {
            p.a("Utility", "illegality abe adapter : push pkg is null");
        } else if (TextUtils.isEmpty(stringExtra)) {
            p.a("Utility", "illegality abe adapter : src pkg is null");
        } else if (b2.equals(context.getPackageName())) {
            p.a("Utility", "illegality abe adapter : abe is not pushservice");
        } else if (!b2.equals(stringExtra)) {
            p.d("Utility", "proxy to core : intent pkg : " + intent.getPackage() + " ; src pkg : " + stringExtra + " ; push pkg : " + b2);
            intent.setPackage(b2);
            intent.setClassName(b2, "com.vivo.push.sdk.service.PushService");
            context.startService(intent);
        } else {
            p.a("Utility", "illegality abe adapter : pushPkg = " + b2 + " ; srcPkg = " + stringExtra);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:44:0x008e A[SYNTHETIC, Splitter:B:44:0x008e] */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x009d A[SYNTHETIC, Splitter:B:50:0x009d] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean e(android.content.Context r10) {
        /*
            r0 = 0
            r1 = 0
            if (r10 != 0) goto L_0x0012
            java.lang.String r10 = "Utility"
            java.lang.String r2 = "context is null"
            com.vivo.push.util.p.a((java.lang.String) r10, (java.lang.String) r2)     // Catch:{ Exception -> 0x000f }
            return r0
        L_0x000c:
            r10 = move-exception
            goto L_0x009b
        L_0x000f:
            r10 = move-exception
            goto L_0x0085
        L_0x0012:
            java.lang.String r2 = r10.getPackageName()     // Catch:{ Exception -> 0x000f }
            android.content.pm.PackageManager r3 = r10.getPackageManager()     // Catch:{ Exception -> 0x000f }
            android.content.pm.PackageInfo r3 = r3.getPackageInfo(r2, r0)     // Catch:{ Exception -> 0x000f }
            int r3 = r3.versionCode     // Catch:{ Exception -> 0x000f }
            android.content.ContentResolver r4 = r10.getContentResolver()     // Catch:{ Exception -> 0x000f }
            android.net.Uri r5 = com.vivo.push.z.b     // Catch:{ Exception -> 0x000f }
            r6 = 0
            java.lang.String r7 = "pushVersion = ? and appPkgName = ? and appCode = ? "
            r10 = 3
            java.lang.String[] r8 = new java.lang.String[r10]     // Catch:{ Exception -> 0x000f }
            java.lang.String r10 = "293"
            r8[r0] = r10     // Catch:{ Exception -> 0x000f }
            r10 = 1
            r8[r10] = r2     // Catch:{ Exception -> 0x000f }
            r2 = 2
            java.lang.String r3 = java.lang.String.valueOf(r3)     // Catch:{ Exception -> 0x000f }
            r8[r2] = r3     // Catch:{ Exception -> 0x000f }
            r9 = 0
            android.database.Cursor r2 = r4.query(r5, r6, r7, r8, r9)     // Catch:{ Exception -> 0x000f }
            if (r2 != 0) goto L_0x005d
            java.lang.String r10 = "Utility"
            java.lang.String r1 = "cursor is null"
            com.vivo.push.util.p.a((java.lang.String) r10, (java.lang.String) r1)     // Catch:{ Exception -> 0x005a, all -> 0x0057 }
            if (r2 == 0) goto L_0x0056
            r2.close()     // Catch:{ Exception -> 0x004e }
            goto L_0x0056
        L_0x004e:
            r10 = move-exception
            java.lang.String r1 = "Utility"
            java.lang.String r2 = "close"
            com.vivo.push.util.p.a(r1, r2, r10)
        L_0x0056:
            return r0
        L_0x0057:
            r10 = move-exception
            r1 = r2
            goto L_0x009b
        L_0x005a:
            r10 = move-exception
            r1 = r2
            goto L_0x0085
        L_0x005d:
            boolean r1 = r2.moveToFirst()     // Catch:{ Exception -> 0x005a, all -> 0x0057 }
            if (r1 == 0) goto L_0x007f
            java.lang.String r1 = "permission"
            int r1 = r2.getColumnIndex(r1)     // Catch:{ Exception -> 0x005a, all -> 0x0057 }
            int r1 = r2.getInt(r1)     // Catch:{ Exception -> 0x005a, all -> 0x0057 }
            r1 = r1 & r10
            if (r1 == 0) goto L_0x007f
            if (r2 == 0) goto L_0x007e
            r2.close()     // Catch:{ Exception -> 0x0076 }
            goto L_0x007e
        L_0x0076:
            r0 = move-exception
            java.lang.String r1 = "Utility"
            java.lang.String r2 = "close"
            com.vivo.push.util.p.a(r1, r2, r0)
        L_0x007e:
            return r10
        L_0x007f:
            if (r2 == 0) goto L_0x009a
            r2.close()     // Catch:{ Exception -> 0x0092 }
            goto L_0x009a
        L_0x0085:
            java.lang.String r2 = "Utility"
            java.lang.String r3 = "isSupport"
            com.vivo.push.util.p.a(r2, r3, r10)     // Catch:{ all -> 0x000c }
            if (r1 == 0) goto L_0x009a
            r1.close()     // Catch:{ Exception -> 0x0092 }
            goto L_0x009a
        L_0x0092:
            r10 = move-exception
            java.lang.String r1 = "Utility"
            java.lang.String r2 = "close"
            com.vivo.push.util.p.a(r1, r2, r10)
        L_0x009a:
            return r0
        L_0x009b:
            if (r1 == 0) goto L_0x00a9
            r1.close()     // Catch:{ Exception -> 0x00a1 }
            goto L_0x00a9
        L_0x00a1:
            r0 = move-exception
            java.lang.String r1 = "Utility"
            java.lang.String r2 = "close"
            com.vivo.push.util.p.a(r1, r2, r0)
        L_0x00a9:
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: com.vivo.push.util.z.e(android.content.Context):boolean");
    }

    /* JADX WARNING: Removed duplicated region for block: B:42:0x007e A[SYNTHETIC, Splitter:B:42:0x007e] */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x008d A[SYNTHETIC, Splitter:B:48:0x008d] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean a(android.content.Context r8, java.lang.String r9, java.lang.String r10) {
        /*
            r0 = 0
            r1 = 0
            if (r8 != 0) goto L_0x0011
            java.lang.String r8 = "Utility"
            java.lang.String r9 = "context is null"
            com.vivo.push.util.p.a((java.lang.String) r8, (java.lang.String) r9)     // Catch:{ Exception -> 0x000f }
            return r0
        L_0x000c:
            r8 = move-exception
            goto L_0x008b
        L_0x000f:
            r8 = move-exception
            goto L_0x0075
        L_0x0011:
            android.content.ContentResolver r2 = r8.getContentResolver()     // Catch:{ Exception -> 0x000f }
            android.net.Uri r3 = com.vivo.push.z.c     // Catch:{ Exception -> 0x000f }
            r4 = 0
            java.lang.String r5 = "appPkgName = ? and regId = ? sdkVersion = ? "
            r8 = 3
            java.lang.String[] r6 = new java.lang.String[r8]     // Catch:{ Exception -> 0x000f }
            r6[r0] = r9     // Catch:{ Exception -> 0x000f }
            r8 = 1
            r6[r8] = r10     // Catch:{ Exception -> 0x000f }
            r8 = 2
            java.lang.String r9 = "293"
            r6[r8] = r9     // Catch:{ Exception -> 0x000f }
            r7 = 0
            android.database.Cursor r8 = r2.query(r3, r4, r5, r6, r7)     // Catch:{ Exception -> 0x000f }
            if (r8 != 0) goto L_0x004c
            java.lang.String r9 = "Utility"
            java.lang.String r10 = "cursor is null"
            com.vivo.push.util.p.a((java.lang.String) r9, (java.lang.String) r10)     // Catch:{ Exception -> 0x0048, all -> 0x0044 }
            if (r8 == 0) goto L_0x0043
            r8.close()     // Catch:{ Exception -> 0x003b }
            goto L_0x0043
        L_0x003b:
            r8 = move-exception
            java.lang.String r9 = "Utility"
            java.lang.String r10 = "close"
            com.vivo.push.util.p.a(r9, r10, r8)
        L_0x0043:
            return r0
        L_0x0044:
            r9 = move-exception
            r1 = r8
            r8 = r9
            goto L_0x008b
        L_0x0048:
            r9 = move-exception
            r1 = r8
            r8 = r9
            goto L_0x0075
        L_0x004c:
            boolean r9 = r8.moveToFirst()     // Catch:{ Exception -> 0x0048, all -> 0x0044 }
            if (r9 == 0) goto L_0x006f
            java.lang.String r9 = "clientState"
            int r9 = r8.getColumnIndex(r9)     // Catch:{ Exception -> 0x0048, all -> 0x0044 }
            java.lang.String r9 = r8.getString(r9)     // Catch:{ Exception -> 0x0048, all -> 0x0044 }
            boolean r9 = java.lang.Boolean.parseBoolean(r9)     // Catch:{ Exception -> 0x0048, all -> 0x0044 }
            if (r8 == 0) goto L_0x006e
            r8.close()     // Catch:{ Exception -> 0x0066 }
            goto L_0x006e
        L_0x0066:
            r8 = move-exception
            java.lang.String r10 = "Utility"
            java.lang.String r0 = "close"
            com.vivo.push.util.p.a(r10, r0, r8)
        L_0x006e:
            return r9
        L_0x006f:
            if (r8 == 0) goto L_0x008a
            r8.close()     // Catch:{ Exception -> 0x0082 }
            goto L_0x008a
        L_0x0075:
            java.lang.String r9 = "Utility"
            java.lang.String r10 = "isOverdue"
            com.vivo.push.util.p.a(r9, r10, r8)     // Catch:{ all -> 0x000c }
            if (r1 == 0) goto L_0x008a
            r1.close()     // Catch:{ Exception -> 0x0082 }
            goto L_0x008a
        L_0x0082:
            r8 = move-exception
            java.lang.String r9 = "Utility"
            java.lang.String r10 = "close"
            com.vivo.push.util.p.a(r9, r10, r8)
        L_0x008a:
            return r0
        L_0x008b:
            if (r1 == 0) goto L_0x0099
            r1.close()     // Catch:{ Exception -> 0x0091 }
            goto L_0x0099
        L_0x0091:
            r9 = move-exception
            java.lang.String r10 = "Utility"
            java.lang.String r0 = "close"
            com.vivo.push.util.p.a(r10, r0, r9)
        L_0x0099:
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.vivo.push.util.z.a(android.content.Context, java.lang.String, java.lang.String):boolean");
    }

    /* JADX WARNING: Removed duplicated region for block: B:40:0x0075 A[SYNTHETIC, Splitter:B:40:0x0075] */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x0085 A[SYNTHETIC, Splitter:B:47:0x0085] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.util.Map<java.lang.String, java.lang.String> f(android.content.Context r8) {
        /*
            r0 = 0
            if (r8 != 0) goto L_0x0015
            java.lang.String r8 = "Utility"
            java.lang.String r1 = "getDebugInfo error : context is null"
            com.vivo.push.util.p.a((java.lang.String) r8, (java.lang.String) r1)     // Catch:{ Exception -> 0x0011, all -> 0x000b }
            return r0
        L_0x000b:
            r8 = move-exception
            r7 = r0
            r0 = r8
            r8 = r7
            goto L_0x0083
        L_0x0011:
            r8 = move-exception
            r1 = r8
            r8 = r0
            goto L_0x006c
        L_0x0015:
            android.content.ContentResolver r1 = r8.getContentResolver()     // Catch:{ Exception -> 0x0011, all -> 0x000b }
            android.net.Uri r2 = com.vivo.push.z.d     // Catch:{ Exception -> 0x0011, all -> 0x000b }
            r3 = 0
            r4 = 0
            r5 = 0
            r6 = 0
            android.database.Cursor r8 = r1.query(r2, r3, r4, r5, r6)     // Catch:{ Exception -> 0x0011, all -> 0x000b }
            if (r8 != 0) goto L_0x003d
            java.lang.String r1 = "Utility"
            java.lang.String r2 = "cursor is null"
            com.vivo.push.util.p.a((java.lang.String) r1, (java.lang.String) r2)     // Catch:{ Exception -> 0x003b }
            if (r8 == 0) goto L_0x003a
            r8.close()     // Catch:{ Exception -> 0x0032 }
            goto L_0x003a
        L_0x0032:
            r8 = move-exception
            java.lang.String r1 = "Utility"
            java.lang.String r2 = "close"
            com.vivo.push.util.p.a(r1, r2, r8)
        L_0x003a:
            return r0
        L_0x003b:
            r1 = move-exception
            goto L_0x006c
        L_0x003d:
            java.util.HashMap r1 = new java.util.HashMap     // Catch:{ Exception -> 0x003b }
            r1.<init>()     // Catch:{ Exception -> 0x003b }
        L_0x0042:
            boolean r2 = r8.moveToNext()     // Catch:{ Exception -> 0x003b }
            if (r2 == 0) goto L_0x005d
            int r2 = r8.getColumnCount()     // Catch:{ Exception -> 0x003b }
            r3 = 0
        L_0x004d:
            if (r3 >= r2) goto L_0x0042
            java.lang.String r4 = r8.getColumnName(r3)     // Catch:{ Exception -> 0x003b }
            java.lang.String r5 = r8.getString(r3)     // Catch:{ Exception -> 0x003b }
            r1.put(r4, r5)     // Catch:{ Exception -> 0x003b }
            int r3 = r3 + 1
            goto L_0x004d
        L_0x005d:
            if (r8 == 0) goto L_0x006b
            r8.close()     // Catch:{ Exception -> 0x0063 }
            goto L_0x006b
        L_0x0063:
            r8 = move-exception
            java.lang.String r0 = "Utility"
            java.lang.String r2 = "close"
            com.vivo.push.util.p.a(r0, r2, r8)
        L_0x006b:
            return r1
        L_0x006c:
            java.lang.String r2 = "Utility"
            java.lang.String r3 = "isOverdue"
            com.vivo.push.util.p.a(r2, r3, r1)     // Catch:{ all -> 0x0082 }
            if (r8 == 0) goto L_0x0081
            r8.close()     // Catch:{ Exception -> 0x0079 }
            goto L_0x0081
        L_0x0079:
            r8 = move-exception
            java.lang.String r1 = "Utility"
            java.lang.String r2 = "close"
            com.vivo.push.util.p.a(r1, r2, r8)
        L_0x0081:
            return r0
        L_0x0082:
            r0 = move-exception
        L_0x0083:
            if (r8 == 0) goto L_0x0091
            r8.close()     // Catch:{ Exception -> 0x0089 }
            goto L_0x0091
        L_0x0089:
            r8 = move-exception
            java.lang.String r1 = "Utility"
            java.lang.String r2 = "close"
            com.vivo.push.util.p.a(r1, r2, r8)
        L_0x0091:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.vivo.push.util.z.f(android.content.Context):java.util.Map");
    }
}
