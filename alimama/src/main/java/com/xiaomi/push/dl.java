package com.xiaomi.push;

public class dl {
    private static volatile dl a;

    /* renamed from: a  reason: collision with other field name */
    private dk f236a;

    public static dl a() {
        if (a == null) {
            synchronized (dl.class) {
                if (a == null) {
                    a = new dl();
                }
            }
        }
        return a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public dk m176a() {
        return this.f236a;
    }

    public void a(dk dkVar) {
        this.f236a = dkVar;
    }
}
