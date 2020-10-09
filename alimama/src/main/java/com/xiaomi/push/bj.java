package com.xiaomi.push;

import com.taobao.weex.el.parse.Operators;
import com.xiaomi.channel.commonutils.logger.b;
import java.util.Date;

class bj implements fp {
    final /* synthetic */ bi a;

    bj(bi biVar) {
        this.a = biVar;
    }

    public void a(fm fmVar) {
        b.c("[Slim] " + bi.a(this.a).format(new Date()) + " Connection reconnected (" + bi.a(this.a).hashCode() + Operators.BRACKET_END_STR);
    }

    public void a(fm fmVar, int i, Exception exc) {
        b.c("[Slim] " + bi.a(this.a).format(new Date()) + " Connection closed (" + bi.a(this.a).hashCode() + Operators.BRACKET_END_STR);
    }

    public void a(fm fmVar, Exception exc) {
        b.c("[Slim] " + bi.a(this.a).format(new Date()) + " Reconnection failed due to an exception (" + bi.a(this.a).hashCode() + Operators.BRACKET_END_STR);
        exc.printStackTrace();
    }

    public void b(fm fmVar) {
        b.c("[Slim] " + bi.a(this.a).format(new Date()) + " Connection started (" + bi.a(this.a).hashCode() + Operators.BRACKET_END_STR);
    }
}
