package com.alibaba.ha.core;

import android.util.Log;
import com.alibaba.ha.protocol.AliHaParam;
import com.alibaba.ha.protocol.AliHaPlugin;
import com.taobao.weex.el.parse.Operators;
import java.util.ArrayList;
import java.util.List;

public class AliHaCore {
    private final String TAG;
    private List<AliHaPlugin> plugins;

    private AliHaCore() {
        this.plugins = new ArrayList();
        this.TAG = "AliHaCore";
    }

    private static class InstanceCreater {
        /* access modifiers changed from: private */
        public static AliHaCore instance = new AliHaCore();

        private InstanceCreater() {
        }
    }

    public static synchronized AliHaCore getInstance() {
        AliHaCore access$100;
        synchronized (AliHaCore.class) {
            access$100 = InstanceCreater.instance;
        }
        return access$100;
    }

    public void registPlugin(AliHaPlugin aliHaPlugin) throws Exception {
        if (aliHaPlugin != null) {
            this.plugins.add(aliHaPlugin);
        }
    }

    public void start(AliHaParam aliHaParam) throws Exception {
        for (AliHaPlugin startWithPlugin : this.plugins) {
            startWithPlugin(aliHaParam, startWithPlugin);
        }
    }

    public void startWithPlugin(AliHaParam aliHaParam, AliHaPlugin aliHaPlugin) {
        if (aliHaParam != null && aliHaPlugin != null) {
            String name = aliHaPlugin.getName();
            Long valueOf = Long.valueOf(System.currentTimeMillis());
            if (name == null) {
                name = "Unknown";
            }
            Log.i("AliHaCore", "start init plugin " + name);
            aliHaPlugin.start(aliHaParam);
            Log.i("AliHaCore", "end init plugin " + name + Operators.SPACE_STR + (System.currentTimeMillis() - valueOf.longValue()) + "ms");
        }
    }
}
