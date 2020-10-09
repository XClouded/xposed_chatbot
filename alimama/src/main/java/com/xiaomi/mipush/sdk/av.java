package com.xiaomi.mipush.sdk;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.ServiceInfo;
import android.taobao.windvane.util.ConfigStorage;
import android.text.TextUtils;
import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.mipush.sdk.PushMessageHandler;
import com.xiaomi.push.Cif;
import com.xiaomi.push.ai;
import com.xiaomi.push.ay;
import com.xiaomi.push.bk;
import com.xiaomi.push.ev;
import com.xiaomi.push.hg;
import com.xiaomi.push.hq;
import com.xiaomi.push.hs;
import com.xiaomi.push.ht;
import com.xiaomi.push.hu;
import com.xiaomi.push.hw;
import com.xiaomi.push.hx;
import com.xiaomi.push.ic;
import com.xiaomi.push.ij;
import com.xiaomi.push.iq;
import com.xiaomi.push.ir;
import com.xiaomi.push.iw;
import com.xiaomi.push.r;
import com.xiaomi.push.service.z;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.TimeZone;

public class av {
    private static av a;

    /* renamed from: a  reason: collision with other field name */
    private static Object f36a = new Object();

    /* renamed from: a  reason: collision with other field name */
    private static Queue<String> f37a;

    /* renamed from: a  reason: collision with other field name */
    private Context f38a;

    private av(Context context) {
        this.f38a = context.getApplicationContext();
        if (this.f38a == null) {
            this.f38a = context;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:61:0x0148  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static android.content.Intent a(android.content.Context r6, java.lang.String r7, java.util.Map<java.lang.String, java.lang.String> r8) {
        /*
            r0 = 0
            if (r8 == 0) goto L_0x018d
            java.lang.String r1 = "notify_effect"
            boolean r1 = r8.containsKey(r1)
            if (r1 != 0) goto L_0x000d
            goto L_0x018d
        L_0x000d:
            java.lang.String r1 = "notify_effect"
            java.lang.Object r1 = r8.get(r1)
            java.lang.String r1 = (java.lang.String) r1
            r2 = -1
            java.lang.String r3 = "intent_flag"
            java.lang.Object r3 = r8.get(r3)
            java.lang.String r3 = (java.lang.String) r3
            boolean r4 = android.text.TextUtils.isEmpty(r3)     // Catch:{ NumberFormatException -> 0x002a }
            if (r4 != 0) goto L_0x0043
            int r3 = java.lang.Integer.parseInt(r3)     // Catch:{ NumberFormatException -> 0x002a }
            r2 = r3
            goto L_0x0043
        L_0x002a:
            r3 = move-exception
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "Cause by intent_flag: "
            r4.append(r5)
            java.lang.String r3 = r3.getMessage()
            r4.append(r3)
            java.lang.String r3 = r4.toString()
            com.xiaomi.channel.commonutils.logger.b.d(r3)
        L_0x0043:
            java.lang.String r3 = com.xiaomi.push.service.ap.a
            boolean r3 = r3.equals(r1)
            if (r3 == 0) goto L_0x0071
            android.content.pm.PackageManager r8 = r6.getPackageManager()     // Catch:{ Exception -> 0x0056 }
            android.content.Intent r7 = r8.getLaunchIntentForPackage(r7)     // Catch:{ Exception -> 0x0056 }
            r8 = r7
            goto L_0x0146
        L_0x0056:
            r7 = move-exception
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>()
            java.lang.String r1 = "Cause: "
            r8.append(r1)
            java.lang.String r7 = r7.getMessage()
            r8.append(r7)
            java.lang.String r7 = r8.toString()
            com.xiaomi.channel.commonutils.logger.b.d(r7)
            goto L_0x0145
        L_0x0071:
            java.lang.String r3 = com.xiaomi.push.service.ap.b
            boolean r3 = r3.equals(r1)
            if (r3 == 0) goto L_0x00c9
            java.lang.String r1 = "intent_uri"
            boolean r1 = r8.containsKey(r1)
            if (r1 == 0) goto L_0x00a9
            java.lang.String r1 = "intent_uri"
            java.lang.Object r8 = r8.get(r1)
            java.lang.String r8 = (java.lang.String) r8
            if (r8 == 0) goto L_0x0145
            r1 = 1
            android.content.Intent r8 = android.content.Intent.parseUri(r8, r1)     // Catch:{ URISyntaxException -> 0x0097 }
            r8.setPackage(r7)     // Catch:{ URISyntaxException -> 0x0095 }
            goto L_0x0146
        L_0x0095:
            r7 = move-exception
            goto L_0x0099
        L_0x0097:
            r7 = move-exception
            r8 = r0
        L_0x0099:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r3 = "Cause: "
            r1.append(r3)
            java.lang.String r7 = r7.getMessage()
            goto L_0x013a
        L_0x00a9:
            java.lang.String r1 = "class_name"
            boolean r1 = r8.containsKey(r1)
            if (r1 == 0) goto L_0x0145
            java.lang.String r1 = "class_name"
            java.lang.Object r8 = r8.get(r1)
            java.lang.String r8 = (java.lang.String) r8
            android.content.Intent r1 = new android.content.Intent
            r1.<init>()
            android.content.ComponentName r3 = new android.content.ComponentName
            r3.<init>(r7, r8)
            r1.setComponent(r3)
            r8 = r1
            goto L_0x0146
        L_0x00c9:
            java.lang.String r7 = com.xiaomi.push.service.ap.c
            boolean r7 = r7.equals(r1)
            if (r7 == 0) goto L_0x0145
            java.lang.String r7 = "web_uri"
            java.lang.Object r7 = r8.get(r7)
            java.lang.String r7 = (java.lang.String) r7
            if (r7 == 0) goto L_0x0145
            java.lang.String r7 = r7.trim()
            java.lang.String r8 = "http://"
            boolean r8 = r7.startsWith(r8)
            if (r8 != 0) goto L_0x0100
            java.lang.String r8 = "https://"
            boolean r8 = r7.startsWith(r8)
            if (r8 != 0) goto L_0x0100
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>()
            java.lang.String r1 = "http://"
            r8.append(r1)
            r8.append(r7)
            java.lang.String r7 = r8.toString()
        L_0x0100:
            java.net.URL r8 = new java.net.URL     // Catch:{ MalformedURLException -> 0x012a }
            r8.<init>(r7)     // Catch:{ MalformedURLException -> 0x012a }
            java.lang.String r8 = r8.getProtocol()     // Catch:{ MalformedURLException -> 0x012a }
            java.lang.String r1 = "http"
            boolean r1 = r1.equals(r8)     // Catch:{ MalformedURLException -> 0x012a }
            if (r1 != 0) goto L_0x0119
            java.lang.String r1 = "https"
            boolean r8 = r1.equals(r8)     // Catch:{ MalformedURLException -> 0x012a }
            if (r8 == 0) goto L_0x0145
        L_0x0119:
            android.content.Intent r8 = new android.content.Intent     // Catch:{ MalformedURLException -> 0x012a }
            java.lang.String r1 = "android.intent.action.VIEW"
            r8.<init>(r1)     // Catch:{ MalformedURLException -> 0x012a }
            android.net.Uri r7 = android.net.Uri.parse(r7)     // Catch:{ MalformedURLException -> 0x0128 }
            r8.setData(r7)     // Catch:{ MalformedURLException -> 0x0128 }
            goto L_0x0146
        L_0x0128:
            r7 = move-exception
            goto L_0x012c
        L_0x012a:
            r7 = move-exception
            r8 = r0
        L_0x012c:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r3 = "Cause: "
            r1.append(r3)
            java.lang.String r7 = r7.getMessage()
        L_0x013a:
            r1.append(r7)
            java.lang.String r7 = r1.toString()
            com.xiaomi.channel.commonutils.logger.b.d(r7)
            goto L_0x0146
        L_0x0145:
            r8 = r0
        L_0x0146:
            if (r8 == 0) goto L_0x018d
            if (r2 < 0) goto L_0x014d
            r8.setFlags(r2)
        L_0x014d:
            r7 = 268435456(0x10000000, float:2.5243549E-29)
            r8.addFlags(r7)
            android.content.pm.PackageManager r6 = r6.getPackageManager()     // Catch:{ Exception -> 0x0174 }
            r7 = 65536(0x10000, float:9.18355E-41)
            android.content.pm.ResolveInfo r6 = r6.resolveActivity(r8, r7)     // Catch:{ Exception -> 0x0174 }
            if (r6 == 0) goto L_0x015f
            return r8
        L_0x015f:
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0174 }
            r6.<init>()     // Catch:{ Exception -> 0x0174 }
            java.lang.String r7 = "not resolve activity:"
            r6.append(r7)     // Catch:{ Exception -> 0x0174 }
            r6.append(r8)     // Catch:{ Exception -> 0x0174 }
            java.lang.String r6 = r6.toString()     // Catch:{ Exception -> 0x0174 }
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.String) r6)     // Catch:{ Exception -> 0x0174 }
            goto L_0x018d
        L_0x0174:
            r6 = move-exception
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            java.lang.String r8 = "Cause: "
            r7.append(r8)
            java.lang.String r6 = r6.getMessage()
            r7.append(r6)
            java.lang.String r6 = r7.toString()
            com.xiaomi.channel.commonutils.logger.b.d(r6)
        L_0x018d:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.mipush.sdk.av.a(android.content.Context, java.lang.String, java.util.Map):android.content.Intent");
    }

    /* JADX WARNING: type inference failed for: r4v16, types: [java.util.List] */
    /* JADX WARNING: type inference failed for: r10v5, types: [java.util.List, java.util.ArrayList] */
    /* JADX WARNING: type inference failed for: r12v0, types: [java.util.List] */
    /* JADX WARNING: type inference failed for: r10v7, types: [java.util.List, java.util.ArrayList] */
    /* JADX WARNING: type inference failed for: r12v1, types: [java.util.List] */
    /* JADX WARNING: type inference failed for: r10v9, types: [java.util.List, java.util.ArrayList] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 3 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private com.xiaomi.mipush.sdk.PushMessageHandler.a a(com.xiaomi.push.ic r20, boolean r21, byte[] r22, java.lang.String r23, int r24) {
        /*
            r19 = this;
            r1 = r19
            r2 = r20
            r0 = r21
            r3 = r22
            r8 = r23
            r9 = r24
            r10 = 0
            android.content.Context r4 = r1.f38a     // Catch:{ v -> 0x09b7, iw -> 0x099a }
            com.xiaomi.push.ir r4 = com.xiaomi.mipush.sdk.ar.a((android.content.Context) r4, (com.xiaomi.push.ic) r2)     // Catch:{ v -> 0x09b7, iw -> 0x099a }
            if (r4 != 0) goto L_0x0041
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ v -> 0x09b7, iw -> 0x099a }
            r0.<init>()     // Catch:{ v -> 0x09b7, iw -> 0x099a }
            java.lang.String r3 = "receiving an un-recognized message. "
            r0.append(r3)     // Catch:{ v -> 0x09b7, iw -> 0x099a }
            com.xiaomi.push.hg r3 = r2.f606a     // Catch:{ v -> 0x09b7, iw -> 0x099a }
            r0.append(r3)     // Catch:{ v -> 0x09b7, iw -> 0x099a }
            java.lang.String r0 = r0.toString()     // Catch:{ v -> 0x09b7, iw -> 0x099a }
            com.xiaomi.channel.commonutils.logger.b.d(r0)     // Catch:{ v -> 0x09b7, iw -> 0x099a }
            android.content.Context r0 = r1.f38a     // Catch:{ v -> 0x09b7, iw -> 0x099a }
            com.xiaomi.push.ev r0 = com.xiaomi.push.ev.a((android.content.Context) r0)     // Catch:{ v -> 0x09b7, iw -> 0x099a }
            android.content.Context r3 = r1.f38a     // Catch:{ v -> 0x09b7, iw -> 0x099a }
            java.lang.String r3 = r3.getPackageName()     // Catch:{ v -> 0x09b7, iw -> 0x099a }
            java.lang.String r4 = com.xiaomi.push.eu.a((int) r24)     // Catch:{ v -> 0x09b7, iw -> 0x099a }
            java.lang.String r5 = "receiving an un-recognized message."
            r0.b(r3, r4, r8, r5)     // Catch:{ v -> 0x09b7, iw -> 0x099a }
            return r10
        L_0x0041:
            com.xiaomi.push.hg r5 = r20.a()
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r7 = "processing a message, action="
            r6.append(r7)
            r6.append(r5)
            java.lang.String r6 = r6.toString()
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.String) r6)
            int[] r6 = com.xiaomi.mipush.sdk.ax.a
            int r5 = r5.ordinal()
            r5 = r6[r5]
            r6 = 1
            r11 = 0
            r7 = 0
            switch(r5) {
                case 1: goto L_0x070f;
                case 2: goto L_0x0661;
                case 3: goto L_0x0646;
                case 4: goto L_0x060d;
                case 5: goto L_0x05d4;
                case 6: goto L_0x04b0;
                case 7: goto L_0x006a;
                default: goto L_0x0068;
            }
        L_0x0068:
            goto L_0x0999
        L_0x006a:
            android.content.Context r0 = r1.f38a
            java.lang.String r0 = r0.getPackageName()
            android.content.Context r5 = r1.f38a
            com.xiaomi.push.hg r8 = com.xiaomi.push.hg.Notification
            int r3 = r3.length
            com.xiaomi.push.da.a(r0, r5, r4, r8, r3)
            boolean r0 = r4 instanceof com.xiaomi.push.hx
            if (r0 == 0) goto L_0x020b
            com.xiaomi.push.hx r4 = (com.xiaomi.push.hx) r4
            java.lang.String r0 = r4.a()
            com.xiaomi.push.hq r2 = com.xiaomi.push.hq.DisablePushMessage
            java.lang.String r2 = r2.f485a
            java.lang.String r3 = r4.f570d
            boolean r2 = r2.equalsIgnoreCase(r3)
            r3 = 10
            if (r2 == 0) goto L_0x0147
            long r4 = r4.f563a
            int r2 = (r4 > r11 ? 1 : (r4 == r11 ? 0 : -1))
            if (r2 != 0) goto L_0x00eb
            java.lang.Class<com.xiaomi.mipush.sdk.ao> r2 = com.xiaomi.mipush.sdk.ao.class
            monitor-enter(r2)
            android.content.Context r3 = r1.f38a     // Catch:{ all -> 0x00e8 }
            com.xiaomi.mipush.sdk.ao r3 = com.xiaomi.mipush.sdk.ao.a((android.content.Context) r3)     // Catch:{ all -> 0x00e8 }
            boolean r3 = r3.a((java.lang.String) r0)     // Catch:{ all -> 0x00e8 }
            if (r3 == 0) goto L_0x00e5
            android.content.Context r3 = r1.f38a     // Catch:{ all -> 0x00e8 }
            com.xiaomi.mipush.sdk.ao r3 = com.xiaomi.mipush.sdk.ao.a((android.content.Context) r3)     // Catch:{ all -> 0x00e8 }
            r3.c(r0)     // Catch:{ all -> 0x00e8 }
            java.lang.String r0 = "syncing"
            android.content.Context r3 = r1.f38a     // Catch:{ all -> 0x00e8 }
            com.xiaomi.mipush.sdk.ao r3 = com.xiaomi.mipush.sdk.ao.a((android.content.Context) r3)     // Catch:{ all -> 0x00e8 }
            com.xiaomi.mipush.sdk.bd r4 = com.xiaomi.mipush.sdk.bd.DISABLE_PUSH     // Catch:{ all -> 0x00e8 }
            java.lang.String r3 = r3.a((com.xiaomi.mipush.sdk.bd) r4)     // Catch:{ all -> 0x00e8 }
            boolean r0 = r0.equals(r3)     // Catch:{ all -> 0x00e8 }
            if (r0 == 0) goto L_0x00e5
            android.content.Context r0 = r1.f38a     // Catch:{ all -> 0x00e8 }
            com.xiaomi.mipush.sdk.ao r0 = com.xiaomi.mipush.sdk.ao.a((android.content.Context) r0)     // Catch:{ all -> 0x00e8 }
            com.xiaomi.mipush.sdk.bd r3 = com.xiaomi.mipush.sdk.bd.DISABLE_PUSH     // Catch:{ all -> 0x00e8 }
            java.lang.String r4 = "synced"
            r0.a(r3, r4)     // Catch:{ all -> 0x00e8 }
            android.content.Context r0 = r1.f38a     // Catch:{ all -> 0x00e8 }
            com.xiaomi.mipush.sdk.MiPushClient.clearNotification(r0)     // Catch:{ all -> 0x00e8 }
            android.content.Context r0 = r1.f38a     // Catch:{ all -> 0x00e8 }
            com.xiaomi.mipush.sdk.MiPushClient.clearLocalNotificationType(r0)     // Catch:{ all -> 0x00e8 }
            com.xiaomi.mipush.sdk.PushMessageHandler.a()     // Catch:{ all -> 0x00e8 }
            android.content.Context r0 = r1.f38a     // Catch:{ all -> 0x00e8 }
            com.xiaomi.mipush.sdk.ay r0 = com.xiaomi.mipush.sdk.ay.a((android.content.Context) r0)     // Catch:{ all -> 0x00e8 }
            r0.b()     // Catch:{ all -> 0x00e8 }
        L_0x00e5:
            monitor-exit(r2)     // Catch:{ all -> 0x00e8 }
            goto L_0x0999
        L_0x00e8:
            r0 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x00e8 }
            throw r0
        L_0x00eb:
            java.lang.String r2 = "syncing"
            android.content.Context r4 = r1.f38a
            com.xiaomi.mipush.sdk.ao r4 = com.xiaomi.mipush.sdk.ao.a((android.content.Context) r4)
            com.xiaomi.mipush.sdk.bd r5 = com.xiaomi.mipush.sdk.bd.DISABLE_PUSH
            java.lang.String r4 = r4.a((com.xiaomi.mipush.sdk.bd) r5)
            boolean r2 = r2.equals(r4)
            if (r2 == 0) goto L_0x013c
            java.lang.Class<com.xiaomi.mipush.sdk.ao> r2 = com.xiaomi.mipush.sdk.ao.class
            monitor-enter(r2)
            android.content.Context r4 = r1.f38a     // Catch:{ all -> 0x0139 }
            com.xiaomi.mipush.sdk.ao r4 = com.xiaomi.mipush.sdk.ao.a((android.content.Context) r4)     // Catch:{ all -> 0x0139 }
            boolean r4 = r4.a((java.lang.String) r0)     // Catch:{ all -> 0x0139 }
            if (r4 == 0) goto L_0x0136
            android.content.Context r4 = r1.f38a     // Catch:{ all -> 0x0139 }
            com.xiaomi.mipush.sdk.ao r4 = com.xiaomi.mipush.sdk.ao.a((android.content.Context) r4)     // Catch:{ all -> 0x0139 }
            int r4 = r4.a((java.lang.String) r0)     // Catch:{ all -> 0x0139 }
            if (r4 >= r3) goto L_0x012d
            android.content.Context r3 = r1.f38a     // Catch:{ all -> 0x0139 }
            com.xiaomi.mipush.sdk.ao r3 = com.xiaomi.mipush.sdk.ao.a((android.content.Context) r3)     // Catch:{ all -> 0x0139 }
            r3.b(r0)     // Catch:{ all -> 0x0139 }
            android.content.Context r3 = r1.f38a     // Catch:{ all -> 0x0139 }
            com.xiaomi.mipush.sdk.ay r3 = com.xiaomi.mipush.sdk.ay.a((android.content.Context) r3)     // Catch:{ all -> 0x0139 }
            r3.a((boolean) r6, (java.lang.String) r0)     // Catch:{ all -> 0x0139 }
            goto L_0x0136
        L_0x012d:
            android.content.Context r3 = r1.f38a     // Catch:{ all -> 0x0139 }
            com.xiaomi.mipush.sdk.ao r3 = com.xiaomi.mipush.sdk.ao.a((android.content.Context) r3)     // Catch:{ all -> 0x0139 }
            r3.c(r0)     // Catch:{ all -> 0x0139 }
        L_0x0136:
            monitor-exit(r2)     // Catch:{ all -> 0x0139 }
            goto L_0x0999
        L_0x0139:
            r0 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x0139 }
            throw r0
        L_0x013c:
            android.content.Context r2 = r1.f38a
            com.xiaomi.mipush.sdk.ao r2 = com.xiaomi.mipush.sdk.ao.a((android.content.Context) r2)
            r2.c(r0)
            goto L_0x0999
        L_0x0147:
            com.xiaomi.push.hq r2 = com.xiaomi.push.hq.EnablePushMessage
            java.lang.String r2 = r2.f485a
            java.lang.String r5 = r4.f570d
            boolean r2 = r2.equalsIgnoreCase(r5)
            if (r2 == 0) goto L_0x01e9
            long r4 = r4.f563a
            int r2 = (r4 > r11 ? 1 : (r4 == r11 ? 0 : -1))
            if (r2 != 0) goto L_0x0198
            java.lang.Class<com.xiaomi.mipush.sdk.ao> r2 = com.xiaomi.mipush.sdk.ao.class
            monitor-enter(r2)
            android.content.Context r3 = r1.f38a     // Catch:{ all -> 0x0195 }
            com.xiaomi.mipush.sdk.ao r3 = com.xiaomi.mipush.sdk.ao.a((android.content.Context) r3)     // Catch:{ all -> 0x0195 }
            boolean r3 = r3.a((java.lang.String) r0)     // Catch:{ all -> 0x0195 }
            if (r3 == 0) goto L_0x0192
            android.content.Context r3 = r1.f38a     // Catch:{ all -> 0x0195 }
            com.xiaomi.mipush.sdk.ao r3 = com.xiaomi.mipush.sdk.ao.a((android.content.Context) r3)     // Catch:{ all -> 0x0195 }
            r3.c(r0)     // Catch:{ all -> 0x0195 }
            java.lang.String r0 = "syncing"
            android.content.Context r3 = r1.f38a     // Catch:{ all -> 0x0195 }
            com.xiaomi.mipush.sdk.ao r3 = com.xiaomi.mipush.sdk.ao.a((android.content.Context) r3)     // Catch:{ all -> 0x0195 }
            com.xiaomi.mipush.sdk.bd r4 = com.xiaomi.mipush.sdk.bd.ENABLE_PUSH     // Catch:{ all -> 0x0195 }
            java.lang.String r3 = r3.a((com.xiaomi.mipush.sdk.bd) r4)     // Catch:{ all -> 0x0195 }
            boolean r0 = r0.equals(r3)     // Catch:{ all -> 0x0195 }
            if (r0 == 0) goto L_0x0192
            android.content.Context r0 = r1.f38a     // Catch:{ all -> 0x0195 }
            com.xiaomi.mipush.sdk.ao r0 = com.xiaomi.mipush.sdk.ao.a((android.content.Context) r0)     // Catch:{ all -> 0x0195 }
            com.xiaomi.mipush.sdk.bd r3 = com.xiaomi.mipush.sdk.bd.ENABLE_PUSH     // Catch:{ all -> 0x0195 }
            java.lang.String r4 = "synced"
            r0.a(r3, r4)     // Catch:{ all -> 0x0195 }
        L_0x0192:
            monitor-exit(r2)     // Catch:{ all -> 0x0195 }
            goto L_0x0999
        L_0x0195:
            r0 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x0195 }
            throw r0
        L_0x0198:
            java.lang.String r2 = "syncing"
            android.content.Context r4 = r1.f38a
            com.xiaomi.mipush.sdk.ao r4 = com.xiaomi.mipush.sdk.ao.a((android.content.Context) r4)
            com.xiaomi.mipush.sdk.bd r5 = com.xiaomi.mipush.sdk.bd.ENABLE_PUSH
            java.lang.String r4 = r4.a((com.xiaomi.mipush.sdk.bd) r5)
            boolean r2 = r2.equals(r4)
            if (r2 == 0) goto L_0x013c
            java.lang.Class<com.xiaomi.mipush.sdk.ao> r2 = com.xiaomi.mipush.sdk.ao.class
            monitor-enter(r2)
            android.content.Context r4 = r1.f38a     // Catch:{ all -> 0x01e6 }
            com.xiaomi.mipush.sdk.ao r4 = com.xiaomi.mipush.sdk.ao.a((android.content.Context) r4)     // Catch:{ all -> 0x01e6 }
            boolean r4 = r4.a((java.lang.String) r0)     // Catch:{ all -> 0x01e6 }
            if (r4 == 0) goto L_0x01e3
            android.content.Context r4 = r1.f38a     // Catch:{ all -> 0x01e6 }
            com.xiaomi.mipush.sdk.ao r4 = com.xiaomi.mipush.sdk.ao.a((android.content.Context) r4)     // Catch:{ all -> 0x01e6 }
            int r4 = r4.a((java.lang.String) r0)     // Catch:{ all -> 0x01e6 }
            if (r4 >= r3) goto L_0x01da
            android.content.Context r3 = r1.f38a     // Catch:{ all -> 0x01e6 }
            com.xiaomi.mipush.sdk.ao r3 = com.xiaomi.mipush.sdk.ao.a((android.content.Context) r3)     // Catch:{ all -> 0x01e6 }
            r3.b(r0)     // Catch:{ all -> 0x01e6 }
            android.content.Context r3 = r1.f38a     // Catch:{ all -> 0x01e6 }
            com.xiaomi.mipush.sdk.ay r3 = com.xiaomi.mipush.sdk.ay.a((android.content.Context) r3)     // Catch:{ all -> 0x01e6 }
            r3.a((boolean) r7, (java.lang.String) r0)     // Catch:{ all -> 0x01e6 }
            goto L_0x01e3
        L_0x01da:
            android.content.Context r3 = r1.f38a     // Catch:{ all -> 0x01e6 }
            com.xiaomi.mipush.sdk.ao r3 = com.xiaomi.mipush.sdk.ao.a((android.content.Context) r3)     // Catch:{ all -> 0x01e6 }
            r3.c(r0)     // Catch:{ all -> 0x01e6 }
        L_0x01e3:
            monitor-exit(r2)     // Catch:{ all -> 0x01e6 }
            goto L_0x0999
        L_0x01e6:
            r0 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x01e6 }
            throw r0
        L_0x01e9:
            com.xiaomi.push.hq r0 = com.xiaomi.push.hq.ThirdPartyRegUpdate
            java.lang.String r0 = r0.f485a
            java.lang.String r2 = r4.f570d
            boolean r0 = r0.equalsIgnoreCase(r2)
            if (r0 == 0) goto L_0x01fa
            r1.b((com.xiaomi.push.hx) r4)
            goto L_0x0999
        L_0x01fa:
            com.xiaomi.push.hq r0 = com.xiaomi.push.hq.UploadTinyData
            java.lang.String r0 = r0.f485a
            java.lang.String r2 = r4.f570d
            boolean r0 = r0.equalsIgnoreCase(r2)
            if (r0 == 0) goto L_0x0999
            r1.a((com.xiaomi.push.hx) r4)
            goto L_0x0999
        L_0x020b:
            boolean r0 = r4 instanceof com.xiaomi.push.Cif
            if (r0 == 0) goto L_0x0999
            com.xiaomi.push.if r4 = (com.xiaomi.push.Cif) r4
            java.lang.String r0 = "registration id expired"
            java.lang.String r3 = r4.f630d
            boolean r0 = r0.equalsIgnoreCase(r3)
            if (r0 == 0) goto L_0x02a5
            android.content.Context r0 = r1.f38a
            java.util.List r0 = com.xiaomi.mipush.sdk.MiPushClient.getAllAlias(r0)
            android.content.Context r2 = r1.f38a
            java.util.List r2 = com.xiaomi.mipush.sdk.MiPushClient.getAllTopic(r2)
            android.content.Context r3 = r1.f38a
            java.util.List r3 = com.xiaomi.mipush.sdk.MiPushClient.getAllUserAccount(r3)
            android.content.Context r4 = r1.f38a
            java.lang.String r4 = com.xiaomi.mipush.sdk.MiPushClient.getAcceptTime(r4)
            android.content.Context r5 = r1.f38a
            com.xiaomi.push.hu r8 = com.xiaomi.push.hu.RegIdExpired
            com.xiaomi.mipush.sdk.MiPushClient.reInitialize(r5, r8)
            java.util.Iterator r0 = r0.iterator()
        L_0x023e:
            boolean r5 = r0.hasNext()
            if (r5 == 0) goto L_0x0255
            java.lang.Object r5 = r0.next()
            java.lang.String r5 = (java.lang.String) r5
            android.content.Context r8 = r1.f38a
            com.xiaomi.mipush.sdk.MiPushClient.removeAlias(r8, r5)
            android.content.Context r8 = r1.f38a
            com.xiaomi.mipush.sdk.MiPushClient.setAlias(r8, r5, r10)
            goto L_0x023e
        L_0x0255:
            java.util.Iterator r0 = r2.iterator()
        L_0x0259:
            boolean r2 = r0.hasNext()
            if (r2 == 0) goto L_0x0270
            java.lang.Object r2 = r0.next()
            java.lang.String r2 = (java.lang.String) r2
            android.content.Context r5 = r1.f38a
            com.xiaomi.mipush.sdk.MiPushClient.removeTopic(r5, r2)
            android.content.Context r5 = r1.f38a
            com.xiaomi.mipush.sdk.MiPushClient.subscribe(r5, r2, r10)
            goto L_0x0259
        L_0x0270:
            java.util.Iterator r0 = r3.iterator()
        L_0x0274:
            boolean r2 = r0.hasNext()
            if (r2 == 0) goto L_0x028b
            java.lang.Object r2 = r0.next()
            java.lang.String r2 = (java.lang.String) r2
            android.content.Context r3 = r1.f38a
            com.xiaomi.mipush.sdk.MiPushClient.removeAccount(r3, r2)
            android.content.Context r3 = r1.f38a
            com.xiaomi.mipush.sdk.MiPushClient.setUserAccount(r3, r2, r10)
            goto L_0x0274
        L_0x028b:
            java.lang.String r0 = ","
            java.lang.String[] r0 = r4.split(r0)
            int r2 = r0.length
            r3 = 2
            if (r2 != r3) goto L_0x0999
            android.content.Context r2 = r1.f38a
            com.xiaomi.mipush.sdk.MiPushClient.removeAcceptTime(r2)
            android.content.Context r2 = r1.f38a
            r3 = r0[r7]
            r0 = r0[r6]
            com.xiaomi.mipush.sdk.MiPushClient.addAcceptTime(r2, r3, r0)
            goto L_0x0999
        L_0x02a5:
            java.lang.String r0 = "client_info_update_ok"
            java.lang.String r3 = r4.f630d
            boolean r0 = r0.equalsIgnoreCase(r3)
            if (r0 == 0) goto L_0x02d8
            java.util.Map r0 = r4.a()
            if (r0 == 0) goto L_0x0999
            java.util.Map r0 = r4.a()
            java.lang.String r2 = "app_version"
            boolean r0 = r0.containsKey(r2)
            if (r0 == 0) goto L_0x0999
            java.util.Map r0 = r4.a()
            java.lang.String r2 = "app_version"
            java.lang.Object r0 = r0.get(r2)
            java.lang.String r0 = (java.lang.String) r0
            android.content.Context r2 = r1.f38a
            com.xiaomi.mipush.sdk.d r2 = com.xiaomi.mipush.sdk.d.a((android.content.Context) r2)
            r2.a((java.lang.String) r0)
            goto L_0x0999
        L_0x02d8:
            com.xiaomi.push.hq r0 = com.xiaomi.push.hq.AwakeApp
            java.lang.String r0 = r0.f485a
            java.lang.String r3 = r4.f630d
            boolean r0 = r0.equalsIgnoreCase(r3)
            if (r0 == 0) goto L_0x0329
            boolean r0 = r20.b()
            if (r0 == 0) goto L_0x0999
            java.util.Map r0 = r4.a()
            if (r0 == 0) goto L_0x0999
            java.util.Map r0 = r4.a()
            java.lang.String r2 = "awake_info"
            boolean r0 = r0.containsKey(r2)
            if (r0 == 0) goto L_0x0999
            java.util.Map r0 = r4.a()
            java.lang.String r2 = "awake_info"
            java.lang.Object r0 = r0.get(r2)
            java.lang.String r0 = (java.lang.String) r0
            android.content.Context r2 = r1.f38a
            android.content.Context r3 = r1.f38a
            com.xiaomi.mipush.sdk.d r3 = com.xiaomi.mipush.sdk.d.a((android.content.Context) r3)
            java.lang.String r3 = r3.a()
            android.content.Context r4 = r1.f38a
            com.xiaomi.push.service.ag r4 = com.xiaomi.push.service.ag.a((android.content.Context) r4)
            com.xiaomi.push.hl r5 = com.xiaomi.push.hl.AwakeInfoUploadWaySwitch
            int r5 = r5.a()
            int r4 = r4.a((int) r5, (int) r7)
            com.xiaomi.mipush.sdk.p.a((android.content.Context) r2, (java.lang.String) r3, (int) r4, (java.lang.String) r0)
            goto L_0x0999
        L_0x0329:
            com.xiaomi.push.hq r0 = com.xiaomi.push.hq.NormalClientConfigUpdate
            java.lang.String r0 = r0.f485a
            java.lang.String r2 = r4.f630d
            boolean r0 = r0.equalsIgnoreCase(r2)
            if (r0 == 0) goto L_0x034c
            com.xiaomi.push.ie r0 = new com.xiaomi.push.ie
            r0.<init>()
            byte[] r2 = r4.a()     // Catch:{ iw -> 0x043a }
            com.xiaomi.push.iq.a(r0, (byte[]) r2)     // Catch:{ iw -> 0x043a }
            android.content.Context r2 = r1.f38a     // Catch:{ iw -> 0x043a }
            com.xiaomi.push.service.ag r2 = com.xiaomi.push.service.ag.a((android.content.Context) r2)     // Catch:{ iw -> 0x043a }
            com.xiaomi.push.service.ah.a((com.xiaomi.push.service.ag) r2, (com.xiaomi.push.ie) r0)     // Catch:{ iw -> 0x043a }
            goto L_0x0999
        L_0x034c:
            com.xiaomi.push.hq r0 = com.xiaomi.push.hq.CustomClientConfigUpdate
            java.lang.String r0 = r0.f485a
            java.lang.String r2 = r4.f630d
            boolean r0 = r0.equalsIgnoreCase(r2)
            if (r0 == 0) goto L_0x036f
            com.xiaomi.push.id r0 = new com.xiaomi.push.id
            r0.<init>()
            byte[] r2 = r4.a()     // Catch:{ iw -> 0x043a }
            com.xiaomi.push.iq.a(r0, (byte[]) r2)     // Catch:{ iw -> 0x043a }
            android.content.Context r2 = r1.f38a     // Catch:{ iw -> 0x043a }
            com.xiaomi.push.service.ag r2 = com.xiaomi.push.service.ag.a((android.content.Context) r2)     // Catch:{ iw -> 0x043a }
            com.xiaomi.push.service.ah.a((com.xiaomi.push.service.ag) r2, (com.xiaomi.push.id) r0)     // Catch:{ iw -> 0x043a }
            goto L_0x0999
        L_0x036f:
            com.xiaomi.push.hq r0 = com.xiaomi.push.hq.SyncInfoResult
            java.lang.String r0 = r0.f485a
            java.lang.String r2 = r4.f630d
            boolean r0 = r0.equalsIgnoreCase(r2)
            if (r0 == 0) goto L_0x0382
            android.content.Context r0 = r1.f38a
            com.xiaomi.mipush.sdk.be.a((android.content.Context) r0, (com.xiaomi.push.Cif) r4)
            goto L_0x0999
        L_0x0382:
            com.xiaomi.push.hq r0 = com.xiaomi.push.hq.ForceSync
            java.lang.String r0 = r0.f485a
            java.lang.String r2 = r4.f630d
            boolean r0 = r0.equalsIgnoreCase(r2)
            if (r0 == 0) goto L_0x039a
            java.lang.String r0 = "receive force sync notification"
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.String) r0)
            android.content.Context r0 = r1.f38a
            com.xiaomi.mipush.sdk.be.a((android.content.Context) r0, (boolean) r7)
            goto L_0x0999
        L_0x039a:
            com.xiaomi.push.hq r0 = com.xiaomi.push.hq.CancelPushMessage
            java.lang.String r0 = r0.f485a
            java.lang.String r2 = r4.f630d
            boolean r0 = r0.equals(r2)
            if (r0 == 0) goto L_0x041b
            java.util.Map r0 = r4.a()
            if (r0 == 0) goto L_0x0999
            java.util.Map r0 = r4.a()
            java.lang.String r2 = com.xiaomi.push.service.ap.J
            boolean r0 = r0.containsKey(r2)
            r2 = -2
            if (r0 == 0) goto L_0x03d6
            java.util.Map r0 = r4.a()
            java.lang.String r3 = com.xiaomi.push.service.ap.J
            java.lang.Object r0 = r0.get(r3)
            java.lang.String r0 = (java.lang.String) r0
            boolean r3 = android.text.TextUtils.isEmpty(r0)
            if (r3 != 0) goto L_0x03d6
            int r0 = java.lang.Integer.parseInt(r0)     // Catch:{ NumberFormatException -> 0x03d1 }
            r2 = r0
            goto L_0x03d6
        L_0x03d1:
            r0 = move-exception
            r3 = r0
            r3.printStackTrace()
        L_0x03d6:
            r0 = -1
            if (r2 < r0) goto L_0x03e0
            android.content.Context r0 = r1.f38a
            com.xiaomi.mipush.sdk.MiPushClient.clearNotification(r0, r2)
            goto L_0x0999
        L_0x03e0:
            java.lang.String r0 = ""
            java.lang.String r2 = ""
            java.util.Map r3 = r4.a()
            java.lang.String r5 = com.xiaomi.push.service.ap.H
            boolean r3 = r3.containsKey(r5)
            if (r3 == 0) goto L_0x03fc
            java.util.Map r0 = r4.a()
            java.lang.String r3 = com.xiaomi.push.service.ap.H
            java.lang.Object r0 = r0.get(r3)
            java.lang.String r0 = (java.lang.String) r0
        L_0x03fc:
            java.util.Map r3 = r4.a()
            java.lang.String r5 = com.xiaomi.push.service.ap.I
            boolean r3 = r3.containsKey(r5)
            if (r3 == 0) goto L_0x0414
            java.util.Map r2 = r4.a()
            java.lang.String r3 = com.xiaomi.push.service.ap.I
            java.lang.Object r2 = r2.get(r3)
            java.lang.String r2 = (java.lang.String) r2
        L_0x0414:
            android.content.Context r3 = r1.f38a
            com.xiaomi.mipush.sdk.MiPushClient.clearNotification(r3, r0, r2)
            goto L_0x0999
        L_0x041b:
            com.xiaomi.push.hq r0 = com.xiaomi.push.hq.HybridRegisterResult
            java.lang.String r0 = r0.f485a
            java.lang.String r2 = r4.f630d
            boolean r0 = r0.equals(r2)
            if (r0 == 0) goto L_0x0440
            com.xiaomi.push.ih r0 = new com.xiaomi.push.ih     // Catch:{ iw -> 0x043a }
            r0.<init>()     // Catch:{ iw -> 0x043a }
            byte[] r2 = r4.a()     // Catch:{ iw -> 0x043a }
            com.xiaomi.push.iq.a(r0, (byte[]) r2)     // Catch:{ iw -> 0x043a }
            android.content.Context r2 = r1.f38a     // Catch:{ iw -> 0x043a }
            com.xiaomi.mipush.sdk.MiPushClient4Hybrid.onReceiveRegisterResult(r2, r0)     // Catch:{ iw -> 0x043a }
            goto L_0x0999
        L_0x043a:
            r0 = move-exception
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.Throwable) r0)
            goto L_0x0999
        L_0x0440:
            com.xiaomi.push.hq r0 = com.xiaomi.push.hq.HybridUnregisterResult
            java.lang.String r0 = r0.f485a
            java.lang.String r2 = r4.f630d
            boolean r0 = r0.equals(r2)
            if (r0 == 0) goto L_0x045f
            com.xiaomi.push.in r0 = new com.xiaomi.push.in     // Catch:{ iw -> 0x043a }
            r0.<init>()     // Catch:{ iw -> 0x043a }
            byte[] r2 = r4.a()     // Catch:{ iw -> 0x043a }
            com.xiaomi.push.iq.a(r0, (byte[]) r2)     // Catch:{ iw -> 0x043a }
            android.content.Context r2 = r1.f38a     // Catch:{ iw -> 0x043a }
            com.xiaomi.mipush.sdk.MiPushClient4Hybrid.onReceiveUnregisterResult(r2, r0)     // Catch:{ iw -> 0x043a }
            goto L_0x0999
        L_0x045f:
            com.xiaomi.push.hq r0 = com.xiaomi.push.hq.PushLogUpload
            java.lang.String r0 = r0.f485a
            java.lang.String r2 = r4.f630d
            boolean r0 = r0.equals(r2)
            if (r0 == 0) goto L_0x0999
            java.util.Map r0 = r4.a()
            if (r0 == 0) goto L_0x0999
            java.util.Map r0 = r4.a()
            java.lang.String r2 = "packages"
            boolean r0 = r0.containsKey(r2)
            if (r0 == 0) goto L_0x0999
            java.util.Map r0 = r4.a()
            java.lang.String r2 = "packages"
            java.lang.Object r0 = r0.get(r2)
            java.lang.String r0 = (java.lang.String) r0
            java.lang.String r2 = ","
            java.lang.String[] r0 = r0.split(r2)
            android.content.Context r2 = r1.f38a
            java.lang.String r2 = r2.getPackageName()
            java.lang.String r3 = "com.xiaomi.xmsf"
            boolean r2 = android.text.TextUtils.equals(r2, r3)
            if (r2 == 0) goto L_0x04a9
            android.content.Context r2 = r1.f38a
            com.xiaomi.mipush.sdk.Logger.uploadLogFile(r2, r6)
            android.content.Context r2 = r1.f38a
            r1.a((android.content.Context) r2, (java.lang.String[]) r0)
            goto L_0x0999
        L_0x04a9:
            android.content.Context r0 = r1.f38a
            com.xiaomi.mipush.sdk.Logger.uploadLogFile(r0, r7)
            goto L_0x0999
        L_0x04b0:
            android.content.Context r0 = r1.f38a
            java.lang.String r0 = r0.getPackageName()
            android.content.Context r2 = r1.f38a
            com.xiaomi.push.hg r5 = com.xiaomi.push.hg.Command
            int r3 = r3.length
            com.xiaomi.push.da.a(r0, r2, r4, r5, r3)
            com.xiaomi.push.ib r4 = (com.xiaomi.push.ib) r4
            java.lang.String r13 = r4.a()
            java.util.List r0 = r4.a()
            long r2 = r4.f594a
            int r5 = (r2 > r11 ? 1 : (r2 == r11 ? 0 : -1))
            if (r5 != 0) goto L_0x05c3
            com.xiaomi.push.fa r2 = com.xiaomi.push.fa.COMMAND_SET_ACCEPT_TIME
            java.lang.String r2 = r2.f328a
            boolean r2 = android.text.TextUtils.equals(r13, r2)
            if (r2 == 0) goto L_0x052c
            if (r0 == 0) goto L_0x052c
            int r2 = r0.size()
            if (r2 <= r6) goto L_0x052c
            android.content.Context r2 = r1.f38a
            java.lang.Object r3 = r0.get(r7)
            java.lang.String r3 = (java.lang.String) r3
            java.lang.Object r5 = r0.get(r6)
            java.lang.String r5 = (java.lang.String) r5
            com.xiaomi.mipush.sdk.MiPushClient.addAcceptTime(r2, r3, r5)
            java.lang.String r2 = "00:00"
            java.lang.Object r3 = r0.get(r7)
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0513
            java.lang.String r2 = "00:00"
            java.lang.Object r3 = r0.get(r6)
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0513
            android.content.Context r2 = r1.f38a
            com.xiaomi.mipush.sdk.d r2 = com.xiaomi.mipush.sdk.d.a((android.content.Context) r2)
            r2.a((boolean) r6)
            goto L_0x051c
        L_0x0513:
            android.content.Context r2 = r1.f38a
            com.xiaomi.mipush.sdk.d r2 = com.xiaomi.mipush.sdk.d.a((android.content.Context) r2)
            r2.a((boolean) r7)
        L_0x051c:
            java.lang.String r2 = "GMT+08"
            java.util.TimeZone r2 = java.util.TimeZone.getTimeZone(r2)
            java.util.TimeZone r3 = java.util.TimeZone.getDefault()
            java.util.List r0 = r1.a((java.util.TimeZone) r2, (java.util.TimeZone) r3, (java.util.List<java.lang.String>) r0)
            goto L_0x05c3
        L_0x052c:
            com.xiaomi.push.fa r2 = com.xiaomi.push.fa.COMMAND_SET_ALIAS
            java.lang.String r2 = r2.f328a
            boolean r2 = android.text.TextUtils.equals(r13, r2)
            if (r2 == 0) goto L_0x054b
            if (r0 == 0) goto L_0x054b
            int r2 = r0.size()
            if (r2 <= 0) goto L_0x054b
            android.content.Context r2 = r1.f38a
            java.lang.Object r3 = r0.get(r7)
            java.lang.String r3 = (java.lang.String) r3
            com.xiaomi.mipush.sdk.MiPushClient.addAlias(r2, r3)
            goto L_0x05c3
        L_0x054b:
            com.xiaomi.push.fa r2 = com.xiaomi.push.fa.COMMAND_UNSET_ALIAS
            java.lang.String r2 = r2.f328a
            boolean r2 = android.text.TextUtils.equals(r13, r2)
            if (r2 == 0) goto L_0x0569
            if (r0 == 0) goto L_0x0569
            int r2 = r0.size()
            if (r2 <= 0) goto L_0x0569
            android.content.Context r2 = r1.f38a
            java.lang.Object r3 = r0.get(r7)
            java.lang.String r3 = (java.lang.String) r3
            com.xiaomi.mipush.sdk.MiPushClient.removeAlias(r2, r3)
            goto L_0x05c3
        L_0x0569:
            com.xiaomi.push.fa r2 = com.xiaomi.push.fa.COMMAND_SET_ACCOUNT
            java.lang.String r2 = r2.f328a
            boolean r2 = android.text.TextUtils.equals(r13, r2)
            if (r2 == 0) goto L_0x0587
            if (r0 == 0) goto L_0x0587
            int r2 = r0.size()
            if (r2 <= 0) goto L_0x0587
            android.content.Context r2 = r1.f38a
            java.lang.Object r3 = r0.get(r7)
            java.lang.String r3 = (java.lang.String) r3
            com.xiaomi.mipush.sdk.MiPushClient.addAccount(r2, r3)
            goto L_0x05c3
        L_0x0587:
            com.xiaomi.push.fa r2 = com.xiaomi.push.fa.COMMAND_UNSET_ACCOUNT
            java.lang.String r2 = r2.f328a
            boolean r2 = android.text.TextUtils.equals(r13, r2)
            if (r2 == 0) goto L_0x05a5
            if (r0 == 0) goto L_0x05a5
            int r2 = r0.size()
            if (r2 <= 0) goto L_0x05a5
            android.content.Context r2 = r1.f38a
            java.lang.Object r3 = r0.get(r7)
            java.lang.String r3 = (java.lang.String) r3
            com.xiaomi.mipush.sdk.MiPushClient.removeAccount(r2, r3)
            goto L_0x05c3
        L_0x05a5:
            com.xiaomi.push.fa r2 = com.xiaomi.push.fa.COMMAND_CHK_VDEVID
            java.lang.String r2 = r2.f328a
            boolean r2 = android.text.TextUtils.equals(r13, r2)
            if (r2 == 0) goto L_0x05c3
            if (r0 == 0) goto L_0x05c2
            int r2 = r0.size()
            if (r2 <= 0) goto L_0x05c2
            android.content.Context r2 = r1.f38a
            java.lang.Object r0 = r0.get(r7)
            java.lang.String r0 = (java.lang.String) r0
            com.xiaomi.push.i.a((android.content.Context) r2, (java.lang.String) r0)
        L_0x05c2:
            return r10
        L_0x05c3:
            r14 = r0
            long r2 = r4.f594a
            java.lang.String r0 = r4.f602d
            java.lang.String r18 = r4.b()
            r15 = r2
            r17 = r0
            com.xiaomi.mipush.sdk.MiPushCommandMessage r0 = com.xiaomi.mipush.sdk.PushMessageHelper.generateCommandMessage(r13, r14, r15, r17, r18)
            return r0
        L_0x05d4:
            com.xiaomi.push.ip r4 = (com.xiaomi.push.ip) r4
            long r2 = r4.f764a
            int r0 = (r2 > r11 ? 1 : (r2 == r11 ? 0 : -1))
            if (r0 != 0) goto L_0x05e5
            android.content.Context r0 = r1.f38a
            java.lang.String r2 = r4.a()
            com.xiaomi.mipush.sdk.MiPushClient.removeTopic(r0, r2)
        L_0x05e5:
            java.lang.String r0 = r4.a()
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            if (r0 != 0) goto L_0x05fb
            java.util.ArrayList r10 = new java.util.ArrayList
            r10.<init>()
            java.lang.String r0 = r4.a()
            r10.add(r0)
        L_0x05fb:
            r12 = r10
            com.xiaomi.push.fa r0 = com.xiaomi.push.fa.COMMAND_UNSUBSCRIBE_TOPIC
            java.lang.String r11 = r0.f328a
            long r13 = r4.f764a
            java.lang.String r15 = r4.f770d
            java.lang.String r16 = r4.b()
            com.xiaomi.mipush.sdk.MiPushCommandMessage r0 = com.xiaomi.mipush.sdk.PushMessageHelper.generateCommandMessage(r11, r12, r13, r15, r16)
            return r0
        L_0x060d:
            com.xiaomi.push.il r4 = (com.xiaomi.push.il) r4
            long r2 = r4.f719a
            int r0 = (r2 > r11 ? 1 : (r2 == r11 ? 0 : -1))
            if (r0 != 0) goto L_0x061e
            android.content.Context r0 = r1.f38a
            java.lang.String r2 = r4.a()
            com.xiaomi.mipush.sdk.MiPushClient.addTopic(r0, r2)
        L_0x061e:
            java.lang.String r0 = r4.a()
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            if (r0 != 0) goto L_0x0634
            java.util.ArrayList r10 = new java.util.ArrayList
            r10.<init>()
            java.lang.String r0 = r4.a()
            r10.add(r0)
        L_0x0634:
            r12 = r10
            com.xiaomi.push.fa r0 = com.xiaomi.push.fa.COMMAND_SUBSCRIBE_TOPIC
            java.lang.String r11 = r0.f328a
            long r13 = r4.f719a
            java.lang.String r15 = r4.f725d
            java.lang.String r16 = r4.b()
            com.xiaomi.mipush.sdk.MiPushCommandMessage r0 = com.xiaomi.mipush.sdk.PushMessageHelper.generateCommandMessage(r11, r12, r13, r15, r16)
            return r0
        L_0x0646:
            com.xiaomi.push.in r4 = (com.xiaomi.push.in) r4
            long r2 = r4.f744a
            int r0 = (r2 > r11 ? 1 : (r2 == r11 ? 0 : -1))
            if (r0 != 0) goto L_0x065c
            android.content.Context r0 = r1.f38a
            com.xiaomi.mipush.sdk.d r0 = com.xiaomi.mipush.sdk.d.a((android.content.Context) r0)
            r0.a()
            android.content.Context r0 = r1.f38a
            com.xiaomi.mipush.sdk.MiPushClient.clearExtras(r0)
        L_0x065c:
            com.xiaomi.mipush.sdk.PushMessageHandler.a()
            goto L_0x0999
        L_0x0661:
            r0 = r4
            com.xiaomi.push.ih r0 = (com.xiaomi.push.ih) r0
            android.content.Context r2 = r1.f38a
            com.xiaomi.mipush.sdk.d r2 = com.xiaomi.mipush.sdk.d.a((android.content.Context) r2)
            java.lang.String r2 = r2.f56a
            boolean r3 = android.text.TextUtils.isEmpty(r2)
            if (r3 != 0) goto L_0x06f4
            java.lang.String r3 = r0.a()
            boolean r2 = android.text.TextUtils.equals(r2, r3)
            if (r2 != 0) goto L_0x067e
            goto L_0x06f4
        L_0x067e:
            android.content.Context r2 = r1.f38a
            com.xiaomi.mipush.sdk.d r2 = com.xiaomi.mipush.sdk.d.a((android.content.Context) r2)
            r2.f56a = r10
            long r2 = r0.f668a
            int r4 = (r2 > r11 ? 1 : (r2 == r11 ? 0 : -1))
            if (r4 != 0) goto L_0x06b5
            android.content.Context r2 = r1.f38a
            com.xiaomi.mipush.sdk.d r2 = com.xiaomi.mipush.sdk.d.a((android.content.Context) r2)
            java.lang.String r3 = r0.f678e
            java.lang.String r4 = r0.f679f
            java.lang.String r5 = r0.f685l
            r2.b(r3, r4, r5)
            android.content.Context r2 = r1.f38a
            com.xiaomi.push.ev r2 = com.xiaomi.push.ev.a((android.content.Context) r2)
            android.content.Context r3 = r1.f38a
            java.lang.String r3 = r3.getPackageName()
            java.lang.String r4 = com.xiaomi.push.eu.a((int) r24)
            r6 = 6006(0x1776, float:8.416E-42)
            java.lang.String r7 = "receive register result success"
        L_0x06af:
            r5 = r23
            r2.a(r3, r4, r5, r6, r7)
            goto L_0x06ca
        L_0x06b5:
            android.content.Context r2 = r1.f38a
            com.xiaomi.push.ev r2 = com.xiaomi.push.ev.a((android.content.Context) r2)
            android.content.Context r3 = r1.f38a
            java.lang.String r3 = r3.getPackageName()
            java.lang.String r4 = com.xiaomi.push.eu.a((int) r24)
            r6 = 6006(0x1776, float:8.416E-42)
            java.lang.String r7 = "receive register result fail"
            goto L_0x06af
        L_0x06ca:
            java.lang.String r2 = r0.f678e
            boolean r2 = android.text.TextUtils.isEmpty(r2)
            if (r2 != 0) goto L_0x06dc
            java.util.ArrayList r10 = new java.util.ArrayList
            r10.<init>()
            java.lang.String r2 = r0.f678e
            r10.add(r2)
        L_0x06dc:
            r4 = r10
            com.xiaomi.push.fa r2 = com.xiaomi.push.fa.COMMAND_REGISTER
            java.lang.String r3 = r2.f328a
            long r5 = r0.f668a
            java.lang.String r7 = r0.f677d
            r8 = 0
            com.xiaomi.mipush.sdk.MiPushCommandMessage r0 = com.xiaomi.mipush.sdk.PushMessageHelper.generateCommandMessage(r3, r4, r5, r7, r8)
            android.content.Context r2 = r1.f38a
            com.xiaomi.mipush.sdk.ay r2 = com.xiaomi.mipush.sdk.ay.a((android.content.Context) r2)
            r2.d()
            return r0
        L_0x06f4:
            java.lang.String r0 = "bad Registration result:"
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.String) r0)
            android.content.Context r0 = r1.f38a
            com.xiaomi.push.ev r0 = com.xiaomi.push.ev.a((android.content.Context) r0)
            android.content.Context r2 = r1.f38a
            java.lang.String r2 = r2.getPackageName()
            java.lang.String r3 = com.xiaomi.push.eu.a((int) r24)
            java.lang.String r4 = "bad Registration result:"
            r0.b(r2, r3, r8, r4)
            return r10
        L_0x070f:
            android.content.Context r5 = r1.f38a
            com.xiaomi.mipush.sdk.d r5 = com.xiaomi.mipush.sdk.d.a((android.content.Context) r5)
            boolean r5 = r5.d()
            if (r5 == 0) goto L_0x0738
            if (r0 != 0) goto L_0x0738
            java.lang.String r0 = "receive a message in pause state. drop it"
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.String) r0)
            android.content.Context r0 = r1.f38a
            com.xiaomi.push.ev r0 = com.xiaomi.push.ev.a((android.content.Context) r0)
            android.content.Context r2 = r1.f38a
            java.lang.String r2 = r2.getPackageName()
            java.lang.String r3 = com.xiaomi.push.eu.a((int) r24)
            java.lang.String r4 = "receive a message in pause state. drop it"
            r0.a((java.lang.String) r2, (java.lang.String) r3, (java.lang.String) r8, (java.lang.String) r4)
            return r10
        L_0x0738:
            com.xiaomi.push.ij r4 = (com.xiaomi.push.ij) r4
            com.xiaomi.push.hs r5 = r4.a()
            if (r5 != 0) goto L_0x075b
            java.lang.String r0 = "receive an empty message without push content, drop it"
            com.xiaomi.channel.commonutils.logger.b.d(r0)
            android.content.Context r0 = r1.f38a
            com.xiaomi.push.ev r0 = com.xiaomi.push.ev.a((android.content.Context) r0)
            android.content.Context r2 = r1.f38a
            java.lang.String r2 = r2.getPackageName()
            java.lang.String r3 = com.xiaomi.push.eu.a((int) r24)
            java.lang.String r4 = "receive an empty message without push content, drop it"
            r0.b(r2, r3, r8, r4)
            return r10
        L_0x075b:
            if (r0 == 0) goto L_0x0788
            boolean r6 = com.xiaomi.push.service.z.a((com.xiaomi.push.ic) r20)
            if (r6 == 0) goto L_0x0777
            android.content.Context r6 = r1.f38a
            java.lang.String r7 = r5.a()
            com.xiaomi.push.ht r13 = r20.a()
            java.lang.String r14 = r2.f613b
            java.lang.String r15 = r5.b()
            com.xiaomi.mipush.sdk.MiPushClient.reportIgnoreRegMessageClicked(r6, r7, r13, r14, r15)
            goto L_0x0788
        L_0x0777:
            android.content.Context r6 = r1.f38a
            java.lang.String r7 = r5.a()
            com.xiaomi.push.ht r13 = r20.a()
            java.lang.String r14 = r5.b()
            com.xiaomi.mipush.sdk.MiPushClient.reportMessageClicked(r6, r7, r13, r14)
        L_0x0788:
            if (r0 != 0) goto L_0x07cd
            java.lang.String r6 = r4.d()
            boolean r6 = android.text.TextUtils.isEmpty(r6)
            if (r6 != 0) goto L_0x07ac
            android.content.Context r6 = r1.f38a
            java.lang.String r7 = r4.d()
            long r6 = com.xiaomi.mipush.sdk.MiPushClient.aliasSetTime(r6, r7)
            int r13 = (r6 > r11 ? 1 : (r6 == r11 ? 0 : -1))
            if (r13 >= 0) goto L_0x07ac
            android.content.Context r6 = r1.f38a
            java.lang.String r7 = r4.d()
            com.xiaomi.mipush.sdk.MiPushClient.addAlias(r6, r7)
            goto L_0x07cd
        L_0x07ac:
            java.lang.String r6 = r4.c()
            boolean r6 = android.text.TextUtils.isEmpty(r6)
            if (r6 != 0) goto L_0x07cd
            android.content.Context r6 = r1.f38a
            java.lang.String r7 = r4.c()
            long r6 = com.xiaomi.mipush.sdk.MiPushClient.topicSubscribedTime(r6, r7)
            int r13 = (r6 > r11 ? 1 : (r6 == r11 ? 0 : -1))
            if (r13 >= 0) goto L_0x07cd
            android.content.Context r6 = r1.f38a
            java.lang.String r7 = r4.c()
            com.xiaomi.mipush.sdk.MiPushClient.addTopic(r6, r7)
        L_0x07cd:
            com.xiaomi.push.ht r6 = r2.f607a
            if (r6 == 0) goto L_0x07e6
            com.xiaomi.push.ht r6 = r2.f607a
            java.util.Map r6 = r6.a()
            if (r6 == 0) goto L_0x07e6
            com.xiaomi.push.ht r6 = r2.f607a
            java.util.Map<java.lang.String, java.lang.String> r6 = r6.f520a
            java.lang.String r7 = "jobkey"
            java.lang.Object r6 = r6.get(r7)
            java.lang.String r6 = (java.lang.String) r6
            goto L_0x07e7
        L_0x07e6:
            r6 = r10
        L_0x07e7:
            boolean r7 = android.text.TextUtils.isEmpty(r6)
            if (r7 == 0) goto L_0x07f1
            java.lang.String r6 = r5.a()
        L_0x07f1:
            if (r0 != 0) goto L_0x0835
            android.content.Context r7 = r1.f38a
            boolean r7 = a((android.content.Context) r7, (java.lang.String) r6)
            if (r7 == 0) goto L_0x0835
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r5 = "drop a duplicate message, key="
            r3.append(r5)
            r3.append(r6)
            java.lang.String r3 = r3.toString()
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.String) r3)
            android.content.Context r3 = r1.f38a
            com.xiaomi.push.ev r3 = com.xiaomi.push.ev.a((android.content.Context) r3)
            android.content.Context r5 = r1.f38a
            java.lang.String r5 = r5.getPackageName()
            java.lang.String r7 = com.xiaomi.push.eu.a((int) r24)
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            java.lang.String r11 = "drop a duplicate message, key="
            r9.append(r11)
            r9.append(r6)
            java.lang.String r6 = r9.toString()
            r3.c(r5, r7, r8, r6)
            goto L_0x098e
        L_0x0835:
            com.xiaomi.push.ht r7 = r20.a()
            com.xiaomi.mipush.sdk.MiPushMessage r7 = com.xiaomi.mipush.sdk.PushMessageHelper.generateMessage(r4, r7, r0)
            int r11 = r7.getPassThrough()
            if (r11 != 0) goto L_0x0855
            if (r0 != 0) goto L_0x0855
            java.util.Map r11 = r7.getExtra()
            boolean r11 = com.xiaomi.push.service.z.a((java.util.Map<java.lang.String, java.lang.String>) r11)
            if (r11 == 0) goto L_0x0855
            android.content.Context r0 = r1.f38a
            com.xiaomi.push.service.z.a((android.content.Context) r0, (com.xiaomi.push.ic) r2, (byte[]) r3)
            return r10
        L_0x0855:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r11 = "receive a message, msgid="
            r3.append(r11)
            java.lang.String r11 = r5.a()
            r3.append(r11)
            java.lang.String r11 = ", jobkey="
            r3.append(r11)
            r3.append(r6)
            java.lang.String r3 = r3.toString()
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.String) r3)
            if (r0 == 0) goto L_0x098d
            java.util.Map r3 = r7.getExtra()
            if (r3 == 0) goto L_0x098d
            java.util.Map r3 = r7.getExtra()
            java.lang.String r6 = "notify_effect"
            boolean r3 = r3.containsKey(r6)
            if (r3 == 0) goto L_0x098d
            java.util.Map r0 = r7.getExtra()
            java.lang.String r3 = "notify_effect"
            java.lang.Object r3 = r0.get(r3)
            r11 = r3
            java.lang.String r11 = (java.lang.String) r11
            boolean r3 = com.xiaomi.push.service.z.a((com.xiaomi.push.ic) r20)
            if (r3 == 0) goto L_0x0909
            android.content.Context r3 = r1.f38a
            java.lang.String r2 = r2.f613b
            android.content.Intent r0 = a((android.content.Context) r3, (java.lang.String) r2, (java.util.Map<java.lang.String, java.lang.String>) r0)
            java.lang.String r2 = "eventMessageType"
            r0.putExtra(r2, r9)
            java.lang.String r2 = "messageId"
            r0.putExtra(r2, r8)
            if (r0 != 0) goto L_0x08cb
            java.lang.String r0 = "Getting Intent fail from ignore reg message. "
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.String) r0)
            android.content.Context r0 = r1.f38a
            com.xiaomi.push.ev r0 = com.xiaomi.push.ev.a((android.content.Context) r0)
            android.content.Context r2 = r1.f38a
            java.lang.String r2 = r2.getPackageName()
            java.lang.String r3 = com.xiaomi.push.eu.a((int) r24)
            java.lang.String r4 = "Getting Intent fail from ignore reg message."
            r0.b(r2, r3, r8, r4)
            return r10
        L_0x08cb:
            java.lang.String r2 = r5.c()
            boolean r3 = android.text.TextUtils.isEmpty(r2)
            if (r3 != 0) goto L_0x08da
            java.lang.String r3 = "payload"
            r0.putExtra(r3, r2)
        L_0x08da:
            android.content.Context r2 = r1.f38a
            r2.startActivity(r0)
            android.content.Context r0 = r1.f38a
            com.xiaomi.push.ev r2 = com.xiaomi.push.ev.a((android.content.Context) r0)
            android.content.Context r0 = r1.f38a
            java.lang.String r3 = r0.getPackageName()
            java.lang.String r4 = com.xiaomi.push.eu.a((int) r24)
            r6 = 3006(0xbbe, float:4.212E-42)
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r5 = "business message is clicked typeId "
            r0.append(r5)
            r0.append(r11)
            java.lang.String r7 = r0.toString()
            r5 = r23
            r2.a(r3, r4, r5, r6, r7)
            goto L_0x098c
        L_0x0909:
            android.content.Context r2 = r1.f38a
            android.content.Context r3 = r1.f38a
            java.lang.String r3 = r3.getPackageName()
            android.content.Intent r0 = a((android.content.Context) r2, (java.lang.String) r3, (java.util.Map<java.lang.String, java.lang.String>) r0)
            if (r0 == 0) goto L_0x098c
            java.lang.String r2 = com.xiaomi.push.service.ap.c
            boolean r2 = r11.equals(r2)
            if (r2 != 0) goto L_0x092e
            java.lang.String r2 = "key_message"
            r0.putExtra(r2, r7)
            java.lang.String r2 = "eventMessageType"
            r0.putExtra(r2, r9)
            java.lang.String r2 = "messageId"
            r0.putExtra(r2, r8)
        L_0x092e:
            android.content.Context r2 = r1.f38a
            r2.startActivity(r0)
            java.lang.String r0 = "start activity succ"
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.String) r0)
            android.content.Context r0 = r1.f38a
            com.xiaomi.push.ev r2 = com.xiaomi.push.ev.a((android.content.Context) r0)
            android.content.Context r0 = r1.f38a
            java.lang.String r3 = r0.getPackageName()
            java.lang.String r4 = com.xiaomi.push.eu.a((int) r24)
            r6 = 1006(0x3ee, float:1.41E-42)
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r5 = "notification message is clicked typeId "
            r0.append(r5)
            r0.append(r11)
            java.lang.String r7 = r0.toString()
            r5 = r23
            r2.a(r3, r4, r5, r6, r7)
            java.lang.String r0 = com.xiaomi.push.service.ap.c
            boolean r0 = r11.equals(r0)
            if (r0 == 0) goto L_0x098c
            android.content.Context r0 = r1.f38a
            com.xiaomi.push.ev r0 = com.xiaomi.push.ev.a((android.content.Context) r0)
            android.content.Context r2 = r1.f38a
            java.lang.String r2 = r2.getPackageName()
            java.lang.String r3 = com.xiaomi.push.eu.a((int) r24)
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "try open web page typeId "
            r4.append(r5)
            r4.append(r11)
            java.lang.String r4 = r4.toString()
            r0.a((java.lang.String) r2, (java.lang.String) r3, (java.lang.String) r8, (java.lang.String) r4)
        L_0x098c:
            return r10
        L_0x098d:
            r10 = r7
        L_0x098e:
            com.xiaomi.push.ht r3 = r20.a()
            if (r3 != 0) goto L_0x0999
            if (r0 != 0) goto L_0x0999
            r1.a((com.xiaomi.push.ij) r4, (com.xiaomi.push.ic) r2)
        L_0x0999:
            return r10
        L_0x099a:
            r0 = move-exception
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.Throwable) r0)
            java.lang.String r2 = "receive a message which action string is not valid. is the reg expired?"
            com.xiaomi.channel.commonutils.logger.b.d(r2)
            android.content.Context r2 = r1.f38a
            com.xiaomi.push.ev r2 = com.xiaomi.push.ev.a((android.content.Context) r2)
            android.content.Context r3 = r1.f38a
            java.lang.String r3 = r3.getPackageName()
            java.lang.String r4 = com.xiaomi.push.eu.a((int) r24)
            r2.a((java.lang.String) r3, (java.lang.String) r4, (java.lang.String) r8, (java.lang.Throwable) r0)
            return r10
        L_0x09b7:
            r0 = move-exception
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.Throwable) r0)
            r19.a((com.xiaomi.push.ic) r20)
            android.content.Context r2 = r1.f38a
            com.xiaomi.push.ev r2 = com.xiaomi.push.ev.a((android.content.Context) r2)
            android.content.Context r3 = r1.f38a
            java.lang.String r3 = r3.getPackageName()
            java.lang.String r4 = com.xiaomi.push.eu.a((int) r24)
            r2.a((java.lang.String) r3, (java.lang.String) r4, (java.lang.String) r8, (java.lang.Throwable) r0)
            return r10
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.mipush.sdk.av.a(com.xiaomi.push.ic, boolean, byte[], java.lang.String, int):com.xiaomi.mipush.sdk.PushMessageHandler$a");
    }

    private PushMessageHandler.a a(ic icVar, byte[] bArr) {
        String str;
        String str2 = null;
        try {
            ir a2 = ar.a(this.f38a, icVar);
            if (a2 == null) {
                b.d("message arrived: receiving an un-recognized message. " + icVar.f606a);
                return null;
            }
            hg a3 = icVar.a();
            b.a("message arrived: processing an arrived message, action=" + a3);
            if (ax.a[a3.ordinal()] != 1) {
                return null;
            }
            ij ijVar = (ij) a2;
            hs a4 = ijVar.a();
            if (a4 == null) {
                str = "message arrived: receive an empty message without push content, drop it";
                b.d(str);
                return null;
            }
            if (!(icVar.f607a == null || icVar.f607a.a() == null)) {
                str2 = icVar.f607a.f520a.get("jobkey");
            }
            MiPushMessage generateMessage = PushMessageHelper.generateMessage(ijVar, icVar.a(), false);
            generateMessage.setArrivedMessage(true);
            b.a("message arrived: receive a message, msgid=" + a4.a() + ", jobkey=" + str2);
            return generateMessage;
        } catch (v e) {
            b.a((Throwable) e);
            str = "message arrived: receive a message but decrypt failed. report when click.";
        } catch (iw e2) {
            b.a((Throwable) e2);
            str = "message arrived: receive a message which action string is not valid. is the reg expired?";
        }
    }

    public static av a(Context context) {
        if (a == null) {
            a = new av(context);
        }
        return a;
    }

    private void a() {
        SharedPreferences sharedPreferences = this.f38a.getSharedPreferences("mipush_extra", 0);
        long currentTimeMillis = System.currentTimeMillis();
        if (Math.abs(currentTimeMillis - sharedPreferences.getLong(Constants.SP_KEY_LAST_REINITIALIZE, 0)) > ConfigStorage.DEFAULT_SMALL_MAX_AGE) {
            MiPushClient.reInitialize(this.f38a, hu.PackageUnregistered);
            sharedPreferences.edit().putLong(Constants.SP_KEY_LAST_REINITIALIZE, currentTimeMillis).commit();
        }
    }

    /* access modifiers changed from: private */
    public void a(Context context, PackageInfo packageInfo) {
        ServiceInfo[] serviceInfoArr = packageInfo.services;
        if (serviceInfoArr != null) {
            int length = serviceInfoArr.length;
            int i = 0;
            while (i < length) {
                ServiceInfo serviceInfo = serviceInfoArr[i];
                if (!serviceInfo.exported || !serviceInfo.enabled || !"com.xiaomi.mipush.sdk.PushMessageHandler".equals(serviceInfo.name) || context.getPackageName().equals(serviceInfo.packageName)) {
                    i++;
                } else {
                    try {
                        Intent intent = new Intent();
                        intent.setClassName(serviceInfo.packageName, serviceInfo.name);
                        intent.setAction("com.xiaomi.mipush.sdk.SYNC_LOG");
                        PushMessageHandler.a(context, intent);
                        return;
                    } catch (Throwable unused) {
                        return;
                    }
                }
            }
        }
    }

    public static void a(Context context, String str) {
        synchronized (f36a) {
            f37a.remove(str);
            d.a(context);
            SharedPreferences a2 = d.a(context);
            String a3 = ay.a((Collection<?>) f37a, ",");
            SharedPreferences.Editor edit = a2.edit();
            edit.putString("pref_msg_ids", a3);
            r.a(edit);
        }
    }

    private void a(Context context, String[] strArr) {
        ai.a(context).a((Runnable) new aw(this, strArr, context));
    }

    private void a(hx hxVar) {
        String a2 = hxVar.a();
        b.b("receive ack " + a2);
        Map a3 = hxVar.a();
        if (a3 != null) {
            String str = (String) a3.get("real_source");
            if (!TextUtils.isEmpty(str)) {
                b.b("receive ack : messageId = " + a2 + "  realSource = " + str);
                bk.a(this.f38a).a(a2, str, Boolean.valueOf(hxVar.f563a == 0));
            }
        }
    }

    private void a(ic icVar) {
        b.a("receive a message but decrypt failed. report now.");
        Cif ifVar = new Cif(icVar.a().f518a, false);
        ifVar.c(hq.DecryptMessageFail.f485a);
        ifVar.b(icVar.a());
        ifVar.d(icVar.f613b);
        ifVar.f625a = new HashMap();
        ifVar.f625a.put("regid", MiPushClient.getRegId(this.f38a));
        ay.a(this.f38a).a(ifVar, hg.Notification, false, (ht) null);
    }

    private void a(ij ijVar, ic icVar) {
        ht a2 = icVar.a();
        hw hwVar = new hw();
        hwVar.b(ijVar.b());
        hwVar.a(ijVar.a());
        hwVar.a(ijVar.a().a());
        if (!TextUtils.isEmpty(ijVar.c())) {
            hwVar.c(ijVar.c());
        }
        if (!TextUtils.isEmpty(ijVar.d())) {
            hwVar.d(ijVar.d());
        }
        hwVar.a(iq.a(this.f38a, icVar));
        ay.a(this.f38a).a(hwVar, hg.AckMessage, a2);
    }

    private void a(String str, long j, f fVar) {
        bd a2 = m.a(fVar);
        if (a2 != null) {
            if (j == 0) {
                synchronized (ao.class) {
                    if (ao.a(this.f38a).a(str)) {
                        ao.a(this.f38a).c(str);
                        if ("syncing".equals(ao.a(this.f38a).a(a2))) {
                            ao.a(this.f38a).a(a2, "synced");
                        }
                    }
                }
            } else if ("syncing".equals(ao.a(this.f38a).a(a2))) {
                synchronized (ao.class) {
                    if (ao.a(this.f38a).a(str)) {
                        if (ao.a(this.f38a).a(str) < 10) {
                            ao.a(this.f38a).b(str);
                            ay.a(this.f38a).a(str, a2, fVar);
                        } else {
                            ao.a(this.f38a).c(str);
                        }
                    }
                }
            } else {
                ao.a(this.f38a).c(str);
            }
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    private static boolean m34a(Context context, String str) {
        synchronized (f36a) {
            d.a(context);
            SharedPreferences a2 = d.a(context);
            if (f37a == null) {
                String[] split = a2.getString("pref_msg_ids", "").split(",");
                f37a = new LinkedList();
                for (String add : split) {
                    f37a.add(add);
                }
            }
            if (f37a.contains(str)) {
                return true;
            }
            f37a.add(str);
            if (f37a.size() > 25) {
                f37a.poll();
            }
            String a3 = ay.a((Collection<?>) f37a, ",");
            SharedPreferences.Editor edit = a2.edit();
            edit.putString("pref_msg_ids", a3);
            r.a(edit);
            return false;
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    private boolean m35a(ic icVar) {
        if (!TextUtils.equals(Constants.HYBRID_PACKAGE_NAME, icVar.b()) && !TextUtils.equals(Constants.HYBRID_DEBUG_PACKAGE_NAME, icVar.b())) {
            return false;
        }
        Map a2 = icVar.a() == null ? null : icVar.a().a();
        if (a2 == null) {
            return false;
        }
        String str = (String) a2.get(Constants.EXTRA_KEY_PUSH_SERVER_ACTION);
        return TextUtils.equals(str, Constants.EXTRA_VALUE_HYBRID_MESSAGE) || TextUtils.equals(str, Constants.EXTRA_VALUE_PLATFORM_MESSAGE);
    }

    private void b(hx hxVar) {
        long j;
        f fVar;
        b.c("ASSEMBLE_PUSH : " + hxVar.toString());
        String a2 = hxVar.a();
        Map a3 = hxVar.a();
        if (a3 != null) {
            String str = (String) a3.get(Constants.ASSEMBLE_PUSH_REG_INFO);
            if (!TextUtils.isEmpty(str)) {
                if (str.contains("brand:" + ap.FCM.name())) {
                    b.a("ASSEMBLE_PUSH : receive fcm token sync ack");
                    j.b(this.f38a, f.ASSEMBLE_PUSH_FCM, str);
                    j = hxVar.f563a;
                    fVar = f.ASSEMBLE_PUSH_FCM;
                } else {
                    if (str.contains("brand:" + ap.HUAWEI.name())) {
                        b.a("ASSEMBLE_PUSH : receive hw token sync ack");
                        j.b(this.f38a, f.ASSEMBLE_PUSH_HUAWEI, str);
                        j = hxVar.f563a;
                        fVar = f.ASSEMBLE_PUSH_HUAWEI;
                    } else {
                        if (str.contains("brand:" + ap.OPPO.name())) {
                            b.a("ASSEMBLE_PUSH : receive COS token sync ack");
                            j.b(this.f38a, f.ASSEMBLE_PUSH_COS, str);
                            j = hxVar.f563a;
                            fVar = f.ASSEMBLE_PUSH_COS;
                        } else {
                            if (str.contains("brand:" + ap.VIVO.name())) {
                                b.a("ASSEMBLE_PUSH : receive FTOS token sync ack");
                                j.b(this.f38a, f.ASSEMBLE_PUSH_FTOS, str);
                                j = hxVar.f563a;
                                fVar = f.ASSEMBLE_PUSH_FTOS;
                            } else {
                                return;
                            }
                        }
                    }
                }
                a(a2, j, fVar);
            }
        }
    }

    private void b(ic icVar) {
        ht a2 = icVar.a();
        hw hwVar = new hw();
        hwVar.b(icVar.a());
        hwVar.a(a2.a());
        hwVar.a(a2.a());
        if (!TextUtils.isEmpty(a2.b())) {
            hwVar.c(a2.b());
        }
        hwVar.a(iq.a(this.f38a, icVar));
        ay.a(this.f38a).a(hwVar, hg.AckMessage, false, icVar.a());
    }

    public PushMessageHandler.a a(Intent intent) {
        String str;
        String action = intent.getAction();
        b.a("receive an intent from server, action=" + action);
        String stringExtra = intent.getStringExtra("mrt");
        if (stringExtra == null) {
            stringExtra = Long.toString(System.currentTimeMillis());
        }
        String stringExtra2 = intent.getStringExtra("messageId");
        int intExtra = intent.getIntExtra("eventMessageType", -1);
        if ("com.xiaomi.mipush.RECEIVE_MESSAGE".equals(action)) {
            byte[] byteArrayExtra = intent.getByteArrayExtra("mipush_payload");
            boolean booleanExtra = intent.getBooleanExtra("mipush_notified", false);
            if (byteArrayExtra == null) {
                b.d("receiving an empty message, drop");
                ev.a(this.f38a).a(this.f38a.getPackageName(), intent, "receiving an empty message, drop");
                return null;
            }
            ic icVar = new ic();
            try {
                iq.a(icVar, byteArrayExtra);
                d a2 = d.a(this.f38a);
                ht a3 = icVar.a();
                if (icVar.a() == hg.SendMessage && a3 != null && !a2.d() && !booleanExtra) {
                    a3.a("mrt", stringExtra);
                    a3.a("mat", Long.toString(System.currentTimeMillis()));
                    if (!a(icVar)) {
                        b(icVar);
                    } else {
                        b.b("this is a mina's message, ack later");
                        a3.a(Constants.EXTRA_KEY_HYBRID_MESSAGE_TS, String.valueOf(a3.a()));
                        a3.a(Constants.EXTRA_KEY_HYBRID_DEVICE_STATUS, String.valueOf(iq.a(this.f38a, icVar)));
                    }
                }
                if (icVar.a() == hg.SendMessage && !icVar.b()) {
                    if (!z.a(icVar)) {
                        Object[] objArr = new Object[2];
                        objArr[0] = icVar.b();
                        objArr[1] = a3 != null ? a3.a() : "";
                        b.a(String.format("drop an un-encrypted messages. %1$s, %2$s", objArr));
                        ev a4 = ev.a(this.f38a);
                        String packageName = this.f38a.getPackageName();
                        Object[] objArr2 = new Object[2];
                        objArr2[0] = icVar.b();
                        objArr2[1] = a3 != null ? a3.a() : "";
                        a4.a(packageName, intent, String.format("drop an un-encrypted messages. %1$s, %2$s", objArr2));
                        return null;
                    } else if (!booleanExtra || a3.a() == null || !a3.a().containsKey("notify_effect")) {
                        b.a(String.format("drop an un-encrypted messages. %1$s, %2$s", new Object[]{icVar.b(), a3.a()}));
                        ev a5 = ev.a(this.f38a);
                        String packageName2 = this.f38a.getPackageName();
                        Object[] objArr3 = new Object[2];
                        objArr3[0] = icVar.b();
                        objArr3[1] = a3 != null ? a3.a() : "";
                        a5.a(packageName2, intent, String.format("drop an un-encrypted messages. %1$s, %2$s", objArr3));
                        return null;
                    }
                }
                if (a2.c() || icVar.f606a == hg.Registration) {
                    if (!a2.c() || !a2.e()) {
                        return a(icVar, booleanExtra, byteArrayExtra, stringExtra2, intExtra);
                    }
                    if (icVar.f606a == hg.UnRegistration) {
                        a2.a();
                        MiPushClient.clearExtras(this.f38a);
                        PushMessageHandler.a();
                    } else {
                        MiPushClient.unregisterPush(this.f38a);
                    }
                } else if (z.a(icVar)) {
                    return a(icVar, booleanExtra, byteArrayExtra, stringExtra2, intExtra);
                } else {
                    b.d("receive message without registration. need re-register!");
                    ev.a(this.f38a).a(this.f38a.getPackageName(), intent, "receive message without registration. need re-register!");
                    a();
                }
            } catch (iw | Exception e) {
                ev.a(this.f38a).a(this.f38a.getPackageName(), intent, e);
                b.a(e);
            }
        } else if ("com.xiaomi.mipush.ERROR".equals(action)) {
            MiPushCommandMessage miPushCommandMessage = new MiPushCommandMessage();
            ic icVar2 = new ic();
            try {
                byte[] byteArrayExtra2 = intent.getByteArrayExtra("mipush_payload");
                if (byteArrayExtra2 != null) {
                    iq.a(icVar2, byteArrayExtra2);
                }
            } catch (iw unused) {
            }
            miPushCommandMessage.setCommand(String.valueOf(icVar2.a()));
            miPushCommandMessage.setResultCode((long) intent.getIntExtra("mipush_error_code", 0));
            miPushCommandMessage.setReason(intent.getStringExtra("mipush_error_msg"));
            b.d("receive a error message. code = " + intent.getIntExtra("mipush_error_code", 0) + ", msg= " + intent.getStringExtra("mipush_error_msg"));
            return miPushCommandMessage;
        } else if ("com.xiaomi.mipush.MESSAGE_ARRIVED".equals(action)) {
            byte[] byteArrayExtra3 = intent.getByteArrayExtra("mipush_payload");
            if (byteArrayExtra3 == null) {
                b.d("message arrived: receiving an empty message, drop");
                return null;
            }
            ic icVar3 = new ic();
            try {
                iq.a(icVar3, byteArrayExtra3);
                d a6 = d.a(this.f38a);
                if (z.a(icVar3)) {
                    str = "message arrived: receive ignore reg message, ignore!";
                } else if (!a6.c()) {
                    str = "message arrived: receive message without registration. need unregister or re-register!";
                } else if (!a6.c() || !a6.e()) {
                    return a(icVar3, byteArrayExtra3);
                } else {
                    str = "message arrived: app info is invalidated";
                }
                b.d(str);
            } catch (iw | Exception e2) {
                b.a(e2);
            }
        }
        return null;
    }

    public List<String> a(TimeZone timeZone, TimeZone timeZone2, List<String> list) {
        List<String> list2 = list;
        if (timeZone.equals(timeZone2)) {
            return list2;
        }
        long rawOffset = (long) (((timeZone.getRawOffset() - timeZone2.getRawOffset()) / 1000) / 60);
        long parseLong = Long.parseLong(list2.get(0).split(":")[0]);
        long parseLong2 = Long.parseLong(list2.get(0).split(":")[1]);
        long j = ((((parseLong * 60) + parseLong2) - rawOffset) + 1440) % 1440;
        long parseLong3 = ((((Long.parseLong(list2.get(1).split(":")[0]) * 60) + Long.parseLong(list2.get(1).split(":")[1])) - rawOffset) + 1440) % 1440;
        ArrayList arrayList = new ArrayList();
        arrayList.add(String.format("%1$02d:%2$02d", new Object[]{Long.valueOf(j / 60), Long.valueOf(j % 60)}));
        arrayList.add(String.format("%1$02d:%2$02d", new Object[]{Long.valueOf(parseLong3 / 60), Long.valueOf(parseLong3 % 60)}));
        return arrayList;
    }
}
