package com.xiaomi.miui.pushads.sdk;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.push.ce;
import com.xiaomi.push.cf;
import java.util.List;

public class k extends MiPushClient.MiPushClientCallback implements c {
    private static k a;

    /* renamed from: a  reason: collision with other field name */
    private int f86a;

    /* renamed from: a  reason: collision with other field name */
    private Context f87a;

    /* renamed from: a  reason: collision with other field name */
    private SharedPreferences f88a;

    /* renamed from: a  reason: collision with other field name */
    private Handler f89a;

    /* renamed from: a  reason: collision with other field name */
    private g f90a;

    /* renamed from: a  reason: collision with other field name */
    private cf f91a;

    /* renamed from: a  reason: collision with other field name */
    private String f92a;
    private int b;

    /* renamed from: b  reason: collision with other field name */
    private String f93b;

    public enum a {
        NONE,
        Wifi,
        MN2G,
        MN3G,
        MN4G
    }

    public static synchronized k a() {
        k kVar;
        synchronized (k.class) {
            kVar = a;
        }
        return kVar;
    }

    public static void a(String str) {
        Log.d("ads-notify-fd5dfce4", str);
    }

    private void a(String str, int i, String str2) {
        new j(this.f87a, this.f88a, str, i, str2, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);
    }

    private void a(String str, long j, int i) {
        this.b++;
        d.a("存入cache 的数量: " + this.b);
        this.f90a.a(str, j, i);
        this.f90a.a();
    }

    private void a(String str, String str2) {
        a(str, 0, str2);
    }

    private boolean a(ce ceVar) {
        int i;
        int i2;
        String str;
        SharedPreferences sharedPreferences;
        if (ceVar.c <= 0) {
            a("白名单用户");
            return true;
        }
        switch (ceVar.a) {
            case 1:
                i = ceVar.c * 4;
                a("冒泡上限: " + i);
                sharedPreferences = this.f88a;
                str = "bubblecount";
                break;
            case 2:
                i = ceVar.c;
                a("通知上限: " + i);
                sharedPreferences = this.f88a;
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
        a("广告次数超过上限---已经获得次数： " + i2 + " 上限: " + i);
        return false;
    }

    public synchronized int a(int i) {
        int i2;
        SharedPreferences sharedPreferences;
        String str;
        i2 = 0;
        if (i == 2) {
            try {
                sharedPreferences = this.f88a;
                str = "notifycount";
            } catch (Throwable th) {
                throw th;
            }
        } else if (i == 1) {
            sharedPreferences = this.f88a;
            str = "bubblecount";
        }
        i2 = sharedPreferences.getInt(str, 0);
        return i2;
    }

    /* renamed from: a  reason: collision with other method in class */
    public synchronized void m80a(int i) {
        SharedPreferences.Editor putInt;
        if (i == 2) {
            try {
                putInt = this.f88a.edit().putInt("notifycount", this.f88a.getInt("notifycount", 0) + 1);
            } catch (Throwable th) {
                throw th;
            }
        } else if (i == 1) {
            putInt = this.f88a.edit().putInt("bubblecount", this.f88a.getInt("bubblecount", 0) + 1);
        }
        putInt.commit();
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:0x00cc A[ADDED_TO_REGION] */
    /* JADX WARNING: Removed duplicated region for block: B:24:? A[ADDED_TO_REGION, RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void a(int r4, com.xiaomi.push.ce r5, com.xiaomi.miui.pushads.sdk.j r6) {
        /*
            r3 = this;
            if (r5 != 0) goto L_0x0008
            java.lang.String r4 = "返回广告为null"
            a((java.lang.String) r4)
            return
        L_0x0008:
            r6 = -1
            if (r4 != r6) goto L_0x0067
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r0 = "广告下载失败: "
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
            if (r6 >= r0) goto L_0x0061
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
            goto L_0x00c8
        L_0x0061:
            java.lang.String r6 = "下载失败次数超过 10 不写入缓存"
        L_0x0063:
            com.xiaomi.miui.pushads.sdk.d.a(r6)
            goto L_0x00c8
        L_0x0067:
            if (r4 != 0) goto L_0x00af
            int r6 = r5.c
            if (r6 <= 0) goto L_0x007c
            int r6 = r3.f86a
            int r6 = r6 + 1
            r3.f86a = r6
            com.xiaomi.miui.pushads.sdk.k r6 = a()
            int r0 = r5.a
            r6.a((int) r0)
        L_0x007c:
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r0 = "广告下载成功: id: "
            r6.append(r0)
            long r0 = r5.f173a
            r6.append(r0)
            java.lang.String r0 = " 类型: "
            r6.append(r0)
            int r0 = r5.a
            r6.append(r0)
            java.lang.String r0 = " 成功次数: "
            r6.append(r0)
            com.xiaomi.miui.pushads.sdk.k r0 = a()
            int r1 = r5.a
            int r0 = r0.a((int) r1)
            r6.append(r0)
            java.lang.String r6 = r6.toString()
            a((java.lang.String) r6)
            goto L_0x00c8
        L_0x00af:
            java.lang.String r6 = "com.miui.ads"
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "广告无效或者超过限制 "
            r0.append(r1)
            r0.append(r4)
            java.lang.String r0 = r0.toString()
            android.util.Log.w(r6, r0)
            java.lang.String r6 = "广告无效或者超过限制"
            goto L_0x0063
        L_0x00c8:
            com.xiaomi.push.cf r6 = r3.f91a
            if (r6 == 0) goto L_0x00e4
            if (r4 != 0) goto L_0x00e4
            boolean r4 = r3.a((com.xiaomi.push.ce) r5)
            if (r4 == 0) goto L_0x00df
            java.lang.String r4 = "===========给APP 发送广告信息"
            a((java.lang.String) r4)
            com.xiaomi.push.cf r4 = r3.f91a
            r4.a(r5)
            goto L_0x00e4
        L_0x00df:
            java.lang.String r4 = "广告数量超过限制，不返回给APP"
            a((java.lang.String) r4)
        L_0x00e4:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.miui.pushads.sdk.k.a(int, com.xiaomi.push.ce, com.xiaomi.miui.pushads.sdk.j):void");
    }

    public void onCommandResult(String str, long j, String str2, List<String> list) {
        if (j != 0) {
            a("命令失败: " + str + " code: " + j + " reason: " + str2);
            for (int i = 0; i < list.size(); i++) {
                a("param: " + list.get(i));
            }
        }
        if (TextUtils.equals(MiPushClient.COMMAND_SET_ALIAS, str)) {
            boolean z = false;
            for (int i2 = 0; i2 < list.size(); i2++) {
                if (TextUtils.equals(this.f92a, list.get(i2))) {
                    a("设置别名成功: ");
                    z = true;
                }
            }
            if (!z) {
                a("设置别名失败，重新设置: ");
                this.f89a.sendEmptyMessage(2);
            }
        }
    }

    public void onInitializeResult(long j, String str, String str2) {
        if (this.f91a != null) {
            Message obtainMessage = this.f89a.obtainMessage();
            obtainMessage.what = 4;
            obtainMessage.arg1 = (int) j;
            obtainMessage.obj = str2;
            this.f89a.sendMessage(obtainMessage);
        }
        if (0 == j) {
            a("通道进行初始化OK");
            this.f89a.sendEmptyMessage(3);
            this.f89a.sendEmptyMessage(5);
            return;
        }
        a("通道初始化失败， 已经通知了app，需要重新 open 通道");
    }

    public void onReceiveMessage(String str, String str2, String str3, boolean z) {
        a("接受到消息 " + str + "##" + str3 + "##");
        if (f.a(this.f92a)) {
            a("没有有效alias，忽略消息 " + str + "##" + str3 + "##");
        } else if (f.a(str2) || f.a(this.f92a) || TextUtils.equals(this.f92a, str2)) {
            a(str, this.f93b);
        } else {
            a("接受到不同alias 的消息，注销旧的 " + str + "##" + str3 + "##");
            MiPushClient.unsetAlias(this.f87a, str2, getCategory());
        }
    }

    public void onSubscribeResult(long j, String str, String str2) {
    }

    public void onUnsubscribeResult(long j, String str, String str2) {
    }
}
