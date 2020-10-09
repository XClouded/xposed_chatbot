package com.ali.user.mobile.service;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import com.ali.user.mobile.model.RegistParam;
import com.ali.user.mobile.model.UrlParam;

public interface NavigatorService {
    void navToLoginPage(Context context, String str, boolean z, boolean z2);

    void openAccountBindPage(Context context, String str);

    void openBindPage(Context context, Boolean bool, String str, String str2);

    void openLoginPage(Context context, String str, Bundle bundle);

    void openRegisterPage(Context context, RegistParam registParam);

    void openWebViewPage(Context context, UrlParam urlParam);

    void startWebViewForResult(Activity activity, UrlParam urlParam);
}
