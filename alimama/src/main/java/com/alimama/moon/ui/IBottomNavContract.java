package com.alimama.moon.ui;

import com.alimama.moon.IPresenter;
import com.alimama.moon.IView;

public interface IBottomNavContract {

    public interface IBottomNavView extends IView<IBottomNavPresenter> {
        boolean onSelectBottomNavTab(int i, boolean z);

        void showUserGuide();
    }

    public interface IBottomNavPresenter extends IPresenter {
        boolean selectBottomNavTab(int i, boolean z);
    }
}
