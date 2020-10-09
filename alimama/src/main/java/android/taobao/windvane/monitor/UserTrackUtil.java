package android.taobao.windvane.monitor;

import android.taobao.windvane.config.WVCommonConfig;
import android.taobao.windvane.util.TaoLog;
import android.text.TextUtils;

import java.lang.reflect.Method;
import java.util.Map;

import mtopsdk.common.util.SymbolExpUtil;

public class UserTrackUtil {
    public static final int EVENTID_DEV_STORAGE = 15307;
    public static final int EVENTID_ERROR = 15306;
    public static final int EVENTID_MONITOR = 15301;
    public static final int EVENTID_PA_APPS = 15305;
    public static final int EVENTID_PA_SAPP = 15303;
    public static final int EVENTID_PA_UCSDK = 15309;
    public static boolean OFF = false;
    private static final String TAG = "UserTrackUtil";
    private static boolean isInit = false;
    private static Method utCommitEvent;
    private static Method utCommitEventWithArgs;
    private static Method utCommitPage;

    public static void init() {
        if (!isInit) {
            try {
                Class<?> cls = Class.forName("com.taobao.statistic.TBS$Ext");
                utCommitEvent = cls.getDeclaredMethod("commitEvent", new Class[]{Integer.TYPE, Object.class, Object.class, Object.class});
                utCommitPage = cls.getDeclaredMethod("commitEvent", new Class[]{String.class, Integer.TYPE, Object.class, Object.class, Object.class, String[].class});
                utCommitEventWithArgs = cls.getDeclaredMethod("commitEvent", new Class[]{Integer.TYPE, Object.class, Object.class, Object.class, String[].class});
                isInit = true;
            } catch (ClassNotFoundException unused) {
                TaoLog.d(TAG, "UT class not found");
            } catch (NoSuchMethodException unused2) {
                TaoLog.d(TAG, "UT method not found");
            }
        }
    }

    public static void commitEvent(int i, String str, String str2, String str3) {
        if (utCommitEvent != null && WVCommonConfig.commonConfig.monitorStatus != 0) {
            try {
                if (TaoLog.getLogStatus()) {
                    TaoLog.d(TAG, "commitEvent: " + i + "||" + str + "||" + str2 + "||" + str3);
                }
                utCommitEvent.invoke((Object) null, new Object[]{Integer.valueOf(i), str, str2, str3});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v1, resolved type: java.lang.Object[]} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void commitEvent(java.lang.String r5, int r6, java.lang.String r7, java.lang.String r8, java.lang.String r9, java.lang.String r10) {
        /*
            java.lang.reflect.Method r0 = utCommitPage
            if (r0 == 0) goto L_0x007b
            android.taobao.windvane.config.WVCommonConfigData r0 = android.taobao.windvane.config.WVCommonConfig.commonConfig
            int r0 = r0.monitorStatus
            if (r0 != 0) goto L_0x000b
            goto L_0x007b
        L_0x000b:
            boolean r0 = android.taobao.windvane.util.TaoLog.getLogStatus()     // Catch:{ Exception -> 0x0076 }
            r1 = 1
            r2 = 0
            if (r0 == 0) goto L_0x0054
            java.lang.String r0 = "UserTrackUtil"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0076 }
            r3.<init>()     // Catch:{ Exception -> 0x0076 }
            java.lang.String r4 = "commitEvent: "
            r3.append(r4)     // Catch:{ Exception -> 0x0076 }
            r3.append(r5)     // Catch:{ Exception -> 0x0076 }
            java.lang.String r4 = "||"
            r3.append(r4)     // Catch:{ Exception -> 0x0076 }
            r3.append(r6)     // Catch:{ Exception -> 0x0076 }
            java.lang.String r4 = "||"
            r3.append(r4)     // Catch:{ Exception -> 0x0076 }
            r3.append(r7)     // Catch:{ Exception -> 0x0076 }
            java.lang.String r4 = "||"
            r3.append(r4)     // Catch:{ Exception -> 0x0076 }
            r3.append(r8)     // Catch:{ Exception -> 0x0076 }
            java.lang.String r4 = "||"
            r3.append(r4)     // Catch:{ Exception -> 0x0076 }
            r3.append(r9)     // Catch:{ Exception -> 0x0076 }
            java.lang.CharSequence[] r4 = new java.lang.CharSequence[r1]     // Catch:{ Exception -> 0x0076 }
            r4[r2] = r10     // Catch:{ Exception -> 0x0076 }
            java.lang.CharSequence r4 = android.text.TextUtils.concat(r4)     // Catch:{ Exception -> 0x0076 }
            r3.append(r4)     // Catch:{ Exception -> 0x0076 }
            java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x0076 }
            android.taobao.windvane.util.TaoLog.d(r0, r3)     // Catch:{ Exception -> 0x0076 }
        L_0x0054:
            java.lang.reflect.Method r0 = utCommitPage     // Catch:{ Exception -> 0x0076 }
            r3 = 0
            r4 = 6
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch:{ Exception -> 0x0076 }
            r4[r2] = r5     // Catch:{ Exception -> 0x0076 }
            java.lang.Integer r5 = java.lang.Integer.valueOf(r6)     // Catch:{ Exception -> 0x0076 }
            r4[r1] = r5     // Catch:{ Exception -> 0x0076 }
            r5 = 2
            r4[r5] = r7     // Catch:{ Exception -> 0x0076 }
            r5 = 3
            r4[r5] = r8     // Catch:{ Exception -> 0x0076 }
            r5 = 4
            r4[r5] = r9     // Catch:{ Exception -> 0x0076 }
            r5 = 5
            java.lang.String[] r6 = new java.lang.String[r1]     // Catch:{ Exception -> 0x0076 }
            r6[r2] = r10     // Catch:{ Exception -> 0x0076 }
            r4[r5] = r6     // Catch:{ Exception -> 0x0076 }
            r0.invoke(r3, r4)     // Catch:{ Exception -> 0x0076 }
            goto L_0x007a
        L_0x0076:
            r5 = move-exception
            r5.printStackTrace()
        L_0x007a:
            return
        L_0x007b:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.monitor.UserTrackUtil.commitEvent(java.lang.String, int, java.lang.String, java.lang.String, java.lang.String, java.lang.String):void");
    }

    public static void commitEvent(int i, String str, String str2, String str3, String[] strArr) {
        if (utCommitEventWithArgs != null && WVCommonConfig.commonConfig.monitorStatus != 0) {
            try {
                if (TaoLog.getLogStatus()) {
                    TaoLog.d(TAG, "commitEvent: " + i + "||" + str + "||" + str2 + "||" + str3 + TextUtils.concat(strArr));
                }
                utCommitEventWithArgs.invoke((Object) null, new Object[]{Integer.valueOf(i), str, str2, str3, strArr});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String toArgString(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder(map.size() * 28);
        for (Map.Entry next : map.entrySet()) {
            String str = (String) next.getKey();
            if (!TextUtils.isEmpty(str)) {
                sb.append(str);
                sb.append(SymbolExpUtil.SYMBOL_EQUAL);
                sb.append(next.getValue());
                sb.append(",");
            }
        }
        return sb.substring(0, sb.length() - 1);
    }
}
