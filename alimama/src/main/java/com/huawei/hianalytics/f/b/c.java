package com.huawei.hianalytics.f.b;

public abstract class c {
    protected String a;
    protected String b;
    protected String c;
    protected String d;
    protected String e;
    protected String f;
    protected String g;

    public void a(c cVar) {
        if (cVar != null) {
            cVar.c(e());
            cVar.a(c());
            cVar.b(d());
            cVar.e(g());
            cVar.d(f());
            cVar.f(h());
            cVar.g(i());
        }
    }

    public void a(String str) {
        this.f = str;
    }

    public void b(String str) {
        this.g = str;
    }

    public String c() {
        return this.f;
    }

    public void c(String str) {
        this.a = str;
    }

    public String d() {
        return this.g;
    }

    public void d(String str) {
        this.b = str;
    }

    public String e() {
        return this.a;
    }

    public void e(String str) {
        this.d = str;
    }

    public String f() {
        return this.b;
    }

    public void f(String str) {
        this.c = str;
    }

    public String g() {
        return this.d;
    }

    public void g(String str) {
        this.e = str;
    }

    public String h() {
        return this.c;
    }

    public String i() {
        return this.e;
    }
}
