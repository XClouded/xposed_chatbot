package com.huawei.hms.update.a.a;

/* compiled from: UpdateInfo */
public class c {
    public int a = 0;
    public String b = "";
    public String c = "";
    public int d = 0;
    public String e = "";

    public c() {
    }

    public c(String str, int i, String str2, int i2, String str3) {
        this.b = str;
        this.a = i;
        this.c = str2;
        this.d = i2;
        this.e = str3;
    }

    public boolean a() {
        return this.a > 0 && this.d > 0 && this.c != null && !this.c.isEmpty();
    }
}
