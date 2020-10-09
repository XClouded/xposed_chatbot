package com.xiaomi.push.service;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.push.ab;
import com.xiaomi.push.l;
import java.util.ArrayList;
import java.util.List;

public class ax {
    private static ax a;

    /* renamed from: a  reason: collision with other field name */
    private static String f872a;

    /* renamed from: a  reason: collision with other field name */
    private Context f873a;

    /* renamed from: a  reason: collision with other field name */
    private Messenger f874a;

    /* renamed from: a  reason: collision with other field name */
    private List<Message> f875a = new ArrayList();

    /* renamed from: a  reason: collision with other field name */
    private boolean f876a = false;
    /* access modifiers changed from: private */
    public Messenger b;
    /* access modifiers changed from: private */

    /* renamed from: b  reason: collision with other field name */
    public boolean f877b = false;

    private ax(Context context) {
        this.f873a = context.getApplicationContext();
        this.f874a = new Messenger(new ay(this, Looper.getMainLooper()));
        if (a()) {
            b.c("use miui push service");
            this.f876a = true;
        }
    }

    private Message a(Intent intent) {
        Message obtain = Message.obtain();
        obtain.what = 17;
        obtain.obj = intent;
        return obtain;
    }

    public static ax a(Context context) {
        if (a == null) {
            a = new ax(context);
        }
        return a;
    }

    /* renamed from: a  reason: collision with other method in class */
    private synchronized void m584a(Intent intent) {
        if (this.f877b) {
            Message a2 = a(intent);
            if (this.f875a.size() >= 50) {
                this.f875a.remove(0);
            }
            this.f875a.add(a2);
            return;
        } else if (this.b == null) {
            Context context = this.f873a;
            az azVar = new az(this);
            Context context2 = this.f873a;
            context.bindService(intent, azVar, 1);
            this.f877b = true;
            this.f875a.clear();
            this.f875a.add(a(intent));
        } else {
            try {
                this.b.send(a(intent));
            } catch (RemoteException unused) {
                this.b = null;
                this.f877b = false;
            }
        }
        return;
    }

    private boolean a() {
        if (ab.e) {
            return false;
        }
        try {
            PackageInfo packageInfo = this.f873a.getPackageManager().getPackageInfo("com.xiaomi.xmsf", 4);
            return packageInfo != null && packageInfo.versionCode >= 104;
        } catch (Exception unused) {
            return false;
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m585a(Intent intent) {
        try {
            if (l.a() || Build.VERSION.SDK_INT < 26) {
                this.f873a.startService(intent);
                return true;
            }
            a(intent);
            return true;
        } catch (Exception e) {
            b.a((Throwable) e);
            return false;
        }
    }
}
