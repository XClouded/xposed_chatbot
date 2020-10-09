package com.taobao.accs.data;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.taobao.accs.IAppReceiver;
import com.taobao.accs.IAppReceiverV1;
import com.taobao.accs.base.AccsDataListener;
import com.taobao.accs.base.TaoBaseService;
import com.taobao.accs.client.GlobalClientInfo;
import com.taobao.accs.common.Constants;
import com.taobao.accs.common.ThreadPoolExecutorFactory;
import com.taobao.accs.dispatch.IntentDispatch;
import com.taobao.accs.net.BaseConnection;
import com.taobao.accs.utl.ALog;
import com.taobao.accs.utl.AccsHandler;
import com.taobao.accs.utl.AdapterUtilityImpl;
import com.taobao.accs.utl.AppMonitorAdapter;
import com.taobao.accs.utl.BaseMonitor;
import com.taobao.accs.utl.UTMini;
import com.taobao.accs.utl.UtilityImpl;
import com.uc.webview.export.extension.UCCore;
import com.vivo.push.PushClientConstants;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.android.agoo.common.Config;

public class MsgDistribute {
    private static final String KEY_ROUTING_ACK = "routingAck";
    private static final String KEY_ROUTING_MSG = "routingMsg";
    private static final long MIN_SPACE = 5242880;
    private static final String TAG = "MsgDistribute";
    private static volatile MsgDistribute instance;
    /* access modifiers changed from: private */
    public static Set<String> mRoutingDataIds;

    /* access modifiers changed from: protected */
    public String getChannelService(Context context) {
        return AdapterUtilityImpl.channelService;
    }

    /* access modifiers changed from: protected */
    public String getMsgDistributeService(Context context) {
        return AdapterUtilityImpl.msgService;
    }

    public static MsgDistribute getInstance() {
        if (instance == null) {
            synchronized (MsgDistribute.class) {
                if (instance == null) {
                    instance = new MsgDistribute();
                }
            }
        }
        return instance;
    }

    public static void distribMessage(Context context, Intent intent) {
        distribMessage(context, (BaseConnection) null, intent);
    }

    public static void distribMessage(final Context context, final BaseConnection baseConnection, final Intent intent) {
        try {
            ThreadPoolExecutorFactory.getScheduledExecutor().execute(new Runnable() {
                public void run() {
                    MsgDistribute.getInstance().distribute(context, baseConnection, intent);
                }
            });
        } catch (Throwable th) {
            if (!(baseConnection == null || intent == null)) {
                String stringExtra = intent.getStringExtra(Constants.KEY_CONFIG_TAG);
                String stringExtra2 = intent.getStringExtra(Constants.KEY_DATA_ID);
                if (!TextUtils.isEmpty(stringExtra) && !TextUtils.isEmpty(stringExtra2)) {
                    baseConnection.send(Message.buildErrorReportMessage(stringExtra2, intent.getStringExtra("serviceId"), baseConnection.getHost((String) null), 3), true);
                }
            }
            ALog.e(TAG, "distribMessage", th, new Object[0]);
            UTMini instance2 = UTMini.getInstance();
            instance2.commitEvent(66001, "MsgToBuss8", "distribMessage" + th.toString(), Integer.valueOf(Constants.SDK_VERSION_CODE));
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v24, resolved type: java.lang.Object[]} */
    /* JADX WARNING: type inference failed for: r3v1, types: [boolean] */
    /* JADX WARNING: type inference failed for: r3v11 */
    /* JADX WARNING: type inference failed for: r3v24 */
    /* JADX WARNING: type inference failed for: r3v26 */
    /* JADX WARNING: type inference failed for: r3v28 */
    /* access modifiers changed from: private */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:126:0x0264  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void distribute(android.content.Context r26, com.taobao.accs.net.BaseConnection r27, android.content.Intent r28) {
        /*
            r25 = this;
            r13 = r25
            r0 = r26
            r14 = r27
            r15 = r28
            java.lang.String r1 = "dataId"
            java.lang.String r12 = r15.getStringExtra(r1)
            java.lang.String r1 = "serviceId"
            java.lang.String r11 = r15.getStringExtra(r1)
            java.lang.String r1 = r28.getAction()
            com.taobao.accs.utl.ALog$Level r2 = com.taobao.accs.utl.ALog.Level.D
            boolean r2 = com.taobao.accs.utl.ALog.isPrintLog(r2)
            r3 = 5
            r10 = 4
            r9 = 2
            r8 = 3
            r7 = 0
            r6 = 1
            if (r2 == 0) goto L_0x0042
            java.lang.String r2 = "MsgDistribute"
            java.lang.String r4 = "distribute ready"
            r5 = 6
            java.lang.Object[] r5 = new java.lang.Object[r5]
            java.lang.String r16 = "action"
            r5[r7] = r16
            r5[r6] = r1
            java.lang.String r16 = "dataId"
            r5[r9] = r16
            r5[r8] = r12
            java.lang.String r16 = "serviceId"
            r5[r10] = r16
            r5[r3] = r11
            com.taobao.accs.utl.ALog.d(r2, r4, r5)
        L_0x0042:
            boolean r2 = android.text.TextUtils.isEmpty(r1)
            r4 = 66001(0x101d1, float:9.2487E-41)
            r5 = 0
            if (r2 == 0) goto L_0x0074
            if (r14 == 0) goto L_0x0059
            java.lang.String r0 = r14.getHost(r5)
            com.taobao.accs.data.Message r0 = com.taobao.accs.data.Message.buildErrorReportMessage(r12, r11, r0, r8)
            r14.send(r0, r6)
        L_0x0059:
            java.lang.String r0 = "MsgDistribute"
            java.lang.String r1 = "action null"
            java.lang.Object[] r2 = new java.lang.Object[r7]
            com.taobao.accs.utl.ALog.e(r0, r1, r2)
            com.taobao.accs.utl.UTMini r0 = com.taobao.accs.utl.UTMini.getInstance()
            java.lang.String r1 = "MsgToBuss9"
            java.lang.String r2 = "action null"
            int r3 = com.taobao.accs.common.Constants.SDK_VERSION_CODE
            java.lang.Integer r3 = java.lang.Integer.valueOf(r3)
            r0.commitEvent(r4, r1, r2, r3)
            return
        L_0x0074:
            java.lang.String r2 = "com.taobao.accs.intent.action.RECEIVE"
            boolean r2 = android.text.TextUtils.equals(r1, r2)     // Catch:{ Throwable -> 0x0243 }
            if (r2 == 0) goto L_0x01fb
            java.lang.String r1 = "command"
            r2 = -1
            int r4 = r15.getIntExtra(r1, r2)     // Catch:{ Throwable -> 0x01f1 }
            java.lang.String r1 = "userInfo"
            java.lang.String r16 = r15.getStringExtra(r1)     // Catch:{ Throwable -> 0x01e6 }
            java.lang.String r1 = "errorCode"
            int r17 = r15.getIntExtra(r1, r7)     // Catch:{ Throwable -> 0x01e6 }
            java.lang.String r1 = "appKey"
            java.lang.String r18 = r15.getStringExtra(r1)     // Catch:{ Throwable -> 0x01e6 }
            java.lang.String r1 = "configTag"
            java.lang.String r3 = r15.getStringExtra(r1)     // Catch:{ Throwable -> 0x01e6 }
            java.lang.String r1 = r28.getPackage()     // Catch:{ Throwable -> 0x01e6 }
            if (r1 != 0) goto L_0x00b2
            java.lang.String r1 = r26.getPackageName()     // Catch:{ Throwable -> 0x00a9 }
            r15.setPackage(r1)     // Catch:{ Throwable -> 0x00a9 }
            goto L_0x00b2
        L_0x00a9:
            r0 = move-exception
            r13 = r5
        L_0x00ab:
            r5 = r11
            r2 = r12
        L_0x00ad:
            r3 = 1
        L_0x00ae:
            r16 = 2
            goto L_0x024b
        L_0x00b2:
            java.lang.String r1 = "accs"
            boolean r1 = r1.equals(r11)     // Catch:{ Throwable -> 0x01e6 }
            if (r1 == 0) goto L_0x00d6
            java.lang.String r1 = "MsgDistribute"
            java.lang.String r2 = "distribute start"
            java.lang.Object[] r5 = new java.lang.Object[r10]     // Catch:{ Throwable -> 0x00d0 }
            java.lang.String r20 = "appkey"
            r5[r7] = r20     // Catch:{ Throwable -> 0x00d0 }
            r5[r6] = r18     // Catch:{ Throwable -> 0x00d0 }
            java.lang.String r20 = "config"
            r5[r9] = r20     // Catch:{ Throwable -> 0x00d0 }
            r5[r8] = r3     // Catch:{ Throwable -> 0x00d0 }
            com.taobao.accs.utl.ALog.e(r1, r2, r5)     // Catch:{ Throwable -> 0x00d0 }
            goto L_0x00eb
        L_0x00d0:
            r0 = move-exception
            r5 = r11
            r2 = r12
            r3 = 1
            r13 = 0
            goto L_0x00ae
        L_0x00d6:
            java.lang.String r1 = "MsgDistribute"
            java.lang.String r2 = "distribute start"
            java.lang.Object[] r5 = new java.lang.Object[r10]     // Catch:{ Throwable -> 0x01e1 }
            java.lang.String r20 = "appkey"
            r5[r7] = r20     // Catch:{ Throwable -> 0x01e1 }
            r5[r6] = r18     // Catch:{ Throwable -> 0x01e1 }
            java.lang.String r20 = "config"
            r5[r9] = r20     // Catch:{ Throwable -> 0x01e1 }
            r5[r8] = r3     // Catch:{ Throwable -> 0x01e1 }
            com.taobao.accs.utl.ALog.d(r1, r2, r5)     // Catch:{ Throwable -> 0x01e1 }
        L_0x00eb:
            boolean r1 = r13.handleRoutingMsgAck(r0, r15, r12, r11)     // Catch:{ Throwable -> 0x01e1 }
            if (r1 == 0) goto L_0x00f2
            return
        L_0x00f2:
            if (r4 >= 0) goto L_0x0125
            if (r14 == 0) goto L_0x0106
            r1 = 0
            java.lang.String r0 = r14.getHost(r1)     // Catch:{ Throwable -> 0x0103 }
            com.taobao.accs.data.Message r0 = com.taobao.accs.data.Message.buildErrorReportMessage(r12, r11, r0, r8)     // Catch:{ Throwable -> 0x00d0 }
            r14.send(r0, r6)     // Catch:{ Throwable -> 0x00d0 }
            goto L_0x0106
        L_0x0103:
            r0 = move-exception
            r13 = r1
            goto L_0x00ab
        L_0x0106:
            java.lang.String r0 = "MsgDistribute"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x00d0 }
            r1.<init>()     // Catch:{ Throwable -> 0x00d0 }
            java.lang.String r2 = "command error:"
            r1.append(r2)     // Catch:{ Throwable -> 0x00d0 }
            r1.append(r4)     // Catch:{ Throwable -> 0x00d0 }
            java.lang.String r1 = r1.toString()     // Catch:{ Throwable -> 0x00d0 }
            java.lang.Object[] r2 = new java.lang.Object[r9]     // Catch:{ Throwable -> 0x00d0 }
            java.lang.String r3 = "serviceId"
            r2[r7] = r3     // Catch:{ Throwable -> 0x00d0 }
            r2[r6] = r11     // Catch:{ Throwable -> 0x00d0 }
            com.taobao.accs.utl.ALog.e(r0, r1, r2)     // Catch:{ Throwable -> 0x00d0 }
            return
        L_0x0125:
            boolean r1 = r13.checkSpace(r4, r11, r12)     // Catch:{ Throwable -> 0x01e1 }
            if (r1 == 0) goto L_0x013a
            if (r14 == 0) goto L_0x0139
            r5 = 0
            java.lang.String r0 = r14.getHost(r5)     // Catch:{ Throwable -> 0x00a9 }
            com.taobao.accs.data.Message r0 = com.taobao.accs.data.Message.buildErrorReportMessage(r12, r11, r0, r8)     // Catch:{ Throwable -> 0x00a9 }
            r14.send(r0, r6)     // Catch:{ Throwable -> 0x00a9 }
        L_0x0139:
            return
        L_0x013a:
            r5 = 0
            boolean r1 = r13.handleRoutingMsg(r0, r15, r12, r11)     // Catch:{ Throwable -> 0x01e6 }
            if (r1 == 0) goto L_0x0142
            return
        L_0x0142:
            com.taobao.accs.client.GlobalClientInfo r1 = com.taobao.accs.client.GlobalClientInfo.getInstance(r26)     // Catch:{ Throwable -> 0x01e6 }
            java.util.Map r2 = r1.getAppReceiver()     // Catch:{ Throwable -> 0x01e6 }
            boolean r1 = android.text.TextUtils.isEmpty(r3)     // Catch:{ Throwable -> 0x01e6 }
            if (r1 != 0) goto L_0x015d
            if (r2 == 0) goto L_0x0159
            java.lang.Object r1 = r2.get(r3)     // Catch:{ Throwable -> 0x00a9 }
            com.taobao.accs.IAppReceiver r1 = (com.taobao.accs.IAppReceiver) r1     // Catch:{ Throwable -> 0x00a9 }
            goto L_0x015a
        L_0x0159:
            r1 = r5
        L_0x015a:
            r19 = r1
            goto L_0x015f
        L_0x015d:
            r19 = r5
        L_0x015f:
            boolean r1 = com.taobao.accs.utl.OrangeAdapter.isChannelModeEnable()     // Catch:{ Throwable -> 0x01e6 }
            if (r1 != 0) goto L_0x0184
            r1 = r25
            r20 = r2
            r2 = r26
            r21 = r3
            r3 = r11
            r22 = r4
            r4 = r12
            r13 = r5
            r5 = r28
            r6 = r19
            boolean r1 = r1.handleMsgInChannelProcess(r2, r3, r4, r5, r6)     // Catch:{ Throwable -> 0x017d }
            if (r1 == 0) goto L_0x018b
            return
        L_0x017d:
            r0 = move-exception
            r5 = r11
            r2 = r12
            r4 = r22
            goto L_0x00ad
        L_0x0184:
            r20 = r2
            r21 = r3
            r22 = r4
            r13 = r5
        L_0x018b:
            r1 = r25
            r2 = r26
            r3 = r27
            r4 = r28
            r5 = r21
            r6 = r18
            r7 = r22
            r8 = r16
            r16 = 2
            r9 = r11
            r10 = r12
            r23 = r11
            r11 = r19
            r24 = r12
            r12 = r17
            r1.handleControlMsg(r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12)     // Catch:{ Throwable -> 0x01d8 }
            boolean r1 = android.text.TextUtils.isEmpty(r23)     // Catch:{ Throwable -> 0x01d8 }
            if (r1 != 0) goto L_0x01c7
            r1 = r25
            r2 = r26
            r3 = r27
            r4 = r19
            r5 = r28
            r6 = r23
            r7 = r24
            r8 = r22
            r9 = r17
            r1.handleBusinessMsg(r2, r3, r4, r5, r6, r7, r8, r9)     // Catch:{ Throwable -> 0x01d8 }
            goto L_0x0290
        L_0x01c7:
            r1 = r25
            r2 = r26
            r3 = r20
            r4 = r28
            r5 = r22
            r6 = r17
            r1.handBroadCastMsg(r2, r3, r4, r5, r6)     // Catch:{ Throwable -> 0x01d8 }
            goto L_0x0290
        L_0x01d8:
            r0 = move-exception
            r4 = r22
            r5 = r23
            r2 = r24
            r3 = 1
            goto L_0x01f9
        L_0x01e1:
            r0 = move-exception
            r22 = r4
            r13 = 0
            goto L_0x01ea
        L_0x01e6:
            r0 = move-exception
            r22 = r4
            r13 = r5
        L_0x01ea:
            r16 = 2
            r5 = r11
            r2 = r12
            r3 = 1
            goto L_0x024b
        L_0x01f1:
            r0 = move-exception
            r13 = r5
            r16 = 2
            r5 = r11
            r2 = r12
        L_0x01f7:
            r3 = 1
        L_0x01f8:
            r4 = 0
        L_0x01f9:
            r7 = 0
            goto L_0x024b
        L_0x01fb:
            r13 = r5
            r23 = r11
            r24 = r12
            r16 = 2
            if (r14 == 0) goto L_0x021f
            java.lang.String r0 = r14.getHost(r13)     // Catch:{ Throwable -> 0x0219 }
            r5 = r23
            r2 = r24
            com.taobao.accs.data.Message r0 = com.taobao.accs.data.Message.buildErrorReportMessage(r2, r5, r0, r3)     // Catch:{ Throwable -> 0x0217 }
            r3 = 1
            r14.send(r0, r3)     // Catch:{ Throwable -> 0x0215 }
            goto L_0x0224
        L_0x0215:
            r0 = move-exception
            goto L_0x01f8
        L_0x0217:
            r0 = move-exception
            goto L_0x01f7
        L_0x0219:
            r0 = move-exception
            r5 = r23
            r2 = r24
            goto L_0x01f7
        L_0x021f:
            r5 = r23
            r2 = r24
            r3 = 1
        L_0x0224:
            java.lang.String r0 = "MsgDistribute"
            java.lang.String r6 = "distribMessage action error"
            r7 = 0
            java.lang.Object[] r8 = new java.lang.Object[r7]     // Catch:{ Throwable -> 0x023e }
            com.taobao.accs.utl.ALog.e(r0, r6, r8)     // Catch:{ Throwable -> 0x023e }
            com.taobao.accs.utl.UTMini r0 = com.taobao.accs.utl.UTMini.getInstance()     // Catch:{ Throwable -> 0x023e }
            java.lang.String r6 = "MsgToBuss10"
            int r8 = com.taobao.accs.common.Constants.SDK_VERSION_CODE     // Catch:{ Throwable -> 0x023e }
            java.lang.Integer r8 = java.lang.Integer.valueOf(r8)     // Catch:{ Throwable -> 0x023e }
            r0.commitEvent(r4, r6, r1, r8)     // Catch:{ Throwable -> 0x023e }
            goto L_0x0290
        L_0x023e:
            r0 = move-exception
            goto L_0x024a
        L_0x0240:
            r0 = move-exception
            r7 = 0
            goto L_0x024a
        L_0x0243:
            r0 = move-exception
            r13 = r5
            r5 = r11
            r2 = r12
            r3 = 1
            r16 = 2
        L_0x024a:
            r4 = 0
        L_0x024b:
            java.lang.String r1 = "MsgDistribute"
            java.lang.String r6 = "distribMessage"
            r8 = 4
            java.lang.Object[] r8 = new java.lang.Object[r8]
            java.lang.String r9 = "dataId"
            r8[r7] = r9
            r8[r3] = r2
            java.lang.String r7 = "serviceId"
            r8[r16] = r7
            r7 = 3
            r8[r7] = r5
            com.taobao.accs.utl.ALog.e(r1, r6, r0, r8)
            if (r14 == 0) goto L_0x026f
            java.lang.String r1 = r14.getHost(r13)
            com.taobao.accs.data.Message r1 = com.taobao.accs.data.Message.buildErrorReportMessage(r2, r5, r1, r7)
            r14.send(r1, r3)
        L_0x026f:
            java.lang.String r1 = "accs"
            java.lang.String r2 = "send_fail"
            java.lang.String r3 = "1"
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r7 = "distribute error "
            r6.append(r7)
            r6.append(r4)
            java.lang.String r0 = com.taobao.accs.utl.UtilityImpl.getStackMsg(r0)
            r6.append(r0)
            java.lang.String r0 = r6.toString()
            com.taobao.accs.utl.AppMonitorAdapter.commitAlarmFail(r1, r2, r5, r3, r0)
        L_0x0290:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.accs.data.MsgDistribute.distribute(android.content.Context, com.taobao.accs.net.BaseConnection, android.content.Intent):void");
    }

    /* access modifiers changed from: protected */
    public boolean checkSpace(int i, String str, String str2) {
        if (i != 100 && !GlobalClientInfo.AGOO_SERVICE_ID.equals(str)) {
            long usableSpace = UtilityImpl.getUsableSpace();
            if (usableSpace != -1 && usableSpace <= 5242880) {
                AppMonitorAdapter.commitAlarmFail("accs", BaseMonitor.ALARM_POINT_REQ_ERROR, str, "1", "space low " + usableSpace);
                ALog.e(TAG, "user space low, don't distribute", "size", Long.valueOf(usableSpace), "serviceId", str);
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean handleMsgInChannelProcess(Context context, String str, String str2, Intent intent, IAppReceiver iAppReceiver) {
        try {
            if (TextUtils.isEmpty(str)) {
                return false;
            }
            String service = GlobalClientInfo.getInstance(context).getService(intent.getStringExtra(Constants.KEY_CONFIG_TAG), str);
            if (TextUtils.isEmpty(service) && iAppReceiver != null) {
                service = iAppReceiver.getService(str);
            }
            if (TextUtils.isEmpty(service)) {
                service = GlobalClientInfo.getInstance(context).getService(str);
            }
            if (!TextUtils.isEmpty(service) || UtilityImpl.isMainProcess(context)) {
                return false;
            }
            if ("accs".equals(str)) {
                ALog.e(TAG, "start MsgDistributeService", Constants.KEY_DATA_ID, str2);
            } else {
                ALog.i(TAG, "start MsgDistributeService", Constants.KEY_DATA_ID, str2);
            }
            intent.setClassName(intent.getPackage(), getMsgDistributeService(context));
            IntentDispatch.dispatchIntent(context, intent);
            return true;
        } catch (Throwable th) {
            ALog.e(TAG, "handleMsgInChannelProcess", th, new Object[0]);
            return false;
        }
    }

    private void handleControlMsg(Context context, BaseConnection baseConnection, Intent intent, String str, String str2, int i, String str3, String str4, String str5, IAppReceiver iAppReceiver, int i2) {
        BaseConnection baseConnection2 = baseConnection;
        String str6 = str2;
        int i3 = i;
        String str7 = str3;
        String str8 = str4;
        String str9 = str5;
        IAppReceiver iAppReceiver2 = iAppReceiver;
        int i4 = i2;
        if (ALog.isPrintLog(ALog.Level.D)) {
            Object[] objArr = new Object[12];
            objArr[0] = Constants.KEY_CONFIG_TAG;
            objArr[1] = str;
            objArr[2] = Constants.KEY_DATA_ID;
            objArr[3] = str9;
            objArr[4] = "serviceId";
            objArr[5] = str8;
            objArr[6] = "command";
            objArr[7] = Integer.valueOf(i);
            objArr[8] = "errorCode";
            objArr[9] = Integer.valueOf(i2);
            objArr[10] = "appReceiver";
            objArr[11] = iAppReceiver2 == null ? null : iAppReceiver.getClass().getName();
            ALog.d(TAG, "handleControlMsg", objArr);
        }
        if (iAppReceiver2 != null) {
            switch (i3) {
                case 1:
                    if (!(iAppReceiver2 instanceof IAppReceiverV1)) {
                        iAppReceiver.onBindApp(i2);
                        break;
                    } else {
                        ((IAppReceiverV1) iAppReceiver2).onBindApp(i4, (String) null);
                        break;
                    }
                case 2:
                    if (i4 == 200) {
                        UtilityImpl.disableService(context);
                    }
                    iAppReceiver.onUnbindApp(i2);
                    break;
                case 3:
                    iAppReceiver2.onBindUser(str7, i4);
                    break;
                case 4:
                    iAppReceiver.onUnbindUser(i2);
                    break;
                default:
                    switch (i3) {
                        case 100:
                            if (TextUtils.isEmpty(str4)) {
                                iAppReceiver2.onSendData(str9, i4);
                                break;
                            }
                            break;
                        case 101:
                            if (TextUtils.isEmpty(str4)) {
                                ALog.d(TAG, "handleControlMsg serviceId isEmpty", new Object[0]);
                                byte[] byteArrayExtra = intent.getByteArrayExtra("data");
                                if (byteArrayExtra != null) {
                                    iAppReceiver2.onData(str7, str9, byteArrayExtra);
                                    break;
                                }
                            }
                            break;
                    }
            }
        }
        if (i3 == 1 && GlobalClientInfo.mAgooAppReceiver != null && str6 != null && str6.equals(Config.getAgooAppKey(context))) {
            ALog.d(TAG, "handleControlMsg agoo receiver onBindApp", new Object[0]);
            GlobalClientInfo.mAgooAppReceiver.onBindApp(i4, (String) null);
        } else if (iAppReceiver2 == null && i3 != 104 && i3 != 103) {
            if (baseConnection2 != null) {
                baseConnection2.send(Message.buildErrorReportMessage(str9, str8, baseConnection2.getHost((String) null), 0), true);
            }
            AppMonitorAdapter.commitAlarmFail("accs", BaseMonitor.ALARM_POINT_REQ_ERROR, str8, "1", "appReceiver null return");
            UTMini.getInstance().commitEvent(66001, "MsgToBuss7", "commandId=" + i3, "serviceId=" + str8 + " errorCode=" + i4 + " dataId=" + str9, Integer.valueOf(Constants.SDK_VERSION_CODE));
        }
    }

    /* access modifiers changed from: protected */
    public void handleBusinessMsg(Context context, BaseConnection baseConnection, IAppReceiver iAppReceiver, Intent intent, String str, String str2, int i, int i2) {
        Context context2 = context;
        BaseConnection baseConnection2 = baseConnection;
        IAppReceiver iAppReceiver2 = iAppReceiver;
        Intent intent2 = intent;
        String str3 = str;
        String str4 = str2;
        int i3 = i;
        ALog.i(TAG, "handleBusinessMsg start", Constants.KEY_DATA_ID, str4, "serviceId", str3, "command", Integer.valueOf(i));
        String service = GlobalClientInfo.getInstance(context).getService(intent2.getStringExtra(Constants.KEY_CONFIG_TAG), str3);
        if (TextUtils.isEmpty(service) && iAppReceiver2 != null) {
            service = iAppReceiver2.getService(str3);
        }
        if (TextUtils.isEmpty(service)) {
            service = GlobalClientInfo.getInstance(context).getService(str3);
        }
        if (!TextUtils.isEmpty(service)) {
            if (ALog.isPrintLog(ALog.Level.D)) {
                ALog.d(TAG, "handleBusinessMsg to start service", PushClientConstants.TAG_CLASS_NAME, service);
            }
            intent2.setClassName(context2, service);
            IntentDispatch.dispatchIntent(context2, intent2);
        } else {
            AccsDataListener listener = GlobalClientInfo.getInstance(context).getListener(str3);
            if (listener != null) {
                if (ALog.isPrintLog(ALog.Level.D)) {
                    ALog.d(TAG, "handleBusinessMsg getListener not null", new Object[0]);
                }
                AccsHandler.onReceiveData(context2, intent2, listener);
            } else {
                if (baseConnection2 != null) {
                    baseConnection2.send(Message.buildErrorReportMessage(str4, str3, baseConnection2.getHost((String) null), 0), true);
                }
                ALog.e(TAG, "handleBusinessMsg getListener also null", new Object[0]);
                AppMonitorAdapter.commitAlarmFail("accs", BaseMonitor.ALARM_POINT_REQ_ERROR, str3, "1", "service is null");
            }
        }
        UTMini.getInstance().commitEvent(66001, "MsgToBuss", "commandId=" + i3, "serviceId=" + str3 + " errorCode=" + i2 + " dataId=" + str4, Integer.valueOf(Constants.SDK_VERSION_CODE));
        StringBuilder sb = new StringBuilder();
        sb.append("2commandId=");
        sb.append(i3);
        sb.append("serviceId=");
        sb.append(str3);
        AppMonitorAdapter.commitCount("accs", BaseMonitor.COUNT_POINT_TO_BUSS, sb.toString(), 0.0d);
    }

    /* access modifiers changed from: protected */
    public void handBroadCastMsg(Context context, Map<String, IAppReceiver> map, Intent intent, int i, int i2) {
        Context context2 = context;
        Intent intent2 = intent;
        int i3 = i;
        ALog.i(TAG, "handBroadCastMsg", "command", Integer.valueOf(i));
        HashMap hashMap = new HashMap();
        if (map != null) {
            for (Map.Entry next : map.entrySet()) {
                Map<String, String> allService = GlobalClientInfo.getInstance(context).getAllService((String) next.getKey());
                if (allService == null) {
                    allService = ((IAppReceiver) next.getValue()).getAllServices();
                }
                if (allService != null) {
                    hashMap.putAll(allService);
                }
            }
        }
        if (i3 == 103) {
            for (String str : hashMap.keySet()) {
                if ("accs".equals(str) || "windvane".equals(str) || "motu-remote".equals(str)) {
                    String str2 = (String) hashMap.get(str);
                    if (TextUtils.isEmpty(str2)) {
                        str2 = GlobalClientInfo.getInstance(context).getService(str);
                    }
                    if (!TextUtils.isEmpty(str2)) {
                        intent2.setClassName(context2, str2);
                        IntentDispatch.dispatchIntent(context2, intent2);
                    }
                }
            }
            boolean booleanExtra = intent2.getBooleanExtra(Constants.KEY_CONNECT_AVAILABLE, false);
            String stringExtra = intent2.getStringExtra("host");
            String stringExtra2 = intent2.getStringExtra(Constants.KEY_ERROR_DETAIL);
            boolean booleanExtra2 = intent2.getBooleanExtra(Constants.KEY_TYPE_INAPP, false);
            boolean booleanExtra3 = intent2.getBooleanExtra(Constants.KEY_CENTER_HOST, false);
            TaoBaseService.ConnectInfo connectInfo = null;
            if (!TextUtils.isEmpty(stringExtra)) {
                if (booleanExtra) {
                    connectInfo = new TaoBaseService.ConnectInfo(stringExtra, booleanExtra2, booleanExtra3);
                } else {
                    connectInfo = new TaoBaseService.ConnectInfo(stringExtra, booleanExtra2, booleanExtra3, i2, stringExtra2);
                }
                connectInfo.connected = booleanExtra;
            }
            if (connectInfo != null) {
                ALog.d(TAG, "handBroadCastMsg ACTION_CONNECT_INFO", connectInfo);
                Intent intent3 = new Intent(Constants.ACTION_CONNECT_INFO);
                intent3.setPackage(context.getPackageName());
                intent3.putExtra(Constants.KEY_CONNECT_INFO, connectInfo);
                context2.sendBroadcast(intent3);
                return;
            }
            ALog.e(TAG, "handBroadCastMsg connect info null, host empty", new Object[0]);
        } else if (i3 == 104) {
            for (String str3 : hashMap.keySet()) {
                String str4 = (String) hashMap.get(str3);
                if (TextUtils.isEmpty(str4)) {
                    str4 = GlobalClientInfo.getInstance(context).getService(str3);
                }
                if (!TextUtils.isEmpty(str4)) {
                    intent2.setClassName(context2, str4);
                    IntentDispatch.dispatchIntent(context2, intent2);
                }
            }
        } else {
            ALog.w(TAG, "handBroadCastMsg not handled command", new Object[0]);
        }
    }

    private boolean handleRoutingMsgAck(Context context, Intent intent, String str, String str2) {
        boolean z;
        boolean booleanExtra = intent.getBooleanExtra(KEY_ROUTING_ACK, false);
        boolean booleanExtra2 = intent.getBooleanExtra(KEY_ROUTING_MSG, false);
        if (booleanExtra) {
            ALog.e(TAG, "recieve routiong ack", Constants.KEY_DATA_ID, str, "serviceId", str2);
            if (mRoutingDataIds != null) {
                mRoutingDataIds.remove(str);
            }
            AppMonitorAdapter.commitAlarmSuccess("accs", BaseMonitor.ALARM_MSG_ROUTING_RATE, "");
            z = true;
        } else {
            z = false;
        }
        if (booleanExtra2) {
            try {
                String stringExtra = intent.getStringExtra("packageName");
                ALog.e(TAG, "send routiong ack", Constants.KEY_DATA_ID, str, "to pkg", stringExtra, "serviceId", str2);
                Intent intent2 = new Intent(Constants.ACTION_COMMAND);
                intent2.putExtra("command", 106);
                intent2.setClassName(stringExtra, AdapterUtilityImpl.channelService);
                intent2.putExtra(KEY_ROUTING_ACK, true);
                intent2.putExtra("packageName", stringExtra);
                intent2.putExtra(Constants.KEY_DATA_ID, str);
                IntentDispatch.dispatchIntent(context, intent);
            } catch (Throwable th) {
                ALog.e(TAG, "send routing ack", th, "serviceId", str2);
            }
        }
        return z;
    }

    private boolean handleRoutingMsg(Context context, final Intent intent, final String str, final String str2) {
        if (context.getPackageName().equals(intent.getPackage())) {
            return false;
        }
        try {
            ALog.e(TAG, "start MsgDistributeService", "receive pkg", context.getPackageName(), "target pkg", intent.getPackage(), "serviceId", str2);
            intent.setClassName(intent.getPackage(), AdapterUtilityImpl.msgService);
            intent.putExtra(KEY_ROUTING_MSG, true);
            intent.putExtra("packageName", context.getPackageName());
            IntentDispatch.dispatchIntent(context, intent);
            if (mRoutingDataIds == null) {
                mRoutingDataIds = new HashSet();
            }
            mRoutingDataIds.add(str);
            ThreadPoolExecutorFactory.schedule(new Runnable() {
                public void run() {
                    if (MsgDistribute.mRoutingDataIds != null && MsgDistribute.mRoutingDataIds.contains(str)) {
                        ALog.e(MsgDistribute.TAG, "routing msg time out, try election", Constants.KEY_DATA_ID, str, "serviceId", str2);
                        MsgDistribute.mRoutingDataIds.remove(str);
                        AppMonitorAdapter.commitAlarmFail("accs", BaseMonitor.ALARM_MSG_ROUTING_RATE, "", "timeout", "pkg:" + intent.getPackage());
                    }
                }
            }, 10, TimeUnit.SECONDS);
        } catch (Throwable th) {
            AppMonitorAdapter.commitAlarmFail("accs", BaseMonitor.ALARM_MSG_ROUTING_RATE, "", UCCore.EVENT_EXCEPTION, th.toString());
            ALog.e(TAG, "routing msg error, try election", th, "serviceId", str2, Constants.KEY_DATA_ID, str);
        }
        return true;
    }
}
