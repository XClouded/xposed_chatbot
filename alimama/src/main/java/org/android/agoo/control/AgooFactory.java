package org.android.agoo.control;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import com.alibaba.analytics.core.sync.UploadQueueMgr;
import com.taobao.accs.base.TaoBaseService;
import com.taobao.accs.client.AdapterGlobalClientInfo;
import com.taobao.accs.common.ThreadPoolExecutorFactory;
import com.taobao.accs.dispatch.IntentDispatch;
import com.taobao.accs.utl.ALog;
import com.taobao.accs.utl.AdapterUtilityImpl;
import com.taobao.accs.utl.AppMonitorAdapter;
import com.taobao.accs.utl.BaseMonitor;
import com.taobao.accs.utl.UTMini;
import java.util.ArrayList;
import java.util.Iterator;
import org.android.agoo.common.AgooConstants;
import org.android.agoo.common.Config;
import org.android.agoo.common.MsgDO;
import org.android.agoo.message.MessageService;
import org.android.agoo.service.SendMessage;
import org.json.JSONArray;
import org.json.JSONObject;

public class AgooFactory {
    private static final String DEAL_MESSAGE = "accs.msgRecevie";
    private static final String TAG = "AgooFactory";
    /* access modifiers changed from: private */
    public static Context mContext;
    /* access modifiers changed from: private */
    public MessageService messageService = null;
    protected NotifManager notifyManager = null;

    public void init(Context context, NotifManager notifManager, MessageService messageService2) {
        mContext = context.getApplicationContext();
        this.notifyManager = notifManager;
        if (this.notifyManager == null) {
            this.notifyManager = new NotifManager();
        }
        this.notifyManager.init(mContext);
        this.messageService = messageService2;
        if (this.messageService == null) {
            this.messageService = new MessageService();
        }
        this.messageService.init(mContext);
    }

    public void saveMsg(byte[] bArr) {
        saveMsg(bArr, (String) null);
    }

    public void saveMsg(final byte[] bArr, final String str) {
        if (bArr != null && bArr.length > 0) {
            ThreadPoolExecutorFactory.execute(new Runnable() {
                public void run() {
                    try {
                        String str = new String(bArr, "utf-8");
                        JSONArray jSONArray = new JSONArray(str);
                        int length = jSONArray.length();
                        if (length == 1) {
                            String str2 = null;
                            String str3 = null;
                            for (int i = 0; i < length; i++) {
                                JSONObject jSONObject = jSONArray.getJSONObject(i);
                                if (jSONObject != null) {
                                    str2 = jSONObject.getString(UploadQueueMgr.MSGTYPE_INTERVAL);
                                    str3 = jSONObject.getString("p");
                                }
                            }
                            if (ALog.isPrintLog(ALog.Level.I)) {
                                ALog.i(AgooFactory.TAG, "saveMsg msgId:" + str2 + ",message=" + str + ",currentPack=" + str3 + ",reportTimes=" + Config.getReportCacheMsg(AgooFactory.mContext), new Object[0]);
                            }
                            if (!TextUtils.isEmpty(str3) && TextUtils.equals(str3, AgooFactory.mContext.getPackageName())) {
                                if (TextUtils.isEmpty(str)) {
                                    AgooFactory.this.messageService.addAccsMessage(str2, str, "0");
                                } else {
                                    AgooFactory.this.messageService.addAccsMessage(str2, str, str);
                                }
                            }
                        }
                    } catch (Throwable th) {
                        ALog.e(AgooFactory.TAG, "saveMsg fail:" + th.toString(), new Object[0]);
                    }
                }
            });
        }
    }

    public void msgRecevie(byte[] bArr, String str) {
        msgRecevie(bArr, str, (TaoBaseService.ExtraInfo) null);
    }

    public void msgRecevie(final byte[] bArr, final String str, final TaoBaseService.ExtraInfo extraInfo) {
        try {
            if (ALog.isPrintLog(ALog.Level.I)) {
                ALog.i(TAG, "into--[AgooFactory,msgRecevie]:messageSource=" + str, new Object[0]);
            }
            ThreadPoolExecutorFactory.execute(new Runnable() {
                public void run() {
                    AgooFactory.this.msgReceiverPreHandler(bArr, str, extraInfo, true);
                }
            });
        } catch (Throwable th) {
            ALog.e(TAG, "serviceImpl init task fail:" + th.toString(), new Object[0]);
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(3:57|58|(1:60)) */
    /* JADX WARNING: Code restructure failed: missing block: B:59:0x0213, code lost:
        if (com.taobao.accs.utl.ALog.isPrintLog(com.taobao.accs.utl.ALog.Level.I) != false) goto L_0x0215;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x0215, code lost:
        com.taobao.accs.utl.ALog.i(TAG, "agoo msg has no time", new java.lang.Object[0]);
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:57:0x020d */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.os.Bundle msgReceiverPreHandler(byte[] r30, java.lang.String r31, com.taobao.accs.base.TaoBaseService.ExtraInfo r32, boolean r33) {
        /*
            r29 = this;
            r8 = r29
            r0 = r30
            r9 = r31
            r10 = r32
            r1 = 66002(0x101d2, float:9.2489E-41)
            r11 = 0
            r12 = 0
            if (r0 == 0) goto L_0x02ab
            int r2 = r0.length     // Catch:{ Throwable -> 0x02a9 }
            if (r2 > 0) goto L_0x0014
            goto L_0x02ab
        L_0x0014:
            java.lang.String r13 = new java.lang.String     // Catch:{ Throwable -> 0x02a9 }
            java.lang.String r2 = "utf-8"
            r13.<init>(r0, r2)     // Catch:{ Throwable -> 0x02a9 }
            com.taobao.accs.utl.ALog$Level r0 = com.taobao.accs.utl.ALog.Level.I     // Catch:{ Throwable -> 0x02a9 }
            boolean r0 = com.taobao.accs.utl.ALog.isPrintLog(r0)     // Catch:{ Throwable -> 0x02a9 }
            if (r0 == 0) goto L_0x004e
            java.lang.String r0 = "AgooFactory"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x02a9 }
            r2.<init>()     // Catch:{ Throwable -> 0x02a9 }
            java.lang.String r3 = "msgRecevie,message--->["
            r2.append(r3)     // Catch:{ Throwable -> 0x02a9 }
            r2.append(r13)     // Catch:{ Throwable -> 0x02a9 }
            java.lang.String r3 = "]"
            r2.append(r3)     // Catch:{ Throwable -> 0x02a9 }
            java.lang.String r3 = ",utdid="
            r2.append(r3)     // Catch:{ Throwable -> 0x02a9 }
            android.content.Context r3 = mContext     // Catch:{ Throwable -> 0x02a9 }
            java.lang.String r3 = com.taobao.accs.utl.AdapterUtilityImpl.getDeviceId(r3)     // Catch:{ Throwable -> 0x02a9 }
            r2.append(r3)     // Catch:{ Throwable -> 0x02a9 }
            java.lang.String r2 = r2.toString()     // Catch:{ Throwable -> 0x02a9 }
            java.lang.Object[] r3 = new java.lang.Object[r12]     // Catch:{ Throwable -> 0x02a9 }
            com.taobao.accs.utl.ALog.i(r0, r2, r3)     // Catch:{ Throwable -> 0x02a9 }
        L_0x004e:
            boolean r0 = android.text.TextUtils.isEmpty(r13)     // Catch:{ Throwable -> 0x02a9 }
            if (r0 == 0) goto L_0x0084
            com.taobao.accs.utl.UTMini r0 = com.taobao.accs.utl.UTMini.getInstance()     // Catch:{ Throwable -> 0x02a9 }
            java.lang.String r2 = "accs.msgRecevie"
            android.content.Context r3 = mContext     // Catch:{ Throwable -> 0x02a9 }
            java.lang.String r3 = com.taobao.accs.utl.AdapterUtilityImpl.getDeviceId(r3)     // Catch:{ Throwable -> 0x02a9 }
            java.lang.String r4 = "message==null"
            r0.commitEvent(r1, r2, r3, r4)     // Catch:{ Throwable -> 0x02a9 }
            java.lang.String r0 = "AgooFactory"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x02a9 }
            r1.<init>()     // Catch:{ Throwable -> 0x02a9 }
            java.lang.String r2 = "handleMessage message==null,utdid="
            r1.append(r2)     // Catch:{ Throwable -> 0x02a9 }
            android.content.Context r2 = mContext     // Catch:{ Throwable -> 0x02a9 }
            java.lang.String r2 = com.taobao.accs.utl.AdapterUtilityImpl.getDeviceId(r2)     // Catch:{ Throwable -> 0x02a9 }
            r1.append(r2)     // Catch:{ Throwable -> 0x02a9 }
            java.lang.String r1 = r1.toString()     // Catch:{ Throwable -> 0x02a9 }
            java.lang.Object[] r2 = new java.lang.Object[r12]     // Catch:{ Throwable -> 0x02a9 }
            com.taobao.accs.utl.ALog.i(r0, r1, r2)     // Catch:{ Throwable -> 0x02a9 }
            return r11
        L_0x0084:
            org.json.JSONArray r0 = new org.json.JSONArray     // Catch:{ Throwable -> 0x02a9 }
            r0.<init>(r13)     // Catch:{ Throwable -> 0x02a9 }
            int r14 = r0.length()     // Catch:{ Throwable -> 0x02a9 }
            java.lang.StringBuilder r15 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x02a9 }
            r15.<init>()     // Catch:{ Throwable -> 0x02a9 }
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x02a9 }
            r7.<init>()     // Catch:{ Throwable -> 0x02a9 }
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x02a9 }
            r6.<init>()     // Catch:{ Throwable -> 0x02a9 }
            r1 = r11
            r2 = r1
            r5 = 0
        L_0x009f:
            if (r5 >= r14) goto L_0x0284
            android.os.Bundle r4 = new android.os.Bundle     // Catch:{ Throwable -> 0x02a9 }
            r4.<init>()     // Catch:{ Throwable -> 0x02a9 }
            org.json.JSONObject r1 = r0.getJSONObject(r5)     // Catch:{ Throwable -> 0x02a9 }
            if (r1 != 0) goto L_0x00b8
            r22 = r0
            r11 = r4
            r16 = r5
            r12 = r13
            r27 = r14
            r14 = r6
            r13 = r7
            goto L_0x0276
        L_0x00b8:
            org.android.agoo.common.MsgDO r3 = new org.android.agoo.common.MsgDO     // Catch:{ Throwable -> 0x02a9 }
            r3.<init>()     // Catch:{ Throwable -> 0x02a9 }
            java.lang.String r11 = "p"
            java.lang.String r11 = r1.getString(r11)     // Catch:{ Throwable -> 0x02a9 }
            java.lang.String r12 = "i"
            java.lang.String r12 = r1.getString(r12)     // Catch:{ Throwable -> 0x02a9 }
            r22 = r0
            java.lang.String r0 = "b"
            java.lang.String r0 = r1.getString(r0)     // Catch:{ Throwable -> 0x02a9 }
            r23 = r2
            java.lang.String r2 = "f"
            r25 = r6
            r24 = r7
            long r6 = r1.getLong(r2)     // Catch:{ Throwable -> 0x02a9 }
            java.lang.String r2 = "ext"
            boolean r2 = r1.isNull(r2)     // Catch:{ Throwable -> 0x02a9 }
            if (r2 != 0) goto L_0x00ec
            java.lang.String r2 = "ext"
            java.lang.String r2 = r1.getString(r2)     // Catch:{ Throwable -> 0x02a9 }
            goto L_0x00ee
        L_0x00ec:
            r2 = r23
        L_0x00ee:
            r15.append(r12)     // Catch:{ Throwable -> 0x02a9 }
            r26 = r13
            int r13 = r14 + -1
            if (r5 >= r13) goto L_0x00ff
            r27 = r14
            java.lang.String r14 = ","
            r15.append(r14)     // Catch:{ Throwable -> 0x02a9 }
            goto L_0x0101
        L_0x00ff:
            r27 = r14
        L_0x0101:
            r3.msgIds = r12     // Catch:{ Throwable -> 0x02a9 }
            r3.extData = r2     // Catch:{ Throwable -> 0x02a9 }
            r3.removePacks = r11     // Catch:{ Throwable -> 0x02a9 }
            r3.messageSource = r9     // Catch:{ Throwable -> 0x02a9 }
            boolean r14 = android.text.TextUtils.isEmpty(r0)     // Catch:{ Throwable -> 0x02a9 }
            if (r14 == 0) goto L_0x0125
            java.lang.String r0 = "11"
            r3.errorCode = r0     // Catch:{ Throwable -> 0x02a9 }
            org.android.agoo.control.NotifManager r0 = r8.notifyManager     // Catch:{ Throwable -> 0x02a9 }
            r0.handlerACKMessage(r3, r10)     // Catch:{ Throwable -> 0x02a9 }
        L_0x0118:
            r23 = r2
            r11 = r4
            r16 = r5
            r13 = r24
            r14 = r25
        L_0x0121:
            r12 = r26
            goto L_0x0274
        L_0x0125:
            boolean r14 = android.text.TextUtils.isEmpty(r11)     // Catch:{ Throwable -> 0x02a9 }
            if (r14 == 0) goto L_0x0135
            java.lang.String r0 = "12"
            r3.errorCode = r0     // Catch:{ Throwable -> 0x02a9 }
            org.android.agoo.control.NotifManager r0 = r8.notifyManager     // Catch:{ Throwable -> 0x02a9 }
            r0.handlerACKMessage(r3, r10)     // Catch:{ Throwable -> 0x02a9 }
            goto L_0x0118
        L_0x0135:
            r16 = -1
            int r14 = (r6 > r16 ? 1 : (r6 == r16 ? 0 : -1))
            if (r14 != 0) goto L_0x0145
            java.lang.String r0 = "13"
            r3.errorCode = r0     // Catch:{ Throwable -> 0x02a9 }
            org.android.agoo.control.NotifManager r0 = r8.notifyManager     // Catch:{ Throwable -> 0x02a9 }
            r0.handlerACKMessage(r3, r10)     // Catch:{ Throwable -> 0x02a9 }
            goto L_0x0118
        L_0x0145:
            android.content.Context r14 = mContext     // Catch:{ Throwable -> 0x02a9 }
            boolean r14 = checkPackage(r14, r11)     // Catch:{ Throwable -> 0x02a9 }
            if (r14 != 0) goto L_0x0199
            java.lang.String r0 = "AgooFactory"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x02a9 }
            r1.<init>()     // Catch:{ Throwable -> 0x02a9 }
            java.lang.String r3 = "msgRecevie checkpackage is del,pack="
            r1.append(r3)     // Catch:{ Throwable -> 0x02a9 }
            r1.append(r11)     // Catch:{ Throwable -> 0x02a9 }
            java.lang.String r1 = r1.toString()     // Catch:{ Throwable -> 0x02a9 }
            r3 = 0
            java.lang.Object[] r6 = new java.lang.Object[r3]     // Catch:{ Throwable -> 0x02a9 }
            com.taobao.accs.utl.ALog.d(r0, r1, r6)     // Catch:{ Throwable -> 0x02a9 }
            com.taobao.accs.utl.UTMini r16 = com.taobao.accs.utl.UTMini.getInstance()     // Catch:{ Throwable -> 0x02a9 }
            r17 = 66002(0x101d2, float:9.2489E-41)
            java.lang.String r18 = "accs.msgRecevie"
            android.content.Context r0 = mContext     // Catch:{ Throwable -> 0x02a9 }
            java.lang.String r19 = com.taobao.accs.utl.AdapterUtilityImpl.getDeviceId(r0)     // Catch:{ Throwable -> 0x02a9 }
            java.lang.String r20 = "deletePack"
            r21 = r11
            r16.commitEvent(r17, r18, r19, r20, r21)     // Catch:{ Throwable -> 0x02a9 }
            r14 = r25
            r14.append(r11)     // Catch:{ Throwable -> 0x02a9 }
            r0 = r24
            r0.append(r12)     // Catch:{ Throwable -> 0x02a9 }
            if (r5 >= r13) goto L_0x0192
            java.lang.String r1 = ","
            r14.append(r1)     // Catch:{ Throwable -> 0x02a9 }
            java.lang.String r1 = ","
            r0.append(r1)     // Catch:{ Throwable -> 0x02a9 }
        L_0x0192:
            r13 = r0
            r23 = r2
            r11 = r4
            r16 = r5
            goto L_0x0121
        L_0x0199:
            r13 = r24
            r14 = r25
            android.os.Bundle r6 = getFlag(r6, r3)     // Catch:{ Throwable -> 0x02a9 }
            java.lang.String r7 = "encrypted"
            java.lang.String r7 = r6.getString(r7)     // Catch:{ Throwable -> 0x02a9 }
            android.content.Context r16 = mContext     // Catch:{ Throwable -> 0x02a9 }
            r28 = r5
            java.lang.String r5 = r16.getPackageName()     // Catch:{ Throwable -> 0x02a9 }
            boolean r5 = r5.equals(r11)     // Catch:{ Throwable -> 0x02a9 }
            if (r5 == 0) goto L_0x01f4
            r5 = 4
            java.lang.String r5 = java.lang.Integer.toString(r5)     // Catch:{ Throwable -> 0x02a9 }
            boolean r5 = android.text.TextUtils.equals(r7, r5)     // Catch:{ Throwable -> 0x02a9 }
            if (r5 == 0) goto L_0x01c2
            r5 = 0
            goto L_0x01f6
        L_0x01c2:
            java.lang.String r0 = "AgooFactory"
            java.lang.String r1 = "msgRecevie msg encrypted flag not exist, cannot prase!!!"
            r5 = 0
            java.lang.Object[] r6 = new java.lang.Object[r5]     // Catch:{ Throwable -> 0x02a9 }
            com.taobao.accs.utl.ALog.e(r0, r1, r6)     // Catch:{ Throwable -> 0x02a9 }
            com.taobao.accs.utl.UTMini r16 = com.taobao.accs.utl.UTMini.getInstance()     // Catch:{ Throwable -> 0x02a9 }
            r17 = 66002(0x101d2, float:9.2489E-41)
            java.lang.String r18 = "accs.msgRecevie"
            android.content.Context r0 = mContext     // Catch:{ Throwable -> 0x02a9 }
            java.lang.String r19 = com.taobao.accs.utl.AdapterUtilityImpl.getDeviceId(r0)     // Catch:{ Throwable -> 0x02a9 }
            java.lang.String r20 = "encrypted!=4"
            java.lang.String r21 = "15"
            r16.commitEvent(r17, r18, r19, r20, r21)     // Catch:{ Throwable -> 0x02a9 }
            java.lang.String r0 = "24"
            r3.errorCode = r0     // Catch:{ Throwable -> 0x02a9 }
            org.android.agoo.control.NotifManager r0 = r8.notifyManager     // Catch:{ Throwable -> 0x02a9 }
            r0.handlerACKMessage(r3, r10)     // Catch:{ Throwable -> 0x02a9 }
            r23 = r2
            r11 = r4
            r12 = r26
            r16 = r28
            goto L_0x0274
        L_0x01f4:
            r3 = 1
            r5 = 1
        L_0x01f6:
            if (r6 == 0) goto L_0x01fb
            r4.putAll(r6)     // Catch:{ Throwable -> 0x02a9 }
        L_0x01fb:
            java.lang.String r3 = "t"
            java.lang.String r1 = r1.getString(r3)     // Catch:{ Throwable -> 0x020d }
            boolean r3 = android.text.TextUtils.isEmpty(r1)     // Catch:{ Throwable -> 0x020d }
            if (r3 != 0) goto L_0x021f
            java.lang.String r3 = "time"
            r4.putString(r3, r1)     // Catch:{ Throwable -> 0x020d }
            goto L_0x021f
        L_0x020d:
            com.taobao.accs.utl.ALog$Level r1 = com.taobao.accs.utl.ALog.Level.I     // Catch:{ Throwable -> 0x02a9 }
            boolean r1 = com.taobao.accs.utl.ALog.isPrintLog(r1)     // Catch:{ Throwable -> 0x02a9 }
            if (r1 == 0) goto L_0x021f
            java.lang.String r1 = "AgooFactory"
            java.lang.String r3 = "agoo msg has no time"
            r6 = 0
            java.lang.Object[] r7 = new java.lang.Object[r6]     // Catch:{ Throwable -> 0x02a9 }
            com.taobao.accs.utl.ALog.i(r1, r3, r7)     // Catch:{ Throwable -> 0x02a9 }
        L_0x021f:
            java.lang.String r1 = "trace"
            long r6 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x02a9 }
            r4.putLong(r1, r6)     // Catch:{ Throwable -> 0x02a9 }
            java.lang.String r1 = "id"
            r4.putString(r1, r12)     // Catch:{ Throwable -> 0x02a9 }
            java.lang.String r1 = "body"
            r4.putString(r1, r0)     // Catch:{ Throwable -> 0x02a9 }
            java.lang.String r0 = "source"
            r4.putString(r0, r11)     // Catch:{ Throwable -> 0x02a9 }
            java.lang.String r0 = "fromAppkey"
            android.content.Context r1 = mContext     // Catch:{ Throwable -> 0x02a9 }
            java.lang.String r1 = org.android.agoo.common.Config.getAgooAppKey(r1)     // Catch:{ Throwable -> 0x02a9 }
            r4.putString(r0, r1)     // Catch:{ Throwable -> 0x02a9 }
            java.lang.String r0 = "extData"
            r4.putString(r0, r2)     // Catch:{ Throwable -> 0x02a9 }
            java.lang.String r0 = "oriData"
            r12 = r26
            r4.putString(r0, r12)     // Catch:{ Throwable -> 0x02a9 }
            if (r33 == 0) goto L_0x0263
            android.content.Context r3 = mContext     // Catch:{ Throwable -> 0x02a9 }
            r1 = r29
            r23 = r2
            r2 = r3
            r3 = r11
            r11 = r4
            r16 = r28
            r6 = r31
            r7 = r32
            r1.sendMsgToBussiness(r2, r3, r4, r5, r6, r7)     // Catch:{ Throwable -> 0x02a9 }
            goto L_0x0274
        L_0x0263:
            r23 = r2
            r11 = r4
            r16 = r28
            java.lang.String r1 = "type"
            java.lang.String r2 = "common-push"
            r11.putString(r1, r2)     // Catch:{ Throwable -> 0x02a9 }
            java.lang.String r1 = "message_source"
            r11.putString(r1, r9)     // Catch:{ Throwable -> 0x02a9 }
        L_0x0274:
            r2 = r23
        L_0x0276:
            int r5 = r16 + 1
            r1 = r11
            r7 = r13
            r6 = r14
            r0 = r22
            r14 = r27
            r11 = 0
            r13 = r12
            r12 = 0
            goto L_0x009f
        L_0x0284:
            r14 = r6
            r13 = r7
            int r0 = r14.length()     // Catch:{ Throwable -> 0x02a9 }
            if (r0 <= 0) goto L_0x02a8
            org.android.agoo.common.MsgDO r0 = new org.android.agoo.common.MsgDO     // Catch:{ Throwable -> 0x02a9 }
            r0.<init>()     // Catch:{ Throwable -> 0x02a9 }
            java.lang.String r2 = r13.toString()     // Catch:{ Throwable -> 0x02a9 }
            r0.msgIds = r2     // Catch:{ Throwable -> 0x02a9 }
            java.lang.String r2 = r14.toString()     // Catch:{ Throwable -> 0x02a9 }
            r0.removePacks = r2     // Catch:{ Throwable -> 0x02a9 }
            java.lang.String r2 = "10"
            r0.errorCode = r2     // Catch:{ Throwable -> 0x02a9 }
            r0.messageSource = r9     // Catch:{ Throwable -> 0x02a9 }
            org.android.agoo.control.NotifManager r2 = r8.notifyManager     // Catch:{ Throwable -> 0x02a9 }
            r2.handlerACKMessage(r0, r10)     // Catch:{ Throwable -> 0x02a9 }
        L_0x02a8:
            return r1
        L_0x02a9:
            r0 = move-exception
            goto L_0x02dd
        L_0x02ab:
            com.taobao.accs.utl.UTMini r0 = com.taobao.accs.utl.UTMini.getInstance()     // Catch:{ Throwable -> 0x02a9 }
            java.lang.String r2 = "accs.msgRecevie"
            android.content.Context r3 = mContext     // Catch:{ Throwable -> 0x02a9 }
            java.lang.String r3 = com.taobao.accs.utl.AdapterUtilityImpl.getDeviceId(r3)     // Catch:{ Throwable -> 0x02a9 }
            java.lang.String r4 = "data==null"
            r0.commitEvent(r1, r2, r3, r4)     // Catch:{ Throwable -> 0x02a9 }
            java.lang.String r0 = "AgooFactory"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x02a9 }
            r1.<init>()     // Catch:{ Throwable -> 0x02a9 }
            java.lang.String r2 = "handleMessage data==null,utdid="
            r1.append(r2)     // Catch:{ Throwable -> 0x02a9 }
            android.content.Context r2 = mContext     // Catch:{ Throwable -> 0x02a9 }
            java.lang.String r2 = com.taobao.accs.utl.AdapterUtilityImpl.getDeviceId(r2)     // Catch:{ Throwable -> 0x02a9 }
            r1.append(r2)     // Catch:{ Throwable -> 0x02a9 }
            java.lang.String r1 = r1.toString()     // Catch:{ Throwable -> 0x02a9 }
            r2 = 0
            java.lang.Object[] r3 = new java.lang.Object[r2]     // Catch:{ Throwable -> 0x02a9 }
            com.taobao.accs.utl.ALog.i(r0, r1, r3)     // Catch:{ Throwable -> 0x02a9 }
            r1 = 0
            return r1
        L_0x02dd:
            com.taobao.accs.utl.ALog$Level r1 = com.taobao.accs.utl.ALog.Level.E
            boolean r1 = com.taobao.accs.utl.ALog.isPrintLog(r1)
            if (r1 == 0) goto L_0x02fe
            java.lang.String r1 = "AgooFactory"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "msgRecevie is error,e="
            r2.append(r3)
            r2.append(r0)
            java.lang.String r0 = r2.toString()
            r2 = 0
            java.lang.Object[] r2 = new java.lang.Object[r2]
            com.taobao.accs.utl.ALog.e(r1, r0, r2)
        L_0x02fe:
            r1 = 0
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: org.android.agoo.control.AgooFactory.msgReceiverPreHandler(byte[], java.lang.String, com.taobao.accs.base.TaoBaseService$ExtraInfo, boolean):android.os.Bundle");
    }

    /* JADX WARNING: Removed duplicated region for block: B:23:0x00b5 A[Catch:{ Throwable -> 0x00e7 }] */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x00dd A[Catch:{ Throwable -> 0x00e7 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String parseEncryptedMsg(java.lang.String r9) {
        /*
            r0 = 0
            r1 = 0
            android.content.Context r2 = mContext     // Catch:{ Throwable -> 0x00e7 }
            java.lang.String r2 = org.android.agoo.common.Config.getAgooAppKey(r2)     // Catch:{ Throwable -> 0x00e7 }
            java.lang.String r3 = "ACCS_SDK"
            android.content.Context r4 = mContext     // Catch:{ Throwable -> 0x00e7 }
            boolean r3 = com.taobao.accs.utl.UtilityImpl.utdidChanged(r3, r4)     // Catch:{ Throwable -> 0x00e7 }
            if (r3 == 0) goto L_0x001b
            java.lang.String r3 = "ACCS_SDK"
            android.content.Context r4 = mContext     // Catch:{ Throwable -> 0x00e7 }
            java.lang.String r3 = com.taobao.accs.utl.UtilityImpl.getUtdid(r3, r4)     // Catch:{ Throwable -> 0x00e7 }
            goto L_0x0021
        L_0x001b:
            android.content.Context r3 = mContext     // Catch:{ Throwable -> 0x00e7 }
            java.lang.String r3 = com.taobao.accs.utl.AdapterUtilityImpl.getDeviceId(r3)     // Catch:{ Throwable -> 0x00e7 }
        L_0x0021:
            boolean r4 = android.text.TextUtils.isEmpty(r3)     // Catch:{ Throwable -> 0x00e7 }
            if (r4 == 0) goto L_0x002d
            android.content.Context r3 = mContext     // Catch:{ Throwable -> 0x00e7 }
            java.lang.String r3 = com.taobao.accs.utl.AdapterUtilityImpl.getDeviceId(r3)     // Catch:{ Throwable -> 0x00e7 }
        L_0x002d:
            int r4 = com.taobao.accs.client.AdapterGlobalClientInfo.mSecurityType     // Catch:{ Throwable -> 0x00e7 }
            r5 = 2
            if (r4 != r5) goto L_0x0066
            java.lang.String r4 = com.taobao.accs.utl.AdapterUtilityImpl.mAgooAppSecret     // Catch:{ Throwable -> 0x00e7 }
            boolean r4 = android.text.TextUtils.isEmpty(r4)     // Catch:{ Throwable -> 0x00e7 }
            if (r4 != 0) goto L_0x005c
            java.lang.String r4 = com.taobao.accs.utl.AdapterUtilityImpl.mAgooAppSecret     // Catch:{ Throwable -> 0x00e7 }
            java.lang.String r5 = "utf-8"
            byte[] r4 = r4.getBytes(r5)     // Catch:{ Throwable -> 0x00e7 }
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x00e7 }
            r5.<init>()     // Catch:{ Throwable -> 0x00e7 }
            r5.append(r2)     // Catch:{ Throwable -> 0x00e7 }
            r5.append(r3)     // Catch:{ Throwable -> 0x00e7 }
            java.lang.String r3 = r5.toString()     // Catch:{ Throwable -> 0x00e7 }
            java.lang.String r5 = "utf-8"
            byte[] r3 = r3.getBytes(r5)     // Catch:{ Throwable -> 0x00e7 }
            byte[] r3 = org.android.agoo.common.EncryptUtil.hmacSha1(r4, r3)     // Catch:{ Throwable -> 0x00e7 }
            goto L_0x00b0
        L_0x005c:
            java.lang.String r3 = "AgooFactory"
            java.lang.String r4 = "getAppsign secret null"
            java.lang.Object[] r5 = new java.lang.Object[r1]     // Catch:{ Throwable -> 0x00e7 }
            com.taobao.accs.utl.ALog.e(r3, r4, r5)     // Catch:{ Throwable -> 0x00e7 }
            goto L_0x00af
        L_0x0066:
            android.content.Context r4 = mContext     // Catch:{ Throwable -> 0x00e7 }
            com.alibaba.wireless.security.open.SecurityGuardManager r4 = com.alibaba.wireless.security.open.SecurityGuardManager.getInstance(r4)     // Catch:{ Throwable -> 0x00e7 }
            if (r4 == 0) goto L_0x00a6
            java.lang.String r5 = "AgooFactory"
            java.lang.String r6 = "SecurityGuardManager not null!"
            java.lang.Object[] r7 = new java.lang.Object[r1]     // Catch:{ Throwable -> 0x00e7 }
            com.taobao.accs.utl.ALog.d(r5, r6, r7)     // Catch:{ Throwable -> 0x00e7 }
            com.alibaba.wireless.security.open.securesignature.ISecureSignatureComponent r4 = r4.getSecureSignatureComp()     // Catch:{ Throwable -> 0x00e7 }
            com.alibaba.wireless.security.open.SecurityGuardParamContext r5 = new com.alibaba.wireless.security.open.SecurityGuardParamContext     // Catch:{ Throwable -> 0x00e7 }
            r5.<init>()     // Catch:{ Throwable -> 0x00e7 }
            r5.appKey = r2     // Catch:{ Throwable -> 0x00e7 }
            java.util.Map<java.lang.String, java.lang.String> r6 = r5.paramMap     // Catch:{ Throwable -> 0x00e7 }
            java.lang.String r7 = "INPUT"
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x00e7 }
            r8.<init>()     // Catch:{ Throwable -> 0x00e7 }
            r8.append(r2)     // Catch:{ Throwable -> 0x00e7 }
            r8.append(r3)     // Catch:{ Throwable -> 0x00e7 }
            java.lang.String r3 = r8.toString()     // Catch:{ Throwable -> 0x00e7 }
            r6.put(r7, r3)     // Catch:{ Throwable -> 0x00e7 }
            r3 = 3
            r5.requestType = r3     // Catch:{ Throwable -> 0x00e7 }
            java.lang.String r3 = com.taobao.accs.client.AdapterGlobalClientInfo.mAuthCode     // Catch:{ Throwable -> 0x00e7 }
            java.lang.String r3 = r4.signRequest(r5, r3)     // Catch:{ Throwable -> 0x00e7 }
            byte[] r3 = org.android.agoo.common.EncryptUtil.hexStringToByteArray(r3)     // Catch:{ Throwable -> 0x00e7 }
            goto L_0x00b0
        L_0x00a6:
            java.lang.String r3 = "AgooFactory"
            java.lang.String r4 = "SecurityGuardManager is null"
            java.lang.Object[] r5 = new java.lang.Object[r1]     // Catch:{ Throwable -> 0x00e7 }
            com.taobao.accs.utl.ALog.e(r3, r4, r5)     // Catch:{ Throwable -> 0x00e7 }
        L_0x00af:
            r3 = r0
        L_0x00b0:
            if (r3 == 0) goto L_0x00dd
            int r4 = r3.length     // Catch:{ Throwable -> 0x00e7 }
            if (r4 <= 0) goto L_0x00dd
            r4 = 8
            byte[] r9 = android.util.Base64.decode(r9, r4)     // Catch:{ Throwable -> 0x00e7 }
            javax.crypto.spec.SecretKeySpec r4 = new javax.crypto.spec.SecretKeySpec     // Catch:{ Throwable -> 0x00e7 }
            byte[] r3 = org.android.agoo.common.EncryptUtil.md5(r3)     // Catch:{ Throwable -> 0x00e7 }
            java.lang.String r5 = "AES"
            r4.<init>(r3, r5)     // Catch:{ Throwable -> 0x00e7 }
            java.lang.String r3 = "utf-8"
            byte[] r2 = r2.getBytes(r3)     // Catch:{ Throwable -> 0x00e7 }
            byte[] r2 = org.android.agoo.common.EncryptUtil.md5(r2)     // Catch:{ Throwable -> 0x00e7 }
            byte[] r9 = org.android.agoo.common.EncryptUtil.aesDecrypt(r9, r4, r2)     // Catch:{ Throwable -> 0x00e7 }
            java.lang.String r2 = new java.lang.String     // Catch:{ Throwable -> 0x00e7 }
            java.lang.String r3 = "utf-8"
            r2.<init>(r9, r3)     // Catch:{ Throwable -> 0x00e7 }
            r0 = r2
            goto L_0x00f1
        L_0x00dd:
            java.lang.String r9 = "AgooFactory"
            java.lang.String r2 = "aesDecrypt key is null!"
            java.lang.Object[] r3 = new java.lang.Object[r1]     // Catch:{ Throwable -> 0x00e7 }
            com.taobao.accs.utl.ALog.e(r9, r2, r3)     // Catch:{ Throwable -> 0x00e7 }
            goto L_0x00f1
        L_0x00e7:
            r9 = move-exception
            java.lang.String r2 = "AgooFactory"
            java.lang.String r3 = "parseEncryptedMsg failure: "
            java.lang.Object[] r1 = new java.lang.Object[r1]
            com.taobao.accs.utl.ALog.e(r2, r3, r9, r1)
        L_0x00f1:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.android.agoo.control.AgooFactory.parseEncryptedMsg(java.lang.String):java.lang.String");
    }

    public void reportCacheMsg() {
        try {
            ThreadPoolExecutorFactory.execute(new Runnable() {
                public void run() {
                    ArrayList<MsgDO> unReportMsg = AgooFactory.this.messageService.getUnReportMsg();
                    if (unReportMsg != null && unReportMsg.size() > 0) {
                        ALog.e(AgooFactory.TAG, "reportCacheMsg", "size", Integer.valueOf(unReportMsg.size()));
                        Iterator<MsgDO> it = unReportMsg.iterator();
                        while (it.hasNext()) {
                            MsgDO next = it.next();
                            if (next != null) {
                                next.isFromCache = true;
                                AgooFactory.this.notifyManager.report(next, (TaoBaseService.ExtraInfo) null);
                            }
                        }
                    }
                }
            });
        } catch (Throwable th) {
            ALog.e(TAG, "reportCacheMsg fail:" + th.toString(), new Object[0]);
        }
    }

    public void updateMsg(final byte[] bArr, final boolean z) {
        ThreadPoolExecutorFactory.execute(new Runnable() {
            public void run() {
                try {
                    String str = new String(bArr, "utf-8");
                    if (TextUtils.isEmpty(str)) {
                        AppMonitorAdapter.commitCount("accs", BaseMonitor.COUNT_AGOO_FAIL_ACK, "msg==null", 0.0d);
                        return;
                    }
                    ALog.i(AgooFactory.TAG, "message = " + str, new Object[0]);
                    JSONObject jSONObject = new JSONObject(str);
                    String str2 = null;
                    String string = jSONObject.getString("api");
                    String string2 = jSONObject.getString("id");
                    if (TextUtils.equals(string, "agooReport")) {
                        str2 = jSONObject.getString("status");
                    }
                    if (TextUtils.equals(string, AgooConstants.AGOO_SERVICE_AGOOACK)) {
                        AppMonitorAdapter.commitCount("accs", BaseMonitor.COUNT_AGOO_SUCCESS_ACK, "handlerACKMessage", 0.0d);
                    }
                    if (!TextUtils.isEmpty(string) && !TextUtils.isEmpty(string2)) {
                        if (!TextUtils.isEmpty(str2)) {
                            if (ALog.isPrintLog(ALog.Level.I)) {
                                ALog.i(AgooFactory.TAG, "updateMsg data begin,api=" + string + ",id=" + string2 + ",status=" + str2 + ",reportTimes=" + Config.getReportCacheMsg(AgooFactory.mContext), new Object[0]);
                            }
                            if (TextUtils.equals(string, "agooReport")) {
                                if (TextUtils.equals(str2, "4") && z) {
                                    AgooFactory.this.messageService.updateAccsMessage(string2, "1");
                                } else if ((TextUtils.equals(str2, "8") || TextUtils.equals(str2, "9")) && z) {
                                    AgooFactory.this.messageService.updateAccsMessage(string2, MessageService.MSG_DB_COMPLETE);
                                }
                                AppMonitorAdapter.commitCount("accs", BaseMonitor.COUNT_AGOO_SUCCESS_ACK, str2, 0.0d);
                                return;
                            }
                            return;
                        }
                    }
                    AppMonitorAdapter.commitCount("accs", BaseMonitor.COUNT_AGOO_FAIL_ACK, "json key null", 0.0d);
                } catch (Throwable th) {
                    ALog.e(AgooFactory.TAG, "updateMsg get data error,e=" + th, new Object[0]);
                    AppMonitorAdapter.commitCount("accs", BaseMonitor.COUNT_AGOO_FAIL_ACK, "json exception", 0.0d);
                }
            }
        });
    }

    public void updateNotifyMsg(final String str, final String str2) {
        ThreadPoolExecutorFactory.execute(new Runnable() {
            public void run() {
                AgooFactory.this.updateMsgStatus(str, str2);
            }
        });
    }

    public void updateMsgStatus(String str, String str2) {
        try {
            if (TextUtils.isEmpty(str)) {
                return;
            }
            if (!TextUtils.isEmpty(str2)) {
                if (ALog.isPrintLog(ALog.Level.I)) {
                    ALog.i(TAG, "updateNotifyMsg begin,messageId=" + str + ",status=" + str2 + ",reportTimes=" + Config.getReportCacheMsg(mContext), new Object[0]);
                }
                if (TextUtils.equals(str2, "8")) {
                    this.messageService.updateAccsMessage(str, "2");
                } else if (TextUtils.equals(str2, "9")) {
                    this.messageService.updateAccsMessage(str, "3");
                }
            }
        } catch (Throwable th) {
            ALog.e(TAG, "updateNotifyMsg e=" + th.toString(), new Object[0]);
        }
    }

    private static final boolean checkPackage(Context context, String str) {
        try {
            if (context.getPackageManager().getApplicationInfo(str, 0) != null) {
                return true;
            }
            return false;
        } catch (Throwable unused) {
        }
    }

    private static Bundle getFlag(long j, MsgDO msgDO) {
        Bundle bundle = new Bundle();
        try {
            char[] charArray = Long.toBinaryString(j).toCharArray();
            if (charArray != null && 8 <= charArray.length) {
                if (8 <= charArray.length) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("");
                    sb.append(Integer.parseInt("" + charArray[1] + charArray[2] + charArray[3] + charArray[4], 2));
                    bundle.putString(AgooConstants.MESSAGE_ENCRYPTED, sb.toString());
                    if (charArray[6] == '1') {
                        bundle.putString(AgooConstants.MESSAGE_REPORT, "1");
                        msgDO.reportStr = "1";
                    }
                    if (charArray[7] == '1') {
                        bundle.putString(AgooConstants.MESSAGE_NOTIFICATION, "1");
                    }
                }
                if (9 <= charArray.length && charArray[8] == '1') {
                    bundle.putString(AgooConstants.MESSAGE_HAS_TEST, "1");
                }
                if (10 <= charArray.length && charArray[9] == '1') {
                    bundle.putString(AgooConstants.MESSAGE_DUPLICATE, "1");
                }
                if (11 <= charArray.length && charArray[10] == '1') {
                    bundle.putInt("popup", 1);
                }
            }
        } catch (Throwable unused) {
        }
        return bundle;
    }

    private void sendMsgToBussiness(Context context, String str, Bundle bundle, boolean z, String str2, TaoBaseService.ExtraInfo extraInfo) {
        Intent intent = new Intent();
        intent.setAction(AgooConstants.INTENT_FROM_AGOO_MESSAGE);
        intent.setPackage(str);
        intent.putExtras(bundle);
        intent.putExtra("type", "common-push");
        intent.putExtra(AgooConstants.MESSAGE_SOURCE, str2);
        intent.addFlags(32);
        try {
            Bundle bundle2 = new Bundle();
            bundle2.putSerializable(AgooConstants.MESSAGE_ACCS_EXTRA, extraInfo);
            intent.putExtra(AgooConstants.MESSAGE_AGOO_BUNDLE, bundle2);
        } catch (Throwable th) {
            ALog.e(TAG, "sendMsgToBussiness", th, new Object[0]);
        }
        if (ALog.isPrintLog(ALog.Level.I)) {
            ALog.i(TAG, "sendMsgToBussiness intent:" + bundle.toString() + ",utdid=" + AdapterUtilityImpl.getDeviceId(context) + ",pack=" + str + ",agooFlag=" + z, new Object[0]);
        }
        if (z) {
            UTMini.getInstance().commitEvent(AgooConstants.AGOO_EVENT_ID, DEAL_MESSAGE, AdapterUtilityImpl.getDeviceId(context), "agooMsg", "15");
            sendMsgByBindService(str, intent);
            return;
        }
        intent.setClassName(str, AdapterGlobalClientInfo.getAgooCustomServiceName(str));
        IntentDispatch.dispatchIntent(context, intent);
    }

    private void sendMsgByBindService(String str, Intent intent) {
        try {
            if (ALog.isPrintLog(ALog.Level.D)) {
                ALog.d(TAG, "onHandleMessage current tid:" + Thread.currentThread().getId(), new Object[0]);
            }
            ThreadPoolExecutorFactory.execute(new SendMessageRunnable(str, intent));
        } catch (Throwable th) {
            ALog.e(TAG, "sendMsgByBindService error >>", th, new Object[0]);
        }
    }

    class SendMessageRunnable implements Runnable {
        private Intent intent;
        private String pack;

        public SendMessageRunnable(String str, Intent intent2) {
            this.pack = str;
            this.intent = intent2;
        }

        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|(2:8|13)(1:12)) */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x0065 */
        /* JADX WARNING: Removed duplicated region for block: B:12:? A[RETURN, SYNTHETIC] */
        /* JADX WARNING: Removed duplicated region for block: B:8:0x00c9 A[Catch:{ Throwable -> 0x00d3 }] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void run() {
            /*
                r6 = this;
                r0 = 0
                java.lang.String r1 = "AgooFactory"
                java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x00d3 }
                r2.<init>()     // Catch:{ Throwable -> 0x00d3 }
                java.lang.String r3 = "running tid:"
                r2.append(r3)     // Catch:{ Throwable -> 0x00d3 }
                java.lang.Thread r3 = java.lang.Thread.currentThread()     // Catch:{ Throwable -> 0x00d3 }
                long r3 = r3.getId()     // Catch:{ Throwable -> 0x00d3 }
                r2.append(r3)     // Catch:{ Throwable -> 0x00d3 }
                java.lang.String r3 = ",pack="
                r2.append(r3)     // Catch:{ Throwable -> 0x00d3 }
                java.lang.String r3 = r6.pack     // Catch:{ Throwable -> 0x00d3 }
                r2.append(r3)     // Catch:{ Throwable -> 0x00d3 }
                java.lang.String r2 = r2.toString()     // Catch:{ Throwable -> 0x00d3 }
                java.lang.Object[] r3 = new java.lang.Object[r0]     // Catch:{ Throwable -> 0x00d3 }
                com.taobao.accs.utl.ALog.d(r1, r2, r3)     // Catch:{ Throwable -> 0x00d3 }
                android.content.Context r1 = org.android.agoo.control.AgooFactory.mContext     // Catch:{ Throwable -> 0x00d3 }
                android.content.Intent r2 = r6.intent     // Catch:{ Throwable -> 0x00d3 }
                r1.sendBroadcast(r2)     // Catch:{ Throwable -> 0x00d3 }
                java.lang.String r1 = "AgooFactory"
                java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x00d3 }
                r2.<init>()     // Catch:{ Throwable -> 0x00d3 }
                java.lang.String r3 = "SendMessageRunnable for accs,pack="
                r2.append(r3)     // Catch:{ Throwable -> 0x00d3 }
                java.lang.String r3 = r6.pack     // Catch:{ Throwable -> 0x00d3 }
                r2.append(r3)     // Catch:{ Throwable -> 0x00d3 }
                java.lang.String r2 = r2.toString()     // Catch:{ Throwable -> 0x00d3 }
                java.lang.Object[] r3 = new java.lang.Object[r0]     // Catch:{ Throwable -> 0x00d3 }
                com.taobao.accs.utl.ALog.d(r1, r2, r3)     // Catch:{ Throwable -> 0x00d3 }
                android.content.Intent r1 = r6.intent     // Catch:{ Throwable -> 0x0065 }
                java.lang.String r2 = r6.pack     // Catch:{ Throwable -> 0x0065 }
                r1.setPackage(r2)     // Catch:{ Throwable -> 0x0065 }
                android.content.Intent r1 = r6.intent     // Catch:{ Throwable -> 0x0065 }
                java.lang.String r2 = "org.agoo.android.intent.action.RECEIVE"
                r1.setAction(r2)     // Catch:{ Throwable -> 0x0065 }
                android.content.Context r1 = org.android.agoo.control.AgooFactory.mContext     // Catch:{ Throwable -> 0x0065 }
                android.content.Intent r2 = r6.intent     // Catch:{ Throwable -> 0x0065 }
                com.taobao.accs.dispatch.IntentDispatch.dispatchIntent(r1, r2)     // Catch:{ Throwable -> 0x0065 }
            L_0x0065:
                android.content.Intent r1 = new android.content.Intent     // Catch:{ Throwable -> 0x00d3 }
                java.lang.String r2 = "org.android.agoo.client.MessageReceiverService"
                r1.<init>(r2)     // Catch:{ Throwable -> 0x00d3 }
                java.lang.String r2 = r6.pack     // Catch:{ Throwable -> 0x00d3 }
                r1.setPackage(r2)     // Catch:{ Throwable -> 0x00d3 }
                java.lang.String r2 = "AgooFactory"
                java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x00d3 }
                r3.<init>()     // Catch:{ Throwable -> 0x00d3 }
                java.lang.String r4 = "this message pack:"
                r3.append(r4)     // Catch:{ Throwable -> 0x00d3 }
                java.lang.String r4 = r6.pack     // Catch:{ Throwable -> 0x00d3 }
                r3.append(r4)     // Catch:{ Throwable -> 0x00d3 }
                java.lang.String r3 = r3.toString()     // Catch:{ Throwable -> 0x00d3 }
                java.lang.Object[] r4 = new java.lang.Object[r0]     // Catch:{ Throwable -> 0x00d3 }
                com.taobao.accs.utl.ALog.d(r2, r3, r4)     // Catch:{ Throwable -> 0x00d3 }
                java.lang.String r2 = "AgooFactory"
                java.lang.String r3 = "start to service..."
                java.lang.Object[] r4 = new java.lang.Object[r0]     // Catch:{ Throwable -> 0x00d3 }
                com.taobao.accs.utl.ALog.d(r2, r3, r4)     // Catch:{ Throwable -> 0x00d3 }
                org.android.agoo.control.AgooFactory$MessageConnection r2 = new org.android.agoo.control.AgooFactory$MessageConnection     // Catch:{ Throwable -> 0x00d3 }
                org.android.agoo.control.AgooFactory r3 = org.android.agoo.control.AgooFactory.this     // Catch:{ Throwable -> 0x00d3 }
                android.content.Intent r4 = r6.intent     // Catch:{ Throwable -> 0x00d3 }
                java.lang.String r5 = "id"
                java.lang.String r4 = r4.getStringExtra(r5)     // Catch:{ Throwable -> 0x00d3 }
                android.content.Intent r5 = r6.intent     // Catch:{ Throwable -> 0x00d3 }
                r2.<init>(r4, r5)     // Catch:{ Throwable -> 0x00d3 }
                android.content.Context r3 = org.android.agoo.control.AgooFactory.mContext     // Catch:{ Throwable -> 0x00d3 }
                r4 = 17
                boolean r1 = r3.bindService(r1, r2, r4)     // Catch:{ Throwable -> 0x00d3 }
                java.lang.String r2 = "AgooFactory"
                java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x00d3 }
                r3.<init>()     // Catch:{ Throwable -> 0x00d3 }
                java.lang.String r4 = "start service ret:"
                r3.append(r4)     // Catch:{ Throwable -> 0x00d3 }
                r3.append(r1)     // Catch:{ Throwable -> 0x00d3 }
                java.lang.String r3 = r3.toString()     // Catch:{ Throwable -> 0x00d3 }
                java.lang.Object[] r4 = new java.lang.Object[r0]     // Catch:{ Throwable -> 0x00d3 }
                com.taobao.accs.utl.ALog.d(r2, r3, r4)     // Catch:{ Throwable -> 0x00d3 }
                if (r1 != 0) goto L_0x00f0
                java.lang.String r1 = "AgooFactory"
                java.lang.String r2 = "SendMessageRunnable is error"
                java.lang.Object[] r3 = new java.lang.Object[r0]     // Catch:{ Throwable -> 0x00d3 }
                com.taobao.accs.utl.ALog.d(r1, r2, r3)     // Catch:{ Throwable -> 0x00d3 }
                goto L_0x00f0
            L_0x00d3:
                r1 = move-exception
                java.lang.String r2 = "AgooFactory"
                java.lang.StringBuilder r3 = new java.lang.StringBuilder
                r3.<init>()
                java.lang.String r4 = "SendMessageRunnable is error,e="
                r3.append(r4)
                java.lang.String r1 = r1.toString()
                r3.append(r1)
                java.lang.String r1 = r3.toString()
                java.lang.Object[] r0 = new java.lang.Object[r0]
                com.taobao.accs.utl.ALog.e(r2, r1, r0)
            L_0x00f0:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: org.android.agoo.control.AgooFactory.SendMessageRunnable.run():void");
        }
    }

    class MessageConnection implements ServiceConnection {
        /* access modifiers changed from: private */
        public Intent intent;
        private String messageId;
        /* access modifiers changed from: private */
        public ServiceConnection sc = this;
        /* access modifiers changed from: private */
        public SendMessage sendMessage;

        public MessageConnection(String str, Intent intent2) {
            this.messageId = str;
            this.intent = intent2;
        }

        public void onServiceDisconnected(ComponentName componentName) {
            ALog.d(AgooFactory.TAG, "MessageConnection disConnected", new Object[0]);
        }

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            ALog.d(AgooFactory.TAG, "MessageConnection conneted:" + componentName, new Object[0]);
            this.sendMessage = SendMessage.Stub.asInterface(iBinder);
            ALog.d(AgooFactory.TAG, "onConnected current tid:" + Thread.currentThread().getId(), new Object[0]);
            ALog.d(AgooFactory.TAG, "MessageConnection sent:" + this.intent, new Object[0]);
            if (this.sendMessage != null) {
                ThreadPoolExecutorFactory.execute(new Runnable() {
                    public void run() {
                        try {
                            ALog.d(AgooFactory.TAG, "onConnected running tid:" + Thread.currentThread().getId(), new Object[0]);
                            MessageConnection.this.sendMessage.doSend(MessageConnection.this.intent);
                        } catch (RemoteException e) {
                            ALog.e(AgooFactory.TAG, "send error", e, new Object[0]);
                        } catch (Throwable th) {
                            ALog.d(AgooFactory.TAG, "send finish. close this connection", new Object[0]);
                            SendMessage unused = MessageConnection.this.sendMessage = null;
                            AgooFactory.mContext.unbindService(MessageConnection.this.sc);
                            throw th;
                        }
                        ALog.d(AgooFactory.TAG, "send finish. close this connection", new Object[0]);
                        SendMessage unused2 = MessageConnection.this.sendMessage = null;
                        AgooFactory.mContext.unbindService(MessageConnection.this.sc);
                    }
                });
            }
        }
    }
}
