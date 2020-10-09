package com.taobao.orange.launch;

import android.app.Application;
import com.taobao.orange.OConfig;
import com.taobao.orange.OConstant;
import com.taobao.orange.OrangeConfig;
import com.taobao.orange.util.OLog;
import java.io.Serializable;
import java.util.HashMap;

public class HTaoLaunchOrange implements Serializable {
    private static final String TAG = "HTaoLaunchOrange";

    public void init(Application application, HashMap<String, Object> hashMap) {
        String str;
        int i;
        String str2;
        OLog.d(TAG, "init start", new Object[0]);
        String str3 = "23070080";
        int envMode = OConstant.ENV.ONLINE.getEnvMode();
        try {
            str = (String) hashMap.get("appVersion");
            try {
                i = ((Integer) hashMap.get(OConstant.LAUNCH_ENVINDEX)).intValue();
            } catch (Throwable th) {
                th = th;
                i = envMode;
                OLog.e(TAG, "init", th, new Object[0]);
                OrangeConfig.getInstance().init(application, new OConfig.Builder().setAppKey(str3).setAppVersion(str).setEnv(i).setServerType(OConstant.SERVER.TAOBAO.ordinal()).setIndexUpdateMode(OConstant.UPDMODE.O_XMD.ordinal()).setReportAck(false).build());
            }
            try {
                if (i == OConstant.ENV.ONLINE.getEnvMode()) {
                    str2 = (String) hashMap.get(OConstant.LAUNCH_ONLINEAPPKEY);
                } else if (i == OConstant.ENV.PREPARE.getEnvMode()) {
                    str2 = (String) hashMap.get(OConstant.LAUNCH_PREAPPKEY);
                } else {
                    str2 = (String) hashMap.get(OConstant.LAUNCH_TESTAPPKEY);
                }
                str3 = str2;
            } catch (Throwable th2) {
                th = th2;
                OLog.e(TAG, "init", th, new Object[0]);
                OrangeConfig.getInstance().init(application, new OConfig.Builder().setAppKey(str3).setAppVersion(str).setEnv(i).setServerType(OConstant.SERVER.TAOBAO.ordinal()).setIndexUpdateMode(OConstant.UPDMODE.O_XMD.ordinal()).setReportAck(false).build());
            }
        } catch (Throwable th3) {
            th = th3;
            str = "*";
            i = envMode;
            OLog.e(TAG, "init", th, new Object[0]);
            OrangeConfig.getInstance().init(application, new OConfig.Builder().setAppKey(str3).setAppVersion(str).setEnv(i).setServerType(OConstant.SERVER.TAOBAO.ordinal()).setIndexUpdateMode(OConstant.UPDMODE.O_XMD.ordinal()).setReportAck(false).build());
        }
        OrangeConfig.getInstance().init(application, new OConfig.Builder().setAppKey(str3).setAppVersion(str).setEnv(i).setServerType(OConstant.SERVER.TAOBAO.ordinal()).setIndexUpdateMode(OConstant.UPDMODE.O_XMD.ordinal()).setReportAck(false).build());
    }
}
