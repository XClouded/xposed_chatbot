package com.uploader.implement;

import android.content.Context;
import android.text.TextUtils;
import android.util.Pair;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.taobao.accs.common.Constants;
import com.uploader.export.EnvironmentElement;
import com.uploader.export.IUploaderDependency;
import com.uploader.export.IUploaderEnvironment;
import com.uploader.export.UploaderEnvironment;
import java.util.ArrayList;
import java.util.List;

/* compiled from: UploaderConfig */
public class c {
    public final a a;
    public final UploaderEnvironment b;
    public final Context c;
    IUploaderEnvironment d;

    c(Context context, IUploaderDependency iUploaderDependency) {
        this.c = context;
        IUploaderEnvironment environment = iUploaderDependency.getEnvironment();
        if (environment instanceof UploaderEnvironment) {
            this.b = (UploaderEnvironment) environment;
        } else {
            this.d = iUploaderDependency.getEnvironment();
            this.b = new UploaderEnvironment(0) {
                public synchronized EnvironmentElement getCurrentElement() {
                    EnvironmentElement currentElement = super.getCurrentElement();
                    if (currentElement.environment == c.this.d.getEnvironment() && currentElement.appKey.equals(c.this.d.getAppKey())) {
                        return currentElement;
                    }
                    return new EnvironmentElement(c.this.d.getEnvironment(), c.this.d.getAppKey(), TextUtils.isEmpty(c.this.d.getDomain()) ? currentElement.host : c.this.d.getDomain(), currentElement.ipAddress);
                }

                public int getEnvironment() {
                    return c.this.d.getEnvironment();
                }

                public String getUtdid() {
                    return c.this.d.getUtdid();
                }

                public String getAppVersion() {
                    return c.this.d.getAppVersion();
                }

                public String getUserId() {
                    return c.this.d.getUserId();
                }

                public String signature(String str) {
                    return c.this.d.signature(str);
                }

                public int putSslTicket(Context context, String str, byte[] bArr) {
                    return c.this.d.putSslTicket(context, str, bArr);
                }

                public byte[] getSslTicket(Context context, String str) {
                    return c.this.d.getSslTicket(context, str);
                }

                public byte[] decrypt(Context context, String str, byte[] bArr) {
                    return c.this.d.decrypt(context, str, bArr);
                }

                public boolean enableFlowControl() {
                    return c.this.d.enableFlowControl();
                }
            };
        }
        this.a = new a(this.b);
        b.a(iUploaderDependency.getStatistics());
        a.a(iUploaderDependency.getLog());
    }

    /* compiled from: UploaderConfig */
    public static class a {
        C0030a a = new C0030a();
        C0030a b = new C0030a();
        C0030a c = new C0030a();
        final UploaderEnvironment d;

        /* renamed from: com.uploader.implement.c$a$a  reason: collision with other inner class name */
        /* compiled from: UploaderConfig */
        static class C0030a {
            public List<Pair<String, Integer>> a = new ArrayList();
            public int b = 0;
            public List<Pair<Boolean, Pair<String, Integer>>> c = new ArrayList();
            public int d = 0;
            public Pair<String, Long> e;
            public long f = 0;

            C0030a() {
            }
        }

        a(UploaderEnvironment uploaderEnvironment) {
            this.d = uploaderEnvironment;
        }

        public Pair<String, Long> a() {
            return ((C0030a) a(this.d.getCurrentElement()).first).e;
        }

        @NonNull
        public Pair<String, Integer> b() {
            EnvironmentElement currentElement = this.d.getCurrentElement();
            Pair<C0030a, Integer> a2 = a(currentElement);
            if (((C0030a) a2.first).a.size() == 0) {
                ((C0030a) a2.first).a.add(new Pair(currentElement.host, a2.second));
                ((C0030a) a2.first).a.add(new Pair(currentElement.ipAddress, a2.second));
            }
            if (((C0030a) a2.first).b >= ((C0030a) a2.first).a.size()) {
                ((C0030a) a2.first).b = 0;
            }
            return ((C0030a) a2.first).a.get(((C0030a) a2.first).b);
        }

        public void c() {
            ((C0030a) a(this.d.getCurrentElement()).first).b++;
        }

        public void a(String str, long j, List<Pair<String, Integer>> list, List<Pair<Boolean, Pair<String, Integer>>> list2) {
            EnvironmentElement currentElement = this.d.getCurrentElement();
            Pair<C0030a, Integer> a2 = a(currentElement);
            long currentTimeMillis = ((C0030a) a2.first).f + (System.currentTimeMillis() / 1000) + 120;
            if (j < currentTimeMillis) {
                j = currentTimeMillis;
            }
            ((C0030a) a2.first).e = new Pair<>(str, Long.valueOf(j));
            if (list2 != null && list2.size() > 0) {
                ((C0030a) a2.first).c.clear();
                for (Pair<Boolean, Pair<String, Integer>> add : list2) {
                    ((C0030a) a2.first).c.add(add);
                }
                ((C0030a) a2.first).d = 0;
            }
            if (list != null && list.size() > 0) {
                ((C0030a) a2.first).a.clear();
                Pair pair = new Pair(currentElement.host, a2.second);
                Pair pair2 = new Pair(currentElement.ipAddress, a2.second);
                for (Pair next : list) {
                    if (!pair.equals(next) && !pair2.equals(next)) {
                        ((C0030a) a2.first).a.add(next);
                    }
                }
                ((C0030a) a2.first).a.add(pair);
                ((C0030a) a2.first).a.add(pair2);
                ((C0030a) a2.first).b = 0;
            }
        }

        @Nullable
        public Pair<Boolean, Pair<String, Integer>> d() {
            Pair<C0030a, Integer> a2 = a(this.d.getCurrentElement());
            if (((C0030a) a2.first).c.size() == 0) {
                return null;
            }
            if (((C0030a) a2.first).d >= ((C0030a) a2.first).c.size()) {
                ((C0030a) a2.first).d = 0;
            }
            return ((C0030a) a2.first).c.get(((C0030a) a2.first).d);
        }

        public void e() {
            ((C0030a) a(this.d.getCurrentElement()).first).d++;
        }

        public void a(long j) {
            EnvironmentElement currentElement = this.d.getCurrentElement();
            Pair<C0030a, Integer> a2 = a(currentElement);
            ((C0030a) a2.first).f = j - (System.currentTimeMillis() / 1000);
            if (a.a(4)) {
                a.a(4, "UploaderConfig", "[updateTimestampOffset] update timestamp succeed.,environment:" + currentElement.environment + ", offset=" + ((C0030a) a2.first).f + " seconds");
            }
        }

        public long f() {
            return ((C0030a) a(this.d.getCurrentElement()).first).f;
        }

        public String g() {
            return this.d.getCurrentElement().host;
        }

        /* access modifiers changed from: package-private */
        public Pair<C0030a, Integer> a(EnvironmentElement environmentElement) {
            switch (environmentElement.environment) {
                case 1:
                    return new Pair<>(this.b, 80);
                case 2:
                    return new Pair<>(this.c, 80);
                default:
                    return new Pair<>(this.a, Integer.valueOf(Constants.PORT));
            }
        }
    }
}
