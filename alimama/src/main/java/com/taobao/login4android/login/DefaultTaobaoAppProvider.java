package com.taobao.login4android.login;

import android.text.TextUtils;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.app.login.dataprovider.TaobaoAppProvider;
import com.ali.user.mobile.app.report.info.Location;
import com.ali.user.mobile.service.RpcService;
import com.ali.user.mobile.service.ServiceFactory;
import com.ali.user.mobile.service.StorageService;
import com.taobao.login4android.Login;
import com.taobao.login4android.config.LoginSwitch;
import com.taobao.login4android.session.encode.PhoneInfo;

public class DefaultTaobaoAppProvider extends TaobaoAppProvider {
    public String getAppkey() {
        if (TextUtils.isEmpty(this.appKey)) {
            switch (getEnvType()) {
                case 0:
                case 1:
                    this.appKey = ((StorageService) ServiceFactory.getService(StorageService.class)).getAppKey(2);
                    break;
                default:
                    this.appKey = ((StorageService) ServiceFactory.getService(StorageService.class)).getAppKey(0);
                    break;
            }
        }
        return this.appKey;
    }

    public String getDeviceId() {
        this.deviceId = ((RpcService) ServiceFactory.getService(RpcService.class)).getDeviceId();
        return this.deviceId;
    }

    public boolean isTaobaoApp() {
        return this.isTaobaoApp;
    }

    public String getImei() {
        return PhoneInfo.getImei(DataProviderFactory.getApplicationContext());
    }

    public String getImsi() {
        return PhoneInfo.getImsi(DataProviderFactory.getApplicationContext());
    }

    public boolean isAPDIDDegrade() {
        return LoginSwitch.getSwitch(LoginSwitch.APDID_DEGRADE_SWITCH, "false");
    }

    public boolean isFindPWDDegrade() {
        return LoginSwitch.getSwitch(LoginSwitch.FINDPWD_DEGRADE_SWITCH, "false");
    }

    public boolean isReportDegrade() {
        return LoginSwitch.getSwitch(LoginSwitch.REPORTDEVICE_DEGRADE_SWITCH, "false");
    }

    public Location getLocation() {
        if (Login.getLocationProvider() != null) {
            return Login.getLocationProvider().getLocation();
        }
        return null;
    }

    public boolean isForbidLoginFromBackground() {
        return LoginSwitch.getSwitch(LoginSwitch.FORBID_LOGIN_FROM_BACKGROUND, "false");
    }

    public boolean needAccsLogin() {
        return LoginSwitch.getSwitch(LoginSwitch.ACCS_LOGIN_SWITCH, "false");
    }

    public boolean enableAlipaySSO() {
        return LoginSwitch.getSwitch(LoginSwitch.ALIPAY_SSO_SWITCH, "true") && this.enableAlipaySSO;
    }

    public boolean useSeparateThreadPool() {
        return LoginSwitch.getSwitch(LoginSwitch.USE_SEPARATE_THREADPOOL, "true") && this.useSeparateThreadPool;
    }

    public boolean isRefreshCookiesDegrade() {
        return LoginSwitch.getSwitch(LoginSwitch.REFRESH_COOKIE_DEGRADE_SWITCH, "true");
    }

    public boolean isForbiddenRefreshCookieInAutoLogin() {
        return LoginSwitch.getSwitch(LoginSwitch.FORBIDDEN_REFRESH_COOKIE_IN_AUTO_LOGIN, "false");
    }

    public boolean isAccountChangeDegrade() {
        return LoginSwitch.getSwitch(LoginSwitch.MULTI_ACCOUNT_CHANGE_DEGRADE_SWITCH, "false");
    }

    public boolean isShowFamilyAccountTip() {
        return LoginSwitch.getSwitch(LoginSwitch.FAMILY_ACCOUNT_TIP_SWITCH, "false");
    }

    public boolean supportFaceLogin() {
        return LoginSwitch.getSwitch(LoginSwitch.SUPPORT_FACE_LOGIN, "true") && this.supportFaceLogin;
    }
}
