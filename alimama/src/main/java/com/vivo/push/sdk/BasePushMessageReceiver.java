package com.vivo.push.sdk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.text.TextUtils;
import com.vivo.push.model.UnvarnishedMessage;
import com.vivo.push.p;
import com.vivo.push.util.s;
import java.util.List;

public abstract class BasePushMessageReceiver extends BroadcastReceiver implements PushMessageCallback {
    public static final String TAG = "PushMessageReceiver";

    public void onBind(Context context, int i, String str) {
    }

    public void onDelAlias(Context context, int i, List<String> list, List<String> list2, String str) {
    }

    public void onDelTags(Context context, int i, List<String> list, List<String> list2, String str) {
    }

    public void onListTags(Context context, int i, List<String> list, String str) {
    }

    public void onLog(Context context, String str, int i, boolean z) {
    }

    public void onPublish(Context context, int i, String str) {
    }

    public void onSetAlias(Context context, int i, List<String> list, List<String> list2, String str) {
    }

    public void onSetTags(Context context, int i, List<String> list, List<String> list2, String str) {
    }

    public void onTransmissionMessage(Context context, UnvarnishedMessage unvarnishedMessage) {
    }

    public void onUnBind(Context context, int i, String str) {
    }

    public final void onReceive(Context context, Intent intent) {
        Context applicationContext = context.getApplicationContext();
        p.a().a(applicationContext);
        try {
            int intExtra = intent.getIntExtra("method", -1);
            String stringExtra = intent.getStringExtra("req_id");
            com.vivo.push.util.p.d(TAG, "PushMessageReceiver " + applicationContext.getPackageName() + " ; type = " + intExtra + " ; requestId = " + stringExtra);
            try {
                p.a().a(intent, (PushMessageCallback) this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e2) {
            com.vivo.push.util.p.b(TAG, "get method error", e2);
        }
    }

    public boolean isAllowNet(Context context) {
        if (context == null) {
            com.vivo.push.util.p.a(TAG, "isAllowNet sContext is null");
            return false;
        }
        String packageName = context.getPackageName();
        if (TextUtils.isEmpty(packageName)) {
            com.vivo.push.util.p.a(TAG, "isAllowNet pkgName is null");
            return false;
        }
        Intent intent = new Intent("com.vivo.pushservice.action.PUSH_SERVICE");
        intent.setPackage(packageName);
        List<ResolveInfo> queryIntentServices = context.getPackageManager().queryIntentServices(intent, 576);
        if (queryIntentServices != null && queryIntentServices.size() > 0) {
            return s.a(context, packageName);
        }
        com.vivo.push.util.p.a(TAG, "this is client sdk");
        return true;
    }
}
