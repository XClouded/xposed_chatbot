package com.ali.user.mobile.login.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.base.UIBaseConstants;
import com.ali.user.mobile.coordinator.CoordinatorWrapper;
import com.ali.user.mobile.log.UserTrackAdapter;
import com.ali.user.mobile.login.LoginType;
import com.ali.user.mobile.login.model.LoginConstant;
import com.ali.user.mobile.login.presenter.FaceLoginPresenter;
import com.ali.user.mobile.login.presenter.UserLoginPresenter;
import com.ali.user.mobile.model.LoginParam;
import com.ali.user.mobile.model.RegionInfo;
import com.ali.user.mobile.model.RegistParam;
import com.ali.user.mobile.rpc.HistoryAccount;
import com.ali.user.mobile.rpc.LoginHistory;
import com.ali.user.mobile.rpc.RpcResponse;
import com.ali.user.mobile.security.SecurityGuardManagerWraper;
import com.ali.user.mobile.service.FaceService;
import com.ali.user.mobile.service.NavigatorService;
import com.ali.user.mobile.service.ServiceFactory;
import com.ali.user.mobile.ui.R;
import com.ali.user.mobile.ui.widget.BottomMenuFragment;
import com.ali.user.mobile.ui.widget.CircleImageView;
import com.ali.user.mobile.ui.widget.MenuItem;
import com.ali.user.mobile.ui.widget.MenuItemOnClickListener;
import com.ali.user.mobile.utils.StringUtil;
import com.alibaba.fastjson.JSON;
import java.util.ArrayList;
import java.util.List;

public class BaseFaceLoginFragment extends BaseLoginFragment implements FaceLoginView {
    protected String mCurrentSelectedAccount;
    protected FaceLoginPresenter mFaceLoginPresenter;
    protected CircleImageView mHeadImageView;
    protected TextView mPwdLoginTextView;
    protected TextView mSMSLoginTextView;
    protected Button mScanLoginButton;
    protected TextView mShowIdTextView;
    protected UserLoginPresenter mUserLoginPresenter;

    public String getPageName() {
        return "Page_FaceLogin";
    }

    /* access modifiers changed from: protected */
    public void onDeleteAccount() {
    }

    public void onGetRegion(List<RegionInfo> list) {
    }

    public void onNeedVerification(String str, int i) {
    }

    public void setLoginAccountInfo(String str) {
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        initParams();
    }

    private void initParams() {
        LoginParam loginParam;
        Bundle arguments = getArguments();
        if (arguments != null) {
            String str = (String) arguments.get(UIBaseConstants.IntentExtrasNamesConstants.PARAM_LOGIN_PARAM);
            arguments.putString(UIBaseConstants.IntentExtrasNamesConstants.PARAM_LOGIN_PARAM, "");
            if (!TextUtils.isEmpty(str)) {
                loginParam = (LoginParam) JSON.parseObject(str, LoginParam.class);
                this.mUserLoginPresenter = new UserLoginPresenter(this, loginParam);
                this.mFaceLoginPresenter = new FaceLoginPresenter(this, loginParam);
            }
        }
        loginParam = null;
        this.mUserLoginPresenter = new UserLoginPresenter(this, loginParam);
        this.mFaceLoginPresenter = new FaceLoginPresenter(this, loginParam);
    }

    public void initViews(View view) {
        super.initViews(view);
        this.mHeadImageView = (CircleImageView) view.findViewById(R.id.aliuser_login_avatar);
        this.mShowIdTextView = (TextView) view.findViewById(R.id.aliuser_scan_login_account_tv);
        this.mScanLoginButton = (Button) view.findViewById(R.id.aliuser_scan_login_btn);
        this.mPwdLoginTextView = (TextView) view.findViewById(R.id.aliuser_scan_switch_pwd);
        this.mSMSLoginTextView = (TextView) view.findViewById(R.id.aliuser_scan_switch_sms);
        setOnClickListener(this.mScanLoginButton, this.mPwdLoginTextView, this.mSMSLoginTextView);
        this.mUserLoginPresenter.onStart();
        initMode();
    }

    private void initMode() {
        if (!this.mUserLoginActivity.hadReadHistory) {
            readAccountFromHistory();
        } else if (this.mUserLoginActivity != null) {
            this.isHistoryMode = true;
            switchToHistoryMode(this.mUserLoginActivity.mHistoryAccount);
        }
    }

    private void readAccountFromHistory() {
        new CoordinatorWrapper().execute(new AsyncTask<Object, Void, LoginHistory>() {
            /* access modifiers changed from: protected */
            public LoginHistory doInBackground(Object... objArr) {
                return SecurityGuardManagerWraper.getLoginHistory();
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(LoginHistory loginHistory) {
                if (BaseFaceLoginFragment.this.mUserLoginActivity != null) {
                    BaseFaceLoginFragment.this.mUserLoginActivity.hadReadHistory = true;
                    if (loginHistory == null || loginHistory.accountHistory == null || loginHistory.accountHistory.size() <= 0) {
                        BaseFaceLoginFragment.this.isHistoryMode = false;
                        BaseFaceLoginFragment.this.switchMode(BaseFaceLoginFragment.this.isHistoryMode, (HistoryAccount) null);
                        return;
                    }
                    BaseFaceLoginFragment.this.isHistoryMode = true;
                    if (BaseFaceLoginFragment.this.mUserLoginPresenter.getLoginParam() == null || (BaseFaceLoginFragment.this.mUserLoginPresenter.getLoginParam() != null && TextUtils.isEmpty(BaseFaceLoginFragment.this.mUserLoginPresenter.getLoginParam().loginAccount))) {
                        int i = loginHistory.index;
                        if (i < 0 || i >= loginHistory.accountHistory.size()) {
                            i = loginHistory.accountHistory.size() - 1;
                        }
                        BaseFaceLoginFragment.this.mUserLoginActivity.mHistoryAccount = loginHistory.accountHistory.get(i);
                        BaseFaceLoginFragment.this.switchToHistoryMode(BaseFaceLoginFragment.this.mUserLoginActivity.mHistoryAccount);
                    }
                }
            }
        }, new Object[0]);
    }

    /* access modifiers changed from: private */
    public void switchToHistoryMode(HistoryAccount historyAccount) {
        if (isActivityAvaiable() && historyAccount != null) {
            this.mCurrentSelectedAccount = historyAccount.userInputName;
            String hideAccount = StringUtil.hideAccount(this.mCurrentSelectedAccount);
            if (!TextUtils.isEmpty(hideAccount)) {
                this.mShowIdTextView.setText(hideAccount);
                updateAvatar(historyAccount.headImg);
                if (historyAccount.hasPwd == 0) {
                    this.mPwdLoginTextView.setVisibility(8);
                } else {
                    this.mPwdLoginTextView.setVisibility(0);
                }
                if (TextUtils.isEmpty(historyAccount.loginPhone)) {
                    this.mSMSLoginTextView.setVisibility(8);
                } else {
                    this.mSMSLoginTextView.setVisibility(0);
                }
            }
        }
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.aliuser_scan_login_btn) {
            UserTrackAdapter.sendControlUT(getPageName(), "Button-FaceLogin");
            onFaceLogin();
        } else if (id == R.id.aliuser_scan_switch_sms) {
            UserTrackAdapter.sendControlUT(getPageName(), "Button-ChooseSMSLogin");
            switchToSmsLogin();
        } else if (id == R.id.aliuser_scan_switch_pwd) {
            UserTrackAdapter.sendControlUT(getPageName(), "Button-ChoosePwdLogin");
            switchToPwdLogin();
        } else {
            super.onClick(view);
        }
    }

    /* access modifiers changed from: protected */
    public void showBottomMenu() {
        BottomMenuFragment bottomMenuFragment = new BottomMenuFragment();
        ArrayList arrayList = new ArrayList();
        MenuItem menuItem = new MenuItem();
        menuItem.setText(getString(R.string.aliuser_other_account_login));
        menuItem.setMenuItemOnClickListener(new MenuItemOnClickListener(bottomMenuFragment, menuItem) {
            public void onClickMenuItem(View view, MenuItem menuItem) {
                UserTrackAdapter.sendControlUT(BaseFaceLoginFragment.this.getPageName(), "Button-ChooseOtherAccountLogin");
                Intent intent = new Intent();
                intent.putExtra(LoginConstant.FORCE_NORMAL_MODE, true);
                BaseFaceLoginFragment.this.mUserLoginActivity.gotoPwdLoginFragment(intent);
            }
        });
        MenuItem menuItem2 = new MenuItem();
        menuItem2.setText(getString(R.string.aliuser_reg));
        menuItem2.setMenuItemOnClickListener(new MenuItemOnClickListener(bottomMenuFragment, menuItem2) {
            public void onClickMenuItem(View view, MenuItem menuItem) {
                UserTrackAdapter.sendControlUT(BaseFaceLoginFragment.this.getPageName(), "Button-Reg");
                RegistParam registParam = new RegistParam();
                registParam.registSite = BaseFaceLoginFragment.this.getLoginSite();
                ((NavigatorService) ServiceFactory.getService(NavigatorService.class)).openRegisterPage(BaseFaceLoginFragment.this.mAttachedActivity, registParam);
            }
        });
        MenuItem menuItem3 = new MenuItem();
        menuItem3.setText(getString(R.string.aliuser_help));
        menuItem3.setMenuItemOnClickListener(new MenuItemOnClickListener(bottomMenuFragment, menuItem3) {
            public void onClickMenuItem(View view, MenuItem menuItem) {
                if (BaseFaceLoginFragment.this.isActive()) {
                    BaseFaceLoginFragment.this.openHelp();
                }
            }
        });
        arrayList.add(menuItem);
        arrayList.add(menuItem2);
        arrayList.add(menuItem3);
        bottomMenuFragment.setMenuItems(arrayList);
        bottomMenuFragment.show(getFragmentManager(), getPageName());
    }

    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.aliuser_menu_item_more).setVisible(false);
        menu.findItem(R.id.aliuser_menu_item_help).setVisible(true);
        super.onPrepareOptionsMenu(menu);
    }

    /* access modifiers changed from: protected */
    public void onFaceLogin() {
        if (ServiceFactory.getService(FaceService.class) != null) {
            LoginParam loginParam = new LoginParam();
            loginParam.havanaId = this.mUserLoginActivity.mHistoryAccount.userId;
            loginParam.deviceTokenKey = this.mUserLoginActivity.mHistoryAccount.tokenKey;
            this.mFaceLoginPresenter.fetchScanToken(loginParam);
        }
    }

    /* access modifiers changed from: protected */
    public void switchToSmsLogin() {
        Intent intent = new Intent();
        if (!(this.mUserLoginPresenter == null || this.mUserLoginPresenter.getLoginParam() == null)) {
            LoginParam loginParam = new LoginParam();
            loginParam.source = this.mUserLoginPresenter.getLoginParam().source;
            intent.putExtra(UIBaseConstants.IntentExtrasNamesConstants.PARAM_LOGIN_PARAM, JSON.toJSONString(loginParam));
        }
        this.mUserLoginActivity.gotoMobileLoginFragment(intent);
    }

    /* access modifiers changed from: protected */
    public void switchToPwdLogin() {
        Intent intent = new Intent();
        if (!(this.mUserLoginPresenter == null || this.mUserLoginPresenter.getLoginParam() == null)) {
            LoginParam loginParam = new LoginParam();
            loginParam.source = this.mUserLoginPresenter.getLoginParam().source;
            intent.putExtra(UIBaseConstants.IntentExtrasNamesConstants.PARAM_LOGIN_PARAM, JSON.toJSONString(loginParam));
        }
        this.mUserLoginActivity.gotoPwdLoginFragment(intent);
    }

    public void toLastLoginFragment() {
        Intent intent = new Intent();
        if (!(this.mUserLoginPresenter == null || this.mUserLoginPresenter.getLoginParam() == null)) {
            LoginParam loginParam = new LoginParam();
            loginParam.source = this.mUserLoginPresenter.getLoginParam().source;
            intent.putExtra(UIBaseConstants.IntentExtrasNamesConstants.PARAM_LOGIN_PARAM, JSON.toJSONString(loginParam));
        }
        this.mUserLoginActivity.goPwdOrSMSFragment(intent);
    }

    /* access modifiers changed from: protected */
    public int getLayoutContent() {
        return R.layout.ali_user_face_fragment;
    }

    public void onSuccess(LoginParam loginParam, RpcResponse rpcResponse) {
        dismissLoading();
        this.mUserLoginPresenter.onLoginSuccess(loginParam, rpcResponse);
    }

    public void onError(RpcResponse rpcResponse) {
        this.mUserLoginPresenter.onLoginFail(rpcResponse);
    }

    public LoginType getLoginType() {
        return LoginType.TAOBAO_ACCOUNT;
    }

    public int getLoginSite() {
        if (!this.isHistoryMode || this.mUserLoginActivity.mHistoryAccount == null) {
            return DataProviderFactory.getDataProvider().getSite();
        }
        return this.mUserLoginActivity.mHistoryAccount.getLoginSite();
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.mUserLoginPresenter != null) {
            this.mUserLoginPresenter.onDestory();
        }
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        this.mUserLoginPresenter.onActivityResult(i, i2, intent);
    }
}
