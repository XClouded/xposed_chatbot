package com.alimama.moon.ui;

import androidx.annotation.DrawableRes;
import com.alimama.moon.IPresenter;
import com.alimama.moon.IView;

public interface IWizardContract {

    public interface IWizardPresenter extends IPresenter {
        void clickMakeMoney();

        void clickSkipBtn();

        int getWizardCount();

        @DrawableRes
        int getWizardImageRes(int i);

        boolean init();

        void selectWizard(int i);
    }

    public interface IWizardView extends IView<IWizardPresenter> {
        void hideIndicator();

        void hideMakeMoneyBtn();

        void showIndicator();

        void showMakeMoneyBtn();

        void showNextPage();
    }
}
