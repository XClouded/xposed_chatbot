package com.xiaomi.push.service;

import android.text.TextUtils;
import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.push.Cif;
import com.xiaomi.push.hg;
import com.xiaomi.push.hk;
import com.xiaomi.push.ht;
import com.xiaomi.push.ic;
import com.xiaomi.push.iq;
import com.xiaomi.push.service.XMPushService;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class j extends XMPushService.i {
    final /* synthetic */ i a;

    /* renamed from: a  reason: collision with other field name */
    final /* synthetic */ String f910a;

    /* renamed from: a  reason: collision with other field name */
    final /* synthetic */ List f911a;
    final /* synthetic */ String b;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    j(i iVar, int i, String str, List list, String str2) {
        super(i);
        this.a = iVar;
        this.f910a = str;
        this.f911a = list;
        this.b = str2;
    }

    public String a() {
        return "Send tiny data.";
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m604a() {
        String a2 = this.a.a(this.f910a);
        ArrayList<Cif> a3 = be.a(this.f911a, this.f910a, a2, 32768);
        b.a("TinyData LongConnUploader.upload pack notifications " + a3.toString() + "  ts:" + System.currentTimeMillis());
        if (a3 != null) {
            Iterator<Cif> it = a3.iterator();
            while (it.hasNext()) {
                Cif next = it.next();
                next.a("uploadWay", "longXMPushService");
                ic a4 = w.a(this.f910a, a2, next, hg.Notification);
                if (!TextUtils.isEmpty(this.b) && !TextUtils.equals(this.f910a, this.b)) {
                    if (a4.a() == null) {
                        ht htVar = new ht();
                        htVar.a("-1");
                        a4.a(htVar);
                    }
                    a4.a().b("ext_traffic_source_pkg", this.b);
                }
                this.a.a.a(this.f910a, iq.a(a4), true);
            }
            for (hk d : this.f911a) {
                b.a("TinyData LongConnUploader.upload uploaded by com.xiaomi.push.service.TinyDataUploader.  item" + d.d() + "  ts:" + System.currentTimeMillis());
            }
            return;
        }
        b.d("TinyData LongConnUploader.upload Get a null XmPushActionNotification list when TinyDataHelper.pack() in XMPushService.");
    }
}
