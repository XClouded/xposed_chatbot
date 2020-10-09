package com.alimama.moon.push;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.annotation.Nullable;
import com.alimama.moon.ui.BaseActivity;
import com.alimama.union.app.pagerouter.MoonComponentManager;
import com.taobao.agoo.TaobaoRegister;
import org.android.agoo.common.AgooConstants;

public class AccsPushRedirectActivity extends BaseActivity {
    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        Bundle extras;
        super.onResume();
        Intent intent = getIntent();
        if (intent != null && (extras = intent.getExtras()) != null) {
            String string = extras.getString("url");
            MoonComponentManager.getInstance().getPageRouter().popUpLastActivity(this);
            if (TextUtils.isEmpty(string)) {
                string = "unionApp://ws-home";
            }
            MoonComponentManager.getInstance().getPageRouter().gotoPage(string);
            TaobaoRegister.clickMessage(this, extras.getString("messageId"), extras.getString(AgooConstants.MESSAGE_EXT));
            finish();
        }
    }
}
