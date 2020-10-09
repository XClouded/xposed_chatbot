package com.taobao.weex;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.taobao.windvane.extra.uc.WVUCWebViewClient;
import android.taobao.windvane.extra.uc.WVUCWebViewFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.taobao.android.nav.Nav;
import com.taobao.tao.BaseActivity;
import com.taobao.weex.adapter.R;
import com.uc.webview.export.WebView;

public class WxBriefWvActivity extends BaseActivity {
    /* JADX WARNING: type inference failed for: r1v0, types: [android.content.Context, com.taobao.weex.WxBriefWvActivity, com.taobao.tao.BaseActivity, android.app.Activity] */
    public void onCreate(Bundle bundle, PersistableBundle persistableBundle) {
        WxBriefWvActivity.super.onCreate(bundle, persistableBundle);
        setContentView(R.layout.wx_brief_wv_layout);
        Bundle extras = getIntent().getExtras();
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        WVUCWebViewFragment wVUCWebViewFragment = new WVUCWebViewFragment(this);
        wVUCWebViewFragment.setArguments(extras);
        beginTransaction.add(R.id.wx_brief_wv_container, (Fragment) wVUCWebViewFragment);
        beginTransaction.commit();
        wVUCWebViewFragment.setWebViewClient(new WVUCWebViewClient(this) {
            public boolean shouldOverrideUrlLoading(WebView webView, String str) {
                return Nav.from((Context) this.mContext.get()).toUri(str);
            }
        });
    }
}
