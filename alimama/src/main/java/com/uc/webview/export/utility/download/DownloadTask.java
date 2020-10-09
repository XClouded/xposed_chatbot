package com.uc.webview.export.utility.download;

import android.content.Context;
import android.webkit.ValueCallback;
import com.alipay.sdk.util.e;
import com.uc.webview.export.annotations.Api;
import com.uc.webview.export.extension.UCCore;
import com.uc.webview.export.internal.interfaces.IWaStat;
import com.uc.webview.export.internal.utility.i;
import java.util.concurrent.ConcurrentHashMap;

@Api
/* compiled from: U4Source */
public class DownloadTask implements Runnable {
    private static final ConcurrentHashMap<Integer, Integer> a = new ConcurrentHashMap<>();
    private final Object[] b = new Object[3];
    /* access modifiers changed from: private */
    public final ValueCallback<DownloadTask>[] c = new ValueCallback[10];
    /* access modifiers changed from: private */
    public final String[] d = new String[3];
    private final long[] e = new long[3];
    private ValueCallback<Object[]> f;

    public DownloadTask(Context context, String str, ValueCallback<Object[]> valueCallback) {
        int hashCode = str.hashCode();
        this.b[2] = context;
        synchronized (a) {
            if (!a.containsKey(Integer.valueOf(hashCode))) {
                a.put(Integer.valueOf(hashCode), Integer.valueOf(hashCode));
            } else {
                throw new RuntimeException("Duplicate task.");
            }
        }
        String valueOf = hashCode >= 0 ? String.valueOf(hashCode) : String.valueOf(hashCode).replace('-', '_');
        this.f = valueCallback;
        IWaStat.WaStat.stat(IWaStat.DOWNLOAD);
        this.d[0] = str;
        String[] strArr = this.d;
        strArr[1] = context.getApplicationContext().getCacheDir().getAbsolutePath() + "/ucdown" + valueOf;
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        try {
            int hashCode = this.d[0].hashCode();
            synchronized (a) {
                a.remove(Integer.valueOf(hashCode));
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public DownloadTask onEvent(String str, ValueCallback<DownloadTask> valueCallback) {
        if (str.equals("success")) {
            this.c[0] = valueCallback;
        } else if (str.equals(e.a)) {
            this.c[1] = valueCallback;
        } else if (str.equals("recovered")) {
            this.c[2] = valueCallback;
        } else if (str.equals("progress")) {
            this.c[3] = valueCallback;
        } else if (str.equals(UCCore.EVENT_EXCEPTION)) {
            this.c[4] = valueCallback;
        } else if (str.equals("check")) {
            this.c[5] = valueCallback;
        } else if (str.equals("header")) {
            this.c[6] = valueCallback;
        } else if (str.equals("exists")) {
            this.c[7] = valueCallback;
        } else if (str.equals("beginDownload")) {
            this.c[8] = valueCallback;
        } else if (str.equals("delete")) {
            this.c[9] = valueCallback;
        } else {
            throw new RuntimeException("The given event:" + str + " is unknown.");
        }
        return this;
    }

    public Throwable getException() {
        return (Throwable) this.b[1];
    }

    public String getFilePath() {
        return this.d[2];
    }

    public long getTotalSize() {
        return this.e[0];
    }

    public long getCurrentSize() {
        return this.e[1];
    }

    public long getLastModified() {
        return this.e[2];
    }

    public DownloadTask start() {
        this.b[0] = new Thread(this, "UCCore-DownloadTask.start");
        ((Thread) this.b[0]).start();
        return this;
    }

    public DownloadTask stop() {
        this.b[0] = null;
        return this;
    }

    public DownloadTask stopWith(Runnable runnable) {
        this.b[0] = new Thread(new a(this, runnable), "UCCore-DownloadTask.stopWith");
        ((Thread) this.b[0]).start();
        return this;
    }

    public DownloadTask planWith(Runnable runnable) {
        i.a((Runnable) new b(this, runnable));
        return this;
    }

    public DownloadTask delete() {
        return delete(false);
    }

    public DownloadTask delete(boolean z) {
        IWaStat.WaStat.stat(IWaStat.SHARE_CORE_DELETE_UPD_FILE_PV);
        this.b[0] = new Thread(new c(this, z), "UCCore-DownloadTask.delete");
        ((Thread) this.b[0]).start();
        return this;
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(6:120|121|122|123|124|125) */
    /* JADX WARNING: Code restructure failed: missing block: B:157:0x0297, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:174:0x02bf, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:193:0x02d9, code lost:
        return;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:122:0x0224 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:124:0x0227 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:129:0x022f */
    /* JADX WARNING: Missing exception handler attribute for start block: B:134:0x0234 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:136:0x0237 */
    /* JADX WARNING: Removed duplicated region for block: B:119:0x021e A[LOOP:1: B:100:0x01e1->B:119:0x021e, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:120:0x0221 A[SYNTHETIC, Splitter:B:120:0x0221] */
    /* JADX WARNING: Removed duplicated region for block: B:199:0x02e2 A[Catch:{ Throwable -> 0x0306 }] */
    /* JADX WARNING: Removed duplicated region for block: B:208:0x031b  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void run() {
        /*
            r23 = this;
            r1 = r23
            monitor-enter(r23)
            android.os.Looper r0 = android.os.Looper.myLooper()     // Catch:{ all -> 0x0332 }
            android.os.Looper r2 = android.os.Looper.getMainLooper()     // Catch:{ all -> 0x0332 }
            if (r0 == r2) goto L_0x032a
            android.webkit.ValueCallback<com.uc.webview.export.utility.download.DownloadTask>[] r0 = r1.c     // Catch:{ all -> 0x0332 }
            r2 = 0
            r3 = r0[r2]     // Catch:{ all -> 0x0332 }
            android.webkit.ValueCallback<com.uc.webview.export.utility.download.DownloadTask>[] r0 = r1.c     // Catch:{ all -> 0x0332 }
            r4 = 1
            r5 = r0[r4]     // Catch:{ all -> 0x0332 }
            android.webkit.ValueCallback<com.uc.webview.export.utility.download.DownloadTask>[] r0 = r1.c     // Catch:{ all -> 0x0332 }
            r6 = 2
            r7 = r0[r6]     // Catch:{ all -> 0x0332 }
            android.webkit.ValueCallback<com.uc.webview.export.utility.download.DownloadTask>[] r0 = r1.c     // Catch:{ all -> 0x0332 }
            r8 = 3
            r8 = r0[r8]     // Catch:{ all -> 0x0332 }
            android.webkit.ValueCallback<com.uc.webview.export.utility.download.DownloadTask>[] r0 = r1.c     // Catch:{ all -> 0x0332 }
            r9 = 4
            r9 = r0[r9]     // Catch:{ all -> 0x0332 }
            android.webkit.ValueCallback<com.uc.webview.export.utility.download.DownloadTask>[] r0 = r1.c     // Catch:{ all -> 0x0332 }
            r10 = 5
            r0 = r0[r10]     // Catch:{ all -> 0x0332 }
            android.webkit.ValueCallback<com.uc.webview.export.utility.download.DownloadTask>[] r11 = r1.c     // Catch:{ all -> 0x0332 }
            r12 = 6
            r11 = r11[r12]     // Catch:{ all -> 0x0332 }
            android.webkit.ValueCallback<com.uc.webview.export.utility.download.DownloadTask>[] r13 = r1.c     // Catch:{ all -> 0x0332 }
            r14 = 7
            r13 = r13[r14]     // Catch:{ all -> 0x0332 }
            android.webkit.ValueCallback<com.uc.webview.export.utility.download.DownloadTask>[] r14 = r1.c     // Catch:{ all -> 0x0332 }
            r15 = 8
            r14 = r14[r15]     // Catch:{ all -> 0x0332 }
            if (r0 == 0) goto L_0x0046
            r0.onReceiveValue(r1)     // Catch:{ Throwable -> 0x0041 }
            goto L_0x0046
        L_0x0041:
            r0 = move-exception
            r21 = r9
            goto L_0x030c
        L_0x0046:
            java.lang.Object[] r0 = r1.b     // Catch:{ Throwable -> 0x0041 }
            r15 = 0
            r0[r4] = r15     // Catch:{ Throwable -> 0x0041 }
            java.lang.String[] r0 = r1.d     // Catch:{ Throwable -> 0x0041 }
            r12 = r0[r2]     // Catch:{ Throwable -> 0x0041 }
            java.lang.String[] r0 = r1.d     // Catch:{ Throwable -> 0x0041 }
            r10 = r0[r4]     // Catch:{ Throwable -> 0x0041 }
            android.webkit.ValueCallback<java.lang.Object[]> r0 = r1.f     // Catch:{ Throwable -> 0x006b, Exception -> 0x0065 }
            if (r0 == 0) goto L_0x006f
            android.webkit.ValueCallback<java.lang.Object[]> r0 = r1.f     // Catch:{ Throwable -> 0x006b, Exception -> 0x0065 }
            java.lang.Object[] r6 = new java.lang.Object[r4]     // Catch:{ Throwable -> 0x006b, Exception -> 0x0065 }
            java.lang.Integer r17 = java.lang.Integer.valueOf(r4)     // Catch:{ Throwable -> 0x006b, Exception -> 0x0065 }
            r6[r2] = r17     // Catch:{ Throwable -> 0x006b, Exception -> 0x0065 }
            r0.onReceiveValue(r6)     // Catch:{ Throwable -> 0x006b, Exception -> 0x0065 }
            goto L_0x006f
        L_0x0065:
            r0 = move-exception
            r2 = r0
            r21 = r9
            goto L_0x02de
        L_0x006b:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ Exception -> 0x02da }
        L_0x006f:
            android.util.Pair r0 = com.uc.webview.export.internal.utility.k.a((java.lang.String) r12, (java.net.URL) r15)     // Catch:{ Exception -> 0x02da }
            long[] r6 = r1.e     // Catch:{ Throwable -> 0x0041 }
            java.lang.Object r15 = r0.first     // Catch:{ Throwable -> 0x0041 }
            java.lang.Long r15 = (java.lang.Long) r15     // Catch:{ Throwable -> 0x0041 }
            r18 = r5
            long r4 = r15.longValue()     // Catch:{ Throwable -> 0x0041 }
            r6[r2] = r4     // Catch:{ Throwable -> 0x0041 }
            long[] r6 = r1.e     // Catch:{ Throwable -> 0x0041 }
            java.lang.Object r0 = r0.second     // Catch:{ Throwable -> 0x0041 }
            java.lang.Long r0 = (java.lang.Long) r0     // Catch:{ Throwable -> 0x0041 }
            r19 = r3
            long r2 = r0.longValue()     // Catch:{ Throwable -> 0x0041 }
            r15 = 2
            r6[r15] = r2     // Catch:{ Throwable -> 0x0041 }
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0041 }
            r0.<init>()     // Catch:{ Throwable -> 0x0041 }
            r0.append(r4)     // Catch:{ Throwable -> 0x0041 }
            java.lang.String r6 = "_"
            r0.append(r6)     // Catch:{ Throwable -> 0x0041 }
            r0.append(r2)     // Catch:{ Throwable -> 0x0041 }
            java.lang.String r2 = r0.toString()     // Catch:{ Throwable -> 0x0041 }
            java.lang.String[] r0 = r1.d     // Catch:{ Throwable -> 0x0041 }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0041 }
            r3.<init>()     // Catch:{ Throwable -> 0x0041 }
            r3.append(r10)     // Catch:{ Throwable -> 0x0041 }
            java.lang.String r6 = "/"
            r3.append(r6)     // Catch:{ Throwable -> 0x0041 }
            r3.append(r2)     // Catch:{ Throwable -> 0x0041 }
            java.lang.String r3 = r3.toString()     // Catch:{ Throwable -> 0x0041 }
            r6 = 2
            r0[r6] = r3     // Catch:{ Throwable -> 0x0041 }
            java.io.File r6 = new java.io.File     // Catch:{ Throwable -> 0x0041 }
            r6.<init>(r3)     // Catch:{ Throwable -> 0x0041 }
            long[] r0 = r1.e     // Catch:{ Throwable -> 0x0041 }
            r20 = r8
            r21 = r9
            long r8 = r6.length()     // Catch:{ Throwable -> 0x030b }
            r3 = 1
            r0[r3] = r8     // Catch:{ Throwable -> 0x030b }
            int r0 = (r8 > r4 ? 1 : (r8 == r4 ? 0 : -1))
            if (r0 == 0) goto L_0x00d5
            r3 = 1
            goto L_0x00d6
        L_0x00d5:
            r3 = 0
        L_0x00d6:
            if (r11 == 0) goto L_0x00e1
            r11.onReceiveValue(r1)     // Catch:{ Throwable -> 0x00dc }
            goto L_0x00e1
        L_0x00dc:
            r0 = move-exception
            r11 = r0
            r11.printStackTrace()     // Catch:{ Throwable -> 0x030b }
        L_0x00e1:
            java.lang.Object[] r0 = r1.b     // Catch:{ Throwable -> 0x030b }
            r11 = 0
            r0 = r0[r11]     // Catch:{ Throwable -> 0x030b }
            java.lang.Thread r11 = java.lang.Thread.currentThread()     // Catch:{ Throwable -> 0x030b }
            if (r0 == r11) goto L_0x00ee
            monitor-exit(r23)
            return
        L_0x00ee:
            android.webkit.ValueCallback<java.lang.Object[]> r0 = r1.f     // Catch:{ Throwable -> 0x0104 }
            if (r0 == 0) goto L_0x0108
            android.webkit.ValueCallback<java.lang.Object[]> r0 = r1.f     // Catch:{ Throwable -> 0x0104 }
            r11 = 1
            java.lang.Object[] r15 = new java.lang.Object[r11]     // Catch:{ Throwable -> 0x0104 }
            r11 = 2
            java.lang.Integer r11 = java.lang.Integer.valueOf(r11)     // Catch:{ Throwable -> 0x0104 }
            r16 = 0
            r15[r16] = r11     // Catch:{ Throwable -> 0x0104 }
            r0.onReceiveValue(r15)     // Catch:{ Throwable -> 0x0104 }
            goto L_0x0108
        L_0x0104:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ Throwable -> 0x030b }
        L_0x0108:
            if (r3 == 0) goto L_0x02c6
            java.io.File r0 = new java.io.File     // Catch:{ Throwable -> 0x030b }
            r0.<init>(r10)     // Catch:{ Throwable -> 0x030b }
            com.uc.webview.export.cyclone.UCCyclone.expectCreateDirFile(r0)     // Catch:{ Throwable -> 0x030b }
            java.io.File[] r0 = r0.listFiles()     // Catch:{ Throwable -> 0x030b }
            int r10 = r0.length     // Catch:{ Throwable -> 0x030b }
            r11 = 0
        L_0x0118:
            if (r11 >= r10) goto L_0x0136
            r13 = r0[r11]     // Catch:{ Throwable -> 0x030b }
            java.lang.String r15 = r13.getName()     // Catch:{ Throwable -> 0x030b }
            boolean r15 = r15.equals(r2)     // Catch:{ Throwable -> 0x030b }
            if (r15 != 0) goto L_0x012e
            r22 = r2
            r2 = 0
            r15 = 0
            com.uc.webview.export.cyclone.UCCyclone.recursiveDelete(r13, r15, r2)     // Catch:{ Throwable -> 0x030b }
            goto L_0x0131
        L_0x012e:
            r22 = r2
            r2 = 0
        L_0x0131:
            int r11 = r11 + 1
            r2 = r22
            goto L_0x0118
        L_0x0136:
            boolean r0 = r6.exists()     // Catch:{ Throwable -> 0x030b }
            if (r0 != 0) goto L_0x013f
            r6.createNewFile()     // Catch:{ Throwable -> 0x030b }
        L_0x013f:
            int r0 = (r8 > r4 ? 1 : (r8 == r4 ? 0 : -1))
            if (r0 >= 0) goto L_0x0253
            if (r14 == 0) goto L_0x014e
            r14.onReceiveValue(r1)     // Catch:{ Exception -> 0x0149 }
            goto L_0x014e
        L_0x0149:
            r0 = move-exception
            r2 = r0
            r2.printStackTrace()     // Catch:{ Throwable -> 0x030b }
        L_0x014e:
            boolean r0 = com.uc.webview.export.internal.SDKFactory.f     // Catch:{ Throwable -> 0x016b }
            if (r0 != 0) goto L_0x016f
            java.lang.String r0 = "traffic_stat"
            java.lang.String r0 = com.uc.webview.export.extension.UCCore.getParam(r0)     // Catch:{ Throwable -> 0x016b }
            boolean r0 = java.lang.Boolean.parseBoolean(r0)     // Catch:{ Throwable -> 0x016b }
            if (r0 == 0) goto L_0x016f
            int r0 = android.os.Build.VERSION.SDK_INT     // Catch:{ Throwable -> 0x016b }
            r2 = 14
            if (r0 < r2) goto L_0x016f
            r0 = 40962(0xa002, float:5.74E-41)
            android.net.TrafficStats.setThreadStatsTag(r0)     // Catch:{ Throwable -> 0x016b }
            goto L_0x016f
        L_0x016b:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ Throwable -> 0x030b }
        L_0x016f:
            java.net.URL r0 = new java.net.URL     // Catch:{ Throwable -> 0x030b }
            r0.<init>(r12)     // Catch:{ Throwable -> 0x030b }
            java.net.URLConnection r0 = r0.openConnection()     // Catch:{ Throwable -> 0x030b }
            r2 = r0
            java.net.HttpURLConnection r2 = (java.net.HttpURLConnection) r2     // Catch:{ Throwable -> 0x030b }
            int r0 = com.uc.webview.export.internal.utility.k.c()     // Catch:{ Throwable -> 0x030b }
            r2.setConnectTimeout(r0)     // Catch:{ Throwable -> 0x030b }
            int r0 = com.uc.webview.export.internal.utility.k.d()     // Catch:{ Throwable -> 0x030b }
            r2.setReadTimeout(r0)     // Catch:{ Throwable -> 0x030b }
            r10 = 1
            r2.setInstanceFollowRedirects(r10)     // Catch:{ Throwable -> 0x030b }
            java.lang.String r0 = "GET"
            r2.setRequestMethod(r0)     // Catch:{ Throwable -> 0x030b }
            r10 = 0
            int r0 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
            if (r0 <= 0) goto L_0x01c3
            java.lang.String r0 = "Range"
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x030b }
            java.lang.String r11 = "bytes="
            r10.<init>(r11)     // Catch:{ Throwable -> 0x030b }
            r10.append(r8)     // Catch:{ Throwable -> 0x030b }
            java.lang.String r8 = "-"
            r10.append(r8)     // Catch:{ Throwable -> 0x030b }
            r10.append(r4)     // Catch:{ Throwable -> 0x030b }
            java.lang.String r8 = r10.toString()     // Catch:{ Throwable -> 0x030b }
            r2.setRequestProperty(r0, r8)     // Catch:{ Throwable -> 0x030b }
            java.lang.String r0 = "sdk_dl_r"
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r0)     // Catch:{ Throwable -> 0x030b }
            if (r7 == 0) goto L_0x01c3
            r7.onReceiveValue(r1)     // Catch:{ Throwable -> 0x01be }
            goto L_0x01c3
        L_0x01be:
            r0 = move-exception
            r7 = r0
            r7.printStackTrace()     // Catch:{ Throwable -> 0x030b }
        L_0x01c3:
            r2.connect()     // Catch:{ Throwable -> 0x030b }
            int r0 = r2.getResponseCode()     // Catch:{ Throwable -> 0x030b }
            r7 = 200(0xc8, float:2.8E-43)
            if (r0 < r7) goto L_0x0238
            r7 = 303(0x12f, float:4.25E-43)
            if (r0 > r7) goto L_0x0238
            java.io.InputStream r7 = r2.getInputStream()     // Catch:{ Throwable -> 0x030b }
            java.io.FileOutputStream r8 = new java.io.FileOutputStream     // Catch:{ all -> 0x0230 }
            r9 = 1
            r8.<init>(r6, r9)     // Catch:{ all -> 0x0230 }
            r0 = 51200(0xc800, float:7.1746E-41)
            byte[] r9 = new byte[r0]     // Catch:{ all -> 0x022b }
        L_0x01e1:
            int r10 = r7.read(r9)     // Catch:{ all -> 0x022b }
            if (r10 <= 0) goto L_0x0202
            r11 = 0
            r8.write(r9, r11, r10)     // Catch:{ all -> 0x022b }
            long[] r0 = r1.e     // Catch:{ all -> 0x022b }
            r11 = 1
            r12 = r0[r11]     // Catch:{ all -> 0x022b }
            long r14 = (long) r10     // Catch:{ all -> 0x022b }
            long r12 = r12 + r14
            r0[r11] = r12     // Catch:{ all -> 0x022b }
            if (r20 == 0) goto L_0x0202
            r11 = r20
            r11.onReceiveValue(r1)     // Catch:{ Throwable -> 0x01fc }
            goto L_0x0204
        L_0x01fc:
            r0 = move-exception
            r12 = r0
            r12.printStackTrace()     // Catch:{ all -> 0x022b }
            goto L_0x0204
        L_0x0202:
            r11 = r20
        L_0x0204:
            if (r10 <= 0) goto L_0x020f
            long[] r0 = r1.e     // Catch:{ all -> 0x022b }
            r10 = 1
            r12 = r0[r10]     // Catch:{ all -> 0x022b }
            int r0 = (r12 > r4 ? 1 : (r12 == r4 ? 0 : -1))
            if (r0 <= 0) goto L_0x0210
        L_0x020f:
            r3 = 0
        L_0x0210:
            if (r3 == 0) goto L_0x0221
            java.lang.Object[] r0 = r1.b     // Catch:{ all -> 0x022b }
            r10 = 0
            r0 = r0[r10]     // Catch:{ all -> 0x022b }
            java.lang.Thread r10 = java.lang.Thread.currentThread()     // Catch:{ all -> 0x022b }
            if (r0 == r10) goto L_0x021e
            goto L_0x0221
        L_0x021e:
            r20 = r11
            goto L_0x01e1
        L_0x0221:
            r8.close()     // Catch:{ Throwable -> 0x0224 }
        L_0x0224:
            r7.close()     // Catch:{ Throwable -> 0x0227 }
        L_0x0227:
            r2.disconnect()     // Catch:{ Throwable -> 0x0254 }
            goto L_0x0254
        L_0x022b:
            r0 = move-exception
            r8.close()     // Catch:{ Throwable -> 0x022f }
        L_0x022f:
            throw r0     // Catch:{ all -> 0x0230 }
        L_0x0230:
            r0 = move-exception
            r7.close()     // Catch:{ Throwable -> 0x0234 }
        L_0x0234:
            r2.disconnect()     // Catch:{ Throwable -> 0x0237 }
        L_0x0237:
            throw r0     // Catch:{ Throwable -> 0x030b }
        L_0x0238:
            com.uc.webview.export.cyclone.UCKnownException r2 = new com.uc.webview.export.cyclone.UCKnownException     // Catch:{ Throwable -> 0x030b }
            r3 = 4020(0xfb4, float:5.633E-42)
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x030b }
            java.lang.String r5 = "httpcode:"
            r4.<init>(r5)     // Catch:{ Throwable -> 0x030b }
            r4.append(r0)     // Catch:{ Throwable -> 0x030b }
            java.lang.String r0 = " not correct."
            r4.append(r0)     // Catch:{ Throwable -> 0x030b }
            java.lang.String r0 = r4.toString()     // Catch:{ Throwable -> 0x030b }
            r2.<init>((int) r3, (java.lang.String) r0)     // Catch:{ Throwable -> 0x030b }
            throw r2     // Catch:{ Throwable -> 0x030b }
        L_0x0253:
            r3 = 0
        L_0x0254:
            if (r3 != 0) goto L_0x02c4
            long r2 = r6.length()     // Catch:{ Throwable -> 0x030b }
            int r0 = (r4 > r2 ? 1 : (r4 == r2 ? 0 : -1))
            if (r0 == 0) goto L_0x0298
            java.lang.String r0 = "sdk_dl_f"
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r0)     // Catch:{ Throwable -> 0x030b }
            java.lang.Object[] r0 = r1.b     // Catch:{ Throwable -> 0x030b }
            java.lang.RuntimeException r2 = new java.lang.RuntimeException     // Catch:{ Throwable -> 0x030b }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x030b }
            java.lang.String r7 = "Size mismatch:"
            r3.<init>(r7)     // Catch:{ Throwable -> 0x030b }
            long r6 = r6.length()     // Catch:{ Throwable -> 0x030b }
            r3.append(r6)     // Catch:{ Throwable -> 0x030b }
            java.lang.String r6 = "/"
            r3.append(r6)     // Catch:{ Throwable -> 0x030b }
            r3.append(r4)     // Catch:{ Throwable -> 0x030b }
            java.lang.String r3 = r3.toString()     // Catch:{ Throwable -> 0x030b }
            r2.<init>(r3)     // Catch:{ Throwable -> 0x030b }
            r3 = 1
            r0[r3] = r2     // Catch:{ Throwable -> 0x030b }
            if (r18 == 0) goto L_0x0296
            r2 = r18
            r2.onReceiveValue(r1)     // Catch:{ Throwable -> 0x028f }
            goto L_0x0296
        L_0x028f:
            r0 = move-exception
            r2 = r0
            r2.printStackTrace()     // Catch:{ Throwable -> 0x030b }
            monitor-exit(r23)
            return
        L_0x0296:
            monitor-exit(r23)
            return
        L_0x0298:
            java.lang.String r0 = "sdk_dl_s"
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r0)     // Catch:{ Throwable -> 0x030b }
            if (r19 == 0) goto L_0x02aa
            r2 = r19
            r2.onReceiveValue(r1)     // Catch:{ Throwable -> 0x02a5 }
            goto L_0x02aa
        L_0x02a5:
            r0 = move-exception
            r2 = r0
            r2.printStackTrace()     // Catch:{ Throwable -> 0x030b }
        L_0x02aa:
            android.webkit.ValueCallback<java.lang.Object[]> r0 = r1.f     // Catch:{ Throwable -> 0x02c0 }
            if (r0 == 0) goto L_0x02be
            android.webkit.ValueCallback<java.lang.Object[]> r0 = r1.f     // Catch:{ Throwable -> 0x02c0 }
            r2 = 1
            java.lang.Object[] r3 = new java.lang.Object[r2]     // Catch:{ Throwable -> 0x02c0 }
            r2 = 5
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)     // Catch:{ Throwable -> 0x02c0 }
            r4 = 0
            r3[r4] = r2     // Catch:{ Throwable -> 0x02c0 }
            r0.onReceiveValue(r3)     // Catch:{ Throwable -> 0x02c0 }
        L_0x02be:
            monitor-exit(r23)
            return
        L_0x02c0:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ Throwable -> 0x030b }
        L_0x02c4:
            monitor-exit(r23)
            return
        L_0x02c6:
            java.lang.String r0 = "sdk_dl_x"
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r0)     // Catch:{ Throwable -> 0x030b }
            if (r13 == 0) goto L_0x02d8
            r13.onReceiveValue(r1)     // Catch:{ Throwable -> 0x02d1 }
            goto L_0x02d8
        L_0x02d1:
            r0 = move-exception
            r2 = r0
            r2.printStackTrace()     // Catch:{ Throwable -> 0x030b }
            monitor-exit(r23)
            return
        L_0x02d8:
            monitor-exit(r23)
            return
        L_0x02da:
            r0 = move-exception
            r21 = r9
            r2 = r0
        L_0x02de:
            android.webkit.ValueCallback<java.lang.Object[]> r0 = r1.f     // Catch:{ Throwable -> 0x0306 }
            if (r0 == 0) goto L_0x030a
            android.webkit.ValueCallback<java.lang.Object[]> r0 = r1.f     // Catch:{ Throwable -> 0x0306 }
            r3 = 2
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch:{ Throwable -> 0x0306 }
            r4 = 6
            java.lang.Integer r4 = java.lang.Integer.valueOf(r4)     // Catch:{ Throwable -> 0x0306 }
            r5 = 0
            r3[r5] = r4     // Catch:{ Throwable -> 0x0306 }
            java.lang.Class r4 = r2.getClass()     // Catch:{ Throwable -> 0x0306 }
            java.lang.String r4 = r4.getName()     // Catch:{ Throwable -> 0x0306 }
            int r4 = r4.hashCode()     // Catch:{ Throwable -> 0x0306 }
            java.lang.Integer r4 = java.lang.Integer.valueOf(r4)     // Catch:{ Throwable -> 0x0306 }
            r5 = 1
            r3[r5] = r4     // Catch:{ Throwable -> 0x0306 }
            r0.onReceiveValue(r3)     // Catch:{ Throwable -> 0x0306 }
            goto L_0x030a
        L_0x0306:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ Throwable -> 0x030b }
        L_0x030a:
            throw r2     // Catch:{ Throwable -> 0x030b }
        L_0x030b:
            r0 = move-exception
        L_0x030c:
            r0.printStackTrace()     // Catch:{ all -> 0x0332 }
            java.lang.String r2 = "sdk_dl_e"
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r2)     // Catch:{ all -> 0x0332 }
            java.lang.Object[] r2 = r1.b     // Catch:{ all -> 0x0332 }
            r3 = 1
            r2[r3] = r0     // Catch:{ all -> 0x0332 }
            if (r21 == 0) goto L_0x0328
            r2 = r21
            r2.onReceiveValue(r1)     // Catch:{ Throwable -> 0x0321 }
            goto L_0x0328
        L_0x0321:
            r0 = move-exception
            r2 = r0
            r2.printStackTrace()     // Catch:{ all -> 0x0332 }
            monitor-exit(r23)
            return
        L_0x0328:
            monitor-exit(r23)
            return
        L_0x032a:
            java.lang.RuntimeException r0 = new java.lang.RuntimeException     // Catch:{ all -> 0x0332 }
            java.lang.String r2 = "Download should not run in UI thread."
            r0.<init>(r2)     // Catch:{ all -> 0x0332 }
            throw r0     // Catch:{ all -> 0x0332 }
        L_0x0332:
            r0 = move-exception
            monitor-exit(r23)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.utility.download.DownloadTask.run():void");
    }
}
