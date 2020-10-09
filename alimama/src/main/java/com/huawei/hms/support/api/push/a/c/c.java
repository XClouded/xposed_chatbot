package com.huawei.hms.support.api.push.a.c;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.RemoteViews;
import com.huawei.hms.support.api.push.a.d.a;
import com.huawei.hms.support.api.push.a.d.b;
import java.security.SecureRandom;
import java.util.Date;

/* compiled from: NotificationStyleUtil */
public class c {
    public static void a(Context context, Bitmap bitmap, RemoteViews remoteViews) {
        if (context != null && remoteViews != null && a.a()) {
            if (bitmap == null) {
                int i = context.getApplicationInfo().icon;
                if (i == 0 && (i = context.getResources().getIdentifier("btn_star_big_on", "drawable", "android")) == 0) {
                    i = 17301651;
                }
                remoteViews.setImageViewResource(b.a(context, "id", "icon"), i);
                return;
            }
            remoteViews.setImageViewBitmap(b.a(context, "id", "icon"), bitmap);
        }
    }

    public static void a(Context context, int i, RemoteViews remoteViews, com.huawei.hms.support.api.push.a.b.a aVar) {
        if (context == null || remoteViews == null || aVar == null) {
            com.huawei.hms.support.log.a.c("PushSelfShowLog", "showRightBtn error");
        } else if ((a.STYLE_2.ordinal() == aVar.u() || a.STYLE_3.ordinal() == aVar.u() || a.STYLE_4.ordinal() == aVar.u()) && !TextUtils.isEmpty(aVar.v()[0]) && !TextUtils.isEmpty(aVar.w()[0])) {
            int a = b.a(context, "id", "right_btn");
            remoteViews.setViewVisibility(a, 0);
            remoteViews.setTextViewText(a, aVar.v()[0]);
            remoteViews.setOnClickPendingIntent(a, a(context, i, aVar.w()[0]));
        }
    }

    public static PendingIntent a(Context context, int i, String str) {
        Intent flags = new Intent("com.huawei.android.push.intent.CLICK").setPackage(context.getPackageName()).setFlags(32);
        flags.putExtra("notifyId", i);
        flags.putExtra("clickBtn", str);
        int hashCode = (context.getPackageName() + str + new SecureRandom().nextInt() + new Date().toString()).hashCode();
        StringBuilder sb = new StringBuilder();
        sb.append("getPendingIntent,requestCode:");
        sb.append(hashCode);
        com.huawei.hms.support.log.a.a("PushSelfShowLog", sb.toString());
        return PendingIntent.getBroadcast(context, hashCode, flags, 134217728);
    }

    public static String a(Context context, com.huawei.hms.support.api.push.a.b.a aVar) {
        if (context == null || aVar == null) {
            return "";
        }
        if (!TextUtils.isEmpty(aVar.n())) {
            return aVar.n();
        }
        return context.getResources().getString(context.getApplicationInfo().labelRes);
    }
}
