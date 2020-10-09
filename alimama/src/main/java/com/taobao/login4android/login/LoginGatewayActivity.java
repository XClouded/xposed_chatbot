package com.taobao.login4android.login;

import android.content.Intent;
import android.os.Bundle;
import com.ali.user.mobile.base.ui.BaseActivity;
import com.ali.user.mobile.login.model.LoginConstant;
import com.taobao.login4android.scan.QrScanActivity;

public class LoginGatewayActivity extends BaseActivity {
    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (getIntent() != null && getIntent().getData() != null) {
            getIntent().getData().getHost();
            if ("/qrcodeCheck.htm".equals(getIntent().getData().getPath())) {
                String uri = getIntent().getData().toString();
                Intent intent = new Intent(this, QrScanActivity.class);
                intent.putExtra(LoginConstant.SCAN_KEY, uri);
                startActivity(intent);
                finish();
            }
        }
    }
}
