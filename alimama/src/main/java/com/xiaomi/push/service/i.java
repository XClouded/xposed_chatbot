package com.xiaomi.push.service;

import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.push.hf;
import com.xiaomi.push.hk;
import com.xiaomi.push.service.XMPushService;
import java.util.List;

public class i implements hf {
    /* access modifiers changed from: private */
    public final XMPushService a;

    public i(XMPushService xMPushService) {
        this.a = xMPushService;
    }

    /* access modifiers changed from: private */
    public String a(String str) {
        return "com.xiaomi.xmsf".equals(str) ? "1000271" : this.a.getSharedPreferences("pref_registered_pkg_names", 0).getString(str, (String) null);
    }

    public void a(List<hk> list, String str, String str2) {
        b.a("TinyData LongConnUploader.upload items size:" + list.size() + "  ts:" + System.currentTimeMillis());
        this.a.a((XMPushService.i) new j(this, 4, str, list, str2));
    }
}
