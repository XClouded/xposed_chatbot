package com.taobao.tao.remotebusiness.handler;

import android.os.Handler;
import android.os.Looper;
import com.taobao.tao.remotebusiness.MtopBusiness;
import mtopsdk.mtop.common.MtopEvent;
import mtopsdk.mtop.common.MtopListener;

public class HandlerMgr extends Handler {
    public static final int ON_CACHED = 4;
    public static final int ON_DATA_RECEIVED = 1;
    public static final int ON_FINISHED = 3;
    public static final int ON_HEADER = 2;
    private static final String TAG = "mtopsdk.HandlerMgr";
    private static volatile Handler mHandler;

    private HandlerMgr(Looper looper) {
        super(looper);
    }

    public static Handler instance() {
        if (mHandler == null) {
            synchronized (HandlerMgr.class) {
                if (mHandler == null) {
                    mHandler = new HandlerMgr(Looper.getMainLooper());
                }
            }
        }
        return mHandler;
    }

    /* JADX WARNING: Removed duplicated region for block: B:49:0x0116  */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x0145  */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x0171  */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x01ab  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void handleMessage(android.os.Message r15) {
        /*
            r14 = this;
            java.lang.Object r0 = r15.obj
            com.taobao.tao.remotebusiness.handler.HandlerParam r0 = (com.taobao.tao.remotebusiness.handler.HandlerParam) r0
            java.lang.String r1 = ""
            if (r0 != 0) goto L_0x0010
            java.lang.String r15 = "mtopsdk.HandlerMgr"
            java.lang.String r0 = "HandlerMsg is null."
            mtopsdk.common.util.TBSdkLog.e((java.lang.String) r15, (java.lang.String) r1, (java.lang.String) r0)
            return
        L_0x0010:
            com.taobao.tao.remotebusiness.MtopBusiness r2 = r0.mtopBusiness
            if (r2 == 0) goto L_0x002a
            com.taobao.tao.remotebusiness.MtopBusiness r1 = r0.mtopBusiness
            java.lang.String r1 = r1.getSeqNo()
            com.taobao.tao.remotebusiness.MtopBusiness r2 = r0.mtopBusiness
            boolean r2 = r2.isTaskCanceled()
            if (r2 == 0) goto L_0x002a
            java.lang.String r15 = "mtopsdk.HandlerMgr"
            java.lang.String r0 = "The request of MtopBusiness is cancelled."
            mtopsdk.common.util.TBSdkLog.i((java.lang.String) r15, (java.lang.String) r1, (java.lang.String) r0)
            return
        L_0x002a:
            com.taobao.tao.remotebusiness.MtopBusiness r2 = r0.mtopBusiness
            java.lang.Object r2 = r2.getReqContext()
            int r3 = r15.what
            r4 = 1
            r5 = 0
            switch(r3) {
                case 1: goto L_0x01d6;
                case 2: goto L_0x01b2;
                case 3: goto L_0x00cb;
                case 4: goto L_0x0039;
                default: goto L_0x0037;
            }
        L_0x0037:
            goto L_0x01f9
        L_0x0039:
            mtopsdk.common.util.TBSdkLog$LogEnable r3 = mtopsdk.common.util.TBSdkLog.LogEnable.InfoEnable
            boolean r3 = mtopsdk.common.util.TBSdkLog.isLogEnable(r3)
            if (r3 == 0) goto L_0x0048
            java.lang.String r3 = "mtopsdk.HandlerMgr"
            java.lang.String r6 = "onReceive: ON_CACHED"
            mtopsdk.common.util.TBSdkLog.i((java.lang.String) r3, (java.lang.String) r1, (java.lang.String) r6)
        L_0x0048:
            mtopsdk.mtop.common.MtopEvent r3 = r0.event
            mtopsdk.mtop.common.MtopCacheEvent r3 = (mtopsdk.mtop.common.MtopCacheEvent) r3
            if (r3 != 0) goto L_0x0056
            java.lang.String r15 = "mtopsdk.HandlerMgr"
            java.lang.String r0 = "HandlerMsg.event is null."
            mtopsdk.common.util.TBSdkLog.e((java.lang.String) r15, (java.lang.String) r1, (java.lang.String) r0)
            return
        L_0x0056:
            mtopsdk.mtop.domain.MtopResponse r6 = r3.getMtopResponse()
            if (r6 == 0) goto L_0x0089
            mtopsdk.mtop.domain.MtopResponse r6 = r3.getMtopResponse()
            mtopsdk.mtop.util.MtopStatistics r6 = r6.getMtopStat()
            if (r6 == 0) goto L_0x0089
            mtopsdk.mtop.util.MtopStatistics$RbStatisticData r7 = r6.getRbStatData()
            long r8 = java.lang.System.currentTimeMillis()
            com.taobao.tao.remotebusiness.MtopBusiness r10 = r0.mtopBusiness
            long r10 = r10.onBgFinishTime
            long r8 = r8 - r10
            r7.toMainThTime = r8
            mtopsdk.common.util.TBSdkLog$LogEnable r8 = mtopsdk.common.util.TBSdkLog.LogEnable.DebugEnable
            boolean r8 = mtopsdk.common.util.TBSdkLog.isLogEnable(r8)
            if (r8 == 0) goto L_0x0086
            java.lang.String r8 = "mtopsdk.HandlerMgr"
            java.lang.String r7 = r7.toString()
            mtopsdk.common.util.TBSdkLog.d((java.lang.String) r8, (java.lang.String) r1, (java.lang.String) r7)
        L_0x0086:
            r6.commitStatData(r4)
        L_0x0089:
            mtopsdk.mtop.common.MtopListener r4 = r0.listener     // Catch:{ Throwable -> 0x00c1 }
            boolean r4 = r4 instanceof com.taobao.tao.remotebusiness.IRemoteCacheListener     // Catch:{ Throwable -> 0x00c1 }
            if (r4 == 0) goto L_0x00a1
            java.lang.String r4 = "mtopsdk.HandlerMgr"
            java.lang.String r6 = "listener onCached callback"
            mtopsdk.common.util.TBSdkLog.i((java.lang.String) r4, (java.lang.String) r1, (java.lang.String) r6)     // Catch:{ Throwable -> 0x00c1 }
            mtopsdk.mtop.common.MtopListener r4 = r0.listener     // Catch:{ Throwable -> 0x00c1 }
            com.taobao.tao.remotebusiness.IRemoteCacheListener r4 = (com.taobao.tao.remotebusiness.IRemoteCacheListener) r4     // Catch:{ Throwable -> 0x00c1 }
            mtopsdk.mtop.domain.BaseOutDo r0 = r0.pojo     // Catch:{ Throwable -> 0x00c1 }
            r4.onCached(r3, r0, r2)     // Catch:{ Throwable -> 0x00c1 }
            goto L_0x01f9
        L_0x00a1:
            java.lang.String r3 = "mtopsdk.HandlerMgr"
            com.taobao.tao.remotebusiness.MtopBusiness r4 = r0.mtopBusiness     // Catch:{ Throwable -> 0x00c1 }
            java.lang.String r4 = r4.getSeqNo()     // Catch:{ Throwable -> 0x00c1 }
            java.lang.String r6 = "listener onCached transfer to onSuccess callback"
            mtopsdk.common.util.TBSdkLog.i((java.lang.String) r3, (java.lang.String) r4, (java.lang.String) r6)     // Catch:{ Throwable -> 0x00c1 }
            mtopsdk.mtop.common.MtopListener r3 = r0.listener     // Catch:{ Throwable -> 0x00c1 }
            com.taobao.tao.remotebusiness.IRemoteListener r3 = (com.taobao.tao.remotebusiness.IRemoteListener) r3     // Catch:{ Throwable -> 0x00c1 }
            com.taobao.tao.remotebusiness.MtopBusiness r4 = r0.mtopBusiness     // Catch:{ Throwable -> 0x00c1 }
            int r4 = r4.getRequestType()     // Catch:{ Throwable -> 0x00c1 }
            mtopsdk.mtop.domain.MtopResponse r6 = r0.mtopResponse     // Catch:{ Throwable -> 0x00c1 }
            mtopsdk.mtop.domain.BaseOutDo r0 = r0.pojo     // Catch:{ Throwable -> 0x00c1 }
            r3.onSuccess(r4, r6, r0, r2)     // Catch:{ Throwable -> 0x00c1 }
            goto L_0x01f9
        L_0x00c1:
            r0 = move-exception
            java.lang.String r2 = "mtopsdk.HandlerMgr"
            java.lang.String r3 = "listener onCached callback error."
            mtopsdk.common.util.TBSdkLog.e(r2, r1, r3, r0)
            goto L_0x01f9
        L_0x00cb:
            mtopsdk.common.util.TBSdkLog$LogEnable r2 = mtopsdk.common.util.TBSdkLog.LogEnable.InfoEnable
            boolean r2 = mtopsdk.common.util.TBSdkLog.isLogEnable(r2)
            if (r2 == 0) goto L_0x00da
            java.lang.String r2 = "mtopsdk.HandlerMgr"
            java.lang.String r3 = "onReceive: ON_FINISHED."
            mtopsdk.common.util.TBSdkLog.i((java.lang.String) r2, (java.lang.String) r1, (java.lang.String) r3)
        L_0x00da:
            long r2 = java.lang.System.currentTimeMillis()
            mtopsdk.mtop.domain.MtopResponse r6 = r0.mtopResponse
            r7 = 0
            if (r6 == 0) goto L_0x010b
            mtopsdk.mtop.domain.MtopResponse r6 = r0.mtopResponse
            mtopsdk.mtop.util.MtopStatistics r6 = r6.getMtopStat()
            if (r6 == 0) goto L_0x0109
            mtopsdk.mtop.util.MtopStatistics$RbStatisticData r9 = r6.getRbStatData()
            com.taobao.tao.remotebusiness.MtopBusiness r10 = r0.mtopBusiness
            long r10 = r10.onBgFinishTime
            long r10 = r2 - r10
            r9.toMainThTime = r10
            mtopsdk.mtop.domain.MtopResponse r10 = r0.mtopResponse
            byte[] r10 = r10.getBytedata()
            if (r10 == 0) goto L_0x010d
            mtopsdk.mtop.domain.MtopResponse r10 = r0.mtopResponse
            byte[] r10 = r10.getBytedata()
            int r10 = r10.length
            long r10 = (long) r10
            goto L_0x010e
        L_0x0109:
            r9 = r5
            goto L_0x010d
        L_0x010b:
            r6 = r5
            r9 = r6
        L_0x010d:
            r10 = r7
        L_0x010e:
            com.taobao.tao.remotebusiness.MtopBusiness r12 = r0.mtopBusiness
            mtopsdk.mtop.common.MtopNetworkProp r12 = r12.mtopProp
            android.os.Handler r12 = r12.handler
            if (r12 == 0) goto L_0x0145
            if (r6 == 0) goto L_0x0136
            com.taobao.tao.remotebusiness.MtopBusiness r7 = r0.mtopBusiness
            mtopsdk.mtop.common.MtopNetworkProp r7 = r7.mtopProp
            android.os.Handler r7 = r7.handler
            android.os.Looper r7 = r7.getLooper()
            android.os.Looper r8 = android.os.Looper.getMainLooper()
            boolean r7 = r7.equals(r8)
            r6.isMain = r7
            long r7 = java.lang.System.currentTimeMillis()
            r6.rspCbStart = r7
            long r7 = r6.rspCbStart
            r6.rspCbEnd = r7
        L_0x0136:
            com.taobao.tao.remotebusiness.MtopBusiness r7 = r0.mtopBusiness
            mtopsdk.mtop.common.MtopNetworkProp r7 = r7.mtopProp
            android.os.Handler r7 = r7.handler
            com.taobao.tao.remotebusiness.handler.HandlerMgr$1 r8 = new com.taobao.tao.remotebusiness.handler.HandlerMgr$1
            r8.<init>(r0)
            r7.post(r8)
            goto L_0x0169
        L_0x0145:
            if (r9 == 0) goto L_0x0151
            long r7 = r6.currentTimeMillis()
            long r12 = java.lang.System.currentTimeMillis()
            r6.rspCbStart = r12
        L_0x0151:
            com.taobao.tao.remotebusiness.MtopBusiness r12 = r0.mtopBusiness
            mtopsdk.mtop.domain.MtopResponse r13 = r0.mtopResponse
            mtopsdk.mtop.domain.BaseOutDo r0 = r0.pojo
            r12.doFinish(r13, r0)
            if (r9 == 0) goto L_0x0169
            long r12 = r6.currentTimeMillis()
            long r12 = r12 - r7
            r9.bizCallbackTime = r12
            long r7 = java.lang.System.currentTimeMillis()
            r6.rspCbEnd = r7
        L_0x0169:
            mtopsdk.common.util.TBSdkLog$LogEnable r0 = mtopsdk.common.util.TBSdkLog.LogEnable.InfoEnable
            boolean r0 = mtopsdk.common.util.TBSdkLog.isLogEnable(r0)
            if (r0 == 0) goto L_0x01a9
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r7 = 128(0x80, float:1.794E-43)
            r0.<init>(r7)
            java.lang.String r7 = "onReceive: ON_FINISHED. "
            r0.append(r7)
            java.lang.String r7 = "doFinishTime="
            r0.append(r7)
            long r7 = java.lang.System.currentTimeMillis()
            long r7 = r7 - r2
            r0.append(r7)
            java.lang.String r2 = "; dataSize="
            r0.append(r2)
            r0.append(r10)
            java.lang.String r2 = "; "
            r0.append(r2)
            if (r9 == 0) goto L_0x01a0
            java.lang.String r2 = r9.toString()
            r0.append(r2)
        L_0x01a0:
            java.lang.String r2 = "mtopsdk.HandlerMgr"
            java.lang.String r0 = r0.toString()
            mtopsdk.common.util.TBSdkLog.i((java.lang.String) r2, (java.lang.String) r1, (java.lang.String) r0)
        L_0x01a9:
            if (r6 == 0) goto L_0x01f9
            r6.commitStatData(r4)
            r6.commitFullTrace()
            goto L_0x01f9
        L_0x01b2:
            mtopsdk.common.util.TBSdkLog$LogEnable r3 = mtopsdk.common.util.TBSdkLog.LogEnable.InfoEnable
            boolean r3 = mtopsdk.common.util.TBSdkLog.isLogEnable(r3)
            if (r3 == 0) goto L_0x01c1
            java.lang.String r3 = "mtopsdk.HandlerMgr"
            java.lang.String r4 = "onReceive: ON_HEADER."
            mtopsdk.common.util.TBSdkLog.i((java.lang.String) r3, (java.lang.String) r1, (java.lang.String) r4)
        L_0x01c1:
            mtopsdk.mtop.common.MtopListener r3 = r0.listener     // Catch:{ Throwable -> 0x01cd }
            com.taobao.tao.remotebusiness.IRemoteProcessListener r3 = (com.taobao.tao.remotebusiness.IRemoteProcessListener) r3     // Catch:{ Throwable -> 0x01cd }
            mtopsdk.mtop.common.MtopEvent r0 = r0.event     // Catch:{ Throwable -> 0x01cd }
            mtopsdk.mtop.common.MtopHeaderEvent r0 = (mtopsdk.mtop.common.MtopHeaderEvent) r0     // Catch:{ Throwable -> 0x01cd }
            r3.onHeader(r0, r2)     // Catch:{ Throwable -> 0x01cd }
            goto L_0x01f9
        L_0x01cd:
            r0 = move-exception
            java.lang.String r2 = "mtopsdk.HandlerMgr"
            java.lang.String r3 = "listener onHeader callback error."
            mtopsdk.common.util.TBSdkLog.e(r2, r1, r3, r0)
            goto L_0x01f9
        L_0x01d6:
            mtopsdk.common.util.TBSdkLog$LogEnable r3 = mtopsdk.common.util.TBSdkLog.LogEnable.InfoEnable
            boolean r3 = mtopsdk.common.util.TBSdkLog.isLogEnable(r3)
            if (r3 == 0) goto L_0x01e5
            java.lang.String r3 = "mtopsdk.HandlerMgr"
            java.lang.String r4 = "onReceive: ON_DATA_RECEIVED."
            mtopsdk.common.util.TBSdkLog.i((java.lang.String) r3, (java.lang.String) r1, (java.lang.String) r4)
        L_0x01e5:
            mtopsdk.mtop.common.MtopListener r3 = r0.listener     // Catch:{ Throwable -> 0x01f1 }
            com.taobao.tao.remotebusiness.IRemoteProcessListener r3 = (com.taobao.tao.remotebusiness.IRemoteProcessListener) r3     // Catch:{ Throwable -> 0x01f1 }
            mtopsdk.mtop.common.MtopEvent r0 = r0.event     // Catch:{ Throwable -> 0x01f1 }
            mtopsdk.mtop.common.MtopProgressEvent r0 = (mtopsdk.mtop.common.MtopProgressEvent) r0     // Catch:{ Throwable -> 0x01f1 }
            r3.onDataReceived(r0, r2)     // Catch:{ Throwable -> 0x01f1 }
            goto L_0x01f9
        L_0x01f1:
            r0 = move-exception
            java.lang.String r2 = "mtopsdk.HandlerMgr"
            java.lang.String r3 = "listener onDataReceived callback error."
            mtopsdk.common.util.TBSdkLog.e(r2, r1, r3, r0)
        L_0x01f9:
            r15.obj = r5
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.tao.remotebusiness.handler.HandlerMgr.handleMessage(android.os.Message):void");
    }

    public static HandlerParam getHandlerMsg(MtopListener mtopListener, MtopEvent mtopEvent, MtopBusiness mtopBusiness) {
        return new HandlerParam(mtopListener, mtopEvent, mtopBusiness);
    }
}
