package com.ali.user.mobile.base.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.ali.user.mobile.app.init.Debuggable;
import com.ali.user.mobile.common.api.AliUserLogin;
import com.ali.user.mobile.helper.ActivityUIHelper;
import com.ali.user.mobile.helper.IDialogHelper;

public class BaseFragment extends Fragment {
    protected boolean isConfigureChanged = false;
    protected ActivityUIHelper mActivityHelper;
    protected BaseActivity mAttachedActivity;
    protected int mCurrentScreenOrientation = 1;
    protected IDialogHelper mDialogHelper;
    protected View mFragmentView;

    /* access modifiers changed from: protected */
    public int getLayoutContent() {
        return -1;
    }

    /* access modifiers changed from: protected */
    public void initViews(View view) {
    }

    public boolean onBackPressed() {
        return false;
    }

    /* access modifiers changed from: protected */
    public void onScreenRotate(int i) {
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setHasOptionsMenu(true);
        if (getActivity() != null && !Debuggable.isDebug()) {
            getActivity().getWindow().addFlags(8192);
        }
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(getLayoutContent(), (ViewGroup) null);
        this.mFragmentView = inflate;
        initViews(inflate);
        return inflate;
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof BaseActivity) {
            this.mAttachedActivity = (BaseActivity) activity;
        }
        if (!(AliUserLogin.mAppreanceExtentions == null || AliUserLogin.mAppreanceExtentions.getDialogHelper() == null)) {
            try {
                this.mDialogHelper = (IDialogHelper) AliUserLogin.mAppreanceExtentions.getDialogHelper().newInstance();
            } catch (Throwable unused) {
            }
        }
        this.mActivityHelper = new ActivityUIHelper(activity);
    }

    public void onHiddenChanged(boolean z) {
        super.onHiddenChanged(z);
        if (this.isConfigureChanged && !z) {
            onScreenRotate(this.mCurrentScreenOrientation);
            this.isConfigureChanged = false;
        }
    }

    public void setUserVisibleHint(boolean z) {
        super.setUserVisibleHint(z);
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mCurrentScreenOrientation = configuration.orientation;
        onScreenRotate(configuration.orientation);
    }

    /* access modifiers changed from: protected */
    public ActionBar getSupportActionBar() {
        return getAppCompatActivity().getSupportActionBar();
    }

    /* access modifiers changed from: protected */
    public AppCompatActivity getAppCompatActivity() {
        return this.mAttachedActivity;
    }

    /* access modifiers changed from: protected */
    public boolean isActivityAvaiable() {
        if (!(this.mAttachedActivity != null && (Build.VERSION.SDK_INT >= 17 ? !(this.mAttachedActivity.isFinishing() || this.mAttachedActivity.isDestroyed()) : !this.mAttachedActivity.isFinishing())) || !isAdded()) {
            return false;
        }
        return true;
    }

    public void alert(String str, String str2, String str3, DialogInterface.OnClickListener onClickListener, String str4, DialogInterface.OnClickListener onClickListener2) {
        if (this.mDialogHelper != null) {
            this.mDialogHelper.alert(getActivity(), str, str2, str3, onClickListener, str4, onClickListener2);
        } else {
            this.mActivityHelper.alert(str, str2, str3, onClickListener, str4, onClickListener2);
        }
    }

    public void alertList(String[] strArr, DialogInterface.OnClickListener onClickListener, boolean z) {
        this.mActivityHelper.alertList(strArr, onClickListener, z);
    }

    public void toast(String str, int i) {
        if (this.mDialogHelper == null) {
            this.mActivityHelper.toast(str, i);
        } else if (getActivity() != null) {
            this.mDialogHelper.toast(getActivity().getApplicationContext(), str, i);
        }
    }

    /* access modifiers changed from: protected */
    public void snackBar(String str, int i) {
        if (this.mDialogHelper != null) {
            this.mDialogHelper.snackBar(getView(), str, i, "", (View.OnClickListener) null);
        }
    }

    /* access modifiers changed from: protected */
    public void snackBar(String str, int i, String str2, View.OnClickListener onClickListener) {
        if (this.mDialogHelper != null) {
            this.mDialogHelper.snackBar(getView(), str, i, str2, onClickListener);
        }
    }

    /* access modifiers changed from: protected */
    public void showProgress(String str) {
        Log.e("TAG", "loading ");
        if (this.mDialogHelper != null) {
            this.mDialogHelper.showProgressDialog(getActivity(), str, true);
        } else {
            this.mActivityHelper.showProgress(str);
        }
    }

    public void showLoading() {
        showProgress("");
    }

    public void dismissLoading() {
        if (getActivity() != null && !getActivity().isFinishing()) {
            dismissProgress();
        }
    }

    /* access modifiers changed from: protected */
    public void dismissProgress() {
        if (this.mDialogHelper != null) {
            this.mDialogHelper.dismissProgressDialog();
        } else {
            this.mActivityHelper.dismissProgressDialog();
        }
    }

    /* access modifiers changed from: protected */
    public void dismissAlertDialog() {
        if (this.mDialogHelper != null) {
            this.mDialogHelper.dismissAlertDialog();
        } else {
            this.mActivityHelper.dismissAlertDialog();
        }
    }
}
