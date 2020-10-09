package com.huawei.updatesdk.sdk.service.download;

import anet.channel.util.HttpConstant;
import com.huawei.updatesdk.sdk.a.d.c.b;
import com.huawei.updatesdk.sdk.a.d.e;
import com.huawei.updatesdk.sdk.service.download.bean.DownloadTask;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.net.ssl.HttpsURLConnection;
import org.apache.http.conn.ssl.SSLSocketFactory;

public class g {
    private static volatile g a;

    public static class a {
        private boolean a = false;
        private Exception b = null;

        public void a(Exception exc) {
            this.b = exc;
        }

        public void a(boolean z) {
            this.a = z;
        }

        public boolean a() {
            return this.a;
        }

        public Exception b() {
            return this.b;
        }
    }

    public static long a(String str) {
        String str2;
        String str3;
        long parseLong;
        long j = -1;
        if (e.a(str) || !str.startsWith("bytes")) {
            return -1;
        }
        int indexOf = str.indexOf(47);
        if (-1 != indexOf) {
            try {
                parseLong = Long.parseLong(str.substring(indexOf + 1));
            } catch (NumberFormatException unused) {
                str2 = "HiAppDownload";
                str3 = "getEntityLegth NumberFormatException";
                com.huawei.updatesdk.sdk.a.c.a.a.a.d(str2, str3);
                return j;
            }
            try {
                com.huawei.updatesdk.sdk.a.c.a.a.a.a("HiAppDownload", "get new filelength by Content-Range:" + parseLong);
                return parseLong;
            } catch (NumberFormatException unused2) {
                j = parseLong;
                str2 = "HiAppDownload";
                str3 = "getEntityLegth NumberFormatException";
                com.huawei.updatesdk.sdk.a.c.a.a.a.d(str2, str3);
                return j;
            }
        } else {
            str2 = "HiAppDownload";
            str3 = "getEntityLegth failed Content-Range";
            com.huawei.updatesdk.sdk.a.c.a.a.a.d(str2, str3);
            return j;
        }
    }

    public static a a(DownloadTask downloadTask, final HttpURLConnection httpURLConnection, boolean z) {
        a aVar;
        Exception e;
        if (httpURLConnection == null) {
            return null;
        }
        ExecutorService newSingleThreadExecutor = Executors.newSingleThreadExecutor();
        aVar = new a();
        Future submit = newSingleThreadExecutor.submit(new Callable<a>() {
            /* renamed from: a */
            public a call() {
                a aVar = new a();
                try {
                    httpURLConnection.connect();
                    aVar.a(true);
                } catch (IOException e) {
                    com.huawei.updatesdk.sdk.a.c.a.a.a.d("HiAppDownload", e.toString());
                    aVar.a((Exception) e);
                }
                return aVar;
            }
        });
        if (downloadTask != null && z) {
            downloadTask.b((Future<?>) submit);
        }
        try {
            return (a) submit.get(8000, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e2) {
            com.huawei.updatesdk.sdk.a.c.a.a.a.d("HiAppDownload", e2.toString());
            e = new SocketTimeoutException("connect timeout");
        } catch (Exception e3) {
            e = e3;
            com.huawei.updatesdk.sdk.a.c.a.a.a.d("HiAppDownload", e.toString());
        }
        aVar.a(e);
        return aVar;
    }

    public static synchronized g a() {
        g gVar;
        synchronized (g.class) {
            if (a == null) {
                a = new g();
            }
            gVar = a;
        }
        return gVar;
    }

    private Proxy b() {
        if (b.b(com.huawei.updatesdk.sdk.service.a.a.a().b())) {
            return b.a();
        }
        return null;
    }

    public HttpURLConnection a(String str, boolean z) throws IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IllegalAccessException {
        URL url = new URL(str);
        Proxy b = b();
        HttpURLConnection httpURLConnection = (HttpURLConnection) (b == null ? url.openConnection() : url.openConnection(b));
        httpURLConnection.setConnectTimeout(7000);
        httpURLConnection.setReadTimeout(10000);
        httpURLConnection.setUseCaches(false);
        httpURLConnection.setDoInput(true);
        httpURLConnection.setRequestProperty(HttpConstant.ACCEPT_ENCODING, "identity");
        httpURLConnection.setInstanceFollowRedirects(z);
        if (httpURLConnection instanceof HttpsURLConnection) {
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) httpURLConnection;
            httpsURLConnection.setSSLSocketFactory(com.huawei.updatesdk.sdk.a.b.b.a(com.huawei.updatesdk.sdk.service.a.a.a().b()));
            httpsURLConnection.setHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);
        }
        return httpURLConnection;
    }
}
