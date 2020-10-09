package com.xiaomi.push;

import com.xiaomi.push.iy;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public class ji extends iy {
    private static int b = 10000;
    private static int c = 10000;
    private static int d = 10000;
    private static int e = 10485760;
    private static int f = 104857600;

    public static class a extends iy.a {
        public a() {
            super(false, true);
        }

        public a(boolean z, boolean z2, int i) {
            super(z, z2, i);
        }

        public jc a(jm jmVar) {
            ji jiVar = new ji(jmVar, this.f782a, this.b);
            if (this.a != 0) {
                jiVar.b(this.a);
            }
            return jiVar;
        }
    }

    public ji(jm jmVar, boolean z, boolean z2) {
        super(jmVar, z, z2);
    }

    public ja a() {
        byte a2 = a();
        int a3 = a();
        if (a3 <= c) {
            return new ja(a2, a3);
        }
        throw new jd(3, "Thrift list size " + a3 + " out of range!");
    }

    /* renamed from: a  reason: collision with other method in class */
    public jb m523a() {
        byte a2 = a();
        byte a3 = a();
        int a4 = a();
        if (a4 <= b) {
            return new jb(a2, a3, a4);
        }
        throw new jd(3, "Thrift map size " + a4 + " out of range!");
    }

    /* renamed from: a  reason: collision with other method in class */
    public jg m524a() {
        byte a2 = a();
        int a3 = a();
        if (a3 <= d) {
            return new jg(a2, a3);
        }
        throw new jd(3, "Thrift set size " + a3 + " out of range!");
    }

    /* renamed from: a  reason: collision with other method in class */
    public String m525a() {
        int a2 = a();
        if (a2 > e) {
            throw new jd(3, "Thrift string size " + a2 + " out of range!");
        } else if (this.a.b() < a2) {
            return a(a2);
        } else {
            try {
                String str = new String(this.a.a(), this.a.a(), a2, "UTF-8");
                this.a.a(a2);
                return str;
            } catch (UnsupportedEncodingException unused) {
                throw new iw("JVM DOES NOT SUPPORT UTF-8");
            }
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public ByteBuffer m526a() {
        int a2 = a();
        if (a2 <= f) {
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
        throw new jd(3, "Thrift binary size " + a2 + " out of range!");
    }
}
