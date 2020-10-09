package com.ali.user.mobile.service;

import android.app.Activity;
import com.ali.user.mobile.login.model.SNSSignInAccount;
import com.ali.user.mobile.model.CommonCallback;
import com.taobao.android.sns4android.SNSPlatform;

public interface SNSBindService {
    void bind(Activity activity, SNSPlatform sNSPlatform, CommonCallback commonCallback);

    void doBind(Activity activity, SNSSignInAccount sNSSignInAccount, CommonCallback commonCallback);

    void doChangeBind(SNSSignInAccount sNSSignInAccount, String str, String str2, CommonCallback commonCallback);
}
