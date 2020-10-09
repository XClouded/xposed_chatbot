package com.vivo.push.b;

import com.vivo.push.a;
import com.vivo.push.y;

/* compiled from: OnReceiveCommand */
public class u extends y {
    private String a = null;
    private int b = 0;

    public String toString() {
        return "OnReceiveCommand";
    }

    public u(int i) {
        super(i);
    }

    public final String g() {
        return this.a;
    }

    public final int h() {
        return this.b;
    }

    /* access modifiers changed from: protected */
    public void c(a aVar) {
        aVar.a("req_id", this.a);
        aVar.a("status_msg_code", this.b);
    }

    /* access modifiers changed from: protected */
    public void d(a aVar) {
        this.a = aVar.a("req_id");
        this.b = aVar.b("status_msg_code", this.b);
    }
}
