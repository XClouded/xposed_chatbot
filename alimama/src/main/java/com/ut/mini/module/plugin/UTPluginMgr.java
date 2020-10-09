package com.ut.mini.module.plugin;

import android.text.TextUtils;
import com.alibaba.analytics.core.config.UTClientConfigMgr;
import com.alibaba.fastjson.JSONObject;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class UTPluginMgr {
    private static UTPluginMgr mInstance = new UTPluginMgr();
    private Map<String, UTPlugin> allUTPluginMap = new Hashtable();
    private Map<String, UTPlugin> openUTPluginMap = new Hashtable();
    private UTPluginConfig utPluginConfig = null;

    private UTPluginMgr() {
        UTClientConfigMgr.getInstance().registerConfigChangeListener(new UTClientConfigMgr.IConfigChangeListener() {
            public String getKey() {
                return "plugin";
            }

            public void onChange(String str) {
                UTPluginMgr.this.parseUTPluginConfig(str);
            }
        });
    }

    public static UTPluginMgr getInstance() {
        return mInstance;
    }

    public void forEachPlugin(IUTPluginForEachDelegate iUTPluginForEachDelegate) {
        if (iUTPluginForEachDelegate != null) {
            try {
                for (Map.Entry<String, UTPlugin> value : this.openUTPluginMap.entrySet()) {
                    UTPlugin uTPlugin = (UTPlugin) value.getValue();
                    if (uTPlugin != null) {
                        iUTPluginForEachDelegate.onPluginForEach(uTPlugin);
                    }
                }
            } catch (Throwable unused) {
            }
        }
    }

    public synchronized void registerPlugin(UTPlugin uTPlugin) {
        if (uTPlugin != null) {
            String pluginName = uTPlugin.getPluginName();
            if (TextUtils.isEmpty(pluginName)) {
                pluginName = "OldUTPlugin_" + uTPlugin.hashCode();
            }
            if (!TextUtils.isEmpty(pluginName) && !this.allUTPluginMap.containsKey(pluginName)) {
                this.allUTPluginMap.put(pluginName, uTPlugin);
                if (isOpen(pluginName)) {
                    this.openUTPluginMap.put(pluginName, uTPlugin);
                }
            }
        }
    }

    public synchronized void unregisterPlugin(UTPlugin uTPlugin) {
        if (uTPlugin != null) {
            String pluginName = uTPlugin.getPluginName();
            if (TextUtils.isEmpty(pluginName)) {
                pluginName = "OldUTPlugin_" + uTPlugin.hashCode();
            }
            if (!TextUtils.isEmpty(pluginName)) {
                this.allUTPluginMap.remove(pluginName);
                this.openUTPluginMap.remove(pluginName);
            }
        }
    }

    public boolean isOpen() {
        return this.openUTPluginMap != null && this.openUTPluginMap.size() > 1;
    }

    private boolean isOpen(String str) {
        if (this.utPluginConfig == null) {
            return true;
        }
        List<String> open = this.utPluginConfig.getOpen();
        if (open != null && open.contains(str)) {
            return true;
        }
        List<String> close = this.utPluginConfig.getClose();
        if (close != null && close.contains(str)) {
            return false;
        }
        String other = this.utPluginConfig.getOther();
        if (TextUtils.isEmpty(other) || !other.equals("close")) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: private */
    public synchronized void parseUTPluginConfig(String str) {
        try {
            this.utPluginConfig = (UTPluginConfig) JSONObject.parseObject(str, UTPluginConfig.class);
        } catch (Exception unused) {
            this.utPluginConfig = null;
        }
        for (Map.Entry next : this.allUTPluginMap.entrySet()) {
            String str2 = (String) next.getKey();
            if (!isOpen(str2)) {
                this.openUTPluginMap.remove(str2);
            } else if (!this.openUTPluginMap.containsKey(str2)) {
                this.openUTPluginMap.put(str2, (UTPlugin) next.getValue());
            }
        }
    }
}
