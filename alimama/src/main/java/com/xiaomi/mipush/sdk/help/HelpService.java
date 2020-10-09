package com.xiaomi.mipush.sdk.help;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import com.xiaomi.mipush.sdk.p;

public class HelpService extends IntentService {
    public HelpService() {
        super("intentService");
    }

    /* access modifiers changed from: protected */
    public void onHandleIntent(Intent intent) {
        if (!TextUtils.isEmpty(intent.getStringExtra("awake_info"))) {
            p.a(this, intent, (Uri) null);
        }
    }
}
