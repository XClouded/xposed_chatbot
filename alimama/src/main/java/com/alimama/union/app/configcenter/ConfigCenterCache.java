package com.alimama.union.app.configcenter;

import android.app.Application;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import com.alimama.union.app.logger.NewMonitorLogger;
import com.alimamaunion.base.configcenter.ConfigData;
import com.alimamaunion.base.configcenter.DefaultConfigCenterCache;
import com.alimamaunion.base.configcenter.IConfigCenterCache;
import com.alimamaunion.base.safejson.SafeJSONArray;
import com.alimamaunion.base.safejson.SafeJSONObject;
import com.taobao.weex.el.parse.Operators;

public class ConfigCenterCache implements IConfigCenterCache {
    private static final String TAG = "ConfigCenterCache";
    private final DefaultConfigCenterCache mDefaultCache;

    public ConfigCenterCache(@NonNull Application application) {
        this.mDefaultCache = new DefaultConfigCenterCache(application);
    }

    public ConfigData read(String str) {
        ConfigData read = this.mDefaultCache.read(str);
        if (read == null || TextUtils.isEmpty(read.lastModified) || TextUtils.equals("0", read.lastModified)) {
            NewMonitorLogger.ConfigCenter.getConfigError(TAG, "加载配置失败，fallback到本地数据", str);
        }
        return read;
    }

    public void write(String str, ConfigData configData) {
        if (checkConfigDataValid(configData, str)) {
            this.mDefaultCache.write(str, configData);
        }
    }

    public void remove(String str) {
        this.mDefaultCache.remove(str);
    }

    private boolean checkConfigDataValid(ConfigData configData, String str) {
        String str2;
        if (configData == null || configData.data == null) {
            return true;
        }
        try {
            str2 = new String(configData.data);
            try {
                if (TextUtils.isEmpty(str2)) {
                    return true;
                }
                if (str2.startsWith(Operators.BLOCK_START_STR)) {
                    new SafeJSONObject(str2);
                    return true;
                } else if (!str2.startsWith(Operators.ARRAY_START_STR)) {
                    return true;
                } else {
                    new SafeJSONArray(str2);
                    return true;
                }
            } catch (Exception unused) {
                NewMonitorLogger.ConfigCenter.configJsonError(TAG, str, str2);
                return false;
            }
        } catch (Exception unused2) {
            str2 = null;
            NewMonitorLogger.ConfigCenter.configJsonError(TAG, str, str2);
            return false;
        }
    }
}
