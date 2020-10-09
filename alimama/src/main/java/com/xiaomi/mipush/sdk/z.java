package com.xiaomi.mipush.sdk;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.text.TextUtils;
import java.util.Arrays;
import java.util.HashSet;

public class z {

    public static class a extends RuntimeException {
        public a(String str) {
            super(str);
        }
    }

    public static class b {
        public String a;

        /* renamed from: a  reason: collision with other field name */
        public boolean f78a;
        public String b;

        /* renamed from: b  reason: collision with other field name */
        public boolean f79b;

        public b(String str, boolean z, boolean z2, String str2) {
            this.a = str;
            this.f78a = z;
            this.f79b = z2;
            this.b = str2;
        }
    }

    private static ActivityInfo a(PackageManager packageManager, Intent intent, Class<?> cls) {
        for (ResolveInfo resolveInfo : packageManager.queryBroadcastReceivers(intent, 16384)) {
            ActivityInfo activityInfo = resolveInfo.activityInfo;
            if (activityInfo != null && cls.getCanonicalName().equals(activityInfo.name)) {
                return activityInfo;
            }
        }
        return null;
    }

    public static void a(Context context) {
        new Thread(new aa(context)).start();
    }

    private static void a(Context context, String str, String str2) {
        PackageManager packageManager = context.getPackageManager();
        String packageName = context.getPackageName();
        Intent intent = new Intent(str);
        intent.setPackage(packageName);
        boolean z = false;
        for (ResolveInfo resolveInfo : packageManager.queryBroadcastReceivers(intent, 16384)) {
            ActivityInfo activityInfo = resolveInfo.activityInfo;
            if (activityInfo == null || TextUtils.isEmpty(activityInfo.name) || !activityInfo.name.equals(str2)) {
                z = false;
                continue;
            } else {
                z = true;
                continue;
            }
            if (z) {
                break;
            }
        }
        if (!z) {
            throw new a(String.format("<receiver android:name=\"%1$s\" .../> is missing or disabled in AndroidManifest.", new Object[]{str2}));
        }
    }

    private static void a(ActivityInfo activityInfo, Boolean[] boolArr) {
        if (boolArr[0].booleanValue() != activityInfo.enabled) {
            throw new a(String.format("<receiver android:name=\"%1$s\" .../> in AndroidManifest had the wrong enabled attribute, which should be android:enabled=%2$b.", new Object[]{activityInfo.name, boolArr[0]}));
        } else if (boolArr[1].booleanValue() != activityInfo.exported) {
            throw new a(String.format("<receiver android:name=\"%1$s\" .../> in AndroidManifest had the wrong exported attribute, which should be android:exported=%2$b.", new Object[]{activityInfo.name, boolArr[1]}));
        }
    }

    private static boolean a(PackageInfo packageInfo, String[] strArr) {
        for (ServiceInfo serviceInfo : packageInfo.services) {
            if (a(strArr, serviceInfo.name)) {
                return true;
            }
        }
        return false;
    }

    private static boolean a(String[] strArr, String str) {
        if (!(strArr == null || str == null)) {
            for (String equals : strArr) {
                if (TextUtils.equals(equals, str)) {
                    return true;
                }
            }
        }
        return false;
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x007b  */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x00ab  */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x00cd  */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x00a9 A[EDGE_INSN: B:40:0x00a9->B:30:0x00a9 ?: BREAK  , SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x0075 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void c(android.content.Context r8) {
        /*
            android.content.pm.PackageManager r0 = r8.getPackageManager()
            java.lang.String r1 = r8.getPackageName()
            android.content.Intent r2 = new android.content.Intent
            java.lang.String r3 = com.xiaomi.push.service.ap.o
            r2.<init>(r3)
            r2.setPackage(r1)
            r3 = 1
            r4 = 0
            java.lang.String r5 = "com.xiaomi.push.service.receivers.PingReceiver"
            java.lang.Class r5 = java.lang.Class.forName(r5)     // Catch:{ ClassNotFoundException -> 0x005c }
            android.content.pm.ActivityInfo r2 = a((android.content.pm.PackageManager) r0, (android.content.Intent) r2, (java.lang.Class<?>) r5)     // Catch:{ ClassNotFoundException -> 0x005c }
            boolean r5 = com.xiaomi.mipush.sdk.MiPushClient.shouldUseMIUIPush(r8)     // Catch:{ ClassNotFoundException -> 0x005c }
            r6 = 2
            if (r5 != 0) goto L_0x004b
            if (r2 == 0) goto L_0x0039
            java.lang.Boolean[] r5 = new java.lang.Boolean[r6]     // Catch:{ ClassNotFoundException -> 0x005c }
            java.lang.Boolean r6 = java.lang.Boolean.valueOf(r3)     // Catch:{ ClassNotFoundException -> 0x005c }
            r5[r4] = r6     // Catch:{ ClassNotFoundException -> 0x005c }
            java.lang.Boolean r6 = java.lang.Boolean.valueOf(r4)     // Catch:{ ClassNotFoundException -> 0x005c }
            r5[r3] = r6     // Catch:{ ClassNotFoundException -> 0x005c }
        L_0x0035:
            a((android.content.pm.ActivityInfo) r2, (java.lang.Boolean[]) r5)     // Catch:{ ClassNotFoundException -> 0x005c }
            goto L_0x0060
        L_0x0039:
            com.xiaomi.mipush.sdk.z$a r2 = new com.xiaomi.mipush.sdk.z$a     // Catch:{ ClassNotFoundException -> 0x005c }
            java.lang.String r5 = "<receiver android:name=\"%1$s\" .../> is missing or disabled in AndroidManifest."
            java.lang.Object[] r6 = new java.lang.Object[r3]     // Catch:{ ClassNotFoundException -> 0x005c }
            java.lang.String r7 = "com.xiaomi.push.service.receivers.PingReceiver"
            r6[r4] = r7     // Catch:{ ClassNotFoundException -> 0x005c }
            java.lang.String r5 = java.lang.String.format(r5, r6)     // Catch:{ ClassNotFoundException -> 0x005c }
            r2.<init>(r5)     // Catch:{ ClassNotFoundException -> 0x005c }
            throw r2     // Catch:{ ClassNotFoundException -> 0x005c }
        L_0x004b:
            if (r2 == 0) goto L_0x0060
            java.lang.Boolean[] r5 = new java.lang.Boolean[r6]     // Catch:{ ClassNotFoundException -> 0x005c }
            java.lang.Boolean r6 = java.lang.Boolean.valueOf(r3)     // Catch:{ ClassNotFoundException -> 0x005c }
            r5[r4] = r6     // Catch:{ ClassNotFoundException -> 0x005c }
            java.lang.Boolean r6 = java.lang.Boolean.valueOf(r4)     // Catch:{ ClassNotFoundException -> 0x005c }
            r5[r3] = r6     // Catch:{ ClassNotFoundException -> 0x005c }
            goto L_0x0035
        L_0x005c:
            r2 = move-exception
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.Throwable) r2)
        L_0x0060:
            android.content.Intent r2 = new android.content.Intent
            java.lang.String r5 = "com.xiaomi.mipush.RECEIVE_MESSAGE"
            r2.<init>(r5)
            r2.setPackage(r1)
            r1 = 16384(0x4000, float:2.2959E-41)
            java.util.List r0 = r0.queryBroadcastReceivers(r2, r1)
            java.util.Iterator r0 = r0.iterator()
            r1 = 0
        L_0x0075:
            boolean r2 = r0.hasNext()
            if (r2 == 0) goto L_0x00a9
            java.lang.Object r2 = r0.next()
            android.content.pm.ResolveInfo r2 = (android.content.pm.ResolveInfo) r2
            android.content.pm.ActivityInfo r2 = r2.activityInfo
            if (r2 == 0) goto L_0x00a6
            java.lang.String r5 = r2.name     // Catch:{ ClassNotFoundException -> 0x00a1 }
            boolean r5 = android.text.TextUtils.isEmpty(r5)     // Catch:{ ClassNotFoundException -> 0x00a1 }
            if (r5 != 0) goto L_0x00a6
            java.lang.Class<com.xiaomi.mipush.sdk.PushMessageReceiver> r5 = com.xiaomi.mipush.sdk.PushMessageReceiver.class
            java.lang.String r6 = r2.name     // Catch:{ ClassNotFoundException -> 0x00a1 }
            java.lang.Class r6 = java.lang.Class.forName(r6)     // Catch:{ ClassNotFoundException -> 0x00a1 }
            boolean r5 = r5.isAssignableFrom(r6)     // Catch:{ ClassNotFoundException -> 0x00a1 }
            if (r5 == 0) goto L_0x00a6
            boolean r2 = r2.enabled     // Catch:{ ClassNotFoundException -> 0x00a1 }
            if (r2 == 0) goto L_0x00a6
            r1 = 1
            goto L_0x00a7
        L_0x00a1:
            r2 = move-exception
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.Throwable) r2)
            goto L_0x0075
        L_0x00a6:
            r1 = 0
        L_0x00a7:
            if (r1 == 0) goto L_0x0075
        L_0x00a9:
            if (r1 == 0) goto L_0x00cd
            boolean r0 = com.xiaomi.mipush.sdk.MiPushClient.getOpenHmsPush(r8)
            if (r0 == 0) goto L_0x00bf
            java.lang.String r0 = "com.huawei.android.push.intent.RECEIVE"
            java.lang.String r1 = "com.xiaomi.assemble.control.HmsPushReceiver"
            a((android.content.Context) r8, (java.lang.String) r0, (java.lang.String) r1)
            java.lang.String r0 = "com.huawei.intent.action.PUSH"
            java.lang.String r1 = "com.huawei.hms.support.api.push.PushEventReceiver"
            a((android.content.Context) r8, (java.lang.String) r0, (java.lang.String) r1)
        L_0x00bf:
            boolean r0 = com.xiaomi.mipush.sdk.MiPushClient.getOpenVIVOPush(r8)
            if (r0 == 0) goto L_0x00cc
            java.lang.String r0 = "com.vivo.pushclient.action.RECEIVE"
            java.lang.String r1 = "com.xiaomi.assemble.control.FTOSPushMessageReceiver"
            a((android.content.Context) r8, (java.lang.String) r0, (java.lang.String) r1)
        L_0x00cc:
            return
        L_0x00cd:
            com.xiaomi.mipush.sdk.z$a r8 = new com.xiaomi.mipush.sdk.z$a
            java.lang.String r0 = "Receiver: none of the subclasses of PushMessageReceiver is enabled or defined."
            r8.<init>(r0)
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.mipush.sdk.z.c(android.content.Context):void");
    }

    /* access modifiers changed from: private */
    public static void c(Context context, PackageInfo packageInfo) {
        boolean z;
        HashSet hashSet = new HashSet();
        String str = context.getPackageName() + ".permission.MIPUSH_RECEIVE";
        hashSet.addAll(Arrays.asList(new String[]{"android.permission.INTERNET", "android.permission.ACCESS_NETWORK_STATE", str, "android.permission.ACCESS_WIFI_STATE", "android.permission.READ_PHONE_STATE", "android.permission.GET_TASKS", "android.permission.VIBRATE"}));
        if (packageInfo.permissions != null) {
            PermissionInfo[] permissionInfoArr = packageInfo.permissions;
            int length = permissionInfoArr.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                } else if (str.equals(permissionInfoArr[i].name)) {
                    z = true;
                    break;
                } else {
                    i++;
                }
            }
        }
        z = false;
        if (z) {
            if (packageInfo.requestedPermissions != null) {
                for (String str2 : packageInfo.requestedPermissions) {
                    if (!TextUtils.isEmpty(str2) && hashSet.contains(str2)) {
                        hashSet.remove(str2);
                        if (hashSet.isEmpty()) {
                            break;
                        }
                    }
                }
            }
            if (!hashSet.isEmpty()) {
                throw new a(String.format("<uses-permission android:name=\"%1$s\"/> is missing in AndroidManifest.", new Object[]{hashSet.iterator().next()}));
            }
            return;
        }
        throw new a(String.format("<permission android:name=\"%1$s\" .../> is undefined in AndroidManifest.", new Object[]{str}));
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:3:0x004d, code lost:
        if (a(r12, new java.lang.String[]{"com.xiaomi.push.service.XMJobService", "com.xiaomi.push.service.XMPushService"}) != false) goto L_0x004f;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void d(android.content.Context r11, android.content.pm.PackageInfo r12) {
        /*
            java.util.HashMap r0 = new java.util.HashMap
            r0.<init>()
            java.util.HashMap r1 = new java.util.HashMap
            r1.<init>()
            java.lang.Class<com.xiaomi.mipush.sdk.PushMessageHandler> r2 = com.xiaomi.mipush.sdk.PushMessageHandler.class
            java.lang.String r2 = r2.getCanonicalName()
            com.xiaomi.mipush.sdk.z$b r3 = new com.xiaomi.mipush.sdk.z$b
            java.lang.Class<com.xiaomi.mipush.sdk.PushMessageHandler> r4 = com.xiaomi.mipush.sdk.PushMessageHandler.class
            java.lang.String r4 = r4.getCanonicalName()
            java.lang.String r5 = ""
            r6 = 1
            r3.<init>(r4, r6, r6, r5)
            r1.put(r2, r3)
            java.lang.Class<com.xiaomi.mipush.sdk.MessageHandleService> r2 = com.xiaomi.mipush.sdk.MessageHandleService.class
            java.lang.String r2 = r2.getCanonicalName()
            com.xiaomi.mipush.sdk.z$b r3 = new com.xiaomi.mipush.sdk.z$b
            java.lang.Class<com.xiaomi.mipush.sdk.MessageHandleService> r4 = com.xiaomi.mipush.sdk.MessageHandleService.class
            java.lang.String r4 = r4.getCanonicalName()
            java.lang.String r5 = ""
            r7 = 0
            r3.<init>(r4, r6, r7, r5)
            r1.put(r2, r3)
            boolean r2 = com.xiaomi.mipush.sdk.MiPushClient.shouldUseMIUIPush(r11)
            r3 = 2
            if (r2 == 0) goto L_0x004f
            java.lang.String[] r2 = new java.lang.String[r3]
            java.lang.String r4 = "com.xiaomi.push.service.XMJobService"
            r2[r7] = r4
            java.lang.String r4 = "com.xiaomi.push.service.XMPushService"
            r2[r6] = r4
            boolean r2 = a((android.content.pm.PackageInfo) r12, (java.lang.String[]) r2)
            if (r2 == 0) goto L_0x006b
        L_0x004f:
            java.lang.String r2 = "com.xiaomi.push.service.XMJobService"
            com.xiaomi.mipush.sdk.z$b r4 = new com.xiaomi.mipush.sdk.z$b
            java.lang.String r5 = "com.xiaomi.push.service.XMJobService"
            java.lang.String r8 = "android.permission.BIND_JOB_SERVICE"
            r4.<init>(r5, r6, r7, r8)
            r1.put(r2, r4)
            java.lang.String r2 = "com.xiaomi.push.service.XMPushService"
            com.xiaomi.mipush.sdk.z$b r4 = new com.xiaomi.mipush.sdk.z$b
            java.lang.String r5 = "com.xiaomi.push.service.XMPushService"
            java.lang.String r8 = ""
            r4.<init>(r5, r6, r7, r8)
            r1.put(r2, r4)
        L_0x006b:
            boolean r2 = com.xiaomi.mipush.sdk.MiPushClient.getOpenFCMPush(r11)
            if (r2 == 0) goto L_0x008d
            java.lang.String r2 = "com.xiaomi.assemble.control.MiFireBaseInstanceIdService"
            com.xiaomi.mipush.sdk.z$b r4 = new com.xiaomi.mipush.sdk.z$b
            java.lang.String r5 = "com.xiaomi.assemble.control.MiFireBaseInstanceIdService"
            java.lang.String r8 = ""
            r4.<init>(r5, r6, r7, r8)
            r1.put(r2, r4)
            java.lang.String r2 = "com.xiaomi.assemble.control.MiFirebaseMessagingService"
            com.xiaomi.mipush.sdk.z$b r4 = new com.xiaomi.mipush.sdk.z$b
            java.lang.String r5 = "com.xiaomi.assemble.control.MiFirebaseMessagingService"
            java.lang.String r8 = ""
            r4.<init>(r5, r6, r7, r8)
            r1.put(r2, r4)
        L_0x008d:
            boolean r11 = com.xiaomi.mipush.sdk.MiPushClient.getOpenOPPOPush(r11)
            if (r11 == 0) goto L_0x00a1
            java.lang.String r11 = "com.xiaomi.assemble.control.COSPushMessageService"
            com.xiaomi.mipush.sdk.z$b r2 = new com.xiaomi.mipush.sdk.z$b
            java.lang.String r4 = "com.xiaomi.assemble.control.COSPushMessageService"
            java.lang.String r5 = "com.coloros.mcs.permission.SEND_MCS_MESSAGE"
            r2.<init>(r4, r6, r6, r5)
            r1.put(r11, r2)
        L_0x00a1:
            android.content.pm.ServiceInfo[] r11 = r12.services
            if (r11 == 0) goto L_0x0138
            android.content.pm.ServiceInfo[] r11 = r12.services
            int r12 = r11.length
            r2 = 0
        L_0x00a9:
            if (r2 >= r12) goto L_0x0138
            r4 = r11[r2]
            java.lang.String r5 = r4.name
            boolean r5 = android.text.TextUtils.isEmpty(r5)
            if (r5 != 0) goto L_0x0134
            java.lang.String r5 = r4.name
            boolean r5 = r1.containsKey(r5)
            if (r5 == 0) goto L_0x0134
            java.lang.String r5 = r4.name
            java.lang.Object r5 = r1.remove(r5)
            com.xiaomi.mipush.sdk.z$b r5 = (com.xiaomi.mipush.sdk.z.b) r5
            boolean r8 = r5.f78a
            boolean r9 = r5.f79b
            java.lang.String r5 = r5.b
            boolean r10 = r4.enabled
            if (r8 != r10) goto L_0x011c
            boolean r8 = r4.exported
            if (r9 != r8) goto L_0x0104
            boolean r8 = android.text.TextUtils.isEmpty(r5)
            if (r8 != 0) goto L_0x00f6
            java.lang.String r8 = r4.permission
            boolean r8 = android.text.TextUtils.equals(r5, r8)
            if (r8 == 0) goto L_0x00e2
            goto L_0x00f6
        L_0x00e2:
            com.xiaomi.mipush.sdk.z$a r11 = new com.xiaomi.mipush.sdk.z$a
            java.lang.Object[] r12 = new java.lang.Object[r3]
            java.lang.String r0 = r4.name
            r12[r7] = r0
            r12[r6] = r5
            java.lang.String r0 = "<service android:name=\"%1$s\" .../> in AndroidManifest had the wrong permission attribute, which should be android:permission=\"%2$s\"."
            java.lang.String r12 = java.lang.String.format(r0, r12)
            r11.<init>(r12)
            throw r11
        L_0x00f6:
            java.lang.String r5 = r4.name
            java.lang.String r4 = r4.processName
            r0.put(r5, r4)
            boolean r4 = r1.isEmpty()
            if (r4 == 0) goto L_0x0134
            goto L_0x0138
        L_0x0104:
            com.xiaomi.mipush.sdk.z$a r11 = new com.xiaomi.mipush.sdk.z$a
            java.lang.Object[] r12 = new java.lang.Object[r3]
            java.lang.String r0 = r4.name
            r12[r7] = r0
            java.lang.Boolean r0 = java.lang.Boolean.valueOf(r9)
            r12[r6] = r0
            java.lang.String r0 = "<service android:name=\"%1$s\" .../> in AndroidManifest had the wrong exported attribute, which should be android:exported=%2$b."
            java.lang.String r12 = java.lang.String.format(r0, r12)
            r11.<init>(r12)
            throw r11
        L_0x011c:
            com.xiaomi.mipush.sdk.z$a r11 = new com.xiaomi.mipush.sdk.z$a
            java.lang.Object[] r12 = new java.lang.Object[r3]
            java.lang.String r0 = r4.name
            r12[r7] = r0
            java.lang.Boolean r0 = java.lang.Boolean.valueOf(r8)
            r12[r6] = r0
            java.lang.String r0 = "<service android:name=\"%1$s\" .../> in AndroidManifest had the wrong enabled attribute, which should be android:enabled=%2$b."
            java.lang.String r12 = java.lang.String.format(r0, r12)
            r11.<init>(r12)
            throw r11
        L_0x0134:
            int r2 = r2 + 1
            goto L_0x00a9
        L_0x0138:
            boolean r11 = r1.isEmpty()
            if (r11 == 0) goto L_0x01b8
            java.lang.Class<com.xiaomi.mipush.sdk.PushMessageHandler> r11 = com.xiaomi.mipush.sdk.PushMessageHandler.class
            java.lang.String r11 = r11.getCanonicalName()
            java.lang.Object r11 = r0.get(r11)
            java.lang.CharSequence r11 = (java.lang.CharSequence) r11
            java.lang.Class<com.xiaomi.mipush.sdk.MessageHandleService> r12 = com.xiaomi.mipush.sdk.MessageHandleService.class
            java.lang.String r12 = r12.getCanonicalName()
            java.lang.Object r12 = r0.get(r12)
            java.lang.CharSequence r12 = (java.lang.CharSequence) r12
            boolean r11 = android.text.TextUtils.equals(r11, r12)
            if (r11 == 0) goto L_0x019a
            java.lang.String r11 = "com.xiaomi.push.service.XMJobService"
            boolean r11 = r0.containsKey(r11)
            if (r11 == 0) goto L_0x0199
            java.lang.String r11 = "com.xiaomi.push.service.XMPushService"
            boolean r11 = r0.containsKey(r11)
            if (r11 == 0) goto L_0x0199
            java.lang.String r11 = "com.xiaomi.push.service.XMJobService"
            java.lang.Object r11 = r0.get(r11)
            java.lang.CharSequence r11 = (java.lang.CharSequence) r11
            java.lang.String r12 = "com.xiaomi.push.service.XMPushService"
            java.lang.Object r12 = r0.get(r12)
            java.lang.CharSequence r12 = (java.lang.CharSequence) r12
            boolean r11 = android.text.TextUtils.equals(r11, r12)
            if (r11 == 0) goto L_0x0183
            goto L_0x0199
        L_0x0183:
            com.xiaomi.mipush.sdk.z$a r11 = new com.xiaomi.mipush.sdk.z$a
            java.lang.Object[] r12 = new java.lang.Object[r3]
            java.lang.String r0 = "com.xiaomi.push.service.XMJobService"
            r12[r7] = r0
            java.lang.String r0 = "com.xiaomi.push.service.XMPushService"
            r12[r6] = r0
            java.lang.String r0 = "\"%1$s\" and \"%2$s\" must be running in the same process."
            java.lang.String r12 = java.lang.String.format(r0, r12)
            r11.<init>(r12)
            throw r11
        L_0x0199:
            return
        L_0x019a:
            com.xiaomi.mipush.sdk.z$a r11 = new com.xiaomi.mipush.sdk.z$a
            java.lang.Object[] r12 = new java.lang.Object[r3]
            java.lang.Class<com.xiaomi.mipush.sdk.PushMessageHandler> r0 = com.xiaomi.mipush.sdk.PushMessageHandler.class
            java.lang.String r0 = r0.getCanonicalName()
            r12[r7] = r0
            java.lang.Class<com.xiaomi.mipush.sdk.MessageHandleService> r0 = com.xiaomi.mipush.sdk.MessageHandleService.class
            java.lang.String r0 = r0.getCanonicalName()
            r12[r6] = r0
            java.lang.String r0 = "\"%1$s\" and \"%2$s\" must be running in the same process."
            java.lang.String r12 = java.lang.String.format(r0, r12)
            r11.<init>(r12)
            throw r11
        L_0x01b8:
            com.xiaomi.mipush.sdk.z$a r11 = new com.xiaomi.mipush.sdk.z$a
            java.lang.Object[] r12 = new java.lang.Object[r6]
            java.util.Set r0 = r1.keySet()
            java.util.Iterator r0 = r0.iterator()
            java.lang.Object r0 = r0.next()
            r12[r7] = r0
            java.lang.String r0 = "<service android:name=\"%1$s\" .../> is missing or disabled in AndroidManifest."
            java.lang.String r12 = java.lang.String.format(r0, r12)
            r11.<init>(r12)
            throw r11
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.mipush.sdk.z.d(android.content.Context, android.content.pm.PackageInfo):void");
    }
}
