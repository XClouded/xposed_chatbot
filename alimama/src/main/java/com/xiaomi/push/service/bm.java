package com.xiaomi.push.service;

import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.push.ee;
import com.xiaomi.push.fn;
import com.xiaomi.push.fq;
import java.util.Map;

class bm extends fn {
    final /* synthetic */ XMPushService a;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    bm(XMPushService xMPushService, Map map, int i, String str, fq fqVar) {
        super(map, i, str, fqVar);
        this.a = xMPushService;
    }

    public byte[] a() {
        try {
            ee.b bVar = new ee.b();
            bVar.a(ba.a().a());
            return bVar.a();
        } catch (Exception e) {
            b.a("getOBBString err: " + e.toString());
            return null;
        }
    }
}
