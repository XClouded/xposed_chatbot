package com.huawei.hianalytics.e;

import android.text.TextUtils;

public class d {
    private String a;
    private String b;
    private String c;
    private String d;
    private String e;
    private String f;
    private String g;
    private int h = 5;
    private String i;
    private String j;
    private boolean k;
    private String l;
    private String m;
    private String n;
    private String o;
    private String p;
    private boolean q = true;

    public int a() {
        return this.h;
    }

    public void a(int i2) {
        this.h = i2;
    }

    public void a(String str) {
        this.d = str;
    }

    public void a(boolean z) {
        this.k = z;
    }

    public int b() {
        return this.h * 2;
    }

    public void b(String str) {
        this.o = str;
    }

    public void b(boolean z) {
        this.q = z;
    }

    public String c() {
        return this.d;
    }

    public void c(String str) {
        this.a = str;
    }

    public String d() {
        return this.o;
    }

    public void d(String str) {
        this.b = str;
    }

    public String e() {
        return this.a;
    }

    public void e(String str) {
        this.c = str;
    }

    public String f() {
        return this.b;
    }

    public void f(String str) {
        this.e = str;
    }

    public String g() {
        return this.c;
    }

    public void g(String str) {
        this.f = str;
    }

    public String h() {
        return this.e;
    }

    public void h(String str) {
        this.g = str;
    }

    public String i() {
        return this.f;
    }

    public void i(String str) {
        this.i = str;
    }

    public String j() {
        return TextUtils.isEmpty(this.g) ? this.f : this.g;
    }

    public void j(String str) {
        this.j = str;
    }

    public String k() {
        return this.i;
    }

    public void k(String str) {
        this.l = str;
    }

    public String l() {
        return this.j;
    }

    public void l(String str) {
        this.m = str;
    }

    public void m(String str) {
        this.n = str;
    }

    public boolean m() {
        return this.k;
    }

    public String n() {
        return this.l;
    }

    public void n(String str) {
        this.p = str;
    }

    public String o() {
        return this.m;
    }

    public String p() {
        return this.n;
    }

    public String q() {
        return this.p;
    }

    public boolean r() {
        return this.q;
    }
}
