package com.ali.user.mobile.login.ui;

import com.ali.user.mobile.base.BaseView;

public interface FastRegView extends BaseView {
    void onRegisterFail(int i, String str);

    void onRegisterSuccess(String str);
}
