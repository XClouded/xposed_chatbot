package com.vivo.push.c;

import android.content.Context;
import android.text.TextUtils;
import com.taobao.alivfssdk.utils.AVFSCacheConstants;
import com.vivo.push.b.aa;
import com.vivo.push.b.w;
import com.vivo.push.p;
import com.vivo.push.util.NotifyAdapterUtil;
import com.vivo.push.util.d;
import com.vivo.push.util.z;
import com.vivo.push.y;
import java.util.HashMap;

/* compiled from: OnUndoMsgReceiveTask */
final class ah extends ab {
    ah(y yVar) {
        super(yVar);
    }

    /* access modifiers changed from: protected */
    public final void a(y yVar) {
        w wVar = (w) yVar;
        if (!p.a().g() || a(z.d(this.a), wVar.e(), wVar.i())) {
            boolean repealNotifyById = NotifyAdapterUtil.repealNotifyById(this.a, (int) wVar.d());
            com.vivo.push.util.p.d("OnUndoMsgTask", "undo message " + wVar.d() + AVFSCacheConstants.COMMA_SEP + repealNotifyById);
            if (repealNotifyById) {
                Context context = this.a;
                com.vivo.push.util.p.b(context, "回收client通知成功, 上报埋点 1031, messageId = " + wVar.d());
                d.a(this.a, wVar.d(), 1031);
                return;
            }
            com.vivo.push.util.p.d("OnUndoMsgTask", "undo message fail，messageId = " + wVar.d());
            Context context2 = this.a;
            com.vivo.push.util.p.c(context2, "回收client通知失败，messageId = " + wVar.d());
            return;
        }
        com.vivo.push.util.p.d("OnUndoMsgTask", " vertify msg is error ");
        aa aaVar = new aa(1021);
        HashMap hashMap = new HashMap();
        hashMap.put("messageID", String.valueOf(wVar.f()));
        String b = z.b(this.a, this.a.getPackageName());
        if (!TextUtils.isEmpty(b)) {
            hashMap.put("remoteAppId", b);
        }
        aaVar.a(hashMap);
        p.a().a((y) aaVar);
    }
}
