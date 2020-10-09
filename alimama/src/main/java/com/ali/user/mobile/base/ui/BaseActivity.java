package com.ali.user.mobile.base.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.ali.user.mobile.app.dataprovider.DataProvider;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.common.api.AliUserLogin;
import com.ali.user.mobile.helper.ActivityUIHelper;
import com.ali.user.mobile.helper.IDialogHelper;
import com.ali.user.mobile.log.TLogAdapter;
import com.ali.user.mobile.login.action.LoginResActions;
import com.ali.user.mobile.security.biz.R;

public class BaseActivity extends AppCompatActivity {
    private static final String TAG = "login.BaseActivity";
    private boolean hasFocus;
    protected boolean isLoginObserver;
    boolean isOpened = false;
    protected ViewGroup mContentView;
    protected IDialogHelper mDialogHelper;
    protected BroadcastReceiver mLoginReceiver;
    protected Toolbar mToolbar;
    protected ViewGroup mViewGroup;
    public boolean supportTaobaoOrAlipay = false;
    /* access modifiers changed from: private */
    public boolean waitForFocus;
    private View waitForFocusView;

    /* access modifiers changed from: protected */
    public void initViews() {
    }

    public boolean isNavIconLeftBack() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isShowNavIcon() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isShowToolbarInFragment() {
        return true;
    }

    public Toolbar getToolbar() {
        return this.mToolbar;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        onLanguageSwitchNotify();
        if (AliUserLogin.mAppreanceExtentions == null || AliUserLogin.mAppreanceExtentions.getDialogHelper() == null) {
            this.mDialogHelper = new ActivityUIHelper(this);
        } else {
            try {
                this.mDialogHelper = (IDialogHelper) AliUserLogin.mAppreanceExtentions.getDialogHelper().newInstance();
            } catch (Throwable th) {
                th.printStackTrace();
                this.mDialogHelper = new ActivityUIHelper(this);
            }
        }
        if (this.isLoginObserver) {
            this.mLoginReceiver = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("onReceive action=");
                    sb.append(intent);
                    TLogAdapter.d(BaseActivity.TAG, sb.toString() == null ? "" : intent.getAction());
                    if (LoginResActions.LOGIN_SUCCESS_ACTION.equals(intent.getAction())) {
                        BaseActivity.this.finishWhenLoginSuccess();
                    }
                }
            };
            LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(this.mLoginReceiver, new IntentFilter(LoginResActions.LOGIN_SUCCESS_ACTION));
        }
        setupViews();
        try {
            if (needToolbar()) {
                initToolBar();
            }
        } catch (Throwable th2) {
            th2.printStackTrace();
        }
        initViews();
        setListenerToRootView();
        try {
            if (AliUserLogin.mAppreanceExtentions == null || (AliUserLogin.mAppreanceExtentions != null && AliUserLogin.mAppreanceExtentions.isNeedDarkStatusBarMode())) {
                StatusBarHelper.setStatusBarMode(this, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    public void finishWhenLoginSuccess() {
        finish();
    }

    /* access modifiers changed from: protected */
    public boolean needToolbar() {
        return AliUserLogin.mAppreanceExtentions == null || AliUserLogin.mAppreanceExtentions.isNeedToolbar();
    }

    /* access modifiers changed from: protected */
    public void setDefaultTheme() {
        setTheme(R.style.AliUserAppThemeBase);
    }

    /* access modifiers changed from: protected */
    public void setupViews() {
        setContentView(R.layout.aliuser_activity_container);
        this.mViewGroup = (ViewGroup) findViewById(R.id.aliuser_main_content);
        this.mContentView = (ViewGroup) findViewById(R.id.aliuser_content);
        this.mContentView.addView((ViewGroup) getLayoutInflater().inflate(getLayoutContent(), this.mViewGroup, false));
    }

    public void setContainerBackground(int i) {
        if (this.mViewGroup != null) {
            this.mViewGroup.setBackgroundResource(i);
        }
    }

    /* access modifiers changed from: protected */
    public int getLayoutContent() {
        return R.layout.aliuser_activity_parent_default_content;
    }

    /* access modifiers changed from: protected */
    public void initToolBar() {
        ViewGroup viewGroup = (ViewGroup) getLayoutInflater().inflate(R.layout.aliuser_toolbar, this.mViewGroup, false);
        this.mToolbar = (Toolbar) viewGroup.findViewById(R.id.aliuser_toolbar);
        this.mViewGroup.addView(viewGroup, 0);
        setSupportActionBar(this.mToolbar);
        if (!isShowNavIcon()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        } else if (isNavIconLeftBack()) {
            setNavigationBackIcon();
        } else {
            setNavigationCloseIcon();
        }
        this.mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                BaseActivity.this.hideInputMethodPannel(view);
                BaseActivity.this.onBackPressed();
            }
        });
        this.mToolbar.setNavigationContentDescription(R.string.aliuser_title_back);
        if (DataProviderFactory.getDataProvider().isTaobaoApp()) {
            ViewGroup.LayoutParams layoutParams = this.mToolbar.getLayoutParams();
            layoutParams.height = (int) getResources().getDimension(R.dimen.aliuser_btn_height);
            this.mToolbar.setLayoutParams(layoutParams);
        }
        if (AliUserLogin.mAppreanceExtentions != null && !AliUserLogin.mAppreanceExtentions.isNeedToolbar()) {
            getSupportActionBar().hide();
        }
    }

    public void setNavigationBackIcon() {
        if (this.mToolbar != null) {
            this.mToolbar.setNavigationIcon(R.drawable.aliuser_ic_actionbar_back);
        }
    }

    public void setNavigationCloseIcon() {
        if (this.mToolbar != null) {
            this.mToolbar.setNavigationIcon(R.drawable.aliuser_ic_actionbar_close);
        }
    }

    public void setListenerToRootView() {
        final View findViewById = getWindow().getDecorView().findViewById(16908290);
        findViewById.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                if (findViewById.getRootView().getHeight() - findViewById.getHeight() > 100) {
                    BaseActivity.this.dismissProgressDialog();
                    boolean z = BaseActivity.this.isOpened;
                    BaseActivity.this.isOpened = true;
                } else if (BaseActivity.this.isOpened) {
                    BaseActivity.this.isOpened = false;
                }
            }
        });
    }

    public void alert(String str, String str2, String str3, DialogInterface.OnClickListener onClickListener, String str4, DialogInterface.OnClickListener onClickListener2) {
        if (this.mDialogHelper != null) {
            this.mDialogHelper.alert(this, str, str2, str3, onClickListener, str4, onClickListener2);
        }
    }

    public void toast(String str, int i) {
        if (this.mDialogHelper != null) {
            this.mDialogHelper.toast(this, str, i);
        }
    }

    /* access modifiers changed from: protected */
    public void snackBar(View view, String str, int i, String str2, View.OnClickListener onClickListener) {
        if (this.mDialogHelper != null) {
            this.mDialogHelper.snackBar(view, str, i, str2, (View.OnClickListener) null);
        }
    }

    public void showProgress(String str) {
        if (this.mDialogHelper != null) {
            this.mDialogHelper.showProgressDialog(this, str, true);
        }
    }

    public void dismissProgressDialog() {
        if (this.mDialogHelper != null) {
            this.mDialogHelper.dismissProgressDialog();
        }
    }

    public void dismissAlertDialog() {
        if (this.mDialogHelper != null) {
            this.mDialogHelper.dismissAlertDialog();
        }
    }

    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        this.hasFocus = z;
        if (z && this.waitForFocus) {
            invokeInputMethod(this.waitForFocusView);
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        onLanguageSwitchNotify();
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        onLanguageSwitchNotify();
    }

    /* access modifiers changed from: protected */
    public void onLanguageSwitchNotify() {
        if (DataProviderFactory.getDataProvider() instanceof DataProvider) {
            Configuration configuration = getResources().getConfiguration();
            configuration.locale = DataProviderFactory.getDataProvider().getCurrentLanguage();
            if (configuration.locale != null) {
                TLogAdapter.i(TAG, "current language = " + configuration.locale.toString());
                getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
                return;
            }
            TLogAdapter.i(TAG, "current language = null");
        }
    }

    /* access modifiers changed from: protected */
    public void showInputMethodPannel(View view) {
        if (this.hasFocus) {
            invokeInputMethod(view);
            return;
        }
        this.waitForFocus = true;
        this.waitForFocusView = view;
    }

    private void invokeInputMethod(final View view) {
        getWindow().getDecorView().post(new Runnable() {
            public void run() {
                try {
                    view.requestFocus();
                    ((InputMethodManager) BaseActivity.this.getSystemService("input_method")).showSoftInput(view, 0);
                    boolean unused = BaseActivity.this.waitForFocus = false;
                    BaseActivity.this.dismissProgressDialog();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /* access modifiers changed from: protected */
    public void hideInputMethodPannel(View view) {
        if (view != null) {
            try {
                ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow(view.getWindowToken(), 2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.mLoginReceiver != null) {
            LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(this.mLoginReceiver);
        }
    }
}
