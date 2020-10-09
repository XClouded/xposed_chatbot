package com.huawei.hms.support.api.push;

import com.huawei.hms.core.aidl.IMessageEntity;
import com.huawei.hms.support.api.c;
import com.huawei.hms.support.api.client.ApiClient;
import com.huawei.hms.support.api.client.Status;
import com.huawei.hms.support.api.entity.push.TagsResp;
import com.huawei.hms.support.log.a;
import org.json.JSONArray;
import org.json.JSONObject;

public class HandleTagPendingResultImpl extends c<HandleTagsResult, TagsResp> {
    private ApiClient a;

    public HandleTagPendingResultImpl(ApiClient apiClient, String str, IMessageEntity iMessageEntity) {
        super(apiClient, str, iMessageEntity);
        this.a = apiClient;
    }

    private static void a(ApiClient apiClient, String str) {
        if (apiClient == null) {
            a.a("HandleTagPendingResultImpl", "the client is null when adding or deleting tags from file.");
            return;
        }
        try {
            JSONArray a2 = com.huawei.hms.support.api.push.b.a.a.a.a(str);
            if (a2 != null) {
                com.huawei.hms.support.api.push.b.a.a.c cVar = new com.huawei.hms.support.api.push.b.a.a.c(apiClient.getContext(), "tags_info");
                int length = a2.length();
                for (int i = 0; i < length; i++) {
                    JSONObject optJSONObject = a2.optJSONObject(i);
                    if (optJSONObject != null) {
                        String optString = optJSONObject.optString("tagKey");
                        int optInt = optJSONObject.optInt("opType");
                        if (1 == optInt) {
                            cVar.a(optString, (Object) optJSONObject.optString("tagValue"));
                        } else if (2 == optInt) {
                            cVar.d(optString);
                        }
                    }
                }
            }
        } catch (Exception e) {
            a.c("HandleTagPendingResultImpl", "when adding or deleting tags from file excepiton," + e.getMessage());
        }
    }

    public HandleTagsResult onComplete(TagsResp tagsResp) {
        a.a("HandleTagPendingResultImpl", "report tag completely, retcode is:" + tagsResp.getRetCode());
        if (907122001 == tagsResp.getRetCode()) {
            a.a("HandleTagPendingResultImpl", "report tag success.");
            a(this.a, tagsResp.getContent());
        }
        HandleTagsResult handleTagsResult = new HandleTagsResult();
        handleTagsResult.setStatus(new Status(tagsResp.getRetCode()));
        handleTagsResult.setTagsRes(tagsResp);
        return handleTagsResult;
    }
}
