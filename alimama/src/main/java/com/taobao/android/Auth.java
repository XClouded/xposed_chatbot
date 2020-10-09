package com.taobao.android;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.TextUtils;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.app.init.Debuggable;
import com.ali.user.mobile.app.login.dataprovider.TaobaoAppProvider;
import com.ali.user.mobile.coordinator.CoordinatorWrapper;
import com.ali.user.mobile.info.AlipayInfo;
import com.ali.user.mobile.info.AppInfo;
import com.ali.user.mobile.log.TLogAdapter;
import com.ali.user.mobile.log.UserTrackAdapter;
import com.ali.user.mobile.utils.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.taobao.android.sso.v2.launch.ILoginListener;
import com.taobao.android.sso.v2.launch.Platform;
import com.taobao.android.sso.v2.launch.SsoLogin;
import com.taobao.android.sso.v2.launch.exception.SSOException;
import com.taobao.android.sso.v2.launch.model.ISsoRemoteParam;
import com.taobao.login4android.constants.LoginEnvType;
import com.taobao.login4android.constants.LoginUrlConstants;
import com.taobao.login4android.log.LoginTLogAdapter;
import com.taobao.login4android.session.ISession;
import com.taobao.login4android.session.constants.SessionConstants;
import com.taobao.login4android.thread.LoginAsyncTask;
import com.taobao.weex.BuildConfig;
import java.util.Properties;
import java.util.regex.Pattern;

public class Auth {
    public static final String TAG = "Login.Auth";
    private static final Object lock = new Object();
    private static AsyncTask loginTask;
    private static transient Pattern[] mLoginPatterns;
    private static transient Pattern[] mLogoutPatterns;
    private static BroadcastReceiver mReceiver;
    public static ISession session;

    public static void init(Context context, String str, String str2, LoginEnvType loginEnvType, TaobaoAppProvider taobaoAppProvider) {
        init(context, str, str2, loginEnvType, taobaoAppProvider, loginEnvType.getSdkEnvType() == 1);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:30:0x00a2, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static synchronized void init(android.content.Context r4, java.lang.String r5, java.lang.String r6, com.taobao.login4android.constants.LoginEnvType r7, final com.ali.user.mobile.app.login.dataprovider.TaobaoAppProvider r8, boolean r9) {
        /*
            java.lang.Class<com.taobao.android.Auth> r0 = com.taobao.android.Auth.class
            monitor-enter(r0)
            java.lang.String r1 = "LoginAPI_Init"
            com.ali.user.mobile.log.UserTrackAdapter.sendUT((java.lang.String) r1)     // Catch:{ all -> 0x00a3 }
            com.ali.user.mobile.app.init.Debuggable.init(r4)     // Catch:{ all -> 0x00a3 }
            com.ali.user.mobile.app.dataprovider.IDataProvider r1 = com.ali.user.mobile.app.dataprovider.DataProviderFactory.getDataProvider()     // Catch:{ all -> 0x00a3 }
            if (r1 == 0) goto L_0x0028
            com.ali.user.mobile.app.dataprovider.IDataProvider r1 = com.ali.user.mobile.app.dataprovider.DataProviderFactory.getDataProvider()     // Catch:{ all -> 0x00a3 }
            boolean r1 = r1 instanceof com.ali.user.mobile.app.login.dataprovider.TaobaoAppProvider     // Catch:{ all -> 0x00a3 }
            if (r1 == 0) goto L_0x0028
            android.content.Context r1 = com.ali.user.mobile.app.dataprovider.DataProviderFactory.getApplicationContext()     // Catch:{ all -> 0x00a3 }
            if (r1 == 0) goto L_0x0028
            java.lang.String r4 = "Login.Auth"
            java.lang.String r5 = "Login has inited, discard current request."
            com.ali.user.mobile.log.TLogAdapter.d(r4, r5)     // Catch:{ all -> 0x00a3 }
            monitor-exit(r0)
            return
        L_0x0028:
            java.lang.String r1 = "Login.Auth"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x00a3 }
            r2.<init>()     // Catch:{ all -> 0x00a3 }
            java.lang.String r3 = "start Login init"
            r2.append(r3)     // Catch:{ all -> 0x00a3 }
            com.ali.user.mobile.info.AppInfo r3 = com.ali.user.mobile.info.AppInfo.getInstance()     // Catch:{ all -> 0x00a3 }
            java.lang.String r3 = r3.getAppVersion()     // Catch:{ all -> 0x00a3 }
            r2.append(r3)     // Catch:{ all -> 0x00a3 }
            java.lang.String r2 = r2.toString()     // Catch:{ all -> 0x00a3 }
            com.ali.user.mobile.log.TLogAdapter.e((java.lang.String) r1, (java.lang.String) r2)     // Catch:{ all -> 0x00a3 }
            r8.setContext(r4)     // Catch:{ all -> 0x00a3 }
            r8.setTTID(r5)     // Catch:{ all -> 0x00a3 }
            r8.setProductVersion(r6)     // Catch:{ all -> 0x00a3 }
            int r5 = r7.getSdkEnvType()     // Catch:{ all -> 0x00a3 }
            r8.setEnvType(r5)     // Catch:{ all -> 0x00a3 }
            r8.setAppDebug(r9)     // Catch:{ all -> 0x00a3 }
            com.ali.user.mobile.app.dataprovider.DataProviderFactory.setDataProvider(r8)     // Catch:{ all -> 0x00a3 }
            com.ali.user.mobile.app.init.AliUserInit.initSystemService()     // Catch:{ all -> 0x00a3 }
            java.lang.Class<com.ali.user.mobile.service.StorageService> r5 = com.ali.user.mobile.service.StorageService.class
            java.lang.Object r5 = com.ali.user.mobile.service.ServiceFactory.getService(r5)     // Catch:{ all -> 0x00a3 }
            com.ali.user.mobile.service.StorageService r5 = (com.ali.user.mobile.service.StorageService) r5     // Catch:{ all -> 0x00a3 }
            android.content.Context r6 = r4.getApplicationContext()     // Catch:{ all -> 0x00a3 }
            r5.init(r6)     // Catch:{ all -> 0x00a3 }
            com.taobao.login4android.session.SessionManager r4 = com.taobao.login4android.session.SessionManager.getInstance(r4)     // Catch:{ all -> 0x00a3 }
            session = r4     // Catch:{ all -> 0x00a3 }
            com.ali.user.mobile.coordinator.CoordinatorWrapper r4 = new com.ali.user.mobile.coordinator.CoordinatorWrapper     // Catch:{ all -> 0x00a3 }
            r4.<init>()     // Catch:{ all -> 0x00a3 }
            com.taobao.android.Auth$1 r5 = new com.taobao.android.Auth$1     // Catch:{ all -> 0x00a3 }
            r5.<init>(r8)     // Catch:{ all -> 0x00a3 }
            r4.execute(r5)     // Catch:{ all -> 0x00a3 }
            android.content.BroadcastReceiver r4 = mReceiver     // Catch:{ all -> 0x00a3 }
            if (r4 != 0) goto L_0x00a1
            java.lang.Object r4 = lock     // Catch:{ all -> 0x00a3 }
            monitor-enter(r4)     // Catch:{ all -> 0x00a3 }
            android.content.BroadcastReceiver r5 = mReceiver     // Catch:{ all -> 0x009e }
            if (r5 != 0) goto L_0x009c
            com.taobao.login4android.broadcast.LoginBroadcastReceiver r5 = new com.taobao.login4android.broadcast.LoginBroadcastReceiver     // Catch:{ all -> 0x009e }
            r5.<init>()     // Catch:{ all -> 0x009e }
            mReceiver = r5     // Catch:{ all -> 0x009e }
            android.content.Context r5 = com.ali.user.mobile.app.dataprovider.DataProviderFactory.getApplicationContext()     // Catch:{ all -> 0x009e }
            android.content.BroadcastReceiver r6 = mReceiver     // Catch:{ all -> 0x009e }
            com.taobao.login4android.broadcast.LoginBroadcastHelper.registerLoginReceiver(r5, r6)     // Catch:{ all -> 0x009e }
        L_0x009c:
            monitor-exit(r4)     // Catch:{ all -> 0x009e }
            goto L_0x00a1
        L_0x009e:
            r5 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x009e }
            throw r5     // Catch:{ all -> 0x00a3 }
        L_0x00a1:
            monitor-exit(r0)
            return
        L_0x00a3:
            r4 = move-exception
            monitor-exit(r0)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.Auth.init(android.content.Context, java.lang.String, java.lang.String, com.taobao.login4android.constants.LoginEnvType, com.ali.user.mobile.app.login.dataprovider.TaobaoAppProvider, boolean):void");
    }

    public static void login(boolean z) {
        login(z, (Bundle) null);
    }

    public static void login(final boolean z, final Bundle bundle) {
        UserTrackAdapter.sendUT("AuthAPI_Login");
        TLogAdapter.d(TAG, " start login : showAuthUI " + z);
        loginTask = new LoginAsyncTask() {
            public Object excuteTask(Object[] objArr) throws RemoteException {
                AuthController.getInstance().autoLogin(z, bundle);
                return null;
            }
        };
        new CoordinatorWrapper().execute(loginTask, new Object[0]);
    }

    public static void logout(Context context, int i, String str, String str2, String str3, boolean z) {
        UserTrackAdapter.sendUT("LoginAPI_Logout");
        final int i2 = i;
        final String str4 = str;
        final String str5 = str2;
        final String str6 = str3;
        final boolean z2 = z;
        final Context context2 = context;
        new CoordinatorWrapper().execute(new LoginAsyncTask<Object, Void, Void>() {
            public Void excuteTask(Object... objArr) throws RemoteException {
                AuthController.getInstance().logout(i2, str4, str5, str6, z2);
                if (!Debuggable.isDebug()) {
                    return null;
                }
                LoginTLogAdapter.d(LoginAsyncTask.TAG, "logout finish");
                return null;
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(Void voidR) {
                if (context2 != null) {
                    AuthController.getInstance().openLoginPage(context2, true);
                }
            }
        }, new Object[0]);
    }

    public static void logout(Context context) {
        logout(context, getLoginSite(), getSid(), getLoginToken(), getUserId(), false);
    }

    public static void navByScene(Context context, String str) {
        navByScene(context, str, DataProviderFactory.getDataProvider().getSite());
    }

    public static void navByScene(Context context, String str, int i) {
        Properties properties = new Properties();
        if (!TextUtils.isEmpty(str)) {
            properties.put("scene", str);
        }
        UserTrackAdapter.sendUT("LoginAPI_NavByScene", properties);
        AuthController.getInstance().navToWebViewByScene(context, str, i);
    }

    public static boolean checkSessionValid() {
        if (session != null) {
            return session.checkSessionValid();
        }
        return false;
    }

    public static String getSid() {
        return session != null ? session.getSid() : "";
    }

    public static String getSubSid() {
        return session != null ? session.getSubSid() : "";
    }

    public static String getNick() {
        return session != null ? session.getNick() : "";
    }

    public static String getEcode() {
        return session != null ? session.getEcode() : "";
    }

    @Deprecated
    public static String getUserName() {
        return session != null ? session.getUserName() : "";
    }

    public static String getUserId() {
        return session != null ? session.getUserId() : "";
    }

    public static String getOldUserId() {
        return session != null ? session.getOldUserId() : "";
    }

    public static String getEmail() {
        return session != null ? session.getEmail() : "";
    }

    public static int getLoginSite() {
        if (session != null) {
            return session.getLoginSite();
        }
        return 0;
    }

    public static String getHeadPicLink() {
        return session != null ? session.getHeadPicLink() : "";
    }

    public static String getLoginToken() {
        return session != null ? session.getLoginToken() : "";
    }

    public static String getDisplayNick() {
        return session != null ? session.getDisplayNick() : "";
    }

    public static String getOneTimeToken() {
        return session != null ? session.getOneTimeToken() : "";
    }

    public static String getAlipayLoginId() {
        if (session == null) {
            return "";
        }
        try {
            JSONObject parseObject = JSON.parseObject(session.getExtJson());
            if (parseObject != null) {
                return StringUtil.hideAccount(parseObject.getString(SessionConstants.ALIPAY_LOGIN_ID));
            }
            return "";
        } catch (Throwable th) {
            th.printStackTrace();
            return "";
        }
    }

    public static String getSnsNick() {
        if (session == null) {
            return "";
        }
        try {
            JSONObject parseObject = JSON.parseObject(session.getExtJson());
            if (parseObject != null) {
                return parseObject.getString(SessionConstants.TAOBAO_NICK_NAME);
            }
            return "";
        } catch (Throwable th) {
            th.printStackTrace();
            return "";
        }
    }

    public static String getLoginPhone() {
        if (session == null) {
            return "";
        }
        String loginPhone = session.getLoginPhone();
        if (TextUtils.isEmpty(loginPhone)) {
            return "";
        }
        return StringUtil.hideAccount(loginPhone);
    }

    public static long getHavanaSsoTokenExpiredTime() {
        if (session != null) {
            return session.getHavanaSsoTokenExpiredTime();
        }
        return 0;
    }

    public static boolean getCommentUsed() {
        if (session != null) {
            return session.isCommentTokenUsed();
        }
        return true;
    }

    public static String getExtJson() {
        return session != null ? session.getExtJson() : "";
    }

    public static boolean isSupport(Platform platform) {
        if (platform == null) {
            return false;
        }
        switch (platform) {
            case PLATFORM_ALIPAY:
                return SsoLogin.isSupportAliaySso();
            case PLATFORM_TAOBAO:
                return SsoLogin.isSupportTBSsoV2(DataProviderFactory.getApplicationContext());
            default:
                return SsoLogin.isSupportTBSsoV2(DataProviderFactory.getApplicationContext());
        }
    }

    public static void authorize(Activity activity, Platform platform, String str) throws SSOException {
        if (activity == null || TextUtils.isEmpty(str)) {
            throw new SSOException("param can't be null");
        }
        switch (platform) {
            case PLATFORM_ALIPAY:
                try {
                    SsoLogin.launchAlipay(activity, DataProviderFactory.getDataProvider().getAlipaySsoDesKey(), AlipayInfo.getInstance().getApdidToken(), activity.getPackageName(), str);
                    return;
                } catch (Throwable th) {
                    throw new SSOException(th.getMessage());
                }
            case PLATFORM_TAOBAO:
                launchTao(activity, str);
                return;
            default:
                launchTao(activity, str);
                return;
        }
    }

    private static void launchTao(Activity activity, String str) {
        SsoLogin.launchTao(activity, new ISsoRemoteParam() {
            public String getServerTime() {
                return BuildConfig.buildJavascriptFrameworkVersion;
            }

            public String getTtid() {
                return DataProviderFactory.getDataProvider().getTTID();
            }

            public String getImei() {
                return DataProviderFactory.getDataProvider().getImei();
            }

            public String getImsi() {
                return DataProviderFactory.getDataProvider().getImsi();
            }

            public String getDeviceId() {
                return DataProviderFactory.getDataProvider().getDeviceId();
            }

            public String getAppKey() {
                return DataProviderFactory.getDataProvider().getAppkey();
            }

            public String getApdid() {
                return AlipayInfo.getInstance().getApdid();
            }

            public String getUmidToken() {
                return AppInfo.getInstance().getUmidToken();
            }

            public String getAtlas() {
                return DataProviderFactory.getDataProvider().getEnvType() == LoginEnvType.DEV.getSdkEnvType() ? "daily" : "";
            }
        }, str);
    }

    public static void handleResultIntent(Platform platform, Intent intent, ILoginListener iLoginListener) {
        if (iLoginListener != null) {
            if (intent == null) {
                iLoginListener.onFail(new SSOException((Integer) -1, "intent is null"));
                return;
            }
            switch (platform) {
                case PLATFORM_ALIPAY:
                    SsoLogin.handleAlipaySSOResultIntent(intent, iLoginListener);
                    return;
                case PLATFORM_TAOBAO:
                    SsoLogin.handleResultIntent(iLoginListener, intent);
                    return;
                default:
                    SsoLogin.handleResultIntent(iLoginListener, intent);
                    return;
            }
        }
    }

    private static boolean isLoginUrlInner(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        if (mLoginPatterns == null && !TextUtils.isEmpty(LoginUrlConstants.getLoginUrls())) {
            String[] split = LoginUrlConstants.getLoginUrls().split("[;]");
            mLoginPatterns = new Pattern[split.length];
            int length = mLoginPatterns.length;
            for (int i = 0; i < length; i++) {
                mLoginPatterns[i] = Pattern.compile(split[i]);
            }
        }
        for (Pattern matcher : mLoginPatterns) {
            if (matcher.matcher(str).matches()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isLoginUrl(String str) {
        try {
            return isLoginUrlInner(str);
        } catch (Throwable unused) {
            UserTrackAdapter.sendUT("Event_isLoginUrl_Fail");
            return false;
        }
    }

    private static boolean isLogoutUrlInner(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        if (mLogoutPatterns == null && !TextUtils.isEmpty(LoginUrlConstants.getLogoutUrls())) {
            String[] split = LoginUrlConstants.getLogoutUrls().split("[;]");
            mLogoutPatterns = new Pattern[split.length];
            int length = mLogoutPatterns.length;
            for (int i = 0; i < length; i++) {
                mLogoutPatterns[i] = Pattern.compile(split[i]);
            }
        }
        for (Pattern matcher : mLogoutPatterns) {
            if (matcher.matcher(str).matches()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isLogoutUrl(String str) {
        try {
            return isLogoutUrlInner(str);
        } catch (Throwable unused) {
            UserTrackAdapter.sendUT("Event_isLoginUrl_Fail");
            return false;
        }
    }
}
