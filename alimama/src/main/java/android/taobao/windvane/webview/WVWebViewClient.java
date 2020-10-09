package android.taobao.windvane.webview;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.taobao.windvane.config.GlobalConfig;
import android.taobao.windvane.config.WVDomainConfig;
import android.taobao.windvane.config.WVServerConfig;
import android.taobao.windvane.jsbridge.WVJsBridge;
import android.taobao.windvane.jspatch.WVJsPatch;
import android.taobao.windvane.monitor.WVErrorMonitorInterface;
import android.taobao.windvane.monitor.WVMonitorService;
import android.taobao.windvane.service.WVEventService;
import android.taobao.windvane.urlintercept.WVURLInterceptService;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.util.WVUrlUtil;
import android.text.TextUtils;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.taobao.weex.el.parse.Operators;

import java.util.HashMap;

public class WVWebViewClient extends WebViewClient {
    private static final String TAG = "WVWebViewClient";
    private String currentUrl = null;
    public WebViewClient extraWebViewClient = null;
    boolean isError;
    protected Context mContext;
    /* access modifiers changed from: private */
    public long mPageFinshTime = 0;

    public WVWebViewClient(Context context) {
        this.mContext = context;
    }

    public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
        this.isError = false;
        if (webView instanceof IWVWebView) {
            WVEventService.getInstance().onEvent(1001, (IWVWebView) webView, str, bitmap);
        }
        if (TaoLog.getLogStatus()) {
            TaoLog.i(TAG, "onPageStarted : " + str);
        }
        this.currentUrl = str;
        ((WVWebView) webView).onMessage(400, (Object) null);
        if (WVMonitorService.getPerformanceMonitor() != null) {
            WVMonitorService.getPerformanceMonitor().didPageStartLoadAtTime(str, System.currentTimeMillis());
        }
        WVJsBridge.getInstance().tryToRunTailBridges();
        if (this.extraWebViewClient != null) {
            this.extraWebViewClient.onPageStarted(webView, str, bitmap);
        }
    }

    public void onPageFinished(WebView webView, final String str) {
        TaoLog.i(TAG, "onPageFinished : " + str);
        this.mPageFinshTime = System.currentTimeMillis();
        if (!this.isError && webView.getVisibility() == 4) {
            this.isError = false;
            webView.setVisibility(0);
        }
        super.onPageFinished(webView, str);
        if (webView instanceof WVWebView) {
            ((WVWebView) webView).setCurrentUrl(str, "onPageFinished");
        }
        if (webView instanceof IWVWebView) {
            IWVWebView iWVWebView = (IWVWebView) webView;
            WVEventService.getInstance().onEvent(1002, iWVWebView, str, new Object[0]);
            WVJsPatch.getInstance().execute(iWVWebView, str);
        }
        final WVWebView wVWebView = (WVWebView) webView;
        if (TaoLog.getLogStatus()) {
            TaoLog.v(TAG, "Page finish: " + str);
        }
        wVWebView.onMessage(401, (Object) null);
        wVWebView.fireEvent("WindVaneReady", String.format("{'version':'%s'}", new Object[]{GlobalConfig.VERSION}));
        wVWebView.evaluateJavascript("(function(p){if(!p||!p.timing)return;var t=p.timing,s=t.navigationStart,sc=t.secureConnectionStart,dc=t.domComplete,lee=t.loadEventEnd;return JSON.stringify({dns:t.domainLookupEnd-t.domainLookupStart,c:t.connectEnd-t.connectStart,scs:sc>0?sc-s:0,req:t.requestStart-s,rps:t.responseStart-s,rpe:t.responseEnd-s,dl:t.domLoading-s,dcl:t.domContentLoadedEventEnd-s,dc:dc>0?dc-s:0,lee:lee>0?lee-s:0})})(window.performance)", new ValueCallback<String>() {
            public void onReceiveValue(String str) {
                if (WVMonitorService.getPerformanceMonitor() != null) {
                    WVMonitorService.getPerformanceMonitor().didPagePerformanceInfo(str, str);
                    WVMonitorService.getPerformanceMonitor().didPageFinishLoadAtTime(str, WVWebViewClient.this.mPageFinshTime);
                }
            }
        });
        wVWebView.evaluateJavascript("javascript:(function(f){if(f.__windvane__.call) return true; else return false})(window)", new ValueCallback<String>() {
            public void onReceiveValue(String str) {
                TaoLog.i("WVJsBridge", "has windvane :" + str);
                if ("false".equals(str)) {
                    wVWebView.loadUrl("javascript:(function(f){try{if(f.__windvane__.nativeCall){var h=f.__windvane__||(f.__windvane__={});var c=\"wvapi:\"+(Math.floor(Math.random()*(1<<16))),a=0,b={},g=function(j){if(j&&typeof j==\"string\"){try{return JSON.parse(j)}catch(i){return{ret:\"HY_RESULT_PARSE_ERROR\"}}}else{return j||{}}};h.call=function(i,m,l,e,k){if(typeof l!=\"function\"){l=null}if(typeof e!=\"function\"){e=null}var j=c+(a++);b[j]={s:l,f:e,};if(k>0){b[j].t=setTimeout(function(){h.onFailure(j,{ret:\"HY_TIMEOUT\"})},k)}if(!m){m={}}if(typeof m!=\"string\"){m=JSON.stringify(m)}f.__windvane__.nativeCall(i,m,j,location.href)};h.find=function(i,j){var e=b[i]||{};if(e.t){clearTimeout(e.t);delete e.t}if(!j){delete b[i]}return e};h.onSuccess=function(j,e,k){var i=h.find(j,k).s;if(i){i(g(e))}};h.onFailure=function(j,e){var i=h.find(j,false).f;if(i){i(g(e))}}}}catch(d){}})(window);");
                }
            }
        });
        if (this.extraWebViewClient != null) {
            this.extraWebViewClient.onPageFinished(webView, str);
        }
    }

    public void onReceivedError(WebView webView, int i, String str, String str2) {
        if (TaoLog.getLogStatus()) {
            TaoLog.e(TAG, "Receive error, code: " + i + "; desc: " + str + "; url: " + str2);
        }
        if (webView instanceof IWVWebView) {
            if (WVEventService.getInstance().onEvent(1005, (IWVWebView) webView, str2, Integer.valueOf(i), str, str2).isSuccess) {
                return;
            }
        }
        String url = webView.getUrl();
        if (url == null || url.equals(str2)) {
            HashMap hashMap = new HashMap(2);
            hashMap.put("cause", str + " [" + i + Operators.ARRAY_END_STR);
            hashMap.put("url", str2);
            ((WVWebView) webView).onMessage(402, hashMap);
            this.isError = true;
            webView.setVisibility(4);
        }
        if (WVMonitorService.getErrorMonitor() != null) {
            WVErrorMonitorInterface errorMonitor = WVMonitorService.getErrorMonitor();
            if (url == null) {
                url = str2;
            }
            errorMonitor.didOccurNativeError(url, i, str);
        }
        if (this.extraWebViewClient != null) {
            this.extraWebViewClient.onReceivedError(webView, i, str, str2);
        }
    }

    public boolean shouldOverrideUrlLoading(WebView webView, String str) {
        if (!WVUrlUtil.isCommonUrl(str) || !WVServerConfig.isBlackUrl(str)) {
            if (TaoLog.getLogStatus()) {
                TaoLog.v(TAG, "shouldOverrideUrlLoading: " + str);
            }
            if ((webView instanceof IWVWebView) && WVEventService.getInstance().onEvent(1003, (IWVWebView) webView, str, new Object[0]).isSuccess) {
                return true;
            }
            if (str.startsWith("mailto:") || str.startsWith("tel:")) {
                try {
                    Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(str));
                    intent.setFlags(268435456);
                    this.mContext.startActivity(intent);
                } catch (ActivityNotFoundException unused) {
                    TaoLog.e(TAG, "shouldOverrideUrlLoading: ActivityNotFoundException, url=" + str);
                }
                return true;
            }
            try {
                if ((webView instanceof IWVWebView) && WVURLInterceptService.getWVURLIntercepter() != null && WVURLInterceptService.getWVURLIntercepter().isOpenURLIntercept()) {
                    if (WVURLInterceptService.getWVURLIntercepter().isNeedupdateURLRule(false)) {
                        WVURLInterceptService.getWVURLIntercepter().updateURLRule();
                    }
                    if (WVURLInterceptService.getWVURLIntercepter().shouldOverrideUrlLoading(this.mContext, (IWVWebView) webView, str)) {
                        if (TaoLog.getLogStatus()) {
                            TaoLog.v(TAG, "intercept url: " + str);
                        }
                        return true;
                    }
                }
            } catch (Exception e) {
                TaoLog.e(TAG, "shouldOverrideUrlLoading: doFilter error, " + e.getMessage());
            }
            if (WVURLInterceptService.getWVABTestHandler() != null && WVUrlUtil.shouldTryABTest(str)) {
                String aBTestUrl = WVURLInterceptService.getWVABTestHandler().toABTestUrl(str);
                if (!TextUtils.isEmpty(aBTestUrl) && !aBTestUrl.equals(str)) {
                    TaoLog.i(TAG, str + " abTestUrl to : " + aBTestUrl);
                    webView.loadUrl(aBTestUrl);
                    return true;
                }
            }
            if (webView instanceof WVWebView) {
                ((WVWebView) webView).setCurrentUrl(str, "shouldOverrideUrlLoading");
            }
            TaoLog.i(TAG, "shouldOverrideUrlLoading : " + str);
            if (this.extraWebViewClient != null) {
                return this.extraWebViewClient.shouldOverrideUrlLoading(webView, str);
            }
            return false;
        }
        String forbiddenDomainRedirectURL = WVDomainConfig.getInstance().getForbiddenDomainRedirectURL();
        if (TextUtils.isEmpty(forbiddenDomainRedirectURL)) {
            HashMap hashMap = new HashMap(2);
            hashMap.put("cause", "ACCESS_FORBIDDEN");
            hashMap.put("url", str);
            ((WVWebView) webView).onMessage(402, hashMap);
        } else {
            webView.loadUrl(forbiddenDomainRedirectURL);
        }
        return true;
    }

    /* JADX WARNING: Removed duplicated region for block: B:42:0x0100 A[SYNTHETIC, Splitter:B:42:0x0100] */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x010f  */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x014e  */
    @android.annotation.TargetApi(11)
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.webkit.WebResourceResponse shouldInterceptRequest(android.webkit.WebView r8, java.lang.String r9) {
        /*
            r7 = this;
            boolean r0 = r8 instanceof android.taobao.windvane.webview.IWVWebView
            r1 = 21
            if (r0 == 0) goto L_0x00a4
            android.taobao.windvane.service.WVEventService r0 = android.taobao.windvane.service.WVEventService.getInstance()
            r2 = 1004(0x3ec, float:1.407E-42)
            r3 = r8
            android.taobao.windvane.webview.IWVWebView r3 = (android.taobao.windvane.webview.IWVWebView) r3
            r4 = 0
            java.lang.Object[] r4 = new java.lang.Object[r4]
            android.taobao.windvane.service.WVEventResult r0 = r0.onEvent(r2, r3, r9, r4)
            boolean r2 = r0.isSuccess
            if (r2 == 0) goto L_0x00a4
            java.lang.Object r2 = r0.resultObj
            if (r2 == 0) goto L_0x00a4
            java.lang.Object r2 = r0.resultObj
            boolean r2 = r2 instanceof android.taobao.windvane.webview.WVWrapWebResourceResponse
            if (r2 == 0) goto L_0x00a4
            java.lang.Object r8 = r0.resultObj
            android.taobao.windvane.webview.WVWrapWebResourceResponse r8 = (android.taobao.windvane.webview.WVWrapWebResourceResponse) r8
            boolean r0 = android.taobao.windvane.util.TaoLog.getLogStatus()
            if (r0 == 0) goto L_0x0044
            java.lang.String r0 = "WVWebViewClient"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "预加载命中 : "
            r2.append(r3)
            r2.append(r9)
            java.lang.String r2 = r2.toString()
            android.taobao.windvane.util.TaoLog.d(r0, r2)
        L_0x0044:
            android.webkit.WebResourceResponse r0 = new android.webkit.WebResourceResponse
            java.lang.String r2 = r8.mMimeType
            java.lang.String r3 = r8.mEncoding
            java.io.InputStream r4 = r8.mInputStream
            r0.<init>(r2, r3, r4)
            int r2 = android.os.Build.VERSION.SDK_INT
            if (r2 < r1) goto L_0x00a3
            java.util.Map<java.lang.String, java.lang.String> r1 = r8.mHeaders
            if (r1 == 0) goto L_0x008e
            boolean r9 = android.taobao.windvane.config.WVServerConfig.isAllowAccess(r9)     // Catch:{ Throwable -> 0x0089 }
            if (r9 == 0) goto L_0x0083
            java.util.Map<java.lang.String, java.lang.String> r9 = r8.mHeaders     // Catch:{ Throwable -> 0x0089 }
            java.lang.String r1 = "Access-Control-Allow-Origin"
            boolean r9 = r9.containsKey(r1)     // Catch:{ Throwable -> 0x0089 }
            if (r9 != 0) goto L_0x0083
            java.util.HashMap r9 = new java.util.HashMap     // Catch:{ Throwable -> 0x0089 }
            r9.<init>()     // Catch:{ Throwable -> 0x0089 }
            java.util.Map<java.lang.String, java.lang.String> r8 = r8.mHeaders     // Catch:{ Throwable -> 0x0089 }
            r9.putAll(r8)     // Catch:{ Throwable -> 0x0089 }
            java.lang.String r8 = "Access-Control-Allow-Origin"
            java.lang.String r1 = "*"
            r9.put(r8, r1)     // Catch:{ Throwable -> 0x0089 }
            r0.setResponseHeaders(r9)     // Catch:{ Throwable -> 0x0089 }
            java.lang.String r8 = "WVWebViewClient"
            java.lang.String r9 = "add cross origin header"
            android.taobao.windvane.util.TaoLog.w(r8, r9)     // Catch:{ Throwable -> 0x0089 }
            goto L_0x00a3
        L_0x0083:
            java.util.Map<java.lang.String, java.lang.String> r8 = r8.mHeaders     // Catch:{ Throwable -> 0x0089 }
            r0.setResponseHeaders(r8)     // Catch:{ Throwable -> 0x0089 }
            goto L_0x00a3
        L_0x0089:
            r8 = move-exception
            r8.printStackTrace()
            goto L_0x00a3
        L_0x008e:
            boolean r8 = android.taobao.windvane.config.WVServerConfig.isAllowAccess(r9)
            if (r8 == 0) goto L_0x00a3
            java.util.HashMap r8 = new java.util.HashMap
            r8.<init>()
            java.lang.String r9 = "Access-Control-Allow-Origin"
            java.lang.String r1 = "*"
            r8.put(r9, r1)
            r0.setResponseHeaders(r8)
        L_0x00a3:
            return r0
        L_0x00a4:
            android.taobao.windvane.cache.WVCacheManager r0 = android.taobao.windvane.cache.WVCacheManager.getInstance()
            boolean r0 = r0.isCacheEnabled(r9)
            r2 = 0
            if (r0 == 0) goto L_0x0105
            java.lang.String r0 = android.taobao.windvane.util.WVUrlUtil.removeScheme(r9)
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            android.taobao.windvane.cache.WVCacheManager r4 = android.taobao.windvane.cache.WVCacheManager.getInstance()
            r5 = 1
            java.lang.String r4 = r4.getCacheDir(r5)
            r3.append(r4)
            java.lang.String r4 = java.io.File.separator
            r3.append(r4)
            java.lang.String r0 = android.taobao.windvane.util.DigestUtils.md5ToHex((java.lang.String) r0)
            r3.append(r0)
            java.lang.String r0 = r3.toString()
            java.io.File r3 = new java.io.File     // Catch:{ Exception -> 0x00fd }
            r3.<init>(r0)     // Catch:{ Exception -> 0x00fd }
            java.io.FileInputStream r0 = new java.io.FileInputStream     // Catch:{ Exception -> 0x00fd }
            r0.<init>(r3)     // Catch:{ Exception -> 0x00fd }
            android.webkit.WebResourceResponse r3 = new android.webkit.WebResourceResponse     // Catch:{ Exception -> 0x00fb }
            java.lang.String r4 = "image/png"
            java.lang.String r5 = "UTF-8"
            r3.<init>(r4, r5, r0)     // Catch:{ Exception -> 0x00fb }
            int r4 = android.os.Build.VERSION.SDK_INT     // Catch:{ Exception -> 0x00fb }
            if (r4 < r1) goto L_0x00fa
            java.util.HashMap r1 = new java.util.HashMap     // Catch:{ Exception -> 0x00fb }
            r1.<init>()     // Catch:{ Exception -> 0x00fb }
            java.lang.String r4 = "Access-Control-Allow-Origin"
            java.lang.String r5 = "*"
            r1.put(r4, r5)     // Catch:{ Exception -> 0x00fb }
            r3.setResponseHeaders(r1)     // Catch:{ Exception -> 0x00fb }
        L_0x00fa:
            return r3
        L_0x00fb:
            goto L_0x00fe
        L_0x00fd:
            r0 = r2
        L_0x00fe:
            if (r0 == 0) goto L_0x0105
            r0.close()     // Catch:{ IOException -> 0x0104 }
            goto L_0x0105
        L_0x0104:
        L_0x0105:
            android.taobao.windvane.cache.WVMemoryCache r0 = android.taobao.windvane.cache.WVMemoryCache.getInstance()
            android.taobao.windvane.cache.WVMemoryCacheInfo r0 = r0.getMemoryCacheByUrl(r9)
            if (r0 == 0) goto L_0x014e
            long r3 = java.lang.System.currentTimeMillis()
            long r5 = r0.cachedTime
            long r3 = r3 - r5
            r5 = 2000(0x7d0, double:9.88E-321)
            int r8 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
            if (r8 >= 0) goto L_0x0130
            android.webkit.WebResourceResponse r2 = new android.webkit.WebResourceResponse
            android.taobao.windvane.util.MimeTypeEnum r8 = android.taobao.windvane.util.MimeTypeEnum.HTML
            java.lang.String r8 = r8.getMimeType()
            java.lang.String r1 = "UTF-8"
            java.io.ByteArrayInputStream r3 = new java.io.ByteArrayInputStream
            byte[] r0 = r0.mCachedDatas
            r3.<init>(r0)
            r2.<init>(r8, r1, r3)
        L_0x0130:
            android.taobao.windvane.cache.WVMemoryCache r8 = android.taobao.windvane.cache.WVMemoryCache.getInstance()
            r8.clearCacheByUrl(r9)
            java.lang.String r8 = "WVWebViewClient"
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "WVMemoryCacheInfo 命中 : "
            r0.append(r1)
            r0.append(r9)
            java.lang.String r9 = r0.toString()
            android.taobao.windvane.util.TaoLog.i(r8, r9)
            return r2
        L_0x014e:
            java.lang.String r0 = "WVWebViewClient"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "shouldInterceptRequest : "
            r1.append(r2)
            r1.append(r9)
            java.lang.String r1 = r1.toString()
            android.taobao.windvane.util.TaoLog.i(r0, r1)
            android.taobao.windvane.monitor.WVPerformanceMonitorInterface r0 = android.taobao.windvane.monitor.WVMonitorService.getPerformanceMonitor()
            if (r0 == 0) goto L_0x0176
            android.taobao.windvane.monitor.WVPerformanceMonitorInterface r1 = android.taobao.windvane.monitor.WVMonitorService.getPerformanceMonitor()
            r3 = 0
            r4 = 1
            r5 = 0
            r6 = 0
            r2 = r9
            r1.didGetResourceStatusCode(r2, r3, r4, r5, r6)
        L_0x0176:
            android.webkit.WebViewClient r0 = r7.extraWebViewClient
            if (r0 == 0) goto L_0x0181
            android.webkit.WebViewClient r0 = r7.extraWebViewClient
            android.webkit.WebResourceResponse r8 = r0.shouldInterceptRequest(r8, r9)
            return r8
        L_0x0181:
            android.webkit.WebResourceResponse r8 = super.shouldInterceptRequest(r8, r9)
            return r8
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.webview.WVWebViewClient.shouldInterceptRequest(android.webkit.WebView, java.lang.String):android.webkit.WebResourceResponse");
    }

    @SuppressLint({"NewApi"})
    public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
        String sslError2 = sslError.toString();
        if (TaoLog.getLogStatus()) {
            TaoLog.e(TAG, "onReceivedSslError  url: " + sslError.getUrl() + "errorMsg:" + sslError2);
        }
        String url = webView.getUrl();
        if (webView instanceof IWVWebView) {
            WVEventService.getInstance().onEvent(1006, (IWVWebView) webView, url, sslError2);
        }
        if (WVMonitorService.getErrorMonitor() != null) {
            WVMonitorService.getErrorMonitor().didOccurNativeError(url, 1006, sslError2);
        }
        if (this.extraWebViewClient != null) {
            this.extraWebViewClient.onReceivedSslError(webView, sslErrorHandler, sslError);
        } else {
            super.onReceivedSslError(webView, sslErrorHandler, sslError);
        }
    }
}
