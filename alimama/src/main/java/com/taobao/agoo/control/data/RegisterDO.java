package com.taobao.agoo.control.data;

import com.taobao.accs.common.Constants;
import com.taobao.accs.utl.ALog;
import com.taobao.accs.utl.JsonUtility;

public class RegisterDO extends BaseDO {
    public static final String JSON_CMD_REGISTER = "register";
    private static final String TAG = "RegisterDO";
    public String appKey;
    public String appVersion;
    public String c0;
    public String c1;
    public String c2;
    public String c3;
    public String c4;
    public String c5;
    public String c6;
    public String notifyEnable;
    public String packageName;
    public String romInfo;
    public String sdkVersion = String.valueOf(Constants.SDK_VERSION_CODE);
    public String ttid;
    public String utdid;

    public byte[] buildData() {
        try {
            String jSONObject = new JsonUtility.JsonObjectBuilder().put(BaseDO.JSON_CMD, this.cmd).put("appKey", this.appKey).put("utdid", this.utdid).put("appVersion", this.appVersion).put("sdkVersion", this.sdkVersion).put("ttid", this.ttid).put("packageName", this.packageName).put("notifyEnable", this.notifyEnable).put("romInfo", this.romInfo).put("c0", this.c0).put("c1", this.c1).put("c2", this.c2).put("c3", this.c3).put("c4", this.c4).put("c5", this.c5).put("c6", this.c6).build().toString();
            ALog.i(TAG, "buildData", "data", jSONObject);
            return jSONObject.getBytes("utf-8");
        } catch (Throwable th) {
            ALog.e(TAG, "buildData", th, new Object[0]);
            return null;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:37:0x00b8  */
    /* JADX WARNING: Removed duplicated region for block: B:40:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static byte[] buildRegister(android.content.Context r8, java.lang.String r9, java.lang.String r10) {
        /*
            r0 = 0
            r1 = 1
            r2 = 0
            java.lang.String r3 = com.taobao.accs.utl.UtilityImpl.getDeviceId(r8)     // Catch:{ Throwable -> 0x009d, all -> 0x009a }
            java.lang.String r4 = r8.getPackageName()     // Catch:{ Throwable -> 0x009d, all -> 0x009a }
            com.taobao.accs.client.GlobalClientInfo r5 = com.taobao.accs.client.GlobalClientInfo.getInstance(r8)     // Catch:{ Throwable -> 0x009d, all -> 0x009a }
            android.content.pm.PackageInfo r5 = r5.getPackageInfo()     // Catch:{ Throwable -> 0x009d, all -> 0x009a }
            java.lang.String r5 = r5.versionName     // Catch:{ Throwable -> 0x009d, all -> 0x009a }
            boolean r6 = android.text.TextUtils.isEmpty(r9)     // Catch:{ Throwable -> 0x009d, all -> 0x009a }
            if (r6 != 0) goto L_0x0079
            boolean r6 = android.text.TextUtils.isEmpty(r3)     // Catch:{ Throwable -> 0x009d, all -> 0x009a }
            if (r6 != 0) goto L_0x0079
            boolean r6 = android.text.TextUtils.isEmpty(r5)     // Catch:{ Throwable -> 0x009d, all -> 0x009a }
            if (r6 == 0) goto L_0x0028
            goto L_0x0079
        L_0x0028:
            com.taobao.agoo.control.data.RegisterDO r6 = new com.taobao.agoo.control.data.RegisterDO     // Catch:{ Throwable -> 0x009d, all -> 0x009a }
            r6.<init>()     // Catch:{ Throwable -> 0x009d, all -> 0x009a }
            java.lang.String r7 = "register"
            r6.cmd = r7     // Catch:{ Throwable -> 0x0077 }
            r6.appKey = r9     // Catch:{ Throwable -> 0x0077 }
            r6.utdid = r3     // Catch:{ Throwable -> 0x0077 }
            r6.appVersion = r5     // Catch:{ Throwable -> 0x0077 }
            r6.ttid = r10     // Catch:{ Throwable -> 0x0077 }
            r6.packageName = r4     // Catch:{ Throwable -> 0x0077 }
            java.lang.String r9 = android.os.Build.BRAND     // Catch:{ Throwable -> 0x0077 }
            r6.c0 = r9     // Catch:{ Throwable -> 0x0077 }
            java.lang.String r9 = android.os.Build.MODEL     // Catch:{ Throwable -> 0x0077 }
            r6.c1 = r9     // Catch:{ Throwable -> 0x0077 }
            java.lang.String r9 = com.taobao.accs.utl.AdapterUtilityImpl.isNotificationEnabled(r8)     // Catch:{ Throwable -> 0x0077 }
            r6.notifyEnable = r9     // Catch:{ Throwable -> 0x0077 }
            java.lang.String r9 = "ACCS_SDK_CHANNEL"
            java.lang.String r10 = r6.notifyEnable     // Catch:{ Throwable -> 0x0077 }
            com.taobao.accs.utl.UtilityImpl.saveNotificationState(r8, r9, r10)     // Catch:{ Throwable -> 0x0077 }
            com.taobao.accs.utl.RomInfoCollecter r9 = com.taobao.accs.utl.RomInfoCollecter.getCollecter()     // Catch:{ Throwable -> 0x0077 }
            java.lang.String r9 = r9.collect()     // Catch:{ Throwable -> 0x0077 }
            r6.romInfo = r9     // Catch:{ Throwable -> 0x0077 }
            java.lang.String r9 = "phone"
            java.lang.Object r8 = r8.getSystemService(r9)     // Catch:{ Throwable -> 0x0077 }
            android.telephony.TelephonyManager r8 = (android.telephony.TelephonyManager) r8     // Catch:{ Throwable -> 0x0077 }
            if (r8 == 0) goto L_0x0069
            java.lang.String r9 = r8.getDeviceId()     // Catch:{ Throwable -> 0x0077 }
            goto L_0x006a
        L_0x0069:
            r9 = r2
        L_0x006a:
            r6.c2 = r9     // Catch:{ Throwable -> 0x0077 }
            if (r8 == 0) goto L_0x0073
            java.lang.String r8 = r8.getSubscriberId()     // Catch:{ Throwable -> 0x0077 }
            goto L_0x0074
        L_0x0073:
            r8 = r2
        L_0x0074:
            r6.c3 = r8     // Catch:{ Throwable -> 0x0077 }
            goto L_0x00b0
        L_0x0077:
            r8 = move-exception
            goto L_0x009f
        L_0x0079:
            java.lang.String r8 = "RegisterDO"
            java.lang.String r10 = "buildRegister param null"
            r4 = 6
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch:{ Throwable -> 0x009d, all -> 0x009a }
            java.lang.String r6 = "appKey"
            r4[r0] = r6     // Catch:{ Throwable -> 0x009d, all -> 0x009a }
            r4[r1] = r9     // Catch:{ Throwable -> 0x009d, all -> 0x009a }
            r9 = 2
            java.lang.String r6 = "utdid"
            r4[r9] = r6     // Catch:{ Throwable -> 0x009d, all -> 0x009a }
            r9 = 3
            r4[r9] = r3     // Catch:{ Throwable -> 0x009d, all -> 0x009a }
            r9 = 4
            java.lang.String r3 = "appVersion"
            r4[r9] = r3     // Catch:{ Throwable -> 0x009d, all -> 0x009a }
            r9 = 5
            r4[r9] = r5     // Catch:{ Throwable -> 0x009d, all -> 0x009a }
            com.taobao.accs.utl.ALog.e(r8, r10, r4)     // Catch:{ Throwable -> 0x009d, all -> 0x009a }
            return r2
        L_0x009a:
            r8 = move-exception
            r6 = r2
            goto L_0x00b6
        L_0x009d:
            r8 = move-exception
            r6 = r2
        L_0x009f:
            java.lang.String r9 = "RegisterDO"
            java.lang.String r10 = "buildRegister"
            java.lang.Object[] r1 = new java.lang.Object[r1]     // Catch:{ all -> 0x00b5 }
            java.lang.String r8 = r8.getMessage()     // Catch:{ all -> 0x00b5 }
            r1[r0] = r8     // Catch:{ all -> 0x00b5 }
            com.taobao.accs.utl.ALog.w(r9, r10, r1)     // Catch:{ all -> 0x00b5 }
            if (r6 == 0) goto L_0x00b4
        L_0x00b0:
            byte[] r2 = r6.buildData()
        L_0x00b4:
            return r2
        L_0x00b5:
            r8 = move-exception
        L_0x00b6:
            if (r6 == 0) goto L_0x00bb
            r6.buildData()
        L_0x00bb:
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.agoo.control.data.RegisterDO.buildRegister(android.content.Context, java.lang.String, java.lang.String):byte[]");
    }
}
