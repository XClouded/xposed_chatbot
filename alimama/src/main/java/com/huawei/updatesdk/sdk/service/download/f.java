package com.huawei.updatesdk.sdk.service.download;

import android.content.Context;
import android.os.Process;
import android.support.v4.media.session.PlaybackStateCompat;
import com.alibaba.wireless.security.SecExceptionCode;
import com.huawei.updatesdk.sdk.a.d.c.b;
import com.huawei.updatesdk.sdk.service.download.bean.DownloadTask;
import com.huawei.updatesdk.sdk.service.download.bean.a;
import com.taobao.weex.el.parse.Operators;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.util.concurrent.Future;
import javax.net.ssl.SSLProtocolException;

public class f implements Runnable {
    String a = "";
    String b = "";
    private DownloadTask c;
    private a d;
    private j e = null;
    private volatile boolean f = false;
    private volatile boolean g = true;
    private volatile boolean h = true;
    private boolean i = false;
    private int j = 0;
    private long k = 0;
    private long l = 0;
    private Future<?> m = null;

    public f(DownloadTask downloadTask, a aVar, j jVar) {
        this.c = downloadTask;
        this.d = aVar;
        this.e = jVar;
        this.g = true;
    }

    private long a(long j2, long j3, boolean z) {
        return !z ? j3 : Math.min(j2 + PlaybackStateCompat.ACTION_SET_SHUFFLE_MODE_ENABLED, j3);
    }

    private void a(int i2, long j2, long j3) throws c {
        if (i2 == -1) {
            String str = " thread download failed,response null, lastUrl=" + this.b;
            com.huawei.updatesdk.sdk.a.c.a.a.a.d("HiAppDownload", str);
            throw new c(108, str);
        } else if (i2 == 416) {
            String str2 = "server file is wrong : 416 response [package= " + this.c.w() + ", storeSize=" + this.c.r() + ", rangeStart=" + j2 + ", rangeEnd=" + j3 + ", lastUrl=" + this.b + Operators.ARRAY_END_STR;
            com.huawei.updatesdk.sdk.a.c.a.a.a.d("HiAppDownload", str2);
            throw new c(106, str2);
        } else if (i2 != 200 && i2 != 206) {
            String str3 = "thread download failed:bad http response [responseCode=" + i2 + ", lastUrl=" + this.b + Operators.ARRAY_END_STR;
            com.huawei.updatesdk.sdk.a.c.a.a.a.d("HiAppDownload", str3);
            throw new c(109, str3);
        }
    }

    private void a(int i2, HttpURLConnection httpURLConnection) throws c {
        long j2;
        if (httpURLConnection != null) {
            boolean z = true;
            if (206 == i2) {
                j2 = g.a(httpURLConnection.getHeaderField("Content-Range"));
            } else if (200 == i2) {
                z = false;
                j2 = (long) httpURLConnection.getContentLength();
            } else {
                j2 = -1;
            }
            if (j2 > 0 && j2 != this.c.r()) {
                String str = "server file length is wrong [package= " + this.c.w() + ", getLengthByRange=" + z + ", streamLength=" + j2 + ", storeSize=" + this.c.r() + ", lastUrl=" + this.b + Operators.ARRAY_END_STR;
                com.huawei.updatesdk.sdk.a.c.a.a.a.d("HiAppDownload", str);
                throw new c(106, str);
            }
        }
    }

    private void a(long j2) throws c {
        if (j2 > 0) {
            try {
                g();
                d();
                com.huawei.updatesdk.sdk.a.c.a.a.a.c("HiAppDownload", "downloadOneRange will try again after timeout:" + j2);
                Thread.sleep(j2);
            } catch (InterruptedException unused) {
                com.huawei.updatesdk.sdk.a.c.a.a.a.d("HiAppDownload", "sleep interrupted!");
            }
        }
    }

    private void a(c cVar) {
        if (this.g) {
            synchronized (this.e) {
                this.e.a(cVar);
            }
        }
    }

    private void a(BufferedInputStream bufferedInputStream, RandomAccessFile randomAccessFile) throws IOException, c {
        byte[] bArr;
        RandomAccessFile randomAccessFile2 = randomAccessFile;
        byte[] bArr2 = new byte[8192];
        int i2 = 1048576;
        byte[] bArr3 = new byte[1048576];
        long currentTimeMillis = System.currentTimeMillis() - 500;
        int i3 = 0;
        while (true) {
            d();
            int read = bufferedInputStream.read(bArr2);
            d();
            this.j = 0;
            if (read == -1) {
                break;
            }
            d();
            if (read + i3 > i2 || System.currentTimeMillis() > 1000 + currentTimeMillis) {
                try {
                    randomAccessFile2.write(bArr3, 0, i3);
                    currentTimeMillis = System.currentTimeMillis();
                    d();
                    byte[] bArr4 = bArr3;
                    this.d.a(this.d.d() + ((long) i3));
                    f();
                    bArr = bArr4;
                    i3 = 0;
                } catch (IOException e2) {
                    com.huawei.updatesdk.sdk.a.c.a.a.a.a("HiAppDownload", "write file failed", e2);
                    throw new c(SecExceptionCode.SEC_ERROR_INIT_DATA_FILE_MISMATCH, e2.getMessage());
                }
            } else {
                bArr = bArr3;
            }
            System.arraycopy(bArr2, 0, bArr, i3, read);
            i3 += read;
            bArr3 = bArr;
            i2 = 1048576;
        }
        if (i3 > 0) {
            try {
                randomAccessFile2.write(bArr3, 0, i3);
                d();
                this.d.a(this.d.d() + ((long) i3));
                f();
            } catch (IOException e3) {
                com.huawei.updatesdk.sdk.a.c.a.a.a.a("HiAppDownload", "write file failed", e3);
                throw new c(SecExceptionCode.SEC_ERROR_INIT_DATA_FILE_MISMATCH, e3.getMessage());
            }
        }
    }

    private boolean a(IOException iOException) {
        return (iOException instanceof SocketTimeoutException) || (iOException instanceof SSLProtocolException);
    }

    private void d() throws c {
        if (!this.g) {
            throw new c(103, "thread download quit because  stopped");
        } else if (!this.c.g) {
        } else {
            if (this.c.g() == 3) {
                throw new c(104, "thread download canceled!");
            }
            throw new c(105, "thread download paused!");
        }
    }

    private boolean e() {
        return (this.d.c() - this.d.b()) + 1 > this.d.d();
    }

    private void f() {
        synchronized (this.e) {
            this.e.b();
        }
    }

    private void g() {
        Context b2 = com.huawei.updatesdk.sdk.service.a.a.a().b();
        if (b2 != null && !b.a(b2)) {
            com.huawei.updatesdk.sdk.a.c.a.a.a.c("HiAppDownload", "thread download interrupted as no active network");
            this.c.a(true, 2);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:36:0x0150, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x0151, code lost:
        r12 = r1;
        r13 = r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x0154, code lost:
        r0 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x0155, code lost:
        r12 = r1;
        r13 = r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x0159, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x015a, code lost:
        r12 = r1;
        r13 = r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x0160, code lost:
        r0 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x0161, code lost:
        r12 = r1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x0164, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x0165, code lost:
        r12 = r1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:0x0167, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:65:0x018e, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:67:0x0191, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:75:0x01a4, code lost:
        r0 = e;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:105:0x0220  */
    /* JADX WARNING: Removed duplicated region for block: B:108:0x0209 A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x0154 A[ExcHandler: all (th java.lang.Throwable), Splitter:B:29:0x013b] */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x0159 A[ExcHandler: Exception (e java.lang.Exception), Splitter:B:29:0x013b] */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x0160 A[ExcHandler: all (th java.lang.Throwable), PHI: r13 
  PHI: (r13v9 java.io.RandomAccessFile) = (r13v1 java.io.RandomAccessFile), (r13v7 java.io.RandomAccessFile), (r13v1 java.io.RandomAccessFile) binds: [B:27:0x0129, B:50:0x016c, B:28:?] A[DONT_GENERATE, DONT_INLINE], Splitter:B:27:0x0129] */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x0164 A[ExcHandler: Exception (e java.lang.Exception), PHI: r13 
  PHI: (r13v8 java.io.RandomAccessFile) = (r13v1 java.io.RandomAccessFile), (r13v7 java.io.RandomAccessFile), (r13v1 java.io.RandomAccessFile) binds: [B:27:0x0129, B:50:0x016c, B:28:?] A[DONT_GENERATE, DONT_INLINE], Splitter:B:27:0x0129] */
    /* JADX WARNING: Removed duplicated region for block: B:75:0x01a4 A[ExcHandler: Exception (e java.lang.Exception), PHI: r10 r11 
  PHI: (r10v7 java.net.HttpURLConnection) = (r10v1 java.net.HttpURLConnection), (r10v10 java.net.HttpURLConnection), (r10v10 java.net.HttpURLConnection), (r10v10 java.net.HttpURLConnection), (r10v10 java.net.HttpURLConnection), (r10v10 java.net.HttpURLConnection), (r10v10 java.net.HttpURLConnection) binds: [B:2:0x002f, B:7:0x0073, B:8:?, B:10:0x00d3, B:12:0x00d7, B:62:0x0189, B:22:0x0112] A[DONT_GENERATE, DONT_INLINE]
  PHI: (r11v6 java.io.InputStream) = (r11v1 java.io.InputStream), (r11v1 java.io.InputStream), (r11v1 java.io.InputStream), (r11v1 java.io.InputStream), (r11v1 java.io.InputStream), (r11v1 java.io.InputStream), (r11v13 java.io.InputStream) binds: [B:2:0x002f, B:7:0x0073, B:8:?, B:10:0x00d3, B:12:0x00d7, B:62:0x0189, B:22:0x0112] A[DONT_GENERATE, DONT_INLINE], Splitter:B:2:0x002f] */
    /* JADX WARNING: Removed duplicated region for block: B:88:0x01d6 A[Catch:{ all -> 0x01a1 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void h() throws com.huawei.updatesdk.sdk.service.download.c {
        /*
            r17 = this;
            r7 = r17
            r8 = 0
            r7.j = r8
            java.lang.String r0 = ""
            r7.a = r0
            java.lang.String r0 = ""
            r7.b = r0
            java.lang.String r0 = "HiAppDownload"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "downloadOneRange begin :"
            r1.append(r2)
            com.huawei.updatesdk.sdk.service.download.bean.DownloadTask r2 = r7.c
            java.lang.String r2 = r2.w()
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            com.huawei.updatesdk.sdk.a.c.a.a.a.c(r0, r1)
            r0 = 0
            r10 = 0
            r11 = 0
            r12 = 0
            r13 = 0
        L_0x002e:
            r14 = 1
            r17.d()     // Catch:{ IOException -> 0x01b5, Exception -> 0x01a4 }
            long r0 = (long) r0     // Catch:{ IOException -> 0x01b5, Exception -> 0x01a4 }
            r7.a((long) r0)     // Catch:{ IOException -> 0x01b5, Exception -> 0x01a4 }
            r17.d()     // Catch:{ IOException -> 0x01b5, Exception -> 0x01a4 }
            com.huawei.updatesdk.sdk.service.download.bean.a r0 = r7.d     // Catch:{ IOException -> 0x01b5, Exception -> 0x01a4 }
            long r0 = r0.b()     // Catch:{ IOException -> 0x01b5, Exception -> 0x01a4 }
            com.huawei.updatesdk.sdk.service.download.bean.a r2 = r7.d     // Catch:{ IOException -> 0x01b5, Exception -> 0x01a4 }
            long r2 = r2.d()     // Catch:{ IOException -> 0x01b5, Exception -> 0x01a4 }
            r4 = 0
            long r4 = r0 + r2
            com.huawei.updatesdk.sdk.service.download.bean.a r0 = r7.d     // Catch:{ IOException -> 0x01b5, Exception -> 0x01a4 }
            long r15 = r0.c()     // Catch:{ IOException -> 0x01b5, Exception -> 0x01a4 }
            com.huawei.updatesdk.sdk.service.a.a r0 = com.huawei.updatesdk.sdk.service.a.a.a()     // Catch:{ IOException -> 0x01b5, Exception -> 0x01a4 }
            android.content.Context r0 = r0.b()     // Catch:{ IOException -> 0x01b5, Exception -> 0x01a4 }
            boolean r6 = com.huawei.updatesdk.sdk.a.d.c.b.b(r0)     // Catch:{ IOException -> 0x01b5, Exception -> 0x01a4 }
            r1 = r17
            r2 = r4
            r8 = r4
            r4 = r15
            long r5 = r1.a((long) r2, (long) r4, (boolean) r6)     // Catch:{ IOException -> 0x01b5, Exception -> 0x01a4 }
            com.huawei.updatesdk.sdk.service.download.g r0 = com.huawei.updatesdk.sdk.service.download.g.a()     // Catch:{ IOException -> 0x019c, Exception -> 0x0198, all -> 0x0193 }
            com.huawei.updatesdk.sdk.service.download.bean.DownloadTask r1 = r7.c     // Catch:{ IOException -> 0x019c, Exception -> 0x0198, all -> 0x0193 }
            java.lang.String r1 = r1.k()     // Catch:{ IOException -> 0x019c, Exception -> 0x0198, all -> 0x0193 }
            java.net.HttpURLConnection r10 = r0.a(r1, r14)     // Catch:{ IOException -> 0x019c, Exception -> 0x0198, all -> 0x0193 }
            java.lang.String r0 = "Range"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x01b5, Exception -> 0x01a4 }
            r1.<init>()     // Catch:{ IOException -> 0x01b5, Exception -> 0x01a4 }
            java.lang.String r2 = "bytes="
            r1.append(r2)     // Catch:{ IOException -> 0x01b5, Exception -> 0x01a4 }
            r1.append(r8)     // Catch:{ IOException -> 0x01b5, Exception -> 0x01a4 }
            java.lang.String r2 = "-"
            r1.append(r2)     // Catch:{ IOException -> 0x01b5, Exception -> 0x01a4 }
            r1.append(r5)     // Catch:{ IOException -> 0x01b5, Exception -> 0x01a4 }
            java.lang.String r1 = r1.toString()     // Catch:{ IOException -> 0x01b5, Exception -> 0x01a4 }
            r10.setRequestProperty(r0, r1)     // Catch:{ IOException -> 0x01b5, Exception -> 0x01a4 }
            java.lang.String r0 = "HiAppDownload"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x01b5, Exception -> 0x01a4 }
            r1.<init>()     // Catch:{ IOException -> 0x01b5, Exception -> 0x01a4 }
            java.lang.String r2 = "downloadOneRange before connect :"
            r1.append(r2)     // Catch:{ IOException -> 0x01b5, Exception -> 0x01a4 }
            com.huawei.updatesdk.sdk.service.download.bean.DownloadTask r2 = r7.c     // Catch:{ IOException -> 0x01b5, Exception -> 0x01a4 }
            java.lang.String r2 = r2.w()     // Catch:{ IOException -> 0x01b5, Exception -> 0x01a4 }
            r1.append(r2)     // Catch:{ IOException -> 0x01b5, Exception -> 0x01a4 }
            java.lang.String r2 = ", protocol="
            r1.append(r2)     // Catch:{ IOException -> 0x01b5, Exception -> 0x01a4 }
            com.huawei.updatesdk.sdk.service.download.bean.DownloadTask r2 = r7.c     // Catch:{ IOException -> 0x01b5, Exception -> 0x01a4 }
            int r2 = r2.i()     // Catch:{ IOException -> 0x01b5, Exception -> 0x01a4 }
            r1.append(r2)     // Catch:{ IOException -> 0x01b5, Exception -> 0x01a4 }
            java.lang.String r2 = " range:"
            r1.append(r2)     // Catch:{ IOException -> 0x01b5, Exception -> 0x01a4 }
            r1.append(r8)     // Catch:{ IOException -> 0x01b5, Exception -> 0x01a4 }
            java.lang.String r2 = "---"
            r1.append(r2)     // Catch:{ IOException -> 0x01b5, Exception -> 0x01a4 }
            r1.append(r5)     // Catch:{ IOException -> 0x01b5, Exception -> 0x01a4 }
            java.lang.String r1 = r1.toString()     // Catch:{ IOException -> 0x01b5, Exception -> 0x01a4 }
            com.huawei.updatesdk.sdk.a.c.a.a.a.c(r0, r1)     // Catch:{ IOException -> 0x01b5, Exception -> 0x01a4 }
            com.huawei.updatesdk.sdk.service.download.bean.DownloadTask r0 = r7.c     // Catch:{ IOException -> 0x01b5, Exception -> 0x01a4 }
            java.lang.String r0 = r0.k()     // Catch:{ IOException -> 0x01b5, Exception -> 0x01a4 }
            r7.b = r0     // Catch:{ IOException -> 0x01b5, Exception -> 0x01a4 }
            r1 = 0
            r15 = 0
            com.huawei.updatesdk.sdk.service.download.g$a r0 = com.huawei.updatesdk.sdk.service.download.g.a(r15, r10, r1)     // Catch:{ IOException -> 0x0191, Exception -> 0x01a4 }
            r17.d()     // Catch:{ IOException -> 0x018e, Exception -> 0x01a4 }
            boolean r1 = r0.a()     // Catch:{ IOException -> 0x018e, Exception -> 0x01a4 }
            if (r1 == 0) goto L_0x0188
            java.io.InputStream r3 = r10.getInputStream()     // Catch:{ IOException -> 0x018e, Exception -> 0x01a4 }
            java.net.URL r0 = r10.getURL()     // Catch:{ IOException -> 0x0185, Exception -> 0x0182, all -> 0x017e }
            java.lang.String r0 = r0.toString()     // Catch:{ IOException -> 0x0185, Exception -> 0x0182, all -> 0x017e }
            r7.b = r0     // Catch:{ IOException -> 0x0185, Exception -> 0x0182, all -> 0x017e }
            java.net.URL r0 = r10.getURL()     // Catch:{ IOException -> 0x0185, Exception -> 0x0182, all -> 0x017e }
            java.lang.String r0 = r0.getHost()     // Catch:{ IOException -> 0x0185, Exception -> 0x0182, all -> 0x017e }
            r7.a = r0     // Catch:{ IOException -> 0x0185, Exception -> 0x0182, all -> 0x017e }
            java.lang.String r0 = r7.a     // Catch:{ IOException -> 0x0185, Exception -> 0x0182, all -> 0x017e }
            boolean r0 = com.huawei.updatesdk.sdk.a.d.e.d(r0)     // Catch:{ IOException -> 0x0185, Exception -> 0x0182, all -> 0x017e }
            if (r0 != 0) goto L_0x0106
            java.lang.String r0 = com.huawei.updatesdk.sdk.service.download.i.a(r10)     // Catch:{ IOException -> 0x0185, Exception -> 0x0182, all -> 0x017e }
            r7.a = r0     // Catch:{ IOException -> 0x0185, Exception -> 0x0182, all -> 0x017e }
        L_0x0106:
            int r0 = r10.getResponseCode()     // Catch:{ IOException -> 0x0185, Exception -> 0x0182, all -> 0x017e }
            r11 = 0
            r7.j = r11     // Catch:{ IOException -> 0x0185, Exception -> 0x0182, all -> 0x017e }
            r1 = r17
            r2 = r0
            r11 = r3
            r3 = r8
            r1.a((int) r2, (long) r3, (long) r5)     // Catch:{ IOException -> 0x018e, Exception -> 0x01a4 }
            com.huawei.updatesdk.sdk.service.download.bean.DownloadTask r1 = r7.c     // Catch:{ IOException -> 0x018e, Exception -> 0x01a4 }
            java.lang.String r1 = r1.k()     // Catch:{ IOException -> 0x018e, Exception -> 0x01a4 }
            boolean r1 = com.huawei.updatesdk.sdk.service.download.e.b((java.lang.String) r1)     // Catch:{ IOException -> 0x018e, Exception -> 0x01a4 }
            if (r1 != 0) goto L_0x0124
            r7.a((int) r0, (java.net.HttpURLConnection) r10)     // Catch:{ IOException -> 0x018e, Exception -> 0x01a4 }
        L_0x0124:
            java.io.BufferedInputStream r1 = new java.io.BufferedInputStream     // Catch:{ IOException -> 0x018e, Exception -> 0x01a4 }
            r1.<init>(r11)     // Catch:{ IOException -> 0x018e, Exception -> 0x01a4 }
            java.io.RandomAccessFile r2 = new java.io.RandomAccessFile     // Catch:{ IOException -> 0x0167, Exception -> 0x0164, all -> 0x0160 }
            java.io.File r0 = new java.io.File     // Catch:{ IOException -> 0x0167, Exception -> 0x0164, all -> 0x0160 }
            com.huawei.updatesdk.sdk.service.download.bean.DownloadTask r3 = r7.c     // Catch:{ IOException -> 0x0167, Exception -> 0x0164, all -> 0x0160 }
            java.lang.String r3 = r3.t()     // Catch:{ IOException -> 0x0167, Exception -> 0x0164, all -> 0x0160 }
            r0.<init>(r3)     // Catch:{ IOException -> 0x0167, Exception -> 0x0164, all -> 0x0160 }
            java.lang.String r3 = "rwd"
            r2.<init>(r0, r3)     // Catch:{ IOException -> 0x0167, Exception -> 0x0164, all -> 0x0160 }
            r2.seek(r8)     // Catch:{ IOException -> 0x015d, Exception -> 0x0159, all -> 0x0154 }
            r7.a((java.io.BufferedInputStream) r1, (java.io.RandomAccessFile) r2)     // Catch:{ IOException -> 0x0150, Exception -> 0x0159, all -> 0x0154 }
            com.huawei.updatesdk.sdk.a.d.c.a(r2)
            com.huawei.updatesdk.sdk.a.d.c.a(r11)
            com.huawei.updatesdk.sdk.a.d.c.a(r1)
            if (r10 == 0) goto L_0x014f
            r10.disconnect()
        L_0x014f:
            return
        L_0x0150:
            r0 = move-exception
            r12 = r1
            r13 = r2
            goto L_0x018f
        L_0x0154:
            r0 = move-exception
            r12 = r1
            r13 = r2
            goto L_0x0215
        L_0x0159:
            r0 = move-exception
            r12 = r1
            r13 = r2
            goto L_0x01a5
        L_0x015d:
            r0 = move-exception
            r13 = r2
            goto L_0x0168
        L_0x0160:
            r0 = move-exception
            r12 = r1
            goto L_0x0215
        L_0x0164:
            r0 = move-exception
            r12 = r1
            goto L_0x01a5
        L_0x0167:
            r0 = move-exception
        L_0x0168:
            java.lang.String r2 = "HiAppDownload"
            java.lang.String r3 = "get file failed"
            com.huawei.updatesdk.sdk.a.c.a.a.a.a(r2, r3, r0)     // Catch:{ IOException -> 0x017b, Exception -> 0x0164, all -> 0x0160 }
            com.huawei.updatesdk.sdk.service.download.c r2 = new com.huawei.updatesdk.sdk.service.download.c     // Catch:{ IOException -> 0x017b, Exception -> 0x0164, all -> 0x0160 }
            r3 = 110(0x6e, float:1.54E-43)
            java.lang.String r0 = r0.getMessage()     // Catch:{ IOException -> 0x017b, Exception -> 0x0164, all -> 0x0160 }
            r2.<init>(r3, r0)     // Catch:{ IOException -> 0x017b, Exception -> 0x0164, all -> 0x0160 }
            throw r2     // Catch:{ IOException -> 0x017b, Exception -> 0x0164, all -> 0x0160 }
        L_0x017b:
            r0 = move-exception
            r12 = r1
            goto L_0x018f
        L_0x017e:
            r0 = move-exception
            r11 = r3
            goto L_0x0215
        L_0x0182:
            r0 = move-exception
            r11 = r3
            goto L_0x01a5
        L_0x0185:
            r0 = move-exception
            r11 = r3
            goto L_0x018f
        L_0x0188:
            r1 = 0
            java.lang.Exception r0 = r0.b()     // Catch:{ IOException -> 0x0191, Exception -> 0x01a4 }
            throw r0     // Catch:{ IOException -> 0x0191, Exception -> 0x01a4 }
        L_0x018e:
            r0 = move-exception
        L_0x018f:
            r1 = 0
            goto L_0x01b8
        L_0x0191:
            r0 = move-exception
            goto L_0x01b8
        L_0x0193:
            r0 = move-exception
            r15 = 0
            r10 = r15
            goto L_0x0215
        L_0x0198:
            r0 = move-exception
            r15 = 0
            r9 = r15
            goto L_0x01a6
        L_0x019c:
            r0 = move-exception
            r1 = 0
            r15 = 0
            r10 = r15
            goto L_0x01b8
        L_0x01a1:
            r0 = move-exception
            goto L_0x0215
        L_0x01a4:
            r0 = move-exception
        L_0x01a5:
            r9 = r10
        L_0x01a6:
            com.huawei.updatesdk.sdk.service.download.c r1 = new com.huawei.updatesdk.sdk.service.download.c     // Catch:{ all -> 0x01b2 }
            r2 = 111(0x6f, float:1.56E-43)
            java.lang.String r0 = r0.getMessage()     // Catch:{ all -> 0x01b2 }
            r1.<init>(r2, r0)     // Catch:{ all -> 0x01b2 }
            throw r1     // Catch:{ all -> 0x01b2 }
        L_0x01b2:
            r0 = move-exception
            r10 = r9
            goto L_0x0215
        L_0x01b5:
            r0 = move-exception
            r1 = 0
            r15 = 0
        L_0x01b8:
            java.lang.String r2 = "HiAppDownload"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x01a1 }
            r3.<init>()     // Catch:{ all -> 0x01a1 }
            java.lang.String r4 = "catch IO exception in downloadOneRange, lastUrl = "
            r3.append(r4)     // Catch:{ all -> 0x01a1 }
            java.lang.String r4 = r7.b     // Catch:{ all -> 0x01a1 }
            r3.append(r4)     // Catch:{ all -> 0x01a1 }
            java.lang.String r3 = r3.toString()     // Catch:{ all -> 0x01a1 }
            com.huawei.updatesdk.sdk.a.c.a.a.a.a(r2, r3, r0)     // Catch:{ all -> 0x01a1 }
            boolean r2 = r7.a((java.io.IOException) r0)     // Catch:{ all -> 0x01a1 }
            if (r2 == 0) goto L_0x0209
            int r2 = r7.j     // Catch:{ all -> 0x01a1 }
            int r2 = r2 + r14
            r7.j = r2     // Catch:{ all -> 0x01a1 }
            int r2 = r7.j     // Catch:{ all -> 0x01a1 }
            r3 = 2
            if (r2 > r3) goto L_0x01fd
            int r2 = r7.j     // Catch:{ all -> 0x01a1 }
            int r2 = r2 * 2000
            boolean r0 = r0 instanceof javax.net.ssl.SSLProtocolException     // Catch:{ all -> 0x01a1 }
            if (r0 == 0) goto L_0x01eb
            r0 = 200(0xc8, float:2.8E-43)
            goto L_0x01ec
        L_0x01eb:
            r0 = r2
        L_0x01ec:
            com.huawei.updatesdk.sdk.a.d.c.a(r13)
            com.huawei.updatesdk.sdk.a.d.c.a(r11)
            com.huawei.updatesdk.sdk.a.d.c.a(r12)
            if (r10 == 0) goto L_0x01fa
            r10.disconnect()
        L_0x01fa:
            r8 = 0
            goto L_0x002e
        L_0x01fd:
            com.huawei.updatesdk.sdk.service.download.c r1 = new com.huawei.updatesdk.sdk.service.download.c     // Catch:{ all -> 0x01a1 }
            r2 = 112(0x70, float:1.57E-43)
            java.lang.String r0 = r0.getMessage()     // Catch:{ all -> 0x01a1 }
            r1.<init>(r2, r0)     // Catch:{ all -> 0x01a1 }
            throw r1     // Catch:{ all -> 0x01a1 }
        L_0x0209:
            com.huawei.updatesdk.sdk.service.download.c r1 = new com.huawei.updatesdk.sdk.service.download.c     // Catch:{ all -> 0x01a1 }
            r2 = 113(0x71, float:1.58E-43)
            java.lang.String r0 = r0.getMessage()     // Catch:{ all -> 0x01a1 }
            r1.<init>(r2, r0)     // Catch:{ all -> 0x01a1 }
            throw r1     // Catch:{ all -> 0x01a1 }
        L_0x0215:
            com.huawei.updatesdk.sdk.a.d.c.a(r13)
            com.huawei.updatesdk.sdk.a.d.c.a(r11)
            com.huawei.updatesdk.sdk.a.d.c.a(r12)
            if (r10 == 0) goto L_0x0223
            r10.disconnect()
        L_0x0223:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.huawei.updatesdk.sdk.service.download.f.h():void");
    }

    private void i() {
        if (this.g) {
            synchronized (this.e) {
                this.e.a();
            }
        }
    }

    public void a(Future<?> future) {
        this.m = future;
    }

    public boolean a() {
        return this.i;
    }

    public boolean b() {
        return this.f;
    }

    public void c() {
        this.g = false;
    }

    public void run() {
        Process.setThreadPriority(10);
        com.huawei.updatesdk.sdk.a.c.a.a.a.c("HiAppDownload", "one download thread begin: " + this.c.w() + " thread:" + Thread.currentThread().getId() + " startpos= " + this.d.b());
        this.h = false;
        this.k = System.currentTimeMillis();
        do {
            try {
                h();
                if (!this.g || !e()) {
                    this.f = true;
                    this.i = true;
                    i();
                    this.l = System.currentTimeMillis();
                    com.huawei.updatesdk.sdk.a.c.a.a.a.c("HiAppDownload", "one download thread end: " + this.c.w() + " thread:" + Thread.currentThread().getId());
                }
                h();
                break;
            } catch (c e2) {
                this.f = true;
                a(e2);
                com.huawei.updatesdk.sdk.a.c.a.a.a.d("HiAppDownload", "one download thread end: " + this.c.w() + " error:" + e2.getMessage());
                this.l = System.currentTimeMillis();
                return;
            }
        } while (!e());
        this.f = true;
        this.i = true;
        i();
        this.l = System.currentTimeMillis();
        com.huawei.updatesdk.sdk.a.c.a.a.a.c("HiAppDownload", "one download thread end: " + this.c.w() + " thread:" + Thread.currentThread().getId());
    }
}
