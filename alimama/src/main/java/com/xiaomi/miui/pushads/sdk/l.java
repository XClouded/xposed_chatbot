package com.xiaomi.miui.pushads.sdk;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.push.cd;
import com.xiaomi.push.ce;
import com.xiaomi.push.cg;
import com.xiaomi.push.cj;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

public class l extends MiPushClient.MiPushClientCallback implements c {
    private static l a;

    /* renamed from: a  reason: collision with other field name */
    static final /* synthetic */ boolean f95a = (!l.class.desiredAssertionStatus());

    /* renamed from: a  reason: collision with other field name */
    private int f96a;

    /* renamed from: a  reason: collision with other field name */
    private Context f97a;

    /* renamed from: a  reason: collision with other field name */
    private SharedPreferences f98a;

    /* renamed from: a  reason: collision with other field name */
    private Handler f99a;

    /* renamed from: a  reason: collision with other field name */
    private e f100a;

    /* renamed from: a  reason: collision with other field name */
    private g f101a;

    /* renamed from: a  reason: collision with other field name */
    private cd f102a;

    /* renamed from: a  reason: collision with other field name */
    private cj f103a;

    /* renamed from: a  reason: collision with other field name */
    private String f104a;
    private int b;

    /* renamed from: b  reason: collision with other field name */
    private String f105b;
    private String c;

    private PendingIntent a(h hVar, int i) {
        PendingIntent a2;
        Intent intent = new Intent(this.f97a, MiPushRelayTraceService.class);
        Bundle bundle = new Bundle();
        bundle.putAll(hVar.a());
        bundle.putInt("intenttype", i);
        if (!(i != 2 || this.f100a == null || (a2 = this.f100a.a(new h(hVar))) == null)) {
            bundle.putParcelable("pendingintent", a2);
        }
        intent.putExtras(bundle);
        int i2 = (int) hVar.f173a;
        return PendingIntent.getService(this.f97a, (i2 * i2) + i, intent, 134217728);
    }

    public static synchronized l a() {
        l lVar;
        synchronized (l.class) {
            lVar = a;
        }
        return lVar;
    }

    private void a(h hVar) {
        Bitmap decodeFile;
        a("sdk handle notify");
        int hashCode = hVar.a.hashCode() + hVar.d.hashCode();
        int a2 = this.f100a.a();
        Notification.Builder builder = new Notification.Builder(this.f97a);
        if (a2 != 0) {
            builder.setSmallIcon(a2);
        }
        NotificationBaseRemoteView notificationBaseRemoteView = new NotificationBaseRemoteView(this.f97a);
        notificationBaseRemoteView.a(hVar.d, hVar.e);
        notificationBaseRemoteView.a(a2);
        a(hVar, hashCode, notificationBaseRemoteView);
        builder.setContent(notificationBaseRemoteView);
        builder.setTicker(hVar.c).setAutoCancel(true);
        builder.setContentIntent(a(hVar, 2));
        builder.setDeleteIntent(a(hVar, 1));
        Notification build = builder.build();
        if (!TextUtils.isEmpty(hVar.a()) && (decodeFile = BitmapFactory.decodeFile(hVar.a())) != null) {
            a("big picture");
            NotificationBigRemoteView notificationBigRemoteView = new NotificationBigRemoteView(this.f97a);
            notificationBigRemoteView.a(hVar.d, hVar.e);
            notificationBigRemoteView.a(a2);
            notificationBigRemoteView.a(decodeFile);
            a(hVar, hashCode, (NotificationBaseRemoteView) notificationBigRemoteView);
            build.bigContentView = notificationBigRemoteView;
        }
        ((NotificationManager) this.f97a.getSystemService("notification")).notify(hashCode, build);
    }

    private void a(h hVar, int i, NotificationBaseRemoteView notificationBaseRemoteView) {
        PendingIntent b2 = (hVar.g == null || TextUtils.isEmpty(hVar.g.trim()) || this.f100a == null) ? null : this.f100a.b(new h(hVar));
        if (b2 != null) {
            Intent intent = new Intent(this.f97a, MiPushRelayTraceService.class);
            Bundle bundle = new Bundle();
            bundle.putAll(hVar.a());
            bundle.putInt("intenttype", 2);
            bundle.putInt("notifyid", i);
            bundle.putParcelable("pendingintent", b2);
            intent.putExtras(bundle);
            int i2 = (int) hVar.f173a;
            notificationBaseRemoteView.a(hVar.g, PendingIntent.getService(this.f97a, (i2 * i2) + 3, intent, 134217728));
        }
    }

    private void a(ce ceVar) {
        if (this.f103a != null) {
            a(this.f105b + "--->receivedT " + ceVar.f173a);
            this.f103a.c(new cg(ceVar));
        }
    }

    public static void a(String str) {
        Log.d("ads-notify-fd5dfce4", str);
    }

    private void a(String str, int i, String str2) {
        new j(this.f97a, this.f98a, str, i, str2, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);
    }

    private void a(String str, long j, int i) {
        this.b++;
        d.a("存入cache 的数量: " + this.b);
        if (this.f101a != null) {
            this.f101a.a(str, j, i);
            this.f101a.a();
        }
    }

    private void a(String str, String str2) {
        a(str, 0, str2);
    }

    /* renamed from: a  reason: collision with other method in class */
    private boolean m81a(ce ceVar) {
        int i;
        int i2;
        String str;
        SharedPreferences sharedPreferences;
        if (ceVar.c <= 0) {
            a("white user");
            return true;
        }
        switch (ceVar.a) {
            case 1:
                i = ceVar.c * 4;
                a("bubble uplimit: " + i);
                sharedPreferences = this.f98a;
                str = "bubblecount";
                break;
            case 2:
                i = ceVar.c;
                a("notify uplimit: " + i);
                sharedPreferences = this.f98a;
                str = "notifycount";
                break;
            default:
                i = 0;
                i2 = 0;
                break;
        }
        i2 = sharedPreferences.getInt(str, 0);
        if (i2 <= i) {
            return true;
        }
        a("reach up limit---already count： " + i2 + " 上限: " + i);
        return false;
    }

    /* renamed from: a  reason: collision with other method in class */
    private boolean m82a(String str) {
        try {
            JSONObject jSONObject = new JSONObject(str);
            if (jSONObject.optInt("showType") != 1000) {
                return false;
            }
            if (this.f102a != null) {
                this.f102a.a(jSONObject.optString("content"));
                return true;
            }
            Log.e("ads-notify-fd5dfce4", "接受到外部的消息，但是外部的listener");
            return true;
        } catch (JSONException unused) {
            return true;
        }
    }

    private void b(ce ceVar) {
        a(ceVar);
        if (ceVar.a == 1) {
            a aVar = (a) ceVar;
            if (this.f100a != null) {
                this.f100a.a(aVar);
            }
        } else if (ceVar.a == 2) {
            h hVar = (h) ceVar;
            try {
                a(this.f105b + "--->get notify");
                if (this.f100a != null) {
                    if (!this.f100a.a(new h(hVar))) {
                        a(hVar);
                    }
                }
            } catch (Exception unused) {
                Log.e("ads-notify-fd5dfce4", "SDK 发出notification 失败");
            }
        }
    }

    public synchronized int a(int i) {
        int i2;
        SharedPreferences sharedPreferences;
        String str;
        i2 = 0;
        if (i == 2) {
            try {
                sharedPreferences = this.f98a;
                str = "notifycount";
            } catch (Throwable th) {
                throw th;
            }
        } else if (i == 1) {
            sharedPreferences = this.f98a;
            str = "bubblecount";
        }
        i2 = sharedPreferences.getInt(str, 0);
        return i2;
    }

    /* renamed from: a  reason: collision with other method in class */
    public synchronized void m83a(int i) {
        SharedPreferences.Editor putInt;
        if (i == 2) {
            try {
                putInt = this.f98a.edit().putInt("notifycount", this.f98a.getInt("notifycount", 0) + 1);
            } catch (Throwable th) {
                throw th;
            }
        } else if (i == 1) {
            putInt = this.f98a.edit().putInt("bubblecount", this.f98a.getInt("bubblecount", 0) + 1);
        }
        putInt.commit();
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:0x00ec A[ADDED_TO_REGION] */
    /* JADX WARNING: Removed duplicated region for block: B:24:? A[ADDED_TO_REGION, RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void a(int r4, com.xiaomi.push.ce r5, com.xiaomi.miui.pushads.sdk.j r6) {
        /*
            r3 = this;
            if (r5 != 0) goto L_0x0019
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = r3.f105b
            r4.append(r5)
            java.lang.String r5 = "--->cell is null"
            r4.append(r5)
            java.lang.String r4 = r4.toString()
            a((java.lang.String) r4)
            return
        L_0x0019:
            r6 = -1
            if (r4 != r6) goto L_0x007d
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r0 = r3.f105b
            r6.append(r0)
            java.lang.String r0 = "--->download failed: "
            r6.append(r0)
            long r0 = r5.f173a
            r6.append(r0)
            java.lang.String r6 = r6.toString()
            a((java.lang.String) r6)
            int r6 = r5.d
            int r6 = r6 + 1
            r5.d = r6
            int r6 = r5.d
            r0 = 10
            if (r6 >= r0) goto L_0x0077
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r0 = "下载失败写入缓存 "
            r6.append(r0)
            java.lang.String r0 = r5.h
            r6.append(r0)
            java.lang.String r0 = "  "
            r6.append(r0)
            long r0 = r5.f175b
            r6.append(r0)
            java.lang.String r0 = "  "
            r6.append(r0)
            int r0 = r5.d
            r6.append(r0)
            java.lang.String r6 = r6.toString()
            com.xiaomi.miui.pushads.sdk.d.a(r6)
            java.lang.String r6 = r5.h
            long r0 = r5.f175b
            int r2 = r5.d
            r3.a((java.lang.String) r6, (long) r0, (int) r2)
            goto L_0x00e8
        L_0x0077:
            java.lang.String r6 = "下载失败次数超过 10 不写入缓存"
        L_0x0079:
            com.xiaomi.miui.pushads.sdk.d.a(r6)
            goto L_0x00e8
        L_0x007d:
            if (r4 != 0) goto L_0x00cf
            int r6 = r5.c
            if (r6 <= 0) goto L_0x0092
            int r6 = r3.f96a
            int r6 = r6 + 1
            r3.f96a = r6
            com.xiaomi.miui.pushads.sdk.l r6 = a()
            int r0 = r5.a
            r6.a((int) r0)
        L_0x0092:
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r0 = r3.f105b
            r6.append(r0)
            java.lang.String r0 = "--->download sucess: "
            r6.append(r0)
            java.lang.String r0 = "id: "
            r6.append(r0)
            long r0 = r5.f173a
            r6.append(r0)
            java.lang.String r0 = " type: "
            r6.append(r0)
            int r0 = r5.a
            r6.append(r0)
            java.lang.String r0 = " count: "
            r6.append(r0)
            com.xiaomi.miui.pushads.sdk.l r0 = a()
            int r1 = r5.a
            int r0 = r0.a((int) r1)
            r6.append(r0)
            java.lang.String r6 = r6.toString()
            a((java.lang.String) r6)
            goto L_0x00e8
        L_0x00cf:
            java.lang.String r6 = "com.miui.ads"
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "广告无效或者超过限制 "
            r0.append(r1)
            r0.append(r4)
            java.lang.String r0 = r0.toString()
            android.util.Log.w(r6, r0)
            java.lang.String r6 = "广告无效或者超过限制"
            goto L_0x0079
        L_0x00e8:
            com.xiaomi.miui.pushads.sdk.e r6 = r3.f100a
            if (r6 == 0) goto L_0x010e
            if (r4 != 0) goto L_0x010e
            boolean r4 = r3.a((com.xiaomi.push.ce) r5)
            if (r4 == 0) goto L_0x00f8
            r3.b(r5)
            goto L_0x010e
        L_0x00f8:
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = r3.f105b
            r4.append(r5)
            java.lang.String r5 = "--->reach limit, no return to app"
            r4.append(r5)
            java.lang.String r4 = r4.toString()
            a((java.lang.String) r4)
        L_0x010e:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.miui.pushads.sdk.l.a(int, com.xiaomi.push.ce, com.xiaomi.miui.pushads.sdk.j):void");
    }

    public void onCommandResult(String str, long j, String str2, List<String> list) {
        a(this.f105b + "--->onCommandResult == " + str + " resultCode: " + j + " reason: " + str2);
        for (int i = 0; i < list.size(); i++) {
            a("param: " + list.get(i));
        }
        if (TextUtils.equals(MiPushClient.COMMAND_SET_ALIAS, str)) {
            boolean z = false;
            for (int i2 = 0; i2 < list.size(); i2++) {
                if (TextUtils.equals(this.f104a, list.get(i2))) {
                    a(this.f105b + "--->alias ok: ");
                    z = true;
                }
            }
            if (!z) {
                a(this.f105b + "--->alias failed, retry: ");
            }
        }
    }

    public void onInitializeResult(long j, String str, String str2) {
        if (this.f100a != null) {
            Message obtainMessage = this.f99a.obtainMessage();
            obtainMessage.what = 4;
            obtainMessage.arg1 = (int) j;
            obtainMessage.obj = str2;
            this.f99a.sendMessage(obtainMessage);
        }
        if (0 == j) {
            a(this.f105b + "--->cahnel OK");
            this.f99a.sendEmptyMessage(3);
            if (!f.a(this.c)) {
                this.f99a.sendEmptyMessage(6);
            }
            this.f99a.sendEmptyMessage(5);
            return;
        }
        a(this.f105b + "--->chanle failed， need app reopen");
    }

    public void onReceiveMessage(String str, String str2, String str3, boolean z) {
        a(this.f105b + "--->##" + str3);
        if (f.a(this.f104a) && f.a(this.c)) {
            a(this.f105b + "--->no alias，ignore the msg " + str + "##" + str3);
        } else if (!f.a(str2) && !f.a(this.f104a) && !TextUtils.equals(this.f104a, str2)) {
            a(this.f105b + "--->get msg for different alias. unset " + str + "##" + str3);
            MiPushClient.unsetAlias(this.f97a, str2, getCategory());
        } else if (!f.a(str3) && !f.a(this.c) && !TextUtils.equals(this.c, str3)) {
            a(this.f105b + "--->get msg for old topic, unset " + str + "##" + str3);
            MiPushClient.unsubscribe(this.f97a, str3, getCategory());
        } else if (!a(str)) {
            a(str, this.f105b);
        }
    }

    public void onSubscribeResult(long j, String str, String str2) {
        a(this.f105b + "--->topic resultCode: " + j + " reason: " + str + " topic: " + str2);
        if (j != 0) {
            this.f99a.sendEmptyMessageDelayed(6, 3600000);
        }
    }

    public void onUnsubscribeResult(long j, String str, String str2) {
        a(this.f105b + "--->unsuscribe topic resultCode: " + j + " reason: " + str + " topic: " + str2);
    }
}
