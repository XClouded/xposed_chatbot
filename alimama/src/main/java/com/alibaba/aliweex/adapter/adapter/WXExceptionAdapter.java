package com.alibaba.aliweex.adapter.adapter;

import android.app.Application;
import android.text.TextUtils;
import com.alibaba.aliweex.AliWeex;
import com.alibaba.aliweex.IConfigAdapter;
import com.alibaba.aliweex.utils.WXInitConfigManager;
import com.alibaba.android.bindingx.core.internal.BindingXConstants;
import com.taobao.weex.adapter.IWXJSExceptionAdapter;
import com.taobao.weex.utils.WXLogUtils;

public class WXExceptionAdapter implements IWXJSExceptionAdapter {
    private final String ERROR_CODE = "errorCode";
    private final String ERROR_GROUP = "errorGroup";
    private final String ERROR_TYPE = "errorType";
    private final String FRAMEWORK_VERSION = "frameWorkVersion";
    private final String INSTANCE_ID = BindingXConstants.KEY_INSTANCE_ID;

    /* JADX WARNING: Can't wrap try/catch for region: R(22:20|(1:22)|23|(3:25|(1:27)(1:28)|29)|30|(1:32)|33|(4:37|(1:39)(1:40)|41|(5:43|(3:45|(1:47)(1:48)|49)|50|51|52))|56|(1:58)(1:59)|60|(1:62)(1:63)|64|(2:68|(1:70))|71|72|(1:82)|83|84|(1:90)|91|(1:103)(4:94|95|98|104)) */
    /* JADX WARNING: Code restructure failed: missing block: B:100:0x0269, code lost:
        android.util.Log.e("weex js err", "build weex callback data err", r12);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:101:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:97:?, code lost:
        r0 = new java.util.HashMap();
        r0.put("bundleUrl", r12.getBundleUrl());
        r0.put("errorCode", r12.getErrCode());
        r0.put(com.uc.webview.export.extension.UCCore.EVENT_EXCEPTION, r12.getException());
        r0.put("extParams", r12.getExtParams());
        r0.put("function", r12.getFunction());
        r0.put(com.alibaba.android.bindingx.core.internal.BindingXConstants.KEY_INSTANCE_ID, r12.getInstanceId());
        r0.put("jsFrameworkVersion", r12.getJsFrameworkVersion());
        r0.put("weexVersion", r12.getWeexVersion());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:99:0x0268, code lost:
        r12 = move-exception;
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:83:0x01c7 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:96:0x0215 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onJSException(com.taobao.weex.common.WXJSExceptionInfo r12) {
        /*
            r11 = this;
            if (r12 != 0) goto L_0x000a
            java.lang.String r12 = "WXJSExceptionAdapter"
            java.lang.String r0 = "null == exception"
            android.util.Log.e(r12, r0)
            return
        L_0x000a:
            java.lang.String r0 = "weex js err"
            java.lang.String r1 = "js err start"
            android.util.Log.i(r0, r1)     // Catch:{ Exception -> 0x0268 }
            com.alibaba.ha.bizerrorreporter.module.BizErrorModule r0 = new com.alibaba.ha.bizerrorreporter.module.BizErrorModule     // Catch:{ Exception -> 0x0268 }
            r0.<init>()     // Catch:{ Exception -> 0x0268 }
            com.alibaba.ha.bizerrorreporter.module.AggregationType r1 = com.alibaba.ha.bizerrorreporter.module.AggregationType.CONTENT     // Catch:{ Exception -> 0x0268 }
            r0.aggregationType = r1     // Catch:{ Exception -> 0x0268 }
            com.taobao.weex.common.WXErrorCode r1 = r12.getErrCode()     // Catch:{ Exception -> 0x0268 }
            com.taobao.weex.common.WXErrorCode$ErrorGroup r2 = r1.getErrorGroup()     // Catch:{ Exception -> 0x0268 }
            com.taobao.weex.common.WXErrorCode$ErrorGroup r3 = com.taobao.weex.common.WXErrorCode.ErrorGroup.NATIVE     // Catch:{ Exception -> 0x0268 }
            r4 = 1
            r5 = 0
            if (r2 != r3) goto L_0x003b
            com.taobao.weex.common.WXErrorCode$ErrorType r2 = r1.getErrorType()     // Catch:{ Exception -> 0x0268 }
            com.taobao.weex.common.WXErrorCode$ErrorType r3 = com.taobao.weex.common.WXErrorCode.ErrorType.NATIVE_ERROR     // Catch:{ Exception -> 0x0268 }
            if (r2 != r3) goto L_0x003b
            java.lang.String r2 = "weex_native_error"
            r0.businessType = r2     // Catch:{ Exception -> 0x0268 }
            java.lang.String r2 = r1.getErrorCode()     // Catch:{ Exception -> 0x0268 }
            r0.exceptionCode = r2     // Catch:{ Exception -> 0x0268 }
            goto L_0x005f
        L_0x003b:
            java.lang.String r2 = "WEEX_ERROR"
            r0.businessType = r2     // Catch:{ Exception -> 0x0268 }
            java.lang.String r2 = r12.getBundleUrl()     // Catch:{ Exception -> 0x0268 }
            if (r2 == 0) goto L_0x0058
            java.lang.String r2 = com.alibaba.aliweex.utils.WXUriUtil.getRealNameFromNameOrUrl(r2, r4)     // Catch:{ Exception -> 0x0268 }
            int r3 = r2.length()     // Catch:{ Exception -> 0x0268 }
            r6 = 1024(0x400, float:1.435E-42)
            if (r3 <= r6) goto L_0x0055
            java.lang.String r2 = r2.substring(r5, r6)     // Catch:{ Exception -> 0x0268 }
        L_0x0055:
            r0.exceptionCode = r2     // Catch:{ Exception -> 0x0268 }
            goto L_0x005f
        L_0x0058:
            java.lang.String r2 = "weex js err"
            java.lang.String r3 = "bundle url is null"
            android.util.Log.i(r2, r3)     // Catch:{ Exception -> 0x0268 }
        L_0x005f:
            java.lang.String r2 = r0.businessType     // Catch:{ Exception -> 0x0268 }
            boolean r2 = r11.reportSwitch(r2)     // Catch:{ Exception -> 0x0268 }
            if (r2 != 0) goto L_0x0068
            return
        L_0x0068:
            java.lang.String r2 = r12.getBundleUrl()     // Catch:{ Exception -> 0x0268 }
            r0.exceptionDetail = r2     // Catch:{ Exception -> 0x0268 }
            java.lang.String r2 = r1.getErrorCode()     // Catch:{ Exception -> 0x0268 }
            r0.exceptionId = r2     // Catch:{ Exception -> 0x0268 }
            java.lang.String r2 = r12.getWeexVersion()     // Catch:{ Exception -> 0x0268 }
            if (r2 == 0) goto L_0x007c
            r0.exceptionVersion = r2     // Catch:{ Exception -> 0x0268 }
        L_0x007c:
            java.lang.String r2 = r12.getException()     // Catch:{ Exception -> 0x0268 }
            if (r2 == 0) goto L_0x00a5
            java.lang.String r3 = "\n"
            int r3 = r2.indexOf(r3)     // Catch:{ Exception -> 0x0268 }
            if (r3 <= 0) goto L_0x008f
            java.lang.String r3 = r2.substring(r5, r3)     // Catch:{ Exception -> 0x0268 }
            goto L_0x0090
        L_0x008f:
            r3 = r2
        L_0x0090:
            r0.exceptionArg1 = r3     // Catch:{ Exception -> 0x0268 }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0268 }
            r3.<init>()     // Catch:{ Exception -> 0x0268 }
            r3.append(r2)     // Catch:{ Exception -> 0x0268 }
            java.lang.String r6 = "\nend_weex_stack\n"
            r3.append(r6)     // Catch:{ Exception -> 0x0268 }
            java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x0268 }
            r0.exceptionArg3 = r3     // Catch:{ Exception -> 0x0268 }
        L_0x00a5:
            java.lang.String r3 = r12.getFunction()     // Catch:{ Exception -> 0x0268 }
            if (r3 == 0) goto L_0x00ad
            r0.exceptionArg2 = r3     // Catch:{ Exception -> 0x0268 }
        L_0x00ad:
            java.util.HashMap r3 = new java.util.HashMap     // Catch:{ Exception -> 0x0268 }
            r3.<init>()     // Catch:{ Exception -> 0x0268 }
            java.lang.String r6 = "errorCode"
            java.lang.String r7 = r1.getErrorCode()     // Catch:{ Exception -> 0x0268 }
            r3.put(r6, r7)     // Catch:{ Exception -> 0x0268 }
            java.lang.String r6 = "errorGroup"
            com.taobao.weex.common.WXErrorCode$ErrorGroup r7 = r1.getErrorGroup()     // Catch:{ Exception -> 0x0268 }
            r3.put(r6, r7)     // Catch:{ Exception -> 0x0268 }
            java.lang.String r6 = "errorType"
            com.taobao.weex.common.WXErrorCode$ErrorType r7 = r1.getErrorType()     // Catch:{ Exception -> 0x0268 }
            r3.put(r6, r7)     // Catch:{ Exception -> 0x0268 }
            com.taobao.weex.WXSDKManager r6 = com.taobao.weex.WXSDKManager.getInstance()     // Catch:{ Exception -> 0x0268 }
            java.lang.String r7 = r12.getInstanceId()     // Catch:{ Exception -> 0x0268 }
            com.taobao.weex.WXSDKInstance r6 = r6.getSDKInstance(r7)     // Catch:{ Exception -> 0x0268 }
            if (r6 == 0) goto L_0x0145
            com.taobao.weex.common.WXErrorCode$ErrorGroup r7 = r1.getErrorGroup()     // Catch:{ Exception -> 0x0268 }
            com.taobao.weex.common.WXErrorCode$ErrorGroup r8 = com.taobao.weex.common.WXErrorCode.ErrorGroup.JS     // Catch:{ Exception -> 0x0268 }
            if (r7 != r8) goto L_0x0145
            com.taobao.weex.common.WXPerformance r7 = r6.getWXPerformance()     // Catch:{ Exception -> 0x0268 }
            java.lang.String r7 = r7.pageName     // Catch:{ Exception -> 0x0268 }
            java.lang.String r8 = "wxBundlePageName"
            if (r7 != 0) goto L_0x00f1
            java.lang.String r9 = "unKnowPageNameCaseUnSet"
            goto L_0x00f2
        L_0x00f1:
            r9 = r7
        L_0x00f2:
            r3.put(r8, r9)     // Catch:{ Exception -> 0x0268 }
            android.content.Context r8 = r6.getContext()     // Catch:{ Exception -> 0x0268 }
            boolean r8 = r8 instanceof com.alibaba.aliweex.adapter.adapter.IFeedBackCallBack     // Catch:{ Exception -> 0x0268 }
            if (r8 == 0) goto L_0x0145
            android.content.Context r8 = r6.getContext()     // Catch:{ Exception -> 0x0268 }
            com.alibaba.aliweex.adapter.adapter.IFeedBackCallBack r8 = (com.alibaba.aliweex.adapter.adapter.IFeedBackCallBack) r8     // Catch:{ Exception -> 0x0268 }
            com.alibaba.aliweex.adapter.adapter.IFeedBackCallBack r8 = (com.alibaba.aliweex.adapter.adapter.IFeedBackCallBack) r8     // Catch:{ Exception -> 0x0268 }
            java.util.HashMap r9 = new java.util.HashMap     // Catch:{ Exception -> 0x0268 }
            r9.<init>()     // Catch:{ Exception -> 0x0268 }
            java.lang.String r10 = "useWeex"
            java.lang.Boolean r4 = java.lang.Boolean.valueOf(r4)     // Catch:{ Exception -> 0x0268 }
            r9.put(r10, r4)     // Catch:{ Exception -> 0x0268 }
            java.lang.String r4 = "infoType"
            java.lang.String r10 = r0.businessType     // Catch:{ Exception -> 0x0268 }
            r9.put(r4, r10)     // Catch:{ Exception -> 0x0268 }
            if (r2 == 0) goto L_0x0132
            int r4 = r2.length()     // Catch:{ Exception -> 0x0268 }
            r10 = 200(0xc8, float:2.8E-43)
            if (r4 <= r10) goto L_0x0125
            goto L_0x0129
        L_0x0125:
            int r10 = r2.length()     // Catch:{ Exception -> 0x0268 }
        L_0x0129:
            java.lang.String r2 = r2.substring(r5, r10)     // Catch:{ Exception -> 0x0268 }
            java.lang.String r4 = "shortErrorMsg"
            r9.put(r4, r2)     // Catch:{ Exception -> 0x0268 }
        L_0x0132:
            java.lang.String r2 = "weexRealPageName"
            r9.put(r2, r7)     // Catch:{ Exception -> 0x0268 }
            java.lang.String r2 = com.alibaba.fastjson.JSON.toJSONString(r9)     // Catch:{ Throwable -> 0x0141 }
            java.lang.String r4 = r0.businessType     // Catch:{ Throwable -> 0x0141 }
            r8.addFeedCallBackInfo(r4, r2)     // Catch:{ Throwable -> 0x0141 }
            goto L_0x0145
        L_0x0141:
            r2 = move-exception
            r2.printStackTrace()     // Catch:{ Exception -> 0x0268 }
        L_0x0145:
            java.lang.String r2 = r12.getInstanceId()     // Catch:{ Exception -> 0x0268 }
            if (r2 == 0) goto L_0x0151
            java.lang.String r4 = "instanceId"
            r3.put(r4, r2)     // Catch:{ Exception -> 0x0268 }
            goto L_0x0158
        L_0x0151:
            java.lang.String r4 = "instanceId"
            java.lang.String r5 = "no instanceId"
            r3.put(r4, r5)     // Catch:{ Exception -> 0x0268 }
        L_0x0158:
            java.lang.String r4 = r12.getJsFrameworkVersion()     // Catch:{ Exception -> 0x0268 }
            if (r4 == 0) goto L_0x0164
            java.lang.String r5 = "frameWorkVersion"
            r3.put(r5, r4)     // Catch:{ Exception -> 0x0268 }
            goto L_0x016b
        L_0x0164:
            java.lang.String r4 = "frameWorkVersion"
            java.lang.String r5 = "no framework version"
            r3.put(r4, r5)     // Catch:{ Exception -> 0x0268 }
        L_0x016b:
            java.util.Map r4 = r12.getExtParams()     // Catch:{ Exception -> 0x0268 }
            if (r4 == 0) goto L_0x0194
            int r5 = r4.size()     // Catch:{ Exception -> 0x0268 }
            if (r5 <= 0) goto L_0x0194
            r3.putAll(r4)     // Catch:{ Exception -> 0x0268 }
            java.lang.String r5 = "wxGreyBundle"
            java.lang.Object r5 = r4.get(r5)     // Catch:{ Exception -> 0x0268 }
            java.lang.String r5 = (java.lang.String) r5     // Catch:{ Exception -> 0x0268 }
            boolean r5 = android.text.TextUtils.isEmpty(r5)     // Catch:{ Exception -> 0x0268 }
            if (r5 != 0) goto L_0x0194
            java.lang.String r5 = "wxGreyBundle"
            java.lang.Object r4 = r4.get(r5)     // Catch:{ Exception -> 0x0268 }
            java.lang.String r4 = (java.lang.String) r4     // Catch:{ Exception -> 0x0268 }
            r0.exceptionId = r4     // Catch:{ Exception -> 0x0268 }
        L_0x0194:
            boolean r4 = android.text.TextUtils.isEmpty(r2)     // Catch:{ Throwable -> 0x01c7 }
            if (r4 != 0) goto L_0x01c7
            com.taobao.weex.WXSDKManager r4 = com.taobao.weex.WXSDKManager.getInstance()     // Catch:{ Throwable -> 0x01c7 }
            java.util.Map r4 = r4.getAllInstanceMap()     // Catch:{ Throwable -> 0x01c7 }
            java.lang.Object r2 = r4.get(r2)     // Catch:{ Throwable -> 0x01c7 }
            com.taobao.weex.WXSDKInstance r2 = (com.taobao.weex.WXSDKInstance) r2     // Catch:{ Throwable -> 0x01c7 }
            if (r2 == 0) goto L_0x01c7
            java.util.Map r2 = r2.getContainerInfo()     // Catch:{ Throwable -> 0x01c7 }
            if (r2 == 0) goto L_0x01c7
            java.lang.String r4 = com.alibaba.aliweex.AliWXSDKEngine.WX_AIR_TAG     // Catch:{ Throwable -> 0x01c7 }
            boolean r4 = r2.containsKey(r4)     // Catch:{ Throwable -> 0x01c7 }
            if (r4 == 0) goto L_0x01c7
            java.lang.String r4 = com.alibaba.aliweex.AliWXSDKEngine.WX_AIR_TAG     // Catch:{ Throwable -> 0x01c7 }
            java.lang.Object r2 = r2.get(r4)     // Catch:{ Throwable -> 0x01c7 }
            java.lang.String r2 = (java.lang.String) r2     // Catch:{ Throwable -> 0x01c7 }
            if (r2 == 0) goto L_0x01c7
            java.lang.String r4 = com.alibaba.aliweex.AliWXSDKEngine.WX_AIR_TAG     // Catch:{ Throwable -> 0x01c7 }
            r3.put(r4, r2)     // Catch:{ Throwable -> 0x01c7 }
        L_0x01c7:
            r0.exceptionArgs = r3     // Catch:{ Exception -> 0x0268 }
            java.lang.String r2 = "god_eye_stage_data"
            boolean r2 = r11.reportSwitch(r2)     // Catch:{ Exception -> 0x0268 }
            if (r2 == 0) goto L_0x01e6
            com.taobao.weex.common.WXErrorCode r2 = com.taobao.weex.common.WXErrorCode.WX_ERROR_WHITE_SCREEN     // Catch:{ Exception -> 0x0268 }
            if (r1 == r2) goto L_0x01e6
            com.alibaba.aliweex.AliWeex r2 = com.alibaba.aliweex.AliWeex.getInstance()     // Catch:{ Exception -> 0x0268 }
            com.alibaba.aliweex.adapter.IGodEyeStageAdapter r2 = r2.getGodEyeStageAdapter()     // Catch:{ Exception -> 0x0268 }
            if (r2 == 0) goto L_0x01e6
            java.lang.String r4 = "exception_weex_check"
            java.lang.String r5 = r0.exceptionArg1     // Catch:{ Exception -> 0x0268 }
            r2.onException(r4, r5, r3)     // Catch:{ Exception -> 0x0268 }
        L_0x01e6:
            java.lang.Thread r2 = java.lang.Thread.currentThread()     // Catch:{ Exception -> 0x0268 }
            r0.thread = r2     // Catch:{ Exception -> 0x0268 }
            com.alibaba.ha.bizerrorreporter.BizErrorReporter r2 = com.alibaba.ha.bizerrorreporter.BizErrorReporter.getInstance()     // Catch:{ Exception -> 0x0268 }
            com.alibaba.aliweex.AliWeex r3 = com.alibaba.aliweex.AliWeex.getInstance()     // Catch:{ Exception -> 0x0268 }
            android.app.Application r3 = r3.getApplication()     // Catch:{ Exception -> 0x0268 }
            r2.send(r3, r0)     // Catch:{ Exception -> 0x0268 }
            java.lang.String r0 = "weex js err"
            java.lang.String r2 = "js err end"
            android.util.Log.i(r0, r2)     // Catch:{ Exception -> 0x0268 }
            com.taobao.weex.common.WXErrorCode$ErrorGroup r0 = r1.getErrorGroup()     // Catch:{ Exception -> 0x0268 }
            com.taobao.weex.common.WXErrorCode$ErrorGroup r1 = com.taobao.weex.common.WXErrorCode.ErrorGroup.JS     // Catch:{ Exception -> 0x0268 }
            if (r0 != r1) goto L_0x0270
            if (r6 == 0) goto L_0x0270
            java.lang.String r0 = com.alibaba.fastjson.JSON.toJSONString(r12)     // Catch:{ Exception -> 0x0215 }
            com.alibaba.fastjson.JSONObject r0 = com.alibaba.fastjson.JSON.parseObject(r0)     // Catch:{ Exception -> 0x0215 }
            goto L_0x0262
        L_0x0215:
            java.util.HashMap r0 = new java.util.HashMap     // Catch:{ Exception -> 0x0268 }
            r0.<init>()     // Catch:{ Exception -> 0x0268 }
            java.lang.String r1 = "bundleUrl"
            java.lang.String r2 = r12.getBundleUrl()     // Catch:{ Exception -> 0x0268 }
            r0.put(r1, r2)     // Catch:{ Exception -> 0x0268 }
            java.lang.String r1 = "errorCode"
            com.taobao.weex.common.WXErrorCode r2 = r12.getErrCode()     // Catch:{ Exception -> 0x0268 }
            r0.put(r1, r2)     // Catch:{ Exception -> 0x0268 }
            java.lang.String r1 = "exception"
            java.lang.String r2 = r12.getException()     // Catch:{ Exception -> 0x0268 }
            r0.put(r1, r2)     // Catch:{ Exception -> 0x0268 }
            java.lang.String r1 = "extParams"
            java.util.Map r2 = r12.getExtParams()     // Catch:{ Exception -> 0x0268 }
            r0.put(r1, r2)     // Catch:{ Exception -> 0x0268 }
            java.lang.String r1 = "function"
            java.lang.String r2 = r12.getFunction()     // Catch:{ Exception -> 0x0268 }
            r0.put(r1, r2)     // Catch:{ Exception -> 0x0268 }
            java.lang.String r1 = "instanceId"
            java.lang.String r2 = r12.getInstanceId()     // Catch:{ Exception -> 0x0268 }
            r0.put(r1, r2)     // Catch:{ Exception -> 0x0268 }
            java.lang.String r1 = "jsFrameworkVersion"
            java.lang.String r2 = r12.getJsFrameworkVersion()     // Catch:{ Exception -> 0x0268 }
            r0.put(r1, r2)     // Catch:{ Exception -> 0x0268 }
            java.lang.String r1 = "weexVersion"
            java.lang.String r12 = r12.getWeexVersion()     // Catch:{ Exception -> 0x0268 }
            r0.put(r1, r12)     // Catch:{ Exception -> 0x0268 }
        L_0x0262:
            java.lang.String r12 = "exception"
            r6.fireGlobalEventCallback(r12, r0)     // Catch:{ Exception -> 0x0268 }
            goto L_0x0270
        L_0x0268:
            r12 = move-exception
            java.lang.String r0 = "weex js err"
            java.lang.String r1 = "build weex callback data err"
            android.util.Log.e(r0, r1, r12)
        L_0x0270:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.aliweex.adapter.adapter.WXExceptionAdapter.onJSException(com.taobao.weex.common.WXJSExceptionInfo):void");
    }

    public static String getAppVersionName() {
        try {
            Application application = AliWeex.getInstance().getApplication();
            if (application == null) {
                return "";
            }
            return application.getPackageManager().getPackageInfo(application.getPackageName(), 0).versionName;
        } catch (Exception e) {
            WXLogUtils.e("WXExceptionAdapter getAppVersionName Exception: ", (Throwable) e);
            return "";
        }
    }

    private boolean reportSwitch(String str) {
        if (TextUtils.isEmpty(str)) {
            return true;
        }
        double d = 100.0d;
        double random = Math.random() * 100.0d;
        if ("weex_native_error".equals(str)) {
            String appVersionName = getAppVersionName();
            if (TextUtils.isEmpty(appVersionName) || appVersionName.split(".").length == 3) {
                d = 10.0d;
            }
        }
        IConfigAdapter configAdapter = AliWeex.getInstance().getConfigAdapter();
        if (configAdapter != null) {
            try {
                d = Double.valueOf(configAdapter.getConfig(WXInitConfigManager.WXAPM_CONFIG_GROUP, str, String.valueOf(d))).doubleValue();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (random < d) {
            return true;
        }
        return false;
    }
}
