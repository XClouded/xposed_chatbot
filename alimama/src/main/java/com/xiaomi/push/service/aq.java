package com.xiaomi.push.service;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import com.xiaomi.push.as;
import com.xiaomi.push.cq;
import com.xiaomi.push.ct;
import com.xiaomi.push.cu;
import com.xiaomi.push.ed;
import com.xiaomi.push.ee;
import com.xiaomi.push.fb;
import com.xiaomi.push.fm;
import com.xiaomi.push.go;
import com.xiaomi.push.gy;
import com.xiaomi.push.ha;
import com.xiaomi.push.service.ba;
import com.xiaomi.push.t;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

public class aq extends ba.a implements cu.a {
    private long a;

    /* renamed from: a  reason: collision with other field name */
    private XMPushService f865a;

    static class a implements cu.b {
        a() {
        }

        public String a(String str) {
            Uri.Builder buildUpon = Uri.parse(str).buildUpon();
            buildUpon.appendQueryParameter("sdkver", String.valueOf(38));
            buildUpon.appendQueryParameter("osver", String.valueOf(Build.VERSION.SDK_INT));
            buildUpon.appendQueryParameter("os", go.a(Build.MODEL + ":" + Build.VERSION.INCREMENTAL));
            buildUpon.appendQueryParameter("mi", String.valueOf(t.a()));
            String builder = buildUpon.toString();
            com.xiaomi.channel.commonutils.logger.b.c("fetch bucket from : " + builder);
            URL url = new URL(builder);
            int port = url.getPort() == -1 ? 80 : url.getPort();
            try {
                long currentTimeMillis = System.currentTimeMillis();
                String a = as.a(t.a(), url);
                long currentTimeMillis2 = System.currentTimeMillis() - currentTimeMillis;
                ha.a(url.getHost() + ":" + port, (int) currentTimeMillis2, (Exception) null);
                return a;
            } catch (IOException e) {
                ha.a(url.getHost() + ":" + port, -1, e);
                throw e;
            }
        }
    }

    static class b extends cu {
        protected b(Context context, ct ctVar, cu.b bVar, String str) {
            super(context, ctVar, bVar, str);
        }

        /* access modifiers changed from: protected */
        public String a(ArrayList<String> arrayList, String str, String str2, boolean z) {
            try {
                if (gy.a().a()) {
                    str2 = ba.a();
                }
                return super.a(arrayList, str, str2, z);
            } catch (IOException e) {
                ha.a(0, fb.GSLB_ERR.a(), 1, (String) null, as.b(a) ? 1 : 0);
                throw e;
            }
        }
    }

    aq(XMPushService xMPushService) {
        this.f865a = xMPushService;
    }

    public static void a(XMPushService xMPushService) {
        aq aqVar = new aq(xMPushService);
        ba.a().a((ba.a) aqVar);
        synchronized (cu.class) {
            cu.a((cu.a) aqVar);
            cu.a(xMPushService, (ct) null, new a(), "0", "push", "2.2");
        }
    }

    public cu a(Context context, ct ctVar, cu.b bVar, String str) {
        return new b(context, ctVar, bVar, str);
    }

    public void a(ed.a aVar) {
    }

    public void a(ee.b bVar) {
        cq b2;
        if (bVar.b() && bVar.a() && System.currentTimeMillis() - this.a > 3600000) {
            com.xiaomi.channel.commonutils.logger.b.a("fetch bucket :" + bVar.a());
            this.a = System.currentTimeMillis();
            cu a2 = cu.a();
            a2.a();
            a2.b();
            fm a3 = this.f865a.a();
            if (a3 != null && (b2 = a2.b(a3.a().c())) != null) {
                ArrayList a4 = b2.a();
                boolean z = true;
                Iterator it = a4.iterator();
                while (true) {
                    if (it.hasNext()) {
                        if (((String) it.next()).equals(a3.a())) {
                            z = false;
                            break;
                        }
                    } else {
                        break;
                    }
                }
                if (z && !a4.isEmpty()) {
                    com.xiaomi.channel.commonutils.logger.b.a("bucket changed, force reconnect");
                    this.f865a.a(0, (Exception) null);
                    this.f865a.a(false);
                }
            }
        }
    }
}
