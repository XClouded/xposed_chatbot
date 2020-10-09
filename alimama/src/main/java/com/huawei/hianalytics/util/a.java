package com.huawei.hianalytics.util;

public class a {
    private byte[] a;
    private int b;
    private int c;

    public a() {
        this.a = null;
        this.b = 1024;
        this.c = 0;
        this.a = new byte[this.b];
    }

    public a(int i) {
        this.a = null;
        this.b = 1024;
        this.c = 0;
        this.b = i;
        this.a = new byte[i];
    }

    public int a() {
        return this.c;
    }

    public void a(byte[] bArr, int i) {
        if (i > 0) {
            if (this.a.length - this.c >= i) {
                System.arraycopy(bArr, 0, this.a, this.c, i);
            } else {
                byte[] bArr2 = new byte[((this.a.length + i) << 1)];
                System.arraycopy(this.a, 0, bArr2, 0, this.c);
                System.arraycopy(bArr, 0, bArr2, this.c, i);
                this.a = bArr2;
            }
            this.c += i;
        }
    }

    public byte[] b() {
        if (this.c <= 0) {
            return new byte[0];
        }
        byte[] bArr = new byte[this.c];
        System.arraycopy(this.a, 0, bArr, 0, this.c);
        return bArr;
    }
}
