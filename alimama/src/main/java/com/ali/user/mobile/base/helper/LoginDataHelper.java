package com.ali.user.mobile.base.helper;

import android.content.Intent;
import android.text.TextUtils;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.app.init.Debuggable;
import com.ali.user.mobile.db.LoginHistoryOperater;
import com.ali.user.mobile.log.UserTrackAdapter;
import com.ali.user.mobile.login.action.LoginResActions;
import com.ali.user.mobile.login.model.LoginConstant;
import com.ali.user.mobile.rpc.HistoryAccount;
import com.ali.user.mobile.rpc.login.model.AliUserResponseData;
import com.ali.user.mobile.rpc.login.model.LoginReturnData;
import com.ali.user.mobile.rpc.login.model.SessionModel;
import com.ali.user.mobile.security.SecurityGuardManagerWraper;
import com.ali.user.mobile.utils.SharedPreferencesUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.alipay.sdk.cons.b;
import com.taobao.login4android.broadcast.LoginAction;
import com.taobao.login4android.constants.LoginConstants;
import com.taobao.login4android.constants.LoginStatus;
import com.taobao.login4android.log.LoginTLogAdapter;
import com.taobao.login4android.session.ISession;
import com.taobao.login4android.session.SessionManager;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class LoginDataHelper {
    public static final String TAG = "login.LoginDataHelper";

    public static boolean processLoginReturnData(boolean z, LoginReturnData loginReturnData, String str) {
        return processLoginReturnData(z, loginReturnData, "", str);
    }

    public static boolean processLoginReturnData(boolean z, LoginReturnData loginReturnData, String str, String str2) {
        if (loginReturnData == null || loginReturnData.data == null) {
            return false;
        }
        if (Debuggable.isDebug()) {
            LoginTLogAdapter.d(TAG, "LoginResponse Data=" + loginReturnData.data);
        }
        if (LoginStatus.isFromChangeAccount() && z) {
            HashMap hashMap = new HashMap();
            hashMap.put(LoginConstants.LOGOUT_TYPE, LoginConstants.LogoutType.CHANGE_ACCOUNT.getType());
            BroadCastHelper.sendBroadcast(LoginAction.NOTIFY_LOGOUT, false, 0, str, hashMap, "before recover account");
        }
        try {
            AliUserResponseData aliUserResponseData = (AliUserResponseData) JSON.parseObject(loginReturnData.data, AliUserResponseData.class);
            SessionManager instance = SessionManager.getInstance(DataProviderFactory.getApplicationContext());
            if (!TextUtils.isEmpty(instance.getUserId()) && !TextUtils.equals(aliUserResponseData.userId, instance.getUserId())) {
                UserTrackAdapter.sendUT("Page_AccountManager", "ChangeMultiAccountsSuc");
            }
            onLoginSuccess(loginReturnData, aliUserResponseData, instance);
            handleHistory(loginReturnData, instance, aliUserResponseData);
            if (!z) {
                return true;
            }
            BroadCastHelper.sendLocalBroadCast(new Intent(LoginResActions.LOGIN_SUCCESS_ACTION));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Properties properties = new Properties();
            if (!TextUtils.isEmpty(loginReturnData.showLoginId)) {
                properties.setProperty("username", loginReturnData.showLoginId);
            }
            if (!TextUtils.isEmpty(e.getMessage())) {
                properties.setProperty("errorCode", e.getMessage());
            }
            if (!TextUtils.isEmpty(DataProviderFactory.getDataProvider().getAppkey())) {
                properties.setProperty("appName", DataProviderFactory.getDataProvider().getAppkey());
            }
            UserTrackAdapter.sendUT("Event_LoginFail", properties);
            return false;
        }
    }

    public static void handleHistory(LoginReturnData loginReturnData, ISession iSession, AliUserResponseData aliUserResponseData) {
        String str;
        String str2;
        LoginReturnData loginReturnData2 = loginReturnData;
        AliUserResponseData aliUserResponseData2 = aliUserResponseData;
        if (loginReturnData2.site != -1) {
            iSession.setLoginSite(loginReturnData2.site);
        }
        if (loginReturnData2.deviceToken != null) {
            String str3 = loginReturnData2.deviceToken.key;
            str2 = loginReturnData2.deviceToken.salt;
            str = str3;
        } else {
            str2 = null;
            str = null;
        }
        int i = loginReturnData2.site;
        String str4 = loginReturnData2.showLoginId;
        if (TextUtils.isEmpty(str4)) {
            str4 = loginReturnData2.taobaoNick;
        }
        HistoryAccount historyAccount = r5;
        HistoryAccount historyAccount2 = new HistoryAccount(str4, loginReturnData2.mobile, aliUserResponseData2.headPicLink, loginReturnData2.hid.longValue(), loginReturnData2.alipayHid == null ? 0 : loginReturnData2.alipayHid.longValue(), aliUserResponseData2.autoLoginToken, aliUserResponseData2.loginTime, str, loginReturnData2.loginType, loginReturnData2.taobaoNick, loginReturnData2.email, loginReturnData2.alipayCrossed, i);
        if (!TextUtils.isEmpty(loginReturnData2.accountId)) {
            historyAccount.setAccountId(loginReturnData2.accountId);
        }
        historyAccount.setLoginPhone(aliUserResponseData2.loginPhone);
        if (aliUserResponseData2.loginServiceExt != null) {
            String str5 = aliUserResponseData2.loginServiceExt.get("loginType");
            if (!TextUtils.equals(str5, LoginConstant.LOGIN_TYPE_AUTOLOGIN)) {
                SharedPreferencesUtil.saveData(DataProviderFactory.getApplicationContext(), LoginConstant.LOGIN_TYPE, str5);
            }
            String str6 = aliUserResponseData2.loginServiceExt.get("loginEntrance");
            if (!TextUtils.isEmpty(str6)) {
                SharedPreferencesUtil.saveData(DataProviderFactory.getApplicationContext(), LoginConstant.LOGIN_ENTRANCE, str6);
            }
            if (!aliUserResponseData2.loginServiceExt.containsKey("hasPwd")) {
                historyAccount.hasPwd = -1;
            } else if (TextUtils.equals("true", aliUserResponseData2.loginServiceExt.get("hasPwd"))) {
                historyAccount.hasPwd = 1;
            } else {
                historyAccount.hasPwd = 0;
            }
        }
        if (loginReturnData2.deviceToken != null) {
            LoginHistoryOperater.getInstance().saveHistory(historyAccount, str2);
        } else if (DataProviderFactory.getDataProvider().isSaveHistoryWithoutSalt()) {
            LoginHistoryOperater.getInstance().saveHistoryWithNoSalt(historyAccount);
        } else if (loginReturnData2.hid != null) {
            SecurityGuardManagerWraper.updateLoginHistoryIndex(historyAccount);
        }
        SessionModel sessionModel = new SessionModel();
        sessionModel.sid = aliUserResponseData2.sid;
        sessionModel.ecode = aliUserResponseData2.ecode;
        sessionModel.nick = aliUserResponseData2.nick;
        sessionModel.userId = aliUserResponseData2.userId;
        sessionModel.email = aliUserResponseData2.email;
        sessionModel.havanaId = aliUserResponseData2.havanaId;
        sessionModel.alipayHid = aliUserResponseData2.alipayHid;
        sessionModel.loginTime = aliUserResponseData2.loginTime;
        sessionModel.autoLoginToken = aliUserResponseData2.autoLoginToken;
        sessionModel.headPicLink = aliUserResponseData2.headPicLink;
        sessionModel.havanaSsoToken = aliUserResponseData2.havanaSsoToken;
        sessionModel.havanaSsoTokenExpiredTime = aliUserResponseData2.havanaSsoTokenExpiredTime;
        sessionModel.externalCookies = aliUserResponseData2.externalCookies;
        sessionModel.cookies = aliUserResponseData2.cookies;
        sessionModel.ssoToken = aliUserResponseData2.ssoToken;
        sessionModel.expires = aliUserResponseData2.expires;
        sessionModel.extendAttribute = aliUserResponseData2.extendAttribute;
        sessionModel.loginServiceExt = aliUserResponseData2.loginServiceExt;
        sessionModel.site = loginReturnData2.site;
        sessionModel.showLoginId = loginReturnData2.showLoginId;
        SecurityGuardManagerWraper.putSessionModelToFile(sessionModel);
    }

    public static SessionModel sessionToModel(ISession iSession) {
        SessionModel sessionModel = new SessionModel();
        sessionModel.sid = iSession.getSid();
        sessionModel.ecode = iSession.getEcode();
        sessionModel.nick = iSession.getNick();
        sessionModel.userId = iSession.getUserId();
        sessionModel.email = iSession.getEmail();
        sessionModel.autoLoginToken = iSession.getLoginToken();
        sessionModel.havanaSsoToken = iSession.getOneTimeToken();
        sessionModel.havanaSsoTokenExpiredTime = iSession.getHavanaSsoTokenExpiredTime();
        sessionModel.ssoToken = iSession.getSsoToken();
        sessionModel.expires = iSession.getSessionExpiredTime();
        if (!TextUtils.isEmpty(iSession.getExtJson())) {
            try {
                sessionModel.loginServiceExt = (Map) JSON.parseObject(iSession.getExtJson(), new TypeReference<Map<String, String>>() {
                }, new Feature[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        sessionModel.site = iSession.getLoginSite();
        sessionModel.showLoginId = iSession.getEmail();
        if (TextUtils.isEmpty(sessionModel.showLoginId)) {
            sessionModel.showLoginId = iSession.getNick();
        }
        return sessionModel;
    }

    /* JADX WARNING: Removed duplicated region for block: B:25:0x0096  */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x00ce A[Catch:{ Exception -> 0x00e0 }] */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x00d1 A[Catch:{ Exception -> 0x00e0 }] */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x01a0 A[ADDED_TO_REGION] */
    /* JADX WARNING: Removed duplicated region for block: B:59:? A[ADDED_TO_REGION, RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0044  */
    /* JADX WARNING: Removed duplicated region for block: B:9:0x004b  */
    @android.annotation.SuppressLint({"CommitPrefEdits"})
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void onLoginSuccess(com.ali.user.mobile.rpc.login.model.LoginReturnData r40, com.ali.user.mobile.rpc.login.model.AliUserResponseData r41, com.taobao.login4android.session.ISession r42) {
        /*
            r1 = r40
            r2 = r41
            r15 = r42
            java.lang.String r14 = r2.sid
            java.lang.String r12 = r2.subSid
            java.lang.String r13 = r2.ecode
            java.lang.String r6 = r2.nick
            java.lang.String r11 = r2.userId
            java.util.Map<java.lang.String, java.lang.Object> r0 = r2.extendAttribute
            java.lang.String r8 = r2.headPicLink
            java.lang.String r9 = r2.autoLoginToken
            java.lang.String r10 = r2.ssoToken
            java.lang.String r7 = r2.havanaSsoToken
            long r4 = r2.havanaSsoTokenExpiredTime
            r22 = r4
            long r3 = r2.expires
            r24 = r9
            r25 = r10
            long r9 = r2.loginTime
            java.lang.String[] r5 = r2.externalCookies
            java.lang.String[] r15 = r2.cookies
            r26 = r15
            java.lang.String r15 = r2.email
            r27 = r15
            java.lang.String r15 = r2.loginPhone
            if (r1 == 0) goto L_0x003e
            r28 = r5
            java.lang.String r5 = r1.sessionDisastergrd
            if (r5 != 0) goto L_0x003b
            goto L_0x0040
        L_0x003b:
            java.lang.String r5 = r1.sessionDisastergrd
            goto L_0x0042
        L_0x003e:
            r28 = r5
        L_0x0040:
            java.lang.String r5 = ""
        L_0x0042:
            if (r1 != 0) goto L_0x004b
            java.lang.String r16 = ""
            r29 = r5
            r5 = r16
            goto L_0x004f
        L_0x004b:
            r29 = r5
            java.lang.String r5 = r1.displayNick
        L_0x004f:
            sendBroadcastHavanaSsoToken(r7)
            java.lang.String r1 = r2.uidDigest
            r30 = r5
            com.ut.mini.UTAnalytics r5 = com.ut.mini.UTAnalytics.getInstance()     // Catch:{ Throwable -> 0x006f }
            r5.updateUserAccount(r6, r11, r1)     // Catch:{ Throwable -> 0x006f }
            boolean r5 = android.text.TextUtils.isEmpty(r11)     // Catch:{ Throwable -> 0x006f }
            if (r5 != 0) goto L_0x0076
            boolean r5 = android.text.TextUtils.isEmpty(r1)     // Catch:{ Throwable -> 0x006f }
            if (r5 == 0) goto L_0x0076
            java.lang.String r5 = "UT_OPENID_IS_NULL"
            com.ali.user.mobile.log.UserTrackAdapter.sendUT((java.lang.String) r5)     // Catch:{ Throwable -> 0x006f }
            goto L_0x0076
        L_0x006f:
            com.ut.mini.UTAnalytics r5 = com.ut.mini.UTAnalytics.getInstance()
            r5.updateUserAccount(r6, r11)
        L_0x0076:
            android.content.Context r5 = com.ali.user.mobile.app.dataprovider.DataProviderFactory.getApplicationContext()
            com.taobao.dp.DeviceSecuritySDK r5 = com.taobao.dp.DeviceSecuritySDK.getInstance(r5)
            r5.sendLoginResult(r6)
            android.content.Context r5 = com.ali.user.mobile.app.dataprovider.DataProviderFactory.getApplicationContext()
            com.taobao.wireless.security.sdk.SecurityGuardManager r5 = com.taobao.wireless.security.sdk.SecurityGuardManager.getInstance(r5)
            if (r5 == 0) goto L_0x0094
            com.taobao.wireless.security.sdk.datacollection.IDataCollectionComponent r5 = r5.getDataCollectionComp()
            if (r5 == 0) goto L_0x0094
            r5.setNick(r6)
        L_0x0094:
            if (r0 == 0) goto L_0x00bb
            java.lang.String r5 = "ssoDomainList"
            java.lang.Object r5 = r0.get(r5)     // Catch:{ Exception -> 0x00b4 }
            if (r5 != 0) goto L_0x009f
            goto L_0x00bb
        L_0x009f:
            java.lang.String r5 = "ssoDomainList"
            java.lang.Object r0 = r0.get(r5)     // Catch:{ Exception -> 0x00b4 }
            com.alibaba.fastjson.JSONArray r0 = (com.alibaba.fastjson.JSONArray) r0     // Catch:{ Exception -> 0x00b4 }
            com.alibaba.fastjson.JSONArray r0 = (com.alibaba.fastjson.JSONArray) r0     // Catch:{ Exception -> 0x00b4 }
            r5 = 0
            java.lang.String[] r5 = new java.lang.String[r5]     // Catch:{ Exception -> 0x00b4 }
            java.lang.Object[] r0 = r0.toArray(r5)     // Catch:{ Exception -> 0x00b4 }
            java.lang.String[] r0 = (java.lang.String[]) r0     // Catch:{ Exception -> 0x00b4 }
            r5 = r0
            goto L_0x00bc
        L_0x00b4:
            r0 = move-exception
            r31 = r7
            r32 = r12
            r5 = 0
            goto L_0x00e9
        L_0x00bb:
            r5 = 0
        L_0x00bc:
            java.lang.String r0 = "login.LoginDataHelper"
            r31 = r7
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00e2 }
            r7.<init>()     // Catch:{ Exception -> 0x00e2 }
            r32 = r12
            java.lang.String r12 = "domainList: "
            r7.append(r12)     // Catch:{ Exception -> 0x00e0 }
            if (r5 != 0) goto L_0x00d1
            java.lang.String r12 = ""
            goto L_0x00d5
        L_0x00d1:
            java.lang.Object r12 = com.alibaba.fastjson.JSON.toJSON(r5)     // Catch:{ Exception -> 0x00e0 }
        L_0x00d5:
            r7.append(r12)     // Catch:{ Exception -> 0x00e0 }
            java.lang.String r7 = r7.toString()     // Catch:{ Exception -> 0x00e0 }
            com.taobao.login4android.log.LoginTLogAdapter.d(r0, r7)     // Catch:{ Exception -> 0x00e0 }
            goto L_0x00ec
        L_0x00e0:
            r0 = move-exception
            goto L_0x00e9
        L_0x00e2:
            r0 = move-exception
            goto L_0x00e7
        L_0x00e4:
            r0 = move-exception
            r31 = r7
        L_0x00e7:
            r32 = r12
        L_0x00e9:
            r0.printStackTrace()
        L_0x00ec:
            r16 = r5
            long r17 = adjustSessionExpireTime(r3, r9)
            r3 = r42
            r19 = r22
            r4 = r14
            r21 = r28
            r12 = r29
            r7 = r30
            r5 = r13
            r2 = r7
            r22 = r31
            r7 = r11
            r28 = r9
            r9 = r24
            r10 = r25
            r33 = r11
            r11 = r22
            r34 = r1
            r36 = r12
            r35 = r13
            r1 = r32
            r12 = r19
            r37 = r14
            r14 = r21
            r39 = r1
            r38 = r2
            r21 = r15
            r1 = r27
            r2 = r42
            r15 = r26
            r19 = r28
            r3.onLoginSuccess(r4, r5, r6, r7, r8, r9, r10, r11, r12, r14, r15, r16, r17, r19, r21)
            r2.setEmail(r1)
            r1 = r39
            r2.setSubSid(r1)
            r5 = r38
            r2.setDisplayNick(r5)
            r1 = r34
            r2.setUidDigest(r1)
            r5 = r36
            r2.setSessionDisastergrd(r5)
            r1 = r40
            if (r1 == 0) goto L_0x0157
            java.util.Map<java.lang.String, java.lang.String> r0 = r1.extMap
            if (r0 == 0) goto L_0x0157
            java.util.Map<java.lang.String, java.lang.String> r0 = r1.extMap
            java.lang.String r1 = "encryptUserId"
            java.lang.Object r0 = r0.get(r1)
            java.lang.String r0 = (java.lang.String) r0
            r2.setOldEncryptedUserId(r0)
        L_0x0157:
            java.lang.Class<com.ali.user.mobile.service.RpcService> r0 = com.ali.user.mobile.service.RpcService.class
            java.lang.Object r0 = com.ali.user.mobile.service.ServiceFactory.getService(r0)
            com.ali.user.mobile.service.RpcService r0 = (com.ali.user.mobile.service.RpcService) r0
            r3 = r33
            r1 = r37
            r0.registerSessionInfo(r1, r3, r5)
            java.lang.String r0 = "login.LoginDataHelper"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "registeSessionInfo to mtopsdk:(sid:"
            r4.append(r5)
            r4.append(r1)
            java.lang.String r1 = ",ecode:"
            r4.append(r1)
            r1 = r35
            r4.append(r1)
            java.lang.String r1 = ",userId:"
            r4.append(r1)
            r4.append(r3)
            java.lang.String r1 = ")."
            r4.append(r1)
            java.lang.String r1 = r4.toString()
            com.taobao.login4android.log.LoginTLogAdapter.d(r0, r1)
            long r0 = java.lang.System.currentTimeMillis()
            com.taobao.login4android.constants.LoginStatus.setLastRefreshCookieTime(r0)
            r1 = r41
            java.util.Map<java.lang.String, java.lang.String> r0 = r1.loginServiceExt
            if (r0 == 0) goto L_0x01ab
            if (r2 == 0) goto L_0x01ab
            java.util.Map<java.lang.String, java.lang.String> r0 = r1.loginServiceExt
            java.lang.String r0 = com.alibaba.fastjson.JSON.toJSONString(r0)
            r2.setExtJson(r0)
        L_0x01ab:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ali.user.mobile.base.helper.LoginDataHelper.onLoginSuccess(com.ali.user.mobile.rpc.login.model.LoginReturnData, com.ali.user.mobile.rpc.login.model.AliUserResponseData, com.taobao.login4android.session.ISession):void");
    }

    public static long adjustSessionExpireTime(long j, long j2) {
        long currentTimeMillis = System.currentTimeMillis() / 1000;
        if (currentTimeMillis > j2) {
            return j > 0 ? j + (currentTimeMillis - j2) : 86400 + currentTimeMillis;
        }
        return j;
    }

    private static void sendBroadcastHavanaSsoToken(String str) {
        LoginTLogAdapter.i(TAG, "sendBroadcastHavanaSsoToken start");
        if (!TextUtils.isEmpty(str) && DataProviderFactory.getDataProvider() != null && isYunOS()) {
            Intent intent = new Intent();
            intent.setPackage("com.yunos.account");
            intent.setAction("com.yunos.account.LOGIN_HAVANA_SSOTOKEN");
            intent.putExtra("havana_sso_token", str);
            intent.putExtra(b.h, DataProviderFactory.getDataProvider().getAppkey());
            DataProviderFactory.getApplicationContext().sendBroadcast(intent);
            LoginTLogAdapter.i(TAG, "sendBroadcastHavanaSsoToken end");
        }
    }

    public static boolean isYunOS() {
        String str;
        String str2 = null;
        try {
            Method method = Class.forName("android.os.SystemProperties").getMethod("get", new Class[]{String.class});
            str = (String) method.invoke((Object) null, new Object[]{"ro.yunos.version"});
            try {
                str2 = (String) method.invoke((Object) null, new Object[]{"ro.yunos.build.version"});
            } catch (ClassNotFoundException | Exception | NoSuchMethodException unused) {
            }
        } catch (ClassNotFoundException | Exception | NoSuchMethodException unused2) {
            str = null;
        }
        return !TextUtils.isEmpty(str) || !TextUtils.isEmpty(str2);
    }
}
