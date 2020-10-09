package com.alibaba.ut.abtest.pipeline.request;

import com.alibaba.analytics.core.ClientVariables;
import com.alibaba.ut.abtest.internal.ABContext;
import com.alibaba.ut.abtest.internal.bucketing.model.ExperimentResponseData;
import com.alibaba.ut.abtest.internal.debug.ReportLog;
import com.alibaba.ut.abtest.internal.util.SystemInformation;
import com.alibaba.ut.abtest.pipeline.Request;
import com.alibaba.ut.abtest.pipeline.RequestMethod;
import com.ut.device.UTDevice;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class RequestFactory {
    private RequestFactory() {
    }

    public static Request createExperimentRequest() {
        HashMap hashMap = new HashMap();
        hashMap.put("platform", "android");
        hashMap.put("utdid", UTDevice.getUtdid(ABContext.getInstance().getContext()));
        hashMap.put("appKey", ClientVariables.getInstance().getAppKey());
        hashMap.put("configVersion", String.valueOf(ABContext.getInstance().getDecisionService().getExperimentDataVersion()));
        hashMap.put("userId", ABContext.getInstance().getUserId());
        hashMap.put("userNick", ABContext.getInstance().getUserNick());
        hashMap.put("appVersion", SystemInformation.getInstance().getAppVersionName());
        hashMap.put("channel", SystemInformation.getInstance().getChannel());
        return new Request.Builder("/v3.0/api/experiment/allocate").setMethod(RequestMethod.POST).setParams(RequestParam.create((Map<String, Object>) hashMap)).setResponseClass(ExperimentResponseData.class).build();
    }

    public static Request createLogReportRequest(List<ReportLog> list, String str) {
        ArrayList arrayList = new ArrayList();
        for (ReportLog next : list) {
            HashMap hashMap = new HashMap();
            hashMap.put("level", next.getLevel());
            hashMap.put("content", next.getContent());
            hashMap.put("platform", "android");
            hashMap.put("source", "ab");
            hashMap.put("type", next.getType());
            hashMap.put("createTime", String.valueOf(next.getTime()));
            arrayList.add(hashMap);
        }
        RequestParam create = RequestParam.create((List<Object>) arrayList);
        HashMap hashMap2 = new HashMap();
        hashMap2.put("ab-debug-key", str);
        return new Request.Builder("/v2.0/api/experiment/uploadDebugLogs").setMethod(RequestMethod.POST).setParams(create).setHeaders(hashMap2).build();
    }
}
