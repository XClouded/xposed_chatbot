package android.taobao.windvane.jsbridge.api;

import android.net.wifi.ScanResult;
import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVCallBackContext;

import java.util.List;

public class WVNetwork extends WVApiPlugin {
    public boolean execute(String str, String str2, WVCallBackContext wVCallBackContext) {
        if (!"getNetworkType".equals(str)) {
            return false;
        }
        getNetworkType(str2, wVCallBackContext);
        return true;
    }

    public StringBuffer lookUpScan(List<ScanResult> list) {
        StringBuffer stringBuffer = new StringBuffer();
        int i = 0;
        while (i < list.size()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Index_");
            int i2 = i + 1;
            sb.append(new Integer(i2).toString());
            sb.append(":");
            stringBuffer.append(sb.toString());
            stringBuffer.append(list.get(i).SSID.toString());
            stringBuffer.append("\n");
            i = i2;
        }
        return stringBuffer;
    }

    /* JADX WARNING: Removed duplicated region for block: B:11:0x001c A[SYNTHETIC, Splitter:B:11:0x001c] */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x005c A[SYNTHETIC, Splitter:B:22:0x005c] */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x00b8  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void getNetworkType(java.lang.String r7, android.taobao.windvane.jsbridge.WVCallBackContext r8) {
        /*
            r6 = this;
            android.taobao.windvane.jsbridge.WVResult r0 = new android.taobao.windvane.jsbridge.WVResult
            r0.<init>()
            r1 = 0
            org.json.JSONObject r2 = new org.json.JSONObject     // Catch:{ Exception -> 0x0018 }
            r2.<init>(r7)     // Catch:{ Exception -> 0x0018 }
            java.lang.String r7 = "wifiStatus"
            boolean r7 = r2.optBoolean(r7, r1)     // Catch:{ Exception -> 0x0018 }
            java.lang.String r3 = "wifiList"
            boolean r2 = r2.optBoolean(r3, r1)     // Catch:{ Exception -> 0x0019 }
            goto L_0x001a
        L_0x0018:
            r7 = 0
        L_0x0019:
            r2 = 0
        L_0x001a:
            if (r7 == 0) goto L_0x005a
            android.content.Context r7 = r6.mContext     // Catch:{ Throwable -> 0x0059 }
            java.lang.String r3 = "wifi"
            java.lang.Object r7 = r7.getSystemService(r3)     // Catch:{ Throwable -> 0x0059 }
            android.net.wifi.WifiManager r7 = (android.net.wifi.WifiManager) r7     // Catch:{ Throwable -> 0x0059 }
            android.net.wifi.WifiInfo r7 = r7.getConnectionInfo()     // Catch:{ Throwable -> 0x0059 }
            if (r7 == 0) goto L_0x005a
            java.lang.String r3 = r7.getSSID()     // Catch:{ Throwable -> 0x0059 }
            java.lang.String r4 = "\""
            boolean r4 = r3.startsWith(r4)     // Catch:{ Throwable -> 0x0059 }
            if (r4 == 0) goto L_0x004a
            java.lang.String r4 = "\""
            boolean r4 = r3.endsWith(r4)     // Catch:{ Throwable -> 0x0059 }
            if (r4 == 0) goto L_0x004a
            int r4 = r3.length()     // Catch:{ Throwable -> 0x0059 }
            r5 = 1
            int r4 = r4 - r5
            java.lang.String r3 = r3.substring(r5, r4)     // Catch:{ Throwable -> 0x0059 }
        L_0x004a:
            java.lang.String r4 = "ssid"
            r0.addData((java.lang.String) r4, (java.lang.String) r3)     // Catch:{ Throwable -> 0x0059 }
            java.lang.String r3 = "bssid"
            java.lang.String r7 = r7.getBSSID()     // Catch:{ Throwable -> 0x0059 }
            r0.addData((java.lang.String) r3, (java.lang.String) r7)     // Catch:{ Throwable -> 0x0059 }
            goto L_0x005a
        L_0x0059:
        L_0x005a:
            if (r2 == 0) goto L_0x00a4
            android.content.Context r7 = r6.mContext     // Catch:{ Throwable -> 0x00a3 }
            java.lang.String r2 = "wifi"
            java.lang.Object r7 = r7.getSystemService(r2)     // Catch:{ Throwable -> 0x00a3 }
            android.net.wifi.WifiManager r7 = (android.net.wifi.WifiManager) r7     // Catch:{ Throwable -> 0x00a3 }
            r7.startScan()     // Catch:{ Throwable -> 0x00a3 }
            org.json.JSONArray r2 = new org.json.JSONArray     // Catch:{ Throwable -> 0x00a3 }
            r2.<init>()     // Catch:{ Throwable -> 0x00a3 }
            java.util.List r7 = r7.getScanResults()     // Catch:{ Throwable -> 0x00a3 }
        L_0x0072:
            int r3 = r7.size()     // Catch:{ Throwable -> 0x00a3 }
            if (r1 >= r3) goto L_0x009d
            org.json.JSONObject r3 = new org.json.JSONObject     // Catch:{ Throwable -> 0x00a3 }
            r3.<init>()     // Catch:{ Throwable -> 0x00a3 }
            java.lang.String r4 = "ssid"
            java.lang.Object r5 = r7.get(r1)     // Catch:{ Throwable -> 0x00a3 }
            android.net.wifi.ScanResult r5 = (android.net.wifi.ScanResult) r5     // Catch:{ Throwable -> 0x00a3 }
            java.lang.String r5 = r5.SSID     // Catch:{ Throwable -> 0x00a3 }
            r3.put(r4, r5)     // Catch:{ Throwable -> 0x00a3 }
            java.lang.String r4 = "bssid"
            java.lang.Object r5 = r7.get(r1)     // Catch:{ Throwable -> 0x00a3 }
            android.net.wifi.ScanResult r5 = (android.net.wifi.ScanResult) r5     // Catch:{ Throwable -> 0x00a3 }
            java.lang.String r5 = r5.BSSID     // Catch:{ Throwable -> 0x00a3 }
            r3.put(r4, r5)     // Catch:{ Throwable -> 0x00a3 }
            r2.put(r3)     // Catch:{ Throwable -> 0x00a3 }
            int r1 = r1 + 1
            goto L_0x0072
        L_0x009d:
            java.lang.String r7 = "wifiList"
            r0.addData((java.lang.String) r7, (org.json.JSONArray) r2)     // Catch:{ Throwable -> 0x00a3 }
            goto L_0x00a4
        L_0x00a3:
        L_0x00a4:
            android.content.Context r7 = r6.mContext
            java.util.Map r7 = android.taobao.windvane.util.NetWork.getNetWorkSubTypeMap(r7)
            java.lang.String r1 = "type"
            java.lang.Object r1 = r7.get(r1)
            java.lang.CharSequence r1 = (java.lang.CharSequence) r1
            boolean r1 = android.text.TextUtils.isEmpty(r1)
            if (r1 != 0) goto L_0x00d2
            java.lang.String r1 = "message"
            java.lang.String r2 = "message"
            java.lang.Object r2 = r7.get(r2)
            java.lang.String r2 = (java.lang.String) r2
            r0.addData((java.lang.String) r1, (java.lang.String) r2)
            java.lang.String r1 = "type"
            java.lang.String r2 = "type"
            java.lang.Object r7 = r7.get(r2)
            java.lang.String r7 = (java.lang.String) r7
            r0.addData((java.lang.String) r1, (java.lang.String) r7)
        L_0x00d2:
            r8.success((android.taobao.windvane.jsbridge.WVResult) r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.jsbridge.api.WVNetwork.getNetworkType(java.lang.String, android.taobao.windvane.jsbridge.WVCallBackContext):void");
    }
}
