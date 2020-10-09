package com.xiaomi.mipush.sdk.help;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import com.xiaomi.mipush.sdk.p;

public class HelpActivity extends Activity {
    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        p.a(this, getIntent(), (Uri) null);
        finish();
    }
}
