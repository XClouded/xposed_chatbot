package com.huawei.updatesdk.sdk.a.d.b;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import com.huawei.updatesdk.sdk.a.d.c.b;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class a {
    private static Map<String, Object> a = new HashMap();

    public static String a() {
        return Build.DISPLAY;
    }

    public static void a(Context context) {
        b(context);
    }

    public static long b(Context context) {
        long j;
        Long l = (Long) a.get("TotalMem");
        if (l != null) {
            j = l.longValue();
        } else {
            j = Build.VERSION.SDK_INT >= 16 ? f(context) : g(context);
            if (j > 0) {
                a.put("TotalMem", Long.valueOf(j));
            }
        }
        com.huawei.updatesdk.sdk.a.c.a.a.a.a("DeviceUtil", "getTotalMem, totalMem:" + j);
        return j;
    }

    public static String b() {
        DisplayMetrics h = h(com.huawei.updatesdk.sdk.service.a.a.a().b());
        return h != null ? String.valueOf(h.densityDpi) : "";
    }

    public static int c(Context context) {
        try {
            return Integer.parseInt(context.getPackageManager().getPackageInfo(context.getPackageName(), 16).versionCode + "");
        } catch (PackageManager.NameNotFoundException | NumberFormatException unused) {
            return 1;
        }
    }

    public static String c() {
        String str = "en";
        String str2 = "";
        String str3 = "US";
        Locale locale = Locale.getDefault();
        if (locale != null) {
            str = locale.getLanguage();
            if (Build.VERSION.SDK_INT >= 21) {
                str2 = locale.getScript();
            }
            str3 = locale.getCountry();
        }
        if (TextUtils.isEmpty(str2)) {
            return str + "_" + str3;
        }
        return str + "_" + str2 + "_" + str3;
    }

    public static String d() {
        DisplayMetrics h = h(com.huawei.updatesdk.sdk.service.a.a.a().b());
        if (h == null) {
            return "";
        }
        String valueOf = String.valueOf(h.widthPixels);
        String valueOf2 = String.valueOf(h.heightPixels);
        return valueOf + "_" + valueOf2;
    }

    public static String d(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 16).versionName + "";
        } catch (PackageManager.NameNotFoundException unused) {
            return null;
        }
    }

    public static String e(Context context) {
        String d = d(context);
        if (d == null) {
            return d;
        }
        int i = 0;
        int i2 = 0;
        while (i < d.length() && i2 < 3) {
            if (d.charAt(i) == '.') {
                i2++;
            }
            i++;
        }
        return 3 == i2 ? d.substring(0, i - 1) : d;
    }

    public static boolean e() {
        if (com.huawei.updatesdk.sdk.service.a.a.a() == null) {
            return false;
        }
        return b.a(com.huawei.updatesdk.sdk.service.a.a.a().b());
    }

    @SuppressLint({"NewApi"})
    private static long f(Context context) {
        try {
            ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
            ((ActivityManager) context.getSystemService("activity")).getMemoryInfo(memoryInfo);
            return memoryInfo.totalMem;
        } catch (Exception e) {
            com.huawei.updatesdk.sdk.a.c.a.a.a.d("DeviceUtil", e.getMessage());
            return 0;
        }
    }

    public static boolean f() {
        PackageManager packageManager = com.huawei.updatesdk.sdk.service.a.a.a().b().getPackageManager();
        try {
            packageManager.getPackageInfo("com.google.android.gsf.login", 16);
            packageManager.getPackageInfo("com.google.android.gsf", 16);
            return (packageManager.getPackageInfo("com.google.android.gms", 16).applicationInfo.flags & 1) != 0;
        } catch (PackageManager.NameNotFoundException e) {
            com.huawei.updatesdk.sdk.a.c.a.a.a.d("DeviceUtil", e.getMessage());
            return false;
        }
    }

    public static int g() {
        StringBuilder sb;
        String str;
        String str2;
        try {
            Class<?> cls = Class.forName("android.os.UserHandle");
            return ((Integer) cls.getMethod("myUserId", new Class[0]).invoke(cls, new Object[0])).intValue();
        } catch (IllegalArgumentException e) {
            str = "DeviceUtil";
            sb = new StringBuilder();
            sb.append("get current uid IllegalArgumentException! ");
            str2 = e.toString();
            sb.append(str2);
            com.huawei.updatesdk.sdk.a.c.a.a.a.d(str, sb.toString());
            return 0;
        } catch (ClassNotFoundException e2) {
            str = "DeviceUtil";
            sb = new StringBuilder();
            sb.append("get current uid ClassNotFoundException! ");
            str2 = e2.toString();
            sb.append(str2);
            com.huawei.updatesdk.sdk.a.c.a.a.a.d(str, sb.toString());
            return 0;
        } catch (NoSuchMethodException e3) {
            str = "DeviceUtil";
            sb = new StringBuilder();
            sb.append("get current uid NoSuchMethodException! ");
            str2 = e3.toString();
            sb.append(str2);
            com.huawei.updatesdk.sdk.a.c.a.a.a.d(str, sb.toString());
            return 0;
        } catch (InvocationTargetException e4) {
            str = "DeviceUtil";
            sb = new StringBuilder();
            sb.append("get current uid InvocationTargetException! ");
            str2 = e4.toString();
            sb.append(str2);
            com.huawei.updatesdk.sdk.a.c.a.a.a.d(str, sb.toString());
            return 0;
        } catch (IllegalAccessException e5) {
            str = "DeviceUtil";
            sb = new StringBuilder();
            sb.append("get current uid IllegalAccessException! ");
            str2 = e5.toString();
            sb.append(str2);
            com.huawei.updatesdk.sdk.a.c.a.a.a.d(str, sb.toString());
            return 0;
        } catch (Exception e6) {
            str = "DeviceUtil";
            sb = new StringBuilder();
            sb.append("get current uid IllegalAccessException! ");
            str2 = e6.toString();
            sb.append(str2);
            com.huawei.updatesdk.sdk.a.c.a.a.a.d(str, sb.toString());
            return 0;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:40:0x007d A[SYNTHETIC, Splitter:B:40:0x007d] */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x008d A[SYNTHETIC, Splitter:B:45:0x008d] */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x009e A[SYNTHETIC, Splitter:B:53:0x009e] */
    /* JADX WARNING: Removed duplicated region for block: B:58:0x00ae A[SYNTHETIC, Splitter:B:58:0x00ae] */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x00bf A[SYNTHETIC, Splitter:B:66:0x00bf] */
    /* JADX WARNING: Removed duplicated region for block: B:71:0x00cf A[SYNTHETIC, Splitter:B:71:0x00cf] */
    /* JADX WARNING: Removed duplicated region for block: B:79:0x00e0 A[SYNTHETIC, Splitter:B:79:0x00e0] */
    /* JADX WARNING: Removed duplicated region for block: B:84:0x00f0 A[SYNTHETIC, Splitter:B:84:0x00f0] */
    /* JADX WARNING: Removed duplicated region for block: B:90:0x00fb A[SYNTHETIC, Splitter:B:90:0x00fb] */
    /* JADX WARNING: Removed duplicated region for block: B:95:0x010b A[SYNTHETIC, Splitter:B:95:0x010b] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static long g(android.content.Context r7) {
        /*
            r7 = 0
            r0 = 0
            java.lang.String r2 = "/proc/meminfo"
            java.io.FileInputStream r3 = new java.io.FileInputStream     // Catch:{ NumberFormatException -> 0x00d3, UnsupportedEncodingException -> 0x00b2, FileNotFoundException -> 0x0091, IOException -> 0x0070, all -> 0x006c }
            r3.<init>(r2)     // Catch:{ NumberFormatException -> 0x00d3, UnsupportedEncodingException -> 0x00b2, FileNotFoundException -> 0x0091, IOException -> 0x0070, all -> 0x006c }
            java.io.BufferedReader r2 = new java.io.BufferedReader     // Catch:{ NumberFormatException -> 0x0069, UnsupportedEncodingException -> 0x0067, FileNotFoundException -> 0x0065, IOException -> 0x0063 }
            java.io.InputStreamReader r4 = new java.io.InputStreamReader     // Catch:{ NumberFormatException -> 0x0069, UnsupportedEncodingException -> 0x0067, FileNotFoundException -> 0x0065, IOException -> 0x0063 }
            java.lang.String r5 = "UTF-8"
            r4.<init>(r3, r5)     // Catch:{ NumberFormatException -> 0x0069, UnsupportedEncodingException -> 0x0067, FileNotFoundException -> 0x0065, IOException -> 0x0063 }
            r2.<init>(r4)     // Catch:{ NumberFormatException -> 0x0069, UnsupportedEncodingException -> 0x0067, FileNotFoundException -> 0x0065, IOException -> 0x0063 }
            java.lang.String r7 = r2.readLine()     // Catch:{ NumberFormatException -> 0x005d, UnsupportedEncodingException -> 0x0058, FileNotFoundException -> 0x0053, IOException -> 0x004e, all -> 0x0049 }
            if (r7 == 0) goto L_0x002a
            java.lang.String r4 = "\\s+"
            java.lang.String[] r7 = r7.split(r4)     // Catch:{ NumberFormatException -> 0x005d, UnsupportedEncodingException -> 0x0058, FileNotFoundException -> 0x0053, IOException -> 0x004e, all -> 0x0049 }
            r4 = 1
            r7 = r7[r4]     // Catch:{ NumberFormatException -> 0x005d, UnsupportedEncodingException -> 0x0058, FileNotFoundException -> 0x0053, IOException -> 0x004e, all -> 0x0049 }
            long r4 = java.lang.Long.parseLong(r7)     // Catch:{ NumberFormatException -> 0x005d, UnsupportedEncodingException -> 0x0058, FileNotFoundException -> 0x0053, IOException -> 0x004e, all -> 0x0049 }
            r0 = r4
        L_0x002a:
            r2.close()     // Catch:{ IOException -> 0x002e }
            goto L_0x0038
        L_0x002e:
            r7 = move-exception
            java.lang.String r2 = "DeviceUtil"
            java.lang.String r7 = r7.getMessage()
            com.huawei.updatesdk.sdk.a.c.a.a.a.d(r2, r7)
        L_0x0038:
            r3.close()     // Catch:{ IOException -> 0x003d }
            goto L_0x00f3
        L_0x003d:
            r7 = move-exception
            java.lang.String r2 = "DeviceUtil"
            java.lang.String r7 = r7.getMessage()
            com.huawei.updatesdk.sdk.a.c.a.a.a.d(r2, r7)
            goto L_0x00f3
        L_0x0049:
            r7 = move-exception
            r0 = r7
            r7 = r2
            goto L_0x00f9
        L_0x004e:
            r7 = move-exception
            r6 = r2
            r2 = r7
            r7 = r6
            goto L_0x0072
        L_0x0053:
            r7 = move-exception
            r6 = r2
            r2 = r7
            r7 = r6
            goto L_0x0093
        L_0x0058:
            r7 = move-exception
            r6 = r2
            r2 = r7
            r7 = r6
            goto L_0x00b4
        L_0x005d:
            r7 = move-exception
            r6 = r2
            r2 = r7
            r7 = r6
            goto L_0x00d5
        L_0x0063:
            r2 = move-exception
            goto L_0x0072
        L_0x0065:
            r2 = move-exception
            goto L_0x0093
        L_0x0067:
            r2 = move-exception
            goto L_0x00b4
        L_0x0069:
            r2 = move-exception
            goto L_0x00d5
        L_0x006c:
            r0 = move-exception
            r3 = r7
            goto L_0x00f9
        L_0x0070:
            r2 = move-exception
            r3 = r7
        L_0x0072:
            java.lang.String r4 = "DeviceUtil"
            java.lang.String r2 = r2.getMessage()     // Catch:{ all -> 0x00f8 }
            com.huawei.updatesdk.sdk.a.c.a.a.a.d(r4, r2)     // Catch:{ all -> 0x00f8 }
            if (r7 == 0) goto L_0x008b
            r7.close()     // Catch:{ IOException -> 0x0081 }
            goto L_0x008b
        L_0x0081:
            r7 = move-exception
            java.lang.String r2 = "DeviceUtil"
            java.lang.String r7 = r7.getMessage()
            com.huawei.updatesdk.sdk.a.c.a.a.a.d(r2, r7)
        L_0x008b:
            if (r3 == 0) goto L_0x00f3
            r3.close()     // Catch:{ IOException -> 0x003d }
            goto L_0x00f3
        L_0x0091:
            r2 = move-exception
            r3 = r7
        L_0x0093:
            java.lang.String r4 = "DeviceUtil"
            java.lang.String r2 = r2.getMessage()     // Catch:{ all -> 0x00f8 }
            com.huawei.updatesdk.sdk.a.c.a.a.a.d(r4, r2)     // Catch:{ all -> 0x00f8 }
            if (r7 == 0) goto L_0x00ac
            r7.close()     // Catch:{ IOException -> 0x00a2 }
            goto L_0x00ac
        L_0x00a2:
            r7 = move-exception
            java.lang.String r2 = "DeviceUtil"
            java.lang.String r7 = r7.getMessage()
            com.huawei.updatesdk.sdk.a.c.a.a.a.d(r2, r7)
        L_0x00ac:
            if (r3 == 0) goto L_0x00f3
            r3.close()     // Catch:{ IOException -> 0x003d }
            goto L_0x00f3
        L_0x00b2:
            r2 = move-exception
            r3 = r7
        L_0x00b4:
            java.lang.String r4 = "DeviceUtil"
            java.lang.String r2 = r2.getMessage()     // Catch:{ all -> 0x00f8 }
            com.huawei.updatesdk.sdk.a.c.a.a.a.d(r4, r2)     // Catch:{ all -> 0x00f8 }
            if (r7 == 0) goto L_0x00cd
            r7.close()     // Catch:{ IOException -> 0x00c3 }
            goto L_0x00cd
        L_0x00c3:
            r7 = move-exception
            java.lang.String r2 = "DeviceUtil"
            java.lang.String r7 = r7.getMessage()
            com.huawei.updatesdk.sdk.a.c.a.a.a.d(r2, r7)
        L_0x00cd:
            if (r3 == 0) goto L_0x00f3
            r3.close()     // Catch:{ IOException -> 0x003d }
            goto L_0x00f3
        L_0x00d3:
            r2 = move-exception
            r3 = r7
        L_0x00d5:
            java.lang.String r4 = "DeviceUtil"
            java.lang.String r2 = r2.getMessage()     // Catch:{ all -> 0x00f8 }
            com.huawei.updatesdk.sdk.a.c.a.a.a.d(r4, r2)     // Catch:{ all -> 0x00f8 }
            if (r7 == 0) goto L_0x00ee
            r7.close()     // Catch:{ IOException -> 0x00e4 }
            goto L_0x00ee
        L_0x00e4:
            r7 = move-exception
            java.lang.String r2 = "DeviceUtil"
            java.lang.String r7 = r7.getMessage()
            com.huawei.updatesdk.sdk.a.c.a.a.a.d(r2, r7)
        L_0x00ee:
            if (r3 == 0) goto L_0x00f3
            r3.close()     // Catch:{ IOException -> 0x003d }
        L_0x00f3:
            r2 = 1024(0x400, double:5.06E-321)
            long r0 = r0 * r2
            return r0
        L_0x00f8:
            r0 = move-exception
        L_0x00f9:
            if (r7 == 0) goto L_0x0109
            r7.close()     // Catch:{ IOException -> 0x00ff }
            goto L_0x0109
        L_0x00ff:
            r7 = move-exception
            java.lang.String r1 = "DeviceUtil"
            java.lang.String r7 = r7.getMessage()
            com.huawei.updatesdk.sdk.a.c.a.a.a.d(r1, r7)
        L_0x0109:
            if (r3 == 0) goto L_0x0119
            r3.close()     // Catch:{ IOException -> 0x010f }
            goto L_0x0119
        L_0x010f:
            r7 = move-exception
            java.lang.String r7 = r7.getMessage()
            java.lang.String r1 = "DeviceUtil"
            com.huawei.updatesdk.sdk.a.c.a.a.a.d(r1, r7)
        L_0x0119:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.huawei.updatesdk.sdk.a.d.b.a.g(android.content.Context):long");
    }

    private static DisplayMetrics h(Context context) {
        Display defaultDisplay;
        WindowManager windowManager = (WindowManager) context.getSystemService("window");
        if (windowManager == null || (defaultDisplay = windowManager.getDefaultDisplay()) == null) {
            return null;
        }
        DisplayMetrics displayMetrics = new DisplayMetrics();
        defaultDisplay.getMetrics(displayMetrics);
        return displayMetrics;
    }

    public static String h() {
        return Build.VERSION.RELEASE.trim();
    }
}
