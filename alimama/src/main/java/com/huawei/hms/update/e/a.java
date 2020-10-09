package com.huawei.hms.update.e;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.taobao.windvane.config.WVConfigManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import com.huawei.hms.activity.BridgeActivity;
import com.huawei.hms.api.HuaweiApiAvailability;
import com.huawei.hms.c.f;
import com.huawei.hms.c.g;
import com.huawei.hms.c.j;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/* compiled from: AbsUpdateWizard */
public abstract class a implements com.huawei.hms.activity.a {
    public WeakReference<Activity> a;
    public com.huawei.hms.activity.a b;
    protected v c = null;
    protected b d = null;
    protected boolean e = false;
    protected int f = -1;
    protected String g = null;
    protected String h = null;
    protected int i = 0;
    protected String j = null;

    public void a(b bVar) {
    }

    /* access modifiers changed from: package-private */
    public abstract void a(Class<? extends b> cls);

    public void b(b bVar) {
    }

    public static String a(int i2) {
        if (i2 == 0) {
            return t.class.getName();
        }
        if (i2 == 2) {
            return k.class.getName();
        }
        switch (i2) {
            case 4:
                return l.class.getName();
            case 5:
                return m.class.getName();
            case 6:
                return w.class.getName();
            default:
                return "";
        }
    }

    /* access modifiers changed from: protected */
    public Activity a() {
        if (this.a == null) {
            return null;
        }
        return (Activity) this.a.get();
    }

    public void onBridgeActivityCreate(Activity activity) {
        this.a = new WeakReference<>(activity);
        if (this.c == null) {
            Intent intent = activity.getIntent();
            if (intent != null) {
                this.c = (v) intent.getSerializableExtra(BridgeActivity.EXTRA_DELEGATE_UPDATE_INFO);
                if (this.c == null) {
                    return;
                }
            } else {
                return;
            }
        }
        this.g = this.c.b();
        this.h = this.c.e();
        this.i = this.c.c();
        this.j = this.c.d();
        this.b = null;
        this.e = false;
        this.f = -1;
    }

    public void onBridgeActivityDestroy() {
        this.a = null;
        b();
        if (this.e && this.b != null) {
            this.b.onBridgeActivityDestroy();
        }
    }

    public void onBridgeConfigurationChanged() {
        if (this.e && this.b != null) {
            this.b.onBridgeConfigurationChanged();
        } else if (this.d != null) {
            Class<?> cls = this.d.getClass();
            this.d.c();
            this.d = null;
            a((Class<? extends b>) cls);
        }
    }

    /* access modifiers changed from: protected */
    public void b() {
        if (this.d != null) {
            try {
                this.d.c();
                this.d = null;
            } catch (IllegalStateException e2) {
                com.huawei.hms.support.log.a.d("AbsUpdateWizard", "In dismissDialog, Failed to dismiss the dialog." + e2.getMessage());
            }
        }
    }

    /* access modifiers changed from: protected */
    public void a(int i2, int i3) {
        Activity a2;
        if (!com.huawei.hms.support.b.a.a().b() && (a2 = a()) != null && !a2.isFinishing()) {
            int b2 = new g(a2).b(this.g);
            HashMap hashMap = new HashMap();
            hashMap.put(WVConfigManager.CONFIGNAME_PACKAGE, a2.getPackageName());
            hashMap.put("target_package", this.g);
            hashMap.put("target_ver", String.valueOf(b2));
            hashMap.put("sdk_ver", String.valueOf(HuaweiApiAvailability.HMS_SDK_VERSION_CODE));
            hashMap.put("app_id", j.a((Context) a2));
            hashMap.put("trigger_api", "core.connnect");
            hashMap.put("update_type", String.valueOf(i3));
            hashMap.put("net_type", String.valueOf(f.a((Context) a2)));
            hashMap.put("result", c(i2, i3));
            com.huawei.hms.support.b.a.a().a((Context) a2, "HMS_SDK_UPDATE", (Map<String, String>) hashMap);
        }
    }

    private String c(int i2, int i3) {
        String valueOf = String.valueOf(i2);
        if (i3 == 0) {
            return "0000" + valueOf;
        } else if (i3 != 2) {
            switch (i3) {
                case 4:
                    return "6000" + valueOf;
                case 5:
                    return "5000" + valueOf;
                case 6:
                    return "4000" + valueOf;
                default:
                    return valueOf;
            }
        } else {
            return "2000" + valueOf;
        }
    }

    /* access modifiers changed from: protected */
    public boolean a(boolean z) {
        Activity a2 = a();
        if (a2 == null) {
            return false;
        }
        ArrayList f2 = this.c.f();
        if (f2.size() > 0) {
            f2.remove(0);
        }
        if (this.b == null) {
            a(f2);
        }
        if (this.b == null) {
            return false;
        }
        this.e = true;
        this.c.a(f2);
        this.c.b(z);
        this.b.onBridgeActivityCreate(a2);
        return true;
    }

    private void a(ArrayList arrayList) {
        String a2 = (arrayList == null || arrayList.size() <= 0) ? null : a(((Integer) arrayList.get(0)).intValue());
        if (a2 != null) {
            try {
                this.b = (com.huawei.hms.activity.a) Class.forName(a2).asSubclass(com.huawei.hms.activity.a.class).newInstance();
            } catch (ClassCastException | ClassNotFoundException | IllegalAccessException | InstantiationException e2) {
                com.huawei.hms.support.log.a.d("AbsUpdateWizard", "getBridgeActivityDelegate error" + e2.getMessage());
            }
        }
    }

    /* access modifiers changed from: protected */
    public void b(int i2, int i3) {
        Activity a2 = a();
        if (a2 != null && !a2.isFinishing()) {
            a(i2, i3);
            Intent intent = new Intent();
            intent.putExtra(BridgeActivity.EXTRA_DELEGATE_CLASS_NAME, getClass().getName());
            intent.putExtra("intent.extra.RESULT", i2);
            a2.setResult(-1, intent);
            a2.finish();
        }
    }

    public void onKeyUp(int i2, KeyEvent keyEvent) {
        if (this.e && this.b != null) {
            this.b.onKeyUp(i2, keyEvent);
        }
    }

    /* access modifiers changed from: protected */
    public boolean a(String str, int i2) {
        Activity a2;
        if (!TextUtils.isEmpty(str) && (a2 = a()) != null && !a2.isFinishing() && new g(a2).b(str) >= i2) {
            return true;
        }
        return false;
    }
}
