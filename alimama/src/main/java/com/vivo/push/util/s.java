package com.vivo.push.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.text.TextUtils;
import com.taobao.weex.el.parse.Operators;
import com.vivo.push.cache.e;
import com.vivo.push.model.b;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import kotlin.UByte;

/* compiled from: PushPackageUtils */
public final class s {
    private static Boolean a;

    public static b a(Context context) {
        b bVar;
        b e;
        Context applicationContext = context.getApplicationContext();
        b e2 = e(applicationContext);
        if (e2 != null) {
            p.d("PushPackageUtils", "get system push info :" + e2);
            return e2;
        }
        List<String> f = f(applicationContext);
        b e3 = e(applicationContext, applicationContext.getPackageName());
        if (f.size() <= 0) {
            if (e3 != null && e3.d()) {
                e2 = e3;
            }
            p.a("PushPackageUtils", "findAllPushPackages error: find no package!");
        } else {
            b bVar2 = null;
            String a2 = y.b(applicationContext).a("com.vivo.push.cur_pkg", (String) null);
            if (TextUtils.isEmpty(a2) || !a(applicationContext, a2, "com.vivo.pushservice.action.METHOD") || (bVar = e(applicationContext, a2)) == null || !bVar.d()) {
                bVar = null;
            }
            if (e3 == null || !e3.d()) {
                e3 = null;
            }
            if (bVar == null) {
                bVar = null;
            }
            if (e3 != null && (bVar == null || (!e3.c() ? bVar.c() || e3.b() > bVar.b() : bVar.c() && e3.b() > bVar.b()))) {
                bVar = e3;
            }
            HashMap hashMap = new HashMap();
            if (bVar == null) {
                bVar = null;
            } else if (!bVar.c()) {
                bVar2 = bVar;
                bVar = null;
            }
            int size = f.size();
            b bVar3 = bVar2;
            b bVar4 = bVar;
            e2 = bVar3;
            for (int i = 0; i < size; i++) {
                String str = f.get(i);
                if (!TextUtils.isEmpty(str) && (e = e(applicationContext, str)) != null) {
                    hashMap.put(str, e);
                    if (e.d()) {
                        if (e.c()) {
                            if (bVar4 == null || e.b() > bVar4.b()) {
                                bVar4 = e;
                            }
                        } else if (e2 == null || e.b() > e2.b()) {
                            e2 = e;
                        }
                    }
                }
            }
            if (e2 == null) {
                p.d("PushPackageUtils", "findSuitablePushPackage, all push app in balck list.");
                e2 = bVar4;
            }
        }
        if (e2 == null) {
            p.b(applicationContext, "查找最优包为空!");
            p.d("PushPackageUtils", "finSuitablePushPackage is null");
        } else if (e2.c()) {
            p.a(applicationContext, "查找最优包为:" + e2.a() + Operators.BRACKET_START_STR + e2.b() + ", Black)");
            p.d("PushPackageUtils", "finSuitablePushPackage" + e2.a() + Operators.BRACKET_START_STR + e2.b() + ", Black)");
        } else {
            p.a(applicationContext, "查找最优包为:" + e2.a() + Operators.BRACKET_START_STR + e2.b() + Operators.BRACKET_END_STR);
            p.d("PushPackageUtils", "finSuitablePushPackage" + e2.a() + Operators.BRACKET_START_STR + e2.b() + Operators.BRACKET_END_STR);
        }
        return e2;
    }

    /* JADX WARNING: Removed duplicated region for block: B:54:0x00be A[SYNTHETIC, Splitter:B:54:0x00be] */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x00c5 A[SYNTHETIC, Splitter:B:59:0x00c5] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String b(android.content.Context r8) {
        /*
            r0 = 0
            android.content.ContentResolver r1 = r8.getContentResolver()     // Catch:{ Exception -> 0x00b1, all -> 0x00ac }
            android.net.Uri r2 = com.vivo.push.z.a     // Catch:{ Exception -> 0x00b1, all -> 0x00ac }
            r3 = 0
            r4 = 0
            r5 = 0
            r6 = 0
            android.database.Cursor r8 = r1.query(r2, r3, r4, r5, r6)     // Catch:{ Exception -> 0x00b1, all -> 0x00ac }
            if (r8 != 0) goto L_0x002d
            java.lang.String r1 = "PushPackageUtils"
            java.lang.String r2 = "cursor is null"
            com.vivo.push.util.p.a((java.lang.String) r1, (java.lang.String) r2)     // Catch:{ Exception -> 0x0027 }
            if (r8 == 0) goto L_0x0026
            r8.close()     // Catch:{ Exception -> 0x001e }
            goto L_0x0026
        L_0x001e:
            r8 = move-exception
            java.lang.String r1 = "PushPackageUtils"
            java.lang.String r2 = "close"
            com.vivo.push.util.p.a(r1, r2, r8)
        L_0x0026:
            return r0
        L_0x0027:
            r1 = move-exception
            r7 = r1
            r1 = r0
            r0 = r7
            goto L_0x00b5
        L_0x002d:
            r1 = 0
            r1 = r0
            r2 = 0
        L_0x0030:
            boolean r3 = r8.moveToNext()     // Catch:{ Exception -> 0x00aa }
            if (r3 == 0) goto L_0x0075
            java.lang.String r3 = "pushPkgName"
            java.lang.String r4 = "name"
            int r4 = r8.getColumnIndex(r4)     // Catch:{ Exception -> 0x00aa }
            java.lang.String r4 = r8.getString(r4)     // Catch:{ Exception -> 0x00aa }
            boolean r3 = r3.equals(r4)     // Catch:{ Exception -> 0x00aa }
            if (r3 == 0) goto L_0x0054
            java.lang.String r3 = "value"
            int r3 = r8.getColumnIndex(r3)     // Catch:{ Exception -> 0x00aa }
            java.lang.String r3 = r8.getString(r3)     // Catch:{ Exception -> 0x00aa }
            r1 = r3
            goto L_0x0030
        L_0x0054:
            java.lang.String r3 = "pushEnable"
            java.lang.String r4 = "name"
            int r4 = r8.getColumnIndex(r4)     // Catch:{ Exception -> 0x00aa }
            java.lang.String r4 = r8.getString(r4)     // Catch:{ Exception -> 0x00aa }
            boolean r3 = r3.equals(r4)     // Catch:{ Exception -> 0x00aa }
            if (r3 == 0) goto L_0x0030
            java.lang.String r2 = "value"
            int r2 = r8.getColumnIndex(r2)     // Catch:{ Exception -> 0x00aa }
            java.lang.String r2 = r8.getString(r2)     // Catch:{ Exception -> 0x00aa }
            boolean r2 = java.lang.Boolean.parseBoolean(r2)     // Catch:{ Exception -> 0x00aa }
            goto L_0x0030
        L_0x0075:
            boolean r3 = android.text.TextUtils.isEmpty(r1)     // Catch:{ Exception -> 0x00aa }
            if (r3 == 0) goto L_0x008a
            if (r8 == 0) goto L_0x0089
            r8.close()     // Catch:{ Exception -> 0x0081 }
            goto L_0x0089
        L_0x0081:
            r8 = move-exception
            java.lang.String r1 = "PushPackageUtils"
            java.lang.String r2 = "close"
            com.vivo.push.util.p.a(r1, r2, r8)
        L_0x0089:
            return r0
        L_0x008a:
            if (r2 != 0) goto L_0x009b
            if (r8 == 0) goto L_0x009a
            r8.close()     // Catch:{ Exception -> 0x0092 }
            goto L_0x009a
        L_0x0092:
            r8 = move-exception
            java.lang.String r1 = "PushPackageUtils"
            java.lang.String r2 = "close"
            com.vivo.push.util.p.a(r1, r2, r8)
        L_0x009a:
            return r0
        L_0x009b:
            if (r8 == 0) goto L_0x00c1
            r8.close()     // Catch:{ Exception -> 0x00a1 }
            goto L_0x00c1
        L_0x00a1:
            r8 = move-exception
            java.lang.String r0 = "PushPackageUtils"
            java.lang.String r2 = "close"
            com.vivo.push.util.p.a(r0, r2, r8)
            goto L_0x00c1
        L_0x00aa:
            r0 = move-exception
            goto L_0x00b5
        L_0x00ac:
            r8 = move-exception
            r7 = r0
            r0 = r8
            r8 = r7
            goto L_0x00c3
        L_0x00b1:
            r1 = move-exception
            r8 = r0
            r0 = r1
            r1 = r8
        L_0x00b5:
            java.lang.String r2 = "PushPackageUtils"
            java.lang.String r3 = "getSystemPush"
            com.vivo.push.util.p.a(r2, r3, r0)     // Catch:{ all -> 0x00c2 }
            if (r8 == 0) goto L_0x00c1
            r8.close()     // Catch:{ Exception -> 0x00a1 }
        L_0x00c1:
            return r1
        L_0x00c2:
            r0 = move-exception
        L_0x00c3:
            if (r8 == 0) goto L_0x00d1
            r8.close()     // Catch:{ Exception -> 0x00c9 }
            goto L_0x00d1
        L_0x00c9:
            r8 = move-exception
            java.lang.String r1 = "PushPackageUtils"
            java.lang.String r2 = "close"
            com.vivo.push.util.p.a(r1, r2, r8)
        L_0x00d1:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.vivo.push.util.s.b(android.content.Context):java.lang.String");
    }

    private static b e(Context context) {
        String b = b(context);
        ApplicationInfo applicationInfo = null;
        if (TextUtils.isEmpty(b)) {
            return null;
        }
        b bVar = new b(b);
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(b, 128);
            if (packageInfo != null) {
                bVar.a(packageInfo.versionCode);
                bVar.a(packageInfo.versionName);
                applicationInfo = packageInfo.applicationInfo;
            }
            if (applicationInfo != null) {
                bVar.a(z.a(context, b));
            }
            bVar.a(a(context, bVar.b()));
            bVar.b(a(context, b));
            return bVar;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            p.d("PushPackageUtils", "PackageManager NameNotFoundException is null");
            return null;
        }
    }

    private static b e(Context context, String str) {
        ApplicationInfo applicationInfo = null;
        if (!TextUtils.isEmpty(str)) {
            if (a(context, str, "com.vivo.pushservice.action.METHOD") || a(context, str, "com.vivo.pushservice.action.RECEIVE")) {
                b bVar = new b(str);
                try {
                    PackageInfo packageInfo = context.getPackageManager().getPackageInfo(str, 128);
                    if (packageInfo != null) {
                        bVar.a(packageInfo.versionCode);
                        bVar.a(packageInfo.versionName);
                        applicationInfo = packageInfo.applicationInfo;
                    }
                    if (applicationInfo != null) {
                        bVar.a(z.a(context, str));
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    p.a("PushPackageUtils", "getPushPackageInfo exception: ", e);
                }
                bVar.b(a(context, str));
                bVar.a(a(context, bVar.b()));
                return bVar;
            }
        }
        return null;
    }

    public static Set<String> c(Context context) {
        List<ResolveInfo> list;
        List<ResolveInfo> list2;
        HashSet hashSet = new HashSet();
        try {
            list = context.getPackageManager().queryBroadcastReceivers(new Intent("com.vivo.pushservice.action.RECEIVE"), 576);
        } catch (Exception unused) {
            list = null;
        }
        try {
            list2 = context.getPackageManager().queryBroadcastReceivers(new Intent("com.vivo.pushclient.action.RECEIVE"), 576);
        } catch (Exception unused2) {
            list2 = null;
        }
        if (list != null && list.size() > 0) {
            if (list2 != null && list2.size() > 0) {
                list.addAll(list2);
            }
            list2 = list;
        }
        if (list2 != null && list2.size() > 0) {
            for (ResolveInfo next : list2) {
                if (next != null) {
                    String str = next.activityInfo.packageName;
                    if (!TextUtils.isEmpty(str)) {
                        hashSet.add(str);
                    }
                }
            }
        }
        return hashSet;
    }

    public static boolean a(Context context, String str) {
        if (TextUtils.isEmpty(str) || context == null) {
            return false;
        }
        Intent intent = new Intent("com.vivo.pushservice.action.PUSH_SERVICE");
        intent.setPackage(str);
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> queryIntentServices = packageManager.queryIntentServices(intent, 576);
        if (queryIntentServices == null || queryIntentServices.size() <= 0) {
            p.a("PushPackageUtils", "isEnablePush error: can not find push service.");
            return false;
        }
        int size = queryIntentServices.size();
        boolean z = false;
        for (int i = 0; i < size; i++) {
            ResolveInfo resolveInfo = queryIntentServices.get(i);
            if (!(resolveInfo == null || resolveInfo.serviceInfo == null)) {
                String str2 = resolveInfo.serviceInfo.name;
                boolean z2 = resolveInfo.serviceInfo.exported;
                if ("com.vivo.push.sdk.service.PushService".equals(str2) && z2) {
                    boolean z3 = resolveInfo.serviceInfo.enabled;
                    int componentEnabledSetting = packageManager.getComponentEnabledSetting(new ComponentName(str, "com.vivo.push.sdk.service.PushService"));
                    boolean z4 = true;
                    if (componentEnabledSetting != 1 && (componentEnabledSetting != 0 || !z3)) {
                        z4 = false;
                    }
                    z = z4;
                }
            }
        }
        return z;
    }

    private static boolean a(Context context, long j) {
        e a2 = com.vivo.push.cache.b.a().a(context);
        if (a2 != null) {
            return a2.isInBlackList(j);
        }
        return false;
    }

    private static boolean a(Context context, String str, String str2) {
        List<ResolveInfo> list;
        Intent intent = new Intent(str2);
        intent.setPackage(str);
        try {
            list = context.getPackageManager().queryBroadcastReceivers(intent, 576);
        } catch (Exception unused) {
            list = null;
        }
        return list != null && list.size() > 0;
    }

    public static boolean b(Context context, String str) {
        return a(context, str, "com.vivo.pushclient.action.RECEIVE");
    }

    public static boolean c(Context context, String str) {
        return a(context, str, "com.vivo.pushservice.action.RECEIVE");
    }

    public static boolean d(Context context, String str) {
        return a(context, str, "com.vivo.pushservice.action.METHOD");
    }

    private static List<String> f(Context context) {
        List<ResolveInfo> list;
        h.a("findAllCoreClientPush");
        ArrayList arrayList = new ArrayList();
        try {
            list = context.getPackageManager().queryIntentServices(new Intent("com.vivo.pushservice.action.PUSH_SERVICE"), 576);
        } catch (Exception unused) {
            list = null;
        }
        if (list != null && list.size() > 0) {
            int size = list.size();
            for (int i = 0; i < size; i++) {
                ResolveInfo resolveInfo = list.get(i);
                if (resolveInfo != null) {
                    String str = resolveInfo.serviceInfo.packageName;
                    if (!TextUtils.isEmpty(str)) {
                        arrayList.add(str);
                    }
                }
            }
        }
        if (arrayList.size() <= 0) {
            p.d("PushPackageUtils", "get all push packages is null");
        }
        return arrayList;
    }

    private static String f(Context context, String str) {
        if (TextUtils.isEmpty(str) || context == null) {
            return null;
        }
        try {
            byte[] digest = MessageDigest.getInstance("SHA256").digest(context.getPackageManager().getPackageInfo(str, 64).signatures[0].toByteArray());
            StringBuffer stringBuffer = new StringBuffer();
            for (byte b : digest) {
                String upperCase = Integer.toHexString(b & UByte.MAX_VALUE).toUpperCase(Locale.US);
                if (upperCase.length() == 1) {
                    stringBuffer.append("0");
                }
                stringBuffer.append(upperCase);
            }
            return stringBuffer.toString();
        } catch (Exception e) {
            p.a("PushPackageUtils", (Throwable) e);
            return null;
        }
    }

    public static boolean d(Context context) {
        ProviderInfo resolveContentProvider;
        if (a != null) {
            return a.booleanValue();
        }
        String str = null;
        if (!(context == null || TextUtils.isEmpty("com.vivo.push.sdk.service.SystemPushConfig") || (resolveContentProvider = context.getPackageManager().resolveContentProvider("com.vivo.push.sdk.service.SystemPushConfig", 128)) == null)) {
            str = resolveContentProvider.packageName;
        }
        Boolean valueOf = Boolean.valueOf("BCC35D4D3606F154F0402AB7634E8490C0B244C2675C3C6238986987024F0C02".equals(f(context, str)));
        a = valueOf;
        return valueOf.booleanValue();
    }
}
