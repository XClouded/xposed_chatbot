package com.xiaomi.push;

import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.zip.Adler32;

class fg {
    private fi a;

    /* renamed from: a  reason: collision with other field name */
    private fk f352a;

    /* renamed from: a  reason: collision with other field name */
    private InputStream f353a;

    /* renamed from: a  reason: collision with other field name */
    private ByteBuffer f354a = ByteBuffer.allocate(2048);

    /* renamed from: a  reason: collision with other field name */
    private Adler32 f355a = new Adler32();

    /* renamed from: a  reason: collision with other field name */
    private volatile boolean f356a;

    /* renamed from: a  reason: collision with other field name */
    private byte[] f357a;
    private ByteBuffer b = ByteBuffer.allocate(4);

    fg(InputStream inputStream, fk fkVar) {
        this.f353a = new BufferedInputStream(inputStream);
        this.f352a = fkVar;
        this.a = new fi();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0068, code lost:
        if (r0 < 2048) goto L_0x003f;
     */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x00a4  */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x00b7  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.nio.ByteBuffer a() {
        /*
            r8 = this;
            java.nio.ByteBuffer r0 = r8.f354a
            r0.clear()
            java.nio.ByteBuffer r0 = r8.f354a
            r1 = 8
            r8.a(r0, r1)
            java.nio.ByteBuffer r0 = r8.f354a
            r1 = 0
            short r0 = r0.getShort(r1)
            java.nio.ByteBuffer r2 = r8.f354a
            r3 = 2
            short r2 = r2.getShort(r3)
            r3 = -15618(0xffffffffffffc2fe, float:NaN)
            if (r0 != r3) goto L_0x00ea
            r0 = 5
            if (r2 != r0) goto L_0x00ea
            java.nio.ByteBuffer r0 = r8.f354a
            r2 = 4
            int r0 = r0.getInt(r2)
            java.nio.ByteBuffer r3 = r8.f354a
            int r3 = r3.position()
            r4 = 32768(0x8000, float:4.5918E-41)
            if (r0 > r4) goto L_0x00e2
            int r4 = r0 + 4
            java.nio.ByteBuffer r5 = r8.f354a
            int r5 = r5.remaining()
            if (r4 <= r5) goto L_0x005c
            int r4 = r0 + 2048
        L_0x003f:
            java.nio.ByteBuffer r4 = java.nio.ByteBuffer.allocate(r4)
            java.nio.ByteBuffer r5 = r8.f354a
            byte[] r5 = r5.array()
            java.nio.ByteBuffer r6 = r8.f354a
            int r6 = r6.arrayOffset()
            java.nio.ByteBuffer r7 = r8.f354a
            int r7 = r7.position()
            int r6 = r6 + r7
            r4.put(r5, r1, r6)
            r8.f354a = r4
            goto L_0x006b
        L_0x005c:
            java.nio.ByteBuffer r4 = r8.f354a
            int r4 = r4.capacity()
            r5 = 4096(0x1000, float:5.74E-42)
            if (r4 <= r5) goto L_0x006b
            r4 = 2048(0x800, float:2.87E-42)
            if (r0 >= r4) goto L_0x006b
            goto L_0x003f
        L_0x006b:
            java.nio.ByteBuffer r4 = r8.f354a
            r8.a(r4, r0)
            java.nio.ByteBuffer r4 = r8.b
            r4.clear()
            java.nio.ByteBuffer r4 = r8.b
            r8.a(r4, r2)
            java.nio.ByteBuffer r2 = r8.b
            r2.position(r1)
            java.nio.ByteBuffer r2 = r8.b
            int r2 = r2.getInt()
            java.util.zip.Adler32 r4 = r8.f355a
            r4.reset()
            java.util.zip.Adler32 r4 = r8.f355a
            java.nio.ByteBuffer r5 = r8.f354a
            byte[] r5 = r5.array()
            java.nio.ByteBuffer r6 = r8.f354a
            int r6 = r6.position()
            r4.update(r5, r1, r6)
            java.util.zip.Adler32 r1 = r8.f355a
            long r4 = r1.getValue()
            int r1 = (int) r4
            if (r2 != r1) goto L_0x00b7
            byte[] r1 = r8.f357a
            if (r1 == 0) goto L_0x00b4
            byte[] r1 = r8.f357a
            java.nio.ByteBuffer r2 = r8.f354a
            byte[] r2 = r2.array()
            r4 = 1
            com.xiaomi.push.service.au.a(r1, r2, r4, r3, r0)
        L_0x00b4:
            java.nio.ByteBuffer r0 = r8.f354a
            return r0
        L_0x00b7:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "CRC = "
            r0.append(r1)
            java.util.zip.Adler32 r1 = r8.f355a
            long r3 = r1.getValue()
            int r1 = (int) r3
            r0.append(r1)
            java.lang.String r1 = " and "
            r0.append(r1)
            r0.append(r2)
            java.lang.String r0 = r0.toString()
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.String) r0)
            java.io.IOException r0 = new java.io.IOException
            java.lang.String r1 = "Corrupted Blob bad CRC"
            r0.<init>(r1)
            throw r0
        L_0x00e2:
            java.io.IOException r0 = new java.io.IOException
            java.lang.String r1 = "Blob size too large"
            r0.<init>(r1)
            throw r0
        L_0x00ea:
            java.io.IOException r0 = new java.io.IOException
            java.lang.String r1 = "Malformed Input"
            r0.<init>(r1)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.fg.a():java.nio.ByteBuffer");
    }

    private void a(ByteBuffer byteBuffer, int i) {
        int position = byteBuffer.position();
        do {
            int read = this.f353a.read(byteBuffer.array(), position, i);
            if (read != -1) {
                i -= read;
                position += read;
            } else {
                throw new EOFException();
            }
        } while (i > 0);
        byteBuffer.position(position);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:34:0x0137, code lost:
        r6.f352a.a(r0);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void c() {
        /*
            r6 = this;
            r0 = 0
            r6.f356a = r0
            com.xiaomi.push.ff r1 = r6.a()
            java.lang.String r2 = "CONN"
            java.lang.String r3 = r1.a()
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0066
            byte[] r1 = r1.a()
            com.xiaomi.push.ee$f r1 = com.xiaomi.push.ee.f.a((byte[]) r1)
            boolean r2 = r1.a()
            if (r2 == 0) goto L_0x002b
            com.xiaomi.push.fk r0 = r6.f352a
            java.lang.String r2 = r1.a()
            r0.a((java.lang.String) r2)
            r0 = 1
        L_0x002b:
            boolean r2 = r1.c()
            if (r2 == 0) goto L_0x004e
            com.xiaomi.push.ee$b r2 = r1.a()
            com.xiaomi.push.ff r3 = new com.xiaomi.push.ff
            r3.<init>()
            java.lang.String r4 = "SYNC"
            java.lang.String r5 = "CONF"
            r3.a((java.lang.String) r4, (java.lang.String) r5)
            byte[] r2 = r2.a()
            r4 = 0
            r3.a((byte[]) r2, (java.lang.String) r4)
            com.xiaomi.push.fk r2 = r6.f352a
            r2.a((com.xiaomi.push.ff) r3)
        L_0x004e:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "[Slim] CONN: host = "
            r2.append(r3)
            java.lang.String r1 = r1.b()
            r2.append(r1)
            java.lang.String r1 = r2.toString()
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.String) r1)
        L_0x0066:
            if (r0 == 0) goto L_0x013f
            com.xiaomi.push.fk r0 = r6.f352a
            byte[] r0 = r0.a()
            r6.f357a = r0
        L_0x0070:
            boolean r0 = r6.f356a
            if (r0 != 0) goto L_0x013e
            com.xiaomi.push.ff r0 = r6.a()
            com.xiaomi.push.fk r1 = r6.f352a
            r1.c()
            short r1 = r0.a()
            switch(r1) {
                case 1: goto L_0x0137;
                case 2: goto L_0x00de;
                case 3: goto L_0x009d;
                default: goto L_0x0084;
            }
        L_0x0084:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "[Slim] unknow blob type "
            r1.append(r2)
            short r0 = r0.a()
            r1.append(r0)
            java.lang.String r0 = r1.toString()
        L_0x0099:
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.String) r0)
            goto L_0x0070
        L_0x009d:
            com.xiaomi.push.fi r1 = r6.a     // Catch:{ Exception -> 0x00af }
            byte[] r2 = r0.a()     // Catch:{ Exception -> 0x00af }
            com.xiaomi.push.fk r3 = r6.f352a     // Catch:{ Exception -> 0x00af }
            com.xiaomi.push.gd r1 = r1.a(r2, r3)     // Catch:{ Exception -> 0x00af }
            com.xiaomi.push.fk r2 = r6.f352a     // Catch:{ Exception -> 0x00af }
            r2.b((com.xiaomi.push.gd) r1)     // Catch:{ Exception -> 0x00af }
            goto L_0x0070
        L_0x00af:
            r1 = move-exception
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
        L_0x00b5:
            java.lang.String r3 = "[Slim] Parse packet from Blob chid="
            r2.append(r3)
            int r3 = r0.a()
            r2.append(r3)
            java.lang.String r3 = "; Id="
            r2.append(r3)
            java.lang.String r0 = r0.e()
            r2.append(r0)
            java.lang.String r0 = " failure:"
            r2.append(r0)
            java.lang.String r0 = r1.getMessage()
            r2.append(r0)
            java.lang.String r0 = r2.toString()
            goto L_0x0099
        L_0x00de:
            java.lang.String r1 = "SECMSG"
            java.lang.String r2 = r0.a()
            boolean r1 = r1.equals(r2)
            if (r1 == 0) goto L_0x0137
            int r1 = r0.a()
            r2 = 2
            if (r1 == r2) goto L_0x00f8
            int r1 = r0.a()
            r2 = 3
            if (r1 != r2) goto L_0x0137
        L_0x00f8:
            java.lang.String r1 = r0.b()
            boolean r1 = android.text.TextUtils.isEmpty(r1)
            if (r1 == 0) goto L_0x0137
            int r1 = r0.a()     // Catch:{ Exception -> 0x012f }
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)     // Catch:{ Exception -> 0x012f }
            java.lang.String r1 = r1.toString()     // Catch:{ Exception -> 0x012f }
            java.lang.String r2 = r0.g()     // Catch:{ Exception -> 0x012f }
            com.xiaomi.push.service.al r3 = com.xiaomi.push.service.al.a()     // Catch:{ Exception -> 0x012f }
            com.xiaomi.push.service.al$b r1 = r3.a((java.lang.String) r1, (java.lang.String) r2)     // Catch:{ Exception -> 0x012f }
            com.xiaomi.push.fi r2 = r6.a     // Catch:{ Exception -> 0x012f }
            java.lang.String r1 = r1.h     // Catch:{ Exception -> 0x012f }
            byte[] r1 = r0.a((java.lang.String) r1)     // Catch:{ Exception -> 0x012f }
            com.xiaomi.push.fk r3 = r6.f352a     // Catch:{ Exception -> 0x012f }
            com.xiaomi.push.gd r1 = r2.a(r1, r3)     // Catch:{ Exception -> 0x012f }
            com.xiaomi.push.fk r2 = r6.f352a     // Catch:{ Exception -> 0x012f }
            r2.b((com.xiaomi.push.gd) r1)     // Catch:{ Exception -> 0x012f }
            goto L_0x0070
        L_0x012f:
            r1 = move-exception
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            goto L_0x00b5
        L_0x0137:
            com.xiaomi.push.fk r1 = r6.f352a
            r1.a((com.xiaomi.push.ff) r0)
            goto L_0x0070
        L_0x013e:
            return
        L_0x013f:
            java.lang.String r0 = "[Slim] Invalid CONN"
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.String) r0)
            java.io.IOException r0 = new java.io.IOException
            java.lang.String r1 = "Invalid Connection"
            r0.<init>(r1)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.fg.c():void");
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x0059  */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x0073  */
    /* renamed from: a  reason: collision with other method in class */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.xiaomi.push.ff m298a() {
        /*
            r6 = this;
            r0 = 0
            java.nio.ByteBuffer r1 = r6.a()     // Catch:{ IOException -> 0x0055 }
            int r2 = r1.position()     // Catch:{ IOException -> 0x0055 }
            r1.flip()     // Catch:{ IOException -> 0x0053 }
            r3 = 8
            r1.position(r3)     // Catch:{ IOException -> 0x0053 }
            if (r2 != r3) goto L_0x0019
            com.xiaomi.push.fj r1 = new com.xiaomi.push.fj     // Catch:{ IOException -> 0x0053 }
            r1.<init>()     // Catch:{ IOException -> 0x0053 }
            goto L_0x0021
        L_0x0019:
            java.nio.ByteBuffer r1 = r1.slice()     // Catch:{ IOException -> 0x0053 }
            com.xiaomi.push.ff r1 = com.xiaomi.push.ff.a((java.nio.ByteBuffer) r1)     // Catch:{ IOException -> 0x0053 }
        L_0x0021:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x0053 }
            r3.<init>()     // Catch:{ IOException -> 0x0053 }
            java.lang.String r4 = "[Slim] Read {cmd="
            r3.append(r4)     // Catch:{ IOException -> 0x0053 }
            java.lang.String r4 = r1.a()     // Catch:{ IOException -> 0x0053 }
            r3.append(r4)     // Catch:{ IOException -> 0x0053 }
            java.lang.String r4 = ";chid="
            r3.append(r4)     // Catch:{ IOException -> 0x0053 }
            int r4 = r1.a()     // Catch:{ IOException -> 0x0053 }
            r3.append(r4)     // Catch:{ IOException -> 0x0053 }
            java.lang.String r4 = ";len="
            r3.append(r4)     // Catch:{ IOException -> 0x0053 }
            r3.append(r2)     // Catch:{ IOException -> 0x0053 }
            java.lang.String r4 = "}"
            r3.append(r4)     // Catch:{ IOException -> 0x0053 }
            java.lang.String r3 = r3.toString()     // Catch:{ IOException -> 0x0053 }
            com.xiaomi.channel.commonutils.logger.b.c(r3)     // Catch:{ IOException -> 0x0053 }
            return r1
        L_0x0053:
            r1 = move-exception
            goto L_0x0057
        L_0x0055:
            r1 = move-exception
            r2 = 0
        L_0x0057:
            if (r2 != 0) goto L_0x005f
            java.nio.ByteBuffer r2 = r6.f354a
            int r2 = r2.position()
        L_0x005f:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "[Slim] read Blob ["
            r3.append(r4)
            java.nio.ByteBuffer r4 = r6.f354a
            byte[] r4 = r4.array()
            r5 = 128(0x80, float:1.794E-43)
            if (r2 <= r5) goto L_0x0075
            r2 = 128(0x80, float:1.794E-43)
        L_0x0075:
            java.lang.String r0 = com.xiaomi.push.af.a(r4, r0, r2)
            r3.append(r0)
            java.lang.String r0 = "] Err:"
            r3.append(r0)
            java.lang.String r0 = r1.getMessage()
            r3.append(r0)
            java.lang.String r0 = r3.toString()
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.String) r0)
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.fg.m298a():com.xiaomi.push.ff");
    }

    /* access modifiers changed from: package-private */
    /* renamed from: a  reason: collision with other method in class */
    public void m299a() {
        try {
            c();
        } catch (IOException e) {
            if (!this.f356a) {
                throw e;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void b() {
        this.f356a = true;
    }
}
