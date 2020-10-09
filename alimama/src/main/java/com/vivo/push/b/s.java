package com.vivo.push.b;

import android.text.TextUtils;
import com.vivo.push.a;
import com.vivo.push.model.InsideNotificationItem;
import com.vivo.push.util.q;

/* compiled from: OnNotifyArrivedReceiveCommand */
public final class s extends x {
    protected InsideNotificationItem a;
    private String b;

    public final String toString() {
        return "OnNotifyArrivedCommand";
    }

    public s() {
        super(4);
    }

    public final InsideNotificationItem d() {
        return this.a;
    }

    /* access modifiers changed from: protected */
    public final void c(a aVar) {
        super.c(aVar);
        this.b = q.b(this.a);
        aVar.a("notification_v1", this.b);
    }

    /* access modifiers changed from: protected */
    public final void d(a aVar) {
        super.d(aVar);
        this.b = aVar.a("notification_v1");
        if (!TextUtils.isEmpty(this.b)) {
            this.a = q.a(this.b);
            if (this.a != null) {
                this.a.setMsgId(f());
            }
        }
    }

    public final String e() {
        if (!TextUtils.isEmpty(this.b)) {
            return this.b;
        }
        if (this.a == null) {
            return null;
        }
        return q.b(this.a);
    }
}
