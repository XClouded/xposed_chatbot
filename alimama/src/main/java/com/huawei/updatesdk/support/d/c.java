package com.huawei.updatesdk.support.d;

import com.huawei.updatesdk.sdk.a.c.a.a.a;
import com.huawei.updatesdk.sdk.service.b.b;
import java.util.ArrayList;
import java.util.List;

public final class c {
    private static c a = new c();
    private static a b = new a() {
        private final List<b> a = new ArrayList();

        public void a(int i, b bVar) {
            synchronized (this.a) {
                for (b a2 : this.a) {
                    a2.a(i, bVar);
                }
            }
        }

        /* JADX WARNING: Code restructure failed: missing block: B:12:0x0017, code lost:
            r3 = "InstallObserverManager";
            r1 = "registerObserver IllegalArgumentException";
         */
        /* JADX WARNING: Code restructure failed: missing block: B:16:0x001f, code lost:
            r3 = "InstallObserverManager";
            r1 = "registerObserver ClassCastException";
         */
        /* JADX WARNING: Code restructure failed: missing block: B:18:0x0024, code lost:
            r3 = "InstallObserverManager";
            r1 = "registerObserver UnsupportedOperationException";
         */
        /* JADX WARNING: Code restructure failed: missing block: B:23:0x002c, code lost:
            throw r3;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:6:0x0007, code lost:
            r3 = move-exception;
         */
        /* JADX WARNING: Exception block dominator not found, dom blocks: [B:3:0x0005, B:10:0x0011] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void a(com.huawei.updatesdk.support.d.b r3) {
            /*
                r2 = this;
                java.util.List<com.huawei.updatesdk.support.d.b> r0 = r2.a
                monitor-enter(r0)
                if (r3 != 0) goto L_0x0009
                monitor-exit(r0)     // Catch:{ all -> 0x0007 }
                return
            L_0x0007:
                r3 = move-exception
                goto L_0x002b
            L_0x0009:
                java.util.List<com.huawei.updatesdk.support.d.b> r1 = r2.a     // Catch:{ all -> 0x0007 }
                boolean r1 = r1.contains(r3)     // Catch:{ all -> 0x0007 }
                if (r1 != 0) goto L_0x0029
                java.util.List<com.huawei.updatesdk.support.d.b> r1 = r2.a     // Catch:{ UnsupportedOperationException -> 0x0024, ClassCastException -> 0x001f, IllegalArgumentException -> 0x0017 }
                r1.add(r3)     // Catch:{ UnsupportedOperationException -> 0x0024, ClassCastException -> 0x001f, IllegalArgumentException -> 0x0017 }
                goto L_0x0029
            L_0x0017:
                java.lang.String r3 = "InstallObserverManager"
                java.lang.String r1 = "registerObserver IllegalArgumentException"
            L_0x001b:
                com.huawei.updatesdk.sdk.a.c.a.a.a.d(r3, r1)     // Catch:{ all -> 0x0007 }
                goto L_0x0029
            L_0x001f:
                java.lang.String r3 = "InstallObserverManager"
                java.lang.String r1 = "registerObserver ClassCastException"
                goto L_0x001b
            L_0x0024:
                java.lang.String r3 = "InstallObserverManager"
                java.lang.String r1 = "registerObserver UnsupportedOperationException"
                goto L_0x001b
            L_0x0029:
                monitor-exit(r0)     // Catch:{ all -> 0x0007 }
                return
            L_0x002b:
                monitor-exit(r0)     // Catch:{ all -> 0x0007 }
                throw r3
            */
            throw new UnsupportedOperationException("Method not decompiled: com.huawei.updatesdk.support.d.c.AnonymousClass1.a(com.huawei.updatesdk.support.d.b):void");
        }

        public void b(b bVar) {
            synchronized (this.a) {
                try {
                    this.a.remove(bVar);
                } catch (UnsupportedOperationException unused) {
                    a.d("InstallObserverManager", "unRegisterObserver UnsupportedOperationException");
                }
            }
        }
    };

    private c() {
    }

    public static c a() {
        return a;
    }

    public static a b() {
        return b;
    }

    public void a(b bVar) {
        b.a(0, bVar);
    }

    public void b(b bVar) {
        b.a(1, bVar);
    }

    public void c(b bVar) {
        b.a(2, bVar);
    }
}
