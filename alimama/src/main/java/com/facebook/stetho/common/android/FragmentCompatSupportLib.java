package com.facebook.stetho.common.android;

import android.app.Dialog;
import android.content.res.Resources;
import android.view.View;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.facebook.stetho.common.android.FragmentCompat;
import javax.annotation.Nullable;

final class FragmentCompatSupportLib extends FragmentCompat<Fragment, DialogFragment, FragmentManager, FragmentActivity> {
    private static final DialogFragmentAccessorSupportLib sDialogFragmentAccessor = new DialogFragmentAccessorSupportLib();
    private static final FragmentAccessorSupportLib sFragmentAccessor = new FragmentAccessorSupportLib();
    private static final FragmentActivityAccessorSupportLib sFragmentActivityAccessor = new FragmentActivityAccessorSupportLib();
    private static final FragmentCompat.FragmentManagerAccessorViaReflection<FragmentManager, Fragment> sFragmentManagerAccessor = new FragmentCompat.FragmentManagerAccessorViaReflection<>();

    FragmentCompatSupportLib() {
    }

    public Class<Fragment> getFragmentClass() {
        return Fragment.class;
    }

    public Class<DialogFragment> getDialogFragmentClass() {
        return DialogFragment.class;
    }

    public Class<FragmentActivity> getFragmentActivityClass() {
        return FragmentActivity.class;
    }

    public FragmentAccessorSupportLib forFragment() {
        return sFragmentAccessor;
    }

    public DialogFragmentAccessorSupportLib forDialogFragment() {
        return sDialogFragmentAccessor;
    }

    public FragmentManagerAccessor<FragmentManager, Fragment> forFragmentManager() {
        return sFragmentManagerAccessor;
    }

    public FragmentActivityAccessorSupportLib forFragmentActivity() {
        return sFragmentActivityAccessor;
    }

    private static class FragmentAccessorSupportLib implements FragmentAccessor<Fragment, FragmentManager> {
        private FragmentAccessorSupportLib() {
        }

        @Nullable
        public FragmentManager getFragmentManager(Fragment fragment) {
            return fragment.getFragmentManager();
        }

        public Resources getResources(Fragment fragment) {
            return fragment.getResources();
        }

        public int getId(Fragment fragment) {
            return fragment.getId();
        }

        @Nullable
        public String getTag(Fragment fragment) {
            return fragment.getTag();
        }

        @Nullable
        public View getView(Fragment fragment) {
            return fragment.getView();
        }

        @Nullable
        public FragmentManager getChildFragmentManager(Fragment fragment) {
            return fragment.getChildFragmentManager();
        }
    }

    private static class DialogFragmentAccessorSupportLib extends FragmentAccessorSupportLib implements DialogFragmentAccessor<DialogFragment, Fragment, FragmentManager> {
        private DialogFragmentAccessorSupportLib() {
            super();
        }

        public Dialog getDialog(DialogFragment dialogFragment) {
            return dialogFragment.getDialog();
        }
    }

    private static class FragmentActivityAccessorSupportLib implements FragmentActivityAccessor<FragmentActivity, FragmentManager> {
        private FragmentActivityAccessorSupportLib() {
        }

        @Nullable
        public FragmentManager getFragmentManager(FragmentActivity fragmentActivity) {
            return fragmentActivity.getSupportFragmentManager();
        }
    }
}
