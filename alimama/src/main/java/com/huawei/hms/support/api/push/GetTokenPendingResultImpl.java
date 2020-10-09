package com.huawei.hms.support.api.push;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.huawei.hms.core.aidl.IMessageEntity;
import com.huawei.hms.support.api.c;
import com.huawei.hms.support.api.client.ApiClient;
import com.huawei.hms.support.api.client.Status;
import com.huawei.hms.support.api.entity.push.TokenResp;
import com.huawei.hms.support.log.a;

public class GetTokenPendingResultImpl extends c<TokenResult, TokenResp> {
    private Context a;

    public GetTokenPendingResultImpl(ApiClient apiClient, String str, IMessageEntity iMessageEntity) {
        super(apiClient, str, iMessageEntity);
        this.a = apiClient.getContext();
    }

    public TokenResult onComplete(TokenResp tokenResp) {
        TokenResult tokenResult = new TokenResult();
        a.b("GetTokenPendingResultImpl", "get token complete, the return code:" + tokenResp.getRetCode());
        tokenResult.setStatus(new Status(tokenResp.getRetCode()));
        tokenResult.setTokenRes(tokenResp);
        if (!TextUtils.isEmpty(tokenResp.getToken()) && this.a != null) {
            try {
                Intent flags = new Intent("com.huawei.android.push.intent.REGISTRATION").setPackage(this.a.getPackageName()).putExtra("device_token", tokenResp.getToken().getBytes("UTF-8")).setFlags(32);
                Context context = this.a;
                context.sendBroadcast(flags, this.a.getPackageName() + ".permission.PROCESS_PUSH_MSG");
            } catch (RuntimeException unused) {
                a.b("GetTokenPendingResultImpl", "send broadcast runtime failed");
            } catch (Exception unused2) {
                a.b("GetTokenPendingResultImpl", "send broadcast failed");
            }
        }
        return tokenResult;
    }

    /* access modifiers changed from: protected */
    public TokenResult onError(int i) {
        TokenResult tokenResult = new TokenResult();
        tokenResult.setStatus(new Status(i));
        return tokenResult;
    }
}
