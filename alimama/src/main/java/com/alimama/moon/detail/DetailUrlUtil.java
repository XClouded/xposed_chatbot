package com.alimama.moon.detail;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.alimama.moon.web.WebActivity;
import com.alimama.moon.web.WebPageIntentGenerator;

public class DetailUrlUtil {
    public static Intent getRegisterIntent(Context context) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.setData(WebPageIntentGenerator.getAgreementUrlUri().buildUpon().appendQueryParameter("title", "注册淘宝客").build());
        return intent;
    }

    public static Intent getIntent(Context context, String str, String str2) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.setData(Uri.parse(str).buildUpon().appendQueryParameter("title", str2).build());
        return intent;
    }

    public static Intent getHelpIntent(Context context) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.setData(WebPageIntentGenerator.getAccountHelpUri().buildUpon().appendQueryParameter("title", "操作帮助").build());
        return intent;
    }
}
