package com.alimama.union.app.personalCenter.presenter;

import android.app.Activity;
import com.alimama.union.app.aalogin.ILogin;
import com.alimama.union.app.aalogin.model.User;
import com.alimama.union.app.personalCenter.IPersonalContract;

public class PersonalPresenter implements IPersonalContract.IPersonalPresenter {
    private ILogin login;
    private IPersonalContract.IPersonalView view;

    public PersonalPresenter(IPersonalContract.IPersonalView iPersonalView, ILogin iLogin) {
        this.view = iPersonalView;
        this.login = iLogin;
        this.view.setPresenter(this);
    }

    public void getTaobaoAccountInfo() {
        if (this.login.checkSessionValid()) {
            User user = this.login.getUser();
            this.view.updateTaobaoAccountInfoView(user);
            this.view.updateMemberInfoView(Long.valueOf(user.getMemberId()));
            return;
        }
        this.view.updateNotLoginView();
    }

    public void clickLogin(Activity activity) {
        if (!this.login.checkSessionValid()) {
            this.login.showLoginUI(activity);
        }
    }

    public void clickLogout() {
        if (this.login.checkSessionValid()) {
            this.login.logout();
        }
    }

    public void onLoginStatusChange() {
        getTaobaoAccountInfo();
    }

    public void start() {
        getTaobaoAccountInfo();
    }
}
