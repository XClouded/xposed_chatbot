package com.xiaomi.mipush.sdk;

import android.content.Context;
import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.push.hl;
import com.xiaomi.push.service.ag;
import java.util.HashMap;
import java.util.Map;

public class g implements AbstractPushManager {
    private static volatile g a;

    /* renamed from: a  reason: collision with other field name */
    private Context f64a;

    /* renamed from: a  reason: collision with other field name */
    private PushConfiguration f65a;

    /* renamed from: a  reason: collision with other field name */
    private Map<f, AbstractPushManager> f66a = new HashMap();
    /* access modifiers changed from: private */

    /* renamed from: a  reason: collision with other field name */
    public boolean f67a = false;

    private g(Context context) {
        this.f64a = context.getApplicationContext();
    }

    public static g a(Context context) {
        if (a == null) {
            synchronized (g.class) {
                if (a == null) {
                    a = new g(context);
                }
            }
        }
        return a;
    }

    /* JADX WARNING: type inference failed for: r1v13, types: [boolean] */
    /* JADX WARNING: type inference failed for: r1v27, types: [boolean] */
    /* JADX WARNING: type inference failed for: r1v43, types: [boolean] */
    private void a() {
        AbstractPushManager a2;
        AbstractPushManager a3;
        AbstractPushManager a4;
        AbstractPushManager a5;
        if (this.f65a != null) {
            if (this.f65a.getOpenHmsPush()) {
                StringBuilder sb = new StringBuilder();
                sb.append("ASSEMBLE_PUSH : ");
                sb.append(" HW user switch : " + this.f65a.getOpenHmsPush() + " HW online switch : " + j.a(this.f64a, f.ASSEMBLE_PUSH_HUAWEI) + " HW isSupport : " + ap.HUAWEI.equals(o.a(this.f64a)));
                b.a(sb.toString());
            }
            if (this.f65a.getOpenHmsPush() && j.a(this.f64a, f.ASSEMBLE_PUSH_HUAWEI) != null && ap.HUAWEI.equals(o.a(this.f64a))) {
                if (!a(f.ASSEMBLE_PUSH_HUAWEI)) {
                    a(f.ASSEMBLE_PUSH_HUAWEI, at.a(this.f64a, f.ASSEMBLE_PUSH_HUAWEI));
                }
                b.c("hw manager add to list");
            } else if (a(f.ASSEMBLE_PUSH_HUAWEI) && (a5 = a(f.ASSEMBLE_PUSH_HUAWEI)) != null) {
                a(f.ASSEMBLE_PUSH_HUAWEI);
                a5.unregister();
            }
            if (this.f65a.getOpenFCMPush()) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("ASSEMBLE_PUSH : ");
                sb2.append(" FCM user switch : " + this.f65a.getOpenFCMPush() + " FCM online switch : " + j.a(this.f64a, f.ASSEMBLE_PUSH_FCM) + " FCM isSupport : " + o.a(this.f64a));
                b.a(sb2.toString());
            }
            if (this.f65a.getOpenFCMPush() && j.a(this.f64a, f.ASSEMBLE_PUSH_FCM) != null && o.a(this.f64a)) {
                if (!a(f.ASSEMBLE_PUSH_FCM)) {
                    a(f.ASSEMBLE_PUSH_FCM, at.a(this.f64a, f.ASSEMBLE_PUSH_FCM));
                }
                b.c("fcm manager add to list");
            } else if (a(f.ASSEMBLE_PUSH_FCM) && (a4 = a(f.ASSEMBLE_PUSH_FCM)) != null) {
                a(f.ASSEMBLE_PUSH_FCM);
                a4.unregister();
            }
            if (this.f65a.getOpenCOSPush()) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append("ASSEMBLE_PUSH : ");
                sb3.append(" COS user switch : " + this.f65a.getOpenCOSPush() + " COS online switch : " + j.a(this.f64a, f.ASSEMBLE_PUSH_COS) + " COS isSupport : " + o.b(this.f64a));
                b.a(sb3.toString());
            }
            if (this.f65a.getOpenCOSPush() && j.a(this.f64a, f.ASSEMBLE_PUSH_COS) != null && o.b(this.f64a)) {
                a(f.ASSEMBLE_PUSH_COS, at.a(this.f64a, f.ASSEMBLE_PUSH_COS));
            } else if (a(f.ASSEMBLE_PUSH_COS) && (a3 = a(f.ASSEMBLE_PUSH_COS)) != null) {
                a(f.ASSEMBLE_PUSH_COS);
                a3.unregister();
            }
            if (this.f65a.getOpenFTOSPush() && j.a(this.f64a, f.ASSEMBLE_PUSH_FTOS) != null && o.c(this.f64a)) {
                a(f.ASSEMBLE_PUSH_FTOS, at.a(this.f64a, f.ASSEMBLE_PUSH_FTOS));
            } else if (a(f.ASSEMBLE_PUSH_FTOS) && (a2 = a(f.ASSEMBLE_PUSH_FTOS)) != null) {
                a(f.ASSEMBLE_PUSH_FTOS);
                a2.unregister();
            }
        }
    }

    public AbstractPushManager a(f fVar) {
        return this.f66a.get(fVar);
    }

    public void a(PushConfiguration pushConfiguration) {
        this.f65a = pushConfiguration;
        this.f67a = ag.a(this.f64a).a(hl.AggregatePushSwitch.a(), true);
        if (this.f65a.getOpenHmsPush() || this.f65a.getOpenFCMPush() || this.f65a.getOpenCOSPush()) {
            ag.a(this.f64a).a((ag.a) new h(this, 101, "assemblePush"));
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m65a(f fVar) {
        this.f66a.remove(fVar);
    }

    public void a(f fVar, AbstractPushManager abstractPushManager) {
        if (abstractPushManager != null) {
            if (this.f66a.containsKey(fVar)) {
                this.f66a.remove(fVar);
            }
            this.f66a.put(fVar, abstractPushManager);
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m66a(f fVar) {
        return this.f66a.containsKey(fVar);
    }

    public boolean b(f fVar) {
        boolean z = false;
        switch (i.a[fVar.ordinal()]) {
            case 1:
                if (this.f65a != null) {
                    return this.f65a.getOpenHmsPush();
                }
                return false;
            case 2:
                if (this.f65a != null) {
                    return this.f65a.getOpenFCMPush();
                }
                return false;
            case 3:
                if (this.f65a != null) {
                    z = this.f65a.getOpenCOSPush();
                    break;
                }
                break;
            case 4:
                break;
            default:
                return false;
        }
        return this.f65a != null ? this.f65a.getOpenFTOSPush() : z;
    }

    public void register() {
        b.a("ASSEMBLE_PUSH : assemble push register");
        if (this.f66a.size() <= 0) {
            a();
        }
        if (this.f66a.size() > 0) {
            for (AbstractPushManager next : this.f66a.values()) {
                if (next != null) {
                    next.register();
                }
            }
            j.a(this.f64a);
        }
    }

    public void unregister() {
        b.a("ASSEMBLE_PUSH : assemble push unregister");
        for (AbstractPushManager next : this.f66a.values()) {
            if (next != null) {
                next.unregister();
            }
        }
        this.f66a.clear();
    }
}
