package mtopsdk.mtop.intf;

import androidx.annotation.NonNull;
import anetwork.network.cache.Cache;
import java.util.HashMap;
import java.util.Map;
import mtopsdk.common.config.MtopConfigListener;
import mtopsdk.common.log.LogAdapter;
import mtopsdk.common.util.StringUtils;
import mtopsdk.common.util.SwitchConfigUtil;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.mtop.antiattack.AntiAttackHandler;
import mtopsdk.mtop.common.MtopStatsListener;
import mtopsdk.mtop.domain.EnvModeEnum;
import mtopsdk.mtop.features.MtopFeatureManager;
import mtopsdk.mtop.global.MtopConfig;
import mtopsdk.mtop.global.SwitchConfig;
import mtopsdk.mtop.stat.IUploadStats;
import mtopsdk.mtop.util.MtopSDKThreadPoolExecutorFactory;
import mtopsdk.network.Call;
import mtopsdk.security.ISign;

public final class MtopSetting {
    private static final String TAG = "mtopsdk.MtopSetting";
    protected static final Map<String, MtopConfig> mtopConfigMap = new HashMap();

    @Deprecated
    public static void setSecurityAppKey(String str) {
    }

    private MtopSetting() {
    }

    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0048, code lost:
        return r0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static mtopsdk.mtop.global.MtopConfig getMtopConfigByID(java.lang.String r4) {
        /*
            boolean r0 = mtopsdk.common.util.StringUtils.isNotBlank(r4)
            if (r0 == 0) goto L_0x0007
            goto L_0x0009
        L_0x0007:
            java.lang.String r4 = "INNER"
        L_0x0009:
            java.util.Map<java.lang.String, mtopsdk.mtop.intf.Mtop> r0 = mtopsdk.mtop.intf.Mtop.instanceMap
            java.lang.Object r0 = r0.get(r4)
            mtopsdk.mtop.intf.Mtop r0 = (mtopsdk.mtop.intf.Mtop) r0
            if (r0 != 0) goto L_0x004e
            java.lang.Class<mtopsdk.mtop.intf.Mtop> r1 = mtopsdk.mtop.intf.Mtop.class
            monitor-enter(r1)
            java.util.Map<java.lang.String, mtopsdk.mtop.intf.Mtop> r0 = mtopsdk.mtop.intf.Mtop.instanceMap     // Catch:{ all -> 0x004b }
            java.lang.Object r0 = r0.get(r4)     // Catch:{ all -> 0x004b }
            mtopsdk.mtop.intf.Mtop r0 = (mtopsdk.mtop.intf.Mtop) r0     // Catch:{ all -> 0x004b }
            if (r0 != 0) goto L_0x0049
            java.util.Map<java.lang.String, mtopsdk.mtop.global.MtopConfig> r0 = mtopConfigMap     // Catch:{ all -> 0x004b }
            java.lang.Object r0 = r0.get(r4)     // Catch:{ all -> 0x004b }
            mtopsdk.mtop.global.MtopConfig r0 = (mtopsdk.mtop.global.MtopConfig) r0     // Catch:{ all -> 0x004b }
            if (r0 != 0) goto L_0x0047
            java.lang.Class<mtopsdk.mtop.intf.MtopSetting> r0 = mtopsdk.mtop.intf.MtopSetting.class
            monitor-enter(r0)     // Catch:{ all -> 0x004b }
            java.util.Map<java.lang.String, mtopsdk.mtop.global.MtopConfig> r2 = mtopConfigMap     // Catch:{ all -> 0x0044 }
            java.lang.Object r2 = r2.get(r4)     // Catch:{ all -> 0x0044 }
            mtopsdk.mtop.global.MtopConfig r2 = (mtopsdk.mtop.global.MtopConfig) r2     // Catch:{ all -> 0x0044 }
            if (r2 != 0) goto L_0x0041
            mtopsdk.mtop.global.MtopConfig r2 = new mtopsdk.mtop.global.MtopConfig     // Catch:{ all -> 0x0044 }
            r2.<init>(r4)     // Catch:{ all -> 0x0044 }
            java.util.Map<java.lang.String, mtopsdk.mtop.global.MtopConfig> r3 = mtopConfigMap     // Catch:{ all -> 0x0044 }
            r3.put(r4, r2)     // Catch:{ all -> 0x0044 }
        L_0x0041:
            monitor-exit(r0)     // Catch:{ all -> 0x0044 }
            r0 = r2
            goto L_0x0047
        L_0x0044:
            r4 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0044 }
            throw r4     // Catch:{ all -> 0x004b }
        L_0x0047:
            monitor-exit(r1)     // Catch:{ all -> 0x004b }
            return r0
        L_0x0049:
            monitor-exit(r1)     // Catch:{ all -> 0x004b }
            goto L_0x004e
        L_0x004b:
            r4 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x004b }
            throw r4
        L_0x004e:
            mtopsdk.mtop.global.MtopConfig r4 = r0.getMtopConfig()
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: mtopsdk.mtop.intf.MtopSetting.getMtopConfigByID(java.lang.String):mtopsdk.mtop.global.MtopConfig");
    }

    public static void setAppKeyIndex(String str, int i, int i2) {
        MtopConfig mtopConfigByID = getMtopConfigByID(str);
        mtopConfigByID.onlineAppKeyIndex = i;
        mtopConfigByID.dailyAppkeyIndex = i2;
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            TBSdkLog.i(TAG, mtopConfigByID.instanceId + " [setAppKeyIndex] onlineAppKeyIndex=" + i + ",dailyAppkeyIndex=" + i2);
        }
    }

    @Deprecated
    public static void setAppKeyIndex(int i, int i2) {
        setAppKeyIndex((String) null, i, i2);
    }

    public static void setAuthCode(String str, String str2) {
        MtopConfig mtopConfigByID = getMtopConfigByID(str);
        mtopConfigByID.authCode = str2;
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            TBSdkLog.i(TAG, mtopConfigByID.instanceId + " [setAuthCode] authCode=" + str2);
        }
    }

    @Deprecated
    public static void setAuthCode(String str) {
        setAuthCode((String) null, str);
    }

    public static void setWuaAuthCode(String str, String str2) {
        MtopConfig mtopConfigByID = getMtopConfigByID(str);
        mtopConfigByID.wuaAuthCode = str2;
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            TBSdkLog.i(TAG, mtopConfigByID.instanceId + " [setWuaAuthCode] wuaAuthCode=" + str2);
        }
    }

    public static void setAppVersion(String str, String str2) {
        MtopConfig mtopConfigByID = getMtopConfigByID(str);
        mtopConfigByID.appVersion = str2;
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            TBSdkLog.i(TAG, mtopConfigByID.instanceId + " [setAppVersion] appVersion=" + str2);
        }
    }

    public static void setAppKey(String str, String str2) {
        MtopConfig mtopConfigByID = getMtopConfigByID(str);
        mtopConfigByID.appKey = str2;
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            TBSdkLog.i(TAG, mtopConfigByID.instanceId + " [setAppKey] appKey=" + str2);
        }
    }

    @Deprecated
    public static void setAppVersion(String str) {
        setAppVersion((String) null, str);
    }

    public static void setMtopDomain(String str, String str2, String str3, String str4) {
        MtopConfig mtopConfigByID = getMtopConfigByID(str);
        if (StringUtils.isNotBlank(str2)) {
            mtopConfigByID.mtopDomain.updateDomain(EnvModeEnum.ONLINE, str2);
        }
        if (StringUtils.isNotBlank(str3)) {
            mtopConfigByID.mtopDomain.updateDomain(EnvModeEnum.PREPARE, str3);
        }
        if (StringUtils.isNotBlank(str4)) {
            mtopConfigByID.mtopDomain.updateDomain(EnvModeEnum.TEST, str4);
        }
    }

    @Deprecated
    public static void setMtopDomain(String str, String str2, String str3) {
        setMtopDomain((String) null, str, str2, str3);
    }

    @Deprecated
    public static void setMtopFeatureFlag(MtopFeatureManager.MtopFeatureEnum mtopFeatureEnum, boolean z) {
        setMtopFeatureFlag((String) null, MtopFeatureManager.getMtopFeatureByFeatureEnum(mtopFeatureEnum), z);
    }

    public static void setMtopFeatureFlag(String str, int i, boolean z) {
        if (i >= 1) {
            MtopConfig mtopConfigByID = getMtopConfigByID(str);
            if (z) {
                mtopConfigByID.mtopFeatures.add(Integer.valueOf(i));
            } else {
                mtopConfigByID.mtopFeatures.remove(Integer.valueOf(i));
            }
        }
    }

    public static void setAntiAttackHandler(String str, AntiAttackHandler antiAttackHandler) {
        MtopConfig mtopConfigByID = getMtopConfigByID(str);
        mtopConfigByID.antiAttackHandler = antiAttackHandler;
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            TBSdkLog.i(TAG, mtopConfigByID.instanceId + " [setAntiAttackHandler] set antiAttackHandler succeed.");
        }
    }

    @Deprecated
    public static void setXOrangeQ(String str) {
        setXOrangeQ((String) null, str);
    }

    public static void setXOrangeQ(String str, String str2) {
        if (StringUtils.isNotBlank(str2)) {
            MtopConfig mtopConfigByID = getMtopConfigByID(str);
            mtopConfigByID.xOrangeQ = str2;
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                TBSdkLog.i(TAG, mtopConfigByID.instanceId + " [setXOrangeQ] set xOrangeQ succeed.xOrangeQ=" + str2);
            }
        }
    }

    public static void setMtopConfigListener(final MtopConfigListener mtopConfigListener) {
        SwitchConfig.getInstance().setMtopConfigListener(mtopConfigListener);
        SwitchConfigUtil.setMtopConfigListener(mtopConfigListener);
        TBSdkLog.i(TAG, "[setMtopConfigListener] set MtopConfigListener succeed.");
        MtopSDKThreadPoolExecutorFactory.submit(new Runnable() {
            public void run() {
                if (mtopConfigListener != null) {
                    MtopConfig mtopConfigByID = MtopSetting.getMtopConfigByID((String) null);
                    if (mtopConfigByID.context != null) {
                        mtopConfigListener.initConfig(mtopConfigByID.context);
                    }
                }
            }
        });
    }

    public static void setLogAdapterImpl(LogAdapter logAdapter) {
        if (logAdapter != null) {
            MtopConfig.logAdapterImpl = logAdapter;
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                TBSdkLog.i(TAG, "[setLogAdapterImpl] set logAdapter succeed.logAdapterImpl=" + logAdapter);
            }
        }
    }

    @Deprecated
    public static void setCacheImpl(Cache cache) {
        setCacheImpl((String) null, cache);
    }

    public static void setCacheImpl(String str, Cache cache) {
        if (cache != null) {
            MtopConfig mtopConfigByID = getMtopConfigByID(str);
            mtopConfigByID.cacheImpl = cache;
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                TBSdkLog.i(TAG, mtopConfigByID.instanceId + " [setCacheImpl] set CacheImpl succeed.cacheImpl=" + cache);
            }
        }
    }

    public static void setISignImpl(String str, ISign iSign) {
        MtopConfig mtopConfigByID = getMtopConfigByID(str);
        mtopConfigByID.sign = iSign;
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            TBSdkLog.i(TAG, mtopConfigByID.instanceId + "[setISignImpl] set ISign succeed.signImpl=" + iSign);
        }
    }

    public static void setUploadStats(String str, IUploadStats iUploadStats) {
        MtopConfig mtopConfigByID = getMtopConfigByID(str);
        mtopConfigByID.uploadStats = iUploadStats;
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            TBSdkLog.i(TAG, mtopConfigByID.instanceId + "[setUploadStats] set IUploadStats succeed.uploadStats=" + iUploadStats);
        }
    }

    public static void setCallFactoryImpl(String str, Call.Factory factory) {
        if (factory != null) {
            MtopConfig mtopConfigByID = getMtopConfigByID(str);
            mtopConfigByID.callFactory = factory;
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                TBSdkLog.i(TAG, mtopConfigByID.instanceId + "[setCallFactoryImpl] set CallFactoryImpl succeed.callFactory=" + factory);
            }
        }
    }

    public static void setEnableProperty(String str, String str2, boolean z) {
        if (str2 != null) {
            MtopConfig mtopConfigByID = getMtopConfigByID(str);
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                TBSdkLog.i(TAG, mtopConfigByID.instanceId + "[setEnableProperty] set enableProperty succeed.property=" + str2 + ",enable=" + z);
            }
            char c = 65535;
            int hashCode = str2.hashCode();
            if (hashCode != -514993282) {
                if (hashCode != -309052356) {
                    if (hashCode == 1971193321 && str2.equals(MtopEnablePropertyType.ENABLE_NEW_DEVICE_ID)) {
                        c = 1;
                    }
                } else if (str2.equals(MtopEnablePropertyType.ENABLE_HEADER_URL_ENCODE)) {
                    c = 2;
                }
            } else if (str2.equals(MtopEnablePropertyType.ENABLE_NOTIFY_SESSION_RET)) {
                c = 0;
            }
            switch (c) {
                case 0:
                    mtopConfigByID.notifySessionResult = z;
                    return;
                case 1:
                    mtopConfigByID.enableNewDeviceId = z;
                    return;
                case 2:
                    mtopConfigByID.enableHeaderUrlEncode = z;
                    return;
                default:
                    return;
            }
        }
    }

    public static void setParam(String str, String str2, @NonNull String str3, @NonNull String str4) {
        if (str2 != null && str3 != null && str4 != null) {
            MtopConfig mtopConfigByID = getMtopConfigByID(str);
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                TBSdkLog.i(TAG, mtopConfigByID.instanceId + "[setParam] set Param succeed.mtopParamType=" + str2 + ",key=" + str3 + ",value=" + str4);
            }
            char c = 65535;
            int hashCode = str2.hashCode();
            if (hashCode != 77406376) {
                if (hashCode != 1924418611) {
                    if (hashCode == 2127025805 && str2.equals(MtopParamType.HEADER)) {
                        c = 0;
                    }
                } else if (str2.equals(MtopParamType.ABTEST)) {
                    c = 2;
                }
            } else if (str2.equals(MtopParamType.QUERY)) {
                c = 1;
            }
            switch (c) {
                case 0:
                    mtopConfigByID.mtopGlobalHeaders.put(str3, str4);
                    return;
                case 1:
                    mtopConfigByID.mtopGlobalQuerys.put(str3, str4);
                    return;
                case 2:
                    mtopConfigByID.mtopGlobalABTestParams.put(str3, str4);
                    return;
                default:
                    return;
            }
        }
    }

    public static void removeParam(String str, String str2, @NonNull String str3) {
        if (str2 != null && str3 != null) {
            MtopConfig mtopConfigByID = getMtopConfigByID(str);
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                TBSdkLog.i(TAG, mtopConfigByID.instanceId + "[removeParam] remove Param succeed.mtopParamType=" + str2 + ",key=" + str3);
            }
            char c = 65535;
            int hashCode = str2.hashCode();
            if (hashCode != 77406376) {
                if (hashCode != 1924418611) {
                    if (hashCode == 2127025805 && str2.equals(MtopParamType.HEADER)) {
                        c = 0;
                    }
                } else if (str2.equals(MtopParamType.ABTEST)) {
                    c = 2;
                }
            } else if (str2.equals(MtopParamType.QUERY)) {
                c = 1;
            }
            switch (c) {
                case 0:
                    mtopConfigByID.mtopGlobalHeaders.remove(str3);
                    return;
                case 1:
                    mtopConfigByID.mtopGlobalQuerys.remove(str3);
                    return;
                case 2:
                    mtopConfigByID.mtopGlobalABTestParams.remove(str3);
                    return;
                default:
                    return;
            }
        }
    }

    public static void addMtopStatisListener(String str, @NonNull MtopStatsListener mtopStatsListener) {
        MtopConfig mtopConfigByID = getMtopConfigByID(str);
        mtopConfigByID.mtopStatsListener = mtopStatsListener;
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            TBSdkLog.i(TAG, mtopConfigByID.instanceId + " [addMtopStatisListener] set MtopStatsListener succeed.");
        }
    }

    public static void setRouterId(String str, String str2) {
        if (!StringUtils.isEmpty(str2)) {
            MtopConfig mtopConfigByID = getMtopConfigByID(str);
            mtopConfigByID.routerId = str2;
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                TBSdkLog.i(TAG, mtopConfigByID.instanceId + "[setRouterId] set routerId succeed.routerId=" + str2);
            }
        }
    }

    public static void setPlaceId(String str, String str2) {
        if (!StringUtils.isEmpty(str2)) {
            MtopConfig mtopConfigByID = getMtopConfigByID(str);
            mtopConfigByID.placeId = str2;
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                TBSdkLog.i(TAG, mtopConfigByID.instanceId + "[setPlaceId] set placeId succeed.placeId=" + str2);
            }
        }
    }
}
