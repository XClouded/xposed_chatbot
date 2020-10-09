package com.alibaba.ut.abtest.internal.bucketing;

import android.net.Uri;
import android.text.TextUtils;
import com.alibaba.ut.abtest.UTABDataListener;
import com.alibaba.ut.abtest.UTABTest;
import com.alibaba.ut.abtest.internal.ABConstants;
import com.alibaba.ut.abtest.internal.ABContext;
import com.alibaba.ut.abtest.internal.bucketing.model.ExperimentGroup;
import com.alibaba.ut.abtest.internal.bucketing.model.ExperimentGroupPO;
import com.alibaba.ut.abtest.internal.database.WhereConditionCollector;
import com.alibaba.ut.abtest.internal.util.LogUtils;
import com.alibaba.ut.abtest.internal.util.Preferences;
import com.alibaba.ut.abtest.internal.util.StringUtils;
import com.alibaba.ut.abtest.internal.util.Utils;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ExperimentManager {
    private static final String TAG = "ExperimentManager";
    private static ExperimentManager instance;
    private ConcurrentHashMap<String, Set<UTABDataListener>> dataListeners = new ConcurrentHashMap<>();
    private ExperimentCache experimentCache = new ExperimentCache(this.experimentGroupDao);
    private ExperimentGroupDao experimentGroupDao = new ExperimentGroupDao();

    private ExperimentManager() {
    }

    public static synchronized ExperimentManager getInstance() {
        ExperimentManager experimentManager;
        synchronized (ExperimentManager.class) {
            if (instance == null) {
                instance = new ExperimentManager();
            }
            experimentManager = instance;
        }
        return experimentManager;
    }

    public void loadMemoryCache() {
        try {
            this.experimentCache.initialize();
        } catch (Throwable th) {
            LogUtils.logE(TAG, th.getMessage(), th);
        }
    }

    public void clearMemoryCache() {
        this.experimentCache.clear();
    }

    public void clearExperimentsCache() {
        try {
            this.experimentCache.clear();
            clearExperimentDataVersion();
            clearExperimentDataSignature();
            this.experimentGroupDao.delete((String) null, new String[0]);
        } catch (Throwable th) {
            LogUtils.logE(TAG, "clearExperimentsCache", th);
        }
    }

    public Long getExperimentId(long j) {
        return this.experimentCache.getExperimentId(j);
    }

    public List<ExperimentGroup> getExperimentGroups(Uri uri) {
        return this.experimentCache.getItem(uri);
    }

    public List<ExperimentGroup> getExperimentGroups(String str) {
        return this.experimentCache.getItem(str);
    }

    public synchronized void saveExperiments(List<ExperimentGroupPO> list, long j, String str) {
        StringBuilder sb = new StringBuilder();
        sb.append("待转换实验分组数量");
        int i = 0;
        sb.append(list == null ? 0 : list.size());
        sb.append(", dataVersion=");
        sb.append(j);
        sb.append(", dataSignature=");
        sb.append(str);
        LogUtils.logD(TAG, sb.toString());
        List<ExperimentGroup> createExperimentGroups = ExperimentBuilder.createExperimentGroups(list);
        StringBuilder sb2 = new StringBuilder();
        sb2.append("待缓存实验分组数量");
        if (createExperimentGroups != null) {
            i = createExperimentGroups.size();
        }
        sb2.append(i);
        sb2.append(", dataVersion=");
        sb2.append(j);
        sb2.append(", dataSignature=");
        sb2.append(str);
        LogUtils.logD(TAG, sb2.toString());
        clearMemoryCache();
        this.experimentGroupDao.deleteByCurrentUser((WhereConditionCollector) null);
        if (createExperimentGroups != null && !createExperimentGroups.isEmpty()) {
            ArrayList arrayList = new ArrayList(createExperimentGroups.size());
            for (ExperimentGroup next : createExperimentGroups) {
                this.experimentCache.addItem(next);
                arrayList.add(ExperimentBuilder.createExperimentGroupDO(next));
            }
            this.experimentGroupDao.insertInTx(arrayList);
            LogUtils.logD(TAG, "待存储实验分组数量" + arrayList.size() + ", dataVersion=" + j + ", dataSignature=" + str);
        }
        setExperimentDataVersion(j);
        setExperimentDataSignature(str);
    }

    public void saveBetaExperiments(List<ExperimentGroupPO> list) {
        for (ExperimentGroup next : ExperimentBuilder.createExperimentGroups(list)) {
            this.experimentCache.removeItem(next);
            this.experimentCache.addItem(next);
        }
    }

    public long getExperimentDataVersion() {
        Preferences instance2 = Preferences.getInstance();
        return instance2.getLong(ABConstants.Preference.EXPERIMENT_DATA_VERSION + StringUtils.nullToEmpty(ABContext.getInstance().getUserId()), 0);
    }

    private void setExperimentDataVersion(long j) {
        Preferences instance2 = Preferences.getInstance();
        instance2.putLong(ABConstants.Preference.EXPERIMENT_DATA_VERSION + StringUtils.nullToEmpty(ABContext.getInstance().getUserId()), j);
    }

    private void clearExperimentDataVersion() {
        Map<String, ?> all = Preferences.getInstance().getAll();
        if (all != null) {
            for (String next : all.keySet()) {
                if (next.startsWith(ABConstants.Preference.EXPERIMENT_DATA_VERSION)) {
                    Preferences.getInstance().removeAsync(next);
                }
            }
        }
    }

    public String getExperimentDataSignature() {
        Preferences instance2 = Preferences.getInstance();
        return instance2.getString(ABConstants.Preference.EXPERIMENT_DATA_SIGNATURE + StringUtils.nullToEmpty(ABContext.getInstance().getUserId()), (String) null);
    }

    private void setExperimentDataSignature(String str) {
        Preferences instance2 = Preferences.getInstance();
        instance2.putString(ABConstants.Preference.EXPERIMENT_DATA_SIGNATURE + StringUtils.nullToEmpty(ABContext.getInstance().getUserId()), str);
    }

    private void clearExperimentDataSignature() {
        Map<String, ?> all = Preferences.getInstance().getAll();
        if (all != null) {
            for (String next : all.keySet()) {
                if (next.startsWith(ABConstants.Preference.EXPERIMENT_DATA_SIGNATURE)) {
                    Preferences.getInstance().removeAsync(next);
                }
            }
        }
    }

    public void addDataListener(String str, String str2, UTABDataListener uTABDataListener) {
        if (!TextUtils.equals(str, UTABTest.COMPONENT_URI)) {
            String experimentComponentKey = Utils.getExperimentComponentKey(str, str2);
            Set set = this.dataListeners.get(experimentComponentKey);
            synchronized (this) {
                if (set == null) {
                    try {
                        set = new HashSet();
                        this.dataListeners.put(experimentComponentKey, set);
                    } catch (Throwable th) {
                        throw th;
                    }
                }
                set.add(uTABDataListener);
            }
        }
    }

    public void removeDataListener(String str, String str2, UTABDataListener uTABDataListener) {
        if (!TextUtils.equals(str, UTABTest.COMPONENT_URI)) {
            String experimentComponentKey = Utils.getExperimentComponentKey(str, str2);
            if (uTABDataListener == null) {
                this.dataListeners.remove(experimentComponentKey);
                return;
            }
            Set set = this.dataListeners.get(experimentComponentKey);
            if (set != null) {
                synchronized (this) {
                    set.remove(uTABDataListener);
                }
            }
        }
    }
}
