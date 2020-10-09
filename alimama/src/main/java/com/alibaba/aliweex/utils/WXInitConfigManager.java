package com.alibaba.aliweex.utils;

import android.content.SharedPreferences;
import com.alibaba.aliweex.AliWeex;
import com.alibaba.aliweex.IConfigAdapter;
import com.alibaba.aliweex.adapter.IConfigGeneratorAdapter;
import com.alibaba.aliweex.adapter.IConfigModuleAdapter;
import com.alibaba.aliweex.adapter.IConfigModuleListener;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.cache.RegisterCache;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WXInitConfigManager {
    public static final String INIT_CONFIG_GROUP = "android_weex_ext_config";
    public static final String WXAPM_CONFIG_GROUP = "wxapm";
    private static volatile WXInitConfigManager instance = null;
    public static final String key_back_to_home_when_exception = "backToHomeWhenException";
    public static final String key_enableAutoScan = "enableAutoScan";
    public static final String key_enableBackUpThread = "enableBackUpThread";
    public static final String key_enableBackUpThreadCache = "enableBackUpThreadCache";
    public static final String key_enableRegisterCache = "enableRegisterCache";
    public static final String key_enable_init_async = "enableInitAsync";
    public static final String key_enable_lazy_init = "enableLazyInit";
    public static final String key_enable_mtop_usecache = "enableMtopCache";
    public static final String key_enable_so_loader = "enableInitSoLoader";
    public static final String key_initLeftSize = "initLeftSize";
    private IConfigModuleAdapter adapter = null;
    public ConfigKV c_back_to_home = null;
    public ConfigKV c_enableAutoScan = null;
    public ConfigKV c_enableBackUpThread = null;
    public ConfigKV c_enableBackUpThreadCache = null;
    public ConfigKV c_enableRegisterCache = null;
    public ConfigKV c_enableSoLoader = null;
    public ConfigKV c_enable_alarm_sig = null;
    public ConfigKV c_enable_init_async = null;
    public ConfigKV c_enable_lazy_init = null;
    public ConfigKV c_enable_mtop_cache = null;
    public ConfigKV c_enable_rax_pkg = null;
    public ConfigKV c_initLeftSize = null;
    public ConfigKV c_release_map = null;
    public ConfigKV c_use_runtime_api = null;
    private IConfigModuleListener listener = null;
    /* access modifiers changed from: private */
    public List<ConfigKV> mInitConfigs = new ArrayList();
    private SharedPreferences sharedPreferences = null;

    public static WXInitConfigManager getInstance() {
        if (instance == null) {
            synchronized (WXInitConfigManager.class) {
                if (instance == null) {
                    instance = new WXInitConfigManager();
                }
            }
        }
        return instance;
    }

    public boolean initOk() {
        if (this.sharedPreferences == null) {
            ensureSP();
        }
        return this.adapter != null;
    }

    private WXInitConfigManager() {
        initDefaultConfigs();
        IConfigGeneratorAdapter configGeneratorAdapter = AliWeex.getInstance().getConfigGeneratorAdapter();
        if (configGeneratorAdapter != null) {
            this.adapter = configGeneratorAdapter.generateConfigInstance("");
            this.listener = new IConfigModuleListener() {
                public void onConfigUpdate(String str, Map<String, String> map) {
                    IConfigAdapter configAdapter = AliWeex.getInstance().getConfigAdapter();
                    if (configAdapter != null) {
                        for (ConfigKV configKV : WXInitConfigManager.this.mInitConfigs) {
                            if (configKV.group.equals(str)) {
                                WXInitConfigManager.this.getConfigAndWrite(configAdapter, configKV);
                            }
                        }
                        if ("android_weex_ext_config".equals(str)) {
                            WXInitConfigManager.this.doSomething();
                        }
                    }
                }
            };
            registerListener(new String[]{"android_weex_ext_config", WXAPM_CONFIG_GROUP});
        }
        ensureSP();
    }

    /* access modifiers changed from: private */
    public void getConfigAndWrite(IConfigAdapter iConfigAdapter, ConfigKV configKV) {
        write(configKV.key, iConfigAdapter.getConfig(configKV.group, configKV.key, configKV.defaultValue));
    }

    private void initDefaultConfigs() {
        boolean isTaobao = WXUtil.isTaobao();
        WXLogUtils.e("aliweex initInitConfig:" + isTaobao);
        this.c_enableAutoScan = new ConfigKV(key_enableAutoScan, isTaobao ? "false" : "true", "android_weex_ext_config");
        this.mInitConfigs.add(this.c_enableAutoScan);
        this.c_enableRegisterCache = new ConfigKV(key_enableRegisterCache, isTaobao ? "true" : "false", "android_weex_ext_config");
        this.mInitConfigs.add(this.c_enableRegisterCache);
        this.c_enableBackUpThread = new ConfigKV(key_enableBackUpThread, isTaobao ? "true" : "false", "android_weex_ext_config");
        this.mInitConfigs.add(this.c_enableBackUpThread);
        this.c_enableBackUpThreadCache = new ConfigKV(key_enableBackUpThreadCache, "true", "android_weex_ext_config");
        this.mInitConfigs.add(this.c_enableBackUpThreadCache);
        this.c_enableSoLoader = new ConfigKV(key_enable_so_loader, "true", "android_weex_ext_config");
        this.mInitConfigs.add(this.c_enableSoLoader);
        this.c_initLeftSize = new ConfigKV(key_initLeftSize, "50", "android_weex_ext_config");
        this.mInitConfigs.add(this.c_initLeftSize);
        this.c_enable_lazy_init = new ConfigKV(key_enable_lazy_init, "true", "android_weex_ext_config");
        this.mInitConfigs.add(this.c_enable_lazy_init);
        this.c_enable_init_async = new ConfigKV(key_enable_init_async, "true", "android_weex_ext_config");
        this.mInitConfigs.add(this.c_enable_init_async);
        this.c_back_to_home = new ConfigKV(key_back_to_home_when_exception, "false", "android_weex_ext_config");
        this.mInitConfigs.add(this.c_back_to_home);
        this.c_use_runtime_api = new ConfigKV("use_runtime_api", "0", WXAPM_CONFIG_GROUP);
        this.mInitConfigs.add(this.c_use_runtime_api);
        this.c_enable_alarm_sig = new ConfigKV("enableAlarmSignal", "true", WXAPM_CONFIG_GROUP);
        this.mInitConfigs.add(this.c_enable_alarm_sig);
        this.c_enable_rax_pkg = new ConfigKV("loadRaxPkg", "true", WXAPM_CONFIG_GROUP);
        this.mInitConfigs.add(this.c_enable_rax_pkg);
        this.c_release_map = new ConfigKV("release_map", "true", WXAPM_CONFIG_GROUP);
        this.mInitConfigs.add(this.c_release_map);
        this.c_enable_mtop_cache = new ConfigKV(key_enable_mtop_usecache, "false", "android_weex_ext_config");
        this.mInitConfigs.add(this.c_enable_mtop_cache);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x001b, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private synchronized void ensureSP() {
        /*
            r3 = this;
            monitor-enter(r3)
            android.content.SharedPreferences r0 = r3.sharedPreferences     // Catch:{ all -> 0x001c }
            if (r0 == 0) goto L_0x0007
            monitor-exit(r3)
            return
        L_0x0007:
            com.alibaba.aliweex.AliWeex r0 = com.alibaba.aliweex.AliWeex.getInstance()     // Catch:{ all -> 0x001c }
            android.app.Application r0 = r0.getApplication()     // Catch:{ all -> 0x001c }
            if (r0 == 0) goto L_0x001a
            java.lang.String r1 = "weex_init_config"
            r2 = 0
            android.content.SharedPreferences r0 = r0.getSharedPreferences(r1, r2)     // Catch:{ all -> 0x001c }
            r3.sharedPreferences = r0     // Catch:{ all -> 0x001c }
        L_0x001a:
            monitor-exit(r3)
            return
        L_0x001c:
            r0 = move-exception
            monitor-exit(r3)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.aliweex.utils.WXInitConfigManager.ensureSP():void");
    }

    /* access modifiers changed from: private */
    public void doSomething() {
        RegisterCache.getInstance().setEnableAutoScan("true".equals(get(key_enableAutoScan, "false")));
    }

    /* access modifiers changed from: package-private */
    public void registerListener(String[] strArr) {
        if (this.adapter != null) {
            this.adapter.registerListener(strArr, this.listener);
        }
    }

    /* access modifiers changed from: package-private */
    public void unregisterListener(String[] strArr) {
        if (this.adapter != null) {
            this.adapter.unregisterListener(strArr, this.listener);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0040, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private synchronized void write(java.lang.String r3, java.lang.String r4) {
        /*
            r2 = this;
            monitor-enter(r2)
            r2.ensureSP()     // Catch:{ all -> 0x0041 }
            android.content.SharedPreferences r0 = r2.sharedPreferences     // Catch:{ all -> 0x0041 }
            if (r0 == 0) goto L_0x003f
            boolean r0 = android.text.TextUtils.isEmpty(r3)     // Catch:{ all -> 0x0041 }
            if (r0 != 0) goto L_0x003f
            boolean r0 = android.text.TextUtils.isEmpty(r4)     // Catch:{ all -> 0x0041 }
            if (r0 == 0) goto L_0x0015
            goto L_0x003f
        L_0x0015:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x0041 }
            r0.<init>()     // Catch:{ all -> 0x0041 }
            java.lang.String r1 = "save Init Config : "
            r0.append(r1)     // Catch:{ all -> 0x0041 }
            r0.append(r3)     // Catch:{ all -> 0x0041 }
            java.lang.String r1 = ":"
            r0.append(r1)     // Catch:{ all -> 0x0041 }
            r0.append(r4)     // Catch:{ all -> 0x0041 }
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x0041 }
            com.taobao.weex.utils.WXLogUtils.e(r0)     // Catch:{ all -> 0x0041 }
            android.content.SharedPreferences r0 = r2.sharedPreferences     // Catch:{ all -> 0x0041 }
            android.content.SharedPreferences$Editor r0 = r0.edit()     // Catch:{ all -> 0x0041 }
            r0.putString(r3, r4)     // Catch:{ all -> 0x0041 }
            r0.commit()     // Catch:{ all -> 0x0041 }
            monitor-exit(r2)
            return
        L_0x003f:
            monitor-exit(r2)
            return
        L_0x0041:
            r3 = move-exception
            monitor-exit(r2)
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.aliweex.utils.WXInitConfigManager.write(java.lang.String, java.lang.String):void");
    }

    public synchronized String get(String str, String str2) {
        ensureSP();
        if (this.sharedPreferences != null) {
            if (str != null) {
                str2 = this.sharedPreferences.getString(str, str2);
            }
        }
        return str2;
    }

    public synchronized String getFromConfigKV(ConfigKV configKV) {
        if (configKV == null) {
            return null;
        }
        return get(configKV.key, configKV.defaultValue);
    }

    public synchronized String getConfigKVFirstValue(ConfigKV configKV) {
        if (configKV == null) {
            return null;
        }
        if (configKV.firstValue == null) {
            configKV.firstValue = tryGetConfigFromSpAndOrange(configKV.group, configKV.key, configKV.defaultValue);
        }
        return configKV.firstValue;
    }

    public String tryGetConfigFromSpAndOrange(String str, String str2, String str3) {
        String str4 = get(str2, str3);
        IConfigAdapter configAdapter = AliWeex.getInstance().getConfigAdapter();
        if (configAdapter == null) {
            return str4;
        }
        return configAdapter.getConfig(str, str2, str4);
    }

    public void initConfigSettings() {
        int i;
        if (initOk()) {
            String fromConfigKV = getFromConfigKV(this.c_enableAutoScan);
            WXLogUtils.e("updateGlobalConfig enableAutoScan " + fromConfigKV);
            RegisterCache.getInstance().setEnableAutoScan("true".equals(fromConfigKV));
            String fromConfigKV2 = getFromConfigKV(this.c_enableRegisterCache);
            WXLogUtils.e("updateGlobalConfig enableRegisterCache " + fromConfigKV2);
            RegisterCache.getInstance().setEnable("true".equals(fromConfigKV2));
            String str = get(key_initLeftSize, "50");
            WXLogUtils.e("updateGlobalConfig initLeftSize " + str);
            try {
                i = Integer.parseInt(str);
            } catch (Exception unused) {
                i = 50;
            }
            RegisterCache.getInstance().setDoNotCacheSize(i);
        }
    }

    public static class ConfigKV {
        public String defaultValue;
        public String firstValue = null;
        public String group;
        public String key;

        ConfigKV(String str, String str2, String str3) {
            this.key = str;
            this.defaultValue = str2;
            this.group = str3;
        }
    }
}
