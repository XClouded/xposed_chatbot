package com.xiaomi.push;

public final class jl extends jm {
    private int a;

    /* renamed from: a  reason: collision with other field name */
    private byte[] f792a;
    private int b;

    public int a() {
        return this.a;
    }

    public int a(byte[] bArr, int i, int i2) {
        int b2 = b();
        if (i2 > b2) {
            i2 = b2;
        }
        if (i2 > 0) {
            System.arraycopy(this.f792a, this.a, bArr, i, i2);
            a(i2);
        }
        return i2;
    }

    public void a(int i) {
        this.a += i;
    }

    public void a(byte[] bArr) {
        b(bArr, 0, bArr.length);
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m529a(byte[] bArr, int i, int i2) {
        throw new UnsupportedOperationException("No writing allowed!");
    }

    /* renamed from: a  reason: collision with other method in class */
    public byte[] m530a() {
        return this.f792a;
    }

    public int b() {
        return this.b - this.a;
    }

    public void b(byte[] bArr, int i, int i2) {
        this.f792a = bArr;
        this.a = i;
        this.b = i + i2;
    }
}
