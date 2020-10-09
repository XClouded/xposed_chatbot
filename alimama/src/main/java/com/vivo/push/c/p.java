package com.vivo.push.c;

import android.text.TextUtils;
import com.vivo.push.b.aa;
import com.vivo.push.b.j;
import com.vivo.push.b.q;
import com.vivo.push.cache.ClientConfigManagerImpl;
import com.vivo.push.model.UnvarnishedMessage;
import com.vivo.push.util.z;
import com.vivo.push.w;
import com.vivo.push.y;
import java.util.HashMap;

/* compiled from: OnMessageReceiveTask */
final class p extends ab {
    p(y yVar) {
        super(yVar);
    }

    /* access modifiers changed from: protected */
    public final void a(y yVar) {
        q qVar = (q) yVar;
        com.vivo.push.p.a().a((y) new j(String.valueOf(qVar.f())));
        if (!ClientConfigManagerImpl.getInstance(this.a).isEnablePush()) {
            com.vivo.push.util.p.d("OnMessageTask", "command  " + yVar + " is ignore by disable push ");
            aa aaVar = new aa(1020);
            HashMap hashMap = new HashMap();
            hashMap.put("messageID", String.valueOf(qVar.f()));
            String b = z.b(this.a, this.a.getPackageName());
            if (!TextUtils.isEmpty(b)) {
                hashMap.put("remoteAppId", b);
            }
            aaVar.a(hashMap);
            com.vivo.push.p.a().a((y) aaVar);
        } else if (!com.vivo.push.p.a().g() || a(z.d(this.a), qVar.d(), qVar.i())) {
            UnvarnishedMessage e = qVar.e();
            if (e != null) {
                int targetType = e.getTargetType();
                String tragetContent = e.getTragetContent();
                com.vivo.push.util.p.d("OnMessageTask", "tragetType is " + targetType + " ; target is " + tragetContent);
                w.b(new q(this, e));
                return;
            }
            com.vivo.push.util.p.a("OnMessageTask", " message is null");
        } else {
            aa aaVar2 = new aa(1021);
            HashMap hashMap2 = new HashMap();
            hashMap2.put("messageID", String.valueOf(qVar.f()));
            String b2 = z.b(this.a, this.a.getPackageName());
            if (!TextUtils.isEmpty(b2)) {
                hashMap2.put("remoteAppId", b2);
            }
            aaVar2.a(hashMap2);
            com.vivo.push.p.a().a((y) aaVar2);
        }
    }
}
