package com.alimama.union.app.aalogin;

import alimama.com.unwbase.UNWManager;
import alimama.com.unwbase.interfaces.IPush;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.Nullable;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.info.AlipayInfo;
import com.ali.user.mobile.info.AppInfo;
import com.alibaba.motu.crashreporter.MotuCrashReporter;
import com.alimama.moon.R;
import com.alimama.moon.dao.SettingManager;
import com.alimama.moon.eventbus.IEventBus;
import com.alimama.moon.eventbus.LoginEvent;
import com.alimama.moon.network.MtopException;
import com.alimama.moon.network.api.domin.MtopAlimamaMoonProviderUserMemberinfoGetResponse;
import com.alimama.moon.service.BeanContext;
import com.alimama.moon.utils.StringUtil;
import com.alimama.moon.utils.ToastUtil;
import com.alimama.union.app.aalogin.ILogin;
import com.alimama.union.app.aalogin.model.User;
import com.alimama.union.app.aalogin.view.LoginChooserActivity;
import com.alimama.union.app.configproperties.EnvHelper;
import com.alimama.union.app.network.IWebService;
import com.alimama.union.app.network.request.UserMemberinfoGetRequest;
import com.alimama.union.app.network.response.UserMemberinfoGetResponseData;
import com.taobao.android.sso.v2.launch.SsoLogin;
import com.taobao.android.sso.v2.launch.exception.SSOException;
import com.taobao.android.sso.v2.launch.model.ISsoRemoteParam;
import com.taobao.login4android.Login;
import com.taobao.login4android.broadcast.LoginBroadcastHelper;
import com.taobao.login4android.constants.LoginEnvType;
import com.taobao.weex.BuildConfig;
import com.ut.mini.UTAnalytics;
import javax.inject.Inject;
import javax.inject.Named;
import mtopsdk.mtop.domain.IMTOPDataObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaobaoLogin implements ILogin {
    private static final String TAG = "TaobaoLogin";
    /* access modifiers changed from: private */
    public static final Logger logger = LoggerFactory.getLogger((Class<?>) TaobaoLogin.class);
    /* access modifiers changed from: private */
    public final Context appContext;
    private final IEventBus eventBus;
    private boolean hasRegistered = false;
    private ILogin.ILoginListener listener;
    private final BroadcastReceiver loginReceiver;
    /* access modifiers changed from: private */
    public Long memberId = 0L;
    private Long taobaoNumId = 0L;
    private final IWebService webService;

    public boolean isLogining() {
        return false;
    }

    @Inject
    public TaobaoLogin(@Named("appContext") Context context, @Named("mtop_service") IWebService iWebService, IEventBus iEventBus) {
        this.appContext = context;
        this.webService = iWebService;
        this.eventBus = iEventBus;
        this.loginReceiver = new LoginBroadcastReceiver();
        initTaobaoLogin();
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0040  */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x0043  */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x004d  */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0057  */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x009f  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void initTaobaoLogin() {
        /*
            r7 = this;
            com.alimama.union.app.configproperties.EnvHelper r0 = com.alimama.union.app.configproperties.EnvHelper.getInstance()
            java.lang.String r0 = r0.getCurrentApiEnv()
            int r1 = r0.hashCode()
            r2 = -1012222381(0xffffffffc3aab653, float:-341.4244)
            r3 = 0
            r4 = 1
            if (r1 == r2) goto L_0x0032
            r2 = -318370553(0xffffffffed060d07, float:-2.5929213E27)
            if (r1 == r2) goto L_0x0028
            r2 = 95458899(0x5b09653, float:1.6606181E-35)
            if (r1 == r2) goto L_0x001e
            goto L_0x003c
        L_0x001e:
            java.lang.String r1 = "debug"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x003c
            r0 = 2
            goto L_0x003d
        L_0x0028:
            java.lang.String r1 = "prepare"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x003c
            r0 = 1
            goto L_0x003d
        L_0x0032:
            java.lang.String r1 = "online"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x003c
            r0 = 0
            goto L_0x003d
        L_0x003c:
            r0 = -1
        L_0x003d:
            switch(r0) {
                case 0: goto L_0x0057;
                case 1: goto L_0x004d;
                case 2: goto L_0x0043;
                default: goto L_0x0040;
            }
        L_0x0040:
            com.taobao.login4android.constants.LoginEnvType r0 = com.taobao.login4android.constants.LoginEnvType.ONLINE
            goto L_0x0060
        L_0x0043:
            com.taobao.login4android.constants.LoginEnvType r0 = com.taobao.login4android.constants.LoginEnvType.DEV
            java.lang.Boolean r1 = java.lang.Boolean.valueOf(r4)
            com.ali.user.mobile.app.init.Debuggable.setDebug(r1)
            goto L_0x0060
        L_0x004d:
            com.taobao.login4android.constants.LoginEnvType r0 = com.taobao.login4android.constants.LoginEnvType.PRE
            java.lang.Boolean r1 = java.lang.Boolean.valueOf(r4)
            com.ali.user.mobile.app.init.Debuggable.setDebug(r1)
            goto L_0x0060
        L_0x0057:
            com.taobao.login4android.constants.LoginEnvType r0 = com.taobao.login4android.constants.LoginEnvType.ONLINE
            java.lang.Boolean r1 = java.lang.Boolean.valueOf(r3)
            com.ali.user.mobile.app.init.Debuggable.setDebug(r1)
        L_0x0060:
            android.content.Context r1 = r7.appContext
            android.content.Context r1 = r1.getApplicationContext()
            java.lang.String r2 = "10002089@moon_android_7.3.4"
            java.lang.String r5 = "7.3.4"
            com.alimama.union.app.aalogin.NTaobaoAppProvider r6 = new com.alimama.union.app.aalogin.NTaobaoAppProvider
            r6.<init>()
            com.taobao.login4android.Login.init((android.content.Context) r1, (java.lang.String) r2, (java.lang.String) r5, (com.taobao.login4android.constants.LoginEnvType) r0, (com.taobao.login4android.login.DefaultTaobaoAppProvider) r6)
            android.content.Context r0 = r7.appContext
            android.content.Context r0 = r0.getApplicationContext()
            com.taobao.login4android.constants.LoginStatus.init(r0)
            com.alimama.union.app.aalogin.TaobaoLogin$1 r0 = new com.alimama.union.app.aalogin.TaobaoLogin$1
            r0.<init>()
            com.ali.user.mobile.common.api.AliUserLogin.setLoginAppreanceExtions(r0)
            java.util.HashMap r0 = new java.util.HashMap
            r0.<init>()
            java.lang.String r1 = "login_init"
            java.lang.String r2 = "inited"
            r0.put(r1, r2)
            java.lang.String r1 = "TaobaoLogin"
            android.content.Context r2 = r7.appContext
            java.lang.String r2 = com.alimama.moon.utils.CommonUtils.getProcessName(r2)
            boolean r5 = com.taobao.login4android.Login.checkSessionValid()
            com.taobao.login4android.session.ISession r6 = com.taobao.login4android.Login.session
            if (r6 != 0) goto L_0x00a0
            r3 = 1
        L_0x00a0:
            com.alimama.union.app.logger.NewMonitorLogger.Login.init(r1, r0, r2, r5, r3)
            r7.registerReceiver()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alimama.union.app.aalogin.TaobaoLogin.initTaobaoLogin():void");
    }

    /* access modifiers changed from: private */
    public void onNotifyLoginCancel() {
        this.eventBus.post(new LoginEvent.LoginCancelEvent());
    }

    /* access modifiers changed from: private */
    public void onNotifyLogout() {
        clearAccount();
    }

    /* access modifiers changed from: private */
    public void onNotifyLoginSuccess() {
        if (StringUtil.isBlank(Login.getUserId())) {
            logger.warn("onNotifyLoginSuccess, but Login.getUserId return null");
            return;
        }
        MotuCrashReporter.getInstance().setUserNick(getNick());
        this.taobaoNumId = Long.valueOf(Login.getUserId());
        UTAnalytics.getInstance().updateUserAccount(getNick(), getUserId());
        new GetMemberInfoAsyncTask(this.appContext, this.webService, this.eventBus, this.listener).execute(new Long[]{this.taobaoNumId});
    }

    public String getSid() {
        return Login.getSid();
    }

    public String getEcode() {
        return Login.getEcode();
    }

    public boolean checkSessionValid() {
        return Login.checkSessionValid() && Long.valueOf(((SettingManager) BeanContext.get(SettingManager.class)).getMemberId()).longValue() > 0;
    }

    public void launchTaobao(Activity activity) {
        try {
            if (!SsoLogin.isSupportTBSsoV2(activity)) {
                logger.warn("tbSsoV2 not supported!");
                ToastUtil.toast((Context) activity, (int) R.string.msg_tbssov2_not_supported);
                return;
            }
            SsoLogin.launchTao(activity, buildSsoParams());
        } catch (SSOException e) {
            logger.error(e.getMessage());
        }
    }

    private ISsoRemoteParam buildSsoParams() {
        return new ISsoRemoteParam() {
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
        };
    }

    public void showLoginUI(Activity activity) {
        Login.login(true);
    }

    public void showLoginChooserUI() {
        Intent intent = new Intent(this.appContext, LoginChooserActivity.class);
        intent.addFlags(268435456);
        this.appContext.startActivity(intent);
    }

    public void autoLogin() {
        if (checkSessionValid()) {
            MotuCrashReporter.getInstance().setUserNick(getNick());
        } else {
            Login.login(false);
        }
    }

    public void logout() {
        Login.logout();
    }

    public void logout(ILogin.ILoginListener iLoginListener) {
        this.listener = iLoginListener;
        logout();
    }

    @Nullable
    public User getUser() {
        if (!checkSessionValid()) {
            return null;
        }
        User user = new User();
        user.setUserId(Login.getUserId());
        user.setUserNick(Login.getNick());
        user.setAvatarLink(Login.getHeadPicLink());
        user.setMemberId(((SettingManager) BeanContext.get(SettingManager.class)).getMemberId());
        return user;
    }

    public String getUserId() {
        return Login.getUserId();
    }

    public String getNick() {
        return Login.getNick();
    }

    public String getAvatarLink() {
        return Login.getHeadPicLink();
    }

    public void registerReceiver() {
        if (!this.hasRegistered) {
            LoginBroadcastHelper.registerLoginReceiver(this.appContext, this.loginReceiver);
            this.hasRegistered = true;
        }
    }

    public void unregisterReceiver() {
        LoginBroadcastHelper.unregisterLoginReceiver(this.appContext, this.loginReceiver);
        this.hasRegistered = false;
    }

    public void saveAccount() {
        if (this.memberId == null || this.memberId.longValue() <= 0 || this.taobaoNumId == null || this.taobaoNumId.longValue() <= 0) {
            logger.warn("memberId or taobaoNumId less than 0, memberId: {}, taobaoNumId: {}", (Object) this.memberId, (Object) this.taobaoNumId);
            return;
        }
        ((SettingManager) BeanContext.get(SettingManager.class)).setMemberId(this.memberId);
        IPush iPush = (IPush) UNWManager.getInstance().getService(IPush.class);
        if (iPush != null) {
            iPush.setAlias(String.valueOf(this.taobaoNumId));
        }
        this.eventBus.post(new LoginEvent.LoginSuccessEvent());
        if (this.listener != null) {
            this.listener.onLoginSuccess();
        }
    }

    public void clearAccount() {
        MotuCrashReporter.getInstance().setUserNick((String) null);
        ((SettingManager) BeanContext.get(SettingManager.class)).clearUserInfo();
        this.eventBus.post(new LoginEvent.LogoutEvent());
        IPush iPush = (IPush) UNWManager.getInstance().getService(IPush.class);
        if (iPush != null) {
            iPush.removeAlias();
        }
    }

    private class LoginBroadcastReceiver extends BroadcastReceiver {
        private LoginBroadcastReceiver() {
        }

        /* JADX WARNING: Code restructure failed: missing block: B:2:0x0003, code lost:
            r6 = r7.getAction();
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onReceive(android.content.Context r6, android.content.Intent r7) {
            /*
                r5 = this;
                if (r7 != 0) goto L_0x0003
                return
            L_0x0003:
                java.lang.String r6 = r7.getAction()
                com.taobao.login4android.broadcast.LoginAction r7 = com.taobao.login4android.broadcast.LoginAction.valueOf(r6)
                if (r7 != 0) goto L_0x000e
                return
            L_0x000e:
                org.slf4j.Logger r0 = com.alimama.union.app.aalogin.TaobaoLogin.logger
                r0.info(r6)
                java.util.HashMap r6 = new java.util.HashMap
                r6.<init>()
                int[] r0 = com.alimama.union.app.aalogin.TaobaoLogin.AnonymousClass4.$SwitchMap$com$taobao$login4android$broadcast$LoginAction
                int r7 = r7.ordinal()
                r7 = r0[r7]
                r0 = 0
                r1 = 1
                switch(r7) {
                    case 1: goto L_0x00a2;
                    case 2: goto L_0x007a;
                    case 3: goto L_0x0052;
                    case 4: goto L_0x00c9;
                    case 5: goto L_0x0029;
                    case 6: goto L_0x00c9;
                    case 7: goto L_0x00c9;
                    case 8: goto L_0x00c9;
                    case 9: goto L_0x00c9;
                    case 10: goto L_0x00c9;
                    case 11: goto L_0x00c9;
                    case 12: goto L_0x00c9;
                    case 13: goto L_0x00c9;
                    case 14: goto L_0x00c9;
                    case 15: goto L_0x00c9;
                    case 16: goto L_0x00c9;
                    default: goto L_0x0027;
                }
            L_0x0027:
                goto L_0x00c9
            L_0x0029:
                com.alimama.union.app.aalogin.TaobaoLogin r7 = com.alimama.union.app.aalogin.TaobaoLogin.this
                r7.onNotifyLogout()
                r6.clear()
                java.lang.String r7 = "login_callback"
                java.lang.String r2 = "logout"
                r6.put(r7, r2)
                java.lang.String r7 = "TaobaoLogin"
                com.alimama.union.app.aalogin.TaobaoLogin r2 = com.alimama.union.app.aalogin.TaobaoLogin.this
                android.content.Context r2 = r2.appContext
                java.lang.String r2 = com.alimama.moon.utils.CommonUtils.getProcessName(r2)
                boolean r3 = com.taobao.login4android.Login.checkSessionValid()
                com.taobao.login4android.session.ISession r4 = com.taobao.login4android.Login.session
                if (r4 != 0) goto L_0x004d
                r0 = 1
            L_0x004d:
                com.alimama.union.app.logger.NewMonitorLogger.Login.loginOther(r7, r6, r2, r3, r0)
                goto L_0x00c9
            L_0x0052:
                com.alimama.union.app.aalogin.TaobaoLogin r7 = com.alimama.union.app.aalogin.TaobaoLogin.this
                r7.onNotifyLoginCancel()
                r6.clear()
                java.lang.String r7 = "login_callback"
                java.lang.String r2 = "cancel"
                r6.put(r7, r2)
                java.lang.String r7 = "TaobaoLogin"
                com.alimama.union.app.aalogin.TaobaoLogin r2 = com.alimama.union.app.aalogin.TaobaoLogin.this
                android.content.Context r2 = r2.appContext
                java.lang.String r2 = com.alimama.moon.utils.CommonUtils.getProcessName(r2)
                boolean r3 = com.taobao.login4android.Login.checkSessionValid()
                com.taobao.login4android.session.ISession r4 = com.taobao.login4android.Login.session
                if (r4 != 0) goto L_0x0076
                r0 = 1
            L_0x0076:
                com.alimama.union.app.logger.NewMonitorLogger.Login.loginOther(r7, r6, r2, r3, r0)
                goto L_0x00c9
            L_0x007a:
                com.alimama.union.app.aalogin.TaobaoLogin r7 = com.alimama.union.app.aalogin.TaobaoLogin.this
                r7.onNotifyLoginFailed()
                r6.clear()
                java.lang.String r7 = "login_callback"
                java.lang.String r2 = "failed"
                r6.put(r7, r2)
                java.lang.String r7 = "TaobaoLogin"
                com.alimama.union.app.aalogin.TaobaoLogin r2 = com.alimama.union.app.aalogin.TaobaoLogin.this
                android.content.Context r2 = r2.appContext
                java.lang.String r2 = com.alimama.moon.utils.CommonUtils.getProcessName(r2)
                boolean r3 = com.taobao.login4android.Login.checkSessionValid()
                com.taobao.login4android.session.ISession r4 = com.taobao.login4android.Login.session
                if (r4 != 0) goto L_0x009e
                r0 = 1
            L_0x009e:
                com.alimama.union.app.logger.NewMonitorLogger.Login.loginFailed(r7, r6, r2, r3, r0)
                goto L_0x00c9
            L_0x00a2:
                com.alimama.union.app.aalogin.TaobaoLogin r7 = com.alimama.union.app.aalogin.TaobaoLogin.this
                r7.onNotifyLoginSuccess()
                r6.clear()
                java.lang.String r7 = "login_callback"
                java.lang.String r2 = "success"
                r6.put(r7, r2)
                java.lang.String r7 = "TaobaoLogin"
                com.alimama.union.app.aalogin.TaobaoLogin r2 = com.alimama.union.app.aalogin.TaobaoLogin.this
                android.content.Context r2 = r2.appContext
                java.lang.String r2 = com.alimama.moon.utils.CommonUtils.getProcessName(r2)
                boolean r3 = com.taobao.login4android.Login.checkSessionValid()
                com.taobao.login4android.session.ISession r4 = com.taobao.login4android.Login.session
                if (r4 != 0) goto L_0x00c6
                r0 = 1
            L_0x00c6:
                com.alimama.union.app.logger.NewMonitorLogger.Login.loginOther(r7, r6, r2, r3, r0)
            L_0x00c9:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.alimama.union.app.aalogin.TaobaoLogin.LoginBroadcastReceiver.onReceive(android.content.Context, android.content.Intent):void");
        }
    }

    /* access modifiers changed from: private */
    public void onNotifyLoginFailed() {
        clearAccount();
    }

    private class GetMemberInfoAsyncTask extends AsyncTask<Long, Integer, GetMemberInfoRet> {
        private final Context appContext;
        private final IEventBus eventBus;
        private final ILogin.ILoginListener listener;
        private Long taobaoNumId;
        private final IWebService webService;

        public GetMemberInfoAsyncTask(Context context, IWebService iWebService, IEventBus iEventBus, ILogin.ILoginListener iLoginListener) {
            this.appContext = context;
            this.webService = iWebService;
            this.eventBus = iEventBus;
            this.listener = iLoginListener;
        }

        /* access modifiers changed from: protected */
        public GetMemberInfoRet doInBackground(Long... lArr) {
            GetMemberInfoRet getMemberInfoRet = new GetMemberInfoRet();
            this.taobaoNumId = lArr[0];
            UserMemberinfoGetRequest userMemberinfoGetRequest = new UserMemberinfoGetRequest();
            userMemberinfoGetRequest.setTaobaoNumId(this.taobaoNumId.longValue());
            try {
                getMemberInfoRet.retCode = "SUCCESS";
                getMemberInfoRet.data = ((MtopAlimamaMoonProviderUserMemberinfoGetResponse) this.webService.get((IMTOPDataObject) userMemberinfoGetRequest, MtopAlimamaMoonProviderUserMemberinfoGetResponse.class)).getData();
            } catch (MtopException e) {
                getMemberInfoRet.retCode = e.getRetCode();
            }
            return getMemberInfoRet;
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(GetMemberInfoRet getMemberInfoRet) {
            if (getMemberInfoRet == null) {
                handleSystemError();
            } else if (TaobaoLogin.this.bypassingMemberInfoCheck()) {
                handleGetMemberInfoSuccess(getMemberInfoRet.data);
            } else {
                String str = getMemberInfoRet.retCode;
                char c = 65535;
                switch (str.hashCode()) {
                    case -2091610046:
                        if (str.equals("FAIL_BIZ_TAOBAO_ACCOUNT_NOT_SECURITY")) {
                            c = 4;
                            break;
                        }
                        break;
                    case -1938251650:
                        if (str.equals("FAIL_BIZ_MAMA_REGISTER_CONDITION_NOT_FULFILL")) {
                            c = 3;
                            break;
                        }
                        break;
                    case -1435538978:
                        if (str.equals("FAIL_BIZ_TAOBAO_ACCOUNT_NOT_EXISTS")) {
                            c = 1;
                            break;
                        }
                        break;
                    case -1149187101:
                        if (str.equals("SUCCESS")) {
                            c = 5;
                            break;
                        }
                        break;
                    case -709017636:
                        if (str.equals("FAIL_BIZ_PARAMETER")) {
                            c = 0;
                            break;
                        }
                        break;
                    case -319291070:
                        if (str.equals("FAIL_BIZ_SYSTEM_ERR")) {
                            c = 2;
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                    case 1:
                    case 2:
                        handleSystemError();
                        return;
                    case 3:
                        handleRegisterConditionNotFulfill();
                        return;
                    case 4:
                        handleTaobaoAccountNotSecurity();
                        return;
                    case 5:
                        handleGetMemberInfoSuccess(getMemberInfoRet.data);
                        return;
                    default:
                        return;
                }
            }
        }

        private void handleTaobaoAccountNotSecurity() {
            Login.logout();
            this.eventBus.post(new LoginEvent.TaobaoAccountNotSecurityEvent());
            if (this.listener != null) {
                this.listener.onLoginFailure(this.appContext.getString(R.string.tb_account_not_security));
            }
        }

        private void handleRegisterConditionNotFulfill() {
            Login.logout();
            this.eventBus.post(new LoginEvent.NotMatchAccountConditionEvent());
            if (this.listener != null) {
                this.listener.onLoginFailure(this.appContext.getString(R.string.tb_account_not_allow));
            }
        }

        private void handleGetMemberInfoSuccess(UserMemberinfoGetResponseData userMemberinfoGetResponseData) {
            Long unused = TaobaoLogin.this.memberId = Long.valueOf(userMemberinfoGetResponseData.getMemberId());
            if (TaobaoLogin.this.bypassingMemberInfoCheck()) {
                TaobaoLogin.this.saveAccount();
                return;
            }
            switch ((int) userMemberinfoGetResponseData.getStatus()) {
                case 0:
                    if (userMemberinfoGetResponseData.getMemberId() > 0) {
                        TaobaoLogin.this.saveAccount();
                        return;
                    } else {
                        handleSystemError();
                        return;
                    }
                case 1:
                    this.eventBus.post(new LoginEvent.NeedAgreementEvent());
                    return;
                case 2:
                    Login.logout();
                    this.eventBus.post(new LoginEvent.MamaAccountFrozenEvent());
                    if (this.listener != null) {
                        this.listener.onLoginFailure(this.appContext.getString(R.string.member_account_unused));
                        return;
                    }
                    return;
                default:
                    return;
            }
        }

        private void handleSystemError() {
            Login.logout();
            this.eventBus.post(new LoginEvent.LoginSystemErrorEvent());
            if (this.listener != null) {
                this.listener.onLoginFailure(this.appContext.getString(R.string.server_exception));
            }
        }
    }

    /* access modifiers changed from: private */
    public boolean bypassingMemberInfoCheck() {
        boolean isDailyEnv = EnvHelper.getInstance().isDailyEnv();
        if (isDailyEnv) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    ToastUtil.showToast(TaobaoLogin.this.appContext, "正在绕过阿里妈妈账号的身份验证!!!!");
                }
            });
        }
        return isDailyEnv;
    }

    private static class GetMemberInfoRet {
        public UserMemberinfoGetResponseData data;
        public String retCode;

        private GetMemberInfoRet() {
        }
    }
}
