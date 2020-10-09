package com.huawei.hms.update.e;

import java.io.Serializable;
import java.util.ArrayList;

/* compiled from: UpdateBean */
public class v implements Serializable {
    private boolean a;
    private String b;
    private int c;
    private String d;
    private String e;
    private ArrayList f;
    private boolean g = true;

    private static <T> T a(T t) {
        return t;
    }

    /* access modifiers changed from: package-private */
    public boolean a() {
        return ((Boolean) a(Boolean.valueOf(this.a))).booleanValue();
    }

    public void a(boolean z) {
        this.a = z;
    }

    /* access modifiers changed from: package-private */
    public String b() {
        return (String) a(this.b);
    }

    public void a(String str) {
        this.b = str;
    }

    /* access modifiers changed from: package-private */
    public int c() {
        return ((Integer) a(Integer.valueOf(this.c))).intValue();
    }

    public void a(int i) {
        this.c = i;
    }

    /* access modifiers changed from: package-private */
    public String d() {
        return (String) a(this.d);
    }

    public void b(String str) {
        this.d = str;
    }

    public String e() {
        return (String) a(this.e);
    }

    public void c(String str) {
        this.e = str;
    }

    public ArrayList f() {
        return (ArrayList) a(this.f);
    }

    public void a(ArrayList arrayList) {
        this.f = arrayList;
    }

    public boolean g() {
        return ((Boolean) a(Boolean.valueOf(this.g))).booleanValue();
    }

    public void b(boolean z) {
        this.g = z;
    }
}
