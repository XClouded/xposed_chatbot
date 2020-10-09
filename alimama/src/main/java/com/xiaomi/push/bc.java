package com.xiaomi.push;

import android.content.Context;
import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.clientreport.processor.IEventProcessor;
import com.xiaomi.clientreport.processor.IPerfProcessor;
import com.xiaomi.clientreport.processor.c;

public class bc implements Runnable {
    private Context a;

    /* renamed from: a  reason: collision with other field name */
    private c f135a;

    public void a(Context context) {
        this.a = context;
    }

    public void a(c cVar) {
        this.f135a = cVar;
    }

    public void run() {
        bh a2;
        String str;
        String str2;
        long currentTimeMillis;
        try {
            if (this.f135a != null) {
                this.f135a.a();
            }
            b.c("begin read and send perf / event");
            if (this.f135a instanceof IEventProcessor) {
                a2 = bh.a(this.a);
                str = "sp_client_report_status";
                str2 = "event_last_upload_time";
                currentTimeMillis = System.currentTimeMillis();
            } else if (this.f135a instanceof IPerfProcessor) {
                a2 = bh.a(this.a);
                str = "sp_client_report_status";
                str2 = "perf_last_upload_time";
                currentTimeMillis = System.currentTimeMillis();
            } else {
                return;
            }
            a2.a(str, str2, currentTimeMillis);
        } catch (Exception e) {
            b.a((Throwable) e);
        }
    }
}
