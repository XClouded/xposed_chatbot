package com.xiaomi.push;

import android.content.Context;
import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.clientreport.processor.d;

public class bd implements Runnable {
    private Context a;

    /* renamed from: a  reason: collision with other field name */
    private d f136a;

    public bd(Context context, d dVar) {
        this.a = context;
        this.f136a = dVar;
    }

    public void run() {
        try {
            if (this.f136a != null) {
                this.f136a.b();
            }
        } catch (Exception e) {
            b.a((Throwable) e);
        }
    }
}
