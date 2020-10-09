package com.huawei.hms.support.api.push.a.c;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.core.app.NotificationCompat;
import com.huawei.hms.a.a;
import com.huawei.hms.support.api.push.a.b.a;

/* compiled from: PushNotification */
public class d {
    private static int a;

    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0170, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0175, code lost:
        return;
     */
    @android.annotation.TargetApi(26)
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized void a(android.content.Context r11, com.huawei.hms.support.api.push.a.b.a r12, java.lang.String r13) {
        /*
            java.lang.Class<com.huawei.hms.support.api.push.a.c.d> r0 = com.huawei.hms.support.api.push.a.c.d.class
            monitor-enter(r0)
            if (r11 == 0) goto L_0x0174
            if (r12 != 0) goto L_0x0009
            goto L_0x0174
        L_0x0009:
            java.lang.String r1 = "PushSelfShowLog"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x0171 }
            r2.<init>()     // Catch:{ all -> 0x0171 }
            java.lang.String r3 = " showNotification , the msg id = "
            r2.append(r3)     // Catch:{ all -> 0x0171 }
            java.lang.String r3 = r12.a()     // Catch:{ all -> 0x0171 }
            r2.append(r3)     // Catch:{ all -> 0x0171 }
            java.lang.String r2 = r2.toString()     // Catch:{ all -> 0x0171 }
            com.huawei.hms.support.log.a.a(r1, r2)     // Catch:{ all -> 0x0171 }
            int r1 = a     // Catch:{ all -> 0x0171 }
            if (r1 != 0) goto L_0x0049
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x0171 }
            r1.<init>()     // Catch:{ all -> 0x0171 }
            java.lang.String r2 = r11.getPackageName()     // Catch:{ all -> 0x0171 }
            r1.append(r2)     // Catch:{ all -> 0x0171 }
            java.util.Date r2 = new java.util.Date     // Catch:{ all -> 0x0171 }
            r2.<init>()     // Catch:{ all -> 0x0171 }
            java.lang.String r2 = r2.toString()     // Catch:{ all -> 0x0171 }
            r1.append(r2)     // Catch:{ all -> 0x0171 }
            java.lang.String r1 = r1.toString()     // Catch:{ all -> 0x0171 }
            int r1 = r1.hashCode()     // Catch:{ all -> 0x0171 }
            a = r1     // Catch:{ all -> 0x0171 }
        L_0x0049:
            java.lang.String r1 = r12.e()     // Catch:{ all -> 0x0171 }
            boolean r1 = android.text.TextUtils.isEmpty(r1)     // Catch:{ all -> 0x0171 }
            if (r1 == 0) goto L_0x006e
            int r1 = a     // Catch:{ all -> 0x0171 }
            int r1 = r1 + 1
            a = r1     // Catch:{ all -> 0x0171 }
            int r2 = a     // Catch:{ all -> 0x0171 }
            int r2 = r2 + 1
            a = r2     // Catch:{ all -> 0x0171 }
            int r3 = a     // Catch:{ all -> 0x0171 }
            int r3 = r3 + 1
            a = r3     // Catch:{ all -> 0x0171 }
            int r4 = a     // Catch:{ all -> 0x0171 }
            int r4 = r4 + 1
            a = r4     // Catch:{ all -> 0x0171 }
        L_0x006b:
            r9 = r2
            r10 = r3
            goto L_0x009c
        L_0x006e:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x0171 }
            r1.<init>()     // Catch:{ all -> 0x0171 }
            java.lang.String r2 = r12.i()     // Catch:{ all -> 0x0171 }
            r1.append(r2)     // Catch:{ all -> 0x0171 }
            java.lang.String r2 = r12.e()     // Catch:{ all -> 0x0171 }
            r1.append(r2)     // Catch:{ all -> 0x0171 }
            java.lang.String r1 = r1.toString()     // Catch:{ all -> 0x0171 }
            int r1 = r1.hashCode()     // Catch:{ all -> 0x0171 }
            int r2 = a     // Catch:{ all -> 0x0171 }
            int r2 = r2 + 1
            a = r2     // Catch:{ all -> 0x0171 }
            int r3 = a     // Catch:{ all -> 0x0171 }
            int r3 = r3 + 1
            a = r3     // Catch:{ all -> 0x0171 }
            int r4 = a     // Catch:{ all -> 0x0171 }
            int r4 = r4 + 1
            a = r4     // Catch:{ all -> 0x0171 }
            goto L_0x006b
        L_0x009c:
            java.lang.String r2 = "PushSelfShowLog"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x0171 }
            r3.<init>()     // Catch:{ all -> 0x0171 }
            java.lang.String r5 = "notifyId:"
            r3.append(r5)     // Catch:{ all -> 0x0171 }
            r3.append(r1)     // Catch:{ all -> 0x0171 }
            java.lang.String r5 = ",openNotifyId:"
            r3.append(r5)     // Catch:{ all -> 0x0171 }
            r3.append(r9)     // Catch:{ all -> 0x0171 }
            java.lang.String r5 = ",delNotifyId:"
            r3.append(r5)     // Catch:{ all -> 0x0171 }
            r3.append(r10)     // Catch:{ all -> 0x0171 }
            java.lang.String r5 = ",alarmNotifyId:"
            r3.append(r5)     // Catch:{ all -> 0x0171 }
            r3.append(r4)     // Catch:{ all -> 0x0171 }
            java.lang.String r3 = r3.toString()     // Catch:{ all -> 0x0171 }
            com.huawei.hms.support.log.a.a(r2, r3)     // Catch:{ all -> 0x0171 }
            r2 = 0
            boolean r3 = com.huawei.hms.support.api.push.a.d.a.a()     // Catch:{ all -> 0x0171 }
            if (r3 == 0) goto L_0x00d9
            r5 = r11
            r6 = r12
            r7 = r13
            r8 = r1
            android.app.Notification r2 = a(r5, r6, r7, r8, r9, r10)     // Catch:{ all -> 0x0171 }
        L_0x00d9:
            java.lang.String r3 = "notification"
            java.lang.Object r3 = r11.getSystemService(r3)     // Catch:{ all -> 0x0171 }
            android.app.NotificationManager r3 = (android.app.NotificationManager) r3     // Catch:{ all -> 0x0171 }
            if (r3 == 0) goto L_0x016f
            if (r2 == 0) goto L_0x016f
            int r5 = android.os.Build.VERSION.SDK_INT     // Catch:{ all -> 0x0171 }
            r6 = 26
            if (r5 < r6) goto L_0x0100
            java.lang.String r5 = "hms_push_channel"
            int r5 = com.huawei.hms.c.h.c(r5)     // Catch:{ all -> 0x0171 }
            java.lang.String r5 = r11.getString(r5)     // Catch:{ all -> 0x0171 }
            android.app.NotificationChannel r6 = new android.app.NotificationChannel     // Catch:{ all -> 0x0171 }
            java.lang.String r7 = "HwPushChannelID"
            r8 = 3
            r6.<init>(r7, r5, r8)     // Catch:{ all -> 0x0171 }
            r3.createNotificationChannel(r6)     // Catch:{ all -> 0x0171 }
        L_0x0100:
            r3.notify(r1, r2)     // Catch:{ all -> 0x0171 }
            int r2 = r12.f()     // Catch:{ all -> 0x0171 }
            if (r2 <= 0) goto L_0x016f
            android.content.Intent r2 = new android.content.Intent     // Catch:{ all -> 0x0171 }
            java.lang.String r3 = "com.huawei.intent.action.PUSH_DELAY_NOTIFY"
            r2.<init>(r3)     // Catch:{ all -> 0x0171 }
            java.lang.String r3 = "selfshow_info"
            byte[] r5 = r12.c()     // Catch:{ all -> 0x0171 }
            android.content.Intent r3 = r2.putExtra(r3, r5)     // Catch:{ all -> 0x0171 }
            java.lang.String r5 = "selfshow_token"
            byte[] r6 = r12.d()     // Catch:{ all -> 0x0171 }
            android.content.Intent r3 = r3.putExtra(r5, r6)     // Catch:{ all -> 0x0171 }
            java.lang.String r5 = "selfshow_event_id"
            java.lang.String r6 = "-1"
            android.content.Intent r3 = r3.putExtra(r5, r6)     // Catch:{ all -> 0x0171 }
            java.lang.String r5 = "extra_encrypt_data"
            android.content.Intent r13 = r3.putExtra(r5, r13)     // Catch:{ all -> 0x0171 }
            java.lang.String r3 = "selfshow_notify_id"
            android.content.Intent r13 = r13.putExtra(r3, r1)     // Catch:{ all -> 0x0171 }
            java.lang.String r1 = r11.getPackageName()     // Catch:{ all -> 0x0171 }
            android.content.Intent r13 = r13.setPackage(r1)     // Catch:{ all -> 0x0171 }
            r1 = 32
            r13.setFlags(r1)     // Catch:{ all -> 0x0171 }
            int r12 = r12.f()     // Catch:{ all -> 0x0171 }
            long r12 = (long) r12     // Catch:{ all -> 0x0171 }
            a(r11, r2, r12, r4)     // Catch:{ all -> 0x0171 }
            java.lang.String r11 = "PushSelfShowLog"
            java.lang.StringBuilder r12 = new java.lang.StringBuilder     // Catch:{ all -> 0x0171 }
            r12.<init>()     // Catch:{ all -> 0x0171 }
            java.lang.String r13 = "setDelayAlarm alarmNotityId"
            r12.append(r13)     // Catch:{ all -> 0x0171 }
            r12.append(r4)     // Catch:{ all -> 0x0171 }
            java.lang.String r13 = " and intent is "
            r12.append(r13)     // Catch:{ all -> 0x0171 }
            java.lang.String r13 = r2.toURI()     // Catch:{ all -> 0x0171 }
            r12.append(r13)     // Catch:{ all -> 0x0171 }
            java.lang.String r12 = r12.toString()     // Catch:{ all -> 0x0171 }
            com.huawei.hms.support.log.a.a(r11, r12)     // Catch:{ all -> 0x0171 }
        L_0x016f:
            monitor-exit(r0)
            return
        L_0x0171:
            r11 = move-exception
            monitor-exit(r0)
            throw r11
        L_0x0174:
            monitor-exit(r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.huawei.hms.support.api.push.a.c.d.a(android.content.Context, com.huawei.hms.support.api.push.a.b.a, java.lang.String):void");
    }

    private static PendingIntent a(Context context, a aVar, String str, int i, int i2) {
        Intent intent = new Intent("com.huawei.intent.action.PUSH_DELAY_NOTIFY");
        intent.putExtra("selfshow_info", aVar.c()).putExtra("selfshow_token", aVar.d()).putExtra("selfshow_event_id", "1").putExtra("extra_encrypt_data", str).putExtra("selfshow_notify_id", i).setPackage(context.getPackageName()).setFlags(268435456);
        return PendingIntent.getBroadcast(context, i2, intent, 134217728);
    }

    private static PendingIntent b(Context context, a aVar, String str, int i, int i2) {
        Intent intent = new Intent("com.huawei.intent.action.PUSH_DELAY_NOTIFY");
        intent.putExtra("selfshow_info", aVar.c()).putExtra("selfshow_token", aVar.d()).putExtra("selfshow_event_id", "2").putExtra("selfshow_notify_id", i).setPackage(context.getPackageName()).putExtra("extra_encrypt_data", str).setFlags(268435456);
        return PendingIntent.getBroadcast(context, i2, intent, 134217728);
    }

    @TargetApi(26)
    public static Notification a(Context context, a aVar, String str, int i, int i2, int i3) {
        Notification.Builder builder = new Notification.Builder(context);
        b.a(context, builder, aVar);
        int i4 = context.getApplicationInfo().labelRes;
        builder.setTicker(aVar.l());
        builder.setWhen(System.currentTimeMillis());
        builder.setAutoCancel(true);
        builder.setDefaults(1);
        if (aVar.n() == null || "".equals(aVar.n())) {
            builder.setContentTitle(context.getResources().getString(i4));
        } else {
            builder.setContentTitle(aVar.n());
        }
        builder.setContentText(aVar.l());
        builder.setContentIntent(a(context, aVar, str, i, i2));
        builder.setDeleteIntent(b(context, aVar, str, i, i3));
        Bitmap b = b.b(context, aVar);
        if (b != null) {
            builder.setLargeIcon(b);
        }
        if (Build.VERSION.SDK_INT >= 26) {
            builder.setChannelId("HwPushChannelID");
        }
        a(context, builder, aVar);
        b(context, builder, aVar);
        if (f.a(context, builder, i, aVar, b) != null) {
            return builder.getNotification();
        }
        com.huawei.hms.support.log.a.c("PushSelfShowLog", "builder is null after add style.");
        return null;
    }

    @SuppressLint({"NewApi"})
    private static void a(Context context, Notification.Builder builder, a aVar) {
        if ("com.huawei.android.pushagent".equals(context.getPackageName())) {
            Bundle bundle = new Bundle();
            String i = aVar.i();
            if (!TextUtils.isEmpty(i)) {
                bundle.putString("hw_origin_sender_package_name", i);
                builder.setExtras(bundle);
            }
        }
    }

    @SuppressLint({"NewApi"})
    private static void b(Context context, Notification.Builder builder, a aVar) {
        if (a.C0009a.a >= 11 && com.huawei.hms.support.api.push.a.d.a.c(context)) {
            Bundle bundle = new Bundle();
            String i = aVar.i();
            com.huawei.hms.support.log.a.b("PushSelfShowLog", "the package name of notification is:" + i);
            if (!TextUtils.isEmpty(i)) {
                String a2 = com.huawei.hms.support.api.push.a.d.a.a(context, i);
                com.huawei.hms.support.log.a.b("PushSelfShowLog", "the app name is:" + a2);
                if (a2 != null) {
                    bundle.putCharSequence("android.extraAppName", a2);
                }
            }
            builder.setExtras(bundle);
        }
    }

    public static void a(Context context, Intent intent, long j, int i) {
        try {
            com.huawei.hms.support.log.a.a("PushSelfShowLog", "enter setDelayAlarm(intent:" + intent.toURI() + " interval:" + j + "ms, context:" + context);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(NotificationCompat.CATEGORY_ALARM);
            if (alarmManager != null) {
                alarmManager.set(0, System.currentTimeMillis() + j, PendingIntent.getBroadcast(context, i, intent, 134217728));
            }
        } catch (RuntimeException unused) {
            com.huawei.hms.support.log.a.c("PushSelfShowLog", "set DelayAlarm error");
        } catch (Exception unused2) {
            com.huawei.hms.support.log.a.c("PushSelfShowLog", "set DelayAlarm error");
        }
    }
}
