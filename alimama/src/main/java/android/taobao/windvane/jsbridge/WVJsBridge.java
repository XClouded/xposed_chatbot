package android.taobao.windvane.jsbridge;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.taobao.windvane.monitor.WVJSBrdigeMonitorInterface;
import android.taobao.windvane.monitor.WVMonitorService;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.webview.IWVWebView;
import android.text.TextUtils;
import android.webkit.ValueCallback;

import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.el.parse.Operators;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WVJsBridge implements Handler.Callback {
    public static final int CALL_ALIAS = 7;
    public static final int CALL_DESTROY = 8;
    public static final int CALL_EXECUTE = 0;
    public static final int CALL_METHOD = 1;
    public static final int CLOSED = 4;
    public static final int ERROR_EXECUTE = 6;
    public static final int NO_CLASS = 5;
    public static final int NO_METHOD = 2;
    public static final int NO_PERMISSION = 3;
    private static final String TAG = "WVJsBridge";
    public static boolean enableGetParamByJs = true;
    private static Handler mHandler;
    private static WVJsBridge mJsBridge;
    private boolean enabled = true;
    private boolean isInit = false;
    private boolean mSkipPreprocess;
    public ArrayList<WVCallMethodContext> mTailBridges = null;

    public synchronized void tryToRunTailBridges() {
        if (this.mTailBridges != null) {
            Iterator<WVCallMethodContext> it = this.mTailBridges.iterator();
            while (it.hasNext()) {
                WVCallMethodContext next = it.next();
                aftercallMethod(next, "");
                TaoLog.i(TAG, "excute TailJSBridge : " + next.objectName + " : " + next.methodName);
            }
            this.mTailBridges.clear();
            this.mTailBridges = null;
        }
    }

    public static synchronized WVJsBridge getInstance() {
        WVJsBridge wVJsBridge;
        synchronized (WVJsBridge.class) {
            if (mJsBridge == null) {
                mJsBridge = new WVJsBridge();
            }
            wVJsBridge = mJsBridge;
        }
        return wVJsBridge;
    }

    private WVJsBridge() {
        mHandler = new Handler(Looper.getMainLooper(), this);
    }

    public synchronized void init() {
        this.isInit = true;
    }

    public void skipPreprocess() {
        this.mSkipPreprocess = true;
    }

    public void setEnabled(boolean z) {
        this.enabled = z;
    }

    public void exCallMethod(WVPluginEntryManager wVPluginEntryManager, WVCallMethodContext wVCallMethodContext, IJsApiFailedCallBack iJsApiFailedCallBack, IJsApiSucceedCallBack iJsApiSucceedCallBack) {
        if (wVCallMethodContext != null) {
            wVCallMethodContext.failedCallBack = iJsApiFailedCallBack;
            wVCallMethodContext.succeedCallBack = iJsApiSucceedCallBack;
            if (TextUtils.isEmpty(wVCallMethodContext.params)) {
                wVCallMethodContext.params = "{}";
            }
            TaoLog.i(TAG, "before call object=[" + wVCallMethodContext.objectName + "].");
            if (wVCallMethodContext.objectName != null) {
                wVCallMethodContext.classinstance = wVPluginEntryManager.getEntry(wVCallMethodContext.objectName);
                if (wVCallMethodContext.classinstance instanceof WVApiPlugin) {
                    startCall(0, wVCallMethodContext);
                } else {
                    startCall(2, wVCallMethodContext);
                }
            }
        }
    }

    public void callMethod(IWVWebView iWVWebView, String str) {
        callMethod(iWVWebView, str, (IJsApiSucceedCallBack) null, (IJsApiFailedCallBack) null);
    }

    private void callMethod(IWVWebView iWVWebView, String str, IJsApiSucceedCallBack iJsApiSucceedCallBack, IJsApiFailedCallBack iJsApiFailedCallBack) {
        boolean z;
        if (TaoLog.getLogStatus()) {
            TaoLog.d(TAG, "callMethod: url=" + str);
        }
        if (!this.isInit) {
            TaoLog.w(TAG, "jsbridge is not init.");
            return;
        }
        final WVCallMethodContext request = getRequest(str);
        if (request == null) {
            TaoLog.w(TAG, "url format error and call canceled. url=" + str);
            return;
        }
        request.webview = iWVWebView;
        if (iJsApiSucceedCallBack != null) {
            request.succeedCallBack = iJsApiSucceedCallBack;
        }
        if (iJsApiFailedCallBack != null) {
            request.failedCallBack = iJsApiFailedCallBack;
        }
        final String url = request.webview.getUrl();
        if (enableGetParamByJs) {
            try {
                JSONObject.parse(request.params);
                z = false;
            } catch (Throwable th) {
                if (WVMonitorService.getJsBridgeMonitor() != null) {
                    WVJSBrdigeMonitorInterface jsBridgeMonitor = WVMonitorService.getJsBridgeMonitor();
                    String message = th.getMessage();
                    String str2 = request.params;
                    jsBridgeMonitor.commitParamParseError(url, message, str2, request.objectName + "." + request.methodName);
                }
                z = true;
            }
            if (z) {
                iWVWebView.evaluateJavascript(String.format("javascript:window.WindVane&&window.WindVane.getParam(%s);", new Object[]{request.token}), new ValueCallback<String>() {
                    public void onReceiveValue(String str) {
                        if (str.startsWith("\"")) {
                            str = JSONObject.parse(str).toString();
                        }
                        if (!TextUtils.isEmpty(str)) {
                            request.params = str;
                        }
                        new AsyncTask<Void, Integer, Void>() {
                            /* access modifiers changed from: protected */
                            public Void doInBackground(Void... voidArr) {
                                WVJsBridge.this.callMethod(request, url);
                                return null;
                            }
                        }.execute(new Void[0]);
                    }
                });
                return;
            }
        }
        new AsyncTask<Void, Integer, Void>() {
            /* access modifiers changed from: protected */
            public Void doInBackground(Void... voidArr) {
                WVJsBridge.this.callMethod(request, url);
                return null;
            }
        }.execute(new Void[0]);
    }

    public void callMethod(WVCallMethodContext wVCallMethodContext, String str) {
        if (TaoLog.getLogStatus()) {
            TaoLog.d(TAG, "callMethod-obj:" + wVCallMethodContext.objectName + " method:" + wVCallMethodContext.methodName + " param:" + wVCallMethodContext.params + " sid:" + wVCallMethodContext.token);
        }
        if (!this.enabled || wVCallMethodContext.webview == null) {
            TaoLog.w(TAG, "jsbridge is closed.");
            startCall(4, wVCallMethodContext);
            return;
        }
        if (!this.mSkipPreprocess) {
            if (WVJsbridgeService.getPagePreprocessors() != null && !WVJsbridgeService.getPagePreprocessors().isEmpty()) {
                for (WVJSAPIPageAuth next : WVJsbridgeService.getPagePreprocessors()) {
                    if (next.needAuth(wVCallMethodContext.webview)) {
                        if (next.apiAuthCheck(str, wVCallMethodContext.objectName, wVCallMethodContext.methodName, wVCallMethodContext.params)) {
                            aftercallMethod(wVCallMethodContext, str);
                            return;
                        } else {
                            startCall(3, wVCallMethodContext);
                            return;
                        }
                    }
                }
            }
            if (WVJsbridgeService.getJSBridgePreprocessors() != null && !WVJsbridgeService.getJSBridgePreprocessors().isEmpty()) {
                for (WVJSAPIAuthCheck apiAuthCheck : WVJsbridgeService.getJSBridgePreprocessors()) {
                    if (!apiAuthCheck.apiAuthCheck(str, wVCallMethodContext.objectName, wVCallMethodContext.methodName, wVCallMethodContext.params)) {
                        TaoLog.w(TAG, "preprocessor call fail, callMethod cancel.");
                        startCall(3, wVCallMethodContext);
                        return;
                    }
                }
            }
            if (WVJsbridgeService.getJSBridgeayncPreprocessors() != null && !WVJsbridgeService.getJSBridgeayncPreprocessors().isEmpty()) {
                for (WVAsyncAuthCheck AsyncapiAuthCheck : WVJsbridgeService.getJSBridgeayncPreprocessors()) {
                    if (AsyncapiAuthCheck.AsyncapiAuthCheck(str, wVCallMethodContext, new WVAsyncAuthCheckCallBackforJsBridge())) {
                        TaoLog.w(TAG, "enter  WVAsyncAuthCheck preprocessor  ");
                        return;
                    }
                }
            }
        }
        aftercallMethod(wVCallMethodContext, str);
    }

    public static void aftercallMethod(WVCallMethodContext wVCallMethodContext, String str) {
        Map<String, String> originalPlugin = WVPluginManager.getOriginalPlugin(wVCallMethodContext.objectName, wVCallMethodContext.methodName);
        if (originalPlugin != null) {
            if (TaoLog.getLogStatus()) {
                TaoLog.i(TAG, "call method through alias name. newObject: " + originalPlugin.get("name") + " newMethod: " + originalPlugin.get("method"));
            }
            wVCallMethodContext.objectName = originalPlugin.get("name");
            wVCallMethodContext.methodName = originalPlugin.get("method");
            startCall(7, wVCallMethodContext);
        }
        Object jsObject = wVCallMethodContext.webview.getJsObject(wVCallMethodContext.objectName);
        if (jsObject == null) {
            TaoLog.w(TAG, "callMethod: Plugin " + wVCallMethodContext.objectName + " didn't found, you should call WVPluginManager.registerPlugin first.");
            startCall(5, wVCallMethodContext);
        } else if (jsObject instanceof WVApiPlugin) {
            wVCallMethodContext.classinstance = jsObject;
            startCall(0, wVCallMethodContext);
        } else if (jsObject instanceof String) {
            TaoLog.e(TAG, "cannot call method for context is null");
            startCall(8, wVCallMethodContext);
        } else {
            try {
                if (wVCallMethodContext.methodName != null) {
                    Method method = jsObject.getClass().getMethod(wVCallMethodContext.methodName, new Class[]{Object.class, String.class});
                    if (method.isAnnotationPresent(WindVaneInterface.class)) {
                        wVCallMethodContext.classinstance = jsObject;
                        wVCallMethodContext.method = method;
                        startCall(1, wVCallMethodContext);
                        return;
                    }
                    TaoLog.w(TAG, "callMethod: Method " + wVCallMethodContext.methodName + " didn't has @WindVaneInterface annotation, obj=" + wVCallMethodContext.objectName);
                }
            } catch (NoSuchMethodException unused) {
                TaoLog.e(TAG, "callMethod: Method " + wVCallMethodContext.methodName + " didn't found. It must has two parameter, Object.class and String.class, obj=" + wVCallMethodContext.objectName);
            }
        }
    }

    public static void startCall(int i, WVCallMethodContext wVCallMethodContext) {
        Message obtain = Message.obtain();
        obtain.what = i;
        obtain.obj = wVCallMethodContext;
        mHandler.sendMessage(obtain);
    }

    public boolean handleMessage(Message message) {
        WVCallMethodContext wVCallMethodContext = (WVCallMethodContext) message.obj;
        if (wVCallMethodContext == null) {
            TaoLog.e(TAG, "CallMethodContext is null, and do nothing.");
            return false;
        }
        WVCallBackContext wVCallBackContext = new WVCallBackContext(wVCallMethodContext.webview, wVCallMethodContext.token, wVCallMethodContext.objectName, wVCallMethodContext.methodName, wVCallMethodContext.succeedCallBack, wVCallMethodContext.failedCallBack);
        if (wVCallMethodContext.classinstance != null) {
            wVCallBackContext.setInstancename(wVCallMethodContext.classinstance.getClass().getName());
        }
        switch (message.what) {
            case 0:
                Object obj = wVCallMethodContext.classinstance;
                StringBuilder sb = new StringBuilder();
                sb.append("call method=[");
                sb.append(wVCallMethodContext.objectName);
                sb.append(".");
                sb.append(wVCallMethodContext.methodName);
                sb.append("], object=[");
                sb.append(obj == null ? null : obj.getClass().getSimpleName());
                sb.append("].");
                TaoLog.i(TAG, sb.toString());
                if (!((WVApiPlugin) obj).executeSafe(wVCallMethodContext.methodName, TextUtils.isEmpty(wVCallMethodContext.params) ? "{}" : wVCallMethodContext.params, wVCallBackContext)) {
                    if (TaoLog.getLogStatus()) {
                        TaoLog.w(TAG, "WVApiPlugin execute failed.object:" + wVCallMethodContext.objectName + ", method: " + wVCallMethodContext.methodName);
                    }
                    startCall(6, wVCallMethodContext);
                } else {
                    try {
                        IWVWebView iWVWebView = wVCallMethodContext.webview;
                        ConcurrentHashMap<String, Integer> concurrentHashMap = IWVWebView.JsbridgeHis;
                        int i = 1;
                        String str = wVCallMethodContext.objectName + "." + wVCallMethodContext.methodName;
                        if (concurrentHashMap.containsKey(str)) {
                            i = Integer.valueOf(((Integer) concurrentHashMap.get(str)).intValue() + 1);
                        }
                        IWVWebView iWVWebView2 = wVCallMethodContext.webview;
                        IWVWebView.JsbridgeHis.put(str, i);
                    } catch (Exception unused) {
                    }
                }
                return true;
            case 1:
                Object obj2 = wVCallMethodContext.classinstance;
                try {
                    Method method = wVCallMethodContext.method;
                    Object[] objArr = new Object[2];
                    objArr[0] = wVCallBackContext;
                    objArr[1] = TextUtils.isEmpty(wVCallMethodContext.params) ? "{}" : wVCallMethodContext.params;
                    method.invoke(obj2, objArr);
                } catch (Exception e) {
                    TaoLog.e(TAG, "call method " + wVCallMethodContext.method + " exception. " + e.getMessage());
                }
                return true;
            case 2:
                WVResult wVResult = new WVResult();
                wVResult.setResult(WVResult.NO_METHOD);
                StringBuilder sb2 = new StringBuilder();
                sb2.append("No Method Error: method=[");
                sb2.append(wVCallMethodContext.objectName);
                sb2.append(".");
                sb2.append(wVCallMethodContext.methodName);
                sb2.append(Operators.ARRAY_END_STR);
                sb2.append(",url=[");
                sb2.append(wVCallBackContext.getWebview() == null ? "" : wVCallBackContext.getWebview().getUrl());
                sb2.append(Operators.ARRAY_END_STR);
                wVResult.addData("msg", sb2.toString());
                wVCallBackContext.error(wVResult);
                return true;
            case 3:
                WVResult wVResult2 = new WVResult();
                wVResult2.setResult(WVResult.NO_PERMISSION);
                StringBuilder sb3 = new StringBuilder();
                sb3.append("method=[");
                sb3.append(wVCallMethodContext.objectName);
                sb3.append(".");
                sb3.append(wVCallMethodContext.methodName);
                sb3.append(Operators.ARRAY_END_STR);
                sb3.append(",url=[");
                sb3.append(wVCallBackContext.getWebview() == null ? "" : wVCallBackContext.getWebview().getUrl());
                sb3.append(Operators.ARRAY_END_STR);
                wVResult2.addData("msg", sb3.toString());
                wVCallBackContext.error(wVResult2);
                return true;
            case 4:
                WVResult wVResult3 = new WVResult();
                wVResult3.setResult(WVResult.CLOSED);
                StringBuilder sb4 = new StringBuilder();
                sb4.append("method=[");
                sb4.append(wVCallMethodContext.objectName);
                sb4.append(".");
                sb4.append(wVCallMethodContext.methodName);
                sb4.append(Operators.ARRAY_END_STR);
                sb4.append(",url=[");
                sb4.append(wVCallBackContext.getWebview() == null ? "" : wVCallBackContext.getWebview().getUrl());
                sb4.append(Operators.ARRAY_END_STR);
                wVResult3.addData("msg", sb4.toString());
                wVCallBackContext.error(wVResult3);
                return true;
            case 5:
                WVResult wVResult4 = new WVResult();
                wVResult4.setResult(WVResult.NO_METHOD);
                StringBuilder sb5 = new StringBuilder();
                sb5.append("No Class Error: method=[");
                sb5.append(wVCallMethodContext.objectName);
                sb5.append(".");
                sb5.append(wVCallMethodContext.methodName);
                sb5.append(Operators.ARRAY_END_STR);
                sb5.append(",url=[");
                sb5.append(wVCallBackContext.getWebview() == null ? "" : wVCallBackContext.getWebview().getUrl());
                sb5.append(Operators.ARRAY_END_STR);
                wVResult4.addData("msg", sb5.toString());
                wVCallBackContext.error(wVResult4);
                return true;
            case 6:
                WVResult wVResult5 = new WVResult();
                wVResult5.setResult(WVResult.NO_METHOD);
                StringBuilder sb6 = new StringBuilder();
                sb6.append("Execute error:method=[");
                sb6.append(wVCallMethodContext.objectName);
                sb6.append(".");
                sb6.append(wVCallMethodContext.methodName);
                sb6.append(Operators.ARRAY_END_STR);
                sb6.append(",url=[");
                sb6.append(wVCallBackContext.getWebview() == null ? "" : wVCallBackContext.getWebview().getUrl());
                sb6.append(Operators.ARRAY_END_STR);
                wVResult5.addData("msg", sb6.toString());
                wVCallBackContext.error(wVResult5);
                return true;
            case 7:
                WVResult wVResult6 = new WVResult();
                wVResult6.setResult("CALL_ALIAS");
                wVResult6.addData("msg", wVCallBackContext.getWebview() == null ? "" : wVCallBackContext.getWebview().getUrl());
                wVResult6.setSuccess();
                wVCallBackContext.commitJsBridgeReturn(wVResult6);
                return true;
            case 8:
                WVResult wVResult7 = new WVResult();
                wVResult7.setResult("HY_FAILED");
                StringBuilder sb7 = new StringBuilder();
                sb7.append("Null Context Error:");
                sb7.append(wVCallBackContext.getWebview() == null ? "" : wVCallBackContext.getWebview().getUrl());
                wVResult7.addData("msg", sb7.toString());
                wVCallBackContext.error(wVResult7);
                return true;
            default:
                return false;
        }
    }

    private WVCallMethodContext getRequest(String str) {
        if (str == null || !str.startsWith("hybrid://")) {
            return null;
        }
        try {
            WVCallMethodContext wVCallMethodContext = new WVCallMethodContext();
            int indexOf = str.indexOf(58, 9);
            wVCallMethodContext.objectName = str.substring(9, indexOf);
            int indexOf2 = str.indexOf(47, indexOf);
            wVCallMethodContext.token = str.substring(indexOf + 1, indexOf2);
            int indexOf3 = str.indexOf(63, indexOf2);
            if (indexOf3 > 0) {
                wVCallMethodContext.methodName = str.substring(indexOf2 + 1, indexOf3);
                wVCallMethodContext.params = str.substring(indexOf3 + 1);
            } else {
                wVCallMethodContext.methodName = str.substring(indexOf2 + 1);
            }
            if (wVCallMethodContext.objectName.length() <= 0 || wVCallMethodContext.token.length() <= 0 || wVCallMethodContext.methodName.length() <= 0) {
                return null;
            }
            return wVCallMethodContext;
        } catch (StringIndexOutOfBoundsException unused) {
        }
    }

    public void destroy() {
        this.isInit = false;
    }
}
