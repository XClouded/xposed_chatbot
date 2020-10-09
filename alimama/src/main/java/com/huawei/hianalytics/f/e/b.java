package com.huawei.hianalytics.f.e;

import android.taobao.windvane.util.ConfigStorage;
import java.util.Calendar;
import java.util.UUID;

public class b {
    /* access modifiers changed from: private */
    public volatile boolean a = false;
    private volatile long b = 0;
    private a c = null;

    private class a {
        String a = UUID.randomUUID().toString().replace("-", "");
        boolean b;
        private long d;

        a(long j) {
            this.a += "_" + j;
            this.d = j;
            this.b = true;
            boolean unused = b.this.a = false;
        }

        private boolean a(long j, long j2) {
            Calendar instance = Calendar.getInstance();
            instance.setTimeInMillis(j);
            Calendar instance2 = Calendar.getInstance();
            instance2.setTimeInMillis(j2);
            return (instance.get(1) == instance2.get(1) && instance.get(6) == instance2.get(6)) ? false : true;
        }

        private void b(long j) {
            com.huawei.hianalytics.g.b.b("SessionWrapper", "getNewSession() session is flush!");
            this.a = UUID.randomUUID().toString();
            this.a = this.a.replace("-", "");
            this.a += "_" + j;
            this.d = j;
            this.b = true;
        }

        private boolean b(long j, long j2) {
            return j2 - j >= ConfigStorage.DEFAULT_SMALL_MAX_AGE;
        }

        /* access modifiers changed from: package-private */
        public void a(long j) {
            if (b.this.a) {
                boolean unused = b.this.a = false;
                b(j);
            } else if (b(this.d, j) || a(this.d, j)) {
                b(j);
            } else {
                this.d = j;
                this.b = false;
            }
        }
    }

    public String a() {
        if (this.c != null) {
            return this.c.a;
        }
        com.huawei.hianalytics.g.b.c("SessionWrapper", "getSessionName(): session not prepared. onEvent() must be called first.");
        return "";
    }

    /* access modifiers changed from: package-private */
    public void a(long j) {
        if (this.c == null) {
            com.huawei.hianalytics.g.b.b("SessionWrapper", "Session is first flush");
            this.c = new a(j);
            return;
        }
        this.c.a(j);
    }

    public synchronized void b(long j) {
        this.a = true;
        this.b = j;
    }

    public boolean b() {
        if (this.c != null) {
            return this.c.b;
        }
        com.huawei.hianalytics.g.b.c("SessionWrapper", "isFirstEvent(): session not prepared. onEvent() must be called first.");
        return false;
    }

    public void c() {
        this.c = null;
        this.b = 0;
        this.a = false;
    }

    public synchronized void c(long j) {
        if (this.b == 0) {
            com.huawei.hianalytics.g.b.c("SessionWrapper", "OnBackground() need to be called before!");
            return;
        }
        this.a = j - this.b > 30000;
        this.b = 0;
    }
}
