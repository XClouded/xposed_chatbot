package com.taobao.accs.utl;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import com.taobao.accs.ACCSManager;
import com.taobao.accs.IACCSManager;
import com.taobao.accs.base.TaoBaseService;
import com.taobao.accs.common.Constants;
import java.util.HashMap;
import java.util.Map;

public class AccsHandler {
    public static final String TAG = "AccsHandler";
    private static Handler handler = new Handler(Looper.getMainLooper());

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static int onReceiveData(android.content.Context r21, android.content.Intent r22, com.taobao.accs.base.AccsDataListener r23) {
        /*
            r0 = r21
            r6 = r22
            r1 = r23
            r7 = 2
            r8 = 0
            if (r1 == 0) goto L_0x029e
            if (r0 != 0) goto L_0x000e
            goto L_0x029e
        L_0x000e:
            if (r6 == 0) goto L_0x029d
            java.lang.String r2 = "command"
            r3 = -1
            int r2 = r6.getIntExtra(r2, r3)     // Catch:{ Exception -> 0x0271 }
            java.lang.String r3 = "errorCode"
            int r5 = r6.getIntExtra(r3, r8)     // Catch:{ Exception -> 0x0271 }
            java.lang.String r3 = "userInfo"
            java.lang.String r3 = r6.getStringExtra(r3)     // Catch:{ Exception -> 0x0271 }
            java.lang.String r4 = "dataId"
            java.lang.String r4 = r6.getStringExtra(r4)     // Catch:{ Exception -> 0x0271 }
            java.lang.String r9 = "serviceId"
            java.lang.String r9 = r6.getStringExtra(r9)     // Catch:{ Exception -> 0x0271 }
            com.taobao.accs.utl.ALog$Level r10 = com.taobao.accs.utl.ALog.Level.I     // Catch:{ Exception -> 0x0271 }
            boolean r10 = com.taobao.accs.utl.ALog.isPrintLog(r10)     // Catch:{ Exception -> 0x0271 }
            if (r10 == 0) goto L_0x006c
            java.lang.String r10 = TAG     // Catch:{ Exception -> 0x0271 }
            java.lang.String r11 = "onReceiveData"
            r12 = 8
            java.lang.Object[] r12 = new java.lang.Object[r12]     // Catch:{ Exception -> 0x0271 }
            java.lang.String r13 = "dataId"
            r12[r8] = r13     // Catch:{ Exception -> 0x0271 }
            r13 = 1
            r12[r13] = r4     // Catch:{ Exception -> 0x0271 }
            java.lang.String r13 = "serviceId"
            r12[r7] = r13     // Catch:{ Exception -> 0x0271 }
            r13 = 3
            r12[r13] = r9     // Catch:{ Exception -> 0x0271 }
            r13 = 4
            java.lang.String r14 = "command"
            r12[r13] = r14     // Catch:{ Exception -> 0x0271 }
            r13 = 5
            java.lang.Integer r14 = java.lang.Integer.valueOf(r2)     // Catch:{ Exception -> 0x0271 }
            r12[r13] = r14     // Catch:{ Exception -> 0x0271 }
            r13 = 6
            java.lang.String r14 = "className"
            r12[r13] = r14     // Catch:{ Exception -> 0x0271 }
            r13 = 7
            java.lang.Class r14 = r23.getClass()     // Catch:{ Exception -> 0x0271 }
            java.lang.String r14 = r14.getName()     // Catch:{ Exception -> 0x0271 }
            r12[r13] = r14     // Catch:{ Exception -> 0x0271 }
            com.taobao.accs.utl.ALog.i(r10, r11, r12)     // Catch:{ Exception -> 0x0271 }
        L_0x006c:
            if (r2 <= 0) goto L_0x0265
            com.taobao.accs.utl.UTMini r15 = com.taobao.accs.utl.UTMini.getInstance()     // Catch:{ Exception -> 0x0271 }
            r16 = 66001(0x101d1, float:9.2487E-41)
            java.lang.String r17 = "MsgToBuss5"
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0271 }
            r10.<init>()     // Catch:{ Exception -> 0x0271 }
            java.lang.String r11 = "commandId="
            r10.append(r11)     // Catch:{ Exception -> 0x0271 }
            r10.append(r2)     // Catch:{ Exception -> 0x0271 }
            java.lang.String r18 = r10.toString()     // Catch:{ Exception -> 0x0271 }
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0271 }
            r10.<init>()     // Catch:{ Exception -> 0x0271 }
            java.lang.String r11 = "serviceId="
            r10.append(r11)     // Catch:{ Exception -> 0x0271 }
            r10.append(r9)     // Catch:{ Exception -> 0x0271 }
            java.lang.String r11 = " dataId="
            r10.append(r11)     // Catch:{ Exception -> 0x0271 }
            r10.append(r4)     // Catch:{ Exception -> 0x0271 }
            java.lang.String r19 = r10.toString()     // Catch:{ Exception -> 0x0271 }
            int r10 = com.taobao.accs.common.Constants.SDK_VERSION_CODE     // Catch:{ Exception -> 0x0271 }
            java.lang.Integer r20 = java.lang.Integer.valueOf(r10)     // Catch:{ Exception -> 0x0271 }
            r15.commitEvent(r16, r17, r18, r19, r20)     // Catch:{ Exception -> 0x0271 }
            java.lang.String r10 = "accs"
            java.lang.String r11 = "to_buss"
            java.lang.StringBuilder r12 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0271 }
            r12.<init>()     // Catch:{ Exception -> 0x0271 }
            java.lang.String r13 = "3commandId="
            r12.append(r13)     // Catch:{ Exception -> 0x0271 }
            r12.append(r2)     // Catch:{ Exception -> 0x0271 }
            java.lang.String r13 = "serviceId="
            r12.append(r13)     // Catch:{ Exception -> 0x0271 }
            r12.append(r9)     // Catch:{ Exception -> 0x0271 }
            java.lang.String r12 = r12.toString()     // Catch:{ Exception -> 0x0271 }
            r13 = 0
            com.taobao.accs.utl.AppMonitorAdapter.commitCount(r10, r11, r12, r13)     // Catch:{ Exception -> 0x0271 }
            switch(r2) {
                case 5: goto L_0x0252;
                case 6: goto L_0x0247;
                case 100: goto L_0x020b;
                case 101: goto L_0x013f;
                case 103: goto L_0x00fd;
                case 104: goto L_0x00d3;
                default: goto L_0x00cf;
            }     // Catch:{ Exception -> 0x0271 }
        L_0x00cf:
            java.lang.String r0 = TAG     // Catch:{ Exception -> 0x0271 }
            goto L_0x025d
        L_0x00d3:
            java.lang.String r0 = "anti_brush_ret"
            boolean r0 = r6.getBooleanExtra(r0, r8)     // Catch:{ Exception -> 0x0271 }
            java.lang.String r2 = TAG     // Catch:{ Exception -> 0x0271 }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0271 }
            r3.<init>()     // Catch:{ Exception -> 0x0271 }
            java.lang.String r4 = "onReceiveData anti brush result:"
            r3.append(r4)     // Catch:{ Exception -> 0x0271 }
            r3.append(r0)     // Catch:{ Exception -> 0x0271 }
            java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x0271 }
            java.lang.Object[] r4 = new java.lang.Object[r8]     // Catch:{ Exception -> 0x0271 }
            com.taobao.accs.utl.ALog.e(r2, r3, r4)     // Catch:{ Exception -> 0x0271 }
            android.os.Handler r2 = handler     // Catch:{ Exception -> 0x0271 }
            com.taobao.accs.utl.AccsHandler$6 r3 = new com.taobao.accs.utl.AccsHandler$6     // Catch:{ Exception -> 0x0271 }
            r3.<init>(r1, r0)     // Catch:{ Exception -> 0x0271 }
            r2.post(r3)     // Catch:{ Exception -> 0x0271 }
            goto L_0x029d
        L_0x00fd:
            java.lang.String r0 = "connect_avail"
            boolean r0 = r6.getBooleanExtra(r0, r8)     // Catch:{ Exception -> 0x0271 }
            java.lang.String r2 = "host"
            java.lang.String r2 = r6.getStringExtra(r2)     // Catch:{ Exception -> 0x0271 }
            java.lang.String r3 = "errorDetail"
            java.lang.String r9 = r6.getStringExtra(r3)     // Catch:{ Exception -> 0x0271 }
            java.lang.String r3 = "type_inapp"
            boolean r3 = r6.getBooleanExtra(r3, r8)     // Catch:{ Exception -> 0x0271 }
            java.lang.String r4 = "is_center_host"
            boolean r4 = r6.getBooleanExtra(r4, r8)     // Catch:{ Exception -> 0x0271 }
            boolean r6 = android.text.TextUtils.isEmpty(r2)     // Catch:{ Exception -> 0x0271 }
            if (r6 != 0) goto L_0x029d
            if (r0 == 0) goto L_0x012f
            android.os.Handler r0 = handler     // Catch:{ Exception -> 0x0271 }
            com.taobao.accs.utl.AccsHandler$7 r5 = new com.taobao.accs.utl.AccsHandler$7     // Catch:{ Exception -> 0x0271 }
            r5.<init>(r1, r2, r3, r4)     // Catch:{ Exception -> 0x0271 }
            r0.post(r5)     // Catch:{ Exception -> 0x0271 }
            goto L_0x029d
        L_0x012f:
            android.os.Handler r10 = handler     // Catch:{ Exception -> 0x0271 }
            com.taobao.accs.utl.AccsHandler$8 r11 = new com.taobao.accs.utl.AccsHandler$8     // Catch:{ Exception -> 0x0271 }
            r0 = r11
            r1 = r23
            r6 = r9
            r0.<init>(r1, r2, r3, r4, r5, r6)     // Catch:{ Exception -> 0x0271 }
            r10.post(r11)     // Catch:{ Exception -> 0x0271 }
            goto L_0x029d
        L_0x013f:
            java.lang.String r2 = "data"
            byte[] r5 = r6.getByteArrayExtra(r2)     // Catch:{ Exception -> 0x0271 }
            java.lang.String r2 = "bizAck"
            boolean r2 = r6.getBooleanExtra(r2, r8)     // Catch:{ Exception -> 0x0271 }
            if (r5 == 0) goto L_0x01f5
            com.taobao.accs.utl.ALog$Level r10 = com.taobao.accs.utl.ALog.Level.D     // Catch:{ Exception -> 0x0271 }
            boolean r10 = com.taobao.accs.utl.ALog.isPrintLog(r10)     // Catch:{ Exception -> 0x0271 }
            if (r10 == 0) goto L_0x0175
            java.lang.String r10 = TAG     // Catch:{ Exception -> 0x0271 }
            java.lang.StringBuilder r11 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0271 }
            r11.<init>()     // Catch:{ Exception -> 0x0271 }
            java.lang.String r12 = "onReceiveData COMMAND_RECEIVE_DATA onData dataId:"
            r11.append(r12)     // Catch:{ Exception -> 0x0271 }
            r11.append(r4)     // Catch:{ Exception -> 0x0271 }
            java.lang.String r12 = " serviceId:"
            r11.append(r12)     // Catch:{ Exception -> 0x0271 }
            r11.append(r9)     // Catch:{ Exception -> 0x0271 }
            java.lang.String r11 = r11.toString()     // Catch:{ Exception -> 0x0271 }
            java.lang.Object[] r12 = new java.lang.Object[r8]     // Catch:{ Exception -> 0x0271 }
            com.taobao.accs.utl.ALog.d(r10, r11, r12)     // Catch:{ Exception -> 0x0271 }
        L_0x0175:
            com.taobao.accs.base.TaoBaseService$ExtraInfo r10 = getExtraInfo(r22)     // Catch:{ Exception -> 0x0271 }
            if (r2 == 0) goto L_0x0198
            java.lang.String r2 = TAG     // Catch:{ Exception -> 0x0271 }
            java.lang.StringBuilder r11 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0271 }
            r11.<init>()     // Catch:{ Exception -> 0x0271 }
            java.lang.String r12 = "onReceiveData try to send biz ack dataId "
            r11.append(r12)     // Catch:{ Exception -> 0x0271 }
            r11.append(r4)     // Catch:{ Exception -> 0x0271 }
            java.lang.String r11 = r11.toString()     // Catch:{ Exception -> 0x0271 }
            java.lang.Object[] r12 = new java.lang.Object[r8]     // Catch:{ Exception -> 0x0271 }
            com.taobao.accs.utl.ALog.i(r2, r11, r12)     // Catch:{ Exception -> 0x0271 }
            java.util.Map<java.lang.Integer, java.lang.String> r2 = r10.oriExtHeader     // Catch:{ Exception -> 0x0271 }
            sendBusinessAck(r0, r6, r4, r2)     // Catch:{ Exception -> 0x0271 }
        L_0x0198:
            android.os.Bundle r2 = r22.getExtras()     // Catch:{ Exception -> 0x01c2 }
            java.lang.Class<com.taobao.accs.ut.monitor.NetPerformanceMonitor> r11 = com.taobao.accs.ut.monitor.NetPerformanceMonitor.class
            java.lang.ClassLoader r11 = r11.getClassLoader()     // Catch:{ Exception -> 0x01c2 }
            r2.setClassLoader(r11)     // Catch:{ Exception -> 0x01c2 }
            android.os.Bundle r2 = r22.getExtras()     // Catch:{ Exception -> 0x01c2 }
            java.lang.String r6 = "monitor"
            java.io.Serializable r2 = r2.getSerializable(r6)     // Catch:{ Exception -> 0x01c2 }
            com.taobao.accs.ut.monitor.NetPerformanceMonitor r2 = (com.taobao.accs.ut.monitor.NetPerformanceMonitor) r2     // Catch:{ Exception -> 0x01c2 }
            if (r2 == 0) goto L_0x01cc
            r2.onToAccsTime()     // Catch:{ Exception -> 0x01c2 }
            boolean r0 = r0 instanceof org.android.agoo.accs.AgooService     // Catch:{ Exception -> 0x01c2 }
            if (r0 != 0) goto L_0x01cc
            anet.channel.appmonitor.IAppMonitor r0 = anet.channel.appmonitor.AppMonitor.getInstance()     // Catch:{ Exception -> 0x01c2 }
            r0.commitStat(r2)     // Catch:{ Exception -> 0x01c2 }
            goto L_0x01cc
        L_0x01c2:
            r0 = move-exception
            java.lang.String r2 = TAG     // Catch:{ Exception -> 0x0271 }
            java.lang.String r6 = "get NetPerformanceMonitor Error:"
            java.lang.Object[] r11 = new java.lang.Object[r8]     // Catch:{ Exception -> 0x0271 }
            com.taobao.accs.utl.ALog.e(r2, r6, r0, r11)     // Catch:{ Exception -> 0x0271 }
        L_0x01cc:
            java.lang.String r0 = "accs"
            java.lang.String r2 = "to_buss_success"
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0271 }
            r6.<init>()     // Catch:{ Exception -> 0x0271 }
            java.lang.String r11 = "1commandId=101serviceId="
            r6.append(r11)     // Catch:{ Exception -> 0x0271 }
            r6.append(r9)     // Catch:{ Exception -> 0x0271 }
            java.lang.String r6 = r6.toString()     // Catch:{ Exception -> 0x0271 }
            com.taobao.accs.utl.AppMonitorAdapter.commitCount(r0, r2, r6, r13)     // Catch:{ Exception -> 0x0271 }
            android.os.Handler r11 = handler     // Catch:{ Exception -> 0x0271 }
            com.taobao.accs.utl.AccsHandler$3 r12 = new com.taobao.accs.utl.AccsHandler$3     // Catch:{ Exception -> 0x0271 }
            r0 = r12
            r1 = r23
            r2 = r9
            r6 = r10
            r0.<init>(r1, r2, r3, r4, r5, r6)     // Catch:{ Exception -> 0x0271 }
            r11.post(r12)     // Catch:{ Exception -> 0x0271 }
            goto L_0x029d
        L_0x01f5:
            java.lang.String r0 = TAG     // Catch:{ Exception -> 0x0271 }
            java.lang.String r1 = "onReceiveData COMMAND_RECEIVE_DATA msg null"
            java.lang.Object[] r2 = new java.lang.Object[r8]     // Catch:{ Exception -> 0x0271 }
            com.taobao.accs.utl.ALog.e(r0, r1, r2)     // Catch:{ Exception -> 0x0271 }
            java.lang.String r0 = "accs"
            java.lang.String r1 = "send_fail"
            java.lang.String r2 = "1"
            java.lang.String r3 = "COMMAND_RECEIVE_DATA msg null"
            com.taobao.accs.utl.AppMonitorAdapter.commitAlarmFail(r0, r1, r9, r2, r3)     // Catch:{ Exception -> 0x0271 }
            goto L_0x029d
        L_0x020b:
            java.lang.String r0 = "res"
            java.lang.String r2 = "send_type"
            java.lang.String r2 = r6.getStringExtra(r2)     // Catch:{ Exception -> 0x0271 }
            boolean r0 = android.text.TextUtils.equals(r0, r2)     // Catch:{ Exception -> 0x0271 }
            if (r0 == 0) goto L_0x0234
            java.lang.String r0 = "data"
            byte[] r10 = r6.getByteArrayExtra(r0)     // Catch:{ Exception -> 0x0271 }
            android.os.Handler r11 = handler     // Catch:{ Exception -> 0x0271 }
            com.taobao.accs.utl.AccsHandler$4 r12 = new com.taobao.accs.utl.AccsHandler$4     // Catch:{ Exception -> 0x0271 }
            r0 = r12
            r1 = r23
            r2 = r9
            r3 = r4
            r4 = r5
            r5 = r10
            r6 = r22
            r0.<init>(r1, r2, r3, r4, r5, r6)     // Catch:{ Exception -> 0x0271 }
            r11.post(r12)     // Catch:{ Exception -> 0x0271 }
            goto L_0x029d
        L_0x0234:
            android.os.Handler r10 = handler     // Catch:{ Exception -> 0x0271 }
            com.taobao.accs.utl.AccsHandler$5 r11 = new com.taobao.accs.utl.AccsHandler$5     // Catch:{ Exception -> 0x0271 }
            r0 = r11
            r1 = r23
            r2 = r9
            r3 = r4
            r4 = r5
            r5 = r22
            r0.<init>(r1, r2, r3, r4, r5)     // Catch:{ Exception -> 0x0271 }
            r10.post(r11)     // Catch:{ Exception -> 0x0271 }
            goto L_0x029d
        L_0x0247:
            android.os.Handler r0 = handler     // Catch:{ Exception -> 0x0271 }
            com.taobao.accs.utl.AccsHandler$2 r2 = new com.taobao.accs.utl.AccsHandler$2     // Catch:{ Exception -> 0x0271 }
            r2.<init>(r1, r9, r5, r6)     // Catch:{ Exception -> 0x0271 }
            r0.post(r2)     // Catch:{ Exception -> 0x0271 }
            goto L_0x029d
        L_0x0252:
            android.os.Handler r0 = handler     // Catch:{ Exception -> 0x0271 }
            com.taobao.accs.utl.AccsHandler$1 r2 = new com.taobao.accs.utl.AccsHandler$1     // Catch:{ Exception -> 0x0271 }
            r2.<init>(r1, r9, r5, r6)     // Catch:{ Exception -> 0x0271 }
            r0.post(r2)     // Catch:{ Exception -> 0x0271 }
            goto L_0x029d
        L_0x025d:
            java.lang.String r1 = "onReceiveData command not handled"
            java.lang.Object[] r2 = new java.lang.Object[r8]     // Catch:{ Exception -> 0x0271 }
            com.taobao.accs.utl.ALog.w(r0, r1, r2)     // Catch:{ Exception -> 0x0271 }
            goto L_0x029d
        L_0x0265:
            java.lang.String r0 = TAG     // Catch:{ Exception -> 0x0271 }
            java.lang.String r1 = "onReceiveData command not handled"
            java.lang.Object[] r2 = new java.lang.Object[r8]     // Catch:{ Exception -> 0x0271 }
            com.taobao.accs.utl.ALog.w(r0, r1, r2)     // Catch:{ Exception -> 0x0271 }
            goto L_0x029d
        L_0x026f:
            r0 = move-exception
            goto L_0x029c
        L_0x0271:
            r0 = move-exception
            java.lang.String r1 = "accs"
            java.lang.String r2 = "send_fail"
            java.lang.String r3 = ""
            java.lang.String r4 = "1"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ all -> 0x026f }
            r5.<init>()     // Catch:{ all -> 0x026f }
            java.lang.String r6 = "callback error"
            r5.append(r6)     // Catch:{ all -> 0x026f }
            java.lang.String r6 = r0.toString()     // Catch:{ all -> 0x026f }
            r5.append(r6)     // Catch:{ all -> 0x026f }
            java.lang.String r5 = r5.toString()     // Catch:{ all -> 0x026f }
            com.taobao.accs.utl.AppMonitorAdapter.commitAlarmFail(r1, r2, r3, r4, r5)     // Catch:{ all -> 0x026f }
            java.lang.String r1 = TAG     // Catch:{ all -> 0x026f }
            java.lang.String r2 = "onReceiveData"
            java.lang.Object[] r3 = new java.lang.Object[r8]     // Catch:{ all -> 0x026f }
            com.taobao.accs.utl.ALog.e(r1, r2, r0, r3)     // Catch:{ all -> 0x026f }
            goto L_0x029d
        L_0x029c:
            throw r0
        L_0x029d:
            return r7
        L_0x029e:
            java.lang.String r0 = TAG
            java.lang.String r1 = "onReceiveData listener or context null"
            java.lang.Object[] r2 = new java.lang.Object[r8]
            com.taobao.accs.utl.ALog.e(r0, r1, r2)
            return r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.accs.utl.AccsHandler.onReceiveData(android.content.Context, android.content.Intent, com.taobao.accs.base.AccsDataListener):int");
    }

    private static Map<TaoBaseService.ExtHeaderType, String> getExtHeader(Map<Integer, String> map) {
        HashMap hashMap;
        if (map == null) {
            return null;
        }
        try {
            hashMap = new HashMap();
            try {
                for (TaoBaseService.ExtHeaderType extHeaderType : TaoBaseService.ExtHeaderType.values()) {
                    String str = map.get(Integer.valueOf(extHeaderType.ordinal()));
                    if (!TextUtils.isEmpty(str)) {
                        hashMap.put(extHeaderType, str);
                    }
                }
            } catch (Exception e) {
                e = e;
                ALog.e(TAG, "getExtHeader", e, new Object[0]);
                return hashMap;
            }
        } catch (Exception e2) {
            e = e2;
            hashMap = null;
            ALog.e(TAG, "getExtHeader", e, new Object[0]);
            return hashMap;
        }
        return hashMap;
    }

    /* access modifiers changed from: private */
    public static TaoBaseService.ExtraInfo getExtraInfo(Intent intent) {
        TaoBaseService.ExtraInfo extraInfo = new TaoBaseService.ExtraInfo();
        try {
            HashMap hashMap = (HashMap) intent.getSerializableExtra(TaoBaseService.ExtraInfo.EXT_HEADER);
            Map<TaoBaseService.ExtHeaderType, String> extHeader = getExtHeader(hashMap);
            String stringExtra = intent.getStringExtra("packageName");
            String stringExtra2 = intent.getStringExtra("host");
            extraInfo.connType = intent.getIntExtra(Constants.KEY_CONN_TYPE, 0);
            extraInfo.extHeader = extHeader;
            extraInfo.oriExtHeader = hashMap;
            extraInfo.fromPackage = stringExtra;
            extraInfo.fromHost = stringExtra2;
        } catch (Throwable th) {
            ALog.e(TAG, "getExtraInfo", th, new Object[0]);
        }
        return extraInfo;
    }

    private static void sendBusinessAck(Context context, Intent intent, String str, Map<Integer, String> map) {
        try {
            ALog.i(TAG, "sendBusinessAck", Constants.KEY_DATA_ID, str);
            if (intent != null) {
                String stringExtra = intent.getStringExtra("host");
                String stringExtra2 = intent.getStringExtra("source");
                String stringExtra3 = intent.getStringExtra(Constants.KEY_TARGET);
                String stringExtra4 = intent.getStringExtra("appKey");
                String stringExtra5 = intent.getStringExtra(Constants.KEY_CONFIG_TAG);
                short shortExtra = intent.getShortExtra(Constants.KEY_FLAGS, 0);
                IACCSManager accsInstance = ACCSManager.getAccsInstance(context, stringExtra4, stringExtra5);
                if (accsInstance != null) {
                    accsInstance.sendBusinessAck(stringExtra3, stringExtra2, str, shortExtra, stringExtra, map);
                    AppMonitorAdapter.commitCount("accs", BaseMonitor.COUNT_BUSINESS_ACK_SUCC, "", 0.0d);
                    return;
                }
                AppMonitorAdapter.commitCount("accs", BaseMonitor.COUNT_BUSINESS_ACK_FAIL, "no acsmgr", 0.0d);
            }
        } catch (Throwable th) {
            ALog.e(TAG, "sendBusinessAck", th, new Object[0]);
            AppMonitorAdapter.commitCount("accs", BaseMonitor.COUNT_BUSINESS_ACK_FAIL, th.toString(), 0.0d);
        }
    }
}
