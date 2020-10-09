package com.taobao.login4android.jsbridge;

import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.jsbridge.WVResult;
import android.text.TextUtils;
import com.ali.user.mobile.app.init.Debuggable;
import com.ali.user.mobile.common.api.AliUserLogin;
import com.ali.user.mobile.common.api.LoginApprearanceExtensions;
import com.ali.user.mobile.log.TLogAdapter;
import com.ali.user.mobile.rpc.ApiConstants;
import com.ali.user.mobile.service.RpcService;
import com.ali.user.mobile.service.ServiceFactory;
import com.ali.user.mobile.service.UIService;
import com.taobao.login4android.session.SessionManager;
import org.json.JSONException;
import org.json.JSONObject;

public class SDKJSBridgeService extends WVApiPlugin {
    /* access modifiers changed from: private */
    public String Tag = "SDKJSBridgeService";

    public boolean execute(String str, String str2, WVCallBackContext wVCallBackContext) {
        if ("help".equals(str)) {
            setHelp(wVCallBackContext, str2);
            return true;
        } else if ("testAccountSsoLogin".equals(str)) {
            testSsoLogin(wVCallBackContext, str2);
            return true;
        } else if ("testRegisterHidSid".equals(str)) {
            testRegisterHidSid(wVCallBackContext, str2);
            return true;
        } else if ("testMtopLogout".equals(str)) {
            testMtopLogout(wVCallBackContext);
            return true;
        } else if ("testSetSid".equals(str)) {
            setSid(wVCallBackContext, str2);
            return true;
        } else if (!"testSetHid".equals(str)) {
            return false;
        } else {
            setHid(wVCallBackContext, str2);
            return true;
        }
    }

    public synchronized void setHid(WVCallBackContext wVCallBackContext, String str) {
        if (!TextUtils.isEmpty(str)) {
            try {
                SessionManager.getInstance(this.mContext).setUserId(new JSONObject(str).optString(ApiConstants.ApiField.HID));
            } catch (Throwable th) {
                th.printStackTrace();
                paramErrorCallback(wVCallBackContext);
            }
        } else {
            paramErrorCallback(wVCallBackContext);
        }
    }

    public synchronized void setSid(WVCallBackContext wVCallBackContext, String str) {
        if (!TextUtils.isEmpty(str)) {
            try {
                SessionManager.getInstance(this.mContext).setSid(new JSONObject(str).optString("sid"));
            } catch (Throwable th) {
                th.printStackTrace();
                paramErrorCallback(wVCallBackContext);
            }
        } else {
            paramErrorCallback(wVCallBackContext);
        }
    }

    public synchronized void testRegisterHidSid(WVCallBackContext wVCallBackContext, String str) {
        if (!TextUtils.isEmpty(str)) {
            try {
                JSONObject jSONObject = new JSONObject(str);
                String optString = jSONObject.optString(ApiConstants.ApiField.HID);
                String optString2 = jSONObject.optString("sid");
                if (TextUtils.isEmpty(optString) || TextUtils.isEmpty(optString2)) {
                    paramErrorCallback(wVCallBackContext);
                } else {
                    ((RpcService) ServiceFactory.getService(RpcService.class)).registerSessionInfo(optString2, optString, "");
                }
            } catch (Throwable th) {
                th.printStackTrace();
                paramErrorCallback(wVCallBackContext);
            }
        } else {
            paramErrorCallback(wVCallBackContext);
        }
    }

    private void paramErrorCallback(WVCallBackContext wVCallBackContext) {
        if (wVCallBackContext != null) {
            paramError(wVCallBackContext);
        }
    }

    public synchronized void testMtopLogout(WVCallBackContext wVCallBackContext) {
        ((RpcService) ServiceFactory.getService(RpcService.class)).logout();
        if (wVCallBackContext != null) {
            wVCallBackContext.success();
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(4:12|13|14|15) */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0055, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:?, code lost:
        paramErrorCallback(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x005a, code lost:
        return;
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:12:0x0056 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void testSsoLogin(final android.taobao.windvane.jsbridge.WVCallBackContext r4, java.lang.String r5) {
        /*
            r3 = this;
            monitor-enter(r3)
            boolean r0 = android.text.TextUtils.isEmpty(r5)     // Catch:{ all -> 0x0060 }
            if (r0 != 0) goto L_0x005b
            org.json.JSONObject r0 = new org.json.JSONObject     // Catch:{ JSONException -> 0x0056 }
            r0.<init>(r5)     // Catch:{ JSONException -> 0x0056 }
            java.lang.String r5 = r3.Tag     // Catch:{ JSONException -> 0x0056 }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ JSONException -> 0x0056 }
            r1.<init>()     // Catch:{ JSONException -> 0x0056 }
            java.lang.String r2 = "jsobj = "
            r1.append(r2)     // Catch:{ JSONException -> 0x0056 }
            java.lang.String r2 = r0.toString()     // Catch:{ JSONException -> 0x0056 }
            r1.append(r2)     // Catch:{ JSONException -> 0x0056 }
            java.lang.String r1 = r1.toString()     // Catch:{ JSONException -> 0x0056 }
            com.ali.user.mobile.log.TLogAdapter.d(r5, r1)     // Catch:{ JSONException -> 0x0056 }
            java.lang.String r5 = "action"
            java.lang.Object r5 = r0.get(r5)     // Catch:{ JSONException -> 0x0056 }
            java.lang.String r5 = (java.lang.String) r5     // Catch:{ JSONException -> 0x0056 }
            java.lang.String r1 = "token"
            java.lang.Object r0 = r0.get(r1)     // Catch:{ JSONException -> 0x0056 }
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ JSONException -> 0x0056 }
            boolean r1 = android.text.TextUtils.isEmpty(r0)     // Catch:{ JSONException -> 0x0056 }
            if (r1 != 0) goto L_0x0054
            java.lang.String r1 = "testAccountSso"
            boolean r5 = r1.equals(r5)     // Catch:{ JSONException -> 0x0056 }
            if (r5 == 0) goto L_0x0054
            com.ali.user.mobile.coordinator.CoordinatorWrapper r5 = new com.ali.user.mobile.coordinator.CoordinatorWrapper     // Catch:{ JSONException -> 0x0056 }
            r5.<init>()     // Catch:{ JSONException -> 0x0056 }
            com.taobao.login4android.jsbridge.SDKJSBridgeService$1 r1 = new com.taobao.login4android.jsbridge.SDKJSBridgeService$1     // Catch:{ JSONException -> 0x0056 }
            r1.<init>(r0, r4)     // Catch:{ JSONException -> 0x0056 }
            r0 = 0
            java.lang.Object[] r0 = new java.lang.Object[r0]     // Catch:{ JSONException -> 0x0056 }
            r5.execute(r1, r0)     // Catch:{ JSONException -> 0x0056 }
        L_0x0054:
            monitor-exit(r3)
            return
        L_0x0056:
            r3.paramErrorCallback(r4)     // Catch:{ all -> 0x0060 }
            monitor-exit(r3)
            return
        L_0x005b:
            r3.paramErrorCallback(r4)     // Catch:{ all -> 0x0060 }
            monitor-exit(r3)
            return
        L_0x0060:
            r4 = move-exception
            monitor-exit(r3)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.login4android.jsbridge.SDKJSBridgeService.testSsoLogin(android.taobao.windvane.jsbridge.WVCallBackContext, java.lang.String):void");
    }

    private void paramError(WVCallBackContext wVCallBackContext) {
        WVResult wVResult = new WVResult();
        wVResult.setResult("HY_PARAM_ERR");
        wVCallBackContext.error(wVResult);
    }

    /* access modifiers changed from: private */
    public void failCallback(WVCallBackContext wVCallBackContext, String str, String str2) {
        WVResult wVResult = new WVResult();
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("message", str);
            jSONObject.put("code", str2);
        } catch (Exception unused) {
        }
        wVResult.setData(jSONObject);
        wVResult.setResult("HY_FAILED");
        wVCallBackContext.error(wVResult);
    }

    public synchronized void setHelp(WVCallBackContext wVCallBackContext, String str) {
        LoginApprearanceExtensions loginApprearanceExtensions = AliUserLogin.mAppreanceExtentions;
        if (loginApprearanceExtensions != null && loginApprearanceExtensions.needHelp()) {
            requestHelp(wVCallBackContext, str);
        }
    }

    public void requestHelp(WVCallBackContext wVCallBackContext, String str) {
        if (wVCallBackContext == null) {
            TLogAdapter.e(this.Tag, "Callback is null");
        }
        if (!TextUtils.isEmpty(str)) {
            try {
                JSONObject jSONObject = new JSONObject(str);
                if (Debuggable.isDebug()) {
                    String str2 = this.Tag;
                    TLogAdapter.d(str2, "jsobj = " + jSONObject.toString());
                }
                ((UIService) ServiceFactory.getService(UIService.class)).switchWebViewTitleBarRightButton(this.mContext, true, (String) jSONObject.get("key1"));
                WVResult wVResult = new WVResult();
                wVResult.setResult("success");
                wVCallBackContext.success(wVResult);
            } catch (JSONException unused) {
                paramError(wVCallBackContext);
            }
        }
    }
}
