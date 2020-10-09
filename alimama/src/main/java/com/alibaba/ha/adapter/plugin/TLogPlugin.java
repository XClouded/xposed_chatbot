package com.alibaba.ha.adapter.plugin;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import com.alibaba.ha.adapter.AliHaAdapter;
import com.alibaba.ha.adapter.Plugin;
import com.alibaba.ha.adapter.service.RandomService;
import com.alibaba.ha.adapter.service.godeye.GodEyeAppAllInfoListener;
import com.alibaba.ha.adapter.service.godeye.GodEyeOnAccurateBootListener;
import com.alibaba.ha.adapter.service.tlog.TLogMonitorImpl;
import com.alibaba.ha.protocol.AliHaParam;
import com.alibaba.ha.protocol.AliHaPlugin;
import com.alibaba.motu.tbrest.utils.AppUtils;
import com.alibaba.motu.tbrest.utils.DeviceUtils;
import com.taobao.android.tlog.message.TLogMessage;
import com.taobao.android.tlog.uploader.TLogUploader;
import com.taobao.tao.log.LogLevel;
import com.taobao.tao.log.TLogInitializer;
import com.taobao.tao.log.godeye.GodeyeInitializer;
import java.util.concurrent.atomic.AtomicBoolean;

public class TLogPlugin implements AliHaPlugin {
    AtomicBoolean enabling = new AtomicBoolean(false);

    public String getName() {
        return Plugin.tlog.name();
    }

    public void start(AliHaParam aliHaParam) {
        Application application = aliHaParam.application;
        Context context = aliHaParam.context;
        String str = aliHaParam.appKey;
        String str2 = aliHaParam.appId;
        String str3 = aliHaParam.appVersion;
        String str4 = aliHaParam.userNick;
        String utdid = DeviceUtils.getUtdid(context);
        if (context == null || str == null || str3 == null) {
            Log.e(AliHaAdapter.TAG, "param is unlegal, tlog plugin start failure ");
            return;
        }
        LogLevel logLevel = LogLevel.W;
        String myProcessNameByAppProcessInfo = AppUtils.getMyProcessNameByAppProcessInfo(context);
        if (myProcessNameByAppProcessInfo == null) {
            myProcessNameByAppProcessInfo = "DEFAULT";
        }
        String str5 = myProcessNameByAppProcessInfo;
        String randomNum = new RandomService().getRandomNum();
        if (randomNum == null) {
            randomNum = "8951ae070be6560f4fc1401e90a83a4e";
        }
        String str6 = randomNum;
        String str7 = AliHaAdapter.TAG;
        Log.i(str7, "init tlog, appKey is " + str + " appVersion is " + str3 + " logLevel is " + logLevel + " namePrefix is " + str5);
        if (this.enabling.compareAndSet(false, true)) {
            try {
                TLogInitializer.getInstance().builder(context, logLevel, TLogInitializer.DEFAULT_DIR, str5, str, str3).setApplication(application).setSecurityKey(str6).setUserNick(str4).setUtdid(utdid).setAppId(str2).init();
                TLogMonitorImpl tLogMonitorImpl = new TLogMonitorImpl();
                tLogMonitorImpl.init();
                TLogInitializer.getInstance().settLogMonitor(tLogMonitorImpl);
                TLogInitializer.getInstance().setLogUploader(new TLogUploader());
                TLogInitializer.getInstance().setMessageSender(new TLogMessage());
                AliHaAdapter.getInstance().telescopeService.addOnAccurateBootListener(new GodEyeOnAccurateBootListener());
                GodeyeInitializer.getInstance().registGodEyeAppListener(new GodEyeAppAllInfoListener());
            } catch (Exception e) {
                Log.e(AliHaAdapter.TAG, "param is unlegal, tlog plugin start failure ", e);
            }
        }
    }
}
