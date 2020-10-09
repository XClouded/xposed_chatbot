package com.alimama.moon.ui.splashad;

import alimama.com.unwrouter.UNWRouter;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.alimama.moon.App;
import com.alimama.moon.R;
import com.alimama.moon.ui.PageRouterActivity;
import com.alimama.moon.ui.splashad.ISplashAdContract;
import com.alimama.moon.usertrack.UTHelper;
import com.alimama.union.app.aalogin.ILogin;
import com.alimama.union.app.pagerouter.AppPageInfo;
import com.alimama.union.app.pagerouter.MoonComponentManager;
import com.taobao.ju.track.constants.Constants;
import com.ut.mini.UTAnalytics;
import com.ut.mini.UTHitBuilders;
import java.util.HashMap;
import javax.inject.Inject;

public class SplashAdActivity extends PageRouterActivity implements ISplashAdContract.ISplashAdView, View.OnClickListener {
    private static final String PAGE_NAME = "Page_tblm_lmapp_SplashAd";
    private static final String SPM_CNT = "a21wq.10629445";
    private ImageView adView;
    private TextView controlView;
    @Inject
    ILogin login;
    @Inject
    UNWRouter pageRouter;
    private ISplashAdContract.ISplashAdPresenter presenter;

    public void onCreate(@Nullable Bundle bundle) {
        hideSystemUI();
        super.onCreate(bundle);
        App.getAppComponent().inject(this);
        setContentView((int) R.layout.activity_splash_ad);
        this.adView = (ImageView) findViewById(R.id.splash_ad_container_img);
        this.adView.setOnClickListener(this);
        this.controlView = (TextView) findViewById(R.id.splash_ad_container_text);
        this.controlView.setOnClickListener(this);
        this.presenter = new SplashAdPresenter(this);
        this.presenter.requestAd();
        UTAnalytics.getInstance().getDefaultTracker().updatePageName(this, PAGE_NAME);
        HashMap hashMap = new HashMap();
        hashMap.put(Constants.PARAM_OUTER_SPM_CNT, SPM_CNT);
        UTAnalytics.getInstance().getDefaultTracker().updatePageProperties(this, hashMap);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        UTAnalytics.getInstance().getDefaultTracker().pageAppear(this);
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        UTAnalytics.getInstance().getDefaultTracker().pageDisAppear(this);
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
    }

    public void setPresenter(SplashAdPresenter splashAdPresenter) {
        this.presenter = splashAdPresenter;
    }

    public void updateControlView(String str) {
        if (this.controlView != null) {
            this.controlView.setText(str);
        }
    }

    public void setAdImageview(Bitmap bitmap) {
        if (this.adView != null) {
            this.adView.setImageBitmap(bitmap);
        }
        if (this.controlView != null) {
            this.controlView.setVisibility(0);
        }
    }

    public void closeView() {
        if (this.login.checkSessionValid()) {
            MoonComponentManager.getInstance().getPageRouter().gotoPage(AppPageInfo.PAGE_HOME);
            finish();
            return;
        }
        MoonComponentManager.getInstance().getPageRouter().gotoPage(AppPageInfo.PAGE_LOGIN_CHOOSE);
        finish();
    }

    private void showLoginChooserPage() {
        MoonComponentManager.getInstance().getPageRouter().gotoPage(AppPageInfo.PAGE_LOGIN_CHOOSE);
        finish();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.splash_ad_container_img:
                UTAnalytics.getInstance().getDefaultTracker().send(new UTHitBuilders.UTControlHitBuilder(PAGE_NAME, UTHelper.CONTROL_NAME_CLICK_AD).build());
                if (this.presenter != null) {
                    this.presenter.clickAdView(this);
                    return;
                }
                return;
            case R.id.splash_ad_container_text:
                UTAnalytics.getInstance().getDefaultTracker().send(new UTHitBuilders.UTControlHitBuilder(PAGE_NAME, "click_skip").build());
                if (this.presenter != null && this.controlView != null) {
                    this.presenter.clickControlView(this.controlView.getText().toString());
                    return;
                }
                return;
            default:
                return;
        }
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i == 4) {
            return true;
        }
        return super.onKeyDown(i, keyEvent);
    }

    private void hideSystemUI() {
        int systemUiVisibility = ((getWindow().getDecorView().getSystemUiVisibility() ^ 256) ^ 1024) ^ 4;
        if (Build.VERSION.SDK_INT >= 19) {
            systemUiVisibility ^= 4096;
        }
        getWindow().getDecorView().setSystemUiVisibility(systemUiVisibility);
    }
}
