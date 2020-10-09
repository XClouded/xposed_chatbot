package com.taobao.login4android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.TextUtils;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.app.init.Debuggable;
import com.ali.user.mobile.coordinator.CoordinatorWrapper;
import com.ali.user.mobile.log.TLogAdapter;
import com.ali.user.mobile.log.UserTrackAdapter;
import com.ali.user.mobile.model.RegistParam;
import com.ali.user.mobile.model.UrlParam;
import com.ali.user.mobile.security.SecurityGuardManagerWraper;
import com.ali.user.mobile.service.MemberCenterService;
import com.ali.user.mobile.service.ServiceFactory;
import com.ali.user.mobile.utils.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.taobao.login4android.biz.alipaysso.AlipayConstant;
import com.taobao.login4android.biz.getAlipayCookies.mtop.GetAlipayCookiesResponseData;
import com.taobao.login4android.biz.getWapCookies.GetAlipayCookiesBusiness;
import com.taobao.login4android.broadcast.LoginAction;
import com.taobao.login4android.constants.LoginConstants;
import com.taobao.login4android.constants.LoginEnvType;
import com.taobao.login4android.constants.LoginStatus;
import com.taobao.login4android.constants.LoginUrlConstants;
import com.taobao.login4android.location.LocationProvider;
import com.taobao.login4android.log.LoginTLogAdapter;
import com.taobao.login4android.login.CheckResultCallback;
import com.taobao.login4android.login.DefaultTaobaoAppProvider;
import com.taobao.login4android.login.InternalTokenCallback;
import com.taobao.login4android.login.LoginController;
import com.taobao.login4android.session.ISession;
import com.taobao.login4android.session.constants.SessionConstants;
import com.taobao.login4android.thread.LoginAsyncTask;
import com.taobao.weex.BuildConfig;
import java.util.Properties;
import java.util.regex.Pattern;

public class Login {
    private static final long COOKIES_REFRESH_INTERVAL = 1800000;
    private static final long COOKIES_REFRESH_SHRINK = 1680000;
    @Deprecated
    private static final long LOGIN_TIMEOUT = 10000;
    private static final String REFRESH_RESULT = "refreshResult";
    public static final String TAG = "login.Login";
    public static Bundle launchBundle;
    private static LocationProvider locationProvider;
    private static final Object lock = new Object();
    private static AsyncTask loginTask;
    private static Object mLock = new Object();
    private static transient Pattern[] mLoginPatterns;
    private static transient Pattern[] mLogoutPatterns;
    private static BroadcastReceiver mReceiver;
    public static ISession session;

    public static void setLaunchBundle(Bundle bundle) {
        TLogAdapter.e(TAG, "setLaunchBundle," + bundle.getString(AlipayConstant.LOGIN_ALIPAY_TOKEN_KEY));
        launchBundle = bundle;
    }

    public static void init(Context context, String str, String str2, LoginEnvType loginEnvType) {
        init(context, str, str2, loginEnvType, new DefaultTaobaoAppProvider(), loginEnvType != null && loginEnvType.getSdkEnvType() == 1);
    }

    public static void init(Context context, String str, String str2, LoginEnvType loginEnvType, DefaultTaobaoAppProvider defaultTaobaoAppProvider) {
        init(context, str, str2, loginEnvType, defaultTaobaoAppProvider, loginEnvType.getSdkEnvType() == 1);
    }

    public static synchronized void init(Context context, String str, String str2, LoginEnvType loginEnvType, boolean z) {
        synchronized (Login.class) {
            init(context, str, str2, loginEnvType, new DefaultTaobaoAppProvider(), z);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:28:0x009a, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static synchronized void init(android.content.Context r4, java.lang.String r5, java.lang.String r6, com.taobao.login4android.constants.LoginEnvType r7, final com.taobao.login4android.login.DefaultTaobaoAppProvider r8, boolean r9) {
        /*
            java.lang.Class<com.taobao.login4android.Login> r0 = com.taobao.login4android.Login.class
            monitor-enter(r0)
            com.ali.user.mobile.app.init.Debuggable.init(r4)     // Catch:{ all -> 0x009b }
            com.ali.user.mobile.app.dataprovider.IDataProvider r1 = com.ali.user.mobile.app.dataprovider.DataProviderFactory.getDataProvider()     // Catch:{ all -> 0x009b }
            if (r1 == 0) goto L_0x0023
            com.ali.user.mobile.app.dataprovider.IDataProvider r1 = com.ali.user.mobile.app.dataprovider.DataProviderFactory.getDataProvider()     // Catch:{ all -> 0x009b }
            boolean r1 = r1 instanceof com.taobao.login4android.login.DefaultTaobaoAppProvider     // Catch:{ all -> 0x009b }
            if (r1 == 0) goto L_0x0023
            android.content.Context r1 = com.ali.user.mobile.app.dataprovider.DataProviderFactory.getApplicationContext()     // Catch:{ all -> 0x009b }
            if (r1 == 0) goto L_0x0023
            java.lang.String r4 = "login.Login"
            java.lang.String r5 = "Login has inited, discard current request."
            com.ali.user.mobile.log.TLogAdapter.d(r4, r5)     // Catch:{ all -> 0x009b }
            monitor-exit(r0)
            return
        L_0x0023:
            com.taobao.login4android.session.SessionManager r1 = com.taobao.login4android.session.SessionManager.getInstance(r4)     // Catch:{ all -> 0x009b }
            session = r1     // Catch:{ all -> 0x009b }
            java.lang.String r1 = "login.Login"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x009b }
            r2.<init>()     // Catch:{ all -> 0x009b }
            java.lang.String r3 = " start Login init.app version = "
            r2.append(r3)     // Catch:{ all -> 0x009b }
            com.ali.user.mobile.info.AppInfo r3 = com.ali.user.mobile.info.AppInfo.getInstance()     // Catch:{ all -> 0x009b }
            java.lang.String r3 = r3.getAppVersion()     // Catch:{ all -> 0x009b }
            r2.append(r3)     // Catch:{ all -> 0x009b }
            java.lang.String r2 = r2.toString()     // Catch:{ all -> 0x009b }
            com.ali.user.mobile.log.TLogAdapter.e((java.lang.String) r1, (java.lang.String) r2)     // Catch:{ all -> 0x009b }
            r8.setContext(r4)     // Catch:{ all -> 0x009b }
            r8.setTTID(r5)     // Catch:{ all -> 0x009b }
            r8.setProductVersion(r6)     // Catch:{ all -> 0x009b }
            int r5 = r7.getSdkEnvType()     // Catch:{ all -> 0x009b }
            r8.setEnvType(r5)     // Catch:{ all -> 0x009b }
            r8.setAppDebug(r9)     // Catch:{ all -> 0x009b }
            com.ali.user.mobile.app.dataprovider.DataProviderFactory.setDataProvider(r8)     // Catch:{ all -> 0x009b }
            java.lang.Class<com.ali.user.mobile.service.StorageService> r5 = com.ali.user.mobile.service.StorageService.class
            java.lang.Object r5 = com.ali.user.mobile.service.ServiceFactory.getService(r5)     // Catch:{ all -> 0x009b }
            com.ali.user.mobile.service.StorageService r5 = (com.ali.user.mobile.service.StorageService) r5     // Catch:{ all -> 0x009b }
            android.content.Context r4 = r4.getApplicationContext()     // Catch:{ all -> 0x009b }
            r5.init(r4)     // Catch:{ all -> 0x009b }
            com.ali.user.mobile.coordinator.CoordinatorWrapper r4 = new com.ali.user.mobile.coordinator.CoordinatorWrapper     // Catch:{ all -> 0x009b }
            r4.<init>()     // Catch:{ all -> 0x009b }
            com.taobao.login4android.Login$1 r5 = new com.taobao.login4android.Login$1     // Catch:{ all -> 0x009b }
            r5.<init>(r8)     // Catch:{ all -> 0x009b }
            r4.execute(r5)     // Catch:{ all -> 0x009b }
            android.content.BroadcastReceiver r4 = mReceiver     // Catch:{ all -> 0x009b }
            if (r4 != 0) goto L_0x0099
            java.lang.Object r4 = lock     // Catch:{ all -> 0x009b }
            monitor-enter(r4)     // Catch:{ all -> 0x009b }
            android.content.BroadcastReceiver r5 = mReceiver     // Catch:{ all -> 0x0096 }
            if (r5 != 0) goto L_0x0094
            com.taobao.login4android.broadcast.LoginBroadcastReceiver r5 = new com.taobao.login4android.broadcast.LoginBroadcastReceiver     // Catch:{ all -> 0x0096 }
            r5.<init>()     // Catch:{ all -> 0x0096 }
            mReceiver = r5     // Catch:{ all -> 0x0096 }
            android.content.Context r5 = com.ali.user.mobile.app.dataprovider.DataProviderFactory.getApplicationContext()     // Catch:{ all -> 0x0096 }
            android.content.BroadcastReceiver r6 = mReceiver     // Catch:{ all -> 0x0096 }
            com.taobao.login4android.broadcast.LoginBroadcastHelper.registerLoginReceiver(r5, r6)     // Catch:{ all -> 0x0096 }
        L_0x0094:
            monitor-exit(r4)     // Catch:{ all -> 0x0096 }
            goto L_0x0099
        L_0x0096:
            r5 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x0096 }
            throw r5     // Catch:{ all -> 0x009b }
        L_0x0099:
            monitor-exit(r0)
            return
        L_0x009b:
            r4 = move-exception
            monitor-exit(r0)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.login4android.Login.init(android.content.Context, java.lang.String, java.lang.String, com.taobao.login4android.constants.LoginEnvType, com.taobao.login4android.login.DefaultTaobaoAppProvider, boolean):void");
    }

    public static void setLocationProvider(LocationProvider locationProvider2) {
        locationProvider = locationProvider2;
        if (locationProvider2 != null) {
            DataProviderFactory.getDataProvider().setLocation(locationProvider2.getLocation());
        }
    }

    public static LocationProvider getLocationProvider() {
        return locationProvider;
    }

    public static void login(boolean z) {
        login(z, (Bundle) null);
    }

    public static void login(final boolean z, final Bundle bundle) {
        String str;
        UserTrackAdapter.sendUT("LoginAPI_Login");
        TLogAdapter.d(TAG, "start login: showUI:" + z);
        if (bundle != null) {
            LoginStatus.browserRefUrl = bundle.getString(LoginConstants.BROWSER_REF_URL);
        }
        if (!LoginStatus.compareAndSetLogining(false, true)) {
            StringBuilder sb = new StringBuilder();
            sb.append("login: return because is logining right now. isLogining=true, userLogin=");
            sb.append(LoginStatus.isUserLogin());
            sb.append(", lastLoginTime=");
            sb.append(LoginStatus.getLastLoginTime());
            sb.append(", extraData = ");
            if (bundle == null) {
                str = BuildConfig.buildJavascriptFrameworkVersion;
            } else {
                str = bundle.toString();
            }
            sb.append(str);
            TLogAdapter.e(TAG, sb.toString());
            if (System.currentTimeMillis() - LoginStatus.getLastLoginTime() >= 300000) {
                LoginStatus.resetLoginFlag();
            } else if (!z) {
                return;
            } else {
                if ((System.currentTimeMillis() - LoginStatus.getLastLoginTime() >= LOGIN_TIMEOUT || LoginStatus.isUserLogin()) && loginTask != null && !loginTask.isCancelled() && loginTask.getStatus() != AsyncTask.Status.FINISHED) {
                    TLogAdapter.e(TAG, "cancel last login task");
                    try {
                        loginTask.cancel(true);
                        return;
                    } catch (Throwable th) {
                        th.printStackTrace();
                        return;
                    }
                } else {
                    return;
                }
            }
        }
        loginTask = new LoginAsyncTask<Object, Void, Void>() {
            public Void excuteTask(Object... objArr) throws RemoteException {
                LoginController.getInstance().autoLogin(z, bundle);
                TLogAdapter.d(LoginAsyncTask.TAG, "loginWithBundle finish");
                return null;
            }
        };
        new CoordinatorWrapper().execute(loginTask, new Object[0]);
    }

    public static void bindAlipay(final String str, final String str2) {
        new CoordinatorWrapper().execute(new LoginAsyncTask<Object, Void, Void>() {
            public Void excuteTask(Object... objArr) throws RemoteException {
                LoginController.getInstance().bindAlipay(str, str2);
                TLogAdapter.d(LoginAsyncTask.TAG, "bindAlipay finish");
                return null;
            }
        }, new Object[0]);
    }

    public static void goRegister(final RegistParam registParam) {
        new CoordinatorWrapper().execute(new LoginAsyncTask() {
            public Object excuteTask(Object[] objArr) throws Exception {
                LoginController.getInstance().openRegisterPage(DataProviderFactory.getApplicationContext(), registParam);
                TLogAdapter.d(LoginAsyncTask.TAG, "goRegister finish");
                return null;
            }
        }, new Object[0]);
    }

    public static void applyToken(InternalTokenCallback internalTokenCallback) {
        UserTrackAdapter.sendUT("LoginAPI_ApplyToken");
        LoginController.getInstance().applyToken(internalTokenCallback);
        TLogAdapter.d(TAG, "applyToken finish");
    }

    public static void applyTokenWithRemoteBiz(InternalTokenCallback internalTokenCallback) {
        UserTrackAdapter.sendUT("LoginAPI_ApplyTokenRemoteBiz");
        LoginController.getInstance().applyTokenWithRemoteBiz(DataProviderFactory.getDataProvider().getSite(), getUserId(), internalTokenCallback);
        TLogAdapter.d(TAG, "applyToken finish");
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
                LoginController.getInstance().logout(i2, str4, str5, str6, z2);
                if (!Debuggable.isDebug()) {
                    return null;
                }
                LoginTLogAdapter.d(LoginAsyncTask.TAG, "logout finish");
                return null;
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(Void voidR) {
                if (context2 != null) {
                    LoginController.getInstance().openLoginPage(context2);
                }
            }
        }, new Object[0]);
    }

    public static void logout(Context context) {
        logout(context, getLoginSite(), getSid(), getLoginToken(), getUserId(), false);
    }

    public static void logout() {
        logout((Context) null);
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
        LoginController.getInstance().navToWebViewByScene(context, str, i);
    }

    public static void navByScene(Context context, String str, boolean z) {
        if (!z || ServiceFactory.getService(MemberCenterService.class) == null) {
            navByScene(context, str, DataProviderFactory.getDataProvider().getSite());
        } else {
            ((MemberCenterService) ServiceFactory.getService(MemberCenterService.class)).navByScene(context, str);
        }
    }

    public static void navToIVByScene(Context context, String str) {
        navToIVByScene(context, str, (Bundle) null);
    }

    public static void navToIVByScene(Context context, String str, Bundle bundle) {
        navToIVByScene(context, str, DataProviderFactory.getDataProvider().getSite(), bundle);
    }

    public static void navToIVByScene(Context context, String str, int i, Bundle bundle) {
        Properties properties = new Properties();
        if (!TextUtils.isEmpty(str)) {
            properties.put("scene", str);
        }
        UserTrackAdapter.sendUT("LoginAPI_navToIV", properties);
        LoginController.getInstance().navToIVByScene(context, str, i, bundle);
    }

    public static void navToIVWithUserId(Context context, String str, String str2) {
        LoginController.getInstance().navToIVWithUserId(context, str, str2);
    }

    public static void openUrl(Context context, String str) {
        UrlParam urlParam = new UrlParam();
        urlParam.url = str;
        LoginController.getInstance().openUrl(context, urlParam);
    }

    public static void openScheme(Context context, String str) {
        UrlParam urlParam = new UrlParam();
        urlParam.url = str;
        LoginController.getInstance().openScheme(context, urlParam);
    }

    public static void checkNickModifiable(CheckResultCallback checkResultCallback) {
        LoginController.getInstance().checkNickModifiable(checkResultCallback);
    }

    public static GetAlipayCookiesResponseData refreshAlipayCookie() {
        if (!checkSessionValid()) {
            return null;
        }
        return new GetAlipayCookiesBusiness().getAlipayCookies();
    }

    public static void refreshCookies() {
        boolean z;
        UserTrackAdapter.sendUT("LoginAPI_RefreshCookies");
        if (!checkSessionValid()) {
            if (Debuggable.isDebug()) {
                LoginTLogAdapter.d(TAG, "SessionManager invalid, discard refresh cookies");
            }
            notifyRefreshResult(false);
            return;
        }
        synchronized (mLock) {
            if (System.currentTimeMillis() - LoginStatus.getLastRefreshCookieTime() > 1800000) {
                z = true;
                LoginStatus.setLastRefreshCookieTime(System.currentTimeMillis());
            } else {
                z = false;
            }
        }
        if (z) {
            new CoordinatorWrapper().execute(new LoginAsyncTask<Object, Void, Boolean>() {
                public Boolean excuteTask(Object... objArr) throws RemoteException {
                    return Boolean.valueOf(LoginController.getInstance().refreshCookies(true, false));
                }

                /* access modifiers changed from: protected */
                public void onPostExecute(Boolean bool) {
                    if (bool == null || !bool.booleanValue()) {
                        LoginStatus.setLastRefreshCookieTime(System.currentTimeMillis() - Login.COOKIES_REFRESH_SHRINK);
                        Login.notifyRefreshResult(false);
                    } else {
                        LoginStatus.setLastRefreshCookieTime(System.currentTimeMillis());
                        Login.notifyRefreshResult(true);
                    }
                    if (Debuggable.isDebug()) {
                        LoginTLogAdapter.d(LoginAsyncTask.TAG, "refreshCookies finish");
                    }
                }
            }, new Object[0]);
            return;
        }
        notifyRefreshResult(false);
        if (Debuggable.isDebug()) {
            LoginTLogAdapter.d(TAG, "No need to refresh cookies");
        }
    }

    /* access modifiers changed from: private */
    public static void notifyRefreshResult(boolean z) {
        try {
            Intent intent = new Intent(LoginAction.NOTIFY_REFRESH_COOKIES.name());
            intent.putExtra(REFRESH_RESULT, z);
            intent.setPackage(DataProviderFactory.getApplicationContext().getPackageName());
            DataProviderFactory.getApplicationContext().sendBroadcast(intent);
            if (Debuggable.isDebug()) {
                LoginTLogAdapter.d(TAG, "sendBroadcast:" + LoginAction.NOTIFY_REFRESH_COOKIES.name());
            }
        } catch (NullPointerException unused) {
        }
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

    public static String getOldEncryptedUserId() {
        return session != null ? session.getOldEncryptedUserId() : "";
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

    public static void setOneTimeToken(String str) {
        if (session != null) {
            session.setOneTimeToken(str);
        }
    }

    public static void setHavanaSsoTokenExpiredTime(long j) {
        if (session != null) {
            session.setHavanaSsoTokenExpiredTime(j);
        }
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

    public static void setCommentUsed(boolean z) {
        if (session != null) {
            session.setCommentTokenUsed(z);
        }
    }

    public static void clearHistoryNames() {
        LoginController.getInstance().clearHistoryNames();
    }

    public static String getDeviceTokenKey(String str) {
        return SecurityGuardManagerWraper.getDeviceTokenKey(str);
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
