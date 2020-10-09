package com.taobao.phenix.compat;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.TextUtils;
import com.taobao.orange.OrangeConfigListenerV1;
import com.taobao.orange.OrangeConfigLocal;
import com.taobao.phenix.common.UnitedLog;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class TBCloudConfigCenter {
    private static final String CACHE_KEY_LAST_COVERAGE = "_last_coverage";
    private static final String CACHE_KEY_LAST_ENABLED = "_last_enabled";
    private static final String CACHE_KEY_PREFIX = "cloudimg_";
    private static final String CLOUD_KEY_API_BLACKLIST = "_api_blacklist";
    private static final String CLOUD_KEY_COVERAGE = "_coverage";
    private static final String CLOUD_KEY_CPU_ABI = "_cpu_blacklist";
    private static final String CLOUD_KEY_MODEL_BLACKLIST = "_model_blacklist";
    private static final String CLOUD_KEY_MODEL_WHITELIST = "_model_whitelist";
    private static final String CLOUD_KEY_PERMANENT = "_permanent";
    private static final String CLOUD_KEY_VENDOR_BLACKLIST = "_vendor_blacklist";
    private static final String CLOUD_KEY_VENDOR_WHITELIST = "_vendor_whitelist";
    private static final String CONFIG_GROUP_NAME = "android_image_function_control";
    private static final String CONFIG_ITEM_SEPARATOR = ",";
    private static final String CONFIG_PERMANENT_TRUE = "1";
    public static final int COVERAGE_RANGE_MAX = 100;
    public static final int COVERAGE_RANGE_MIN = 0;
    private static final int DEFAULT_COVERAGE_OF_ANDROID_P = 0;
    private static final int DEFAULT_COVERAGE_OF_APG_SUPPORT = 0;
    private static final int DEFAULT_COVERAGE_OF_APNG_SUPPORT = 100;
    private static final int DEFAULT_COVERAGE_OF_ASHMEM_SUPPORT = 100;
    private static final int DEFAULT_COVERAGE_OF_BITMAP_POOL = 100;
    private static final int DEFAULT_COVERAGE_OF_DECODE_CANCELLABLE = 100;
    private static final int DEFAULT_COVERAGE_OF_DOMAIN_MERGE_SPECIAL = 0;
    private static final int DEFAULT_COVERAGE_OF_EXTERNAL_DECODER_PRIOR = 0;
    private static final int DEFAULT_COVERAGE_OF_HEIF_BUGFIX = 100;
    private static final int DEFAULT_COVERAGE_OF_HEIF_PNG_SUPPORT = 0;
    private static final int DEFAULT_COVERAGE_OF_HEIF_SUPPORT = 100;
    private static final int DEFAULT_COVERAGE_OF_IMAGE_ASYNC = 100;
    private static final int DEFAULT_COVERAGE_OF_MONITOR_STAT_SAMPLING = 20;
    private static final int DEFAULT_COVERAGE_OF_NETWORK_MAX_RUNNING = 100;
    private static final int DEFAULT_COVERAGE_OF_NEW_CACHE_POOL = 100;
    private static final int DEFAULT_COVERAGE_OF_NEW_LAUNCH = 100;
    private static final int DEFAULT_COVERAGE_OF_NEW_THREAD_MODEL = 0;
    private static final int DEFAULT_COVERAGE_OF_NON_CRITICAL_REPORT_SAMPLING = 40;
    private static final int DEFAULT_COVERAGE_OF_NO_REUSE_WEBP = 100;
    private static final int DEFAULT_COVERAGE_OF_RXMODEL_RECYCLE = 0;
    private static final int DEFAULT_COVERAGE_OF_THUMB_NAIL = 0;
    private static final int DEFAULT_COVERAGE_OF_TTL_CACHE = 100;
    private static final int DEFAULT_COVERAGE_OF_UNIFY_THREAD_POOL = 100;
    private static final int DEFAULT_COVERAGE_OF_WEBP_BUG_FIX = 100;
    private static final int DEFAULT_COVERAGE_OF_WEBP_CONVERT = 0;
    private static final int DEFAULT_COVERAGE_OF_WEBP_DEGRADE = 100;
    private static final boolean DEFAULT_PERMANENT_OF_ANDROID_P = false;
    private static final boolean DEFAULT_PERMANENT_OF_APG_SUPPORT = true;
    private static final boolean DEFAULT_PERMANENT_OF_APNG_SUPPORT = true;
    private static final boolean DEFAULT_PERMANENT_OF_ASHMEM_SUPPORT = false;
    private static final boolean DEFAULT_PERMANENT_OF_BITMAP_POOL = false;
    private static final boolean DEFAULT_PERMANENT_OF_DECODE_CANCELLABLE = false;
    private static final boolean DEFAULT_PERMANENT_OF_DOMAIN_MERGE_SPECIAL = false;
    private static final boolean DEFAULT_PERMANENT_OF_EXTERNAL_DECODER_PRIOR = false;
    private static final boolean DEFAULT_PERMANENT_OF_HEIF_BUGFIX = false;
    private static final boolean DEFAULT_PERMANENT_OF_HEIF_PNG_SUPPORT = false;
    private static final boolean DEFAULT_PERMANENT_OF_HEIF_SUPPORT = false;
    private static final boolean DEFAULT_PERMANENT_OF_IMAGE_ASYNC = false;
    private static final boolean DEFAULT_PERMANENT_OF_MONITOR_STAT_SAMPLING = false;
    private static final boolean DEFAULT_PERMANENT_OF_NETWORK_MAX_RUNNING = false;
    private static final boolean DEFAULT_PERMANENT_OF_NEW_CACHE_POOL = false;
    private static final boolean DEFAULT_PERMANENT_OF_NEW_LAUNCH = false;
    private static final boolean DEFAULT_PERMANENT_OF_NEW_THREAD_MODEL = false;
    private static final boolean DEFAULT_PERMANENT_OF_NON_CRITICAL_REPORT_SAMPLING = false;
    private static final boolean DEFAULT_PERMANENT_OF_NO_REUSE_WEBP = false;
    private static final boolean DEFAULT_PERMANENT_OF_RXMODEL_RECYCLE = false;
    private static final boolean DEFAULT_PERMANENT_OF_THUMB_NAIL = false;
    private static final boolean DEFAULT_PERMANENT_OF_TTL_CACHE = false;
    private static final boolean DEFAULT_PERMANENT_OF_UNIFY_THREAD_POOL = false;
    private static final boolean DEFAULT_PERMANENT_OF_WEBP_BUG_FIX = false;
    private static final boolean DEFAULT_PERMANENT_OF_WEBP_CONVERT = false;
    private static final boolean DEFAULT_PERMANENT_OF_WEBP_DEGRADE = false;
    public static final String EXPERIMENT_AB_ID = "experiment_id";
    public static long EXPERIMENT_ID = 0;
    public static final String EXPERIMENT_NAMESPACE = "middleware_yixiu";
    public static final int FID_OF_ANDROID_P = 29;
    public static final int FID_OF_APG_SUPPORT = 17;
    public static final int FID_OF_APNG_SUPPORT = 20;
    public static final int FID_OF_ASHMEM_SUPPORT = 19;
    public static final int FID_OF_BITMAP_POOL = 18;
    public static final int FID_OF_DECODE_CANCELLABLE = 16;
    public static final int FID_OF_DOMAIN_MERGE_SPECIAL = 37;
    public static final int FID_OF_EXTERNAL_DECODER_PRIOR = 15;
    public static final int FID_OF_HEIF_BUGFIX = 30;
    public static final int FID_OF_HEIF_PNG_SUPPORT = 23;
    public static final int FID_OF_HEIF_SUPPORT = 22;
    public static final int FID_OF_IMAGE_ASYNC = 28;
    public static final int FID_OF_MONITOR_STAT_SAMPLING = 13;
    public static final int FID_OF_NETWORK_MAX_RUNNING = 14;
    public static final int FID_OF_NEW_CACHE_POOL = 26;
    public static final int FID_OF_NEW_LAUNCH = 27;
    public static final int FID_OF_NEW_THREAD_MODEL = 24;
    public static final int FID_OF_NON_CRITICAL_REPORT_SAMPLING = 21;
    public static final int FID_OF_NO_REUSE_WEBP = 32;
    public static final int FID_OF_RXMODEL_RECYCLE = 25;
    public static final int FID_OF_THUMB_NAIL = 36;
    public static final int FID_OF_TTL_CACHE = 38;
    public static final int FID_OF_UNIFY_THREAD_POOL = 12;
    public static final int FID_OF_WEBP_BUG_FIX = 35;
    public static final int FID_OF_WEBP_CONVERT = 33;
    public static final int FID_OF_WEBP_DEGRADE = 31;
    public static final String IMAGE_MODULE = "img_phenix";
    public static final String OPEN_TTL = "openhttpcache";
    private static final String TAG = "CloudConfig";
    public static String TTL_DOMAIN = "picasso.alicdn.com";
    public static long TTL_MAX_VALUE = 604800;
    private static Random sRandom = new Random();
    private static TBCloudConfigCenter sTBCloudConfigCenter;
    /* access modifiers changed from: private */
    public List<CloudConfigChangeListener> mConfigChangeListeners;
    /* access modifiers changed from: private */
    public final SharedPreferences mSharedPref;

    public interface CloudConfigChangeListener {
        void onConfigUpdated(TBCloudConfigCenter tBCloudConfigCenter);
    }

    public static synchronized TBCloudConfigCenter getInstance(Context context) {
        TBCloudConfigCenter tBCloudConfigCenter;
        synchronized (TBCloudConfigCenter.class) {
            if (sTBCloudConfigCenter == null) {
                sTBCloudConfigCenter = new TBCloudConfigCenter(context);
            }
            tBCloudConfigCenter = sTBCloudConfigCenter;
        }
        return tBCloudConfigCenter;
    }

    public TBCloudConfigCenter(Context context) {
        this.mSharedPref = context.getSharedPreferences("cloud_image_setting", 0);
        OrangeConfigLocal.getInstance().registerListener(new String[]{CONFIG_GROUP_NAME}, (OrangeConfigListenerV1) new OrangeConfigListenerV1() {
            public void onConfigUpdate(String str, boolean z) {
                Map<String, String> configs;
                UnitedLog.e(TBCloudConfigCenter.TAG, "orange configs callback with group=%s, from cache=%b", str, Boolean.valueOf(z));
                if (!z && TBCloudConfigCenter.CONFIG_GROUP_NAME.equals(str) && (configs = OrangeConfigLocal.getInstance().getConfigs(TBCloudConfigCenter.CONFIG_GROUP_NAME)) != null) {
                    Set<String> keySet = configs.keySet();
                    SharedPreferences.Editor edit = TBCloudConfigCenter.this.mSharedPref.edit();
                    for (String next : keySet) {
                        String str2 = configs.get(next);
                        edit.putString(TBCloudConfigCenter.this.concatCloudKey(next), str2);
                        UnitedLog.d(TBCloudConfigCenter.TAG, "update configs from orange, save result=true, key=%s, value=%s", next, str2);
                    }
                    edit.apply();
                    synchronized (TBCloudConfigCenter.this) {
                        if (TBCloudConfigCenter.this.mConfigChangeListeners != null) {
                            for (CloudConfigChangeListener onConfigUpdated : TBCloudConfigCenter.this.mConfigChangeListeners) {
                                onConfigUpdated.onConfigUpdated(TBCloudConfigCenter.this);
                            }
                        }
                    }
                }
            }
        });
        OrangeConfigLocal.getInstance().getConfigs(CONFIG_GROUP_NAME);
    }

    public synchronized void addConfigChangeListener(CloudConfigChangeListener cloudConfigChangeListener) {
        if (this.mConfigChangeListeners == null) {
            this.mConfigChangeListeners = new ArrayList();
        }
        this.mConfigChangeListeners.add(cloudConfigChangeListener);
    }

    /* access modifiers changed from: private */
    public String concatCloudKey(String str) {
        StringBuilder sb = new StringBuilder(30);
        sb.append(CACHE_KEY_PREFIX);
        sb.append(str);
        return sb.toString();
    }

    private String concatCacheKey(int i, String str) {
        StringBuilder sb = new StringBuilder(str.length() + 11);
        sb.append(CACHE_KEY_PREFIX);
        sb.append(i);
        sb.append(str);
        return sb.toString();
    }

    private void updateConfigEntity(ConfigEntity configEntity) {
        try {
            String cacheString = getCacheString(concatCacheKey(configEntity.fid, CLOUD_KEY_COVERAGE));
            String cacheString2 = getCacheString(concatCacheKey(configEntity.fid, CLOUD_KEY_PERMANENT));
            String cacheString3 = getCacheString(concatCacheKey(configEntity.fid, CLOUD_KEY_MODEL_BLACKLIST));
            String cacheString4 = getCacheString(concatCacheKey(configEntity.fid, CLOUD_KEY_VENDOR_BLACKLIST));
            String cacheString5 = getCacheString(concatCacheKey(configEntity.fid, CLOUD_KEY_API_BLACKLIST));
            String cacheString6 = getCacheString(concatCacheKey(configEntity.fid, CLOUD_KEY_CPU_ABI));
            String cacheString7 = getCacheString(concatCacheKey(configEntity.fid, CLOUD_KEY_MODEL_WHITELIST));
            String cacheString8 = getCacheString(concatCacheKey(configEntity.fid, CLOUD_KEY_VENDOR_WHITELIST));
            UnitedLog.d(TAG, "update configs from local cache, fid=%d, coverage=%s, permanent=%s, modelBlacks=%s, vendorBlacks=%s, apiBlacks=%s, cpuBlacks=%s", Integer.valueOf(configEntity.fid), cacheString, cacheString2, cacheString3, cacheString4, cacheString5, cacheString6);
            if (!TextUtils.isEmpty(cacheString)) {
                configEntity.coverage = Math.min(100, Math.max(0, str2Int(cacheString, configEntity.coverage)));
            }
            if (!TextUtils.isEmpty(cacheString2)) {
                configEntity.permanent = "1".equals(cacheString2);
            }
            if (!TextUtils.isEmpty(cacheString3)) {
                configEntity.modelBlacklist = str2Strs(cacheString3);
            }
            if (!TextUtils.isEmpty(cacheString4)) {
                configEntity.vendorBlacklist = str2Strs(cacheString4);
            }
            if (!TextUtils.isEmpty(cacheString5)) {
                configEntity.apiBlacklist = str2Ints(cacheString5, 0);
            }
            if (!TextUtils.isEmpty(cacheString6)) {
                configEntity.cpuAbiBlacklist = str2Strs(cacheString6);
            }
            if (!TextUtils.isEmpty(cacheString7)) {
                configEntity.modelWhitelist = str2Strs(cacheString7);
            }
            if (!TextUtils.isEmpty(cacheString8)) {
                configEntity.vendorWhitelist = str2Strs(cacheString8);
            }
        } catch (Exception e) {
            UnitedLog.e(TAG, "update configs from local cache error=%s", e);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0059, code lost:
        r7 = getCacheInt(r5);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean getControlResult(com.taobao.phenix.compat.TBCloudConfigCenter.ConfigEntity r14) {
        /*
            r13 = this;
            r13.updateConfigEntity(r14)
            int r0 = r14.fid
            int r1 = r14.coverage
            r2 = 1
            if (r1 > 0) goto L_0x0017
            int r1 = r14.fid
            r3 = 29
            if (r1 != r3) goto L_0x0017
            boolean r1 = r14.inWhitelist()
            if (r1 == 0) goto L_0x0017
            return r2
        L_0x0017:
            int r1 = r14.coverage
            r3 = 2
            r4 = 0
            if (r1 <= 0) goto L_0x00f3
            boolean r1 = r14.shouldSkip()
            if (r1 == 0) goto L_0x0025
            goto L_0x00f3
        L_0x0025:
            int r1 = r14.coverage
            r5 = 100
            if (r1 < r5) goto L_0x0043
            java.lang.String r1 = "CloudConfig"
            java.lang.String r5 = "feature[%d] enabled, cause all open, coverage=%d"
            java.lang.Object[] r3 = new java.lang.Object[r3]
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)
            r3[r4] = r0
            int r14 = r14.coverage
            java.lang.Integer r14 = java.lang.Integer.valueOf(r14)
            r3[r2] = r14
            com.taobao.phenix.common.UnitedLog.i(r1, r5, r3)
            return r2
        L_0x0043:
            java.lang.String r1 = "_last_enabled"
            java.lang.String r1 = r13.concatCacheKey(r0, r1)
            java.lang.String r5 = "_last_coverage"
            java.lang.String r5 = r13.concatCacheKey(r0, r5)
            java.lang.Boolean r6 = r13.getCacheBool(r1)
            boolean r7 = r14.permanent
            if (r7 == 0) goto L_0x006a
            if (r6 == 0) goto L_0x006a
            java.lang.Integer r7 = r13.getCacheInt(r5)
            if (r7 == 0) goto L_0x006a
            int r8 = r14.coverage
            int r7 = r7.intValue()
            if (r8 == r7) goto L_0x0068
            goto L_0x006a
        L_0x0068:
            r7 = 0
            goto L_0x006b
        L_0x006a:
            r7 = 1
        L_0x006b:
            if (r7 == 0) goto L_0x0074
            int r8 = r14.coverage
            boolean r8 = randomEnabled(r8)
            goto L_0x0078
        L_0x0074:
            boolean r8 = r6.booleanValue()
        L_0x0078:
            boolean r9 = r14.permanent
            if (r9 != 0) goto L_0x00a2
            if (r6 == 0) goto L_0x00a2
            boolean r6 = r13.removeCache(r1)
            boolean r9 = r13.removeCache(r5)
            if (r9 == 0) goto L_0x008c
            if (r6 == 0) goto L_0x008c
            r6 = 1
            goto L_0x008d
        L_0x008c:
            r6 = 0
        L_0x008d:
            java.lang.String r9 = "CloudConfig"
            java.lang.String r10 = "feature[%d] remove all keys, cause permanent YES to NO, result=%b"
            java.lang.Object[] r11 = new java.lang.Object[r3]
            java.lang.Integer r12 = java.lang.Integer.valueOf(r0)
            r11[r4] = r12
            java.lang.Boolean r6 = java.lang.Boolean.valueOf(r6)
            r11[r2] = r6
            com.taobao.phenix.common.UnitedLog.d(r9, r10, r11)
        L_0x00a2:
            boolean r6 = r14.permanent
            if (r6 == 0) goto L_0x00d6
            if (r7 == 0) goto L_0x00d6
            java.lang.Boolean r6 = java.lang.Boolean.valueOf(r8)
            boolean r1 = r13.setCacheValue(r1, r6)
            int r14 = r14.coverage
            java.lang.Integer r14 = java.lang.Integer.valueOf(r14)
            boolean r14 = r13.setCacheValue(r5, r14)
            if (r14 == 0) goto L_0x00c0
            if (r1 == 0) goto L_0x00c0
            r14 = 1
            goto L_0x00c1
        L_0x00c0:
            r14 = 0
        L_0x00c1:
            java.lang.String r1 = "CloudConfig"
            java.lang.String r5 = "feature[%d] save keys[enabled&coverage] at permanent true, cause coverage changed or not been saved, result=%b"
            java.lang.Object[] r6 = new java.lang.Object[r3]
            java.lang.Integer r9 = java.lang.Integer.valueOf(r0)
            r6[r4] = r9
            java.lang.Boolean r14 = java.lang.Boolean.valueOf(r14)
            r6[r2] = r14
            com.taobao.phenix.common.UnitedLog.d(r1, r5, r6)
        L_0x00d6:
            java.lang.String r14 = "CloudConfig"
            java.lang.String r1 = "feature[%d] open result=%B, has random=%b"
            r5 = 3
            java.lang.Object[] r5 = new java.lang.Object[r5]
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)
            r5[r4] = r0
            java.lang.Boolean r0 = java.lang.Boolean.valueOf(r8)
            r5[r2] = r0
            java.lang.Boolean r0 = java.lang.Boolean.valueOf(r7)
            r5[r3] = r0
            com.taobao.phenix.common.UnitedLog.i(r14, r1, r5)
            return r8
        L_0x00f3:
            java.lang.String r1 = "CloudConfig"
            java.lang.String r5 = "feature[%d] disabled, cause all close or should skip, coverage=%d"
            java.lang.Object[] r3 = new java.lang.Object[r3]
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)
            r3[r4] = r0
            int r14 = r14.coverage
            java.lang.Integer r14 = java.lang.Integer.valueOf(r14)
            r3[r2] = r14
            com.taobao.phenix.common.UnitedLog.i(r1, r5, r3)
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.phenix.compat.TBCloudConfigCenter.getControlResult(com.taobao.phenix.compat.TBCloudConfigCenter$ConfigEntity):boolean");
    }

    public int getFeatureCoverage(int i) {
        ConfigEntity configEntity;
        if (i != 13) {
            configEntity = i != 21 ? null : new ConfigEntity(i, 40, false);
        } else {
            configEntity = new ConfigEntity(i, 20, false);
        }
        if (configEntity == null) {
            return 0;
        }
        updateConfigEntity(configEntity);
        int i2 = configEntity.coverage;
        if (configEntity.coverage <= 0 || configEntity.shouldSkip()) {
            UnitedLog.i(TAG, "feature[%d] disabled, cause all close or should skip, coverage=%d", Integer.valueOf(i), Integer.valueOf(configEntity.coverage));
            return 0;
        } else if (configEntity.coverage < 100) {
            return i2;
        } else {
            UnitedLog.i(TAG, "feature[%d] enabled, cause all open, coverage=%d", Integer.valueOf(i), Integer.valueOf(configEntity.coverage));
            return 100;
        }
    }

    public boolean isFeatureEnabled(int i) {
        switch (i) {
            case 12:
                return getControlResult(new ConfigEntity(i, 100, false));
            case 14:
                return getControlResult(new ConfigEntity(i, 100, false));
            case 15:
                return getControlResult(new ConfigEntity(i, 0, false));
            case 16:
                return getControlResult(new ConfigEntity(i, 100, false));
            case 17:
                return getControlResult(new ConfigEntity(i, 0, true));
            case 18:
                return getControlResult(new ConfigEntity(i, 100, false));
            case 19:
                return getControlResult(new ConfigEntity(i, 100, false));
            case 20:
                return getControlResult(new ConfigEntity(i, 100, true));
            case 22:
                return getControlResult(new ConfigEntity(i, 100, false));
            case 23:
                return getControlResult(new ConfigEntity(i, 0, false));
            case 24:
                return getControlResult(new ConfigEntity(i, 0, false));
            case 25:
                return getControlResult(new ConfigEntity(i, 0, false));
            case 26:
                getControlResult(new ConfigEntity(i, 100, false));
                break;
            case 27:
                break;
            case 28:
                return getControlResult(new ConfigEntity(i, 100, false));
            case 29:
                return getControlResult(new ConfigEntity(i, 0, false));
            case 30:
                return getControlResult(new ConfigEntity(i, 100, false));
            case 31:
                return getControlResult(new ConfigEntity(i, 100, false));
            case 32:
                return getControlResult(new ConfigEntity(i, 100, false));
            case 33:
                return getControlResult(new ConfigEntity(i, 0, false));
            case 35:
                return getControlResult(new ConfigEntity(i, 100, false));
            case 36:
                return getControlResult(new ConfigEntity(i, 0, false));
            case 37:
                return getControlResult(new ConfigEntity(i, 0, false));
            case 38:
                return getControlResult(new ConfigEntity(i, 100, false));
            default:
                return false;
        }
        return getControlResult(new ConfigEntity(i, 100, false));
    }

    private boolean removeCache(String str) {
        if (this.mSharedPref == null) {
            return false;
        }
        SharedPreferences.Editor edit = this.mSharedPref.edit();
        edit.remove(str);
        edit.apply();
        return true;
    }

    /* access modifiers changed from: package-private */
    public boolean setCacheValue(String str, Object obj) {
        if (this.mSharedPref == null) {
            return false;
        }
        boolean z = true;
        SharedPreferences.Editor edit = this.mSharedPref.edit();
        if (obj instanceof String) {
            edit.putString(str, (String) obj);
        } else if (obj instanceof Integer) {
            edit.putInt(str, ((Integer) obj).intValue());
        } else if (obj instanceof Boolean) {
            edit.putBoolean(str, ((Boolean) obj).booleanValue());
        } else {
            z = false;
        }
        edit.apply();
        return z;
    }

    /* access modifiers changed from: package-private */
    public String getCacheString(String str) {
        if (this.mSharedPref == null) {
            return null;
        }
        return this.mSharedPref.getString(str, (String) null);
    }

    private Integer getCacheInt(String str) {
        if (this.mSharedPref == null) {
            return 0;
        }
        return Integer.valueOf(this.mSharedPref.getInt(str, 0));
    }

    private Boolean getCacheBool(String str) {
        boolean z = false;
        if (this.mSharedPref != null && this.mSharedPref.getBoolean(str, false)) {
            z = true;
        }
        return Boolean.valueOf(z);
    }

    public static boolean randomEnabled(int i) {
        return sRandom.nextInt(100) + 1 <= i;
    }

    private int str2Int(String str, int i) {
        try {
            return Integer.parseInt(str);
        } catch (Exception unused) {
            return i;
        }
    }

    private int[] str2Ints(String str, int i) {
        String[] split = str.split(",");
        int[] iArr = new int[split.length];
        for (int i2 = 0; i2 < iArr.length; i2++) {
            iArr[i2] = str2Int(split[i2], i);
        }
        return iArr;
    }

    private String[] str2Strs(String str) {
        return str.split(",");
    }

    static class ConfigEntity {
        public int[] apiBlacklist;
        public int coverage;
        public String[] cpuAbiBlacklist;
        public final int fid;
        public String[] modelBlacklist;
        public String[] modelWhitelist;
        public boolean permanent;
        public String[] vendorBlacklist;
        public String[] vendorWhitelist;

        public ConfigEntity(int i, int i2, boolean z) {
            this.fid = i;
            this.coverage = i2;
            this.permanent = z;
        }

        public boolean isHitInStrs(String[] strArr, String str) {
            if (!(str == null || strArr == null || (r1 = strArr.length) <= 0)) {
                for (String equals : strArr) {
                    if (str.equals(equals)) {
                        return true;
                    }
                }
            }
            return false;
        }

        public boolean isHitInInts(int[] iArr, int i) {
            if (iArr != null && (r1 = iArr.length) > 0) {
                for (int i2 : iArr) {
                    if (i == i2) {
                        return true;
                    }
                }
            }
            return false;
        }

        public boolean shouldSkip() {
            return isHitInInts(this.apiBlacklist, Build.VERSION.SDK_INT) || isHitInStrs(this.modelBlacklist, Build.MODEL) || isHitInStrs(this.vendorBlacklist, Build.MANUFACTURER) || isHitInStrs(this.cpuAbiBlacklist, Build.CPU_ABI);
        }

        public boolean inWhitelist() {
            return isHitInStrs(this.modelWhitelist, Build.MODEL) || isHitInStrs(this.vendorWhitelist, Build.MANUFACTURER);
        }
    }
}
