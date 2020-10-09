package com.alibaba.ut.abtest.internal.config;

import android.text.TextUtils;
import com.alibaba.ut.abtest.internal.ABConstants;
import com.alibaba.ut.abtest.internal.util.LogUtils;
import com.alibaba.ut.abtest.internal.util.SystemInformation;
import com.alibaba.ut.abtest.internal.util.Utils;
import com.alibaba.ut.abtest.internal.util.hash.Hashing;
import com.taobao.orange.OConfigListener;
import com.taobao.orange.OrangeConfig;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class OrangeConfigService implements OConfigListener {
    private static final String KEY_DATA_TRIGGER_ENABLED = "data_trigger_enabled";
    private static final String KEY_DOWNLOAD_EXPERIMENT_DATA_DELAY_TIME = "download_experiment_data_delay_time";
    private static final String KEY_ENABLED = "enabled";
    private static final String KEY_NAV_ENABLED = "nav_enabled";
    private static final String KEY_NAV_IGNORES = "nav_ignores";
    private static final String KEY_REQUEST_EXPERIMENT_DATA_INTERVAL_TIME = "request_experiment_data_interval_time";
    private static final String KEY_TRACK_1022_DISABLED_EXPERIMENTS = "track_1022_disabled_experiments";
    private static final String KEY_TRACK_1022_DISABLED_GROUPS = "track_1022_disabled_groups";
    private static final String KEY_TRACK_AUTO_ENABLED = "track_auto_enabled";
    private static final String NS_SDK_CONFIG = "yixiu_sdk_config";
    private static final String TAG = "OrangeConfigService";
    private static OrangeConfigService instance;
    private boolean dataTriggerEnabled = true;
    private long downloadExperimentDataDelayTime = 60000;
    private boolean enabled = true;
    private boolean navEnabled = true;
    private Set<String> navIgnores = new HashSet();
    private final Object navIgnoresLock = new Object();
    private long requestExperimentDataIntervalTime = 60000;
    private Set<Long> track1022DisabledExperiments = new HashSet();
    private final Object track1022DisabledExperimentsLock = new Object();
    private Set<Long> track1022DisabledGroups = new HashSet();
    private final Object track1022DisabledGroupsLock = new Object();
    private boolean trackAutoEnabled = true;

    private OrangeConfigService() {
    }

    public static OrangeConfigService getInstance() {
        if (instance == null) {
            synchronized (OrangeConfigService.class) {
                if (instance == null) {
                    instance = new OrangeConfigService();
                }
            }
        }
        return instance;
    }

    public synchronized void initialize() {
        OrangeConfig.getInstance().registerListener(new String[]{NS_SDK_CONFIG}, this, true);
        syncConfig();
    }

    public void syncConfig() {
        int size;
        checkConfigUpdate();
        checkTrack1022DisabledExperimentsUpdate();
        checkTrack1022DisabledGroupsUpdate();
        synchronized (this.navIgnoresLock) {
            size = this.navIgnores == null ? 0 : this.navIgnores.size();
        }
        LogUtils.logDAndReport(TAG, "当前设备一休配置：\n全局开启=" + this.enabled + ",\n" + "触发更新开启=" + this.dataTriggerEnabled + ",\n" + "自动埋点开启=" + this.trackAutoEnabled + ", \n" + "同步数据间隔时间=" + this.requestExperimentDataIntervalTime + ", \n" + "获取数据随机范围=" + this.downloadExperimentDataDelayTime + "\n" + "导航拦截开启=" + this.navEnabled + ",\n" + "导航拦截忽略数量=" + size);
    }

    public void onConfigUpdate(String str, Map<String, String> map) {
        LogUtils.logD(TAG, "onConfigUpdate. namespace=" + str + ", map=" + map);
        if (TextUtils.equals(str, NS_SDK_CONFIG)) {
            syncConfig();
        }
    }

    private void checkConfigUpdate() {
        Map<String, String> configs = OrangeConfig.getInstance().getConfigs(NS_SDK_CONFIG);
        if (configs != null && !configs.isEmpty()) {
            try {
                String utdid = SystemInformation.getInstance().getUtdid();
                int i = Utils.toInt(configs.get(KEY_ENABLED), -1);
                if (i >= 0) {
                    this.enabled = isInSample(i, utdid + Calendar.getInstance().get(3) + "SDK");
                } else {
                    this.enabled = true;
                }
                int i2 = Utils.toInt(configs.get(KEY_NAV_ENABLED), -1);
                if (i2 >= 0) {
                    this.navEnabled = isInSample(i2, utdid + Calendar.getInstance().get(3) + "SDK");
                } else {
                    this.navEnabled = true;
                }
                int i3 = Utils.toInt(configs.get(KEY_DATA_TRIGGER_ENABLED), -1);
                if (i3 >= 0) {
                    this.dataTriggerEnabled = isInSample(i3, utdid + Calendar.getInstance().get(3) + "DATA_TRIGGER");
                } else {
                    this.dataTriggerEnabled = true;
                }
                int i4 = Utils.toInt(configs.get(KEY_TRACK_AUTO_ENABLED), -1);
                if (i4 >= 0) {
                    this.trackAutoEnabled = isInSample(i4, utdid + Calendar.getInstance().get(3) + "TRACK_AUTO");
                } else {
                    this.trackAutoEnabled = true;
                }
            } catch (Throwable th) {
                LogUtils.logE(TAG, th.getMessage(), th);
            }
            try {
                if (this.navEnabled) {
                    String str = configs.get(KEY_NAV_IGNORES);
                    if (TextUtils.isEmpty(str)) {
                        synchronized (this.navIgnoresLock) {
                            this.navIgnores.clear();
                        }
                    } else {
                        String[] split = Utils.split(str, ",", true);
                        synchronized (this.navIgnoresLock) {
                            this.navIgnores.clear();
                            if (split != null) {
                                for (String add : split) {
                                    this.navIgnores.add(add);
                                }
                            }
                        }
                    }
                }
            } catch (Throwable th2) {
                LogUtils.logE(TAG, th2.getMessage(), th2);
            }
            try {
                this.requestExperimentDataIntervalTime = Utils.toLong(configs.get(KEY_REQUEST_EXPERIMENT_DATA_INTERVAL_TIME), 60000);
                if (this.requestExperimentDataIntervalTime < 0) {
                    this.requestExperimentDataIntervalTime = 60000;
                }
            } catch (Throwable th3) {
                LogUtils.logE(TAG, th3.getMessage(), th3);
            }
            try {
                this.downloadExperimentDataDelayTime = Utils.toLong(configs.get(KEY_DOWNLOAD_EXPERIMENT_DATA_DELAY_TIME), 60000);
                if (this.downloadExperimentDataDelayTime < 0) {
                    this.downloadExperimentDataDelayTime = 60000;
                }
            } catch (Throwable th4) {
                LogUtils.logE(TAG, th4.getMessage(), th4);
            }
        }
    }

    private boolean isInSample(int i, String str) {
        if (i == 0) {
            return false;
        }
        int abs = Math.abs(Hashing.getMurmur3_32().hashString(str, ABConstants.BasicConstants.DEFAULT_CHARSET).asInt()) % 10000;
        LogUtils.logD(TAG, "isInSample, seed=" + str + ", configValue=" + i + ", sample=" + abs);
        if (abs < i) {
            return true;
        }
        return false;
    }

    private void checkTrack1022DisabledExperimentsUpdate() {
        long[] splitLongs;
        Map<String, String> configs = OrangeConfig.getInstance().getConfigs(NS_SDK_CONFIG);
        if (configs != null) {
            try {
                if (!configs.isEmpty()) {
                    String str = configs.get(KEY_TRACK_1022_DISABLED_EXPERIMENTS);
                    LogUtils.logD(TAG, "checkTrack1022DisabledExperimentsUpdate. value=" + str);
                    synchronized (this.track1022DisabledExperimentsLock) {
                        this.track1022DisabledExperiments.clear();
                        if (!TextUtils.isEmpty(str) && (splitLongs = Utils.splitLongs(str)) != null && splitLongs.length > 0) {
                            for (long valueOf : splitLongs) {
                                this.track1022DisabledExperiments.add(Long.valueOf(valueOf));
                            }
                        }
                    }
                    return;
                }
            } catch (Throwable th) {
                LogUtils.logE(TAG, th.getMessage(), th);
                return;
            }
        }
        synchronized (this.track1022DisabledExperimentsLock) {
            this.track1022DisabledExperiments.clear();
        }
    }

    public boolean isTrack1022ExperimentDisabled(Long l) {
        boolean contains;
        if (l == null || l.longValue() <= 0) {
            return false;
        }
        try {
            synchronized (this.track1022DisabledExperimentsLock) {
                contains = this.track1022DisabledExperiments.contains(Long.valueOf(l.longValue()));
            }
            return contains;
        } catch (Throwable th) {
            LogUtils.logE(TAG, th.getMessage(), th);
            return false;
        }
    }

    private void checkTrack1022DisabledGroupsUpdate() {
        long[] splitLongs;
        Map<String, String> configs = OrangeConfig.getInstance().getConfigs(NS_SDK_CONFIG);
        if (configs != null) {
            try {
                if (!configs.isEmpty()) {
                    String str = configs.get(KEY_TRACK_1022_DISABLED_GROUPS);
                    LogUtils.logD(TAG, "checkTrack1022DisabledGroupsUpdate. value=" + str);
                    synchronized (this.track1022DisabledGroupsLock) {
                        this.track1022DisabledGroups.clear();
                        if (!TextUtils.isEmpty(str) && (splitLongs = Utils.splitLongs(str)) != null && splitLongs.length > 0) {
                            for (long valueOf : splitLongs) {
                                this.track1022DisabledGroups.add(Long.valueOf(valueOf));
                            }
                        }
                    }
                    return;
                }
            } catch (Throwable th) {
                LogUtils.logE(TAG, th.getMessage(), th);
                return;
            }
        }
        synchronized (this.track1022DisabledGroupsLock) {
            this.track1022DisabledGroups.clear();
        }
    }

    public boolean isTrack1022GroupDisabled(Long l) {
        boolean contains;
        if (l == null || l.longValue() <= 0) {
            return false;
        }
        try {
            synchronized (this.track1022DisabledGroupsLock) {
                contains = this.track1022DisabledGroups.contains(l);
            }
            return contains;
        } catch (Throwable th) {
            LogUtils.logE(TAG, th.getMessage(), th);
            return false;
        }
    }

    public long getRequestExperimentDataIntervalTime() {
        return this.requestExperimentDataIntervalTime;
    }

    public long getDownloadExperimentDataDelayTime() {
        return this.downloadExperimentDataDelayTime;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public boolean isNavEnabled() {
        return this.navEnabled;
    }

    public boolean isNavIgnored(String str) {
        boolean contains;
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        try {
            synchronized (this.navIgnoresLock) {
                contains = this.navIgnores.contains(str);
            }
            return contains;
        } catch (Throwable th) {
            LogUtils.logE(TAG, th.getMessage(), th);
            return false;
        }
    }

    public boolean isDataTriggerEnabled() {
        return this.dataTriggerEnabled;
    }

    public boolean isTrackAutoEnabled() {
        return this.trackAutoEnabled;
    }
}
