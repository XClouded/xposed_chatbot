package com.xiaomi.push.service;

import android.content.Context;
import android.text.TextUtils;
import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.push.cq;
import com.xiaomi.push.cu;
import com.xiaomi.push.dd;
import com.xiaomi.push.ee;
import com.xiaomi.push.fb;
import com.xiaomi.push.ff;
import com.xiaomi.push.fn;
import com.xiaomi.push.ga;
import com.xiaomi.push.gb;
import com.xiaomi.push.gc;
import com.xiaomi.push.gd;
import com.xiaomi.push.gr;
import com.xiaomi.push.ha;
import com.xiaomi.push.service.XMPushService;
import com.xiaomi.push.service.al;
import java.util.Date;

public class ak {
    private XMPushService a;

    ak(XMPushService xMPushService) {
        this.a = xMPushService;
    }

    private void a(ga gaVar) {
        String c = gaVar.c();
        if (!TextUtils.isEmpty(c)) {
            String[] split = c.split(";");
            cq a2 = cu.a().a(fn.a(), false);
            if (a2 != null && split.length > 0) {
                a2.a(split);
                this.a.a(20, (Exception) null);
                this.a.a(true);
            }
        }
    }

    private void b(gd gdVar) {
        al.b a2;
        String l = gdVar.l();
        String k = gdVar.k();
        if (!TextUtils.isEmpty(l) && !TextUtils.isEmpty(k) && (a2 = al.a().a(k, l)) != null) {
            gr.a(this.a, a2.f856a, (long) gr.a(gdVar.a()), true, true, System.currentTimeMillis());
        }
    }

    private void c(ff ffVar) {
        al.b a2;
        String g = ffVar.g();
        String num = Integer.toString(ffVar.a());
        if (!TextUtils.isEmpty(g) && !TextUtils.isEmpty(num) && (a2 = al.a().a(num, g)) != null) {
            gr.a(this.a, a2.f856a, (long) ffVar.c(), true, true, System.currentTimeMillis());
        }
    }

    public void a(ff ffVar) {
        if (5 != ffVar.a()) {
            c(ffVar);
        }
        try {
            b(ffVar);
        } catch (Exception e) {
            b.a("handle Blob chid = " + ffVar.a() + " cmd = " + ffVar.a() + " packetid = " + ffVar.e() + " failure ", (Throwable) e);
        }
    }

    public void a(gd gdVar) {
        if (!"5".equals(gdVar.k())) {
            b(gdVar);
        }
        String k = gdVar.k();
        if (TextUtils.isEmpty(k)) {
            k = "1";
            gdVar.l(k);
        }
        if (k.equals("0")) {
            b.a("Received wrong packet with chid = 0 : " + gdVar.a());
        }
        if (gdVar instanceof gb) {
            ga a2 = gdVar.a("kick");
            if (a2 != null) {
                String l = gdVar.l();
                String a3 = a2.a("type");
                String a4 = a2.a("reason");
                b.a("kicked by server, chid=" + k + " res=" + al.b.a(l) + " type=" + a3 + " reason=" + a4);
                if ("wait".equals(a3)) {
                    al.b a5 = al.a().a(k, l);
                    if (a5 != null) {
                        this.a.a(a5);
                        a5.a(al.c.unbind, 3, 0, a4, a3);
                        return;
                    }
                    return;
                }
                this.a.a(k, l, 3, a4, a3);
                al.a().a(k, l);
                return;
            }
        } else if (gdVar instanceof gc) {
            gc gcVar = (gc) gdVar;
            if ("redir".equals(gcVar.b())) {
                ga a6 = gcVar.a("hosts");
                if (a6 != null) {
                    a(a6);
                    return;
                }
                return;
            }
        }
        this.a.b().a(this.a, k, gdVar);
    }

    public void b(ff ffVar) {
        StringBuilder sb;
        String a2;
        String str;
        al.c cVar;
        int i;
        int i2;
        String a3 = ffVar.a();
        if (ffVar.a() != 0) {
            String num = Integer.toString(ffVar.a());
            if ("SECMSG".equals(ffVar.a())) {
                if (!ffVar.a()) {
                    this.a.b().a(this.a, num, ffVar);
                    return;
                }
                sb = new StringBuilder();
                sb.append("Recv SECMSG errCode = ");
                sb.append(ffVar.b());
                sb.append(" errStr = ");
                a2 = ffVar.c();
            } else if ("BIND".equals(a3)) {
                ee.d a4 = ee.d.a(ffVar.a());
                String g = ffVar.g();
                al.b a5 = al.a().a(num, g);
                if (a5 != null) {
                    if (a4.a()) {
                        b.a("SMACK: channel bind succeeded, chid=" + ffVar.a());
                        a5.a(al.c.binded, 1, 0, (String) null, (String) null);
                        return;
                    }
                    String a6 = a4.a();
                    if ("auth".equals(a6)) {
                        if ("invalid-sig".equals(a4.b())) {
                            b.a("SMACK: bind error invalid-sig token = " + a5.c + " sec = " + a5.h);
                            ha.a(0, fb.BIND_INVALID_SIG.a(), 1, (String) null, 0);
                        }
                        cVar = al.c.unbind;
                        i = 1;
                        i2 = 5;
                    } else if ("cancel".equals(a6)) {
                        cVar = al.c.unbind;
                        i = 1;
                        i2 = 7;
                    } else {
                        if ("wait".equals(a6)) {
                            this.a.a(a5);
                            a5.a(al.c.unbind, 1, 7, a4.b(), a6);
                        }
                        str = "SMACK: channel bind failed, chid=" + num + " reason=" + a4.b();
                        b.a(str);
                    }
                    a5.a(cVar, i, i2, a4.b(), a6);
                    al.a().a(num, g);
                    str = "SMACK: channel bind failed, chid=" + num + " reason=" + a4.b();
                    b.a(str);
                }
                return;
            } else if ("KICK".equals(a3)) {
                ee.g a7 = ee.g.a(ffVar.a());
                String g2 = ffVar.g();
                String a8 = a7.a();
                String b = a7.b();
                b.a("kicked by server, chid=" + num + " res= " + al.b.a(g2) + " type=" + a8 + " reason=" + b);
                if ("wait".equals(a8)) {
                    al.b a9 = al.a().a(num, g2);
                    if (a9 != null) {
                        this.a.a(a9);
                        a9.a(al.c.unbind, 3, 0, b, a8);
                        return;
                    }
                    return;
                }
                this.a.a(num, g2, 3, b, a8);
                al.a().a(num, g2);
                return;
            } else {
                return;
            }
        } else if ("PING".equals(a3)) {
            byte[] a10 = ffVar.a();
            if (a10 != null && a10.length > 0) {
                ee.j a11 = ee.j.a(a10);
                if (a11.b()) {
                    ba.a().a(a11.a());
                }
            }
            if (!"com.xiaomi.xmsf".equals(this.a.getPackageName())) {
                this.a.a();
            }
            if ("1".equals(ffVar.e())) {
                b.a("received a server ping");
            } else {
                ha.b();
            }
            this.a.b();
            return;
        } else if ("SYNC".equals(a3)) {
            if ("CONF".equals(ffVar.b())) {
                ba.a().a(ee.b.a(ffVar.a()));
                return;
            } else if (TextUtils.equals("U", ffVar.b())) {
                ee.k a12 = ee.k.a(ffVar.a());
                dd.a((Context) this.a).a(a12.a(), a12.b(), new Date(a12.a()), new Date(a12.b()), a12.c() * 1024, a12.e());
                ff ffVar2 = new ff();
                ffVar2.a(0);
                ffVar2.a(ffVar.a(), "UCA");
                ffVar2.a(ffVar.e());
                this.a.a((XMPushService.i) new aw(this.a, ffVar2));
                return;
            } else if (TextUtils.equals("P", ffVar.b())) {
                ee.i a13 = ee.i.a(ffVar.a());
                ff ffVar3 = new ff();
                ffVar3.a(0);
                ffVar3.a(ffVar.a(), "PCA");
                ffVar3.a(ffVar.e());
                ee.i iVar = new ee.i();
                if (a13.a()) {
                    iVar.a(a13.a());
                }
                ffVar3.a(iVar.a(), (String) null);
                this.a.a((XMPushService.i) new aw(this.a, ffVar3));
                sb = new StringBuilder();
                sb.append("ACK msgP: id = ");
                a2 = ffVar.e();
            } else {
                return;
            }
        } else if ("NOTIFY".equals(ffVar.a())) {
            ee.h a14 = ee.h.a(ffVar.a());
            sb = new StringBuilder();
            sb.append("notify by server err = ");
            sb.append(a14.c());
            sb.append(" desc = ");
            a2 = a14.a();
        } else {
            return;
        }
        sb.append(a2);
        str = sb.toString();
        b.a(str);
    }
}
