package com.vivo.push;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.vivo.push.util.p;

/* compiled from: PushClientThread */
final class x extends Handler {
    x(Looper looper) {
        super(looper);
    }

    public final void handleMessage(Message message) {
        Object obj = message.obj;
        if (obj instanceof v) {
            v vVar = (v) obj;
            p.c("PushClientThread", "PushClientThread-handleMessage, task = " + vVar);
            vVar.run();
        }
    }
}
