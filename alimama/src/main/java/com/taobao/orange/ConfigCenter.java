package com.taobao.orange;

import android.content.Context;
import android.content.IntentFilter;
import android.os.RemoteException;
import android.text.TextUtils;
import anetwork.channel.interceptor.InterceptorManager;
import com.alibaba.android.bindingx.core.internal.BindingXConstants;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.ta.utdid2.device.UTDevice;
import com.taobao.orange.OConstant;
import com.taobao.orange.accssupport.OrangeAccsService;
import com.taobao.orange.aidl.OrangeConfigListenerStub;
import com.taobao.orange.aidl.ParcelableConfigListener;
import com.taobao.orange.cache.ConfigCache;
import com.taobao.orange.cache.IndexCache;
import com.taobao.orange.candidate.MultiAnalyze;
import com.taobao.orange.inner.OInitListener;
import com.taobao.orange.model.ConfigDO;
import com.taobao.orange.model.IndexDO;
import com.taobao.orange.model.NameSpaceDO;
import com.taobao.orange.receiver.OrangeReceiver;
import com.taobao.orange.sync.IndexUpdateHandler;
import com.taobao.orange.sync.NetworkInterceptor;
import com.taobao.orange.util.FileUtil;
import com.taobao.orange.util.OLog;
import com.taobao.orange.util.OrangeMonitor;
import com.taobao.orange.util.OrangeMonitorData;
import com.taobao.orange.util.OrangeUtils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.json.JSONObject;

public class ConfigCenter {
    static final int BASE_ACKDELAY_INTERVAL = 10;
    private static final long FAIL_LOAD_INDEX_UPD_INTERVAL = 180000;
    private static final long FAIL_LOAD_INDEX_UPD_NUM = 10;
    private static final long MAX_LISTENER_NUM = 10;
    static final String TAG = "ConfigCenter";
    private static volatile long failLastIndexUpdTime;
    static ConfigCenter mInstance = new ConfigCenter();
    Set<String> failCandidateSet = new HashSet();
    public AtomicBoolean isAfterIdle = new AtomicBoolean(false);
    ConfigCache mConfigCache = new ConfigCache();
    final ConcurrentLinkedQueue<NameSpaceDO> mConfigWaitingNetworkQueue = new ConcurrentLinkedQueue<>();
    private Map<String, Long> mDowngradeConfigMap = new ConcurrentHashMap();
    final Set<String> mFailRequestsSet = new HashSet();
    final Set<ParcelableConfigListener> mGlobalListeners = Collections.synchronizedSet(new HashSet());
    IndexCache mIndexCache = new IndexCache();
    volatile OInitListener mInitListener = null;
    volatile boolean mIsFirstInstall = false;
    public AtomicBoolean mIsOrangeInit = new AtomicBoolean(false);
    final Map<String, Set<ParcelableConfigListener>> mListeners = new HashMap();
    final Map<String, Long> mLoadingConfigMap = new ConcurrentHashMap();
    /* access modifiers changed from: private */
    public AtomicInteger mRequestCount = new AtomicInteger(0);

    public static ConfigCenter getInstance() {
        return mInstance;
    }

    private ConfigCenter() {
    }

    public ConcurrentLinkedQueue<NameSpaceDO> getConfigWaitingNetworkQueue() {
        return this.mConfigWaitingNetworkQueue;
    }

    public ConfigCache getConfigCache() {
        return this.mConfigCache;
    }

    public void setInitListener(OInitListener oInitListener) {
        this.mInitListener = oInitListener;
    }

    public boolean addGlobalListener(OConfigListener oConfigListener) {
        if (oConfigListener != null) {
            return this.mGlobalListeners.add(new OrangeConfigListenerStub(oConfigListener));
        }
        return false;
    }

    public boolean removeGlobalListener(OConfigListener oConfigListener) {
        if (oConfigListener != null) {
            return this.mGlobalListeners.remove(new OrangeConfigListenerStub(oConfigListener));
        }
        return false;
    }

    @Deprecated
    public void setGlobalListener(OConfigListener oConfigListener) {
        this.mGlobalListeners.add(new OrangeConfigListenerStub(oConfigListener));
    }

    public void init(final Context context, final OConfig oConfig) {
        if (context == null || TextUtils.isEmpty(oConfig.appKey) || TextUtils.isEmpty(oConfig.appVersion)) {
            OLog.e(TAG, "init start", "input param error");
            return;
        }
        OThreadFactory.execute(new Runnable() {
            public void run() {
                synchronized (ConfigCenter.this) {
                    if (!ConfigCenter.this.mIsOrangeInit.get()) {
                        GlobalOrange.deviceId = UTDevice.getUtdid(context);
                        if (OLog.isPrintLog(2)) {
                            SimplePropertyPreFilter simplePropertyPreFilter = new SimplePropertyPreFilter(OConfig.class, new String[0]);
                            simplePropertyPreFilter.getExcludes().add("appSecret");
                            OLog.i(ConfigCenter.TAG, "init start", "sdkVersion", "1.5.4.40", "utdid", GlobalOrange.deviceId, BindingXConstants.KEY_CONFIG, JSON.toJSONString((Object) oConfig, (SerializeFilter) simplePropertyPreFilter, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue, SerializerFeature.SortField));
                        }
                        GlobalOrange.context = context.getApplicationContext();
                        GlobalOrange.appKey = oConfig.appKey;
                        GlobalOrange.appVersion = oConfig.appVersion;
                        GlobalOrange.userId = oConfig.userId;
                        GlobalOrange.appSecret = oConfig.appSecret;
                        GlobalOrange.authCode = oConfig.authCode;
                        GlobalOrange.reportUpdateAck = oConfig.reportAck;
                        GlobalOrange.statUsedConfig = oConfig.statUsedConfig;
                        GlobalOrange.indexUpdMode = OConstant.UPDMODE.valueOf(oConfig.indexUpdateMode);
                        GlobalOrange.env = OConstant.ENV.valueOf(oConfig.env);
                        GlobalOrange.randomDelayAckInterval = ConfigCenter.this.updateRandomDelayAckInterval(10);
                        GlobalOrange.probeHosts.addAll(Arrays.asList(oConfig.probeHosts));
                        GlobalOrange.dcHost = oConfig.dcHost;
                        if (oConfig.dcVips != null) {
                            GlobalOrange.dcVips.addAll(Arrays.asList(oConfig.dcVips));
                        }
                        GlobalOrange.ackHost = oConfig.ackHost;
                        if (oConfig.ackVips != null) {
                            GlobalOrange.ackVips.addAll(Arrays.asList(oConfig.ackVips));
                        }
                        ConfigCenter.this.mListeners.put("orange", new HashSet<ParcelableConfigListener>() {
                            {
                                add(new ParcelableConfigListener.Stub() {
                                    public void onConfigUpdate(String str, Map map) throws RemoteException {
                                        ConfigCenter.this.updateSystemConfig(map);
                                    }
                                });
                            }
                        });
                        MultiAnalyze.initBuildInCandidates();
                        ConfigCenter.this.loadCaches();
                        File file = new File(FileUtil.getOrangeConfigDir(), IndexCache.INDEX_STORE_NAME);
                        ConfigCenter.this.mIsFirstInstall = !file.exists();
                        OrangeMonitor.init();
                        try {
                            Class.forName(OConstant.REFLECT_NETWORK_INTERCEPTOR);
                            Class.forName(OConstant.REFLECT_NETWORK_INTERCEPTORMANAGER);
                            InterceptorManager.addInterceptor(new NetworkInterceptor());
                            OLog.i(ConfigCenter.TAG, "init", "add orange interceptor success to networksdk");
                        } catch (ClassNotFoundException e) {
                            OLog.w(ConfigCenter.TAG, "init", e, "add orange interceptor fail as not found networksdk");
                        }
                        ConfigCenter.this.mIsOrangeInit.set(true);
                        ConfigCenter.this.forceCheckUpdate();
                        OrangeAccsService.complete();
                        if (ConfigCenter.this.mInitListener != null) {
                            ConfigCenter.this.mInitListener.complete();
                        }
                        if (oConfig.time >= 0) {
                            OThreadFactory.execute(new Runnable() {
                                public void run() {
                                    ConfigCenter.this.delayLoadConfig();
                                }
                            }, oConfig.time);
                        }
                        OThreadFactory.execute(new Runnable() {
                            public void run() {
                                OrangeMonitorData orangeMonitorData = new OrangeMonitorData();
                                orangeMonitorData.performance.bootType = ConfigCenter.this.mIsFirstInstall;
                                orangeMonitorData.performance.downgradeType = GlobalOrange.downgrade;
                                orangeMonitorData.performance.monitorType = 2;
                                orangeMonitorData.performance.requestCount = ConfigCenter.this.mRequestCount.get();
                                orangeMonitorData.performance.persistCount = FileUtil.persistCount.get();
                                orangeMonitorData.performance.restoreCount = FileUtil.restoreCount.get();
                                orangeMonitorData.performance.persistTime = FileUtil.persistTime.get();
                                orangeMonitorData.performance.restoreTime = FileUtil.restoreTime.get();
                                orangeMonitorData.performance.ioTime = FileUtil.ioTime.get();
                                OrangeMonitor.commitBootPerformanceInfo(orangeMonitorData);
                                OrangeMonitor.mPerformanceInfoRecordDone = true;
                            }
                        }, 90000);
                        OLog.i(ConfigCenter.TAG, "init completed", new Object[0]);
                    } else {
                        OLog.w(ConfigCenter.TAG, "already init", new Object[0]);
                    }
                }
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void loadCaches() {
        try {
            long currentTimeMillis = System.currentTimeMillis();
            OLog.i(TAG, "loadCaches", "start index");
            this.mIndexCache.load();
            Set<NameSpaceDO> allNameSpaces = this.mIndexCache.getAllNameSpaces();
            OLog.i(TAG, "loadCaches", "start restore configs", Integer.valueOf(allNameSpaces.size()));
            Set<NameSpaceDO> load = this.mConfigCache.load(allNameSpaces);
            OLog.i(TAG, "loadCaches", "finish restore configs", Integer.valueOf(allNameSpaces.size()), "cost(ms)", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
            if (load != null && !load.isEmpty()) {
                OLog.i(TAG, "loadCaches", "start load notMatchNamespaces", Integer.valueOf(load.size()));
                long currentTimeMillis2 = System.currentTimeMillis();
                for (NameSpaceDO next : load) {
                    OrangeMonitor.commitCount(OConstant.MONITOR_PRIVATE_MODULE, OConstant.POINT_CONFIG_NOTMATCH_COUNTS, next.name, 1.0d);
                    loadConfig(next);
                }
                OLog.i(TAG, "loadCaches", "finish load notMatchNamespaces", Integer.valueOf(load.size()), "cost(ms)", Long.valueOf(System.currentTimeMillis() - currentTimeMillis2));
            }
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            GlobalOrange.context.registerReceiver(new OrangeReceiver(), intentFilter);
        } catch (Throwable th) {
            OLog.e(TAG, "loadCaches", th, new Object[0]);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            th.printStackTrace(new PrintStream(byteArrayOutputStream));
            OrangeMonitor.commitFail(OConstant.MONITOR_MODULE, OConstant.POINT_EXCEPTION, "0", OConstant.CODE_POINT_EXP_LOAD_CACHE, byteArrayOutputStream.toString());
        }
    }

    public void forceCheckUpdate() {
        if (!this.mIsOrangeInit.get()) {
            OLog.w(TAG, "forceCheckUpdate fail as not finish orange init", new Object[0]);
        } else if (GlobalOrange.indexUpdMode != OConstant.UPDMODE.O_XMD) {
            OLog.i(TAG, "forceCheckUpdate start", new Object[0]);
            IndexUpdateHandler.checkIndexUpdate(this.mIndexCache.getAppIndexVersion(), this.mIndexCache.getVersionIndexVersion());
        } else {
            OLog.w(TAG, "forceCheckUpdate fail as not allow in O_XMD mode", new Object[0]);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0006, code lost:
        r1 = r1.get(r2);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String getConfig(java.lang.String r1, java.lang.String r2, java.lang.String r3) {
        /*
            r0 = this;
            java.util.Map r1 = r0.getConfigs(r1)
            if (r1 == 0) goto L_0x000f
            java.lang.Object r1 = r1.get(r2)
            java.lang.String r1 = (java.lang.String) r1
            if (r1 == 0) goto L_0x000f
            return r1
        L_0x000f:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.orange.ConfigCenter.getConfig(java.lang.String, java.lang.String, java.lang.String):java.lang.String");
    }

    public Map<String, String> getConfigs(String str) {
        try {
            return (Map) getConfigObj(str);
        } catch (Throwable th) {
            OLog.w(TAG, "getConfigs error", th, "namespace", str);
            return null;
        }
    }

    public String getCustomConfig(String str, String str2) {
        try {
            String str3 = (String) getConfigObj(str);
            return str3 == null ? str2 : str3;
        } catch (Throwable th) {
            OLog.w(TAG, "getCustomConfig error", th, "namespace", str);
            return str2;
        }
    }

    private <T> T getConfigObj(String str) {
        if (TextUtils.isEmpty(str)) {
            OLog.e(TAG, "getConfigObj error, namespace is empty", new Object[0]);
            return null;
        } else if ("orange".equals(str) || IndexCache.INDEX_STORE_NAME.equals(str)) {
            OLog.e(TAG, "getConfigObj error, namespace is occupied by sdk", new Object[0]);
            return null;
        } else {
            T configObj = this.mConfigCache.getConfigObj(str);
            if (configObj == null) {
                if (OLog.isPrintLog(0)) {
                    OLog.v(TAG, "getConfigObj", "namespace", str, "...null");
                }
                final NameSpaceDO nameSpace = this.mIndexCache.getNameSpace(str);
                if (nameSpace == null || !this.mIsOrangeInit.get()) {
                    addFail(str);
                } else if (!checkLoading(str, false)) {
                    OThreadFactory.execute(new Runnable() {
                        public void run() {
                            if (OLog.isPrintLog(0)) {
                                OLog.d(ConfigCenter.TAG, "getConfigObj force to load", "namespace", nameSpace.name);
                            }
                            ConfigCenter.this.loadConfigLazy(nameSpace);
                        }
                    });
                }
            }
            return configObj;
        }
    }

    public void loadConfigLazy(NameSpaceDO nameSpaceDO) {
        if (nameSpaceDO == null) {
            OLog.e(TAG, "loadConfigLazy fail", "nameSpaceDO is null");
        } else if ("orange".equals(nameSpaceDO.name)) {
            loadConfig(nameSpaceDO);
        } else if (GlobalOrange.downgrade > 0) {
            OLog.w(TAG, "loadConfigLazy downgrade, back to old strategy", nameSpaceDO.name, nameSpaceDO.loadLevel, Integer.valueOf(GlobalOrange.downgrade));
            loadConfig(nameSpaceDO);
            if (OrangeMonitor.mAppMonitorValid && this.mDowngradeConfigMap.get(nameSpaceDO.name) == null) {
                this.mDowngradeConfigMap.put(nameSpaceDO.name, Long.valueOf(System.currentTimeMillis()));
                OrangeMonitor.commitCount(OConstant.MONITOR_MODULE, OConstant.POINT_DOWNGRADE, nameSpaceDO.name, 1.0d);
            }
        } else {
            OLog.d(TAG, "loadConfigLazy", nameSpaceDO.name, nameSpaceDO.loadLevel, nameSpaceDO.highLazy);
            if (nameSpaceDO.highLazy.intValue() == 0 || !this.mIsFirstInstall) {
                loadConfig(nameSpaceDO);
            } else if (this.isAfterIdle.get()) {
                loadConfig(nameSpaceDO);
            } else {
                getConfigWaitingNetworkQueue().offer(nameSpaceDO);
                OLog.d(TAG, "offer a namespace", nameSpaceDO.name, "to network queue");
            }
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v16, resolved type: com.taobao.orange.ConfigCenter$3} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v17, resolved type: com.taobao.orange.ConfigCenter$3} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v28, resolved type: com.taobao.orange.ConfigCenter$4} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v31, resolved type: com.taobao.orange.ConfigCenter$3} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void loadConfig(com.taobao.orange.model.NameSpaceDO r18) {
        /*
            r17 = this;
            r8 = r17
            r9 = r18
            r10 = 0
            r11 = 1
            if (r9 != 0) goto L_0x0016
            java.lang.String r0 = "ConfigCenter"
            java.lang.String r1 = "loadConfig fail"
            java.lang.Object[] r2 = new java.lang.Object[r11]
            java.lang.String r3 = "nameSpaceDO is null"
            r2[r10] = r3
            com.taobao.orange.util.OLog.e(r0, r1, r2)
            return
        L_0x0016:
            java.lang.String r0 = "STANDARD"
            java.lang.String r1 = r9.type
            boolean r0 = r0.equals(r1)
            r12 = 4
            r13 = 3
            r14 = 2
            if (r0 == 0) goto L_0x0027
            java.lang.Class<com.taobao.orange.model.ConfigDO> r0 = com.taobao.orange.model.ConfigDO.class
        L_0x0025:
            r7 = r0
            goto L_0x0034
        L_0x0027:
            java.lang.String r0 = "CUSTOM"
            java.lang.String r1 = r9.type
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x024a
            java.lang.Class<com.taobao.orange.model.CustomConfigDO> r0 = com.taobao.orange.model.CustomConfigDO.class
            goto L_0x0025
        L_0x0034:
            java.lang.String r0 = r9.name
            boolean r0 = r8.checkLoading(r0, r11)
            if (r0 == 0) goto L_0x0054
            boolean r0 = com.taobao.orange.util.OLog.isPrintLog(r13)
            if (r0 == 0) goto L_0x0053
            java.lang.String r0 = "ConfigCenter"
            java.lang.String r1 = "loadConfig break as is loading"
            java.lang.Object[] r2 = new java.lang.Object[r14]
            java.lang.String r3 = "namespace"
            r2[r10] = r3
            java.lang.String r3 = r9.name
            r2[r11] = r3
            com.taobao.orange.util.OLog.w(r0, r1, r2)
        L_0x0053:
            return
        L_0x0054:
            com.taobao.orange.cache.IndexCache r0 = r8.mIndexCache     // Catch:{ Throwable -> 0x021e }
            java.lang.String r0 = r0.getCdnUrl()     // Catch:{ Throwable -> 0x021e }
            boolean r1 = android.text.TextUtils.isEmpty(r0)     // Catch:{ Throwable -> 0x021e }
            if (r1 == 0) goto L_0x0078
            java.lang.String r0 = "ConfigCenter"
            java.lang.String r1 = "loadConfig fail"
            java.lang.Object[] r2 = new java.lang.Object[r11]     // Catch:{ Throwable -> 0x021e }
            java.lang.String r3 = "cdnUrl is null"
            r2[r10] = r3     // Catch:{ Throwable -> 0x021e }
            com.taobao.orange.util.OLog.e(r0, r1, r2)     // Catch:{ Throwable -> 0x021e }
            java.lang.String r0 = r9.name     // Catch:{ Throwable -> 0x021e }
            r8.addFail(r0)     // Catch:{ Throwable -> 0x021e }
            java.lang.String r0 = r9.name     // Catch:{ Throwable -> 0x021e }
            r8.removeLoading(r0)     // Catch:{ Throwable -> 0x021e }
            return
        L_0x0078:
            boolean r1 = com.taobao.orange.util.OLog.isPrintLog(r11)     // Catch:{ Throwable -> 0x021e }
            if (r1 == 0) goto L_0x0089
            java.lang.String r1 = "ConfigCenter"
            java.lang.String r2 = "loadConfig start"
            java.lang.Object[] r3 = new java.lang.Object[r11]     // Catch:{ Throwable -> 0x021e }
            r3[r10] = r9     // Catch:{ Throwable -> 0x021e }
            com.taobao.orange.util.OLog.d(r1, r2, r3)     // Catch:{ Throwable -> 0x021e }
        L_0x0089:
            com.taobao.orange.cache.ConfigCache r1 = r8.mConfigCache     // Catch:{ Throwable -> 0x021e }
            java.util.Map r1 = r1.getConfigMap()     // Catch:{ Throwable -> 0x021e }
            java.lang.String r2 = r9.name     // Catch:{ Throwable -> 0x021e }
            java.lang.Object r1 = r1.get(r2)     // Catch:{ Throwable -> 0x021e }
            com.taobao.orange.model.ConfigDO r1 = (com.taobao.orange.model.ConfigDO) r1     // Catch:{ Throwable -> 0x021e }
            boolean r1 = r9.checkValid(r1)     // Catch:{ Throwable -> 0x021e }
            if (r1 != 0) goto L_0x00a8
            java.lang.String r0 = r9.name     // Catch:{ Throwable -> 0x021e }
            r8.removeFail(r0)     // Catch:{ Throwable -> 0x021e }
            java.lang.String r0 = r9.name     // Catch:{ Throwable -> 0x021e }
            r8.removeLoading(r0)     // Catch:{ Throwable -> 0x021e }
            return
        L_0x00a8:
            com.taobao.orange.model.CandidateDO r1 = r9.curCandidateDO     // Catch:{ Throwable -> 0x021e }
            if (r1 == 0) goto L_0x00b9
            com.taobao.orange.model.CandidateDO r1 = r9.curCandidateDO     // Catch:{ Throwable -> 0x021e }
            java.lang.String r1 = r1.resourceId     // Catch:{ Throwable -> 0x021e }
            com.taobao.orange.model.CandidateDO r2 = r9.curCandidateDO     // Catch:{ Throwable -> 0x021e }
            java.lang.String r2 = r2.md5     // Catch:{ Throwable -> 0x021e }
            com.taobao.orange.model.CandidateDO r3 = r9.curCandidateDO     // Catch:{ Throwable -> 0x021e }
            java.lang.String r3 = r3.version     // Catch:{ Throwable -> 0x021e }
            goto L_0x00bf
        L_0x00b9:
            java.lang.String r1 = r9.resourceId     // Catch:{ Throwable -> 0x021e }
            java.lang.String r2 = r9.md5     // Catch:{ Throwable -> 0x021e }
            java.lang.String r3 = r9.version     // Catch:{ Throwable -> 0x021e }
        L_0x00bf:
            r15 = r3
            boolean r3 = com.taobao.orange.util.OLog.isPrintLog(r10)     // Catch:{ Throwable -> 0x021e }
            if (r3 == 0) goto L_0x00dd
            java.lang.String r3 = "ConfigCenter"
            java.lang.String r4 = "loadConfig check"
            java.lang.Object[] r5 = new java.lang.Object[r12]     // Catch:{ Throwable -> 0x021e }
            java.lang.String r6 = "config"
            r5[r10] = r6     // Catch:{ Throwable -> 0x021e }
            java.lang.String r6 = r9.name     // Catch:{ Throwable -> 0x021e }
            r5[r11] = r6     // Catch:{ Throwable -> 0x021e }
            java.lang.String r6 = "version"
            r5[r14] = r6     // Catch:{ Throwable -> 0x021e }
            r5[r13] = r15     // Catch:{ Throwable -> 0x021e }
            com.taobao.orange.util.OLog.v(r3, r4, r5)     // Catch:{ Throwable -> 0x021e }
        L_0x00dd:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x021e }
            r3.<init>()     // Catch:{ Throwable -> 0x021e }
            r3.append(r0)     // Catch:{ Throwable -> 0x021e }
            java.lang.String r0 = java.io.File.separator     // Catch:{ Throwable -> 0x021e }
            r3.append(r0)     // Catch:{ Throwable -> 0x021e }
            r3.append(r1)     // Catch:{ Throwable -> 0x021e }
            java.lang.String r0 = r3.toString()     // Catch:{ Throwable -> 0x021e }
            com.taobao.orange.ConfigCenter$3 r1 = new com.taobao.orange.ConfigCenter$3     // Catch:{ Throwable -> 0x021e }
            r1.<init>(r0, r2, r7)     // Catch:{ Throwable -> 0x021e }
            java.lang.Object r0 = r1.syncRequest()     // Catch:{ Throwable -> 0x021e }
            com.taobao.orange.model.ConfigDO r0 = (com.taobao.orange.model.ConfigDO) r0     // Catch:{ Throwable -> 0x021e }
            boolean r2 = com.taobao.orange.util.OrangeMonitor.mPerformanceInfoRecordDone     // Catch:{ Throwable -> 0x021e }
            if (r2 != 0) goto L_0x0105
            java.util.concurrent.atomic.AtomicInteger r2 = r8.mRequestCount     // Catch:{ Throwable -> 0x021e }
            r2.incrementAndGet()     // Catch:{ Throwable -> 0x021e }
        L_0x0105:
            boolean r2 = com.taobao.orange.GlobalOrange.fallbackAvoid     // Catch:{ Throwable -> 0x021e }
            if (r2 != 0) goto L_0x014f
            if (r0 == 0) goto L_0x0111
            boolean r2 = r0.checkValid()     // Catch:{ Throwable -> 0x021e }
            if (r2 != 0) goto L_0x015a
        L_0x0111:
            boolean r0 = com.taobao.orange.util.OLog.isPrintLog(r10)     // Catch:{ Throwable -> 0x021e }
            if (r0 == 0) goto L_0x0134
            java.lang.String r0 = "ConfigCenter"
            java.lang.String r2 = "loadConfig cdnReq fail downgrade to authReq"
            java.lang.Object[] r3 = new java.lang.Object[r12]     // Catch:{ Throwable -> 0x021e }
            java.lang.String r4 = "code"
            r3[r10] = r4     // Catch:{ Throwable -> 0x021e }
            java.lang.String r4 = r1.getCode()     // Catch:{ Throwable -> 0x021e }
            r3[r11] = r4     // Catch:{ Throwable -> 0x021e }
            java.lang.String r4 = "msg"
            r3[r14] = r4     // Catch:{ Throwable -> 0x021e }
            java.lang.String r1 = r1.getMessage()     // Catch:{ Throwable -> 0x021e }
            r3[r13] = r1     // Catch:{ Throwable -> 0x021e }
            com.taobao.orange.util.OLog.v(r0, r2, r3)     // Catch:{ Throwable -> 0x021e }
        L_0x0134:
            com.taobao.orange.ConfigCenter$4 r0 = new com.taobao.orange.ConfigCenter$4     // Catch:{ Throwable -> 0x021e }
            java.lang.String r3 = r9.md5     // Catch:{ Throwable -> 0x021e }
            r4 = 0
            java.lang.String r5 = "/downloadResource"
            r1 = r0
            r2 = r17
            r6 = r18
            r1.<init>(r3, r4, r5, r6, r7)     // Catch:{ Throwable -> 0x021e }
            java.lang.Object r1 = r0.syncRequest()     // Catch:{ Throwable -> 0x021e }
            com.taobao.orange.model.ConfigDO r1 = (com.taobao.orange.model.ConfigDO) r1     // Catch:{ Throwable -> 0x021e }
            r16 = r1
            r1 = r0
            r0 = r16
            goto L_0x015a
        L_0x014f:
            java.lang.String r2 = "private_orange"
            java.lang.String r3 = "fallback_avoid"
            java.lang.String r4 = r9.name     // Catch:{ Throwable -> 0x021e }
            r5 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            com.taobao.orange.util.OrangeMonitor.commitCount(r2, r3, r4, r5)     // Catch:{ Throwable -> 0x021e }
        L_0x015a:
            if (r0 == 0) goto L_0x01be
            boolean r2 = r0.checkValid()     // Catch:{ Throwable -> 0x021e }
            if (r2 == 0) goto L_0x01be
            java.lang.String r2 = r0.version     // Catch:{ Throwable -> 0x021e }
            boolean r2 = r2.equals(r15)     // Catch:{ Throwable -> 0x021e }
            if (r2 == 0) goto L_0x01be
            java.lang.String r2 = r0.name     // Catch:{ Throwable -> 0x021e }
            java.lang.String r3 = r9.name     // Catch:{ Throwable -> 0x021e }
            boolean r2 = r2.equals(r3)     // Catch:{ Throwable -> 0x021e }
            if (r2 == 0) goto L_0x01be
            java.lang.String r1 = r9.name     // Catch:{ Throwable -> 0x021e }
            r8.removeFail(r1)     // Catch:{ Throwable -> 0x021e }
            java.lang.String r1 = r9.name     // Catch:{ Throwable -> 0x021e }
            r8.removeLoading(r1)     // Catch:{ Throwable -> 0x021e }
            java.lang.String r1 = "OrangeConfig"
            java.lang.String r2 = "config_rate"
            java.lang.String r3 = r9.name     // Catch:{ Throwable -> 0x021e }
            com.taobao.orange.util.OrangeMonitor.commitSuccess(r1, r2, r3)     // Catch:{ Throwable -> 0x021e }
            java.lang.String r1 = "config_update"
            java.lang.String r2 = r9.name     // Catch:{ Throwable -> 0x021e }
            java.lang.String r3 = r9.version     // Catch:{ Throwable -> 0x021e }
            com.taobao.orange.util.OrangeMonitor.commitConfigMonitor(r1, r2, r3)     // Catch:{ Throwable -> 0x021e }
            com.taobao.orange.model.CandidateDO r1 = r9.curCandidateDO     // Catch:{ Throwable -> 0x021e }
            r0.candidate = r1     // Catch:{ Throwable -> 0x021e }
            com.taobao.orange.cache.ConfigCache r1 = r8.mConfigCache     // Catch:{ Throwable -> 0x021e }
            r1.cache(r0)     // Catch:{ Throwable -> 0x021e }
            boolean r1 = com.taobao.orange.util.OLog.isPrintLog(r14)     // Catch:{ Throwable -> 0x021e }
            if (r1 == 0) goto L_0x01aa
            java.lang.String r1 = "ConfigCenter"
            java.lang.String r2 = "loadConfig success"
            java.lang.Object[] r3 = new java.lang.Object[r11]     // Catch:{ Throwable -> 0x021e }
            r3[r10] = r0     // Catch:{ Throwable -> 0x021e }
            com.taobao.orange.util.OLog.i(r1, r2, r3)     // Catch:{ Throwable -> 0x021e }
        L_0x01aa:
            com.taobao.orange.model.ConfigAckDO r1 = new com.taobao.orange.model.ConfigAckDO     // Catch:{ Exception -> 0x0249 }
            java.lang.String r2 = r0.name     // Catch:{ Exception -> 0x0249 }
            java.lang.String r3 = r0.id     // Catch:{ Exception -> 0x0249 }
            java.lang.String r4 = com.taobao.orange.util.OrangeUtils.getCurFormatTime()     // Catch:{ Exception -> 0x0249 }
            java.lang.String r0 = r0.version     // Catch:{ Exception -> 0x0249 }
            r1.<init>(r2, r3, r4, r0)     // Catch:{ Exception -> 0x0249 }
            com.taobao.orange.util.ReportAckUtils.reportConfigAck(r1)     // Catch:{ Exception -> 0x0249 }
            goto L_0x0249
        L_0x01be:
            java.lang.String r2 = r9.name     // Catch:{ Throwable -> 0x021e }
            r8.addFail(r2)     // Catch:{ Throwable -> 0x021e }
            java.lang.String r2 = r9.name     // Catch:{ Throwable -> 0x021e }
            r8.removeLoading(r2)     // Catch:{ Throwable -> 0x021e }
            java.lang.String r2 = "-200"
            java.lang.String r3 = r1.getCode()     // Catch:{ Throwable -> 0x021e }
            boolean r2 = r2.equals(r3)     // Catch:{ Throwable -> 0x021e }
            if (r2 != 0) goto L_0x01f6
            if (r0 == 0) goto L_0x01e5
            boolean r0 = r0.checkValid()     // Catch:{ Throwable -> 0x021e }
            if (r0 != 0) goto L_0x01e5
            r0 = -5
            r1.setCode(r0)     // Catch:{ Throwable -> 0x021e }
            java.lang.String r0 = "config is invaild"
            r1.setMessage(r0)     // Catch:{ Throwable -> 0x021e }
        L_0x01e5:
            java.lang.String r0 = "OrangeConfig"
            java.lang.String r2 = "config_rate"
            java.lang.String r3 = r9.name     // Catch:{ Throwable -> 0x021e }
            java.lang.String r4 = r1.getCode()     // Catch:{ Throwable -> 0x021e }
            java.lang.String r5 = r1.getMessage()     // Catch:{ Throwable -> 0x021e }
            com.taobao.orange.util.OrangeMonitor.commitFail(r0, r2, r3, r4, r5)     // Catch:{ Throwable -> 0x021e }
        L_0x01f6:
            java.lang.String r0 = "ConfigCenter"
            java.lang.String r2 = "loadConfig fail"
            r3 = 6
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch:{ Throwable -> 0x021e }
            java.lang.String r4 = "namespace"
            r3[r10] = r4     // Catch:{ Throwable -> 0x021e }
            java.lang.String r4 = r9.name     // Catch:{ Throwable -> 0x021e }
            r3[r11] = r4     // Catch:{ Throwable -> 0x021e }
            java.lang.String r4 = "code"
            r3[r14] = r4     // Catch:{ Throwable -> 0x021e }
            java.lang.String r4 = r1.getCode()     // Catch:{ Throwable -> 0x021e }
            r3[r13] = r4     // Catch:{ Throwable -> 0x021e }
            java.lang.String r4 = "msg"
            r3[r12] = r4     // Catch:{ Throwable -> 0x021e }
            r4 = 5
            java.lang.String r1 = r1.getMessage()     // Catch:{ Throwable -> 0x021e }
            r3[r4] = r1     // Catch:{ Throwable -> 0x021e }
            com.taobao.orange.util.OLog.e(r0, r2, r3)     // Catch:{ Throwable -> 0x021e }
            goto L_0x0249
        L_0x021e:
            r0 = move-exception
            java.lang.String r1 = r9.name
            r8.addFail(r1)
            java.lang.String r1 = r9.name
            r8.removeLoading(r1)
            java.lang.String r1 = "OrangeConfig"
            java.lang.String r2 = "config_rate"
            java.lang.String r3 = r9.name
            java.lang.String r4 = "0"
            java.lang.String r5 = r0.getMessage()
            com.taobao.orange.util.OrangeMonitor.commitFail(r1, r2, r3, r4, r5)
            java.lang.String r1 = "ConfigCenter"
            java.lang.String r2 = "loadConfig fail"
            java.lang.Object[] r3 = new java.lang.Object[r14]
            java.lang.String r4 = "namespace"
            r3[r10] = r4
            java.lang.String r4 = r9.name
            r3[r11] = r4
            com.taobao.orange.util.OLog.e(r1, r2, r0, r3)
        L_0x0249:
            return
        L_0x024a:
            java.lang.String r0 = "ConfigCenter"
            java.lang.String r1 = "loadConfig fail not support type"
            java.lang.Object[] r2 = new java.lang.Object[r12]
            java.lang.String r3 = "namespace"
            r2[r10] = r3
            java.lang.String r3 = r9.name
            r2[r11] = r3
            java.lang.String r3 = "type"
            r2[r14] = r3
            java.lang.String r3 = r9.type
            r2[r13] = r3
            com.taobao.orange.util.OLog.e(r0, r1, r2)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.orange.ConfigCenter.loadConfig(com.taobao.orange.model.NameSpaceDO):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0077, code lost:
        r13 = r10.mConfigCache.getConfigMap().get(r11);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0083, code lost:
        if (r13 == null) goto L_0x00c4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0085, code lost:
        r13 = r13.getCurVersion();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x008d, code lost:
        if (com.taobao.orange.util.OLog.isPrintLog(0) == false) goto L_0x00a4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x008f, code lost:
        com.taobao.orange.util.OLog.v(TAG, "registerListener onConfigUpdate", "namespace", r11, "version", r13);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:?, code lost:
        r0 = new java.util.HashMap();
        r0.put("fromCache", "true");
        r0.put("configVersion", r13);
        r12.onConfigUpdate(r11, r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x00b9, code lost:
        r11 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x00ba, code lost:
        com.taobao.orange.util.OLog.w(TAG, "registerListener", r11, new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x00c6, code lost:
        if (r10.mIndexCache == null) goto L_0x00e7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x00ce, code lost:
        if (r10.mIndexCache.getNameSpace(r11) == null) goto L_0x00e7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x00d6, code lost:
        if (r10.mIsOrangeInit.get() == false) goto L_0x00e7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x00dc, code lost:
        if (checkLoading(r11, false) != false) goto L_?;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x00de, code lost:
        com.taobao.orange.OThreadFactory.execute(new com.taobao.orange.ConfigCenter.AnonymousClass6(r10));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x00e7, code lost:
        addFail(r11);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:?, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void registerListener(final java.lang.String r11, com.taobao.orange.aidl.ParcelableConfigListener r12, boolean r13) {
        /*
            r10 = this;
            boolean r0 = android.text.TextUtils.isEmpty(r11)
            if (r0 != 0) goto L_0x00ee
            if (r12 != 0) goto L_0x000a
            goto L_0x00ee
        L_0x000a:
            java.util.Map<java.lang.String, java.util.Set<com.taobao.orange.aidl.ParcelableConfigListener>> r0 = r10.mListeners
            monitor-enter(r0)
            java.util.Map<java.lang.String, java.util.Set<com.taobao.orange.aidl.ParcelableConfigListener>> r1 = r10.mListeners     // Catch:{ all -> 0x00eb }
            java.lang.Object r1 = r1.get(r11)     // Catch:{ all -> 0x00eb }
            java.util.Set r1 = (java.util.Set) r1     // Catch:{ all -> 0x00eb }
            if (r1 != 0) goto L_0x0025
            com.taobao.orange.ConfigCenter$5 r1 = new com.taobao.orange.ConfigCenter$5     // Catch:{ all -> 0x00eb }
            r1.<init>()     // Catch:{ all -> 0x00eb }
            java.util.Set r1 = java.util.Collections.newSetFromMap(r1)     // Catch:{ all -> 0x00eb }
            java.util.Map<java.lang.String, java.util.Set<com.taobao.orange.aidl.ParcelableConfigListener>> r2 = r10.mListeners     // Catch:{ all -> 0x00eb }
            r2.put(r11, r1)     // Catch:{ all -> 0x00eb }
        L_0x0025:
            boolean r2 = r1.contains(r12)     // Catch:{ all -> 0x00eb }
            if (r2 == 0) goto L_0x002d
            monitor-exit(r0)     // Catch:{ all -> 0x00eb }
            return
        L_0x002d:
            r2 = 3
            r3 = 4
            r4 = 2
            r5 = 1
            r6 = 0
            if (r13 == 0) goto L_0x005b
            r1.add(r12)     // Catch:{ all -> 0x00eb }
            boolean r13 = com.taobao.orange.util.OLog.isPrintLog(r5)     // Catch:{ all -> 0x00eb }
            if (r13 == 0) goto L_0x0076
            java.lang.String r13 = "ConfigCenter"
            java.lang.String r7 = "registerListener append"
            java.lang.Object[] r8 = new java.lang.Object[r3]     // Catch:{ all -> 0x00eb }
            java.lang.String r9 = "namespace"
            r8[r6] = r9     // Catch:{ all -> 0x00eb }
            r8[r5] = r11     // Catch:{ all -> 0x00eb }
            java.lang.String r9 = "size"
            r8[r4] = r9     // Catch:{ all -> 0x00eb }
            int r1 = r1.size()     // Catch:{ all -> 0x00eb }
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)     // Catch:{ all -> 0x00eb }
            r8[r2] = r1     // Catch:{ all -> 0x00eb }
            com.taobao.orange.util.OLog.d(r13, r7, r8)     // Catch:{ all -> 0x00eb }
            goto L_0x0076
        L_0x005b:
            boolean r13 = com.taobao.orange.util.OLog.isPrintLog(r5)     // Catch:{ all -> 0x00eb }
            if (r13 == 0) goto L_0x0070
            java.lang.String r13 = "ConfigCenter"
            java.lang.String r7 = "registerListener cover"
            java.lang.Object[] r8 = new java.lang.Object[r4]     // Catch:{ all -> 0x00eb }
            java.lang.String r9 = "namespace"
            r8[r6] = r9     // Catch:{ all -> 0x00eb }
            r8[r5] = r11     // Catch:{ all -> 0x00eb }
            com.taobao.orange.util.OLog.d(r13, r7, r8)     // Catch:{ all -> 0x00eb }
        L_0x0070:
            r1.clear()     // Catch:{ all -> 0x00eb }
            r1.add(r12)     // Catch:{ all -> 0x00eb }
        L_0x0076:
            monitor-exit(r0)     // Catch:{ all -> 0x00eb }
            com.taobao.orange.cache.ConfigCache r13 = r10.mConfigCache
            java.util.Map r13 = r13.getConfigMap()
            java.lang.Object r13 = r13.get(r11)
            com.taobao.orange.model.ConfigDO r13 = (com.taobao.orange.model.ConfigDO) r13
            if (r13 == 0) goto L_0x00c4
            java.lang.String r13 = r13.getCurVersion()
            boolean r0 = com.taobao.orange.util.OLog.isPrintLog(r6)
            if (r0 == 0) goto L_0x00a4
            java.lang.String r0 = "ConfigCenter"
            java.lang.String r1 = "registerListener onConfigUpdate"
            java.lang.Object[] r3 = new java.lang.Object[r3]
            java.lang.String r7 = "namespace"
            r3[r6] = r7
            r3[r5] = r11
            java.lang.String r5 = "version"
            r3[r4] = r5
            r3[r2] = r13
            com.taobao.orange.util.OLog.v(r0, r1, r3)
        L_0x00a4:
            java.util.HashMap r0 = new java.util.HashMap     // Catch:{ Throwable -> 0x00b9 }
            r0.<init>()     // Catch:{ Throwable -> 0x00b9 }
            java.lang.String r1 = "fromCache"
            java.lang.String r2 = "true"
            r0.put(r1, r2)     // Catch:{ Throwable -> 0x00b9 }
            java.lang.String r1 = "configVersion"
            r0.put(r1, r13)     // Catch:{ Throwable -> 0x00b9 }
            r12.onConfigUpdate(r11, r0)     // Catch:{ Throwable -> 0x00b9 }
            goto L_0x00ea
        L_0x00b9:
            r11 = move-exception
            java.lang.String r12 = "ConfigCenter"
            java.lang.String r13 = "registerListener"
            java.lang.Object[] r0 = new java.lang.Object[r6]
            com.taobao.orange.util.OLog.w(r12, r13, r11, r0)
            goto L_0x00ea
        L_0x00c4:
            com.taobao.orange.cache.IndexCache r12 = r10.mIndexCache
            if (r12 == 0) goto L_0x00e7
            com.taobao.orange.cache.IndexCache r12 = r10.mIndexCache
            com.taobao.orange.model.NameSpaceDO r12 = r12.getNameSpace(r11)
            if (r12 == 0) goto L_0x00e7
            java.util.concurrent.atomic.AtomicBoolean r12 = r10.mIsOrangeInit
            boolean r12 = r12.get()
            if (r12 == 0) goto L_0x00e7
            boolean r12 = r10.checkLoading(r11, r6)
            if (r12 != 0) goto L_0x00ea
            com.taobao.orange.ConfigCenter$6 r12 = new com.taobao.orange.ConfigCenter$6
            r12.<init>(r11)
            com.taobao.orange.OThreadFactory.execute(r12)
            goto L_0x00ea
        L_0x00e7:
            r10.addFail(r11)
        L_0x00ea:
            return
        L_0x00eb:
            r11 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x00eb }
            throw r11
        L_0x00ee:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.orange.ConfigCenter.registerListener(java.lang.String, com.taobao.orange.aidl.ParcelableConfigListener, boolean):void");
    }

    public void unregisterListener(String str, ParcelableConfigListener parcelableConfigListener) {
        if (!TextUtils.isEmpty(str) && parcelableConfigListener != null) {
            synchronized (this.mListeners) {
                Set set = this.mListeners.get(str);
                if (set != null && set.size() > 0 && set.remove(parcelableConfigListener) && OLog.isPrintLog(1)) {
                    OLog.d(TAG, "unregisterListener", "namespace", str, "size", Integer.valueOf(set.size()));
                }
            }
        }
    }

    public void unregisterListeners(String str) {
        if (!TextUtils.isEmpty(str)) {
            synchronized (this.mListeners) {
                this.mListeners.remove(str);
            }
        }
    }

    public void notifyListeners(String str, String str2, boolean z) {
        if (!TextUtils.isEmpty(str)) {
            HashMap hashMap = new HashMap();
            hashMap.put("fromCache", String.valueOf(z));
            hashMap.put("configVersion", str2);
            if (!z && !this.mGlobalListeners.isEmpty()) {
                for (ParcelableConfigListener onConfigUpdate : this.mGlobalListeners) {
                    try {
                        onConfigUpdate.onConfigUpdate(str, hashMap);
                    } catch (Throwable th) {
                        OLog.w(TAG, "notifyGlobalListeners", th, new Object[0]);
                    }
                }
            }
            HashSet<ParcelableConfigListener> hashSet = new HashSet<>();
            synchronized (this.mListeners) {
                Set set = this.mListeners.get(str);
                if (set != null && set.size() > 0) {
                    hashSet.addAll(set);
                }
            }
            if (hashSet.size() > 0) {
                if (OLog.isPrintLog(1)) {
                    OLog.d(TAG, "notifyListeners ", "namespace", str, "args", hashMap, "listenerSet.size", Integer.valueOf(hashSet.size()));
                }
                for (ParcelableConfigListener onConfigUpdate2 : hashSet) {
                    try {
                        onConfigUpdate2.onConfigUpdate(str, hashMap);
                    } catch (Throwable th2) {
                        OLog.w(TAG, "notifyListeners", th2, new Object[0]);
                    }
                }
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:8:0x001d, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void updateIndex(com.taobao.orange.sync.IndexUpdateHandler.IndexUpdateInfo r10) {
        /*
            r9 = this;
            monitor-enter(r9)
            boolean r10 = r9.loadIndex(r10)     // Catch:{ all -> 0x00ba }
            r0 = 1
            r1 = 0
            if (r10 != 0) goto L_0x001e
            boolean r10 = com.taobao.orange.util.OLog.isPrintLog(r1)     // Catch:{ all -> 0x00ba }
            if (r10 == 0) goto L_0x001c
            java.lang.String r10 = "ConfigCenter"
            java.lang.String r2 = "updateIndex"
            java.lang.Object[] r0 = new java.lang.Object[r0]     // Catch:{ all -> 0x00ba }
            java.lang.String r3 = "no need update or update fail index file"
            r0[r1] = r3     // Catch:{ all -> 0x00ba }
            com.taobao.orange.util.OLog.v(r10, r2, r0)     // Catch:{ all -> 0x00ba }
        L_0x001c:
            monitor-exit(r9)
            return
        L_0x001e:
            long r2 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x00ba }
            java.util.HashSet r10 = new java.util.HashSet     // Catch:{ all -> 0x00ba }
            com.taobao.orange.cache.ConfigCache r4 = r9.mConfigCache     // Catch:{ all -> 0x00ba }
            java.util.Map r4 = r4.getConfigMap()     // Catch:{ all -> 0x00ba }
            int r4 = r4.size()     // Catch:{ all -> 0x00ba }
            java.util.Set<java.lang.String> r5 = r9.mFailRequestsSet     // Catch:{ all -> 0x00ba }
            int r5 = r5.size()     // Catch:{ all -> 0x00ba }
            int r4 = r4 + r5
            double r4 = (double) r4
            r6 = 4608983858650965606(0x3ff6666666666666, double:1.4)
            java.lang.Double.isNaN(r4)
            double r4 = r4 * r6
            int r4 = (int) r4
            r10.<init>(r4)     // Catch:{ all -> 0x00ba }
            com.taobao.orange.cache.ConfigCache r4 = r9.mConfigCache     // Catch:{ all -> 0x00ba }
            java.util.Map r4 = r4.getConfigMap()     // Catch:{ all -> 0x00ba }
            java.util.Set r4 = r4.keySet()     // Catch:{ all -> 0x00ba }
            r10.addAll(r4)     // Catch:{ all -> 0x00ba }
            java.util.Set<java.lang.String> r4 = r9.mFailRequestsSet     // Catch:{ all -> 0x00ba }
            monitor-enter(r4)     // Catch:{ all -> 0x00ba }
            java.util.Set<java.lang.String> r5 = r9.mFailRequestsSet     // Catch:{ all -> 0x00b7 }
            r10.addAll(r5)     // Catch:{ all -> 0x00b7 }
            monitor-exit(r4)     // Catch:{ all -> 0x00b7 }
            com.taobao.orange.cache.IndexCache r4 = r9.mIndexCache     // Catch:{ all -> 0x00ba }
            java.util.Set r10 = r4.getUpdateNameSpaces(r10)     // Catch:{ all -> 0x00ba }
            java.lang.String r4 = "ConfigCenter"
            java.lang.String r5 = "updateIndex"
            r6 = 2
            java.lang.Object[] r7 = new java.lang.Object[r6]     // Catch:{ all -> 0x00ba }
            java.lang.String r8 = "start load updateNameSpaces"
            r7[r1] = r8     // Catch:{ all -> 0x00ba }
            int r8 = r10.size()     // Catch:{ all -> 0x00ba }
            java.lang.Integer r8 = java.lang.Integer.valueOf(r8)     // Catch:{ all -> 0x00ba }
            r7[r0] = r8     // Catch:{ all -> 0x00ba }
            com.taobao.orange.util.OLog.i(r4, r5, r7)     // Catch:{ all -> 0x00ba }
            java.util.Iterator r4 = r10.iterator()     // Catch:{ all -> 0x00ba }
        L_0x007c:
            boolean r5 = r4.hasNext()     // Catch:{ all -> 0x00ba }
            if (r5 == 0) goto L_0x008c
            java.lang.Object r5 = r4.next()     // Catch:{ all -> 0x00ba }
            com.taobao.orange.model.NameSpaceDO r5 = (com.taobao.orange.model.NameSpaceDO) r5     // Catch:{ all -> 0x00ba }
            r9.loadConfigLazy(r5)     // Catch:{ all -> 0x00ba }
            goto L_0x007c
        L_0x008c:
            long r4 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x00ba }
            r7 = 0
            long r4 = r4 - r2
            java.lang.String r2 = "ConfigCenter"
            java.lang.String r3 = "updateIndex"
            r7 = 4
            java.lang.Object[] r7 = new java.lang.Object[r7]     // Catch:{ all -> 0x00ba }
            java.lang.String r8 = "finish load updateNameSpaces"
            r7[r1] = r8     // Catch:{ all -> 0x00ba }
            int r10 = r10.size()     // Catch:{ all -> 0x00ba }
            java.lang.Integer r10 = java.lang.Integer.valueOf(r10)     // Catch:{ all -> 0x00ba }
            r7[r0] = r10     // Catch:{ all -> 0x00ba }
            java.lang.String r10 = "cost(ms)"
            r7[r6] = r10     // Catch:{ all -> 0x00ba }
            r10 = 3
            java.lang.Long r0 = java.lang.Long.valueOf(r4)     // Catch:{ all -> 0x00ba }
            r7[r10] = r0     // Catch:{ all -> 0x00ba }
            com.taobao.orange.util.OLog.i(r2, r3, r7)     // Catch:{ all -> 0x00ba }
            monitor-exit(r9)
            return
        L_0x00b7:
            r10 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x00b7 }
            throw r10     // Catch:{ all -> 0x00ba }
        L_0x00ba:
            r10 = move-exception
            monitor-exit(r9)
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.orange.ConfigCenter.updateIndex(com.taobao.orange.sync.IndexUpdateHandler$IndexUpdateInfo):void");
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v8, resolved type: com.taobao.orange.ConfigCenter$7} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v9, resolved type: com.taobao.orange.ConfigCenter$7} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v23, resolved type: com.taobao.orange.ConfigCenter$8} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v26, resolved type: com.taobao.orange.ConfigCenter$7} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean loadIndex(com.taobao.orange.sync.IndexUpdateHandler.IndexUpdateInfo r18) {
        /*
            r17 = this;
            r7 = r17
            r8 = r18
            r9 = 0
            if (r8 == 0) goto L_0x026a
            boolean r0 = r18.checkValid()
            if (r0 != 0) goto L_0x000f
            goto L_0x026a
        L_0x000f:
            com.taobao.orange.cache.IndexCache r0 = r7.mIndexCache
            com.taobao.orange.model.IndexDO r0 = r0.getIndex()
            java.lang.String r0 = r0.md5
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            r10 = 1
            if (r0 != 0) goto L_0x003c
            com.taobao.orange.cache.IndexCache r0 = r7.mIndexCache
            com.taobao.orange.model.IndexDO r0 = r0.getIndex()
            java.lang.String r0 = r0.md5
            java.lang.String r1 = r8.md5
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x003c
            java.lang.String r0 = "ConfigCenter"
            java.lang.String r1 = "loadIndex fail"
            java.lang.Object[] r2 = new java.lang.Object[r10]
            java.lang.String r3 = "cdnMd5 is match"
            r2[r9] = r3
            com.taobao.orange.util.OLog.w(r0, r1, r2)
            return r9
        L_0x003c:
            java.util.concurrent.atomic.AtomicInteger r0 = com.taobao.orange.GlobalOrange.indexContinueFailsNum
            int r0 = r0.get()
            long r0 = (long) r0
            r2 = 10
            r11 = 3
            int r4 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r4 < 0) goto L_0x008a
            long r0 = java.lang.System.currentTimeMillis()
            long r2 = failLastIndexUpdTime
            r4 = 0
            int r6 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r6 == 0) goto L_0x0078
            long r2 = failLastIndexUpdTime
            long r0 = r0 - r2
            r2 = 180000(0x2bf20, double:8.8932E-319)
            int r6 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r6 > 0) goto L_0x0061
            return r9
        L_0x0061:
            java.util.concurrent.atomic.AtomicInteger r0 = com.taobao.orange.GlobalOrange.indexContinueFailsNum
            r0.set(r9)
            failLastIndexUpdTime = r4
            boolean r0 = com.taobao.orange.util.OLog.isPrintLog(r11)
            if (r0 == 0) goto L_0x008a
            java.lang.String r0 = "ConfigCenter"
            java.lang.String r1 = "updateIndex continuous fail already wait 100s"
            java.lang.Object[] r2 = new java.lang.Object[r9]
            com.taobao.orange.util.OLog.w(r0, r1, r2)
            goto L_0x008a
        L_0x0078:
            failLastIndexUpdTime = r0
            boolean r0 = com.taobao.orange.util.OLog.isPrintLog(r11)
            if (r0 == 0) goto L_0x0089
            java.lang.String r0 = "ConfigCenter"
            java.lang.String r1 = "updateIndex continuous fail numbers exceed 10"
            java.lang.Object[] r2 = new java.lang.Object[r9]
            com.taobao.orange.util.OLog.w(r0, r1, r2)
        L_0x0089:
            return r9
        L_0x008a:
            java.util.concurrent.atomic.AtomicInteger r0 = com.taobao.orange.GlobalOrange.indexContinueFailsNum
            r0.incrementAndGet()
            r12 = 2
            boolean r0 = com.taobao.orange.util.OLog.isPrintLog(r12)
            r13 = 4
            if (r0 == 0) goto L_0x00ba
            java.lang.String r0 = "ConfigCenter"
            java.lang.String r1 = "loadIndex start"
            r2 = 6
            java.lang.Object[] r2 = new java.lang.Object[r2]
            java.lang.String r3 = "cdn"
            r2[r9] = r3
            java.lang.String r3 = r8.cdn
            r2[r10] = r3
            java.lang.String r3 = "resource"
            r2[r12] = r3
            java.lang.String r3 = r8.resourceId
            r2[r11] = r3
            java.lang.String r3 = "md5"
            r2[r13] = r3
            r3 = 5
            java.lang.String r4 = r8.md5
            r2[r3] = r4
            com.taobao.orange.util.OLog.i(r0, r1, r2)
        L_0x00ba:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0250 }
            r0.<init>()     // Catch:{ Throwable -> 0x0250 }
            java.lang.String r1 = com.taobao.orange.GlobalOrange.schema     // Catch:{ Throwable -> 0x0250 }
            r0.append(r1)     // Catch:{ Throwable -> 0x0250 }
            java.lang.String r1 = "://"
            r0.append(r1)     // Catch:{ Throwable -> 0x0250 }
            java.lang.String r1 = r8.cdn     // Catch:{ Throwable -> 0x0250 }
            r0.append(r1)     // Catch:{ Throwable -> 0x0250 }
            java.lang.String r1 = java.io.File.separator     // Catch:{ Throwable -> 0x0250 }
            r0.append(r1)     // Catch:{ Throwable -> 0x0250 }
            java.lang.String r1 = r8.resourceId     // Catch:{ Throwable -> 0x0250 }
            r0.append(r1)     // Catch:{ Throwable -> 0x0250 }
            java.lang.String r0 = r0.toString()     // Catch:{ Throwable -> 0x0250 }
            com.taobao.orange.ConfigCenter$7 r1 = new com.taobao.orange.ConfigCenter$7     // Catch:{ Throwable -> 0x0250 }
            java.lang.String r2 = r8.md5     // Catch:{ Throwable -> 0x0250 }
            r1.<init>(r0, r2)     // Catch:{ Throwable -> 0x0250 }
            java.lang.Object r0 = r1.syncRequest()     // Catch:{ Throwable -> 0x0250 }
            com.taobao.orange.model.IndexDO r0 = (com.taobao.orange.model.IndexDO) r0     // Catch:{ Throwable -> 0x0250 }
            boolean r2 = com.taobao.orange.util.OrangeMonitor.mPerformanceInfoRecordDone     // Catch:{ Throwable -> 0x0250 }
            if (r2 != 0) goto L_0x00f2
            java.util.concurrent.atomic.AtomicInteger r2 = r7.mRequestCount     // Catch:{ Throwable -> 0x0250 }
            r2.incrementAndGet()     // Catch:{ Throwable -> 0x0250 }
        L_0x00f2:
            boolean r2 = com.taobao.orange.GlobalOrange.fallbackAvoid     // Catch:{ Throwable -> 0x0250 }
            r14 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            if (r2 != 0) goto L_0x013e
            if (r0 == 0) goto L_0x0100
            boolean r2 = r0.checkValid()     // Catch:{ Throwable -> 0x0250 }
            if (r2 != 0) goto L_0x0147
        L_0x0100:
            boolean r0 = com.taobao.orange.util.OLog.isPrintLog(r9)     // Catch:{ Throwable -> 0x0250 }
            if (r0 == 0) goto L_0x0123
            java.lang.String r0 = "ConfigCenter"
            java.lang.String r2 = "loadIndex cdnReq fail downgrade to authReq"
            java.lang.Object[] r3 = new java.lang.Object[r13]     // Catch:{ Throwable -> 0x0250 }
            java.lang.String r4 = "code"
            r3[r9] = r4     // Catch:{ Throwable -> 0x0250 }
            java.lang.String r4 = r1.getCode()     // Catch:{ Throwable -> 0x0250 }
            r3[r10] = r4     // Catch:{ Throwable -> 0x0250 }
            java.lang.String r4 = "msg"
            r3[r12] = r4     // Catch:{ Throwable -> 0x0250 }
            java.lang.String r1 = r1.getMessage()     // Catch:{ Throwable -> 0x0250 }
            r3[r11] = r1     // Catch:{ Throwable -> 0x0250 }
            com.taobao.orange.util.OLog.v(r0, r2, r3)     // Catch:{ Throwable -> 0x0250 }
        L_0x0123:
            com.taobao.orange.ConfigCenter$8 r0 = new com.taobao.orange.ConfigCenter$8     // Catch:{ Throwable -> 0x0250 }
            java.lang.String r3 = r8.md5     // Catch:{ Throwable -> 0x0250 }
            r4 = 0
            java.lang.String r5 = "/downloadResource"
            r1 = r0
            r2 = r17
            r6 = r18
            r1.<init>(r3, r4, r5, r6)     // Catch:{ Throwable -> 0x0250 }
            java.lang.Object r1 = r0.syncRequest()     // Catch:{ Throwable -> 0x0250 }
            com.taobao.orange.model.IndexDO r1 = (com.taobao.orange.model.IndexDO) r1     // Catch:{ Throwable -> 0x0250 }
            r16 = r1
            r1 = r0
            r0 = r16
            goto L_0x0147
        L_0x013e:
            java.lang.String r2 = "private_orange"
            java.lang.String r3 = "fallback_avoid"
            java.lang.String r4 = r8.resourceId     // Catch:{ Throwable -> 0x0250 }
            com.taobao.orange.util.OrangeMonitor.commitCount(r2, r3, r4, r14)     // Catch:{ Throwable -> 0x0250 }
        L_0x0147:
            if (r0 == 0) goto L_0x0204
            boolean r2 = r0.checkValid()     // Catch:{ Throwable -> 0x0250 }
            if (r2 == 0) goto L_0x0204
            java.util.concurrent.atomic.AtomicInteger r1 = com.taobao.orange.GlobalOrange.indexContinueFailsNum     // Catch:{ Throwable -> 0x0250 }
            r1.set(r9)     // Catch:{ Throwable -> 0x0250 }
            java.lang.String r1 = r0.id     // Catch:{ Throwable -> 0x0250 }
            com.taobao.orange.cache.IndexCache r2 = r7.mIndexCache     // Catch:{ Throwable -> 0x0250 }
            com.taobao.orange.model.IndexDO r2 = r2.getIndex()     // Catch:{ Throwable -> 0x0250 }
            java.lang.String r2 = r2.id     // Catch:{ Throwable -> 0x0250 }
            boolean r1 = r1.equals(r2)     // Catch:{ Throwable -> 0x0250 }
            if (r1 != 0) goto L_0x01f6
            java.lang.String r1 = r0.version     // Catch:{ Throwable -> 0x0250 }
            com.taobao.orange.cache.IndexCache r2 = r7.mIndexCache     // Catch:{ Throwable -> 0x0250 }
            com.taobao.orange.model.IndexDO r2 = r2.getIndex()     // Catch:{ Throwable -> 0x0250 }
            java.lang.String r2 = r2.version     // Catch:{ Throwable -> 0x0250 }
            boolean r1 = r1.equals(r2)     // Catch:{ Throwable -> 0x0250 }
            if (r1 == 0) goto L_0x0176
            goto L_0x01f6
        L_0x0176:
            java.lang.String r1 = r8.md5     // Catch:{ Throwable -> 0x0250 }
            r0.md5 = r1     // Catch:{ Throwable -> 0x0250 }
            com.taobao.orange.cache.IndexCache r1 = r7.mIndexCache     // Catch:{ Throwable -> 0x0250 }
            java.util.List r1 = r1.cache(r0)     // Catch:{ Throwable -> 0x0250 }
            java.lang.String r2 = "OrangeConfig"
            java.lang.String r3 = "index_rate"
            java.lang.String r4 = r8.resourceId     // Catch:{ Throwable -> 0x0250 }
            com.taobao.orange.util.OrangeMonitor.commitSuccess(r2, r3, r4)     // Catch:{ Throwable -> 0x0250 }
            boolean r2 = com.taobao.orange.util.OLog.isPrintLog(r10)     // Catch:{ Throwable -> 0x0250 }
            if (r2 == 0) goto L_0x01a2
            java.lang.String r2 = "ConfigCenter"
            java.lang.String r3 = "loadIndex success"
            java.lang.Object[] r4 = new java.lang.Object[r12]     // Catch:{ Throwable -> 0x0250 }
            java.lang.String r5 = "indexDO"
            r4[r9] = r5     // Catch:{ Throwable -> 0x0250 }
            java.lang.String r5 = com.taobao.orange.util.OrangeUtils.formatIndexDO(r0)     // Catch:{ Throwable -> 0x0250 }
            r4[r10] = r5     // Catch:{ Throwable -> 0x0250 }
            com.taobao.orange.util.OLog.d(r2, r3, r4)     // Catch:{ Throwable -> 0x0250 }
        L_0x01a2:
            com.taobao.orange.model.IndexAckDO r2 = new com.taobao.orange.model.IndexAckDO     // Catch:{ Exception -> 0x01b3 }
            java.lang.String r0 = r0.id     // Catch:{ Exception -> 0x01b3 }
            java.lang.String r3 = com.taobao.orange.util.OrangeUtils.getCurFormatTime()     // Catch:{ Exception -> 0x01b3 }
            java.lang.String r4 = r8.md5     // Catch:{ Exception -> 0x01b3 }
            r2.<init>(r0, r3, r4)     // Catch:{ Exception -> 0x01b3 }
            com.taobao.orange.util.ReportAckUtils.reportIndexAck(r2)     // Catch:{ Exception -> 0x01b3 }
            goto L_0x01bd
        L_0x01b3:
            r0 = move-exception
            java.lang.String r2 = "ConfigCenter"
            java.lang.String r3 = "loadIndex"
            java.lang.Object[] r4 = new java.lang.Object[r9]     // Catch:{ Throwable -> 0x0250 }
            com.taobao.orange.util.OLog.w(r2, r3, r0, r4)     // Catch:{ Throwable -> 0x0250 }
        L_0x01bd:
            int r0 = r1.size()     // Catch:{ Throwable -> 0x0250 }
            if (r0 <= 0) goto L_0x01f5
            boolean r0 = com.taobao.orange.util.OLog.isPrintLog(r12)     // Catch:{ Throwable -> 0x0250 }
            if (r0 == 0) goto L_0x01d8
            java.lang.String r0 = "ConfigCenter"
            java.lang.String r2 = "loadIndex remove diff namespace"
            java.lang.Object[] r3 = new java.lang.Object[r12]     // Catch:{ Throwable -> 0x0250 }
            java.lang.String r4 = "removeNamespaces"
            r3[r9] = r4     // Catch:{ Throwable -> 0x0250 }
            r3[r10] = r1     // Catch:{ Throwable -> 0x0250 }
            com.taobao.orange.util.OLog.i(r0, r2, r3)     // Catch:{ Throwable -> 0x0250 }
        L_0x01d8:
            java.util.Iterator r0 = r1.iterator()     // Catch:{ Throwable -> 0x0250 }
        L_0x01dc:
            boolean r1 = r0.hasNext()     // Catch:{ Throwable -> 0x0250 }
            if (r1 == 0) goto L_0x01f5
            java.lang.Object r1 = r0.next()     // Catch:{ Throwable -> 0x0250 }
            java.lang.String r1 = (java.lang.String) r1     // Catch:{ Throwable -> 0x0250 }
            java.lang.String r2 = "private_orange"
            java.lang.String r3 = "config_remove_counts"
            com.taobao.orange.util.OrangeMonitor.commitCount(r2, r3, r1, r14)     // Catch:{ Throwable -> 0x0250 }
            com.taobao.orange.cache.ConfigCache r2 = r7.mConfigCache     // Catch:{ Throwable -> 0x0250 }
            r2.remove(r1)     // Catch:{ Throwable -> 0x0250 }
            goto L_0x01dc
        L_0x01f5:
            return r10
        L_0x01f6:
            java.lang.String r0 = "ConfigCenter"
            java.lang.String r1 = "loadIndex fail"
            java.lang.Object[] r2 = new java.lang.Object[r10]     // Catch:{ Throwable -> 0x0250 }
            java.lang.String r3 = "id or version is match"
            r2[r9] = r3     // Catch:{ Throwable -> 0x0250 }
            com.taobao.orange.util.OLog.w(r0, r1, r2)     // Catch:{ Throwable -> 0x0250 }
            return r9
        L_0x0204:
            java.lang.String r2 = "-200"
            java.lang.String r3 = r1.getCode()     // Catch:{ Throwable -> 0x0250 }
            boolean r2 = r2.equals(r3)     // Catch:{ Throwable -> 0x0250 }
            if (r2 != 0) goto L_0x0232
            if (r0 == 0) goto L_0x0221
            boolean r0 = r0.checkValid()     // Catch:{ Throwable -> 0x0250 }
            if (r0 != 0) goto L_0x0221
            r0 = -5
            r1.setCode(r0)     // Catch:{ Throwable -> 0x0250 }
            java.lang.String r0 = "index is invaild"
            r1.setMessage(r0)     // Catch:{ Throwable -> 0x0250 }
        L_0x0221:
            java.lang.String r0 = "OrangeConfig"
            java.lang.String r2 = "index_rate"
            java.lang.String r3 = r8.resourceId     // Catch:{ Throwable -> 0x0250 }
            java.lang.String r4 = r1.getCode()     // Catch:{ Throwable -> 0x0250 }
            java.lang.String r5 = r1.getMessage()     // Catch:{ Throwable -> 0x0250 }
            com.taobao.orange.util.OrangeMonitor.commitFail(r0, r2, r3, r4, r5)     // Catch:{ Throwable -> 0x0250 }
        L_0x0232:
            java.lang.String r0 = "ConfigCenter"
            java.lang.String r2 = "loadIndex fail"
            java.lang.Object[] r3 = new java.lang.Object[r13]     // Catch:{ Throwable -> 0x0250 }
            java.lang.String r4 = "code"
            r3[r9] = r4     // Catch:{ Throwable -> 0x0250 }
            java.lang.String r4 = r1.getCode()     // Catch:{ Throwable -> 0x0250 }
            r3[r10] = r4     // Catch:{ Throwable -> 0x0250 }
            java.lang.String r4 = "msg"
            r3[r12] = r4     // Catch:{ Throwable -> 0x0250 }
            java.lang.String r1 = r1.getMessage()     // Catch:{ Throwable -> 0x0250 }
            r3[r11] = r1     // Catch:{ Throwable -> 0x0250 }
            com.taobao.orange.util.OLog.e(r0, r2, r3)     // Catch:{ Throwable -> 0x0250 }
            goto L_0x0269
        L_0x0250:
            r0 = move-exception
            java.lang.String r1 = "OrangeConfig"
            java.lang.String r2 = "index_rate"
            java.lang.String r3 = r8.resourceId
            java.lang.String r4 = "0"
            java.lang.String r5 = r0.getMessage()
            com.taobao.orange.util.OrangeMonitor.commitFail(r1, r2, r3, r4, r5)
            java.lang.String r1 = "ConfigCenter"
            java.lang.String r2 = "loadIndex fail"
            java.lang.Object[] r3 = new java.lang.Object[r9]
            com.taobao.orange.util.OLog.e(r1, r2, r0, r3)
        L_0x0269:
            return r9
        L_0x026a:
            java.lang.String r0 = "ConfigCenter"
            java.lang.String r1 = "updateIndex param is null"
            java.lang.Object[] r2 = new java.lang.Object[r9]
            com.taobao.orange.util.OLog.e(r0, r1, r2)
            return r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.orange.ConfigCenter.loadIndex(com.taobao.orange.sync.IndexUpdateHandler$IndexUpdateInfo):boolean");
    }

    /* access modifiers changed from: package-private */
    public void updateSystemConfig(Map map) {
        List<String> parseArray;
        List<String> parseArray2;
        JSONArray parseArray3;
        try {
            Map map2 = (Map) this.mConfigCache.getConfigObj("orange");
            if (OLog.isPrintLog(2)) {
                OLog.i(TAG, "updateSystemConfig", "args", map, "orangeConfigs", map2);
            }
            if (map2 != null && !map2.isEmpty()) {
                String str = (String) map2.get(OConstant.SYSKEY_FALLBACK_AVOID);
                if (!TextUtils.isEmpty(str)) {
                    GlobalOrange.fallbackAvoid = Boolean.parseBoolean(str);
                    OLog.i(TAG, "updateSystemConfig", OConstant.SYSKEY_FALLBACK_AVOID, Boolean.valueOf(GlobalOrange.fallbackAvoid));
                }
                String str2 = (String) map2.get(OConstant.SYSKEY_REQ_RETRY_NUM);
                if (!TextUtils.isEmpty(str2)) {
                    int parseInt = Integer.parseInt(str2);
                    if (parseInt > 5) {
                        parseInt = 5;
                    }
                    GlobalOrange.reqRetryNum = parseInt;
                    OLog.i(TAG, "updateSystemConfig", OConstant.SYSKEY_REQ_RETRY_NUM, Integer.valueOf(GlobalOrange.reqRetryNum));
                }
                String str3 = (String) map2.get(OConstant.SYSKEY_REPORT_UPDACK);
                if (!TextUtils.isEmpty(str3)) {
                    GlobalOrange.reportUpdateAck = Integer.parseInt(str3) == 1;
                    OLog.i(TAG, "updateSystemConfig", OConstant.SYSKEY_REPORT_UPDACK, Boolean.valueOf(GlobalOrange.reportUpdateAck));
                }
                String str4 = (String) map2.get(OConstant.SYSKEY_DELAYACK_INTERVAL);
                if (!TextUtils.isEmpty(str4)) {
                    long parseLong = Long.parseLong(str4);
                    OLog.i(TAG, "updateSystemConfig", OConstant.SYSKEY_DELAYACK_INTERVAL, Long.valueOf(parseLong));
                    if (parseLong > 0) {
                        GlobalOrange.randomDelayAckInterval = updateRandomDelayAckInterval(parseLong);
                        OLog.i(TAG, "updateSystemConfig", "randomDelayAckInterval", Long.valueOf(GlobalOrange.randomDelayAckInterval));
                    }
                }
                String str5 = (String) map2.get(OConstant.SYSKEY_INDEXUPD_MODE);
                if (!TextUtils.isEmpty(str5)) {
                    GlobalOrange.indexUpdMode = OConstant.UPDMODE.valueOf(Integer.parseInt(str5));
                    OLog.i(TAG, "updateSystemConfig", "indexUpdMode", GlobalOrange.indexUpdMode);
                }
                String str6 = (String) map2.get("downgrade");
                if (!TextUtils.isEmpty(str6)) {
                    if (Boolean.valueOf(str6).booleanValue()) {
                        GlobalOrange.downgrade = 2;
                    }
                    OLog.i(TAG, "updateSystemConfig", "downgrade", Integer.valueOf(GlobalOrange.downgrade));
                }
                String str7 = (String) map2.get("hosts");
                if (!TextUtils.isEmpty(str7) && (parseArray3 = JSON.parseArray(str7)) != null && parseArray3.size() >= 0) {
                    ArrayList arrayList = new ArrayList(parseArray3.size());
                    for (int i = 0; i < parseArray3.size(); i++) {
                        String string = parseArray3.getJSONObject(i).getString("host");
                        if (!TextUtils.isEmpty(string)) {
                            arrayList.add(string);
                        }
                    }
                    if (arrayList.size() > 0) {
                        GlobalOrange.probeHosts.clear();
                        GlobalOrange.probeHosts.addAll(arrayList);
                        OLog.i(TAG, "updateSystemConfig", "probeHosts", GlobalOrange.probeHosts);
                    }
                }
                String str8 = (String) map2.get(OConstant.SYSKEY_DCVIPS);
                if (!TextUtils.isEmpty(str8) && (parseArray2 = JSON.parseArray(str8, String.class)) != null && parseArray2.size() > 0) {
                    GlobalOrange.dcVips.clear();
                    GlobalOrange.dcVips.addAll(parseArray2);
                    OLog.i(TAG, "updateSystemConfig", OConstant.SYSKEY_DCVIPS, GlobalOrange.dcVips);
                }
                String str9 = (String) map2.get(OConstant.SYSKEY_ACKVIPS);
                if (!TextUtils.isEmpty(str9) && (parseArray = JSON.parseArray(str9, String.class)) != null && parseArray.size() > 0) {
                    GlobalOrange.ackVips.clear();
                    GlobalOrange.ackVips.addAll(parseArray);
                    OLog.i(TAG, "updateSystemConfig", OConstant.SYSKEY_ACKVIPS, GlobalOrange.ackVips);
                }
            }
        } catch (Throwable th) {
            OLog.e(TAG, "updateSystemConfig", th, new Object[0]);
        }
    }

    /* access modifiers changed from: package-private */
    public long updateRandomDelayAckInterval(long j) {
        if (j == 0) {
            return 0;
        }
        return OrangeUtils.hash(GlobalOrange.deviceId) % (j * 1000);
    }

    public void addFails(String[] strArr) {
        ArrayList arrayList = new ArrayList(strArr.length);
        for (String str : strArr) {
            if (!TextUtils.isEmpty(str)) {
                arrayList.add(str);
            }
        }
        if (!arrayList.isEmpty()) {
            synchronized (this.mFailRequestsSet) {
                if (this.mFailRequestsSet.addAll(arrayList) && OLog.isPrintLog(2)) {
                    OLog.i(TAG, "addFails", "namespaces", arrayList);
                }
            }
        }
    }

    private void addFail(String str) {
        if (!TextUtils.isEmpty(str)) {
            synchronized (this.mFailRequestsSet) {
                if (this.mFailRequestsSet.add(str) && OLog.isPrintLog(2)) {
                    OLog.i(TAG, "addFail", "namespace", str);
                }
            }
        }
    }

    public void removeFail(String str) {
        if (!TextUtils.isEmpty(str)) {
            synchronized (this.mFailRequestsSet) {
                if (this.mFailRequestsSet.remove(str) && OLog.isPrintLog(2)) {
                    OLog.i(TAG, "removeFail", "namespace", str);
                }
            }
        }
    }

    public synchronized void retryFailRequests() {
        HashSet<NameSpaceDO> hashSet = new HashSet<>();
        synchronized (this.mFailRequestsSet) {
            for (String nameSpace : this.mFailRequestsSet) {
                NameSpaceDO nameSpace2 = this.mIndexCache.getNameSpace(nameSpace);
                if (nameSpace2 != null) {
                    hashSet.add(nameSpace2);
                }
            }
        }
        if (!hashSet.isEmpty()) {
            OLog.i(TAG, "retryFailRequests", "start load retryNamespaces", Integer.valueOf(hashSet.size()));
            long currentTimeMillis = System.currentTimeMillis();
            for (NameSpaceDO loadConfig : hashSet) {
                loadConfig(loadConfig);
            }
            OLog.i(TAG, "retryFailRequests", "finish load retryNamespaces", Integer.valueOf(hashSet.size()), "cost(ms)", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
        } else if (OLog.isPrintLog(1)) {
            OLog.d(TAG, "retryFailRequests no any", new Object[0]);
        }
    }

    private boolean checkLoading(String str, boolean z) {
        if (this.mLoadingConfigMap.get(str) != null) {
            OLog.d(TAG, BindingXConstants.KEY_CONFIG, str, "is loading");
            return true;
        }
        if (z) {
            this.mLoadingConfigMap.put(str, Long.valueOf(System.currentTimeMillis()));
        }
        return false;
    }

    private void removeLoading(String str) {
        if (!TextUtils.isEmpty(str)) {
            this.mLoadingConfigMap.remove(str);
        }
    }

    public void rematchNamespace(final Set<String> set) {
        OThreadFactory.execute(new Runnable() {
            /* JADX WARNING: Code restructure failed: missing block: B:38:0x00f0, code lost:
                return;
             */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void run() {
                /*
                    r10 = this;
                    com.taobao.orange.ConfigCenter r0 = com.taobao.orange.ConfigCenter.this
                    monitor-enter(r0)
                    java.util.Set r1 = r2     // Catch:{ all -> 0x010c }
                    r2 = 0
                    if (r1 == 0) goto L_0x00f1
                    java.util.Set r1 = r2     // Catch:{ all -> 0x010c }
                    boolean r1 = r1.isEmpty()     // Catch:{ all -> 0x010c }
                    if (r1 != 0) goto L_0x00f1
                    com.taobao.orange.ConfigCenter r1 = com.taobao.orange.ConfigCenter.this     // Catch:{ all -> 0x010c }
                    com.taobao.orange.cache.IndexCache r1 = r1.mIndexCache     // Catch:{ all -> 0x010c }
                    java.util.Map<java.lang.String, java.util.Set<java.lang.String>> r1 = r1.candidateNamespace     // Catch:{ all -> 0x010c }
                    if (r1 == 0) goto L_0x00f1
                    com.taobao.orange.ConfigCenter r1 = com.taobao.orange.ConfigCenter.this     // Catch:{ all -> 0x010c }
                    com.taobao.orange.cache.IndexCache r1 = r1.mIndexCache     // Catch:{ all -> 0x010c }
                    java.util.Map<java.lang.String, java.util.Set<java.lang.String>> r1 = r1.candidateNamespace     // Catch:{ all -> 0x010c }
                    boolean r1 = r1.isEmpty()     // Catch:{ all -> 0x010c }
                    if (r1 == 0) goto L_0x0026
                    goto L_0x00f1
                L_0x0026:
                    java.util.Set r1 = r2     // Catch:{ all -> 0x010c }
                    com.taobao.orange.ConfigCenter r3 = com.taobao.orange.ConfigCenter.this     // Catch:{ all -> 0x010c }
                    java.util.Set<java.lang.String> r3 = r3.failCandidateSet     // Catch:{ all -> 0x010c }
                    r1.addAll(r3)     // Catch:{ all -> 0x010c }
                    com.taobao.orange.ConfigCenter r1 = com.taobao.orange.ConfigCenter.this     // Catch:{ all -> 0x010c }
                    java.util.Set<java.lang.String> r1 = r1.failCandidateSet     // Catch:{ all -> 0x010c }
                    r1.clear()     // Catch:{ all -> 0x010c }
                    r1 = 1
                    boolean r3 = com.taobao.orange.util.OLog.isPrintLog(r1)     // Catch:{ all -> 0x010c }
                    if (r3 == 0) goto L_0x004a
                    java.lang.String r3 = "ConfigCenter"
                    java.lang.String r4 = "rematchNamespace"
                    java.lang.Object[] r5 = new java.lang.Object[r1]     // Catch:{ all -> 0x010c }
                    java.lang.String r6 = "<<<<<<<<<<<<<< START >>>>>>>>>>>>>>"
                    r5[r2] = r6     // Catch:{ all -> 0x010c }
                    com.taobao.orange.util.OLog.d(r3, r4, r5)     // Catch:{ all -> 0x010c }
                L_0x004a:
                    boolean r3 = com.taobao.orange.util.OLog.isPrintLog(r1)     // Catch:{ all -> 0x010c }
                    r4 = 2
                    if (r3 == 0) goto L_0x0062
                    java.lang.String r3 = "ConfigCenter"
                    java.lang.String r5 = "rematchNamespace"
                    java.lang.Object[] r6 = new java.lang.Object[r4]     // Catch:{ all -> 0x010c }
                    java.lang.String r7 = "candidateKeys"
                    r6[r2] = r7     // Catch:{ all -> 0x010c }
                    java.util.Set r7 = r2     // Catch:{ all -> 0x010c }
                    r6[r1] = r7     // Catch:{ all -> 0x010c }
                    com.taobao.orange.util.OLog.d(r3, r5, r6)     // Catch:{ all -> 0x010c }
                L_0x0062:
                    java.util.HashSet r3 = new java.util.HashSet     // Catch:{ all -> 0x010c }
                    r3.<init>()     // Catch:{ all -> 0x010c }
                    java.util.Set r5 = r2     // Catch:{ all -> 0x010c }
                    java.util.Iterator r5 = r5.iterator()     // Catch:{ all -> 0x010c }
                L_0x006d:
                    boolean r6 = r5.hasNext()     // Catch:{ all -> 0x010c }
                    if (r6 == 0) goto L_0x008b
                    java.lang.Object r6 = r5.next()     // Catch:{ all -> 0x010c }
                    java.lang.String r6 = (java.lang.String) r6     // Catch:{ all -> 0x010c }
                    com.taobao.orange.ConfigCenter r7 = com.taobao.orange.ConfigCenter.this     // Catch:{ all -> 0x010c }
                    com.taobao.orange.cache.IndexCache r7 = r7.mIndexCache     // Catch:{ all -> 0x010c }
                    java.util.Map<java.lang.String, java.util.Set<java.lang.String>> r7 = r7.candidateNamespace     // Catch:{ all -> 0x010c }
                    java.lang.Object r6 = r7.get(r6)     // Catch:{ all -> 0x010c }
                    java.util.Set r6 = (java.util.Set) r6     // Catch:{ all -> 0x010c }
                    if (r6 == 0) goto L_0x006d
                    r3.addAll(r6)     // Catch:{ all -> 0x010c }
                    goto L_0x006d
                L_0x008b:
                    boolean r5 = com.taobao.orange.util.OLog.isPrintLog(r1)     // Catch:{ all -> 0x010c }
                    if (r5 == 0) goto L_0x00a0
                    java.lang.String r5 = "ConfigCenter"
                    java.lang.String r6 = "rematchNamespace"
                    java.lang.Object[] r7 = new java.lang.Object[r4]     // Catch:{ all -> 0x010c }
                    java.lang.String r8 = "specialNamespaces"
                    r7[r2] = r8     // Catch:{ all -> 0x010c }
                    r7[r1] = r3     // Catch:{ all -> 0x010c }
                    com.taobao.orange.util.OLog.d(r5, r6, r7)     // Catch:{ all -> 0x010c }
                L_0x00a0:
                    java.util.Iterator r3 = r3.iterator()     // Catch:{ all -> 0x010c }
                L_0x00a4:
                    boolean r5 = r3.hasNext()     // Catch:{ all -> 0x010c }
                    if (r5 == 0) goto L_0x00dc
                    java.lang.Object r5 = r3.next()     // Catch:{ all -> 0x010c }
                    java.lang.String r5 = (java.lang.String) r5     // Catch:{ all -> 0x010c }
                    com.taobao.orange.ConfigCenter r6 = com.taobao.orange.ConfigCenter.this     // Catch:{ all -> 0x010c }
                    com.taobao.orange.cache.ConfigCache r6 = r6.mConfigCache     // Catch:{ all -> 0x010c }
                    java.util.Map r6 = r6.getConfigMap()     // Catch:{ all -> 0x010c }
                    boolean r6 = r6.containsKey(r5)     // Catch:{ all -> 0x010c }
                    if (r6 != 0) goto L_0x00ce
                    java.lang.String r6 = "ConfigCenter"
                    java.lang.String r7 = "rematchNamespace break as not used DEFAULT"
                    java.lang.Object[] r8 = new java.lang.Object[r4]     // Catch:{ all -> 0x010c }
                    java.lang.String r9 = "namespace"
                    r8[r2] = r9     // Catch:{ all -> 0x010c }
                    r8[r1] = r5     // Catch:{ all -> 0x010c }
                    com.taobao.orange.util.OLog.w(r6, r7, r8)     // Catch:{ all -> 0x010c }
                    goto L_0x00a4
                L_0x00ce:
                    com.taobao.orange.ConfigCenter r6 = com.taobao.orange.ConfigCenter.this     // Catch:{ all -> 0x010c }
                    com.taobao.orange.ConfigCenter r7 = com.taobao.orange.ConfigCenter.this     // Catch:{ all -> 0x010c }
                    com.taobao.orange.cache.IndexCache r7 = r7.mIndexCache     // Catch:{ all -> 0x010c }
                    com.taobao.orange.model.NameSpaceDO r5 = r7.getNameSpace(r5)     // Catch:{ all -> 0x010c }
                    r6.loadConfig(r5)     // Catch:{ all -> 0x010c }
                    goto L_0x00a4
                L_0x00dc:
                    boolean r3 = com.taobao.orange.util.OLog.isPrintLog(r1)     // Catch:{ all -> 0x010c }
                    if (r3 == 0) goto L_0x00ef
                    java.lang.String r3 = "ConfigCenter"
                    java.lang.String r4 = "rematchNamespace"
                    java.lang.Object[] r1 = new java.lang.Object[r1]     // Catch:{ all -> 0x010c }
                    java.lang.String r5 = "<<<<<<<<<<<<<< END >>>>>>>>>>>>>>"
                    r1[r2] = r5     // Catch:{ all -> 0x010c }
                    com.taobao.orange.util.OLog.d(r3, r4, r1)     // Catch:{ all -> 0x010c }
                L_0x00ef:
                    monitor-exit(r0)     // Catch:{ all -> 0x010c }
                    return
                L_0x00f1:
                    r1 = 3
                    boolean r1 = com.taobao.orange.util.OLog.isPrintLog(r1)     // Catch:{ all -> 0x010c }
                    if (r1 == 0) goto L_0x0101
                    java.lang.String r1 = "ConfigCenter"
                    java.lang.String r3 = "rematchNamespace fail"
                    java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ all -> 0x010c }
                    com.taobao.orange.util.OLog.w(r1, r3, r2)     // Catch:{ all -> 0x010c }
                L_0x0101:
                    com.taobao.orange.ConfigCenter r1 = com.taobao.orange.ConfigCenter.this     // Catch:{ all -> 0x010c }
                    java.util.Set<java.lang.String> r1 = r1.failCandidateSet     // Catch:{ all -> 0x010c }
                    java.util.Set r2 = r2     // Catch:{ all -> 0x010c }
                    r1.addAll(r2)     // Catch:{ all -> 0x010c }
                    monitor-exit(r0)     // Catch:{ all -> 0x010c }
                    return
                L_0x010c:
                    r1 = move-exception
                    monitor-exit(r0)     // Catch:{ all -> 0x010c }
                    throw r1
                */
                throw new UnsupportedOperationException("Method not decompiled: com.taobao.orange.ConfigCenter.AnonymousClass9.run():void");
            }
        });
    }

    public void delayLoadConfig() {
        OLog.d(TAG, "delayLoadConfig", new Object[0]);
        if (!this.mIsOrangeInit.get()) {
            this.isAfterIdle.compareAndSet(false, true);
            OLog.w(TAG, "delayLoadConfig fail as not finish orange init", new Object[0]);
        } else if (this.isAfterIdle.compareAndSet(false, true)) {
            OrangeMonitorData orangeMonitorData = new OrangeMonitorData();
            orangeMonitorData.performance.bootType = this.mIsFirstInstall;
            orangeMonitorData.performance.downgradeType = GlobalOrange.downgrade;
            orangeMonitorData.performance.monitorType = 0;
            orangeMonitorData.performance.requestCount = this.mRequestCount.get();
            orangeMonitorData.performance.persistCount = FileUtil.persistCount.get();
            orangeMonitorData.performance.restoreCount = FileUtil.restoreCount.get();
            orangeMonitorData.performance.persistTime = FileUtil.persistTime.get();
            orangeMonitorData.performance.restoreTime = FileUtil.restoreTime.get();
            orangeMonitorData.performance.ioTime = FileUtil.ioTime.get();
            if (getConfigWaitingNetworkQueue() != null) {
                OThreadFactory.execute(new Runnable() {
                    public void run() {
                        HashSet<NameSpaceDO> hashSet = new HashSet<>();
                        while (!ConfigCenter.this.getConfigWaitingNetworkQueue().isEmpty()) {
                            NameSpaceDO poll = ConfigCenter.this.getConfigWaitingNetworkQueue().poll();
                            if (poll != null) {
                                hashSet.add(poll);
                            }
                        }
                        for (NameSpaceDO nameSpaceDO : hashSet) {
                            if (nameSpaceDO != null) {
                                if (OLog.isPrintLog(0)) {
                                    OLog.d(ConfigCenter.TAG, "idle load config", "namespace", nameSpaceDO.name);
                                }
                                ConfigCenter.this.loadConfig(nameSpaceDO);
                            }
                        }
                    }
                });
            }
            for (String str : getConfigCache().getConfigMap().keySet()) {
                final ConfigDO configDO = getConfigCache().getConfigMap().get(str);
                if (configDO != null && !configDO.persisted) {
                    OThreadFactory.executeDisk(new Runnable() {
                        public void run() {
                            if (OLog.isPrintLog(0)) {
                                OLog.d(ConfigCenter.TAG, "idle persist config", "namespace", configDO.name);
                            }
                            configDO.persisted = true;
                            FileUtil.persistObject(configDO, configDO.name);
                        }
                    });
                }
            }
            OrangeMonitor.commitBootPerformanceInfo(orangeMonitorData);
        }
    }

    public JSONObject getIndexAndConfigs() {
        try {
            HashMap hashMap = new HashMap();
            hashMap.put("index", getIndex());
            hashMap.put(BindingXConstants.KEY_CONFIG, getAllConfigs());
            return new JSONObject(hashMap);
        } catch (Exception e) {
            OLog.e(TAG, "getIndexAndConfigs", e, new Object[0]);
            return null;
        }
    }

    public JSONObject getAllConfigs() {
        try {
            return new JSONObject(JSON.toJSONString(OrangeUtils.sortMapByKey(this.mConfigCache.getConfigMap(), true)));
        } catch (Exception e) {
            OLog.e(TAG, "getAllConfigs", e, new Object[0]);
            return null;
        }
    }

    public JSONObject getIndex() {
        try {
            IndexDO indexDO = new IndexDO(this.mIndexCache.getIndex());
            Collections.sort(indexDO.mergedNamespaces, new Comparator<NameSpaceDO>() {
                public int compare(NameSpaceDO nameSpaceDO, NameSpaceDO nameSpaceDO2) {
                    return nameSpaceDO.name.compareTo(nameSpaceDO2.name);
                }
            });
            return new JSONObject(JSON.toJSONString(indexDO));
        } catch (Exception e) {
            OLog.e(TAG, "getIndex", e, new Object[0]);
            return null;
        }
    }
}
