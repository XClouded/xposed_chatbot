package com.xiaomi.push;

import android.content.Context;
import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.push.iy;
import com.xiaomi.push.ji;

public class iq {
    public static short a(Context context, ic icVar) {
        int i = 0;
        int a = g.a(context, icVar.f613b).a() + 0 + (ah.b(context) ? 4 : 0);
        if (ah.a(context)) {
            i = 8;
        }
        return (short) (a + i);
    }

    public static <T extends ir<T, ?>> void a(T t, byte[] bArr) {
        if (bArr != null) {
            new iv(new ji.a(true, true, bArr.length)).a(t, bArr);
            return;
        }
        throw new iw("the message byte is empty.");
    }

    public static <T extends ir<T, ?>> byte[] a(T t) {
        if (t == null) {
            return null;
        }
        try {
            return new ix(new iy.a()).a(t);
        } catch (iw e) {
            b.a("convertThriftObjectToBytes catch TException.", (Throwable) e);
            return null;
        }
    }
}
