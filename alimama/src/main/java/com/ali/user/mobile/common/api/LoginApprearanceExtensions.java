package com.ali.user.mobile.common.api;

import com.ali.user.mobile.ui.widget.WidgetExtension;

public class LoginApprearanceExtensions extends WidgetExtension {
    protected int loginContainerBackground = -1;
    protected Class mDialogHelper;
    protected Class mNavHelper;
    protected boolean needCountryModule = true;
    protected boolean needDarkStatusBarMode = true;
    protected boolean needHelp = false;
    protected boolean needLoginToolbar = true;
    protected boolean needRegister = true;
    protected boolean needToolbar = true;

    public Class getCustomChangeBindFragment() {
        return null;
    }

    public Class getCustomGuideFragment() {
        return null;
    }

    public Class getCustomLoginFragment() {
        return null;
    }

    public Class getCustomMobileLoginFragment() {
        return null;
    }

    public Class getCustomMobileRegisterFragment() {
        return null;
    }

    public Class getCustomRegisterChinaFragment() {
        return null;
    }

    public Class getCustomRegisterCountryListFragment() {
        return null;
    }

    public Class getCustomRegisterForeignFragment() {
        return null;
    }

    public Class getCustomRegisterSMSChinaFragment() {
        return null;
    }

    public Class getCustomRegisterSMSForeignFragment() {
        return null;
    }

    public void setNavHelper(Class cls) {
        this.mNavHelper = cls;
    }

    public Class getNavHelper() {
        return this.mNavHelper;
    }

    public void setNeedHelp(boolean z) {
        this.needHelp = z;
    }

    public boolean needHelp() {
        return this.needHelp;
    }

    public boolean needRegister() {
        return this.needRegister;
    }

    public boolean needCountryModule() {
        return this.needCountryModule;
    }

    public void setNeedCountryModule(boolean z) {
        this.needCountryModule = z;
    }

    public int getLoginContainerBackground() {
        return this.loginContainerBackground;
    }

    public void setLoginContainerBackground(int i) {
        this.loginContainerBackground = i;
    }

    public boolean isNeedToolbar() {
        return this.needToolbar;
    }

    public boolean isNeedLoginToolbar() {
        return this.needLoginToolbar;
    }

    public void setNeedLoginToolbar(boolean z) {
        this.needLoginToolbar = z;
    }

    public void setNeedToolbar(boolean z) {
        this.needToolbar = z;
    }

    public boolean isNeedDarkStatusBarMode() {
        return this.needDarkStatusBarMode;
    }

    public void setNeedDarkStatusBarMode(boolean z) {
        this.needDarkStatusBarMode = z;
    }

    public void setDialogHelper(Class cls) {
        this.mDialogHelper = cls;
    }

    public Class getDialogHelper() {
        return this.mDialogHelper;
    }
}
