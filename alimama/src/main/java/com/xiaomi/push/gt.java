package com.xiaomi.push;

import com.xiaomi.push.service.XMPushService;
import com.xiaomi.push.service.al;

class gt implements al.b.a {
    private int a;

    /* renamed from: a  reason: collision with other field name */
    private fm f426a;

    /* renamed from: a  reason: collision with other field name */
    private XMPushService f427a;

    /* renamed from: a  reason: collision with other field name */
    private al.b f428a;

    /* renamed from: a  reason: collision with other field name */
    private al.c f429a;

    /* renamed from: a  reason: collision with other field name */
    private boolean f430a = false;

    gt(XMPushService xMPushService, al.b bVar) {
        this.f427a = xMPushService;
        this.f429a = al.c.binding;
        this.f428a = bVar;
    }

    private void b() {
        this.f428a.b(this);
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void c() {
        /*
            r3 = this;
            r3.b()
            boolean r0 = r3.f430a
            if (r0 != 0) goto L_0x0008
            return
        L_0x0008:
            int r0 = r3.a
            r1 = 11
            if (r0 != r1) goto L_0x000f
            return
        L_0x000f:
            com.xiaomi.push.gy r0 = com.xiaomi.push.gy.a()
            com.xiaomi.push.fc r0 = r0.a()
            int[] r1 = com.xiaomi.push.gv.a
            com.xiaomi.push.service.al$c r2 = r3.f429a
            int r2 = r2.ordinal()
            r1 = r1[r2]
            switch(r1) {
                case 1: goto L_0x002e;
                case 2: goto L_0x005b;
                case 3: goto L_0x0025;
                default: goto L_0x0024;
            }
        L_0x0024:
            goto L_0x005b
        L_0x0025:
            com.xiaomi.push.fb r1 = com.xiaomi.push.fb.BIND_SUCCESS
        L_0x0027:
            int r1 = r1.a()
            r0.f333a = r1
            goto L_0x005b
        L_0x002e:
            int r1 = r3.a
            r2 = 17
            if (r1 != r2) goto L_0x0037
            com.xiaomi.push.fb r1 = com.xiaomi.push.fb.BIND_TCP_READ_TIMEOUT
            goto L_0x0027
        L_0x0037:
            int r1 = r3.a
            r2 = 21
            if (r1 != r2) goto L_0x0040
            com.xiaomi.push.fb r1 = com.xiaomi.push.fb.BIND_TIMEOUT
            goto L_0x0027
        L_0x0040:
            com.xiaomi.push.gx r1 = com.xiaomi.push.gy.a()     // Catch:{ NullPointerException -> 0x005a }
            java.lang.Exception r1 = r1.a()     // Catch:{ NullPointerException -> 0x005a }
            com.xiaomi.push.gw$a r1 = com.xiaomi.push.gw.c(r1)     // Catch:{ NullPointerException -> 0x005a }
            com.xiaomi.push.fb r2 = r1.a     // Catch:{ NullPointerException -> 0x005a }
            int r2 = r2.a()     // Catch:{ NullPointerException -> 0x005a }
            r0.f333a = r2     // Catch:{ NullPointerException -> 0x005a }
            java.lang.String r1 = r1.f431a     // Catch:{ NullPointerException -> 0x005a }
            r0.c((java.lang.String) r1)     // Catch:{ NullPointerException -> 0x005a }
            goto L_0x005b
        L_0x005a:
            r0 = 0
        L_0x005b:
            if (r0 == 0) goto L_0x0083
            com.xiaomi.push.fm r1 = r3.f426a
            java.lang.String r1 = r1.a()
            r0.b((java.lang.String) r1)
            com.xiaomi.push.service.al$b r1 = r3.f428a
            java.lang.String r1 = r1.f859b
            r0.d((java.lang.String) r1)
            r1 = 1
            r0.f336b = r1
            com.xiaomi.push.service.al$b r1 = r3.f428a     // Catch:{ NumberFormatException -> 0x007c }
            java.lang.String r1 = r1.g     // Catch:{ NumberFormatException -> 0x007c }
            int r1 = java.lang.Integer.parseInt(r1)     // Catch:{ NumberFormatException -> 0x007c }
            byte r1 = (byte) r1     // Catch:{ NumberFormatException -> 0x007c }
            r0.a((byte) r1)     // Catch:{ NumberFormatException -> 0x007c }
        L_0x007c:
            com.xiaomi.push.gy r1 = com.xiaomi.push.gy.a()
            r1.a((com.xiaomi.push.fc) r0)
        L_0x0083:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.gt.c():void");
    }

    /* access modifiers changed from: package-private */
    public void a() {
        this.f428a.a((al.b.a) this);
        this.f426a = this.f427a.a();
    }

    public void a(al.c cVar, al.c cVar2, int i) {
        if (!this.f430a && cVar == al.c.binding) {
            this.f429a = cVar2;
            this.a = i;
            this.f430a = true;
        }
        this.f427a.a((XMPushService.i) new gu(this, 4));
    }
}
