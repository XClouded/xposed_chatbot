package com.xiaomi.push;

public class jk extends jm {
    private int a;

    /* renamed from: a  reason: collision with other field name */
    private iu f791a;

    public jk(int i) {
        this.f791a = new iu(i);
    }

    public int a(byte[] bArr, int i, int i2) {
        byte[] a2 = this.f791a.a();
        if (i2 > this.f791a.a() - this.a) {
            i2 = this.f791a.a() - this.a;
        }
        if (i2 > 0) {
            System.arraycopy(a2, this.a, bArr, i, i2);
            this.a += i2;
        }
        return i2;
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m528a(byte[] bArr, int i, int i2) {
        this.f791a.write(bArr, i, i2);
    }

    public int a_() {
        return this.f791a.size();
    }
}
