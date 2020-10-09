package com.xiaomi.push;

import android.util.Log;
import com.xiaomi.push.al;

class di extends al.b {
    final /* synthetic */ dh a;

    di(dh dhVar) {
        this.a = dhVar;
    }

    public void b() {
        if (!dh.a().isEmpty()) {
            try {
                if (!aa.d()) {
                    Log.w(dh.a(this.a), "SDCard is unavailable.");
                } else {
                    dh.a(this.a);
                }
            } catch (Exception e) {
                Log.e(dh.a(this.a), "", e);
            }
        }
    }
}
