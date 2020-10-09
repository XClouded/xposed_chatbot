package android.taobao.windvane.packageapp.zipapp.utils;

import android.support.v4.media.session.PlaybackStateCompat;
import android.taobao.windvane.config.GlobalConfig;
import android.taobao.windvane.config.WVConfigManager;
import android.taobao.windvane.connect.ConnectManager;
import android.taobao.windvane.connect.HttpConnectListener;
import android.taobao.windvane.connect.HttpResponse;
import android.taobao.windvane.monitor.WVMonitorService;
import android.taobao.windvane.packageapp.WVPackageAppManager;
import android.taobao.windvane.packageapp.WVPackageAppRuntime;
import android.taobao.windvane.packageapp.ZipAppFileManager;
import android.taobao.windvane.packageapp.adaptive.ZCacheConfigManager;
import android.taobao.windvane.packageapp.cleanup.WVPackageAppCleanup;
import android.taobao.windvane.packageapp.zipapp.ConfigManager;
import android.taobao.windvane.packageapp.zipapp.ZipAppDownloaderQueue;
import android.taobao.windvane.packageapp.zipapp.ZipAppManager;
import android.taobao.windvane.packageapp.zipapp.ZipPrefixesManager;
import android.taobao.windvane.packageapp.zipapp.data.WMLWrapData;
import android.taobao.windvane.packageapp.zipapp.data.ZipAppInfo;
import android.taobao.windvane.packageapp.zipapp.data.ZipUpdateInfoEnum;
import android.taobao.windvane.packageapp.zipdownload.InstanceZipDownloader;
import android.taobao.windvane.service.WVEventContext;
import android.taobao.windvane.service.WVEventResult;
import android.taobao.windvane.service.WVEventService;
import android.taobao.windvane.service.WVInstantEventListener;
import android.taobao.windvane.thread.WVThreadPool;
import android.taobao.windvane.util.TaoLog;
import android.text.TextUtils;
import android.util.Log;

import com.taobao.alivfssdk.utils.AVFSCacheConstants;
import com.taobao.ju.track.constants.Constants;
import com.taobao.weex.el.parse.Operators;
import com.taobao.zcache.ZCacheManager;
import com.taobao.zcachecorewrapper.IZCacheCore;
import com.taobao.zcachecorewrapper.model.AppInfo;
import com.taobao.zcachecorewrapper.model.Error;

import org.json.JSONObject;

import java.io.File;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;

public class WMLAppManager {
    private static final String TAG = "WMLAppManager";
    private static BlockingQueue<String> miniPrefetch = new LinkedBlockingDeque();
    private static WMLAppManager sInstance;
    private Consumer consumer;

    public interface LoadAppCallback {
        void onError(String str, String str2);

        void onLoaded(WMLWrapData wMLWrapData);

        void onProgress(int i);
    }

    public static WMLAppManager getInstance() {
        if (sInstance == null) {
            synchronized (WMLAppManager.class) {
                if (sInstance == null) {
                    sInstance = new WMLAppManager();
                }
            }
        }
        return sInstance;
    }

    private WMLAppManager() {
        this.consumer = null;
        this.consumer = new Consumer(miniPrefetch);
        WVThreadPool.getInstance().execute(this.consumer);
    }

    public boolean isApp(String str) {
        return ZipPrefixesManager.getInstance().isAvailableApp(str);
    }

    class Consumer implements Runnable {
        private BlockingQueue<String> queue;

        Consumer(BlockingQueue<String> blockingQueue) {
            this.queue = blockingQueue;
        }

        /* JADX WARNING: Can't wrap try/catch for region: R(4:1|2|7|41) */
        /* JADX WARNING: Code restructure failed: missing block: B:33:0x00e3, code lost:
            r2 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:36:?, code lost:
            throw r2;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:37:0x00e6, code lost:
            r1 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:38:0x00e7, code lost:
            r1.printStackTrace();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:39:0x00ec, code lost:
            r1 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:40:0x00ed, code lost:
            r1.printStackTrace();
         */
        /* JADX WARNING: Missing exception handler attribute for start block: B:1:0x0005 */
        /* JADX WARNING: Removed duplicated region for block: B:1:0x0005 A[LOOP:0: B:1:0x0005->B:41:0x0005, LOOP_START, SYNTHETIC, Splitter:B:1:0x0005] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void run() {
            /*
                r11 = this;
                java.util.HashSet r0 = new java.util.HashSet
                r0.<init>()
            L_0x0005:
                java.util.concurrent.BlockingQueue<java.lang.String> r1 = r11.queue     // Catch:{ InterruptedException -> 0x0005, JSONException -> 0x00ec, UnsupportedEncodingException -> 0x00e6 }
                monitor-enter(r1)     // Catch:{ InterruptedException -> 0x0005, JSONException -> 0x00ec, UnsupportedEncodingException -> 0x00e6 }
                java.util.concurrent.BlockingQueue<java.lang.String> r2 = r11.queue     // Catch:{ all -> 0x00e3 }
                java.lang.Object r2 = r2.peek()     // Catch:{ all -> 0x00e3 }
                if (r2 == 0) goto L_0x00d4
                java.util.concurrent.BlockingQueue<java.lang.String> r2 = r11.queue     // Catch:{ all -> 0x00e3 }
                java.lang.Object r2 = r2.poll()     // Catch:{ all -> 0x00e3 }
                java.lang.String r2 = (java.lang.String) r2     // Catch:{ all -> 0x00e3 }
                android.taobao.windvane.packageapp.zipapp.data.ZipGlobalConfig r3 = android.taobao.windvane.packageapp.zipapp.ConfigManager.getLocGlobalConfig()     // Catch:{ all -> 0x00e3 }
                android.taobao.windvane.packageapp.zipapp.data.ZipAppInfo r3 = r3.getAppInfo(r2)     // Catch:{ all -> 0x00e3 }
                if (r3 == 0) goto L_0x0047
                java.lang.String r4 = ""
                java.lang.String r3 = android.taobao.windvane.packageapp.WVPackageAppRuntime.isAvailable(r4, r3)     // Catch:{ all -> 0x00e3 }
                if (r3 != 0) goto L_0x0047
                java.lang.String r3 = "WVZCache"
                java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x00e3 }
                r4.<init>()     // Catch:{ all -> 0x00e3 }
                java.lang.String r5 = "duplicate prefetch app: ["
                r4.append(r5)     // Catch:{ all -> 0x00e3 }
                r4.append(r2)     // Catch:{ all -> 0x00e3 }
                java.lang.String r2 = "]"
                r4.append(r2)     // Catch:{ all -> 0x00e3 }
                java.lang.String r2 = r4.toString()     // Catch:{ all -> 0x00e3 }
                android.taobao.windvane.util.TaoLog.e(r3, r2)     // Catch:{ all -> 0x00e3 }
                monitor-exit(r1)     // Catch:{ all -> 0x00e3 }
                goto L_0x0005
            L_0x0047:
                android.taobao.windvane.connect.ConnectManager r3 = android.taobao.windvane.connect.ConnectManager.getInstance()     // Catch:{ all -> 0x00e3 }
                android.taobao.windvane.packageapp.zipapp.utils.WMLAppManager r4 = android.taobao.windvane.packageapp.zipapp.utils.WMLAppManager.this     // Catch:{ all -> 0x00e3 }
                java.lang.String r4 = r4.getConfigUrlByAppName(r2)     // Catch:{ all -> 0x00e3 }
                r5 = 0
                android.taobao.windvane.connect.HttpResponse r3 = r3.connectSync(r4, r5)     // Catch:{ all -> 0x00e3 }
                boolean r4 = r3.isSuccess()     // Catch:{ all -> 0x00e3 }
                if (r4 == 0) goto L_0x00e0
                byte[] r3 = r3.getData()     // Catch:{ all -> 0x00e3 }
                if (r3 == 0) goto L_0x00e0
                java.lang.String r4 = new java.lang.String     // Catch:{ all -> 0x00e3 }
                java.lang.String r5 = "utf-8"
                r4.<init>(r3, r5)     // Catch:{ all -> 0x00e3 }
                org.json.JSONObject r3 = new org.json.JSONObject     // Catch:{ all -> 0x00e3 }
                r3.<init>(r4)     // Catch:{ all -> 0x00e3 }
                org.json.JSONObject r3 = r3.optJSONObject(r2)     // Catch:{ all -> 0x00e3 }
                if (r3 != 0) goto L_0x0076
                monitor-exit(r1)     // Catch:{ all -> 0x00e3 }
                goto L_0x0005
            L_0x0076:
                java.lang.String r4 = "v"
                java.lang.String r5 = ""
                java.lang.String r4 = r3.optString(r4, r5)     // Catch:{ all -> 0x00e3 }
                boolean r5 = android.text.TextUtils.isEmpty(r4)     // Catch:{ all -> 0x00e3 }
                if (r5 == 0) goto L_0x0087
                monitor-exit(r1)     // Catch:{ all -> 0x00e3 }
                goto L_0x0005
            L_0x0087:
                android.taobao.windvane.packageapp.zipapp.data.ZipGlobalConfig r5 = android.taobao.windvane.packageapp.zipapp.ConfigManager.getLocGlobalConfig()     // Catch:{ all -> 0x00e3 }
                android.taobao.windvane.packageapp.zipapp.data.ZipAppInfo r6 = r5.getAppInfo(r2)     // Catch:{ all -> 0x00e3 }
                if (r6 != 0) goto L_0x009c
                android.taobao.windvane.packageapp.zipapp.data.ZipAppInfo r6 = new android.taobao.windvane.packageapp.zipapp.data.ZipAppInfo     // Catch:{ all -> 0x00e3 }
                r6.<init>()     // Catch:{ all -> 0x00e3 }
                r7 = 1
                r6.isOptional = r7     // Catch:{ all -> 0x00e3 }
                r5.putAppInfo2Table(r2, r6)     // Catch:{ all -> 0x00e3 }
            L_0x009c:
                r6.v = r4     // Catch:{ all -> 0x00e3 }
                r6.name = r2     // Catch:{ all -> 0x00e3 }
                int r4 = android.taobao.windvane.packageapp.zipapp.utils.ZipAppConstants.ZIP_NEWEST     // Catch:{ all -> 0x00e3 }
                r6.status = r4     // Catch:{ all -> 0x00e3 }
                java.lang.String r4 = "s"
                r7 = 0
                long r4 = r3.optLong(r4, r7)     // Catch:{ all -> 0x00e3 }
                r6.s = r4     // Catch:{ all -> 0x00e3 }
                java.lang.String r4 = "f"
                r9 = 5
                long r4 = r3.optLong(r4, r9)     // Catch:{ all -> 0x00e3 }
                r6.f = r4     // Catch:{ all -> 0x00e3 }
                java.lang.String r4 = "t"
                long r4 = r3.optLong(r4, r7)     // Catch:{ all -> 0x00e3 }
                r6.t = r4     // Catch:{ all -> 0x00e3 }
                java.lang.String r4 = "z"
                java.lang.String r5 = ""
                java.lang.String r3 = r3.optString(r4, r5)     // Catch:{ all -> 0x00e3 }
                r6.z = r3     // Catch:{ all -> 0x00e3 }
                r6.installedSeq = r7     // Catch:{ all -> 0x00e3 }
                java.lang.String r3 = "0.0"
                r6.installedVersion = r3     // Catch:{ all -> 0x00e3 }
                r0.add(r2)     // Catch:{ all -> 0x00e3 }
                goto L_0x00e0
            L_0x00d4:
                r2 = 0
                android.taobao.windvane.packageapp.jsbridge.WVZCache.doPrefetch(r0, r2)     // Catch:{ all -> 0x00e3 }
                r0.clear()     // Catch:{ all -> 0x00e3 }
                java.util.concurrent.BlockingQueue<java.lang.String> r2 = r11.queue     // Catch:{ all -> 0x00e3 }
                r2.wait()     // Catch:{ all -> 0x00e3 }
            L_0x00e0:
                monitor-exit(r1)     // Catch:{ all -> 0x00e3 }
                goto L_0x0005
            L_0x00e3:
                r2 = move-exception
                monitor-exit(r1)     // Catch:{ all -> 0x00e3 }
                throw r2     // Catch:{ InterruptedException -> 0x0005, JSONException -> 0x00ec, UnsupportedEncodingException -> 0x00e6 }
            L_0x00e6:
                r1 = move-exception
                r1.printStackTrace()
                goto L_0x0005
            L_0x00ec:
                r1 = move-exception
                r1.printStackTrace()
                goto L_0x0005
            */
            throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.packageapp.zipapp.utils.WMLAppManager.Consumer.run():void");
        }
    }

    public void prefetchApps(Set<String> set) {
        if (!"3".equals(GlobalConfig.zType)) {
            synchronized (miniPrefetch) {
                if (miniPrefetch.size() == 0) {
                    miniPrefetch.addAll(set);
                    miniPrefetch.notify();
                } else {
                    miniPrefetch.addAll(set);
                }
            }
        }
    }

    public void loadApp(final String str, final LoadAppCallback loadAppCallback) {
        ZCacheConfigManager.getInstance().triggerZCacheConfig();
        final InstantPerformanceData instantPerformanceData = new InstantPerformanceData();
        instantPerformanceData.t_startTime = System.currentTimeMillis();
        instantPerformanceData.appName = str;
        if ("3".equals(GlobalConfig.zType)) {
            instantPerformanceData.type = "3";
            ZCacheManager.instance().getAppPath(str, new IZCacheCore.AppInfoCallback() {
                public void onReceive(AppInfo appInfo, Error error) {
                    long j;
                    AppInfo appInfo2 = appInfo;
                    Error error2 = error;
                    instantPerformanceData.t_endTime = System.currentTimeMillis();
                    if (appInfo2 != null) {
                        j = appInfo2.downloadDuration;
                        WMLWrapData wMLWrapData = new WMLWrapData();
                        if (TextUtils.isEmpty(appInfo2.rootPath)) {
                            InstantPerformanceData instantPerformanceData = instantPerformanceData;
                            instantPerformanceData.msg = InstantPerformanceData.LoadType.LOAD_FOR_FILE_NOT_FOUND.getCode() + ":" + InstantPerformanceData.LoadType.LOAD_FOR_FILE_NOT_FOUND.getMsg();
                            loadAppCallback.onError(InstantPerformanceData.LoadType.LOAD_FOR_FILE_NOT_FOUND.getCode(), InstantPerformanceData.LoadType.LOAD_FOR_FILE_NOT_FOUND.getMsg());
                            if (WVMonitorService.getPackageMonitorInterface() != null) {
                                WVMonitorService.getPackageMonitorInterface().commitZCacheDownLoadTime(instantPerformanceData.appName, instantPerformanceData.task_wait, j, instantPerformanceData.t_endTime - instantPerformanceData.t_startTime, instantPerformanceData.msg, false);
                                return;
                            }
                            return;
                        }
                        instantPerformanceData.isSuccess = true;
                        wMLWrapData.setRootDir(new File(appInfo2.rootPath));
                        if (appInfo2.isAppInstalled) {
                            InstantPerformanceData instantPerformanceData2 = instantPerformanceData;
                            instantPerformanceData2.msg = InstantPerformanceData.LoadType.LOAD_LOCAL.getCode() + ":" + InstantPerformanceData.LoadType.LOAD_LOCAL.getMsg();
                            wMLWrapData.setStorage(InstantPerformanceData.LoadType.LOAD_LOCAL.getMsg());
                        } else {
                            InstantPerformanceData instantPerformanceData3 = instantPerformanceData;
                            instantPerformanceData3.msg = InstantPerformanceData.LoadType.LOAD_NORMAL.getCode() + ":" + InstantPerformanceData.LoadType.LOAD_NORMAL.getMsg();
                            wMLWrapData.setStorage(InstantPerformanceData.LoadType.LOAD_NORMAL.getMsg());
                        }
                        loadAppCallback.onLoaded(wMLWrapData);
                    } else {
                        instantPerformanceData.isSuccess = false;
                        InstantPerformanceData instantPerformanceData4 = instantPerformanceData;
                        instantPerformanceData4.msg = InstantPerformanceData.LoadType.LOAD_OTHER_ERROR.getCode() + ":" + InstantPerformanceData.LoadType.LOAD_OTHER_ERROR.getMsg() + ":" + error2.errMsg;
                        loadAppCallback.onError(String.valueOf(error2.errCode), error2.errMsg);
                        j = 0;
                    }
                    long j2 = j;
                    if (WVMonitorService.getPackageMonitorInterface() != null) {
                        WVMonitorService.getPackageMonitorInterface().commitZCacheDownLoadTime(instantPerformanceData.appName, instantPerformanceData.task_wait, j2, instantPerformanceData.t_endTime - instantPerformanceData.t_startTime, instantPerformanceData.msg, false);
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append("miniApp use ZCache 3.0, oldConfig=[");
                    sb.append(ZCacheConfigManager.getInstance().useOldConfig());
                    sb.append("], ");
                    sb.append("name=[");
                    sb.append(str);
                    sb.append("], path=[");
                    sb.append(appInfo2 == null ? null : appInfo2.rootPath);
                    sb.append("], code=[");
                    sb.append(error2.errCode);
                    sb.append("]; msg=[");
                    sb.append(error2.errMsg);
                    sb.append(Operators.ARRAY_END_STR);
                    TaoLog.i("ZCache", sb.toString());
                }
            });
        } else if (!WVPackageAppManager.isInit) {
            TaoLog.i("ZCache", "ZCache is not init");
            loadAppCallback.onError(WMLErrorCode.ERROR_ZCACHE_NOT_INIT.code(), WMLErrorCode.ERROR_ZCACHE_NOT_INIT.message());
        } else {
            TaoLog.i("ZCache", "miniApp:[" + str + "] use zCache 2.0");
            instantPerformanceData.type = "2";
            ZipAppInfo appInfo = ConfigManager.getLocGlobalConfig().getAppInfo(str);
            WVPackageAppCleanup.getInstance().addInfoIfNeed(appInfo);
            if (appInfo != null) {
                TaoLog.d(TAG, "found ZipAppInfo: " + str);
                if (WVPackageAppRuntime.isAvailable("", appInfo) == null) {
                    TaoLog.d(TAG, "app already installed: " + str);
                    WVPackageAppCleanup.getInstance().updateAccessTimes(appInfo.name, false);
                    File file = new File(ZipAppFileManager.getInstance().getZipResAbsolutePath(appInfo, "", false));
                    if (!ZipAppManager.validInstallZipPackage(appInfo, true, false)) {
                        TaoLog.d(TAG, "app [" + str + "] error validity");
                        instantPerformanceData.msg = InstantPerformanceData.LoadType.LOAD_ERROR_VALIDITY.getCode() + ":" + InstantPerformanceData.LoadType.LOAD_ERROR_VALIDITY.getMsg();
                        downLoadConfigAndZip(str, instantPerformanceData, loadAppCallback, false);
                        file.delete();
                    } else if (file.exists()) {
                        TaoLog.d(TAG, "file loaded by zcache: " + str);
                        appInfo.isInUse = true;
                        WMLWrapData wMLWrapData = new WMLWrapData();
                        wMLWrapData.setRootDir(file);
                        wMLWrapData.setStorage(InstantPerformanceData.LoadType.LOAD_LOCAL.getMsg());
                        loadAppCallback.onLoaded(wMLWrapData);
                        instantPerformanceData.t_endTime = System.currentTimeMillis();
                        instantPerformanceData.msg = InstantPerformanceData.LoadType.LOAD_LOCAL.getCode() + ":" + InstantPerformanceData.LoadType.LOAD_LOCAL.getMsg();
                        instantPerformanceData.isSuccess = true;
                        if (WVMonitorService.getPackageMonitorInterface() != null) {
                            WVMonitorService.getPackageMonitorInterface().commitZCacheDownLoadTime(instantPerformanceData.appName, instantPerformanceData.task_wait, instantPerformanceData.d_endTime - instantPerformanceData.d_startTime, instantPerformanceData.t_endTime - instantPerformanceData.t_startTime, instantPerformanceData.msg, instantPerformanceData.isSuccess);
                        }
                    } else {
                        TaoLog.d(TAG, "file not existed: " + str);
                        forceUninstall(appInfo);
                        instantPerformanceData.msg = InstantPerformanceData.LoadType.LOAD_FOR_FILE_NOT_FOUND.getCode() + ":" + InstantPerformanceData.LoadType.LOAD_FOR_FILE_NOT_FOUND.getMsg();
                        downLoadConfigAndZip(str, instantPerformanceData, loadAppCallback, false);
                    }
                } else {
                    TaoLog.d(TAG, "bad resource [" + str + Operators.ARRAY_END_STR);
                    instantPerformanceData.msg = InstantPerformanceData.LoadType.LOAD_BAD_RESOURCE.getCode() + ":" + InstantPerformanceData.LoadType.LOAD_BAD_RESOURCE.getMsg();
                    downLoadConfigAndZip(str, instantPerformanceData, loadAppCallback, false);
                }
            } else {
                TaoLog.d(TAG, "ZipAppInfo not found: " + str);
                instantPerformanceData.msg = InstantPerformanceData.LoadType.LOAD_NORMAL.getCode() + ":" + InstantPerformanceData.LoadType.LOAD_NORMAL.getMsg();
                downLoadConfigAndZip(str, instantPerformanceData, loadAppCallback, true);
            }
        }
    }

    private void downLoadConfigAndZip(String str, InstantPerformanceData instantPerformanceData, LoadAppCallback loadAppCallback, boolean z) {
        instantPerformanceData.d_startTime = System.currentTimeMillis();
        instantPerformanceData.msg += "  remain:" + (((ThreadPoolExecutor) WVThreadPool.getInstance().getExecutor()).getCorePoolSize() - ((ThreadPoolExecutor) WVThreadPool.getInstance().getExecutor()).getActiveCount());
        final LoadAppCallback loadAppCallback2 = loadAppCallback;
        final InstantPerformanceData instantPerformanceData2 = instantPerformanceData;
        final String str2 = str;
        final boolean z2 = z;
        ConnectManager.getInstance().connect(getConfigUrlByAppName(str), new HttpConnectListener<HttpResponse>() {
            private long start = 0;

            public void onError(int i, String str) {
                loadAppCallback2.onError(WMLErrorCode.ERROR_FETCH_APP_CONFIG.code(), WMLErrorCode.ERROR_FETCH_APP_CONFIG.message());
                instantPerformanceData2.isSuccess = false;
                InstantPerformanceData instantPerformanceData = instantPerformanceData2;
                InstantPerformanceData instantPerformanceData2 = instantPerformanceData2;
                long currentTimeMillis = System.currentTimeMillis();
                instantPerformanceData2.t_endTime = currentTimeMillis;
                instantPerformanceData.d_endTime = currentTimeMillis;
                InstantPerformanceData instantPerformanceData3 = instantPerformanceData2;
                instantPerformanceData3.msg = WMLErrorCode.ERROR_FETCH_APP_CONFIG.code() + ":" + WMLErrorCode.ERROR_FETCH_APP_CONFIG.message() + " url:" + WMLAppManager.this.getConfigUrlByAppName(str2);
                if (WVMonitorService.getPackageMonitorInterface() != null) {
                    WVMonitorService.getPackageMonitorInterface().commitZCacheDownLoadTime(instantPerformanceData2.appName, instantPerformanceData2.task_wait, instantPerformanceData2.d_endTime - instantPerformanceData2.d_startTime, instantPerformanceData2.t_endTime - instantPerformanceData2.t_startTime, instantPerformanceData2.msg, instantPerformanceData2.isSuccess);
                }
            }

            public void onStart() {
                instantPerformanceData2.task_wait = System.currentTimeMillis() - instantPerformanceData2.d_startTime;
                this.start = System.currentTimeMillis();
            }

            public void onFinish(HttpResponse httpResponse, int i) {
                if (System.currentTimeMillis() - this.start > 1000) {
                    TaoLog.e(WMLAppManager.TAG, "warning for weak network");
                    StringBuilder sb = new StringBuilder();
                    InstantPerformanceData instantPerformanceData = instantPerformanceData2;
                    sb.append(instantPerformanceData.msg);
                    sb.append(" WARNING: MAYBE_WEAK_NETWORK");
                    instantPerformanceData.msg = sb.toString();
                }
                TaoLog.d(WMLAppManager.TAG, "get app config by url: " + str2);
                byte[] data = httpResponse.getData();
                if (data == null) {
                    TaoLog.d(WMLAppManager.TAG, "failed to fetch app config: " + str2);
                    loadAppCallback2.onError(WMLErrorCode.ERROR_FETCH_APP_CONFIG.code(), WMLErrorCode.ERROR_FETCH_APP_CONFIG.message());
                    instantPerformanceData2.isSuccess = false;
                    InstantPerformanceData instantPerformanceData2 = instantPerformanceData2;
                    InstantPerformanceData instantPerformanceData3 = instantPerformanceData2;
                    long currentTimeMillis = System.currentTimeMillis();
                    instantPerformanceData3.t_endTime = currentTimeMillis;
                    instantPerformanceData2.d_endTime = currentTimeMillis;
                    InstantPerformanceData instantPerformanceData4 = instantPerformanceData2;
                    instantPerformanceData4.msg = WMLErrorCode.ERROR_FETCH_APP_CONFIG.code() + ":" + WMLErrorCode.ERROR_FETCH_APP_CONFIG.message() + " no data by url:" + WMLAppManager.this.getConfigUrlByAppName(str2);
                    if (WVMonitorService.getPackageMonitorInterface() != null) {
                        WVMonitorService.getPackageMonitorInterface().commitZCacheDownLoadTime(instantPerformanceData2.appName, instantPerformanceData2.task_wait, instantPerformanceData2.d_endTime - instantPerformanceData2.d_startTime, instantPerformanceData2.t_endTime - instantPerformanceData2.t_startTime, instantPerformanceData2.msg, instantPerformanceData2.isSuccess);
                        return;
                    }
                    return;
                }
                try {
                    JSONObject optJSONObject = new JSONObject(new String(data, "utf-8")).optJSONObject(str2);
                    if (optJSONObject == null) {
                        TaoLog.d(WMLAppManager.TAG, "empty ap config: " + str2);
                        loadAppCallback2.onError(WMLErrorCode.ERROR_EMPTY_APP_CONFIG.code(), WMLErrorCode.ERROR_EMPTY_APP_CONFIG.message());
                        instantPerformanceData2.isSuccess = false;
                        InstantPerformanceData instantPerformanceData5 = instantPerformanceData2;
                        InstantPerformanceData instantPerformanceData6 = instantPerformanceData2;
                        long currentTimeMillis2 = System.currentTimeMillis();
                        instantPerformanceData6.t_endTime = currentTimeMillis2;
                        instantPerformanceData5.d_endTime = currentTimeMillis2;
                        InstantPerformanceData instantPerformanceData7 = instantPerformanceData2;
                        instantPerformanceData7.msg = WMLErrorCode.ERROR_EMPTY_APP_CONFIG.code() + ":" + WMLErrorCode.ERROR_EMPTY_APP_CONFIG.message();
                        if (WVMonitorService.getPackageMonitorInterface() != null) {
                            WVMonitorService.getPackageMonitorInterface().commitZCacheDownLoadTime(instantPerformanceData2.appName, instantPerformanceData2.task_wait, instantPerformanceData2.d_endTime - instantPerformanceData2.d_startTime, instantPerformanceData2.t_endTime - instantPerformanceData2.t_startTime, instantPerformanceData2.msg, instantPerformanceData2.isSuccess);
                            return;
                        }
                        return;
                    }
                    String optString = optJSONObject.optString("v", "");
                    if (TextUtils.isEmpty(optString)) {
                        TaoLog.d(WMLAppManager.TAG, "invalid version: " + str2);
                        loadAppCallback2.onError(WMLErrorCode.ERROR_INVALID_APP_VERSION.code(), WMLErrorCode.ERROR_INVALID_APP_VERSION.message());
                        instantPerformanceData2.isSuccess = false;
                        InstantPerformanceData instantPerformanceData8 = instantPerformanceData2;
                        InstantPerformanceData instantPerformanceData9 = instantPerformanceData2;
                        long currentTimeMillis3 = System.currentTimeMillis();
                        instantPerformanceData9.t_endTime = currentTimeMillis3;
                        instantPerformanceData8.d_endTime = currentTimeMillis3;
                        InstantPerformanceData instantPerformanceData10 = instantPerformanceData2;
                        instantPerformanceData10.msg = WMLErrorCode.ERROR_INVALID_APP_VERSION.code() + ":" + WMLErrorCode.ERROR_INVALID_APP_VERSION.message();
                        if (WVMonitorService.getPackageMonitorInterface() != null) {
                            WVMonitorService.getPackageMonitorInterface().commitZCacheDownLoadTime(instantPerformanceData2.appName, instantPerformanceData2.task_wait, instantPerformanceData2.d_endTime - instantPerformanceData2.d_startTime, instantPerformanceData2.t_endTime - instantPerformanceData2.t_startTime, instantPerformanceData2.msg, instantPerformanceData2.isSuccess);
                            return;
                        }
                        return;
                    }
                    ZipAppInfo appInfo = ConfigManager.getLocGlobalConfig().getAppInfo(str2);
                    if (appInfo == null) {
                        appInfo = new ZipAppInfo();
                        appInfo.isOptional = z2;
                    }
                    appInfo.v = optString;
                    appInfo.name = str2;
                    appInfo.status = ZipAppConstants.ZIP_NEWEST;
                    appInfo.s = optJSONObject.optLong("s", 0);
                    appInfo.f = optJSONObject.optLong("f", 5);
                    appInfo.t = optJSONObject.optLong("t", 0);
                    appInfo.z = optJSONObject.optString("z", "");
                    appInfo.installedSeq = 0;
                    appInfo.installedVersion = Constants.PARAM_OUTER_SPM_AB_OR_CD_NONE;
                    WVEventService.getInstance().setInstantEvent(new AppDownloadListener(appInfo, instantPerformanceData2, loadAppCallback2));
                    WMLAppManager.this.downloadApp(appInfo, instantPerformanceData2, loadAppCallback2);
                } catch (Throwable th) {
                    th.printStackTrace();
                    loadAppCallback2.onError(WMLErrorCode.ERROR_PARSE_APP_CONFIG.code(), WMLErrorCode.ERROR_PARSE_APP_CONFIG.message());
                    instantPerformanceData2.isSuccess = false;
                    InstantPerformanceData instantPerformanceData11 = instantPerformanceData2;
                    InstantPerformanceData instantPerformanceData12 = instantPerformanceData2;
                    long currentTimeMillis4 = System.currentTimeMillis();
                    instantPerformanceData12.t_endTime = currentTimeMillis4;
                    instantPerformanceData11.d_endTime = currentTimeMillis4;
                    InstantPerformanceData instantPerformanceData13 = instantPerformanceData2;
                    instantPerformanceData13.msg = WMLErrorCode.ERROR_PARSE_APP_CONFIG.code() + ":" + WMLErrorCode.ERROR_PARSE_APP_CONFIG.message() + ", err:" + th.getMessage();
                    if (WVMonitorService.getPackageMonitorInterface() != null) {
                        WVMonitorService.getPackageMonitorInterface().commitZCacheDownLoadTime(instantPerformanceData2.appName, instantPerformanceData2.task_wait, instantPerformanceData2.d_endTime - instantPerformanceData2.d_startTime, instantPerformanceData2.t_endTime - instantPerformanceData2.t_startTime, instantPerformanceData2.msg, instantPerformanceData2.isSuccess);
                    }
                }
            }
        }, str);
    }

    public void setDamage(String str, boolean z) {
        ZipAppInfo appInfo = ConfigManager.getLocGlobalConfig().getAppInfo(str);
        if (appInfo != null) {
            appInfo.isDamage = z;
        }
    }

    public void closeApp(String str) {
        ZipAppInfo appInfo = ConfigManager.getLocGlobalConfig().getAppInfo(str);
        if (appInfo != null) {
            appInfo.isInUse = false;
            if (appInfo.isDamage) {
                ZipAppManager.getInstance().unInstall(appInfo);
                TaoLog.e(TAG, "App has damaged, uninstall it: " + appInfo.name);
            }
        }
    }

    public void deleteApp(String str) {
        ZipAppInfo appInfo = ConfigManager.getLocGlobalConfig().getAppInfo(str);
        if (appInfo != null) {
            forceUninstall(appInfo);
            TaoLog.e(TAG, "delete app: " + appInfo.name);
        }
    }

    private void forceUninstall(ZipAppInfo zipAppInfo) {
        if (zipAppInfo != null) {
            zipAppInfo.isInUse = false;
            zipAppInfo.f |= PlaybackStateCompat.ACTION_SKIP_TO_QUEUE_ITEM;
            ZipAppManager.getInstance().unInstall(zipAppInfo);
            if (ConfigManager.getLocGlobalConfig().getAppInfo(zipAppInfo.name) == null) {
                Log.d(TAG, "uninstall success: " + zipAppInfo.name);
                return;
            }
            Log.d(TAG, "uninstall failed: " + zipAppInfo.name);
        }
    }

    public void commitVisit(String str) {
        if (ConfigManager.getLocGlobalConfig().getAppInfo(str) != null) {
            WVPackageAppCleanup.getInstance().updateAccessTimes(str, false);
        }
    }

    /* access modifiers changed from: private */
    public void downloadApp(ZipAppInfo zipAppInfo, InstantPerformanceData instantPerformanceData, LoadAppCallback loadAppCallback) {
        TaoLog.d(TAG, "start download app: " + zipAppInfo.name);
        if (zipAppInfo.getInfo() == ZipUpdateInfoEnum.ZIP_UPDATE_INFO_DELETE) {
            loadAppCallback.onError(WMLErrorCode.ERROR_APP_DELETED.code(), WMLErrorCode.ERROR_APP_DELETED.message());
            long currentTimeMillis = System.currentTimeMillis();
            instantPerformanceData.t_endTime = currentTimeMillis;
            instantPerformanceData.d_endTime = currentTimeMillis;
            instantPerformanceData.isSuccess = false;
            instantPerformanceData.msg = WMLErrorCode.ERROR_APP_DELETED.code() + ":" + WMLErrorCode.ERROR_APP_DELETED.message();
            if (WVMonitorService.getPackageMonitorInterface() != null) {
                WVMonitorService.getPackageMonitorInterface().commitZCacheDownLoadTime(instantPerformanceData.appName, instantPerformanceData.task_wait, instantPerformanceData.d_endTime - instantPerformanceData.d_startTime, instantPerformanceData.t_endTime - instantPerformanceData.t_startTime, instantPerformanceData.msg, instantPerformanceData.isSuccess);
                return;
            }
            return;
        }
        zipAppInfo.isInstantApp = true;
        WVPackageAppCleanup.getInstance().updateAccessTimes(zipAppInfo.name, false);
        ZipAppDownloaderQueue.getInstance().instantTaskName = zipAppInfo.name;
        new InstanceZipDownloader(zipAppInfo.getZipUrl(), WVPackageAppManager.getInstance(), 4, zipAppInfo).start();
        if (!WVConfigManager.getInstance().checkIfUpdate(WVConfigManager.WVConfigUpdateFromType.WVConfigUpdateFromTypeActive)) {
            ZipAppDownloaderQueue.getInstance().startUpdateAppsTask();
        }
    }

    /* access modifiers changed from: private */
    public String getConfigUrlByAppName(String str) {
        return WVConfigManager.getInstance().configDomainByEnv() + "/app/" + str + "/config/app.json";
    }

    private static class AppDownloadListener implements WVInstantEventListener {
        private LoadAppCallback callback;
        private ZipAppInfo info;
        private InstantPerformanceData loadData;

        public AppDownloadListener(ZipAppInfo zipAppInfo, InstantPerformanceData instantPerformanceData, LoadAppCallback loadAppCallback) {
            this.callback = loadAppCallback;
            this.info = zipAppInfo;
            this.loadData = instantPerformanceData;
        }

        public WVEventResult onInstantEvent(int i, WVEventContext wVEventContext, Object... objArr) {
            if (i != 6004) {
                if (i != 6010) {
                    switch (i) {
                        case 6007:
                            if (this.callback == null) {
                                return null;
                            }
                            String str = objArr[2];
                            if (!this.info.name.equals(str)) {
                                return null;
                            }
                            String str2 = objArr[1];
                            this.callback.onError(WMLErrorCode.ERROR_UNZIP_APP.code(), WMLErrorCode.ERROR_UNZIP_APP.message());
                            TaoLog.d(WMLAppManager.TAG, "zip app failed: " + str + AVFSCacheConstants.COMMA_SEP + str2);
                            this.loadData.isSuccess = false;
                            StringBuilder sb = new StringBuilder();
                            InstantPerformanceData instantPerformanceData = this.loadData;
                            sb.append(instantPerformanceData.msg);
                            sb.append(" errorMsg:");
                            sb.append(str2);
                            sb.append(",name:");
                            sb.append(str);
                            instantPerformanceData.msg = sb.toString();
                            this.loadData.t_endTime = System.currentTimeMillis();
                            if (WVMonitorService.getPackageMonitorInterface() != null) {
                                WVMonitorService.getPackageMonitorInterface().commitZCacheDownLoadTime(this.loadData.appName, this.loadData.task_wait, this.loadData.d_endTime - this.loadData.d_startTime, this.loadData.t_endTime - this.loadData.t_startTime, this.loadData.msg, this.loadData.isSuccess);
                            }
                            WVEventService.getInstance().removeInstantEvent(this);
                            return null;
                        case 6008:
                            if (this.callback == null) {
                                return null;
                            }
                            String str3 = objArr[0];
                            if (!this.info.name.equals(str3)) {
                                return null;
                            }
                            TaoLog.d(WMLAppManager.TAG, "install complete: " + str3);
                            this.info.isInUse = true;
                            File file = new File(ZipAppFileManager.getInstance().getZipResAbsolutePath(this.info, "", false));
                            if (file.exists()) {
                                TaoLog.d(WMLAppManager.TAG, "app loaded: " + str3 + " at " + file.getPath());
                                WMLWrapData wMLWrapData = new WMLWrapData();
                                wMLWrapData.setRootDir(file);
                                wMLWrapData.setStorage(InstantPerformanceData.LoadType.LOAD_NORMAL.getMsg());
                                this.callback.onLoaded(wMLWrapData);
                                this.loadData.isSuccess = true;
                            } else {
                                TaoLog.d(WMLAppManager.TAG, "failed to install app: " + str3);
                                this.callback.onError(WMLErrorCode.ERROR_INSTALL_APP.code(), WMLErrorCode.ERROR_INSTALL_APP.message());
                                this.loadData.isSuccess = false;
                                InstantPerformanceData instantPerformanceData2 = this.loadData;
                                instantPerformanceData2.msg = WMLErrorCode.ERROR_INSTALL_APP.code() + ":" + WMLErrorCode.ERROR_INSTALL_APP.message();
                            }
                            this.loadData.t_endTime = System.currentTimeMillis();
                            if (WVMonitorService.getPackageMonitorInterface() != null) {
                                WVMonitorService.getPackageMonitorInterface().commitZCacheDownLoadTime(this.loadData.appName, this.loadData.task_wait, this.loadData.d_endTime - this.loadData.d_startTime, this.loadData.t_endTime - this.loadData.t_startTime, this.loadData.msg, this.loadData.isSuccess);
                            }
                            WVEventService.getInstance().removeInstantEvent(this);
                            return null;
                        default:
                            return null;
                    }
                } else if (this.callback == null) {
                    return null;
                } else {
                    this.loadData.d_endTime = System.currentTimeMillis();
                    return null;
                }
            } else if (this.callback == null) {
                return null;
            } else {
                String str4 = objArr[1];
                if (!this.info.name.equals(str4)) {
                    return null;
                }
                TaoLog.d(WMLAppManager.TAG, "download progress: " + str4 + " -> " + objArr[0]);
                this.callback.onProgress(objArr[0].intValue());
                return null;
            }
        }
    }
}
