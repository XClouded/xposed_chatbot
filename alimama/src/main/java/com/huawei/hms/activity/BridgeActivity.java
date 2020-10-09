package com.huawei.hms.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import com.huawei.hms.a.a;
import com.huawei.hms.c.h;
import com.huawei.hms.c.j;
import com.huawei.hms.support.log.a;
import java.lang.reflect.InvocationTargetException;

public class BridgeActivity extends Activity {
    public static final String EXTRA_DELEGATE_CLASS_NAME = "intent.extra.DELEGATE_CLASS_OBJECT";
    public static final String EXTRA_DELEGATE_UPDATE_INFO = "intent.extra.update.info";
    public static final String EXTRA_INTENT = "intent.extra.intent";
    public static final String EXTRA_IS_FULLSCREEN = "intent.extra.isfullscreen";
    public static final String EXTRA_RESULT = "intent.extra.RESULT";
    private a a;

    private static void a(Window window, boolean z) {
        try {
            window.getClass().getMethod("setHwFloating", new Class[]{Boolean.TYPE}).invoke(window, new Object[]{Boolean.valueOf(z)});
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | InvocationTargetException e) {
            a.d("BridgeActivity", "In setHwFloating, Failed to call Window.setHwFloating()." + e.getMessage());
        }
    }

    public static Intent getIntentStartBridgeActivity(Activity activity, String str) {
        Intent intent = new Intent(activity, BridgeActivity.class);
        intent.putExtra(EXTRA_DELEGATE_CLASS_NAME, str);
        intent.putExtra(EXTRA_IS_FULLSCREEN, j.a(activity));
        return intent;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        a();
        if (getIntent() != null) {
            if (h.a() == null) {
                h.a(getApplicationContext());
            }
            if (!b()) {
                setResult(1, (Intent) null);
                finish();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        if (this.a != null) {
            this.a.onBridgeActivityDestroy();
        }
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        if (this.a != null) {
            this.a.onBridgeConfigurationChanged();
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (this.a != null && !this.a.onBridgeActivityResult(i, i2, intent) && !isFinishing()) {
            setResult(i2, intent);
            finish();
        }
    }

    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        if (this.a != null) {
            this.a.onKeyUp(i, keyEvent);
        }
        return super.onKeyUp(i, keyEvent);
    }

    public void finish() {
        a.b("BridgeActivity", "Enter finish.");
        super.finish();
    }

    private void a() {
        requestWindowFeature(1);
        if (a.C0009a.a >= 9) {
            Window window = getWindow();
            window.addFlags(67108864);
            a(window, true);
        }
    }

    private boolean b() {
        Intent intent = getIntent();
        if (intent == null) {
            com.huawei.hms.support.log.a.d("BridgeActivity", "In initialize, Must not pass in a null intent.");
            return false;
        }
        if (intent.getBooleanExtra(EXTRA_IS_FULLSCREEN, false)) {
            getWindow().setFlags(1024, 1024);
        }
        String stringExtra = intent.getStringExtra(EXTRA_DELEGATE_CLASS_NAME);
        if (stringExtra == null) {
            com.huawei.hms.support.log.a.d("BridgeActivity", "In initialize, Must not pass in a null or non class object.");
            return false;
        }
        try {
            this.a = (a) Class.forName(stringExtra).asSubclass(a.class).newInstance();
            this.a.onBridgeActivityCreate(this);
            return true;
        } catch (ClassCastException | ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            com.huawei.hms.support.log.a.d("BridgeActivity", "In initialize, Failed to create 'IUpdateWizard' instance." + e.getMessage());
            return false;
        }
    }
}
