package com.huawei.updatesdk.sdk.service.c;

import android.os.AsyncTask;
import android.text.TextUtils;
import com.huawei.updatesdk.sdk.a.d.e;
import com.huawei.updatesdk.sdk.service.c.a.a;
import com.huawei.updatesdk.sdk.service.c.a.c;
import com.huawei.updatesdk.sdk.service.c.a.d;
import java.util.concurrent.Executor;
import org.json.JSONException;
import org.json.JSONObject;

public class b extends AsyncTask<c, Void, d> {
    protected c a = null;
    private d b = null;
    private a c = null;
    private com.huawei.updatesdk.sdk.a.b.a d = null;
    private int e = 0;

    public b(c cVar, a aVar) {
        this.a = cVar;
        this.c = aVar;
    }

    private d a(String str, String str2, d dVar) {
        try {
            dVar.fromJson(new JSONObject(str2));
            dVar.a(0);
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | JSONException e2) {
            com.huawei.updatesdk.sdk.a.c.a.a.a.a("StoreTask", "parse json error", e2);
        }
        return dVar;
    }

    private void a(d dVar, int i, d.a aVar, Throwable th) {
        if (dVar != null) {
            dVar.a(i);
            dVar.a(aVar);
            dVar.a(th.toString());
        }
    }

    private void a(String str, String str2, String str3) {
        String str4;
        StringBuilder sb;
        String str5;
        if (TextUtils.isEmpty(str)) {
            str4 = "StoreTask";
            sb = new StringBuilder();
            sb.append("Store response error, method:");
            sb.append(this.a.g());
            sb.append(", url:");
            sb.append(str3);
            sb.append(", body:");
            sb.append(str2);
            str5 = ", resData == null";
        } else {
            str4 = "StoreTask";
            sb = new StringBuilder();
            sb.append("Store response error, method:");
            sb.append(this.a.g());
            sb.append(", url:");
            sb.append(str3);
            sb.append(", body:");
            sb.append(str2);
            str5 = ", resData is not json string";
        }
        sb.append(str5);
        com.huawei.updatesdk.sdk.a.c.a.a.a.a(str4, sb.toString());
    }

    private void a(String str, Throwable th) {
        StringBuilder sb = new StringBuilder();
        sb.append("invoke store error");
        sb.append(", exceptionType:");
        sb.append(th.toString());
        sb.append(", url:");
        sb.append(str);
        sb.append(", method:");
        sb.append(this.a.g());
        sb.append(", retryTimes:" + this.e);
        com.huawei.updatesdk.sdk.a.c.a.a.a.a("StoreTask", sb.toString(), th);
    }

    private boolean a(String str) {
        return TextUtils.isEmpty(str) || !e.c(str);
    }

    private boolean c(d dVar) {
        if (isCancelled()) {
            return false;
        }
        if (dVar.c() != 1 && dVar.c() != 2) {
            return false;
        }
        int i = this.e;
        this.e = i + 1;
        if (i >= 3) {
            com.huawei.updatesdk.sdk.a.c.a.a.a.a("StoreTask", "retry completed total times = " + this.e + ",response.responseCode = " + dVar.c());
            return false;
        }
        com.huawei.updatesdk.sdk.a.c.a.a.a.a("StoreTask", "retry times = " + this.e + ",response.responseCode = " + dVar.c());
        return true;
    }

    private void d() {
        d(this.b);
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x0047  */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x0052  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void d(com.huawei.updatesdk.sdk.service.c.a.d r5) {
        /*
            r4 = this;
            boolean r0 = r4.isCancelled()
            if (r0 != 0) goto L_0x0060
            com.huawei.updatesdk.sdk.service.c.a.a r0 = r4.c
            if (r0 == 0) goto L_0x0060
            if (r5 != 0) goto L_0x0059
            java.lang.String r0 = "StoreTask"
            java.lang.String r1 = "notifyResult, response is null"
            com.huawei.updatesdk.sdk.a.c.a.a.a.d(r0, r1)
            com.huawei.updatesdk.sdk.service.c.a.c r0 = r4.a     // Catch:{ InstantiationException -> 0x0028, IllegalAccessException -> 0x001f }
            java.lang.String r0 = r0.g()     // Catch:{ InstantiationException -> 0x0028, IllegalAccessException -> 0x001f }
            com.huawei.updatesdk.sdk.service.c.a.d r0 = com.huawei.updatesdk.sdk.service.c.a.a(r0)     // Catch:{ InstantiationException -> 0x0028, IllegalAccessException -> 0x001f }
            r5 = r0
            goto L_0x0045
        L_0x001f:
            r0 = move-exception
            java.lang.String r1 = "StoreTask"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            goto L_0x0030
        L_0x0028:
            r0 = move-exception
            java.lang.String r1 = "StoreTask"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
        L_0x0030:
            java.lang.String r3 = "notifyResult, create response error, method:"
            r2.append(r3)
            com.huawei.updatesdk.sdk.service.c.a.c r3 = r4.a
            java.lang.String r3 = r3.g()
            r2.append(r3)
            java.lang.String r2 = r2.toString()
            com.huawei.updatesdk.sdk.a.c.a.a.a.a(r1, r2, r0)
        L_0x0045:
            if (r5 != 0) goto L_0x0052
            com.huawei.updatesdk.sdk.service.c.a.d r5 = new com.huawei.updatesdk.sdk.service.c.a.d
            r5.<init>()
            com.huawei.updatesdk.sdk.service.c.a.d$a r0 = com.huawei.updatesdk.sdk.service.c.a.d.a.PARAM_ERROR
        L_0x004e:
            r5.a((com.huawei.updatesdk.sdk.service.c.a.d.a) r0)
            goto L_0x0055
        L_0x0052:
            com.huawei.updatesdk.sdk.service.c.a.d$a r0 = com.huawei.updatesdk.sdk.service.c.a.d.a.UNKNOWN_EXCEPTION
            goto L_0x004e
        L_0x0055:
            r0 = 1
            r5.a((int) r0)
        L_0x0059:
            com.huawei.updatesdk.sdk.service.c.a.a r0 = r4.c
            com.huawei.updatesdk.sdk.service.c.a.c r1 = r4.a
            r0.b(r1, r5)
        L_0x0060:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.huawei.updatesdk.sdk.service.c.b.d(com.huawei.updatesdk.sdk.service.c.a.d):void");
    }

    public final d a() {
        d dVar = null;
        do {
            if (this.e > 0 && dVar != null) {
                com.huawei.updatesdk.sdk.a.c.a.a.a.a("StoreTask", "call store error! responseCode:" + dVar.c() + ", retryTimes:" + this.e);
            }
            dVar = b();
            a(dVar);
        } while (c(dVar));
        this.b = dVar;
        return this.b;
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public d doInBackground(c... cVarArr) {
        com.huawei.updatesdk.sdk.a.c.a.a.a.a("StoreTask", "doInBackground, method:" + this.a.g());
        d a2 = a();
        if (this.c != null) {
            this.c.a(this.a, a2);
        }
        return a2;
    }

    public void a(d dVar) {
    }

    public final void a(Executor executor) {
        executeOnExecutor(executor, new c[]{this.a});
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x00df, code lost:
        r1 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x00e0, code lost:
        r6 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x00e3, code lost:
        r1 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x00e4, code lost:
        r2 = r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x00e6, code lost:
        r1 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x00e7, code lost:
        r6 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x00ea, code lost:
        r1 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x00eb, code lost:
        r6 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x00ee, code lost:
        r1 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x00ef, code lost:
        r6 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x00f2, code lost:
        r3 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x00f3, code lost:
        r6 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x00f5, code lost:
        r3 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x00f6, code lost:
        r6 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x00f9, code lost:
        r1 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x00fa, code lost:
        r6 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x00fd, code lost:
        r1 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x00fe, code lost:
        r6 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x0101, code lost:
        r2.a(r1.toString());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x0109, code lost:
        r1 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x010a, code lost:
        a(r2, 6, com.huawei.updatesdk.sdk.service.c.a.d.a.NO_PROGUARD, r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x0111, code lost:
        r1 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:0x0112, code lost:
        r6 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:61:0x0116, code lost:
        r1 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:62:0x0117, code lost:
        r6 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:64:0x011e, code lost:
        r1 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:65:0x011f, code lost:
        r6 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:67:0x0123, code lost:
        r3 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:68:0x0124, code lost:
        r6 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:70:0x012e, code lost:
        r1 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:71:0x012f, code lost:
        r6 = null;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x00e3 A[ExcHandler: ArrayIndexOutOfBoundsException (e java.lang.ArrayIndexOutOfBoundsException), Splitter:B:3:0x000e] */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x0101  */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x0109 A[ExcHandler: ArrayIndexOutOfBoundsException (e java.lang.ArrayIndexOutOfBoundsException), PHI: r2 
  PHI: (r2v11 com.huawei.updatesdk.sdk.service.c.a.d) = (r2v0 com.huawei.updatesdk.sdk.service.c.a.d), (r2v26 com.huawei.updatesdk.sdk.service.c.a.d), (r2v26 com.huawei.updatesdk.sdk.service.c.a.d) binds: [B:1:0x0004, B:16:0x0087, B:17:?] A[DONT_GENERATE, DONT_INLINE], Splitter:B:1:0x0004] */
    /* JADX WARNING: Removed duplicated region for block: B:76:0x013a  */
    /* JADX WARNING: Removed duplicated region for block: B:78:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.huawei.updatesdk.sdk.service.c.a.d b() {
        /*
            r9 = this;
            r0 = 5
            r1 = 2
            r2 = 0
            r3 = 1
            com.huawei.updatesdk.sdk.service.c.a.c r4 = r9.a     // Catch:{ ConnectException -> 0x012e, SocketTimeoutException | ConnectTimeoutException -> 0x0123, IOException -> 0x011e, IllegalArgumentException -> 0x0116, IllegalAccessException -> 0x0111, ArrayIndexOutOfBoundsException -> 0x0109, Exception -> 0x00fd }
            java.lang.String r4 = r4.g()     // Catch:{ ConnectException -> 0x012e, SocketTimeoutException | ConnectTimeoutException -> 0x0123, IOException -> 0x011e, IllegalArgumentException -> 0x0116, IllegalAccessException -> 0x0111, ArrayIndexOutOfBoundsException -> 0x0109, Exception -> 0x00fd }
            com.huawei.updatesdk.sdk.service.c.a.d r4 = com.huawei.updatesdk.sdk.service.c.a.a(r4)     // Catch:{ ConnectException -> 0x012e, SocketTimeoutException | ConnectTimeoutException -> 0x0123, IOException -> 0x011e, IllegalArgumentException -> 0x0116, IllegalAccessException -> 0x0111, ArrayIndexOutOfBoundsException -> 0x0109, Exception -> 0x00fd }
            boolean r5 = com.huawei.updatesdk.sdk.a.d.b.a.e()     // Catch:{ ConnectException -> 0x00f9, SocketTimeoutException -> 0x00f5, ConnectTimeoutException -> 0x00f2, IOException -> 0x00ee, IllegalArgumentException -> 0x00ea, IllegalAccessException -> 0x00e6, ArrayIndexOutOfBoundsException -> 0x00e3, Exception -> 0x00df }
            if (r5 != 0) goto L_0x001e
            r5 = 3
            r4.a((int) r5)     // Catch:{ ConnectException -> 0x00f9, SocketTimeoutException -> 0x00f5, ConnectTimeoutException -> 0x00f2, IOException -> 0x00ee, IllegalArgumentException -> 0x00ea, IllegalAccessException -> 0x00e6, ArrayIndexOutOfBoundsException -> 0x00e3, Exception -> 0x00df }
            com.huawei.updatesdk.sdk.service.c.a.d$a r5 = com.huawei.updatesdk.sdk.service.c.a.d.a.NO_NETWORK     // Catch:{ ConnectException -> 0x00f9, SocketTimeoutException -> 0x00f5, ConnectTimeoutException -> 0x00f2, IOException -> 0x00ee, IllegalArgumentException -> 0x00ea, IllegalAccessException -> 0x00e6, ArrayIndexOutOfBoundsException -> 0x00e3, Exception -> 0x00df }
            r4.a((com.huawei.updatesdk.sdk.service.c.a.d.a) r5)     // Catch:{ ConnectException -> 0x00f9, SocketTimeoutException -> 0x00f5, ConnectTimeoutException -> 0x00f2, IOException -> 0x00ee, IllegalArgumentException -> 0x00ea, IllegalAccessException -> 0x00e6, ArrayIndexOutOfBoundsException -> 0x00e3, Exception -> 0x00df }
            goto L_0x007e
        L_0x001e:
            com.huawei.updatesdk.sdk.service.c.a.c r5 = r9.a     // Catch:{ ConnectException -> 0x00f9, SocketTimeoutException -> 0x00f5, ConnectTimeoutException -> 0x00f2, IOException -> 0x00ee, IllegalArgumentException -> 0x00ea, IllegalAccessException -> 0x00e6, ArrayIndexOutOfBoundsException -> 0x00e3, Exception -> 0x00df }
            java.lang.String r5 = r5.d()     // Catch:{ ConnectException -> 0x00f9, SocketTimeoutException -> 0x00f5, ConnectTimeoutException -> 0x00f2, IOException -> 0x00ee, IllegalArgumentException -> 0x00ea, IllegalAccessException -> 0x00e6, ArrayIndexOutOfBoundsException -> 0x00e3, Exception -> 0x00df }
            com.huawei.updatesdk.sdk.service.c.a.c r6 = r9.a     // Catch:{ ConnectException -> 0x00f9, SocketTimeoutException -> 0x00f5, ConnectTimeoutException -> 0x00f2, IOException -> 0x00ee, IllegalArgumentException -> 0x00ea, IllegalAccessException -> 0x00e6, ArrayIndexOutOfBoundsException -> 0x00e3, Exception -> 0x00df }
            java.lang.String r6 = r6.h()     // Catch:{ ConnectException -> 0x00f9, SocketTimeoutException -> 0x00f5, ConnectTimeoutException -> 0x00f2, IOException -> 0x00ee, IllegalArgumentException -> 0x00ea, IllegalAccessException -> 0x00e6, ArrayIndexOutOfBoundsException -> 0x00e3, Exception -> 0x00df }
            java.lang.String r2 = "StoreTask"
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ ConnectException -> 0x00dd, SocketTimeoutException -> 0x00db, ConnectTimeoutException -> 0x00d9, IOException -> 0x00d7, IllegalArgumentException -> 0x00d5, IllegalAccessException -> 0x00d3, ArrayIndexOutOfBoundsException -> 0x00e3, Exception -> 0x00d1 }
            r7.<init>()     // Catch:{ ConnectException -> 0x00dd, SocketTimeoutException -> 0x00db, ConnectTimeoutException -> 0x00d9, IOException -> 0x00d7, IllegalArgumentException -> 0x00d5, IllegalAccessException -> 0x00d3, ArrayIndexOutOfBoundsException -> 0x00e3, Exception -> 0x00d1 }
            java.lang.String r8 = "callStore, method:"
            r7.append(r8)     // Catch:{ ConnectException -> 0x00dd, SocketTimeoutException -> 0x00db, ConnectTimeoutException -> 0x00d9, IOException -> 0x00d7, IllegalArgumentException -> 0x00d5, IllegalAccessException -> 0x00d3, ArrayIndexOutOfBoundsException -> 0x00e3, Exception -> 0x00d1 }
            com.huawei.updatesdk.sdk.service.c.a.c r8 = r9.a     // Catch:{ ConnectException -> 0x00dd, SocketTimeoutException -> 0x00db, ConnectTimeoutException -> 0x00d9, IOException -> 0x00d7, IllegalArgumentException -> 0x00d5, IllegalAccessException -> 0x00d3, ArrayIndexOutOfBoundsException -> 0x00e3, Exception -> 0x00d1 }
            java.lang.String r8 = r8.g()     // Catch:{ ConnectException -> 0x00dd, SocketTimeoutException -> 0x00db, ConnectTimeoutException -> 0x00d9, IOException -> 0x00d7, IllegalArgumentException -> 0x00d5, IllegalAccessException -> 0x00d3, ArrayIndexOutOfBoundsException -> 0x00e3, Exception -> 0x00d1 }
            r7.append(r8)     // Catch:{ ConnectException -> 0x00dd, SocketTimeoutException -> 0x00db, ConnectTimeoutException -> 0x00d9, IOException -> 0x00d7, IllegalArgumentException -> 0x00d5, IllegalAccessException -> 0x00d3, ArrayIndexOutOfBoundsException -> 0x00e3, Exception -> 0x00d1 }
            java.lang.String r8 = ", url:"
            r7.append(r8)     // Catch:{ ConnectException -> 0x00dd, SocketTimeoutException -> 0x00db, ConnectTimeoutException -> 0x00d9, IOException -> 0x00d7, IllegalArgumentException -> 0x00d5, IllegalAccessException -> 0x00d3, ArrayIndexOutOfBoundsException -> 0x00e3, Exception -> 0x00d1 }
            r7.append(r6)     // Catch:{ ConnectException -> 0x00dd, SocketTimeoutException -> 0x00db, ConnectTimeoutException -> 0x00d9, IOException -> 0x00d7, IllegalArgumentException -> 0x00d5, IllegalAccessException -> 0x00d3, ArrayIndexOutOfBoundsException -> 0x00e3, Exception -> 0x00d1 }
            java.lang.String r8 = ", body:"
            r7.append(r8)     // Catch:{ ConnectException -> 0x00dd, SocketTimeoutException -> 0x00db, ConnectTimeoutException -> 0x00d9, IOException -> 0x00d7, IllegalArgumentException -> 0x00d5, IllegalAccessException -> 0x00d3, ArrayIndexOutOfBoundsException -> 0x00e3, Exception -> 0x00d1 }
            r7.append(r5)     // Catch:{ ConnectException -> 0x00dd, SocketTimeoutException -> 0x00db, ConnectTimeoutException -> 0x00d9, IOException -> 0x00d7, IllegalArgumentException -> 0x00d5, IllegalAccessException -> 0x00d3, ArrayIndexOutOfBoundsException -> 0x00e3, Exception -> 0x00d1 }
            java.lang.String r7 = r7.toString()     // Catch:{ ConnectException -> 0x00dd, SocketTimeoutException -> 0x00db, ConnectTimeoutException -> 0x00d9, IOException -> 0x00d7, IllegalArgumentException -> 0x00d5, IllegalAccessException -> 0x00d3, ArrayIndexOutOfBoundsException -> 0x00e3, Exception -> 0x00d1 }
            com.huawei.updatesdk.sdk.a.c.a.a.a.a(r2, r7)     // Catch:{ ConnectException -> 0x00dd, SocketTimeoutException -> 0x00db, ConnectTimeoutException -> 0x00d9, IOException -> 0x00d7, IllegalArgumentException -> 0x00d5, IllegalAccessException -> 0x00d3, ArrayIndexOutOfBoundsException -> 0x00e3, Exception -> 0x00d1 }
            com.huawei.updatesdk.sdk.a.b.a r2 = new com.huawei.updatesdk.sdk.a.b.a     // Catch:{ ConnectException -> 0x00dd, SocketTimeoutException -> 0x00db, ConnectTimeoutException -> 0x00d9, IOException -> 0x00d7, IllegalArgumentException -> 0x00d5, IllegalAccessException -> 0x00d3, ArrayIndexOutOfBoundsException -> 0x00e3, Exception -> 0x00d1 }
            r2.<init>()     // Catch:{ ConnectException -> 0x00dd, SocketTimeoutException -> 0x00db, ConnectTimeoutException -> 0x00d9, IOException -> 0x00d7, IllegalArgumentException -> 0x00d5, IllegalAccessException -> 0x00d3, ArrayIndexOutOfBoundsException -> 0x00e3, Exception -> 0x00d1 }
            r9.d = r2     // Catch:{ ConnectException -> 0x00dd, SocketTimeoutException -> 0x00db, ConnectTimeoutException -> 0x00d9, IOException -> 0x00d7, IllegalArgumentException -> 0x00d5, IllegalAccessException -> 0x00d3, ArrayIndexOutOfBoundsException -> 0x00e3, Exception -> 0x00d1 }
            com.huawei.updatesdk.sdk.a.b.a r2 = r9.d     // Catch:{ ConnectException -> 0x00dd, SocketTimeoutException -> 0x00db, ConnectTimeoutException -> 0x00d9, IOException -> 0x00d7, IllegalArgumentException -> 0x00d5, IllegalAccessException -> 0x00d3, ArrayIndexOutOfBoundsException -> 0x00e3, Exception -> 0x00d1 }
            java.lang.String r7 = "UTF-8"
            java.lang.String r8 = r9.c()     // Catch:{ ConnectException -> 0x00dd, SocketTimeoutException -> 0x00db, ConnectTimeoutException -> 0x00d9, IOException -> 0x00d7, IllegalArgumentException -> 0x00d5, IllegalAccessException -> 0x00d3, ArrayIndexOutOfBoundsException -> 0x00e3, Exception -> 0x00d1 }
            com.huawei.updatesdk.sdk.a.b.a$a r2 = r2.a(r6, r5, r7, r8)     // Catch:{ ConnectException -> 0x00dd, SocketTimeoutException -> 0x00db, ConnectTimeoutException -> 0x00d9, IOException -> 0x00d7, IllegalArgumentException -> 0x00d5, IllegalAccessException -> 0x00d3, ArrayIndexOutOfBoundsException -> 0x00e3, Exception -> 0x00d1 }
            java.lang.String r2 = r2.a()     // Catch:{ ConnectException -> 0x00dd, SocketTimeoutException -> 0x00db, ConnectTimeoutException -> 0x00d9, IOException -> 0x00d7, IllegalArgumentException -> 0x00d5, IllegalAccessException -> 0x00d3, ArrayIndexOutOfBoundsException -> 0x00e3, Exception -> 0x00d1 }
            boolean r7 = r9.a((java.lang.String) r2)     // Catch:{ ConnectException -> 0x00dd, SocketTimeoutException -> 0x00db, ConnectTimeoutException -> 0x00d9, IOException -> 0x00d7, IllegalArgumentException -> 0x00d5, IllegalAccessException -> 0x00d3, ArrayIndexOutOfBoundsException -> 0x00e3, Exception -> 0x00d1 }
            if (r7 == 0) goto L_0x0081
            r4.a((int) r3)     // Catch:{ ConnectException -> 0x00dd, SocketTimeoutException -> 0x00db, ConnectTimeoutException -> 0x00d9, IOException -> 0x00d7, IllegalArgumentException -> 0x00d5, IllegalAccessException -> 0x00d3, ArrayIndexOutOfBoundsException -> 0x00e3, Exception -> 0x00d1 }
            com.huawei.updatesdk.sdk.service.c.a.d$a r7 = com.huawei.updatesdk.sdk.service.c.a.d.a.JSON_ERROR     // Catch:{ ConnectException -> 0x00dd, SocketTimeoutException -> 0x00db, ConnectTimeoutException -> 0x00d9, IOException -> 0x00d7, IllegalArgumentException -> 0x00d5, IllegalAccessException -> 0x00d3, ArrayIndexOutOfBoundsException -> 0x00e3, Exception -> 0x00d1 }
            r4.a((com.huawei.updatesdk.sdk.service.c.a.d.a) r7)     // Catch:{ ConnectException -> 0x00dd, SocketTimeoutException -> 0x00db, ConnectTimeoutException -> 0x00d9, IOException -> 0x00d7, IllegalArgumentException -> 0x00d5, IllegalAccessException -> 0x00d3, ArrayIndexOutOfBoundsException -> 0x00e3, Exception -> 0x00d1 }
            r9.a((java.lang.String) r2, (java.lang.String) r5, (java.lang.String) r6)     // Catch:{ ConnectException -> 0x00dd, SocketTimeoutException -> 0x00db, ConnectTimeoutException -> 0x00d9, IOException -> 0x00d7, IllegalArgumentException -> 0x00d5, IllegalAccessException -> 0x00d3, ArrayIndexOutOfBoundsException -> 0x00e3, Exception -> 0x00d1 }
        L_0x007e:
            r2 = r4
            goto L_0x0138
        L_0x0081:
            com.huawei.updatesdk.sdk.service.c.a.d r2 = r9.a((java.lang.String) r5, (java.lang.String) r2, (com.huawei.updatesdk.sdk.service.c.a.d) r4)     // Catch:{ ConnectException -> 0x00dd, SocketTimeoutException -> 0x00db, ConnectTimeoutException -> 0x00d9, IOException -> 0x00d7, IllegalArgumentException -> 0x00d5, IllegalAccessException -> 0x00d3, ArrayIndexOutOfBoundsException -> 0x00e3, Exception -> 0x00d1 }
            java.lang.String r4 = "StoreTask"
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ ConnectException -> 0x00ce, SocketTimeoutException | ConnectTimeoutException -> 0x00cb, IOException -> 0x00c8, IllegalArgumentException -> 0x00c5, IllegalAccessException -> 0x00c2, ArrayIndexOutOfBoundsException -> 0x0109, Exception -> 0x00bf }
            r7.<init>()     // Catch:{ ConnectException -> 0x00ce, SocketTimeoutException | ConnectTimeoutException -> 0x00cb, IOException -> 0x00c8, IllegalArgumentException -> 0x00c5, IllegalAccessException -> 0x00c2, ArrayIndexOutOfBoundsException -> 0x0109, Exception -> 0x00bf }
            java.lang.String r8 = "callStore response, method:"
            r7.append(r8)     // Catch:{ ConnectException -> 0x00ce, SocketTimeoutException | ConnectTimeoutException -> 0x00cb, IOException -> 0x00c8, IllegalArgumentException -> 0x00c5, IllegalAccessException -> 0x00c2, ArrayIndexOutOfBoundsException -> 0x0109, Exception -> 0x00bf }
            com.huawei.updatesdk.sdk.service.c.a.c r8 = r9.a     // Catch:{ ConnectException -> 0x00ce, SocketTimeoutException | ConnectTimeoutException -> 0x00cb, IOException -> 0x00c8, IllegalArgumentException -> 0x00c5, IllegalAccessException -> 0x00c2, ArrayIndexOutOfBoundsException -> 0x0109, Exception -> 0x00bf }
            java.lang.String r8 = r8.g()     // Catch:{ ConnectException -> 0x00ce, SocketTimeoutException | ConnectTimeoutException -> 0x00cb, IOException -> 0x00c8, IllegalArgumentException -> 0x00c5, IllegalAccessException -> 0x00c2, ArrayIndexOutOfBoundsException -> 0x0109, Exception -> 0x00bf }
            r7.append(r8)     // Catch:{ ConnectException -> 0x00ce, SocketTimeoutException | ConnectTimeoutException -> 0x00cb, IOException -> 0x00c8, IllegalArgumentException -> 0x00c5, IllegalAccessException -> 0x00c2, ArrayIndexOutOfBoundsException -> 0x0109, Exception -> 0x00bf }
            java.lang.String r8 = ", url:"
            r7.append(r8)     // Catch:{ ConnectException -> 0x00ce, SocketTimeoutException | ConnectTimeoutException -> 0x00cb, IOException -> 0x00c8, IllegalArgumentException -> 0x00c5, IllegalAccessException -> 0x00c2, ArrayIndexOutOfBoundsException -> 0x0109, Exception -> 0x00bf }
            r7.append(r6)     // Catch:{ ConnectException -> 0x00ce, SocketTimeoutException | ConnectTimeoutException -> 0x00cb, IOException -> 0x00c8, IllegalArgumentException -> 0x00c5, IllegalAccessException -> 0x00c2, ArrayIndexOutOfBoundsException -> 0x0109, Exception -> 0x00bf }
            java.lang.String r8 = ", body:"
            r7.append(r8)     // Catch:{ ConnectException -> 0x00ce, SocketTimeoutException | ConnectTimeoutException -> 0x00cb, IOException -> 0x00c8, IllegalArgumentException -> 0x00c5, IllegalAccessException -> 0x00c2, ArrayIndexOutOfBoundsException -> 0x0109, Exception -> 0x00bf }
            r7.append(r5)     // Catch:{ ConnectException -> 0x00ce, SocketTimeoutException | ConnectTimeoutException -> 0x00cb, IOException -> 0x00c8, IllegalArgumentException -> 0x00c5, IllegalAccessException -> 0x00c2, ArrayIndexOutOfBoundsException -> 0x0109, Exception -> 0x00bf }
            java.lang.String r5 = ", Receive Json msg:"
            r7.append(r5)     // Catch:{ ConnectException -> 0x00ce, SocketTimeoutException | ConnectTimeoutException -> 0x00cb, IOException -> 0x00c8, IllegalArgumentException -> 0x00c5, IllegalAccessException -> 0x00c2, ArrayIndexOutOfBoundsException -> 0x0109, Exception -> 0x00bf }
            java.lang.String r5 = r2.toJson()     // Catch:{ ConnectException -> 0x00ce, SocketTimeoutException | ConnectTimeoutException -> 0x00cb, IOException -> 0x00c8, IllegalArgumentException -> 0x00c5, IllegalAccessException -> 0x00c2, ArrayIndexOutOfBoundsException -> 0x0109, Exception -> 0x00bf }
            r7.append(r5)     // Catch:{ ConnectException -> 0x00ce, SocketTimeoutException | ConnectTimeoutException -> 0x00cb, IOException -> 0x00c8, IllegalArgumentException -> 0x00c5, IllegalAccessException -> 0x00c2, ArrayIndexOutOfBoundsException -> 0x0109, Exception -> 0x00bf }
            java.lang.String r5 = r7.toString()     // Catch:{ ConnectException -> 0x00ce, SocketTimeoutException | ConnectTimeoutException -> 0x00cb, IOException -> 0x00c8, IllegalArgumentException -> 0x00c5, IllegalAccessException -> 0x00c2, ArrayIndexOutOfBoundsException -> 0x0109, Exception -> 0x00bf }
            com.huawei.updatesdk.sdk.a.c.a.a.a.a(r4, r5)     // Catch:{ ConnectException -> 0x00ce, SocketTimeoutException | ConnectTimeoutException -> 0x00cb, IOException -> 0x00c8, IllegalArgumentException -> 0x00c5, IllegalAccessException -> 0x00c2, ArrayIndexOutOfBoundsException -> 0x0109, Exception -> 0x00bf }
            goto L_0x0138
        L_0x00bf:
            r1 = move-exception
            goto L_0x00ff
        L_0x00c2:
            r1 = move-exception
            goto L_0x0113
        L_0x00c5:
            r1 = move-exception
            goto L_0x0118
        L_0x00c8:
            r1 = move-exception
            goto L_0x0120
        L_0x00cb:
            r3 = move-exception
            goto L_0x0125
        L_0x00ce:
            r1 = move-exception
            goto L_0x0130
        L_0x00d1:
            r1 = move-exception
            goto L_0x00e1
        L_0x00d3:
            r1 = move-exception
            goto L_0x00e8
        L_0x00d5:
            r1 = move-exception
            goto L_0x00ec
        L_0x00d7:
            r1 = move-exception
            goto L_0x00f0
        L_0x00d9:
            r3 = move-exception
            goto L_0x00f7
        L_0x00db:
            r3 = move-exception
            goto L_0x00f7
        L_0x00dd:
            r1 = move-exception
            goto L_0x00fb
        L_0x00df:
            r1 = move-exception
            r6 = r2
        L_0x00e1:
            r2 = r4
            goto L_0x00ff
        L_0x00e3:
            r1 = move-exception
            r2 = r4
            goto L_0x010a
        L_0x00e6:
            r1 = move-exception
            r6 = r2
        L_0x00e8:
            r2 = r4
            goto L_0x0113
        L_0x00ea:
            r1 = move-exception
            r6 = r2
        L_0x00ec:
            r2 = r4
            goto L_0x0118
        L_0x00ee:
            r1 = move-exception
            r6 = r2
        L_0x00f0:
            r2 = r4
            goto L_0x0120
        L_0x00f2:
            r3 = move-exception
            r6 = r2
            goto L_0x00f7
        L_0x00f5:
            r3 = move-exception
            r6 = r2
        L_0x00f7:
            r2 = r4
            goto L_0x0125
        L_0x00f9:
            r1 = move-exception
            r6 = r2
        L_0x00fb:
            r2 = r4
            goto L_0x0130
        L_0x00fd:
            r1 = move-exception
            r6 = r2
        L_0x00ff:
            if (r2 == 0) goto L_0x0135
            java.lang.String r3 = r1.toString()
            r2.a((java.lang.String) r3)
            goto L_0x0135
        L_0x0109:
            r1 = move-exception
        L_0x010a:
            r3 = 6
            com.huawei.updatesdk.sdk.service.c.a.d$a r4 = com.huawei.updatesdk.sdk.service.c.a.d.a.NO_PROGUARD
            r9.a(r2, r3, r4, r1)
            goto L_0x0138
        L_0x0111:
            r1 = move-exception
            r6 = r2
        L_0x0113:
            com.huawei.updatesdk.sdk.service.c.a.d$a r4 = com.huawei.updatesdk.sdk.service.c.a.d.a.UNKNOWN_EXCEPTION
            goto L_0x0132
        L_0x0116:
            r1 = move-exception
            r6 = r2
        L_0x0118:
            com.huawei.updatesdk.sdk.service.c.a.d$a r3 = com.huawei.updatesdk.sdk.service.c.a.d.a.PARAM_ERROR
            r9.a(r2, r0, r3, r1)
            goto L_0x0135
        L_0x011e:
            r1 = move-exception
            r6 = r2
        L_0x0120:
            com.huawei.updatesdk.sdk.service.c.a.d$a r4 = com.huawei.updatesdk.sdk.service.c.a.d.a.IO_EXCEPTION
            goto L_0x0132
        L_0x0123:
            r3 = move-exception
            r6 = r2
        L_0x0125:
            com.huawei.updatesdk.sdk.service.c.a.d$a r4 = com.huawei.updatesdk.sdk.service.c.a.d.a.CONNECT_EXCEPTION
            r9.a(r2, r1, r4, r3)
            r9.a(r6, r3)
            goto L_0x0138
        L_0x012e:
            r1 = move-exception
            r6 = r2
        L_0x0130:
            com.huawei.updatesdk.sdk.service.c.a.d$a r4 = com.huawei.updatesdk.sdk.service.c.a.d.a.CONNECT_EXCEPTION
        L_0x0132:
            r9.a(r2, r3, r4, r1)
        L_0x0135:
            r9.a(r6, r1)
        L_0x0138:
            if (r2 != 0) goto L_0x0147
            com.huawei.updatesdk.sdk.service.c.a.d r2 = new com.huawei.updatesdk.sdk.service.c.a.d
            r2.<init>()
            r2.a((int) r0)
            com.huawei.updatesdk.sdk.service.c.a.d$a r0 = com.huawei.updatesdk.sdk.service.c.a.d.a.PARAM_ERROR
            r2.a((com.huawei.updatesdk.sdk.service.c.a.d.a) r0)
        L_0x0147:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.huawei.updatesdk.sdk.service.c.b.b():com.huawei.updatesdk.sdk.service.c.a.d");
    }

    /* access modifiers changed from: protected */
    /* renamed from: b */
    public void onPostExecute(d dVar) {
        com.huawei.updatesdk.sdk.a.c.a.a.a.a("StoreTask", "onPostExecute, method:" + this.a.g());
        d();
    }

    /* access modifiers changed from: protected */
    public String c() {
        return "Android/1.0";
    }
}
