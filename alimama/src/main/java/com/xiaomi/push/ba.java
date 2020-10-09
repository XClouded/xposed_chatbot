package com.xiaomi.push;

import android.content.Context;
import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.clientreport.manager.a;
import com.xiaomi.push.ai;

public class ba extends ai.a {
    private Context a;

    public ba(Context context) {
        this.a = context;
    }

    private boolean a() {
        return a.a(this.a).a().isEventUploadSwitchOpen();
    }

    /* renamed from: a  reason: collision with other method in class */
    public int m114a() {
        return 100886;
    }

    public void run() {
        try {
            if (a()) {
                b.c(this.a.getPackageName() + " begin upload event");
                a.a(this.a).b();
            }
        } catch (Exception e) {
            b.a((Throwable) e);
        }
    }
}
