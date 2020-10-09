package android.taobao.windvane.jsbridge.api;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.taobao.windvane.config.EnvEnum;
import android.taobao.windvane.config.GlobalConfig;
import android.taobao.windvane.jsbridge.IJsApiFailedCallBack;
import android.taobao.windvane.jsbridge.IJsApiSucceedCallBack;
import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.jsbridge.WVCallMethodContext;
import android.taobao.windvane.jsbridge.WVJsBridge;
import android.taobao.windvane.jsbridge.WVResult;
import android.taobao.windvane.util.CommonUtils;
import android.taobao.windvane.util.EnvUtil;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.webview.WVWebView;
import android.text.TextUtils;

import com.vivo.push.PushClientConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import anetwork.channel.util.RequestConstant;

public class WVBase extends WVApiPlugin {
    public boolean execute(String str, String str2, WVCallBackContext wVCallBackContext) {
        if ("isWindVaneSDK".equals(str)) {
            isWindVaneSDK(wVCallBackContext, str2);
            return true;
        } else if ("plusUT".equals(str)) {
            plusUT(wVCallBackContext, str2);
            return true;
        } else if ("commitUTEvent".equals(str)) {
            commitUTEvent(wVCallBackContext, str2);
            return true;
        } else if ("isInstall".equals(str)) {
            isInstall(wVCallBackContext, str2);
            return true;
        } else if ("isAppsInstalled".equals(str)) {
            isAppsInstalled(wVCallBackContext, str2);
            return true;
        } else if ("copyToClipboard".equals(str)) {
            copyToClipboard(wVCallBackContext, str2);
            return true;
        } else if (!"addTailJSBridge".equals(str)) {
            return false;
        } else {
            addTailJSBridge(wVCallBackContext, str2);
            return true;
        }
    }

    public void addTailJSBridge(WVCallBackContext wVCallBackContext, String str) {
        try {
            JSONObject jSONObject = new JSONObject(str);
            String string = jSONObject.getString(PushClientConstants.TAG_CLASS_NAME);
            String string2 = jSONObject.getString("handlerName");
            String string3 = jSONObject.getString("params");
            WVCallMethodContext wVCallMethodContext = new WVCallMethodContext();
            wVCallMethodContext.objectName = string;
            wVCallMethodContext.methodName = string2;
            wVCallMethodContext.params = string3;
            wVCallMethodContext.webview = this.mWebView;
            wVCallMethodContext.succeedCallBack = new IJsApiSucceedCallBack() {
                public void succeed(String str) {
                }
            };
            wVCallMethodContext.failedCallBack = new IJsApiFailedCallBack() {
                public void fail(String str) {
                }
            };
            if (WVJsBridge.getInstance().mTailBridges == null) {
                WVJsBridge.getInstance().mTailBridges = new ArrayList<>();
            }
            WVJsBridge.getInstance().mTailBridges.add(wVCallMethodContext);
            TaoLog.i(WVAPI.PluginName.API_BASE, "addTailJSBridge : " + str);
        } catch (Exception unused) {
        }
    }

    public void isWindVaneSDK(WVCallBackContext wVCallBackContext, String str) {
        WVResult wVResult = new WVResult();
        wVResult.addData("os", "android");
        wVResult.addData("version", GlobalConfig.VERSION);
        wVResult.addData("debug", (Object) Boolean.valueOf(EnvUtil.isAppDebug()));
        if (TaoLog.getLogStatus()) {
            TaoLog.d(WVAPI.PluginName.API_BASE, "isWindVaneSDK: version=8.5.0");
        }
        String str2 = "release";
        if (EnvEnum.DAILY.equals(GlobalConfig.env)) {
            str2 = "daily";
        } else if (EnvEnum.PRE.equals(GlobalConfig.env)) {
            str2 = RequestConstant.ENV_PRE;
        }
        wVResult.addData("env", str2);
        String str3 = "WVUCWebView";
        if (this.mWebView instanceof WVWebView) {
            str3 = "WVWebView";
        }
        wVResult.addData("container", str3);
        wVCallBackContext.success(wVResult);
    }

    /* JADX WARNING: Removed duplicated region for block: B:24:0x007a  */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x009a  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void plusUT(android.taobao.windvane.jsbridge.WVCallBackContext r17, java.lang.String r18) {
        /*
            r16 = this;
            r0 = r17
            r1 = r18
            r2 = 1
            r3 = 0
            org.json.JSONObject r4 = new org.json.JSONObject     // Catch:{ JSONException -> 0x0072 }
            r4.<init>(r1)     // Catch:{ JSONException -> 0x0072 }
            java.lang.String r5 = "eid"
            int r5 = r4.getInt(r5)     // Catch:{ JSONException -> 0x0072 }
            java.lang.String r6 = "a1"
            java.lang.String r6 = r4.getString(r6)     // Catch:{ JSONException -> 0x0072 }
            java.lang.String r7 = "a2"
            java.lang.String r7 = r4.getString(r7)     // Catch:{ JSONException -> 0x0072 }
            java.lang.String r8 = "a3"
            java.lang.String r8 = r4.getString(r8)     // Catch:{ JSONException -> 0x0072 }
            java.lang.String[] r9 = new java.lang.String[r3]     // Catch:{ JSONException -> 0x0072 }
            java.lang.String r10 = "args"
            boolean r10 = r4.has(r10)     // Catch:{ JSONException -> 0x0072 }
            if (r10 == 0) goto L_0x0062
            java.lang.String r10 = "args"
            org.json.JSONObject r4 = r4.getJSONObject(r10)     // Catch:{ JSONException -> 0x0072 }
            if (r4 == 0) goto L_0x0062
            int r9 = r4.length()     // Catch:{ JSONException -> 0x0072 }
            java.lang.String[] r9 = new java.lang.String[r9]     // Catch:{ JSONException -> 0x0072 }
            java.util.Iterator r10 = r4.keys()     // Catch:{ JSONException -> 0x0072 }
            r11 = 0
        L_0x0040:
            boolean r12 = r10.hasNext()     // Catch:{ JSONException -> 0x0072 }
            if (r12 == 0) goto L_0x0062
            java.lang.Object r12 = r10.next()     // Catch:{ JSONException -> 0x0072 }
            java.lang.String r12 = (java.lang.String) r12     // Catch:{ JSONException -> 0x0072 }
            java.lang.String r13 = r4.getString(r12)     // Catch:{ JSONException -> 0x0072 }
            java.lang.String r14 = "%s=%s"
            r15 = 2
            java.lang.Object[] r15 = new java.lang.Object[r15]     // Catch:{ JSONException -> 0x0072 }
            r15[r3] = r12     // Catch:{ JSONException -> 0x0072 }
            r15[r2] = r13     // Catch:{ JSONException -> 0x0072 }
            java.lang.String r12 = java.lang.String.format(r14, r15)     // Catch:{ JSONException -> 0x0072 }
            r9[r11] = r12     // Catch:{ JSONException -> 0x0072 }
            int r11 = r11 + 1
            goto L_0x0040
        L_0x0062:
            r4 = 9100(0x238c, float:1.2752E-41)
            if (r5 < r4) goto L_0x006a
            r4 = 9200(0x23f0, float:1.2892E-41)
            if (r5 < r4) goto L_0x006e
        L_0x006a:
            r4 = 19999(0x4e1f, float:2.8025E-41)
            if (r5 != r4) goto L_0x0072
        L_0x006e:
            android.taobao.windvane.monitor.UserTrackUtil.commitEvent(r5, r6, r7, r8, r9)     // Catch:{ JSONException -> 0x0073 }
            goto L_0x0073
        L_0x0072:
            r2 = 0
        L_0x0073:
            android.taobao.windvane.jsbridge.WVResult r3 = new android.taobao.windvane.jsbridge.WVResult
            r3.<init>()
            if (r2 == 0) goto L_0x009a
            r0.success((android.taobao.windvane.jsbridge.WVResult) r3)
            boolean r0 = android.taobao.windvane.util.TaoLog.getLogStatus()
            if (r0 == 0) goto L_0x00b8
            java.lang.String r0 = "Base"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "plusUT: param="
            r2.append(r3)
            r2.append(r1)
            java.lang.String r1 = r2.toString()
            android.taobao.windvane.util.TaoLog.d(r0, r1)
            goto L_0x00b8
        L_0x009a:
            java.lang.String r2 = "Base"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "plusUT: parameter error, param="
            r4.append(r5)
            r4.append(r1)
            java.lang.String r1 = r4.toString()
            android.taobao.windvane.util.TaoLog.e(r2, r1)
            java.lang.String r1 = "HY_PARAM_ERR"
            r3.setResult(r1)
            r0.error((android.taobao.windvane.jsbridge.WVResult) r3)
        L_0x00b8:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.jsbridge.api.WVBase.plusUT(android.taobao.windvane.jsbridge.WVCallBackContext, java.lang.String):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:0x006a  */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x008a  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void commitUTEvent(android.taobao.windvane.jsbridge.WVCallBackContext r17, java.lang.String r18) {
        /*
            r16 = this;
            r0 = r17
            r1 = r18
            r2 = 1
            r3 = 0
            org.json.JSONObject r4 = new org.json.JSONObject     // Catch:{ JSONException -> 0x0062 }
            r4.<init>(r1)     // Catch:{ JSONException -> 0x0062 }
            java.lang.String r5 = "eventId"
            int r5 = r4.getInt(r5)     // Catch:{ JSONException -> 0x0062 }
            java.lang.String r6 = "arg1"
            java.lang.String r6 = r4.getString(r6)     // Catch:{ JSONException -> 0x0062 }
            java.lang.String r7 = "arg2"
            java.lang.String r7 = r4.getString(r7)     // Catch:{ JSONException -> 0x0062 }
            java.lang.String r8 = "arg3"
            java.lang.String r8 = r4.getString(r8)     // Catch:{ JSONException -> 0x0062 }
            java.lang.String r9 = "args"
            org.json.JSONObject r4 = r4.getJSONObject(r9)     // Catch:{ JSONException -> 0x0062 }
            r9 = 0
            if (r4 == 0) goto L_0x0059
            int r9 = r4.length()     // Catch:{ JSONException -> 0x0062 }
            java.lang.String[] r9 = new java.lang.String[r9]     // Catch:{ JSONException -> 0x0062 }
            java.util.Iterator r10 = r4.keys()     // Catch:{ JSONException -> 0x0062 }
            r11 = 0
        L_0x0037:
            boolean r12 = r10.hasNext()     // Catch:{ JSONException -> 0x0062 }
            if (r12 == 0) goto L_0x0059
            java.lang.Object r12 = r10.next()     // Catch:{ JSONException -> 0x0062 }
            java.lang.String r12 = (java.lang.String) r12     // Catch:{ JSONException -> 0x0062 }
            java.lang.String r13 = r4.getString(r12)     // Catch:{ JSONException -> 0x0062 }
            java.lang.String r14 = "%s=%s"
            r15 = 2
            java.lang.Object[] r15 = new java.lang.Object[r15]     // Catch:{ JSONException -> 0x0062 }
            r15[r3] = r12     // Catch:{ JSONException -> 0x0062 }
            r15[r2] = r13     // Catch:{ JSONException -> 0x0062 }
            java.lang.String r12 = java.lang.String.format(r14, r15)     // Catch:{ JSONException -> 0x0062 }
            r9[r11] = r12     // Catch:{ JSONException -> 0x0062 }
            int r11 = r11 + 1
            goto L_0x0037
        L_0x0059:
            r4 = 64403(0xfb93, float:9.0248E-41)
            if (r4 != r5) goto L_0x0062
            android.taobao.windvane.monitor.UserTrackUtil.commitEvent(r5, r6, r7, r8, r9)     // Catch:{ JSONException -> 0x0063 }
            goto L_0x0063
        L_0x0062:
            r2 = 0
        L_0x0063:
            android.taobao.windvane.jsbridge.WVResult r3 = new android.taobao.windvane.jsbridge.WVResult
            r3.<init>()
            if (r2 == 0) goto L_0x008a
            r0.success((android.taobao.windvane.jsbridge.WVResult) r3)
            boolean r0 = android.taobao.windvane.util.TaoLog.getLogStatus()
            if (r0 == 0) goto L_0x00a8
            java.lang.String r0 = "Base"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "commitUTEvent: param="
            r2.append(r3)
            r2.append(r1)
            java.lang.String r1 = r2.toString()
            android.taobao.windvane.util.TaoLog.d(r0, r1)
            goto L_0x00a8
        L_0x008a:
            java.lang.String r2 = "Base"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "commitUTEvent: parameter error, param="
            r4.append(r5)
            r4.append(r1)
            java.lang.String r1 = r4.toString()
            android.taobao.windvane.util.TaoLog.e(r2, r1)
            java.lang.String r1 = "HY_PARAM_ERR"
            r3.setResult(r1)
            r0.error((android.taobao.windvane.jsbridge.WVResult) r3)
        L_0x00a8:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.jsbridge.api.WVBase.commitUTEvent(android.taobao.windvane.jsbridge.WVCallBackContext, java.lang.String):void");
    }

    public void isInstall(WVCallBackContext wVCallBackContext, String str) {
        String str2;
        try {
            str2 = new JSONObject(str).getString("android");
        } catch (JSONException unused) {
            TaoLog.e(WVAPI.PluginName.API_BASE, "isInstall parse params error, params: " + str);
            str2 = null;
        }
        WVResult wVResult = new WVResult();
        boolean isAppInstalled = CommonUtils.isAppInstalled(this.mWebView.getContext(), str2);
        if (TaoLog.getLogStatus()) {
            TaoLog.d(WVAPI.PluginName.API_BASE, "isInstall " + isAppInstalled + " for package " + str2);
        }
        if (isAppInstalled) {
            wVCallBackContext.success(wVResult);
        } else {
            wVCallBackContext.error(wVResult);
        }
    }

    public void isAppsInstalled(WVCallBackContext wVCallBackContext, String str) {
        PackageInfo packageInfo;
        try {
            JSONObject jSONObject = new JSONObject(str);
            Iterator<String> keys = jSONObject.keys();
            WVResult wVResult = new WVResult();
            PackageManager packageManager = this.mWebView.getContext().getPackageManager();
            while (keys.hasNext()) {
                String next = keys.next();
                try {
                    try {
                        packageInfo = packageManager.getPackageInfo(jSONObject.getJSONObject(next).optString("android"), 0);
                    } catch (Exception unused) {
                        packageInfo = null;
                    }
                    wVResult.addData(next, packageInfo == null ? "0" : "1");
                } catch (JSONException e) {
                    e.printStackTrace();
                    wVResult.addData(next, "0");
                }
            }
            wVResult.setSuccess();
            wVCallBackContext.success(wVResult);
        } catch (JSONException e2) {
            e2.printStackTrace();
            wVCallBackContext.error();
        }
    }

    private void copyToClipboard(WVCallBackContext wVCallBackContext, String str) {
        WVResult wVResult = new WVResult("HY_PARAM_ERR");
        String str2 = "HY_PARAM_ERR";
        if (!TextUtils.isEmpty(str)) {
            try {
                JSONObject jSONObject = new JSONObject(str);
                if (jSONObject.has("text")) {
                    String string = jSONObject.getString("text");
                    if (Build.VERSION.SDK_INT >= 11) {
                        ((ClipboardManager) this.mWebView.getContext().getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText(string, string));
                        WVResult wVResult2 = new WVResult(WVResult.SUCCESS);
                        try {
                            wVCallBackContext.success(wVResult2);
                            return;
                        } catch (JSONException e) {
                            JSONException jSONException = e;
                            wVResult = wVResult2;
                            e = jSONException;
                            e.printStackTrace();
                            wVResult.addData("msg", str2);
                            wVCallBackContext.error(wVResult);
                        }
                    } else {
                        str2 = "HY_FAILED";
                        wVResult = new WVResult("HY_FAILED");
                    }
                }
            } catch (JSONException e2) {
                e = e2;
                e.printStackTrace();
                wVResult.addData("msg", str2);
                wVCallBackContext.error(wVResult);
            }
        }
        wVResult.addData("msg", str2);
        wVCallBackContext.error(wVResult);
    }
}
