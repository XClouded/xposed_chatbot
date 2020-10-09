package com.alibaba.ha.adapter.plugin;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import com.alibaba.ha.adapter.AliHaAdapter;
import com.alibaba.ha.adapter.Plugin;
import com.alibaba.ha.adapter.service.telescope.TaobaoOnlineStatistics;
import com.alibaba.ha.protocol.AliHaParam;
import com.alibaba.ha.protocol.AliHaPlugin;
import com.taobao.onlinemonitor.OnLineMonitor;
import com.taobao.onlinemonitor.OnLineMonitorApp;
import java.util.concurrent.atomic.AtomicBoolean;

public class OnLineMonitorPlugin implements AliHaPlugin {
    AtomicBoolean enabling = new AtomicBoolean(false);

    public String getName() {
        return Plugin.onlineMonitor.name();
    }

    public void start(AliHaParam aliHaParam) {
        try {
            String str = aliHaParam.appId;
            String str2 = aliHaParam.appKey;
            String str3 = aliHaParam.appVersion;
            Application application = aliHaParam.application;
            Context context = aliHaParam.context;
            if (!(context == null || application == null || str == null || str2 == null)) {
                if (str3 != null) {
                    String str4 = AliHaAdapter.TAG;
                    Log.i(str4, "init onlineMonitor, appId is " + str + " appKey is " + str2 + " appVersion is " + str3);
                    if (this.enabling.compareAndSet(false, true)) {
                        initOnlineMonitor(application, context);
                        return;
                    }
                    return;
                }
            }
            Log.e(AliHaAdapter.TAG, "param is unlegal, crashreporter plugin start failure ");
        } catch (Exception unused) {
        }
    }

    private void initOnlineMonitor(Application application, Context context) {
        if (application != null && Build.VERSION.SDK_INT >= 14) {
            OnLineMonitorApp.init(application, context);
            OnLineMonitor.registerOnlineStatistics(new TaobaoOnlineStatistics());
            OnLineMonitor.start();
            OnLineMonitorApp.sIsDebug = false;
        }
    }
}
