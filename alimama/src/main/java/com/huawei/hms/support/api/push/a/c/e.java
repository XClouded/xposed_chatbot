package com.huawei.hms.support.api.push.a.c;

import android.app.Notification;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import com.huawei.hms.support.api.push.a.b.a;

/* compiled from: PushNotificationStyle */
public class e {
    public static void a(Context context, Notification.Builder builder, int i, Bitmap bitmap, a aVar) {
        if (aVar == null || aVar.l() == null) {
            com.huawei.hms.support.log.a.b("PushSelfShowLog", "msg is null");
        } else if (!TextUtils.isEmpty(aVar.l()) && aVar.l().contains("##")) {
            builder.setTicker(aVar.l().replace("##", "，"));
            if (!com.huawei.hms.support.api.push.a.d.a.b()) {
                builder.setContentText(aVar.l().replace("##", "，"));
                return;
            }
            builder.setLargeIcon(bitmap);
            builder.setContentTitle(c.a(context, aVar));
            Notification.InboxStyle inboxStyle = new Notification.InboxStyle();
            String[] split = aVar.l().split("##");
            int length = split.length;
            if (length > 4) {
                length = 4;
            }
            if (!TextUtils.isEmpty(aVar.x())) {
                inboxStyle.setBigContentTitle(aVar.x());
                builder.setContentText(aVar.x());
                if (4 == length) {
                    length--;
                }
            }
            for (int i2 = 0; i2 < length; i2++) {
                inboxStyle.addLine(split[i2]);
            }
            if (aVar.v() != null && aVar.v().length > 0) {
                int length2 = aVar.v().length;
                for (int i3 = 0; i3 < length2; i3++) {
                    if (!TextUtils.isEmpty(aVar.v()[i3]) && !TextUtils.isEmpty(aVar.w()[i3])) {
                        builder.addAction(0, aVar.v()[i3], c.a(context, i, aVar.w()[i3]));
                    }
                }
            }
            builder.setStyle(inboxStyle);
        }
    }
}
