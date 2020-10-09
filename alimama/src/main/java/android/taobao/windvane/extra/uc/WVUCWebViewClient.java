package android.taobao.windvane.extra.uc;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Handler;
import android.os.Looper;
import android.taobao.windvane.config.GlobalConfig;
import android.taobao.windvane.config.WVCommonConfig;
import android.taobao.windvane.config.WVDomainConfig;
import android.taobao.windvane.config.WVServerConfig;
import android.taobao.windvane.config.WVUCPrecacheManager;
import android.taobao.windvane.jsbridge.WVJsBridge;
import android.taobao.windvane.jspatch.WVJsPatch;
import android.taobao.windvane.monitor.AppMonitorUtil;
import android.taobao.windvane.monitor.WVErrorMonitorInterface;
import android.taobao.windvane.monitor.WVMonitorService;
import android.taobao.windvane.monitor.WVPerformanceMonitorInterface;
import android.taobao.windvane.service.WVEventService;
import android.taobao.windvane.urlintercept.WVURLInterceptService;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.util.WVUrlUtil;
import android.taobao.windvane.webview.IWVWebView;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.ValueCallback;

import com.alibaba.android.umbrella.utils.UmbrellaConstants;
import com.taobao.weaver.prefetch.WMLPrefetch;
import com.taobao.weex.el.parse.Operators;
import com.taobao.weex.utils.tools.TimeCalculator;
import com.uc.webview.export.SslErrorHandler;
import com.uc.webview.export.WebResourceRequest;
import com.uc.webview.export.WebResourceResponse;
import com.uc.webview.export.WebView;
import com.uc.webview.export.WebViewClient;
import com.uc.webview.export.extension.RenderProcessGoneDetail;
import com.uc.webview.export.extension.UCCore;
import com.uc.webview.export.extension.UCExtension;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

public class WVUCWebViewClient extends WebViewClient {
    private static final String SANDBOX_TAG = "WVUCWebViewClient.sandbox";
    public static final String SCHEME_GEO = "geo:0,0?q=";
    public static final String SCHEME_MAILTO = "mailto:";
    public static final String SCHEME_SMS = "sms:";
    public static final String SCHEME_TEL = "tel:";
    private static final String TAG = "WVUCWebViewClient";
    public int crashCount = 0;
    boolean isError;
    protected WeakReference<Context> mContext;
    private Runnable mCrashCountReseter = new Runnable() {
        public void run() {
            TaoLog.e(WVUCWebViewClient.SANDBOX_TAG, "crash count reset - " + WVUCWebViewClient.this.crashCount);
            WVUCWebViewClient.this.crashCount = 0;
        }
    };
    private Handler mRenderProcessHandler;
    private boolean useOldBridge = false;

    public WVUCWebViewClient(Context context) {
        this.mContext = new WeakReference<>(context);
    }

    public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
        this.isError = false;
        if (WVMonitorService.getPerformanceMonitor() != null) {
            WVMonitorService.getPerformanceMonitor().didPageStartLoadAtTime(str, System.currentTimeMillis());
        }
        if (webView instanceof WVUCWebView) {
            WVEventService.getInstance().onEvent(1001, (IWVWebView) webView, str, bitmap);
            WVUCWebView wVUCWebView = (WVUCWebView) webView;
            wVUCWebView.onMessage(400, (Object) null);
            wVUCWebView.mPageStart = System.currentTimeMillis();
        }
        WVJsBridge.getInstance().tryToRunTailBridges();
        TaoLog.e(TAG, "onPageStarted : " + str);
    }

    public void onPageFinished(WebView webView, String str) {
        final WebView webView2 = webView;
        final String str2 = str;
        TaoLog.i(TAG, "onPageFinished : " + str2);
        final long currentTimeMillis = System.currentTimeMillis();
        boolean z = false;
        if (!this.isError && webView.getVisibility() == 4) {
            this.isError = false;
            webView2.setVisibility(0);
        }
        super.onPageFinished(webView, str);
        if (webView2 instanceof WVUCWebView) {
            WVUCWebView wVUCWebView = (WVUCWebView) webView2;
            wVUCWebView.setCurrentUrl(str2, "onPageFinished");
            wVUCWebView.onMessage(401, (Object) null);
        }
        if (webView2 instanceof IWVWebView) {
            IWVWebView iWVWebView = (IWVWebView) webView2;
            WVEventService.getInstance().onEvent(1002, iWVWebView, str2, new Object[0]);
            WVJsPatch.getInstance().execute(iWVWebView, str2);
            iWVWebView.fireEvent("WindVaneReady", String.format("{'version':'%s'}", new Object[]{GlobalConfig.VERSION}));
        }
        if (WVMonitorService.getPerformanceMonitor() != null) {
            UCExtension uCExtension = webView.getUCExtension();
            if (uCExtension != null) {
                z = uCExtension.isLoadFromCachedPage();
            }
            WVMonitorService.getPerformanceMonitor().didGetPageStatusCode(str, -1, z ? 72 : WVUCWebView.getFromType(), (String) null, (String) null, (String) null, (Map<String, String>) null, (WVPerformanceMonitorInterface.NetStat) null);
        }
        webView2.evaluateJavascript("(function(p){if(!p||!p.timing)return;var t=p.timing,s=t.navigationStart,sc=t.secureConnectionStart,dc=t.domComplete,les=t.loadEventStart,lee=t.loadEventEnd;return JSON.stringify({dns:t.domainLookupEnd-t.domainLookupStart,c:t.connectEnd-t.connectStart,scs:sc>0?sc-s:0,req:t.requestStart-s,rps:t.responseStart-s,rpe:t.responseEnd-s,dl:t.domLoading-s,dcl:t.domContentLoadedEventEnd-s,dc:dc>0?dc-s:0,les:les>0?les-s:0,lee:lee>0?lee-s:0})})(window.performance)", new ValueCallback<String>() {
            public void onReceiveValue(String str) {
                if (WVMonitorService.getPerformanceMonitor() != null) {
                    WVMonitorService.getPerformanceMonitor().didPagePerformanceInfo(str2, str);
                    WVMonitorService.getPerformanceMonitor().didPageFinishLoadAtTime(str2, currentTimeMillis);
                }
            }
        });
        TaoLog.i(TAG, str2 + " LayerType : " + webView.getLayerType());
        if (webView.getCurrentViewCoreType() == 2) {
            webView2.evaluateJavascript("javascript:(function(f){if(f.__windvane__.call) return true; else return false})(window)", new ValueCallback<String>() {
                public void onReceiveValue(String str) {
                    TaoLog.i("WVJsBridge", "has windvane :" + str);
                    if ("false".equals(str)) {
                        webView2.loadUrl("javascript:(function(f){try{if(f.__windvane__.nativeCall){var h=f.__windvane__||(f.__windvane__={});var c=\"wvapi:\"+(Math.floor(Math.random()*(1<<16))),a=0,b={},g=function(j){if(j&&typeof j==\"string\"){try{return JSON.parse(j)}catch(i){return{ret:\"HY_RESULT_PARSE_ERROR\"}}}else{return j||{}}};h.call=function(i,m,l,e,k){if(typeof l!=\"function\"){l=null}if(typeof e!=\"function\"){e=null}var j=c+(a++);b[j]={s:l,f:e,};if(k>0){b[j].t=setTimeout(function(){h.onFailure(j,{ret:\"HY_TIMEOUT\"})},k)}if(!m){m={}}if(typeof m!=\"string\"){m=JSON.stringify(m)}f.__windvane__.nativeCall(i,m,j,location.href)};h.find=function(i,j){var e=b[i]||{};if(e.t){clearTimeout(e.t);delete e.t}if(!j){delete b[i]}return e};h.onSuccess=function(j,e,k){var i=h.find(j,k).s;if(i){i(g(e))}};h.onFailure=function(j,e){var i=h.find(j,false).f;if(i){i(g(e))}}}}catch(d){}})(window);");
                    }
                }
            });
        }
        if (WebView.getCoreType() == 1 || WebView.getCoreType() == 3) {
            TaoLog.d(TAG, "onPageFinished.  core type = " + WebView.getCoreType());
            AppMonitorUtil.commitSuccess(AppMonitorUtil.MONITOR_POINT_WEB_CORE_TYPE_BY_PV, (String) null);
            if (WVMonitorService.getWvMonitorInterface() != null) {
                WVMonitorService.getWvMonitorInterface().commitCoreTypeByPV(String.valueOf(WVCommonConfig.commonConfig.initUCCorePolicy), "U4");
            }
        } else {
            AppMonitorUtil.commitFail(AppMonitorUtil.MONITOR_POINT_WEB_CORE_TYPE_BY_PV, WebView.getCoreType(), "", "");
            if (WVMonitorService.getWvMonitorInterface() != null) {
                WVMonitorService.getWvMonitorInterface().commitCoreTypeByPV(String.valueOf(WVCommonConfig.commonConfig.initUCCorePolicy), TimeCalculator.PLATFORM_ANDROID);
            }
        }
        if (WebView.getCoreType() == 3) {
            if (webView.getContext() != null) {
                WVUCWebView.createStaticWebViewIfNeeded(webView.getContext());
            }
            tryPreCacheResources(webView);
        }
    }

    private void tryPreCacheResources(WebView webView) {
        if (WVUCPrecacheManager.canClearPrecache()) {
            WVUCPrecacheManager.resetClearConfig();
            UCCore.clearPrecacheResources((String[]) null);
            WVUCPrecacheManager.setHasPrecache(false);
        }
        if (WVUCPrecacheManager.canPrecache()) {
            WVUCPrecacheManager.resetClearConfig();
            WVUCPrecacheManager.resetPrecacheConfig();
            HashSet<String> preMemCacheUrlSet = WVUCPrecacheManager.preMemCacheUrlSet();
            if (preMemCacheUrlSet != null) {
                HashMap hashMap = new HashMap();
                Iterator<String> it = preMemCacheUrlSet.iterator();
                while (it.hasNext()) {
                    String next = it.next();
                    WebResourceResponse shouldInterceptRequest = shouldInterceptRequest(webView, new WebResourceRequest(next, new HashMap()));
                    if (shouldInterceptRequest != null) {
                        hashMap.put(next, shouldInterceptRequest);
                    }
                }
                if (hashMap.size() > 0) {
                    HashMap hashMap2 = new HashMap();
                    hashMap2.put("maxAge", "3600");
                    hashMap2.put("ignoreQuery", "1");
                    UCCore.precacheResources(hashMap, hashMap2);
                    WVUCPrecacheManager.setHasPrecache(true);
                }
            }
        }
    }

    public boolean onRenderProcessGone(final WebView webView, RenderProcessGoneDetail renderProcessGoneDetail) {
        TaoLog.e(SANDBOX_TAG, "onRenderProcessGone webview:" + webView + ", crash:" + renderProcessGoneDetail.didCrash() + ", priority:" + renderProcessGoneDetail.rendererPriorityAtExit());
        if (this.crashCount >= 5) {
            if (WVMonitorService.getWvMonitorInterface() != null) {
                WVMonitorService.getWvMonitorInterface().commitRenderType(webView.getUrl(), "R_Fail", WVCommonConfig.commonConfig.webMultiPolicy);
            }
            this.crashCount = 0;
            Log.e(SANDBOX_TAG, "onRenderProcessGone webview:" + webView.getClass().getSimpleName() + ", crash:" + renderProcessGoneDetail.didCrash(), new Throwable());
            return false;
        } else if (webView != null) {
            this.crashCount++;
            if (this.mRenderProcessHandler == null) {
                this.mRenderProcessHandler = new Handler(Looper.getMainLooper());
            }
            this.mRenderProcessHandler.postDelayed(new Runnable() {
                public void run() {
                    if (!webView.isDestroied()) {
                        webView.reload();
                    }
                }
            }, 200);
            this.mRenderProcessHandler.removeCallbacks(this.mCrashCountReseter);
            this.mRenderProcessHandler.postDelayed(this.mCrashCountReseter, UmbrellaConstants.PERFORMANCE_DATA_ALIVE);
            return true;
        } else {
            Log.e(SANDBOX_TAG, "onRenderProcessGone - WebView is null");
            return false;
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
        if (((i > -16 && i < 0) || i == -80 || i == -50) && (webView instanceof WVUCWebView) && (url == null || url.equals(str2))) {
            HashMap hashMap = new HashMap(2);
            hashMap.put("cause", str + " [" + i + Operators.ARRAY_END_STR);
            hashMap.put("url", str2);
            this.isError = true;
            webView.setVisibility(4);
            ((WVUCWebView) webView).onMessage(402, hashMap);
        }
        if (WVMonitorService.getErrorMonitor() != null) {
            WVErrorMonitorInterface errorMonitor = WVMonitorService.getErrorMonitor();
            if (url != null) {
                str2 = url;
            }
            errorMonitor.didOccurNativeError(str2, i, str);
        }
    }

    public boolean shouldOverrideUrlLoading(WebView webView, String str) {
        if (WVUrlUtil.isCommonUrl(str) && WVServerConfig.isBlackUrl(str)) {
            String forbiddenDomainRedirectURL = WVDomainConfig.getInstance().getForbiddenDomainRedirectURL();
            if (TextUtils.isEmpty(forbiddenDomainRedirectURL)) {
                HashMap hashMap = new HashMap(2);
                hashMap.put("cause", "ACCESS_FORBIDDEN");
                hashMap.put("url", str);
                ((WVUCWebView) webView).onMessage(402, hashMap);
            } else {
                webView.loadUrl(forbiddenDomainRedirectURL);
            }
            return true;
        } else if ((webView instanceof IWVWebView) && WVEventService.getInstance().onEvent(1003, (IWVWebView) webView, str, new Object[0]).isSuccess) {
            return true;
        } else {
            Context context = (Context) this.mContext.get();
            if (str.startsWith("mailto:") || str.startsWith("tel:") || str.startsWith(SCHEME_SMS)) {
                try {
                    Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(str));
                    intent.setFlags(268435456);
                    context.startActivity(intent);
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
                    if (WVURLInterceptService.getWVURLIntercepter().shouldOverrideUrlLoading(context, (IWVWebView) webView, str)) {
                        TaoLog.i(TAG, "intercept url : " + str);
                        return true;
                    }
                }
            } catch (Exception e) {
                TaoLog.e(TAG, "shouldOverrideUrlLoading: doFilter error, " + e.getMessage());
            }
            if (webView instanceof WVUCWebView) {
                UCNetworkDelegate.getInstance().onUrlChange((WVUCWebView) webView, str);
            }
            TaoLog.i(TAG, "shouldOverrideUrlLoading : " + str);
            try {
                HashMap hashMap2 = new HashMap();
                hashMap2.put("userAgent", webView.getSettings().getUserAgentString());
                WMLPrefetch.getInstance().prefetchData(str, hashMap2);
            } catch (Throwable th) {
                TaoLog.e(TAG, "failed to call prefetch: " + th.getMessage());
                th.printStackTrace();
            }
            return super.shouldOverrideUrlLoading(webView, str);
        }
    }

    public WebResourceResponse shouldInterceptRequest(WebView webView, WebResourceRequest webResourceRequest) {
        if (WebView.getCoreType() != 3) {
            TaoLog.e(TAG, "Only U4 WebView will use shouldInterceptRequest(API Level 21), return null.");
            return null;
        } else if (webResourceRequest == null || webResourceRequest.getUrl() == null) {
            TaoLog.e(TAG, "shouldInterceptRequest, invalid request.");
            return null;
        } else {
            String uri = webResourceRequest.getUrl().toString();
            if (WVUCPrecacheManager.getInstance().hasPrecacheDoc(uri)) {
                WVUCPrecacheManager.getInstance().clearPrecacheDoc(uri);
                return null;
            } else if (!(webView instanceof IWVWebView)) {
                return null;
            } else {
                return shouldInterceptRequestInternal(webView, uri, WVEventService.getInstance().onEvent(1008, (IWVWebView) webView, uri, webResourceRequest.getRequestHeaders()));
            }
        }
    }

    public WebResourceResponse shouldInterceptRequest(WebView webView, String str) {
        if (WebView.getCoreType() == 3) {
            TaoLog.e(TAG, "U4 WebView will not use shouldInterceptRequest(API Level 11), return null.");
            return null;
        } else if (webView instanceof IWVWebView) {
            return shouldInterceptRequestInternal(webView, str, WVEventService.getInstance().onEvent(1004, (IWVWebView) webView, str, new Object[0]));
        } else {
            return null;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:44:0x00fc A[SYNTHETIC, Splitter:B:44:0x00fc] */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x010b  */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x014a  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private com.uc.webview.export.WebResourceResponse shouldInterceptRequestInternal(com.uc.webview.export.WebView r7, java.lang.String r8, android.taobao.windvane.service.WVEventResult r9) {
        /*
            r6 = this;
            boolean r0 = r9.isSuccess
            if (r0 == 0) goto L_0x008f
            java.lang.Object r0 = r9.resultObj
            if (r0 == 0) goto L_0x008f
            java.lang.Object r0 = r9.resultObj
            boolean r0 = r0 instanceof android.taobao.windvane.webview.WVWrapWebResourceResponse
            if (r0 == 0) goto L_0x008f
            java.lang.Object r7 = r9.resultObj
            android.taobao.windvane.webview.WVWrapWebResourceResponse r7 = (android.taobao.windvane.webview.WVWrapWebResourceResponse) r7
            boolean r9 = android.taobao.windvane.util.TaoLog.getLogStatus()
            if (r9 == 0) goto L_0x002e
            java.lang.String r9 = "ZCache"
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "预加载命中 : "
            r0.append(r1)
            r0.append(r8)
            java.lang.String r0 = r0.toString()
            android.taobao.windvane.util.TaoLog.e(r9, r0)
        L_0x002e:
            com.uc.webview.export.WebResourceResponse r9 = new com.uc.webview.export.WebResourceResponse
            java.lang.String r0 = r7.mMimeType
            java.lang.String r1 = r7.mEncoding
            java.io.InputStream r2 = r7.mInputStream
            r9.<init>(r0, r1, r2)
            java.util.Map<java.lang.String, java.lang.String> r0 = r7.mHeaders     // Catch:{ Throwable -> 0x008a }
            if (r0 == 0) goto L_0x0074
            boolean r8 = android.taobao.windvane.config.WVServerConfig.isAllowAccess(r8)     // Catch:{ Throwable -> 0x006f }
            if (r8 == 0) goto L_0x0069
            java.util.Map<java.lang.String, java.lang.String> r8 = r7.mHeaders     // Catch:{ Throwable -> 0x006f }
            java.lang.String r0 = "Access-Control-Allow-Origin"
            boolean r8 = r8.containsKey(r0)     // Catch:{ Throwable -> 0x006f }
            if (r8 != 0) goto L_0x0069
            java.util.HashMap r8 = new java.util.HashMap     // Catch:{ Throwable -> 0x006f }
            r8.<init>()     // Catch:{ Throwable -> 0x006f }
            java.util.Map<java.lang.String, java.lang.String> r7 = r7.mHeaders     // Catch:{ Throwable -> 0x006f }
            r8.putAll(r7)     // Catch:{ Throwable -> 0x006f }
            java.lang.String r7 = "Access-Control-Allow-Origin"
            java.lang.String r0 = "*"
            r8.put(r7, r0)     // Catch:{ Throwable -> 0x006f }
            r9.setResponseHeaders(r8)     // Catch:{ Throwable -> 0x006f }
            java.lang.String r7 = "WVUCWebViewClient"
            java.lang.String r8 = "add cross origin header"
            android.taobao.windvane.util.TaoLog.w(r7, r8)     // Catch:{ Throwable -> 0x006f }
            goto L_0x008e
        L_0x0069:
            java.util.Map<java.lang.String, java.lang.String> r7 = r7.mHeaders     // Catch:{ Throwable -> 0x006f }
            r9.setResponseHeaders(r7)     // Catch:{ Throwable -> 0x006f }
            goto L_0x008e
        L_0x006f:
            r7 = move-exception
            r7.printStackTrace()     // Catch:{ Throwable -> 0x008a }
            goto L_0x008e
        L_0x0074:
            boolean r7 = android.taobao.windvane.config.WVServerConfig.isAllowAccess(r8)     // Catch:{ Throwable -> 0x008a }
            if (r7 == 0) goto L_0x008e
            java.util.HashMap r7 = new java.util.HashMap     // Catch:{ Throwable -> 0x008a }
            r7.<init>()     // Catch:{ Throwable -> 0x008a }
            java.lang.String r8 = "Access-Control-Allow-Origin"
            java.lang.String r0 = "*"
            r7.put(r8, r0)     // Catch:{ Throwable -> 0x008a }
            r9.setResponseHeaders(r7)     // Catch:{ Throwable -> 0x008a }
            goto L_0x008e
        L_0x008a:
            r7 = move-exception
            r7.printStackTrace()
        L_0x008e:
            return r9
        L_0x008f:
            android.taobao.windvane.monitor.WVPerformanceMonitorInterface r9 = android.taobao.windvane.monitor.WVMonitorService.getPerformanceMonitor()
            if (r9 == 0) goto L_0x00a4
            android.taobao.windvane.monitor.WVPerformanceMonitorInterface r0 = android.taobao.windvane.monitor.WVMonitorService.getPerformanceMonitor()
            r2 = 0
            int r3 = android.taobao.windvane.extra.uc.WVUCWebView.getFromType()
            r4 = 0
            r5 = 0
            r1 = r8
            r0.didGetResourceStatusCode(r1, r2, r3, r4, r5)
        L_0x00a4:
            android.taobao.windvane.cache.WVCacheManager r9 = android.taobao.windvane.cache.WVCacheManager.getInstance()
            boolean r9 = r9.isCacheEnabled(r8)
            r0 = 0
            if (r9 == 0) goto L_0x0101
            java.lang.String r9 = android.taobao.windvane.util.WVUrlUtil.removeScheme(r8)
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            android.taobao.windvane.cache.WVCacheManager r2 = android.taobao.windvane.cache.WVCacheManager.getInstance()
            r3 = 1
            java.lang.String r2 = r2.getCacheDir(r3)
            r1.append(r2)
            java.lang.String r2 = java.io.File.separator
            r1.append(r2)
            java.lang.String r9 = android.taobao.windvane.util.DigestUtils.md5ToHex((java.lang.String) r9)
            r1.append(r9)
            java.lang.String r9 = r1.toString()
            java.io.File r1 = new java.io.File     // Catch:{ Exception -> 0x00f9 }
            r1.<init>(r9)     // Catch:{ Exception -> 0x00f9 }
            java.io.FileInputStream r9 = new java.io.FileInputStream     // Catch:{ Exception -> 0x00f9 }
            r9.<init>(r1)     // Catch:{ Exception -> 0x00f9 }
            com.uc.webview.export.WebResourceResponse r1 = new com.uc.webview.export.WebResourceResponse     // Catch:{ Exception -> 0x00f7 }
            java.lang.String r2 = "image/png"
            java.lang.String r3 = "UTF-8"
            r1.<init>(r2, r3, r9)     // Catch:{ Exception -> 0x00f7 }
            java.util.HashMap r2 = new java.util.HashMap     // Catch:{ Exception -> 0x00f7 }
            r2.<init>()     // Catch:{ Exception -> 0x00f7 }
            java.lang.String r3 = "Access-Control-Allow-Origin"
            java.lang.String r4 = "*"
            r2.put(r3, r4)     // Catch:{ Exception -> 0x00f7 }
            r1.setResponseHeaders(r2)     // Catch:{ Exception -> 0x00f7 }
            return r1
        L_0x00f7:
            goto L_0x00fa
        L_0x00f9:
            r9 = r0
        L_0x00fa:
            if (r9 == 0) goto L_0x0101
            r9.close()     // Catch:{ IOException -> 0x0100 }
            goto L_0x0101
        L_0x0100:
        L_0x0101:
            android.taobao.windvane.cache.WVMemoryCache r9 = android.taobao.windvane.cache.WVMemoryCache.getInstance()
            android.taobao.windvane.cache.WVMemoryCacheInfo r9 = r9.getMemoryCacheByUrl(r8)
            if (r9 == 0) goto L_0x014a
            long r1 = java.lang.System.currentTimeMillis()
            long r3 = r9.cachedTime
            long r1 = r1 - r3
            r3 = 2000(0x7d0, double:9.88E-321)
            int r7 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
            if (r7 >= 0) goto L_0x012c
            com.uc.webview.export.WebResourceResponse r0 = new com.uc.webview.export.WebResourceResponse
            android.taobao.windvane.util.MimeTypeEnum r7 = android.taobao.windvane.util.MimeTypeEnum.HTML
            java.lang.String r7 = r7.getMimeType()
            java.lang.String r1 = "UTF-8"
            java.io.ByteArrayInputStream r2 = new java.io.ByteArrayInputStream
            byte[] r9 = r9.mCachedDatas
            r2.<init>(r9)
            r0.<init>(r7, r1, r2)
        L_0x012c:
            android.taobao.windvane.cache.WVMemoryCache r7 = android.taobao.windvane.cache.WVMemoryCache.getInstance()
            r7.clearCacheByUrl(r8)
            java.lang.String r7 = "WVUCWebViewClient"
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            java.lang.String r1 = "WVMemoryCacheInfo 命中 : "
            r9.append(r1)
            r9.append(r8)
            java.lang.String r8 = r9.toString()
            android.taobao.windvane.util.TaoLog.i(r7, r8)
            return r0
        L_0x014a:
            boolean r9 = android.taobao.windvane.util.TaoLog.getLogStatus()
            if (r9 == 0) goto L_0x0166
            java.lang.String r9 = "WVUCWebViewClient"
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "shouldInterceptRequest : "
            r0.append(r1)
            r0.append(r8)
            java.lang.String r0 = r0.toString()
            android.taobao.windvane.util.TaoLog.d(r9, r0)
        L_0x0166:
            com.uc.webview.export.WebResourceResponse r7 = super.shouldInterceptRequest((com.uc.webview.export.WebView) r7, (java.lang.String) r8)
            return r7
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.extra.uc.WVUCWebViewClient.shouldInterceptRequestInternal(com.uc.webview.export.WebView, java.lang.String, android.taobao.windvane.service.WVEventResult):com.uc.webview.export.WebResourceResponse");
    }

    @SuppressLint({"NewApi"})
    public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
        String sslError2 = sslError.toString();
        if (TaoLog.getLogStatus()) {
            TaoLog.e(TAG, "onReceivedSslError  url: " + sslError.getUrl() + "errorMsg:" + sslError2);
        }
        String url = webView.getUrl();
        if (webView instanceof WVUCWebView) {
            HashMap hashMap = new HashMap(2);
            hashMap.put("cause", "SSL_ERROR");
            hashMap.put("url", url);
            ((WVUCWebView) webView).onMessage(402, hashMap);
        }
        if (webView instanceof IWVWebView) {
            WVEventService.getInstance().onEvent(1006, (IWVWebView) webView, url, sslError2);
        }
        if (WVMonitorService.getErrorMonitor() != null) {
            WVMonitorService.getErrorMonitor().didOccurNativeError(url, 1006, sslError2);
        }
        super.onReceivedSslError(webView, sslErrorHandler, sslError);
    }
}
