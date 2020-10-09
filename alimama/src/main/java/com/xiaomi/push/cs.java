package com.xiaomi.push;

import java.net.InetSocketAddress;

public final class cs {
    private int a;

    /* renamed from: a  reason: collision with other field name */
    private String f196a;

    public cs(String str, int i) {
        this.f196a = str;
        this.a = i;
    }

    public static cs a(String str, int i) {
        String str2;
        int lastIndexOf = str.lastIndexOf(":");
        if (lastIndexOf != -1) {
            str2 = str.substring(0, lastIndexOf);
            try {
                int parseInt = Integer.parseInt(str.substring(lastIndexOf + 1));
                if (parseInt > 0) {
                    i = parseInt;
                }
            } catch (NumberFormatException unused) {
            }
        } else {
            str2 = str;
        }
        return new cs(str2, i);
    }

    /* renamed from: a  reason: collision with other method in class */
    public static InetSocketAddress m160a(String str, int i) {
        cs a2 = a(str, i);
        return new InetSocketAddress(a2.a(), a2.a());
    }

    public int a() {
        return this.a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public String m161a() {
        return this.f196a;
    }

    public String toString() {
        if (this.a <= 0) {
            return this.f196a;
        }
        return this.f196a + ":" + this.a;
    }
}
