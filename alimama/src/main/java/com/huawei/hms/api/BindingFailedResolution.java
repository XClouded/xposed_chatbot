package com.huawei.hms.api;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.view.KeyEvent;
import com.huawei.hms.b.f;
import com.huawei.hms.c.h;
import com.huawei.hms.c.j;

public class BindingFailedResolution implements ServiceConnection, com.huawei.hms.activity.a {
    private Activity a;
    private boolean b = true;
    /* access modifiers changed from: private */
    public a c;
    private Handler d = null;
    private Handler e = null;

    public int getRequestCode() {
        return 2003;
    }

    public void onServiceDisconnected(ComponentName componentName) {
    }

    public void onBridgeActivityCreate(Activity activity) {
        this.a = activity;
        d.a.a(this.a);
        a();
        a(activity);
    }

    private void a() {
        if (this.e != null) {
            this.e.removeMessages(3);
        } else {
            this.e = new Handler(Looper.getMainLooper(), new a(this));
        }
        this.e.sendEmptyMessageDelayed(3, 2000);
    }

    private void a(Activity activity) {
        Intent intent = new Intent();
        intent.setClassName(HuaweiApiAvailability.SERVICES_PACKAGE, HuaweiApiAvailability.ACTIVITY_NAME);
        com.huawei.hms.support.log.a.b("BindingFailedResolution", "onBridgeActivityCreate：try to start HMS");
        try {
            activity.startActivityForResult(intent, getRequestCode());
        } catch (Throwable th) {
            com.huawei.hms.support.log.a.d("BindingFailedResolution", "ActivityNotFoundException：" + th.getMessage());
            if (this.e != null) {
                this.e.removeMessages(3);
                this.e = null;
            }
            b();
        }
    }

    public void onBridgeActivityDestroy() {
        e();
        d.a.b(this.a);
        this.a = null;
    }

    public boolean onBridgeActivityResult(int i, int i2, Intent intent) {
        if (i != getRequestCode()) {
            return false;
        }
        com.huawei.hms.support.log.a.b("BindingFailedResolution", "onBridgeActivityResult");
        if (this.e != null) {
            this.e.removeMessages(3);
            this.e = null;
        }
        b();
        return true;
    }

    private void b() {
        if (c()) {
            d();
            return;
        }
        com.huawei.hms.support.log.a.d("BindingFailedResolution", "In connect, bind core try fail");
        a(false);
    }

    public void onBridgeConfigurationChanged() {
        if (this.c != null) {
            com.huawei.hms.support.log.a.b("BindingFailedResolution", "re show prompt dialog");
            f();
        }
    }

    public void onKeyUp(int i, KeyEvent keyEvent) {
        com.huawei.hms.support.log.a.b("BindingFailedResolution", "On key up when resolve conn error");
    }

    /* access modifiers changed from: protected */
    public Activity getActivity() {
        return this.a;
    }

    /* access modifiers changed from: private */
    public void a(boolean z) {
        if (this.b) {
            this.b = false;
            onStartResult(z);
        }
    }

    /* access modifiers changed from: protected */
    public void onStartResult(boolean z) {
        if (getActivity() != null) {
            if (z) {
                a(0);
            } else {
                f();
            }
        }
    }

    private boolean c() {
        Activity activity = getActivity();
        if (activity == null) {
            return false;
        }
        Intent intent = new Intent(HuaweiApiAvailability.SERVICES_ACTION);
        intent.setPackage(HuaweiApiAvailability.SERVICES_PACKAGE);
        return activity.bindService(intent, this, 1);
    }

    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        e();
        a(true);
        Activity activity = getActivity();
        if (activity != null) {
            j.a((Context) activity, (ServiceConnection) this);
        }
    }

    /* access modifiers changed from: private */
    public void a(int i) {
        Activity activity = getActivity();
        if (activity != null && !activity.isFinishing()) {
            com.huawei.hms.support.log.a.b("BindingFailedResolution", "finishBridgeActivity：" + i);
            Intent intent = new Intent();
            intent.putExtra("intent.extra.RESULT", i);
            activity.setResult(-1, intent);
            activity.finish();
        }
    }

    private void d() {
        if (this.d != null) {
            this.d.removeMessages(2);
        } else {
            this.d = new Handler(Looper.getMainLooper(), new b(this));
        }
        this.d.sendEmptyMessageDelayed(2, 5000);
    }

    private void e() {
        if (this.d != null) {
            this.d.removeMessages(2);
            this.d = null;
        }
    }

    private void f() {
        Activity activity = getActivity();
        if (activity != null && !activity.isFinishing()) {
            if (this.c == null) {
                this.c = new a((a) null);
            } else {
                this.c.b();
            }
            com.huawei.hms.support.log.a.d("BindingFailedResolution", "showPromptdlg to resolve conn error");
            this.c.a(activity, new c(this));
        }
    }

    private static class a extends f {
        private a() {
        }

        /* synthetic */ a(a aVar) {
            this();
        }

        /* access modifiers changed from: protected */
        public String a(Context context) {
            return h.a("hms_bindfaildlg_message", j.a(context, (String) null), j.a(context, HuaweiApiAvailability.SERVICES_PACKAGE));
        }

        /* access modifiers changed from: protected */
        public String b(Context context) {
            return h.d("hms_confirm");
        }
    }
}
