package com.alimama.moon.ui.splashad;

import android.app.Activity;
import android.graphics.Bitmap;
import com.alimama.moon.IPresenter;
import com.alimama.moon.IView;

public interface ISplashAdContract {

    public interface ISplashAdPresenter extends IPresenter {
        void clickAdView(Activity activity);

        void clickControlView(String str);

        void requestAd();
    }

    public interface ISplashAdView extends IView<SplashAdPresenter> {
        void closeView();

        void setAdImageview(Bitmap bitmap);

        void updateControlView(String str);
    }
}
