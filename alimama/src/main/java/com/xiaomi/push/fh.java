package com.xiaomi.push;

import android.os.Build;
import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.push.ee;
import com.xiaomi.push.service.au;
import com.xiaomi.push.service.ba;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Locale;
import java.util.TimeZone;
import java.util.zip.Adler32;

public class fh {
    private int a;

    /* renamed from: a  reason: collision with other field name */
    private fk f358a;

    /* renamed from: a  reason: collision with other field name */
    private OutputStream f359a;

    /* renamed from: a  reason: collision with other field name */
    ByteBuffer f360a = ByteBuffer.allocate(2048);

    /* renamed from: a  reason: collision with other field name */
    private Adler32 f361a = new Adler32();

    /* renamed from: a  reason: collision with other field name */
    private byte[] f362a;
    private int b;

    /* renamed from: b  reason: collision with other field name */
    private ByteBuffer f363b = ByteBuffer.allocate(4);

    fh(OutputStream outputStream, fk fkVar) {
        this.f359a = new BufferedOutputStream(outputStream);
        this.f358a = fkVar;
        TimeZone timeZone = TimeZone.getDefault();
        this.a = timeZone.getRawOffset() / 3600000;
        this.b = timeZone.useDaylightTime() ? 1 : 0;
    }

    public int a(ff ffVar) {
        int c = ffVar.c();
        if (c > 32768) {
            b.a("Blob size=" + c + " should be less than " + 32768 + " Drop blob chid=" + ffVar.a() + " id=" + ffVar.e());
            return 0;
        }
        this.f360a.clear();
        int i = c + 8 + 4;
        if (i > this.f360a.capacity() || this.f360a.capacity() > 4096) {
            this.f360a = ByteBuffer.allocate(i);
        }
        this.f360a.putShort(-15618);
        this.f360a.putShort(5);
        this.f360a.putInt(c);
        int position = this.f360a.position();
        this.f360a = ffVar.a(this.f360a);
        if (!"CONN".equals(ffVar.a())) {
            if (this.f362a == null) {
                this.f362a = this.f358a.a();
            }
            au.a(this.f362a, this.f360a.array(), true, position, c);
        }
        this.f361a.reset();
        this.f361a.update(this.f360a.array(), 0, this.f360a.position());
        this.f363b.putInt(0, (int) this.f361a.getValue());
        this.f359a.write(this.f360a.array(), 0, this.f360a.position());
        this.f359a.write(this.f363b.array(), 0, 4);
        this.f359a.flush();
        int position2 = this.f360a.position() + 4;
        b.c("[Slim] Wrote {cmd=" + ffVar.a() + ";chid=" + ffVar.a() + ";len=" + position2 + "}");
        return position2;
    }

    public void a() {
        ee.e eVar = new ee.e();
        eVar.a(106);
        eVar.a(Build.MODEL);
        eVar.b(t.a());
        eVar.c(ba.a());
        eVar.b(38);
        eVar.d(this.f358a.b());
        eVar.e(this.f358a.a());
        eVar.f(Locale.getDefault().toString());
        eVar.c(Build.VERSION.SDK_INT);
        byte[] a2 = this.f358a.a().a();
        if (a2 != null) {
            eVar.a(ee.b.a(a2));
        }
        ff ffVar = new ff();
        ffVar.a(0);
        ffVar.a("CONN", (String) null);
        ffVar.a(0, "xiaomi.com", (String) null);
        ffVar.a(eVar.a(), (String) null);
        a(ffVar);
        b.a("[slim] open conn: andver=" + Build.VERSION.SDK_INT + " sdk=" + 38 + " hash=" + ba.a() + " tz=" + this.a + ":" + this.b + " Model=" + Build.MODEL + " os=" + Build.VERSION.INCREMENTAL);
    }

    public void b() {
        ff ffVar = new ff();
        ffVar.a("CLOSE", (String) null);
        a(ffVar);
        this.f359a.close();
    }
}
