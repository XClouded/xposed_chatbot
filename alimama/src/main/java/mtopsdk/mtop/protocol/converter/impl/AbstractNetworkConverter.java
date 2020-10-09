package mtopsdk.mtop.protocol.converter.impl;

import android.text.TextUtils;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import mtopsdk.common.util.HttpHeaderConstant;
import mtopsdk.common.util.StringUtils;
import mtopsdk.common.util.SymbolExpUtil;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.framework.domain.MtopContext;
import mtopsdk.mtop.common.MtopCallback;
import mtopsdk.mtop.common.MtopNetworkProp;
import mtopsdk.mtop.domain.EnvModeEnum;
import mtopsdk.mtop.domain.MethodEnum;
import mtopsdk.mtop.global.MtopConfig;
import mtopsdk.mtop.global.SwitchConfig;
import mtopsdk.mtop.intf.Mtop;
import mtopsdk.mtop.protocol.converter.INetworkConverter;
import mtopsdk.mtop.protocol.converter.util.NetworkConverterUtils;
import mtopsdk.network.domain.ParcelableRequestBodyImpl;
import mtopsdk.network.domain.Request;

public abstract class AbstractNetworkConverter implements INetworkConverter {
    private static final String TAG = "mtopsdk.AbstractNetworkConverter";

    /* access modifiers changed from: protected */
    public abstract Map<String, String> getHeaderConversionMap();

    public Request convert(MtopContext mtopContext) {
        URL url;
        byte[] bArr;
        MtopNetworkProp mtopNetworkProp = mtopContext.property;
        MtopConfig mtopConfig = mtopContext.mtopInstance.getMtopConfig();
        String str = mtopContext.seqNo;
        Request.Builder builder = new Request.Builder();
        builder.seqNo(str);
        builder.reqContext(mtopNetworkProp.reqContext);
        if (!TextUtils.isEmpty(mtopNetworkProp.bizIdStr)) {
            builder.bizId(mtopNetworkProp.bizIdStr);
        } else {
            builder.bizId(mtopNetworkProp.bizId);
        }
        builder.connectTimeout(mtopNetworkProp.connTimeout);
        builder.readTimeout(mtopNetworkProp.socketTimeout);
        builder.retryTimes(mtopNetworkProp.retryTimes);
        builder.appKey(mtopNetworkProp.reqAppKey);
        builder.authCode(mtopNetworkProp.authCode);
        EnvModeEnum envModeEnum = mtopConfig.envMode;
        if (envModeEnum != null) {
            switch (envModeEnum) {
                case ONLINE:
                    builder.env(0);
                    break;
                case PREPARE:
                    builder.env(1);
                    break;
                case TEST:
                case TEST_SANDBOX:
                    builder.env(2);
                    break;
            }
        }
        MethodEnum methodEnum = mtopNetworkProp.method;
        Map<String, String> map = mtopContext.protocolParams;
        Map<String, String> map2 = mtopNetworkProp.requestHeaders;
        Map<String, String> map3 = mtopConfig.mtopGlobalHeaders;
        if (!map3.isEmpty()) {
            if (map2 != null) {
                for (Map.Entry next : map3.entrySet()) {
                    String str2 = (String) next.getKey();
                    if (!map2.containsKey(str2)) {
                        map2.put(str2, next.getValue());
                    }
                }
            } else {
                map2 = map3;
            }
        }
        Map<String, String> buildRequestHeaders = buildRequestHeaders(map, map2, mtopConfig.enableHeaderUrlEncode);
        try {
            String remove = map.remove("api");
            builder.api(remove);
            String buildBaseUrl = buildBaseUrl(mtopContext, remove, map.remove("v"));
            mtopContext.baseUrl = buildBaseUrl;
            addMtopSdkProperty(mtopContext.mtopInstance, map);
            if (mtopNetworkProp.queryParameterMap != null && !mtopNetworkProp.queryParameterMap.isEmpty()) {
                for (Map.Entry next2 : mtopNetworkProp.queryParameterMap.entrySet()) {
                    map.put(next2.getKey(), next2.getValue());
                }
            }
            Map<String, String> map4 = mtopConfig.mtopGlobalQuerys;
            if (!map4.isEmpty()) {
                for (Map.Entry next3 : map4.entrySet()) {
                    String str3 = (String) next3.getKey();
                    if (!map.containsKey(str3)) {
                        map.put(str3, next3.getValue());
                    }
                }
            }
            buildRequestHeaders.put("content-type", HttpHeaderConstant.FORM_CONTENT_TYPE);
            if (MethodEnum.POST.getMethod().equals(methodEnum.getMethod())) {
                String createParamQueryStr = NetworkConverterUtils.createParamQueryStr(map, "utf-8");
                if (createParamQueryStr != null) {
                    bArr = createParamQueryStr.getBytes("utf-8");
                    builder.method(methodEnum.getMethod(), new ParcelableRequestBodyImpl(HttpHeaderConstant.FORM_CONTENT_TYPE, bArr));
                    url = NetworkConverterUtils.initUrl(buildBaseUrl, (Map<String, String>) null);
                }
                bArr = null;
                builder.method(methodEnum.getMethod(), new ParcelableRequestBodyImpl(HttpHeaderConstant.FORM_CONTENT_TYPE, bArr));
                url = NetworkConverterUtils.initUrl(buildBaseUrl, (Map<String, String>) null);
            } else {
                if (!(mtopContext.mtopListener instanceof MtopCallback.MtopCacheListener) && !mtopNetworkProp.useCache) {
                    buildRequestHeaders.put("cache-control", HttpHeaderConstant.NO_CACHE);
                }
                mtopContext.queryParams = map;
                url = NetworkConverterUtils.initUrl(buildBaseUrl, map);
            }
        } catch (Exception unused) {
            TBSdkLog.e(TAG, "[createParamPostData]getPostData error");
        } catch (Throwable th) {
            TBSdkLog.e(TAG, str, "[convert]convert Request failed!", th);
            return null;
        }
        if (url != null) {
            mtopContext.stats.domain = url.getHost();
            builder.url(url.toString());
        }
        builder.headers(buildRequestHeaders);
        return builder.build();
    }

    /* access modifiers changed from: protected */
    public Map<String, String> buildRequestHeaders(Map<String, String> map, Map<String, String> map2, boolean z) {
        Map<String, String> headerConversionMap = getHeaderConversionMap();
        if (headerConversionMap == null) {
            TBSdkLog.e(TAG, "[buildRequestHeaders]headerConversionMap is null,buildRequestHeaders error.");
            return map2;
        }
        HashMap hashMap = new HashMap(64);
        if (map2 != null) {
            for (Map.Entry next : map2.entrySet()) {
                String str = (String) next.getKey();
                String str2 = (String) next.getValue();
                if (z) {
                    if (str2 != null) {
                        try {
                            str2 = URLEncoder.encode(str2, "utf-8");
                        } catch (Exception unused) {
                            TBSdkLog.e(TAG, "[buildRequestHeaders]urlEncode " + str + SymbolExpUtil.SYMBOL_EQUAL + str2 + "error");
                        }
                    } else {
                        str2 = null;
                    }
                }
                hashMap.put(str, str2);
            }
        }
        for (Map.Entry next2 : headerConversionMap.entrySet()) {
            String str3 = (String) next2.getKey();
            String remove = map.remove(next2.getValue());
            if (remove != null) {
                try {
                    hashMap.put(str3, URLEncoder.encode(remove, "utf-8"));
                } catch (Exception unused2) {
                    TBSdkLog.e(TAG, "[buildRequestHeaders]urlEncode " + str3 + SymbolExpUtil.SYMBOL_EQUAL + remove + "error");
                }
            }
        }
        String remove2 = map.remove("lng");
        String remove3 = map.remove("lat");
        if (!(remove2 == null || remove3 == null)) {
            StringBuilder sb = new StringBuilder();
            sb.append(remove2);
            sb.append(",");
            sb.append(remove3);
            try {
                hashMap.put(HttpHeaderConstant.X_LOCATION, URLEncoder.encode(sb.toString(), "utf-8"));
            } catch (Exception unused3) {
                TBSdkLog.e(TAG, "[buildRequestHeaders]urlEncode x-location=" + sb.toString() + "error");
            }
        }
        return hashMap;
    }

    /* access modifiers changed from: protected */
    public String buildBaseUrl(MtopContext mtopContext, String str, String str2) {
        StringBuilder sb = new StringBuilder(64);
        try {
            MtopConfig mtopConfig = mtopContext.mtopInstance.getMtopConfig();
            MtopNetworkProp mtopNetworkProp = mtopContext.property;
            mtopNetworkProp.envMode = mtopConfig.envMode;
            sb.append(mtopNetworkProp.protocol.getProtocol());
            String customDomain = getCustomDomain(mtopContext);
            if (StringUtils.isNotBlank(customDomain)) {
                sb.append(customDomain);
            } else {
                sb.append(mtopConfig.mtopDomain.getDomain(mtopContext.property.envMode));
            }
            sb.append("/");
            sb.append(mtopConfig.entrance.getEntrance());
            sb.append("/");
            sb.append(str);
            sb.append("/");
            sb.append(str2);
            sb.append("/");
        } catch (Exception e) {
            TBSdkLog.e(TAG, mtopContext.seqNo, "[buildBaseUrl] build mtop baseUrl error.", e);
        }
        return sb.toString();
    }

    private String getCustomDomain(MtopContext mtopContext) {
        MtopNetworkProp mtopNetworkProp = mtopContext.property;
        if (StringUtils.isNotBlank(mtopNetworkProp.customDomain)) {
            return mtopNetworkProp.customDomain;
        }
        switch (mtopNetworkProp.envMode) {
            case ONLINE:
                if (StringUtils.isNotBlank(mtopNetworkProp.customOnlineDomain)) {
                    return mtopNetworkProp.customOnlineDomain;
                }
                return null;
            case PREPARE:
                if (StringUtils.isNotBlank(mtopNetworkProp.customPreDomain)) {
                    return mtopNetworkProp.customPreDomain;
                }
                return null;
            case TEST:
                if (StringUtils.isNotBlank(mtopNetworkProp.customDailyDomain)) {
                    return mtopNetworkProp.customDailyDomain;
                }
                return null;
            default:
                return null;
        }
    }

    /* access modifiers changed from: protected */
    public void addMtopSdkProperty(Mtop mtop, Map<String, String> map) {
        if (SwitchConfig.getInstance().isMtopsdkPropertySwitchOpen()) {
            for (Map.Entry next : mtop.getMtopConfig().getMtopProperties().entrySet()) {
                try {
                    String str = (String) next.getKey();
                    if (StringUtils.isNotBlank(str) && str.startsWith(HttpHeaderConstant.MTOPSDK_PROPERTY_PREFIX)) {
                        map.put(str.substring(HttpHeaderConstant.MTOPSDK_PROPERTY_PREFIX.length()), (String) next.getValue());
                    }
                } catch (Exception unused) {
                    TBSdkLog.e(TAG, "[addMtopSdkProperty]get mtopsdk properties error,key=" + ((String) next.getKey()) + ",value=" + ((String) next.getValue()));
                }
            }
        }
    }
}
