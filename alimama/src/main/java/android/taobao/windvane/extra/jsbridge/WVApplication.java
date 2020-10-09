package android.taobao.windvane.extra.jsbridge;

import android.os.Build;
import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.jsbridge.WVResult;
import android.taobao.windvane.jsbridge.utils.WVUtils;

public class WVApplication extends WVApiPlugin {
    public boolean execute(String str, String str2, WVCallBackContext wVCallBackContext) {
        if ("getNotificationSettings".equals(str)) {
            getNotificationSettings(wVCallBackContext, str2);
            return true;
        } else if (!"openSettings".equals(str)) {
            return false;
        } else {
            openSettings(wVCallBackContext, str2);
            return true;
        }
    }

    private void getNotificationSettings(WVCallBackContext wVCallBackContext, String str) {
        WVResult wVResult = new WVResult();
        if (Build.VERSION.SDK_INT < 22) {
            wVResult.addData("status", "unknown");
            wVCallBackContext.success(wVResult);
        } else if (!WVUtils.isNotificationEnabled(this.mContext)) {
            wVResult.addData("status", "denied");
            wVCallBackContext.success(wVResult);
        } else {
            wVResult.addData("status", "authorized");
            wVCallBackContext.success(wVResult);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x009a, code lost:
        r4.error();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:4:?, code lost:
        r4.error(new android.taobao.windvane.jsbridge.WVResult("HY_PARAM_ERR"));
        r5 = null;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x000f */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void openSettings(android.taobao.windvane.jsbridge.WVCallBackContext r4, java.lang.String r5) {
        /*
            r3 = this;
            r0 = 0
            org.json.JSONObject r1 = new org.json.JSONObject     // Catch:{ JSONException -> 0x000f }
            r1.<init>(r5)     // Catch:{ JSONException -> 0x000f }
            java.lang.String r5 = "type"
            java.lang.String r2 = ""
            java.lang.String r5 = r1.optString(r5, r2)     // Catch:{ JSONException -> 0x000f }
            goto L_0x001a
        L_0x000f:
            android.taobao.windvane.jsbridge.WVResult r5 = new android.taobao.windvane.jsbridge.WVResult     // Catch:{ Throwable -> 0x009a }
            java.lang.String r1 = "HY_PARAM_ERR"
            r5.<init>(r1)     // Catch:{ Throwable -> 0x009a }
            r4.error((android.taobao.windvane.jsbridge.WVResult) r5)     // Catch:{ Throwable -> 0x009a }
            r5 = r0
        L_0x001a:
            java.lang.String r1 = "Notification"
            boolean r5 = r1.equals(r5)     // Catch:{ Throwable -> 0x009a }
            if (r5 == 0) goto L_0x0067
            int r5 = android.os.Build.VERSION.SDK_INT     // Catch:{ Throwable -> 0x009a }
            r0 = 21
            if (r5 < r0) goto L_0x0057
            android.content.Intent r5 = new android.content.Intent     // Catch:{ Throwable -> 0x009a }
            r5.<init>()     // Catch:{ Throwable -> 0x009a }
            java.lang.String r0 = "android.settings.APP_NOTIFICATION_SETTINGS"
            r5.setAction(r0)     // Catch:{ Throwable -> 0x009a }
            java.lang.String r0 = "app_package"
            android.content.Context r1 = r3.mContext     // Catch:{ Throwable -> 0x009a }
            java.lang.String r1 = r1.getPackageName()     // Catch:{ Throwable -> 0x009a }
            r5.putExtra(r0, r1)     // Catch:{ Throwable -> 0x009a }
            java.lang.String r0 = "app_uid"
            android.content.Context r1 = r3.mContext     // Catch:{ Throwable -> 0x009a }
            android.content.pm.ApplicationInfo r1 = r1.getApplicationInfo()     // Catch:{ Throwable -> 0x009a }
            int r1 = r1.uid     // Catch:{ Throwable -> 0x009a }
            r5.putExtra(r0, r1)     // Catch:{ Throwable -> 0x009a }
            android.content.Context r0 = r3.mContext     // Catch:{ Throwable -> 0x009a }
            if (r0 == 0) goto L_0x0057
            android.content.Context r0 = r3.mContext     // Catch:{ Throwable -> 0x009a }
            r0.startActivity(r5)     // Catch:{ Throwable -> 0x009a }
            r4.success()     // Catch:{ Throwable -> 0x009a }
            return
        L_0x0057:
            android.taobao.windvane.jsbridge.WVResult r5 = new android.taobao.windvane.jsbridge.WVResult     // Catch:{ Throwable -> 0x009a }
            r5.<init>()     // Catch:{ Throwable -> 0x009a }
            java.lang.String r0 = "msg"
            java.lang.String r1 = "fail to open Notification Settings"
            r5.addData((java.lang.String) r0, (java.lang.String) r1)     // Catch:{ Throwable -> 0x009a }
            r4.error((android.taobao.windvane.jsbridge.WVResult) r5)     // Catch:{ Throwable -> 0x009a }
            goto L_0x009d
        L_0x0067:
            android.content.Intent r5 = new android.content.Intent     // Catch:{ Throwable -> 0x009a }
            java.lang.String r1 = "android.settings.APPLICATION_DETAILS_SETTINGS"
            r5.<init>(r1)     // Catch:{ Throwable -> 0x009a }
            android.content.Context r1 = r3.mContext     // Catch:{ Throwable -> 0x009a }
            if (r1 == 0) goto L_0x008a
            java.lang.String r1 = "package"
            android.content.Context r2 = r3.mContext     // Catch:{ Throwable -> 0x009a }
            java.lang.String r2 = r2.getPackageName()     // Catch:{ Throwable -> 0x009a }
            android.net.Uri r0 = android.net.Uri.fromParts(r1, r2, r0)     // Catch:{ Throwable -> 0x009a }
            r5.setData(r0)     // Catch:{ Throwable -> 0x009a }
            android.content.Context r0 = r3.mContext     // Catch:{ Throwable -> 0x009a }
            r0.startActivity(r5)     // Catch:{ Throwable -> 0x009a }
            r4.success()     // Catch:{ Throwable -> 0x009a }
            return
        L_0x008a:
            android.taobao.windvane.jsbridge.WVResult r5 = new android.taobao.windvane.jsbridge.WVResult     // Catch:{ Throwable -> 0x009a }
            r5.<init>()     // Catch:{ Throwable -> 0x009a }
            java.lang.String r0 = "msg"
            java.lang.String r1 = "fail to open Application Settings"
            r5.addData((java.lang.String) r0, (java.lang.String) r1)     // Catch:{ Throwable -> 0x009a }
            r4.error((android.taobao.windvane.jsbridge.WVResult) r5)     // Catch:{ Throwable -> 0x009a }
            goto L_0x009d
        L_0x009a:
            r4.error()
        L_0x009d:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.extra.jsbridge.WVApplication.openSettings(android.taobao.windvane.jsbridge.WVCallBackContext, java.lang.String):void");
    }
}
