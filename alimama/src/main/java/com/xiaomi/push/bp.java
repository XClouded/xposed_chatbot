package com.xiaomi.push;

import android.content.Context;
import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.push.bw;
import java.lang.ref.WeakReference;

public class bp implements Runnable {
    private String a;

    /* renamed from: a  reason: collision with other field name */
    private WeakReference<Context> f153a;

    public bp(String str, WeakReference<Context> weakReference) {
        this.a = str;
        this.f153a = weakReference;
    }

    public void run() {
        Context context;
        if (this.f153a != null && (context = (Context) this.f153a.get()) != null) {
            if (cc.a(this.a) > bo.f151a) {
                bs a2 = bs.a(this.a);
                br a3 = br.a(this.a);
                a2.a((bw.a) a3);
                a3.a((bw.a) bq.a(context, this.a, 1000));
                bw.a(context).a((bw.a) a2);
                return;
            }
            b.b("=====> do not need clean db");
        }
    }
}
