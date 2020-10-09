package com.vivo.push.b;

import com.taobao.weex.el.parse.Operators;
import com.vivo.push.a;
import com.vivo.push.y;
import java.io.Serializable;
import java.util.HashMap;

/* compiled from: ReporterCommand */
public final class aa extends y {
    private HashMap<String, String> a;
    private long b;

    public aa() {
        super(2012);
    }

    public aa(long j) {
        this();
        this.b = j;
    }

    public final void a(HashMap<String, String> hashMap) {
        this.a = hashMap;
    }

    public final void c(a aVar) {
        aVar.a("ReporterCommand.EXTRA_PARAMS", (Serializable) this.a);
        aVar.a("ReporterCommand.EXTRA_REPORTER_TYPE", this.b);
    }

    public final void d(a aVar) {
        this.a = (HashMap) aVar.c("ReporterCommand.EXTRA_PARAMS");
        this.b = aVar.b("ReporterCommand.EXTRA_REPORTER_TYPE", this.b);
    }

    public final String toString() {
        return "ReporterCommand（" + this.b + Operators.BRACKET_END_STR;
    }
}
