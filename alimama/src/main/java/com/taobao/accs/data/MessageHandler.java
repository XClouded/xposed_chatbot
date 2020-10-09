package com.taobao.accs.data;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import anet.channel.GlobalAppRuntimeInfo;
import anet.channel.appmonitor.AppMonitor;
import com.taobao.accs.antibrush.AntiBrush;
import com.taobao.accs.base.TaoBaseService;
import com.taobao.accs.common.Constants;
import com.taobao.accs.common.ThreadPoolExecutorFactory;
import com.taobao.accs.data.Message;
import com.taobao.accs.flowcontrol.FlowControl;
import com.taobao.accs.net.BaseConnection;
import com.taobao.accs.net.InAppConnection;
import com.taobao.accs.ut.monitor.NetPerformanceMonitor;
import com.taobao.accs.ut.monitor.TrafficsMonitor;
import com.taobao.accs.ut.statistics.BindAppStatistic;
import com.taobao.accs.ut.statistics.BindUserStatistic;
import com.taobao.accs.ut.statistics.ReceiveMsgStat;
import com.taobao.accs.ut.statistics.SendAckStatistic;
import com.taobao.accs.utl.ALog;
import com.taobao.accs.utl.AppMonitorAdapter;
import com.taobao.accs.utl.BaseMonitor;
import com.taobao.accs.utl.JsonUtility;
import com.taobao.accs.utl.MessageStreamReader;
import com.taobao.accs.utl.UTMini;
import com.taobao.accs.utl.UtilityImpl;
import com.taobao.weex.common.Constants;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ScheduledFuture;
import java.util.zip.GZIPInputStream;
import org.json.JSONArray;
import org.json.JSONObject;

public class MessageHandler {
    private static final int MESSAGE_ID_CACHE_SIZE = 50;
    private String TAG = "MsgRecv_";
    private Map<String, AssembleMessage> assembleMessageMap = new HashMap();
    private LinkedHashMap<String, String> handledMessageId = new LinkedHashMap<String, String>() {
        /* access modifiers changed from: protected */
        public boolean removeEldestEntry(Map.Entry<String, String> entry) {
            return size() > 50;
        }
    };
    public String mAccsDeviceToken = "";
    public AntiBrush mAntiBrush;
    public int mConnectType;
    private BaseConnection mConnection;
    private Context mContext;
    public FlowControl mFlowControl;
    private Message mLastSendMessage;
    private ReceiveMsgStat mReceiveMsgStat;
    private Runnable mRestoreTrafficsRunnable = new Runnable() {
        public void run() {
            if (MessageHandler.this.mTrafficMonitor != null) {
                MessageHandler.this.mTrafficMonitor.restoreTraffics();
            }
        }
    };
    protected TrafficsMonitor mTrafficMonitor;
    public ConcurrentMap<String, ScheduledFuture<?>> reqTasks = new ConcurrentHashMap();
    private ConcurrentMap<Message.Id, Message> unHandleMessage = new ConcurrentHashMap();
    private boolean unRevPing = false;

    private boolean isNetWorkError(int i) {
        return i == -1 || i == -9 || i == -10 || i == -11;
    }

    public MessageHandler(Context context, BaseConnection baseConnection) {
        String str;
        this.mContext = context;
        this.mConnection = baseConnection;
        this.mTrafficMonitor = new TrafficsMonitor(this.mContext);
        this.mFlowControl = new FlowControl(this.mContext);
        this.mAntiBrush = new AntiBrush(this.mContext);
        if (baseConnection == null) {
            str = this.TAG;
        } else {
            str = this.TAG + baseConnection.mConfigTag;
        }
        this.TAG = str;
        restoreMessageId();
        restoreTraffics();
    }

    public void onMessage(byte[] bArr) throws IOException {
        onMessage(bArr, (String) null);
    }

    public void onMessage(byte[] bArr, String str) throws IOException {
        if (ALog.isPrintLog(ALog.Level.I)) {
            ALog.i(this.TAG, "onMessage", "host", str);
        }
        MessageStreamReader messageStreamReader = new MessageStreamReader(bArr);
        try {
            int readByte = messageStreamReader.readByte();
            int i = (readByte & 240) >> 4;
            if (ALog.isPrintLog(ALog.Level.D)) {
                String str2 = this.TAG;
                ALog.d(str2, "version:" + i, new Object[0]);
            }
            int i2 = readByte & 15;
            if (ALog.isPrintLog(ALog.Level.D)) {
                String str3 = this.TAG;
                ALog.d(str3, "compress:" + i2, new Object[0]);
            }
            messageStreamReader.readByte();
            int readShort = messageStreamReader.readShort();
            if (ALog.isPrintLog(ALog.Level.D)) {
                String str4 = this.TAG;
                ALog.d(str4, "totalLen:" + readShort, new Object[0]);
            }
            int i3 = 0;
            while (i3 < readShort) {
                int readShort2 = messageStreamReader.readShort();
                int i4 = i3 + 2;
                if (readShort2 > 0) {
                    byte[] bArr2 = new byte[readShort2];
                    messageStreamReader.read(bArr2);
                    if (ALog.isPrintLog(ALog.Level.D)) {
                        String str5 = this.TAG;
                        ALog.d(str5, "buf len:" + bArr2.length, new Object[0]);
                    }
                    i3 = i4 + bArr2.length;
                    handleMessage(i2, bArr2, str, i);
                } else {
                    throw new IOException("data format error");
                }
            }
        } catch (Throwable th) {
            messageStreamReader.close();
            throw th;
        }
        messageStreamReader.close();
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v2, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v7, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v8, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v10, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v27, resolved type: com.taobao.accs.ut.monitor.TrafficsMonitor$TrafficInfo} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v33, resolved type: com.taobao.accs.ut.monitor.TrafficsMonitor$TrafficInfo} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v34, resolved type: com.taobao.accs.ut.monitor.TrafficsMonitor$TrafficInfo} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v35, resolved type: com.taobao.accs.ut.monitor.TrafficsMonitor$TrafficInfo} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v36, resolved type: com.taobao.accs.ut.monitor.TrafficsMonitor$TrafficInfo} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v37, resolved type: com.taobao.accs.ut.monitor.TrafficsMonitor$TrafficInfo} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v38, resolved type: com.taobao.accs.ut.monitor.TrafficsMonitor$TrafficInfo} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v39, resolved type: com.taobao.accs.ut.monitor.TrafficsMonitor$TrafficInfo} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v40, resolved type: com.taobao.accs.ut.monitor.TrafficsMonitor$TrafficInfo} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v41, resolved type: com.taobao.accs.ut.monitor.TrafficsMonitor$TrafficInfo} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v42, resolved type: com.taobao.accs.ut.monitor.TrafficsMonitor$TrafficInfo} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v43, resolved type: com.taobao.accs.ut.monitor.TrafficsMonitor$TrafficInfo} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v44, resolved type: com.taobao.accs.ut.monitor.TrafficsMonitor$TrafficInfo} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v45, resolved type: com.taobao.accs.ut.monitor.TrafficsMonitor$TrafficInfo} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v47, resolved type: com.taobao.accs.ut.monitor.TrafficsMonitor$TrafficInfo} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v48, resolved type: com.taobao.accs.ut.monitor.TrafficsMonitor$TrafficInfo} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v49, resolved type: com.taobao.accs.ut.monitor.TrafficsMonitor$TrafficInfo} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v50, resolved type: com.taobao.accs.ut.monitor.TrafficsMonitor$TrafficInfo} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v51, resolved type: com.taobao.accs.ut.monitor.TrafficsMonitor$TrafficInfo} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v52, resolved type: com.taobao.accs.ut.monitor.TrafficsMonitor$TrafficInfo} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v53, resolved type: com.taobao.accs.ut.monitor.TrafficsMonitor$TrafficInfo} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v55, resolved type: com.taobao.accs.ut.monitor.TrafficsMonitor$TrafficInfo} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v56, resolved type: com.taobao.accs.ut.monitor.TrafficsMonitor$TrafficInfo} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v57, resolved type: com.taobao.accs.ut.monitor.TrafficsMonitor$TrafficInfo} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v59, resolved type: com.taobao.accs.ut.monitor.TrafficsMonitor$TrafficInfo} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v60, resolved type: com.taobao.accs.ut.monitor.TrafficsMonitor$TrafficInfo} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v61, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v81, resolved type: com.taobao.accs.ut.monitor.TrafficsMonitor$TrafficInfo} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v62, resolved type: java.lang.String} */
    /* JADX WARNING: type inference failed for: r6v6 */
    /* JADX WARNING: type inference failed for: r15v2, types: [int, boolean] */
    /* JADX WARNING: type inference failed for: r15v7 */
    /* JADX WARNING: type inference failed for: r15v8 */
    /* JADX WARNING: type inference failed for: r15v15 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:220:0x053e  */
    /* JADX WARNING: Removed duplicated region for block: B:228:0x054c A[SYNTHETIC, Splitter:B:228:0x054c] */
    /* JADX WARNING: Removed duplicated region for block: B:234:0x05a5  */
    /* JADX WARNING: Removed duplicated region for block: B:237:0x05b1  */
    /* JADX WARNING: Removed duplicated region for block: B:241:0x061a A[Catch:{ Exception -> 0x06d5 }] */
    /* JADX WARNING: Removed duplicated region for block: B:242:0x061c A[Catch:{ Exception -> 0x06d5 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void handleMessage(int r48, byte[] r49, java.lang.String r50, int r51) throws java.io.IOException {
        /*
            r47 = this;
            r7 = r47
            r0 = r48
            r8 = r49
            r15 = r50
            com.taobao.accs.utl.MessageStreamReader r1 = new com.taobao.accs.utl.MessageStreamReader
            r1.<init>(r8)
            int r2 = r1.readShort()
            long r9 = (long) r2
            com.taobao.accs.utl.ALog$Level r2 = com.taobao.accs.utl.ALog.Level.D
            boolean r2 = com.taobao.accs.utl.ALog.isPrintLog(r2)
            r14 = 0
            if (r2 == 0) goto L_0x0038
            java.lang.String r2 = r7.TAG
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "flag:"
            r3.append(r4)
            int r4 = (int) r9
            java.lang.String r4 = java.lang.Integer.toHexString(r4)
            r3.append(r4)
            java.lang.String r3 = r3.toString()
            java.lang.Object[] r4 = new java.lang.Object[r14]
            com.taobao.accs.utl.ALog.d(r2, r3, r4)
        L_0x0038:
            int r2 = r1.readByte()
            java.lang.String r11 = r1.readString(r2)
            com.taobao.accs.utl.ALog$Level r2 = com.taobao.accs.utl.ALog.Level.D
            boolean r2 = com.taobao.accs.utl.ALog.isPrintLog(r2)
            if (r2 == 0) goto L_0x0060
            java.lang.String r2 = r7.TAG
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "target:"
            r3.append(r4)
            r3.append(r11)
            java.lang.String r3 = r3.toString()
            java.lang.Object[] r4 = new java.lang.Object[r14]
            com.taobao.accs.utl.ALog.d(r2, r3, r4)
        L_0x0060:
            int r2 = r1.readByte()
            java.lang.String r12 = r1.readString(r2)
            com.taobao.accs.utl.ALog$Level r2 = com.taobao.accs.utl.ALog.Level.D
            boolean r2 = com.taobao.accs.utl.ALog.isPrintLog(r2)
            if (r2 == 0) goto L_0x0088
            java.lang.String r2 = r7.TAG
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "source:"
            r3.append(r4)
            r3.append(r12)
            java.lang.String r3 = r3.toString()
            java.lang.Object[] r4 = new java.lang.Object[r14]
            com.taobao.accs.utl.ALog.d(r2, r3, r4)
        L_0x0088:
            int r2 = r1.readByte()     // Catch:{ Exception -> 0x0750 }
            java.lang.String r13 = r1.readString(r2)     // Catch:{ Exception -> 0x0750 }
            com.taobao.accs.utl.ALog$Level r2 = com.taobao.accs.utl.ALog.Level.D
            boolean r2 = com.taobao.accs.utl.ALog.isPrintLog(r2)
            if (r2 == 0) goto L_0x00b0
            java.lang.String r2 = r7.TAG
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "dataId:"
            r3.append(r4)
            r3.append(r13)
            java.lang.String r3 = r3.toString()
            java.lang.Object[] r4 = new java.lang.Object[r14]
            com.taobao.accs.utl.ALog.d(r2, r3, r4)
        L_0x00b0:
            java.lang.String r2 = "4|sal|st"
            boolean r2 = r12.contains(r2)
            if (r2 == 0) goto L_0x00db
            java.lang.String r0 = r7.TAG
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "ignore source 4|sal|st message dataId:"
            r1.append(r2)
            r1.append(r13)
            java.lang.String r1 = r1.toString()
            java.lang.Object[] r2 = new java.lang.Object[r14]
            com.taobao.accs.utl.ALog.e(r0, r1, r2)
            java.util.concurrent.ConcurrentMap<com.taobao.accs.data.Message$Id, com.taobao.accs.data.Message> r0 = r7.unHandleMessage
            com.taobao.accs.data.Message$Id r1 = new com.taobao.accs.data.Message$Id
            r1.<init>(r14, r13)
            r0.remove(r1)
            return
        L_0x00db:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            r2.append(r12)
            r2.append(r13)
            java.lang.String r6 = r2.toString()
            int r2 = r1.available()
            r4 = 2
            r3 = 1
            if (r2 <= 0) goto L_0x0139
            r2 = r51
            if (r2 != r4) goto L_0x011a
            java.util.Map r2 = r7.parseExtHeader(r1)
            if (r2 == 0) goto L_0x0117
            r16 = 16
            java.lang.Integer r5 = java.lang.Integer.valueOf(r16)
            boolean r5 = r2.containsKey(r5)
            if (r5 == 0) goto L_0x0117
            r5 = 17
            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)
            boolean r5 = r2.containsKey(r5)
            if (r5 == 0) goto L_0x0117
            r5 = r2
            r2 = 1
            goto L_0x011c
        L_0x0117:
            r5 = r2
            r2 = 0
            goto L_0x011c
        L_0x011a:
            r2 = 0
            r5 = 0
        L_0x011c:
            if (r0 == 0) goto L_0x012d
            if (r2 == 0) goto L_0x0121
            goto L_0x012d
        L_0x0121:
            if (r0 != r3) goto L_0x0128
            byte[] r16 = r7.gzipInputStream(r1)
            goto L_0x0131
        L_0x0128:
            r16 = r2
            r2 = r5
            r5 = 0
            goto L_0x013d
        L_0x012d:
            byte[] r16 = r1.readAll()
        L_0x0131:
            r46 = r16
            r16 = r2
            r2 = r5
            r5 = r46
            goto L_0x013d
        L_0x0139:
            r2 = 0
            r5 = 0
            r16 = 0
        L_0x013d:
            r1.close()
            if (r5 != 0) goto L_0x0153
            java.lang.String r1 = r7.TAG     // Catch:{ Exception -> 0x014c }
            java.lang.String r4 = "oriData is null"
            java.lang.Object[] r3 = new java.lang.Object[r14]     // Catch:{ Exception -> 0x014c }
            com.taobao.accs.utl.ALog.d(r1, r4, r3)     // Catch:{ Exception -> 0x014c }
            goto L_0x0177
        L_0x014c:
            r0 = move-exception
        L_0x014d:
            r6 = r13
        L_0x014e:
            r2 = 0
            r3 = 0
        L_0x0150:
            r5 = 1
            goto L_0x0714
        L_0x0153:
            com.taobao.accs.utl.ALog$Level r1 = com.taobao.accs.utl.ALog.Level.D     // Catch:{ Exception -> 0x014c }
            boolean r1 = com.taobao.accs.utl.ALog.isPrintLog(r1)     // Catch:{ Exception -> 0x014c }
            if (r1 == 0) goto L_0x0177
            java.lang.String r1 = r7.TAG     // Catch:{ Exception -> 0x014c }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x014c }
            r3.<init>()     // Catch:{ Exception -> 0x014c }
            java.lang.String r4 = "oriData:"
            r3.append(r4)     // Catch:{ Exception -> 0x014c }
            java.lang.String r4 = java.lang.String.valueOf(r5)     // Catch:{ Exception -> 0x014c }
            r3.append(r4)     // Catch:{ Exception -> 0x014c }
            java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x014c }
            java.lang.Object[] r4 = new java.lang.Object[r14]     // Catch:{ Exception -> 0x014c }
            com.taobao.accs.utl.ALog.d(r1, r3, r4)     // Catch:{ Exception -> 0x014c }
        L_0x0177:
            r1 = 15
            long r3 = r9 >> r1
            r21 = 1
            long r3 = r3 & r21
            int r1 = (int) r3     // Catch:{ Exception -> 0x014c }
            int r4 = com.taobao.accs.data.Message.MsgType.valueOf(r1)     // Catch:{ Exception -> 0x014c }
            r1 = 13
            long r23 = r9 >> r1
            r25 = 3
            long r14 = r23 & r25
            int r1 = (int) r14     // Catch:{ Exception -> 0x014c }
            com.taobao.accs.data.Message$ReqType r14 = com.taobao.accs.data.Message.ReqType.valueOf((int) r1)     // Catch:{ Exception -> 0x014c }
            r1 = 12
            long r23 = r9 >> r1
            r28 = r2
            long r1 = r23 & r21
            int r1 = (int) r1     // Catch:{ Exception -> 0x014c }
            r2 = 11
            long r2 = r9 >> r2
            long r2 = r2 & r21
            int r2 = (int) r2     // Catch:{ Exception -> 0x014c }
            int r15 = com.taobao.accs.data.Message.MsgResType.valueOf(r2)     // Catch:{ Exception -> 0x014c }
            r3 = 6
            long r23 = r9 >> r3
            r29 = r4
            long r3 = r23 & r21
            int r2 = (int) r3     // Catch:{ Exception -> 0x014c }
            r3 = 1
            if (r2 != r3) goto L_0x01b2
            r4 = 1
            goto L_0x01b3
        L_0x01b2:
            r4 = 0
        L_0x01b3:
            com.taobao.accs.utl.ALog$Level r2 = com.taobao.accs.utl.ALog.Level.I     // Catch:{ Exception -> 0x014c }
            boolean r2 = com.taobao.accs.utl.ALog.isPrintLog(r2)     // Catch:{ Exception -> 0x014c }
            if (r2 == 0) goto L_0x020c
            java.lang.String r2 = r7.TAG     // Catch:{ Exception -> 0x0207 }
            java.lang.String r3 = "handleMessage"
            r30 = r4
            r4 = 10
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch:{ Exception -> 0x0207 }
            java.lang.String r21 = "dataId"
            r22 = 0
            r4[r22] = r21     // Catch:{ Exception -> 0x0207 }
            r20 = 1
            r4[r20] = r13     // Catch:{ Exception -> 0x0207 }
            java.lang.String r21 = "type"
            r19 = 2
            r4[r19] = r21     // Catch:{ Exception -> 0x0207 }
            java.lang.String r21 = com.taobao.accs.data.Message.MsgType.name(r29)     // Catch:{ Exception -> 0x0207 }
            r22 = 3
            r4[r22] = r21     // Catch:{ Exception -> 0x0207 }
            java.lang.String r21 = "reqType"
            r23 = 4
            r4[r23] = r21     // Catch:{ Exception -> 0x0207 }
            java.lang.String r21 = r14.name()     // Catch:{ Exception -> 0x0207 }
            r18 = 5
            r4[r18] = r21     // Catch:{ Exception -> 0x014c }
            java.lang.String r21 = "resType"
            r24 = 6
            r4[r24] = r21     // Catch:{ Exception -> 0x014c }
            r21 = 7
            java.lang.String r25 = com.taobao.accs.data.Message.MsgResType.name(r15)     // Catch:{ Exception -> 0x014c }
            r4[r21] = r25     // Catch:{ Exception -> 0x014c }
            r21 = 8
            java.lang.String r25 = "target"
            r4[r21] = r25     // Catch:{ Exception -> 0x014c }
            r21 = 9
            r4[r21] = r11     // Catch:{ Exception -> 0x014c }
            com.taobao.accs.utl.ALog.i(r2, r3, r4)     // Catch:{ Exception -> 0x014c }
            goto L_0x0218
        L_0x0207:
            r0 = move-exception
            r18 = 5
            goto L_0x014d
        L_0x020c:
            r30 = r4
            r18 = 5
            r19 = 2
            r22 = 3
            r23 = 4
            r24 = 6
        L_0x0218:
            r4 = r29
            r2 = 1
            if (r4 != r2) goto L_0x032d
            com.taobao.accs.data.Message$ReqType r2 = com.taobao.accs.data.Message.ReqType.ACK     // Catch:{ Exception -> 0x014c }
            if (r14 == r2) goto L_0x0239
            com.taobao.accs.data.Message$ReqType r2 = com.taobao.accs.data.Message.ReqType.RES     // Catch:{ Exception -> 0x014c }
            if (r14 != r2) goto L_0x0226
            goto L_0x0239
        L_0x0226:
            r1 = r4
            r37 = r5
            r38 = r6
            r41 = r9
            r36 = r12
            r6 = r13
            r33 = r15
            r9 = r28
            r35 = r30
        L_0x0236:
            r15 = 1
            goto L_0x033e
        L_0x0239:
            java.util.concurrent.ConcurrentMap<com.taobao.accs.data.Message$Id, com.taobao.accs.data.Message> r2 = r7.unHandleMessage     // Catch:{ Exception -> 0x014c }
            com.taobao.accs.data.Message$Id r3 = new com.taobao.accs.data.Message$Id     // Catch:{ Exception -> 0x014c }
            r31 = r4
            r4 = 0
            r3.<init>(r4, r13)     // Catch:{ Exception -> 0x014c }
            java.lang.Object r2 = r2.remove(r3)     // Catch:{ Exception -> 0x014c }
            r4 = r2
            com.taobao.accs.data.Message r4 = (com.taobao.accs.data.Message) r4     // Catch:{ Exception -> 0x014c }
            if (r4 == 0) goto L_0x02e9
            com.taobao.accs.utl.ALog$Level r2 = com.taobao.accs.utl.ALog.Level.D     // Catch:{ Exception -> 0x014c }
            boolean r2 = com.taobao.accs.utl.ALog.isPrintLog(r2)     // Catch:{ Exception -> 0x014c }
            if (r2 == 0) goto L_0x0263
            java.lang.String r2 = r7.TAG     // Catch:{ Exception -> 0x014c }
            java.lang.String r3 = "handleMessage reqMessage not null"
            r32 = r6
            r33 = r15
            r6 = 0
            java.lang.Object[] r15 = new java.lang.Object[r6]     // Catch:{ Exception -> 0x014c }
            com.taobao.accs.utl.ALog.d(r2, r3, r15)     // Catch:{ Exception -> 0x014c }
            goto L_0x0267
        L_0x0263:
            r32 = r6
            r33 = r15
        L_0x0267:
            r2 = 200(0xc8, float:2.8E-43)
            r3 = 1
            if (r1 != r3) goto L_0x0281
            org.json.JSONObject r1 = new org.json.JSONObject     // Catch:{ Exception -> 0x027e }
            java.lang.String r2 = new java.lang.String     // Catch:{ Exception -> 0x027e }
            r2.<init>(r5)     // Catch:{ Exception -> 0x027e }
            r1.<init>(r2)     // Catch:{ Exception -> 0x027e }
            java.lang.String r2 = "code"
            int r1 = r1.getInt(r2)     // Catch:{ Exception -> 0x027e }
            r6 = r1
            goto L_0x0283
        L_0x027e:
            r1 = -3
            r6 = -3
            goto L_0x0283
        L_0x0281:
            r6 = 200(0xc8, float:2.8E-43)
        L_0x0283:
            com.taobao.accs.ut.monitor.NetPerformanceMonitor r1 = r4.getNetPermanceMonitor()     // Catch:{ Exception -> 0x014c }
            if (r1 == 0) goto L_0x0290
            com.taobao.accs.ut.monitor.NetPerformanceMonitor r1 = r4.getNetPermanceMonitor()     // Catch:{ Exception -> 0x014c }
            r1.onRecAck()     // Catch:{ Exception -> 0x014c }
        L_0x0290:
            com.taobao.accs.data.Message$ReqType r1 = com.taobao.accs.data.Message.ReqType.RES     // Catch:{ Exception -> 0x014c }
            if (r14 != r1) goto L_0x02b8
            r15 = 5
            r1 = r47
            r34 = r28
            r2 = r4
            r3 = r6
            r15 = r4
            r36 = r12
            r35 = r30
            r6 = r31
            r12 = 2
            r4 = r14
            r37 = r5
            r12 = 0
            r39 = r6
            r38 = r32
            r6 = r34
            r1.onResult(r2, r3, r4, r5, r6)     // Catch:{ Exception -> 0x02b3 }
            r5 = r34
            goto L_0x02c9
        L_0x02b3:
            r0 = move-exception
            r3 = r12
            r6 = r13
            goto L_0x03c7
        L_0x02b8:
            r15 = r4
            r37 = r5
            r36 = r12
            r5 = r28
            r35 = r30
            r39 = r31
            r38 = r32
            r12 = 0
            r7.onResult(r15, r6, r5)     // Catch:{ Exception -> 0x02b3 }
        L_0x02c9:
            com.taobao.accs.ut.monitor.TrafficsMonitor$TrafficInfo r6 = new com.taobao.accs.ut.monitor.TrafficsMonitor$TrafficInfo     // Catch:{ Exception -> 0x02b3 }
            java.lang.String r2 = r15.serviceId     // Catch:{ Exception -> 0x02b3 }
            boolean r3 = anet.channel.GlobalAppRuntimeInfo.isAppBackground()     // Catch:{ Exception -> 0x02b3 }
            int r1 = r8.length     // Catch:{ Exception -> 0x02b3 }
            r40 = r13
            long r12 = (long) r1
            r1 = r6
            r4 = r50
            r15 = r6
            r41 = r9
            r9 = r5
            r5 = r12
            r1.<init>(r2, r3, r4, r5)     // Catch:{ Exception -> 0x0328 }
            r7.addTrafficsInfo(r15)     // Catch:{ Exception -> 0x0328 }
            r1 = r39
            r6 = r40
            goto L_0x0236
        L_0x02e9:
            r37 = r5
            r38 = r6
            r41 = r9
            r36 = r12
            r40 = r13
            r33 = r15
            r9 = r28
            r35 = r30
            r39 = r31
            com.taobao.accs.net.BaseConnection r1 = r7.mConnection     // Catch:{ Exception -> 0x0328 }
            com.taobao.accs.net.BaseConnection r2 = r7.mConnection     // Catch:{ Exception -> 0x0328 }
            r3 = 0
            java.lang.String r2 = r2.getHost(r3)     // Catch:{ Exception -> 0x0323 }
            r6 = r40
            r3 = 5
            com.taobao.accs.data.Message r2 = com.taobao.accs.data.Message.buildErrorReportMessage(r6, r11, r2, r3)     // Catch:{ Exception -> 0x03a4 }
            r15 = 1
            r1.send(r2, r15)     // Catch:{ Exception -> 0x03a4 }
            java.lang.String r1 = r7.TAG     // Catch:{ Exception -> 0x03a4 }
            java.lang.String r2 = "handleMessage data ack/res reqMessage is null"
            r3 = 2
            java.lang.Object[] r4 = new java.lang.Object[r3]     // Catch:{ Exception -> 0x03a4 }
            java.lang.String r3 = "dataId"
            r5 = 0
            r4[r5] = r3     // Catch:{ Exception -> 0x03a4 }
            r4[r15] = r6     // Catch:{ Exception -> 0x03a4 }
            com.taobao.accs.utl.ALog.e(r1, r2, r4)     // Catch:{ Exception -> 0x03a4 }
            r1 = r39
            goto L_0x033e
        L_0x0323:
            r0 = move-exception
            r6 = r40
            goto L_0x03c7
        L_0x0328:
            r0 = move-exception
            r6 = r40
            goto L_0x014e
        L_0x032d:
            r37 = r5
            r38 = r6
            r41 = r9
            r36 = r12
            r6 = r13
            r33 = r15
            r9 = r28
            r35 = r30
            r15 = 1
            r1 = r4
        L_0x033e:
            if (r1 != 0) goto L_0x03a7
            com.taobao.accs.data.Message$ReqType r2 = com.taobao.accs.data.Message.ReqType.RES     // Catch:{ Exception -> 0x03a4 }
            if (r14 != r2) goto L_0x03a7
            java.util.concurrent.ConcurrentMap<com.taobao.accs.data.Message$Id, com.taobao.accs.data.Message> r2 = r7.unHandleMessage     // Catch:{ Exception -> 0x03a4 }
            com.taobao.accs.data.Message$Id r3 = new com.taobao.accs.data.Message$Id     // Catch:{ Exception -> 0x03a4 }
            r4 = 0
            r3.<init>(r4, r6)     // Catch:{ Exception -> 0x03a4 }
            java.lang.Object r2 = r2.remove(r3)     // Catch:{ Exception -> 0x03a4 }
            com.taobao.accs.data.Message r2 = (com.taobao.accs.data.Message) r2     // Catch:{ Exception -> 0x03a4 }
            if (r2 == 0) goto L_0x035c
            r3 = r37
            r13 = r50
            r7.handleControlMessage(r2, r3, r8, r13)     // Catch:{ Exception -> 0x03a4 }
            return
        L_0x035c:
            r3 = r37
            r13 = r50
            com.taobao.accs.net.BaseConnection r2 = r7.mConnection     // Catch:{ Exception -> 0x03a4 }
            com.taobao.accs.net.BaseConnection r4 = r7.mConnection     // Catch:{ Exception -> 0x03a4 }
            r5 = 0
            java.lang.String r4 = r4.getHost(r5)     // Catch:{ Exception -> 0x03a1 }
            r5 = 5
            com.taobao.accs.data.Message r4 = com.taobao.accs.data.Message.buildErrorReportMessage(r6, r11, r4, r5)     // Catch:{ Exception -> 0x03a4 }
            r2.send(r4, r15)     // Catch:{ Exception -> 0x03a4 }
            java.lang.String r2 = r7.TAG     // Catch:{ Exception -> 0x03a4 }
            java.lang.String r4 = "handleMessage contorl ACK reqMessage is null"
            r5 = 2
            java.lang.Object[] r10 = new java.lang.Object[r5]     // Catch:{ Exception -> 0x03a4 }
            java.lang.String r5 = "dataId"
            r12 = 0
            r10[r12] = r5     // Catch:{ Exception -> 0x03a4 }
            r10[r15] = r6     // Catch:{ Exception -> 0x03a4 }
            com.taobao.accs.utl.ALog.e(r2, r4, r10)     // Catch:{ Exception -> 0x03a4 }
            com.taobao.accs.utl.ALog$Level r2 = com.taobao.accs.utl.ALog.Level.D     // Catch:{ Exception -> 0x03a4 }
            boolean r2 = com.taobao.accs.utl.ALog.isPrintLog(r2)     // Catch:{ Exception -> 0x03a4 }
            if (r2 == 0) goto L_0x03ab
            java.lang.String r2 = r7.TAG     // Catch:{ Exception -> 0x03a4 }
            java.lang.String r4 = "handleMessage not handled"
            r5 = 2
            java.lang.Object[] r10 = new java.lang.Object[r5]     // Catch:{ Exception -> 0x03a4 }
            java.lang.String r5 = "body"
            r12 = 0
            r10[r12] = r5     // Catch:{ Exception -> 0x03a4 }
            java.lang.String r5 = new java.lang.String     // Catch:{ Exception -> 0x03a4 }
            r5.<init>(r3)     // Catch:{ Exception -> 0x03a4 }
            r10[r15] = r5     // Catch:{ Exception -> 0x03a4 }
            com.taobao.accs.utl.ALog.d(r2, r4, r10)     // Catch:{ Exception -> 0x03a4 }
            goto L_0x03ab
        L_0x03a1:
            r0 = move-exception
            r3 = r5
            goto L_0x03c7
        L_0x03a4:
            r0 = move-exception
            goto L_0x014e
        L_0x03a7:
            r3 = r37
            r13 = r50
        L_0x03ab:
            if (r1 != r15) goto L_0x074f
            com.taobao.accs.data.Message$ReqType r1 = com.taobao.accs.data.Message.ReqType.DATA     // Catch:{ Exception -> 0x03a4 }
            if (r14 != r1) goto L_0x074f
            if (r11 != 0) goto L_0x03ca
            com.taobao.accs.net.BaseConnection r0 = r7.mConnection     // Catch:{ Exception -> 0x03a4 }
            java.lang.String r1 = ""
            com.taobao.accs.net.BaseConnection r2 = r7.mConnection     // Catch:{ Exception -> 0x03a4 }
            r3 = 0
            java.lang.String r2 = r2.getHost(r3)     // Catch:{ Exception -> 0x03c6 }
            com.taobao.accs.data.Message r1 = com.taobao.accs.data.Message.buildErrorReportMessage(r6, r1, r2, r15)     // Catch:{ Exception -> 0x03a4 }
            r0.send(r1, r15)     // Catch:{ Exception -> 0x03a4 }
            return
        L_0x03c6:
            r0 = move-exception
        L_0x03c7:
            r2 = 0
            goto L_0x0150
        L_0x03ca:
            java.lang.String r1 = "\\|"
            java.lang.String[] r1 = r11.split(r1)     // Catch:{ Exception -> 0x03a4 }
            int r2 = r1.length     // Catch:{ Exception -> 0x03a4 }
            r4 = 2
            if (r2 >= r4) goto L_0x03e7
            com.taobao.accs.net.BaseConnection r0 = r7.mConnection     // Catch:{ Exception -> 0x03a4 }
            java.lang.String r1 = ""
            com.taobao.accs.net.BaseConnection r2 = r7.mConnection     // Catch:{ Exception -> 0x03a4 }
            r3 = 0
            java.lang.String r2 = r2.getHost(r3)     // Catch:{ Exception -> 0x03c6 }
            com.taobao.accs.data.Message r1 = com.taobao.accs.data.Message.buildErrorReportMessage(r6, r1, r2, r15)     // Catch:{ Exception -> 0x03a4 }
            r0.send(r1, r15)     // Catch:{ Exception -> 0x03a4 }
            return
        L_0x03e7:
            com.taobao.accs.utl.ALog$Level r2 = com.taobao.accs.utl.ALog.Level.D     // Catch:{ Exception -> 0x03a4 }
            boolean r2 = com.taobao.accs.utl.ALog.isPrintLog(r2)     // Catch:{ Exception -> 0x03a4 }
            if (r2 == 0) goto L_0x0404
            java.lang.String r2 = r7.TAG     // Catch:{ Exception -> 0x03a4 }
            java.lang.String r4 = "handleMessage onPush"
            r5 = 2
            java.lang.Object[] r10 = new java.lang.Object[r5]     // Catch:{ Exception -> 0x03a4 }
            java.lang.String r5 = "isBurstData"
            r12 = 0
            r10[r12] = r5     // Catch:{ Exception -> 0x03a4 }
            java.lang.Boolean r5 = java.lang.Boolean.valueOf(r16)     // Catch:{ Exception -> 0x03a4 }
            r10[r15] = r5     // Catch:{ Exception -> 0x03a4 }
            com.taobao.accs.utl.ALog.d(r2, r4, r10)     // Catch:{ Exception -> 0x03a4 }
        L_0x0404:
            com.taobao.accs.ut.statistics.ReceiveMsgStat r2 = r7.mReceiveMsgStat     // Catch:{ Exception -> 0x03a4 }
            if (r2 == 0) goto L_0x040d
            com.taobao.accs.ut.statistics.ReceiveMsgStat r2 = r7.mReceiveMsgStat     // Catch:{ Exception -> 0x03a4 }
            r2.commitUT()     // Catch:{ Exception -> 0x03a4 }
        L_0x040d:
            com.taobao.accs.ut.statistics.ReceiveMsgStat r2 = new com.taobao.accs.ut.statistics.ReceiveMsgStat     // Catch:{ Exception -> 0x03a4 }
            r2.<init>()     // Catch:{ Exception -> 0x03a4 }
            r7.mReceiveMsgStat = r2     // Catch:{ Exception -> 0x03a4 }
            com.taobao.accs.ut.statistics.ReceiveMsgStat r2 = r7.mReceiveMsgStat     // Catch:{ Exception -> 0x03a4 }
            long r4 = java.lang.System.currentTimeMillis()     // Catch:{ Exception -> 0x03a4 }
            java.lang.String r4 = java.lang.String.valueOf(r4)     // Catch:{ Exception -> 0x03a4 }
            r2.receiveDate = r4     // Catch:{ Exception -> 0x03a4 }
            android.content.Context r2 = r7.mContext     // Catch:{ Exception -> 0x03a4 }
            r4 = r1[r15]     // Catch:{ Exception -> 0x03a4 }
            boolean r2 = com.taobao.accs.utl.UtilityImpl.packageExist(r2, r4)     // Catch:{ Exception -> 0x03a4 }
            if (r2 == 0) goto L_0x06da
            int r2 = r1.length     // Catch:{ Exception -> 0x06d5 }
            r4 = 3
            if (r2 < r4) goto L_0x0433
            r2 = 2
            r5 = r1[r2]     // Catch:{ Exception -> 0x03a4 }
            r14 = r5
            goto L_0x0434
        L_0x0433:
            r14 = 0
        L_0x0434:
            com.taobao.accs.ut.statistics.ReceiveMsgStat r2 = r7.mReceiveMsgStat     // Catch:{ Exception -> 0x06d5 }
            r2.serviceId = r14     // Catch:{ Exception -> 0x06d5 }
            r2 = r38
            boolean r5 = r7.isDuplicateMessage(r2)     // Catch:{ Exception -> 0x06d5 }
            r43 = r11
            if (r5 == 0) goto L_0x047c
            com.taobao.accs.net.BaseConnection r0 = r7.mConnection     // Catch:{ Exception -> 0x03a4 }
            com.taobao.accs.net.BaseConnection r1 = r7.mConnection     // Catch:{ Exception -> 0x03a4 }
            r2 = 0
            java.lang.String r1 = r1.getHost(r2)     // Catch:{ Exception -> 0x0478 }
            r2 = 2
            com.taobao.accs.data.Message r1 = com.taobao.accs.data.Message.buildErrorReportMessage(r6, r14, r1, r2)     // Catch:{ Exception -> 0x03a4 }
            r0.send(r1, r15)     // Catch:{ Exception -> 0x03a4 }
            java.lang.String r0 = r7.TAG     // Catch:{ Exception -> 0x03a4 }
            java.lang.String r1 = "handleMessage msg duplicate"
            java.lang.Object[] r3 = new java.lang.Object[r2]     // Catch:{ Exception -> 0x03a4 }
            java.lang.String r2 = "dataId"
            r4 = 0
            r3[r4] = r2     // Catch:{ Exception -> 0x03a4 }
            r3[r15] = r6     // Catch:{ Exception -> 0x03a4 }
            com.taobao.accs.utl.ALog.e(r0, r1, r3)     // Catch:{ Exception -> 0x03a4 }
            com.taobao.accs.ut.statistics.ReceiveMsgStat r0 = r7.mReceiveMsgStat     // Catch:{ Exception -> 0x03a4 }
            r0.repeat = r15     // Catch:{ Exception -> 0x03a4 }
            r0 = r33
            r45 = r35
            r19 = r36
            r10 = r41
            r20 = r43
            r1 = 1
            r12 = 0
            r16 = 5
            goto L_0x065e
        L_0x0478:
            r0 = move-exception
            r3 = r2
            goto L_0x03c7
        L_0x047c:
            if (r16 == 0) goto L_0x04cb
            byte[] r5 = r7.putBurstMessage(r2, r9, r3)     // Catch:{ Exception -> 0x03a4 }
            if (r5 != 0) goto L_0x0495
            com.taobao.accs.net.BaseConnection r0 = r7.mConnection     // Catch:{ Exception -> 0x03a4 }
            com.taobao.accs.net.BaseConnection r1 = r7.mConnection     // Catch:{ Exception -> 0x03a4 }
            r2 = 0
            java.lang.String r1 = r1.getHost(r2)     // Catch:{ Exception -> 0x0478 }
            com.taobao.accs.data.Message r1 = com.taobao.accs.data.Message.buildErrorReportMessage(r6, r14, r1, r15)     // Catch:{ Exception -> 0x03a4 }
            r0.send(r1, r15)     // Catch:{ Exception -> 0x03a4 }
            return
        L_0x0495:
            if (r0 != r15) goto L_0x04cc
            com.taobao.accs.utl.MessageStreamReader r0 = new com.taobao.accs.utl.MessageStreamReader     // Catch:{ Exception -> 0x03a4 }
            r0.<init>(r5)     // Catch:{ Exception -> 0x03a4 }
            byte[] r5 = r7.gzipInputStream(r0)     // Catch:{ Exception -> 0x03a4 }
            com.taobao.accs.utl.ALog$Level r3 = com.taobao.accs.utl.ALog.Level.D     // Catch:{ Exception -> 0x03a4 }
            boolean r3 = com.taobao.accs.utl.ALog.isPrintLog(r3)     // Catch:{ Exception -> 0x03a4 }
            if (r3 == 0) goto L_0x04c7
            java.lang.String r3 = r7.TAG     // Catch:{ Exception -> 0x03a4 }
            java.lang.String r12 = "handleMessage gzip completeOriData"
            r10 = 4
            java.lang.Object[] r11 = new java.lang.Object[r10]     // Catch:{ Exception -> 0x03a4 }
            java.lang.String r16 = "dataId"
            r17 = 0
            r11[r17] = r16     // Catch:{ Exception -> 0x03a4 }
            r11[r15] = r2     // Catch:{ Exception -> 0x03a4 }
            java.lang.String r16 = "length"
            r17 = 2
            r11[r17] = r16     // Catch:{ Exception -> 0x03a4 }
            int r10 = r5.length     // Catch:{ Exception -> 0x03a4 }
            java.lang.Integer r10 = java.lang.Integer.valueOf(r10)     // Catch:{ Exception -> 0x03a4 }
            r11[r4] = r10     // Catch:{ Exception -> 0x03a4 }
            com.taobao.accs.utl.ALog.d(r3, r12, r11)     // Catch:{ Exception -> 0x03a4 }
        L_0x04c7:
            r0.close()     // Catch:{ Exception -> 0x03a4 }
            goto L_0x04cc
        L_0x04cb:
            r5 = r3
        L_0x04cc:
            r7.recordMessageId(r2)     // Catch:{ Exception -> 0x06d5 }
            java.lang.String r0 = "accs"
            boolean r0 = r0.equals(r14)     // Catch:{ Exception -> 0x06d5 }
            if (r0 == 0) goto L_0x04fa
            java.lang.String r0 = r7.TAG     // Catch:{ Exception -> 0x03a4 }
            java.lang.String r2 = "handleMessage try deliverMsg"
            r3 = 6
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch:{ Exception -> 0x03a4 }
            java.lang.String r10 = "dataId"
            r11 = 0
            r3[r11] = r10     // Catch:{ Exception -> 0x03a4 }
            r3[r15] = r6     // Catch:{ Exception -> 0x03a4 }
            java.lang.String r10 = "target"
            r11 = 2
            r3[r11] = r10     // Catch:{ Exception -> 0x03a4 }
            r10 = r1[r15]     // Catch:{ Exception -> 0x03a4 }
            r3[r4] = r10     // Catch:{ Exception -> 0x03a4 }
            java.lang.String r10 = "serviceId"
            r11 = 4
            r3[r11] = r10     // Catch:{ Exception -> 0x03a4 }
            r10 = 5
            r3[r10] = r14     // Catch:{ Exception -> 0x03a4 }
            com.taobao.accs.utl.ALog.e(r0, r2, r3)     // Catch:{ Exception -> 0x03a4 }
            goto L_0x0526
        L_0x04fa:
            r3 = 6
            com.taobao.accs.utl.ALog$Level r0 = com.taobao.accs.utl.ALog.Level.I     // Catch:{ Exception -> 0x06d5 }
            boolean r0 = com.taobao.accs.utl.ALog.isPrintLog(r0)     // Catch:{ Exception -> 0x06d5 }
            if (r0 == 0) goto L_0x0526
            java.lang.String r0 = r7.TAG     // Catch:{ Exception -> 0x03a4 }
            java.lang.String r2 = "handleMessage try deliverMsg"
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch:{ Exception -> 0x03a4 }
            java.lang.String r10 = "dataId"
            r11 = 0
            r3[r11] = r10     // Catch:{ Exception -> 0x03a4 }
            r3[r15] = r6     // Catch:{ Exception -> 0x03a4 }
            java.lang.String r10 = "target"
            r11 = 2
            r3[r11] = r10     // Catch:{ Exception -> 0x03a4 }
            r10 = r1[r15]     // Catch:{ Exception -> 0x03a4 }
            r3[r4] = r10     // Catch:{ Exception -> 0x03a4 }
            java.lang.String r10 = "serviceId"
            r11 = 4
            r3[r11] = r10     // Catch:{ Exception -> 0x03a4 }
            r16 = 5
            r3[r16] = r14     // Catch:{ Exception -> 0x03a4 }
            com.taobao.accs.utl.ALog.i(r0, r2, r3)     // Catch:{ Exception -> 0x03a4 }
            goto L_0x0528
        L_0x0526:
            r16 = 5
        L_0x0528:
            android.content.Intent r0 = new android.content.Intent     // Catch:{ Exception -> 0x06d5 }
            java.lang.String r2 = "com.taobao.accs.intent.action.RECEIVE"
            r0.<init>(r2)     // Catch:{ Exception -> 0x06d5 }
            r2 = r1[r15]     // Catch:{ Exception -> 0x06d5 }
            r0.setPackage(r2)     // Catch:{ Exception -> 0x06d5 }
            java.lang.String r2 = "command"
            r3 = 101(0x65, float:1.42E-43)
            r0.putExtra(r2, r3)     // Catch:{ Exception -> 0x06d5 }
            int r2 = r1.length     // Catch:{ Exception -> 0x06d5 }
            if (r2 < r4) goto L_0x0546
            java.lang.String r2 = "serviceId"
            r3 = 2
            r10 = r1[r3]     // Catch:{ Exception -> 0x03a4 }
            r0.putExtra(r2, r10)     // Catch:{ Exception -> 0x03a4 }
        L_0x0546:
            java.lang.String r2 = ""
            int r3 = r1.length     // Catch:{ Exception -> 0x06d5 }
            r10 = 4
            if (r3 < r10) goto L_0x0553
            r2 = r1[r4]     // Catch:{ Exception -> 0x03a4 }
            java.lang.String r1 = "userInfo"
            r0.putExtra(r1, r2)     // Catch:{ Exception -> 0x03a4 }
        L_0x0553:
            java.lang.String r1 = "data"
            r0.putExtra(r1, r5)     // Catch:{ Exception -> 0x06d5 }
            java.lang.String r1 = "dataId"
            r0.putExtra(r1, r6)     // Catch:{ Exception -> 0x06d5 }
            java.lang.String r1 = "packageName"
            android.content.Context r3 = r7.mContext     // Catch:{ Exception -> 0x06d5 }
            java.lang.String r3 = r3.getPackageName()     // Catch:{ Exception -> 0x06d5 }
            r0.putExtra(r1, r3)     // Catch:{ Exception -> 0x06d5 }
            java.lang.String r1 = "host"
            r0.putExtra(r1, r13)     // Catch:{ Exception -> 0x06d5 }
            java.lang.String r1 = "conn_type"
            int r3 = r7.mConnectType     // Catch:{ Exception -> 0x06d5 }
            r0.putExtra(r1, r3)     // Catch:{ Exception -> 0x06d5 }
            java.lang.String r1 = "bizAck"
            r12 = r35
            r0.putExtra(r1, r12)     // Catch:{ Exception -> 0x06d5 }
            java.lang.String r1 = "appKey"
            com.taobao.accs.net.BaseConnection r3 = r7.mConnection     // Catch:{ Exception -> 0x06d5 }
            java.lang.String r3 = r3.getAppkey()     // Catch:{ Exception -> 0x06d5 }
            r0.putExtra(r1, r3)     // Catch:{ Exception -> 0x06d5 }
            java.lang.String r1 = "configTag"
            com.taobao.accs.net.BaseConnection r3 = r7.mConnection     // Catch:{ Exception -> 0x06d5 }
            java.lang.String r3 = r3.mConfigTag     // Catch:{ Exception -> 0x06d5 }
            r0.putExtra(r1, r3)     // Catch:{ Exception -> 0x06d5 }
            com.taobao.accs.ut.monitor.NetPerformanceMonitor r1 = new com.taobao.accs.ut.monitor.NetPerformanceMonitor     // Catch:{ Exception -> 0x06d5 }
            r1.<init>()     // Catch:{ Exception -> 0x06d5 }
            r3 = 4
            r1.setMsgType(r3)     // Catch:{ Exception -> 0x06d5 }
            r1.onReceiveData()     // Catch:{ Exception -> 0x06d5 }
            java.lang.String r3 = "monitor"
            r0.putExtra(r3, r1)     // Catch:{ Exception -> 0x06d5 }
            r7.putExtHeaderToIntent(r9, r0)     // Catch:{ Exception -> 0x06d5 }
            if (r12 == 0) goto L_0x05b1
            r10 = r41
            int r1 = (int) r10
            short r1 = (short) r1
            r3 = r36
            r4 = r43
            r7.putBusinessAckInfoToIntent(r0, r3, r4, r1)     // Catch:{ Exception -> 0x03a4 }
            goto L_0x05b7
        L_0x05b1:
            r3 = r36
            r10 = r41
            r4 = r43
        L_0x05b7:
            android.content.Context r1 = r7.mContext     // Catch:{ Exception -> 0x06d5 }
            com.taobao.accs.net.BaseConnection r15 = r7.mConnection     // Catch:{ Exception -> 0x06d5 }
            com.taobao.accs.data.MsgDistribute.distribMessage(r1, r15, r0)     // Catch:{ Exception -> 0x06d5 }
            com.taobao.accs.utl.UTMini r17 = com.taobao.accs.utl.UTMini.getInstance()     // Catch:{ Exception -> 0x06d5 }
            r18 = 66001(0x101d1, float:9.2487E-41)
            java.lang.String r19 = "MsgToBussPush"
            java.lang.String r20 = "commandId=101"
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x06d5 }
            r0.<init>()     // Catch:{ Exception -> 0x06d5 }
            java.lang.String r1 = "serviceId="
            r0.append(r1)     // Catch:{ Exception -> 0x06d5 }
            r0.append(r14)     // Catch:{ Exception -> 0x06d5 }
            java.lang.String r1 = " dataId="
            r0.append(r1)     // Catch:{ Exception -> 0x06d5 }
            r0.append(r6)     // Catch:{ Exception -> 0x06d5 }
            java.lang.String r21 = r0.toString()     // Catch:{ Exception -> 0x06d5 }
            int r0 = com.taobao.accs.common.Constants.SDK_VERSION_CODE     // Catch:{ Exception -> 0x06d5 }
            java.lang.Integer r22 = java.lang.Integer.valueOf(r0)     // Catch:{ Exception -> 0x06d5 }
            r17.commitEvent(r18, r19, r20, r21, r22)     // Catch:{ Exception -> 0x06d5 }
            java.lang.String r0 = "accs"
            java.lang.String r1 = "to_buss"
            java.lang.StringBuilder r15 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x06d5 }
            r15.<init>()     // Catch:{ Exception -> 0x06d5 }
            r44 = r3
            java.lang.String r3 = "1commandId=101serviceId="
            r15.append(r3)     // Catch:{ Exception -> 0x06d5 }
            r15.append(r14)     // Catch:{ Exception -> 0x06d5 }
            java.lang.String r3 = r15.toString()     // Catch:{ Exception -> 0x06d5 }
            r45 = r12
            r12 = 0
            com.taobao.accs.utl.AppMonitorAdapter.commitCount(r0, r1, r3, r12)     // Catch:{ Exception -> 0x06d5 }
            com.taobao.accs.ut.statistics.ReceiveMsgStat r0 = r7.mReceiveMsgStat     // Catch:{ Exception -> 0x06d5 }
            r0.dataId = r6     // Catch:{ Exception -> 0x06d5 }
            com.taobao.accs.ut.statistics.ReceiveMsgStat r0 = r7.mReceiveMsgStat     // Catch:{ Exception -> 0x06d5 }
            r0.userId = r2     // Catch:{ Exception -> 0x06d5 }
            com.taobao.accs.ut.statistics.ReceiveMsgStat r0 = r7.mReceiveMsgStat     // Catch:{ Exception -> 0x06d5 }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x06d5 }
            r1.<init>()     // Catch:{ Exception -> 0x06d5 }
            if (r5 != 0) goto L_0x061c
            r2 = 0
            goto L_0x061d
        L_0x061c:
            int r2 = r5.length     // Catch:{ Exception -> 0x06d5 }
        L_0x061d:
            r1.append(r2)     // Catch:{ Exception -> 0x06d5 }
            java.lang.String r2 = ""
            r1.append(r2)     // Catch:{ Exception -> 0x06d5 }
            java.lang.String r1 = r1.toString()     // Catch:{ Exception -> 0x06d5 }
            r0.dataLen = r1     // Catch:{ Exception -> 0x06d5 }
            com.taobao.accs.ut.statistics.ReceiveMsgStat r0 = r7.mReceiveMsgStat     // Catch:{ Exception -> 0x06d5 }
            android.content.Context r1 = r7.mContext     // Catch:{ Exception -> 0x06d5 }
            java.lang.String r1 = com.taobao.accs.utl.UtilityImpl.getDeviceId(r1)     // Catch:{ Exception -> 0x06d5 }
            r0.deviceId = r1     // Catch:{ Exception -> 0x06d5 }
            com.taobao.accs.ut.statistics.ReceiveMsgStat r0 = r7.mReceiveMsgStat     // Catch:{ Exception -> 0x06d5 }
            long r1 = java.lang.System.currentTimeMillis()     // Catch:{ Exception -> 0x06d5 }
            java.lang.String r1 = java.lang.String.valueOf(r1)     // Catch:{ Exception -> 0x06d5 }
            r0.toBzDate = r1     // Catch:{ Exception -> 0x06d5 }
            com.taobao.accs.ut.monitor.TrafficsMonitor$TrafficInfo r15 = new com.taobao.accs.ut.monitor.TrafficsMonitor$TrafficInfo     // Catch:{ Exception -> 0x06d5 }
            boolean r2 = anet.channel.GlobalAppRuntimeInfo.isAppBackground()     // Catch:{ Exception -> 0x06d5 }
            int r0 = r8.length     // Catch:{ Exception -> 0x06d5 }
            long r0 = (long) r0     // Catch:{ Exception -> 0x06d5 }
            r17 = r0
            r0 = r15
            r1 = r14
            r19 = r44
            r3 = r50
            r20 = r4
            r4 = r17
            r0.<init>(r1, r2, r3, r4)     // Catch:{ Exception -> 0x06d5 }
            r7.addTrafficsInfo(r15)     // Catch:{ Exception -> 0x06d5 }
            r0 = r33
            r1 = 1
        L_0x065e:
            if (r0 != r1) goto L_0x074f
            java.lang.String r0 = "accs"
            boolean r0 = r0.equals(r14)     // Catch:{ Exception -> 0x06d5 }
            if (r0 == 0) goto L_0x067d
            java.lang.String r0 = r7.TAG     // Catch:{ Exception -> 0x03a4 }
            java.lang.String r1 = "handleMessage try sendAck dataId"
            r2 = 2
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ Exception -> 0x03a4 }
            java.lang.String r3 = "dataId"
            r4 = 0
            r2[r4] = r3     // Catch:{ Exception -> 0x03a4 }
            r3 = 1
            r2[r3] = r6     // Catch:{ Exception -> 0x03a4 }
            com.taobao.accs.utl.ALog.e(r0, r1, r2)     // Catch:{ Exception -> 0x03a4 }
            r3 = 1
            r4 = 0
            goto L_0x068f
        L_0x067d:
            java.lang.String r0 = r7.TAG     // Catch:{ Exception -> 0x06d5 }
            java.lang.String r1 = "handleMessage try sendAck dataId"
            r2 = 2
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ Exception -> 0x06d5 }
            java.lang.String r3 = "dataId"
            r4 = 0
            r2[r4] = r3     // Catch:{ Exception -> 0x06d5 }
            r3 = 1
            r2[r3] = r6     // Catch:{ Exception -> 0x06d5 }
            com.taobao.accs.utl.ALog.i(r0, r1, r2)     // Catch:{ Exception -> 0x06d5 }
        L_0x068f:
            com.taobao.accs.net.BaseConnection r0 = r7.mConnection     // Catch:{ Exception -> 0x06d5 }
            r1 = 0
            java.lang.String r8 = r0.getHost(r1)     // Catch:{ Exception -> 0x06d0 }
            com.taobao.accs.net.BaseConnection r0 = r7.mConnection     // Catch:{ Exception -> 0x06d0 }
            java.lang.String r0 = r0.getTag()     // Catch:{ Exception -> 0x06d0 }
            r2 = 0
            int r5 = (int) r10
            short r5 = (short) r5
            r17 = r9
            r9 = r0
            r10 = r20
            r11 = r19
            r3 = r12
            r27 = r45
            r12 = r6
            r13 = r2
            r0 = r14
            r2 = 0
            r14 = r5
            r5 = 1
            r15 = r50
            r16 = r17
            com.taobao.accs.data.Message r8 = com.taobao.accs.data.Message.buildPushAck(r8, r9, r10, r11, r12, r13, r14, r15, r16)     // Catch:{ Exception -> 0x06ce }
            com.taobao.accs.net.BaseConnection r9 = r7.mConnection     // Catch:{ Exception -> 0x06ce }
            r9.send(r8, r5)     // Catch:{ Exception -> 0x06ce }
            java.lang.String r8 = r8.dataId     // Catch:{ Exception -> 0x06ce }
            r7.utStatSendAck(r8, r0)     // Catch:{ Exception -> 0x06ce }
            if (r27 == 0) goto L_0x074f
            java.lang.String r0 = "accs"
            java.lang.String r8 = "ack"
            java.lang.String r9 = ""
            com.taobao.accs.utl.AppMonitorAdapter.commitCount(r0, r8, r9, r3)     // Catch:{ Exception -> 0x06ce }
            goto L_0x074f
        L_0x06ce:
            r0 = move-exception
            goto L_0x06d3
        L_0x06d0:
            r0 = move-exception
            r2 = 0
            r5 = 1
        L_0x06d3:
            r3 = r1
            goto L_0x0714
        L_0x06d5:
            r0 = move-exception
            r2 = 0
            r5 = 1
            r3 = 0
            goto L_0x0714
        L_0x06da:
            r2 = 0
            r3 = 0
            r5 = 1
            java.lang.String r0 = r7.TAG     // Catch:{ Exception -> 0x0713 }
            java.lang.String r4 = "handleMessage not exist, unbind it"
            r8 = 2
            java.lang.Object[] r8 = new java.lang.Object[r8]     // Catch:{ Exception -> 0x0713 }
            java.lang.String r9 = "package"
            r8[r2] = r9     // Catch:{ Exception -> 0x0713 }
            r9 = r1[r5]     // Catch:{ Exception -> 0x0713 }
            r8[r5] = r9     // Catch:{ Exception -> 0x0713 }
            com.taobao.accs.utl.ALog.e(r0, r4, r8)     // Catch:{ Exception -> 0x0713 }
            com.taobao.accs.net.BaseConnection r0 = r7.mConnection     // Catch:{ Exception -> 0x0713 }
            java.lang.String r4 = ""
            com.taobao.accs.net.BaseConnection r8 = r7.mConnection     // Catch:{ Exception -> 0x0713 }
            java.lang.String r8 = r8.getHost(r3)     // Catch:{ Exception -> 0x0713 }
            r9 = 4
            com.taobao.accs.data.Message r4 = com.taobao.accs.data.Message.buildErrorReportMessage(r6, r4, r8, r9)     // Catch:{ Exception -> 0x0713 }
            r0.send(r4, r5)     // Catch:{ Exception -> 0x0713 }
            com.taobao.accs.net.BaseConnection r0 = r7.mConnection     // Catch:{ Exception -> 0x0713 }
            com.taobao.accs.net.BaseConnection r4 = r7.mConnection     // Catch:{ Exception -> 0x0713 }
            java.lang.String r4 = r4.getHost(r3)     // Catch:{ Exception -> 0x0713 }
            r1 = r1[r5]     // Catch:{ Exception -> 0x0713 }
            com.taobao.accs.data.Message r1 = com.taobao.accs.data.Message.buildUnbindApp((java.lang.String) r4, (java.lang.String) r1)     // Catch:{ Exception -> 0x0713 }
            r0.send(r1, r5)     // Catch:{ Exception -> 0x0713 }
            goto L_0x074f
        L_0x0713:
            r0 = move-exception
        L_0x0714:
            java.lang.String r1 = r7.TAG
            java.lang.String r4 = "handleMessage"
            java.lang.Object[] r2 = new java.lang.Object[r2]
            com.taobao.accs.utl.ALog.e(r1, r4, r0, r2)
            com.taobao.accs.net.BaseConnection r1 = r7.mConnection
            java.lang.String r2 = ""
            com.taobao.accs.net.BaseConnection r4 = r7.mConnection
            java.lang.String r3 = r4.getHost(r3)
            r4 = 5
            com.taobao.accs.data.Message r2 = com.taobao.accs.data.Message.buildErrorReportMessage(r6, r2, r3, r4)
            r1.send(r2, r5)
            java.lang.String r1 = "accs"
            java.lang.String r2 = "send_fail"
            java.lang.String r3 = ""
            java.lang.String r4 = "1"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            int r6 = r7.mConnectType
            r5.append(r6)
            java.lang.String r0 = r0.toString()
            r5.append(r0)
            java.lang.String r0 = r5.toString()
            com.taobao.accs.utl.AppMonitorAdapter.commitAlarmFail(r1, r2, r3, r4, r0)
        L_0x074f:
            return
        L_0x0750:
            r0 = move-exception
            r2 = 0
            java.lang.String r3 = r7.TAG
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "dataId read error "
            r4.append(r5)
            java.lang.String r5 = r0.toString()
            r4.append(r5)
            java.lang.String r4 = r4.toString()
            java.lang.Object[] r2 = new java.lang.Object[r2]
            com.taobao.accs.utl.ALog.e(r3, r4, r2)
            r1.close()
            java.lang.String r1 = "accs"
            java.lang.String r2 = "send_fail"
            java.lang.String r3 = ""
            java.lang.String r4 = "1"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            int r6 = r7.mConnectType
            r5.append(r6)
            java.lang.String r6 = "data id read error"
            r5.append(r6)
            java.lang.String r0 = r0.toString()
            r5.append(r0)
            java.lang.String r0 = r5.toString()
            com.taobao.accs.utl.AppMonitorAdapter.commitAlarmFail(r1, r2, r3, r4, r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.accs.data.MessageHandler.handleMessage(int, byte[], java.lang.String, int):void");
    }

    private byte[] gzipInputStream(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            return null;
        }
        GZIPInputStream gZIPInputStream = new GZIPInputStream(inputStream);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            byte[] bArr = new byte[8192];
            while (true) {
                int read = gZIPInputStream.read(bArr);
                if (read <= 0) {
                    break;
                }
                byteArrayOutputStream.write(bArr, 0, read);
            }
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            try {
                gZIPInputStream.close();
                byteArrayOutputStream.close();
            } catch (Exception unused) {
            }
            return byteArray;
        } catch (Exception e) {
            String str = this.TAG;
            ALog.e(str, "uncompress data error " + e.toString(), new Object[0]);
            AppMonitorAdapter.commitAlarmFail("accs", BaseMonitor.ALARM_POINT_REQ_ERROR, "", "1", this.mConnectType + " uncompress data error " + e.toString());
            try {
                gZIPInputStream.close();
                byteArrayOutputStream.close();
            } catch (Exception unused2) {
            }
            return null;
        } catch (Throwable th) {
            try {
                gZIPInputStream.close();
                byteArrayOutputStream.close();
            } catch (Exception unused3) {
            }
            throw th;
        }
    }

    private void handleControlMessage(Message message, byte[] bArr, byte[] bArr2, String str) {
        JSONArray jSONArray;
        Message message2 = message;
        int i = -8;
        try {
            try {
                JSONObject jSONObject = new JSONObject(new String(bArr));
                if (ALog.isPrintLog(ALog.Level.D)) {
                    ALog.d(this.TAG, "handleControlMessage parse", "json", jSONObject.toString());
                }
                if (message2.command.intValue() == 100) {
                    i = 200;
                } else {
                    i = jSONObject.getInt("code");
                }
                if (i == 200) {
                    int intValue = message2.command.intValue();
                    if (intValue != 100) {
                        switch (intValue) {
                            case 1:
                                UtilityImpl.saveUtdid(Constants.SP_FILE_NAME, this.mContext);
                                this.mConnection.getClientManager().onAppBind(this.mContext.getPackageName());
                                JSONObject jSONObject2 = jSONObject.getJSONObject("data");
                                this.mAccsDeviceToken = JsonUtility.getString(jSONObject2, Constants.KEY_DEVICE_TOKEN, (String) null);
                                if (!(jSONObject2 == null || (jSONArray = jSONObject2.getJSONArray(Constants.KEY_PACKAGE_NAMES)) == null)) {
                                    for (int i2 = 0; i2 < jSONArray.length(); i2++) {
                                        String string = jSONArray.getString(i2);
                                        if (UtilityImpl.packageExist(this.mContext, string)) {
                                            this.mConnection.getClientManager().onAppBind(message2.packageName);
                                        } else {
                                            ALog.d(this.TAG, "unbind app", "pkg", string);
                                            this.mConnection.send(Message.buildUnbindApp(this.mConnection.getHost((String) null), string), true);
                                        }
                                    }
                                    break;
                                }
                            case 2:
                                this.mConnection.getClientManager().onAppUnbind(message2.packageName);
                                break;
                            case 3:
                                this.mConnection.getClientManager().onUserBind(message2.packageName, message2.userinfo);
                                break;
                            case 4:
                                this.mConnection.getClientManager().onUserUnBind(message2.packageName, message2.userinfo);
                                break;
                        }
                    } else if ((this.mConnection instanceof InAppConnection) && !message2.target.equals(Constants.TARGET_SERVICE_ST) && !message2.target.equals(Constants.TARGET_FORE) && !message2.target.equals(Constants.TARGET_BACK)) {
                        ((InAppConnection) this.mConnection).onReceiveAccsHeartbeatResp(jSONObject);
                    }
                } else if (message2.command.intValue() == 3 && i == 300) {
                    this.mConnection.getClientManager().onAppUnbind(message2.packageName);
                }
            } catch (Throwable th) {
                th = th;
                ALog.e(this.TAG, "handleControlMessage", th, new Object[0]);
                AppMonitorAdapter.commitAlarmFail("accs", BaseMonitor.ALARM_POINT_REQ_ERROR, "handleControlMessage", "", this.mConnectType + th.toString());
            }
        } catch (Throwable th2) {
            th = th2;
            byte[] bArr3 = bArr;
            ALog.e(this.TAG, "handleControlMessage", th, new Object[0]);
            AppMonitorAdapter.commitAlarmFail("accs", BaseMonitor.ALARM_POINT_REQ_ERROR, "handleControlMessage", "", this.mConnectType + th.toString());
            onResult(message, i, (Message.ReqType) null, bArr, (Map<Integer, String>) null);
            addTrafficsInfo(new TrafficsMonitor.TrafficInfo(message2.serviceId, GlobalAppRuntimeInfo.isAppBackground(), str, (long) bArr2.length));
        }
        onResult(message, i, (Message.ReqType) null, bArr, (Map<Integer, String>) null);
        addTrafficsInfo(new TrafficsMonitor.TrafficInfo(message2.serviceId, GlobalAppRuntimeInfo.isAppBackground(), str, (long) bArr2.length));
    }

    private Map<Integer, String> parseExtHeader(MessageStreamReader messageStreamReader) {
        HashMap hashMap;
        if (messageStreamReader == null) {
            return null;
        }
        try {
            int readShort = messageStreamReader.readShort();
            if (ALog.isPrintLog(ALog.Level.D)) {
                ALog.d(this.TAG, "extHeaderLen:" + readShort, new Object[0]);
            }
            hashMap = null;
            int i = 0;
            while (i < readShort) {
                try {
                    int readShort2 = messageStreamReader.readShort();
                    int i2 = (64512 & readShort2) >> 10;
                    int i3 = readShort2 & Message.EXT_HEADER_VALUE_MAX_LEN;
                    String readString = messageStreamReader.readString(i3);
                    i = i + 2 + i3;
                    if (hashMap == null) {
                        hashMap = new HashMap();
                    }
                    hashMap.put(Integer.valueOf(i2), readString);
                    if (ALog.isPrintLog(ALog.Level.D)) {
                        ALog.d(this.TAG, "", "extHeaderType", Integer.valueOf(i2), "value", readString);
                    }
                } catch (Exception e) {
                    e = e;
                    ALog.e(this.TAG, "parseExtHeader", e, new Object[0]);
                    return hashMap;
                }
            }
        } catch (Exception e2) {
            e = e2;
            hashMap = null;
            ALog.e(this.TAG, "parseExtHeader", e, new Object[0]);
            return hashMap;
        }
        return hashMap;
    }

    public void onResult(Message message, int i) {
        onResult(message, i, (Message.ReqType) null, (byte[]) null, (Map<Integer, String>) null);
    }

    public void onResult(Message message, int i, Map<Integer, String> map) {
        onResult(message, i, (Message.ReqType) null, (byte[]) null, map);
    }

    public void onResult(Message message, int i, Message.ReqType reqType, byte[] bArr, Map<Integer, String> map) {
        byte[] bArr2;
        Message.ReqType reqType2;
        int i2;
        Message message2 = message;
        if (message2.command == null || message.getType() < 0 || message.getType() == 2) {
            ALog.d(this.TAG, "onError, skip ping/ack", new Object[0]);
            return;
        }
        if (message2.cunstomDataId != null) {
            this.reqTasks.remove(message2.cunstomDataId);
        }
        Map<Integer, String> map2 = map;
        String str = null;
        if (this.mAntiBrush.checkAntiBrush(message2.host, map2)) {
            i2 = 70022;
            map2 = null;
            reqType2 = null;
            bArr2 = null;
        } else {
            i2 = i;
            reqType2 = reqType;
            bArr2 = bArr;
        }
        int updateFlowCtrlInfo = this.mFlowControl.updateFlowCtrlInfo(map2, message2.serviceId);
        if (updateFlowCtrlInfo != 0) {
            i2 = updateFlowCtrlInfo == 2 ? 70021 : updateFlowCtrlInfo == 3 ? 70023 : 70020;
            map2 = null;
            reqType2 = null;
            bArr2 = null;
        }
        if (ALog.isPrintLog(ALog.Level.D)) {
            ALog.d(this.TAG, "onResult", "command", message2.command, "erorcode", Integer.valueOf(i2));
        }
        if (message2.command.intValue() != 102) {
            if (message2.isCancel) {
                ALog.e(this.TAG, "onResult message is cancel", "command", message2.command);
            } else if (!isNetWorkError(i2) || message2.command.intValue() == 100 || message2.retryTimes > Message.CONTROL_MAX_RETRY_TIMES) {
                Intent buildBaseReceiveIntent = buildBaseReceiveIntent(message);
                buildBaseReceiveIntent.putExtra("errorCode", i2);
                Message.ReqType valueOf = Message.ReqType.valueOf((message2.flags >> 13) & 3);
                if (reqType2 == Message.ReqType.RES || valueOf == Message.ReqType.REQ) {
                    buildBaseReceiveIntent.putExtra(Constants.KEY_SEND_TYPE, "res");
                }
                if (i2 == 200) {
                    buildBaseReceiveIntent.putExtra("data", bArr2);
                }
                buildBaseReceiveIntent.putExtra("appKey", this.mConnection.mAppkey);
                buildBaseReceiveIntent.putExtra(Constants.KEY_CONFIG_TAG, this.mConnection.mConfigTag);
                putExtHeaderToIntent(map2, buildBaseReceiveIntent);
                MsgDistribute.distribMessage(this.mContext, this.mConnection, buildBaseReceiveIntent);
                if (!TextUtils.isEmpty(message2.serviceId)) {
                    UTMini.getInstance().commitEvent(66001, "MsgToBuss0", "commandId=" + message2.command, "serviceId=" + message2.serviceId + " errorCode=" + i2 + " dataId=" + message2.dataId, Integer.valueOf(Constants.SDK_VERSION_CODE));
                    StringBuilder sb = new StringBuilder();
                    sb.append("1commandId=");
                    sb.append(message2.command);
                    sb.append("serviceId=");
                    sb.append(message2.serviceId);
                    AppMonitorAdapter.commitCount("accs", BaseMonitor.COUNT_POINT_TO_BUSS, sb.toString(), 0.0d);
                }
            } else {
                message2.startSendTime = System.currentTimeMillis();
                message2.retryTimes++;
                ALog.d(this.TAG, "onResult", "retryTimes", Integer.valueOf(message2.retryTimes));
                this.mConnection.send(message2, true);
            }
            NetPerformanceMonitor netPermanceMonitor = message.getNetPermanceMonitor();
            if (netPermanceMonitor != null) {
                netPermanceMonitor.onToBizDate();
                if (message2.host != null) {
                    str = message2.host.toString();
                }
                if (i2 == 200) {
                    netPermanceMonitor.setRet(true);
                    if (message2.retryTimes > 0) {
                        AppMonitorAdapter.commitCount("accs", BaseMonitor.COUNT_POINT_RESEND, "succ", 0.0d);
                        AppMonitorAdapter.commitCount("accs", BaseMonitor.COUNT_POINT_RESEND, "succ_" + message2.retryTimes, 0.0d);
                    } else {
                        AppMonitorAdapter.commitAlarmSuccess("accs", BaseMonitor.ALARM_POINT_REQUEST, str);
                    }
                } else {
                    if (message2.retryTimes > 0) {
                        AppMonitorAdapter.commitCount("accs", BaseMonitor.COUNT_POINT_RESEND, "fail＿" + i2, 0.0d);
                        AppMonitorAdapter.commitCount("accs", BaseMonitor.COUNT_POINT_RESEND, Constants.Event.FAIL, 0.0d);
                    } else if (i2 != -13) {
                        AppMonitorAdapter.commitAlarmFail("accs", BaseMonitor.ALARM_POINT_REQUEST, str, UtilityImpl.int2String(i2), this.mConnectType + message2.serviceId + message2.timeout);
                    }
                    netPermanceMonitor.setRet(false);
                    netPermanceMonitor.setFailReason(i2);
                }
                AppMonitor.getInstance().commitStat(message.getNetPermanceMonitor());
            }
            utStat(message2, i2);
        }
    }

    public void onSendPing() {
        ALog.d(this.TAG, "onSendPing", new Object[0]);
        synchronized (MessageHandler.class) {
            this.unRevPing = true;
        }
    }

    public void onRcvPing() {
        ALog.d(this.TAG, "onRcvPing", new Object[0]);
        synchronized (MessageHandler.class) {
            this.unRevPing = false;
        }
    }

    public boolean getUnrcvPing() {
        return this.unRevPing;
    }

    public void onSend(Message message) {
        if (!(this.mLastSendMessage == null || message.cunstomDataId == null || message.serviceId == null || !this.mLastSendMessage.cunstomDataId.equals(message.cunstomDataId) || !this.mLastSendMessage.serviceId.equals(message.serviceId))) {
            UTMini.getInstance().commitEvent(66001, "SEND_REPEAT", message.serviceId, message.cunstomDataId, Long.valueOf(Thread.currentThread().getId()));
        }
        if (message.getType() != -1 && message.getType() != 2 && !message.isAck) {
            this.unHandleMessage.put(message.getMsgId(), message);
        }
    }

    public void onNetworkFail(int i) {
        this.unRevPing = false;
        Message.Id[] idArr = (Message.Id[]) this.unHandleMessage.keySet().toArray(new Message.Id[0]);
        if (idArr.length > 0) {
            ALog.d(this.TAG, "onNetworkFail", new Object[0]);
            for (Message.Id remove : idArr) {
                Message message = (Message) this.unHandleMessage.remove(remove);
                if (message != null) {
                    onResult(message, i);
                }
            }
        }
    }

    public void cancelControlMessage(Message message) {
        if (this.unHandleMessage.keySet().size() > 0) {
            for (Message.Id id : this.unHandleMessage.keySet()) {
                Message message2 = (Message) this.unHandleMessage.get(id);
                if (!(message2 == null || message2.command == null || !message2.getPackageName().equals(message.getPackageName()))) {
                    switch (message.command.intValue()) {
                        case 1:
                        case 2:
                            if (message2.command.intValue() == 1 || message2.command.intValue() == 2) {
                                message2.isCancel = true;
                                break;
                            }
                        case 3:
                        case 4:
                            if (message2.command.intValue() == 3 || message2.command.intValue() == 4) {
                                message2.isCancel = true;
                                break;
                            }
                        case 5:
                        case 6:
                            if (message2.command.intValue() == 5 || message2.command.intValue() == 6) {
                                message2.isCancel = true;
                                break;
                            }
                    }
                }
                if (message2 != null && message2.isCancel) {
                    ALog.e(this.TAG, "cancelControlMessage", "command", message2.command);
                }
            }
        }
    }

    public int getUnhandledCount() {
        return this.unHandleMessage.size();
    }

    public Collection<Message> getUnhandledMessages() {
        return this.unHandleMessage.values();
    }

    public Set<Message.Id> getUnhandledMessageIds() {
        return this.unHandleMessage.keySet();
    }

    public Message getUnhandledMessage(String str) {
        return (Message) this.unHandleMessage.get(new Message.Id(0, str));
    }

    public Message removeUnhandledMessage(String str) {
        if (!TextUtils.isEmpty(str)) {
            return (Message) this.unHandleMessage.remove(new Message.Id(0, str));
        }
        return null;
    }

    private boolean isDuplicateMessage(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return this.handledMessageId.containsKey(str);
    }

    private byte[] putBurstMessage(String str, Map<Integer, String> map, byte[] bArr) {
        if (bArr != null) {
            try {
                if (bArr.length != 0) {
                    int parseInt = Integer.parseInt(map.get(17));
                    int parseInt2 = Integer.parseInt(map.get(16));
                    if (parseInt2 <= 1) {
                        throw new RuntimeException("burstNums <= 1");
                    } else if (parseInt < 0 || parseInt >= parseInt2) {
                        throw new RuntimeException(String.format("burstNums:%s burstIndex:%s", new Object[]{Integer.valueOf(parseInt2), Integer.valueOf(parseInt)}));
                    } else {
                        String str2 = map.get(18);
                        long j = 0;
                        String str3 = map.get(15);
                        if (!TextUtils.isEmpty(str3)) {
                            j = Long.parseLong(str3);
                        }
                        AssembleMessage assembleMessage = this.assembleMessageMap.get(str);
                        if (assembleMessage == null) {
                            if (ALog.isPrintLog(ALog.Level.I)) {
                                ALog.i(this.TAG, "putBurstMessage", com.taobao.accs.common.Constants.KEY_DATA_ID, str, "burstLength", Integer.valueOf(parseInt2));
                            }
                            assembleMessage = new AssembleMessage(str, parseInt2, str2);
                            assembleMessage.setTimeOut(j);
                            this.assembleMessageMap.put(str, assembleMessage);
                        }
                        return assembleMessage.putBurst(parseInt, parseInt2, bArr);
                    }
                }
            } catch (Throwable th) {
                ALog.w(this.TAG, "putBurstMessage", th, new Object[0]);
                return null;
            }
        }
        throw new RuntimeException("burstLength == 0");
    }

    private void recordMessageId(String str) {
        if (!TextUtils.isEmpty(str) && !this.handledMessageId.containsKey(str)) {
            this.handledMessageId.put(str, str);
            saveMessageId();
        }
    }

    private void restoreMessageId() {
        try {
            File dir = this.mContext.getDir("accs", 0);
            File file = new File(dir, "message" + this.mConnection.getAppkey());
            if (!file.exists()) {
                ALog.d(this.TAG, "message file not exist", new Object[0]);
                return;
            }
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine != null) {
                    this.handledMessageId.put(readLine, readLine);
                } else {
                    bufferedReader.close();
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveMessageId() {
        try {
            File dir = this.mContext.getDir("accs", 0);
            FileWriter fileWriter = new FileWriter(new File(dir, "message" + this.mConnection.getAppkey()));
            fileWriter.write("");
            for (String append : this.handledMessageId.keySet()) {
                fileWriter.append(append).append("\r\n");
            }
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Intent buildBaseReceiveIntent(Message message) {
        Intent intent = new Intent(com.taobao.accs.common.Constants.ACTION_RECEIVE);
        intent.setPackage(message.packageName);
        intent.putExtra("command", message.command);
        intent.putExtra("serviceId", message.serviceId);
        intent.putExtra("userInfo", message.userinfo);
        if (message.command != null && message.command.intValue() == 100) {
            intent.putExtra(com.taobao.accs.common.Constants.KEY_DATA_ID, message.cunstomDataId);
        }
        return intent;
    }

    private void putExtHeaderToIntent(Map<Integer, String> map, Intent intent) {
        if (map != null && intent != null) {
            intent.putExtra(TaoBaseService.ExtraInfo.EXT_HEADER, (HashMap) map);
        }
    }

    private void putBusinessAckInfoToIntent(Intent intent, String str, String str2, short s) {
        if (intent != null) {
            if (!TextUtils.isEmpty(str)) {
                intent.putExtra("source", str);
            }
            if (!TextUtils.isEmpty(str2)) {
                intent.putExtra(com.taobao.accs.common.Constants.KEY_TARGET, str2);
            }
            intent.putExtra(com.taobao.accs.common.Constants.KEY_FLAGS, s);
        }
    }

    public void setReceiveMsgStat(ReceiveMsgStat receiveMsgStat) {
        this.mReceiveMsgStat = receiveMsgStat;
    }

    public ReceiveMsgStat getReceiveMsgStat() {
        return this.mReceiveMsgStat;
    }

    private void utStat(Message message, int i) {
        if (message != null) {
            String deviceId = UtilityImpl.getDeviceId(this.mContext);
            String str = System.currentTimeMillis() + "";
            boolean z = i == 200;
            int intValue = message.command.intValue();
            if (intValue == 1) {
                BindAppStatistic bindAppStatistic = new BindAppStatistic();
                bindAppStatistic.deviceId = deviceId;
                bindAppStatistic.time = str;
                bindAppStatistic.ret = z;
                bindAppStatistic.setFailReason(i);
                bindAppStatistic.commitUT();
            } else if (intValue == 3) {
                BindUserStatistic bindUserStatistic = new BindUserStatistic();
                bindUserStatistic.deviceId = deviceId;
                bindUserStatistic.time = str;
                bindUserStatistic.ret = z;
                bindUserStatistic.userId = message.userinfo;
                bindUserStatistic.setFailReason(i);
                bindUserStatistic.commitUT();
            }
        }
    }

    private void utStatSendAck(String str, String str2) {
        SendAckStatistic sendAckStatistic = new SendAckStatistic();
        sendAckStatistic.deviceId = UtilityImpl.getDeviceId(this.mContext);
        sendAckStatistic.dataId = str;
        sendAckStatistic.sendTime = "" + System.currentTimeMillis();
        sendAckStatistic.failReason = "";
        sendAckStatistic.serviceId = str2;
        sendAckStatistic.sessionId = "";
        sendAckStatistic.commitUT();
    }

    public void restoreTraffics() {
        try {
            ThreadPoolExecutorFactory.getScheduledExecutor().execute(this.mRestoreTrafficsRunnable);
        } catch (Throwable th) {
            ALog.e(this.TAG, "restoreTraffics", th, new Object[0]);
        }
    }

    public void addTrafficsInfo(final TrafficsMonitor.TrafficInfo trafficInfo) {
        try {
            ThreadPoolExecutorFactory.getScheduledExecutor().execute(new Runnable() {
                public void run() {
                    if (MessageHandler.this.mTrafficMonitor != null) {
                        MessageHandler.this.mTrafficMonitor.addTrafficInfo(trafficInfo);
                    }
                }
            });
        } catch (Throwable th) {
            ALog.e(this.TAG, "addTrafficsInfo", th, new Object[0]);
        }
    }
}
