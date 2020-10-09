package com.alibaba.android.anynetwork.plugin.allinone.impl;

import com.alibaba.android.anynetwork.core.ANRequest;
import com.alibaba.android.anynetwork.core.ANResponse;
import com.alibaba.android.anynetwork.plugin.allinone.IAllInOneConverter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mtopsdk.mtop.domain.MtopRequest;
import mtopsdk.mtop.domain.MtopResponse;

public class AllInOneConverterDefaultImpl implements IAllInOneConverter {
    private static final String TAG = "AllInOneConverterDefaultImpl";

    public MtopRequest convertANRequest2MtopRequest(ANRequest aNRequest) {
        MtopRequest mtopRequest = new MtopRequest();
        String networkMtopDataJsonString = aNRequest.getNetworkMtopDataJsonString();
        mtopRequest.setApiName(aNRequest.getNetworkMtopApiName());
        mtopRequest.setData(networkMtopDataJsonString);
        mtopRequest.setNeedEcode(aNRequest.getNetworkMtopNeedEcode());
        mtopRequest.setVersion(aNRequest.getNetworkMtopApiVersion());
        return mtopRequest;
    }

    public ANResponse convertMtopResponse2ANResponse(MtopResponse mtopResponse) {
        return new ANResponse().setBaseType("mtop").setBaseResponseCode(0).setNetworkIsSuccess(mtopResponse.isApiSuccess()).setNetworkResponseCode(new Long((long) mtopResponse.getResponseCode()).toString()).setNetworkResponseStringBody(mtopResponse.getDataJsonObject() == null ? null : mtopResponse.getDataJsonObject().toString()).setNetworkResponseByteBody(mtopResponse.getBytedata()).setNetworkHeader(convertHeader(mtopResponse.getHeaderFields()));
    }

    private Map<String, String> convertHeader(Map<String, List<String>> map) {
        if (map == null) {
            return null;
        }
        HashMap hashMap = new HashMap();
        for (Map.Entry next : map.entrySet()) {
            if (next != null) {
                List list = (List) next.getValue();
                if (list == null || list.size() <= 0) {
                    hashMap.put(next.getKey(), (Object) null);
                } else {
                    hashMap.put(next.getKey(), list.get(0));
                }
            }
        }
        return hashMap;
    }
}
