package com.alimama.moon.ui;

import com.alimama.moon.dao.SettingManager;
import com.alimama.moon.service.BeanContext;
import com.alimama.moon.ui.IBottomNavContract;
import com.alimama.union.app.aalogin.ILogin;

public class BottomNavPresenter implements IBottomNavContract.IBottomNavPresenter {
    private final IBottomNavContract.IBottomNavView bottomNavView;
    private final ILogin login;

    public BottomNavPresenter(IBottomNavContract.IBottomNavView iBottomNavView, ILogin iLogin) {
        this.bottomNavView = iBottomNavView;
        this.login = iLogin;
    }

    public boolean selectBottomNavTab(int i, boolean z) {
        return this.bottomNavView.onSelectBottomNavTab(i, z);
    }

    public void start() {
        if (((SettingManager) BeanContext.get(SettingManager.class)).isUserGuideFirst()) {
            this.bottomNavView.showUserGuide();
        }
    }
}
