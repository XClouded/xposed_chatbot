package com.taobao.accs.net;

import android.content.Context;
import android.taobao.windvane.util.WVConstants;
import android.text.TextUtils;
import androidx.core.app.NotificationCompat;
import anet.channel.AwcnConfig;
import anet.channel.DataFrameCb;
import anet.channel.IAuth;
import anet.channel.RequestCb;
import anet.channel.Session;
import anet.channel.SessionCenter;
import anet.channel.SessionInfo;
import anet.channel.bytes.ByteArray;
import anet.channel.entity.ConnType;
import anet.channel.request.Request;
import anet.channel.session.TnetSpdySession;
import anet.channel.statist.RequestStatistic;
import anet.channel.strategy.ConnProtocol;
import anet.channel.strategy.StrategyTemplate;
import com.alibaba.fastjson.JSONObject;
import com.taobao.accs.ACCSManager;
import com.taobao.accs.AccsClientConfig;
import com.taobao.accs.common.Constants;
import com.taobao.accs.common.ThreadPoolExecutorFactory;
import com.taobao.accs.data.Message;
import com.taobao.accs.ut.monitor.NetPerformanceMonitor;
import com.taobao.accs.ut.statistics.MonitorStatistic;
import com.taobao.accs.ut.statistics.ReceiveMsgStat;
import com.taobao.accs.utl.ALog;
import com.taobao.accs.utl.AppMonitorAdapter;
import com.taobao.accs.utl.BaseMonitor;
import com.taobao.accs.utl.OrangeAdapter;
import com.taobao.accs.utl.UTMini;
import com.taobao.accs.utl.UtilityImpl;
import com.taobao.tao.remotebusiness.js.MtopJSBridge;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.json.JSONException;

public class InAppConnection extends BaseConnection implements DataFrameCb {
    private static final int CONN_TIMEOUT = 60000;
    private static final int RESEND_DELAY = 2000;
    private static final String TAG = "InAppConn_";
    private ScheduledFuture accsHeartBeatFuture;
    private Runnable accsHeartBeatTask = new Runnable() {
        public void run() {
            ALog.d(InAppConnection.this.getTag(), "sendAccsHeartbeatMessage", new Object[0]);
            JSONObject jSONObject = new JSONObject();
            jSONObject.put(MtopJSBridge.MtopJSParam.DATA_TYPE, (Object) "pingreq");
            jSONObject.put("timeInterval", (Object) Long.valueOf(InAppConnection.this.accsHeartbeatInterval));
            ACCSManager.AccsRequest accsRequest = new ACCSManager.AccsRequest((String) null, (String) null, jSONObject.toJSONString().getBytes(), UUID.randomUUID().toString());
            accsRequest.setTarget("accs-iot");
            accsRequest.setTargetServiceName("sal");
            InAppConnection.this.sendMessage(Message.buildRequest(InAppConnection.this.mContext, InAppConnection.this.getHost((String) null), InAppConnection.this.getTag(), InAppConnection.this.mConfig.getStoreId(), InAppConnection.this.mContext.getPackageName(), Constants.TARGET_SERVICE, accsRequest, true), true);
        }
    };
    /* access modifiers changed from: private */
    public long accsHeartbeatInterval = 3600000;
    private boolean mRunning = true;
    private Set<String> mSessionRegistered = Collections.synchronizedSet(new HashSet());
    private Runnable mTryStartServiceRunable = new Runnable() {
        public void run() {
            try {
                if (InAppConnection.this.mContext != null && !TextUtils.isEmpty(InAppConnection.this.getAppkey())) {
                    ALog.i(InAppConnection.this.getTag(), "mTryStartServiceRunable bindapp", new Object[0]);
                    InAppConnection.this.startChannelService();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    private SmartHeartbeatImpl smartHeartbeat;

    public void close() {
    }

    public int getChannelState() {
        return 1;
    }

    public void ping(boolean z, boolean z2) {
    }

    public MonitorStatistic updateMonitorInfo() {
        return null;
    }

    public InAppConnection(Context context, int i, String str) {
        super(context, i, str);
        if (!OrangeAdapter.isTnetLogOff(true)) {
            String tnetLogFilePath = UtilityImpl.getTnetLogFilePath(this.mContext, "inapp");
            String tag = getTag();
            ALog.d(tag, "config tnet log path:" + tnetLogFilePath, new Object[0]);
            if (!TextUtils.isEmpty(tnetLogFilePath)) {
                Session.configTnetALog(context, tnetLogFilePath, 5242880, 5);
            }
        }
        ThreadPoolExecutorFactory.getScheduledExecutor().schedule(this.mTryStartServiceRunable, 120000, TimeUnit.MILLISECONDS);
    }

    public synchronized void start() {
        ALog.d(getTag(), "start", new Object[0]);
        this.mRunning = true;
        initAwcn(this.mContext);
    }

    public void sendMessage(final Message message, boolean z) {
        if (!this.mRunning || message == null) {
            String tag = getTag();
            ALog.e(tag, "not running or msg null! " + this.mRunning, new Object[0]);
            return;
        }
        try {
            if (ThreadPoolExecutorFactory.getSendScheduledExecutor().getQueue().size() <= 1000) {
                ScheduledFuture<?> schedule = ThreadPoolExecutorFactory.getSendScheduledExecutor().schedule(new Runnable() {
                    /* JADX WARNING: Removed duplicated region for block: B:50:0x0213  */
                    /* JADX WARNING: Removed duplicated region for block: B:65:0x0263  */
                    /* JADX WARNING: Removed duplicated region for block: B:66:0x0289  */
                    /* Code decompiled incorrectly, please refer to instructions dump. */
                    public void run() {
                        /*
                            r17 = this;
                            r1 = r17
                            com.taobao.accs.data.Message r0 = r6
                            if (r0 == 0) goto L_0x03aa
                            com.taobao.accs.data.Message r0 = r6
                            com.taobao.accs.ut.monitor.NetPerformanceMonitor r0 = r0.getNetPermanceMonitor()
                            if (r0 == 0) goto L_0x0017
                            com.taobao.accs.data.Message r0 = r6
                            com.taobao.accs.ut.monitor.NetPerformanceMonitor r0 = r0.getNetPermanceMonitor()
                            r0.onTakeFromQueue()
                        L_0x0017:
                            com.taobao.accs.data.Message r0 = r6
                            int r0 = r0.getType()
                            r2 = 3
                            r3 = 4
                            r4 = 2
                            r5 = 0
                            r6 = 1
                            com.taobao.accs.utl.ALog$Level r7 = com.taobao.accs.utl.ALog.Level.D     // Catch:{ Throwable -> 0x02ba }
                            boolean r7 = com.taobao.accs.utl.ALog.isPrintLog(r7)     // Catch:{ Throwable -> 0x02ba }
                            if (r7 == 0) goto L_0x004b
                            com.taobao.accs.net.InAppConnection r7 = com.taobao.accs.net.InAppConnection.this     // Catch:{ Throwable -> 0x02ba }
                            java.lang.String r7 = r7.getTag()     // Catch:{ Throwable -> 0x02ba }
                            java.lang.String r8 = "sendMessage start"
                            java.lang.Object[] r9 = new java.lang.Object[r3]     // Catch:{ Throwable -> 0x02ba }
                            java.lang.String r10 = "dataId"
                            r9[r5] = r10     // Catch:{ Throwable -> 0x02ba }
                            com.taobao.accs.data.Message r10 = r6     // Catch:{ Throwable -> 0x02ba }
                            java.lang.String r10 = r10.dataId     // Catch:{ Throwable -> 0x02ba }
                            r9[r6] = r10     // Catch:{ Throwable -> 0x02ba }
                            java.lang.String r10 = "type"
                            r9[r4] = r10     // Catch:{ Throwable -> 0x02ba }
                            java.lang.String r10 = com.taobao.accs.data.Message.MsgType.name(r0)     // Catch:{ Throwable -> 0x02ba }
                            r9[r2] = r10     // Catch:{ Throwable -> 0x02ba }
                            com.taobao.accs.utl.ALog.d(r7, r8, r9)     // Catch:{ Throwable -> 0x02ba }
                        L_0x004b:
                            if (r0 != r6) goto L_0x01f7
                            com.taobao.accs.data.Message r7 = r6     // Catch:{ Throwable -> 0x02ba }
                            java.net.URL r7 = r7.host     // Catch:{ Throwable -> 0x02ba }
                            if (r7 != 0) goto L_0x0060
                            com.taobao.accs.net.InAppConnection r7 = com.taobao.accs.net.InAppConnection.this     // Catch:{ Throwable -> 0x02ba }
                            com.taobao.accs.data.MessageHandler r7 = r7.mMessageHandler     // Catch:{ Throwable -> 0x02ba }
                            com.taobao.accs.data.Message r8 = r6     // Catch:{ Throwable -> 0x02ba }
                            r9 = -5
                            r7.onResult(r8, r9)     // Catch:{ Throwable -> 0x02ba }
                        L_0x005d:
                            r5 = 1
                            goto L_0x0211
                        L_0x0060:
                            com.taobao.accs.net.InAppConnection r7 = com.taobao.accs.net.InAppConnection.this     // Catch:{ Throwable -> 0x02ba }
                            com.taobao.accs.AccsClientConfig r7 = r7.mConfig     // Catch:{ Throwable -> 0x02ba }
                            java.lang.String r7 = r7.getAppKey()     // Catch:{ Throwable -> 0x02ba }
                            anet.channel.SessionCenter r7 = anet.channel.SessionCenter.getInstance((java.lang.String) r7)     // Catch:{ Throwable -> 0x02ba }
                            com.taobao.accs.net.InAppConnection r8 = com.taobao.accs.net.InAppConnection.this     // Catch:{ Throwable -> 0x02ba }
                            com.taobao.accs.data.Message r9 = r6     // Catch:{ Throwable -> 0x02ba }
                            java.net.URL r9 = r9.host     // Catch:{ Throwable -> 0x02ba }
                            java.lang.String r9 = r9.getHost()     // Catch:{ Throwable -> 0x02ba }
                            r8.registerSessionInfo(r7, r9, r5)     // Catch:{ Throwable -> 0x02ba }
                            com.taobao.accs.data.Message r8 = r6     // Catch:{ Throwable -> 0x02ba }
                            java.net.URL r8 = r8.host     // Catch:{ Throwable -> 0x02ba }
                            java.lang.String r8 = r8.toString()     // Catch:{ Throwable -> 0x02ba }
                            anet.channel.entity.ConnType$TypeLevel r9 = anet.channel.entity.ConnType.TypeLevel.SPDY     // Catch:{ Throwable -> 0x02ba }
                            r10 = 60000(0xea60, double:2.9644E-319)
                            anet.channel.Session r7 = r7.get((java.lang.String) r8, (anet.channel.entity.ConnType.TypeLevel) r9, (long) r10)     // Catch:{ Throwable -> 0x02ba }
                            if (r7 == 0) goto L_0x01f5
                            com.taobao.accs.data.Message r8 = r6     // Catch:{ Throwable -> 0x02ba }
                            com.taobao.accs.net.InAppConnection r9 = com.taobao.accs.net.InAppConnection.this     // Catch:{ Throwable -> 0x02ba }
                            android.content.Context r9 = r9.mContext     // Catch:{ Throwable -> 0x02ba }
                            com.taobao.accs.net.InAppConnection r10 = com.taobao.accs.net.InAppConnection.this     // Catch:{ Throwable -> 0x02ba }
                            int r10 = r10.mConnectionType     // Catch:{ Throwable -> 0x02ba }
                            byte[] r8 = r8.build(r9, r10)     // Catch:{ Throwable -> 0x02ba }
                            java.lang.String r9 = "accs"
                            com.taobao.accs.data.Message r10 = r6     // Catch:{ Throwable -> 0x02ba }
                            java.lang.String r10 = r10.serviceId     // Catch:{ Throwable -> 0x02ba }
                            boolean r9 = r9.equals(r10)     // Catch:{ Throwable -> 0x02ba }
                            r11 = 8
                            r12 = 7
                            r13 = 6
                            r14 = 5
                            r15 = 10
                            if (r9 == 0) goto L_0x00f6
                            com.taobao.accs.net.InAppConnection r9 = com.taobao.accs.net.InAppConnection.this     // Catch:{ Throwable -> 0x02ba }
                            java.lang.String r9 = r9.getTag()     // Catch:{ Throwable -> 0x02ba }
                            java.lang.String r10 = "sendMessage"
                            java.lang.Object[] r15 = new java.lang.Object[r15]     // Catch:{ Throwable -> 0x02ba }
                            java.lang.String r16 = "dataId"
                            r15[r5] = r16     // Catch:{ Throwable -> 0x02ba }
                            com.taobao.accs.data.Message r5 = r6     // Catch:{ Throwable -> 0x02ba }
                            java.lang.String r5 = r5.getDataId()     // Catch:{ Throwable -> 0x02ba }
                            r15[r6] = r5     // Catch:{ Throwable -> 0x02ba }
                            java.lang.String r5 = "command"
                            r15[r4] = r5     // Catch:{ Throwable -> 0x02ba }
                            com.taobao.accs.data.Message r5 = r6     // Catch:{ Throwable -> 0x02ba }
                            java.lang.Integer r5 = r5.command     // Catch:{ Throwable -> 0x02ba }
                            r15[r2] = r5     // Catch:{ Throwable -> 0x02ba }
                            java.lang.String r5 = "host"
                            r15[r3] = r5     // Catch:{ Throwable -> 0x02ba }
                            com.taobao.accs.data.Message r5 = r6     // Catch:{ Throwable -> 0x02ba }
                            java.net.URL r5 = r5.host     // Catch:{ Throwable -> 0x02ba }
                            r15[r14] = r5     // Catch:{ Throwable -> 0x02ba }
                            java.lang.String r5 = "len"
                            r15[r13] = r5     // Catch:{ Throwable -> 0x02ba }
                            if (r8 != 0) goto L_0x00df
                            r5 = 0
                            goto L_0x00e0
                        L_0x00df:
                            int r5 = r8.length     // Catch:{ Throwable -> 0x02ba }
                        L_0x00e0:
                            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)     // Catch:{ Throwable -> 0x02ba }
                            r15[r12] = r5     // Catch:{ Throwable -> 0x02ba }
                            java.lang.String r5 = "utdid"
                            r15[r11] = r5     // Catch:{ Throwable -> 0x02ba }
                            com.taobao.accs.net.InAppConnection r5 = com.taobao.accs.net.InAppConnection.this     // Catch:{ Throwable -> 0x02ba }
                            java.lang.String r5 = r5.mUtdid     // Catch:{ Throwable -> 0x02ba }
                            r11 = 9
                            r15[r11] = r5     // Catch:{ Throwable -> 0x02ba }
                            com.taobao.accs.utl.ALog.e(r9, r10, r15)     // Catch:{ Throwable -> 0x02ba }
                            goto L_0x0148
                        L_0x00f6:
                            com.taobao.accs.utl.ALog$Level r5 = com.taobao.accs.utl.ALog.Level.I     // Catch:{ Throwable -> 0x02ba }
                            boolean r5 = com.taobao.accs.utl.ALog.isPrintLog(r5)     // Catch:{ Throwable -> 0x02ba }
                            if (r5 == 0) goto L_0x0148
                            com.taobao.accs.net.InAppConnection r5 = com.taobao.accs.net.InAppConnection.this     // Catch:{ Throwable -> 0x02ba }
                            java.lang.String r5 = r5.getTag()     // Catch:{ Throwable -> 0x02ba }
                            java.lang.String r9 = "sendMessage"
                            java.lang.Object[] r10 = new java.lang.Object[r15]     // Catch:{ Throwable -> 0x02ba }
                            java.lang.String r15 = "dataId"
                            r16 = 0
                            r10[r16] = r15     // Catch:{ Throwable -> 0x02ba }
                            com.taobao.accs.data.Message r15 = r6     // Catch:{ Throwable -> 0x02ba }
                            java.lang.String r15 = r15.getDataId()     // Catch:{ Throwable -> 0x02ba }
                            r10[r6] = r15     // Catch:{ Throwable -> 0x02ba }
                            java.lang.String r15 = "command"
                            r10[r4] = r15     // Catch:{ Throwable -> 0x02ba }
                            com.taobao.accs.data.Message r15 = r6     // Catch:{ Throwable -> 0x02ba }
                            java.lang.Integer r15 = r15.command     // Catch:{ Throwable -> 0x02ba }
                            r10[r2] = r15     // Catch:{ Throwable -> 0x02ba }
                            java.lang.String r15 = "host"
                            r10[r3] = r15     // Catch:{ Throwable -> 0x02ba }
                            com.taobao.accs.data.Message r15 = r6     // Catch:{ Throwable -> 0x02ba }
                            java.net.URL r15 = r15.host     // Catch:{ Throwable -> 0x02ba }
                            r10[r14] = r15     // Catch:{ Throwable -> 0x02ba }
                            java.lang.String r14 = "len"
                            r10[r13] = r14     // Catch:{ Throwable -> 0x02ba }
                            if (r8 != 0) goto L_0x0132
                            r13 = 0
                            goto L_0x0133
                        L_0x0132:
                            int r13 = r8.length     // Catch:{ Throwable -> 0x02ba }
                        L_0x0133:
                            java.lang.Integer r13 = java.lang.Integer.valueOf(r13)     // Catch:{ Throwable -> 0x02ba }
                            r10[r12] = r13     // Catch:{ Throwable -> 0x02ba }
                            java.lang.String r12 = "utdid"
                            r10[r11] = r12     // Catch:{ Throwable -> 0x02ba }
                            com.taobao.accs.net.InAppConnection r11 = com.taobao.accs.net.InAppConnection.this     // Catch:{ Throwable -> 0x02ba }
                            java.lang.String r11 = r11.mUtdid     // Catch:{ Throwable -> 0x02ba }
                            r12 = 9
                            r10[r12] = r11     // Catch:{ Throwable -> 0x02ba }
                            com.taobao.accs.utl.ALog.d(r5, r9, r10)     // Catch:{ Throwable -> 0x02ba }
                        L_0x0148:
                            com.taobao.accs.data.Message r5 = r6     // Catch:{ Throwable -> 0x02ba }
                            long r9 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x02ba }
                            r5.setSendTime(r9)     // Catch:{ Throwable -> 0x02ba }
                            int r5 = r8.length     // Catch:{ Throwable -> 0x02ba }
                            r9 = 16384(0x4000, float:2.2959E-41)
                            if (r5 <= r9) goto L_0x016e
                            com.taobao.accs.data.Message r5 = r6     // Catch:{ Throwable -> 0x02ba }
                            java.lang.Integer r5 = r5.command     // Catch:{ Throwable -> 0x02ba }
                            int r5 = r5.intValue()     // Catch:{ Throwable -> 0x02ba }
                            r9 = 102(0x66, float:1.43E-43)
                            if (r5 == r9) goto L_0x016e
                            com.taobao.accs.net.InAppConnection r5 = com.taobao.accs.net.InAppConnection.this     // Catch:{ Throwable -> 0x02ba }
                            com.taobao.accs.data.MessageHandler r5 = r5.mMessageHandler     // Catch:{ Throwable -> 0x02ba }
                            com.taobao.accs.data.Message r7 = r6     // Catch:{ Throwable -> 0x02ba }
                            r8 = -4
                            r5.onResult(r7, r8)     // Catch:{ Throwable -> 0x02ba }
                            goto L_0x005d
                        L_0x016e:
                            com.taobao.accs.net.InAppConnection r5 = com.taobao.accs.net.InAppConnection.this     // Catch:{ Throwable -> 0x02ba }
                            com.taobao.accs.data.MessageHandler r5 = r5.mMessageHandler     // Catch:{ Throwable -> 0x02ba }
                            com.taobao.accs.data.Message r9 = r6     // Catch:{ Throwable -> 0x02ba }
                            r5.onSend(r9)     // Catch:{ Throwable -> 0x02ba }
                            com.taobao.accs.data.Message r5 = r6     // Catch:{ Throwable -> 0x02ba }
                            boolean r5 = r5.isAck     // Catch:{ Throwable -> 0x02ba }
                            if (r5 == 0) goto L_0x0189
                            com.taobao.accs.data.Message r5 = r6     // Catch:{ Throwable -> 0x02ba }
                            com.taobao.accs.data.Message$Id r5 = r5.getMsgId()     // Catch:{ Throwable -> 0x02ba }
                            int r5 = r5.getId()     // Catch:{ Throwable -> 0x02ba }
                            int r5 = -r5
                            goto L_0x0193
                        L_0x0189:
                            com.taobao.accs.data.Message r5 = r6     // Catch:{ Throwable -> 0x02ba }
                            com.taobao.accs.data.Message$Id r5 = r5.getMsgId()     // Catch:{ Throwable -> 0x02ba }
                            int r5 = r5.getId()     // Catch:{ Throwable -> 0x02ba }
                        L_0x0193:
                            com.taobao.accs.data.Message r9 = r6     // Catch:{ Throwable -> 0x02ba }
                            boolean r9 = r9.isAck     // Catch:{ Throwable -> 0x02ba }
                            if (r9 == 0) goto L_0x01a6
                            com.taobao.accs.net.InAppConnection r9 = com.taobao.accs.net.InAppConnection.this     // Catch:{ Throwable -> 0x02ba }
                            java.util.LinkedHashMap r9 = r9.mAckMessage     // Catch:{ Throwable -> 0x02ba }
                            java.lang.Integer r10 = java.lang.Integer.valueOf(r5)     // Catch:{ Throwable -> 0x02ba }
                            com.taobao.accs.data.Message r11 = r6     // Catch:{ Throwable -> 0x02ba }
                            r9.put(r10, r11)     // Catch:{ Throwable -> 0x02ba }
                        L_0x01a6:
                            r9 = 200(0xc8, float:2.8E-43)
                            r7.sendCustomFrame(r5, r8, r9)     // Catch:{ Throwable -> 0x02ba }
                            com.taobao.accs.data.Message r5 = r6     // Catch:{ Throwable -> 0x02ba }
                            com.taobao.accs.ut.monitor.NetPerformanceMonitor r5 = r5.getNetPermanceMonitor()     // Catch:{ Throwable -> 0x02ba }
                            if (r5 == 0) goto L_0x01bc
                            com.taobao.accs.data.Message r5 = r6     // Catch:{ Throwable -> 0x02ba }
                            com.taobao.accs.ut.monitor.NetPerformanceMonitor r5 = r5.getNetPermanceMonitor()     // Catch:{ Throwable -> 0x02ba }
                            r5.onSendData()     // Catch:{ Throwable -> 0x02ba }
                        L_0x01bc:
                            com.taobao.accs.net.InAppConnection r5 = com.taobao.accs.net.InAppConnection.this     // Catch:{ Throwable -> 0x02ba }
                            com.taobao.accs.data.Message r7 = r6     // Catch:{ Throwable -> 0x02ba }
                            java.lang.String r7 = r7.getDataId()     // Catch:{ Throwable -> 0x02ba }
                            com.taobao.accs.net.InAppConnection r9 = com.taobao.accs.net.InAppConnection.this     // Catch:{ Throwable -> 0x02ba }
                            com.taobao.accs.AccsClientConfig r9 = r9.mConfig     // Catch:{ Throwable -> 0x02ba }
                            boolean r9 = r9.isQuickReconnect()     // Catch:{ Throwable -> 0x02ba }
                            com.taobao.accs.data.Message r10 = r6     // Catch:{ Throwable -> 0x02ba }
                            int r10 = r10.timeout     // Catch:{ Throwable -> 0x02ba }
                            long r10 = (long) r10     // Catch:{ Throwable -> 0x02ba }
                            r5.setTimeOut(r7, r9, r10)     // Catch:{ Throwable -> 0x02ba }
                            com.taobao.accs.net.InAppConnection r5 = com.taobao.accs.net.InAppConnection.this     // Catch:{ Throwable -> 0x02ba }
                            com.taobao.accs.data.MessageHandler r5 = r5.mMessageHandler     // Catch:{ Throwable -> 0x02ba }
                            com.taobao.accs.ut.monitor.TrafficsMonitor$TrafficInfo r7 = new com.taobao.accs.ut.monitor.TrafficsMonitor$TrafficInfo     // Catch:{ Throwable -> 0x02ba }
                            com.taobao.accs.data.Message r9 = r6     // Catch:{ Throwable -> 0x02ba }
                            java.lang.String r10 = r9.serviceId     // Catch:{ Throwable -> 0x02ba }
                            boolean r11 = anet.channel.GlobalAppRuntimeInfo.isAppBackground()     // Catch:{ Throwable -> 0x02ba }
                            com.taobao.accs.data.Message r9 = r6     // Catch:{ Throwable -> 0x02ba }
                            java.net.URL r9 = r9.host     // Catch:{ Throwable -> 0x02ba }
                            java.lang.String r12 = r9.toString()     // Catch:{ Throwable -> 0x02ba }
                            int r8 = r8.length     // Catch:{ Throwable -> 0x02ba }
                            long r13 = (long) r8     // Catch:{ Throwable -> 0x02ba }
                            r9 = r7
                            r9.<init>(r10, r11, r12, r13)     // Catch:{ Throwable -> 0x02ba }
                            r5.addTrafficsInfo(r7)     // Catch:{ Throwable -> 0x02ba }
                            goto L_0x005d
                        L_0x01f5:
                            r5 = 0
                            goto L_0x0211
                        L_0x01f7:
                            com.taobao.accs.net.InAppConnection r5 = com.taobao.accs.net.InAppConnection.this     // Catch:{ Throwable -> 0x02ba }
                            java.lang.String r5 = r5.getTag()     // Catch:{ Throwable -> 0x02ba }
                            java.lang.String r7 = "sendMessage skip"
                            java.lang.Object[] r8 = new java.lang.Object[r4]     // Catch:{ Throwable -> 0x02ba }
                            java.lang.String r9 = "type"
                            r10 = 0
                            r8[r10] = r9     // Catch:{ Throwable -> 0x02ba }
                            java.lang.String r9 = com.taobao.accs.data.Message.MsgType.name(r0)     // Catch:{ Throwable -> 0x02ba }
                            r8[r6] = r9     // Catch:{ Throwable -> 0x02ba }
                            com.taobao.accs.utl.ALog.e(r5, r7, r8)     // Catch:{ Throwable -> 0x02ba }
                            goto L_0x005d
                        L_0x0211:
                            if (r5 != 0) goto L_0x0257
                            r7 = -11
                            if (r0 != r6) goto L_0x024e
                            com.taobao.accs.data.Message r0 = r6
                            boolean r0 = r0.isTimeOut()
                            if (r0 != 0) goto L_0x022b
                            com.taobao.accs.net.InAppConnection r0 = com.taobao.accs.net.InAppConnection.this
                            com.taobao.accs.data.Message r8 = r6
                            r9 = 2000(0x7d0, float:2.803E-42)
                            boolean r0 = r0.reSend(r8, r9)
                            if (r0 != 0) goto L_0x0234
                        L_0x022b:
                            com.taobao.accs.net.InAppConnection r0 = com.taobao.accs.net.InAppConnection.this
                            com.taobao.accs.data.MessageHandler r0 = r0.mMessageHandler
                            com.taobao.accs.data.Message r8 = r6
                            r0.onResult(r8, r7)
                        L_0x0234:
                            com.taobao.accs.data.Message r0 = r6
                            int r0 = r0.retryTimes
                            if (r0 != r6) goto L_0x0257
                            com.taobao.accs.data.Message r0 = r6
                            com.taobao.accs.ut.monitor.NetPerformanceMonitor r0 = r0.getNetPermanceMonitor()
                            if (r0 == 0) goto L_0x0257
                            java.lang.String r0 = "accs"
                            java.lang.String r7 = "resend"
                            java.lang.String r8 = "total_accs"
                            r9 = 0
                            com.taobao.accs.utl.AppMonitorAdapter.commitCount(r0, r7, r8, r9)
                            goto L_0x0257
                        L_0x024e:
                            com.taobao.accs.net.InAppConnection r0 = com.taobao.accs.net.InAppConnection.this
                            com.taobao.accs.data.MessageHandler r0 = r0.mMessageHandler
                            com.taobao.accs.data.Message r8 = r6
                            r0.onResult(r8, r7)
                        L_0x0257:
                            java.lang.String r0 = "accs"
                            com.taobao.accs.data.Message r7 = r6
                            java.lang.String r7 = r7.serviceId
                            boolean r0 = r0.equals(r7)
                            if (r0 == 0) goto L_0x0289
                            com.taobao.accs.net.InAppConnection r0 = com.taobao.accs.net.InAppConnection.this
                            java.lang.String r0 = r0.getTag()
                            java.lang.String r7 = "sendMessage end"
                            java.lang.Object[] r3 = new java.lang.Object[r3]
                            java.lang.String r8 = "dataId"
                            r9 = 0
                            r3[r9] = r8
                            com.taobao.accs.data.Message r8 = r6
                            java.lang.String r8 = r8.getDataId()
                            r3[r6] = r8
                            java.lang.String r6 = "status"
                            r3[r4] = r6
                            java.lang.Boolean r4 = java.lang.Boolean.valueOf(r5)
                            r3[r2] = r4
                            com.taobao.accs.utl.ALog.e(r0, r7, r3)
                            goto L_0x03aa
                        L_0x0289:
                            com.taobao.accs.utl.ALog$Level r0 = com.taobao.accs.utl.ALog.Level.D
                            boolean r0 = com.taobao.accs.utl.ALog.isPrintLog(r0)
                            if (r0 == 0) goto L_0x03aa
                            com.taobao.accs.net.InAppConnection r0 = com.taobao.accs.net.InAppConnection.this
                            java.lang.String r0 = r0.getTag()
                            java.lang.String r7 = "sendMessage end"
                            java.lang.Object[] r3 = new java.lang.Object[r3]
                            java.lang.String r8 = "dataId"
                            r9 = 0
                            r3[r9] = r8
                            com.taobao.accs.data.Message r8 = r6
                            java.lang.String r8 = r8.getDataId()
                            r3[r6] = r8
                            java.lang.String r6 = "status"
                            r3[r4] = r6
                            java.lang.Boolean r4 = java.lang.Boolean.valueOf(r5)
                            r3[r2] = r4
                            com.taobao.accs.utl.ALog.d(r0, r7, r3)
                            goto L_0x03aa
                        L_0x02b7:
                            r0 = move-exception
                            goto L_0x034c
                        L_0x02ba:
                            r0 = move-exception
                            java.lang.String r5 = "accs"
                            java.lang.String r7 = "send_fail"
                            com.taobao.accs.data.Message r8 = r6     // Catch:{ all -> 0x02b7 }
                            java.lang.String r8 = r8.serviceId     // Catch:{ all -> 0x02b7 }
                            java.lang.String r9 = ""
                            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ all -> 0x02b7 }
                            r10.<init>()     // Catch:{ all -> 0x02b7 }
                            com.taobao.accs.net.InAppConnection r11 = com.taobao.accs.net.InAppConnection.this     // Catch:{ all -> 0x02b7 }
                            int r11 = r11.mConnectionType     // Catch:{ all -> 0x02b7 }
                            r10.append(r11)     // Catch:{ all -> 0x02b7 }
                            java.lang.String r11 = r0.toString()     // Catch:{ all -> 0x02b7 }
                            r10.append(r11)     // Catch:{ all -> 0x02b7 }
                            java.lang.String r10 = r10.toString()     // Catch:{ all -> 0x02b7 }
                            com.taobao.accs.utl.AppMonitorAdapter.commitAlarmFail(r5, r7, r8, r9, r10)     // Catch:{ all -> 0x02b7 }
                            com.taobao.accs.net.InAppConnection r5 = com.taobao.accs.net.InAppConnection.this     // Catch:{ all -> 0x02b7 }
                            java.lang.String r5 = r5.getTag()     // Catch:{ all -> 0x02b7 }
                            java.lang.String r7 = "sendMessage"
                            r8 = 0
                            java.lang.Object[] r9 = new java.lang.Object[r8]     // Catch:{ all -> 0x02b7 }
                            com.taobao.accs.utl.ALog.e(r5, r7, r0, r9)     // Catch:{ all -> 0x02b7 }
                            java.lang.String r0 = "accs"
                            com.taobao.accs.data.Message r5 = r6
                            java.lang.String r5 = r5.serviceId
                            boolean r0 = r0.equals(r5)
                            if (r0 == 0) goto L_0x031f
                            com.taobao.accs.net.InAppConnection r0 = com.taobao.accs.net.InAppConnection.this
                            java.lang.String r0 = r0.getTag()
                            java.lang.String r5 = "sendMessage end"
                            java.lang.Object[] r3 = new java.lang.Object[r3]
                            java.lang.String r7 = "dataId"
                            r8 = 0
                            r3[r8] = r7
                            com.taobao.accs.data.Message r7 = r6
                            java.lang.String r7 = r7.getDataId()
                            r3[r6] = r7
                            java.lang.String r7 = "status"
                            r3[r4] = r7
                            java.lang.Boolean r4 = java.lang.Boolean.valueOf(r6)
                            r3[r2] = r4
                            com.taobao.accs.utl.ALog.e(r0, r5, r3)
                            goto L_0x03aa
                        L_0x031f:
                            com.taobao.accs.utl.ALog$Level r0 = com.taobao.accs.utl.ALog.Level.D
                            boolean r0 = com.taobao.accs.utl.ALog.isPrintLog(r0)
                            if (r0 == 0) goto L_0x03aa
                            com.taobao.accs.net.InAppConnection r0 = com.taobao.accs.net.InAppConnection.this
                            java.lang.String r0 = r0.getTag()
                            java.lang.String r5 = "sendMessage end"
                            java.lang.Object[] r3 = new java.lang.Object[r3]
                            java.lang.String r7 = "dataId"
                            r8 = 0
                            r3[r8] = r7
                            com.taobao.accs.data.Message r7 = r6
                            java.lang.String r7 = r7.getDataId()
                            r3[r6] = r7
                            java.lang.String r7 = "status"
                            r3[r4] = r7
                            java.lang.Boolean r4 = java.lang.Boolean.valueOf(r6)
                            r3[r2] = r4
                            com.taobao.accs.utl.ALog.d(r0, r5, r3)
                            goto L_0x03aa
                        L_0x034c:
                            java.lang.String r5 = "accs"
                            com.taobao.accs.data.Message r7 = r6
                            java.lang.String r7 = r7.serviceId
                            boolean r5 = r5.equals(r7)
                            if (r5 != 0) goto L_0x0385
                            com.taobao.accs.utl.ALog$Level r5 = com.taobao.accs.utl.ALog.Level.D
                            boolean r5 = com.taobao.accs.utl.ALog.isPrintLog(r5)
                            if (r5 == 0) goto L_0x03a9
                            com.taobao.accs.net.InAppConnection r5 = com.taobao.accs.net.InAppConnection.this
                            java.lang.String r5 = r5.getTag()
                            java.lang.Object[] r3 = new java.lang.Object[r3]
                            java.lang.String r7 = "dataId"
                            r8 = 0
                            r3[r8] = r7
                            com.taobao.accs.data.Message r7 = r6
                            java.lang.String r7 = r7.getDataId()
                            r3[r6] = r7
                            java.lang.String r7 = "status"
                            r3[r4] = r7
                            java.lang.Boolean r4 = java.lang.Boolean.valueOf(r6)
                            r3[r2] = r4
                            java.lang.String r2 = "sendMessage end"
                            com.taobao.accs.utl.ALog.d(r5, r2, r3)
                            goto L_0x03a9
                        L_0x0385:
                            com.taobao.accs.net.InAppConnection r5 = com.taobao.accs.net.InAppConnection.this
                            java.lang.String r5 = r5.getTag()
                            java.lang.Object[] r3 = new java.lang.Object[r3]
                            java.lang.String r7 = "dataId"
                            r8 = 0
                            r3[r8] = r7
                            com.taobao.accs.data.Message r7 = r6
                            java.lang.String r7 = r7.getDataId()
                            r3[r6] = r7
                            java.lang.String r7 = "status"
                            r3[r4] = r7
                            java.lang.Boolean r4 = java.lang.Boolean.valueOf(r6)
                            r3[r2] = r4
                            java.lang.String r2 = "sendMessage end"
                            com.taobao.accs.utl.ALog.e(r5, r2, r3)
                        L_0x03a9:
                            throw r0
                        L_0x03aa:
                            return
                        */
                        throw new UnsupportedOperationException("Method not decompiled: com.taobao.accs.net.InAppConnection.AnonymousClass2.run():void");
                    }
                }, message.delyTime, TimeUnit.MILLISECONDS);
                if (message.getType() == 1 && message.cunstomDataId != null) {
                    if (message.isControlFrame() && cancel(message.cunstomDataId)) {
                        this.mMessageHandler.cancelControlMessage(message);
                    }
                    this.mMessageHandler.reqTasks.put(message.cunstomDataId, schedule);
                }
                NetPerformanceMonitor netPermanceMonitor = message.getNetPermanceMonitor();
                if (netPermanceMonitor != null) {
                    netPermanceMonitor.setDeviceId(UtilityImpl.getDeviceId(this.mContext));
                    netPermanceMonitor.setConnType(this.mConnectionType);
                    netPermanceMonitor.onEnterQueueData();
                    return;
                }
                return;
            }
            throw new RejectedExecutionException("accs");
        } catch (RejectedExecutionException unused) {
            this.mMessageHandler.onResult(message, 70008);
            String tag2 = getTag();
            ALog.e(tag2, "send queue full count:" + ThreadPoolExecutorFactory.getSendScheduledExecutor().getQueue().size(), new Object[0]);
        } catch (Throwable th) {
            this.mMessageHandler.onResult(message, -8);
            ALog.e(getTag(), "send error", th, new Object[0]);
        }
    }

    /* access modifiers changed from: protected */
    public void setTimeOut(final String str, final boolean z, long j) {
        ThreadPoolExecutorFactory.getScheduledExecutor().schedule(new Runnable() {
            public void run() {
                Message unhandledMessage = InAppConnection.this.mMessageHandler.getUnhandledMessage(str);
                if (unhandledMessage != null) {
                    InAppConnection.this.mMessageHandler.onResult(unhandledMessage, -9);
                    InAppConnection.this.onTimeOut(str, z, "receive data time out");
                    String tag = InAppConnection.this.getTag();
                    ALog.e(tag, str + "-> receive data time out!", new Object[0]);
                }
            }
        }, j, TimeUnit.MILLISECONDS);
    }

    public void shutdown() {
        ALog.e(getTag(), "shut down", new Object[0]);
        this.mRunning = false;
    }

    public boolean isAlive() {
        return this.mRunning;
    }

    public void notifyNetWorkChange(String str) {
        this.mTimeoutMsgNum = 0;
    }

    public void onTimeOut(String str, boolean z, String str2) {
        Session session;
        try {
            Message removeUnhandledMessage = this.mMessageHandler.removeUnhandledMessage(str);
            if (removeUnhandledMessage != null && removeUnhandledMessage.host != null && (session = SessionCenter.getInstance(this.mConfig.getAppKey()).get(removeUnhandledMessage.host.toString(), 0)) != null) {
                if (z) {
                    session.close(true);
                } else {
                    session.ping(true);
                }
            }
        } catch (Exception e) {
            ALog.e(getTag(), "onTimeOut", e, new Object[0]);
        }
    }

    public void onDataReceive(final TnetSpdySession tnetSpdySession, final byte[] bArr, int i, final int i2) {
        ThreadPoolExecutorFactory.getScheduledExecutor().execute(new Runnable() {
            public void run() {
                if (ALog.isPrintLog(ALog.Level.I)) {
                    ALog.i(InAppConnection.this.getTag(), "onDataReceive", "type", Integer.valueOf(i2));
                }
                if (i2 == 200) {
                    try {
                        long currentTimeMillis = System.currentTimeMillis();
                        InAppConnection.this.mMessageHandler.onMessage(bArr, tnetSpdySession.getHost());
                        ReceiveMsgStat receiveMsgStat = InAppConnection.this.mMessageHandler.getReceiveMsgStat();
                        if (receiveMsgStat != null) {
                            receiveMsgStat.receiveDate = String.valueOf(currentTimeMillis);
                            receiveMsgStat.messageType = InAppConnection.this.mConnectionType == 0 ? NotificationCompat.CATEGORY_SERVICE : "inapp";
                            receiveMsgStat.commitUT();
                        }
                    } catch (Throwable th) {
                        ALog.e(InAppConnection.this.getTag(), "onDataReceive ", th, new Object[0]);
                        UTMini.getInstance().commitEvent(66001, "DATA_RECEIVE", UtilityImpl.getStackMsg(th));
                    }
                } else {
                    String tag = InAppConnection.this.getTag();
                    ALog.e(tag, "drop frame len:" + bArr.length + " frameType" + i2, new Object[0]);
                }
            }
        });
    }

    public void onException(final int i, final int i2, final boolean z, String str) {
        String tag = getTag();
        ALog.e(tag, "errorId:" + i2 + "detail:" + str + " dataId:" + i + " needRetry:" + z, new Object[0]);
        ThreadPoolExecutorFactory.getScheduledExecutor().execute(new Runnable() {
            public void run() {
                Message removeUnhandledMessage;
                if (i > 0) {
                    Message.Id id = new Message.Id(i, "");
                    Message.Id id2 = null;
                    Iterator<Message.Id> it = InAppConnection.this.mMessageHandler.getUnhandledMessageIds().iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        Message.Id next = it.next();
                        if (next.equals(id)) {
                            id2 = next;
                            break;
                        }
                    }
                    if (!(id2 == null || (removeUnhandledMessage = InAppConnection.this.mMessageHandler.removeUnhandledMessage(id2.getDataId())) == null)) {
                        if (z) {
                            if (!InAppConnection.this.reSend(removeUnhandledMessage, 2000)) {
                                InAppConnection.this.mMessageHandler.onResult(removeUnhandledMessage, i2);
                            }
                            if (removeUnhandledMessage.getNetPermanceMonitor() != null) {
                                AppMonitorAdapter.commitCount("accs", BaseMonitor.COUNT_POINT_RESEND, "total_tnet", 0.0d);
                            }
                        } else {
                            InAppConnection.this.mMessageHandler.onResult(removeUnhandledMessage, i2);
                        }
                    }
                }
                if (i < 0 && z) {
                    InAppConnection.this.reSendAck(i);
                }
            }
        });
    }

    public boolean cancel(String str) {
        if (str == null) {
            return false;
        }
        ScheduledFuture scheduledFuture = (ScheduledFuture) this.mMessageHandler.reqTasks.get(str);
        boolean cancel = scheduledFuture != null ? scheduledFuture.cancel(false) : false;
        if (cancel) {
            ALog.e(getTag(), "cancel", "customDataId", str);
        }
        return cancel;
    }

    public void onReceiveAccsHeartbeatResp(org.json.JSONObject jSONObject) {
        org.json.JSONObject jSONObject2 = jSONObject;
        if (jSONObject2 == null) {
            ALog.e(getTag(), "onReceiveAccsHeartbeatResp response data is null", new Object[0]);
            return;
        }
        if (ALog.isPrintLog(ALog.Level.I)) {
            ALog.i(getTag(), "onReceiveAccsHeartbeatResp", "data", jSONObject2);
        }
        try {
            int i = jSONObject2.getInt("timeInterval");
            if (i != -1) {
                long j = (long) (i * 1000);
                if (this.accsHeartbeatInterval != j) {
                    if (i == 0) {
                        j = 3600000;
                    }
                    this.accsHeartbeatInterval = j;
                    this.accsHeartBeatFuture = ThreadPoolExecutorFactory.getScheduledExecutor().scheduleAtFixedRate(this.accsHeartBeatTask, this.accsHeartbeatInterval, this.accsHeartbeatInterval, TimeUnit.MILLISECONDS);
                }
            } else if (this.accsHeartBeatFuture != null) {
                this.accsHeartBeatFuture.cancel(true);
            }
        } catch (JSONException e) {
            ALog.e(getTag(), "onReceiveAccsHeartbeatResp", "e", e.getMessage());
        }
    }

    public String getTag() {
        return TAG + this.mConfigTag;
    }

    /* access modifiers changed from: protected */
    public void initAwcn(Context context) {
        boolean z;
        try {
            if (!this.mAwcnInited) {
                super.initAwcn(context);
                String inappHost = this.mConfig.getInappHost();
                if (!isKeepAlive() || !this.mConfig.isKeepalive()) {
                    ALog.d(getTag(), "initAwcn close keepalive", new Object[0]);
                    z = false;
                } else {
                    z = true;
                }
                if (OrangeAdapter.isChannelModeEnable()) {
                    AwcnConfig.setAccsSessionCreateForbiddenInBg(false);
                }
                registerSessionInfo(SessionCenter.getInstance(this.mConfig.getAppKey()), inappHost, z);
                this.mAwcnInited = true;
                ALog.i(getTag(), "initAwcn success!", new Object[0]);
            }
        } catch (Throwable th) {
            ALog.e(getTag(), "initAwcn", th, new Object[0]);
        }
    }

    public void registerSessionInfo(SessionCenter sessionCenter, String str, boolean z) {
        if (!this.mSessionRegistered.contains(str)) {
            if (!OrangeAdapter.isChannelModeEnable()) {
                this.smartHeartbeat = null;
            } else if (this.smartHeartbeat == null) {
                this.smartHeartbeat = new SmartHeartbeatImpl();
            }
            sessionCenter.registerSessionInfo(SessionInfo.create(str, z, true, new Auth(this, str), this.smartHeartbeat, this));
            sessionCenter.registerPublicKey(str, this.mConfig.getInappPubKey());
            this.mSessionRegistered.add(str);
            ALog.i(getTag(), "registerSessionInfo", "host", str);
        }
    }

    public void updateConfig(AccsClientConfig accsClientConfig) {
        if (accsClientConfig == null) {
            ALog.i(getTag(), "updateConfig null", new Object[0]);
        } else if (accsClientConfig.equals(this.mConfig)) {
            ALog.w(getTag(), "updateConfig not any changed", new Object[0]);
        } else if (this.mAwcnInited || (!OrangeAdapter.isChannelModeEnable() && !UtilityImpl.isMainProcess(this.mContext))) {
            try {
                boolean z = true;
                ALog.w(getTag(), "updateConfig", "old", this.mConfig, "new", accsClientConfig);
                String inappHost = this.mConfig.getInappHost();
                String inappHost2 = accsClientConfig.getInappHost();
                SessionCenter instance = SessionCenter.getInstance(this.mConfig.getAppKey());
                if (instance == null) {
                    ALog.w(getTag(), "updateConfig not need update", new Object[0]);
                    return;
                }
                instance.unregisterSessionInfo(inappHost);
                ALog.w(getTag(), "updateConfig unregisterSessionInfo", "host", inappHost);
                if (this.mSessionRegistered.contains(inappHost)) {
                    this.mSessionRegistered.remove(inappHost);
                    ALog.w(getTag(), "updateConfig removeSessionRegistered", "oldHost", inappHost);
                }
                this.mConfig = accsClientConfig;
                this.mAppkey = this.mConfig.getAppKey();
                this.mConfigTag = this.mConfig.getTag();
                String str = ConnType.PK_ACS;
                if (this.mConfig.getInappPubKey() == 10 || this.mConfig.getInappPubKey() == 11) {
                    str = ConnType.PK_OPEN;
                }
                ALog.i(getTag(), "update config register new conn protocol host:", this.mConfig.getInappHost());
                StrategyTemplate.getInstance().registerConnProtocol(this.mConfig.getInappHost(), ConnProtocol.valueOf(ConnType.HTTP2, ConnType.RTT_0, str, false));
                if (!isKeepAlive() || !this.mConfig.isKeepalive()) {
                    ALog.i(getTag(), "updateConfig close keepalive", new Object[0]);
                    z = false;
                }
                registerSessionInfo(instance, inappHost2, z);
            } catch (Throwable th) {
                ALog.e(getTag(), "updateConfig", th, new Object[0]);
            }
        } else {
            this.mConfig = accsClientConfig;
            initAwcn(this.mContext);
        }
    }

    public static class Auth implements IAuth {
        /* access modifiers changed from: private */
        public String TAG;
        private String authUrl;
        /* access modifiers changed from: private */
        public BaseConnection connection;
        private int connectionType;

        public Auth(BaseConnection baseConnection, String str) {
            this.TAG = baseConnection.getTag();
            this.authUrl = baseConnection.buildAuthUrl("https://" + str + "/accs/");
            this.connectionType = baseConnection.mConnectionType;
            this.connection = baseConnection;
        }

        public void auth(Session session, final IAuth.AuthCallback authCallback) {
            if (OrangeAdapter.isChannelModeEnable()) {
                this.authUrl = this.authUrl.substring(0, this.authUrl.indexOf("&21=")) + "&21=" + BaseConnection.state;
            }
            ALog.e(this.TAG, "auth", WVConstants.INTENT_EXTRA_URL, this.authUrl);
            session.request(new Request.Builder().setUrl(this.authUrl).build(), new RequestCb() {
                public void onDataReceive(ByteArray byteArray, boolean z) {
                }

                public void onResponseCode(int i, Map<String, List<String>> map) {
                    ALog.e(Auth.this.TAG, "auth", "httpStatusCode", Integer.valueOf(i));
                    if (i == 200) {
                        authCallback.onAuthSuccess();
                        if (Auth.this.connection instanceof InAppConnection) {
                            ((InAppConnection) Auth.this.connection).startAccsHeartBeat();
                        }
                    } else {
                        authCallback.onAuthFail(i, "auth fail");
                    }
                    Map<String, String> header = UtilityImpl.getHeader(map);
                    ALog.d(Auth.this.TAG, "auth", "header", header);
                    String str = header.get("x-at");
                    if (!TextUtils.isEmpty(str)) {
                        Auth.this.connection.mConnToken = str;
                    }
                }

                public void onFinish(int i, String str, RequestStatistic requestStatistic) {
                    if (i < 0) {
                        ALog.e(Auth.this.TAG, "auth onFinish", "statusCode", Integer.valueOf(i));
                        authCallback.onAuthFail(i, "onFinish auth fail");
                    }
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void startAccsHeartBeat() {
        if (this.mConfig.isAccsHeartbeatEnable()) {
            ALog.e(getTag(), "startAccsHeartBeat", new Object[0]);
            this.accsHeartBeatFuture = ThreadPoolExecutorFactory.getScheduledExecutor().scheduleAtFixedRate(this.accsHeartBeatTask, this.accsHeartbeatInterval, this.accsHeartbeatInterval, TimeUnit.MILLISECONDS);
        }
    }

    public void setForeBackState(int i) {
        super.setForeBackState(i);
        if (this.smartHeartbeat != null) {
            this.smartHeartbeat.setState(i);
        }
    }
}
