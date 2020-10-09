package anetwork.channel.entity;

import android.os.RemoteException;
import anet.channel.bytes.ByteArray;
import anet.channel.statist.RequestStatistic;
import anet.channel.util.ALog;
import anetwork.channel.aidl.DefaultFinishEvent;
import anetwork.channel.aidl.DefaultProgressEvent;
import anetwork.channel.aidl.ParcelableHeader;
import anetwork.channel.aidl.ParcelableNetworkListener;
import anetwork.channel.aidl.adapter.ParcelableInputStreamImpl;
import anetwork.channel.interceptor.Callback;
import java.util.List;
import java.util.Map;

public class Repeater implements Callback {
    private static final String TAG = "anet.Repeater";
    /* access modifiers changed from: private */
    public boolean bInputStreamListener = false;
    /* access modifiers changed from: private */
    public RequestConfig config = null;
    /* access modifiers changed from: private */
    public ParcelableInputStreamImpl inputStream = null;
    private ParcelableNetworkListener mListenerWrapper;
    /* access modifiers changed from: private */
    public String seqNo;

    public Repeater(ParcelableNetworkListener parcelableNetworkListener, RequestConfig requestConfig) {
        this.mListenerWrapper = parcelableNetworkListener;
        this.config = requestConfig;
        if (parcelableNetworkListener != null) {
            try {
                if ((parcelableNetworkListener.getListenerState() & 8) != 0) {
                    this.bInputStreamListener = true;
                }
            } catch (RemoteException unused) {
            }
        }
    }

    public void onResponseCode(final int i, final Map<String, List<String>> map) {
        if (ALog.isPrintLog(2)) {
            ALog.i(TAG, "[onResponseCode]", this.seqNo, new Object[0]);
        }
        if (this.mListenerWrapper != null) {
            final ParcelableNetworkListener parcelableNetworkListener = this.mListenerWrapper;
            dispatchCallBack(new Runnable() {
                public void run() {
                    try {
                        parcelableNetworkListener.onResponseCode(i, new ParcelableHeader(i, map));
                    } catch (RemoteException unused) {
                    }
                }
            });
        }
    }

    public void onDataReceiveSize(int i, int i2, ByteArray byteArray) {
        if (this.mListenerWrapper != null) {
            final ParcelableNetworkListener parcelableNetworkListener = this.mListenerWrapper;
            final int i3 = i;
            final ByteArray byteArray2 = byteArray;
            final int i4 = i2;
            dispatchCallBack(new Runnable() {
                public void run() {
                    if (!Repeater.this.bInputStreamListener) {
                        try {
                            parcelableNetworkListener.onDataReceived(new DefaultProgressEvent(i3, byteArray2.getDataLength(), i4, byteArray2.getBuffer()));
                        } catch (RemoteException unused) {
                        }
                    } else {
                        try {
                            if (Repeater.this.inputStream == null) {
                                ParcelableInputStreamImpl unused2 = Repeater.this.inputStream = new ParcelableInputStreamImpl();
                                Repeater.this.inputStream.init(Repeater.this.config, i4);
                                Repeater.this.inputStream.write(byteArray2);
                                parcelableNetworkListener.onInputStreamGet(Repeater.this.inputStream);
                                return;
                            }
                            Repeater.this.inputStream.write(byteArray2);
                        } catch (Exception unused3) {
                            if (Repeater.this.inputStream != null) {
                                Repeater.this.inputStream.close();
                            }
                        }
                    }
                }
            });
        }
    }

    public void onFinish(final DefaultFinishEvent defaultFinishEvent) {
        if (ALog.isPrintLog(2)) {
            ALog.i(TAG, "[onFinish] ", this.seqNo, new Object[0]);
        }
        if (this.mListenerWrapper != null) {
            final ParcelableNetworkListener parcelableNetworkListener = this.mListenerWrapper;
            AnonymousClass3 r1 = new Runnable() {
                /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
                /* JADX WARNING: Missing exception handler attribute for start block: B:39:0x013a */
                /* Code decompiled incorrectly, please refer to instructions dump. */
                public void run() {
                    /*
                        r9 = this;
                        anetwork.channel.aidl.DefaultFinishEvent r0 = r5
                        r1 = 0
                        if (r0 == 0) goto L_0x000a
                        anetwork.channel.aidl.DefaultFinishEvent r0 = r5
                        r0.setContext(r1)
                    L_0x000a:
                        long r2 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x0154 }
                        anetwork.channel.aidl.DefaultFinishEvent r0 = r5     // Catch:{ Throwable -> 0x0154 }
                        anet.channel.statist.RequestStatistic r0 = r0.rs     // Catch:{ Throwable -> 0x0154 }
                        if (r0 == 0) goto L_0x0030
                        r0.rspCbStart = r2     // Catch:{ Throwable -> 0x0154 }
                        long r4 = r0.rspEnd     // Catch:{ Throwable -> 0x0154 }
                        r6 = 0
                        long r4 = r2 - r4
                        r0.lastProcessTime = r4     // Catch:{ Throwable -> 0x0154 }
                        long r4 = r0.retryCostTime     // Catch:{ Throwable -> 0x0154 }
                        long r6 = r0.start     // Catch:{ Throwable -> 0x0154 }
                        r8 = 0
                        long r6 = r2 - r6
                        long r4 = r4 + r6
                        r0.oneWayTime = r4     // Catch:{ Throwable -> 0x0154 }
                        anetwork.channel.aidl.DefaultFinishEvent r4 = r5     // Catch:{ Throwable -> 0x0154 }
                        anetwork.channel.statist.StatisticData r4 = r4.getStatisticData()     // Catch:{ Throwable -> 0x0154 }
                        r4.filledBy(r0)     // Catch:{ Throwable -> 0x0154 }
                    L_0x0030:
                        anetwork.channel.aidl.ParcelableNetworkListener r4 = r0     // Catch:{ Throwable -> 0x0154 }
                        anetwork.channel.aidl.DefaultFinishEvent r5 = r5     // Catch:{ Throwable -> 0x0154 }
                        r4.onFinished(r5)     // Catch:{ Throwable -> 0x0154 }
                        if (r0 == 0) goto L_0x004c
                        long r4 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x0154 }
                        r0.rspCbEnd = r4     // Catch:{ Throwable -> 0x0154 }
                        r6 = 0
                        long r4 = r4 - r2
                        r0.callbackTime = r4     // Catch:{ Throwable -> 0x0154 }
                        anet.channel.fulltrace.IFullTraceAnalysis r2 = anet.channel.fulltrace.AnalysisFactory.getInstance()     // Catch:{ Throwable -> 0x0154 }
                        java.lang.String r3 = r0.traceId     // Catch:{ Throwable -> 0x0154 }
                        r2.commitRequest(r3, r0)     // Catch:{ Throwable -> 0x0154 }
                    L_0x004c:
                        anetwork.channel.entity.Repeater r2 = anetwork.channel.entity.Repeater.this     // Catch:{ Throwable -> 0x0154 }
                        anetwork.channel.aidl.adapter.ParcelableInputStreamImpl r2 = r2.inputStream     // Catch:{ Throwable -> 0x0154 }
                        if (r2 == 0) goto L_0x005d
                        anetwork.channel.entity.Repeater r2 = anetwork.channel.entity.Repeater.this     // Catch:{ Throwable -> 0x0154 }
                        anetwork.channel.aidl.adapter.ParcelableInputStreamImpl r2 = r2.inputStream     // Catch:{ Throwable -> 0x0154 }
                        r2.writeEnd()     // Catch:{ Throwable -> 0x0154 }
                    L_0x005d:
                        if (r0 == 0) goto L_0x0154
                        java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0154 }
                        r2.<init>()     // Catch:{ Throwable -> 0x0154 }
                        java.lang.String r3 = "[traceId:"
                        r2.append(r3)     // Catch:{ Throwable -> 0x0154 }
                        java.lang.String r3 = r0.traceId     // Catch:{ Throwable -> 0x0154 }
                        r2.append(r3)     // Catch:{ Throwable -> 0x0154 }
                        java.lang.String r3 = "]"
                        r2.append(r3)     // Catch:{ Throwable -> 0x0154 }
                        java.lang.String r3 = "end, "
                        r2.append(r3)     // Catch:{ Throwable -> 0x0154 }
                        java.lang.String r3 = r0.toString()     // Catch:{ Throwable -> 0x0154 }
                        r2.append(r3)     // Catch:{ Throwable -> 0x0154 }
                        java.lang.String r2 = r2.toString()     // Catch:{ Throwable -> 0x0154 }
                        java.lang.String r3 = "anet.Repeater"
                        anetwork.channel.entity.Repeater r4 = anetwork.channel.entity.Repeater.this     // Catch:{ Throwable -> 0x0154 }
                        java.lang.String r4 = r4.seqNo     // Catch:{ Throwable -> 0x0154 }
                        r5 = 0
                        java.lang.Object[] r6 = new java.lang.Object[r5]     // Catch:{ Throwable -> 0x0154 }
                        anet.channel.util.ALog.e(r3, r2, r4, r6)     // Catch:{ Throwable -> 0x0154 }
                        java.util.concurrent.CopyOnWriteArrayList r2 = anet.channel.GlobalAppRuntimeInfo.getBucketInfo()     // Catch:{ Throwable -> 0x0154 }
                        r3 = 1
                        if (r2 == 0) goto L_0x00b3
                        int r4 = r2.size()     // Catch:{ Throwable -> 0x0154 }
                        r6 = 0
                    L_0x009d:
                        int r7 = r4 + -1
                        if (r6 >= r7) goto L_0x00b3
                        java.lang.Object r7 = r2.get(r6)     // Catch:{ Throwable -> 0x0154 }
                        java.lang.String r7 = (java.lang.String) r7     // Catch:{ Throwable -> 0x0154 }
                        int r8 = r6 + 1
                        java.lang.Object r8 = r2.get(r8)     // Catch:{ Throwable -> 0x0154 }
                        r0.putExtra(r7, r8)     // Catch:{ Throwable -> 0x0154 }
                        int r6 = r6 + 2
                        goto L_0x009d
                    L_0x00b3:
                        anet.channel.fulltrace.IFullTraceAnalysis r2 = anet.channel.fulltrace.AnalysisFactory.getInstance()     // Catch:{ Throwable -> 0x0154 }
                        anet.channel.fulltrace.SceneInfo r2 = r2.getSceneInfo()     // Catch:{ Throwable -> 0x0154 }
                        if (r2 == 0) goto L_0x00f2
                        java.lang.String r4 = "anet.Repeater"
                        java.lang.String r6 = r2.toString()     // Catch:{ Throwable -> 0x0154 }
                        anetwork.channel.entity.Repeater r7 = anetwork.channel.entity.Repeater.this     // Catch:{ Throwable -> 0x0154 }
                        java.lang.String r7 = r7.seqNo     // Catch:{ Throwable -> 0x0154 }
                        java.lang.Object[] r5 = new java.lang.Object[r5]     // Catch:{ Throwable -> 0x0154 }
                        anet.channel.util.ALog.i(r4, r6, r7, r5)     // Catch:{ Throwable -> 0x0154 }
                        long r4 = r0.start     // Catch:{ Throwable -> 0x0154 }
                        long r6 = r2.appLaunchTime     // Catch:{ Throwable -> 0x0154 }
                        r8 = 0
                        long r4 = r4 - r6
                        r0.sinceInitTime = r4     // Catch:{ Throwable -> 0x0154 }
                        int r4 = r2.startType     // Catch:{ Throwable -> 0x0154 }
                        r0.startType = r4     // Catch:{ Throwable -> 0x0154 }
                        int r4 = r2.startType     // Catch:{ Throwable -> 0x0154 }
                        if (r4 == r3) goto L_0x00e6
                        long r3 = r2.appLaunchTime     // Catch:{ Throwable -> 0x0154 }
                        long r5 = r2.lastLaunchTime     // Catch:{ Throwable -> 0x0154 }
                        r7 = 0
                        long r3 = r3 - r5
                        r0.sinceLastLaunchTime = r3     // Catch:{ Throwable -> 0x0154 }
                    L_0x00e6:
                        int r3 = r2.deviceLevel     // Catch:{ Throwable -> 0x0154 }
                        r0.deviceLevel = r3     // Catch:{ Throwable -> 0x0154 }
                        boolean r3 = r2.isUrlLaunch     // Catch:{ Throwable -> 0x0154 }
                        r0.isFromExternal = r3     // Catch:{ Throwable -> 0x0154 }
                        java.lang.String r2 = r2.speedBucket     // Catch:{ Throwable -> 0x0154 }
                        r0.speedBucket = r2     // Catch:{ Throwable -> 0x0154 }
                    L_0x00f2:
                        long r2 = r0.reqServiceTransmissionEnd     // Catch:{ Throwable -> 0x0154 }
                        long r4 = r0.netReqStart     // Catch:{ Throwable -> 0x0154 }
                        r6 = 0
                        long r2 = r2 - r4
                        r0.serializeTransferTime = r2     // Catch:{ Throwable -> 0x0154 }
                        anet.channel.appmonitor.IAppMonitor r2 = anet.channel.appmonitor.AppMonitor.getInstance()     // Catch:{ Throwable -> 0x0154 }
                        r2.commitStat(r0)     // Catch:{ Throwable -> 0x0154 }
                        boolean r2 = anetwork.channel.config.NetworkConfigCenter.isRequestInMonitorList(r0)     // Catch:{ Throwable -> 0x0154 }
                        if (r2 == 0) goto L_0x0113
                        anet.channel.statist.RequestMonitor r2 = new anet.channel.statist.RequestMonitor     // Catch:{ Throwable -> 0x0154 }
                        r2.<init>(r0)     // Catch:{ Throwable -> 0x0154 }
                        anet.channel.appmonitor.IAppMonitor r3 = anet.channel.appmonitor.AppMonitor.getInstance()     // Catch:{ Throwable -> 0x0154 }
                        r3.commitStat(r2)     // Catch:{ Throwable -> 0x0154 }
                    L_0x0113:
                        java.lang.String r2 = r0.ip     // Catch:{ Exception -> 0x013a }
                        org.json.JSONObject r3 = r0.extra     // Catch:{ Exception -> 0x013a }
                        if (r3 != 0) goto L_0x011a
                        goto L_0x0122
                    L_0x011a:
                        org.json.JSONObject r1 = r0.extra     // Catch:{ Exception -> 0x013a }
                        java.lang.String r3 = "firstIp"
                        java.lang.String r1 = r1.optString(r3)     // Catch:{ Exception -> 0x013a }
                    L_0x0122:
                        boolean r2 = anet.channel.strategy.utils.Utils.isIPV6Address(r2)     // Catch:{ Exception -> 0x013a }
                        if (r2 != 0) goto L_0x012e
                        boolean r1 = anet.channel.strategy.utils.Utils.isIPV6Address(r1)     // Catch:{ Exception -> 0x013a }
                        if (r1 == 0) goto L_0x013a
                    L_0x012e:
                        anet.channel.statist.RequestMonitor r1 = new anet.channel.statist.RequestMonitor     // Catch:{ Exception -> 0x013a }
                        r1.<init>(r0)     // Catch:{ Exception -> 0x013a }
                        anet.channel.appmonitor.IAppMonitor r2 = anet.channel.appmonitor.AppMonitor.getInstance()     // Catch:{ Exception -> 0x013a }
                        r2.commitStat(r1)     // Catch:{ Exception -> 0x013a }
                    L_0x013a:
                        anetwork.channel.stat.INetworkStat r1 = anetwork.channel.stat.NetworkStat.getNetworkStat()     // Catch:{ Throwable -> 0x0154 }
                        anetwork.channel.entity.Repeater r2 = anetwork.channel.entity.Repeater.this     // Catch:{ Throwable -> 0x0154 }
                        anetwork.channel.entity.RequestConfig r2 = r2.config     // Catch:{ Throwable -> 0x0154 }
                        java.lang.String r2 = r2.getUrlString()     // Catch:{ Throwable -> 0x0154 }
                        anetwork.channel.aidl.DefaultFinishEvent r3 = r5     // Catch:{ Throwable -> 0x0154 }
                        anetwork.channel.statist.StatisticData r3 = r3.getStatisticData()     // Catch:{ Throwable -> 0x0154 }
                        r1.put(r2, r3)     // Catch:{ Throwable -> 0x0154 }
                        anet.channel.detect.NetworkDetector.commitDetect(r0)     // Catch:{ Throwable -> 0x0154 }
                    L_0x0154:
                        return
                    */
                    throw new UnsupportedOperationException("Method not decompiled: anetwork.channel.entity.Repeater.AnonymousClass3.run():void");
                }
            };
            RequestStatistic requestStatistic = defaultFinishEvent.rs;
            if (requestStatistic != null) {
                requestStatistic.rspCbDispatch = System.currentTimeMillis();
            }
            dispatchCallBack(r1);
        }
        this.mListenerWrapper = null;
    }

    private void dispatchCallBack(Runnable runnable) {
        if (this.config.isSyncRequest()) {
            runnable.run();
        } else {
            RepeatProcessor.submitTask(this.seqNo != null ? this.seqNo.hashCode() : hashCode(), runnable);
        }
    }

    public void setSeqNo(String str) {
        this.seqNo = str;
    }
}
