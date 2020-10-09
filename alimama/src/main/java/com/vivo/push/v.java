package com.vivo.push;

import android.content.Context;
import com.taobao.weex.el.parse.Operators;
import com.vivo.push.b.p;

/* compiled from: PushClientTask */
public abstract class v implements Runnable {
    /* access modifiers changed from: protected */
    public Context a;
    private int b = -1;
    private y c;

    /* access modifiers changed from: protected */
    public abstract void a(y yVar);

    public v(y yVar) {
        this.c = yVar;
        this.b = yVar.b();
        if (this.b >= 0) {
            this.a = p.a().h();
            return;
        }
        throw new IllegalArgumentException("PushTask need a > 0 task id.");
    }

    public final int a() {
        return this.b;
    }

    public final void run() {
        if (this.a != null && !(this.c instanceof p)) {
            Context context = this.a;
            com.vivo.push.util.p.a(context, "[执行指令]" + this.c);
        }
        a(this.c);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(Operators.BLOCK_START_STR);
        sb.append(this.c == null ? "[null]" : this.c.toString());
        sb.append("}");
        return sb.toString();
    }
}
