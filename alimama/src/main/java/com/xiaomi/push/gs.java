package com.xiaomi.push;

import android.content.Context;
import com.xiaomi.push.al;
import java.util.ArrayList;

final class gs extends al.b {
    final /* synthetic */ Context a;

    gs(Context context) {
        this.a = context;
    }

    public void b() {
        ArrayList arrayList;
        synchronized (gr.a()) {
            arrayList = new ArrayList(gr.a());
            gr.a().clear();
        }
        gr.b(this.a, arrayList);
    }
}
