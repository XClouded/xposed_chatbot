package com.ali.user.mobile.app.common.init;

import android.content.SharedPreferences;
import android.text.TextUtils;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.app.dataprovider.IDataProvider;
import com.ali.user.mobile.app.init.AliUserInit;
import com.ali.user.mobile.config.AliuserGlobals;
import com.ali.user.mobile.log.TLogAdapter;
import com.taobao.login4android.session.SessionManager;
import java.util.concurrent.atomic.AtomicBoolean;

public class LaunchInit {
    private static AtomicBoolean INITED = new AtomicBoolean(false);

    public static void init(IDataProvider iDataProvider) {
        if (!INITED.compareAndSet(false, true)) {
            TLogAdapter.e("login.LaunchInit", "duplicate init");
        } else if (iDataProvider == null || iDataProvider.getContext() == null) {
            TLogAdapter.e("login.LaunchInit", (Throwable) new RuntimeException("DataProvider object is null, Failed to initialize"));
        } else {
            DataProviderFactory.setDataProvider(iDataProvider);
            AliUserInit.init();
            initACCSLogin();
        }
    }

    private static void initACCSLogin() {
        String[] split;
        SharedPreferences sharedPreferences = DataProviderFactory.getApplicationContext().getSharedPreferences(SessionManager.USERINFO, 4);
        String string = sharedPreferences.getString("accs_login", "");
        if (!TextUtils.isEmpty(string) && (split = TextUtils.split(string, "\u0005")) != null && split.length == 2) {
            try {
                if (System.currentTimeMillis() - Long.parseLong(split[1]) <= 300000) {
                    AliuserGlobals.NEED_ACCS_LOGIN = true;
                    return;
                }
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.remove("accs_login");
                edit.apply();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        AliuserGlobals.NEED_ACCS_LOGIN = false;
    }
}
