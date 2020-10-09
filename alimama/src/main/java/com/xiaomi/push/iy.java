package com.xiaomi.push;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import kotlin.UByte;

public class iy extends jc {
    private static final jh a = new jh();

    /* renamed from: a  reason: collision with other field name */
    protected int f777a;

    /* renamed from: a  reason: collision with other field name */
    protected boolean f778a = false;

    /* renamed from: a  reason: collision with other field name */
    private byte[] f779a = new byte[1];
    protected boolean b = true;

    /* renamed from: b  reason: collision with other field name */
    private byte[] f780b = new byte[2];
    protected boolean c = false;

    /* renamed from: c  reason: collision with other field name */
    private byte[] f781c = new byte[4];
    private byte[] d = new byte[8];
    private byte[] e = new byte[1];
    private byte[] f = new byte[2];
    private byte[] g = new byte[4];
    private byte[] h = new byte[8];

    public static class a implements je {
        protected int a;

        /* renamed from: a  reason: collision with other field name */
        protected boolean f782a;
        protected boolean b;

        public a() {
            this(false, true);
        }

        public a(boolean z, boolean z2) {
            this(z, z2, 0);
        }

        public a(boolean z, boolean z2, int i) {
            this.f782a = false;
            this.b = true;
            this.f782a = z;
            this.b = z2;
            this.a = i;
        }

        public jc a(jm jmVar) {
            iy iyVar = new iy(jmVar, this.f782a, this.b);
            if (this.a != 0) {
                iyVar.b(this.a);
            }
            return iyVar;
        }
    }

    public iy(jm jmVar, boolean z, boolean z2) {
        super(jmVar);
        this.f778a = z;
        this.b = z2;
    }

    private int a(byte[] bArr, int i, int i2) {
        c(i2);
        return this.a.b(bArr, i, i2);
    }

    public byte a() {
        if (this.a.b() >= 1) {
            byte b2 = this.a.a()[this.a.a()];
            this.a.a(1);
            return b2;
        }
        a(this.e, 0, 1);
        return this.e[0];
    }

    /* renamed from: a  reason: collision with other method in class */
    public double m496a() {
        return Double.longBitsToDouble(a());
    }

    /* renamed from: a  reason: collision with other method in class */
    public int m497a() {
        byte[] bArr = this.g;
        int i = 0;
        if (this.a.b() >= 4) {
            bArr = this.a.a();
            i = this.a.a();
            this.a.a(4);
        } else {
            a(this.g, 0, 4);
        }
        return (bArr[i + 3] & UByte.MAX_VALUE) | ((bArr[i] & UByte.MAX_VALUE) << 24) | ((bArr[i + 1] & UByte.MAX_VALUE) << 16) | ((bArr[i + 2] & UByte.MAX_VALUE) << 8);
    }

    /* renamed from: a  reason: collision with other method in class */
    public long m498a() {
        byte[] bArr = this.h;
        int i = 0;
        if (this.a.b() >= 8) {
            bArr = this.a.a();
            i = this.a.a();
            this.a.a(8);
        } else {
            a(this.h, 0, 8);
        }
        return ((long) (bArr[i + 7] & UByte.MAX_VALUE)) | (((long) (bArr[i] & UByte.MAX_VALUE)) << 56) | (((long) (bArr[i + 1] & UByte.MAX_VALUE)) << 48) | (((long) (bArr[i + 2] & UByte.MAX_VALUE)) << 40) | (((long) (bArr[i + 3] & UByte.MAX_VALUE)) << 32) | (((long) (bArr[i + 4] & UByte.MAX_VALUE)) << 24) | (((long) (bArr[i + 5] & UByte.MAX_VALUE)) << 16) | (((long) (bArr[i + 6] & UByte.MAX_VALUE)) << 8);
    }

    /* renamed from: a  reason: collision with other method in class */
    public iz m499a() {
        byte a2 = a();
        return new iz("", a2, a2 == 0 ? 0 : a());
    }

    /* renamed from: a  reason: collision with other method in class */
    public ja m500a() {
        return new ja(a(), a());
    }

    /* renamed from: a  reason: collision with other method in class */
    public jb m501a() {
        return new jb(a(), a(), a());
    }

    /* renamed from: a  reason: collision with other method in class */
    public jg m502a() {
        return new jg(a(), a());
    }

    /* renamed from: a  reason: collision with other method in class */
    public jh m503a() {
        return a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public String m504a() {
        int a2 = a();
        if (this.a.b() < a2) {
            return a(a2);
        }
        try {
            String str = new String(this.a.a(), this.a.a(), a2, "UTF-8");
            this.a.a(a2);
            return str;
        } catch (UnsupportedEncodingException unused) {
            throw new iw("JVM DOES NOT SUPPORT UTF-8");
        }
    }

    public String a(int i) {
        try {
            c(i);
            byte[] bArr = new byte[i];
            this.a.b(bArr, 0, i);
            return new String(bArr, "UTF-8");
        } catch (UnsupportedEncodingException unused) {
            throw new iw("JVM DOES NOT SUPPORT UTF-8");
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public ByteBuffer m505a() {
        int a2 = a();
        c(a2);
        if (this.a.b() >= a2) {
            ByteBuffer wrap = ByteBuffer.wrap(this.a.a(), this.a.a(), a2);
            this.a.a(a2);
            return wrap;
        }
        byte[] bArr = new byte[a2];
        this.a.b(bArr, 0, a2);
        return ByteBuffer.wrap(bArr);
    }

    /* renamed from: a  reason: collision with other method in class */
    public short m506a() {
        byte[] bArr = this.f;
        int i = 0;
        if (this.a.b() >= 2) {
            bArr = this.a.a();
            i = this.a.a();
            this.a.a(2);
        } else {
            a(this.f, 0, 2);
        }
        return (short) ((bArr[i + 1] & UByte.MAX_VALUE) | ((bArr[i] & UByte.MAX_VALUE) << 8));
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m507a() {
    }

    public void a(byte b2) {
        this.f779a[0] = b2;
        this.a.a(this.f779a, 0, 1);
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m508a(int i) {
        this.f781c[0] = (byte) ((i >> 24) & 255);
        this.f781c[1] = (byte) ((i >> 16) & 255);
        this.f781c[2] = (byte) ((i >> 8) & 255);
        this.f781c[3] = (byte) (i & 255);
        this.a.a(this.f781c, 0, 4);
    }

    public void a(long j) {
        this.d[0] = (byte) ((int) ((j >> 56) & 255));
        this.d[1] = (byte) ((int) ((j >> 48) & 255));
        this.d[2] = (byte) ((int) ((j >> 40) & 255));
        this.d[3] = (byte) ((int) ((j >> 32) & 255));
        this.d[4] = (byte) ((int) ((j >> 24) & 255));
        this.d[5] = (byte) ((int) ((j >> 16) & 255));
        this.d[6] = (byte) ((int) ((j >> 8) & 255));
        this.d[7] = (byte) ((int) (j & 255));
        this.a.a(this.d, 0, 8);
    }

    public void a(iz izVar) {
        a(izVar.a);
        a(izVar.f784a);
    }

    public void a(ja jaVar) {
        a(jaVar.a);
        a(jaVar.f787a);
    }

    public void a(jb jbVar) {
        a(jbVar.a);
        a(jbVar.b);
        a(jbVar.f788a);
    }

    public void a(jh jhVar) {
    }

    public void a(String str) {
        try {
            byte[] bytes = str.getBytes("UTF-8");
            a(bytes.length);
            this.a.a(bytes, 0, bytes.length);
        } catch (UnsupportedEncodingException unused) {
            throw new iw("JVM DOES NOT SUPPORT UTF-8");
        }
    }

    public void a(ByteBuffer byteBuffer) {
        int limit = (byteBuffer.limit() - byteBuffer.position()) - byteBuffer.arrayOffset();
        a(limit);
        this.a.a(byteBuffer.array(), byteBuffer.position() + byteBuffer.arrayOffset(), limit);
    }

    public void a(short s) {
        this.f780b[0] = (byte) ((s >> 8) & 255);
        this.f780b[1] = (byte) (s & 255);
        this.a.a(this.f780b, 0, 2);
    }

    public void a(boolean z) {
        a(z ? (byte) 1 : 0);
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m509a() {
        return a() == 1;
    }

    public void b() {
    }

    public void b(int i) {
        this.f777a = i;
        this.c = true;
    }

    public void c() {
        a((byte) 0);
    }

    /* access modifiers changed from: protected */
    public void c(int i) {
        if (i < 0) {
            throw new iw("Negative length: " + i);
        } else if (this.c) {
            this.f777a -= i;
            if (this.f777a < 0) {
                throw new iw("Message length exceeded: " + i);
            }
        }
    }

    public void d() {
    }

    public void e() {
    }

    public void f() {
    }

    public void g() {
    }

    public void h() {
    }

    public void i() {
    }

    public void j() {
    }
}
