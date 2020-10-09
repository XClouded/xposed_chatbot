package com.xiaomi.push;

import com.taobao.weex.el.parse.Operators;

public class iz {
    public final byte a;

    /* renamed from: a  reason: collision with other field name */
    public final String f783a;

    /* renamed from: a  reason: collision with other field name */
    public final short f784a;

    public iz() {
        this("", (byte) 0, 0);
    }

    public iz(String str, byte b, short s) {
        this.f783a = str;
        this.a = b;
        this.f784a = s;
    }

    public String toString() {
        return "<TField name:'" + this.f783a + "' type:" + this.a + " field-id:" + this.f784a + Operators.G;
    }
}
