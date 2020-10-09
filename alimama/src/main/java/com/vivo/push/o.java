package com.vivo.push;

import android.content.Intent;
import com.vivo.push.b.k;
import com.vivo.push.b.l;
import com.vivo.push.b.m;
import com.vivo.push.b.n;
import com.vivo.push.b.p;
import com.vivo.push.b.q;
import com.vivo.push.b.r;
import com.vivo.push.b.s;
import com.vivo.push.b.t;
import com.vivo.push.b.v;
import com.vivo.push.b.w;
import com.vivo.push.c.ab;
import com.vivo.push.c.ai;

/* compiled from: PushClientFactory */
public final class o implements IPushClientFactory {
    private ai a = new ai();

    public final y createReceiverCommand(Intent intent) {
        y yVar;
        y yVar2;
        int intExtra = intent.getIntExtra("command", -1);
        if (intExtra < 0) {
            intExtra = intent.getIntExtra("method", -1);
        }
        if (intExtra == 20) {
            yVar = new w();
        } else if (intExtra != 2016) {
            switch (intExtra) {
                case 1:
                case 2:
                    yVar2 = new v(intExtra);
                    break;
                case 3:
                    yVar = new q();
                    break;
                case 4:
                    yVar = new s();
                    break;
                case 5:
                    yVar = new r();
                    break;
                case 6:
                    yVar = new t();
                    break;
                case 7:
                    yVar = new p();
                    break;
                case 8:
                    yVar = new com.vivo.push.b.o();
                    break;
                case 9:
                    yVar = new m();
                    break;
                case 10:
                case 11:
                    yVar2 = new k(intExtra);
                    break;
                case 12:
                    yVar = new l();
                    break;
                default:
                    yVar = null;
                    break;
            }
            yVar = yVar2;
        } else {
            yVar = new n();
        }
        if (yVar != null) {
            a a2 = a.a(intent);
            if (a2 == null) {
                com.vivo.push.util.p.b("PushCommand", "bundleWapper is null");
            } else {
                yVar.b(a2);
            }
        }
        return yVar;
    }

    public final v createTask(y yVar) {
        return ai.a(yVar);
    }

    public final ab createReceiveTask(y yVar) {
        return ai.b(yVar);
    }
}
