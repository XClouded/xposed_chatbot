package android.taobao.windvane.config;

import android.taobao.windvane.connect.api.ApiResponse;
import android.taobao.windvane.util.ConfigStorage;
import android.text.TextUtils;
import android.webkit.CookieManager;

public class WVConfigUtils {
    public static final String SPNAME = TAG;
    protected static final String TAG = "WVConfigUtils";
    private static String appVersion = null;
    private static boolean isAppKeyAvailable = false;
    private static boolean isAppKeyChecked = false;

    public static boolean isNeedUpdate(boolean z, String str, String str2) {
        long currentTimeMillis = System.currentTimeMillis() - ConfigStorage.getLongVal(str, str2 + ConfigStorage.KEY_TIME);
        return currentTimeMillis > (z ? ConfigStorage.DEFAULT_SMALL_MAX_AGE : ConfigStorage.DEFAULT_MAX_AGE) || currentTimeMillis < 0;
    }

    /* access modifiers changed from: protected */
    public boolean needSaveConfig(String str) {
        if (!TextUtils.isEmpty(str) && new ApiResponse().parseJsonResult(str).success) {
            return true;
        }
        return false;
    }

    public static boolean isNeedUpdate(String str, String str2) {
        String str3;
        Long l;
        Long.valueOf(0);
        if (str.contains(".")) {
            String[] split = str.split("\\.");
            if (split == null || split.length < 2) {
                return false;
            }
            l = Long.valueOf(Long.parseLong(split[0]));
            str3 = split[1];
        } else {
            l = Long.valueOf(Long.parseLong(str));
            str3 = null;
        }
        String[] split2 = ConfigStorage.getStringVal(WVConfigManager.SPNAME_CONFIG, str2, "0").split("\\.");
        if (l.longValue() > Long.parseLong(split2[0])) {
            return true;
        }
        if (l.longValue() == Long.parseLong(split2[0])) {
            if (str3 != null) {
                if ((split2.length <= 1 || str3.equals(split2[1])) && split2.length >= 2) {
                    return false;
                }
                return true;
            } else if (split2.length > 1) {
                return true;
            }
        }
        return false;
    }

    public static String getTargetValue() {
        char c;
        try {
            CookieManager instance = CookieManager.getInstance();
            String cookie = instance.getCookie("h5." + GlobalConfig.env.getValue() + ".taobao.com");
            if (TextUtils.isEmpty(cookie)) {
                return null;
            }
            int indexOf = cookie.indexOf("abt=");
            if (indexOf == -1) {
                c = 'a';
            } else {
                c = cookie.charAt(indexOf + 4);
            }
            if ('a' > c || c > 'z') {
                return null;
            }
            String valueOf = String.valueOf(c);
            if (!valueOf.equals(ConfigStorage.getStringVal(WVConfigManager.SPNAME_CONFIG, "abt", ""))) {
                ConfigStorage.putStringVal(WVConfigManager.SPNAME_CONFIG, "abt", valueOf);
            }
            return valueOf;
        } catch (Exception unused) {
            return null;
        }
    }

    public static synchronized boolean checkAppKeyAvailable() {
        synchronized (WVConfigUtils.class) {
            if (isAppKeyChecked) {
                boolean z = isAppKeyAvailable;
                return z;
            }
            isAppKeyChecked = true;
            String appKey = GlobalConfig.getInstance().getAppKey();
            if (appKey == null) {
                isAppKeyAvailable = false;
                isAppKeyChecked = false;
                boolean z2 = isAppKeyAvailable;
                return z2;
            }
            char[] charArray = appKey.toCharArray();
            int i = 0;
            while (i < charArray.length) {
                if (charArray[i] >= '0') {
                    if (charArray[i] <= '9') {
                        i++;
                    }
                }
                isAppKeyAvailable = false;
                boolean z3 = isAppKeyAvailable;
                return z3;
            }
            isAppKeyAvailable = true;
            boolean z4 = isAppKeyAvailable;
            return z4;
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(6:2|3|(2:5|6)|7|8|9) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0020 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized java.lang.String dealAppVersion() {
        /*
            java.lang.Class<android.taobao.windvane.config.WVConfigUtils> r0 = android.taobao.windvane.config.WVConfigUtils.class
            monitor-enter(r0)
            java.lang.String r1 = appVersion     // Catch:{ all -> 0x0024 }
            if (r1 != 0) goto L_0x0020
            android.taobao.windvane.config.GlobalConfig r1 = android.taobao.windvane.config.GlobalConfig.getInstance()     // Catch:{ Exception -> 0x0020 }
            java.lang.String r1 = r1.getAppVersion()     // Catch:{ Exception -> 0x0020 }
            java.lang.String r2 = "utf-8"
            java.lang.String r1 = java.net.URLEncoder.encode(r1, r2)     // Catch:{ Exception -> 0x0020 }
            appVersion = r1     // Catch:{ Exception -> 0x0020 }
            java.lang.String r1 = appVersion     // Catch:{ Exception -> 0x0020 }
            java.lang.String r2 = "-"
            java.lang.String r3 = "%2D"
            r1.replace(r2, r3)     // Catch:{ Exception -> 0x0020 }
        L_0x0020:
            java.lang.String r1 = appVersion     // Catch:{ all -> 0x0024 }
            monitor-exit(r0)
            return r1
        L_0x0024:
            r1 = move-exception
            monitor-exit(r0)
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.config.WVConfigUtils.dealAppVersion():java.lang.String");
    }
}
