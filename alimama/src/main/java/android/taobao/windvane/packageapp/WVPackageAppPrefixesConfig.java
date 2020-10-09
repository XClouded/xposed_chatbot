package android.taobao.windvane.packageapp;

import android.taobao.windvane.config.GlobalConfig;
import android.taobao.windvane.config.WVCommonConfig;
import android.taobao.windvane.config.WVConfigManager;
import android.taobao.windvane.config.WVConfigUpdateCallback;
import android.taobao.windvane.config.WVConfigUtils;
import android.taobao.windvane.connect.ConnectManager;
import android.taobao.windvane.connect.HttpConnectListener;
import android.taobao.windvane.connect.HttpResponse;
import android.taobao.windvane.packageapp.zipapp.ConfigManager;
import android.taobao.windvane.packageapp.zipapp.ZipAppManager;
import android.taobao.windvane.packageapp.zipapp.ZipPrefixesManager;
import android.taobao.windvane.packageapp.zipapp.data.ZipAppInfo;
import android.taobao.windvane.packageapp.zipapp.data.ZipAppTypeEnum;
import android.taobao.windvane.packageapp.zipapp.data.ZipGlobalConfig;
import android.taobao.windvane.packageapp.zipapp.utils.ZipAppUtils;
import android.taobao.windvane.util.ConfigStorage;
import android.taobao.windvane.util.TaoLog;
import android.text.TextUtils;

import com.alibaba.analytics.core.Constants;
import com.alibaba.analytics.core.sync.UploadQueueMgr;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.Map;

public class WVPackageAppPrefixesConfig {
    private static final String TAG = "WVPackageAppPrefixesConfig";
    private static final String VERION_KEY = "WVZipPrefixesVersion";
    private static volatile WVPackageAppPrefixesConfig instance;
    public boolean preViewMode = false;
    public int updateCount = 0;
    private String v = "0";

    public static WVPackageAppPrefixesConfig getInstance() {
        if (instance == null) {
            synchronized (WVPackageAppPrefixesConfig.class) {
                if (instance == null) {
                    instance = new WVPackageAppPrefixesConfig();
                    instance.v = ConfigStorage.getStringVal(ZipPrefixesManager.SPNAME, VERION_KEY, "0");
                }
            }
        }
        return instance;
    }

    public synchronized void resetConfig() {
        this.v = "0";
        ConfigStorage.putStringVal(ZipPrefixesManager.SPNAME, VERION_KEY, this.v);
        ZipPrefixesManager.getInstance().clear();
    }

    public void updatePrefixesInfos(String str, final WVConfigUpdateCallback wVConfigUpdateCallback, String str2) {
        this.updateCount = 0;
        long currentTimeMillis = System.currentTimeMillis() - ConfigStorage.getLongVal(WVConfigManager.SPNAME_CONFIG, "prefixes_updateTime", 0);
        if (currentTimeMillis > ((long) WVCommonConfig.commonConfig.recoveryInterval) || currentTimeMillis < 0) {
            this.v = "0";
            str2 = "0";
            ConfigStorage.putLongVal(WVConfigManager.SPNAME_CONFIG, "prefixes_updateTime", System.currentTimeMillis());
        }
        if (TextUtils.isEmpty(str)) {
            if ("3".equals(GlobalConfig.zType)) {
                this.v = "0";
            }
            str = WVConfigManager.getInstance().getConfigUrl(Constants.LogTransferLevel.L7, this.v, WVConfigUtils.getTargetValue(), str2);
        }
        ConnectManager.getInstance().connectSync(str, new HttpConnectListener<HttpResponse>() {
            public void onFinish(HttpResponse httpResponse, int i) {
                if (httpResponse != null && httpResponse.getData() != null) {
                    try {
                        String str = new String(httpResponse.getData(), "utf-8");
                        if ("3".equals(GlobalConfig.zType)) {
                            ZipPrefixesManager.getInstance().prefix = str;
                            TaoLog.i("ZCache", "prefix 3.0");
                            wVConfigUpdateCallback.updateStatus(WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.SUCCESS, 0);
                            return;
                        }
                        if (!WVPackageAppPrefixesConfig.this.parseConfig(str)) {
                            if (wVConfigUpdateCallback != null) {
                                wVConfigUpdateCallback.updateStatus(WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.NO_VERSION, 0);
                            }
                        } else if (wVConfigUpdateCallback != null) {
                            wVConfigUpdateCallback.updateStatus(WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.SUCCESS, WVPackageAppPrefixesConfig.this.updateCount);
                        }
                        TaoLog.i(WVPackageAppPrefixesConfig.TAG, str);
                    } catch (UnsupportedEncodingException e) {
                        if (wVConfigUpdateCallback != null) {
                            wVConfigUpdateCallback.updateStatus(WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.ENCODING_ERROR, 0);
                        }
                        TaoLog.e(WVPackageAppPrefixesConfig.TAG, "config encoding error. " + e.getMessage());
                    }
                } else if (wVConfigUpdateCallback != null) {
                    wVConfigUpdateCallback.updateStatus(WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.NULL_DATA, 0);
                }
            }
        });
    }

    public boolean parseConfig(String str) {
        ZipGlobalConfig locGlobalConfig;
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        ConfigManager.getLocGlobalConfig();
        try {
            JSONObject jSONObject = new JSONObject(str);
            this.v = jSONObject.optString("v");
            String optString = jSONObject.optString(UploadQueueMgr.MSGTYPE_INTERVAL);
            if (this.v == null) {
                return false;
            }
            String optString2 = jSONObject.optString("rules");
            if (!TextUtils.isEmpty(optString2)) {
                Hashtable<String, Hashtable<String, String>> parsePrefixes = ZipAppUtils.parsePrefixes(optString2);
                boolean z = optString != null && "-1".equals(optString);
                if (z && !this.preViewMode) {
                    ZipPrefixesManager.getInstance().clear();
                }
                if (ZipPrefixesManager.getInstance().mergePrefixes(parsePrefixes)) {
                    ConfigStorage.putStringVal(ZipPrefixesManager.SPNAME, VERION_KEY, this.v);
                }
                if (z && (locGlobalConfig = ConfigManager.getLocGlobalConfig()) != null && locGlobalConfig.isAvailableData()) {
                    for (Map.Entry<String, ZipAppInfo> value : locGlobalConfig.getAppsTable().entrySet()) {
                        ZipAppInfo zipAppInfo = (ZipAppInfo) value.getValue();
                        if (!(zipAppInfo == null || zipAppInfo.getAppType() == ZipAppTypeEnum.ZIP_APP_TYPE_ZCACHE || ZipPrefixesManager.getInstance().isAvailableApp(zipAppInfo.name))) {
                            ZipAppManager.getInstance().unInstall(zipAppInfo);
                            TaoLog.e(TAG, "unInstall not availableApp : " + zipAppInfo.name);
                        }
                    }
                }
            }
            return true;
        } catch (Exception unused) {
            TaoLog.e(TAG, "parse PrefixesInfos error!");
            return false;
        }
    }
}
