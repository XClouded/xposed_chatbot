package com.xiaomi.push;

public class jn extends iw {
    protected int a = 0;

    public jn() {
    }

    public jn(int i) {
        this.a = i;
    }

    public jn(int i, String str) {
        super(str);
        this.a = i;
    }

    public jn(int i, Throwable th) {
        super(th);
        this.a = i;
    }

    public jn(String str) {
        super(str);
    }
}
