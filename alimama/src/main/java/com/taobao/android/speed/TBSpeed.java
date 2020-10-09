package com.taobao.android.speed;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import com.taobao.orange.OConfigListener;
import com.taobao.orange.OrangeConfig;
import com.taobao.weex.analyzer.core.NetworkEventSender;
import com.ut.mini.UTAnalytics;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class TBSpeed {
    private static final String ENABLE_SPEED_GRAY = "speedGray";
    private static final boolean ENABLE_SPEED_GRAY_DEFAULT = false;
    private static final String ENABLE_SPEED_OPEN = "speedOpen";
    private static final boolean ENABLE_SPEED_OPEN_DEFAULT = false;
    private static final boolean ENABLE_SPEED_PROXY_DEFAULT = false;
    private static final String ENABLE_SPEED_SWITCH = "isSpeedEnable";
    private static final boolean ENABLE_SPEED_SWITCH_DEFAULT = true;
    private static final String ORANGE_GROUP_NAME = "taobao_speed";
    private static final String SPEED_OPEN_BLACKLIST = "speedBlackList";
    private static final String SP_FILE_NAME = "taobao_speed";
    private static final String SP_SPEED_BIZ_MAP_KEY = "taobao_speed_biz_map";
    private static final String SP_SPEED_DESC = "taobao_speed_desc";
    private static final String SP_SPEED_GRAY_KEY = "taobao_speed_gray";
    private static final String SP_SPEED_OPEN_BLACKLIST_KEY = "taobao_speed_open_blacklist";
    private static final String SP_SPEED_OPEN_KEY = "taobao_speed_open";
    private static final String SP_SPEED_PROXY_KEY = "taobao_speed_proxy_enable";
    private static final String SP_SPEED_SWITCH_KEY = "taobao_speed_switch_enable";
    private static final String SP_SPEED_UTDID_KEY = "taobao_speed_utdid";
    private static final String SP_SUB_EDITION_KEY = "taobao_sub_edition";
    private static final String SP_SUB_EDITION_PASS_PARAMS_KEY = "taobao_sub_edition_pass_params";
    private static final String TAG = "TBSpeed";
    public static final String TAO_SUB_EDITION_DEFAULT = "";
    public static final String TAO_SUB_EDITION_SPEED_DEFAULT = "speed_-1";
    public static final String TAO_SUB_EDITION_SPEED_GRAY = "speed_-2";
    public static final String TAO_SUB_EDITION_STANDARD_GRAY = "standard_-2";
    /* access modifiers changed from: private */
    public static Map<String, Boolean> bizSpeedMap = null;
    /* access modifiers changed from: private */
    public static String blackListSP = null;
    private static Context globalContext = null;
    private static String homeBuckets = null;
    private static boolean isClientSpeed = false;
    private static boolean isInit = false;
    private static LinkedList<WeakReference<ISpeedSwitchListener>> listenerRefs = new LinkedList<>();
    private static Object lock = new Object();
    /* access modifiers changed from: private */
    public static String newSubEdition = null;
    private static String[] openBlackLlist = null;
    private static OConfigListener orangeListener = null;
    private static String speedDesc = null;
    /* access modifiers changed from: private */
    public static boolean speedGray = false;
    /* access modifiers changed from: private */
    public static boolean speedOpen = false;
    private static boolean speedProxyEnable = false;
    /* access modifiers changed from: private */
    public static boolean speedSwithEnable = true;
    private static String subEdition;
    private static String subEditionPassParams;

    @Deprecated
    public static boolean isSpeedEdition(Context context) {
        return false;
    }

    @Deprecated
    public static void registerSpeedSwitchListener(ISpeedSwitchListener iSpeedSwitchListener) {
    }

    @Deprecated
    public static void unregisterSpeedSwitchListener(ISpeedSwitchListener iSpeedSwitchListener) {
    }

    public static String getSpeedDesc(Context context) {
        try {
            if (globalContext == null && context != null) {
                globalContext = context.getApplicationContext();
            }
            init();
        } catch (Throwable unused) {
        }
        if (TextUtils.isEmpty(speedDesc)) {
            return null;
        }
        return speedDesc;
    }

    public static boolean isSpeedEdition(Context context, String str) {
        try {
            if (globalContext == null && context != null) {
                globalContext = context.getApplicationContext();
            }
            init();
        } catch (Throwable unused) {
        }
        if (isClientSpeed) {
            if (openBlackLlist == null || openBlackLlist.length <= 0) {
                return true;
            }
            for (String equals : openBlackLlist) {
                if (TextUtils.equals(equals, str)) {
                    return false;
                }
            }
            return true;
        } else if (bizSpeedMap == null || !bizSpeedMap.containsKey(str)) {
            return false;
        } else {
            return bizSpeedMap.get(str).booleanValue();
        }
    }

    public static String getCurrentSpeedStatus() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("speedOpen=");
        stringBuffer.append(speedOpen);
        stringBuffer.append(", speedGray=");
        stringBuffer.append(speedGray);
        stringBuffer.append(", speedSwitch=");
        stringBuffer.append(speedSwithEnable);
        stringBuffer.append(", speedProxy=");
        stringBuffer.append(speedProxyEnable);
        stringBuffer.append(", blackList=");
        stringBuffer.append(blackListSP);
        stringBuffer.append(", bizIds=");
        if (bizSpeedMap != null && !bizSpeedMap.isEmpty()) {
            for (String next : bizSpeedMap.keySet()) {
                Boolean bool = bizSpeedMap.get(next);
                if (bool != null && bool.booleanValue()) {
                    stringBuffer.append(next);
                    stringBuffer.append("|");
                }
            }
        }
        return stringBuffer.toString();
    }

    public static String getSubEdition() {
        return subEdition;
    }

    public static String getSpeedPassParams() {
        return subEditionPassParams;
    }

    public static String getHomeBuckets() {
        return homeBuckets;
    }

    public static void setSpeedEdition(Context context, String str, Map<String, String> map) {
        HashMap hashMap = new HashMap();
        if (map != null && !map.isEmpty()) {
            putString(SP_SPEED_DESC, map.get(NetworkEventSender.INTENT_EXTRA_DESC));
            for (String next : map.keySet()) {
                if (Boolean.valueOf(map.get(next)).booleanValue()) {
                    hashMap.put(next, Boolean.TRUE);
                }
            }
        }
        try {
            if (globalContext == null) {
                globalContext = context.getApplicationContext();
            }
            init();
            initOrange();
            updateSpeedStatus(str, hashMap, false);
        } catch (Throwable unused) {
        }
    }

    public static void setSpeedPassParams(String str) {
        if (!speedOpen && !speedGray && !TextUtils.equals(subEditionPassParams, str)) {
            subEditionPassParams = str;
            putString(SP_SUB_EDITION_PASS_PARAMS_KEY, subEditionPassParams);
        }
    }

    public static void updateUTParams() {
        String str;
        if (!speedOpen) {
            if (speedGray) {
                str = isClientSpeed ? TAO_SUB_EDITION_SPEED_GRAY : TAO_SUB_EDITION_STANDARD_GRAY;
            } else {
                str = subEdition;
            }
            try {
                if (!TextUtils.isEmpty(str)) {
                    UTAnalytics.getInstance().getDefaultTracker().setGlobalProperty("x-v-s", str);
                } else {
                    UTAnalytics.getInstance().getDefaultTracker().removeGlobalProperty("x-v-s");
                }
            } catch (Throwable unused) {
            }
        }
    }

    public static void updateSpeedProxy(Context context, boolean z) {
        if (!speedOpen && !speedGray) {
            try {
                if (speedProxyEnable != z) {
                    speedProxyEnable = z;
                    putBoolean(SP_SPEED_PROXY_KEY, speedProxyEnable);
                    Log.d(TAG, "update proxy, set speedProxyEnable=" + speedProxyEnable);
                    updateSpeedStatus(z ? TAO_SUB_EDITION_SPEED_DEFAULT : "", bizSpeedMap, true);
                }
            } catch (Throwable unused) {
            }
        }
    }

    public static void updateHomeBuckets(String str) {
        homeBuckets = str;
    }

    public static void updateSpeedUtdid(String str) {
        if (globalContext != null && TextUtils.isEmpty(getString(SP_SPEED_UTDID_KEY, ""))) {
            putString(SP_SPEED_UTDID_KEY, str);
        }
    }

    /* JADX WARNING: Missing exception handler attribute for start block: B:44:0x0117 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void init() {
        /*
            boolean r0 = isInit
            if (r0 == 0) goto L_0x0005
            return
        L_0x0005:
            java.lang.Object r0 = lock
            monitor-enter(r0)
            boolean r1 = isInit     // Catch:{ all -> 0x0120 }
            if (r1 == 0) goto L_0x000e
            monitor-exit(r0)     // Catch:{ all -> 0x0120 }
            return
        L_0x000e:
            java.lang.String r1 = "taobao_speed_desc"
            java.lang.String r2 = ""
            java.lang.String r1 = getString(r1, r2)     // Catch:{ all -> 0x0120 }
            speedDesc = r1     // Catch:{ all -> 0x0120 }
            java.lang.String r1 = "taobao_speed_switch_enable"
            r2 = 1
            boolean r1 = getBoolean(r1, r2)     // Catch:{ all -> 0x0120 }
            speedSwithEnable = r1     // Catch:{ all -> 0x0120 }
            boolean r1 = speedSwithEnable     // Catch:{ all -> 0x0120 }
            if (r1 != 0) goto L_0x0034
            java.lang.String r1 = ""
            subEdition = r1     // Catch:{ all -> 0x0120 }
            isInit = r2     // Catch:{ all -> 0x0120 }
            java.lang.String r1 = "TBSpeed"
            java.lang.String r2 = "init speedSwithEnable=false"
            android.util.Log.e(r1, r2)     // Catch:{ all -> 0x0120 }
            monitor-exit(r0)     // Catch:{ all -> 0x0120 }
            return
        L_0x0034:
            java.lang.String r1 = "taobao_speed_proxy_enable"
            r3 = 0
            boolean r1 = getBoolean(r1, r3)     // Catch:{ all -> 0x0120 }
            speedProxyEnable = r1     // Catch:{ all -> 0x0120 }
            boolean r1 = speedProxyEnable     // Catch:{ all -> 0x0120 }
            if (r1 == 0) goto L_0x0052
            isClientSpeed = r2     // Catch:{ all -> 0x0120 }
            java.lang.String r1 = "speed_-1"
            subEdition = r1     // Catch:{ all -> 0x0120 }
            isInit = r2     // Catch:{ all -> 0x0120 }
            java.lang.String r1 = "TBSpeed"
            java.lang.String r2 = "init speedProxyEnable=true, set isClientSpeed=true"
            android.util.Log.e(r1, r2)     // Catch:{ all -> 0x0120 }
            monitor-exit(r0)     // Catch:{ all -> 0x0120 }
            return
        L_0x0052:
            java.lang.String r1 = "taobao_speed_open_blacklist"
            java.lang.String r4 = ""
            java.lang.String r1 = getString(r1, r4)     // Catch:{ all -> 0x0120 }
            blackListSP = r1     // Catch:{ all -> 0x0120 }
            java.lang.String r1 = blackListSP     // Catch:{ all -> 0x0120 }
            boolean r1 = android.text.TextUtils.isEmpty(r1)     // Catch:{ all -> 0x0120 }
            if (r1 != 0) goto L_0x006e
            java.lang.String r1 = blackListSP     // Catch:{ all -> 0x0120 }
            java.lang.String r4 = ","
            java.lang.String[] r1 = r1.split(r4)     // Catch:{ all -> 0x0120 }
            openBlackLlist = r1     // Catch:{ all -> 0x0120 }
        L_0x006e:
            java.lang.String r1 = "TBSpeed"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x0120 }
            r4.<init>()     // Catch:{ all -> 0x0120 }
            java.lang.String r5 = "init openBlackLlist, set openBlackLlist="
            r4.append(r5)     // Catch:{ all -> 0x0120 }
            java.lang.String r5 = blackListSP     // Catch:{ all -> 0x0120 }
            r4.append(r5)     // Catch:{ all -> 0x0120 }
            java.lang.String r4 = r4.toString()     // Catch:{ all -> 0x0120 }
            android.util.Log.e(r1, r4)     // Catch:{ all -> 0x0120 }
            java.lang.String r1 = "taobao_speed_open"
            boolean r1 = getBoolean(r1, r3)     // Catch:{ all -> 0x0120 }
            speedOpen = r1     // Catch:{ all -> 0x0120 }
            boolean r1 = speedOpen     // Catch:{ all -> 0x0120 }
            if (r1 == 0) goto L_0x009f
            isClientSpeed = r2     // Catch:{ all -> 0x0120 }
            isInit = r2     // Catch:{ all -> 0x0120 }
            java.lang.String r1 = "TBSpeed"
            java.lang.String r2 = "init speedOpen=true, set isClientSpeed=true"
            android.util.Log.e(r1, r2)     // Catch:{ all -> 0x0120 }
            monitor-exit(r0)     // Catch:{ all -> 0x0120 }
            return
        L_0x009f:
            java.lang.String r1 = "taobao_speed_gray"
            boolean r1 = getBoolean(r1, r3)     // Catch:{ all -> 0x0120 }
            speedGray = r1     // Catch:{ all -> 0x0120 }
            boolean r1 = speedGray     // Catch:{ all -> 0x0120 }
            if (r1 == 0) goto L_0x00ca
            initGray()     // Catch:{ all -> 0x0120 }
            isInit = r2     // Catch:{ all -> 0x0120 }
            java.lang.String r1 = "TBSpeed"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x0120 }
            r2.<init>()     // Catch:{ all -> 0x0120 }
            java.lang.String r3 = "init speedGray=true, set isClientSpeed="
            r2.append(r3)     // Catch:{ all -> 0x0120 }
            boolean r3 = isClientSpeed     // Catch:{ all -> 0x0120 }
            r2.append(r3)     // Catch:{ all -> 0x0120 }
            java.lang.String r2 = r2.toString()     // Catch:{ all -> 0x0120 }
            android.util.Log.e(r1, r2)     // Catch:{ all -> 0x0120 }
            monitor-exit(r0)     // Catch:{ all -> 0x0120 }
            return
        L_0x00ca:
            java.lang.String r1 = "taobao_sub_edition_pass_params"
            java.lang.String r4 = ""
            java.lang.String r1 = getString(r1, r4)     // Catch:{ all -> 0x0120 }
            subEditionPassParams = r1     // Catch:{ all -> 0x0120 }
            java.lang.String r1 = "taobao_sub_edition"
            java.lang.String r4 = ""
            java.lang.String r1 = getString(r1, r4)     // Catch:{ all -> 0x0120 }
            subEdition = r1     // Catch:{ all -> 0x0120 }
            newSubEdition = r1     // Catch:{ all -> 0x0120 }
            java.lang.String r1 = "taobao_speed_biz_map"
            java.lang.String r4 = ""
            java.lang.String r1 = getString(r1, r4)     // Catch:{ Throwable -> 0x0117, all -> 0x011a }
            java.lang.String r4 = "TBSpeed"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0117, all -> 0x011a }
            r5.<init>()     // Catch:{ Throwable -> 0x0117, all -> 0x011a }
            java.lang.String r6 = "read bizMapJson="
            r5.append(r6)     // Catch:{ Throwable -> 0x0117, all -> 0x011a }
            r5.append(r1)     // Catch:{ Throwable -> 0x0117, all -> 0x011a }
            java.lang.String r5 = r5.toString()     // Catch:{ Throwable -> 0x0117, all -> 0x011a }
            android.util.Log.w(r4, r5)     // Catch:{ Throwable -> 0x0117, all -> 0x011a }
            boolean r4 = android.text.TextUtils.isEmpty(r1)     // Catch:{ Throwable -> 0x0117, all -> 0x011a }
            if (r4 == 0) goto L_0x0108
            isInit = r2     // Catch:{ all -> 0x0120 }
            monitor-exit(r0)     // Catch:{ all -> 0x0120 }
            return
        L_0x0108:
            com.taobao.android.speed.TBSpeed$1 r4 = new com.taobao.android.speed.TBSpeed$1     // Catch:{ Throwable -> 0x0117, all -> 0x011a }
            r4.<init>()     // Catch:{ Throwable -> 0x0117, all -> 0x011a }
            com.alibaba.fastjson.parser.Feature[] r3 = new com.alibaba.fastjson.parser.Feature[r3]     // Catch:{ Throwable -> 0x0117, all -> 0x011a }
            java.lang.Object r1 = com.alibaba.fastjson.JSON.parseObject((java.lang.String) r1, r4, (com.alibaba.fastjson.parser.Feature[]) r3)     // Catch:{ Throwable -> 0x0117, all -> 0x011a }
            java.util.Map r1 = (java.util.Map) r1     // Catch:{ Throwable -> 0x0117, all -> 0x011a }
            bizSpeedMap = r1     // Catch:{ Throwable -> 0x0117, all -> 0x011a }
        L_0x0117:
            isInit = r2     // Catch:{ all -> 0x0120 }
            goto L_0x011e
        L_0x011a:
            r1 = move-exception
            isInit = r2     // Catch:{ all -> 0x0120 }
            throw r1     // Catch:{ all -> 0x0120 }
        L_0x011e:
            monitor-exit(r0)     // Catch:{ all -> 0x0120 }
            return
        L_0x0120:
            r1 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0120 }
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.speed.TBSpeed.init():void");
    }

    private static void initGray() {
        String string = getString(SP_SPEED_UTDID_KEY, "");
        if (!TextUtils.isEmpty(string)) {
            try {
                if (Math.abs(((long) string.hashCode()) % 100) < 50) {
                    isClientSpeed = true;
                } else {
                    isClientSpeed = false;
                }
            } catch (Throwable unused) {
            }
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0090, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0092, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized void updateSpeedStatus(java.lang.String r4, java.util.Map<java.lang.String, java.lang.Boolean> r5, boolean r6) {
        /*
            java.lang.Class<com.taobao.android.speed.TBSpeed> r0 = com.taobao.android.speed.TBSpeed.class
            monitor-enter(r0)
            boolean r1 = speedOpen     // Catch:{ all -> 0x0093 }
            if (r1 != 0) goto L_0x0091
            boolean r1 = speedGray     // Catch:{ all -> 0x0093 }
            if (r1 == 0) goto L_0x000d
            goto L_0x0091
        L_0x000d:
            boolean r1 = speedProxyEnable     // Catch:{ all -> 0x0093 }
            if (r1 == 0) goto L_0x0014
            java.lang.String r4 = "speed_-1"
            goto L_0x001d
        L_0x0014:
            boolean r1 = speedSwithEnable     // Catch:{ all -> 0x0093 }
            if (r1 != 0) goto L_0x001d
            java.lang.String r4 = ""
            r5.clear()     // Catch:{ all -> 0x0093 }
        L_0x001d:
            if (r5 == 0) goto L_0x0031
            java.util.Map<java.lang.String, java.lang.Boolean> r1 = bizSpeedMap     // Catch:{ all -> 0x0093 }
            boolean r1 = r5.equals(r1)     // Catch:{ all -> 0x0093 }
            if (r1 != 0) goto L_0x0038
            java.lang.String r1 = "taobao_speed_biz_map"
            java.lang.String r2 = com.alibaba.fastjson.JSON.toJSONString(r5)     // Catch:{ all -> 0x0093 }
            putString(r1, r2)     // Catch:{ all -> 0x0093 }
            goto L_0x0038
        L_0x0031:
            java.lang.String r1 = "taobao_speed_biz_map"
            java.lang.String r2 = ""
            putString(r1, r2)     // Catch:{ all -> 0x0093 }
        L_0x0038:
            java.lang.String r1 = newSubEdition     // Catch:{ all -> 0x0093 }
            boolean r1 = android.text.TextUtils.equals(r4, r1)     // Catch:{ all -> 0x0093 }
            if (r1 != 0) goto L_0x008f
            newSubEdition = r4     // Catch:{ all -> 0x0093 }
            java.lang.String r1 = "TBSpeed"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x0093 }
            r2.<init>()     // Catch:{ all -> 0x0093 }
            java.lang.String r3 = "save subEdition "
            r2.append(r3)     // Catch:{ all -> 0x0093 }
            r2.append(r4)     // Catch:{ all -> 0x0093 }
            java.lang.String r2 = r2.toString()     // Catch:{ all -> 0x0093 }
            android.util.Log.e(r1, r2)     // Catch:{ all -> 0x0093 }
            java.lang.String r1 = "taobao_sub_edition"
            putString(r1, r4)     // Catch:{ all -> 0x0093 }
            if (r6 == 0) goto L_0x008f
            subEdition = r4     // Catch:{ all -> 0x0093 }
            bizSpeedMap = r5     // Catch:{ all -> 0x0093 }
            java.lang.String r4 = subEdition     // Catch:{ all -> 0x0093 }
            java.lang.String r5 = "speed_-1"
            boolean r4 = android.text.TextUtils.equals(r4, r5)     // Catch:{ all -> 0x0093 }
            if (r4 == 0) goto L_0x0071
            r4 = 1
            isClientSpeed = r4     // Catch:{ all -> 0x0093 }
            goto L_0x0074
        L_0x0071:
            r4 = 0
            isClientSpeed = r4     // Catch:{ all -> 0x0093 }
        L_0x0074:
            java.lang.String r4 = "TBSpeed"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ all -> 0x0093 }
            r5.<init>()     // Catch:{ all -> 0x0093 }
            java.lang.String r6 = "updateSpeedStatus, set isClientSpeed "
            r5.append(r6)     // Catch:{ all -> 0x0093 }
            boolean r6 = isClientSpeed     // Catch:{ all -> 0x0093 }
            r5.append(r6)     // Catch:{ all -> 0x0093 }
            java.lang.String r5 = r5.toString()     // Catch:{ all -> 0x0093 }
            android.util.Log.e(r4, r5)     // Catch:{ all -> 0x0093 }
            updateUTParams()     // Catch:{ all -> 0x0093 }
        L_0x008f:
            monitor-exit(r0)
            return
        L_0x0091:
            monitor-exit(r0)
            return
        L_0x0093:
            r4 = move-exception
            monitor-exit(r0)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.speed.TBSpeed.updateSpeedStatus(java.lang.String, java.util.Map, boolean):void");
    }

    private static void initOrange() {
        String str;
        try {
            if (orangeListener == null) {
                orangeListener = new OConfigListener() {
                    public void onConfigUpdate(String str, Map<String, String> map) {
                        String str2;
                        String str3;
                        String str4;
                        String str5;
                        Map<String, String> configs = OrangeConfig.getInstance().getConfigs(str);
                        if (configs == null) {
                            str2 = "";
                        } else {
                            str2 = configs.get(TBSpeed.ENABLE_SPEED_SWITCH);
                        }
                        boolean booleanValue = TextUtils.isEmpty(str2) ? true : Boolean.valueOf(str2).booleanValue();
                        if (TBSpeed.speedSwithEnable != booleanValue) {
                            TBSpeed.putBoolean(TBSpeed.SP_SPEED_SWITCH_KEY, booleanValue);
                            boolean unused = TBSpeed.speedSwithEnable = booleanValue;
                            Log.e(TBSpeed.TAG, "orange update, set speedSwithEnable=" + booleanValue);
                            TBSpeed.updateSpeedStatus(TBSpeed.newSubEdition, TBSpeed.bizSpeedMap, true);
                        }
                        if (configs == null) {
                            str3 = "";
                        } else {
                            str3 = configs.get(TBSpeed.ENABLE_SPEED_OPEN);
                        }
                        boolean z = false;
                        boolean booleanValue2 = TextUtils.isEmpty(str3) ? false : Boolean.valueOf(str3).booleanValue();
                        if (TBSpeed.speedOpen != booleanValue2) {
                            TBSpeed.putBoolean(TBSpeed.SP_SPEED_OPEN_KEY, booleanValue2);
                            Log.e(TBSpeed.TAG, "orange update, set speedOpen=" + booleanValue2);
                        }
                        if (configs == null) {
                            str4 = "";
                        } else {
                            str4 = configs.get(TBSpeed.ENABLE_SPEED_GRAY);
                        }
                        if (!TextUtils.isEmpty(str4)) {
                            z = Boolean.valueOf(str4).booleanValue();
                        }
                        if (TBSpeed.speedGray != z) {
                            TBSpeed.putBoolean(TBSpeed.SP_SPEED_GRAY_KEY, z);
                            Log.e(TBSpeed.TAG, "orange update, set speedGray=" + z);
                        }
                        if (configs == null) {
                            str5 = "";
                        } else {
                            str5 = configs.get(TBSpeed.SPEED_OPEN_BLACKLIST);
                        }
                        if (!TextUtils.equals(str5, TBSpeed.blackListSP)) {
                            TBSpeed.putString(TBSpeed.SP_SPEED_OPEN_BLACKLIST_KEY, str5);
                            Log.e(TBSpeed.TAG, "orange update, set blackListSP=" + str5);
                        }
                    }
                };
                boolean z = true;
                OrangeConfig.getInstance().registerListener(new String[]{"taobao_speed"}, orangeListener, true);
                Map<String, String> configs = OrangeConfig.getInstance().getConfigs("taobao_speed");
                if (configs == null) {
                    str = "";
                } else {
                    str = configs.get(ENABLE_SPEED_SWITCH);
                }
                if (!TextUtils.isEmpty(str)) {
                    z = Boolean.valueOf(str).booleanValue();
                }
                speedSwithEnable = z;
            }
        } catch (Throwable th) {
            Log.e(TAG, "register orange listener failed", th);
        }
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences("taobao_speed", 0);
    }

    private static boolean getBoolean(String str, boolean z) {
        if (globalContext == null) {
            return z;
        }
        try {
            return getSharedPreferences(globalContext).getBoolean(str, z);
        } catch (Throwable unused) {
            return z;
        }
    }

    /* access modifiers changed from: private */
    public static void putBoolean(String str, boolean z) {
        if (globalContext != null) {
            try {
                SharedPreferences.Editor edit = getSharedPreferences(globalContext).edit();
                edit.putBoolean(str, z);
                edit.commit();
            } catch (Throwable unused) {
            }
        }
    }

    private static String getString(String str, String str2) {
        if (globalContext == null) {
            return str2;
        }
        try {
            return getSharedPreferences(globalContext).getString(str, str2);
        } catch (Throwable unused) {
            return str2;
        }
    }

    /* access modifiers changed from: private */
    public static void putString(String str, String str2) {
        if (globalContext != null) {
            try {
                SharedPreferences.Editor edit = getSharedPreferences(globalContext).edit();
                edit.putString(str, str2);
                edit.commit();
            } catch (Throwable unused) {
            }
        }
    }
}
