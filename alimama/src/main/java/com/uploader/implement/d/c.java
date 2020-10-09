package com.uploader.implement.d;

import android.os.Handler;
import android.os.Looper;
import android.util.Pair;
import androidx.annotation.NonNull;
import com.alipay.auth.mobile.common.AlipayAuthConstant;
import com.uploader.implement.a.e;
import com.uploader.implement.a.h;
import com.uploader.implement.b.d;
import com.uploader.implement.b.f;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.android.agoo.message.MessageService;

/* compiled from: UploaderSession */
public class c implements com.uploader.implement.b.b, d, b {
    private static final AtomicInteger g = new AtomicInteger(0);
    private a a;
    private ArrayList<e> b = new ArrayList<>();
    private ArrayList<b> c = new ArrayList<>();
    private ArrayList<a> d = new ArrayList<>();
    private com.uploader.implement.b.c e;
    private Handler f;
    private final int h;
    private final com.uploader.implement.c i;

    /* compiled from: UploaderSession */
    private static final class b {
        final e a;
        final h b;
        final com.uploader.implement.b.e c;
        boolean d;
        boolean e;
        int f;
        int g;
        int h;
        int i;
        ByteBuffer j;
        Map<String, String> k;
        f l;
        ByteBuffer m;

        b(@NonNull e eVar, @NonNull com.uploader.implement.b.e eVar2) {
            this.a = eVar;
            this.b = eVar.b();
            this.c = eVar2;
            Map<String, String> map = this.b.e;
            boolean z = false;
            this.d = map == null || map.size() == 0;
            byte[] bArr = this.b.g;
            this.e = (bArr == null || bArr.length == 0) ? true : z;
        }

        /* access modifiers changed from: package-private */
        public void a() {
            Map<String, String> map = this.b.e;
            boolean z = true;
            this.d = map == null || map.size() == 0;
            byte[] bArr = this.b.g;
            if (!(bArr == null || bArr.length == 0)) {
                z = false;
            }
            this.e = z;
            this.g = 0;
            this.f = 0;
            this.h = 0;
            this.j = null;
            this.k = null;
        }

        /* access modifiers changed from: package-private */
        public boolean b() {
            boolean z = this.b.f == null || this.f == this.b.f.length;
            boolean z2 = this.b.a == null || ((long) this.g) == this.b.d;
            if (!this.e || !this.d || !z || !z2) {
                return false;
            }
            return true;
        }
    }

    /* compiled from: UploaderSession */
    static class a implements Runnable {
        final int a;
        final c b;
        final Object[] c;

        a(int i, @NonNull c cVar, Object... objArr) {
            this.a = i;
            this.b = cVar;
            this.c = objArr;
        }

        public void run() {
            switch (this.a) {
                case 1:
                    this.b.c((com.uploader.implement.b.e) this.c[0]);
                    return;
                case 2:
                    this.b.d((com.uploader.implement.b.e) this.c[0]);
                    return;
                case 3:
                    this.b.b((com.uploader.implement.b.e) this.c[0], (com.uploader.implement.c.a) this.c[1]);
                    return;
                case 4:
                    this.b.b((com.uploader.implement.b.e) this.c[0], (f) this.c[1]);
                    return;
                case 5:
                    this.b.a((com.uploader.implement.b.e) this.c[0], ((Integer) this.c[1]).intValue(), false);
                    return;
                case 6:
                    this.b.a((com.uploader.implement.b.e) this.c[0], ((Integer) this.c[1]).intValue(), true);
                    return;
                case 7:
                    this.b.b((b) this.c[0], (e) this.c[1], (com.uploader.implement.b.e) this.c[2]);
                    return;
                case 8:
                    this.b.b((com.uploader.implement.b.e) this.c[0]);
                    return;
                default:
                    return;
            }
        }
    }

    public c(com.uploader.implement.c cVar, com.uploader.implement.b.c cVar2, Looper looper) {
        this.i = cVar;
        this.e = cVar2;
        this.f = new Handler(looper);
        this.h = hashCode();
    }

    public void a(@NonNull e eVar, boolean z) {
        if (!this.b.remove(eVar)) {
            int a2 = a(eVar, this.c);
            if (a2 != -1) {
                boolean a3 = this.e.a((b) this, eVar, z);
                int b2 = b(this.c.remove(a2).c, this.d);
                if (b2 != -1) {
                    this.f.removeCallbacks(this.d.remove(b2));
                }
                if (com.uploader.implement.a.a(2)) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(this.h);
                    sb.append(" cancel, sendingList request");
                    sb.append(eVar.hashCode());
                    sb.append(" remove timeout:");
                    sb.append(b2 != -1);
                    sb.append(" unregister:");
                    sb.append(a3);
                    com.uploader.implement.a.a(2, "UploaderSession", sb.toString());
                }
            } else if (com.uploader.implement.a.a(2)) {
                com.uploader.implement.a.a(2, "UploaderSession", this.h + " cancel, no sending request:" + eVar.hashCode());
            }
        } else if (com.uploader.implement.a.a(2)) {
            com.uploader.implement.a.a(2, "UploaderSession", this.h + " cancel, waiting request:" + eVar.hashCode());
        }
    }

    public void a(@NonNull e eVar) {
        this.b.add(eVar);
        boolean a2 = this.e.a((b) this, eVar, (d) this);
        if (com.uploader.implement.a.a(2)) {
            com.uploader.implement.a.a(2, "UploaderSession", this.h + " send, request:" + eVar.hashCode() + " register:" + a2);
        }
    }

    public void a(@NonNull e eVar, @NonNull e eVar2, boolean z) {
        int indexOf = this.b.indexOf(eVar);
        if (indexOf != -1) {
            this.b.set(indexOf, eVar2);
            boolean a2 = this.e.a(this, eVar, eVar2, this, z);
            if (!a2) {
                this.e.a((b) this, eVar, z);
                this.e.a((b) this, eVar2, (d) this);
            }
            if (com.uploader.implement.a.a(2)) {
                com.uploader.implement.a.a(2, "UploaderSession", this.h + " replace:" + a2 + " waiting request:" + eVar.hashCode());
                return;
            }
            return;
        }
        int a3 = a(eVar, this.c);
        if (a3 == -1) {
            this.b.add(eVar2);
            boolean a4 = this.e.a(this, eVar, eVar2, this, z);
            if (!a4) {
                this.e.a((b) this, eVar, z);
                this.e.a((b) this, eVar2, (d) this);
            }
            if (com.uploader.implement.a.a(2)) {
                com.uploader.implement.a.a(2, "UploaderSession", this.h + " replace:" + a4 + " request:" + eVar.hashCode() + " newRequest:" + eVar2.hashCode());
                return;
            }
            return;
        }
        b remove = this.c.remove(a3);
        this.b.add(eVar2);
        boolean a5 = this.e.a(this, eVar, eVar2, this, z);
        if (!a5) {
            this.e.a((b) this, eVar, z);
            this.e.a((b) this, eVar2, (d) this);
        }
        int b2 = b(remove.c, this.d);
        if (b2 != -1) {
            this.f.removeCallbacks(this.d.remove(b2));
        }
        if (com.uploader.implement.a.a(2)) {
            com.uploader.implement.a.a(2, "UploaderSession", this.h + " replace:" + a5 + " sending request:" + eVar.hashCode() + " newRequest:" + eVar2.hashCode());
        }
    }

    /* access modifiers changed from: package-private */
    public void b(com.uploader.implement.b.e eVar) {
        this.d.remove(this);
        int a2 = a(eVar, this.c);
        if (a2 != -1) {
            if (com.uploader.implement.a.a(2)) {
                com.uploader.implement.a.a(2, "UploaderSession", this.h + " timeout, connection:" + eVar.hashCode());
            }
            a(a2, new com.uploader.implement.c.a(MessageService.MSG_DB_COMPLETE, "2", "data send or receive timeout", true));
        } else if (com.uploader.implement.a.a(8)) {
            com.uploader.implement.a.a(8, "UploaderSession", this.h + " timeout, NO_POSITION, connection:" + eVar.hashCode());
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:66:0x017a A[SYNTHETIC, Splitter:B:66:0x017a] */
    /* JADX WARNING: Removed duplicated region for block: B:74:0x01a0 A[SYNTHETIC, Splitter:B:74:0x01a0] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private com.uploader.implement.c.a a(com.uploader.implement.d.c.b r11, java.nio.ByteBuffer r12) {
        /*
            r10 = this;
            com.uploader.implement.a.h r0 = r11.b
            byte[] r0 = r0.h
            r1 = 0
            r2 = 0
            if (r0 == 0) goto L_0x006f
            com.uploader.implement.a.h r0 = r11.b
            long r3 = r0.c
            int r0 = r11.g
            long r5 = (long) r0
            long r3 = r3 + r5
            int r0 = (int) r3
            com.uploader.implement.a.h r3 = r11.b
            long r3 = r3.d
            int r5 = r11.g
            long r5 = (long) r5
            long r3 = r3 - r5
            int r5 = r12.remaining()
            long r5 = (long) r5
            long r3 = java.lang.Math.min(r3, r5)
            int r3 = (int) r3
            if (r3 < 0) goto L_0x0063
            com.uploader.implement.a.h r4 = r11.b
            long r4 = r4.d
            com.uploader.implement.a.h r6 = r11.b
            byte[] r6 = r6.h
            int r6 = r6.length
            long r6 = (long) r6
            int r8 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r8 <= 0) goto L_0x0034
            goto L_0x0063
        L_0x0034:
            com.uploader.implement.a.h r2 = r11.b
            byte[] r2 = r2.h
            r12.put(r2, r0, r3)
            int r12 = r11.g
            int r12 = r12 + r3
            r11.g = r12
            r11 = 4
            boolean r12 = com.uploader.implement.a.a((int) r11)
            if (r12 == 0) goto L_0x0062
            java.lang.String r12 = "UploaderSession"
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            int r2 = r10.h
            r0.append(r2)
            java.lang.String r2 = " readFromEntity, from copy:"
            r0.append(r2)
            r0.append(r3)
            java.lang.String r0 = r0.toString()
            com.uploader.implement.a.a(r11, r12, r0)
        L_0x0062:
            return r1
        L_0x0063:
            com.uploader.implement.c.a r11 = new com.uploader.implement.c.a
            java.lang.String r12 = "200"
            java.lang.String r0 = "11"
            java.lang.String r1 = "readFromBytes"
            r11.<init>(r12, r0, r1, r2)
            return r11
        L_0x006f:
            com.uploader.implement.a.h r0 = r11.b
            java.io.File r0 = r0.a
            if (r0 == 0) goto L_0x01c4
            boolean r3 = r0.exists()
            if (r3 != 0) goto L_0x007d
            goto L_0x01c4
        L_0x007d:
            long r3 = r0.lastModified()
            com.uploader.implement.a.h r5 = r11.b
            long r5 = r5.b
            int r7 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
            if (r7 == 0) goto L_0x008b
            r5 = 1
            goto L_0x008c
        L_0x008b:
            r5 = 0
        L_0x008c:
            r6 = 8
            if (r5 == 0) goto L_0x00db
            boolean r12 = com.uploader.implement.a.a((int) r6)
            if (r12 == 0) goto L_0x00bd
            java.lang.String r12 = "UploaderSession"
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            int r1 = r10.h
            r0.append(r1)
            java.lang.String r1 = " readFromEntity, file has been modified, origin:"
            r0.append(r1)
            com.uploader.implement.a.h r11 = r11.b
            long r7 = r11.b
            r0.append(r7)
            java.lang.String r11 = " current:"
            r0.append(r11)
            r0.append(r3)
            java.lang.String r11 = r0.toString()
            com.uploader.implement.a.a(r6, r12, r11)
        L_0x00bd:
            r11 = 0
            int r0 = (r11 > r3 ? 1 : (r11 == r3 ? 0 : -1))
            if (r0 != 0) goto L_0x00cf
            com.uploader.implement.c.a r11 = new com.uploader.implement.c.a
            java.lang.String r12 = "200"
            java.lang.String r0 = "10"
            java.lang.String r1 = "file.lastModified()==0"
            r11.<init>(r12, r0, r1, r2)
            return r11
        L_0x00cf:
            com.uploader.implement.c.a r11 = new com.uploader.implement.c.a
            java.lang.String r12 = "200"
            java.lang.String r0 = "6"
            java.lang.String r1 = "file has been modified"
            r11.<init>(r12, r0, r1, r2)
            return r11
        L_0x00db:
            java.io.FileInputStream r3 = new java.io.FileInputStream     // Catch:{ Exception -> 0x016a }
            r3.<init>(r0)     // Catch:{ Exception -> 0x016a }
            java.nio.channels.FileChannel r0 = r3.getChannel()     // Catch:{ Exception -> 0x0164, all -> 0x0162 }
            com.uploader.implement.a.h r4 = r11.b     // Catch:{ Exception -> 0x0164, all -> 0x0162 }
            long r4 = r4.c     // Catch:{ Exception -> 0x0164, all -> 0x0162 }
            int r7 = r11.g     // Catch:{ Exception -> 0x0164, all -> 0x0162 }
            long r7 = (long) r7     // Catch:{ Exception -> 0x0164, all -> 0x0162 }
            long r4 = r4 + r7
            int r0 = r0.read(r12, r4)     // Catch:{ Exception -> 0x0164, all -> 0x0162 }
            if (r0 >= 0) goto L_0x0121
            com.uploader.implement.c.a r11 = new com.uploader.implement.c.a     // Catch:{ Exception -> 0x0164, all -> 0x0162 }
            java.lang.String r12 = "200"
            java.lang.String r0 = "3"
            java.lang.String r1 = "file read failed"
            r11.<init>(r12, r0, r1, r2)     // Catch:{ Exception -> 0x0164, all -> 0x0162 }
            r3.close()     // Catch:{ IOException -> 0x0101 }
            goto L_0x0120
        L_0x0101:
            r12 = move-exception
            boolean r0 = com.uploader.implement.a.a((int) r6)
            if (r0 == 0) goto L_0x0120
            java.lang.String r0 = "UploaderSession"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            int r2 = r10.h
            r1.append(r2)
            java.lang.String r2 = " readFromEntity:"
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            com.uploader.implement.a.a(r6, r0, r1, r12)
        L_0x0120:
            return r11
        L_0x0121:
            int r4 = r11.g     // Catch:{ Exception -> 0x0164, all -> 0x0162 }
            int r4 = r4 + r0
            long r4 = (long) r4     // Catch:{ Exception -> 0x0164, all -> 0x0162 }
            com.uploader.implement.a.h r7 = r11.b     // Catch:{ Exception -> 0x0164, all -> 0x0162 }
            long r7 = r7.d     // Catch:{ Exception -> 0x0164, all -> 0x0162 }
            r9 = 0
            long r4 = r4 - r7
            int r4 = (int) r4     // Catch:{ Exception -> 0x0164, all -> 0x0162 }
            if (r4 <= 0) goto L_0x0137
            int r5 = r12.position()     // Catch:{ Exception -> 0x0164, all -> 0x0162 }
            int r5 = r5 - r4
            r12.position(r5)     // Catch:{ Exception -> 0x0164, all -> 0x0162 }
            int r0 = r0 - r4
        L_0x0137:
            if (r0 <= 0) goto L_0x013e
            int r12 = r11.g     // Catch:{ Exception -> 0x0164, all -> 0x0162 }
            int r12 = r12 + r0
            r11.g = r12     // Catch:{ Exception -> 0x0164, all -> 0x0162 }
        L_0x013e:
            r3.close()     // Catch:{ IOException -> 0x0142 }
            goto L_0x0161
        L_0x0142:
            r11 = move-exception
            boolean r12 = com.uploader.implement.a.a((int) r6)
            if (r12 == 0) goto L_0x0161
            java.lang.String r12 = "UploaderSession"
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            int r2 = r10.h
            r0.append(r2)
            java.lang.String r2 = " readFromEntity:"
            r0.append(r2)
            java.lang.String r0 = r0.toString()
            com.uploader.implement.a.a(r6, r12, r0, r11)
        L_0x0161:
            return r1
        L_0x0162:
            r11 = move-exception
            goto L_0x019e
        L_0x0164:
            r11 = move-exception
            r1 = r3
            goto L_0x016b
        L_0x0167:
            r11 = move-exception
            r3 = r1
            goto L_0x019e
        L_0x016a:
            r11 = move-exception
        L_0x016b:
            com.uploader.implement.c.a r12 = new com.uploader.implement.c.a     // Catch:{ all -> 0x0167 }
            java.lang.String r0 = "200"
            java.lang.String r3 = "3"
            java.lang.String r11 = r11.toString()     // Catch:{ all -> 0x0167 }
            r12.<init>(r0, r3, r11, r2)     // Catch:{ all -> 0x0167 }
            if (r1 == 0) goto L_0x019d
            r1.close()     // Catch:{ IOException -> 0x017e }
            goto L_0x019d
        L_0x017e:
            r11 = move-exception
            boolean r0 = com.uploader.implement.a.a((int) r6)
            if (r0 == 0) goto L_0x019d
            java.lang.String r0 = "UploaderSession"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            int r2 = r10.h
            r1.append(r2)
            java.lang.String r2 = " readFromEntity:"
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            com.uploader.implement.a.a(r6, r0, r1, r11)
        L_0x019d:
            return r12
        L_0x019e:
            if (r3 == 0) goto L_0x01c3
            r3.close()     // Catch:{ IOException -> 0x01a4 }
            goto L_0x01c3
        L_0x01a4:
            r12 = move-exception
            boolean r0 = com.uploader.implement.a.a((int) r6)
            if (r0 == 0) goto L_0x01c3
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            int r1 = r10.h
            r0.append(r1)
            java.lang.String r1 = " readFromEntity:"
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            java.lang.String r1 = "UploaderSession"
            com.uploader.implement.a.a(r6, r1, r0, r12)
        L_0x01c3:
            throw r11
        L_0x01c4:
            com.uploader.implement.c.a r11 = new com.uploader.implement.c.a
            java.lang.String r12 = "200"
            java.lang.String r0 = "3"
            java.lang.String r1 = "file == null || !file.exists()"
            r11.<init>(r12, r0, r1, r2)
            return r11
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uploader.implement.d.c.a(com.uploader.implement.d.c$b, java.nio.ByteBuffer):com.uploader.implement.c.a");
    }

    private com.uploader.implement.c.a b(b bVar, ByteBuffer byteBuffer) {
        byte[] bArr = bVar.b.f;
        int min = Math.min(bArr.length - bVar.f, byteBuffer.remaining());
        if (min < 0) {
            return new com.uploader.implement.c.a(AlipayAuthConstant.LoginResult.SUCCESS, "1", "readFromBytes", false);
        }
        byteBuffer.put(bArr, bVar.f, min);
        bVar.f += min;
        return null;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0032, code lost:
        if (r1 > 0) goto L_0x0038;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void a(int r13) {
        /*
            r12 = this;
            java.util.ArrayList<com.uploader.implement.d.c$b> r0 = r12.c
            java.lang.Object r0 = r0.get(r13)
            com.uploader.implement.d.c$b r0 = (com.uploader.implement.d.c.b) r0
            com.uploader.implement.a.e r1 = r0.a
            com.uploader.implement.b.a r1 = r1.a()
            boolean r1 = r1.e
            r2 = 0
            if (r1 != 0) goto L_0x0035
            com.uploader.implement.a.h r1 = r0.b
            byte[] r1 = r1.f
            if (r1 == 0) goto L_0x001c
            int r1 = r1.length
            int r1 = r1 + r2
            goto L_0x001d
        L_0x001c:
            r1 = 0
        L_0x001d:
            com.uploader.implement.a.h r3 = r0.b
            java.io.File r3 = r3.a
            if (r3 == 0) goto L_0x002a
            long r3 = (long) r1
            com.uploader.implement.a.h r1 = r0.b
            long r5 = r1.d
            long r3 = r3 + r5
            int r1 = (int) r3
        L_0x002a:
            com.uploader.implement.a.h r3 = r0.b
            byte[] r3 = r3.g
            if (r3 == 0) goto L_0x0032
            int r3 = r3.length
            int r1 = r1 + r3
        L_0x0032:
            if (r1 <= 0) goto L_0x0035
            goto L_0x0038
        L_0x0035:
            r1 = 307200(0x4b000, float:4.30479E-40)
        L_0x0038:
            java.nio.ByteBuffer r3 = r0.m
            if (r3 == 0) goto L_0x003f
            r3.clear()
        L_0x003f:
            com.uploader.implement.a.h r4 = r0.b
            byte[] r4 = r4.f
            r5 = 0
            if (r4 == 0) goto L_0x0063
            int r4 = r0.f
            com.uploader.implement.a.h r6 = r0.b
            byte[] r6 = r6.f
            int r6 = r6.length
            if (r4 >= r6) goto L_0x0063
            if (r3 != 0) goto L_0x005e
            java.nio.ByteBuffer r3 = java.nio.ByteBuffer.allocate(r1)     // Catch:{ OutOfMemoryError -> 0x0056 }
            goto L_0x005c
        L_0x0056:
            int r1 = r1 >> 1
            java.nio.ByteBuffer r3 = java.nio.ByteBuffer.allocate(r1)
        L_0x005c:
            r0.m = r3
        L_0x005e:
            com.uploader.implement.c.a r4 = r12.b((com.uploader.implement.d.c.b) r0, (java.nio.ByteBuffer) r3)
            goto L_0x0064
        L_0x0063:
            r4 = r5
        L_0x0064:
            r6 = 1
            if (r4 != 0) goto L_0x0084
            int r7 = r0.g
            long r7 = (long) r7
            com.uploader.implement.a.h r9 = r0.b
            long r9 = r9.d
            int r11 = (r7 > r9 ? 1 : (r7 == r9 ? 0 : -1))
            if (r11 >= 0) goto L_0x0084
            if (r3 != 0) goto L_0x0080
            java.nio.ByteBuffer r3 = java.nio.ByteBuffer.allocate(r1)     // Catch:{ OutOfMemoryError -> 0x0079 }
            goto L_0x007e
        L_0x0079:
            int r1 = r1 >> r6
            java.nio.ByteBuffer r3 = java.nio.ByteBuffer.allocate(r1)
        L_0x007e:
            r0.m = r3
        L_0x0080:
            com.uploader.implement.c.a r4 = r12.a((com.uploader.implement.d.c.b) r0, (java.nio.ByteBuffer) r3)
        L_0x0084:
            if (r4 == 0) goto L_0x008a
            r12.a((int) r13, (com.uploader.implement.c.a) r4)
            return
        L_0x008a:
            java.util.concurrent.atomic.AtomicInteger r13 = g
            int r13 = r13.getAndIncrement()
            r0.h = r13
            boolean r13 = r0.e
            if (r13 != 0) goto L_0x00b9
            int r13 = r0.g
            long r7 = (long) r13
            com.uploader.implement.a.h r13 = r0.b
            long r9 = r13.d
            int r13 = (r7 > r9 ? 1 : (r7 == r9 ? 0 : -1))
            if (r13 != 0) goto L_0x00b9
            com.uploader.implement.a.h r13 = r0.b
            byte[] r13 = r13.g
            if (r3 != 0) goto L_0x00ac
            int r1 = r13.length
            java.nio.ByteBuffer r3 = java.nio.ByteBuffer.allocate(r1)
        L_0x00ac:
            int r1 = r3.remaining()
            int r4 = r13.length
            if (r1 < r4) goto L_0x00b9
            int r1 = r13.length
            r3.put(r13, r2, r1)
            r0.e = r6
        L_0x00b9:
            com.uploader.implement.b.f r13 = r0.l
            if (r13 != 0) goto L_0x00c4
            com.uploader.implement.b.f r13 = new com.uploader.implement.b.f
            r13.<init>()
            r0.l = r13
        L_0x00c4:
            r13.c = r2
            r13.d = r2
            r13.a = r5
            r13.b = r5
            boolean r1 = r0.d
            if (r1 != 0) goto L_0x00d8
            com.uploader.implement.a.h r1 = r0.b
            java.util.Map<java.lang.String, java.lang.String> r1 = r1.e
            r13.a = r1
            r0.d = r6
        L_0x00d8:
            if (r3 == 0) goto L_0x00f2
            int r1 = r3.position()
            r0.i = r1
            byte[] r1 = r3.array()
            r13.b = r1
            int r1 = r3.arrayOffset()
            r13.c = r1
            int r1 = r3.position()
            r13.d = r1
        L_0x00f2:
            r1 = 4
            boolean r2 = com.uploader.implement.a.a((int) r1)
            if (r2 == 0) goto L_0x0188
            java.lang.String r2 = "UploaderSession"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            int r4 = r12.h
            r3.append(r4)
            java.lang.String r4 = " sendRequest, request:"
            r3.append(r4)
            com.uploader.implement.a.e r4 = r0.a
            int r4 = r4.hashCode()
            r3.append(r4)
            java.lang.String r4 = " connection:"
            r3.append(r4)
            com.uploader.implement.b.e r4 = r0.c
            int r4 = r4.hashCode()
            r3.append(r4)
            java.lang.String r4 = " requestData:"
            r3.append(r4)
            java.lang.String r4 = r13.toString()
            r3.append(r4)
            java.lang.String r4 = " currentSendSequence:"
            r3.append(r4)
            int r4 = r0.h
            r3.append(r4)
            java.lang.String r4 = " tailFinish:"
            r3.append(r4)
            boolean r4 = r0.e
            r3.append(r4)
            java.lang.String r4 = " headerFinish:"
            r3.append(r4)
            boolean r4 = r0.d
            r3.append(r4)
            java.lang.String r4 = " entitySizeSent:"
            r3.append(r4)
            int r4 = r0.g
            r3.append(r4)
            java.lang.String r4 = " bytesSizeSent:"
            r3.append(r4)
            int r4 = r0.f
            r3.append(r4)
            java.lang.String r4 = " offset:"
            r3.append(r4)
            com.uploader.implement.a.h r4 = r0.b
            long r4 = r4.c
            r3.append(r4)
            java.lang.String r4 = " length:"
            r3.append(r4)
            com.uploader.implement.a.h r4 = r0.b
            long r4 = r4.d
            r3.append(r4)
            java.lang.String r4 = " requestData length:"
            r3.append(r4)
            int r4 = r13.d
            r3.append(r4)
            java.lang.String r3 = r3.toString()
            com.uploader.implement.a.a(r1, r2, r3)
        L_0x0188:
            com.uploader.implement.b.e r1 = r0.c
            int r2 = r0.h
            r1.a(r13, r2)
            com.uploader.implement.b.e r13 = r0.c
            int r0 = r0.i
            r12.c(r13, r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uploader.implement.d.c.a(int):void");
    }

    public void a(a aVar) {
        this.a = aVar;
    }

    public void a() {
        this.b.clear();
        this.c.clear();
        for (int size = this.d.size() - 1; size >= 0; size--) {
            this.f.removeCallbacks(this.d.remove(size));
        }
        this.e.a(this);
        if (com.uploader.implement.a.a(2)) {
            com.uploader.implement.a.a(2, "UploaderSession", this.h + " release");
        }
    }

    public void a(com.uploader.implement.b.e eVar) {
        this.f.post(new a(1, this, eVar));
    }

    /* access modifiers changed from: package-private */
    public void c(com.uploader.implement.b.e eVar) {
        int a2 = a(eVar, this.c);
        if (a2 != -1) {
            if (com.uploader.implement.a.a(2)) {
                com.uploader.implement.a.a(2, "UploaderSession", this.h + " doConnect, connection:" + eVar.hashCode());
            }
            if (this.a != null) {
                this.a.d(this, this.c.get(a2).a);
            }
            if (this.a != null) {
                this.a.e(this, this.c.get(a2).a);
            }
            a(a2);
        } else if (com.uploader.implement.a.a(8)) {
            com.uploader.implement.a.a(8, "UploaderSession", this.h + " doConnect, NO_POSITION, connection:" + eVar.hashCode());
        }
    }

    /* access modifiers changed from: package-private */
    public void d(com.uploader.implement.b.e eVar) {
        int a2 = a(eVar, this.c);
        if (a2 != -1) {
            if (com.uploader.implement.a.a(2)) {
                com.uploader.implement.a.a(2, "UploaderSession", this.h + " doClose, connection:" + eVar.hashCode());
            }
            eVar.a((com.uploader.implement.b.b) null);
            b bVar = this.c.get(a2);
            bVar.a();
            e(bVar.c);
        } else if (com.uploader.implement.a.a(8)) {
            com.uploader.implement.a.a(8, "UploaderSession", this.h + " doClose, NO_POSITION, connection:" + eVar.hashCode());
        }
    }

    public void a(com.uploader.implement.b.e eVar, com.uploader.implement.c.a aVar) {
        this.f.post(new a(3, this, eVar, aVar));
    }

    /* access modifiers changed from: package-private */
    public void b(com.uploader.implement.b.e eVar, com.uploader.implement.c.a aVar) {
        int a2 = a(eVar, this.c);
        if (a2 != -1) {
            if (com.uploader.implement.a.a(2)) {
                com.uploader.implement.a.a(2, "UploaderSession", this.h + " doError, connection:" + eVar.hashCode() + " error:" + aVar.toString() + " sendingList.size:" + this.c.size());
            }
            b bVar = this.c.get(a2);
            bVar.a();
            e(bVar.c);
            a(a2, aVar);
        } else if (com.uploader.implement.a.a(8)) {
            com.uploader.implement.a.a(8, "UploaderSession", this.h + " doError, NO_POSITION, connection:" + eVar.hashCode());
        }
    }

    private void a(int i2, com.uploader.implement.c.a aVar) {
        b remove = this.c.remove(i2);
        if (com.uploader.implement.a.a(2)) {
            com.uploader.implement.a.a(2, "UploaderSession", this.h + " notifyError, request:" + remove.a.hashCode());
        }
        if (this.a != null) {
            this.a.b(this, remove.a, aVar);
        }
    }

    public void a(com.uploader.implement.b.e eVar, f fVar) {
        this.f.post(new a(4, this, eVar, fVar));
    }

    /* access modifiers changed from: package-private */
    public void b(com.uploader.implement.b.e eVar, f fVar) {
        int a2 = a(eVar, this.c);
        if (a2 != -1) {
            if (com.uploader.implement.a.a(2)) {
                com.uploader.implement.a.a(2, "UploaderSession", this.h + " doReceive, sendingList.size:" + this.c.size() + " index:" + a2 + " connection:" + eVar.hashCode() + " data:" + fVar.toString());
            }
            b bVar = this.c.get(a2);
            c(bVar.c, bVar.i);
            a(bVar, fVar);
            ArrayList arrayList = null;
            do {
                Pair<com.uploader.implement.a.f, Integer> a3 = bVar.a.a(bVar.k, bVar.j.array(), bVar.j.arrayOffset(), bVar.j.position());
                if (arrayList == null) {
                    arrayList = new ArrayList();
                }
                arrayList.add(a3);
                if (a3.first == null) {
                    break;
                }
                bVar.j.flip();
                bVar.j.get(new byte[((Integer) a3.second).intValue()], 0, ((Integer) a3.second).intValue());
                bVar.j.compact();
            } while (bVar.j.position() >= 4);
            if (arrayList != null) {
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    Pair pair = (Pair) it.next();
                    if (pair.first == null) {
                        if (((Integer) pair.second).intValue() < 0) {
                            a(a2, new com.uploader.implement.c.a("400", "2", "response == null && divide < 0", true));
                            return;
                        }
                        return;
                    } else if (this.a != null) {
                        this.a.a((b) this, bVar.a, (com.uploader.implement.a.f) pair.first);
                    }
                }
            }
        } else if (com.uploader.implement.a.a(8)) {
            com.uploader.implement.a.a(8, "UploaderSession", this.h + " doReceive, NO_POSITION, connection:" + eVar.hashCode());
        }
    }

    private static void a(b bVar, f fVar) {
        if (bVar.j == null) {
            bVar.j = ByteBuffer.allocate(128);
            bVar.k = fVar.a;
        }
        int position = bVar.j.position() + fVar.b.length;
        if (bVar.j.capacity() < position) {
            bVar.j.flip();
            bVar.j = ByteBuffer.allocate(position).put(bVar.j);
        }
        bVar.j.put(fVar.b);
    }

    public void a(com.uploader.implement.b.e eVar, int i2) {
        this.f.postDelayed(new a(6, this, eVar, Integer.valueOf(i2)), this.i.b.enableFlowControl() ? 100 : 0);
    }

    private void c(com.uploader.implement.b.e eVar, int i2) {
        a aVar;
        int b2 = b(eVar, this.d);
        if (b2 == -1) {
            aVar = new a(8, this, eVar);
            this.d.add(aVar);
        } else {
            aVar = this.d.get(b2);
            this.f.removeCallbacks(aVar);
        }
        this.f.postDelayed(aVar, (long) ((i2 / 102400) + 30000));
    }

    private void e(com.uploader.implement.b.e eVar) {
        int b2 = b(eVar, this.d);
        if (b2 != -1) {
            this.f.removeCallbacks(this.d.remove(b2));
            if (com.uploader.implement.a.a(2)) {
                com.uploader.implement.a.a(2, "UploaderSession", this.h + " clearTimeout, connection:" + eVar.hashCode());
            }
        } else if (com.uploader.implement.a.a(8)) {
            com.uploader.implement.a.a(8, "UploaderSession", this.h + " clearTimeout, NO_POSITION, connection:" + eVar.hashCode());
        }
    }

    private static int a(com.uploader.implement.b.e eVar, ArrayList<b> arrayList) {
        int size = arrayList.size();
        for (int i2 = 0; i2 < size; i2++) {
            if (arrayList.get(i2).c.equals(eVar)) {
                return i2;
            }
        }
        return -1;
    }

    private static int a(e eVar, ArrayList<b> arrayList) {
        int size = arrayList.size();
        for (int i2 = 0; i2 < size; i2++) {
            if (arrayList.get(i2).a.equals(eVar)) {
                return i2;
            }
        }
        return -1;
    }

    private static int b(com.uploader.implement.b.e eVar, ArrayList<a> arrayList) {
        int size = arrayList.size();
        for (int i2 = 0; i2 < size; i2++) {
            if (arrayList.get(i2).c[0].equals(eVar)) {
                return i2;
            }
        }
        return -1;
    }

    public void b(com.uploader.implement.b.e eVar, int i2) {
        this.f.post(new a(5, this, eVar, Integer.valueOf(i2)));
    }

    /* access modifiers changed from: package-private */
    public void a(com.uploader.implement.b.e eVar, int i2, boolean z) {
        int a2 = a(eVar, this.c);
        if (a2 != -1) {
            b bVar = this.c.get(a2);
            boolean b2 = bVar.b();
            if (com.uploader.implement.a.a(2)) {
                com.uploader.implement.a.a(2, "UploaderSession", this.h + " doSend, begin:" + z + " connection:" + eVar.hashCode() + " sendSequence:" + i2 + " isFinished:" + b2);
            }
            if (z) {
                if (this.a != null) {
                    this.a.a((b) this, bVar.a, bVar.g);
                }
            } else if (!b2) {
                a(a2);
                return;
            } else if (this.a != null) {
                this.a.b(this, bVar.a);
            }
            c(bVar.c, bVar.i);
        } else if (com.uploader.implement.a.a(8)) {
            com.uploader.implement.a.a(8, "UploaderSession", this.h + " doSend, NO_POSITION, connection:" + eVar.hashCode());
        }
    }

    public void a(b bVar, e eVar, com.uploader.implement.b.e eVar2) {
        this.f.post(new a(7, this, bVar, eVar, eVar2));
    }

    /* access modifiers changed from: package-private */
    public void b(b bVar, e eVar, com.uploader.implement.b.e eVar2) {
        boolean z = !this.b.remove(eVar);
        boolean d2 = eVar2.d();
        if (com.uploader.implement.a.a(4)) {
            com.uploader.implement.a.a(4, "UploaderSession", this.h + " onAvailable.session:" + bVar.hashCode() + " request:" + eVar.hashCode() + " noWaitingRequest:" + z + " connection:" + eVar2.hashCode() + " needConnect:" + d2 + " target:" + eVar.a());
        }
        if (!z) {
            eVar2.a(this);
            b bVar2 = new b(eVar, eVar2);
            this.c.add(bVar2);
            if (d2) {
                if (this.a != null) {
                    this.a.c(this, bVar2.a);
                }
                eVar2.b();
                return;
            }
            if (this.a != null) {
                this.a.e(this, bVar2.a);
            }
            a(this.c.size() - 1);
        }
    }
}
