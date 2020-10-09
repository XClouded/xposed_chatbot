package com.taobao.orange.cache;

import android.text.TextUtils;
import com.taobao.orange.ConfigCenter;
import com.taobao.orange.OConstant;
import com.taobao.orange.OThreadFactory;
import com.taobao.orange.model.ConfigDO;
import com.taobao.orange.model.CustomConfigDO;
import com.taobao.orange.model.NameSpaceDO;
import com.taobao.orange.util.FileUtil;
import com.taobao.orange.util.OLog;
import com.taobao.orange.util.OrangeMonitor;
import com.taobao.orange.util.OrangeUtils;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ConfigCache {
    private static final String TAG = "ConfigCache";
    private Map<String, ConfigDO> mConfigMap = new ConcurrentHashMap();

    public Map<String, ConfigDO> getConfigMap() {
        return this.mConfigMap;
    }

    public Set<NameSpaceDO> load(Set<NameSpaceDO> set) {
        HashSet hashSet = new HashSet();
        if (set == null || set.isEmpty()) {
            OLog.w(TAG, "load config cache empty", new Object[0]);
            return null;
        }
        for (NameSpaceDO next : set) {
            ConfigDO restoreConfig = restoreConfig(next);
            if (restoreConfig != null) {
                restoreConfig.persisted = true;
                this.mConfigMap.put(restoreConfig.name, restoreConfig);
                ConfigCenter.getInstance().removeFail(restoreConfig.name);
                ConfigCenter.getInstance().notifyListeners(restoreConfig.name, restoreConfig.getCurVersion(), true);
                if (restoreConfig.candidate == null && OrangeUtils.parseLong(next.version) > OrangeUtils.parseLong(restoreConfig.version)) {
                    hashSet.add(next);
                    OLog.d(TAG, "load not match as version", "name", next.name);
                }
            } else if (NameSpaceDO.LEVEL_HIGH.equals(next.loadLevel)) {
                hashSet.add(next);
            }
        }
        return hashSet;
    }

    private ConfigDO restoreConfig(NameSpaceDO nameSpaceDO) {
        ConfigDO configDO = (ConfigDO) FileUtil.restoreObject(nameSpaceDO.name);
        if (configDO == null) {
            return null;
        }
        if (OLog.isPrintLog(1)) {
            if (configDO.candidate == null) {
                OLog.d(TAG, "restoreConfig", configDO);
            } else {
                OLog.d(TAG, "restoreAbConfig", configDO);
            }
        }
        return configDO;
    }

    public void cache(final ConfigDO configDO) {
        this.mConfigMap.put(configDO.name, configDO);
        ConfigCenter.getInstance().notifyListeners(configDO.name, configDO.getCurVersion(), false);
        if (ConfigCenter.getInstance().isAfterIdle.get()) {
            OThreadFactory.executeDisk(new Runnable() {
                public void run() {
                    FileUtil.persistObject(configDO, configDO.name);
                }
            });
        } else {
            configDO.persisted = false;
        }
    }

    public void remove(String str) {
        if (!TextUtils.isEmpty(str)) {
            FileUtil.deleteConfigFile(str);
        }
    }

    public <T> T getConfigObj(String str) {
        ConfigDO configDO = this.mConfigMap.get(str);
        T t = null;
        if (configDO != null) {
            if (NameSpaceDO.TYPE_STANDARD.equals(configDO.type)) {
                t = configDO.content;
            } else if (NameSpaceDO.TYPE_CUSTOM.equals(configDO.type)) {
                t = ((CustomConfigDO) configDO).stringContent;
            } else {
                OLog.e(TAG, "getConfigs fail unsupport type", new Object[0]);
            }
            if (!configDO.monitored) {
                OrangeMonitor.commitConfigMonitor(OConstant.POINT_CONFIG_USE, configDO.name, configDO.version);
                configDO.monitored = true;
            }
        }
        return t;
    }
}
