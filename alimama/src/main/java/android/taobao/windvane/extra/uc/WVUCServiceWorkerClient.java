package android.taobao.windvane.extra.uc;

import com.uc.webview.export.ServiceWorkerClient;

public class WVUCServiceWorkerClient extends ServiceWorkerClient {
    private static final String TAG = "WVUCServiceWorkerClient";

    /* JADX WARNING: Removed duplicated region for block: B:44:0x0111 A[SYNTHETIC, Splitter:B:44:0x0111] */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x0120  */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x015f  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.uc.webview.export.WebResourceResponse shouldInterceptRequest(com.uc.webview.export.WebResourceRequest r9) {
        /*
            r8 = this;
            android.net.Uri r0 = r9.getUrl()
            java.lang.String r0 = r0.toString()
            android.taobao.windvane.service.WVEventService r1 = android.taobao.windvane.service.WVEventService.getInstance()
            r2 = 0
            java.lang.Object[] r2 = new java.lang.Object[r2]
            r7 = 0
            r3 = 1004(0x3ec, float:1.407E-42)
            android.taobao.windvane.service.WVEventResult r1 = r1.onEvent(r3, r7, r0, r2)
            boolean r2 = r1.isSuccess
            if (r2 == 0) goto L_0x00a5
            java.lang.Object r2 = r1.resultObj
            if (r2 == 0) goto L_0x00a5
            java.lang.Object r2 = r1.resultObj
            boolean r2 = r2 instanceof android.taobao.windvane.webview.WVWrapWebResourceResponse
            if (r2 == 0) goto L_0x00a5
            java.lang.Object r9 = r1.resultObj
            android.taobao.windvane.webview.WVWrapWebResourceResponse r9 = (android.taobao.windvane.webview.WVWrapWebResourceResponse) r9
            boolean r1 = android.taobao.windvane.util.TaoLog.getLogStatus()
            if (r1 == 0) goto L_0x0044
            java.lang.String r1 = "WVUCServiceWorkerClient"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "预加载命中 : "
            r2.append(r3)
            r2.append(r0)
            java.lang.String r2 = r2.toString()
            android.taobao.windvane.util.TaoLog.d(r1, r2)
        L_0x0044:
            com.uc.webview.export.WebResourceResponse r1 = new com.uc.webview.export.WebResourceResponse
            java.lang.String r2 = r9.mMimeType
            java.lang.String r3 = r9.mEncoding
            java.io.InputStream r4 = r9.mInputStream
            r1.<init>(r2, r3, r4)
            java.util.Map<java.lang.String, java.lang.String> r2 = r9.mHeaders     // Catch:{ Throwable -> 0x00a0 }
            if (r2 == 0) goto L_0x008a
            boolean r0 = android.taobao.windvane.config.WVServerConfig.isAllowAccess(r0)     // Catch:{ Throwable -> 0x0085 }
            if (r0 == 0) goto L_0x007f
            java.util.Map<java.lang.String, java.lang.String> r0 = r9.mHeaders     // Catch:{ Throwable -> 0x0085 }
            java.lang.String r2 = "Access-Control-Allow-Origin"
            boolean r0 = r0.containsKey(r2)     // Catch:{ Throwable -> 0x0085 }
            if (r0 != 0) goto L_0x007f
            java.util.HashMap r0 = new java.util.HashMap     // Catch:{ Throwable -> 0x0085 }
            r0.<init>()     // Catch:{ Throwable -> 0x0085 }
            java.util.Map<java.lang.String, java.lang.String> r9 = r9.mHeaders     // Catch:{ Throwable -> 0x0085 }
            r0.putAll(r9)     // Catch:{ Throwable -> 0x0085 }
            java.lang.String r9 = "Access-Control-Allow-Origin"
            java.lang.String r2 = "*"
            r0.put(r9, r2)     // Catch:{ Throwable -> 0x0085 }
            r1.setResponseHeaders(r0)     // Catch:{ Throwable -> 0x0085 }
            java.lang.String r9 = "WVUCServiceWorkerClient"
            java.lang.String r0 = "add cross origin header"
            android.taobao.windvane.util.TaoLog.w(r9, r0)     // Catch:{ Throwable -> 0x0085 }
            goto L_0x00a4
        L_0x007f:
            java.util.Map<java.lang.String, java.lang.String> r9 = r9.mHeaders     // Catch:{ Throwable -> 0x0085 }
            r1.setResponseHeaders(r9)     // Catch:{ Throwable -> 0x0085 }
            goto L_0x00a4
        L_0x0085:
            r9 = move-exception
            r9.printStackTrace()     // Catch:{ Throwable -> 0x00a0 }
            goto L_0x00a4
        L_0x008a:
            boolean r9 = android.taobao.windvane.config.WVServerConfig.isAllowAccess(r0)     // Catch:{ Throwable -> 0x00a0 }
            if (r9 == 0) goto L_0x00a4
            java.util.HashMap r9 = new java.util.HashMap     // Catch:{ Throwable -> 0x00a0 }
            r9.<init>()     // Catch:{ Throwable -> 0x00a0 }
            java.lang.String r0 = "Access-Control-Allow-Origin"
            java.lang.String r2 = "*"
            r9.put(r0, r2)     // Catch:{ Throwable -> 0x00a0 }
            r1.setResponseHeaders(r9)     // Catch:{ Throwable -> 0x00a0 }
            goto L_0x00a4
        L_0x00a0:
            r9 = move-exception
            r9.printStackTrace()
        L_0x00a4:
            return r1
        L_0x00a5:
            android.taobao.windvane.monitor.WVPerformanceMonitorInterface r1 = android.taobao.windvane.monitor.WVMonitorService.getPerformanceMonitor()
            if (r1 == 0) goto L_0x00ba
            android.taobao.windvane.monitor.WVPerformanceMonitorInterface r1 = android.taobao.windvane.monitor.WVMonitorService.getPerformanceMonitor()
            r3 = 0
            int r4 = android.taobao.windvane.extra.uc.WVUCWebView.getFromType()
            r5 = 0
            r6 = 0
            r2 = r0
            r1.didGetResourceStatusCode(r2, r3, r4, r5, r6)
        L_0x00ba:
            android.taobao.windvane.cache.WVCacheManager r1 = android.taobao.windvane.cache.WVCacheManager.getInstance()
            boolean r1 = r1.isCacheEnabled(r0)
            if (r1 == 0) goto L_0x0116
            java.lang.String r1 = android.taobao.windvane.util.WVUrlUtil.removeScheme(r0)
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            android.taobao.windvane.cache.WVCacheManager r3 = android.taobao.windvane.cache.WVCacheManager.getInstance()
            r4 = 1
            java.lang.String r3 = r3.getCacheDir(r4)
            r2.append(r3)
            java.lang.String r3 = java.io.File.separator
            r2.append(r3)
            java.lang.String r1 = android.taobao.windvane.util.DigestUtils.md5ToHex((java.lang.String) r1)
            r2.append(r1)
            java.lang.String r1 = r2.toString()
            java.io.File r2 = new java.io.File     // Catch:{ Exception -> 0x010e }
            r2.<init>(r1)     // Catch:{ Exception -> 0x010e }
            java.io.FileInputStream r1 = new java.io.FileInputStream     // Catch:{ Exception -> 0x010e }
            r1.<init>(r2)     // Catch:{ Exception -> 0x010e }
            com.uc.webview.export.WebResourceResponse r2 = new com.uc.webview.export.WebResourceResponse     // Catch:{ Exception -> 0x010c }
            java.lang.String r3 = "image/png"
            java.lang.String r4 = "UTF-8"
            r2.<init>(r3, r4, r1)     // Catch:{ Exception -> 0x010c }
            java.util.HashMap r3 = new java.util.HashMap     // Catch:{ Exception -> 0x010c }
            r3.<init>()     // Catch:{ Exception -> 0x010c }
            java.lang.String r4 = "Access-Control-Allow-Origin"
            java.lang.String r5 = "*"
            r3.put(r4, r5)     // Catch:{ Exception -> 0x010c }
            r2.setResponseHeaders(r3)     // Catch:{ Exception -> 0x010c }
            return r2
        L_0x010c:
            goto L_0x010f
        L_0x010e:
            r1 = r7
        L_0x010f:
            if (r1 == 0) goto L_0x0116
            r1.close()     // Catch:{ IOException -> 0x0115 }
            goto L_0x0116
        L_0x0115:
        L_0x0116:
            android.taobao.windvane.cache.WVMemoryCache r1 = android.taobao.windvane.cache.WVMemoryCache.getInstance()
            android.taobao.windvane.cache.WVMemoryCacheInfo r1 = r1.getMemoryCacheByUrl(r0)
            if (r1 == 0) goto L_0x015f
            long r2 = java.lang.System.currentTimeMillis()
            long r4 = r1.cachedTime
            long r2 = r2 - r4
            r4 = 2000(0x7d0, double:9.88E-321)
            int r9 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r9 >= 0) goto L_0x0141
            com.uc.webview.export.WebResourceResponse r7 = new com.uc.webview.export.WebResourceResponse
            android.taobao.windvane.util.MimeTypeEnum r9 = android.taobao.windvane.util.MimeTypeEnum.HTML
            java.lang.String r9 = r9.getMimeType()
            java.lang.String r2 = "UTF-8"
            java.io.ByteArrayInputStream r3 = new java.io.ByteArrayInputStream
            byte[] r1 = r1.mCachedDatas
            r3.<init>(r1)
            r7.<init>(r9, r2, r3)
        L_0x0141:
            android.taobao.windvane.cache.WVMemoryCache r9 = android.taobao.windvane.cache.WVMemoryCache.getInstance()
            r9.clearCacheByUrl(r0)
            java.lang.String r9 = "WVUCServiceWorkerClient"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "WVMemoryCacheInfo 命中 : "
            r1.append(r2)
            r1.append(r0)
            java.lang.String r0 = r1.toString()
            android.taobao.windvane.util.TaoLog.i(r9, r0)
            return r7
        L_0x015f:
            boolean r1 = android.taobao.windvane.util.TaoLog.getLogStatus()
            if (r1 == 0) goto L_0x017b
            java.lang.String r1 = "WVUCServiceWorkerClient"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "shouldInterceptRequest : "
            r2.append(r3)
            r2.append(r0)
            java.lang.String r0 = r2.toString()
            android.taobao.windvane.util.TaoLog.d(r1, r0)
        L_0x017b:
            com.uc.webview.export.WebResourceResponse r9 = super.shouldInterceptRequest(r9)
            return r9
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.extra.uc.WVUCServiceWorkerClient.shouldInterceptRequest(com.uc.webview.export.WebResourceRequest):com.uc.webview.export.WebResourceResponse");
    }
}
