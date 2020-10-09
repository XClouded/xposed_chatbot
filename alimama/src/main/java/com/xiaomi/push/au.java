package com.xiaomi.push;

import java.util.LinkedList;

public class au {
    private LinkedList<a> a = new LinkedList<>();

    public static class a {
        /* access modifiers changed from: private */
        public static final au a = new au();

        /* renamed from: a  reason: collision with other field name */
        public int f126a;

        /* renamed from: a  reason: collision with other field name */
        public Object f127a;

        /* renamed from: a  reason: collision with other field name */
        public String f128a;

        a(int i, Object obj) {
            this.f126a = i;
            this.f127a = obj;
        }
    }

    public static au a() {
        return a.a;
    }

    /* renamed from: a  reason: collision with other method in class */
    private void m94a() {
        if (this.a.size() > 100) {
            this.a.removeFirst();
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public synchronized int m95a() {
        return this.a.size();
    }

    /* renamed from: a  reason: collision with other method in class */
    public synchronized LinkedList<a> m96a() {
        LinkedList<a> linkedList;
        linkedList = this.a;
        this.a = new LinkedList<>();
        return linkedList;
    }

    public synchronized void a(Object obj) {
        this.a.add(new a(0, obj));
        a();
    }
}
