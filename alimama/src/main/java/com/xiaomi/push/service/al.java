package com.xiaomi.push.service;

import android.content.Context;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.text.TextUtils;
import com.taobao.android.dinamic.DinamicConstant;
import com.xiaomi.push.service.XMPushService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import mtopsdk.mtop.intf.Mtop;

public class al {
    private static al a;

    /* renamed from: a  reason: collision with other field name */
    private List<a> f846a = new ArrayList();

    /* renamed from: a  reason: collision with other field name */
    private ConcurrentHashMap<String, HashMap<String, b>> f847a = new ConcurrentHashMap<>();

    public interface a {
        void a();
    }

    public static class b {
        private int a = 0;

        /* renamed from: a  reason: collision with other field name */
        public Context f848a;

        /* renamed from: a  reason: collision with other field name */
        IBinder.DeathRecipient f849a = null;

        /* renamed from: a  reason: collision with other field name */
        Messenger f850a;

        /* renamed from: a  reason: collision with other field name */
        private XMPushService.b f851a = new XMPushService.b(this);

        /* renamed from: a  reason: collision with other field name */
        private XMPushService f852a;

        /* renamed from: a  reason: collision with other field name */
        final C0033b f853a = new C0033b();

        /* renamed from: a  reason: collision with other field name */
        c f854a = c.unbind;

        /* renamed from: a  reason: collision with other field name */
        public d f855a;

        /* renamed from: a  reason: collision with other field name */
        public String f856a;

        /* renamed from: a  reason: collision with other field name */
        private List<a> f857a = new ArrayList();

        /* renamed from: a  reason: collision with other field name */
        public boolean f858a;
        c b = null;

        /* renamed from: b  reason: collision with other field name */
        public String f859b;

        /* renamed from: b  reason: collision with other field name */
        private boolean f860b = false;
        public String c;
        public String d;
        public String e;
        public String f;
        public String g;
        public String h;
        public String i;

        public interface a {
            void a(c cVar, c cVar2, int i);
        }

        /* renamed from: com.xiaomi.push.service.al$b$b  reason: collision with other inner class name */
        class C0033b extends XMPushService.i {

            /* renamed from: a  reason: collision with other field name */
            String f861a;
            int b;

            /* renamed from: b  reason: collision with other field name */
            String f862b;
            int c;

            public C0033b() {
                super(0);
            }

            public XMPushService.i a(int i, int i2, String str, String str2) {
                this.b = i;
                this.c = i2;
                this.f862b = str2;
                this.f861a = str;
                return this;
            }

            public String a() {
                return "notify job";
            }

            /* renamed from: a  reason: collision with other method in class */
            public void m576a() {
                if (b.this.a(this.b, this.c, this.f862b)) {
                    b.this.a(this.b, this.c, this.f861a, this.f862b);
                    return;
                }
                com.xiaomi.channel.commonutils.logger.b.b(" ignore notify client :" + b.this.g);
            }
        }

        class c implements IBinder.DeathRecipient {
            final Messenger a;

            /* renamed from: a  reason: collision with other field name */
            final b f863a;

            c(b bVar, Messenger messenger) {
                this.f863a = bVar;
                this.a = messenger;
            }

            public void binderDied() {
                com.xiaomi.channel.commonutils.logger.b.b("peer died, chid = " + this.f863a.g);
                b.a(b.this).a((XMPushService.i) new an(this, 0), 0);
                if ("9".equals(this.f863a.g) && "com.xiaomi.xmsf".equals(b.a(b.this).getPackageName())) {
                    b.a(b.this).a((XMPushService.i) new ao(this, 0), 60000);
                }
            }
        }

        public b() {
        }

        public b(XMPushService xMPushService) {
            this.f852a = xMPushService;
            a((a) new am(this));
        }

        /* JADX WARNING: Code restructure failed: missing block: B:3:0x0009, code lost:
            r1 = r3.lastIndexOf("/");
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public static java.lang.String a(java.lang.String r3) {
            /*
                java.lang.String r0 = ""
                boolean r1 = android.text.TextUtils.isEmpty(r3)
                if (r1 == 0) goto L_0x0009
                return r0
            L_0x0009:
                java.lang.String r1 = "/"
                int r1 = r3.lastIndexOf(r1)
                r2 = -1
                if (r1 == r2) goto L_0x0018
                int r1 = r1 + 1
                java.lang.String r0 = r3.substring(r1)
            L_0x0018:
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.service.al.b.a(java.lang.String):java.lang.String");
        }

        /* access modifiers changed from: private */
        public void a(int i2, int i3, String str, String str2) {
            this.b = this.f854a;
            if (i2 == 2) {
                this.f855a.a(this.f848a, this, i3);
            } else if (i2 == 3) {
                this.f855a.a(this.f848a, this, str2, str);
            } else if (i2 == 1) {
                boolean z = this.f854a == c.binded;
                if (!z && "wait".equals(str2)) {
                    this.a++;
                } else if (z) {
                    this.a = 0;
                    if (this.f850a != null) {
                        try {
                            this.f850a.send(Message.obtain((Handler) null, 16, this.f852a.f807a));
                        } catch (RemoteException unused) {
                        }
                    }
                }
                this.f855a.a(this.f852a, this, z, i3, str);
            }
        }

        /* access modifiers changed from: private */
        public boolean a(int i2, int i3, String str) {
            StringBuilder sb;
            String str2;
            if (this.b == null || !this.f860b) {
                return true;
            }
            if (this.b == this.f854a) {
                sb = new StringBuilder();
                str2 = " status recovered, don't notify client:";
            } else if (this.f850a == null || !this.f860b) {
                sb = new StringBuilder();
                str2 = "peer died, ignore notify ";
            } else {
                com.xiaomi.channel.commonutils.logger.b.b("Peer alive notify status to client:" + this.g);
                return true;
            }
            sb.append(str2);
            sb.append(this.g);
            com.xiaomi.channel.commonutils.logger.b.b(sb.toString());
            return false;
        }

        private boolean b(int i2, int i3, String str) {
            switch (i2) {
                case 1:
                    return this.f854a != c.binded && this.f852a.c() && i3 != 21 && (i3 != 7 || !"wait".equals(str));
                case 2:
                    return this.f852a.c();
                case 3:
                    return !"wait".equals(str);
                default:
                    return false;
            }
        }

        public long a() {
            return (((long) ((Math.random() * 20.0d) - 10.0d)) + ((long) ((this.a + 1) * 15))) * 1000;
        }

        public String a(int i2) {
            switch (i2) {
                case 1:
                    return Mtop.Id.OPEN;
                case 2:
                    return "CLOSE";
                case 3:
                    return "KICK";
                default:
                    return "unknown";
            }
        }

        /* access modifiers changed from: package-private */
        /* renamed from: a  reason: collision with other method in class */
        public void m575a() {
            try {
                Messenger messenger = this.f850a;
                if (!(messenger == null || this.f849a == null)) {
                    messenger.getBinder().unlinkToDeath(this.f849a, 0);
                }
            } catch (Exception unused) {
            }
            this.b = null;
        }

        /* access modifiers changed from: package-private */
        public void a(Messenger messenger) {
            a();
            if (messenger != null) {
                try {
                    this.f850a = messenger;
                    this.f860b = true;
                    this.f849a = new c(this, messenger);
                    messenger.getBinder().linkToDeath(this.f849a, 0);
                } catch (Exception e2) {
                    com.xiaomi.channel.commonutils.logger.b.b("peer linkToDeath err: " + e2.getMessage());
                    this.f850a = null;
                    this.f860b = false;
                }
            } else {
                com.xiaomi.channel.commonutils.logger.b.b("peer linked with old sdk chid = " + this.g);
            }
        }

        public void a(a aVar) {
            synchronized (this.f857a) {
                this.f857a.add(aVar);
            }
        }

        public void a(c cVar, int i2, int i3, String str, String str2) {
            synchronized (this.f857a) {
                for (a a2 : this.f857a) {
                    a2.a(this.f854a, cVar, i3);
                }
            }
            int i4 = 0;
            if (this.f854a != cVar) {
                com.xiaomi.channel.commonutils.logger.b.a(String.format("update the client %7$s status. %1$s->%2$s %3$s %4$s %5$s %6$s", new Object[]{this.f854a, cVar, a(i2), ap.a(i3), str, str2, this.g}));
                this.f854a = cVar;
            }
            if (this.f855a == null) {
                com.xiaomi.channel.commonutils.logger.b.d("status changed while the client dispatcher is missing");
            } else if (cVar != c.binding) {
                if (this.b != null && this.f860b) {
                    i4 = (this.f850a == null || !this.f860b) ? 10100 : 1000;
                }
                this.f852a.b((XMPushService.i) this.f853a);
                if (b(i2, i3, str2)) {
                    a(i2, i3, str, str2);
                } else {
                    this.f852a.a(this.f853a.a(i2, i3, str, str2), (long) i4);
                }
            }
        }

        public void b(a aVar) {
            synchronized (this.f857a) {
                this.f857a.remove(aVar);
            }
        }
    }

    public enum c {
        unbind,
        binding,
        binded
    }

    private al() {
    }

    public static synchronized al a() {
        al alVar;
        synchronized (al.class) {
            if (a == null) {
                a = new al();
            }
            alVar = a;
        }
        return alVar;
    }

    private String a(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        int indexOf = str.indexOf(DinamicConstant.DINAMIC_PREFIX_AT);
        return indexOf > 0 ? str.substring(0, indexOf) : str;
    }

    /* renamed from: a  reason: collision with other method in class */
    public synchronized int m568a() {
        return this.f847a.size();
    }

    public synchronized b a(String str, String str2) {
        HashMap hashMap = this.f847a.get(str);
        if (hashMap == null) {
            return null;
        }
        return (b) hashMap.get(a(str2));
    }

    /* renamed from: a  reason: collision with other method in class */
    public synchronized ArrayList<b> m569a() {
        ArrayList<b> arrayList;
        arrayList = new ArrayList<>();
        for (HashMap<String, b> values : this.f847a.values()) {
            arrayList.addAll(values.values());
        }
        return arrayList;
    }

    /* renamed from: a  reason: collision with other method in class */
    public synchronized Collection<b> m570a(String str) {
        if (!this.f847a.containsKey(str)) {
            return new ArrayList();
        }
        return ((HashMap) this.f847a.get(str).clone()).values();
    }

    /* renamed from: a  reason: collision with other method in class */
    public synchronized List<String> m571a(String str) {
        ArrayList arrayList;
        arrayList = new ArrayList();
        for (HashMap<String, b> values : this.f847a.values()) {
            for (b bVar : values.values()) {
                if (str.equals(bVar.f856a)) {
                    arrayList.add(bVar.g);
                }
            }
        }
        return arrayList;
    }

    /* renamed from: a  reason: collision with other method in class */
    public synchronized void m572a() {
        Iterator it = a().iterator();
        while (it.hasNext()) {
            ((b) it.next()).a();
        }
        this.f847a.clear();
    }

    public synchronized void a(Context context) {
        for (HashMap<String, b> values : this.f847a.values()) {
            for (b a2 : values.values()) {
                a2.a(c.unbind, 1, 3, (String) null, (String) null);
            }
        }
    }

    public synchronized void a(Context context, int i) {
        for (HashMap<String, b> values : this.f847a.values()) {
            for (b a2 : values.values()) {
                a2.a(c.unbind, 2, i, (String) null, (String) null);
            }
        }
    }

    public synchronized void a(a aVar) {
        this.f846a.add(aVar);
    }

    public synchronized void a(b bVar) {
        HashMap hashMap = this.f847a.get(bVar.g);
        if (hashMap == null) {
            hashMap = new HashMap();
            this.f847a.put(bVar.g, hashMap);
        }
        hashMap.put(a(bVar.f859b), bVar);
        for (a a2 : this.f846a) {
            a2.a();
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public synchronized void m573a(String str) {
        HashMap hashMap = this.f847a.get(str);
        if (hashMap != null) {
            for (b a2 : hashMap.values()) {
                a2.a();
            }
            hashMap.clear();
            this.f847a.remove(str);
        }
        for (a a3 : this.f846a) {
            a3.a();
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public synchronized void m574a(String str, String str2) {
        HashMap hashMap = this.f847a.get(str);
        if (hashMap != null) {
            b bVar = (b) hashMap.get(a(str2));
            if (bVar != null) {
                bVar.a();
            }
            hashMap.remove(a(str2));
            if (hashMap.isEmpty()) {
                this.f847a.remove(str);
            }
        }
        for (a a2 : this.f846a) {
            a2.a();
        }
    }

    public synchronized void b() {
        this.f846a.clear();
    }
}
