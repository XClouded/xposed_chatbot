package com.alibaba.ha.adapter;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import com.alibaba.ha.adapter.plugin.CrashReporterPlugin;
import com.alibaba.ha.adapter.plugin.factory.PluginFactory;
import com.alibaba.ha.adapter.service.activity.AdapterActivityLifeCycle;
import com.alibaba.ha.adapter.service.bizError.BizErrorService;
import com.alibaba.ha.adapter.service.crash.CrashService;
import com.alibaba.ha.adapter.service.telescope.TelescopeService;
import com.alibaba.ha.adapter.service.tlog.TLogService;
import com.alibaba.ha.adapter.service.ut.UtAppMonitor;
import com.alibaba.ha.adapter.service.watch.WatchService;
import com.alibaba.ha.core.AliHaCore;
import com.alibaba.ha.protocol.AliHaParam;
import com.alibaba.ha.protocol.AliHaPlugin;
import com.alibaba.motu.crashreporter.MotuCrashReporter;
import com.alibaba.motu.tbrest.SendService;
import com.taobao.onlinemonitor.OnLineMonitorApp;
import java.util.ArrayList;
import java.util.List;

public class AliHaAdapter {
    public static String TAG = "AliHaAdapter";
    public BizErrorService bizErrorService;
    private List<Plugin> blackPlugin;
    public Context context;
    public CrashService crashService;
    public TLogService tLogService;
    public TelescopeService telescopeService;
    public UtAppMonitor utAppMonitor;
    public WatchService watchService;

    private AliHaAdapter() {
        this.blackPlugin = new ArrayList();
        this.bizErrorService = new BizErrorService();
        this.crashService = new CrashService();
        this.telescopeService = new TelescopeService();
        this.tLogService = new TLogService();
        this.watchService = new WatchService();
        this.utAppMonitor = new UtAppMonitor();
        this.context = null;
    }

    private static class InstanceCreater {
        /* access modifiers changed from: private */
        public static AliHaAdapter instance = new AliHaAdapter();

        private InstanceCreater() {
        }
    }

    public static synchronized AliHaAdapter getInstance() {
        AliHaAdapter access$100;
        synchronized (AliHaAdapter.class) {
            access$100 = InstanceCreater.instance;
        }
        return access$100;
    }

    public void removePugin(Plugin plugin) {
        if (plugin != null) {
            String str = TAG;
            Log.w(str, "plugin add to black list success, plugin name is " + plugin.name());
            this.blackPlugin.add(plugin);
        }
    }

    public Boolean startWithBlackPlugin(AliHaConfig aliHaConfig, List<Plugin> list) {
        if (!isLegal(aliHaConfig).booleanValue()) {
            return false;
        }
        if (list != null && list.size() > 0) {
            this.blackPlugin.addAll(list);
        }
        return start(aliHaConfig);
    }

    public Boolean startWithPlugin(AliHaConfig aliHaConfig, Plugin plugin) {
        if (!isLegal(aliHaConfig).booleanValue()) {
            return false;
        }
        AliHaParam buildParam = buildParam(aliHaConfig);
        AliHaPlugin createPlugin = PluginFactory.createPlugin(plugin);
        if (createPlugin == null) {
            return false;
        }
        AliHaCore.getInstance().startWithPlugin(buildParam, createPlugin);
        return true;
    }

    public Boolean start(AliHaConfig aliHaConfig) {
        if (!isLegal(aliHaConfig).booleanValue()) {
            return false;
        }
        AliHaParam buildParam = buildParam(aliHaConfig);
        try {
            if (!this.blackPlugin.contains(Plugin.crashreporter)) {
                AliHaCore.getInstance().startWithPlugin(buildParam, new CrashReporterPlugin());
            } else {
                SendService.getInstance().init(buildParam.context, buildParam.appId, buildParam.appKey, buildParam.appVersion, buildParam.channel, buildParam.userNick);
                String str = TAG;
                Log.i(str, "init send service success, appId is " + buildParam.appId + " appKey is " + buildParam.appKey + " appVersion is " + buildParam.appVersion + " channel is " + buildParam.channel + " userNick is " + buildParam.userNick);
            }
            if (!this.blackPlugin.contains(Plugin.ut)) {
                AliHaCore.getInstance().registPlugin(PluginFactory.createPlugin(Plugin.ut));
            } else {
                printBlackPluginWarn(Plugin.ut.name());
            }
            if (!this.blackPlugin.contains(Plugin.bizErrorReporter)) {
                AliHaCore.getInstance().registPlugin(PluginFactory.createPlugin(Plugin.bizErrorReporter));
            } else {
                printBlackPluginWarn(Plugin.bizErrorReporter.name());
            }
            if (!this.blackPlugin.contains(Plugin.onlineMonitor)) {
                AliHaCore.getInstance().registPlugin(PluginFactory.createPlugin(Plugin.onlineMonitor));
            } else {
                printBlackPluginWarn(Plugin.onlineMonitor.name());
            }
            if (!this.blackPlugin.contains(Plugin.telescope)) {
                AliHaCore.getInstance().registPlugin(PluginFactory.createPlugin(Plugin.telescope));
            } else {
                printBlackPluginWarn(Plugin.telescope.name());
            }
            if (!this.blackPlugin.contains(Plugin.tlog)) {
                AliHaCore.getInstance().registPlugin(PluginFactory.createPlugin(Plugin.tlog));
            } else {
                printBlackPluginWarn(Plugin.tlog.name());
            }
            if (!this.blackPlugin.contains(Plugin.watch)) {
                AliHaCore.getInstance().registPlugin(PluginFactory.createPlugin(Plugin.watch));
            } else {
                printBlackPluginWarn(Plugin.watch.name());
            }
            AliHaCore.getInstance().start(buildParam);
            if (Build.VERSION.SDK_INT >= 14) {
                buildParam.application.registerActivityLifecycleCallbacks(new AdapterActivityLifeCycle());
            } else {
                Log.w(TAG, String.format("build version %s not suppert, registerActivityLifecycleCallbacks failed", new Object[]{Integer.valueOf(Build.VERSION.SDK_INT)}));
            }
            return true;
        } catch (Exception e) {
            Log.e(TAG, "start plugin error ", e);
            return false;
        }
    }

    private AliHaParam buildParam(AliHaConfig aliHaConfig) {
        AliHaParam aliHaParam = new AliHaParam();
        aliHaParam.application = aliHaConfig.application;
        aliHaParam.context = aliHaConfig.context;
        aliHaParam.appKey = aliHaConfig.appKey;
        if (aliHaConfig.isAliyunos.booleanValue()) {
            aliHaParam.appId = aliHaParam.appKey + "@aliyunos";
        } else {
            aliHaParam.appId = aliHaParam.appKey + "@android";
        }
        aliHaParam.appVersion = aliHaConfig.appVersion;
        aliHaParam.channel = aliHaConfig.channel;
        aliHaParam.userNick = aliHaConfig.userNick;
        return aliHaParam;
    }

    private Boolean isLegal(AliHaConfig aliHaConfig) {
        if (aliHaConfig == null) {
            Log.e(TAG, "config is null ");
            return false;
        } else if (aliHaConfig.application == null) {
            Log.e(TAG, "application is null ");
            return false;
        } else if (aliHaConfig.context == null) {
            Log.e(TAG, "context is null ");
            return false;
        } else if (aliHaConfig.appKey == null || aliHaConfig.appVersion == null) {
            String str = TAG;
            Log.e(str, "config is unlegal, ha plugin start failure  appKey is " + aliHaConfig.appKey + " appVersion is " + aliHaConfig.appVersion);
            return false;
        } else {
            this.context = aliHaConfig.context;
            return true;
        }
    }

    private void printBlackPluginWarn(String str) {
        String str2 = TAG;
        Log.w(str2, "plugin " + str + " in black list, remove plugin success! ");
    }

    public void openDebug(Boolean bool) {
        this.tLogService.OpenDebug(bool);
        OnLineMonitorApp.sIsDebug = true;
    }

    public void updateVersion(String str) {
        this.crashService.updateApppVersion(str);
    }

    public void updateUserNick(String str) {
        this.crashService.updateUserNick(str);
    }

    public void updateChannel(String str) {
        this.crashService.updateChannel(str);
    }

    public void changeHost(String str) {
        if (str != null) {
            MotuCrashReporter.getInstance().changeHost(str);
            SendService.getInstance().changeHost(str);
        }
    }

    public void openHttp(Boolean bool) {
        if (bool != null) {
            SendService.getInstance().openHttp = bool;
        }
    }

    public void openPublishEmasHa() {
        changeHost("adash-emas.cn-hangzhou.aliyuncs.com");
    }

    public void changeAppSecretKey(String str) {
        if (str != null) {
            SendService.getInstance().appSecret = str;
        }
    }
}
