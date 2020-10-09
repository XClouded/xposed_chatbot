package com.huawei.hms.update.c;

import android.content.Context;
import android.os.AsyncTask;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.HttpsURLConnection;

/* compiled from: PingTask */
public final class a {
    /* access modifiers changed from: private */
    public static boolean a = false;
    /* access modifiers changed from: private */
    public CountDownLatch b;

    public boolean a(long j, TimeUnit timeUnit) {
        if (a) {
            com.huawei.hms.support.log.a.b("PingTask", "ping google return cache");
            return true;
        }
        com.huawei.hms.support.log.a.b("PingTask", "start ping goole");
        this.b = new CountDownLatch(1);
        new C0013a().execute(new Context[0]);
        try {
            if (!this.b.await(j, timeUnit)) {
                com.huawei.hms.support.log.a.b("PingTask", "await time out");
                return false;
            }
            com.huawei.hms.support.log.a.b("PingTask", "await:isReachable:" + a);
            return a;
        } catch (InterruptedException unused) {
            com.huawei.hms.support.log.a.d("PingTask", "await:InterruptedException:");
            return false;
        }
    }

    /* renamed from: com.huawei.hms.update.c.a$a  reason: collision with other inner class name */
    /* compiled from: PingTask */
    private class C0013a extends AsyncTask<Context, Integer, Boolean> {
        private C0013a() {
        }

        /* access modifiers changed from: protected */
        /* renamed from: a */
        public Boolean doInBackground(Context... contextArr) {
            boolean z = false;
            try {
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) new URL("https://play.google.com/store").openConnection();
                if (httpsURLConnection != null) {
                    httpsURLConnection.setRequestMethod("GET");
                    httpsURLConnection.setConnectTimeout(30000);
                    httpsURLConnection.setReadTimeout(30000);
                    httpsURLConnection.setUseCaches(false);
                    int responseCode = httpsURLConnection.getResponseCode();
                    com.huawei.hms.support.log.a.b("PingTask", "GET google result:" + responseCode);
                    z = true;
                }
            } catch (RuntimeException unused) {
                com.huawei.hms.support.log.a.d("PingTask", "GET google result:RuntimeException");
            } catch (IOException unused2) {
                com.huawei.hms.support.log.a.d("PingTask", "GET google result:safe exception");
            } catch (Exception unused3) {
                com.huawei.hms.support.log.a.d("PingTask", "GET google result:Exception");
            }
            boolean unused4 = a.a = z;
            a.this.b.countDown();
            return Boolean.valueOf(z);
        }
    }
}
