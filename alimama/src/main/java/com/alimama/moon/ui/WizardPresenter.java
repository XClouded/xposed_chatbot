package com.alimama.moon.ui;

import com.alimama.moon.BuildConfig;
import com.alimama.moon.R;
import com.alimama.moon.dao.SettingManager;
import com.alimama.moon.service.BeanContext;
import com.alimama.moon.ui.IWizardContract;

public class WizardPresenter implements IWizardContract.IWizardPresenter {
    private static final int[] WIZARD_IMAGES = {R.drawable.wizard1, R.drawable.wizard2, R.drawable.wizard3, R.drawable.wizard4, R.drawable.wizard5};
    private final IWizardContract.IWizardView view;

    public void start() {
    }

    public WizardPresenter(IWizardContract.IWizardView iWizardView) {
        this.view = iWizardView;
        this.view.setPresenter(this);
    }

    public int getWizardCount() {
        return WIZARD_IMAGES.length;
    }

    public int getWizardImageRes(int i) {
        return WIZARD_IMAGES[i];
    }

    public void selectWizard(int i) {
        if (i == WIZARD_IMAGES.length - 1) {
            this.view.showMakeMoneyBtn();
            this.view.hideIndicator();
            return;
        }
        this.view.showIndicator();
        this.view.hideMakeMoneyBtn();
    }

    public void clickMakeMoney() {
        this.view.showNextPage();
    }

    public void clickSkipBtn() {
        this.view.showNextPage();
    }

    public boolean init() {
        SettingManager settingManager = (SettingManager) BeanContext.get(SettingManager.class);
        if (settingManager.getLastVersion() == 734000) {
            this.view.showNextPage();
            return false;
        }
        settingManager.setLastVersion(BuildConfig.VERSION_CODE);
        return true;
    }
}
