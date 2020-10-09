package com.uc.webview.export.internal.setup;

import android.util.Pair;
import com.uc.webview.export.cyclone.UCElapseTime;

/* compiled from: U4Source */
public final class bo {
    private Pair<Integer, Object> a = new Pair<>(-1, (Object) null);
    private boolean b = false;

    /* JADX WARNING: Can't wrap try/catch for region: R(6:1|2|3|4|5|6) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x000f */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void a(int r2, java.lang.Object r3) {
        /*
            r1 = this;
            monitor-enter(r1)
            android.util.Pair r0 = new android.util.Pair     // Catch:{ all -> 0x0011 }
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)     // Catch:{ all -> 0x0011 }
            r0.<init>(r2, r3)     // Catch:{ all -> 0x0011 }
            r1.a = r0     // Catch:{ all -> 0x0011 }
            r1.notify()     // Catch:{ Exception -> 0x000f }
        L_0x000f:
            monitor-exit(r1)     // Catch:{ all -> 0x0011 }
            return
        L_0x0011:
            r2 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x0011 }
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.internal.setup.bo.a(int, java.lang.Object):void");
    }

    public final Pair<Integer, Object> a(long j) {
        UCElapseTime uCElapseTime = new UCElapseTime();
        synchronized (this) {
            if (((Integer) this.a.first).intValue() != -1) {
                Pair<Integer, Object> pair = this.a;
                return pair;
            }
            long j2 = j <= 0 ? 0 : 100;
            this.b = true;
            while (true) {
                if (uCElapseTime.getMilis() >= j) {
                    if (j > 0) {
                        this.b = false;
                        return new Pair<>(1, (Object) null);
                    }
                }
                try {
                    wait(Math.max(j2, j - uCElapseTime.getMilis()));
                    if (((Integer) this.a.first).intValue() != -1) {
                        this.b = false;
                        Pair<Integer, Object> pair2 = this.a;
                        return pair2;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public final synchronized boolean a() {
        return this.b;
    }
}
