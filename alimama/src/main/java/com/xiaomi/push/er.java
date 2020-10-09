package com.xiaomi.push;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import androidx.core.app.NotificationCompat;
import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.push.service.f;

class er implements en {
    er() {
    }

    private void a(Service service, Intent intent) {
        if ("com.xiaomi.mipush.sdk.WAKEUP".equals(intent.getAction())) {
            String stringExtra = intent.getStringExtra("waker_pkgname");
            String stringExtra2 = intent.getStringExtra("awake_info");
            if (TextUtils.isEmpty(stringExtra)) {
                eg.a(service.getApplicationContext(), NotificationCompat.CATEGORY_SERVICE, 1007, "old version message");
            } else if (!TextUtils.isEmpty(stringExtra2)) {
                String b = ef.b(stringExtra2);
                if (!TextUtils.isEmpty(b)) {
                    eg.a(service.getApplicationContext(), b, 1007, "old version message ");
                } else {
                    eg.a(service.getApplicationContext(), NotificationCompat.CATEGORY_SERVICE, 1008, "B get a incorrect message");
                }
            } else {
                eg.a(service.getApplicationContext(), stringExtra, 1007, "play with service ");
            }
        }
    }

    private void a(Context context, String str, String str2, String str3) {
        if (context == null || TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            if (TextUtils.isEmpty(str3)) {
                eg.a(context, NotificationCompat.CATEGORY_SERVICE, 1008, "argument error");
            } else {
                eg.a(context, str3, 1008, "argument error");
            }
        } else if (!f.a(context, str)) {
            eg.a(context, str3, 1003, "B is not ready");
        } else {
            eg.a(context, str3, 1002, "B is ready");
            eg.a(context, str3, 1004, "A is ready");
            try {
                Intent intent = new Intent();
                intent.setClassName(str, str2);
                intent.setAction("com.xiaomi.mipush.sdk.WAKEUP");
                intent.putExtra("waker_pkgname", context.getPackageName());
                intent.putExtra("awake_info", ef.a(str3));
                if (context.startService(intent) != null) {
                    eg.a(context, str3, 1005, "A is successful");
                    eg.a(context, str3, 1006, "The job is finished");
                    return;
                }
                eg.a(context, str3, 1008, "A is fail to help B's service");
            } catch (Exception e) {
                b.a((Throwable) e);
                eg.a(context, str3, 1008, "A meet a exception when help B's service");
            }
        }
    }

    public void a(Context context, Intent intent, String str) {
        if (context != null && (context instanceof Service)) {
            a((Service) context, intent);
        }
    }

    public void a(Context context, ej ejVar) {
        if (ejVar != null) {
            a(context, ejVar.a(), ejVar.c(), ejVar.d());
        }
    }
}
