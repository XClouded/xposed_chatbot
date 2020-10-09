package com.huawei.updatesdk.sdk.service.download;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.support.v4.media.session.PlaybackStateCompat;
import com.alibaba.taffy.core.util.codec.MessageDigestAlgorithms;
import com.alibaba.wireless.security.SecExceptionCode;
import com.huawei.updatesdk.sdk.a.c.a.a.a;
import com.huawei.updatesdk.sdk.a.d.c;
import com.huawei.updatesdk.sdk.service.download.b;
import com.huawei.updatesdk.sdk.service.download.bean.DownloadTask;
import com.huawei.updatesdk.sdk.service.download.g;
import com.huawei.updatesdk.service.deamon.download.b;
import com.taobao.tao.log.TLogConstant;
import com.taobao.weex.el.parse.Operators;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class e implements j, Runnable {
    private b a = new b();
    private Handler b;
    private volatile boolean c = true;
    private boolean d = false;
    private int e = 0;
    private int f = 0;
    private long g = 0;
    private long h = 0;
    private c i = null;
    private DownloadTask j;
    private Object k = new byte[0];
    private List<f> l = new ArrayList();
    private boolean m = false;
    private boolean n = false;
    private boolean o = false;
    private boolean p = false;
    private int q = 0;

    public e(DownloadTask downloadTask, Handler handler) {
        this.j = downloadTask;
        this.j.a((String) null);
        this.b = handler;
    }

    private void a(int i2) {
        if (this.b != null) {
            a.c("HiAppDownload", "sendMessage2: interrupted = " + this.j.g + ", interrupted reason = " + this.j.h + ", state = " + i2 + ", progress = " + this.j.p());
            this.b.sendMessage(this.b.obtainMessage(i2, this.j));
        }
    }

    private void a(File file) {
        if (!file.setReadable(true, false)) {
            a.d("HiAppDownload", "can not set readable to apk");
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:25:0x00bd, code lost:
        if (r9 != false) goto L_0x00da;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x00c9, code lost:
        if (b(r8.j.k()) == false) goto L_0x00da;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x00cb, code lost:
        r8.j.a((java.lang.String) null);
        r8.o = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x00d3, code lost:
        if (r4 == null) goto L_0x015f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x00d5, code lost:
        r4.disconnect();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:?, code lost:
        com.huawei.updatesdk.sdk.a.c.a.a.a.c("HiAppDownload", "doDispatch succeed, package: " + r8.j.w());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x00f6, code lost:
        if (r4 == null) goto L_?;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x00f8, code lost:
        r4.disconnect();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:?, code lost:
        r8.j.a(r3);
        com.huawei.updatesdk.sdk.a.c.a.a.a.c("HiAppDownload", "doDispatch succeed, no redirect, package: " + r8.j.w() + ", url=" + r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x0129, code lost:
        if (r4 == null) goto L_?;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x012b, code lost:
        r4.disconnect();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:64:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:65:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:66:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:67:?, code lost:
        return;
     */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x015b  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void a(boolean r9) throws com.huawei.updatesdk.sdk.service.download.c {
        /*
            r8 = this;
            java.lang.String r0 = "HiAppDownload"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "begin doDispatch, useHttps: "
            r1.append(r2)
            r1.append(r9)
            java.lang.String r2 = "  package:"
            r1.append(r2)
            com.huawei.updatesdk.sdk.service.download.bean.DownloadTask r2 = r8.j
            java.lang.String r2 = r2.w()
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            com.huawei.updatesdk.sdk.a.c.a.a.a.c(r0, r1)
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            r8.a(r9, r0)
            java.util.Iterator r0 = r0.iterator()
            r1 = 0
            r2 = r1
        L_0x0032:
            boolean r3 = r0.hasNext()
            if (r3 == 0) goto L_0x015f
            java.lang.Object r3 = r0.next()
            java.lang.String r3 = (java.lang.String) r3
            java.lang.String r4 = "HiAppDownload"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r6 = "do dispatch with one url:"
            r5.append(r6)
            r5.append(r3)
            java.lang.String r5 = r5.toString()
            com.huawei.updatesdk.sdk.a.c.a.a.a.c(r4, r5)
            com.huawei.updatesdk.sdk.service.download.bean.DownloadTask r4 = r8.j     // Catch:{ all -> 0x0157 }
            r4.a((java.lang.String) r1)     // Catch:{ all -> 0x0157 }
            java.lang.String r4 = "HiAppDownload"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ all -> 0x0157 }
            r5.<init>()     // Catch:{ all -> 0x0157 }
            java.lang.String r6 = "doDispatch try one url : "
            r5.append(r6)     // Catch:{ all -> 0x0157 }
            com.huawei.updatesdk.sdk.service.download.bean.DownloadTask r6 = r8.j     // Catch:{ all -> 0x0157 }
            java.lang.String r6 = r6.w()     // Catch:{ all -> 0x0157 }
            r5.append(r6)     // Catch:{ all -> 0x0157 }
            java.lang.String r5 = r5.toString()     // Catch:{ all -> 0x0157 }
            com.huawei.updatesdk.sdk.a.c.a.a.a.c(r4, r5)     // Catch:{ all -> 0x0157 }
            java.net.HttpURLConnection r4 = r8.c((java.lang.String) r3)     // Catch:{ all -> 0x0157 }
            r8.c()     // Catch:{ all -> 0x0155 }
            if (r4 != 0) goto L_0x008c
            java.lang.String r2 = "HiAppDownload"
            java.lang.String r3 = "null == connection"
            com.huawei.updatesdk.sdk.a.c.a.a.a.d(r2, r3)     // Catch:{ all -> 0x0155 }
            if (r4 == 0) goto L_0x0152
        L_0x0087:
            r4.disconnect()
            goto L_0x0152
        L_0x008c:
            int r2 = r4.getResponseCode()     // Catch:{ IOException -> 0x0133 }
            r5 = -1
            if (r2 != r5) goto L_0x009d
            java.lang.String r2 = "HiAppDownload"
            java.lang.String r3 = " dispatch onece failed: responsecode is -1"
            com.huawei.updatesdk.sdk.a.c.a.a.a.d(r2, r3)     // Catch:{ all -> 0x0155 }
            if (r4 == 0) goto L_0x0152
            goto L_0x0087
        L_0x009d:
            java.lang.String r5 = "HiAppDownload"
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ all -> 0x0155 }
            r6.<init>()     // Catch:{ all -> 0x0155 }
            java.lang.String r7 = "dispatch response code:"
            r6.append(r7)     // Catch:{ all -> 0x0155 }
            r6.append(r2)     // Catch:{ all -> 0x0155 }
            java.lang.String r6 = r6.toString()     // Catch:{ all -> 0x0155 }
            com.huawei.updatesdk.sdk.a.c.a.a.a.c(r5, r6)     // Catch:{ all -> 0x0155 }
            r5 = 302(0x12e, float:4.23E-43)
            if (r2 != r5) goto L_0x00fc
            boolean r5 = r8.a((java.net.HttpURLConnection) r4)     // Catch:{ all -> 0x0155 }
            if (r5 == 0) goto L_0x00fc
            if (r9 != 0) goto L_0x00da
            com.huawei.updatesdk.sdk.service.download.bean.DownloadTask r9 = r8.j     // Catch:{ all -> 0x0155 }
            java.lang.String r9 = r9.k()     // Catch:{ all -> 0x0155 }
            boolean r9 = b((java.lang.String) r9)     // Catch:{ all -> 0x0155 }
            if (r9 == 0) goto L_0x00da
            com.huawei.updatesdk.sdk.service.download.bean.DownloadTask r9 = r8.j     // Catch:{ all -> 0x0155 }
            r9.a((java.lang.String) r1)     // Catch:{ all -> 0x0155 }
            r9 = 1
            r8.o = r9     // Catch:{ all -> 0x0155 }
            if (r4 == 0) goto L_0x015f
            r4.disconnect()
            goto L_0x015f
        L_0x00da:
            java.lang.String r9 = "HiAppDownload"
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x0155 }
            r0.<init>()     // Catch:{ all -> 0x0155 }
            java.lang.String r1 = "doDispatch succeed, package: "
            r0.append(r1)     // Catch:{ all -> 0x0155 }
            com.huawei.updatesdk.sdk.service.download.bean.DownloadTask r1 = r8.j     // Catch:{ all -> 0x0155 }
            java.lang.String r1 = r1.w()     // Catch:{ all -> 0x0155 }
            r0.append(r1)     // Catch:{ all -> 0x0155 }
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x0155 }
            com.huawei.updatesdk.sdk.a.c.a.a.a.c(r9, r0)     // Catch:{ all -> 0x0155 }
            if (r4 == 0) goto L_0x00fb
            r4.disconnect()
        L_0x00fb:
            return
        L_0x00fc:
            r5 = 200(0xc8, float:2.8E-43)
            if (r2 != r5) goto L_0x012f
            com.huawei.updatesdk.sdk.service.download.bean.DownloadTask r9 = r8.j     // Catch:{ all -> 0x0155 }
            r9.a((java.lang.String) r3)     // Catch:{ all -> 0x0155 }
            java.lang.String r9 = "HiAppDownload"
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x0155 }
            r0.<init>()     // Catch:{ all -> 0x0155 }
            java.lang.String r1 = "doDispatch succeed, no redirect, package: "
            r0.append(r1)     // Catch:{ all -> 0x0155 }
            com.huawei.updatesdk.sdk.service.download.bean.DownloadTask r1 = r8.j     // Catch:{ all -> 0x0155 }
            java.lang.String r1 = r1.w()     // Catch:{ all -> 0x0155 }
            r0.append(r1)     // Catch:{ all -> 0x0155 }
            java.lang.String r1 = ", url="
            r0.append(r1)     // Catch:{ all -> 0x0155 }
            r0.append(r3)     // Catch:{ all -> 0x0155 }
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x0155 }
            com.huawei.updatesdk.sdk.a.c.a.a.a.c(r9, r0)     // Catch:{ all -> 0x0155 }
            if (r4 == 0) goto L_0x012e
            r4.disconnect()
        L_0x012e:
            return
        L_0x012f:
            if (r4 == 0) goto L_0x0152
            goto L_0x0087
        L_0x0133:
            r2 = move-exception
            java.lang.String r3 = "HiAppDownload"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ all -> 0x0155 }
            r5.<init>()     // Catch:{ all -> 0x0155 }
            java.lang.String r6 = "connection.getResponseCode exception: "
            r5.append(r6)     // Catch:{ all -> 0x0155 }
            java.lang.String r2 = r2.getMessage()     // Catch:{ all -> 0x0155 }
            r5.append(r2)     // Catch:{ all -> 0x0155 }
            java.lang.String r2 = r5.toString()     // Catch:{ all -> 0x0155 }
            com.huawei.updatesdk.sdk.a.c.a.a.a.d(r3, r2)     // Catch:{ all -> 0x0155 }
            if (r4 == 0) goto L_0x0152
            goto L_0x0087
        L_0x0152:
            r2 = r4
            goto L_0x0032
        L_0x0155:
            r9 = move-exception
            goto L_0x0159
        L_0x0157:
            r9 = move-exception
            r4 = r2
        L_0x0159:
            if (r4 == 0) goto L_0x015e
            r4.disconnect()
        L_0x015e:
            throw r9
        L_0x015f:
            com.huawei.updatesdk.sdk.service.download.bean.DownloadTask r9 = r8.j
            r9.b((java.util.concurrent.Future<?>) r1)
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            java.lang.String r0 = "doDispatch failed, package: "
            r9.append(r0)
            com.huawei.updatesdk.sdk.service.download.bean.DownloadTask r0 = r8.j
            java.lang.String r0 = r0.w()
            r9.append(r0)
            java.lang.String r9 = r9.toString()
            java.lang.String r0 = "HiAppDownload"
            com.huawei.updatesdk.sdk.a.c.a.a.a.d(r0, r9)
            java.lang.StringBuffer r9 = new java.lang.StringBuffer
            r9.<init>()
            java.lang.String r0 = "dispatch failed [url = "
            r9.append(r0)
            com.huawei.updatesdk.sdk.service.download.bean.DownloadTask r0 = r8.j
            java.lang.String r0 = r0.l()
            r9.append(r0)
            java.lang.String r0 = "]"
            r9.append(r0)
            com.huawei.updatesdk.sdk.service.download.c r0 = new com.huawei.updatesdk.sdk.service.download.c
            r1 = 119(0x77, float:1.67E-43)
            java.lang.String r9 = r9.toString()
            r0.<init>(r1, r9)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.huawei.updatesdk.sdk.service.download.e.a(boolean):void");
    }

    private void a(boolean z, List<String> list) {
        String a2;
        if (!z) {
            List<String> d2 = a.a().d();
            list.add(this.j.l());
            for (String next : d2) {
                String a3 = a.a(this.j.l(), next);
                if (!b(next) && !list.contains(a3)) {
                    list.add(a3);
                }
            }
            return;
        }
        if (b(this.j.l())) {
            a2 = this.j.l();
        } else {
            String c2 = a.a().c();
            if (c2 != null) {
                a2 = a.a(this.j.l(), c2);
            } else {
                return;
            }
        }
        list.add(a2);
    }

    private boolean a(HttpURLConnection httpURLConnection) {
        String headerField = httpURLConnection.getHeaderField("location");
        if (com.huawei.updatesdk.sdk.a.d.e.a(headerField)) {
            a.d("HiAppDownload", "location header is blank");
            return false;
        }
        try {
            this.j.c(0);
            URL url = new URL(headerField);
            if (headerField.indexOf("dispatcher=1") != -1 && com.huawei.updatesdk.sdk.a.d.e.d(url.getHost())) {
                this.j.c(1);
            }
            this.j.a(headerField);
            this.j.b(b(this.j.k()) ? 2 : 1);
            a.c("HiAppDownload", " url redirected , dispatcher = " + this.j.j() + ", protocol = " + this.j.i() + ", url=" + headerField);
            return true;
        } catch (MalformedURLException e2) {
            a.a("HiAppDownload", "doRedirect MalformedURLException happened", e2);
            return false;
        }
    }

    private void b(c cVar) {
        x();
        if (cVar.b() == 104 || cVar.b() == 105) {
            v();
        } else {
            this.j.d(5);
            if (cVar.b() == 121) {
                this.j.d(6);
                this.a.a(this.j, this.a.a(this.j));
            }
        }
        if (this.j.n() == 6) {
            y();
        }
        u();
    }

    private void b(boolean z) throws c {
        a.c("HiAppDownload", "doDownloadOnece begin:" + this.j.w());
        this.i = null;
        try {
            g();
            d();
            k();
            a(z);
            m();
            s();
            w();
            z();
            c();
            y();
            if (n()) {
                c(true);
                e();
                h();
                this.d = true;
                this.j.e().b(System.currentTimeMillis());
                if (!this.j.e().a() && !this.p) {
                    this.j.e().a(true);
                }
                a.c("HiAppDownload", "doDownloadOnece succeed!");
            } else if (this.i != null) {
                if (this.i.b() == 106 || this.i.b() == 122) {
                    this.o = true;
                    if (this.i.b() == 122) {
                        r();
                        this.m = true;
                    }
                }
                throw this.i;
            }
        } catch (c e2) {
            a.d("HiAppDownload", "doDownloadOnece exception, errorcode:" + e2.b() + ", errormessag:" + e2.a());
            this.j.d().a = e2.b();
            this.j.d().b = e2.a();
            if (c(e2)) {
                throw e2;
            }
        } catch (Throwable th) {
            a.d("HiAppDownload", "doDownloadOnece exception,  errormessag:" + th.getMessage());
            this.j.d().a = 111;
            this.j.d().b = th.getMessage();
        }
    }

    static boolean b(String str) {
        if (str == null) {
            return false;
        }
        return str.toLowerCase(Locale.getDefault()).startsWith("https");
    }

    private HttpURLConnection c(String str) throws c {
        long j2 = 0;
        int i2 = 0;
        while (true) {
            c();
            if (j2 > 0) {
                k();
                c();
                try {
                    a.c("HiAppDownload", "doDispatchByOneUrl will try again after timeout:" + j2);
                    Thread.sleep(j2);
                } catch (InterruptedException unused) {
                    a.d("HiAppDownload", "sleep interrupted!");
                }
            }
            c();
            try {
                HttpURLConnection a2 = g.a().a(str, false);
                g.a a3 = g.a(this.j, a2, true);
                if (!a3.a()) {
                    Exception b2 = a3.b();
                    if (b2 != null) {
                        if (!(b2 instanceof SocketTimeoutException)) {
                            break;
                        }
                        i2++;
                        long j3 = 2000 * ((long) i2);
                        if (i2 >= 3) {
                            break;
                        }
                        j2 = j3;
                    } else {
                        break;
                    }
                } else {
                    return a2;
                }
            } catch (IOException | Exception e2) {
                a.a("HiAppDownload", "openConnection", e2);
                return null;
            }
        }
        return null;
    }

    private void c(boolean z) {
        long currentTimeMillis = System.currentTimeMillis();
        long r = this.j.r();
        long j2 = 0;
        for (com.huawei.updatesdk.sdk.service.download.bean.a d2 : this.j.b()) {
            j2 += d2.d();
        }
        double d3 = (double) j2;
        double d4 = (double) r;
        Double.isNaN(d3);
        Double.isNaN(d4);
        int i2 = (int) ((d3 / d4) * 100.0d);
        int i3 = i2 - this.e < 5 ? 800 : 1500;
        if (i2 - this.f <= 0) {
            return;
        }
        if (currentTimeMillis - this.g >= ((long) i3) || i2 - this.f >= 10 || z) {
            double d5 = (double) (currentTimeMillis - this.g);
            Double.isNaN(d5);
            DownloadTask downloadTask = this.j;
            double d6 = (double) (j2 - this.h);
            Double.isNaN(d6);
            downloadTask.g((int) (d6 / (d5 / 1000.0d)));
            this.j.f(i2);
            this.j.b(j2);
            this.j.d(2);
            u();
            this.g = currentTimeMillis;
            this.h = j2;
            this.f = i2;
        }
    }

    private boolean c(c cVar) {
        int b2 = cVar.b();
        return b2 == 100 || b2 == 102 || b2 == 104 || b2 == 105 || b2 == 121 || b2 == 117;
    }

    private boolean d() throws c {
        a.c("HiAppDownload", "begin checkLocalBeforeDownload");
        b.a a2 = this.a.a(this.j);
        if (!a2.a()) {
            a.d("HiAppDownload", "space not enough");
            throw new c(SecExceptionCode.SEC_ERROR_INIT_DATA_FILE_MISMATCH, "space not enough");
        } else if (d(a2.c())) {
            return true;
        } else {
            a.d("HiAppDownload", "prepare file failed");
            throw new c(102, "prepare file failed");
        }
    }

    private boolean d(String str) {
        String str2;
        File file = new File(str, TLogConstant.RUBBISH_DIR);
        if ((!file.exists() || !file.isDirectory()) && !file.mkdirs()) {
            return false;
        }
        if (com.huawei.updatesdk.sdk.a.d.e.a(this.j.t())) {
            str2 = UUID.randomUUID().toString() + ".apk";
        } else if (new File(this.j.t()).exists()) {
            return true;
        } else {
            str2 = this.j.u();
        }
        File file2 = new File(file, str2);
        if (!file2.exists() || file2.delete()) {
            try {
                if (file2.createNewFile()) {
                    this.j.e(file2.getAbsolutePath());
                    this.j.b(0);
                    return true;
                }
                a.d("HiAppDownload", "creat new file failed!");
                return false;
            } catch (IOException e2) {
                a.a("HiAppDownload", "create " + this.j.t() + ", failed!", e2);
            }
        } else {
            a.d("HiAppDownload", "file delete failed!");
            return false;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:0x00f9, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x00fa, code lost:
        r10 = r2;
        r2 = r0;
        r0 = r10;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x010e, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x010f, code lost:
        r2 = r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x010e A[ExcHandler: IOException (e java.io.IOException), Splitter:B:3:0x0033] */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x0138 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x0139  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void e() throws com.huawei.updatesdk.sdk.service.download.c {
        /*
            r11 = this;
            r11.c()
            java.lang.String r0 = "HiAppDownload"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "begin checkDownloadedFile :"
            r1.append(r2)
            com.huawei.updatesdk.sdk.service.download.bean.DownloadTask r2 = r11.j
            java.lang.String r2 = r2.w()
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            com.huawei.updatesdk.sdk.a.c.a.a.a.c(r0, r1)
            java.lang.String r0 = ""
            r1 = 1
            r2 = 0
            r3 = 118(0x76, float:1.65E-43)
            r4 = 0
            com.huawei.updatesdk.sdk.service.download.h r5 = new com.huawei.updatesdk.sdk.service.download.h     // Catch:{ IOException -> 0x0125, IllegalArgumentException -> 0x0114 }
            com.huawei.updatesdk.sdk.service.download.bean.DownloadTask r6 = r11.j     // Catch:{ IOException -> 0x0125, IllegalArgumentException -> 0x0114 }
            java.lang.String r6 = r6.t()     // Catch:{ IOException -> 0x0125, IllegalArgumentException -> 0x0114 }
            java.lang.String r7 = "r"
            r5.<init>(r6, r7)     // Catch:{ IOException -> 0x0125, IllegalArgumentException -> 0x0114 }
            long r6 = r5.length()     // Catch:{ IOException -> 0x010e, IllegalArgumentException -> 0x010c }
            com.huawei.updatesdk.sdk.service.download.bean.DownloadTask r2 = r11.j     // Catch:{ IOException -> 0x010e, IllegalArgumentException -> 0x010c }
            long r8 = r2.r()     // Catch:{ IOException -> 0x010e, IllegalArgumentException -> 0x010c }
            int r2 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
            if (r2 == 0) goto L_0x00a6
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x010e, IllegalArgumentException -> 0x010c }
            r2.<init>()     // Catch:{ IOException -> 0x010e, IllegalArgumentException -> 0x010c }
            java.lang.String r6 = "[package="
            r2.append(r6)     // Catch:{ IOException -> 0x010e, IllegalArgumentException -> 0x010c }
            com.huawei.updatesdk.sdk.service.download.bean.DownloadTask r6 = r11.j     // Catch:{ IOException -> 0x010e, IllegalArgumentException -> 0x010c }
            java.lang.String r6 = r6.w()     // Catch:{ IOException -> 0x010e, IllegalArgumentException -> 0x010c }
            r2.append(r6)     // Catch:{ IOException -> 0x010e, IllegalArgumentException -> 0x010c }
            java.lang.String r6 = ", fileSize= "
            r2.append(r6)     // Catch:{ IOException -> 0x010e, IllegalArgumentException -> 0x010c }
            long r6 = r5.length()     // Catch:{ IOException -> 0x010e, IllegalArgumentException -> 0x010c }
            r2.append(r6)     // Catch:{ IOException -> 0x010e, IllegalArgumentException -> 0x010c }
            java.lang.String r6 = ", storeSize="
            r2.append(r6)     // Catch:{ IOException -> 0x010e, IllegalArgumentException -> 0x010c }
            com.huawei.updatesdk.sdk.service.download.bean.DownloadTask r6 = r11.j     // Catch:{ IOException -> 0x010e, IllegalArgumentException -> 0x010c }
            long r6 = r6.r()     // Catch:{ IOException -> 0x010e, IllegalArgumentException -> 0x010c }
            r2.append(r6)     // Catch:{ IOException -> 0x010e, IllegalArgumentException -> 0x010c }
            java.lang.String r6 = ", url="
            r2.append(r6)     // Catch:{ IOException -> 0x010e, IllegalArgumentException -> 0x010c }
            com.huawei.updatesdk.sdk.service.download.bean.DownloadTask r6 = r11.j     // Catch:{ IOException -> 0x010e, IllegalArgumentException -> 0x010c }
            java.lang.String r6 = r6.k()     // Catch:{ IOException -> 0x010e, IllegalArgumentException -> 0x010c }
            boolean r6 = com.huawei.updatesdk.sdk.a.d.e.a(r6)     // Catch:{ IOException -> 0x010e, IllegalArgumentException -> 0x010c }
            if (r6 == 0) goto L_0x0082
            java.lang.String r6 = "null"
            goto L_0x0088
        L_0x0082:
            com.huawei.updatesdk.sdk.service.download.bean.DownloadTask r6 = r11.j     // Catch:{ IOException -> 0x010e, IllegalArgumentException -> 0x010c }
            java.lang.String r6 = r6.k()     // Catch:{ IOException -> 0x010e, IllegalArgumentException -> 0x010c }
        L_0x0088:
            r2.append(r6)     // Catch:{ IOException -> 0x010e, IllegalArgumentException -> 0x010c }
            java.lang.String r6 = "]"
            r2.append(r6)     // Catch:{ IOException -> 0x010e, IllegalArgumentException -> 0x010c }
            java.lang.String r2 = r2.toString()     // Catch:{ IOException -> 0x010e, IllegalArgumentException -> 0x010c }
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x010e, IllegalArgumentException -> 0x010c }
            r6.<init>()     // Catch:{ IOException -> 0x010e, IllegalArgumentException -> 0x010c }
            java.lang.String r7 = "checkDownloadedFile error: file length wrong "
            r6.append(r7)     // Catch:{ IOException -> 0x010e, IllegalArgumentException -> 0x010c }
            r6.append(r2)     // Catch:{ IOException -> 0x010e, IllegalArgumentException -> 0x010c }
            java.lang.String r2 = r6.toString()     // Catch:{ IOException -> 0x010e, IllegalArgumentException -> 0x010c }
            goto L_0x0107
        L_0x00a6:
            boolean r2 = r11.f()     // Catch:{ IOException -> 0x010e, IllegalArgumentException -> 0x010c }
            if (r2 != 0) goto L_0x00fe
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x010e, IllegalArgumentException -> 0x010c }
            r2.<init>()     // Catch:{ IOException -> 0x010e, IllegalArgumentException -> 0x010c }
            java.lang.String r6 = "[package="
            r2.append(r6)     // Catch:{ IOException -> 0x010e, IllegalArgumentException -> 0x010c }
            com.huawei.updatesdk.sdk.service.download.bean.DownloadTask r6 = r11.j     // Catch:{ IOException -> 0x010e, IllegalArgumentException -> 0x010c }
            java.lang.String r6 = r6.w()     // Catch:{ IOException -> 0x010e, IllegalArgumentException -> 0x010c }
            r2.append(r6)     // Catch:{ IOException -> 0x010e, IllegalArgumentException -> 0x010c }
            java.lang.String r6 = ", url="
            r2.append(r6)     // Catch:{ IOException -> 0x010e, IllegalArgumentException -> 0x010c }
            com.huawei.updatesdk.sdk.service.download.bean.DownloadTask r6 = r11.j     // Catch:{ IOException -> 0x010e, IllegalArgumentException -> 0x010c }
            java.lang.String r6 = r6.k()     // Catch:{ IOException -> 0x010e, IllegalArgumentException -> 0x010c }
            boolean r6 = com.huawei.updatesdk.sdk.a.d.e.a(r6)     // Catch:{ IOException -> 0x010e, IllegalArgumentException -> 0x010c }
            if (r6 == 0) goto L_0x00d3
            java.lang.String r6 = "null"
            goto L_0x00d9
        L_0x00d3:
            com.huawei.updatesdk.sdk.service.download.bean.DownloadTask r6 = r11.j     // Catch:{ IOException -> 0x010e, IllegalArgumentException -> 0x010c }
            java.lang.String r6 = r6.k()     // Catch:{ IOException -> 0x010e, IllegalArgumentException -> 0x010c }
        L_0x00d9:
            r2.append(r6)     // Catch:{ IOException -> 0x010e, IllegalArgumentException -> 0x010c }
            java.lang.String r6 = "]"
            r2.append(r6)     // Catch:{ IOException -> 0x010e, IllegalArgumentException -> 0x010c }
            java.lang.String r2 = r2.toString()     // Catch:{ IOException -> 0x010e, IllegalArgumentException -> 0x010c }
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x010e, IllegalArgumentException -> 0x010c }
            r6.<init>()     // Catch:{ IOException -> 0x010e, IllegalArgumentException -> 0x010c }
            java.lang.String r7 = "checkDownloadedFile error : file hash error "
            r6.append(r7)     // Catch:{ IOException -> 0x010e, IllegalArgumentException -> 0x010c }
            r6.append(r2)     // Catch:{ IOException -> 0x010e, IllegalArgumentException -> 0x010c }
            java.lang.String r2 = r6.toString()     // Catch:{ IOException -> 0x010e, IllegalArgumentException -> 0x010c }
            r11.o = r1     // Catch:{ IOException -> 0x010e, IllegalArgumentException -> 0x00f9 }
            goto L_0x0107
        L_0x00f9:
            r0 = move-exception
            r10 = r2
            r2 = r0
            r0 = r10
            goto L_0x0118
        L_0x00fe:
            java.lang.String r2 = "HiAppDownload"
            java.lang.String r6 = "checkDownloadedFile succeed"
            com.huawei.updatesdk.sdk.a.c.a.a.a.c(r2, r6)     // Catch:{ IOException -> 0x010e, IllegalArgumentException -> 0x010c }
            r2 = r0
            r4 = 1
        L_0x0107:
            com.huawei.updatesdk.sdk.a.d.c.a(r5)
            r0 = r2
            goto L_0x0136
        L_0x010c:
            r2 = move-exception
            goto L_0x0118
        L_0x010e:
            r0 = move-exception
            r2 = r5
            goto L_0x0126
        L_0x0111:
            r0 = move-exception
            r5 = r2
            goto L_0x015a
        L_0x0114:
            r5 = move-exception
            r10 = r5
            r5 = r2
            r2 = r10
        L_0x0118:
            java.lang.String r6 = "HiAppDownload"
            java.lang.String r7 = "checkDownloadedFile IllegalArgumentException:"
            com.huawei.updatesdk.sdk.a.c.a.a.a.a(r6, r7, r2)     // Catch:{ all -> 0x0123 }
            com.huawei.updatesdk.sdk.a.d.c.a(r5)
            goto L_0x0136
        L_0x0123:
            r0 = move-exception
            goto L_0x015a
        L_0x0125:
            r0 = move-exception
        L_0x0126:
            java.lang.String r3 = "HiAppDownload"
            java.lang.String r5 = "checkDownloadedFile IOException:"
            com.huawei.updatesdk.sdk.a.c.a.a.a.a(r3, r5, r0)     // Catch:{ all -> 0x0111 }
            r3 = 123(0x7b, float:1.72E-43)
            java.lang.String r0 = r0.getMessage()     // Catch:{ all -> 0x0111 }
            com.huawei.updatesdk.sdk.a.d.c.a(r2)
        L_0x0136:
            if (r4 == 0) goto L_0x0139
            return
        L_0x0139:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r4 = "end checkDownloadedFile failed: "
            r2.append(r4)
            r2.append(r0)
            java.lang.String r2 = r2.toString()
            java.lang.String r4 = "HiAppDownload"
            com.huawei.updatesdk.sdk.a.c.a.a.a.d(r4, r2)
            r11.r()
            r11.m = r1
            com.huawei.updatesdk.sdk.service.download.c r1 = new com.huawei.updatesdk.sdk.service.download.c
            r1.<init>(r3, r0)
            throw r1
        L_0x015a:
            com.huawei.updatesdk.sdk.a.d.c.a(r5)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.huawei.updatesdk.sdk.service.download.e.e():void");
    }

    private boolean f() {
        String m2 = this.j.m();
        if (com.huawei.updatesdk.sdk.a.d.e.a(m2)) {
            a.b("HiAppDownload", "checkDownloadedFile warning: file sha256 is null");
            return true;
        } else if (!m2.equalsIgnoreCase(c.a(this.j.t(), MessageDigestAlgorithms.SHA_256))) {
            a.d("HiAppDownload", "file sha256 check failed");
            return false;
        } else {
            a.c("HiAppDownload", "file sha256 check succeed");
            return true;
        }
    }

    private void g() throws c {
        if (!com.huawei.updatesdk.sdk.a.d.e.a(this.j.l())) {
            try {
                new URL(this.j.l());
                if (this.j.r() <= 0) {
                    String str = "[fileSize=" + this.j.r() + ", backupFileSize=" + this.j.h() + ", alreadyDownloadSize=" + this.j.s() + ", roundCount=" + this.q + Operators.ARRAY_END_STR;
                    a.d("HiAppDownload", "before download, check task failed:" + "fileSize is wrong " + str);
                    throw new c(100, "fileSize is wrong " + str);
                }
            } catch (MalformedURLException unused) {
                String str2 = "url is wrong : " + this.j.l();
                a.d("HiAppDownload", "before download, check task failed:" + str2);
                throw new c(100, str2);
            }
        } else {
            a.d("HiAppDownload", "before download, check task failed:" + "dispatchUrl is null");
            throw new c(100, "dispatchUrl is null");
        }
    }

    private void h() throws c {
        boolean z;
        int i2;
        a.c("HiAppDownload", "begin processDownloadedTempFile");
        String str = "processDownloadedTempFile failed";
        File file = new File(this.j.t());
        String str2 = a(this.j.t()) + this.j.u();
        if (file.exists()) {
            File file2 = new File(str2);
            if (!file.renameTo(file2)) {
                str2 = file.getAbsolutePath();
                a(file);
            } else {
                a(file2);
            }
            this.j.e(str2);
            i2 = 111;
            z = true;
        } else {
            a.d("HiAppDownload", "Downloaded file not exist:" + this.j.w());
            i2 = 120;
            str = "Downloaded file not exist when process file ";
            z = false;
        }
        a.c("HiAppDownload", "end processDownloadedTempFile");
        if (!z) {
            r();
            this.m = true;
            throw new c(i2, str);
        }
    }

    private void i() {
        if (this.m && this.j.y()) {
            a.c("HiAppDownload", "notifyReDownloadInHttpsStarted, package :" + this.j.w());
            a(10);
            this.m = false;
        }
    }

    private boolean j() throws c {
        a(9);
        Long valueOf = Long.valueOf(System.currentTimeMillis());
        while (System.currentTimeMillis() < valueOf.longValue() + 5000) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException unused) {
                a.d("HiAppDownload", "isRestartInHttpsConfirmed interrupted");
            }
            c();
        }
        return false;
    }

    private void k() {
        Context b2 = com.huawei.updatesdk.sdk.service.a.a.a().b();
        if (b2 != null && !com.huawei.updatesdk.sdk.a.d.c.b.a(b2)) {
            a.c("HiAppDownload", "download interrupted as no active network");
            this.j.a(true, 2);
        }
    }

    private void l() throws c {
        a.c("HiAppDownload", "begin downloadrunnable download ,package:" + this.j.w());
        boolean z = false;
        this.d = false;
        this.j.d(1);
        u();
        this.j.b(this.j.q());
        boolean b2 = b(this.j.l());
        if (!b2 && !this.j.y() && d.a().b()) {
            b2 = true;
        }
        this.q = 1;
        b(b2);
        if (this.o) {
            a.b("HiAppDownload", "possibly hijacked !");
            d.a().a(true);
            this.o = false;
        }
        k();
        if (!this.d) {
            c();
            if (p()) {
                if (this.m && this.j.p() > 0) {
                    z = true;
                }
                this.m = z;
                if (!this.m || this.j.y() || j()) {
                    String a2 = a.a(this.j.q(), a.a().c());
                    this.j.c();
                    this.j.b(a2);
                    this.j.d(a2);
                    i();
                    this.q = 2;
                    b(true);
                }
            }
        }
    }

    private void m() throws c {
        a.c("HiAppDownload", "begin initDownloadThreadInfo");
        c();
        this.j.b().clear();
        ArrayList arrayList = new ArrayList();
        long r = this.j.r();
        int i2 = r < PlaybackStateCompat.ACTION_SET_SHUFFLE_MODE ? 1 : 2;
        int i3 = 0;
        while (i3 < i2) {
            long j2 = r / ((long) i2);
            long j3 = j2 * ((long) i3);
            arrayList.add(new com.huawei.updatesdk.sdk.service.download.bean.a(this.j.o(), com.huawei.updatesdk.sdk.service.download.bean.a.a(), j3, i3 == i2 + -1 ? r - 1 : (j2 + j3) - 1));
            i3++;
        }
        this.j.b().addAll(arrayList);
    }

    private boolean n() {
        for (f a2 : this.l) {
            if (!a2.a()) {
                return false;
            }
        }
        return true;
    }

    private boolean o() {
        for (f b2 : this.l) {
            if (!b2.b()) {
                return false;
            }
        }
        return true;
    }

    private boolean p() {
        if (this.j.k() != null) {
            return (this.j.k() == null || b(this.j.k()) || a.a().c() == null) ? false : true;
        }
        return true;
    }

    private void q() {
        synchronized (this.k) {
            this.k.notifyAll();
        }
    }

    private void r() {
        this.j.x();
        this.j.b(0);
        this.j.f(0);
    }

    private void s() {
        long j2 = 0;
        for (com.huawei.updatesdk.sdk.service.download.bean.a d2 : this.j.b()) {
            j2 += d2.d();
        }
        double d3 = (double) j2;
        double r = (double) this.j.r();
        Double.isNaN(d3);
        Double.isNaN(r);
        int i2 = (int) ((d3 / r) * 100.0d);
        this.j.f(i2);
        this.f = i2;
        this.e = i2;
        this.g = System.currentTimeMillis() - 1000;
        this.h = j2;
        this.j.d().a = 0;
        this.j.d().b = "";
    }

    private void t() {
        if (this.j.n() == 5 || this.j.n() == 3) {
            this.j.x();
        }
    }

    private void u() {
        if (this.b != null) {
            a.c("HiAppDownload", "sendMessage: interrupted = " + this.j.g + ", interrupted reason = " + this.j.h + ", status = " + this.j.n() + ", progress = " + this.j.p());
            this.b.sendMessage(this.b.obtainMessage(this.j.n(), this.j));
        }
    }

    private void v() {
        DownloadTask downloadTask;
        int i2 = 3;
        if (this.j.g() == 3) {
            downloadTask = this.j;
        } else if (this.j.g() == 1 || this.j.g() == 2) {
            downloadTask = this.j;
            i2 = 6;
        } else {
            return;
        }
        downloadTask.d(i2);
    }

    private void w() throws c {
        String str;
        String str2;
        c();
        this.l.clear();
        ConnectivityManager c2 = com.huawei.updatesdk.sdk.service.a.a.a().c();
        if (c2 != null) {
            this.j.a(c2.getActiveNetworkInfo());
        }
        this.p = true;
        for (com.huawei.updatesdk.sdk.service.download.bean.a next : this.j.b()) {
            if (next.b() + next.d() > next.c()) {
                str = "HiAppDownload";
                str2 = "one thread already download finished before, ingnore";
            } else {
                f fVar = new f(this.j, next, this);
                this.l.add(fVar);
                fVar.a(d.a().c().submit(fVar));
                str = "HiAppDownload";
                str2 = "summit thread task, start=" + next.b() + " end=" + next.c() + " finished=" + next.d();
            }
            a.c(str, str2);
            this.p = this.p && next.d() == 0;
        }
        this.j.e().a(System.currentTimeMillis());
    }

    private void x() {
        for (f c2 : this.l) {
            c2.c();
            if (this.j.v() != null) {
                this.j.v().cancel(true);
            }
        }
    }

    private void y() {
        int i2 = 0;
        for (com.huawei.updatesdk.sdk.service.download.bean.a d2 : this.j.b()) {
            i2 = (int) (((long) i2) + d2.d());
        }
        this.j.b((long) i2);
    }

    private void z() {
        synchronized (this.k) {
            while (!o() && !this.j.g && this.c) {
                try {
                    this.k.wait(400);
                } catch (InterruptedException unused) {
                    a.d("HiAppDownload", "task lock interrupted");
                }
            }
        }
        a.c("HiAppDownload", "waitAllDownloadThreadsDone finished");
    }

    public String a(String str) {
        StringBuilder sb;
        String absolutePath;
        if (str == null) {
            return null;
        }
        File parentFile = new File(str).getParentFile();
        if (parentFile == null) {
            return str;
        }
        if (parentFile.getName().equalsIgnoreCase(TLogConstant.RUBBISH_DIR)) {
            sb = new StringBuilder();
            absolutePath = parentFile.getParent();
        } else {
            sb = new StringBuilder();
            absolutePath = parentFile.getAbsolutePath();
        }
        sb.append(absolutePath);
        sb.append(File.separator);
        return sb.toString();
    }

    public void a() {
        if (!this.n) {
            q();
        }
    }

    public void a(b bVar) {
        this.a = bVar;
    }

    public void a(c cVar) {
        if (!this.n) {
            a.d("HiAppDownload", "one thread downloadFailed : errorcode " + cVar.b() + "  errormessage: " + cVar.a());
            if (this.i == null) {
                this.i = cVar;
            }
            x();
            q();
        }
    }

    public void b() {
        if (!this.n) {
            c(false);
        }
    }

    /* access modifiers changed from: protected */
    public void c() throws c {
        if (this.j.g && this.c) {
            this.c = false;
        }
        if (!this.c) {
            a.d("HiAppDownload", "throwIfInterrupt reason : " + this.j.g());
            int i2 = this.j.g() == 3 ? 104 : 105;
            throw new c(i2, "download interrputed : " + this.j.g());
        }
    }

    public void run() {
        try {
            if (this.j != null) {
                l();
                if (!this.d) {
                    c();
                    a.d("HiAppDownload", "quit downloadrunnalbe, result : failed, err:" + this.j.d().b + ",  package:" + this.j.w());
                    this.j.d(5);
                    int i2 = this.j.d().a;
                    if (i2 == 113 || i2 == 112 || i2 == 119) {
                        this.j.a(true, 2);
                        if (i2 == 119) {
                            this.j.a(true, 1);
                        }
                        this.j.d(6);
                        if (this.j.n() == 6) {
                            y();
                        }
                    }
                    u();
                } else {
                    this.a.a(this.j, this.j.t());
                    this.j.d(4);
                    u();
                    a.c("HiAppDownload", "quit downloadrunnalbe, result : succeed ,  package:" + this.j.w());
                }
                t();
                this.n = true;
            }
        } catch (c e2) {
            b(e2);
            a.d("HiAppDownload", "quit downloadrunnalbe, result : failed, errorcode:" + e2.b() + ", error message: " + e2.a() + ",  package:" + this.j.w());
        }
    }
}
