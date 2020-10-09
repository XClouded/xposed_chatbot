package com.vivo.push;

import android.os.Handler;
import android.os.Message;
import com.taobao.weex.el.parse.Operators;
import com.vivo.push.util.p;

/* compiled from: IPCManager */
final class c implements Handler.Callback {
    final /* synthetic */ b a;

    c(b bVar) {
        this.a = bVar;
    }

    public final boolean handleMessage(Message message) {
        if (message == null) {
            p.a("AidlManager", "handleMessage error : msg is null");
            return false;
        }
        switch (message.what) {
            case 1:
                p.a("AidlManager", "In connect, bind core service time out");
                if (this.a.f.get() != 2) {
                    return true;
                }
                this.a.a(1);
                return true;
            case 2:
                if (this.a.f.get() == 4) {
                    this.a.e();
                }
                this.a.a(1);
                return true;
            default:
                p.b("AidlManager", "unknow msg what [" + message.what + Operators.ARRAY_END_STR);
                return true;
        }
    }
}
