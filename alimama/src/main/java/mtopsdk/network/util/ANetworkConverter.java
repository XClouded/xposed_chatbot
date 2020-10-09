package mtopsdk.network.util;

import android.text.TextUtils;
import anetwork.channel.Header;
import anetwork.channel.Request;
import anetwork.channel.entity.BasicHeader;
import anetwork.channel.entity.RequestImpl;
import anetwork.channel.statist.StatisticData;
import anetwork.channel.util.RequestConstant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import mtopsdk.common.util.StringUtils;
import mtopsdk.network.domain.NetworkStats;
import mtopsdk.network.domain.ParcelableRequestBodyImpl;
import mtopsdk.network.impl.ParcelableRequestBodyEntry;

public final class ANetworkConverter {
    public static List<Header> createRequestHeaders(Map<String, String> map) {
        if (map == null || map.size() < 1) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (Map.Entry next : map.entrySet()) {
            if (next != null && StringUtils.isNotBlank((String) next.getKey())) {
                arrayList.add(new BasicHeader((String) next.getKey(), (String) next.getValue()));
            }
        }
        return arrayList;
    }

    public static Request convertRequest(mtopsdk.network.domain.Request request) {
        RequestImpl requestImpl = new RequestImpl(request.url);
        requestImpl.setSeqNo(request.seqNo);
        requestImpl.setRetryTime(request.retryTimes);
        requestImpl.setConnectTimeout(request.connectTimeoutMills);
        requestImpl.setReadTimeout(request.readTimeoutMills);
        if (!TextUtils.isEmpty(request.bizIdStr)) {
            requestImpl.setBizId(request.bizIdStr);
        } else {
            requestImpl.setBizId(request.bizId);
        }
        requestImpl.setMethod(request.method);
        requestImpl.setHeaders(createRequestHeaders(request.headers));
        requestImpl.setExtProperty(RequestConstant.APPKEY, request.appKey);
        requestImpl.setExtProperty(RequestConstant.AUTH_CODE, request.authCode);
        if (!TextUtils.isEmpty(request.fullTraceId)) {
            requestImpl.setExtProperty(RequestConstant.KEY_TRACE_ID, request.fullTraceId);
        }
        switch (request.env) {
            case 0:
                requestImpl.setExtProperty(RequestConstant.ENVIRONMENT, "online");
                break;
            case 1:
                requestImpl.setExtProperty(RequestConstant.ENVIRONMENT, RequestConstant.ENV_PRE);
                break;
            case 2:
                requestImpl.setExtProperty(RequestConstant.ENVIRONMENT, "test");
                break;
        }
        if ("POST".equalsIgnoreCase(request.method)) {
            ParcelableRequestBodyImpl parcelableRequestBodyImpl = (ParcelableRequestBodyImpl) request.body;
            requestImpl.setBodyEntry(new ParcelableRequestBodyEntry(parcelableRequestBodyImpl));
            requestImpl.addHeader("Content-Type", parcelableRequestBodyImpl.contentType());
            long contentLength = parcelableRequestBodyImpl.contentLength();
            if (contentLength > 0) {
                requestImpl.addHeader("Content-Length", String.valueOf(contentLength));
            }
        }
        return requestImpl;
    }

    public static NetworkStats convertNetworkStats(StatisticData statisticData) {
        if (statisticData == null) {
            return null;
        }
        NetworkStats networkStats = new NetworkStats();
        networkStats.resultCode = statisticData.resultCode;
        networkStats.isRequestSuccess = statisticData.isRequestSuccess;
        networkStats.host = statisticData.host;
        networkStats.ip_port = statisticData.ip_port;
        networkStats.connectionType = statisticData.connectionType;
        networkStats.isSSL = statisticData.isSSL;
        networkStats.oneWayTime_ANet = statisticData.oneWayTime_ANet;
        networkStats.processTime = statisticData.processTime;
        networkStats.firstDataTime = statisticData.firstDataTime;
        networkStats.sendWaitTime = statisticData.sendBeforeTime;
        networkStats.recDataTime = statisticData.recDataTime;
        networkStats.sendSize = statisticData.sendSize;
        networkStats.recvSize = statisticData.totalSize;
        networkStats.serverRT = statisticData.serverRT;
        networkStats.dataSpeed = statisticData.dataSpeed;
        networkStats.retryTimes = statisticData.retryTime;
        return networkStats;
    }
}
