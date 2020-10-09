package android.taobao.windvane.packageapp.cleanup;

import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.taobao.windvane.config.WVCommonConfig;
import android.taobao.windvane.monitor.UserTrackUtil;
import android.taobao.windvane.monitor.WVMonitorService;
import android.taobao.windvane.packageapp.zipapp.ConfigManager;
import android.taobao.windvane.packageapp.zipapp.data.ZipAppInfo;
import android.taobao.windvane.packageapp.zipapp.data.ZipAppTypeEnum;
import android.taobao.windvane.packageapp.zipapp.data.ZipGlobalConfig;
import android.taobao.windvane.packageapp.zipapp.data.ZipUpdateInfoEnum;
import android.taobao.windvane.packageapp.zipapp.utils.ZipAppConstants;
import android.taobao.windvane.service.WVEventContext;
import android.taobao.windvane.service.WVEventListener;
import android.taobao.windvane.service.WVEventResult;
import android.taobao.windvane.service.WVEventService;
import android.taobao.windvane.util.ConfigStorage;
import android.taobao.windvane.util.TaoLog;
import android.text.TextUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class WVPackageAppCleanup {
    private static final int CLEAN_CAUSE_AT_CLEAN_PERIOD = 0;
    private static final int CLEAN_CAUSE_LEVEL_HIGH = 1;
    public static final long PER_APP_SPACE = 700000;
    private static String SP_IFNO_KEY = "sp_ifno_key";
    private static String SP_KEY = "lastDel";
    private static String SP_NAME = "WVpackageApp";
    private static final String TAG = "WVPackageAppCleanup";
    private static WVPackageAppCleanup instance;
    HashMap<String, InfoSnippet> infoMap = new HashMap<>();
    private long lastDelTime = ConfigStorage.getLongVal(SP_NAME, SP_KEY, 0);
    private UninstallListener listener;
    private boolean needWriteToDisk = false;

    public interface UninstallListener {
        void onUninstall(List<String> list);
    }

    private float getNoCacheRatio(int i, int i2) {
        int i3 = i2 + i;
        if (i3 == 0) {
            return 0.0f;
        }
        return ((float) i) / ((float) i3);
    }

    private WVPackageAppCleanup() {
    }

    public void init() {
        WVEventService.getInstance().addEventListener(new WVPageEventListener());
        initCheanUpInfoIfNeed();
    }

    public static WVPackageAppCleanup getInstance() {
        if (instance == null) {
            instance = new WVPackageAppCleanup();
        }
        return instance;
    }

    public HashMap<String, InfoSnippet> getInfoMap() {
        return this.infoMap;
    }

    public void updateAccessTimes(String str, boolean z) {
        initCheanUpInfoIfNeed();
        InfoSnippet infoSnippet = this.infoMap.get(str);
        if (infoSnippet == null) {
            initCleanupInfoFromLocal();
            infoSnippet = this.infoMap.get(str);
        }
        if (infoSnippet != null) {
            long currentTimeMillis = System.currentTimeMillis();
            if (infoSnippet.lastAccessTime + ((long) WVCommonConfig.commonConfig.packageAccessInterval) < currentTimeMillis) {
                this.needWriteToDisk = true;
                infoSnippet.count += 1.0d;
                infoSnippet.lastAccessTime = currentTimeMillis;
                if (z) {
                    infoSnippet.failCount++;
                }
            }
        }
    }

    public boolean needInstall(ZipAppInfo zipAppInfo) {
        initCheanUpInfoIfNeed();
        addInfoIfNeed(zipAppInfo);
        ZipGlobalConfig locGlobalConfig = ConfigManager.getLocGlobalConfig();
        boolean z = !locGlobalConfig.isAvailableData() || getInstallNum(locGlobalConfig.getAppsTable()) < getMaxInstallCapacity();
        if (!atCleanUpPeriod()) {
            if (!z && zipAppInfo.getPriority() >= 9 && zipAppInfo.status != ZipAppConstants.ZIP_REMOVED && zipAppInfo.getAppType() != ZipAppTypeEnum.ZIP_APP_TYPE_ZCACHE) {
                updateLastDelTime();
                cleanUp(1);
                z = true;
            }
        } else if (atCleanUpPeriod()) {
            updateLastDelTime();
            cleanUp(0);
        }
        if (zipAppInfo.status == ZipAppConstants.ZIP_REMOVED || zipAppInfo.getInfo() == ZipUpdateInfoEnum.ZIP_UPDATE_INFO_DELETE) {
            return false;
        }
        if (zipAppInfo.getAppType() == ZipAppTypeEnum.ZIP_APP_TYPE_ZCACHE) {
            return true;
        }
        return z;
    }

    private void initCheanUpInfoIfNeed() {
        if (!checkCleanUpCacheExist()) {
            initCleanUpInfo();
        }
    }

    private boolean atCleanUpPeriod() {
        return this.lastDelTime + ((long) WVCommonConfig.commonConfig.packageRemoveInterval) < System.currentTimeMillis();
    }

    @Deprecated
    public void saveInfoSnippetToDisk() {
        String str = "{}";
        if (this.infoMap == null || this.infoMap.size() != 0) {
            try {
                str = JsonUtil.getJsonString(this.infoMap);
            } catch (Exception e) {
                TaoLog.e(TAG, "saveInfoSnippetToDisk exception : " + e.getMessage());
            }
            ConfigStorage.putStringVal(SP_NAME, SP_IFNO_KEY, str);
        }
    }

    @Deprecated
    public void saveInfoSnippetToDiskInner() {
        String str = "{}";
        if (this.infoMap != null && this.infoMap.size() == 0) {
            return;
        }
        if (!this.needWriteToDisk) {
            this.needWriteToDisk = false;
            return;
        }
        try {
            str = JsonUtil.getJsonString(this.infoMap);
        } catch (Exception e) {
            TaoLog.e(TAG, "saveInfoSnippetToDisk exception : " + e.getMessage());
        }
        ConfigStorage.putStringVal(SP_NAME, SP_IFNO_KEY, str);
    }

    public void addInfoIfNeed(ZipAppInfo zipAppInfo) {
        if (zipAppInfo != null && !TextUtils.isEmpty(zipAppInfo.name) && this.infoMap.get(zipAppInfo.name) == null) {
            this.infoMap.put(zipAppInfo.name, new InfoSnippet(zipAppInfo.name, 0, 0, zipAppInfo.getPriority(), 0));
        }
    }

    private void initCleanUpInfo() {
        String stringVal = ConfigStorage.getStringVal(SP_NAME, SP_IFNO_KEY, "{}");
        if (TextUtils.isEmpty(stringVal) || stringVal.equals("{}")) {
            initCleanupInfoFromLocal();
            return;
        }
        try {
            this.infoMap = new HashMap<>();
            JSONObject jSONObject = new JSONObject(stringVal);
            Iterator<String> keys = jSONObject.keys();
            while (keys.hasNext()) {
                String next = keys.next();
                JSONObject optJSONObject = jSONObject.optJSONObject(next);
                InfoSnippet infoSnippet = new InfoSnippet();
                infoSnippet.count = optJSONObject.optDouble("count");
                infoSnippet.name = optJSONObject.optString("name");
                infoSnippet.failCount = optJSONObject.optInt("failCount");
                infoSnippet.needReinstall = optJSONObject.optBoolean("needReinstall");
                infoSnippet.lastAccessTime = optJSONObject.optLong("lastAccessTime");
                this.infoMap.put(next, infoSnippet);
            }
        } catch (Exception e) {
            TaoLog.e(TAG, "parse KEY_CLEAN_UP_INFO Exception:" + e.getMessage());
        }
    }

    private void initCleanupInfoFromLocal() {
        ZipGlobalConfig locGlobalConfig = ConfigManager.getLocGlobalConfig();
        if (locGlobalConfig == null || !locGlobalConfig.isAvailableData()) {
            this.infoMap = new HashMap<>();
            return;
        }
        Map<String, ZipAppInfo> appsTable = locGlobalConfig.getAppsTable();
        for (String next : appsTable.keySet()) {
            ZipAppInfo zipAppInfo = appsTable.get(next);
            if (this.infoMap.get(next) == null) {
                this.infoMap.put(next, new InfoSnippet(zipAppInfo.name, 0, System.currentTimeMillis(), zipAppInfo.getPriority(), 0));
            }
        }
    }

    private boolean checkCleanUpCacheExist() {
        return this.infoMap.size() != 0;
    }

    public void registerUninstallListener(UninstallListener uninstallListener) {
        this.listener = uninstallListener;
    }

    public List<String> cleanUp(int i) {
        Map<String, ZipAppInfo> appsTable = ConfigManager.getLocGlobalConfig().getAppsTable();
        initCleanupInfoFromLocal();
        appMonior(i);
        List<String> calcToRetainApp = calcToRetainApp(appsTable);
        if (this.listener != null && calcToRetainApp.size() > 0) {
            this.listener.onUninstall(calcToRetainApp);
        } else if (!WVCommonConfig.commonConfig.isCheckCleanup) {
            this.listener.onUninstall(calcToRetainApp);
        }
        clearCount();
        saveInfoSnippetToDisk();
        return calcToRetainApp;
    }

    private void appMonior(int i) {
        Map<String, ZipAppInfo> appsTable = ConfigManager.getLocGlobalConfig().getAppsTable();
        long devAvailableSpace = getDevAvailableSpace();
        int storageCapacity = getStorageCapacity();
        int size = appsTable.size();
        int i2 = size - storageCapacity;
        int i3 = i2 < 0 ? 0 : i2;
        float customRadio = getCustomRadio(appsTable);
        int countByType = getCountByType(appsTable, ZipAppConstants.ZIP_REMOVED);
        int countByType2 = getCountByType(appsTable, ZipAppConstants.ZIP_NEWEST);
        float noCacheRatio = getNoCacheRatio(countByType, countByType2);
        if (WVMonitorService.getPackageMonitorInterface() != null) {
            WVMonitorService.getPackageMonitorInterface().onStartCleanAppCache(devAvailableSpace, storageCapacity, size, i3, customRadio, countByType, countByType2, noCacheRatio, i);
        }
    }

    private int getCountByType(Map<String, ZipAppInfo> map, int i) {
        InfoSnippet infoSnippet;
        int i2 = 0;
        for (ZipAppInfo next : map.values()) {
            if (!(next.getAppType() == ZipAppTypeEnum.ZIP_APP_TYPE_ZCACHE || next.status != i || (infoSnippet = this.infoMap.get(next.name)) == null)) {
                double d = (double) i2;
                double d2 = infoSnippet.count;
                Double.isNaN(d);
                i2 = (int) (d + d2);
            }
        }
        return i2;
    }

    private float getCustomRadio(Map<String, ZipAppInfo> map) {
        int i = 0;
        int i2 = 0;
        for (ZipAppInfo next : map.values()) {
            if (next.getAppType() != ZipAppTypeEnum.ZIP_APP_TYPE_ZCACHE) {
                if (next.isOptional) {
                    i2++;
                }
                i++;
            }
        }
        if (i == 0) {
            return 0.0f;
        }
        return ((float) i2) / ((float) i);
    }

    private long getDevAvailableSpace() {
        try {
            StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
            if (Build.VERSION.SDK_INT >= 18) {
                return statFs.getAvailableBytes();
            }
            return ((long) statFs.getAvailableBlocks()) * ((long) statFs.getBlockSize());
        } catch (RuntimeException e) {
            UserTrackUtil.commitEvent(UserTrackUtil.EVENTID_ERROR, e.toString(), "", "");
            return 2147483647L;
        }
    }

    private List<String> calcToRetainApp(final Map<String, ZipAppInfo> map) {
        ArrayList arrayList = new ArrayList(this.infoMap.values());
        try {
            Collections.sort(arrayList, new Comparator<InfoSnippet>() {
                public int compare(InfoSnippet infoSnippet, InfoSnippet infoSnippet2) {
                    ZipAppInfo zipAppInfo = (ZipAppInfo) map.get(infoSnippet.name);
                    if (zipAppInfo == null) {
                        if (WVPackageAppCleanup.this.infoMap.containsValue(infoSnippet)) {
                            WVPackageAppCleanup.this.infoMap.remove(infoSnippet.name);
                        }
                        return -1;
                    }
                    ZipAppInfo zipAppInfo2 = (ZipAppInfo) map.get(infoSnippet2.name);
                    if (zipAppInfo2 == null) {
                        if (WVPackageAppCleanup.this.infoMap.containsValue(infoSnippet2)) {
                            WVPackageAppCleanup.this.infoMap.remove(infoSnippet2.name);
                        }
                        return 1;
                    } else if (zipAppInfo.getAppType() != ZipAppTypeEnum.ZIP_APP_TYPE_ZCACHE && zipAppInfo2.getAppType() == ZipAppTypeEnum.ZIP_APP_TYPE_ZCACHE) {
                        return -1;
                    } else {
                        if (zipAppInfo.getAppType() == ZipAppTypeEnum.ZIP_APP_TYPE_ZCACHE && zipAppInfo2.getAppType() != ZipAppTypeEnum.ZIP_APP_TYPE_ZCACHE) {
                            return 1;
                        }
                        if (zipAppInfo.getPriority() < 9 && zipAppInfo2.getPriority() >= 9) {
                            return -1;
                        }
                        if (zipAppInfo.getPriority() >= 9 && zipAppInfo2.getPriority() < 9) {
                            return 1;
                        }
                        double d = infoSnippet.count;
                        double priority = (double) zipAppInfo.getPriority();
                        double d2 = WVCommonConfig.commonConfig.packagePriorityWeight;
                        Double.isNaN(priority);
                        double d3 = d * ((priority * d2) + 1.0d);
                        double d4 = infoSnippet2.count;
                        double priority2 = (double) zipAppInfo2.getPriority();
                        double d5 = WVCommonConfig.commonConfig.packagePriorityWeight;
                        Double.isNaN(priority2);
                        double d6 = d4 * ((priority2 * d5) + 1.0d);
                        if (d3 == d6) {
                            if (zipAppInfo.getPriority() < zipAppInfo2.getPriority()) {
                                return -1;
                            }
                            if (zipAppInfo.getPriority() > zipAppInfo2.getPriority()) {
                                return 1;
                            }
                            if (zipAppInfo.status == ZipAppConstants.ZIP_REMOVED && zipAppInfo2.status == ZipAppConstants.ZIP_NEWEST) {
                                return -1;
                            }
                            if (zipAppInfo.status == ZipAppConstants.ZIP_NEWEST && zipAppInfo2.status == ZipAppConstants.ZIP_REMOVED) {
                                return 1;
                            }
                            if (!zipAppInfo.isOptional || zipAppInfo2.isOptional) {
                                return (!zipAppInfo.isOptional || !zipAppInfo2.isOptional) ? 1 : 1;
                            }
                            return -1;
                        } else if (d3 < d6) {
                            return -1;
                        } else {
                            return 1;
                        }
                    }
                }
            });
        } catch (Throwable th) {
            TaoLog.e(TAG, th.getMessage());
        }
        return getMostFrequentUsedApp(map, arrayList);
    }

    private void updateLastDelTime() {
        this.lastDelTime = System.currentTimeMillis();
        ConfigStorage.putLongVal(SP_NAME, SP_KEY, this.lastDelTime);
    }

    private void clearCount() {
        for (InfoSnippet next : this.infoMap.values()) {
            double d = next.count;
            next.count = Math.log(1.0d + d);
            if (next.count < 0.3d) {
                next.count = 0.0d;
                TaoLog.i(TAG, next.name + "visit count from : " + d + " to " + next.count);
            }
        }
    }

    private List<String> getMostFrequentUsedApp(Map<String, ZipAppInfo> map, List<InfoSnippet> list) {
        int size = map.size() - getStorageCapacity();
        if (size > 0) {
            return getToRetainList(map, list, size);
        }
        return getToRetainList(map, list, 0);
    }

    private List<String> getToRetainList(Map<String, ZipAppInfo> map, List<InfoSnippet> list, int i) {
        new ArrayList(list);
        if (i >= list.size()) {
            TaoLog.e(TAG, "缓存清理算法出错 ： 待清理的App数量不应大于清理队列中的长度");
        } else if (i != 0) {
            list = list.subList(i, list.size());
        }
        ArrayList arrayList = new ArrayList(list.size());
        for (int i2 = 0; i2 < list.size(); i2++) {
            arrayList.add(list.get(i2).name);
        }
        return arrayList;
    }

    private int getInstallNum(Map<String, ZipAppInfo> map) {
        int i = 0;
        for (ZipAppInfo isAppInstalled : map.values()) {
            if (isAppInstalled.isAppInstalled()) {
                i++;
            }
        }
        return i;
    }

    private int getStorageCapacity() {
        long availableSpace = getAvailableSpace();
        if (availableSpace < 52428800) {
            return 30;
        }
        if (availableSpace <= 52428800 || availableSpace >= ZipAppConstants.LIMITED_APP_SPACE) {
            return WVCommonConfig.commonConfig.packageMaxAppCount;
        }
        return 50;
    }

    private long getAvailableSpace() {
        return getDevAvailableSpace() + getInstalledSpace();
    }

    private int getMaxInstallCapacity() {
        long availableSpace = getAvailableSpace();
        if (availableSpace < 52428800) {
            return 30;
        }
        return (availableSpace <= 52428800 || availableSpace >= ZipAppConstants.LIMITED_APP_SPACE) ? 150 : 75;
    }

    private long getInstalledSpace() {
        ZipGlobalConfig locGlobalConfig = ConfigManager.getLocGlobalConfig();
        if (locGlobalConfig.isAvailableData()) {
            return ((long) getInstallNum(locGlobalConfig.getAppsTable())) * PER_APP_SPACE;
        }
        return 0;
    }

    public static class WVPageEventListener implements WVEventListener {
        public WVEventResult onEvent(int i, WVEventContext wVEventContext, Object... objArr) {
            if (i == 3003) {
                WVPackageAppCleanup.getInstance().saveInfoSnippetToDiskInner();
                TaoLog.d(WVPackageAppCleanup.TAG, "onEvent  PAGE_destroy");
                return null;
            } else if (i != 6001) {
                return null;
            } else {
                WVPackageAppCleanup.getInstance().saveInfoSnippetToDisk();
                TaoLog.d(WVPackageAppCleanup.TAG, "onEvent  PACKAGE_UPLOAD_COMPLETE");
                return null;
            }
        }
    }
}
