package com.alimama.union.app.aalogin.view;

import alimama.com.unwrouter.UNWRouter;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import com.alibaba.android.umbrella.utils.UmbrellaConstants;
import com.alimama.moon.App;
import com.alimama.moon.R;
import com.alimama.moon.detail.DetailUrlUtil;
import com.alimama.moon.eventbus.IEventBus;
import com.alimama.moon.eventbus.ISubscriber;
import com.alimama.moon.eventbus.LoginEvent;
import com.alimama.moon.ui.PageRouterActivity;
import com.alimama.union.app.aalogin.ILogin;
import com.alimama.union.app.pagerouter.AppPageInfo;
import com.alimama.union.app.pagerouter.MoonComponentManager;
import com.uc.webview.export.media.MessageID;
import javax.inject.Inject;
import org.greenrobot.eventbus.EventBusException;
import org.greenrobot.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginChooserActivity extends PageRouterActivity implements ISubscriber {
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) LoginChooserActivity.class);
    private AlertDialog agreementDialog;
    @Inject
    IEventBus eventBus;
    @Inject
    ILogin login;
    private AlertDialog loginFailureDialog;
    @Inject
    UNWRouter pageRouter;
    private View ssoLoginButton;
    private View usernamePwdLoginButton;

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        logger.info(UmbrellaConstants.LIFECYCLE_CREATE);
        App.getAppComponent().inject(this);
        setContentView((int) R.layout.activity_login_chooser);
        this.ssoLoginButton = findViewById(R.id.sso_login_button);
        this.ssoLoginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                LoginChooserActivity.this.showTaobaoLogin();
            }
        });
        this.usernamePwdLoginButton = findViewById(R.id.username_pwd_login_button);
        this.usernamePwdLoginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                LoginChooserActivity.this.showUsernamePwdLogin();
            }
        });
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
    public void onStart() {
        super.onStart();
        logger.info(UmbrellaConstants.LIFECYCLE_START);
        registerEventBus();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        logger.info(UmbrellaConstants.LIFECYCLE_RESUME);
        if (this.login.checkSessionValid()) {
            showMainPage();
        }
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
        unregisterEventBus();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        logger.info("onDestroy");
    }

    /* access modifiers changed from: package-private */
    public void registerEventBus() {
        if (!this.eventBus.isRegistered(this)) {
            try {
                this.eventBus.register(this);
            } catch (EventBusException e) {
                logger.error("EventBus register exception: {}", (Object) e.getMessage());
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void unregisterEventBus() {
        if (this.eventBus.isRegistered(this)) {
            try {
                this.eventBus.unregister(this);
            } catch (EventBusException e) {
                logger.error("EventBus unregister exception: {}", (Object) e.getMessage());
            }
        }
    }

    private void showMainPage() {
        MoonComponentManager.getInstance().getPageRouter().gotoPage(AppPageInfo.PAGE_HOME);
        finish();
    }

    /* access modifiers changed from: private */
    public void showUsernamePwdLogin() {
        this.login.showLoginUI(this);
    }

    /* access modifiers changed from: private */
    public void showTaobaoLogin() {
        this.login.launchTaobao(this);
    }

    @Subscribe
    public void onTaobaoAccountNotSecurityEvent(LoginEvent.TaobaoAccountNotSecurityEvent taobaoAccountNotSecurityEvent) {
        if (!getLoginFailureDialog().isShowing()) {
            getLoginFailureDialog().setMessage(getString(R.string.tb_account_not_security));
            getLoginFailureDialog().show();
        }
    }

    @Subscribe
    public void onNotMatchAccountConditionEvent(LoginEvent.NotMatchAccountConditionEvent notMatchAccountConditionEvent) {
        if (!getLoginFailureDialog().isShowing()) {
            getLoginFailureDialog().setMessage(getString(R.string.tb_account_not_allow));
            getLoginFailureDialog().show();
        }
    }

    @Subscribe
    public void onNeedAgreementEvent(LoginEvent.NeedAgreementEvent needAgreementEvent) {
        if (!getNeedAgreementDialog().isShowing()) {
            getNeedAgreementDialog().show();
        }
    }

    @Subscribe
    public void onMamaAccountFrozenEvent(LoginEvent.MamaAccountFrozenEvent mamaAccountFrozenEvent) {
        if (!getLoginFailureDialog().isShowing()) {
            getLoginFailureDialog().setMessage(getString(R.string.member_account_unused));
            getLoginFailureDialog().show();
        }
    }

    @Subscribe
    public void onLoginSystemErrorEvent(LoginEvent.LoginSystemErrorEvent loginSystemErrorEvent) {
        if (!getLoginFailureDialog().isShowing()) {
            getLoginFailureDialog().setMessage(getString(R.string.server_exception));
            getLoginFailureDialog().show();
        }
    }

    @Subscribe
    public void onLoginSuccessEvent(LoginEvent.LoginSuccessEvent loginSuccessEvent) {
        showMainPage();
    }

    private AlertDialog getLoginFailureDialog() {
        if (this.loginFailureDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle((int) R.string.login_faield_info);
            builder.setPositiveButton((CharSequence) getString(R.string.sure_str), (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            builder.setNegativeButton((CharSequence) getString(R.string.opt_help), (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    this.startActivity(DetailUrlUtil.getHelpIntent(this));
                }
            });
            this.loginFailureDialog = builder.create();
        }
        return this.loginFailureDialog;
    }

    private AlertDialog getNeedAgreementDialog() {
        if (this.agreementDialog == null) {
            View inflate = View.inflate(this, R.layout.dialog_no_member, (ViewGroup) null);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setPositiveButton((CharSequence) getString(R.string.register), (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    this.startActivity(DetailUrlUtil.getRegisterIntent(this));
                }
            });
            builder.setNegativeButton((CharSequence) getString(R.string.cancel_str), (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    LoginChooserActivity.this.login.logout((ILogin.ILoginListener) null);
                    dialogInterface.cancel();
                }
            });
            builder.setView(inflate);
            this.agreementDialog = builder.create();
        }
        return this.agreementDialog;
    }
}
