package com.vivo.push.b;

import com.vivo.push.a;
import java.util.ArrayList;
import java.util.List;

/* compiled from: OnTagsReceiveCommand */
public final class v extends u {
    private ArrayList<String> a = null;
    private ArrayList<String> b = null;

    public final String toString() {
        return "OnSetTagsCommand";
    }

    public v(int i) {
        super(i);
    }

    public final ArrayList<String> d() {
        return this.a;
    }

    public final List<String> e() {
        return this.b;
    }

    /* access modifiers changed from: protected */
    public final void c(a aVar) {
        super.c(aVar);
        aVar.a("content", this.a);
        aVar.a("error_msg", this.b);
    }

    /* access modifiers changed from: protected */
    public final void d(a aVar) {
        super.d(aVar);
        this.a = aVar.b("content");
        this.b = aVar.b("error_msg");
    }
}
