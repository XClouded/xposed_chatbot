package com.xiaomi.mipush.sdk;

import android.content.Context;
import android.text.TextUtils;
import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.push.Cif;
import com.xiaomi.push.av;
import com.xiaomi.push.h;
import com.xiaomi.push.hg;
import com.xiaomi.push.hv;
import com.xiaomi.push.hw;
import com.xiaomi.push.hx;
import com.xiaomi.push.ib;
import com.xiaomi.push.ic;
import com.xiaomi.push.ih;
import com.xiaomi.push.ii;
import com.xiaomi.push.ij;
import com.xiaomi.push.il;
import com.xiaomi.push.in;
import com.xiaomi.push.ip;
import com.xiaomi.push.iq;
import com.xiaomi.push.ir;
import java.nio.ByteBuffer;

public class ar {
    protected static <T extends ir<T, ?>> ic a(Context context, T t, hg hgVar) {
        return a(context, t, hgVar, !hgVar.equals(hg.Registration), context.getPackageName(), d.a(context).a());
    }

    protected static <T extends ir<T, ?>> ic a(Context context, T t, hg hgVar, boolean z, String str, String str2) {
        byte[] bArr;
        String str3;
        byte[] a = iq.a(t);
        if (a == null) {
            str3 = "invoke convertThriftObjectToBytes method, return null.";
        } else {
            ic icVar = new ic();
            if (z) {
                String d = d.a(context).d();
                if (TextUtils.isEmpty(d)) {
                    str3 = "regSecret is empty, return null";
                } else {
                    try {
                        bArr = h.b(av.a(d), a);
                    } catch (Exception unused) {
                        b.d("encryption error. ");
                    }
                    hv hvVar = new hv();
                    hvVar.f533a = 5;
                    hvVar.f534a = "fakeid";
                    icVar.a(hvVar);
                    icVar.a(ByteBuffer.wrap(bArr));
                    icVar.a(hgVar);
                    icVar.b(true);
                    icVar.b(str);
                    icVar.a(z);
                    icVar.a(str2);
                    return icVar;
                }
            }
            bArr = a;
            hv hvVar2 = new hv();
            hvVar2.f533a = 5;
            hvVar2.f534a = "fakeid";
            icVar.a(hvVar2);
            icVar.a(ByteBuffer.wrap(bArr));
            icVar.a(hgVar);
            icVar.b(true);
            icVar.b(str);
            icVar.a(z);
            icVar.a(str2);
            return icVar;
        }
        b.a(str3);
        return null;
    }

    public static ir a(Context context, ic icVar) {
        byte[] bArr;
        if (icVar.b()) {
            try {
                bArr = h.a(av.a(d.a(context).d()), icVar.a());
            } catch (Exception e) {
                throw new v("the aes decrypt failed.", e);
            }
        } else {
            bArr = icVar.a();
        }
        ir a = a(icVar.a(), icVar.f614b);
        if (a != null) {
            iq.a(a, bArr);
        }
        return a;
    }

    private static ir a(hg hgVar, boolean z) {
        switch (as.a[hgVar.ordinal()]) {
            case 1:
                return new ih();
            case 2:
                return new in();
            case 3:
                return new il();
            case 4:
                return new ip();
            case 5:
                return new ij();
            case 6:
                return new hw();
            case 7:
                return new ib();
            case 8:
                return new ii();
            case 9:
                if (z) {
                    return new Cif();
                }
                hx hxVar = new hx();
                hxVar.a(true);
                return hxVar;
            case 10:
                return new ib();
            default:
                return null;
        }
    }
}
