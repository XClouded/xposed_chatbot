package com.alibaba.ut.comm;

import alimama.com.unwrouter.PageInfo;
import android.annotation.TargetApi;
import android.content.Context;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import com.alibaba.ut.IWebView;
import com.alibaba.ut.UT4Aplus;
import com.alibaba.ut.biz.UTAdpater;
import com.alibaba.ut.utils.Logger;
import com.alibaba.ut.webviewadapter.SystemWebView;
import com.alibaba.ut.webviewadapter.UCWebView;
import com.taobao.android.dinamicx.bindingx.DXBindingXConstant;
import com.taobao.weex.el.parse.Operators;
import org.json.JSONException;
import org.json.JSONObject;

public class JsBridge {
    private IWebView mWebView = null;

    @JavascriptInterface
    public String bridgeVersion() {
        return UT4Aplus.SDK_VERSION;
    }

    @JavascriptInterface
    public void onPageShow() {
    }

    public JsBridge(Object obj) {
        if (obj instanceof WebView) {
            this.mWebView = new SystemWebView((WebView) obj);
        } else if (obj instanceof com.uc.webview.export.WebView) {
            this.mWebView = new UCWebView((com.uc.webview.export.WebView) obj);
        }
    }

    public JsBridge(IWebView iWebView) {
        this.mWebView = iWebView;
    }

    @JavascriptInterface
    @com.uc.webview.export.JavascriptInterface
    public void CALL(String str) {
        Object obj;
        Logger.w("p", str);
        Logger.w("wmg_test", str);
        if (TextUtils.isEmpty(str)) {
            Logger.w("p", str);
            return;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            String optString = jSONObject.optString("methodName");
            String optString2 = jSONObject.optString("params");
            String optString3 = jSONObject.optString("callback");
            String optString4 = jSONObject.optString("sid");
            String str2 = "0";
            String str3 = "SUCCESS";
            try {
                obj = invokeNativeMethod(this.mWebView.getContext(), (String) null, optString, optString2);
            } catch (Exception e) {
                str2 = "1";
                str3 = e.toString();
                Logger.w((String) null, e, new Object[0]);
                obj = null;
            }
            if (!TextUtils.isEmpty(optString3)) {
                JSONObject jSONObject2 = new JSONObject();
                if (obj == null) {
                    obj = "";
                }
                jSONObject2.put("result", obj);
                jSONObject2.put("code", str2);
                jSONObject2.put("msg", str3);
                callbackToJs(optString3, new String[]{optString4, jSONObject2.toString()});
            }
        } catch (JSONException e2) {
            Logger.w((String) null, e2, new Object[0]);
        }
    }

    @JavascriptInterface
    public String UTEnv() {
        if (this.mWebView == null) {
            return "default";
        }
        if (this.mWebView instanceof SystemWebView) {
            return PageInfo.PAGE_WEBVIEW;
        }
        return this.mWebView instanceof UCWebView ? "ucwebview" : "iwebview";
    }

    public Object invokeNativeMethod(Context context, String str, String str2, String str3) throws Exception {
        if (str2.equalsIgnoreCase("pageAppear")) {
            UTAdpater.pageAppear(context, str3);
        } else if (str2.equalsIgnoreCase("pageDisAppear")) {
            UTAdpater.pageDisAppear(context, str3);
        } else if (str2.equalsIgnoreCase("updatePageProperties")) {
            UTAdpater.updatePageProperties(context, str3);
            return true;
        } else if (str2.equalsIgnoreCase("updatePageUtparam")) {
            UTAdpater.updatePageUtparam(context, str3);
        } else if (str2.equalsIgnoreCase("updateNextPageUtparam")) {
            UTAdpater.updateNextPageUtparam(str3);
        } else if (str2.equalsIgnoreCase("updateNextPageProperties")) {
            UTAdpater.updateNextPageProperties(str3);
        } else if (str2.equalsIgnoreCase("getParam")) {
            return UTAdpater.getParam();
        } else {
            if (str2.equalsIgnoreCase("getDeviceInfo")) {
                return UTAdpater.getDeviceInfo();
            }
            if (str2.equalsIgnoreCase("setAplusParams")) {
                UTAdpater.setAplusParams(context.hashCode() + "", str3);
            } else if (str2.equalsIgnoreCase("getAplusParams")) {
                return UTAdpater.getAplusParams(context.hashCode() + "");
            } else if (str2.equalsIgnoreCase("removeAplusParams")) {
                UTAdpater.removeAplusParams(context.hashCode() + "");
            } else if (str2.equalsIgnoreCase("utCustomEvent")) {
                UTAdpater.utCustomEvent(str3);
            } else if (str2.equalsIgnoreCase("getPageSpmUrl")) {
                return UTAdpater.getPageSpmUrl(context);
            } else {
                if (str2.equalsIgnoreCase("getPageSpmPre")) {
                    return UTAdpater.getPageSpmPre(context);
                }
                if (str2.equalsIgnoreCase("updatePageURL")) {
                    UTAdpater.updatePageURL(context, str3);
                } else if (str2.equalsIgnoreCase("updatePageName")) {
                    UTAdpater.updatePageName(context, str3);
                } else if (str2.equalsIgnoreCase("turnOnRealtimeDebug")) {
                    UTAdpater.turnOnRealTimeDebug(str3);
                } else if (str2.equalsIgnoreCase("userRegister")) {
                    UTAdpater.userRegister(str3);
                } else if (str2.equalsIgnoreCase("updateUserAccount")) {
                    UTAdpater.updateUserAccount(str3);
                } else if (str2.equalsIgnoreCase("addTPKItem")) {
                    UTAdpater.addTPKItem(str3);
                } else if (str2.equalsIgnoreCase("updateSessionProperties")) {
                    UTAdpater.updateSessionProperties(str3);
                } else if (str2.equalsIgnoreCase("setGlobalProperty")) {
                    UTAdpater.setGlobalProperty(str3);
                } else if (str2.equalsIgnoreCase("setAplus4UT")) {
                    UTAdpater.setAplus4UT(context);
                }
            }
        }
        return null;
    }

    private void callbackToJs(String str, String[] strArr) {
        nativeToJs(this.mWebView, str, strArr);
    }

    public static void nativeToJs(final IWebView iWebView, final String str, final String[] strArr) {
        iWebView.post(new Runnable() {
            @TargetApi(19)
            public void run() {
                String str;
                String str2;
                try {
                    if (str.contains("Aplus4UT")) {
                        str2 = JsBridge.buildAplus4UTJSScript(str, strArr);
                    } else {
                        str2 = JsBridge.buildJSScript(str, strArr);
                    }
                    try {
                        Logger.i("js:", str2);
                        iWebView.evaluateJavascript(str2, (ValueCallback<String>) null);
                    } catch (Throwable th) {
                        Throwable th2 = th;
                        str = str2;
                        th = th2;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    str = null;
                    Logger.w((String) null, th, "native to js", str);
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public static String buildJSScript(String str, String[] strArr) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(Operators.BRACKET_START_STR);
        if (strArr != null && strArr.length > 0) {
            for (int i = 0; i < strArr.length; i++) {
                sb.append(DXBindingXConstant.SINGLE_QUOTE);
                sb.append(strArr[i]);
                sb.append(DXBindingXConstant.SINGLE_QUOTE);
                if (i < strArr.length - 1) {
                    sb.append(',');
                }
            }
        }
        sb.append(");");
        return sb.toString();
    }

    /* access modifiers changed from: private */
    public static String buildAplus4UTJSScript(String str, String[] strArr) {
        StringBuilder sb = new StringBuilder();
        sb.append("if (window.Aplus4UT && window.");
        sb.append(str);
        sb.append(") { window.");
        sb.append(str);
        sb.append(Operators.BRACKET_START_STR);
        if (strArr != null && strArr.length > 0) {
            for (int i = 0; i < strArr.length; i++) {
                sb.append(DXBindingXConstant.SINGLE_QUOTE);
                sb.append(strArr[i]);
                sb.append(DXBindingXConstant.SINGLE_QUOTE);
                if (i < strArr.length - 1) {
                    sb.append(',');
                }
            }
        }
        sb.append(");}");
        return sb.toString();
    }
}
