package com.alibaba.analytics.core.sync;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import com.alibaba.analytics.core.Variables;
import com.alibaba.analytics.core.config.timestamp.ConfigTimeStampMgr;
import com.alibaba.analytics.core.ipv6.TnetIpv6Manager;
import com.alibaba.analytics.core.model.LogField;
import com.alibaba.analytics.core.selfmonitor.SelfMonitorEvent;
import com.alibaba.analytics.core.selfmonitor.SelfMonitorEventDispather;
import com.alibaba.analytics.utils.AppInfoUtil;
import com.alibaba.analytics.utils.ByteUtils;
import com.alibaba.analytics.utils.GzipUtils;
import com.alibaba.analytics.utils.Logger;
import com.alibaba.analytics.utils.ReflectUtils;
import com.alibaba.analytics.utils.UTMCDevice;
import com.alibaba.analytics.utils.WuaHelper;
import com.alibaba.analytics.version.UTBuildInfo;
import com.taobao.weex.adapter.IWXUserTrackAdapter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.zip.GZIPOutputStream;
import mtopsdk.common.util.SymbolExpUtil;

public class BizRequest {
    private static final byte BYTE_ZERO = 0;
    private static final byte FLAGS_GET_CONFIG = 32;
    private static final byte FLAGS_GZIP = 1;
    private static final byte FLAGS_GZIP_FLUSH_DIC = 2;
    private static final byte FLAGS_KEEP_ALIVE = 8;
    private static final byte FLAGS_REAL_TIME_DEBUG = 16;
    private static final int HEAD_LENGTH = 8;
    private static final String LOG_SEPARATE = String.valueOf(1);
    private static final boolean NeedConfigByResponse = true;
    private static final boolean NeedMiniWua = true;
    private static final int PAYLOAD_MAX_LENGTH = 16777216;
    private static final int SplitNumber = 34;
    private static boolean bTestFlowGenterClsExist = false;
    private static Class flowClz = null;
    private static ByteArrayOutputStream mByteArrayOutputStream = null;
    private static GZIPOutputStream mGZIPOutputStream = null;
    static String mMiniWua = null;
    private static int mMiniWuaIndex = 0;
    public static final SelfMonitorEventDispather mMonitor = new SelfMonitorEventDispather();
    private static long mReceivedDataLen = 0;
    static String mResponseAdditionalData = null;

    public static byte[] getPackRequest(Map<String, String> map) throws Exception {
        return getPackRequest(map, 1);
    }

    static byte[] getPackRequestByRealtime(Map<String, String> map) throws Exception {
        return getPackRequest(map, 2);
    }

    static byte[] getPackRequest(Map<String, String> map, int i) throws Exception {
        byte[] bArr;
        int i2 = 2;
        int i3 = 1;
        if (Variables.getInstance().isGzipUpload() || Variables.getInstance().isHttpService()) {
            bArr = GzipUtils.gzip(getPayload(map));
            i2 = 1;
        } else {
            TnetUtil.initTnetStream();
            if (mGZIPOutputStream != null) {
                mGZIPOutputStream.write(getPayloadByDictZip(map));
                mGZIPOutputStream.flush();
                bArr = mByteArrayOutputStream.toByteArray();
                mByteArrayOutputStream.reset();
                i3 = 2;
            } else {
                bArr = GzipUtils.gzip(getPayloadByDictZip(map));
            }
        }
        if (bArr == null) {
            return null;
        }
        if (bArr.length >= 16777216) {
            if (Variables.getInstance().isSelfMonitorTurnOn()) {
                mMonitor.onEvent(SelfMonitorEvent.buildCountEvent(SelfMonitorEvent.DATALEN_OVERFLOW, String.valueOf(bArr.length), Double.valueOf(1.0d)));
            }
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(i2);
        byteArrayOutputStream.write(ByteUtils.intToBytes3(bArr.length));
        byteArrayOutputStream.write(i);
        byte b = (byte) (i3 | 8);
        if (Variables.getInstance().isRealTimeDebug()) {
            b = (byte) (b | FLAGS_REAL_TIME_DEBUG);
        }
        byteArrayOutputStream.write((byte) (b | FLAGS_GET_CONFIG));
        byteArrayOutputStream.write(0);
        byteArrayOutputStream.write(0);
        byteArrayOutputStream.write(bArr);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        try {
            byteArrayOutputStream.close();
        } catch (IOException e) {
            Logger.e((String) null, e, new Object[0]);
        }
        return byteArray;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:?, code lost:
        com.alibaba.analytics.utils.Logger.i("BizRequest", "EventId NumberFormatException. eventId", r3, ",eventLogs", r8.get(r3));
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x007c */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static byte[] getPayload(java.util.Map<java.lang.String, java.lang.String> r8) throws java.lang.Exception {
        /*
            java.io.ByteArrayOutputStream r0 = new java.io.ByteArrayOutputStream
            r0.<init>()
            java.lang.String r1 = getHead()
            r2 = 0
            if (r1 == 0) goto L_0x0026
            int r3 = r1.length()
            if (r3 <= 0) goto L_0x0026
            byte[] r3 = r1.getBytes()
            int r3 = r3.length
            byte[] r3 = com.alibaba.analytics.utils.ByteUtils.intToBytes2(r3)
            r0.write(r3)
            byte[] r1 = r1.getBytes()
            r0.write(r1)
            goto L_0x002d
        L_0x0026:
            byte[] r1 = com.alibaba.analytics.utils.ByteUtils.intToBytes2(r2)
            r0.write(r1)
        L_0x002d:
            if (r8 == 0) goto L_0x009c
            int r1 = r8.size()
            if (r1 <= 0) goto L_0x009c
            java.util.Set r1 = r8.keySet()
            java.util.Iterator r1 = r1.iterator()
        L_0x003d:
            boolean r3 = r1.hasNext()
            if (r3 == 0) goto L_0x009c
            java.lang.Object r3 = r1.next()
            java.lang.String r3 = (java.lang.String) r3
            java.lang.Integer r4 = java.lang.Integer.valueOf(r3)     // Catch:{ Exception -> 0x007c }
            int r4 = r4.intValue()     // Catch:{ Exception -> 0x007c }
            byte[] r4 = com.alibaba.analytics.utils.ByteUtils.intToBytes4(r4)
            r0.write(r4)
            java.lang.Object r3 = r8.get(r3)
            java.lang.String r3 = (java.lang.String) r3
            if (r3 == 0) goto L_0x0074
            byte[] r4 = r3.getBytes()
            int r4 = r4.length
            byte[] r4 = com.alibaba.analytics.utils.ByteUtils.intToBytes4(r4)
            r0.write(r4)
            byte[] r3 = r3.getBytes()
            r0.write(r3)
            goto L_0x003d
        L_0x0074:
            byte[] r3 = com.alibaba.analytics.utils.ByteUtils.intToBytes4(r2)
            r0.write(r3)
            goto L_0x003d
        L_0x007c:
            java.lang.Object r4 = r8.get(r3)     // Catch:{ Throwable -> 0x009a }
            java.lang.String r4 = (java.lang.String) r4     // Catch:{ Throwable -> 0x009a }
            java.lang.String r5 = "BizRequest"
            r6 = 4
            java.lang.Object[] r6 = new java.lang.Object[r6]     // Catch:{ Throwable -> 0x009a }
            java.lang.String r7 = "EventId NumberFormatException. eventId"
            r6[r2] = r7     // Catch:{ Throwable -> 0x009a }
            r7 = 1
            r6[r7] = r3     // Catch:{ Throwable -> 0x009a }
            r3 = 2
            java.lang.String r7 = ",eventLogs"
            r6[r3] = r7     // Catch:{ Throwable -> 0x009a }
            r3 = 3
            r6[r3] = r4     // Catch:{ Throwable -> 0x009a }
            com.alibaba.analytics.utils.Logger.i((java.lang.String) r5, (java.lang.Object[]) r6)     // Catch:{ Throwable -> 0x009a }
            goto L_0x003d
        L_0x009a:
            goto L_0x003d
        L_0x009c:
            byte[] r8 = r0.toByteArray()
            r0.close()     // Catch:{ IOException -> 0x00a4 }
            goto L_0x00a8
        L_0x00a4:
            r0 = move-exception
            r0.printStackTrace()
        L_0x00a8:
            return r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.analytics.core.sync.BizRequest.getPayload(java.util.Map):byte[]");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(3:27|28|43) */
    /* JADX WARNING: Code restructure failed: missing block: B:28:?, code lost:
        com.alibaba.analytics.utils.Logger.i("BizRequest", "EventId NumberFormatException. eventId", r3, ",eventLogs", r11.get(r3));
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:27:0x0096 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static byte[] getPayloadByDictZip(java.util.Map<java.lang.String, java.lang.String> r11) throws java.lang.Exception {
        /*
            java.io.ByteArrayOutputStream r0 = new java.io.ByteArrayOutputStream
            r0.<init>()
            java.lang.String r1 = getHead()
            byte[] r1 = com.alibaba.analytics.utils.ZipDictUtils.getHeadBytes(r1)
            r0.write(r1)
            if (r11 == 0) goto L_0x00c0
            int r1 = r11.size()
            if (r1 <= 0) goto L_0x00c0
            java.io.ByteArrayOutputStream r1 = new java.io.ByteArrayOutputStream
            r1.<init>()
            java.util.Set r2 = r11.keySet()
            java.util.Iterator r2 = r2.iterator()
        L_0x0025:
            boolean r3 = r2.hasNext()
            if (r3 == 0) goto L_0x00b8
            java.lang.Object r3 = r2.next()
            java.lang.String r3 = (java.lang.String) r3
            r4 = 0
            java.lang.Integer r5 = java.lang.Integer.valueOf(r3)     // Catch:{ Exception -> 0x0096 }
            int r5 = r5.intValue()     // Catch:{ Exception -> 0x0096 }
            byte[] r5 = com.alibaba.analytics.utils.ZipDictUtils.getLengthBytes(r5)
            r0.write(r5)
            java.lang.Object r3 = r11.get(r3)
            java.lang.String r3 = (java.lang.String) r3
            if (r3 == 0) goto L_0x0092
            java.lang.String r5 = LOG_SEPARATE
            java.lang.String[] r3 = r3.split(r5)
            int r5 = r3.length
            r6 = 0
        L_0x0051:
            if (r6 >= r5) goto L_0x007c
            r7 = r3[r6]
            boolean r8 = android.text.TextUtils.isEmpty(r7)
            if (r8 != 0) goto L_0x0079
            java.lang.String[] r7 = getSplitResult(r7)
            if (r7 == 0) goto L_0x0079
            r8 = 34
            int r9 = r7.length
            if (r8 != r9) goto L_0x0079
            int r8 = r7.length
            r9 = 0
        L_0x0068:
            if (r9 >= r8) goto L_0x0076
            r10 = r7[r9]
            byte[] r10 = com.alibaba.analytics.utils.ZipDictUtils.getBytes(r10)
            r1.write(r10)
            int r9 = r9 + 1
            goto L_0x0068
        L_0x0076:
            r1.write(r4)
        L_0x0079:
            int r6 = r6 + 1
            goto L_0x0051
        L_0x007c:
            int r3 = r1.size()
            byte[] r3 = com.alibaba.analytics.utils.ZipDictUtils.getLengthBytes(r3)
            r0.write(r3)
            byte[] r3 = r1.toByteArray()
            r0.write(r3)
            r1.reset()
            goto L_0x0025
        L_0x0092:
            r0.write(r4)
            goto L_0x0025
        L_0x0096:
            java.lang.Object r5 = r11.get(r3)     // Catch:{ Throwable -> 0x00b5 }
            java.lang.String r5 = (java.lang.String) r5     // Catch:{ Throwable -> 0x00b5 }
            java.lang.String r6 = "BizRequest"
            r7 = 4
            java.lang.Object[] r7 = new java.lang.Object[r7]     // Catch:{ Throwable -> 0x00b5 }
            java.lang.String r8 = "EventId NumberFormatException. eventId"
            r7[r4] = r8     // Catch:{ Throwable -> 0x00b5 }
            r4 = 1
            r7[r4] = r3     // Catch:{ Throwable -> 0x00b5 }
            r3 = 2
            java.lang.String r4 = ",eventLogs"
            r7[r3] = r4     // Catch:{ Throwable -> 0x00b5 }
            r3 = 3
            r7[r3] = r5     // Catch:{ Throwable -> 0x00b5 }
            com.alibaba.analytics.utils.Logger.i((java.lang.String) r6, (java.lang.Object[]) r7)     // Catch:{ Throwable -> 0x00b5 }
            goto L_0x0025
        L_0x00b5:
            goto L_0x0025
        L_0x00b8:
            r1.close()     // Catch:{ IOException -> 0x00bc }
            goto L_0x00c0
        L_0x00bc:
            r11 = move-exception
            r11.printStackTrace()
        L_0x00c0:
            byte[] r11 = r0.toByteArray()
            r0.close()     // Catch:{ IOException -> 0x00c8 }
            goto L_0x00cc
        L_0x00c8:
            r0 = move-exception
            r0.printStackTrace()
        L_0x00cc:
            return r11
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.analytics.core.sync.BizRequest.getPayloadByDictZip(java.util.Map):byte[]");
    }

    public static String getHead() {
        String str;
        String appkey = AppInfoUtil.getAppkey();
        Context context = Variables.getInstance().getContext();
        String appVersion = Variables.getInstance().getAppVersion();
        if (appVersion == null) {
            appVersion = "";
        }
        String str2 = "";
        Map<String, String> deviceInfo = UTMCDevice.getDeviceInfo(context);
        if (deviceInfo != null && (str2 = deviceInfo.get(LogField.APPVERSION.toString())) == null) {
            str2 = "";
        }
        String channel = AppInfoUtil.getChannel();
        if (channel == null) {
            channel = "";
        }
        String str3 = "";
        if (deviceInfo != null) {
            str3 = deviceInfo.get(LogField.UTDID.toString());
        }
        String fullSDKVersion = UTBuildInfo.getInstance().getFullSDKVersion();
        String str4 = "0";
        if (TnetIpv6Manager.getInstance().isIpv6Connection()) {
            str4 = "1";
        }
        if (Variables.getInstance().isRealTimeDebug()) {
            str = String.format("ak=%s&av=%s&avsys=%s&c=%s&d=%s&sv=%s&ipv6=%s&dk=%s", new Object[]{appkey, appVersion, str2, channel, str3, fullSDKVersion, str4, Variables.getInstance().getDebugKey()});
        } else {
            str = String.format("ak=%s&av=%s&avsys=%s&c=%s&d=%s&sv=%s&ipv6=%s", new Object[]{appkey, appVersion, str2, channel, str3, fullSDKVersion, str4});
        }
        StringBuilder sb = new StringBuilder(str);
        if (Variables.getInstance().isHttpService()) {
            if (mMiniWuaIndex == 0) {
                mMiniWua = WuaHelper.getMiniWua();
            }
            mMiniWuaIndex++;
            if (mMiniWuaIndex > 50) {
                mMiniWuaIndex = 0;
            }
        } else {
            TnetUtil.refreshMiniWua();
        }
        if (!TextUtils.isEmpty(mMiniWua)) {
            sb.append("&");
            sb.append("wua=");
            sb.append(mMiniWua);
        }
        sb.append("&");
        sb.append("_");
        sb.append("ut_sample");
        sb.append(SymbolExpUtil.SYMBOL_EQUAL);
        sb.append(ConfigTimeStampMgr.getInstance().get("ut_sample"));
        sb.append("&");
        sb.append("_");
        sb.append("utap_system");
        sb.append(SymbolExpUtil.SYMBOL_EQUAL);
        sb.append(ConfigTimeStampMgr.getInstance().get("utap_system"));
        sb.append("&");
        sb.append("_");
        sb.append("ap_stat");
        sb.append(SymbolExpUtil.SYMBOL_EQUAL);
        sb.append(ConfigTimeStampMgr.getInstance().get("ap_stat"));
        sb.append("&");
        sb.append("_");
        sb.append("ap_alarm");
        sb.append(SymbolExpUtil.SYMBOL_EQUAL);
        sb.append(ConfigTimeStampMgr.getInstance().get("ap_alarm"));
        sb.append("&");
        sb.append("_");
        sb.append("ap_counter");
        sb.append(SymbolExpUtil.SYMBOL_EQUAL);
        sb.append(ConfigTimeStampMgr.getInstance().get("ap_counter"));
        sb.append("&");
        sb.append("_");
        sb.append("ut_bussiness");
        sb.append(SymbolExpUtil.SYMBOL_EQUAL);
        sb.append(ConfigTimeStampMgr.getInstance().get("ut_bussiness"));
        sb.append("&");
        sb.append("_");
        sb.append("ut_realtime");
        sb.append(SymbolExpUtil.SYMBOL_EQUAL);
        sb.append(ConfigTimeStampMgr.getInstance().get("ut_realtime"));
        String sb2 = sb.toString();
        Logger.i("PostData", "send url :" + sb2);
        return sb2;
    }

    @TargetApi(19)
    static void initOutputStream() {
        if (Build.VERSION.SDK_INT >= 19) {
            closeOutputStream();
            mByteArrayOutputStream = new ByteArrayOutputStream();
            try {
                mGZIPOutputStream = new GZIPOutputStream(mByteArrayOutputStream, true);
            } catch (Exception unused) {
            }
        }
    }

    static void closeOutputStream() {
        closeOutputStream(mGZIPOutputStream);
        closeOutputStream(mByteArrayOutputStream);
    }

    static void closeOutputStream(OutputStream outputStream) {
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String[] getSplitResult(String str) {
        String[] strArr = new String[34];
        int i = 0;
        int i2 = 0;
        while (true) {
            if (i >= strArr.length - 1) {
                break;
            }
            int indexOf = str.indexOf("||", i2);
            if (indexOf == -1) {
                strArr[i] = str.substring(i2);
                break;
            }
            strArr[i] = str.substring(i2, indexOf);
            i2 = indexOf + 2;
            i++;
        }
        strArr[33] = str.substring(i2);
        return strArr;
    }

    static int parseResult(byte[] bArr) {
        int i = -1;
        if (bArr == null || bArr.length < 12) {
            Logger.e("", "recv errCode UNKNOWN_ERROR");
        } else {
            mReceivedDataLen = (long) bArr.length;
            if (ByteUtils.bytesToInt(bArr, 1, 3) + 8 != bArr.length) {
                Logger.e("", "recv len error");
            } else {
                boolean z = 1 == (bArr[5] & 1);
                int bytesToInt = ByteUtils.bytesToInt(bArr, 8, 4);
                int length = bArr.length - 12 >= 0 ? bArr.length - 12 : 0;
                if (length <= 0) {
                    mResponseAdditionalData = null;
                } else if (z) {
                    byte[] bArr2 = new byte[length];
                    System.arraycopy(bArr, 12, bArr2, 0, length);
                    byte[] unGzip = GzipUtils.unGzip(bArr2);
                    mResponseAdditionalData = new String(unGzip, 0, unGzip.length);
                } else {
                    mResponseAdditionalData = new String(bArr, 12, length);
                }
                i = bytesToInt;
            }
        }
        if (107 == i) {
            Variables.getInstance().setHttpService(true);
        }
        if (109 == i) {
            Variables.getInstance().setGzipUpload(true);
        }
        if (115 == i) {
            Variables.getInstance().setRealtimeServiceClosed(true);
        }
        if (116 == i) {
            Variables.getInstance().setAllServiceClosed(true);
        }
        Logger.d("", IWXUserTrackAdapter.MONITOR_ERROR_CODE, Integer.valueOf(i));
        return i;
    }

    static void recordTraffic(long j) {
        Object invokeStaticMethod;
        try {
            Context context = Variables.getInstance().getContext();
            if (context != null) {
                if (!bTestFlowGenterClsExist && flowClz != null) {
                    flowClz = Class.forName("com.taobao.analysis.FlowCenter");
                    bTestFlowGenterClsExist = true;
                }
                if (!(flowClz == null || (invokeStaticMethod = ReflectUtils.invokeStaticMethod(flowClz, "getInstance")) == null)) {
                    Logger.d("", "sendBytes", Long.valueOf(j), "mReceivedDataLen", Long.valueOf(mReceivedDataLen));
                    ReflectUtils.invokeMethod(invokeStaticMethod, "commitFlow", new Object[]{context, "ut", true, "ut", Long.valueOf(j), Long.valueOf(mReceivedDataLen)}, Context.class, String.class, Boolean.TYPE, String.class, Long.TYPE, Long.TYPE);
                }
            }
        } catch (Throwable th) {
            mReceivedDataLen = 0;
            throw th;
        }
        mReceivedDataLen = 0;
    }
}
