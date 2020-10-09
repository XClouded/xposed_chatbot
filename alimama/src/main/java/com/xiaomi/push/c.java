package com.xiaomi.push;

import com.uc.webview.export.extension.UCCore;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public final class c {
    private final int a;

    /* renamed from: a  reason: collision with other field name */
    private final OutputStream f170a;

    /* renamed from: a  reason: collision with other field name */
    private final byte[] f171a;
    private int b;

    public static class a extends IOException {
        a() {
            super("CodedOutputStream was writing to a flat byte array and ran out of space.");
        }
    }

    private c(OutputStream outputStream, byte[] bArr) {
        this.f170a = outputStream;
        this.f171a = bArr;
        this.b = 0;
        this.a = bArr.length;
    }

    private c(byte[] bArr, int i, int i2) {
        this.f170a = null;
        this.f171a = bArr;
        this.b = i;
        this.a = i + i2;
    }

    public static int a(int i) {
        if (i >= 0) {
            return d(i);
        }
        return 10;
    }

    public static int a(int i, int i2) {
        return c(i) + a(i2);
    }

    public static int a(int i, long j) {
        return c(i) + a(j);
    }

    public static int a(int i, a aVar) {
        return c(i) + a(aVar);
    }

    public static int a(int i, e eVar) {
        return c(i) + a(eVar);
    }

    public static int a(int i, String str) {
        return c(i) + a(str);
    }

    public static int a(int i, boolean z) {
        return c(i) + a(z);
    }

    public static int a(long j) {
        return c(j);
    }

    public static int a(a aVar) {
        return d(aVar.a()) + aVar.a();
    }

    public static int a(e eVar) {
        int b2 = eVar.b();
        return d(b2) + b2;
    }

    public static int a(String str) {
        try {
            byte[] bytes = str.getBytes("UTF-8");
            return d(bytes.length) + bytes.length;
        } catch (UnsupportedEncodingException unused) {
            throw new RuntimeException("UTF-8 not supported.");
        }
    }

    public static int a(boolean z) {
        return 1;
    }

    public static c a(OutputStream outputStream) {
        return a(outputStream, 4096);
    }

    public static c a(OutputStream outputStream, int i) {
        return new c(outputStream, new byte[i]);
    }

    public static c a(byte[] bArr, int i, int i2) {
        return new c(bArr, i, i2);
    }

    public static int b(int i) {
        return d(i);
    }

    public static int b(int i, int i2) {
        return c(i) + b(i2);
    }

    public static int b(int i, long j) {
        return c(i) + b(j);
    }

    public static int b(long j) {
        return c(j);
    }

    public static int c(int i) {
        return d(f.a(i, 0));
    }

    public static int c(long j) {
        if ((-128 & j) == 0) {
            return 1;
        }
        if ((-16384 & j) == 0) {
            return 2;
        }
        if ((-2097152 & j) == 0) {
            return 3;
        }
        if ((-268435456 & j) == 0) {
            return 4;
        }
        if ((-34359738368L & j) == 0) {
            return 5;
        }
        if ((-4398046511104L & j) == 0) {
            return 6;
        }
        if ((-562949953421312L & j) == 0) {
            return 7;
        }
        if ((-72057594037927936L & j) == 0) {
            return 8;
        }
        return (j & Long.MIN_VALUE) == 0 ? 9 : 10;
    }

    private void c() {
        if (this.f170a != null) {
            this.f170a.write(this.f171a, 0, this.b);
            this.b = 0;
            return;
        }
        throw new a();
    }

    public static int d(int i) {
        if ((i & -128) == 0) {
            return 1;
        }
        if ((i & -16384) == 0) {
            return 2;
        }
        if ((-2097152 & i) == 0) {
            return 3;
        }
        return (i & -268435456) == 0 ? 4 : 5;
    }

    public int a() {
        if (this.f170a == null) {
            return this.a - this.b;
        }
        throw new UnsupportedOperationException("spaceLeft() can only be called on CodedOutputStreams that are writing to a flat array.");
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m127a() {
        if (this.f170a != null) {
            c();
        }
    }

    public void a(byte b2) {
        if (this.b == this.a) {
            c();
        }
        byte[] bArr = this.f171a;
        int i = this.b;
        this.b = i + 1;
        bArr[i] = b2;
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m128a(int i) {
        if (i >= 0) {
            d(i);
        } else {
            c((long) i);
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m129a(int i, int i2) {
        c(i, 0);
        a(i2);
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m130a(int i, long j) {
        c(i, 0);
        a(j);
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m131a(int i, a aVar) {
        c(i, 2);
        a(aVar);
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m132a(int i, e eVar) {
        c(i, 2);
        a(eVar);
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m133a(int i, String str) {
        c(i, 2);
        a(str);
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m134a(int i, boolean z) {
        c(i, 0);
        a(z);
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m135a(long j) {
        c(j);
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m136a(a aVar) {
        byte[] a2 = aVar.a();
        d(a2.length);
        a(a2);
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m137a(e eVar) {
        d(eVar.a());
        eVar.a(this);
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m138a(String str) {
        byte[] bytes = str.getBytes("UTF-8");
        d(bytes.length);
        a(bytes);
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m139a(boolean z) {
        c(z ? 1 : 0);
    }

    public void a(byte[] bArr) {
        a(bArr, 0, bArr.length);
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m140a(byte[] bArr, int i, int i2) {
        if (this.a - this.b >= i2) {
            System.arraycopy(bArr, i, this.f171a, this.b, i2);
            this.b += i2;
            return;
        }
        int i3 = this.a - this.b;
        System.arraycopy(bArr, i, this.f171a, this.b, i3);
        int i4 = i + i3;
        int i5 = i2 - i3;
        this.b = this.a;
        c();
        if (i5 <= this.a) {
            System.arraycopy(bArr, i4, this.f171a, 0, i5);
            this.b = i5;
            return;
        }
        this.f170a.write(bArr, i4, i5);
    }

    public void b() {
        if (a() != 0) {
            throw new IllegalStateException("Did not write as much data as expected.");
        }
    }

    /* renamed from: b  reason: collision with other method in class */
    public void m141b(int i) {
        d(i);
    }

    /* renamed from: b  reason: collision with other method in class */
    public void m142b(int i, int i2) {
        c(i, 0);
        b(i2);
    }

    /* renamed from: b  reason: collision with other method in class */
    public void m143b(int i, long j) {
        c(i, 0);
        b(j);
    }

    /* renamed from: b  reason: collision with other method in class */
    public void m144b(long j) {
        c(j);
    }

    /* renamed from: c  reason: collision with other method in class */
    public void m145c(int i) {
        a((byte) i);
    }

    public void c(int i, int i2) {
        d(f.a(i, i2));
    }

    /* renamed from: c  reason: collision with other method in class */
    public void m146c(long j) {
        while ((-128 & j) != 0) {
            c((((int) j) & UCCore.SPEEDUP_DEXOPT_POLICY_DAVIK) | 128);
            j >>>= 7;
        }
        c((int) j);
    }

    /* renamed from: d  reason: collision with other method in class */
    public void m147d(int i) {
        while ((i & -128) != 0) {
            c((i & UCCore.SPEEDUP_DEXOPT_POLICY_DAVIK) | 128);
            i >>>= 7;
        }
        c(i);
    }
}
