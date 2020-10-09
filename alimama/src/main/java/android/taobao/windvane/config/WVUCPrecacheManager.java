package android.taobao.windvane.config;

import android.os.Handler;
import android.taobao.windvane.service.WVEventContext;
import android.taobao.windvane.service.WVEventListener;
import android.taobao.windvane.service.WVEventResult;
import android.taobao.windvane.service.WVEventService;
import android.text.TextUtils;

import java.util.HashSet;
import java.util.Iterator;

public class WVUCPrecacheManager implements WVEventListener {
    private static final String TAG = "WVUCPrecacheManager";
    private static WVUCPrecacheManager mInstance = null;
    private static boolean sCanClearByCommonConfig = false;
    private static boolean sCanClearByZcacheUpdate = false;
    private static boolean sCanPrecacheByCommonConfig = false;
    private static boolean sCanPrecacheByZcacheUpdate = false;
    private static boolean sHasInitUrlSet = false;
    private static boolean sHasPrecache = false;
    private static boolean sLastEnableUCPrecache = false;
    private static String sLastPrecachePackageName = "";
    private static long sLastPrecacheTime = -1;
    private static final long sMaxPrecacheTime = 3600000;
    private static HashSet<String> sPreMemCacheUrlSet = new HashSet<>();
    private static HashSet<String> sPrecacheDocResMap = new HashSet<>();
    private Handler mPrecacheHandler;

    private static class WVUCPrecacheManagerHolder {
        static final WVUCPrecacheManager sInstance = new WVUCPrecacheManager();

        private WVUCPrecacheManagerHolder() {
        }
    }

    public static WVUCPrecacheManager getInstance() {
        return WVUCPrecacheManagerHolder.sInstance;
    }

    private WVUCPrecacheManager() {
        this.mPrecacheHandler = null;
        init();
    }

    private void init() {
        WVEventService.getInstance().addEventListener(this);
    }

    public static void resetClearConfig() {
        sCanClearByCommonConfig = false;
        sCanClearByZcacheUpdate = false;
    }

    public static void resetPrecacheConfig() {
        sCanPrecacheByCommonConfig = false;
        sCanPrecacheByZcacheUpdate = false;
    }

    public WVEventResult onEvent(int i, WVEventContext wVEventContext, Object... objArr) {
        if (i == 6008) {
            notifyUpdateZcache(objArr[0]);
            return null;
        } else if (i != 6012) {
            return null;
        } else {
            notifyUpdateCommonConfig();
            return null;
        }
    }

    private static void notifyUpdateZcache(String str) {
        if (!TextUtils.isEmpty(str) && str.equals(sLastPrecachePackageName)) {
            sCanClearByZcacheUpdate = true;
            sCanPrecacheByZcacheUpdate = true;
            updatePreMemCacheUrls();
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:11:0x0029  */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x003e  */
    /* JADX WARNING: Removed duplicated region for block: B:21:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void notifyUpdateCommonConfig() {
        /*
            android.taobao.windvane.config.WVCommonConfigData r0 = android.taobao.windvane.config.WVCommonConfig.commonConfig
            boolean r0 = r0.enableUCPrecache
            android.taobao.windvane.config.WVCommonConfigData r1 = android.taobao.windvane.config.WVCommonConfig.commonConfig
            java.lang.String r1 = r1.precachePackageName
            boolean r2 = sLastEnableUCPrecache
            r3 = 0
            r4 = 1
            if (r2 == 0) goto L_0x0016
            if (r0 != 0) goto L_0x0016
            sCanClearByCommonConfig = r4
            sCanPrecacheByCommonConfig = r3
        L_0x0014:
            r3 = 1
            goto L_0x0021
        L_0x0016:
            boolean r2 = sLastEnableUCPrecache
            if (r2 != 0) goto L_0x0021
            if (r0 == 0) goto L_0x0021
            sCanClearByCommonConfig = r4
            sCanPrecacheByCommonConfig = r4
            goto L_0x0014
        L_0x0021:
            java.lang.String r2 = sLastPrecachePackageName
            boolean r2 = r2.equals(r1)
            if (r2 != 0) goto L_0x0034
            sCanClearByCommonConfig = r4
            boolean r2 = android.text.TextUtils.isEmpty(r1)
            if (r2 != 0) goto L_0x0033
            sCanPrecacheByCommonConfig = r4
        L_0x0033:
            r3 = 1
        L_0x0034:
            sLastEnableUCPrecache = r0
            sLastPrecachePackageName = r1
            if (r3 != 0) goto L_0x003e
            boolean r0 = sHasInitUrlSet
            if (r0 != 0) goto L_0x0041
        L_0x003e:
            updatePreMemCacheUrls()
        L_0x0041:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.config.WVUCPrecacheManager.notifyUpdateCommonConfig():void");
    }

    private static void updatePreMemCacheUrls() {
        if (!WVCommonConfig.commonConfig.enableUCPrecache || TextUtils.isEmpty(sLastPrecachePackageName)) {
            sPreMemCacheUrlSet = new HashSet<>();
            return;
        }
        WVEventResult onEvent = WVEventService.getInstance().onEvent(6011, sLastPrecachePackageName);
        if (onEvent.isSuccess && onEvent.resultObj != null && (onEvent.resultObj instanceof HashSet)) {
            sPreMemCacheUrlSet = (HashSet) onEvent.resultObj;
            sHasInitUrlSet = true;
        }
    }

    public static boolean canClearPrecache() {
        if (!sHasPrecache) {
            return false;
        }
        if (sCanClearByCommonConfig || sCanClearByZcacheUpdate) {
            return true;
        }
        if (sLastPrecacheTime <= 0 || System.currentTimeMillis() - sLastPrecacheTime <= sMaxPrecacheTime) {
            return false;
        }
        return true;
    }

    public static boolean canPrecache() {
        if (!WVCommonConfig.commonConfig.enableUCPrecache || TextUtils.isEmpty(sLastPrecachePackageName)) {
            return false;
        }
        if (!sHasInitUrlSet) {
            sHasInitUrlSet = true;
            updatePreMemCacheUrls();
        }
        if (sPreMemCacheUrlSet == null || sPreMemCacheUrlSet.size() <= 0) {
            return false;
        }
        if (sCanPrecacheByCommonConfig || sCanPrecacheByZcacheUpdate || !sHasPrecache) {
            return true;
        }
        return false;
    }

    public static void setHasPrecache(boolean z) {
        sHasPrecache = z;
        if (z) {
            sLastPrecacheTime = System.currentTimeMillis();
        } else {
            sLastPrecacheTime = -1;
        }
    }

    public static HashSet<String> preMemCacheUrlSet() {
        return sPreMemCacheUrlSet;
    }

    public boolean canPrecacheDoc(String str) {
        if (!WVCommonConfig.commonConfig.enableUCPrecacheDoc || TextUtils.isEmpty(str)) {
            return false;
        }
        if (sPreMemCacheUrlSet.isEmpty()) {
            return true;
        }
        Iterator<String> it = sPreMemCacheUrlSet.iterator();
        while (it.hasNext()) {
            if (str.startsWith(it.next())) {
                return false;
            }
        }
        return true;
    }

    public boolean hasPrecacheDoc(String str) {
        if (str.indexOf("#") > 0) {
            str = str.substring(0, str.indexOf("#"));
        }
        return sPrecacheDocResMap.contains(str);
    }

    public void addPrecacheDoc(String str) {
        if (!TextUtils.isEmpty(str)) {
            if (str.indexOf("#") > 0) {
                str = str.substring(0, str.indexOf("#"));
            }
            sPrecacheDocResMap.add(str);
            sendClearPrecacheDocMessage(str);
        }
    }

    public void clearPrecacheDoc(String str) {
        if (str.indexOf("#") > 0) {
            str = str.substring(0, str.indexOf("#"));
        }
        sPrecacheDocResMap.remove(str);
    }

    private void sendClearPrecacheDocMessage(final String str) {
        if (sPrecacheDocResMap.size() > 0) {
            if (this.mPrecacheHandler == null) {
                this.mPrecacheHandler = new Handler();
            }
            this.mPrecacheHandler.postDelayed(new Runnable() {
                public void run() {
                    WVUCPrecacheManager.this.clearPrecacheDoc(str);
                }
            }, 10000);
        }
    }
}
