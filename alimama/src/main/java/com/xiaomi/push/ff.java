package com.xiaomi.push;

import android.text.TextUtils;
import com.taobao.android.dinamic.DinamicConstant;
import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.push.ee;
import com.xiaomi.push.service.au;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public class ff {
    private static long a = 0;

    /* renamed from: a  reason: collision with other field name */
    private static final byte[] f347a = new byte[0];
    private static String b = (go.a(5) + "-");

    /* renamed from: a  reason: collision with other field name */
    private ee.a f348a;

    /* renamed from: a  reason: collision with other field name */
    String f349a;

    /* renamed from: a  reason: collision with other field name */
    private short f350a;

    /* renamed from: b  reason: collision with other field name */
    private byte[] f351b;

    public ff() {
        this.f350a = 2;
        this.f351b = f347a;
        this.f349a = null;
        this.f348a = new ee.a();
    }

    ff(ee.a aVar, short s, byte[] bArr) {
        this.f350a = 2;
        this.f351b = f347a;
        this.f349a = null;
        this.f348a = aVar;
        this.f350a = s;
        this.f351b = bArr;
    }

    @Deprecated
    public static ff a(gd gdVar, String str) {
        int i;
        ff ffVar = new ff();
        try {
            i = Integer.parseInt(gdVar.k());
        } catch (Exception e) {
            b.a("Blob parse chid err " + e.getMessage());
            i = 1;
        }
        ffVar.a(i);
        ffVar.a(gdVar.j());
        ffVar.c(gdVar.m());
        ffVar.b(gdVar.n());
        ffVar.a("XMLMSG", (String) null);
        try {
            ffVar.a(gdVar.a().getBytes("utf8"), str);
            if (TextUtils.isEmpty(str)) {
                ffVar.a(3);
            } else {
                ffVar.a(2);
                ffVar.a("SECMSG", (String) null);
            }
        } catch (UnsupportedEncodingException e2) {
            b.a("Blob setPayload err： " + e2.getMessage());
        }
        return ffVar;
    }

    static ff a(ByteBuffer byteBuffer) {
        try {
            ByteBuffer slice = byteBuffer.slice();
            short s = slice.getShort(0);
            short s2 = slice.getShort(2);
            int i = slice.getInt(4);
            ee.a aVar = new ee.a();
            aVar.a(slice.array(), slice.arrayOffset() + 8, (int) s2);
            byte[] bArr = new byte[i];
            slice.position(s2 + 8);
            slice.get(bArr, 0, i);
            return new ff(aVar, s, bArr);
        } catch (Exception e) {
            b.a("read Blob err :" + e.getMessage());
            throw new IOException("Malformed Input");
        }
    }

    public static synchronized String d() {
        String sb;
        synchronized (ff.class) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(b);
            long j = a;
            a = 1 + j;
            sb2.append(Long.toString(j));
            sb = sb2.toString();
        }
        return sb;
    }

    public int a() {
        return this.f348a.c();
    }

    /* renamed from: a  reason: collision with other method in class */
    public String m290a() {
        return this.f348a.c();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: a  reason: collision with other method in class */
    public ByteBuffer m291a(ByteBuffer byteBuffer) {
        if (byteBuffer == null) {
            byteBuffer = ByteBuffer.allocate(c());
        }
        byteBuffer.putShort(this.f350a);
        byteBuffer.putShort((short) this.f348a.a());
        byteBuffer.putInt(this.f351b.length);
        int position = byteBuffer.position();
        this.f348a.a(byteBuffer.array(), byteBuffer.arrayOffset() + position, this.f348a.a());
        byteBuffer.position(position + this.f348a.a());
        byteBuffer.put(this.f351b);
        return byteBuffer;
    }

    /* renamed from: a  reason: collision with other method in class */
    public short m292a() {
        return this.f350a;
    }

    public void a(int i) {
        this.f348a.a(i);
    }

    public void a(long j, String str, String str2) {
        if (j != 0) {
            this.f348a.a(j);
        }
        if (!TextUtils.isEmpty(str)) {
            this.f348a.a(str);
        }
        if (!TextUtils.isEmpty(str2)) {
            this.f348a.b(str2);
        }
    }

    public void a(String str) {
        this.f348a.e(str);
    }

    public void a(String str, String str2) {
        if (!TextUtils.isEmpty(str)) {
            this.f348a.c(str);
            this.f348a.a();
            if (!TextUtils.isEmpty(str2)) {
                this.f348a.d(str2);
                return;
            }
            return;
        }
        throw new IllegalArgumentException("command should not be empty");
    }

    public void a(short s) {
        this.f350a = s;
    }

    public void a(byte[] bArr, String str) {
        if (!TextUtils.isEmpty(str)) {
            this.f348a.c(1);
            bArr = au.a(au.a(str, e()), bArr);
        } else {
            this.f348a.c(0);
        }
        this.f351b = bArr;
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m293a() {
        return this.f348a.j();
    }

    /* renamed from: a  reason: collision with other method in class */
    public byte[] m294a() {
        return this.f351b;
    }

    /* renamed from: a  reason: collision with other method in class */
    public byte[] m295a(String str) {
        if (this.f348a.e() == 1) {
            return au.a(au.a(str, e()), this.f351b);
        }
        if (this.f348a.e() == 0) {
            return this.f351b;
        }
        b.a("unknow cipher = " + this.f348a.e());
        return this.f351b;
    }

    public int b() {
        return this.f348a.f();
    }

    /* renamed from: b  reason: collision with other method in class */
    public String m296b() {
        return this.f348a.d();
    }

    public void b(String str) {
        this.f349a = str;
    }

    public int c() {
        return this.f348a.b() + 8 + this.f351b.length;
    }

    /* renamed from: c  reason: collision with other method in class */
    public String m297c() {
        return this.f348a.f();
    }

    public void c(String str) {
        if (!TextUtils.isEmpty(str)) {
            int indexOf = str.indexOf(DinamicConstant.DINAMIC_PREFIX_AT);
            try {
                long parseLong = Long.parseLong(str.substring(0, indexOf));
                int indexOf2 = str.indexOf("/", indexOf);
                String substring = str.substring(indexOf + 1, indexOf2);
                String substring2 = str.substring(indexOf2 + 1);
                this.f348a.a(parseLong);
                this.f348a.a(substring);
                this.f348a.b(substring2);
            } catch (Exception e) {
                b.a("Blob parse user err " + e.getMessage());
            }
        }
    }

    public String e() {
        String e = this.f348a.e();
        if ("ID_NOT_AVAILABLE".equals(e)) {
            return null;
        }
        if (this.f348a.g()) {
            return e;
        }
        String d = d();
        this.f348a.e(d);
        return d;
    }

    public String f() {
        return this.f349a;
    }

    public String g() {
        if (!this.f348a.b()) {
            return null;
        }
        return Long.toString(this.f348a.a()) + DinamicConstant.DINAMIC_PREFIX_AT + this.f348a.a() + "/" + this.f348a.b();
    }

    public String toString() {
        return "Blob [chid=" + a() + "; Id=" + e() + "; cmd=" + a() + "; type=" + a() + "; from=" + g() + " ]";
    }
}
