package com.alimamaunion.base.configcenter;

import android.app.Application;
import android.os.SystemClock;
import android.text.TextUtils;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.json.JSONException;
import org.json.JSONObject;
import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

public class EtaoConfigCenter implements IConfigCenter {
    private static final long DEFAULT_PERIOD = 30000;
    private static EtaoConfigCenter instance;
    private boolean isDebug;
    private Application mApplication;
    private IBackgroundJudge mBackgroundJudge;
    private IConfigCenterCache mConfigCenterCache;
    private IConfigCenterRequest mConfigCenterRequest;
    private List<String> mConfigList = new ArrayList();
    private Map<String, Set<Observer<String>>> mConfigObserverMap = new HashMap();
    private long mPeriod = 60;
    private int mPollPeriod;
    private TimeUnit mPollTimeUnit = TimeUnit.SECONDS;
    private Scheduler.Worker mWorker;
    private Map<String, UpdateState> update = new HashMap();

    public static EtaoConfigCenter getInstance() {
        if (instance == null) {
            synchronized (EtaoConfigCenter.class) {
                if (instance == null) {
                    instance = new EtaoConfigCenter();
                }
            }
        }
        return instance;
    }

    private EtaoConfigCenter() {
    }

    public EtaoConfigCenter init(String[] strArr, Application application) {
        this.mApplication = application;
        if (this.mConfigCenterCache == null) {
            this.mConfigCenterCache = getDefaultCache(this.mApplication);
        }
        if (this.mConfigCenterRequest != null) {
            this.mConfigList.addAll(Arrays.asList(strArr));
            setUpPoller(this.mPeriod, this.mPollTimeUnit);
            return this;
        }
        throw new IllegalArgumentException("ConfigCenterRequest is null");
    }

    public EtaoConfigCenter setDebug(boolean z) {
        this.isDebug = z;
        return this;
    }

    public EtaoConfigCenter setConfigCenterCache(IConfigCenterCache iConfigCenterCache) {
        this.mConfigCenterCache = iConfigCenterCache;
        return this;
    }

    public EtaoConfigCenter setConfigCenterRequest(IConfigCenterRequest iConfigCenterRequest) {
        this.mConfigCenterRequest = iConfigCenterRequest;
        return this;
    }

    public EtaoConfigCenter setBackgroundJudge(IBackgroundJudge iBackgroundJudge) {
        this.mBackgroundJudge = iBackgroundJudge;
        return this;
    }

    public EtaoConfigCenter setPollPeriod(long j, TimeUnit timeUnit) {
        this.mPeriod = j;
        this.mPollTimeUnit = timeUnit;
        return this;
    }

    public void clearCache(String str) {
        this.mConfigCenterCache.remove(str);
    }

    public EtaoConfigCenter addObserver(String str, Observer<String> observer) {
        if (this.mConfigObserverMap == null) {
            this.mConfigObserverMap = new HashMap();
        }
        Set set = this.mConfigObserverMap.get(str);
        if (set == null) {
            set = new HashSet();
        }
        set.add(observer);
        this.mConfigObserverMap.put(str, set);
        return this;
    }

    public void unsubscribe(String str, Observer<String> observer) {
        Set set;
        if (this.mConfigObserverMap != null && (set = this.mConfigObserverMap.get(str)) != null) {
            set.remove(observer);
        }
    }

    public void requestAll() {
        checkConfig(true);
    }

    private synchronized void setUpPoller(long j, TimeUnit timeUnit) {
        if (this.mWorker == null) {
            this.mWorker = Schedulers.newThread().createWorker();
        }
        this.mWorker.schedulePeriodically(new Action0() {
            public void call() {
                EtaoConfigCenter.this.checkConfig(false);
            }
        }, 0, j, timeUnit);
    }

    /* access modifiers changed from: private */
    public void checkConfig(boolean z) {
        if (this.mBackgroundJudge == null || !this.mBackgroundJudge.isBackground()) {
            HashMap hashMap = new HashMap();
            for (int i = 0; i < this.mConfigList.size(); i++) {
                String str = this.mConfigList.get(i);
                if (isKeyStale(str, z)) {
                    hashMap.put(str, this.mConfigCenterCache.read(str).lastModified);
                }
            }
            request(hashMap);
        }
    }

    public void requestSingleConfig(String str, boolean z) {
        HashMap hashMap = new HashMap();
        if (isKeyStale(str, z)) {
            hashMap.put(str, this.mConfigCenterCache.read(str).lastModified);
        }
        request(hashMap);
    }

    private synchronized boolean isKeyStale(String str, boolean z) {
        long j;
        boolean z2 = true;
        if (this.update.containsKey(str)) {
            UpdateState updateState = this.update.get(str);
            long j2 = updateState.lastUpdate + updateState.interval;
            if (!z) {
                if (SystemClock.elapsedRealtime() <= j2) {
                    z2 = false;
                }
            }
            updateState.lastUpdate = z2 ? SystemClock.elapsedRealtime() : updateState.lastUpdate;
            return z2;
        }
        try {
            j = Long.valueOf(this.mConfigCenterCache.read(str).updateInterval).longValue() * 1000;
        } catch (NumberFormatException unused) {
            j = 0;
        }
        if (j == 0) {
            j = DEFAULT_PERIOD;
        }
        this.update.put(str, new UpdateState(SystemClock.elapsedRealtime(), j));
        return true;
    }

    public boolean getSwitch(String str, String str2, boolean z) {
        String str3 = getSwitch(str, str2);
        return !TextUtils.isEmpty(str3) ? TextUtils.equals("1", str3) : z;
    }

    public String getSwitch(String str, String str2, String str3) {
        String str4 = getSwitch(str, str2);
        return !TextUtils.isEmpty(str4) ? str4 : str3;
    }

    public String getSwitch(String str, String str2) {
        String configResult = getConfigResult(str);
        if (TextUtils.isEmpty(configResult)) {
            return "";
        }
        try {
            return new JSONObject(configResult).optString(str2);
        } catch (JSONException unused) {
            return "";
        }
    }

    public String getConfigResult(String str) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(str);
        return getConfigResult((List<String>) arrayList).get(str);
    }

    public Map<String, String> getConfigResult(List<String> list) {
        return getConfigResult(list, false);
    }

    public Map<String, String> getConfigResult(List<String> list, boolean z, boolean z2) {
        return getConfigResult(list);
    }

    public String getConfigResult(String str, boolean z) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(str);
        return getConfigResult((List<String>) arrayList, z).get(str);
    }

    public Map<String, String> getConfigResult(List<String> list, boolean z) {
        if (this.isDebug) {
            configToast(list.toString());
        }
        HashMap hashMap = new HashMap();
        if (this.mConfigCenterCache == null) {
            return hashMap;
        }
        for (int i = 0; i < list.size(); i++) {
            String str = list.get(i);
            ConfigData read = this.mConfigCenterCache.read(str);
            if (read != null) {
                if (!z || read.isChanged) {
                    hashMap.put(str, read.data == null ? "" : new String(read.data));
                } else {
                    hashMap.put(str, "");
                }
                if (read.isChanged) {
                    read.isChanged = false;
                    this.mConfigCenterCache.write(str, read);
                }
            }
        }
        return hashMap;
    }

    private void request(Map<String, String> map) {
        if (map != null && map.size() > 0) {
            try {
                JSONObject jSONObject = new JSONObject();
                for (Map.Entry next : map.entrySet()) {
                    jSONObject.put((String) next.getKey(), next.getValue());
                }
                if (this.mConfigCenterRequest != null) {
                    this.mConfigCenterRequest.doRequest(map, jSONObject.toString(), this);
                }
            } catch (Exception unused) {
            }
        }
    }

    public void onDataReceived(String str, ConfigData configData) {
        HashMap hashMap = new HashMap();
        hashMap.put(str, configData);
        onDataReceived((Map<String, String>) null, (Map<String, ConfigData>) hashMap);
    }

    public void onDataReceived(Map<String, String> map, Map<String, ConfigData> map2) {
        for (Map.Entry next : map2.entrySet()) {
            ConfigData configData = (ConfigData) next.getValue();
            if (configData != null) {
                String str = (String) next.getKey();
                configData.isChanged = map == null || (!TextUtils.isEmpty(map.get(str)) && !TextUtils.equals(map.get(str), configData.lastModified));
                UpdateState updateState = this.update.get(next.getKey());
                long j = 0;
                if (updateState != null) {
                    try {
                        j = 1000 * Long.valueOf(configData.updateInterval).longValue();
                    } catch (Exception unused) {
                    }
                    updateState.interval = j;
                    updateState.lastUpdate = SystemClock.elapsedRealtime();
                }
                this.mConfigCenterCache.write((String) next.getKey(), (ConfigData) next.getValue());
                Set<Observer> set = this.mConfigObserverMap.get(next.getKey());
                if (set != null) {
                    for (Observer observer : set) {
                        if (observer != null) {
                            Observable.just(new String(configData.data)).subscribe(observer);
                        }
                    }
                }
            }
        }
    }

    private void configToast(String str) {
        if (this.mApplication != null) {
            Toast.makeText(this.mApplication, str, 0).show();
        }
    }

    private class UpdateState {
        public long interval;
        public long lastUpdate;

        public UpdateState(long j, long j2) {
            this.lastUpdate = j;
            this.interval = j2;
        }
    }

    private IConfigCenterCache getDefaultCache(Application application) {
        return new DefaultConfigCenterCache(application);
    }
}
