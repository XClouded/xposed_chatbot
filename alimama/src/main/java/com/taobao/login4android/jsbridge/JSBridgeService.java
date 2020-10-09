package com.taobao.login4android.jsbridge;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.jsbridge.WVResult;
import android.text.TextUtils;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.callback.RpcRequestCallbackWithCode;
import com.ali.user.mobile.info.AppInfo;
import com.ali.user.mobile.log.AppMonitorAdapter;
import com.ali.user.mobile.log.TLogAdapter;
import com.ali.user.mobile.log.UserTrackAdapter;
import com.ali.user.mobile.model.CommonCallback;
import com.ali.user.mobile.model.RegistParam;
import com.ali.user.mobile.model.UrlParam;
import com.ali.user.mobile.rpc.ApiConstants;
import com.ali.user.mobile.rpc.HistoryAccount;
import com.ali.user.mobile.rpc.RpcRequest;
import com.ali.user.mobile.rpc.RpcResponse;
import com.ali.user.mobile.security.AlibabaSecurityTokenService;
import com.ali.user.mobile.security.SecurityGuardManagerWraper;
import com.ali.user.mobile.service.NavigatorService;
import com.ali.user.mobile.service.RpcService;
import com.ali.user.mobile.service.ServiceFactory;
import com.ali.user.mobile.utils.MainThreadExecutor;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.taobao.android.sso.v2.security.SSOSecurityService;
import com.taobao.login4android.Login;
import com.taobao.login4android.biz.getAlipayCookies.mtop.GetAlipayCookiesResponseData;
import com.taobao.login4android.biz.getYouKuOpenSid.mtop.GetYoukuOpenSidResponseData;
import com.taobao.login4android.broadcast.LoginAction;
import com.taobao.login4android.broadcast.LoginBroadcastHelper;
import com.taobao.login4android.constants.LoginConstants;
import com.taobao.login4android.login.InternalTokenCallback;
import com.taobao.login4android.login.LoginController;
import com.taobao.login4android.login.TmallSsoLogin;
import java.lang.reflect.Method;
import java.util.HashMap;
import org.json.JSONException;

public class JSBridgeService extends WVApiPlugin {
    private String Tag = "UmidJSBridgeService";
    private Class<?> loginCls = null;
    private Method loginMethod;
    private WVCallBackContext mCallback = null;
    private BroadcastReceiver mLoginReceiver;

    public boolean execute(String str, String str2, WVCallBackContext wVCallBackContext) {
        if ("getUserInfo".equals(str)) {
            getUserInfo(wVCallBackContext);
            return true;
        } else if ("getUmid".equals(str)) {
            getUmid(wVCallBackContext, str2);
            return true;
        } else if ("getWua".equals(str)) {
            getWuaData(wVCallBackContext, str2);
            return true;
        } else if ("getAppKey".equals(str)) {
            getAppKey(wVCallBackContext, str2);
            return true;
        } else if ("getSdkVersion".equals(str)) {
            getSDKVersion(wVCallBackContext, str2);
            return true;
        } else if ("showHelpPageTwo".equals(str)) {
            popup(wVCallBackContext, str2);
            return true;
        } else if ("aluOpenWebViewByUrl".equals(str)) {
            openWebViewByUrl(wVCallBackContext, str2);
            return true;
        } else if ("aluCloseWebView".equals(str)) {
            closeWebViewByUrl(wVCallBackContext, str2);
            return true;
        } else if ("aluMockLogin".equals(str)) {
            mockLogin(wVCallBackContext, str2);
            return true;
        } else if ("refreshAlipayCookie".equals(str)) {
            refreshAlipayCookieWithRemoteBiz(wVCallBackContext, str2);
            return true;
        } else if ("aluSetBackButton".equals(str)) {
            setBackFinish(wVCallBackContext, str2);
            return true;
        } else if ("userIsLogin".equals(str)) {
            checkLogin(wVCallBackContext, str2);
            return true;
        } else if ("isOldLogin".equals(str)) {
            isOldLogin(wVCallBackContext);
            return true;
        } else if ("setNaviBarHidden".equals(str)) {
            closeNaviBar(wVCallBackContext, str2);
            return true;
        } else if ("isMemberSDK".equals(str)) {
            isMemberSDK(wVCallBackContext, str2);
            return true;
        } else if ("aluGetSign".equals(str)) {
            getSign(wVCallBackContext, str2);
            return true;
        } else if ("aluGetAtlasSign".equals(str)) {
            getAtlasSign(wVCallBackContext, str2);
            return true;
        } else if ("trustLogin4Tmall".equals(str)) {
            jumpToTmallWithLoginToken(wVCallBackContext, str2);
            return true;
        } else if ("refreshYoukuCookie".equals(str)) {
            refreshYoukuOpenSid(wVCallBackContext, str2);
            return true;
        } else if ("miniProgram".equals(str)) {
            miniProgram(wVCallBackContext, str2);
            return true;
        } else if ("sdkLogin".equals(str)) {
            sdkLogin(wVCallBackContext);
            return true;
        } else if ("sdkRegister".equals(str)) {
            sdkRegister(wVCallBackContext);
            return true;
        } else if (!"appInstalled".equals(str)) {
            return false;
        } else {
            checkAppInstalled(str2, wVCallBackContext);
            return true;
        }
    }

    public void getUserInfo(WVCallBackContext wVCallBackContext) {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("nick", (Object) Login.getNick());
        jSONObject.put("userId", (Object) Login.getUserId());
        jSONObject.put("sid", (Object) Login.getSid());
        wVCallBackContext.success(jSONObject.toJSONString());
    }

    private synchronized void isOldLogin(WVCallBackContext wVCallBackContext) {
        if (TextUtils.isEmpty(Login.getOldUserId())) {
            wVCallBackContext.error();
        } else {
            wVCallBackContext.success();
        }
    }

    private synchronized void checkAppInstalled(String str, WVCallBackContext wVCallBackContext) {
    }

    private void registerBroadcast(final WVCallBackContext wVCallBackContext, boolean z) {
        this.mLoginReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                LoginAction valueOf;
                if (intent != null && (valueOf = LoginAction.valueOf(intent.getAction())) != null) {
                    switch (AnonymousClass7.$SwitchMap$com$taobao$login4android$broadcast$LoginAction[valueOf.ordinal()]) {
                        case 1:
                            JSBridgeService.this.doWhenReceiveSuccess(wVCallBackContext);
                            return;
                        case 2:
                            JSBridgeService.this.doWhenReceivedCancel(wVCallBackContext);
                            return;
                        case 3:
                            JSBridgeService.this.doWhenReceivedCancel(wVCallBackContext);
                            return;
                        default:
                            return;
                    }
                }
            }
        };
        LoginBroadcastHelper.registerLoginReceiver(DataProviderFactory.getApplicationContext(), this.mLoginReceiver);
    }

    /* renamed from: com.taobao.login4android.jsbridge.JSBridgeService$7  reason: invalid class name */
    static /* synthetic */ class AnonymousClass7 {
        static final /* synthetic */ int[] $SwitchMap$com$taobao$login4android$broadcast$LoginAction = new int[LoginAction.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|8) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        static {
            /*
                com.taobao.login4android.broadcast.LoginAction[] r0 = com.taobao.login4android.broadcast.LoginAction.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$com$taobao$login4android$broadcast$LoginAction = r0
                int[] r0 = $SwitchMap$com$taobao$login4android$broadcast$LoginAction     // Catch:{ NoSuchFieldError -> 0x0014 }
                com.taobao.login4android.broadcast.LoginAction r1 = com.taobao.login4android.broadcast.LoginAction.NOTIFY_LOGIN_SUCCESS     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                int[] r0 = $SwitchMap$com$taobao$login4android$broadcast$LoginAction     // Catch:{ NoSuchFieldError -> 0x001f }
                com.taobao.login4android.broadcast.LoginAction r1 = com.taobao.login4android.broadcast.LoginAction.NOTIFY_LOGIN_CANCEL     // Catch:{ NoSuchFieldError -> 0x001f }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                int[] r0 = $SwitchMap$com$taobao$login4android$broadcast$LoginAction     // Catch:{ NoSuchFieldError -> 0x002a }
                com.taobao.login4android.broadcast.LoginAction r1 = com.taobao.login4android.broadcast.LoginAction.NOTIFY_REGISTER_CANCEL     // Catch:{ NoSuchFieldError -> 0x002a }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x002a }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x002a }
            L_0x002a:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.taobao.login4android.jsbridge.JSBridgeService.AnonymousClass7.<clinit>():void");
        }
    }

    /* access modifiers changed from: private */
    public void doWhenReceiveSuccess(WVCallBackContext wVCallBackContext) {
        if (wVCallBackContext != null) {
            wVCallBackContext.success();
        }
        if (this.mLoginReceiver != null) {
            LoginBroadcastHelper.unregisterLoginReceiver(DataProviderFactory.getApplicationContext(), this.mLoginReceiver);
            this.mLoginReceiver = null;
        }
    }

    /* access modifiers changed from: private */
    public void doWhenReceivedCancel(WVCallBackContext wVCallBackContext) {
        if (wVCallBackContext != null) {
            wVCallBackContext.error();
        }
        if (this.mLoginReceiver != null) {
            LoginBroadcastHelper.unregisterLoginReceiver(DataProviderFactory.getApplicationContext(), this.mLoginReceiver);
            this.mLoginReceiver = null;
        }
    }

    private void sdkLogin(WVCallBackContext wVCallBackContext) {
        registerBroadcast(wVCallBackContext, false);
        Bundle bundle = new Bundle();
        bundle.putString(LoginConstants.BROWSER_REF_URL, "jsbridge.sdkLogin");
        Login.login(true, bundle);
    }

    private void sdkRegister(WVCallBackContext wVCallBackContext) {
        registerBroadcast(wVCallBackContext, true);
        RegistParam registParam = new RegistParam();
        registParam.registSite = DataProviderFactory.getDataProvider().getSite();
        Login.goRegister(registParam);
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.mLoginReceiver != null) {
            LoginBroadcastHelper.unregisterLoginReceiver(DataProviderFactory.getApplicationContext(), this.mLoginReceiver);
        }
    }

    private synchronized void miniProgram(final WVCallBackContext wVCallBackContext, String str) {
        if (wVCallBackContext == null) {
            TLogAdapter.e(this.Tag, "Callback is null");
            return;
        }
        this.mCallback = wVCallBackContext;
        if (!TextUtils.isEmpty(str)) {
            try {
                org.json.JSONObject jSONObject = new org.json.JSONObject(str);
                LoginController.getInstance().navByScheme(jSONObject.getString("slaveAppKey"), jSONObject.getString("packageName"), jSONObject.getString("jumpPage"), (String) null, new CommonCallback() {
                    public void onSuccess() {
                        wVCallBackContext.success();
                    }

                    public void onFail(int i, String str) {
                        WVResult wVResult = new WVResult();
                        wVResult.addData("code", (Object) Integer.valueOf(i));
                        wVResult.addData("msg", str);
                        wVCallBackContext.error(wVResult);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                setErrorCallback(wVCallBackContext);
            }
        } else {
            setErrorCallback(wVCallBackContext);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x003c, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private synchronized void isMemberSDK(android.taobao.windvane.jsbridge.WVCallBackContext r2, java.lang.String r3) {
        /*
            r1 = this;
            monitor-enter(r1)
            if (r2 != 0) goto L_0x000e
            java.lang.String r2 = r1.Tag     // Catch:{ all -> 0x000c }
            java.lang.String r3 = "Callback is null"
            com.ali.user.mobile.log.TLogAdapter.e((java.lang.String) r2, (java.lang.String) r3)     // Catch:{ all -> 0x000c }
            monitor-exit(r1)
            return
        L_0x000c:
            r2 = move-exception
            goto L_0x003d
        L_0x000e:
            r1.mCallback = r2     // Catch:{ all -> 0x000c }
            java.lang.Class<com.ali.user.mobile.service.UIService> r3 = com.ali.user.mobile.service.UIService.class
            java.lang.Object r3 = com.ali.user.mobile.service.ServiceFactory.getService(r3)     // Catch:{ all -> 0x000c }
            com.ali.user.mobile.service.UIService r3 = (com.ali.user.mobile.service.UIService) r3     // Catch:{ all -> 0x000c }
            android.content.Context r0 = r1.mContext     // Catch:{ all -> 0x000c }
            boolean r3 = r3.isWebViewActivity(r0)     // Catch:{ all -> 0x000c }
            if (r3 == 0) goto L_0x002e
            android.taobao.windvane.jsbridge.WVResult r3 = new android.taobao.windvane.jsbridge.WVResult     // Catch:{ all -> 0x000c }
            r3.<init>()     // Catch:{ all -> 0x000c }
            java.lang.String r0 = "HY_SUCCESS"
            r3.setResult(r0)     // Catch:{ all -> 0x000c }
            r2.success((android.taobao.windvane.jsbridge.WVResult) r3)     // Catch:{ all -> 0x000c }
            goto L_0x003b
        L_0x002e:
            android.taobao.windvane.jsbridge.WVResult r3 = new android.taobao.windvane.jsbridge.WVResult     // Catch:{ all -> 0x000c }
            r3.<init>()     // Catch:{ all -> 0x000c }
            java.lang.String r0 = "HY_FAILED"
            r3.setResult(r0)     // Catch:{ all -> 0x000c }
            r2.error((android.taobao.windvane.jsbridge.WVResult) r3)     // Catch:{ all -> 0x000c }
        L_0x003b:
            monitor-exit(r1)
            return
        L_0x003d:
            monitor-exit(r1)
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.login4android.jsbridge.JSBridgeService.isMemberSDK(android.taobao.windvane.jsbridge.WVCallBackContext, java.lang.String):void");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(4:13|14|15|16) */
    /* JADX WARNING: Code restructure failed: missing block: B:14:?, code lost:
        setErrorCallback(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0030, code lost:
        return;
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x002c */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private synchronized void getWuaData(android.taobao.windvane.jsbridge.WVCallBackContext r3, java.lang.String r4) {
        /*
            r2 = this;
            monitor-enter(r2)
            if (r3 != 0) goto L_0x000e
            java.lang.String r3 = r2.Tag     // Catch:{ all -> 0x000c }
            java.lang.String r4 = "Callback is null"
            com.ali.user.mobile.log.TLogAdapter.e((java.lang.String) r3, (java.lang.String) r4)     // Catch:{ all -> 0x000c }
            monitor-exit(r2)
            return
        L_0x000c:
            r3 = move-exception
            goto L_0x0031
        L_0x000e:
            r2.mCallback = r3     // Catch:{ all -> 0x000c }
            com.ali.user.mobile.rpc.login.model.WUAData r4 = com.ali.user.mobile.security.SecurityGuardManagerWraper.getWUA()     // Catch:{ Exception -> 0x002c }
            android.taobao.windvane.jsbridge.WVResult r0 = new android.taobao.windvane.jsbridge.WVResult     // Catch:{ Exception -> 0x002c }
            r0.<init>()     // Catch:{ Exception -> 0x002c }
            java.lang.String r1 = "HY_SUCCESS"
            r0.setResult(r1)     // Catch:{ Exception -> 0x002c }
            java.lang.String r1 = "wua"
            java.lang.String r4 = com.alibaba.fastjson.JSON.toJSONString(r4)     // Catch:{ Exception -> 0x002c }
            r0.addData((java.lang.String) r1, (java.lang.String) r4)     // Catch:{ Exception -> 0x002c }
            r3.success((android.taobao.windvane.jsbridge.WVResult) r0)     // Catch:{ Exception -> 0x002c }
            monitor-exit(r2)
            return
        L_0x002c:
            r2.setErrorCallback(r3)     // Catch:{ all -> 0x000c }
            monitor-exit(r2)
            return
        L_0x0031:
            monitor-exit(r2)
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.login4android.jsbridge.JSBridgeService.getWuaData(android.taobao.windvane.jsbridge.WVCallBackContext, java.lang.String):void");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(2:18|19) */
    /* JADX WARNING: Code restructure failed: missing block: B:19:?, code lost:
        setErrorCallback(r3);
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:18:0x005b */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private synchronized void closeNaviBar(android.taobao.windvane.jsbridge.WVCallBackContext r3, java.lang.String r4) {
        /*
            r2 = this;
            monitor-enter(r2)
            if (r3 != 0) goto L_0x000e
            java.lang.String r3 = r2.Tag     // Catch:{ all -> 0x000c }
            java.lang.String r4 = "Callback is null"
            com.ali.user.mobile.log.TLogAdapter.e((java.lang.String) r3, (java.lang.String) r4)     // Catch:{ all -> 0x000c }
            monitor-exit(r2)
            return
        L_0x000c:
            r3 = move-exception
            goto L_0x0064
        L_0x000e:
            r2.mCallback = r3     // Catch:{ all -> 0x000c }
            boolean r0 = android.text.TextUtils.isEmpty(r4)     // Catch:{ all -> 0x000c }
            if (r0 != 0) goto L_0x005f
            org.json.JSONObject r0 = new org.json.JSONObject     // Catch:{ Exception -> 0x005b }
            r0.<init>(r4)     // Catch:{ Exception -> 0x005b }
            java.lang.String r4 = "hidden"
            java.lang.Object r4 = r0.get(r4)     // Catch:{ Exception -> 0x005b }
            java.lang.String r4 = (java.lang.String) r4     // Catch:{ Exception -> 0x005b }
            java.lang.String r0 = "1"
            boolean r0 = r0.equals(r4)     // Catch:{ Exception -> 0x005b }
            if (r0 == 0) goto L_0x003d
            java.lang.Class<com.ali.user.mobile.service.UIService> r4 = com.ali.user.mobile.service.UIService.class
            java.lang.Object r4 = com.ali.user.mobile.service.ServiceFactory.getService(r4)     // Catch:{ Exception -> 0x005b }
            com.ali.user.mobile.service.UIService r4 = (com.ali.user.mobile.service.UIService) r4     // Catch:{ Exception -> 0x005b }
            android.content.Context r0 = r2.mContext     // Catch:{ Exception -> 0x005b }
            r1 = 0
            r4.setWebViewTitleBarVisibility(r0, r1)     // Catch:{ Exception -> 0x005b }
            r3.success()     // Catch:{ Exception -> 0x005b }
            goto L_0x0062
        L_0x003d:
            java.lang.String r0 = "0"
            boolean r4 = r0.equals(r4)     // Catch:{ Exception -> 0x005b }
            if (r4 == 0) goto L_0x0057
            java.lang.Class<com.ali.user.mobile.service.UIService> r4 = com.ali.user.mobile.service.UIService.class
            java.lang.Object r4 = com.ali.user.mobile.service.ServiceFactory.getService(r4)     // Catch:{ Exception -> 0x005b }
            com.ali.user.mobile.service.UIService r4 = (com.ali.user.mobile.service.UIService) r4     // Catch:{ Exception -> 0x005b }
            android.content.Context r0 = r2.mContext     // Catch:{ Exception -> 0x005b }
            r1 = 1
            r4.setWebViewTitleBarVisibility(r0, r1)     // Catch:{ Exception -> 0x005b }
            r3.success()     // Catch:{ Exception -> 0x005b }
            goto L_0x0062
        L_0x0057:
            r2.setErrorCallback(r3)     // Catch:{ Exception -> 0x005b }
            goto L_0x0062
        L_0x005b:
            r2.setErrorCallback(r3)     // Catch:{ all -> 0x000c }
            goto L_0x0062
        L_0x005f:
            r2.setErrorCallback(r3)     // Catch:{ all -> 0x000c }
        L_0x0062:
            monitor-exit(r2)
            return
        L_0x0064:
            monitor-exit(r2)
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.login4android.jsbridge.JSBridgeService.closeNaviBar(android.taobao.windvane.jsbridge.WVCallBackContext, java.lang.String):void");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(6:13|14|15|16|17|18) */
    /* JADX WARNING: Can't wrap try/catch for region: R(6:7|8|9|10|11|12) */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0039, code lost:
        return;
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x001b */
    /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x0032 */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:17:0x0032=Splitter:B:17:0x0032, B:11:0x001b=Splitter:B:11:0x001b} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private synchronized void checkLogin(android.taobao.windvane.jsbridge.WVCallBackContext r4, java.lang.String r5) {
        /*
            r3 = this;
            monitor-enter(r3)
            if (r4 != 0) goto L_0x0005
            monitor-exit(r3)
            return
        L_0x0005:
            boolean r5 = com.taobao.login4android.Login.checkSessionValid()     // Catch:{ all -> 0x003a }
            if (r5 == 0) goto L_0x0022
            android.taobao.windvane.jsbridge.WVResult r5 = new android.taobao.windvane.jsbridge.WVResult     // Catch:{ all -> 0x003a }
            r5.<init>()     // Catch:{ all -> 0x003a }
            org.json.JSONObject r0 = new org.json.JSONObject     // Catch:{ all -> 0x003a }
            r0.<init>()     // Catch:{ all -> 0x003a }
            java.lang.String r1 = "isLogin"
            r2 = 1
            r0.put(r1, r2)     // Catch:{ Exception -> 0x001b }
        L_0x001b:
            r5.setData(r0)     // Catch:{ all -> 0x003a }
            r4.success((android.taobao.windvane.jsbridge.WVResult) r5)     // Catch:{ all -> 0x003a }
            goto L_0x0038
        L_0x0022:
            android.taobao.windvane.jsbridge.WVResult r5 = new android.taobao.windvane.jsbridge.WVResult     // Catch:{ all -> 0x003a }
            r5.<init>()     // Catch:{ all -> 0x003a }
            org.json.JSONObject r0 = new org.json.JSONObject     // Catch:{ all -> 0x003a }
            r0.<init>()     // Catch:{ all -> 0x003a }
            java.lang.String r1 = "isLogin"
            r2 = 0
            r0.put(r1, r2)     // Catch:{ Exception -> 0x0032 }
        L_0x0032:
            r5.setData(r0)     // Catch:{ all -> 0x003a }
            r4.success((android.taobao.windvane.jsbridge.WVResult) r5)     // Catch:{ all -> 0x003a }
        L_0x0038:
            monitor-exit(r3)
            return
        L_0x003a:
            r4 = move-exception
            monitor-exit(r3)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.login4android.jsbridge.JSBridgeService.checkLogin(android.taobao.windvane.jsbridge.WVCallBackContext, java.lang.String):void");
    }

    private void setBackFinish(WVCallBackContext wVCallBackContext, String str) {
        if (wVCallBackContext == null) {
            TLogAdapter.e(this.Tag, "callback is null");
        }
    }

    private synchronized void refreshAlipayCookieWithRemoteBiz(final WVCallBackContext wVCallBackContext, String str) {
        if (wVCallBackContext == null) {
            TLogAdapter.e(this.Tag, "callback is null");
        } else if (TextUtils.isEmpty(str)) {
            setErrorCallback(wVCallBackContext);
        } else {
            try {
                new org.json.JSONObject(str);
                RpcRequest rpcRequest = new RpcRequest();
                rpcRequest.API_NAME = ApiConstants.ApiName.GET_ALIPAY_COOKIES;
                rpcRequest.VERSION = "1.0";
                rpcRequest.NEED_SESSION = true;
                rpcRequest.NEED_ECODE = true;
                rpcRequest.addParam(ApiConstants.ApiField.UMID_TOKEN, AppInfo.getInstance().getUmidToken());
                rpcRequest.addParam(ApiConstants.ApiField.EXT, str);
                rpcRequest.requestSite = DataProviderFactory.getDataProvider().getSite();
                ((RpcService) ServiceFactory.getService(RpcService.class)).remoteBusiness(rpcRequest, GetAlipayCookiesResponseData.class, (RpcRequestCallbackWithCode) new RpcRequestCallbackWithCode() {
                    public void onSuccess(RpcResponse rpcResponse) {
                        if (rpcResponse == null) {
                            JSBridgeService.this.failCallback(wVCallBackContext, "mtop response=null", "-1");
                        } else if (rpcResponse instanceof GetAlipayCookiesResponseData) {
                            GetAlipayCookiesResponseData getAlipayCookiesResponseData = (GetAlipayCookiesResponseData) rpcResponse;
                            if (getAlipayCookiesResponseData.returnValue == null || getAlipayCookiesResponseData.returnValue.length <= 0) {
                                JSBridgeService.this.failCallback(wVCallBackContext, "mtop response=null", "-1");
                                return;
                            }
                            Login.session.injectExternalCookies(getAlipayCookiesResponseData.returnValue);
                            WVResult wVResult = new WVResult();
                            wVResult.setResult(WVResult.SUCCESS);
                            wVCallBackContext.success(wVResult);
                        } else {
                            JSBridgeService.this.failCallback(wVCallBackContext, "mtop response=null", "-1");
                        }
                    }

                    public void onSystemError(String str, RpcResponse rpcResponse) {
                        JSBridgeService.this.failCallback(wVCallBackContext, str, "-1");
                    }

                    public void onError(String str, RpcResponse rpcResponse) {
                        String str2 = "-1";
                        if (rpcResponse != null) {
                            str2 = String.valueOf(rpcResponse.code);
                        }
                        JSBridgeService.this.failCallback(wVCallBackContext, str, str2);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                setErrorCallback(wVCallBackContext);
            }
        }
    }

    /* access modifiers changed from: private */
    public void failCallback(WVCallBackContext wVCallBackContext, String str, String str2) {
        WVResult wVResult = new WVResult();
        org.json.JSONObject jSONObject = new org.json.JSONObject();
        try {
            jSONObject.put("message", str);
            jSONObject.put("code", str2);
        } catch (Exception unused) {
        }
        wVResult.setData(jSONObject);
        wVResult.setResult("HY_FAILED");
        wVCallBackContext.error(wVResult);
    }

    private synchronized void refreshAlipayCookie(final WVCallBackContext wVCallBackContext, String str) {
        if (wVCallBackContext == null) {
            TLogAdapter.e(this.Tag, "Callback is null");
        } else if (!TextUtils.isEmpty(str)) {
            try {
                new AsyncTask<Void, Void, GetAlipayCookiesResponseData>() {
                    public GetAlipayCookiesResponseData doInBackground(Void... voidArr) {
                        return Login.refreshAlipayCookie();
                    }

                    /* access modifiers changed from: protected */
                    public void onPostExecute(GetAlipayCookiesResponseData getAlipayCookiesResponseData) {
                        JSBridgeService.this.handleServerResponse(getAlipayCookiesResponseData, wVCallBackContext);
                    }
                }.execute(new Void[0]);
            } catch (Exception e) {
                e.printStackTrace();
                setErrorCallback(wVCallBackContext);
            }
        } else {
            setErrorCallback(wVCallBackContext);
        }
    }

    /* access modifiers changed from: private */
    public void handleServerResponse(GetAlipayCookiesResponseData getAlipayCookiesResponseData, WVCallBackContext wVCallBackContext) {
        if (getAlipayCookiesResponseData == null) {
            WVResult wVResult = new WVResult();
            org.json.JSONObject jSONObject = new org.json.JSONObject();
            try {
                if (!Login.checkSessionValid()) {
                    jSONObject.put("message", "-1");
                    jSONObject.put("code", String.valueOf(-1));
                } else {
                    jSONObject.put("message", "-1");
                    jSONObject.put("code", String.valueOf(-1));
                }
            } catch (Exception unused) {
            }
            wVResult.setData(jSONObject);
            wVCallBackContext.error(wVResult);
        } else if (getAlipayCookiesResponseData == null) {
            AppMonitorAdapter.commitFail("Page_Member_Other", "RefreshAlipayCookie", "0", "");
            failCallback(wVCallBackContext, "-1", "-1");
        } else if (getAlipayCookiesResponseData.returnValue == null || getAlipayCookiesResponseData.returnValue.length <= 0) {
            AppMonitorAdapter.commitFail("Page_Member_Other", "RefreshAlipayCookie", "0", String.valueOf(getAlipayCookiesResponseData.code));
            WVResult wVResult2 = new WVResult();
            if (getAlipayCookiesResponseData.message != null) {
                org.json.JSONObject jSONObject2 = new org.json.JSONObject();
                try {
                    jSONObject2.put("message", String.valueOf(getAlipayCookiesResponseData.code));
                    jSONObject2.put("code", String.valueOf(getAlipayCookiesResponseData.code));
                } catch (Exception unused2) {
                }
                wVResult2.setData(jSONObject2);
            }
            wVResult2.setResult("HY_FAILED");
            wVCallBackContext.error(wVResult2);
        } else {
            AppMonitorAdapter.commitSuccess("Page_Member_Other", "RefreshAlipayCookie");
            Login.session.injectExternalCookies(getAlipayCookiesResponseData.returnValue);
            WVResult wVResult3 = new WVResult();
            wVResult3.setResult(WVResult.SUCCESS);
            wVCallBackContext.success(wVResult3);
        }
    }

    private synchronized void mockLogin(WVCallBackContext wVCallBackContext, String str) {
        if (wVCallBackContext == null) {
            TLogAdapter.e(this.Tag, "Callback is null");
        } else if (!TextUtils.isEmpty(str)) {
            try {
                org.json.JSONObject jSONObject = new org.json.JSONObject(str);
                this.loginCls = Class.forName("com.taobao.login4android.Login");
                this.loginMethod = this.loginCls.getDeclaredMethod("login", new Class[]{Boolean.TYPE, Bundle.class});
                Bundle bundle = new Bundle();
                bundle.putBoolean("easylogin2", true);
                bundle.putString("username", (String) jSONObject.opt("username"));
                bundle.putString("password", (String) jSONObject.opt("password"));
                invokeMethod(this.loginMethod, true, bundle);
            } catch (Exception e) {
                e.printStackTrace();
                setErrorCallback(wVCallBackContext);
            }
        } else {
            setErrorCallback(wVCallBackContext);
        }
    }

    private void setErrorCallback(WVCallBackContext wVCallBackContext) {
        WVResult wVResult = new WVResult();
        wVResult.setResult("HY_PARAM_ERR");
        wVCallBackContext.error(wVResult);
    }

    private <T> T invokeMethod(Method method, Object... objArr) {
        if (method == null) {
            return null;
        }
        try {
            return method.invoke(this.loginCls, objArr);
        } catch (Exception e) {
            TLogAdapter.e(this.Tag, "invokeMethod error", e);
            return null;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0024, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void closeWebViewByUrl(android.taobao.windvane.jsbridge.WVCallBackContext r2, java.lang.String r3) {
        /*
            r1 = this;
            monitor-enter(r1)
            if (r2 != 0) goto L_0x000e
            java.lang.String r2 = r1.Tag     // Catch:{ all -> 0x000c }
            java.lang.String r3 = "Callback is null"
            com.ali.user.mobile.log.TLogAdapter.e((java.lang.String) r2, (java.lang.String) r3)     // Catch:{ all -> 0x000c }
            monitor-exit(r1)
            return
        L_0x000c:
            r2 = move-exception
            goto L_0x0025
        L_0x000e:
            r1.mCallback = r2     // Catch:{ all -> 0x000c }
            java.lang.Class<com.ali.user.mobile.service.UIService> r3 = com.ali.user.mobile.service.UIService.class
            java.lang.Object r3 = com.ali.user.mobile.service.ServiceFactory.getService(r3)     // Catch:{ all -> 0x000c }
            com.ali.user.mobile.service.UIService r3 = (com.ali.user.mobile.service.UIService) r3     // Catch:{ all -> 0x000c }
            android.content.Context r0 = r1.mContext     // Catch:{ all -> 0x000c }
            boolean r3 = r3.finishWebViewActivity(r0)     // Catch:{ all -> 0x000c }
            if (r3 != 0) goto L_0x0023
            r1.setErrorCallback(r2)     // Catch:{ all -> 0x000c }
        L_0x0023:
            monitor-exit(r1)
            return
        L_0x0025:
            monitor-exit(r1)
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.login4android.jsbridge.JSBridgeService.closeWebViewByUrl(android.taobao.windvane.jsbridge.WVCallBackContext, java.lang.String):void");
    }

    public synchronized void openWebViewByUrl(WVCallBackContext wVCallBackContext, String str) {
        if (wVCallBackContext == null) {
            try {
                TLogAdapter.e(this.Tag, "Callback is null");
            } catch (Exception e) {
                e.printStackTrace();
                setErrorCallback(wVCallBackContext);
            } catch (Throwable th) {
                throw th;
            }
        }
        this.mCallback = wVCallBackContext;
        if (!TextUtils.isEmpty(str)) {
            UrlParam urlParam = new UrlParam();
            urlParam.scene = "";
            urlParam.url = (String) new org.json.JSONObject(str).get("url");
            urlParam.site = DataProviderFactory.getDataProvider().getSite();
            ((NavigatorService) ServiceFactory.getService(NavigatorService.class)).openWebViewPage(this.mContext, urlParam);
            WVResult wVResult = new WVResult();
            wVResult.setResult("success !!!");
            wVCallBackContext.success(wVResult);
        } else {
            setErrorCallback(wVCallBackContext);
        }
    }

    public synchronized void popup(WVCallBackContext wVCallBackContext, String str) {
    }

    public synchronized void getUmid(WVCallBackContext wVCallBackContext, String str) {
        getUMID(wVCallBackContext, str);
    }

    public void getUMID(WVCallBackContext wVCallBackContext, String str) {
        this.mCallback = wVCallBackContext;
        if (!TextUtils.isEmpty(str)) {
            try {
                WVResult wVResult = new WVResult();
                wVResult.setResult(WVResult.SUCCESS);
                wVResult.addData("aluUmid", AppInfo.getInstance().getUmidToken());
                wVCallBackContext.success(wVResult);
            } catch (Exception unused) {
                setErrorCallback(wVCallBackContext);
            } catch (Throwable unused2) {
                setErrorCallback(wVCallBackContext);
            }
        }
    }

    public void getAppKey(WVCallBackContext wVCallBackContext, String str) {
        this.mCallback = wVCallBackContext;
        if (!TextUtils.isEmpty(str)) {
            try {
                WVResult wVResult = new WVResult();
                wVResult.setResult(WVResult.SUCCESS);
                wVResult.addData("aluAppKey", DataProviderFactory.getDataProvider().getAppkey());
                wVCallBackContext.success(wVResult);
            } catch (Exception unused) {
                setErrorCallback(wVCallBackContext);
            }
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(4:11|12|13|14) */
    /* JADX WARNING: Code restructure failed: missing block: B:12:?, code lost:
        setErrorCallback(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x002e, code lost:
        return;
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x002a */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private synchronized void getSDKVersion(android.taobao.windvane.jsbridge.WVCallBackContext r3, java.lang.String r4) {
        /*
            r2 = this;
            monitor-enter(r2)
            if (r3 != 0) goto L_0x000e
            java.lang.String r3 = r2.Tag     // Catch:{ all -> 0x000c }
            java.lang.String r4 = "Callback is null"
            com.ali.user.mobile.log.TLogAdapter.e((java.lang.String) r3, (java.lang.String) r4)     // Catch:{ all -> 0x000c }
            monitor-exit(r2)
            return
        L_0x000c:
            r3 = move-exception
            goto L_0x002f
        L_0x000e:
            android.taobao.windvane.jsbridge.WVResult r4 = new android.taobao.windvane.jsbridge.WVResult     // Catch:{ Exception -> 0x002a }
            r4.<init>()     // Catch:{ Exception -> 0x002a }
            java.lang.String r0 = "HY_SUCCESS"
            r4.setResult(r0)     // Catch:{ Exception -> 0x002a }
            java.lang.String r0 = "sdkVersion"
            com.ali.user.mobile.info.AppInfo r1 = com.ali.user.mobile.info.AppInfo.getInstance()     // Catch:{ Exception -> 0x002a }
            java.lang.String r1 = r1.getSdkVersion()     // Catch:{ Exception -> 0x002a }
            r4.addData((java.lang.String) r0, (java.lang.String) r1)     // Catch:{ Exception -> 0x002a }
            r3.success((android.taobao.windvane.jsbridge.WVResult) r4)     // Catch:{ Exception -> 0x002a }
            monitor-exit(r2)
            return
        L_0x002a:
            r2.setErrorCallback(r3)     // Catch:{ all -> 0x000c }
            monitor-exit(r2)
            return
        L_0x002f:
            monitor-exit(r2)
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.login4android.jsbridge.JSBridgeService.getSDKVersion(android.taobao.windvane.jsbridge.WVCallBackContext, java.lang.String):void");
    }

    private synchronized void getAtlasSign(WVCallBackContext wVCallBackContext, String str) {
        if (wVCallBackContext == null) {
            TLogAdapter.e(this.Tag, "Callback is null");
            return;
        }
        if (!TextUtils.isEmpty(str)) {
            try {
                String string = new org.json.JSONObject(str).getString("data");
                if (!TextUtils.isEmpty(string) && string.length() < 64) {
                    SSOSecurityService.getInstace(DataProviderFactory.getApplicationContext());
                    String sign = SSOSecurityService.sign(DataProviderFactory.getDataProvider().getAppkey(), string);
                    if (!TextUtils.isEmpty(sign)) {
                        WVResult wVResult = new WVResult();
                        wVResult.setResult(WVResult.SUCCESS);
                        wVResult.addData("signedData", sign);
                        wVResult.addData("appKey", DataProviderFactory.getDataProvider().getAppkey());
                        wVCallBackContext.success(wVResult);
                        return;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        setErrorCallback(wVCallBackContext);
    }

    private synchronized void getSign(WVCallBackContext wVCallBackContext, String str) {
        if (wVCallBackContext == null) {
            TLogAdapter.e(this.Tag, "Callback is null");
            return;
        }
        if (!TextUtils.isEmpty(str)) {
            try {
                String string = new org.json.JSONObject(str).getString("data");
                String userId = Login.getUserId();
                if (!TextUtils.isEmpty(userId) && !TextUtils.isEmpty(string) && string.length() < 128) {
                    HistoryAccount findHistoryAccount = SecurityGuardManagerWraper.findHistoryAccount(Long.parseLong(userId));
                    String sign = AlibabaSecurityTokenService.sign(findHistoryAccount.tokenKey, string);
                    if (!TextUtils.isEmpty(sign)) {
                        WVResult wVResult = new WVResult();
                        wVResult.setResult(WVResult.SUCCESS);
                        wVResult.addData("signedData", sign);
                        wVResult.addData("tokenKey", findHistoryAccount.tokenKey);
                        wVCallBackContext.success(wVResult);
                        return;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        setErrorCallback(wVCallBackContext);
    }

    /* JADX WARNING: Removed duplicated region for block: B:24:0x003c  */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x0045 A[SYNTHETIC, Splitter:B:27:0x0045] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private synchronized void jumpToTmallWithLoginToken(android.taobao.windvane.jsbridge.WVCallBackContext r5, java.lang.String r6) {
        /*
            r4 = this;
            monitor-enter(r4)
            if (r5 != 0) goto L_0x000e
            java.lang.String r5 = r4.Tag     // Catch:{ all -> 0x000c }
            java.lang.String r6 = "Callback is null"
            com.ali.user.mobile.log.TLogAdapter.e((java.lang.String) r5, (java.lang.String) r6)     // Catch:{ all -> 0x000c }
            monitor-exit(r4)
            return
        L_0x000c:
            r5 = move-exception
            goto L_0x005b
        L_0x000e:
            java.lang.String r0 = "false"
            boolean r1 = android.text.TextUtils.isEmpty(r6)     // Catch:{ all -> 0x000c }
            r2 = 0
            if (r1 != 0) goto L_0x002e
            org.json.JSONObject r1 = new org.json.JSONObject     // Catch:{ Exception -> 0x002e }
            r1.<init>(r6)     // Catch:{ Exception -> 0x002e }
            java.lang.String r6 = "onlyLaunch"
            java.lang.Object r6 = r1.get(r6)     // Catch:{ Exception -> 0x002e }
            java.lang.String r6 = (java.lang.String) r6     // Catch:{ Exception -> 0x002e }
            java.lang.String r0 = "targetPage"
            java.lang.Object r0 = r1.get(r0)     // Catch:{ Exception -> 0x002d }
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ Exception -> 0x002d }
            goto L_0x0030
        L_0x002d:
            r0 = r6
        L_0x002e:
            r6 = r0
            r0 = r2
        L_0x0030:
            com.taobao.login4android.login.TmallSsoLogin r1 = com.taobao.login4android.login.TmallSsoLogin.getInstance()     // Catch:{ all -> 0x000c }
            android.content.Context r3 = r4.mContext     // Catch:{ all -> 0x000c }
            boolean r1 = r1.isSupportTmall(r3)     // Catch:{ all -> 0x000c }
            if (r1 != 0) goto L_0x0045
            java.lang.String r6 = "tmall not support"
            java.lang.String r0 = "-2"
            r4.failCallback(r5, r6, r0)     // Catch:{ all -> 0x000c }
            monitor-exit(r4)
            return
        L_0x0045:
            boolean r1 = android.text.TextUtils.isEmpty(r0)     // Catch:{ all -> 0x000c }
            if (r1 == 0) goto L_0x0054
            java.lang.String r6 = "param invalid"
            java.lang.String r0 = "-1"
            r4.failCallback(r5, r6, r0)     // Catch:{ all -> 0x000c }
            monitor-exit(r4)
            return
        L_0x0054:
            android.content.Context r5 = r4.mContext     // Catch:{ all -> 0x000c }
            goTmall(r5, r6, r0, r2)     // Catch:{ all -> 0x000c }
            monitor-exit(r4)
            return
        L_0x005b:
            monitor-exit(r4)
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.login4android.jsbridge.JSBridgeService.jumpToTmallWithLoginToken(android.taobao.windvane.jsbridge.WVCallBackContext, java.lang.String):void");
    }

    public static void goTmall(final Context context, String str, final String str2, final CommonCallback commonCallback) {
        if (!TmallSsoLogin.getInstance().isSupportTmall(context)) {
            if (commonCallback != null) {
                commonCallback.onFail(-2, "tmall not support");
            }
        } else if (TextUtils.equals("true", str)) {
            TmallSsoLogin.getInstance().launchTMall(context, "", str2, true);
            if (commonCallback != null) {
                commonCallback.onSuccess();
            }
        } else {
            HashMap hashMap = new HashMap();
            hashMap.put("source", "tb");
            LoginController.getInstance().applyTokenWithRemoteBiz(DataProviderFactory.getDataProvider().getSite(), Login.getUserId(), hashMap, false, new InternalTokenCallback() {
                public void onSucess(final String str) {
                    MainThreadExecutor.execute(new Runnable() {
                        public void run() {
                            UserTrackAdapter.sendUT("Page_JumpLogin", "GetTokenSuccess");
                            TmallSsoLogin.getInstance().launchTMall(context, str, str2, false);
                            if (commonCallback != null) {
                                commonCallback.onSuccess();
                            }
                        }
                    });
                }

                public void onFail(String str, String str2) {
                    MainThreadExecutor.execute(new Runnable() {
                        public void run() {
                            UserTrackAdapter.sendUT("Page_JumpLogin", "GetTokenFail");
                            if (commonCallback != null) {
                                commonCallback.onSuccess();
                            }
                            TmallSsoLogin.getInstance().launchTMall(context, "", str2, true);
                        }
                    });
                }
            });
        }
    }

    private void refreshYoukuOpenSid(final WVCallBackContext wVCallBackContext, String str) {
        if (wVCallBackContext == null) {
            TLogAdapter.e(this.Tag, "callback is null");
        } else if (TextUtils.isEmpty(str)) {
            setErrorCallback(wVCallBackContext);
        } else {
            RpcRequest rpcRequest = new RpcRequest();
            rpcRequest.API_NAME = ApiConstants.ApiName.GET_YOUKU_OPENSID_COOKIES;
            rpcRequest.VERSION = "1.0";
            rpcRequest.NEED_SESSION = true;
            rpcRequest.NEED_ECODE = true;
            rpcRequest.addParam(ApiConstants.ApiField.UMID_TOKEN, AppInfo.getInstance().getUmidToken());
            rpcRequest.addParam(ApiConstants.ApiField.RISK_CONTROL_INFO, JSON.toJSONString(SecurityGuardManagerWraper.buildWSecurityData()));
            rpcRequest.requestSite = DataProviderFactory.getDataProvider().getSite();
            ((RpcService) ServiceFactory.getService(RpcService.class)).remoteBusiness(rpcRequest, GetYoukuOpenSidResponseData.class, (RpcRequestCallbackWithCode) new RpcRequestCallbackWithCode() {
                public void onSuccess(RpcResponse rpcResponse) {
                    if (rpcResponse == null) {
                        JSBridgeService.this.failCallback(wVCallBackContext, "mtop response=null", "-1");
                    } else if (rpcResponse instanceof GetYoukuOpenSidResponseData) {
                        GetYoukuOpenSidResponseData getYoukuOpenSidResponseData = (GetYoukuOpenSidResponseData) rpcResponse;
                        if (getYoukuOpenSidResponseData.returnValue == null || getYoukuOpenSidResponseData.returnValue.length <= 0) {
                            JSBridgeService.this.failCallback(wVCallBackContext, "mtop response=null", "-1");
                            return;
                        }
                        Login.session.injectExternalCookies(getYoukuOpenSidResponseData.returnValue);
                        WVResult wVResult = new WVResult();
                        wVResult.setResult(WVResult.SUCCESS);
                        wVCallBackContext.success(wVResult);
                    } else {
                        JSBridgeService.this.failCallback(wVCallBackContext, "mtop response=null", "-1");
                    }
                }

                public void onSystemError(String str, RpcResponse rpcResponse) {
                    JSBridgeService.this.failCallback(wVCallBackContext, str, "-1");
                }

                public void onError(String str, RpcResponse rpcResponse) {
                    String str2 = "-1";
                    if (rpcResponse != null) {
                        str2 = String.valueOf(rpcResponse.code);
                    }
                    JSBridgeService.this.failCallback(wVCallBackContext, str, str2);
                }
            });
        }
    }
}
