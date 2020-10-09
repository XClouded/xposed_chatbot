package com.xiaomi.push;

import java.io.InputStream;
import java.util.Vector;
import kotlin.UByte;
import kotlin.jvm.internal.ByteCompanionObject;

public final class b {
    private int a;

    /* renamed from: a  reason: collision with other field name */
    private final InputStream f133a;

    /* renamed from: a  reason: collision with other field name */
    private final byte[] f134a;
    private int b;
    private int c;
    private int d;
    private int e;
    private int f;
    private int g;
    private int h;
    private int i;

    private b(InputStream inputStream) {
        this.f = Integer.MAX_VALUE;
        this.h = 64;
        this.i = 67108864;
        this.f134a = new byte[4096];
        this.a = 0;
        this.c = 0;
        this.f133a = inputStream;
    }

    private b(byte[] bArr, int i2, int i3) {
        this.f = Integer.MAX_VALUE;
        this.h = 64;
        this.i = 67108864;
        this.f134a = bArr;
        this.a = i3 + i2;
        this.c = i2;
        this.f133a = null;
    }

    public static b a(InputStream inputStream) {
        return new b(inputStream);
    }

    public static b a(byte[] bArr, int i2, int i3) {
        return new b(bArr, i2, i3);
    }

    private boolean a(boolean z) {
        if (this.c < this.a) {
            throw new IllegalStateException("refillBuffer() called when buffer wasn't empty.");
        } else if (this.e + this.a != this.f) {
            this.e += this.a;
            this.c = 0;
            this.a = this.f133a == null ? -1 : this.f133a.read(this.f134a);
            if (this.a == 0 || this.a < -1) {
                throw new IllegalStateException("InputStream#read(byte[]) returned invalid result: " + this.a + "\nThe InputStream implementation is buggy.");
            } else if (this.a == -1) {
                this.a = 0;
                if (!z) {
                    return false;
                }
                throw d.a();
            } else {
                b();
                int i2 = this.e + this.a + this.b;
                if (i2 <= this.i && i2 >= 0) {
                    return true;
                }
                throw d.h();
            }
        } else if (!z) {
            return false;
        } else {
            throw d.a();
        }
    }

    private void b() {
        this.a += this.b;
        int i2 = this.e + this.a;
        if (i2 > this.f) {
            this.b = i2 - this.f;
            this.a -= this.b;
            return;
        }
        this.b = 0;
    }

    public byte a() {
        if (this.c == this.a) {
            a(true);
        }
        byte[] bArr = this.f134a;
        int i2 = this.c;
        this.c = i2 + 1;
        return bArr[i2];
    }

    /* renamed from: a  reason: collision with other method in class */
    public int m100a() {
        if (b()) {
            this.d = 0;
            return 0;
        }
        this.d = d();
        if (this.d != 0) {
            return this.d;
        }
        throw d.d();
    }

    public int a(int i2) {
        if (i2 >= 0) {
            int i3 = i2 + this.e + this.c;
            int i4 = this.f;
            if (i3 <= i4) {
                this.f = i3;
                b();
                return i4;
            }
            throw d.a();
        }
        throw d.b();
    }

    /* renamed from: a  reason: collision with other method in class */
    public long m101a() {
        return c();
    }

    /* renamed from: a  reason: collision with other method in class */
    public a m102a() {
        int d2 = d();
        if (d2 > this.a - this.c || d2 <= 0) {
            return a.a(a(d2));
        }
        a a2 = a.a(this.f134a, this.c, d2);
        this.c += d2;
        return a2;
    }

    /* renamed from: a  reason: collision with other method in class */
    public String m103a() {
        int d2 = d();
        if (d2 > this.a - this.c || d2 <= 0) {
            return new String(a(d2), "UTF-8");
        }
        String str = new String(this.f134a, this.c, d2, "UTF-8");
        this.c += d2;
        return str;
    }

    /*  JADX ERROR: StackOverflow in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxOverflowException: 
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:47)
        	at jadx.core.utils.ErrorsCounter.methodError(ErrorsCounter.java:81)
        */
    /* renamed from: a  reason: collision with other method in class */
    public void m104a() {
        /*
            r1 = this;
        L_0x0000:
            int r0 = r1.a()
            if (r0 == 0) goto L_0x000c
            boolean r0 = r1.a((int) r0)
            if (r0 != 0) goto L_0x0000
        L_0x000c:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.b.m104a():void");
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m105a(int i2) {
        if (this.d != i2) {
            throw d.e();
        }
    }

    public void a(e eVar) {
        int d2 = d();
        if (this.g < this.h) {
            int a2 = a(d2);
            this.g++;
            eVar.a(this);
            a(0);
            this.g--;
            b(a2);
            return;
        }
        throw d.g();
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m106a() {
        return d() != 0;
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m107a(int i2) {
        switch (f.a(i2)) {
            case 0:
                b();
                return true;
            case 1:
                d();
                return true;
            case 2:
                c(d());
                return true;
            case 3:
                a();
                a(f.a(f.b(i2), 4));
                return true;
            case 4:
                return false;
            case 5:
                e();
                return true;
            default:
                throw d.f();
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public byte[] m108a(int i2) {
        if (i2 < 0) {
            throw d.b();
        } else if (this.e + this.c + i2 > this.f) {
            c((this.f - this.e) - this.c);
            throw d.a();
        } else if (i2 <= this.a - this.c) {
            byte[] bArr = new byte[i2];
            System.arraycopy(this.f134a, this.c, bArr, 0, i2);
            this.c += i2;
            return bArr;
        } else if (i2 < 4096) {
            byte[] bArr2 = new byte[i2];
            int i3 = this.a - this.c;
            System.arraycopy(this.f134a, this.c, bArr2, 0, i3);
            this.c = this.a;
            while (true) {
                a(true);
                int i4 = i2 - i3;
                if (i4 > this.a) {
                    System.arraycopy(this.f134a, 0, bArr2, i3, this.a);
                    i3 += this.a;
                    this.c = this.a;
                } else {
                    System.arraycopy(this.f134a, 0, bArr2, i3, i4);
                    this.c = i4;
                    return bArr2;
                }
            }
        } else {
            int i5 = this.c;
            int i6 = this.a;
            this.e += this.a;
            this.c = 0;
            this.a = 0;
            int i7 = i6 - i5;
            int i8 = i2 - i7;
            Vector vector = new Vector();
            while (i8 > 0) {
                byte[] bArr3 = new byte[Math.min(i8, 4096)];
                int i9 = 0;
                while (i9 < bArr3.length) {
                    int read = this.f133a == null ? -1 : this.f133a.read(bArr3, i9, bArr3.length - i9);
                    if (read != -1) {
                        this.e += read;
                        i9 += read;
                    } else {
                        throw d.a();
                    }
                }
                i8 -= bArr3.length;
                vector.addElement(bArr3);
            }
            byte[] bArr4 = new byte[i2];
            System.arraycopy(this.f134a, i5, bArr4, 0, i7);
            for (int i10 = 0; i10 < vector.size(); i10++) {
                byte[] bArr5 = (byte[]) vector.elementAt(i10);
                System.arraycopy(bArr5, 0, bArr4, i7, bArr5.length);
                i7 += bArr5.length;
            }
            return bArr4;
        }
    }

    /* renamed from: b  reason: collision with other method in class */
    public int m109b() {
        return d();
    }

    /* renamed from: b  reason: collision with other method in class */
    public long m110b() {
        return c();
    }

    public void b(int i2) {
        this.f = i2;
        b();
    }

    /* renamed from: b  reason: collision with other method in class */
    public boolean m111b() {
        return this.c == this.a && !a(false);
    }

    public int c() {
        return d();
    }

    /* renamed from: c  reason: collision with other method in class */
    public long m112c() {
        long j = 0;
        for (int i2 = 0; i2 < 64; i2 += 7) {
            byte a2 = a();
            j |= ((long) (a2 & ByteCompanionObject.MAX_VALUE)) << i2;
            if ((a2 & ByteCompanionObject.MIN_VALUE) == 0) {
                return j;
            }
        }
        throw d.c();
    }

    public void c(int i2) {
        if (i2 < 0) {
            throw d.b();
        } else if (this.e + this.c + i2 > this.f) {
            c((this.f - this.e) - this.c);
            throw d.a();
        } else if (i2 <= this.a - this.c) {
            this.c += i2;
        } else {
            int i3 = this.a - this.c;
            this.e += this.a;
            this.c = 0;
            this.a = 0;
            while (i3 < i2) {
                int skip = this.f133a == null ? -1 : (int) this.f133a.skip((long) (i2 - i3));
                if (skip > 0) {
                    i3 += skip;
                    this.e += skip;
                } else {
                    throw d.a();
                }
            }
        }
    }

    public int d() {
        int i2;
        byte a2 = a();
        if (a2 >= 0) {
            return a2;
        }
        byte b2 = a2 & ByteCompanionObject.MAX_VALUE;
        byte a3 = a();
        if (a3 >= 0) {
            i2 = a3 << 7;
        } else {
            b2 |= (a3 & ByteCompanionObject.MAX_VALUE) << 7;
            byte a4 = a();
            if (a4 >= 0) {
                i2 = a4 << 14;
            } else {
                b2 |= (a4 & ByteCompanionObject.MAX_VALUE) << 14;
                byte a5 = a();
                if (a5 >= 0) {
                    i2 = a5 << 21;
                } else {
                    byte b3 = b2 | ((a5 & ByteCompanionObject.MAX_VALUE) << 21);
                    byte a6 = a();
                    byte b4 = b3 | (a6 << 28);
                    if (a6 >= 0) {
                        return b4;
                    }
                    for (int i3 = 0; i3 < 5; i3++) {
                        if (a() >= 0) {
                            return b4;
                        }
                    }
                    throw d.c();
                }
            }
        }
        return b2 | i2;
    }

    /* renamed from: d  reason: collision with other method in class */
    public long m113d() {
        byte a2 = a();
        byte a3 = a();
        return ((((long) a3) & 255) << 8) | (((long) a2) & 255) | ((((long) a()) & 255) << 16) | ((((long) a()) & 255) << 24) | ((((long) a()) & 255) << 32) | ((((long) a()) & 255) << 40) | ((((long) a()) & 255) << 48) | ((((long) a()) & 255) << 56);
    }

    public int e() {
        return (a() & UByte.MAX_VALUE) | ((a() & UByte.MAX_VALUE) << 8) | ((a() & UByte.MAX_VALUE) << 16) | ((a() & UByte.MAX_VALUE) << 24);
    }
}
