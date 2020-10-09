package com.huawei.hms.support.api.push;

import com.huawei.hms.core.aidl.IMessageEntity;
import com.huawei.hms.support.api.c;
import com.huawei.hms.support.api.client.ApiClient;
import com.huawei.hms.support.api.client.Status;
import com.huawei.hms.support.api.entity.push.GetTagsResp;
import com.huawei.hms.support.api.push.HmsPushConst;
import com.huawei.hms.support.log.a;

public class GetTagsPendingResultImpl extends c<GetTagResult, GetTagsResp> {
    private ApiClient a;

    public GetTagsPendingResultImpl(ApiClient apiClient, String str, IMessageEntity iMessageEntity) {
        super(apiClient, str, iMessageEntity);
        this.a = apiClient;
    }

    public GetTagResult onComplete(GetTagsResp getTagsResp) {
        GetTagResult getTagResult = new GetTagResult();
        try {
            getTagsResp.setTags(new com.huawei.hms.support.api.push.b.a.a.c(this.a.getContext(), "tags_info").a());
            getTagResult.setTagsRes(getTagsResp);
            getTagResult.setStatus(Status.SUCCESS);
        } catch (Exception e) {
            a.d("GetTagsPendingResultImpl", "get tags failed, error:" + e.getMessage());
            getTagResult.setTagsRes(getTagsResp);
            getTagResult.setStatus(new Status(HmsPushConst.ErrorCode.REPORT_SYSTEM_ERROR));
        }
        return getTagResult;
    }
}
