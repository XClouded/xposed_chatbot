package android.taobao.windvane.packageapp.zipapp;

import android.annotation.SuppressLint;
import android.taobao.windvane.config.GlobalConfig;
import android.taobao.windvane.config.WVCommonConfig;
import android.taobao.windvane.monitor.WVMonitorService;
import android.taobao.windvane.packageapp.WVPackageAppManager;
import android.taobao.windvane.packageapp.cleanup.WVPackageAppCleanup;
import android.taobao.windvane.packageapp.zipapp.data.ZipAppInfo;
import android.taobao.windvane.packageapp.zipapp.data.ZipAppResultCode;
import android.taobao.windvane.packageapp.zipapp.data.ZipAppTypeEnum;
import android.taobao.windvane.packageapp.zipapp.data.ZipGlobalConfig;
import android.taobao.windvane.packageapp.zipapp.data.ZipUpdateInfoEnum;
import android.taobao.windvane.packageapp.zipapp.utils.ZipAppConstants;
import android.taobao.windvane.packageapp.zipdownload.WVZipBPDownloader;
import android.taobao.windvane.util.NetWork;
import android.taobao.windvane.util.TaoLog;

import com.alibaba.aliweex.adapter.module.calendar.DateUtils;
import com.taobao.weex.el.parse.Operators;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class ZipAppDownloaderQueue extends PriorityBlockingQueue {
    private static String TAG = "PackageApp-ZipAppDownloaderQueue";
    private static volatile ZipAppDownloaderQueue instance;
    private static AtomicBoolean isTaskActive = new AtomicBoolean(false);
    public int appendDownloadCount = 0;
    private WVZipBPDownloader currentDownloader = null;
    private String currentTaskName = null;
    public int finishedCount = 0;
    private long forbidDownloadMaxInterval = DateUtils.WEEK;
    public boolean hasPrefetch = false;
    public String instantTaskName = null;
    private boolean isAppBackground = false;
    private boolean isResetState = false;
    private boolean isWifi = false;
    public final Object lock = new Object();
    public int needDownloadCount = WVCommonConfig.commonConfig.packageDownloadLimit;
    public Set<String> prefetch = new HashSet();
    public boolean refreshQueue = false;
    public int successCount = 0;
    private long taskStartTime = 0;
    private long updateInterval = 600000;

    public static ZipAppDownloaderQueue getInstance() {
        if (instance == null) {
            synchronized (ZipAppDownloaderQueue.class) {
                if (instance == null) {
                    instance = new ZipAppDownloaderQueue();
                }
            }
        }
        return instance;
    }

    public void startPriorityDownLoader() {
        this.isWifi = false;
        this.finishedCount = 0;
        this.successCount = 0;
        this.isResetState = false;
        this.currentDownloader = null;
        this.updateInterval = WVCommonConfig.commonConfig.updateInterval * 2;
        doTask();
    }

    private boolean doTask() {
        if (getInstance().size() == 0 || this.finishedCount >= this.needDownloadCount) {
            this.finishedCount = 0;
            this.isResetState = false;
            isTaskActive.compareAndSet(true, false);
            return false;
        }
        ZipGlobalConfig locGlobalConfig = ConfigManager.getLocGlobalConfig();
        ZipDownloaderComparable zipDownloaderComparable = (ZipDownloaderComparable) getInstance().poll();
        if (this.currentTaskName != null) {
            return false;
        }
        this.currentTaskName = zipDownloaderComparable.getAppName();
        ZipAppInfo appInfo = locGlobalConfig.getAppInfo(zipDownloaderComparable.getAppName());
        if (appInfo == null || appInfo.name.equals(getInstance().instantTaskName)) {
            updateState();
            return false;
        }
        if (!(this.taskStartTime == 0 || this.finishedCount == 0 || !TaoLog.getLogStatus())) {
            String str = TAG;
            TaoLog.d(str, appInfo.name + " doTask use time(ms) : " + (System.currentTimeMillis() - this.taskStartTime));
        }
        this.taskStartTime = System.currentTimeMillis();
        if (appInfo.s == appInfo.installedSeq && appInfo.status == ZipAppConstants.ZIP_NEWEST) {
            updateState();
            return false;
        } else if (appInfo.installedSeq != 0 || WVPackageAppCleanup.getInstance().needInstall(appInfo) || !WVCommonConfig.commonConfig.isCheckCleanup) {
            try {
                int i = 4;
                if (this.currentDownloader == null) {
                    String zipUrl = appInfo.getZipUrl();
                    WVPackageAppManager instance2 = WVPackageAppManager.getInstance();
                    if (appInfo.v.equals(appInfo.installedVersion)) {
                        i = 2;
                    }
                    this.currentDownloader = new WVZipBPDownloader(zipUrl, instance2, i, appInfo);
                } else {
                    WVZipBPDownloader wVZipBPDownloader = this.currentDownloader;
                    String zipUrl2 = appInfo.getZipUrl();
                    if (appInfo.v.equals(appInfo.installedVersion)) {
                        i = 2;
                    }
                    wVZipBPDownloader.update(zipUrl2, i, appInfo);
                }
                if (this.currentDownloader.getHandler() != null) {
                    this.currentDownloader.getHandler().post(this.currentDownloader);
                }
                return true;
            } catch (Exception unused) {
                String str2 = TAG;
                TaoLog.w(str2, "update app error : " + appInfo.name);
                updateState();
                return false;
            }
        } else {
            appInfo.status = ZipAppConstants.ZIP_REMOVED;
            if (appInfo.isOptional) {
                appInfo.s = 0;
                appInfo.v = "0";
            }
            updateState();
            return false;
        }
    }

    public void startUpdateAppsTask() {
        if (!isTaskActive.compareAndSet(false, true)) {
            TaoLog.e(TAG, "duplicate task [download zipApps]");
            return;
        }
        TaoLog.i(TAG, "task [download zipApps]");
        if (WVCommonConfig.commonConfig.packageAppStatus != 2) {
            String str = TAG;
            TaoLog.i(str, "not update zip, packageAppStatus is : " + WVCommonConfig.commonConfig.packageAppStatus);
            isTaskActive.compareAndSet(true, false);
        } else if (GlobalConfig.context == null || isAppForeground()) {
            ArrayList arrayList = null;
            if (isUpdateFinish()) {
                getInstance().clear();
                for (Map.Entry<String, ZipAppInfo> value : ConfigManager.getLocGlobalConfig().getAppsTable().entrySet()) {
                    ZipAppInfo zipAppInfo = (ZipAppInfo) value.getValue();
                    if (!needForbidDownload() || zipAppInfo.isInstantApp || zipAppInfo.getPriority() >= 10) {
                        if (zipAppInfo.getInfo() == ZipUpdateInfoEnum.ZIP_UPDATE_INFO_DELETE || zipAppInfo.status == ZipAppConstants.ZIP_REMOVED) {
                            if (zipAppInfo.installedSeq != 0 || zipAppInfo.getInfo() == ZipUpdateInfoEnum.ZIP_UPDATE_INFO_DELETE) {
                                if (arrayList == null) {
                                    arrayList = new ArrayList();
                                }
                                arrayList.add(zipAppInfo);
                            }
                        } else if (zipAppInfo.installedSeq < zipAppInfo.s) {
                            int priority = zipAppInfo.getPriority();
                            if (zipAppInfo.isPreViewApp) {
                                priority = 10;
                            }
                            if (isContinueUpdate(zipAppInfo)) {
                                getInstance().offer(new ZipDownloaderComparable(zipAppInfo.name, priority));
                            }
                        }
                    }
                }
                if (arrayList != null) {
                    Iterator it = arrayList.iterator();
                    while (it.hasNext()) {
                        ZipAppInfo zipAppInfo2 = (ZipAppInfo) it.next();
                        try {
                            int unInstall = ZipAppManager.getInstance().unInstall(zipAppInfo2);
                            if (unInstall == ZipAppResultCode.SECCUSS) {
                                String str2 = TAG;
                                TaoLog.i(str2, zipAppInfo2.name + " unInstall success");
                            } else if (TaoLog.getLogStatus()) {
                                String str3 = TAG;
                                TaoLog.w(str3, "resultcode:" + unInstall + "[updateApps] [" + zipAppInfo2 + Operators.ARRAY_END_STR + " unInstall fail ");
                            }
                        } catch (Exception unused) {
                        }
                    }
                }
                getInstance().removeDuplicate();
                startPriorityDownLoader();
            } else if (this.currentDownloader == null || this.currentDownloader.getDownLoaderStatus() == Thread.State.TERMINATED) {
                doTask();
            } else if (this.updateInterval < System.currentTimeMillis() - this.taskStartTime) {
                this.currentDownloader.cancelTask(true);
                this.currentDownloader = null;
                doTask();
            }
        } else {
            TaoLog.i(TAG, "not update zip, app is background");
            isTaskActive.compareAndSet(true, false);
        }
    }

    private boolean needForbidDownload() {
        long time = new Date().getTime();
        return time >= WVCommonConfig.commonConfig.disableInstallPeriod_start && time < (((WVCommonConfig.commonConfig.disableInstallPeriod_end - WVCommonConfig.commonConfig.disableInstallPeriod_start) > this.forbidDownloadMaxInterval ? 1 : ((WVCommonConfig.commonConfig.disableInstallPeriod_end - WVCommonConfig.commonConfig.disableInstallPeriod_start) == this.forbidDownloadMaxInterval ? 0 : -1)) > 0 ? WVCommonConfig.commonConfig.disableInstallPeriod_start + this.forbidDownloadMaxInterval : WVCommonConfig.commonConfig.disableInstallPeriod_end);
    }

    public void resetState() {
        if (WVMonitorService.getPackageMonitorInterface() != null) {
            if (this.finishedCount != 0) {
                WVMonitorService.getPackageMonitorInterface().commitPackageQueueInfo("1", (long) this.finishedCount, (long) this.successCount);
                TaoLog.i(TAG, "packageAppQueue s : " + this.successCount + "f : " + this.finishedCount);
            } else {
                TaoLog.i(TAG, "no zipApp need update");
            }
        }
        isTaskActive.compareAndSet(true, false);
        this.finishedCount = 0;
        this.successCount = 0;
        this.isResetState = true;
        this.currentDownloader = null;
        this.currentTaskName = null;
        if (this.refreshQueue) {
            this.needDownloadCount -= this.finishedCount;
        } else if (this.appendDownloadCount == 0) {
            this.needDownloadCount = WVCommonConfig.commonConfig.packageDownloadLimit;
        } else {
            this.needDownloadCount = this.appendDownloadCount;
        }
        this.finishedCount = 0;
    }

    public boolean isUpdateFinish() {
        return this.isResetState || getInstance().size() == 0 || this.needDownloadCount == 0 || this.finishedCount >= this.needDownloadCount;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:21:0x005c, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x006c, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void updateState() {
        /*
            r5 = this;
            monitor-enter(r5)
            boolean r0 = r5.hasPrefetch     // Catch:{ all -> 0x006d }
            if (r0 == 0) goto L_0x0009
            boolean r0 = r5.refreshQueue     // Catch:{ all -> 0x006d }
            if (r0 != 0) goto L_0x000f
        L_0x0009:
            boolean r0 = r5.isUpdateFinish()     // Catch:{ all -> 0x006d }
            if (r0 == 0) goto L_0x0061
        L_0x000f:
            boolean r0 = r5.refreshQueue     // Catch:{ all -> 0x006d }
            r1 = 0
            if (r0 == 0) goto L_0x0040
            java.util.Set<java.lang.String> r0 = r5.prefetch     // Catch:{ all -> 0x006d }
            java.util.Iterator r0 = r0.iterator()     // Catch:{ all -> 0x006d }
        L_0x001a:
            boolean r2 = r0.hasNext()     // Catch:{ all -> 0x006d }
            if (r2 == 0) goto L_0x0035
            java.lang.Object r2 = r0.next()     // Catch:{ all -> 0x006d }
            java.lang.String r2 = (java.lang.String) r2     // Catch:{ all -> 0x006d }
            android.taobao.windvane.packageapp.zipapp.ZipDownloaderComparable r3 = new android.taobao.windvane.packageapp.zipapp.ZipDownloaderComparable     // Catch:{ all -> 0x006d }
            r4 = 9
            r3.<init>(r2, r4)     // Catch:{ all -> 0x006d }
            android.taobao.windvane.packageapp.zipapp.ZipAppDownloaderQueue r2 = getInstance()     // Catch:{ all -> 0x006d }
            r2.offer(r3)     // Catch:{ all -> 0x006d }
            goto L_0x001a
        L_0x0035:
            java.util.Set<java.lang.String> r0 = r5.prefetch     // Catch:{ all -> 0x006d }
            r0.clear()     // Catch:{ all -> 0x006d }
            r5.refreshQueue = r1     // Catch:{ all -> 0x006d }
            r5.doTask()     // Catch:{ all -> 0x006d }
            goto L_0x005d
        L_0x0040:
            int r0 = r5.appendDownloadCount     // Catch:{ all -> 0x006d }
            if (r0 == 0) goto L_0x005d
            r0 = 1
            r5.hasPrefetch = r0     // Catch:{ all -> 0x006d }
            r5.resetState()     // Catch:{ all -> 0x006d }
            r5.appendDownloadCount = r1     // Catch:{ all -> 0x006d }
            android.taobao.windvane.config.WVConfigManager r0 = android.taobao.windvane.config.WVConfigManager.getInstance()     // Catch:{ all -> 0x006d }
            android.taobao.windvane.config.WVConfigManager$WVConfigUpdateFromType r1 = android.taobao.windvane.config.WVConfigManager.WVConfigUpdateFromType.WVConfigUpdateFromTypeActive     // Catch:{ all -> 0x006d }
            boolean r0 = r0.checkIfUpdate(r1)     // Catch:{ all -> 0x006d }
            if (r0 != 0) goto L_0x005b
            r5.startUpdateAppsTask()     // Catch:{ all -> 0x006d }
        L_0x005b:
            monitor-exit(r5)
            return
        L_0x005d:
            r5.resetState()     // Catch:{ all -> 0x006d }
            goto L_0x006b
        L_0x0061:
            boolean r0 = r5.isResetState     // Catch:{ all -> 0x006d }
            if (r0 != 0) goto L_0x006b
            r0 = 0
            r5.currentTaskName = r0     // Catch:{ all -> 0x006d }
            r5.doTask()     // Catch:{ all -> 0x006d }
        L_0x006b:
            monitor-exit(r5)
            return
        L_0x006d:
            r0 = move-exception
            monitor-exit(r5)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.packageapp.zipapp.ZipAppDownloaderQueue.updateState():void");
    }

    public synchronized void updateFinshCount(boolean z) {
        if (!this.isResetState) {
            if (z) {
                this.successCount++;
            }
            this.finishedCount++;
        }
    }

    @SuppressLint({"NewApi"})
    public boolean isContinueUpdate(ZipAppInfo zipAppInfo) {
        if (zipAppInfo == null) {
            return false;
        }
        if (zipAppInfo.getAppType() == ZipAppTypeEnum.ZIP_APP_TYPE_REACT || zipAppInfo.getAppType() == ZipAppTypeEnum.ZIP_APP_TYPE_UNKNOWN || zipAppInfo.isInUse) {
            zipAppInfo.status = ZipAppConstants.ZIP_REMOVED;
            ConfigManager.updateGlobalConfig(zipAppInfo, (String) null, false);
            return true;
        }
        if (!zipAppInfo.isInstantApp && !zipAppInfo.isPreViewApp && !this.isWifi) {
            if (NetWork.isConnectionInexpensive()) {
                this.isWifi = true;
            } else if (zipAppInfo.getIs2GUpdate() || zipAppInfo.getIs3GUpdate()) {
                return true;
            } else {
                if (TaoLog.getLogStatus()) {
                    String str = TAG;
                    TaoLog.i(str, "updateAllApps: can not install app [" + zipAppInfo.name + "] network is not wifi");
                }
                return false;
            }
        }
        return true;
    }

    public void removeDuplicate() {
        try {
            Iterator it = getInstance().iterator();
            HashSet hashSet = new HashSet();
            ArrayList arrayList = new ArrayList();
            while (it.hasNext()) {
                ZipDownloaderComparable zipDownloaderComparable = (ZipDownloaderComparable) it.next();
                if (hashSet.add(zipDownloaderComparable.getAppName())) {
                    arrayList.add(zipDownloaderComparable);
                }
            }
            getInstance().clear();
            getInstance().addAll(arrayList);
        } catch (Exception unused) {
        }
    }

    public void removeByName(String str) {
        Iterator it = getInstance().iterator();
        ArrayList arrayList = new ArrayList();
        while (it.hasNext()) {
            ZipDownloaderComparable zipDownloaderComparable = (ZipDownloaderComparable) it.next();
            if (!str.equals(zipDownloaderComparable.getAppName())) {
                arrayList.add(zipDownloaderComparable);
            }
        }
        getInstance().clear();
        getInstance().addAll(arrayList);
    }

    public void setAppBackground(boolean z) {
        this.isAppBackground = z;
    }

    public boolean isAppForeground() {
        try {
            Class<?> cls = Class.forName("com.taobao.taobaocompat.lifecycle.AppForgroundObserver");
            return cls.getField("isForeground").getBoolean(cls);
        } catch (Throwable th) {
            th.printStackTrace();
            return !this.isAppBackground;
        }
    }
}
