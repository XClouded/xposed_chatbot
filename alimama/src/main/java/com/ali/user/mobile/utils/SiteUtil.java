package com.ali.user.mobile.utils;

import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.taobao.login4android.session.SessionManager;

public class SiteUtil {
    public static boolean isMultiSite() {
        return DataProviderFactory.getDataProvider().getSupportedSites() != null && DataProviderFactory.getDataProvider().getSupportedSites().size() > 1;
    }

    public static int getDefaultLoginSite() {
        if (isMultiSite()) {
            return SessionManager.getInstance(DataProviderFactory.getApplicationContext()).getLoginSite();
        }
        return DataProviderFactory.getDataProvider().getSite();
    }

    public static int getBindRealmBySite() {
        switch (getDefaultLoginSite()) {
            case 18:
                return 55;
            case 19:
                return 71;
            default:
                return 55;
        }
    }
}
