package android.taobao.windvane.extra.jsbridge;

import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.jsbridge.api.WVReporter;

public class WVReporterExtra extends WVReporter {
    public boolean execute(String str, String str2, WVCallBackContext wVCallBackContext) {
        if (!"reportPerformanceCheckResult".equals(str)) {
            return false;
        }
        reportPerformanceCheckResult(wVCallBackContext, str2);
        return super.execute(str, str2, wVCallBackContext);
    }

    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x0047 */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x005f A[Catch:{ Exception -> 0x0096 }] */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x0068 A[Catch:{ Exception -> 0x0096 }] */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0077 A[Catch:{ Exception -> 0x0096 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void reportPerformanceCheckResult(android.taobao.windvane.jsbridge.WVCallBackContext r11, java.lang.String r12) {
        /*
            r10 = this;
            org.json.JSONObject r0 = new org.json.JSONObject     // Catch:{ Exception -> 0x0096 }
            r0.<init>(r12)     // Catch:{ Exception -> 0x0096 }
            java.lang.String r12 = "score"
            r1 = 0
            long r1 = r0.optLong(r12, r1)     // Catch:{ Exception -> 0x0096 }
            java.lang.String r12 = "version"
            java.lang.String r3 = ""
            java.lang.String r7 = r0.optString(r12, r3)     // Catch:{ Exception -> 0x0096 }
            java.lang.String r12 = "result"
            java.lang.String r3 = ""
            java.lang.String r9 = r0.optString(r12, r3)     // Catch:{ Exception -> 0x0096 }
            java.lang.String r12 = "detail"
            java.lang.String r3 = ""
            java.lang.String r12 = r0.optString(r12, r3)     // Catch:{ Exception -> 0x0096 }
            android.taobao.windvane.webview.IWVWebView r0 = r10.mWebView     // Catch:{ Exception -> 0x0096 }
            java.lang.String r0 = r0.getUrl()     // Catch:{ Exception -> 0x0096 }
            r3 = 0
            android.taobao.windvane.webview.IWVWebView r4 = r10.mWebView     // Catch:{ Throwable -> 0x0047 }
            boolean r4 = r4 instanceof android.taobao.windvane.webview.WVWebView     // Catch:{ Throwable -> 0x0047 }
            if (r4 == 0) goto L_0x003a
            android.taobao.windvane.webview.IWVWebView r4 = r10.mWebView     // Catch:{ Throwable -> 0x0047 }
            android.taobao.windvane.webview.WVWebView r4 = (android.taobao.windvane.webview.WVWebView) r4     // Catch:{ Throwable -> 0x0047 }
            java.lang.String r4 = r4.bizCode     // Catch:{ Throwable -> 0x0047 }
        L_0x0038:
            r3 = r4
            goto L_0x0047
        L_0x003a:
            android.taobao.windvane.webview.IWVWebView r4 = r10.mWebView     // Catch:{ Throwable -> 0x0047 }
            boolean r4 = r4 instanceof android.taobao.windvane.extra.uc.WVUCWebView     // Catch:{ Throwable -> 0x0047 }
            if (r4 == 0) goto L_0x0047
            android.taobao.windvane.webview.IWVWebView r4 = r10.mWebView     // Catch:{ Throwable -> 0x0047 }
            android.taobao.windvane.extra.uc.WVUCWebView r4 = (android.taobao.windvane.extra.uc.WVUCWebView) r4     // Catch:{ Throwable -> 0x0047 }
            java.lang.String r4 = r4.bizCode     // Catch:{ Throwable -> 0x0047 }
            goto L_0x0038
        L_0x0047:
            android.net.Uri r4 = android.net.Uri.parse(r0)     // Catch:{ Exception -> 0x0096 }
            if (r4 == 0) goto L_0x0061
            boolean r5 = r4.isHierarchical()     // Catch:{ Exception -> 0x0096 }
            if (r5 == 0) goto L_0x0061
            java.lang.String r5 = "wvBizCode"
            java.lang.String r4 = r4.getQueryParameter(r5)     // Catch:{ Exception -> 0x0096 }
            boolean r5 = android.text.TextUtils.isEmpty(r4)     // Catch:{ Exception -> 0x0096 }
            if (r5 != 0) goto L_0x0061
            r8 = r4
            goto L_0x0062
        L_0x0061:
            r8 = r3
        L_0x0062:
            android.taobao.windvane.monitor.WVPerformanceMonitorInterface r3 = android.taobao.windvane.monitor.WVMonitorService.getPerformanceMonitor()     // Catch:{ Exception -> 0x0096 }
            if (r3 == 0) goto L_0x0071
            android.taobao.windvane.monitor.WVPerformanceMonitorInterface r3 = android.taobao.windvane.monitor.WVMonitorService.getPerformanceMonitor()     // Catch:{ Exception -> 0x0096 }
            r4 = r0
            r5 = r1
            r3.didPerformanceCheckResult(r4, r5, r7, r8, r9)     // Catch:{ Exception -> 0x0096 }
        L_0x0071:
            boolean r3 = android.taobao.windvane.util.TaoLog.getLogStatus()     // Catch:{ Exception -> 0x0096 }
            if (r3 == 0) goto L_0x0092
            java.lang.String r3 = "WindVaneWebPerfCheck"
            java.lang.String r4 = "WindVaneWebPerfCheck: %s|%d|%s"
            r5 = 3
            java.lang.Object[] r5 = new java.lang.Object[r5]     // Catch:{ Exception -> 0x0096 }
            r6 = 0
            r5[r6] = r0     // Catch:{ Exception -> 0x0096 }
            r0 = 1
            java.lang.Long r1 = java.lang.Long.valueOf(r1)     // Catch:{ Exception -> 0x0096 }
            r5[r0] = r1     // Catch:{ Exception -> 0x0096 }
            r0 = 2
            r5[r0] = r12     // Catch:{ Exception -> 0x0096 }
            java.lang.String r12 = java.lang.String.format(r4, r5)     // Catch:{ Exception -> 0x0096 }
            android.util.Log.e(r3, r12)     // Catch:{ Exception -> 0x0096 }
        L_0x0092:
            r11.success()     // Catch:{ Exception -> 0x0096 }
            goto L_0x00a8
        L_0x0096:
            r12 = move-exception
            android.taobao.windvane.jsbridge.WVResult r0 = new android.taobao.windvane.jsbridge.WVResult
            r0.<init>()
            java.lang.String r1 = "msg"
            java.lang.String r12 = r12.getMessage()
            r0.addData((java.lang.String) r1, (java.lang.String) r12)
            r11.error((android.taobao.windvane.jsbridge.WVResult) r0)
        L_0x00a8:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.extra.jsbridge.WVReporterExtra.reportPerformanceCheckResult(android.taobao.windvane.jsbridge.WVCallBackContext, java.lang.String):void");
    }
}
