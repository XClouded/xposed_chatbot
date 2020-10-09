package mtopsdk.mtop.intf;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import anetwork.network.cache.Cache;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import mtopsdk.common.util.MtopUtils;
import mtopsdk.common.util.StringUtils;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.mtop.domain.EnvModeEnum;
import mtopsdk.mtop.domain.IMTOPDataObject;
import mtopsdk.mtop.domain.MtopRequest;
import mtopsdk.mtop.global.MtopConfig;
import mtopsdk.mtop.global.init.IMtopInitTask;
import mtopsdk.mtop.global.init.MtopInitTaskFactory;
import mtopsdk.mtop.intf.MtopPrefetch;
import mtopsdk.mtop.util.MtopSDKThreadPoolExecutorFactory;
import mtopsdk.xstate.XState;

public class Mtop {
    private static final int MAX_PREFETCH_LENGTH = 50;
    private static final String TAG = "mtopsdk.Mtop";
    protected static final Map<String, Mtop> instanceMap = new ConcurrentHashMap();
    public static boolean mIsFullTrackValid = false;
    final byte[] initLock = new byte[0];
    final IMtopInitTask initTask;
    volatile String instanceId;
    private volatile boolean isInit = false;
    volatile boolean isInited = false;
    public volatile long lastPrefetchResponseTime = System.currentTimeMillis();
    final MtopConfig mtopConfig;
    private Map<String, MtopBuilder> prefetchBuilderMap = new ConcurrentHashMap();

    public interface Id {
        public static final String INNER = "INNER";
        public static final String OPEN = "OPEN";
        public static final String PRODUCT = "PRODUCT";

        @Retention(RetentionPolicy.SOURCE)
        public @interface Definition {
        }
    }

    private Mtop(String str, @NonNull MtopConfig mtopConfig2) {
        this.instanceId = str;
        this.mtopConfig = mtopConfig2;
        this.initTask = MtopInitTaskFactory.getMtopInitTask(str);
        if (this.initTask != null) {
            try {
                Class.forName("com.taobao.analysis.fulltrace.FullTraceAnalysis");
                Class.forName("com.taobao.analysis.scene.SceneIdentifier");
                Class.forName("com.taobao.analysis.abtest.ABTestCenter");
                mIsFullTrackValid = true;
            } catch (Throwable unused) {
                mIsFullTrackValid = false;
            }
        } else {
            throw new RuntimeException("IMtopInitTask is null,instanceId=" + str);
        }
    }

    @Deprecated
    public static Mtop instance(Context context) {
        return instance((String) null, context, (String) null);
    }

    @Deprecated
    public static Mtop instance(Context context, String str) {
        return instance((String) null, context, str);
    }

    public static Mtop instance(String str, @NonNull Context context) {
        return instance(str, context, (String) null);
    }

    public static Mtop instance(String str, @NonNull Context context, String str2) {
        if (!StringUtils.isNotBlank(str)) {
            str = Id.INNER;
        }
        Mtop mtop = instanceMap.get(str);
        if (mtop == null) {
            synchronized (Mtop.class) {
                mtop = instanceMap.get(str);
                if (mtop == null) {
                    MtopConfig mtopConfig2 = MtopSetting.mtopConfigMap.get(str);
                    if (mtopConfig2 == null) {
                        mtopConfig2 = new MtopConfig(str);
                    }
                    Mtop mtop2 = new Mtop(str, mtopConfig2);
                    mtopConfig2.mtopInstance = mtop2;
                    instanceMap.put(str, mtop2);
                    mtop = mtop2;
                }
            }
        }
        if (!mtop.isInit) {
            mtop.init(context, str2);
        }
        return mtop;
    }

    public static Mtop getMtopInstance(String str) {
        if (!StringUtils.isNotBlank(str)) {
            str = Id.INNER;
        }
        return instanceMap.get(str);
    }

    public String getInstanceId() {
        return this.instanceId;
    }

    public MtopConfig getMtopConfig() {
        return this.mtopConfig;
    }

    private synchronized void init(Context context, String str) {
        if (!this.isInit) {
            if (context == null) {
                TBSdkLog.e(TAG, this.instanceId + " [init] The Parameter context can not be null.");
                return;
            }
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                TBSdkLog.i(TAG, this.instanceId + " [init] context=" + context + ", ttid=" + str);
            }
            this.mtopConfig.context = context.getApplicationContext();
            if (StringUtils.isNotBlank(str)) {
                this.mtopConfig.ttid = str;
            }
            MtopSDKThreadPoolExecutorFactory.submit(new Runnable() {
                public void run() {
                    try {
                        synchronized (Mtop.this.initLock) {
                            long currentTimeMillis = System.currentTimeMillis();
                            try {
                                Mtop.this.updateAppKeyIndex();
                                Mtop.this.initTask.executeCoreTask(Mtop.this.mtopConfig);
                                MtopSDKThreadPoolExecutorFactory.submit(new Runnable() {
                                    public void run() {
                                        try {
                                            Mtop.this.initTask.executeExtraTask(Mtop.this.mtopConfig);
                                        } catch (Throwable th) {
                                            TBSdkLog.e(Mtop.TAG, Mtop.this.instanceId + " [init] executeExtraTask error.", th);
                                        }
                                    }
                                });
                                TBSdkLog.i(Mtop.TAG, Mtop.this.instanceId + " [init]do executeCoreTask cost[ms]: " + (System.currentTimeMillis() - currentTimeMillis));
                                Mtop.this.isInited = true;
                                Mtop.this.initLock.notifyAll();
                            } catch (Throwable th) {
                                TBSdkLog.i(Mtop.TAG, Mtop.this.instanceId + " [init]do executeCoreTask cost[ms]: " + (System.currentTimeMillis() - currentTimeMillis));
                                Mtop.this.isInited = true;
                                Mtop.this.initLock.notifyAll();
                                throw th;
                            }
                        }
                    } catch (Exception e) {
                        TBSdkLog.e(Mtop.TAG, Mtop.this.instanceId + " [init] executeCoreTask error.", (Throwable) e);
                    }
                }
            });
            this.isInit = true;
        }
    }

    public void unInit() {
        this.isInited = false;
        this.isInit = false;
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            TBSdkLog.i(TAG, this.instanceId + "[unInit] MTOPSDK unInit called");
        }
    }

    /* access modifiers changed from: package-private */
    public void updateAppKeyIndex() {
        EnvModeEnum envModeEnum = this.mtopConfig.envMode;
        if (envModeEnum != null) {
            switch (envModeEnum) {
                case ONLINE:
                case PREPARE:
                    this.mtopConfig.appKeyIndex = this.mtopConfig.onlineAppKeyIndex;
                    return;
                case TEST:
                case TEST_SANDBOX:
                    this.mtopConfig.appKeyIndex = this.mtopConfig.dailyAppkeyIndex;
                    return;
                default:
                    return;
            }
        }
    }

    public Mtop switchEnvMode(final EnvModeEnum envModeEnum) {
        if (envModeEnum == null || this.mtopConfig.envMode == envModeEnum) {
            return this;
        }
        if (MtopUtils.isApkDebug(this.mtopConfig.context) || this.mtopConfig.isAllowSwitchEnv.compareAndSet(true, false)) {
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                TBSdkLog.i(TAG, this.instanceId + " [switchEnvMode]MtopSDK switchEnvMode called.envMode=" + envModeEnum);
            }
            MtopSDKThreadPoolExecutorFactory.submit(new Runnable() {
                public void run() {
                    Mtop.this.checkMtopSDKInit();
                    if (Mtop.this.mtopConfig.envMode == envModeEnum) {
                        TBSdkLog.i(Mtop.TAG, Mtop.this.instanceId + " [switchEnvMode] Current EnvMode matches target EnvMode,envMode=" + envModeEnum);
                        return;
                    }
                    if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                        TBSdkLog.i(Mtop.TAG, Mtop.this.instanceId + " [switchEnvMode]MtopSDK switchEnvMode start");
                    }
                    Mtop.this.mtopConfig.envMode = envModeEnum;
                    try {
                        Mtop.this.updateAppKeyIndex();
                        if (EnvModeEnum.ONLINE == envModeEnum) {
                            TBSdkLog.setPrintLog(false);
                        }
                        Mtop.this.initTask.executeCoreTask(Mtop.this.mtopConfig);
                        Mtop.this.initTask.executeExtraTask(Mtop.this.mtopConfig);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                        TBSdkLog.i(Mtop.TAG, Mtop.this.instanceId + " [switchEnvMode]MtopSDK switchEnvMode end. envMode =" + envModeEnum);
                    }
                }
            });
            return this;
        }
        TBSdkLog.e(TAG, this.instanceId + " [switchEnvMode]release package can switch environment only once!");
        return this;
    }

    public boolean checkMtopSDKInit() {
        if (this.isInited) {
            return this.isInited;
        }
        synchronized (this.initLock) {
            try {
                if (!this.isInited) {
                    this.initLock.wait(60000);
                    if (!this.isInited) {
                        TBSdkLog.e(TAG, this.instanceId + " [checkMtopSDKInit]Didn't call Mtop.instance(...),please execute global init.");
                    }
                }
            } catch (Exception e) {
                TBSdkLog.e(TAG, this.instanceId + " [checkMtopSDKInit] wait Mtop initLock failed---" + e.toString());
            }
        }
        return this.isInited;
    }

    public boolean isInited() {
        return this.isInited;
    }

    @Deprecated
    public Mtop registerSessionInfo(String str, @Deprecated String str2, String str3) {
        return registerMultiAccountSession((String) null, str, str3);
    }

    @Deprecated
    public static void setAppKeyIndex(int i, int i2) {
        MtopSetting.setAppKeyIndex(i, i2);
    }

    @Deprecated
    public static void setAppVersion(String str) {
        MtopSetting.setAppVersion(str);
    }

    @Deprecated
    public static void setSecurityAppKey(String str) {
        MtopSetting.setSecurityAppKey(str);
    }

    @Deprecated
    public static void setMtopDomain(String str, String str2, String str3) {
        MtopSetting.setMtopDomain(str, str2, str3);
    }

    public Mtop registerSessionInfo(String str, String str2) {
        return registerMultiAccountSession((String) null, str, str2);
    }

    public Mtop logout() {
        return logoutMultiAccountSession((String) null);
    }

    public Mtop registerMultiAccountSession(@Nullable String str, String str2, String str3) {
        String str4 = this.instanceId;
        if (StringUtils.isBlank(str)) {
            str = "DEFAULT";
        }
        String concatStr = StringUtils.concatStr(str4, str);
        XState.setValue(concatStr, "sid", str2);
        XState.setValue(concatStr, "uid", str3);
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            StringBuilder sb = new StringBuilder(64);
            sb.append(concatStr);
            sb.append(" [registerSessionInfo]register sessionInfo succeed: sid=");
            sb.append(str2);
            sb.append(",uid=");
            sb.append(str3);
            TBSdkLog.i(TAG, sb.toString());
        }
        if (this.mtopConfig.networkPropertyService != null) {
            this.mtopConfig.networkPropertyService.setUserId(str3);
        }
        return this;
    }

    public Mtop logoutMultiAccountSession(@Nullable String str) {
        String str2 = this.instanceId;
        if (StringUtils.isBlank(str)) {
            str = "DEFAULT";
        }
        String concatStr = StringUtils.concatStr(str2, str);
        XState.removeKey(concatStr, "sid");
        XState.removeKey(concatStr, "uid");
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            StringBuilder sb = new StringBuilder(32);
            sb.append(concatStr);
            sb.append(" [logout] remove sessionInfo succeed.");
            TBSdkLog.i(TAG, sb.toString());
        }
        if (this.mtopConfig.networkPropertyService != null) {
            this.mtopConfig.networkPropertyService.setUserId((String) null);
        }
        return this;
    }

    public Mtop registerTtid(String str) {
        if (str != null) {
            this.mtopConfig.ttid = str;
            XState.setValue(this.instanceId, "ttid", str);
            if (this.mtopConfig.networkPropertyService != null) {
                this.mtopConfig.networkPropertyService.setTtid(str);
            }
        }
        return this;
    }

    public Mtop registerUtdid(String str) {
        if (str != null) {
            this.mtopConfig.utdid = str;
            XState.setValue("utdid", str);
        }
        return this;
    }

    public Mtop registerDeviceId(String str) {
        if (str != null) {
            this.mtopConfig.deviceId = str;
            XState.setValue(this.instanceId, "deviceId", str);
        }
        return this;
    }

    @Deprecated
    public String getSid() {
        return getMultiAccountSid((String) null);
    }

    public String getMultiAccountSid(String str) {
        String str2 = this.instanceId;
        if (StringUtils.isBlank(str)) {
            str = "DEFAULT";
        }
        return XState.getValue(StringUtils.concatStr(str2, str), "sid");
    }

    @Deprecated
    public String getUserId() {
        return getMultiAccountUserId((String) null);
    }

    public String getMultiAccountUserId(String str) {
        String str2 = this.instanceId;
        if (StringUtils.isBlank(str)) {
            str = "DEFAULT";
        }
        return XState.getValue(StringUtils.concatStr(str2, str), "uid");
    }

    public String getTtid() {
        return XState.getValue(this.instanceId, "ttid");
    }

    public String getDeviceId() {
        return XState.getValue(this.instanceId, "deviceId");
    }

    public String getUtdid() {
        return XState.getValue("utdid");
    }

    public Mtop setCoordinates(String str, String str2) {
        XState.setValue("lng", str);
        XState.setValue("lat", str2);
        return this;
    }

    public boolean removeCacheBlock(String str) {
        Cache cache = this.mtopConfig.cacheImpl;
        return cache != null && cache.remove(str);
    }

    public boolean unintallCacheBlock(String str) {
        Cache cache = this.mtopConfig.cacheImpl;
        return cache != null && cache.uninstall(str);
    }

    public boolean removeCacheItem(String str, String str2) {
        if (StringUtils.isBlank(str2)) {
            TBSdkLog.e(TAG, "[removeCacheItem] remove CacheItem failed,invalid cacheKey=" + str2);
            return false;
        }
        Cache cache = this.mtopConfig.cacheImpl;
        if (cache == null || !cache.remove(str, str2)) {
            return false;
        }
        return true;
    }

    public Mtop logSwitch(boolean z) {
        TBSdkLog.setPrintLog(z);
        return this;
    }

    public MtopBuilder build(IMTOPDataObject iMTOPDataObject, String str) {
        return new MtopBuilder(this, iMTOPDataObject, str);
    }

    public MtopBuilder build(MtopRequest mtopRequest, String str) {
        return new MtopBuilder(this, mtopRequest, str);
    }

    @Deprecated
    public MtopBuilder build(Object obj, String str) {
        return new MtopBuilder(this, obj, str);
    }

    public Map<String, MtopBuilder> getPrefetchBuilderMap() {
        return this.prefetchBuilderMap;
    }

    public void addPrefetchBuilderToMap(@NonNull MtopBuilder mtopBuilder, String str) {
        if (this.prefetchBuilderMap.size() >= 50) {
            MtopPrefetch.cleanPrefetchCache(mtopBuilder.mtopInstance);
        }
        if (this.prefetchBuilderMap.size() >= 50) {
            MtopPrefetch.onPrefetchAndCommit(MtopPrefetch.IPrefetchCallback.PrefetchCallbackType.TYPE_FULL, mtopBuilder.getMtopPrefetch(), mtopBuilder.getMtopContext(), (HashMap<String, String>) null);
        }
        this.prefetchBuilderMap.put(str, mtopBuilder);
    }
}
