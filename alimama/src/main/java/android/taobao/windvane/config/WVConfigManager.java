package android.taobao.windvane.config;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Looper;
import android.taobao.windvane.connect.ConnectManager;
import android.taobao.windvane.connect.HttpConnectListener;
import android.taobao.windvane.connect.HttpResponse;
import android.taobao.windvane.connect.api.ApiResponse;
import android.taobao.windvane.monitor.WVConfigMonitorInterface;
import android.taobao.windvane.monitor.WVMonitorService;
import android.taobao.windvane.packageapp.WVPackageAppService;
import android.taobao.windvane.packageapp.zipapp.data.ZipAppInfo;
import android.taobao.windvane.service.WVEventContext;
import android.taobao.windvane.service.WVEventId;
import android.taobao.windvane.service.WVEventListener;
import android.taobao.windvane.service.WVEventResult;
import android.taobao.windvane.service.WVEventService;
import android.taobao.windvane.thread.WVThreadPool;
import android.taobao.windvane.util.CommonUtils;
import android.taobao.windvane.util.ConfigStorage;
import android.taobao.windvane.util.EnvUtil;
import android.taobao.windvane.util.TaoLog;
import android.text.TextUtils;

import com.taobao.weex.el.parse.Operators;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import mtopsdk.common.util.HttpHeaderConstant;

public class WVConfigManager {
    public static final String CONFIGNAME_COMMON = "common";
    public static final String CONFIGNAME_COOKIE = "cookie_black_list";
    public static final String CONFIGNAME_CUSTOM = "customs";
    public static final String CONFIGNAME_DOMAIN = "domain";
    public static final String CONFIGNAME_LOCALE = "locale";
    public static final String CONFIGNAME_MONITOR = "monitor";
    public static final String CONFIGNAME_PACKAGE = "package";
    public static final String CONFIGNAME_PREFIXES = "prefixes";
    public static final String CONFIG_UPDATETIME = "_updateTime";
    public static final String CONFIG_UPLOADDATA = "_uploadData";
    private static final String DEFAULT_DEMAIN = "https://wvcfg.alicdn.com/";
    public static final String SPNAME_CONFIG = "wv_main_config";
    private static final String TAG = "WVConfigManager";
    /* access modifiers changed from: private */
    public static volatile WVConfigManager instance = null;
    public static boolean launch = EnvUtil.isTaobao();
    private static long updateInterval = 300000;
    private static long updateIntervalServer = 300000;
    private static long updateTime;
    private String configDomain;
    private boolean enableUpdateConfig;
    /* access modifiers changed from: private */
    public ConcurrentHashMap<String, WVConfigHandler> mConfigMap;
    private ConcurrentHashMap<String, IConfig> mOtherConfigMap;
    /* access modifiers changed from: private */
    public int updateConfigCount;

    public enum WVConfigUpdateFromType {
        WVConfigUpdateFromTypeCustom,
        WVConfigUpdateFromTypeActive,
        WVConfigUpdateFromTypeFinish,
        WVConfigUpdateFromTypePush,
        WVConfigUpdateFromTypeLaunch,
        WVConfigUpdateFromTypeAppActive,
        WVConfigUpdateFromTypeLocaleChange,
        WVConfigUpdateFromZCache3_0
    }

    static /* synthetic */ int access$404(WVConfigManager wVConfigManager) {
        int i = wVConfigManager.updateConfigCount + 1;
        wVConfigManager.updateConfigCount = i;
        return i;
    }

    public static class WVPageEventListener implements WVEventListener {
        public WVEventResult onEvent(int i, WVEventContext wVEventContext, Object... objArr) {
            if (i != 3002 && i != 1002) {
                return null;
            }
            if (WVConfigManager.launch && wVEventContext != null && wVEventContext.webView != null && (wVEventContext.webView._getContext() instanceof Activity) && !wVEventContext.webView._getContext().getClass().getSimpleName().equals("MainActivity3")) {
                WVConfigManager.launch = false;
            }
            if (i == 3002) {
                WVConfigManager.instance.updateConfig(WVConfigUpdateFromType.WVConfigUpdateFromTypeActive);
                return null;
            }
            WVConfigManager.instance.updateConfig(WVConfigUpdateFromType.WVConfigUpdateFromTypeFinish);
            return null;
        }
    }

    private WVConfigManager() {
        this.configDomain = DEFAULT_DEMAIN;
        this.updateConfigCount = 0;
        this.mConfigMap = null;
        this.enableUpdateConfig = true;
        this.mConfigMap = new ConcurrentHashMap<>();
        this.mOtherConfigMap = new ConcurrentHashMap<>();
        WVEventService.getInstance().addEventListener(new WVPageEventListener());
    }

    public static WVConfigManager getInstance() {
        if (instance == null) {
            synchronized (WVConfigManager.class) {
                if (instance == null) {
                    instance = new WVConfigManager();
                }
            }
        }
        return instance;
    }

    public void registerHandler(String str, WVConfigHandler wVConfigHandler) {
        this.mConfigMap.put(str, wVConfigHandler);
    }

    public void setUpdateConfigEnable(boolean z) {
        this.enableUpdateConfig = z;
    }

    /* access modifiers changed from: private */
    public void updateImmediately(final WVConfigUpdateFromType wVConfigUpdateFromType) {
        if (this.enableUpdateConfig && WVConfigUtils.checkAppKeyAvailable()) {
            final long currentTimeMillis = System.currentTimeMillis();
            ConnectManager.getInstance().connectSync(getConfigUrl("0", "0", WVConfigUtils.getTargetValue(), "0"), new HttpConnectListener<HttpResponse>() {
                public void onFinish(HttpResponse httpResponse, int i) {
                    int i2;
                    long currentTimeMillis = System.currentTimeMillis() - currentTimeMillis;
                    if (httpResponse != null) {
                        try {
                            String str = new String(httpResponse.getData(), "utf-8");
                            ApiResponse apiResponse = new ApiResponse();
                            JSONObject jSONObject = apiResponse.parseJsonResult(str).success ? apiResponse.data : null;
                            if (WVMonitorService.getPackageMonitorInterface() != null) {
                                long currentTimeMillis2 = System.currentTimeMillis();
                                Map<String, String> headers = httpResponse.getHeaders();
                                if (headers != null) {
                                    String str2 = headers.get("Age");
                                    if (TextUtils.isEmpty(str2)) {
                                        str2 = headers.get("age");
                                    }
                                    String str3 = headers.get(HttpHeaderConstant.DATE);
                                    if (TextUtils.isEmpty(str3)) {
                                        str3 = headers.get("date");
                                    }
                                    long longValue = !TextUtils.isEmpty(str2) ? Long.valueOf(str2).longValue() * 1000 : 0;
                                    if (!TextUtils.isEmpty(str3)) {
                                        longValue += CommonUtils.parseDate(str3);
                                    }
                                    if (longValue != 0) {
                                        long j = currentTimeMillis2 - longValue;
                                        TaoLog.i(WVConfigManager.TAG, "updateDiffTime by config : " + j);
                                        WVMonitorService.getPackageMonitorInterface().uploadDiffTimeTime(j);
                                    }
                                }
                            }
                            boolean needFull = WVLocaleConfig.getInstance().needFull();
                            WVConfigUpdateFromType wVConfigUpdateFromType = wVConfigUpdateFromType;
                            if (needFull) {
                                wVConfigUpdateFromType = WVConfigUpdateFromType.WVConfigUpdateFromTypeLocaleChange;
                                WVPackageAppService.getWvPackageAppConfig().requestFullConfigNextTime();
                            }
                            if (!(jSONObject == null || WVConfigManager.this.mConfigMap == null)) {
                                Enumeration keys = WVConfigManager.this.mConfigMap.keys();
                                while (keys.hasMoreElements()) {
                                    String str4 = (String) keys.nextElement();
                                    WVConfigManager.this.doUpdateByKey(str4, jSONObject.optString(str4, "0"), (String) null, wVConfigUpdateFromType);
                                }
                                if (WVMonitorService.getConfigMonitor() != null) {
                                    WVMonitorService.getConfigMonitor().didOccurUpdateConfigSuccess("entry");
                                }
                            }
                            WVEventService.getInstance().onEvent(WVEventId.ORANGE_REGISTER);
                            i2 = 1;
                        } catch (Exception e) {
                            if (WVMonitorService.getConfigMonitor() != null) {
                                WVConfigMonitorInterface configMonitor = WVMonitorService.getConfigMonitor();
                                int ordinal = WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.UNKNOWN_ERROR.ordinal();
                                configMonitor.didOccurUpdateConfigError("entry", ordinal, "update entry error : " + e.getMessage());
                            }
                            TaoLog.d(WVConfigManager.TAG, "updateImmediately failed!");
                            i2 = 0;
                        }
                        if (WVMonitorService.getConfigMonitor() != null) {
                            WVMonitorService.getConfigMonitor().didUpdateConfig("entry", wVConfigUpdateFromType.ordinal(), currentTimeMillis, i2, WVConfigManager.this.mConfigMap.size());
                        }
                    }
                }

                public void onError(int i, String str) {
                    TaoLog.d(WVConfigManager.TAG, "update entry failed! : " + str);
                    if (WVMonitorService.getConfigMonitor() != null) {
                        WVMonitorService.getConfigMonitor().didOccurUpdateConfigError("entry-NoNetwork", WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.UNKNOWN_ERROR.ordinal(), str);
                    }
                    super.onError(i, str);
                }
            });
        }
    }

    public boolean checkIfUpdate(WVConfigUpdateFromType wVConfigUpdateFromType) {
        if ("3".equals(GlobalConfig.zType) && wVConfigUpdateFromType != WVConfigUpdateFromType.WVConfigUpdateFromZCache3_0) {
            return false;
        }
        long currentTimeMillis = System.currentTimeMillis();
        boolean z = wVConfigUpdateFromType == WVConfigUpdateFromType.WVConfigUpdateFromTypeAppActive || wVConfigUpdateFromType == WVConfigUpdateFromType.WVConfigUpdateFromTypeLocaleChange || currentTimeMillis - updateTime > updateInterval;
        if (z && WVConfigUtils.checkAppKeyAvailable()) {
            updateTime = currentTimeMillis;
        }
        if (WVConfigUtils.checkAppKeyAvailable()) {
            String format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(Calendar.getInstance().getTime());
            String stringVal = ConfigStorage.getStringVal(SPNAME_CONFIG, "package_uploadData", "0");
            TaoLog.d("WVConfigManager.updateConfig ==> ", "uploadDate = " + stringVal);
            if (!format.equals(stringVal) && WVCommonConfig.commonConfig.monitoredApps.length != 0) {
                StringBuilder sb = new StringBuilder();
                Map<String, ZipAppInfo> appsTable = WVPackageAppService.getWvPackageAppConfig().getGlobalConfig().getAppsTable();
                for (int i = 0; i < WVCommonConfig.commonConfig.monitoredApps.length; i++) {
                    ZipAppInfo zipAppInfo = appsTable.get(WVCommonConfig.commonConfig.monitoredApps[i]);
                    if (zipAppInfo != null && zipAppInfo.isAppInstalled()) {
                        sb.append(zipAppInfo.name);
                        sb.append("-");
                        sb.append(zipAppInfo.installedSeq);
                        if (i != WVCommonConfig.commonConfig.monitoredApps.length - 1) {
                            sb.append(",");
                        }
                    }
                }
                if (WVMonitorService.getPackageMonitorInterface() != null) {
                    WVMonitorService.getPackageMonitorInterface().commitZCacheDiurnalOverview(sb.toString());
                }
                ConfigStorage.putStringVal(SPNAME_CONFIG, "package_uploadData", format);
            }
        }
        return z;
    }

    @TargetApi(11)
    public void updateConfig(final WVConfigUpdateFromType wVConfigUpdateFromType) {
        boolean z;
        long currentTimeMillis = System.currentTimeMillis();
        if ("2".equals(GlobalConfig.zType)) {
            z = wVConfigUpdateFromType == WVConfigUpdateFromType.WVConfigUpdateFromTypeAppActive || wVConfigUpdateFromType == WVConfigUpdateFromType.WVConfigUpdateFromTypeLocaleChange || currentTimeMillis - updateTime > updateInterval;
            if (z) {
                TaoLog.i("ZCache", "update config zcache 2.0");
            }
        } else if (wVConfigUpdateFromType != WVConfigUpdateFromType.WVConfigUpdateFromZCache3_0) {
            TaoLog.i("ZCache", "skip update config, updateType=[" + wVConfigUpdateFromType.name() + Operators.ARRAY_END_STR);
            return;
        } else {
            TaoLog.i("ZCache", "update config zcache 3.0");
            z = true;
        }
        if (z && WVConfigUtils.checkAppKeyAvailable()) {
            updateTime = currentTimeMillis;
            if (launch) {
                updateInterval = 30000;
            } else {
                updateInterval = updateIntervalServer;
            }
            TaoLog.i(TAG, "updateInterval=[" + updateInterval + Operators.ARRAY_END_STR);
            AsyncTask.execute(new Runnable() {
                public void run() {
                    WVConfigManager.this.updateImmediately(wVConfigUpdateFromType);
                }
            });
        }
        if (WVConfigUtils.checkAppKeyAvailable()) {
            String format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(Calendar.getInstance().getTime());
            String stringVal = ConfigStorage.getStringVal(SPNAME_CONFIG, "package_uploadData", "0");
            TaoLog.d("WVConfigManager.updateConfig ==> ", "uploadDate = " + stringVal);
            if (!format.equals(stringVal) && WVCommonConfig.commonConfig.monitoredApps.length != 0) {
                StringBuilder sb = new StringBuilder();
                Map<String, ZipAppInfo> appsTable = WVPackageAppService.getWvPackageAppConfig().getGlobalConfig().getAppsTable();
                for (int i = 0; i < WVCommonConfig.commonConfig.monitoredApps.length; i++) {
                    ZipAppInfo zipAppInfo = appsTable.get(WVCommonConfig.commonConfig.monitoredApps[i]);
                    if (zipAppInfo != null && zipAppInfo.isAppInstalled()) {
                        sb.append(zipAppInfo.name);
                        sb.append("-");
                        sb.append(zipAppInfo.installedSeq);
                        if (i != WVCommonConfig.commonConfig.monitoredApps.length - 1) {
                            sb.append(",");
                        }
                    }
                }
                if (WVMonitorService.getPackageMonitorInterface() != null) {
                    WVMonitorService.getPackageMonitorInterface().commitZCacheDiurnalOverview(sb.toString());
                }
                ConfigStorage.putStringVal(SPNAME_CONFIG, "package_uploadData", format);
            }
        }
    }

    public void updateConfig(String str, String str2, String str3, WVConfigUpdateFromType wVConfigUpdateFromType) {
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
                final String str4 = str;
                final String str5 = str2;
                final String str6 = str3;
                final WVConfigUpdateFromType wVConfigUpdateFromType2 = wVConfigUpdateFromType;
                WVThreadPool.getInstance().execute(new Runnable() {
                    public void run() {
                        WVConfigManager.this.doUpdateByKey(str4, str5, str6, wVConfigUpdateFromType2);
                    }
                });
                return;
            }
            doUpdateByKey(str, str2, str3, wVConfigUpdateFromType);
        }
    }

    /* access modifiers changed from: private */
    public void doUpdateByKey(String str, String str2, String str3, WVConfigUpdateFromType wVConfigUpdateFromType) {
        boolean z;
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            if (!"3".equals(GlobalConfig.zType) || wVConfigUpdateFromType == WVConfigUpdateFromType.WVConfigUpdateFromZCache3_0) {
                if (TextUtils.isEmpty(str3)) {
                    try {
                        z = WVConfigUtils.isNeedUpdate(str2, str);
                    } catch (Exception unused) {
                        z = false;
                    }
                } else {
                    z = true;
                }
                if ("3".equals(GlobalConfig.zType)) {
                    z = true;
                }
                TaoLog.i(TAG, "update key=[" + str + "],needUpdate=[" + z + Operators.ARRAY_END_STR);
                if (wVConfigUpdateFromType == WVConfigUpdateFromType.WVConfigUpdateFromTypeLocaleChange) {
                    z = true;
                }
                if (z) {
                    WVConfigHandler wVConfigHandler = this.mConfigMap.get(str);
                    if (wVConfigHandler != null) {
                        if (!wVConfigHandler.getUpdateStatus() || System.currentTimeMillis() - updateTime >= updateInterval) {
                            wVConfigHandler.setUpdateStatus(true);
                            wVConfigHandler.setSnapshotN(str2);
                            final long currentTimeMillis = System.currentTimeMillis();
                            final WVConfigHandler wVConfigHandler2 = wVConfigHandler;
                            final String str4 = str;
                            final String str5 = str2;
                            final WVConfigUpdateFromType wVConfigUpdateFromType2 = wVConfigUpdateFromType;
                            wVConfigHandler.update(str3, new WVConfigUpdateCallback() {
                                public void updateStatus(WVConfigUpdateCallback.CONFIG_UPDATE_STATUS config_update_status, int i) {
                                    wVConfigHandler2.setUpdateStatus(false);
                                    WVConfigManager.access$404(WVConfigManager.this);
                                    if (WVConfigManager.this.updateConfigCount >= WVConfigManager.this.mConfigMap.size()) {
                                        int unused = WVConfigManager.this.updateConfigCount = 0;
                                        WVEventService.getInstance().onEvent(6002);
                                    }
                                    if (str4.equals("common") || str4.equals("domain") || str4.equals("monitor") || !"3".equals(GlobalConfig.zType)) {
                                        boolean equals = WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.SUCCESS.equals(config_update_status);
                                        WVConfigMonitorInterface configMonitor = WVMonitorService.getConfigMonitor();
                                        if (equals) {
                                            ConfigStorage.putStringVal(WVConfigManager.SPNAME_CONFIG, str4, str5);
                                            if (configMonitor != null) {
                                                configMonitor.didOccurUpdateConfigSuccess(str4);
                                            }
                                        } else if (configMonitor != null && !config_update_status.equals(WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.UNKNOWN_ERROR)) {
                                            String str = str4;
                                            int ordinal = config_update_status.ordinal();
                                            configMonitor.didOccurUpdateConfigError(str, ordinal, str4 + ":" + str5 + ":" + config_update_status);
                                        }
                                        if (configMonitor != null) {
                                            WVMonitorService.getConfigMonitor().didUpdateConfig(str4, wVConfigUpdateFromType2.ordinal(), System.currentTimeMillis() - currentTimeMillis, equals ? 1 : 0, i);
                                        }
                                    }
                                    TaoLog.i(WVConfigManager.TAG, "isUpdateSuccess " + str4 + " : " + config_update_status);
                                }

                                public void updateError(String str, String str2) {
                                    WVConfigMonitorInterface configMonitor = WVMonitorService.getConfigMonitor();
                                    if (configMonitor != null) {
                                        String str3 = str4;
                                        configMonitor.didOccurUpdateConfigError(str3, 7, str + ":" + str2);
                                    }
                                }
                            });
                        } else {
                            return;
                        }
                    }
                } else {
                    this.updateConfigCount++;
                }
                if (this.updateConfigCount >= this.mConfigMap.size()) {
                    this.updateConfigCount = 0;
                    WVEventService.getInstance().onEvent(6002);
                }
            }
        }
    }

    public WVConfigHandler registedHandler(String str) {
        if (this.mConfigMap == null) {
            return null;
        }
        return this.mConfigMap.get(str);
    }

    public void removeHandler(String str) {
        if (this.mConfigMap != null) {
            this.mConfigMap.remove(str);
        }
    }

    public void resetConfig() {
        if (this.mConfigMap != null) {
            Enumeration<String> keys = this.mConfigMap.keys();
            while (keys.hasMoreElements()) {
                ConfigStorage.putStringVal(SPNAME_CONFIG, keys.nextElement(), "0");
            }
        }
        updateTime = 0;
    }

    public HashMap getConfigVersions() {
        HashMap hashMap = new HashMap();
        if (this.mConfigMap != null) {
            Enumeration<String> keys = this.mConfigMap.keys();
            while (keys.hasMoreElements()) {
                String nextElement = keys.nextElement();
                String stringVal = ConfigStorage.getStringVal(SPNAME_CONFIG, nextElement, "0");
                if (!stringVal.contains(".")) {
                    Long valueOf = Long.valueOf(Long.parseLong(stringVal));
                    if (valueOf.longValue() == 0) {
                        stringVal = "NO VERSION";
                    } else if (valueOf.longValue() == Long.MAX_VALUE) {
                        stringVal = "CUSTOM VERION";
                    }
                }
                hashMap.put(nextElement, stringVal);
            }
        }
        return hashMap;
    }

    public void setUpdateInterval(long j) {
        updateIntervalServer = j;
    }

    public void setConfigDomain(String str) {
        if (!TextUtils.isEmpty(str)) {
            this.configDomain = str;
        }
        TaoLog.w(TAG, "changeConfigDomain : " + str);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0067, code lost:
        r7 = android.taobao.windvane.util.ConfigStorage.getStringVal(SPNAME_CONFIG, "abt", "a");
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String getConfigUrl(java.lang.String r5, java.lang.String r6, java.lang.String r7, java.lang.String r8) {
        /*
            r4 = this;
            android.taobao.windvane.config.WVLocaleConfig r0 = android.taobao.windvane.config.WVLocaleConfig.getInstance()
            java.lang.String r0 = r0.mCurrentLocale
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "https://wvcfg.alicdn.com/"
            java.lang.String r3 = r4.configDomain
            boolean r2 = r2.equals(r3)
            if (r2 != 0) goto L_0x001b
            java.lang.String r2 = r4.configDomain
            r1.append(r2)
            goto L_0x0022
        L_0x001b:
            java.lang.String r2 = r4.configDomainByEnv()
            r1.append(r2)
        L_0x0022:
            java.lang.String r2 = "/bizcache/5/windvane/"
            r1.append(r2)
            r1.append(r5)
            java.lang.String r5 = "/"
            r1.append(r5)
            r1.append(r6)
            java.lang.String r5 = "-"
            r1.append(r5)
            r1.append(r8)
            java.lang.String r5 = "/"
            r1.append(r5)
            android.taobao.windvane.config.GlobalConfig r5 = android.taobao.windvane.config.GlobalConfig.getInstance()
            java.lang.String r5 = r5.getAppKey()
            r1.append(r5)
            java.lang.String r5 = "-"
            r1.append(r5)
            java.lang.String r5 = android.taobao.windvane.config.WVConfigUtils.dealAppVersion()
            r1.append(r5)
            if (r0 == 0) goto L_0x0060
            java.lang.String r5 = "-"
            r1.append(r5)
            r1.append(r0)
        L_0x0060:
            java.lang.String r5 = "/"
            r1.append(r5)
            if (r7 != 0) goto L_0x0080
            java.lang.String r5 = "wv_main_config"
            java.lang.String r6 = "abt"
            java.lang.String r7 = "a"
            java.lang.String r7 = android.taobao.windvane.util.ConfigStorage.getStringVal(r5, r6, r7)
            r5 = 0
            char r5 = r7.charAt(r5)
            r6 = 97
            if (r6 > r5) goto L_0x007e
            r6 = 99
            if (r5 <= r6) goto L_0x0080
        L_0x007e:
            java.lang.String r7 = "a"
        L_0x0080:
            r1.append(r7)
            java.lang.String r5 = "/settings.json"
            r1.append(r5)
            java.lang.String r5 = "CONFIG_URL"
            java.lang.String r6 = r1.toString()
            android.taobao.windvane.util.TaoLog.w(r5, r6)
            java.lang.String r5 = r1.toString()
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.config.WVConfigManager.getConfigUrl(java.lang.String, java.lang.String, java.lang.String, java.lang.String):java.lang.String");
    }

    public void registerConfigImpl(String str, IConfig iConfig) {
        this.mOtherConfigMap.put(str, iConfig);
    }

    public void updateConfigByKey(String str, String str2) {
        IConfig iConfig = this.mOtherConfigMap.get(str);
        if (iConfig != null) {
            iConfig.setConfig(str2);
        }
    }

    public String configDomainByEnv() {
        switch (GlobalConfig.env) {
            case ONLINE:
                return "https://wvcfg.alicdn.com";
            case PRE:
                return "http://h5.wapa.taobao.com";
            case DAILY:
                return "https://h5.waptest.taobao.com";
            default:
                return "https://wvcfg.alicdn.com";
        }
    }
}
