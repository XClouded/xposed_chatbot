package com.alibaba.ha.adapter.plugin;

import android.content.Context;
import android.util.Log;
import com.alibaba.ha.adapter.AliHaAdapter;
import com.alibaba.ha.adapter.Plugin;
import com.alibaba.ha.adapter.service.crash.CrashActivityCallBack;
import com.alibaba.ha.protocol.AliHaParam;
import com.alibaba.ha.protocol.AliHaPlugin;
import com.alibaba.motu.crashreporter.MotuCrashReporter;
import com.alibaba.motu.crashreporter.ReporterConfigure;
import java.util.concurrent.atomic.AtomicBoolean;

public class CrashReporterPlugin implements AliHaPlugin {
    AtomicBoolean enabling = new AtomicBoolean(false);

    public String getName() {
        return Plugin.crashreporter.name();
    }

    public void start(AliHaParam aliHaParam) {
        String str = aliHaParam.appId;
        String str2 = aliHaParam.appKey;
        String str3 = aliHaParam.appVersion;
        Context context = aliHaParam.context;
        if (context == null || str == null || str2 == null || str3 == null) {
            Log.e(AliHaAdapter.TAG, "param is unlegal, crashreporter plugin start failure ");
            return;
        }
        String str4 = aliHaParam.channel;
        String str5 = aliHaParam.userNick;
        String str6 = AliHaAdapter.TAG;
        Log.i(str6, "init crashreporter, appId is " + str + " appKey is " + str2 + " appVersion is " + str3 + " channel is " + str4 + " userNick is " + str5);
        if (this.enabling.compareAndSet(false, true)) {
            ReporterConfigure reporterConfigure = new ReporterConfigure();
            reporterConfigure.setEnableDumpSysLog(true);
            reporterConfigure.setEnableDumpRadioLog(true);
            reporterConfigure.setEnableDumpEventsLog(true);
            reporterConfigure.setEnableCatchANRException(true);
            reporterConfigure.enableDeduplication = false;
            try {
                MotuCrashReporter.getInstance().enable(context, str, str2, str3, str4, str5, reporterConfigure);
            } catch (Exception e) {
                Log.e(AliHaAdapter.TAG, "crashreporter plugin start failure ", e);
            }
            AliHaAdapter.getInstance().crashService.addJavaCrashListener(new CrashActivityCallBack());
        }
    }
}
