package com.alibaba.analytics.core.selfmonitor;

import android.text.TextUtils;
import com.alibaba.analytics.core.config.SystemConfigMgr;
import com.alibaba.analytics.utils.Logger;
import com.alibaba.appmonitor.event.EventType;
import com.alibaba.appmonitor.model.Metric;
import com.alibaba.appmonitor.model.MetricRepo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.mtl.appmonitor.model.Measure;
import com.alibaba.mtl.appmonitor.model.MeasureSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SelfMonitorConfigMgr implements SystemConfigMgr.IKVChangeListener {
    private static SelfMonitorConfigMgr instance = new SelfMonitorConfigMgr();
    private final String TAG_ABTEST_BUCKET = "abtest_bucket";
    private final String TAG_ABTEST_OFFLINE = "abtest_offline";
    private final String TAG_SAMPLING_MONITOR_AP = "sampling_monitor_ap";
    private final String TAG_SAMPLING_MONITOR_UT = "sampling_monitor_ut";
    private Map<String, MeasureSet> mAMBucketConfig = Collections.synchronizedMap(new HashMap());
    private Set<String> mAMOfflineConfig = Collections.synchronizedSet(new HashSet());
    private Set<String> mAMSamplingConfig = Collections.synchronizedSet(new HashSet());
    private Set<String> mUtSamplingConfig = Collections.synchronizedSet(new HashSet());

    private SelfMonitorConfigMgr() {
        SystemConfigMgr.getInstance().register("sampling_monitor_ut", this);
        SystemConfigMgr.getInstance().register("sampling_monitor_ap", this);
        SystemConfigMgr.getInstance().register("abtest_bucket", this);
        SystemConfigMgr.getInstance().register("abtest_offline", this);
        parseConfig(this.mUtSamplingConfig, SystemConfigMgr.getInstance().get("sampling_monitor_ut"));
        parseConfig(this.mAMSamplingConfig, SystemConfigMgr.getInstance().get("sampling_monitor_ap"));
        parseConfig(this.mAMOfflineConfig, SystemConfigMgr.getInstance().get("abtest_offline"));
        parseBucketConfig("abtest_bucket", SystemConfigMgr.getInstance().get("abtest_bucket"));
        new ConfigArrivedMonitor().start();
        SelfChecker.getInstance().register();
    }

    public static SelfMonitorConfigMgr getInstance() {
        return instance;
    }

    public void onChange(String str, String str2) {
        Set<String> set;
        if ("sampling_monitor_ut".equalsIgnoreCase(str)) {
            set = this.mUtSamplingConfig;
        } else if ("sampling_monitor_ap".equalsIgnoreCase(str)) {
            set = this.mAMSamplingConfig;
        } else {
            set = "abtest_offline".equalsIgnoreCase(str) ? this.mAMOfflineConfig : null;
        }
        if (set != null) {
            parseConfig(set, str2);
        }
        if ("abtest_bucket".equalsIgnoreCase(str)) {
            parseBucketConfig("abtest_bucket", str2);
        }
    }

    private void parseConfig(Set<String> set, String str) {
        String[] split;
        if (set != null) {
            set.clear();
            if (!TextUtils.isEmpty(str) && (split = str.split(",")) != null && split.length > 0) {
                set.addAll(Arrays.asList(split));
            }
        }
    }

    public boolean isNeedMonitorForUT(String str) {
        return this.mUtSamplingConfig.contains(str);
    }

    public MeasureSet getMeasureSet(String str, String str2) {
        Map<String, MeasureSet> map = this.mAMBucketConfig;
        return map.get(str + ":" + str2);
    }

    public boolean isNeedMonitorForBucket(String str, String str2) {
        return getMeasureSet(str, str2) != null;
    }

    public boolean isNeedMonitorForOffline(EventType eventType, String str, String str2) {
        Set<String> set = this.mAMOfflineConfig;
        StringBuilder sb = new StringBuilder();
        sb.append(eventType);
        sb.append(":");
        sb.append(str);
        sb.append(":");
        sb.append(str2);
        return set.contains(sb.toString());
    }

    private void parseBucketConfig(String str, String str2) {
        Set<String> keySet;
        String[] split;
        this.mAMBucketConfig.clear();
        if (!TextUtils.isEmpty(str2)) {
            try {
                JSONArray parseArray = JSON.parseArray(str2);
                if (parseArray != null) {
                    for (int i = 0; i < parseArray.size(); i++) {
                        JSONObject jSONObject = (JSONObject) parseArray.get(i);
                        if (jSONObject != null) {
                            String string = jSONObject.getString("module");
                            String string2 = jSONObject.getString("mp");
                            JSONObject jSONObject2 = (JSONObject) jSONObject.get("buckets");
                            if (!(jSONObject2 == null || (keySet = jSONObject2.keySet()) == null)) {
                                MeasureSet create = MeasureSet.create();
                                for (String next : keySet) {
                                    String string3 = jSONObject2.getString(next);
                                    if (!TextUtils.isEmpty(string3) && (split = string3.split(",")) != null) {
                                        ArrayList arrayList = new ArrayList();
                                        for (int i2 = 0; i2 < split.length; i2++) {
                                            try {
                                                arrayList.add(Double.valueOf(split[i2]));
                                            } catch (Throwable unused) {
                                            }
                                        }
                                        create.addMeasure(new Measure(next, Double.valueOf(0.0d), arrayList));
                                    }
                                }
                                Map<String, MeasureSet> map = this.mAMBucketConfig;
                                map.put(string + ":" + string2, create);
                                Metric metric = MetricRepo.getRepo().getMetric(string, string2);
                                if (metric != null) {
                                    MetricRepo.getRepo().add(new Metric(string + "_abtest", string2, create, metric.getDimensionSet(), false));
                                }
                            }
                        }
                    }
                }
            } catch (Throwable th) {
                Logger.w("Parse Monitor Bucket error ", th, new Object[0]);
            }
        }
    }
}
