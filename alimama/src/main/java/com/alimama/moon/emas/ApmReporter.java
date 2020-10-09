package com.alimama.moon.emas;

import alimama.com.unwetaologger.base.UNWLogger;
import android.app.Application;
import androidx.annotation.NonNull;
import com.alibaba.motu.tbrest.SendService;
import com.alimama.moon.BuildConfig;
import com.taobao.login4android.Login;
import com.taobao.monitor.adapter.OtherAppApmInitiator;
import com.taobao.monitor.impl.common.DynamicConstants;
import com.taobao.monitor.impl.logger.Logger;
import com.taobao.monitor.impl.processor.launcher.PageList;
import com.taobao.orange.OConstant;
import com.ut.device.UTDevice;
import java.util.HashMap;

public class ApmReporter {
    public static void init(@NonNull Application application, @NonNull String str) {
        Logger.setDebug(false);
        SendService instance = SendService.getInstance();
        instance.init(application, str + "@android", str, BuildConfig.VERSION_NAME, BuildConfig.TTID, Login.getNick());
        HashMap hashMap = new HashMap();
        hashMap.put("deviceId", UTDevice.getUtdid(application));
        hashMap.put(OConstant.LAUNCH_ONLINEAPPKEY, str);
        hashMap.put("appVersion", BuildConfig.VERSION_NAME);
        hashMap.put(UNWLogger.LOG_VALUE_TYPE_PROCESS, application.getPackageName());
        hashMap.put("ttid", BuildConfig.TTID);
        hashMap.put("channel", BuildConfig.TTID);
        new OtherAppApmInitiator().init(application, hashMap);
        PageList.addBlackPage("com.alimama.moon.ui.splashad.SplashAdActivity");
        PageList.addBlackPage("com.alimama.moon.ui.WizardActivity");
        PageList.addWhitePage("com.alimama.moon.ui.BottomNavActivity");
        DynamicConstants.needFragment = true;
    }
}
