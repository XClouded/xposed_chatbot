package mtopsdk.common.util;

import android.content.Context;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.mtop.intf.Mtop;

public class RemoteConfig {
    private static final String TAG = "mtopsdk.RemoteConfig";
    public static final String TB_SPEED_TS_ENABLE = "tsEnable";
    public static final String TB_SPEED_U_LAND = "preUland";
    private static RemoteConfig instance;
    private static Map<String, Integer> segmentSizeMap = new HashMap();
    public long antiAttackWaitInterval = 20;
    public long apiLockInterval = 10;
    public long bizErrorMappingCodeLength = 24;
    private Map<String, String> configItemsMap = null;
    public String degradeApiCacheList = "";
    public String degradeBizErrorMappingApiList = "";
    public final Set<String> degradeBizcodeSets = new HashSet();
    public boolean degradeToSQLite = false;
    public boolean enableArupTlog = true;
    public boolean enableBizErrorCodeMapping = false;
    public boolean enableCache = true;
    public boolean enableErrorCodeMapping = true;
    public boolean enableNewExecutor = true;
    public boolean enableProperty = false;
    public boolean enableSpdy = true;
    public boolean enableSsl = true;
    @Deprecated
    public boolean enableUnit = true;
    public String errorMappingMsg = "";
    public String individualApiLockInterval = "";
    public boolean prefetch = false;
    public String removeCacheBlockList = "";
    public int segmentRetryTimes = -1;
    public int uploadThreadNums = -1;
    public final Set<String> useHttpsBizcodeSets = new HashSet();
    public int useSecurityAdapter = 6;

    public static RemoteConfig getInstance() {
        if (instance == null) {
            synchronized (RemoteConfig.class) {
                if (instance == null) {
                    instance = new RemoteConfig();
                }
            }
        }
        return instance;
    }

    static {
        segmentSizeMap.put("2G", 32768);
        segmentSizeMap.put("3G", 65536);
        segmentSizeMap.put("4G", 524288);
        segmentSizeMap.put("WIFI", 524288);
        segmentSizeMap.put("UNKONWN", 131072);
        segmentSizeMap.put("NET_NO", 131072);
    }

    public Integer getSegmentSize(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        return segmentSizeMap.get(str);
    }

    public void setSegmentSize(String str, int i) {
        if (!StringUtils.isBlank(str) && i > 0) {
            segmentSizeMap.put(str, Integer.valueOf(i));
        }
    }

    public void updateRemoteConfig() {
        this.configItemsMap = SwitchConfigUtil.getSwitchConfigByGroupName(SwitchConfigUtil.CONFIG_GROUP_MTOPSDK_ANDROID_SWITCH);
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            TBSdkLog.i(TAG, "[updateRemoteConfig] configItemsMap=" + this.configItemsMap);
        }
        if (this.configItemsMap != null) {
            String configItemByKey = getConfigItemByKey(SwitchConfigUtil.ENABLE_ERROR_CODE_MAPPING_KEY, "true");
            this.enableErrorCodeMapping = "true".equals(configItemByKey);
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                TBSdkLog.i(TAG, "[setEnableErrorCodeMapping]remote enableErrorCodeMappingConfig=" + configItemByKey + ",enableErrorCodeMapping=" + this.enableErrorCodeMapping);
            }
            String configItemByKey2 = getConfigItemByKey(SwitchConfigUtil.ENABLE_BIZ_ERROR_CODE_MAPPING_KEY, "false");
            this.enableBizErrorCodeMapping = "true".equals(configItemByKey2);
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                TBSdkLog.i(TAG, "[setEnableBizErrorCodeMapping]remote enableBizErrorCodeMappingConfig=" + configItemByKey2 + ",enableBizErrorCodeMapping=" + this.enableBizErrorCodeMapping);
            }
            String configItemByKey3 = getConfigItemByKey(SwitchConfigUtil.ENABLE_SPDY_KEY, "true");
            this.enableSpdy = "true".equals(configItemByKey3);
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                TBSdkLog.i(TAG, "[setEnableSpdy]remote spdySwitchConfig=" + configItemByKey3 + ",enableSpdy=" + this.enableSpdy);
            }
            String configItemByKey4 = getConfigItemByKey(SwitchConfigUtil.ENABLE_SSL_KEY, "true");
            this.enableSsl = "true".equals(configItemByKey4);
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                TBSdkLog.i(TAG, "[setEnableSsl]remote spdySslSwitchConfig=" + configItemByKey4 + ",enableSsl=" + this.enableSsl);
            }
            String configItemByKey5 = getConfigItemByKey(SwitchConfigUtil.ENABLE_CACHE_KEY, "true");
            this.enableCache = "true".equalsIgnoreCase(configItemByKey5);
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                TBSdkLog.i(TAG, "[setEnableCache]remote cacheSwitchConfig=" + configItemByKey5 + ",enableCache=" + this.enableCache);
            }
            String configItemByKey6 = getConfigItemByKey(SwitchConfigUtil.ENABLE_MTOPSDK_PROPERTY_KEY, "false");
            this.enableProperty = !"false".equalsIgnoreCase(configItemByKey6);
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                TBSdkLog.i(TAG, "[setEnableProperty]remote mtopsdkPropertySwitchConfig=" + configItemByKey6 + ",enableProperty=" + this.enableProperty);
            }
            String configItemByKey7 = getConfigItemByKey(SwitchConfigUtil.DEGRADE_TO_SQLITE_KEY, "false");
            this.degradeToSQLite = !"false".equalsIgnoreCase(configItemByKey7);
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                TBSdkLog.i(TAG, "[setDegradeToSQLite]remote degradeToSQLiteConfig=" + configItemByKey7 + ",degradeToSQLite=" + this.degradeToSQLite);
            }
            String configItemByKey8 = getConfigItemByKey(SwitchConfigUtil.ENABLE_NEW_EXECUTOR, "true");
            this.enableNewExecutor = "true".equalsIgnoreCase(configItemByKey8);
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                TBSdkLog.i(TAG, "[setEnableNewExecutor]remote enableNewExecutorConfig=" + configItemByKey8 + ",enableNewExecutor=" + this.enableNewExecutor);
            }
            String configItemByKey9 = getConfigItemByKey(SwitchConfigUtil.API_LOCK_INTERVAL_KEY, (String) null);
            if (StringUtils.isNotBlank(configItemByKey9)) {
                try {
                    this.apiLockInterval = Long.parseLong(configItemByKey9);
                } catch (Exception unused) {
                    TBSdkLog.e(TAG, "[setApiLockInterval]parse apiLockIntervalConfig error,apiLockIntervalConfig=" + configItemByKey9);
                }
            }
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                TBSdkLog.i(TAG, "[setApiLockInterval]remote apiLockIntervalConfig=" + configItemByKey9 + ",apiLockInterval=" + this.apiLockInterval);
            }
            String configItemByKey10 = getConfigItemByKey(SwitchConfigUtil.OPEN_PREFETCH, "false");
            this.prefetch = "true".equalsIgnoreCase(configItemByKey10);
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                TBSdkLog.i(TAG, "[setPrefetch]remote openPrefetchConfig=" + configItemByKey10 + ",prefetch=" + this.prefetch);
            }
            try {
                Mtop instance2 = Mtop.instance(Mtop.Id.INNER, (Context) null);
                if (instance2.getMtopConfig().context != null) {
                    ConfigStoreManager.getInstance().saveConfigItem(instance2.getMtopConfig().context, ConfigStoreManager.MTOP_CONFIG_STORE, "", SwitchConfigUtil.OPEN_PREFETCH, configItemByKey10);
                } else {
                    TBSdkLog.e(TAG, "[prefetch]context can't be null.wait INNER mtopInstance init.");
                }
            } catch (Exception unused2) {
                TBSdkLog.e(TAG, "[prefetch]parse useSecAdapterConfig error,openPrefetchConfig=" + configItemByKey10);
            }
            String configItemByKey11 = getConfigItemByKey(SwitchConfigUtil.ANTI_ATTACK_WAIT_INTERVAL_KEY, (String) null);
            if (StringUtils.isNotBlank(configItemByKey11)) {
                try {
                    this.antiAttackWaitInterval = Long.parseLong(configItemByKey11);
                } catch (Exception unused3) {
                    TBSdkLog.e(TAG, "[setAntiAttackWaitInterval]parse antiAttackWaitIntervalConfig error,antiAttackWaitIntervalConfig=" + configItemByKey11);
                }
            }
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                TBSdkLog.i(TAG, "[setAntiAttackWaitInterval]remote antiAttackWaitIntervalConfig=" + configItemByKey11 + ",antiAttackWaitInterval=" + this.antiAttackWaitInterval);
            }
            String configItemByKey12 = getConfigItemByKey(SwitchConfigUtil.BIZ_ERROR_MAPPING_CODE_LENGTH_KEY, (String) null);
            if (StringUtils.isNotBlank(configItemByKey12)) {
                try {
                    this.bizErrorMappingCodeLength = Long.parseLong(configItemByKey12);
                } catch (Exception unused4) {
                    TBSdkLog.e(TAG, "[setBizErrorMappingCodeLength]parse bizErrorMappingCodeLengthConfig error,bizErrorMappingCodeLengthConfig=" + configItemByKey12);
                }
            }
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                TBSdkLog.i(TAG, "[setBizErrorMappingCodeLength]remote bizErrorMappingCodeLengthConfig=" + configItemByKey12 + ",bizErrorMappingCodeLength=" + this.bizErrorMappingCodeLength);
            }
            this.individualApiLockInterval = getConfigItemByKey(SwitchConfigUtil.INDIVIDUAL_API_LOCK_INTERVAL_KEY, "");
            this.degradeApiCacheList = getConfigItemByKey(SwitchConfigUtil.DEGRADE_API_CACHE_LIST_KEY, "");
            this.removeCacheBlockList = getConfigItemByKey(SwitchConfigUtil.REMOVE_CACHE_BLOCK_LIST_KEY, "");
            this.degradeBizErrorMappingApiList = getConfigItemByKey(SwitchConfigUtil.DEGRADE_BIZ_ERROR_MAPPING_API_LIST_KEY, "");
            this.errorMappingMsg = getConfigItemByKey(SwitchConfigUtil.ERROR_MAPPING_MSG_KEY, "");
            String configItemByKey13 = getConfigItemByKey(SwitchConfigUtil.USE_SECURITY_ADAPTER, "");
            if (StringUtils.isNotBlank(configItemByKey13)) {
                try {
                    int parseInt = Integer.parseInt(configItemByKey13);
                    if (parseInt != this.useSecurityAdapter) {
                        this.useSecurityAdapter = parseInt;
                        Mtop instance3 = Mtop.instance(Mtop.Id.INNER, (Context) null);
                        if (instance3.getMtopConfig().context != null) {
                            ConfigStoreManager.getInstance().saveConfigItem(instance3.getMtopConfig().context, ConfigStoreManager.MTOP_CONFIG_STORE, "", SwitchConfigUtil.USE_SECURITY_ADAPTER, configItemByKey13);
                        } else {
                            TBSdkLog.e(TAG, "[useSecurityAdapter]context can't be null.wait INNER mtopInstance init.");
                        }
                    }
                } catch (Exception unused5) {
                    TBSdkLog.e(TAG, "[useSecurityAdapter]parse useSecAdapterConfig error,useSecAdapterConfig=" + configItemByKey13);
                }
            }
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                TBSdkLog.i(TAG, "[useSecurityAdapter]remote useSecurityAdapterSwitchConfig=" + configItemByKey13 + ",useSecurityAdapter=" + this.useSecurityAdapter);
            }
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                StringBuilder sb = new StringBuilder(128);
                sb.append("[setOtherConfigItemKey] individualApiLockInterval =");
                sb.append(this.individualApiLockInterval);
                sb.append(", degradeApiCacheList =");
                sb.append(this.degradeApiCacheList);
                sb.append(", removeCacheBlockList =");
                sb.append(this.removeCacheBlockList);
                sb.append(", degradeBizErrorMappingApiList =");
                sb.append(this.degradeBizErrorMappingApiList);
                sb.append(", errorMappingMsg =");
                sb.append(this.errorMappingMsg);
                TBSdkLog.i(TAG, sb.toString());
            }
        }
    }

    public void updateUploadRemoteConfig() {
        Map<String, String> switchConfigByGroupName = SwitchConfigUtil.getSwitchConfigByGroupName(SwitchConfigUtil.CONFIG_GROUP_MTOPSDK_UPLOAD_SWITCH);
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            TBSdkLog.i(TAG, "[updateUploadRemoteConfig] uploadConfigItemsMap=" + switchConfigByGroupName);
        }
        if (switchConfigByGroupName != null) {
            String str = switchConfigByGroupName.get(SwitchConfigUtil.SEGMENT_RETRY_TIMES_KEY);
            if (StringUtils.isNotBlank(str)) {
                try {
                    int parseInt = Integer.parseInt(str);
                    if (parseInt >= 0) {
                        this.segmentRetryTimes = parseInt;
                    }
                } catch (Exception unused) {
                    TBSdkLog.w(TAG, "[updateUploadRemoteConfig]parse segmentRetryTimes error,segmentRetryTimesStr=" + str);
                }
                if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                    TBSdkLog.i(TAG, "[updateUploadRemoteConfig]remote segmentRetryTimesStr=" + str + ",segmentRetryTimes=" + this.segmentRetryTimes);
                }
            }
            String str2 = switchConfigByGroupName.get(SwitchConfigUtil.UPLOAD_THREAD_NUMS_KEY);
            if (StringUtils.isNotBlank(str)) {
                try {
                    int parseInt2 = Integer.parseInt(str2);
                    if (parseInt2 >= 0) {
                        this.uploadThreadNums = parseInt2;
                    }
                } catch (Exception unused2) {
                    TBSdkLog.w(TAG, "[updateUploadRemoteConfig]parse uploadThreadNums error,uploadThreadNumsStr=" + str2);
                }
                if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                    TBSdkLog.i(TAG, "[updateUploadRemoteConfig]remote uploadThreadNumsStr=" + str2 + ",uploadThreadNums=" + this.uploadThreadNums);
                }
            }
        }
    }

    private String getConfigItemByKey(String str, String str2) {
        String str3 = null;
        try {
            if (this.configItemsMap != null) {
                str3 = this.configItemsMap.get(str);
            }
        } catch (Exception e) {
            TBSdkLog.w(TAG, "[getConfigItemByKey] get config item error; key=" + str, (Throwable) e);
        }
        return str3 == null ? str2 : str3;
    }

    public void loadLocalConfig(Context context) {
        String str;
        try {
            str = ConfigStoreManager.getInstance().getConfigItem(context, ConfigStoreManager.MTOP_CONFIG_STORE, "", SwitchConfigUtil.USE_SECURITY_ADAPTER);
            try {
                if (StringUtils.isNotBlank(str)) {
                    this.useSecurityAdapter = Integer.parseInt(str);
                    if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                        TBSdkLog.i(TAG, "[loadLocalConfig]local useSecurityAdapterSwitchConfig=" + str + ",useSecurityAdapter=" + this.useSecurityAdapter);
                    }
                }
                String configItem = ConfigStoreManager.getInstance().getConfigItem(context, ConfigStoreManager.MTOP_CONFIG_STORE, "", SwitchConfigUtil.OPEN_PREFETCH);
                if (StringUtils.isNotBlank(configItem)) {
                    this.prefetch = Boolean.parseBoolean(configItem);
                    if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                        TBSdkLog.i(TAG, "[loadLocalConfig]local openPrefetchStr=" + configItem + ",prefetch=" + this.prefetch);
                    }
                }
            } catch (Throwable unused) {
                TBSdkLog.e(TAG, "[loadLocalConfig]parse local useSecurityAdapter error, useSecAdapterStr=" + str);
            }
        } catch (Throwable unused2) {
            str = "";
            TBSdkLog.e(TAG, "[loadLocalConfig]parse local useSecurityAdapter error, useSecAdapterStr=" + str);
        }
    }
}
