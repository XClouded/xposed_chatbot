package com.alibaba.ha.adapter.plugin.factory;

import android.util.Log;
import com.alibaba.ha.adapter.AliHaAdapter;
import com.alibaba.ha.adapter.Plugin;
import com.alibaba.ha.adapter.plugin.BizErrorPlugin;
import com.alibaba.ha.adapter.plugin.CrashReporterPlugin;
import com.alibaba.ha.adapter.plugin.OnLineMonitorPlugin;
import com.alibaba.ha.adapter.plugin.TLogPlugin;
import com.alibaba.ha.adapter.plugin.TelescopePlugin;
import com.alibaba.ha.adapter.plugin.UtPlugin;
import com.alibaba.ha.adapter.plugin.WatchPlugin;
import com.alibaba.ha.protocol.AliHaPlugin;

public class PluginFactory {
    public static AliHaPlugin createPlugin(Plugin plugin) {
        switch (plugin) {
            case ut:
                return new UtPlugin();
            case tlog:
                return new TLogPlugin();
            case bizErrorReporter:
                return new BizErrorPlugin();
            case watch:
                return new WatchPlugin();
            case telescope:
                return new TelescopePlugin();
            case onlineMonitor:
                return new OnLineMonitorPlugin();
            case crashreporter:
                return new CrashReporterPlugin();
            default:
                Log.w(AliHaAdapter.TAG, "plugin not exist! ");
                return null;
        }
    }
}
