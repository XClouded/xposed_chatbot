package com.alimama.moon.ui;

import alimama.com.unwrouter.UNWRouter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.alibaba.android.umbrella.utils.UmbrellaConstants;
import com.alimama.moon.App;
import com.alimama.moon.R;
import com.alimama.moon.ui.IWizardContract;
import com.alimama.moon.utils.CommonUtils;
import com.alimama.union.app.aalogin.ILogin;
import com.alimama.union.app.pagerouter.AppPageInfo;
import com.alimama.union.app.pagerouter.MoonComponentManager;
import com.alimama.union.app.privacy.PrivacyInterface;
import com.alimama.union.app.privacy.PrivacyPermissionManager;
import com.alimama.union.app.privacy.PrivacyUtil;
import com.rd.PageIndicatorView;
import com.uc.webview.export.media.MessageID;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WizardActivity extends PageRouterActivity implements IWizardContract.IWizardView, PrivacyInterface {
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) WizardActivity.class);
    @Inject
    ILogin login;
    private View makeMoneyBtn;
    private PageIndicatorView pageIndicatorView;
    @Inject
    UNWRouter pageRouter;
    /* access modifiers changed from: private */
    public IWizardContract.IWizardPresenter presenter;
    private View skipBtn;
    private ViewPager viewPager;
    private WizardAdapter viewPagerAdapter;

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle bundle) {
        hideSystemUI();
        super.onCreate(bundle);
        if (CommonUtils.isMonkeyBuild()) {
            startActivity(new Intent(this, MonkeyActivity.class));
            finish();
            return;
        }
        logger.info(UmbrellaConstants.LIFECYCLE_CREATE);
        if ((getIntent().getFlags() & 4194304) != 0) {
            finish();
            return;
        }
        App.getAppComponent().inject(this);
        this.login.registerReceiver();
        this.login.autoLogin();
        this.presenter = new WizardPresenter(this);
        if (PrivacyUtil.isPrivacyDialogAppeared(this)) {
            goToNextPage();
        } else {
            new PrivacyPermissionManager((Context) this, (PrivacyInterface) this).initPrivacyDialog();
        }
    }

    private void init() {
        setContentView((int) R.layout.activity_wizard);
        this.viewPager = (ViewPager) findViewById(R.id.view_pager);
        this.pageIndicatorView = (PageIndicatorView) findViewById(R.id.pageIndicatorView);
        this.makeMoneyBtn = findViewById(R.id.btn_make_money);
        this.skipBtn = findViewById(R.id.btn_skip);
        this.viewPagerAdapter = new WizardAdapter(getSupportFragmentManager(), this.presenter);
        this.viewPager.setAdapter(this.viewPagerAdapter);
        this.pageIndicatorView.setCount(this.viewPagerAdapter.getCount());
        this.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int i) {
            }

            public void onPageScrolled(int i, float f, int i2) {
            }

            public void onPageSelected(int i) {
                WizardActivity.this.presenter.selectWizard(i);
            }
        });
        this.makeMoneyBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                WizardActivity.this.presenter.clickMakeMoney();
            }
        });
        this.skipBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                WizardActivity.this.presenter.clickSkipBtn();
            }
        });
    }

    public void goToNextPage() {
        if (this.presenter.init()) {
            init();
            this.presenter.selectWizard(this.viewPager.getCurrentItem());
        }
    }

    private void hideSystemUI() {
        int systemUiVisibility = ((getWindow().getDecorView().getSystemUiVisibility() ^ 256) ^ 1024) ^ 4;
        if (Build.VERSION.SDK_INT >= 19) {
            systemUiVisibility ^= 4096;
        }
        getWindow().getDecorView().setSystemUiVisibility(systemUiVisibility);
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
        logger.info(UmbrellaConstants.LIFECYCLE_START);
    }

    /* access modifiers changed from: protected */
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        logger.info("onNewIntent");
    }

    /* access modifiers changed from: protected */
    public void onRestart() {
        super.onRestart();
        logger.info("onRestart");
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        logger.info(UmbrellaConstants.LIFECYCLE_RESUME);
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        logger.info(MessageID.onPause);
    }

    /* access modifiers changed from: protected */
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        logger.info("onSaveInstanceState");
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        logger.info(MessageID.onStop);
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        logger.info("onDestroy");
    }

    public void setPresenter(IWizardContract.IWizardPresenter iWizardPresenter) {
        this.presenter = iWizardPresenter;
    }

    public void showMakeMoneyBtn() {
        this.makeMoneyBtn.setVisibility(0);
    }

    public void hideIndicator() {
        this.pageIndicatorView.setVisibility(8);
    }

    public void showIndicator() {
        this.pageIndicatorView.setVisibility(0);
    }

    public void hideMakeMoneyBtn() {
        this.makeMoneyBtn.setVisibility(8);
    }

    public void showNextPage() {
        showSplashAdPage();
    }

    private void showSplashAdPage() {
        MoonComponentManager.getInstance().getPageRouter().gotoPage(AppPageInfo.PAGE_SPLASH_AD);
        finish();
    }

    private static class WizardAdapter extends FragmentStatePagerAdapter {
        private final IWizardContract.IWizardPresenter presenter;

        public WizardAdapter(FragmentManager fragmentManager, IWizardContract.IWizardPresenter iWizardPresenter) {
            super(fragmentManager);
            this.presenter = iWizardPresenter;
        }

        public Fragment getItem(int i) {
            WizardFragment wizardFragment = new WizardFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(WizardFragment.EXTRA_WIZARD_IMAGE_RES, this.presenter.getWizardImageRes(i));
            wizardFragment.setArguments(bundle);
            return wizardFragment;
        }

        public int getCount() {
            return this.presenter.getWizardCount();
        }
    }
}
