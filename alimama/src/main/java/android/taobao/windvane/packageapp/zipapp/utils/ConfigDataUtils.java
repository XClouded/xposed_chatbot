package android.taobao.windvane.packageapp.zipapp.utils;

import android.taobao.windvane.config.WVConfigManager;
import android.taobao.windvane.config.WVConfigUpdateCallback;
import android.taobao.windvane.monitor.WVConfigMonitorInterface;
import android.taobao.windvane.monitor.WVMonitorService;
import android.taobao.windvane.packageapp.ZipAppFileManager;
import android.taobao.windvane.packageapp.monitor.GlobalInfoMonitor;
import android.taobao.windvane.packageapp.zipapp.data.ZipAppResultCode;
import android.taobao.windvane.packageapp.zipapp.data.ZipGlobalConfig;
import android.taobao.windvane.util.TaoLog;

public class ConfigDataUtils {
    private static String ATTACH_ITEM_SPLIT = "|";
    private static String ATTACH_SPLIT = "||";
    private static String TAG = "ConfigDataUtils";

    public static ConfigData parseConfig(String str, boolean z, boolean z2) {
        if (str == null) {
            return null;
        }
        ConfigDataUtils configDataUtils = new ConfigDataUtils();
        configDataUtils.getClass();
        ConfigData configData = new ConfigData();
        int lastIndexOf = str.lastIndexOf(ATTACH_SPLIT);
        if (lastIndexOf > 0) {
            configData.json = str.substring(0, lastIndexOf);
            String substring = str.substring(lastIndexOf + 2);
            int indexOf = substring.indexOf(ATTACH_ITEM_SPLIT);
            if (indexOf > 0) {
                configData.systemtime = substring.substring(0, indexOf);
                configData.tk = substring.substring(indexOf + 1);
                if (!z || ZipAppSecurityUtils.validConfigFile(configData.json, configData.tk)) {
                    return configData;
                }
                if (TaoLog.getLogStatus()) {
                    TaoLog.w(TAG, "parseConfig:SecurityUtils validConfigFile fail ");
                }
                if (!z2) {
                    GlobalInfoMonitor.error(ZipAppResultCode.ERR_CHECK_CONFIG_APPS, "");
                }
                return null;
            } else if (z) {
                return null;
            } else {
                configData.systemtime = substring;
                return configData;
            }
        } else if (z) {
            return null;
        } else {
            configData.json = str;
            return configData;
        }
    }

    public static ZipGlobalConfig parseGlobalConfig(String str) {
        try {
            ZipGlobalConfig parseString2GlobalConfig = ZipAppUtils.parseString2GlobalConfig(str);
            parseString2GlobalConfig.setZcacheResConfig(ZipAppUtils.parseZcacheConfig(ZipAppFileManager.getInstance().readZcacheConfig(false)));
            return parseString2GlobalConfig;
        } catch (Throwable th) {
            WVConfigMonitorInterface configMonitor = WVMonitorService.getConfigMonitor();
            if (configMonitor != null) {
                configMonitor.didOccurUpdateConfigError(WVConfigManager.CONFIGNAME_PACKAGE, WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.UNKNOWN_ERROR.ordinal(), th.getMessage());
            }
            String str2 = TAG;
            TaoLog.e(str2, "parseGlobalConfig Exception:" + th.getMessage());
            return null;
        }
    }

    public class ConfigData {
        public String json;
        public String systemtime = "0";
        public String tk;

        public ConfigData() {
        }
    }
}
