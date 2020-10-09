package com.vivo.push.b;

import android.text.TextUtils;
import com.vivo.push.a;
import com.vivo.push.model.InsideNotificationItem;
import com.vivo.push.util.q;
import com.vivo.push.y;
import com.xiaomi.mipush.sdk.Constants;

/* compiled from: OnNotificationClickReceiveCommand */
public final class r extends y {
    private String a;
    private long b;
    private InsideNotificationItem c;

    public final String toString() {
        return "OnNotificationClickCommand";
    }

    public r(String str, long j, InsideNotificationItem insideNotificationItem) {
        super(5);
        this.a = str;
        this.b = j;
        this.c = insideNotificationItem;
    }

    public r() {
        super(5);
    }

    public final String d() {
        return this.a;
    }

    public final long e() {
        return this.b;
    }

    public final InsideNotificationItem f() {
        return this.c;
    }

    /* access modifiers changed from: protected */
    public final void c(a aVar) {
        aVar.a(Constants.PACKAGE_NAME, this.a);
        aVar.a("notify_id", this.b);
        aVar.a("notification_v1", q.b(this.c));
    }

    /* access modifiers changed from: protected */
    public final void d(a aVar) {
        this.a = aVar.a(Constants.PACKAGE_NAME);
        this.b = aVar.b("notify_id", -1);
        String a2 = aVar.a("notification_v1");
        if (!TextUtils.isEmpty(a2)) {
            this.c = q.a(a2);
        }
        if (this.c != null) {
            this.c.setMsgId(this.b);
        }
    }
}
