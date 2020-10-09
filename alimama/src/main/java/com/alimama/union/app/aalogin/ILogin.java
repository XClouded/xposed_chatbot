package com.alimama.union.app.aalogin;

import android.app.Activity;
import com.alimama.union.app.aalogin.model.User;

public interface ILogin {

    public interface ILoginListener {
        void onLoginCancel();

        void onLoginFailure(String str);

        void onLoginSuccess();

        void onLogoutSuccess();
    }

    void autoLogin();

    boolean checkSessionValid();

    void clearAccount();

    String getAvatarLink();

    String getEcode();

    String getNick();

    String getSid();

    User getUser();

    String getUserId();

    boolean isLogining();

    void launchTaobao(Activity activity);

    void logout();

    void logout(ILoginListener iLoginListener);

    void registerReceiver();

    void saveAccount();

    void showLoginChooserUI();

    void showLoginUI(Activity activity);

    void unregisterReceiver();
}
