package com.taobao.android.sso.v2.launch.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.base.helper.BroadCastHelper;
import com.ali.user.mobile.base.ui.BaseFragment;
import com.ali.user.mobile.info.AlipayInfo;
import com.ali.user.mobile.info.AppInfo;
import com.ali.user.mobile.login.action.LoginResActions;
import com.ali.user.mobile.login.model.LoginConstant;
import com.ali.user.mobile.service.NavigatorService;
import com.ali.user.mobile.service.ServiceFactory;
import com.ali.user.mobile.utils.ResourceUtil;
import com.taobao.android.sso.R;
import com.taobao.android.sso.v2.launch.SsoLogin;
import com.taobao.android.sso.v2.launch.model.ISsoRemoteParam;
import com.ut.mini.UTAnalytics;
import com.ut.mini.UTHitBuilders;

public class GuideFragment extends BaseFragment implements View.OnClickListener {
    public static final String PAGE_NAME = "Page_Login4";
    protected String KEY_GUIDE_FRAGMENT_LAYOUT = "key_fragment_guide";
    private ImageButton mAlipaySsoButton;
    private TextView mAppNameTextView;
    private LinearLayout mCloseBtn;
    private ImageView mCloseImageView;
    private BroadcastReceiver mLoginReceiver;
    private ImageButton mPwdButton;
    private ImageButton mTaobaoSsoButton;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    public void onResume() {
        super.onResume();
        UTAnalytics.getInstance().getDefaultTracker().pageDisAppear(getActivity());
        UTAnalytics.getInstance().getDefaultTracker().pageAppearDonotSkip(getActivity(), PAGE_NAME);
    }

    /* access modifiers changed from: protected */
    public int getLayoutContent() {
        return R.layout.ali_user_sso_guide_activity;
    }

    public void initViews(View view) {
        super.initViews(view);
        this.mTaobaoSsoButton = (ImageButton) view.findViewById(R.id.ali_user_guide_tb_login_btn);
        this.mAlipaySsoButton = (ImageButton) view.findViewById(R.id.ali_user_guide_alipay_login_btn);
        this.mPwdButton = (ImageButton) view.findViewById(R.id.ali_user_guide_account_login_btn);
        this.mCloseImageView = (ImageView) view.findViewById(R.id.ali_user_guide_close);
        this.mAppNameTextView = (TextView) view.findViewById(R.id.ali_user_guide_app_name);
        this.mCloseBtn = (LinearLayout) view.findViewById(R.id.ali_user_guide_close_layout);
        this.mTaobaoSsoButton.setOnClickListener(this);
        this.mAlipaySsoButton.setOnClickListener(this);
        this.mPwdButton.setOnClickListener(this);
        this.mAppNameTextView.setOnClickListener(this);
        this.mCloseBtn.setOnClickListener(this);
        if (!TextUtils.isEmpty(DataProviderFactory.getDataProvider().getGuideAppName())) {
            try {
                this.mAppNameTextView.setBackgroundDrawable(ResourceUtil.findDrawableById(DataProviderFactory.getDataProvider().getGuideAppName()));
                this.mAppNameTextView.setText("");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!TextUtils.isEmpty(DataProviderFactory.getDataProvider().getGuideBackground())) {
            try {
                view.setBackgroundDrawable(ResourceUtil.findDrawableById(DataProviderFactory.getDataProvider().getGuideBackground()));
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        if (!TextUtils.isEmpty(DataProviderFactory.getDataProvider().getGuideCloseResource())) {
            try {
                this.mCloseImageView.setBackgroundDrawable(ResourceUtil.findDrawableById(DataProviderFactory.getDataProvider().getGuideCloseResource()));
            } catch (Exception e3) {
                e3.printStackTrace();
            }
        }
        if (!TextUtils.isEmpty(DataProviderFactory.getDataProvider().getGuidePwdLoginResource())) {
            try {
                this.mPwdButton.setBackgroundDrawable(ResourceUtil.findDrawableById(DataProviderFactory.getDataProvider().getGuidePwdLoginResource()));
            } catch (Exception e4) {
                e4.printStackTrace();
            }
        }
        boolean z = DataProviderFactory.getDataProvider().isNeedTaobaoSsoGuide() && SsoLogin.isSupportTBSsoV2(DataProviderFactory.getApplicationContext());
        boolean z2 = DataProviderFactory.getDataProvider().isNeedAlipaySsoGuide() && SsoLogin.isSupportAliaySso() && !TextUtils.isEmpty(DataProviderFactory.getDataProvider().getAlipaySsoDesKey());
        boolean isNeedPwdGuide = DataProviderFactory.getDataProvider().isNeedPwdGuide();
        if (z) {
            this.mTaobaoSsoButton.setVisibility(0);
        }
        if (z2) {
            this.mAlipaySsoButton.setVisibility(0);
        }
        if (isNeedPwdGuide) {
            this.mPwdButton.setVisibility(0);
        }
        this.mLoginReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (LoginResActions.LOGIN_SUCCESS_ACTION.equals(intent.getAction()) && GuideFragment.this.mAttachedActivity != null) {
                    GuideFragment.this.mAttachedActivity.finish();
                }
            }
        };
        LocalBroadcastManager.getInstance(DataProviderFactory.getApplicationContext()).registerReceiver(this.mLoginReceiver, new IntentFilter(LoginResActions.LOGIN_SUCCESS_ACTION));
        if (z2 || z) {
            this.mAttachedActivity.supportTaobaoOrAlipay = true;
            return;
        }
        goTaobaoLoginFragment();
        this.mAttachedActivity.supportTaobaoOrAlipay = false;
    }

    /* access modifiers changed from: protected */
    public void goTaobaoLoginFragment() {
        if (ServiceFactory.getService(NavigatorService.class) != null) {
            Bundle bundle = new Bundle();
            bundle.putBoolean(LoginConstant.LAUNCH_PASS_GUIDE_FRAGMENT, true);
            ((NavigatorService) ServiceFactory.getService(NavigatorService.class)).openLoginPage(this.mAttachedActivity, "", bundle);
        }
    }

    /* access modifiers changed from: protected */
    public void onTbLoginClick(View view) {
        try {
            SsoLogin.launchTao(this.mAttachedActivity, getSsoRemoteParam());
        } catch (Exception e) {
            e.printStackTrace();
            BroadCastHelper.sendLocalBroadCast(new Intent(LoginResActions.LOGIN_NETWORK_ERROR));
            Toast.makeText(DataProviderFactory.getApplicationContext(), DataProviderFactory.getApplicationContext().getString(R.string.aliuser_network_error), 0).show();
        }
    }

    /* access modifiers changed from: protected */
    public void onAlipayLoginClick(View view) {
        try {
            SsoLogin.launchAlipay(this.mAttachedActivity);
        } catch (Exception e) {
            e.printStackTrace();
            BroadCastHelper.sendLocalBroadCast(new Intent(LoginResActions.LOGIN_NETWORK_ERROR));
            Toast.makeText(DataProviderFactory.getApplicationContext(), DataProviderFactory.getApplicationContext().getString(R.string.aliuser_network_error), 0).show();
        }
    }

    /* access modifiers changed from: protected */
    public void onAccountLoginClick(View view) {
        goTaobaoLoginFragment();
    }

    private ISsoRemoteParam getSsoRemoteParam() {
        return new ISsoRemoteParam() {
            public String getServerTime() {
                return null;
            }

            public String getTtid() {
                return DataProviderFactory.getDataProvider().getTTID();
            }

            public String getImei() {
                return DataProviderFactory.getDataProvider().getImei();
            }

            public String getImsi() {
                return DataProviderFactory.getDataProvider().getImsi();
            }

            public String getDeviceId() {
                return DataProviderFactory.getDataProvider().getDeviceId();
            }

            public String getAppKey() {
                return DataProviderFactory.getDataProvider().getAppkey();
            }

            public String getApdid() {
                return AlipayInfo.getInstance().getApdid();
            }

            public String getUmidToken() {
                return AppInfo.getInstance().getUmidToken();
            }

            public String getAtlas() {
                if (DataProviderFactory.getDataProvider().getEnvType() == 1) {
                    return "daily";
                }
                return null;
            }
        };
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.mLoginReceiver != null) {
            LocalBroadcastManager.getInstance(DataProviderFactory.getApplicationContext()).unregisterReceiver(this.mLoginReceiver);
        }
    }

    /* access modifiers changed from: protected */
    public void onCloseClick(View view) {
        BroadCastHelper.sendLocalBroadCast(new Intent(LoginResActions.LOGIN_CANCEL_ACTION));
        this.mAttachedActivity.finish();
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.ali_user_guide_close_layout) {
            UTAnalytics.getInstance().getDefaultTracker().send(new UTHitBuilders.UTControlHitBuilder(PAGE_NAME, "Button-GuideClose").build());
            onCloseClick(view);
        } else if (id == R.id.ali_user_guide_tb_login_btn) {
            UTAnalytics.getInstance().getDefaultTracker().send(new UTHitBuilders.UTControlHitBuilder(PAGE_NAME, "Button-TaoSSO").build());
            onTbLoginClick(view);
        } else if (id == R.id.ali_user_guide_alipay_login_btn) {
            UTAnalytics.getInstance().getDefaultTracker().send(new UTHitBuilders.UTControlHitBuilder(PAGE_NAME, "Button-AlipaySSO").build());
            onAlipayLoginClick(view);
        } else if (id == R.id.ali_user_guide_account_login_btn) {
            UTAnalytics.getInstance().getDefaultTracker().send(new UTHitBuilders.UTControlHitBuilder(PAGE_NAME, "Button-PwdLogin").build());
            onAccountLoginClick(view);
        }
    }
}
