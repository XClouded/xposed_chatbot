package com.alimama.union.app.personalCenter;

import android.app.Activity;
import com.alimama.moon.IPresenter;
import com.alimama.moon.IView;
import com.alimama.union.app.aalogin.model.User;

public interface IPersonalContract {

    public interface IPersonalPresenter extends IPresenter {
        void clickLogin(Activity activity);

        void clickLogout();

        void getTaobaoAccountInfo();

        void onLoginStatusChange();
    }

    public interface IPersonalView extends IView<IPersonalPresenter> {
        void updateMemberInfoView(Long l);

        void updateNotLoginView();

        void updateTaobaoAccountInfoView(User user);
    }
}
