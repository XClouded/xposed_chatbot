package com.xiaomi.mipush.sdk;

import android.content.Context;
import com.xiaomi.push.Cif;
import com.xiaomi.push.hg;
import com.xiaomi.push.hq;
import com.xiaomi.push.ht;
import com.xiaomi.push.service.aj;

public class MiPushClient4VR {
    public static void uploadData(Context context, String str) {
        Cif ifVar = new Cif();
        ifVar.c(hq.VRUpload.f485a);
        ifVar.b(d.a(context).a());
        ifVar.d(context.getPackageName());
        ifVar.a("data", str);
        ifVar.a(aj.a());
        ay.a(context).a(ifVar, hg.Notification, (ht) null);
    }
}
