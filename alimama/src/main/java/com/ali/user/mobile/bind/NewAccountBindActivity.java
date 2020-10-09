package com.ali.user.mobile.bind;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import com.ali.user.mobile.app.init.Debuggable;
import com.ali.user.mobile.common.api.AliUserLogin;
import com.ali.user.mobile.log.TLogAdapter;
import com.ali.user.mobile.webview.WebViewActivity;
import com.uc.webview.export.WebView;

public class NewAccountBindActivity extends WebViewActivity {
    private static final int BIND_ERROR = -1;
    private static final int BIND_SUCCESS = 0;
    private int mBindRet = -1;

    /* access modifiers changed from: protected */
    public boolean overrideUrlLoading(WebView webView, String str) {
        if (Debuggable.isDebug()) {
            TLogAdapter.d("login.NewAccountBindActivity", "overrideUrl:" + str);
        }
        if (str.startsWith("mailto:") || str.startsWith("tel:")) {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse(str)));
            if (AliUserLogin.mBindCaller != null) {
                AliUserLogin.mBindCaller.onBindError((Bundle) null);
            }
            setResult(0);
            finish();
            return true;
        } else if (!this.urlHelper.checkWebviewBridge(str)) {
            return super.overrideUrlLoading(webView, str);
        } else {
            if ("true".equalsIgnoreCase(Uri.parse(str).getQueryParameter("isSuc"))) {
                this.mBindRet = 0;
                if (AliUserLogin.mBindCaller != null) {
                    AliUserLogin.mBindCaller.onBindSuccess((Bundle) null);
                }
                setResult(-1);
                finish();
            }
            return true;
        }
    }

    public void onBackPressed() {
        bindCallerCallback();
        cancleOperation();
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i != 4) {
            return super.onKeyDown(i, keyEvent);
        }
        bindCallerCallback();
        finish();
        return true;
    }

    private void bindCallerCallback() {
        if (AliUserLogin.mBindCaller == null) {
            return;
        }
        if (this.mBindRet == 0) {
            AliUserLogin.mBindCaller.onBindSuccess((Bundle) null);
            setResult(-1);
            return;
        }
        AliUserLogin.mBindCaller.onBindError((Bundle) null);
        setResult(0);
    }
}
