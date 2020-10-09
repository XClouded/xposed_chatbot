package com.vivo.push.sdk.service;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import com.vivo.push.util.p;
import com.vivo.push.util.z;
import java.util.List;

public class LinkProxyActivity extends Activity {
    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        PackageManager packageManager;
        List<ResolveInfo> queryIntentServices;
        super.onCreate(bundle);
        Intent intent = getIntent();
        if (intent == null) {
            p.d("LinkProxyActivity", "enter RequestPermissionsActivity onCreate, intent is null, finish");
            finish();
            return;
        }
        boolean z = false;
        try {
            Window window = getWindow();
            window.setGravity(8388659);
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.x = 0;
            attributes.y = 0;
            attributes.height = 1;
            attributes.width = 1;
            window.setAttributes(attributes);
        } catch (Throwable th) {
            p.b("LinkProxyActivity", "enter onCreate error ", th);
        }
        String packageName = getPackageName();
        p.d("LinkProxyActivity", hashCode() + " enter onCreate " + packageName);
        if (!"com.vivo.abe".equals(packageName)) {
            try {
                if (intent.getExtras() != null) {
                    Intent intent2 = (Intent) intent.getExtras().get("previous_intent");
                    if (!(intent2 == null || (packageManager = getPackageManager()) == null || (queryIntentServices = packageManager.queryIntentServices(intent2, 576)) == null)) {
                        if (!queryIntentServices.isEmpty()) {
                            ResolveInfo resolveInfo = queryIntentServices.get(0);
                            if (!(resolveInfo == null || resolveInfo.serviceInfo == null || !resolveInfo.serviceInfo.exported)) {
                                z = true;
                            }
                        }
                    }
                    if (z) {
                        startService(intent2);
                    } else {
                        p.b("LinkProxyActivity", "service's exported is " + z);
                    }
                }
            } catch (Exception e) {
                p.a("LinkProxyActivity", e.toString(), e);
            }
        } else if (intent == null) {
            try {
                p.d("LinkProxyActivity", "adapterToService intent is null");
            } catch (Exception e2) {
                p.a("LinkProxyActivity", e2.toString(), e2);
            }
        } else if (intent.getExtras() == null) {
            p.d("LinkProxyActivity", "adapterToService getExtras() is null");
        } else {
            Intent intent3 = (Intent) intent.getExtras().get("previous_intent");
            if (intent3 == null) {
                p.d("LinkProxyActivity", "adapterToService proxyIntent is null");
            } else {
                z.a((Context) this, intent3);
            }
        }
        finish();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        p.d("LinkProxyActivity", hashCode() + " onDestory " + getPackageName());
    }
}
