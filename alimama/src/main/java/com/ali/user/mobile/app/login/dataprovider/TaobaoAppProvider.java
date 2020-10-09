package com.ali.user.mobile.app.login.dataprovider;

import com.ali.user.mobile.app.dataprovider.DataProvider;

public class TaobaoAppProvider extends DataProvider {
    public boolean isFindPWDDegrade() {
        return false;
    }

    public boolean isForbiddenRefreshCookieInAutoLogin() {
        return false;
    }

    public boolean isRefreshCookiesDegrade() {
        return true;
    }

    public boolean isReportDegrade() {
        return false;
    }
}
