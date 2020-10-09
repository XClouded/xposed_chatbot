package android.taobao.windvane.jsbridge;

import android.os.Looper;
import android.taobao.windvane.monitor.WVMonitorService;
import android.taobao.windvane.service.WVEventService;
import android.taobao.windvane.util.EnvUtil;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.webview.IWVWebView;
import android.text.TextUtils;

import com.taobao.android.dinamicx.bindingx.DXBindingXConstant;

import org.json.JSONException;
import org.json.JSONObject;

public class WVCallBackContext {
    private static final String TAG = "WVCallBackContext";
    private IJsApiFailedCallBack failedCallBack;
    private String instancename;
    private String mAction = null;
    private boolean mNotiNavtive = false;
    private String methodname;
    private String objectname;
    private IJsApiSucceedCallBack succeedCallBack;
    private String token;
    private IWVWebView webview;

    public WVCallBackContext(IWVWebView iWVWebView) {
        this.webview = iWVWebView;
    }

    public WVCallBackContext(IWVWebView iWVWebView, String str, String str2, String str3) {
        this.webview = iWVWebView;
        this.token = str;
        this.objectname = str2;
        this.methodname = str3;
    }

    public WVCallBackContext(IWVWebView iWVWebView, String str, String str2, String str3, IJsApiSucceedCallBack iJsApiSucceedCallBack, IJsApiFailedCallBack iJsApiFailedCallBack) {
        this.webview = iWVWebView;
        this.token = str;
        this.objectname = str2;
        this.methodname = str3;
        this.failedCallBack = iJsApiFailedCallBack;
        this.succeedCallBack = iJsApiSucceedCallBack;
    }

    public void setInstancename(String str) {
        this.instancename = str;
    }

    public IWVWebView getWebview() {
        return this.webview;
    }

    public void setWebview(IWVWebView iWVWebView) {
        this.webview = iWVWebView;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String str) {
        this.token = str;
    }

    public void fireEvent(String str, String str2) {
        TaoLog.d(TAG, "call fireEvent ");
        WVEventService.getInstance().onEvent(3013, this.mAction, str, str2);
        callback(this.webview, String.format("window.WindVane && window.WindVane.fireEvent('%s', '%%s', %s);", new Object[]{str, null}), str2);
    }

    public static void fireEvent(IWVWebView iWVWebView, String str, String str2) {
        TaoLog.d(TAG, "call fireEvent ");
        WVEventService.getInstance().onEvent(3013, (IWVWebView) null, str, str2);
        callback(iWVWebView, String.format("window.WindVane && window.WindVane.fireEvent('%s', '%%s', %s);", new Object[]{str, null}), str2);
    }

    public void fireEvent(String str) {
        fireEvent(str, "{}");
    }

    public void success() {
        success(WVResult.RET_SUCCESS);
    }

    public void success(WVResult wVResult) {
        if (wVResult != null) {
            wVResult.setSuccess();
            success(wVResult.toJsonString());
            commitJsBridgeReturn(wVResult);
        }
    }

    public void success(String str) {
        TaoLog.d(TAG, "call success ");
        if (this.succeedCallBack != null) {
            this.succeedCallBack.succeed(str);
            return;
        }
        if (this.mNotiNavtive) {
            WVEventService.getInstance().onEvent(3011, (IWVWebView) null, this.webview.getUrl(), this.mAction, str);
            this.mNotiNavtive = false;
            this.mAction = null;
        }
        callback(this.webview, String.format("javascript:window.WindVane&&window.WindVane.onSuccess(%s,'%%s');", new Object[]{this.token}), str);
    }

    public void successAndKeepAlive(String str) {
        TaoLog.d(TAG, "call success and keep alive");
        if (this.succeedCallBack == null || !(this.succeedCallBack instanceof IExtJsApiSuccessCallBack)) {
            callback(this.webview, String.format("javascript:window.WindVane&&window.WindVane.onSuccess('%s','%%s', true);", new Object[]{this.token}), str);
            return;
        }
        ((IExtJsApiSuccessCallBack) this.succeedCallBack).successAndKeepAlive(str);
    }

    public void error() {
        error("{}");
    }

    public void error(WVResult wVResult) {
        if (wVResult != null) {
            error(wVResult.toJsonString());
            commitJsBridgeReturn(wVResult);
        }
    }

    public void error(String str) {
        TaoLog.d(TAG, "call error ");
        if (this.failedCallBack != null) {
            this.failedCallBack.fail(str);
            return;
        }
        if (this.mNotiNavtive) {
            WVEventService.getInstance().onEvent(3012, (IWVWebView) null, this.webview.getUrl(), this.mAction, str);
            this.mNotiNavtive = false;
            this.mAction = null;
        }
        callback(this.webview, String.format("javascript:window.WindVane&&window.WindVane.onFailure(%s,'%%s');", new Object[]{this.token}), str);
    }

    private static void runOnUiThread(IWVWebView iWVWebView, Runnable runnable) {
        if (iWVWebView != null && iWVWebView.getView() != null) {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                runnable.run();
                return;
            }
            try {
                iWVWebView._post(runnable);
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    private static void callback(final IWVWebView iWVWebView, String str, String str2) {
        if (TaoLog.getLogStatus() && EnvUtil.isDebug() && !TextUtils.isEmpty(str2)) {
            try {
                new JSONObject(str2);
            } catch (JSONException unused) {
                TaoLog.e(TAG, "return param is not a valid json!\n" + str + "\n" + str2);
            }
        }
        if (TextUtils.isEmpty(str2)) {
            str2 = "{}";
        }
        try {
            final String format = String.format(str, new Object[]{formatJsonString(str2)});
            try {
                runOnUiThread(iWVWebView, new Runnable() {
                    public void run() {
                        iWVWebView.evaluateJavascript(format);
                    }
                });
            } catch (Exception e) {
                TaoLog.w(TAG, e.getMessage());
            }
        } catch (Exception e2) {
            TaoLog.e(TAG, "callback error. " + e2.getMessage());
        }
    }

    public void setNeedfireNativeEvent(String str, boolean z) {
        this.mAction = str;
        this.mNotiNavtive = z;
        TaoLog.e(TAG, "setNeedfireNativeEvent : " + str);
    }

    private static String formatJsonString(String str) {
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

    public void commitJsBridgeReturn(WVResult wVResult) {
        try {
            WVMonitorService.getJsBridgeMonitor().onJsBridgeReturn("" + this.objectname + "." + this.methodname, this.instancename, wVResult.get("ret", "HY_FAILED_EMPTY"), wVResult.get("msg", ""), this.webview == null ? "unknown" : this.webview.getUrl());
        } catch (Throwable unused) {
        }
    }
}
