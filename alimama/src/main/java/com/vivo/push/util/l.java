package com.vivo.push.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import com.vivo.push.model.InsideNotificationItem;
import java.util.List;

/* compiled from: ImageDownTask */
public final class l extends AsyncTask<String, Void, List<Bitmap>> {
    private Context a;
    private InsideNotificationItem b;
    private long c;
    private boolean d;
    private int e = 0;

    /* access modifiers changed from: protected */
    public final /* synthetic */ void onPostExecute(Object obj) {
        List list = (List) obj;
        super.onPostExecute(list);
        p.c("ImageDownTask", "onPostExecute");
        if (this.b != null) {
            w.b().a("com.vivo.push.notify_key", this.c);
            NotifyAdapterUtil.pushNotification(this.a, list, this.b, this.c, this.e);
        }
    }

    public l(Context context, InsideNotificationItem insideNotificationItem, long j, boolean z) {
        this.a = context;
        this.b = insideNotificationItem;
        this.c = j;
        this.d = z;
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0096, code lost:
        if (r4 != null) goto L_0x0098;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:?, code lost:
        r4.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x00a4, code lost:
        if (r4 != null) goto L_0x0098;
     */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x00b0 A[SYNTHETIC, Splitter:B:43:0x00b0] */
    /* renamed from: a */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.List<android.graphics.Bitmap> doInBackground(java.lang.String... r10) {
        /*
            r9 = this;
            android.content.Context r0 = r9.a
            com.vivo.push.cache.ClientConfigManagerImpl r0 = com.vivo.push.cache.ClientConfigManagerImpl.getInstance(r0)
            int r0 = r0.getNotifyStyle()
            r9.e = r0
            boolean r0 = r9.d
            r1 = 0
            if (r0 != 0) goto L_0x0019
            java.lang.String r10 = "ImageDownTask"
            java.lang.String r0 = "bitmap is not display by forbid net"
            com.vivo.push.util.p.d(r10, r0)
            return r1
        L_0x0019:
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            r2 = 0
            r3 = 0
        L_0x0020:
            r4 = 2
            if (r3 >= r4) goto L_0x00bd
            r4 = r10[r3]
            java.lang.String r5 = "ImageDownTask"
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            java.lang.String r7 = "imgUrl="
            r6.<init>(r7)
            r6.append(r4)
            java.lang.String r7 = " i="
            r6.append(r7)
            r6.append(r3)
            java.lang.String r6 = r6.toString()
            com.vivo.push.util.p.d(r5, r6)
            boolean r5 = android.text.TextUtils.isEmpty(r4)
            if (r5 != 0) goto L_0x00b4
            java.net.URL r5 = new java.net.URL     // Catch:{ MalformedURLException -> 0x009c, IOException -> 0x008e, all -> 0x008c }
            r5.<init>(r4)     // Catch:{ MalformedURLException -> 0x009c, IOException -> 0x008e, all -> 0x008c }
            java.net.URLConnection r4 = r5.openConnection()     // Catch:{ MalformedURLException -> 0x009c, IOException -> 0x008e, all -> 0x008c }
            java.net.HttpURLConnection r4 = (java.net.HttpURLConnection) r4     // Catch:{ MalformedURLException -> 0x009c, IOException -> 0x008e, all -> 0x008c }
            r5 = 30000(0x7530, float:4.2039E-41)
            r4.setConnectTimeout(r5)     // Catch:{ MalformedURLException -> 0x009c, IOException -> 0x008e, all -> 0x008c }
            r5 = 1
            r4.setDoInput(r5)     // Catch:{ MalformedURLException -> 0x009c, IOException -> 0x008e, all -> 0x008c }
            r4.setUseCaches(r2)     // Catch:{ MalformedURLException -> 0x009c, IOException -> 0x008e, all -> 0x008c }
            r4.connect()     // Catch:{ MalformedURLException -> 0x009c, IOException -> 0x008e, all -> 0x008c }
            int r5 = r4.getResponseCode()     // Catch:{ MalformedURLException -> 0x009c, IOException -> 0x008e, all -> 0x008c }
            java.lang.String r6 = "ImageDownTask"
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ MalformedURLException -> 0x009c, IOException -> 0x008e, all -> 0x008c }
            java.lang.String r8 = "code="
            r7.<init>(r8)     // Catch:{ MalformedURLException -> 0x009c, IOException -> 0x008e, all -> 0x008c }
            r7.append(r5)     // Catch:{ MalformedURLException -> 0x009c, IOException -> 0x008e, all -> 0x008c }
            java.lang.String r7 = r7.toString()     // Catch:{ MalformedURLException -> 0x009c, IOException -> 0x008e, all -> 0x008c }
            com.vivo.push.util.p.c((java.lang.String) r6, (java.lang.String) r7)     // Catch:{ MalformedURLException -> 0x009c, IOException -> 0x008e, all -> 0x008c }
            r6 = 200(0xc8, float:2.8E-43)
            if (r5 != r6) goto L_0x0084
            java.io.InputStream r4 = r4.getInputStream()     // Catch:{ MalformedURLException -> 0x009c, IOException -> 0x008e, all -> 0x008c }
            android.graphics.Bitmap r5 = android.graphics.BitmapFactory.decodeStream(r4)     // Catch:{ MalformedURLException -> 0x009d, IOException -> 0x008f }
            goto L_0x0086
        L_0x0084:
            r4 = r1
            r5 = r4
        L_0x0086:
            if (r4 == 0) goto L_0x00a8
            r4.close()     // Catch:{ Exception -> 0x00a8 }
            goto L_0x00a8
        L_0x008c:
            r10 = move-exception
            goto L_0x00ae
        L_0x008e:
            r4 = r1
        L_0x008f:
            java.lang.String r5 = "ImageDownTask"
            java.lang.String r6 = "IOException"
            com.vivo.push.util.p.a((java.lang.String) r5, (java.lang.String) r6)     // Catch:{ all -> 0x00ac }
            if (r4 == 0) goto L_0x00a7
        L_0x0098:
            r4.close()     // Catch:{ Exception -> 0x00a7 }
            goto L_0x00a7
        L_0x009c:
            r4 = r1
        L_0x009d:
            java.lang.String r5 = "ImageDownTask"
            java.lang.String r6 = "MalformedURLException"
            com.vivo.push.util.p.a((java.lang.String) r5, (java.lang.String) r6)     // Catch:{ all -> 0x00ac }
            if (r4 == 0) goto L_0x00a7
            goto L_0x0098
        L_0x00a7:
            r5 = r1
        L_0x00a8:
            r0.add(r5)
            goto L_0x00b9
        L_0x00ac:
            r10 = move-exception
            r1 = r4
        L_0x00ae:
            if (r1 == 0) goto L_0x00b3
            r1.close()     // Catch:{ Exception -> 0x00b3 }
        L_0x00b3:
            throw r10
        L_0x00b4:
            if (r3 != 0) goto L_0x00b9
            r0.add(r1)
        L_0x00b9:
            int r3 = r3 + 1
            goto L_0x0020
        L_0x00bd:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.vivo.push.util.l.doInBackground(java.lang.String[]):java.util.List");
    }
}
