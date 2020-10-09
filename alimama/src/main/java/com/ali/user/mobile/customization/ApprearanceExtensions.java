package com.ali.user.mobile.customization;

import com.ali.user.mobile.common.api.LoginApprearanceExtensions;
import com.ali.user.mobile.login.ui.AliUserLoginFragment;
import com.ali.user.mobile.login.ui.AliUserMobileLoginFragment;
import com.ali.user.mobile.register.ui.AliUserMobileRegisterFragment;

public class ApprearanceExtensions extends LoginApprearanceExtensions {
    private Class<? extends AliUserLoginFragment> mCustomLoginFragment;
    private Class<? extends AliUserMobileLoginFragment> mCustomMobileLoginFragment;
    private Class<? extends AliUserMobileRegisterFragment> mCustomMobileRegisterFragment;
    private Class mDialogHelper;

    private ApprearanceExtensions(Class<? extends AliUserLoginFragment> cls, Class<? extends AliUserMobileRegisterFragment> cls2, Class<? extends AliUserMobileLoginFragment> cls3, Class cls4) {
        this.mCustomLoginFragment = cls;
        this.mCustomMobileRegisterFragment = cls2;
        this.mCustomMobileLoginFragment = cls3;
        this.mDialogHelper = cls4;
    }

    public Class<? extends AliUserLoginFragment> getCustomLoginFragment() {
        return this.mCustomLoginFragment;
    }

    public Class<? extends AliUserMobileRegisterFragment> getCustomMobileRegisterFragment() {
        return this.mCustomMobileRegisterFragment;
    }

    public Class<? extends AliUserMobileLoginFragment> getCustomMobileLoginFragment() {
        return this.mCustomMobileLoginFragment;
    }

    public Class getDialogHelper() {
        return this.mDialogHelper;
    }

    public static final class Builder {
        private Class<? extends AliUserLoginFragment> mCustomLoginFragment;
        private Class<? extends AliUserMobileLoginFragment> mCustomMobileLoginFragment;
        private Class<? extends AliUserMobileRegisterFragment> mCustomMobileRegisterFragment;
        private Class mDialogHelper;

        public Builder customLoginFragment(Class<? extends AliUserLoginFragment> cls) {
            this.mCustomLoginFragment = cls;
            return this;
        }

        public Builder customMobileRegisterFragment(Class<? extends AliUserMobileRegisterFragment> cls) {
            this.mCustomMobileRegisterFragment = cls;
            return this;
        }

        public Builder customMobileLoginFragment(Class<? extends AliUserMobileLoginFragment> cls) {
            this.mCustomMobileLoginFragment = cls;
            return this;
        }

        public Builder dialogHelper(Class cls) {
            this.mDialogHelper = cls;
            return this;
        }

        public ApprearanceExtensions build() {
            return new ApprearanceExtensions(this.mCustomLoginFragment, this.mCustomMobileRegisterFragment, this.mCustomMobileLoginFragment, this.mDialogHelper);
        }
    }
}
