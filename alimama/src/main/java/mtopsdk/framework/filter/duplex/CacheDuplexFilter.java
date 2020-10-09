package mtopsdk.framework.filter.duplex;

import android.content.Context;
import anetwork.network.cache.Cache;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import mtopsdk.common.util.HeaderHandlerUtil;
import mtopsdk.common.util.StringUtils;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.config.AppConfigManager;
import mtopsdk.framework.domain.FilterResult;
import mtopsdk.framework.domain.MtopContext;
import mtopsdk.framework.filter.IAfterFilter;
import mtopsdk.framework.filter.IBeforeFilter;
import mtopsdk.mtop.cache.CacheManager;
import mtopsdk.mtop.cache.domain.ApiCacheDo;
import mtopsdk.mtop.domain.MtopResponse;
import mtopsdk.mtop.domain.ResponseSource;
import mtopsdk.mtop.global.SwitchConfig;

public class CacheDuplexFilter implements IBeforeFilter, IAfterFilter {
    private static final String TAG = "mtopsdk.CacheDuplexFilter";
    private static final Map<Cache, CacheManager> cacheManagerMap = new ConcurrentHashMap(2);

    public String getName() {
        return TAG;
    }

    /* JADX WARNING: Removed duplicated region for block: B:42:0x00ed  */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x00f7 A[RETURN] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String doBefore(mtopsdk.framework.domain.MtopContext r8) {
        /*
            r7 = this;
            java.lang.String r0 = "CONTINUE"
            mtopsdk.mtop.global.SwitchConfig r1 = mtopsdk.mtop.global.SwitchConfig.getInstance()
            java.util.Set<java.lang.String> r1 = r1.degradeApiCacheSet
            if (r1 == 0) goto L_0x003d
            mtopsdk.mtop.domain.MtopRequest r1 = r8.mtopRequest
            java.lang.String r1 = r1.getKey()
            mtopsdk.mtop.global.SwitchConfig r2 = mtopsdk.mtop.global.SwitchConfig.getInstance()
            java.util.Set<java.lang.String> r2 = r2.degradeApiCacheSet
            boolean r2 = r2.contains(r1)
            if (r2 == 0) goto L_0x003d
            mtopsdk.common.util.TBSdkLog$LogEnable r2 = mtopsdk.common.util.TBSdkLog.LogEnable.InfoEnable
            boolean r2 = mtopsdk.common.util.TBSdkLog.isLogEnable(r2)
            if (r2 == 0) goto L_0x003c
            java.lang.String r2 = "mtopsdk.CacheDuplexFilter"
            java.lang.String r8 = r8.seqNo
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "apiKey in degradeApiCacheList,apiKey="
            r3.append(r4)
            r3.append(r1)
            java.lang.String r1 = r3.toString()
            mtopsdk.common.util.TBSdkLog.i((java.lang.String) r2, (java.lang.String) r8, (java.lang.String) r1)
        L_0x003c:
            return r0
        L_0x003d:
            mtopsdk.mtop.util.MtopStatistics r1 = r8.stats
            r2 = 1
            r1.cacheSwitch = r2
            mtopsdk.mtop.intf.Mtop r1 = r8.mtopInstance
            mtopsdk.mtop.global.MtopConfig r1 = r1.getMtopConfig()
            anetwork.network.cache.Cache r1 = r1.cacheImpl
            if (r1 != 0) goto L_0x0073
            mtopsdk.common.util.TBSdkLog$LogEnable r1 = mtopsdk.common.util.TBSdkLog.LogEnable.DebugEnable
            boolean r1 = mtopsdk.common.util.TBSdkLog.isLogEnable(r1)
            if (r1 == 0) goto L_0x0072
            java.lang.String r1 = "mtopsdk.CacheDuplexFilter"
            java.lang.String r2 = r8.seqNo
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = " CacheImpl is null. instanceId="
            r3.append(r4)
            mtopsdk.mtop.intf.Mtop r8 = r8.mtopInstance
            java.lang.String r8 = r8.getInstanceId()
            r3.append(r8)
            java.lang.String r8 = r3.toString()
            mtopsdk.common.util.TBSdkLog.d((java.lang.String) r1, (java.lang.String) r2, (java.lang.String) r8)
        L_0x0072:
            return r0
        L_0x0073:
            java.util.Map<anetwork.network.cache.Cache, mtopsdk.mtop.cache.CacheManager> r2 = cacheManagerMap
            java.lang.Object r2 = r2.get(r1)
            mtopsdk.mtop.cache.CacheManager r2 = (mtopsdk.mtop.cache.CacheManager) r2
            if (r2 != 0) goto L_0x0099
            java.util.Map<anetwork.network.cache.Cache, mtopsdk.mtop.cache.CacheManager> r3 = cacheManagerMap
            monitor-enter(r3)
            java.util.Map<anetwork.network.cache.Cache, mtopsdk.mtop.cache.CacheManager> r2 = cacheManagerMap     // Catch:{ all -> 0x0096 }
            java.lang.Object r2 = r2.get(r1)     // Catch:{ all -> 0x0096 }
            mtopsdk.mtop.cache.CacheManager r2 = (mtopsdk.mtop.cache.CacheManager) r2     // Catch:{ all -> 0x0096 }
            if (r2 != 0) goto L_0x0094
            mtopsdk.mtop.cache.CacheManagerImpl r2 = new mtopsdk.mtop.cache.CacheManagerImpl     // Catch:{ all -> 0x0096 }
            r2.<init>(r1)     // Catch:{ all -> 0x0096 }
            java.util.Map<anetwork.network.cache.Cache, mtopsdk.mtop.cache.CacheManager> r4 = cacheManagerMap     // Catch:{ all -> 0x0096 }
            r4.put(r1, r2)     // Catch:{ all -> 0x0096 }
        L_0x0094:
            monitor-exit(r3)     // Catch:{ all -> 0x0096 }
            goto L_0x0099
        L_0x0096:
            r8 = move-exception
            monitor-exit(r3)     // Catch:{ all -> 0x0096 }
            throw r8
        L_0x0099:
            r1 = 0
            mtopsdk.network.domain.Request r3 = r8.networkRequest     // Catch:{ Exception -> 0x00c5 }
            mtopsdk.mtop.common.MtopListener r4 = r8.mtopListener     // Catch:{ Exception -> 0x00c5 }
            boolean r3 = r2.isNeedReadCache(r3, r4)     // Catch:{ Exception -> 0x00c5 }
            if (r3 == 0) goto L_0x00e7
            mtopsdk.mtop.domain.ResponseSource r3 = new mtopsdk.mtop.domain.ResponseSource     // Catch:{ Exception -> 0x00c5 }
            r3.<init>(r8, r2)     // Catch:{ Exception -> 0x00c5 }
            r8.responseSource = r3     // Catch:{ Exception -> 0x00c3 }
            java.lang.String r1 = r3.getCacheKey()     // Catch:{ Exception -> 0x00c3 }
            java.lang.String r4 = r3.getCacheBlock()     // Catch:{ Exception -> 0x00c3 }
            java.lang.String r5 = r8.seqNo     // Catch:{ Exception -> 0x00c3 }
            anetwork.network.cache.RpcCache r1 = r2.getCache(r1, r4, r5)     // Catch:{ Exception -> 0x00c3 }
            r3.rpcCache = r1     // Catch:{ Exception -> 0x00c3 }
            mtopsdk.mtop.common.MtopNetworkProp r1 = r8.property     // Catch:{ Exception -> 0x00c3 }
            android.os.Handler r1 = r1.handler     // Catch:{ Exception -> 0x00c3 }
            mtopsdk.mtop.cache.handler.CacheStatusHandler.handleCacheStatus(r3, r1)     // Catch:{ Exception -> 0x00c3 }
            goto L_0x00e6
        L_0x00c3:
            r1 = move-exception
            goto L_0x00c8
        L_0x00c5:
            r2 = move-exception
            r3 = r1
            r1 = r2
        L_0x00c8:
            java.lang.String r2 = "mtopsdk.CacheDuplexFilter"
            java.lang.String r4 = r8.seqNo
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r6 = "[initResponseSource] initResponseSource error,apiKey="
            r5.append(r6)
            mtopsdk.mtop.domain.MtopRequest r6 = r8.mtopRequest
            java.lang.String r6 = r6.getKey()
            r5.append(r6)
            java.lang.String r5 = r5.toString()
            mtopsdk.common.util.TBSdkLog.e(r2, r4, r5, r1)
        L_0x00e6:
            r1 = r3
        L_0x00e7:
            if (r1 == 0) goto L_0x00f7
            boolean r2 = r1.requireConnection
            if (r2 != 0) goto L_0x00f7
            mtopsdk.mtop.domain.MtopResponse r0 = r1.cacheResponse
            r8.mtopResponse = r0
            mtopsdk.framework.util.FilterUtils.handleExceptionCallBack(r8)
            java.lang.String r8 = "STOP"
            return r8
        L_0x00f7:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: mtopsdk.framework.filter.duplex.CacheDuplexFilter.doBefore(mtopsdk.framework.domain.MtopContext):java.lang.String");
    }

    public String doAfter(MtopContext mtopContext) {
        if (SwitchConfig.getInstance().degradeApiCacheSet != null) {
            String key = mtopContext.mtopRequest.getKey();
            if (SwitchConfig.getInstance().degradeApiCacheSet.contains(key)) {
                if (!TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                    return FilterResult.CONTINUE;
                }
                String str = mtopContext.seqNo;
                TBSdkLog.i(TAG, str, "apiKey in degradeApiCacheList,apiKey=" + key);
                return FilterResult.CONTINUE;
            }
        }
        MtopResponse mtopResponse = mtopContext.mtopResponse;
        ResponseSource responseSource = mtopContext.responseSource;
        if (!mtopResponse.isApiSuccess() || responseSource == null) {
            return FilterResult.CONTINUE;
        }
        Map<String, List<String>> headerFields = mtopResponse.getHeaderFields();
        CacheManager cacheManager = responseSource.cacheManager;
        if (!cacheManager.isNeedWriteCache(mtopContext.networkRequest, headerFields)) {
            return FilterResult.CONTINUE;
        }
        cacheManager.putCache(responseSource.getCacheKey(), responseSource.getCacheBlock(), mtopResponse);
        updateApiCacheConf(mtopContext, mtopResponse, responseSource.getCacheBlock(), headerFields);
        return FilterResult.CONTINUE;
    }

    private void updateApiCacheConf(MtopContext mtopContext, MtopResponse mtopResponse, String str, Map<String, List<String>> map) {
        String singleHeaderFieldByKey = HeaderHandlerUtil.getSingleHeaderFieldByKey(map, "cache-control");
        if (!StringUtils.isBlank(singleHeaderFieldByKey)) {
            AppConfigManager instance = AppConfigManager.getInstance();
            String api = mtopResponse.getApi();
            String v = mtopResponse.getV();
            String concatStr2LowerCase = StringUtils.concatStr2LowerCase(api, v);
            ApiCacheDo apiCacheDoByKey = instance.getApiCacheDoByKey(concatStr2LowerCase);
            Context context = mtopContext.mtopInstance.getMtopConfig().context;
            if (apiCacheDoByKey == null) {
                ApiCacheDo apiCacheDo = new ApiCacheDo(api, v, str);
                instance.parseCacheControlHeader(singleHeaderFieldByKey, apiCacheDo);
                instance.addApiCacheDoToGroup(concatStr2LowerCase, apiCacheDo);
                instance.storeApiCacheDoMap(context, mtopContext.seqNo);
            } else if (!singleHeaderFieldByKey.equals(apiCacheDoByKey.cacheControlHeader)) {
                instance.parseCacheControlHeader(singleHeaderFieldByKey, apiCacheDoByKey);
                instance.storeApiCacheDoMap(context, mtopContext.seqNo);
            }
        }
    }
}
