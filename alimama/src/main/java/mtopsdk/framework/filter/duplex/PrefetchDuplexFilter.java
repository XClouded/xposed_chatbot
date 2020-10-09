package mtopsdk.framework.filter.duplex;

import androidx.annotation.NonNull;
import com.taobao.analysis.abtest.ABTestCenter;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;
import mtopsdk.common.util.RemoteConfig;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.framework.domain.FilterResult;
import mtopsdk.framework.domain.MtopContext;
import mtopsdk.framework.filter.IAfterFilter;
import mtopsdk.framework.filter.IBeforeFilter;
import mtopsdk.mtop.intf.Mtop;
import mtopsdk.mtop.intf.MtopPrefetch;

public class PrefetchDuplexFilter implements IBeforeFilter, IAfterFilter {
    private static final String TAG = "mtopsdk.PrefetchDuplexFilter";

    @NonNull
    public String getName() {
        return TAG;
    }

    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String doBefore(mtopsdk.framework.domain.MtopContext r12) {
        /*
            r11 = this;
            boolean r0 = r11.isContinue()     // Catch:{ Throwable -> 0x01d4 }
            if (r0 == 0) goto L_0x0009
            java.lang.String r0 = "CONTINUE"
            return r0
        L_0x0009:
            mtopsdk.mtop.intf.MtopBuilder r0 = r12.mtopBuilder     // Catch:{ Throwable -> 0x01d4 }
            mtopsdk.mtop.intf.MtopPrefetch r0 = r0.getMtopPrefetch()     // Catch:{ Throwable -> 0x01d4 }
            if (r0 == 0) goto L_0x0021
            mtopsdk.mtop.intf.Mtop r0 = r12.mtopInstance     // Catch:{ Throwable -> 0x01d4 }
            mtopsdk.mtop.intf.MtopBuilder r1 = r12.mtopBuilder     // Catch:{ Throwable -> 0x01d4 }
            mtopsdk.mtop.domain.MtopRequest r2 = r12.mtopRequest     // Catch:{ Throwable -> 0x01d4 }
            java.lang.String r2 = r2.getKey()     // Catch:{ Throwable -> 0x01d4 }
            r0.addPrefetchBuilderToMap(r1, r2)     // Catch:{ Throwable -> 0x01d4 }
            java.lang.String r0 = "CONTINUE"
            return r0
        L_0x0021:
            mtopsdk.mtop.common.MtopNetworkProp r0 = r12.property     // Catch:{ Throwable -> 0x01d4 }
            boolean r0 = r0.useCache     // Catch:{ Throwable -> 0x01d4 }
            if (r0 == 0) goto L_0x002a
            java.lang.String r0 = "CONTINUE"
            return r0
        L_0x002a:
            mtopsdk.mtop.intf.Mtop r0 = r12.mtopInstance     // Catch:{ Throwable -> 0x01d4 }
            java.util.Map r0 = r0.getPrefetchBuilderMap()     // Catch:{ Throwable -> 0x01d4 }
            mtopsdk.mtop.domain.MtopRequest r1 = r12.mtopRequest     // Catch:{ Throwable -> 0x01d4 }
            java.lang.String r1 = r1.getKey()     // Catch:{ Throwable -> 0x01d4 }
            java.lang.Object r0 = r0.get(r1)     // Catch:{ Throwable -> 0x01d4 }
            mtopsdk.mtop.intf.MtopBuilder r0 = (mtopsdk.mtop.intf.MtopBuilder) r0     // Catch:{ Throwable -> 0x01d4 }
            if (r0 == 0) goto L_0x01f3
            long r1 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x01d4 }
            mtopsdk.mtop.intf.MtopPrefetch r3 = r0.getMtopPrefetch()     // Catch:{ Throwable -> 0x01d4 }
            mtopsdk.mtop.intf.MtopPrefetch$IPrefetchComparator r3 = r3.getComparator()     // Catch:{ Throwable -> 0x01d4 }
            mtopsdk.mtop.intf.MtopBuilder r4 = r12.mtopBuilder     // Catch:{ Throwable -> 0x01d4 }
            mtopsdk.mtop.intf.MtopPrefetch$CompareResult r3 = r3.compare(r4, r0)     // Catch:{ Throwable -> 0x01d4 }
            r4 = 0
            if (r3 == 0) goto L_0x01a2
            boolean r5 = r3.isSame()     // Catch:{ Throwable -> 0x01d4 }
            if (r5 != 0) goto L_0x005b
            goto L_0x01a2
        L_0x005b:
            mtopsdk.mtop.intf.MtopPrefetch r3 = r0.getMtopPrefetch()     // Catch:{ all -> 0x0197 }
            java.util.concurrent.locks.ReentrantLock r3 = r3.lock     // Catch:{ all -> 0x0197 }
            r3.lock()     // Catch:{ all -> 0x0197 }
            mtopsdk.mtop.intf.MtopPrefetch r3 = r0.getMtopPrefetch()     // Catch:{ all -> 0x0197 }
            java.util.concurrent.atomic.AtomicBoolean r3 = r3.response     // Catch:{ all -> 0x0197 }
            boolean r3 = r3.get()     // Catch:{ all -> 0x0197 }
            if (r3 != 0) goto L_0x008a
            mtopsdk.mtop.intf.MtopPrefetch r3 = r0.getMtopPrefetch()     // Catch:{ all -> 0x0197 }
            mtopsdk.framework.domain.MtopContext r3 = r3.mergeContext     // Catch:{ all -> 0x0197 }
            if (r3 != 0) goto L_0x008a
            mtopsdk.mtop.intf.MtopPrefetch r1 = r0.getMtopPrefetch()     // Catch:{ all -> 0x0197 }
            r1.mergeContext = r12     // Catch:{ all -> 0x0197 }
            java.lang.String r1 = "STOP"
            mtopsdk.mtop.intf.MtopPrefetch r0 = r0.getMtopPrefetch()     // Catch:{ Throwable -> 0x01d4 }
            java.util.concurrent.locks.ReentrantLock r0 = r0.lock     // Catch:{ Throwable -> 0x01d4 }
            r0.unlock()     // Catch:{ Throwable -> 0x01d4 }
            return r1
        L_0x008a:
            mtopsdk.mtop.intf.MtopPrefetch r3 = r0.getMtopPrefetch()     // Catch:{ Throwable -> 0x01d4 }
            java.util.concurrent.locks.ReentrantLock r3 = r3.lock     // Catch:{ Throwable -> 0x01d4 }
            r3.unlock()     // Catch:{ Throwable -> 0x01d4 }
            mtopsdk.mtop.intf.MtopPrefetch r3 = r0.getMtopPrefetch()     // Catch:{ Throwable -> 0x01d4 }
            long r5 = r3.prefetchResponseTime     // Catch:{ Throwable -> 0x01d4 }
            r3 = 0
            long r5 = r1 - r5
            mtopsdk.mtop.intf.MtopPrefetch r3 = r0.getMtopPrefetch()     // Catch:{ Throwable -> 0x01d4 }
            long r7 = r3.getPrefetchExpireTime()     // Catch:{ Throwable -> 0x01d4 }
            int r3 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1))
            if (r3 <= 0) goto L_0x00e3
            java.lang.String r1 = "TYPE_EXPIRE"
            mtopsdk.mtop.intf.MtopPrefetch r0 = r0.getMtopPrefetch()     // Catch:{ Throwable -> 0x01d4 }
            mtopsdk.mtop.intf.MtopPrefetch.onPrefetchAndCommit(r1, r0, r12, r4)     // Catch:{ Throwable -> 0x01d4 }
            mtopsdk.mtop.intf.Mtop r0 = r12.mtopInstance     // Catch:{ Throwable -> 0x01d4 }
            java.util.Map r0 = r0.getPrefetchBuilderMap()     // Catch:{ Throwable -> 0x01d4 }
            mtopsdk.mtop.domain.MtopRequest r1 = r12.mtopRequest     // Catch:{ Throwable -> 0x01d4 }
            java.lang.String r1 = r1.getKey()     // Catch:{ Throwable -> 0x01d4 }
            r0.remove(r1)     // Catch:{ Throwable -> 0x01d4 }
            mtopsdk.common.util.TBSdkLog$LogEnable r0 = mtopsdk.common.util.TBSdkLog.LogEnable.DebugEnable     // Catch:{ Throwable -> 0x01d4 }
            boolean r0 = mtopsdk.common.util.TBSdkLog.isLogEnable(r0)     // Catch:{ Throwable -> 0x01d4 }
            if (r0 == 0) goto L_0x00e0
            java.lang.String r0 = "mtopsdk.PrefetchDuplexFilter"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x01d4 }
            r1.<init>()     // Catch:{ Throwable -> 0x01d4 }
            java.lang.String r2 = r12.seqNo     // Catch:{ Throwable -> 0x01d4 }
            r1.append(r2)     // Catch:{ Throwable -> 0x01d4 }
            java.lang.String r2 = "not hit, time expired"
            r1.append(r2)     // Catch:{ Throwable -> 0x01d4 }
            java.lang.String r1 = r1.toString()     // Catch:{ Throwable -> 0x01d4 }
            mtopsdk.common.util.TBSdkLog.d(r0, r1)     // Catch:{ Throwable -> 0x01d4 }
        L_0x00e0:
            java.lang.String r0 = "CONTINUE"
            return r0
        L_0x00e3:
            mtopsdk.mtop.util.MtopStatistics r3 = r12.stats     // Catch:{ Throwable -> 0x01d4 }
            mtopsdk.framework.domain.MtopContext r5 = r0.getMtopContext()     // Catch:{ Throwable -> 0x01d4 }
            mtopsdk.mtop.domain.MtopResponse r5 = r5.mtopResponse     // Catch:{ Throwable -> 0x01d4 }
            r5.setMtopStat(r3)     // Catch:{ Throwable -> 0x01d4 }
            long r6 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x01d4 }
            r3.rspCbDispatch = r6     // Catch:{ Throwable -> 0x01d4 }
            r6 = 1
            r3.isPrefetch = r6     // Catch:{ Throwable -> 0x01d4 }
            mtopsdk.mtop.common.MtopFinishEvent r7 = new mtopsdk.mtop.common.MtopFinishEvent     // Catch:{ Throwable -> 0x01d4 }
            r7.<init>(r5)     // Catch:{ Throwable -> 0x01d4 }
            java.lang.String r8 = r12.seqNo     // Catch:{ Throwable -> 0x01d4 }
            r7.seqNo = r8     // Catch:{ Throwable -> 0x01d4 }
            java.util.Map r8 = r5.getHeaderFields()     // Catch:{ Throwable -> 0x01d4 }
            java.lang.String r9 = "x-s-traceid"
            java.lang.String r8 = mtopsdk.common.util.HeaderHandlerUtil.getSingleHeaderFieldByKey(r8, r9)     // Catch:{ Throwable -> 0x01d4 }
            r3.serverTraceId = r8     // Catch:{ Throwable -> 0x01d4 }
            java.util.Map r8 = r5.getHeaderFields()     // Catch:{ Throwable -> 0x01d4 }
            java.lang.String r9 = "eagleeye-traceid"
            java.lang.String r8 = mtopsdk.common.util.HeaderHandlerUtil.getSingleHeaderFieldByKey(r8, r9)     // Catch:{ Throwable -> 0x01d4 }
            r3.eagleEyeTraceId = r8     // Catch:{ Throwable -> 0x01d4 }
            java.lang.String r8 = r5.getRetCode()     // Catch:{ Throwable -> 0x01d4 }
            r3.retCode = r8     // Catch:{ Throwable -> 0x01d4 }
            int r8 = r5.getResponseCode()     // Catch:{ Throwable -> 0x01d4 }
            r3.statusCode = r8     // Catch:{ Throwable -> 0x01d4 }
            java.lang.String r5 = r5.getMappingCode()     // Catch:{ Throwable -> 0x01d4 }
            r3.mappingCode = r5     // Catch:{ Throwable -> 0x01d4 }
            r3.onEndAndCommit()     // Catch:{ Throwable -> 0x01d4 }
            mtopsdk.mtop.common.MtopListener r5 = r12.mtopListener     // Catch:{ Throwable -> 0x01d4 }
            mtopsdk.mtop.intf.MtopBuilder r8 = r12.mtopBuilder     // Catch:{ Throwable -> 0x01d4 }
            boolean r8 = r8 instanceof com.taobao.tao.remotebusiness.MtopBusiness     // Catch:{ Throwable -> 0x01d4 }
            if (r8 == 0) goto L_0x0136
            r6 = 0
        L_0x0136:
            if (r6 == 0) goto L_0x013e
            long r8 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x01d4 }
            r3.rspCbStart = r8     // Catch:{ Throwable -> 0x01d4 }
        L_0x013e:
            mtopsdk.common.util.TBSdkLog$LogEnable r8 = mtopsdk.common.util.TBSdkLog.LogEnable.DebugEnable     // Catch:{ Throwable -> 0x01d4 }
            boolean r8 = mtopsdk.common.util.TBSdkLog.isLogEnable(r8)     // Catch:{ Throwable -> 0x01d4 }
            if (r8 == 0) goto L_0x015e
            java.lang.String r8 = "mtopsdk.PrefetchDuplexFilter"
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x01d4 }
            r9.<init>()     // Catch:{ Throwable -> 0x01d4 }
            java.lang.String r10 = r12.seqNo     // Catch:{ Throwable -> 0x01d4 }
            r9.append(r10)     // Catch:{ Throwable -> 0x01d4 }
            java.lang.String r10 = "hit cache"
            r9.append(r10)     // Catch:{ Throwable -> 0x01d4 }
            java.lang.String r9 = r9.toString()     // Catch:{ Throwable -> 0x01d4 }
            mtopsdk.common.util.TBSdkLog.d(r8, r9)     // Catch:{ Throwable -> 0x01d4 }
        L_0x015e:
            boolean r8 = r5 instanceof mtopsdk.mtop.common.MtopCallback.MtopFinishListener     // Catch:{ Throwable -> 0x01d4 }
            if (r8 == 0) goto L_0x016b
            mtopsdk.mtop.common.MtopCallback$MtopFinishListener r5 = (mtopsdk.mtop.common.MtopCallback.MtopFinishListener) r5     // Catch:{ Throwable -> 0x01d4 }
            mtopsdk.mtop.common.MtopNetworkProp r8 = r12.property     // Catch:{ Throwable -> 0x01d4 }
            java.lang.Object r8 = r8.reqContext     // Catch:{ Throwable -> 0x01d4 }
            r5.onFinished(r7, r8)     // Catch:{ Throwable -> 0x01d4 }
        L_0x016b:
            if (r6 == 0) goto L_0x0176
            long r5 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x01d4 }
            r3.rspCbEnd = r5     // Catch:{ Throwable -> 0x01d4 }
            r3.commitFullTrace()     // Catch:{ Throwable -> 0x01d4 }
        L_0x0176:
            mtopsdk.mtop.intf.MtopPrefetch r3 = r0.getMtopPrefetch()     // Catch:{ Throwable -> 0x01d4 }
            r3.prefetchHitTime = r1     // Catch:{ Throwable -> 0x01d4 }
            java.lang.String r1 = "TYPE_HIT"
            mtopsdk.mtop.intf.MtopPrefetch r0 = r0.getMtopPrefetch()     // Catch:{ Throwable -> 0x01d4 }
            mtopsdk.mtop.intf.MtopPrefetch.onPrefetchAndCommit(r1, r0, r12, r4)     // Catch:{ Throwable -> 0x01d4 }
            mtopsdk.mtop.intf.Mtop r0 = r12.mtopInstance     // Catch:{ Throwable -> 0x01d4 }
            java.util.Map r0 = r0.getPrefetchBuilderMap()     // Catch:{ Throwable -> 0x01d4 }
            mtopsdk.mtop.domain.MtopRequest r1 = r12.mtopRequest     // Catch:{ Throwable -> 0x01d4 }
            java.lang.String r1 = r1.getKey()     // Catch:{ Throwable -> 0x01d4 }
            r0.remove(r1)     // Catch:{ Throwable -> 0x01d4 }
            java.lang.String r0 = "STOP"
            return r0
        L_0x0197:
            r1 = move-exception
            mtopsdk.mtop.intf.MtopPrefetch r0 = r0.getMtopPrefetch()     // Catch:{ Throwable -> 0x01d4 }
            java.util.concurrent.locks.ReentrantLock r0 = r0.lock     // Catch:{ Throwable -> 0x01d4 }
            r0.unlock()     // Catch:{ Throwable -> 0x01d4 }
            throw r1     // Catch:{ Throwable -> 0x01d4 }
        L_0x01a2:
            java.lang.String r1 = "TYPE_MISS"
            mtopsdk.mtop.intf.MtopPrefetch r0 = r0.getMtopPrefetch()     // Catch:{ Throwable -> 0x01d4 }
            if (r3 == 0) goto L_0x01ae
            java.util.HashMap r4 = r3.getData()     // Catch:{ Throwable -> 0x01d4 }
        L_0x01ae:
            mtopsdk.mtop.intf.MtopPrefetch.onPrefetchAndCommit(r1, r0, r12, r4)     // Catch:{ Throwable -> 0x01d4 }
            mtopsdk.common.util.TBSdkLog$LogEnable r0 = mtopsdk.common.util.TBSdkLog.LogEnable.DebugEnable     // Catch:{ Throwable -> 0x01d4 }
            boolean r0 = mtopsdk.common.util.TBSdkLog.isLogEnable(r0)     // Catch:{ Throwable -> 0x01d4 }
            if (r0 == 0) goto L_0x01d1
            java.lang.String r0 = "mtopsdk.PrefetchDuplexFilter"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x01d4 }
            r1.<init>()     // Catch:{ Throwable -> 0x01d4 }
            java.lang.String r2 = r12.seqNo     // Catch:{ Throwable -> 0x01d4 }
            r1.append(r2)     // Catch:{ Throwable -> 0x01d4 }
            java.lang.String r2 = "not hit, miss not the same request"
            r1.append(r2)     // Catch:{ Throwable -> 0x01d4 }
            java.lang.String r1 = r1.toString()     // Catch:{ Throwable -> 0x01d4 }
            mtopsdk.common.util.TBSdkLog.d(r0, r1)     // Catch:{ Throwable -> 0x01d4 }
        L_0x01d1:
            java.lang.String r0 = "CONTINUE"
            return r0
        L_0x01d4:
            r0 = move-exception
            java.lang.String r1 = "mtopsdk.PrefetchDuplexFilter"
            java.lang.String r2 = r12.seqNo
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "call prefetch filter before error,apiKey="
            r3.append(r4)
            mtopsdk.mtop.domain.MtopRequest r12 = r12.mtopRequest
            java.lang.String r12 = r12.getKey()
            r3.append(r12)
            java.lang.String r12 = r3.toString()
            mtopsdk.common.util.TBSdkLog.e(r1, r2, r12, r0)
        L_0x01f3:
            java.lang.String r12 = "CONTINUE"
            return r12
        */
        throw new UnsupportedOperationException("Method not decompiled: mtopsdk.framework.filter.duplex.PrefetchDuplexFilter.doBefore(mtopsdk.framework.domain.MtopContext):java.lang.String");
    }

    public String doAfter(MtopContext mtopContext) {
        ReentrantLock reentrantLock;
        try {
            if (isContinue() || mtopContext.property.useCache) {
                return FilterResult.CONTINUE;
            }
            long currentTimeMillis = System.currentTimeMillis();
            if (mtopContext.mtopBuilder.getMtopPrefetch() == null) {
                return FilterResult.CONTINUE;
            }
            MtopPrefetch mtopPrefetch = mtopContext.mtopBuilder.getMtopPrefetch();
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.DebugEnable)) {
                TBSdkLog.d(TAG, mtopContext.seqNo + "save prefetch request and get response " + mtopContext.mtopRequest.getKey());
            }
            if (mtopContext.mtopResponse == null) {
                return FilterResult.CONTINUE;
            }
            mtopPrefetch.prefetchResponseTime = currentTimeMillis;
            mtopContext.mtopInstance.lastPrefetchResponseTime = currentTimeMillis;
            reentrantLock = mtopPrefetch.lock;
            reentrantLock.lock();
            mtopPrefetch.response.compareAndSet(false, true);
            if (mtopPrefetch.mergeContext != null) {
                mtopPrefetch.prefetchHitTime = currentTimeMillis;
                MtopPrefetch.onPrefetchAndCommit(MtopPrefetch.IPrefetchCallback.PrefetchCallbackType.TYPE_HIT, mtopPrefetch, mtopContext, (HashMap<String, String>) null);
                mtopContext.mtopInstance.getPrefetchBuilderMap().remove(mtopContext.mtopRequest.getKey());
                mtopContext.mtopListener = mtopPrefetch.mergeContext.mtopListener;
                mtopContext.mtopBuilder = mtopPrefetch.mergeContext.mtopBuilder;
                mtopContext.stats.isPrefetch = true;
            }
            reentrantLock.unlock();
            return FilterResult.CONTINUE;
        } catch (Throwable th) {
            String str = mtopContext.seqNo;
            TBSdkLog.e(TAG, str, "checking after error " + th);
            return FilterResult.CONTINUE;
        }
    }

    private boolean isContinue() {
        if (!RemoteConfig.getInstance().prefetch || !Mtop.mIsFullTrackValid) {
            return true;
        }
        if (ABTestCenter.isTBSpeedEdition(RemoteConfig.TB_SPEED_TS_ENABLE) || ABTestCenter.isTBSpeedEdition(RemoteConfig.TB_SPEED_U_LAND)) {
            return false;
        }
        return true;
    }
}
