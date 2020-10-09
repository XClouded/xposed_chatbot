package android.taobao.windvane.jsbridge.api;

import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVCallBackContext;

public class WVUIToast extends WVApiPlugin {
    private static final String TAG = "WVUIToast";

    public boolean execute(String str, String str2, WVCallBackContext wVCallBackContext) {
        if (!"toast".equals(str)) {
            return false;
        }
        toast(wVCallBackContext, str2);
        return true;
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(10:22|21|25|26|27|28|(0)|34|35|36) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:25:0x0042 */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0055  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void toast(android.taobao.windvane.jsbridge.WVCallBackContext r7, java.lang.String r8) {
        /*
            r6 = this;
            monitor-enter(r6)
            android.content.Context r0 = r6.mContext     // Catch:{ all -> 0x006a }
            boolean r0 = android.taobao.windvane.jsbridge.utils.WVUtils.isNotificationEnabled(r0)     // Catch:{ all -> 0x006a }
            if (r0 != 0) goto L_0x001a
            android.taobao.windvane.jsbridge.WVResult r8 = new android.taobao.windvane.jsbridge.WVResult     // Catch:{ all -> 0x006a }
            r8.<init>()     // Catch:{ all -> 0x006a }
            java.lang.String r0 = "msg"
            java.lang.String r1 = "no permission"
            r8.addData((java.lang.String) r0, (java.lang.String) r1)     // Catch:{ all -> 0x006a }
            r7.error((android.taobao.windvane.jsbridge.WVResult) r8)     // Catch:{ all -> 0x006a }
            monitor-exit(r6)
            return
        L_0x001a:
            java.lang.String r0 = ""
            boolean r1 = android.text.TextUtils.isEmpty(r8)     // Catch:{ all -> 0x006a }
            r2 = 17
            r3 = 1
            r4 = 0
            if (r1 != 0) goto L_0x004e
            java.lang.String r1 = "utf-8"
            java.lang.String r1 = java.net.URLDecoder.decode(r8, r1)     // Catch:{ Exception -> 0x0041 }
            org.json.JSONObject r8 = new org.json.JSONObject     // Catch:{ Exception -> 0x0042 }
            r8.<init>(r1)     // Catch:{ Exception -> 0x0042 }
            java.lang.String r5 = "message"
            java.lang.String r5 = r8.optString(r5)     // Catch:{ Exception -> 0x0042 }
            java.lang.String r0 = "duration"
            int r8 = r8.optInt(r0)     // Catch:{ Exception -> 0x003f }
            r0 = r5
            goto L_0x004f
        L_0x003f:
            r0 = r5
            goto L_0x0042
        L_0x0041:
            r1 = r8
        L_0x0042:
            android.content.Context r8 = r6.mContext     // Catch:{ all -> 0x006a }
            android.widget.Toast r8 = android.widget.Toast.makeText(r8, r1, r3)     // Catch:{ all -> 0x006a }
            r8.setGravity(r2, r4, r4)     // Catch:{ all -> 0x006a }
            r8.show()     // Catch:{ all -> 0x006a }
        L_0x004e:
            r8 = 0
        L_0x004f:
            boolean r1 = android.text.TextUtils.isEmpty(r0)     // Catch:{ all -> 0x006a }
            if (r1 != 0) goto L_0x0065
            r1 = 3
            if (r8 <= r1) goto L_0x0059
            r8 = 1
        L_0x0059:
            android.content.Context r1 = r6.mContext     // Catch:{ all -> 0x006a }
            android.widget.Toast r8 = android.widget.Toast.makeText(r1, r0, r8)     // Catch:{ all -> 0x006a }
            r8.setGravity(r2, r4, r4)     // Catch:{ all -> 0x006a }
            r8.show()     // Catch:{ all -> 0x006a }
        L_0x0065:
            r7.success()     // Catch:{ all -> 0x006a }
            monitor-exit(r6)
            return
        L_0x006a:
            r7 = move-exception
            monitor-exit(r6)
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.jsbridge.api.WVUIToast.toast(android.taobao.windvane.jsbridge.WVCallBackContext, java.lang.String):void");
    }
}
