package com.huawei.hms.support.api.push;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import com.huawei.hms.support.api.ResolvePendingResult;
import com.huawei.hms.support.api.client.ApiClient;
import com.huawei.hms.support.api.client.PendingResult;
import com.huawei.hms.support.api.client.Status;
import com.huawei.hms.support.api.entity.core.CommonCode;
import com.huawei.hms.support.api.entity.push.AgreementReq;
import com.huawei.hms.support.api.entity.push.AgreementResp;
import com.huawei.hms.support.api.entity.push.DeleteTokenReq;
import com.huawei.hms.support.api.entity.push.DeleteTokenResp;
import com.huawei.hms.support.api.entity.push.EnableNotifyReq;
import com.huawei.hms.support.api.entity.push.EnableNotifyResp;
import com.huawei.hms.support.api.entity.push.PushNaming;
import com.huawei.hms.support.api.entity.push.PushStateReq;
import com.huawei.hms.support.api.entity.push.PushStateResp;
import com.huawei.hms.support.api.entity.push.TokenReq;
import com.huawei.hms.support.api.push.b.a.a.c;
import com.huawei.hms.support.api.push.b.a.b;
import com.huawei.hms.support.api.push.b.b.d;
import com.huawei.hms.support.log.a;
import com.vivo.push.PushClientConstants;
import java.util.List;
import java.util.Map;

public class HuaweiPushApiImp implements HuaweiPushApi {
    public PendingResult<TokenResult> getToken(ApiClient apiClient) {
        Context context = apiClient.getContext();
        a.b("HuaweiPushApiImp", "get token, pkgName:" + context.getPackageName());
        c cVar = new c(context, "push_client_self_info");
        TokenReq tokenReq = new TokenReq();
        tokenReq.setPackageName(apiClient.getPackageName());
        if (!cVar.a("hasRequestAgreement")) {
            tokenReq.setFirstTime(true);
            cVar.a("hasRequestAgreement", true);
        } else {
            tokenReq.setFirstTime(false);
        }
        return new GetTokenPendingResultImpl(apiClient, PushNaming.getToken, tokenReq);
    }

    public void deleteToken(ApiClient apiClient, String str) throws PushException {
        Context context = apiClient.getContext();
        a.b("HuaweiPushApiImp", "invoke method: deleteToken, pkgName:" + context.getPackageName());
        if (!TextUtils.isEmpty(str)) {
            try {
                if (str.equals(b.a(context, "push_client_self_info"))) {
                    b.b(context, "push_client_self_info");
                }
                DeleteTokenReq deleteTokenReq = new DeleteTokenReq();
                deleteTokenReq.setPkgName(context.getPackageName());
                deleteTokenReq.setToken(str);
                ResolvePendingResult.build(apiClient, PushNaming.deleteToken, deleteTokenReq, DeleteTokenResp.class).get();
                com.huawei.hms.support.api.push.b.a.a.a.a(apiClient, PushNaming.deleteToken);
            } catch (Exception e) {
                a.a("HuaweiPushApiImp", "delete token failed, e=" + e.getMessage());
                throw new PushException(e + PushException.EXCEPITON_DEL_TOKEN_FAILED);
            }
        } else {
            a.a("HuaweiPushApiImp", "token is null, can not deregister token");
            throw new PushException(PushException.EXCEPITON_TOKEN_INVALID);
        }
    }

    public PendingResult<HandleTagsResult> setTags(ApiClient apiClient, Map<String, String> map) throws PushException {
        return new b().a(apiClient, map);
    }

    public PendingResult<GetTagResult> getTags(ApiClient apiClient) throws PushException {
        return new b().a(apiClient);
    }

    public PendingResult<HandleTagsResult> deleteTags(ApiClient apiClient, List<String> list) throws PushException {
        return new b().a(apiClient, list);
    }

    public boolean getPushState(ApiClient apiClient) {
        PushStateReq pushStateReq = new PushStateReq();
        pushStateReq.setPkgName(apiClient.getPackageName());
        ResolvePendingResult.build(apiClient, PushNaming.getPushState, pushStateReq, PushStateResp.class).get();
        com.huawei.hms.support.api.push.b.a.a.a.a(apiClient, PushNaming.getPushState);
        return true;
    }

    public void enableReceiveNormalMsg(ApiClient apiClient, boolean z) {
        a.b("HuaweiPushApiImp", "invoke enableReceiveNormalMsg, set flag:" + z);
        new c(apiClient.getContext(), "push_switch").a("normal_msg_enable", z ^ true);
        com.huawei.hms.support.api.push.b.a.a.a.a(apiClient, PushNaming.enableReceiveNormalMsg);
    }

    public Status enableReceiveNotifyMsg(ApiClient apiClient, boolean z) {
        a.b("HuaweiPushApiImp", "invoke enableReceiveNotifyMsg, set flag:" + z);
        Context context = apiClient.getContext();
        if (!com.huawei.hms.support.api.push.b.a.a(context)) {
            if (!apiClient.isConnected()) {
                return new Status(CommonCode.ErrorCode.CLIENT_API_INVALID);
            }
            EnableNotifyReq enableNotifyReq = new EnableNotifyReq();
            enableNotifyReq.setPackageName(apiClient.getPackageName());
            enableNotifyReq.setEnable(z);
            ResolvePendingResult.build(apiClient, PushNaming.setNotifyFlag, enableNotifyReq, EnableNotifyResp.class).get();
        } else if (com.huawei.hms.support.api.push.b.a.c(context) < 90101310) {
            Intent putExtra = new Intent("com.huawei.intent.action.SELF_SHOW_FLAG").putExtra("enalbeFlag", d.a(context, context.getPackageName() + "#" + z));
            putExtra.setPackage("android");
            context.sendBroadcast(putExtra);
        } else {
            new c(context, "push_notify_flag").a("notify_msg_enable", !z);
            Uri parse = Uri.parse("content://" + context.getPackageName() + ".huawei.push.provider/" + "push_notify_flag" + ".xml");
            Intent intent = new Intent("com.huawei.android.push.intent.SDK_COMMAND");
            intent.putExtra("type", "enalbeFlag");
            intent.putExtra(PushClientConstants.TAG_PKG_NAME, apiClient.getPackageName());
            intent.putExtra("url", parse);
            intent.setPackage("android");
            context.sendBroadcast(intent);
        }
        return Status.SUCCESS;
    }

    public void queryAgreement(ApiClient apiClient) throws PushException {
        a.b("HuaweiPushApiImp", "invoke queryAgreement");
        Context context = apiClient.getContext();
        AgreementReq agreementReq = new AgreementReq();
        agreementReq.setPkgName(context.getPackageName());
        String a = b.a(context, "push_client_self_info");
        if (!new c(context, "push_client_self_info").a("hasRequestAgreement")) {
            agreementReq.setFirstTime(true);
        } else {
            agreementReq.setFirstTime(false);
        }
        agreementReq.setToken(a);
        ResolvePendingResult.build(apiClient, PushNaming.handleAgreement, agreementReq, AgreementResp.class).get();
    }
}
