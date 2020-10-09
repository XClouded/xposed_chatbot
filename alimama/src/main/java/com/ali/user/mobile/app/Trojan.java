package com.ali.user.mobile.app;

import android.text.TextUtils;
import com.ali.money.shield.mssdk.api.SecurityManager;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.app.init.Debuggable;
import com.ali.user.mobile.utils.EnvUtil;
import com.taobao.login4android.thread.LoginThreadHelper;
import java.util.HashMap;

public class Trojan {
    public static void antiTrojan(String str, boolean z) {
        if (isMssdkExist()) {
            try {
                if (!TextUtils.equals(LoginThreadHelper.getCurProcessName(DataProviderFactory.getApplicationContext()), DataProviderFactory.getApplicationContext().getPackageName())) {
                    return;
                }
            } catch (Throwable unused) {
            }
            HashMap hashMap = new HashMap();
            if (str == null) {
                str = "";
            }
            hashMap.put("userId", str);
            hashMap.put("dailyIndex", 2);
            hashMap.put("onlineIndex", 0);
            hashMap.put("ttid", DataProviderFactory.getDataProvider().getTTID());
            if (TextUtils.isEmpty(str)) {
                hashMap.put("initNoScan", true);
            }
            hashMap.put("envMode", Integer.valueOf(EnvUtil.getAlimmsdk_env()));
            if (Debuggable.isDebug()) {
                hashMap.put("debugLog", true);
            }
            SecurityManager.getInstance(DataProviderFactory.getApplicationContext()).init(hashMap);
        }
    }

    private static boolean isMssdkExist() {
        try {
            Class.forName("com.ali.money.shield.mssdk.api.SecurityManager");
            return true;
        } catch (ClassNotFoundException unused) {
            return false;
        }
    }
}
