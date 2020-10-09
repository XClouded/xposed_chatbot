package com.alibaba.ha.adapter.plugin;

import android.app.Application;
import android.util.Log;
import com.alibaba.ha.adapter.AliHaAdapter;
import com.alibaba.ha.adapter.Plugin;
import com.alibaba.ha.adapter.service.RandomService;
import com.alibaba.ha.protocol.AliHaParam;
import com.alibaba.ha.protocol.AliHaPlugin;
import com.alibaba.mtl.appmonitor.AppMonitor;
import com.ut.mini.IUTApplication;
import com.ut.mini.UTAnalytics;
import com.ut.mini.core.sign.IUTRequestAuthentication;
import com.ut.mini.core.sign.UTBaseRequestAuthentication;
import com.ut.mini.crashhandler.IUTCrashCaughtListner;
import java.util.concurrent.atomic.AtomicBoolean;

public class UtPlugin implements AliHaPlugin {
    AtomicBoolean enabling = new AtomicBoolean(false);

    public String getName() {
        return Plugin.ut.name();
    }

    public void start(AliHaParam aliHaParam) {
        final String str = aliHaParam.appId;
        final String str2 = aliHaParam.appKey;
        final String str3 = aliHaParam.appVersion;
        Application application = aliHaParam.application;
        if (application == null || str == null || str2 == null || str3 == null) {
            Log.e(AliHaAdapter.TAG, "param is unlegal, crashreporter plugin start failure ");
            return;
        }
        String str4 = AliHaAdapter.TAG;
        Log.i(str4, "init ut, appId is " + str + " appKey is " + str2 + " appVersion is " + str3 + " channel is " + aliHaParam.channel);
        if (this.enabling.compareAndSet(false, true)) {
            final AliHaParam aliHaParam2 = aliHaParam;
            UTAnalytics.getInstance().setAppApplicationInstance(application, new IUTApplication() {
                public IUTCrashCaughtListner getUTCrashCraughtListener() {
                    return null;
                }

                public boolean isUTLogEnable() {
                    return true;
                }

                public String getUTAppVersion() {
                    return str3;
                }

                public String getUTChannel() {
                    return aliHaParam2.channel;
                }

                public IUTRequestAuthentication getUTRequestAuthInstance() {
                    String randomNum = new RandomService().getRandomNum();
                    if (randomNum == null) {
                        randomNum = "8951ae070be6560f4fc1401e90a83a4e";
                    }
                    return new UTBaseRequestAuthentication(str2, randomNum);
                }

                public boolean isAliyunOsSystem() {
                    return str.endsWith("aliyunos");
                }

                public boolean isUTCrashHandlerDisable() {
                    Log.i(AliHaAdapter.TAG, "close ut crash handler success");
                    return true;
                }
            });
            try {
                AppMonitor.init(application);
                AppMonitor.setRequestAuthInfo(false, aliHaParam.appKey, aliHaParam.appSecret);
                AppMonitor.setChannel(aliHaParam.channel);
            } catch (Exception unused) {
            }
        }
    }
}
