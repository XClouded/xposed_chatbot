package com.xiaomi.push;

import android.content.Context;
import com.xiaomi.clientreport.data.a;
import com.xiaomi.clientreport.processor.d;

public class az implements Runnable {
    private Context a;

    /* renamed from: a  reason: collision with other field name */
    private a f131a;

    /* renamed from: a  reason: collision with other field name */
    private d f132a;

    public az(Context context, a aVar, d dVar) {
        this.a = context;
        this.f131a = aVar;
        this.f132a = dVar;
    }

    public void run() {
        if (this.f132a != null) {
            this.f132a.a(this.f131a);
        }
    }
}
