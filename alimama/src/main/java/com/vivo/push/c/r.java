package com.vivo.push.c;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import com.vivo.push.b.aa;
import com.vivo.push.b.j;
import com.vivo.push.b.s;
import com.vivo.push.cache.ClientConfigManagerImpl;
import com.vivo.push.model.InsideNotificationItem;
import com.vivo.push.p;
import com.vivo.push.util.d;
import com.vivo.push.util.z;
import com.vivo.push.w;
import com.vivo.push.y;
import java.util.HashMap;

/* compiled from: OnNotificationArrivedReceiveTask */
final class r extends ab {
    r(y yVar) {
        super(yVar);
    }

    /* access modifiers changed from: protected */
    public final void a(y yVar) {
        NotificationManager notificationManager;
        boolean isEnablePush = ClientConfigManagerImpl.getInstance(this.a).isEnablePush();
        s sVar = (s) yVar;
        if (!com.vivo.push.util.s.c(this.a, this.a.getPackageName())) {
            aa aaVar = new aa(2101);
            HashMap hashMap = new HashMap();
            hashMap.put("messageID", String.valueOf(sVar.f()));
            String b = z.b(this.a, this.a.getPackageName());
            if (!TextUtils.isEmpty(b)) {
                hashMap.put("remoteAppId", b);
            }
            aaVar.a(hashMap);
            p.a().a((y) aaVar);
            return;
        }
        p.a().a((y) new j(String.valueOf(sVar.f())));
        com.vivo.push.util.p.d("OnNotificationArrivedTask", "PushMessageReceiver " + this.a.getPackageName() + " isEnablePush :" + isEnablePush);
        if (!isEnablePush) {
            aa aaVar2 = new aa(1020);
            HashMap hashMap2 = new HashMap();
            hashMap2.put("messageID", String.valueOf(sVar.f()));
            String b2 = z.b(this.a, this.a.getPackageName());
            if (!TextUtils.isEmpty(b2)) {
                hashMap2.put("remoteAppId", b2);
            }
            aaVar2.a(hashMap2);
            p.a().a((y) aaVar2);
        } else if (p.a().g() && !a(z.d(this.a), sVar.e(), sVar.i())) {
            aa aaVar3 = new aa(1021);
            HashMap hashMap3 = new HashMap();
            hashMap3.put("messageID", String.valueOf(sVar.f()));
            String b3 = z.b(this.a, this.a.getPackageName());
            if (!TextUtils.isEmpty(b3)) {
                hashMap3.put("remoteAppId", b3);
            }
            aaVar3.a(hashMap3);
            p.a().a((y) aaVar3);
        } else if (Build.VERSION.SDK_INT < 24 || (notificationManager = (NotificationManager) this.a.getSystemService("notification")) == null || notificationManager.areNotificationsEnabled()) {
            InsideNotificationItem d = sVar.d();
            if (d != null) {
                int targetType = d.getTargetType();
                String tragetContent = d.getTragetContent();
                com.vivo.push.util.p.d("OnNotificationArrivedTask", "tragetType is " + targetType + " ; target is " + tragetContent);
                w.b(new s(this, d, sVar));
                return;
            }
            com.vivo.push.util.p.a("OnNotificationArrivedTask", "notify is null");
            Context context = this.a;
            com.vivo.push.util.p.c(context, "通知内容为空，" + sVar.f());
            d.a(this.a, sVar.f(), 1027);
        } else {
            com.vivo.push.util.p.b("OnNotificationArrivedTask", "pkg name : " + this.a.getPackageName() + " notify switch is false");
            com.vivo.push.util.p.b(this.a, "通知开关关闭，导致通知无法展示，请到设置页打开应用通知开关");
            aa aaVar4 = new aa(2104);
            HashMap hashMap4 = new HashMap();
            hashMap4.put("messageID", String.valueOf(sVar.f()));
            String b4 = z.b(this.a, this.a.getPackageName());
            if (!TextUtils.isEmpty(b4)) {
                hashMap4.put("remoteAppId", b4);
            }
            aaVar4.a(hashMap4);
            p.a().a((y) aaVar4);
        }
    }
}
