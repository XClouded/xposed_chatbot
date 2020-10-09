package com.vivo.push;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.text.TextUtils;
import com.taobao.uikit.extend.component.unify.Toast.TBToast;
import com.vivo.push.util.p;
import com.vivo.push.util.s;
import com.vivo.push.util.z;
import com.vivo.vms.IPCCallback;
import com.vivo.vms.IPCInvoke;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/* compiled from: IPCManager */
public final class b implements ServiceConnection {
    private static final Object a = new Object();
    private static Map<String, b> b = new HashMap();
    private boolean c;
    private String d = null;
    private Context e;
    /* access modifiers changed from: private */
    public AtomicInteger f;
    private volatile IPCInvoke g;
    private Object h = new Object();
    private String i;
    private Handler j = null;

    private b(Context context, String str) {
        this.e = context;
        this.i = str;
        boolean z = true;
        this.f = new AtomicInteger(1);
        this.j = new Handler(Looper.getMainLooper(), new c(this));
        this.d = s.b(context);
        if (TextUtils.isEmpty(this.d) || TextUtils.isEmpty(this.i)) {
            Context context2 = this.e;
            p.c(context2, "init error : push pkgname is " + this.d + " ; action is " + this.i);
            this.c = false;
            return;
        }
        this.c = z.a(context, this.d) < 1260 ? false : z;
        b();
    }

    public static b a(Context context, String str) {
        b bVar = b.get(str);
        if (bVar == null) {
            synchronized (a) {
                bVar = b.get(str);
                if (bVar == null) {
                    bVar = new b(context, str);
                    b.put(str, bVar);
                }
            }
        }
        return bVar;
    }

    public final boolean a() {
        this.d = s.b(this.e);
        boolean z = false;
        if (TextUtils.isEmpty(this.d)) {
            p.c(this.e, "push pkgname is null");
            return false;
        }
        if (z.a(this.e, this.d) >= 1260) {
            z = true;
        }
        this.c = z;
        return this.c;
    }

    private void b() {
        int i2 = this.f.get();
        p.d("AidlManager", "Enter connect, Connection Status: " + i2);
        if (i2 != 4 && i2 != 2 && i2 != 3 && i2 != 5 && this.c) {
            a(2);
            if (!c()) {
                a(1);
                p.a("AidlManager", "bind core service fail");
                return;
            }
            this.j.removeMessages(1);
            this.j.sendEmptyMessageDelayed(1, TBToast.Duration.MEDIUM);
        }
    }

    private boolean c() {
        Intent intent = new Intent(this.i);
        intent.setPackage(this.d);
        try {
            return this.e.bindService(intent, this, 1);
        } catch (Exception e2) {
            p.a("AidlManager", "bind core error", e2);
            return false;
        }
    }

    /* access modifiers changed from: private */
    public void a(int i2) {
        this.f.set(i2);
    }

    public final void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        d();
        this.g = IPCInvoke.Stub.asInterface(iBinder);
        if (this.g == null) {
            p.d("AidlManager", "onServiceConnected error : aidl must not be null.");
            e();
            this.f.set(1);
            return;
        }
        if (this.f.get() == 2) {
            a(4);
        } else if (this.f.get() != 4) {
            e();
        }
        synchronized (this.h) {
            this.h.notifyAll();
        }
    }

    private void d() {
        this.j.removeMessages(1);
    }

    /* access modifiers changed from: private */
    public void e() {
        try {
            this.e.unbindService(this);
        } catch (Exception e2) {
            p.a("AidlManager", "On unBindServiceException:" + e2.getMessage());
        }
    }

    public final void onServiceDisconnected(ComponentName componentName) {
        this.g = null;
        a(1);
    }

    public final void onBindingDied(ComponentName componentName) {
        p.b("AidlManager", "onBindingDied : " + componentName);
    }

    public final boolean a(Bundle bundle) {
        b();
        if (this.f.get() == 2) {
            synchronized (this.h) {
                try {
                    this.h.wait(2000);
                } catch (InterruptedException e2) {
                    e2.printStackTrace();
                }
            }
        }
        try {
            int i2 = this.f.get();
            if (i2 == 4) {
                this.j.removeMessages(2);
                this.j.sendEmptyMessageDelayed(2, 30000);
                this.g.asyncCall(bundle, (IPCCallback) null);
                return true;
            }
            p.d("AidlManager", "invoke error : connect status = " + i2);
            return false;
        } catch (Exception e3) {
            p.a("AidlManager", "invoke error ", e3);
            int i3 = this.f.get();
            p.d("AidlManager", "Enter disconnect, Connection Status: " + i3);
            switch (i3) {
                case 2:
                    d();
                    a(1);
                    return false;
                case 3:
                    a(1);
                    return false;
                case 4:
                    a(1);
                    e();
                    return false;
                default:
                    return false;
            }
        }
    }
}
