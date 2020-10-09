package android.taobao.windvane.jsbridge;

import android.os.Looper;
import android.taobao.windvane.config.GlobalConfig;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.webview.IWVWebView;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;

import com.taobao.android.dinamicx.bindingx.DXBindingXConstant;
import com.taobao.weex.el.parse.Operators;

import java.io.Serializable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WVBridgeEngine implements Serializable {
    private static final String FAILURE_CALLBACK_PREFIX = "javascript:window.__windvane__&&window.__windvane__.onFailure&&window.__windvane__.onFailure";
    private static final String SUCCESS_CALLBACK_PREFIX = "javascript:window.__windvane__&&window.__windvane__.onSuccess&&window.__windvane__.onSuccess";
    public static final String WINDVANE_CORE_JS = "(function(f){try{if(f.__windvane__.nativeCall){var h=f.__windvane__||(f.__windvane__={});var c=\"wvapi:\"+(Math.floor(Math.random()*(1<<16))),a=0,b={},g=function(j){if(j&&typeof j==\"string\"){try{return JSON.parse(j)}catch(i){return{ret:\"HY_RESULT_PARSE_ERROR\"}}}else{return j||{}}};h.call=function(i,m,l,e,k){if(typeof l!=\"function\"){l=null}if(typeof e!=\"function\"){e=null}var j=c+(a++);b[j]={s:l,f:e,};if(k>0){b[j].t=setTimeout(function(){h.onFailure(j,{ret:\"HY_TIMEOUT\"})},k)}if(!m){m={}}if(typeof m!=\"string\"){m=JSON.stringify(m)}f.__windvane__.nativeCall(i,m,j,location.href)};h.find=function(i,j){var e=b[i]||{};if(e.t){clearTimeout(e.t);delete e.t}if(!j){delete b[i]}return e};h.onSuccess=function(j,e,k){var i=h.find(j,k).s;if(i){i(g(e))}};h.onFailure=function(j,e){var i=h.find(j,false).f;if(i){i(g(e))}}}}catch(d){}})(window);";
    private static final ExecutorService sExecutors = Executors.newFixedThreadPool(5);
    /* access modifiers changed from: private */
    public IWVWebView mWebview = null;

    @JavascriptInterface
    @com.uc.webview.export.JavascriptInterface
    public String windVaneCoreJs() {
        return WINDVANE_CORE_JS;
    }

    public WVBridgeEngine(IWVWebView iWVWebView) {
        this.mWebview = iWVWebView;
    }

    @JavascriptInterface
    @com.uc.webview.export.JavascriptInterface
    public void nativeCall(String str, String str2, final String str3, final String str4) {
        final WVCallMethodContext wVCallMethodContext = new WVCallMethodContext();
        String[] split = str.split("\\.");
        if (split == null || split.length != 2) {
            WVResult wVResult = new WVResult();
            wVResult.setResult(WVResult.NO_METHOD);
            new WVCallBackContext(this.mWebview, str3, "", "", (IJsApiSucceedCallBack) null, (IJsApiFailedCallBack) null).error(wVResult);
            return;
        }
        wVCallMethodContext.objectName = split[0];
        wVCallMethodContext.methodName = split[1];
        wVCallMethodContext.webview = this.mWebview;
        wVCallMethodContext.token = str3;
        wVCallMethodContext.params = str2;
        if (TextUtils.isEmpty(wVCallMethodContext.params)) {
            wVCallMethodContext.params = "{}";
        }
        TaoLog.i("WVJsBridge", "new bridge, reqId=[" + str3 + Operators.ARRAY_END_STR);
        wVCallMethodContext.succeedCallBack = new IExtJsApiSuccessCallBack() {
            public void successAndKeepAlive(String str) {
                if (TextUtils.isEmpty(str)) {
                    str = "{}";
                }
                final String str2 = "javascript:window.__windvane__&&window.__windvane__.onSuccess&&window.__windvane__.onSuccess('" + str3 + "','" + WVBridgeEngine.this.formatJsonString(str) + "', true);";
                WVBridgeEngine.this.runOnUiThread(new Runnable() {
                    public void run() {
                        WVBridgeEngine.this.mWebview.evaluateJavascript(str2);
                    }
                });
            }

            public void succeed(String str) {
                if (TextUtils.isEmpty(str)) {
                    str = "{}";
                }
                final String access$300 = WVBridgeEngine.this.getCallbackJs(true, str3, WVBridgeEngine.this.formatJsonString(str));
                WVBridgeEngine.this.runOnUiThread(new Runnable() {
                    public void run() {
                        WVBridgeEngine.this.mWebview.evaluateJavascript(access$300);
                    }
                });
            }
        };
        wVCallMethodContext.failedCallBack = new IJsApiFailedCallBack() {
            public void fail(String str) {
                if (TextUtils.isEmpty(str)) {
                    str = "{}";
                }
                final String access$300 = WVBridgeEngine.this.getCallbackJs(false, str3, WVBridgeEngine.this.formatJsonString(str));
                WVBridgeEngine.this.runOnUiThread(new Runnable() {
                    public void run() {
                        WVBridgeEngine.this.mWebview.evaluateJavascript(access$300);
                    }
                });
            }
        };
        sExecutors.submit(new Runnable() {
            public void run() {
                WVJsBridge.getInstance().callMethod(wVCallMethodContext, str4);
            }
        });
    }

    @JavascriptInterface
    @com.uc.webview.export.JavascriptInterface
    public String version() {
        TaoLog.e("WVJSPlugin", "WVJSPlugin __windvane__ version 8.5.0");
        return GlobalConfig.VERSION;
    }

    /* access modifiers changed from: private */
    public void runOnUiThread(Runnable runnable) {
        try {
            if (Looper.getMainLooper() == Looper.myLooper()) {
                runnable.run();
            } else if (this.mWebview != null) {
                this.mWebview.getView().post(runnable);
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    /* access modifiers changed from: private */
    public String getCallbackJs(boolean z, String str, String str2) {
        if (z) {
            return "javascript:window.__windvane__&&window.__windvane__.onSuccess&&window.__windvane__.onSuccess('" + str + "','" + str2 + "');";
        }
        return "javascript:window.__windvane__&&window.__windvane__.onFailure&&window.__windvane__.onFailure('" + str + "','" + str2 + "');";
    }

    /* access modifiers changed from: private */
    public String formatJsonString(String str) {
        if (str.contains(" ")) {
            try {
                str = str.replace(" ", "\\u2028");
            } catch (Exception unused) {
            }
        }
        if (str.contains(" ")) {
            try {
                str = str.replace(" ", "\\u2029");
            } catch (Exception unused2) {
            }
        }
        return str.replace("\\", "\\\\").replace(DXBindingXConstant.SINGLE_QUOTE, "\\'");
    }
}
