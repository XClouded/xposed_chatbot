package com.ali.telescope.api;

import android.app.Application;
import com.ali.telescope.base.plugin.INameConverter;
import com.ali.telescope.base.plugin.PluginIDContant;
import com.ali.telescope.data.AppConfig;
import com.ali.telescope.data.DeviceInfoManager;
import com.ali.telescope.data.beans.AppParam;
import com.ali.telescope.interfaces.OnAccurateBootListener;
import com.ali.telescope.interfaces.TelescopeErrReporter;
import com.ali.telescope.interfaces.TelescopeEventData;
import com.ali.telescope.internal.Constants;
import com.ali.telescope.internal.looper.Loopers;
import com.ali.telescope.internal.pluginengine.PluginDataManager;
import com.ali.telescope.internal.pluginengine.PluginManager;
import com.ali.telescope.internal.pluginengine.TelescopeContextImpl;
import com.ali.telescope.internal.pluginengine.model.PluginData;
import com.ali.telescope.internal.plugins.startPref.StartPrefTaskBean;
import com.ali.telescope.internal.report.ErrReporterListener;
import com.ali.telescope.internal.report.ErrorReportManager;
import com.ali.telescope.internal.report.ReportManager;
import com.ali.telescope.internal.report.SendManager;
import com.ali.telescope.util.StrictRuntime;
import com.ali.telescope.util.TelescopeLog;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Telescope {
    private static Telescope instance;
    /* access modifiers changed from: private */
    public Application application = null;
    private TelescopeContextImpl tcontext = null;

    public interface OnDesignatedActivityName {
    }

    private Telescope(Application application2) {
        this.application = application2;
    }

    public static void start(TelescopeConfig telescopeConfig) {
        try {
            telescopeConfig.checkValid();
            instance = new Telescope(telescopeConfig.application);
            TelescopeLog.sLogLevel = telescopeConfig.logLevel;
            StrictRuntime.sStrict = telescopeConfig.isStrictMode;
            instance.init(telescopeConfig);
            telescopeConfig.recycle();
        } catch (Throwable th) {
            th.printStackTrace();
            StrictRuntime.onHandle("init", "build failed! check your init params.", th);
        }
    }

    public void notifyPluginConfigUpdate(String str) {
        PluginDataManager.onConfigChanged(str, this.application);
    }

    private boolean init(TelescopeConfig telescopeConfig) {
        initAppConfig(telescopeConfig);
        DeviceInfoManager.instance().init(this.application);
        loadPluginData(telescopeConfig);
        this.tcontext = new TelescopeContextImpl();
        if (telescopeConfig.nameConverter != null) {
            instance.tcontext.setNameConverter(telescopeConfig.nameConverter);
        } else {
            instance.tcontext.setNameConverter(INameConverter.DEFAULT_CONVERTR);
        }
        initSuperlog();
        ErrorReportManager.initContext(telescopeConfig.application);
        addTelescopeErrorReporter(new ErrReporterListener());
        PluginManager.init(this.application, this.tcontext);
        Map<String, PluginData> allPluginData = PluginDataManager.getAllPluginData();
        PluginManager.loadPlugin(allPluginData);
        updateAwbPlugin(allPluginData);
        return true;
    }

    private void updateAwbPlugin(Map<String, PluginData> map) {
        PluginData pluginData;
        if (Constants.isAwb && (pluginData = map.get(PluginIDContant.KEY_MAINTHREADBLOCKPLUGIN)) != null) {
            boolean z = pluginData.enable;
        }
    }

    private void initAppConfig(TelescopeConfig telescopeConfig) {
        AppParam appParam = new AppParam();
        appParam.appKey = telescopeConfig.appKey;
        appParam.versionName = telescopeConfig.appVersion;
        appParam.packageName = telescopeConfig.packageName;
        appParam.utdid = TelescopeConfig.utdid;
        AppParam.imei = TelescopeConfig.imei;
        AppParam.imsi = TelescopeConfig.imsi;
        AppParam.channel = TelescopeConfig.channel;
        appParam.isAliyunos = telescopeConfig.isAliyunos;
        AppConfig.init(appParam);
    }

    private void loadPluginData(TelescopeConfig telescopeConfig) {
        PluginDataManager.loadLocalConfig(this.application, telescopeConfig.appVersion);
    }

    private void initSuperlog() {
        Loopers.getReportHandler().post(new Runnable() {
            public void run() {
                HashMap hashMap = new HashMap();
                if (AppConfig.isAliyunos.booleanValue()) {
                    hashMap.put("appId", AppConfig.appKey + "@aliyunos");
                } else {
                    hashMap.put("appId", AppConfig.appKey + "@android");
                }
                hashMap.put("appKey", AppConfig.appKey);
                hashMap.put("appVersion", AppConfig.versionName);
                hashMap.put("packageName", AppConfig.packageName);
                hashMap.put("utdid", AppConfig.utdid);
                HashMap hashMap2 = new HashMap();
                hashMap2.put("isRooted", String.valueOf(DeviceInfoManager.instance().getIsRooted()));
                hashMap2.put("isEmulator", String.valueOf(DeviceInfoManager.instance().isEmulator()));
                hashMap2.put("mobileBrand", String.valueOf(DeviceInfoManager.instance().getMobileBrand()));
                hashMap2.put("mobileModel", String.valueOf(DeviceInfoManager.instance().getMobileModel()));
                hashMap2.put("apiLevel", String.valueOf(DeviceInfoManager.instance().getApiLevel()));
                hashMap2.put("storeTotalSize", String.valueOf(DeviceInfoManager.instance().getStoreTotalSize()));
                hashMap2.put("deviceTotalMemory", String.valueOf(DeviceInfoManager.instance().getDeviceTotalMemory()));
                hashMap2.put("memoryThreshold", String.valueOf(DeviceInfoManager.instance().getMemoryThreshold()));
                hashMap2.put("cpuModel", String.valueOf(DeviceInfoManager.instance().getCpuModel()));
                hashMap2.put("cpuBrand", String.valueOf(DeviceInfoManager.instance().getCpuBrand()));
                hashMap2.put("cpuArch", String.valueOf(DeviceInfoManager.instance().getCpuArch()));
                hashMap2.put("cpuProcessCount", String.valueOf(DeviceInfoManager.instance().getCpuProcessCount()));
                hashMap2.put("cpuFreqArray", Arrays.toString(DeviceInfoManager.instance().getCpuFreqArray()));
                hashMap2.put("cpuMaxFreq", String.valueOf(DeviceInfoManager.instance().getCpuMaxFreq()));
                hashMap2.put("cpuMinFreq", String.valueOf(DeviceInfoManager.instance().getCpuMinFreq()));
                hashMap2.put("gpuMaxFreq", String.valueOf(DeviceInfoManager.instance().getGpuMaxFreq()));
                hashMap2.put("screenWidth", String.valueOf(DeviceInfoManager.instance().getScreenWidth()));
                hashMap2.put("screenHeight", String.valueOf(DeviceInfoManager.instance().getScreenHeight()));
                hashMap2.put("screenDensity", String.valueOf(DeviceInfoManager.instance().getScreenDensity()));
                ReportManager.getInstance().initSuperLog(Telescope.this.application, hashMap, hashMap2);
                if (SendManager.SEND_TOOL_SWITCH == 0) {
                    SendManager.initRestAPI(Telescope.this.application);
                }
            }
        });
    }

    public static void addOnAccurateBootListener(OnAccurateBootListener onAccurateBootListener) {
        if (instance != null) {
            instance.tcontext.addOnAccurateBootListener(onAccurateBootListener);
        }
    }

    public static void addTelescopeErrorReporter(TelescopeErrReporter telescopeErrReporter) {
        if (telescopeErrReporter != null) {
            ErrorReportManager.addTelescopeErrorReporter(telescopeErrReporter);
        }
    }

    public static void addTelescopeEventDataListener(TelescopeEventData telescopeEventData) {
        if (telescopeEventData != null) {
            SendManager.addListener(telescopeEventData);
        }
    }

    public static class TelescopeConfig {
        public static String channel = null;
        public static String imei = null;
        public static String imsi = null;
        public static String utdid = "undefined";
        public String appKey = null;
        /* access modifiers changed from: private */
        public String appVersion = "";
        /* access modifiers changed from: private */
        public Application application = null;
        public Boolean isAliyunos = false;
        /* access modifiers changed from: private */
        public boolean isStrictMode = false;
        /* access modifiers changed from: private */
        public int logLevel = 1;
        public INameConverter nameConverter;
        public String packageName = null;

        public TelescopeConfig logLevel(int i) {
            this.logLevel = i;
            return this;
        }

        public TelescopeConfig application(Application application2) {
            this.application = application2;
            return this;
        }

        public TelescopeConfig strictMode(boolean z) {
            this.isStrictMode = z;
            return this;
        }

        public TelescopeConfig appKey(String str) {
            this.appKey = str;
            return this;
        }

        public TelescopeConfig appVersion(String str) {
            this.appVersion = str;
            return this;
        }

        public TelescopeConfig packageName(String str) {
            this.packageName = str;
            return this;
        }

        public TelescopeConfig nameConverter(INameConverter iNameConverter) {
            this.nameConverter = iNameConverter;
            return this;
        }

        public TelescopeConfig imsi(String str) {
            imsi = str;
            return this;
        }

        public TelescopeConfig imei(String str) {
            imei = str;
            return this;
        }

        public TelescopeConfig channel(String str) {
            channel = str;
            return this;
        }

        public TelescopeConfig utdid(String str) {
            utdid = str;
            return this;
        }

        public TelescopeConfig isAliyunos(Boolean bool) {
            this.isAliyunos = bool;
            return this;
        }

        public void checkValid() throws RuntimeException {
            if (this.application == null || this.appKey == null || this.appVersion == null || this.packageName == null || this.nameConverter == null || channel == null) {
                throw new RuntimeException("You must set application!");
            }
        }

        /* access modifiers changed from: private */
        public void recycle() {
            this.application = null;
            this.logLevel = 1;
            this.isStrictMode = false;
        }
    }

    public static void reportTaskStart(String str) {
        if (instance != null && instance.tcontext != null) {
            instance.tcontext.getBeanReport().send(new StartPrefTaskBean(str, System.currentTimeMillis(), true));
        }
    }

    public static void reportTaskEnd(String str) {
        if (instance != null && instance.tcontext != null) {
            instance.tcontext.getBeanReport().send(new StartPrefTaskBean(str, System.currentTimeMillis(), false));
        }
    }
}
