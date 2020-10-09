package com.huawei.hms.support.api.push.a.c;

import android.app.Notification;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.RemoteViews;
import com.huawei.hms.support.api.push.a.b.a;
import com.huawei.hms.support.api.push.a.d.b;

/* compiled from: PushSelfShowStyle */
public class f {
    public static Notification.Builder a(Context context, Notification.Builder builder, int i, a aVar, Bitmap bitmap) {
        com.huawei.hms.support.log.a.a("PushSelfShowLog", "Notification addStyle");
        if (context == null || builder == null || aVar == null) {
            return builder;
        }
        a aVar2 = a.STYLE_1;
        if (aVar.u() >= 0 && aVar.u() < a.values().length) {
            aVar2 = a.values()[aVar.u()];
        }
        switch (g.a[aVar2.ordinal()]) {
            case 1:
                builder.setContent(a(context, i, bitmap, aVar));
                break;
            case 2:
                e.a(context, builder, i, bitmap, aVar);
                break;
        }
        return builder;
    }

    private static RemoteViews a(Context context, int i, Bitmap bitmap, a aVar) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), b.a(context, "hwpush_layout2"));
        c.a(context, bitmap, remoteViews);
        c.a(context, i, remoteViews, aVar);
        remoteViews.setTextViewText(b.b(context, "title"), c.a(context, aVar));
        remoteViews.setTextViewText(b.b(context, "text"), aVar.l());
        return remoteViews;
    }
}
