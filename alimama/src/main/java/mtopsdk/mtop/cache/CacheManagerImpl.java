package mtopsdk.mtop.cache;

import anetwork.network.cache.Cache;
import anetwork.network.cache.RpcCache;
import java.util.List;
import java.util.Map;
import mtopsdk.common.util.HeaderHandlerUtil;
import mtopsdk.common.util.HttpHeaderConstant;
import mtopsdk.common.util.MtopUtils;
import mtopsdk.common.util.StringUtils;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.config.AppConfigManager;
import mtopsdk.mtop.cache.domain.ApiCacheDo;
import mtopsdk.mtop.common.MtopListener;
import mtopsdk.mtop.domain.MtopResponse;
import mtopsdk.mtop.global.SDKUtils;
import mtopsdk.mtop.global.SwitchConfig;
import mtopsdk.network.domain.Request;
import mtopsdk.xstate.XState;

public class CacheManagerImpl implements CacheManager {
    private static final String METHOD_GET = "GET";
    private static final String QUERY_KEY_DATA = "data";
    private static final String QUERY_KEY_WUA = "wua";
    private static final String TAG = "mtopsdk.CacheManagerImpl";
    private Cache cache = null;

    public CacheManagerImpl(Cache cache2) {
        this.cache = cache2;
    }

    public boolean isNeedReadCache(Request request, MtopListener mtopListener) {
        if (!SwitchConfig.getInstance().isGlobalCacheSwitchOpen()) {
            TBSdkLog.i(TAG, request.seqNo, "[isNeedReadCache]GlobalCacheSwitch=false,Don't read local cache.");
            return false;
        } else if (request != null && "GET".equalsIgnoreCase(request.method)) {
            return !HttpHeaderConstant.NO_CACHE.equalsIgnoreCase(request.header("cache-control"));
        } else {
            return false;
        }
    }

    public boolean isNeedWriteCache(Request request, Map<String, List<String>> map) {
        if (!SwitchConfig.getInstance().isGlobalCacheSwitchOpen()) {
            TBSdkLog.i(TAG, request.seqNo, "[isNeedWriteCache]GlobalCacheSwitch=false,Don't write local cache.");
            return false;
        } else if (!"GET".equalsIgnoreCase(request.method) || map == null) {
            return false;
        } else {
            String singleHeaderFieldByKey = HeaderHandlerUtil.getSingleHeaderFieldByKey(map, "cache-control");
            if (singleHeaderFieldByKey != null && singleHeaderFieldByKey.contains(HttpHeaderConstant.NO_CACHE)) {
                return false;
            }
            String singleHeaderFieldByKey2 = HeaderHandlerUtil.getSingleHeaderFieldByKey(map, "last-modified");
            String singleHeaderFieldByKey3 = HeaderHandlerUtil.getSingleHeaderFieldByKey(map, HttpHeaderConstant.MTOP_X_ETAG);
            if (singleHeaderFieldByKey3 == null) {
                singleHeaderFieldByKey3 = HeaderHandlerUtil.getSingleHeaderFieldByKey(map, "etag");
            }
            if (singleHeaderFieldByKey == null && singleHeaderFieldByKey2 == null && singleHeaderFieldByKey3 == null) {
                return false;
            }
            return true;
        }
    }

    public RpcCache getCache(String str, String str2, String str3) {
        if (this.cache == null) {
            return null;
        }
        RpcCache rpcCache = this.cache.get(str, str2);
        return rpcCache != null ? handleCacheValidation(str3, rpcCache) : rpcCache;
    }

    public boolean putCache(String str, String str2, MtopResponse mtopResponse) {
        if (this.cache == null) {
            return false;
        }
        RpcCache rpcCache = new RpcCache();
        rpcCache.header = mtopResponse.getHeaderFields();
        rpcCache.body = mtopResponse.getBytedata();
        return this.cache.put(str, str2, handleResponseCacheFlag(mtopResponse.getMtopStat() != null ? mtopResponse.getMtopStat().seqNo : "", rpcCache));
    }

    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0053, code lost:
        if (r5.equals("NONE") == false) goto L_0x0074;
     */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x0078  */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x007e  */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x00b6  */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x012f  */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x0134  */
    /* JADX WARNING: Removed duplicated region for block: B:67:0x013a A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x013b A[SYNTHETIC, Splitter:B:68:0x013b] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String getCacheKey(mtopsdk.framework.domain.MtopContext r14) {
        /*
            r13 = this;
            r0 = 0
            if (r14 != 0) goto L_0x0004
            return r0
        L_0x0004:
            mtopsdk.mtop.domain.MtopRequest r1 = r14.mtopRequest
            mtopsdk.mtop.common.MtopNetworkProp r2 = r14.property
            java.lang.String r3 = r14.baseUrl
            java.util.Map<java.lang.String, java.lang.String> r4 = r14.queryParams
            if (r1 == 0) goto L_0x016e
            if (r2 == 0) goto L_0x016e
            if (r3 == 0) goto L_0x016e
            if (r4 != 0) goto L_0x0016
            goto L_0x016e
        L_0x0016:
            java.lang.String r5 = "ALL"
            mtopsdk.config.AppConfigManager r6 = mtopsdk.config.AppConfigManager.getInstance()
            java.lang.String r7 = r1.getKey()
            mtopsdk.mtop.cache.domain.ApiCacheDo r6 = r6.getApiCacheDoByKey(r7)
            r7 = 1
            if (r6 == 0) goto L_0x0031
            boolean r5 = r6.privateScope
            java.lang.String r8 = r6.cacheKeyType
            java.util.List<java.lang.String> r6 = r6.cacheKeyItems
            r12 = r8
            r8 = r5
            r5 = r12
            goto L_0x0033
        L_0x0031:
            r6 = r0
            r8 = 1
        L_0x0033:
            r9 = -1
            int r10 = r5.hashCode()
            r11 = 64897(0xfd81, float:9.094E-41)
            if (r10 == r11) goto L_0x006a
            r11 = 69104(0x10df0, float:9.6835E-41)
            if (r10 == r11) goto L_0x0060
            r11 = 72638(0x11bbe, float:1.01788E-40)
            if (r10 == r11) goto L_0x0056
            r11 = 2402104(0x24a738, float:3.366065E-39)
            if (r10 == r11) goto L_0x004d
            goto L_0x0074
        L_0x004d:
            java.lang.String r10 = "NONE"
            boolean r5 = r5.equals(r10)
            if (r5 == 0) goto L_0x0074
            goto L_0x0075
        L_0x0056:
            java.lang.String r7 = "INC"
            boolean r5 = r5.equals(r7)
            if (r5 == 0) goto L_0x0074
            r7 = 3
            goto L_0x0075
        L_0x0060:
            java.lang.String r7 = "EXC"
            boolean r5 = r5.equals(r7)
            if (r5 == 0) goto L_0x0074
            r7 = 2
            goto L_0x0075
        L_0x006a:
            java.lang.String r7 = "ALL"
            boolean r5 = r5.equals(r7)
            if (r5 == 0) goto L_0x0074
            r7 = 0
            goto L_0x0075
        L_0x0074:
            r7 = -1
        L_0x0075:
            switch(r7) {
                case 0: goto L_0x0134;
                case 1: goto L_0x012f;
                case 2: goto L_0x00b6;
                case 3: goto L_0x007e;
                default: goto L_0x0078;
            }
        L_0x0078:
            java.net.URL r1 = mtopsdk.mtop.protocol.converter.util.NetworkConverterUtils.initUrl(r3, r4)
            goto L_0x0138
        L_0x007e:
            if (r6 != 0) goto L_0x0086
            java.net.URL r1 = mtopsdk.mtop.protocol.converter.util.NetworkConverterUtils.initUrl(r3, r0)
            goto L_0x0138
        L_0x0086:
            java.util.Map<java.lang.String, java.lang.String> r4 = r1.dataParams
            if (r4 == 0) goto L_0x00af
            java.util.HashMap r4 = new java.util.HashMap
            java.util.Map<java.lang.String, java.lang.String> r5 = r1.dataParams
            int r5 = r5.size()
            r4.<init>(r5)
            java.util.Iterator r5 = r6.iterator()
        L_0x0099:
            boolean r6 = r5.hasNext()
            if (r6 == 0) goto L_0x00b0
            java.lang.Object r6 = r5.next()
            java.lang.String r6 = (java.lang.String) r6
            java.util.Map<java.lang.String, java.lang.String> r7 = r1.dataParams
            java.lang.Object r7 = r7.get(r6)
            r4.put(r6, r7)
            goto L_0x0099
        L_0x00af:
            r4 = r0
        L_0x00b0:
            java.net.URL r1 = mtopsdk.mtop.protocol.converter.util.NetworkConverterUtils.initUrl(r3, r4)
            goto L_0x0138
        L_0x00b6:
            if (r6 != 0) goto L_0x00ba
            java.util.List<java.lang.String> r6 = r2.cacheKeyBlackList
        L_0x00ba:
            if (r6 != 0) goto L_0x00c2
            java.net.URL r1 = mtopsdk.mtop.protocol.converter.util.NetworkConverterUtils.initUrl(r3, r4)
            goto L_0x0138
        L_0x00c2:
            java.util.Map<java.lang.String, java.lang.String> r5 = r1.dataParams
            if (r5 == 0) goto L_0x00dc
            java.util.Iterator r5 = r6.iterator()
        L_0x00ca:
            boolean r6 = r5.hasNext()
            if (r6 == 0) goto L_0x00dc
            java.lang.Object r6 = r5.next()
            java.lang.String r6 = (java.lang.String) r6
            java.util.Map<java.lang.String, java.lang.String> r7 = r1.dataParams
            r7.remove(r6)
            goto L_0x00ca
        L_0x00dc:
            java.util.Map<java.lang.String, java.lang.String> r1 = r1.dataParams
            java.lang.String r1 = mtopsdk.mtop.util.ReflectUtil.convertMapToDataStr(r1)
            java.util.HashMap r5 = new java.util.HashMap
            r5.<init>()
            java.util.Set r4 = r4.entrySet()
            java.util.Iterator r4 = r4.iterator()
        L_0x00ef:
            boolean r6 = r4.hasNext()
            if (r6 == 0) goto L_0x012a
            java.lang.Object r6 = r4.next()
            java.util.Map$Entry r6 = (java.util.Map.Entry) r6
            java.lang.String r7 = "data"
            java.lang.Object r9 = r6.getKey()
            boolean r7 = r7.equals(r9)
            if (r7 == 0) goto L_0x010f
            java.lang.Object r6 = r6.getKey()
            r5.put(r6, r1)
            goto L_0x00ef
        L_0x010f:
            java.lang.String r7 = "wua"
            java.lang.Object r9 = r6.getKey()
            java.lang.String r9 = (java.lang.String) r9
            boolean r7 = r7.equalsIgnoreCase(r9)
            if (r7 == 0) goto L_0x011e
            goto L_0x00ef
        L_0x011e:
            java.lang.Object r7 = r6.getKey()
            java.lang.Object r6 = r6.getValue()
            r5.put(r7, r6)
            goto L_0x00ef
        L_0x012a:
            java.net.URL r1 = mtopsdk.mtop.protocol.converter.util.NetworkConverterUtils.initUrl(r3, r5)
            goto L_0x0138
        L_0x012f:
            java.net.URL r1 = mtopsdk.mtop.protocol.converter.util.NetworkConverterUtils.initUrl(r3, r0)
            goto L_0x0138
        L_0x0134:
            java.net.URL r1 = mtopsdk.mtop.protocol.converter.util.NetworkConverterUtils.initUrl(r3, r4)
        L_0x0138:
            if (r1 != 0) goto L_0x013b
            return r0
        L_0x013b:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0163 }
            java.lang.String r1 = r1.getFile()     // Catch:{ Exception -> 0x0163 }
            r3.<init>(r1)     // Catch:{ Exception -> 0x0163 }
            if (r8 == 0) goto L_0x0153
            mtopsdk.mtop.intf.Mtop r1 = r14.mtopInstance     // Catch:{ Exception -> 0x0163 }
            java.lang.String r4 = r2.userInfo     // Catch:{ Exception -> 0x0163 }
            java.lang.String r1 = r1.getMultiAccountUserId(r4)     // Catch:{ Exception -> 0x0163 }
            if (r1 == 0) goto L_0x0153
            r3.append(r1)     // Catch:{ Exception -> 0x0163 }
        L_0x0153:
            java.lang.String r1 = r2.ttid     // Catch:{ Exception -> 0x0163 }
            boolean r2 = mtopsdk.common.util.StringUtils.isNotBlank(r1)     // Catch:{ Exception -> 0x0163 }
            if (r2 == 0) goto L_0x015e
            r3.append(r1)     // Catch:{ Exception -> 0x0163 }
        L_0x015e:
            java.lang.String r1 = r3.toString()     // Catch:{ Exception -> 0x0163 }
            return r1
        L_0x0163:
            r1 = move-exception
            java.lang.String r2 = "mtopsdk.CacheManagerImpl"
            java.lang.String r14 = r14.seqNo
            java.lang.String r3 = "[getCacheKey] getCacheKey error."
            mtopsdk.common.util.TBSdkLog.e(r2, r14, r3, r1)
            return r0
        L_0x016e:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: mtopsdk.mtop.cache.CacheManagerImpl.getCacheKey(mtopsdk.framework.domain.MtopContext):java.lang.String");
    }

    public String getBlockName(String str) {
        ApiCacheDo apiCacheDoByKey;
        if (StringUtils.isBlank(str) || (apiCacheDoByKey = AppConfigManager.getInstance().getApiCacheDoByKey(str)) == null || apiCacheDoByKey.blockName == null) {
            return "";
        }
        return apiCacheDoByKey.blockName;
    }

    @Deprecated
    public String getBlockName(String str, String str2) {
        return getBlockName(StringUtils.concatStr2LowerCase(str, str2));
    }

    private RpcCache handleResponseCacheFlag(String str, RpcCache rpcCache) {
        if (rpcCache == null || rpcCache.header == null) {
            return rpcCache;
        }
        Map<String, List<String>> map = rpcCache.header;
        String singleHeaderFieldByKey = HeaderHandlerUtil.getSingleHeaderFieldByKey(map, "last-modified");
        String singleHeaderFieldByKey2 = HeaderHandlerUtil.getSingleHeaderFieldByKey(map, "cache-control");
        String singleHeaderFieldByKey3 = HeaderHandlerUtil.getSingleHeaderFieldByKey(map, HttpHeaderConstant.MTOP_X_ETAG);
        if (singleHeaderFieldByKey3 == null) {
            singleHeaderFieldByKey3 = HeaderHandlerUtil.getSingleHeaderFieldByKey(map, "etag");
        }
        if (singleHeaderFieldByKey2 == null && singleHeaderFieldByKey == null && singleHeaderFieldByKey3 == null) {
            return rpcCache;
        }
        if (StringUtils.isNotBlank(singleHeaderFieldByKey2) && StringUtils.isNotBlank(singleHeaderFieldByKey)) {
            rpcCache.lastModified = singleHeaderFieldByKey;
            rpcCache.cacheCreateTime = MtopUtils.convertTimeFormatGMT2Long(singleHeaderFieldByKey);
            String[] split = singleHeaderFieldByKey2.split(",");
            if (split != null) {
                for (String str2 : split) {
                    try {
                        if (str2.contains("max-age=")) {
                            rpcCache.maxAge = Long.parseLong(str2.substring("max-age=".length()));
                        } else if (HttpHeaderConstant.OFFLINE_FLAG_ON.equalsIgnoreCase(str2)) {
                            rpcCache.offline = true;
                        }
                    } catch (Exception unused) {
                        TBSdkLog.w(TAG, str, "[handleResponseCacheFlag] parse cacheControlStr error." + singleHeaderFieldByKey2);
                    }
                }
            }
        }
        if (StringUtils.isNotBlank(singleHeaderFieldByKey3)) {
            rpcCache.etag = singleHeaderFieldByKey3;
        }
        return rpcCache;
    }

    private RpcCache handleCacheValidation(String str, RpcCache rpcCache) {
        if (rpcCache == null) {
            return rpcCache;
        }
        if (rpcCache.body == null) {
            rpcCache.cacheStatus = RpcCache.CacheStatus.TIMEOUT;
            return rpcCache;
        } else if (rpcCache.lastModified == null && rpcCache.etag == null) {
            if (rpcCache.offline) {
                rpcCache.cacheStatus = RpcCache.CacheStatus.NEED_UPDATE;
            } else {
                rpcCache.cacheStatus = RpcCache.CacheStatus.TIMEOUT;
            }
            return rpcCache;
        } else {
            if (StringUtils.isNotBlank(rpcCache.lastModified)) {
                long j = rpcCache.cacheCreateTime;
                long j2 = rpcCache.maxAge;
                long correctionTime = SDKUtils.getCorrectionTime();
                if (correctionTime >= j && correctionTime <= j + j2) {
                    rpcCache.cacheStatus = RpcCache.CacheStatus.FRESH;
                } else if (rpcCache.offline) {
                    rpcCache.cacheStatus = RpcCache.CacheStatus.NEED_UPDATE;
                } else {
                    rpcCache.cacheStatus = RpcCache.CacheStatus.TIMEOUT;
                }
                if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                    StringBuilder sb = new StringBuilder(128);
                    sb.append("[handleCacheValidation]cacheStatus=");
                    sb.append(rpcCache.cacheStatus);
                    sb.append(";lastModifiedStr=");
                    sb.append(rpcCache.lastModified);
                    sb.append(";lastModified=");
                    sb.append(j);
                    sb.append(";maxAge=");
                    sb.append(j2);
                    sb.append(";currentTime=");
                    sb.append(correctionTime);
                    sb.append(";t_offset=");
                    sb.append(XState.getTimeOffset());
                    TBSdkLog.i(TAG, str, sb.toString());
                }
            } else if (StringUtils.isNotBlank(rpcCache.etag)) {
                rpcCache.cacheStatus = RpcCache.CacheStatus.NEED_UPDATE;
            }
            return rpcCache;
        }
    }
}
