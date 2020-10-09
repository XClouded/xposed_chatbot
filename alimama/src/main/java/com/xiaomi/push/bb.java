package com.xiaomi.push;

import android.content.Context;
import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.clientreport.manager.a;
import com.xiaomi.push.ai;

public class bb extends ai.a {
    private Context a;

    public bb(Context context) {
        this.a = context;
    }

    private boolean a() {
        return a.a(this.a).a().isPerfUploadSwitchOpen();
    }

    /* renamed from: a  reason: collision with other method in class */
    public int m115a() {
        return 100887;
    }

    public void run() {
        try {
            if (a()) {
                a.a(this.a).c();
                b.c(this.a.getPackageName() + "perf  begin upload");
            }
        } catch (Exception e) {
            b.a((Throwable) e);
        }
    }
}
