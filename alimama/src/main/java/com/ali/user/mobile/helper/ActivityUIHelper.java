package com.ali.user.mobile.helper;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

public class ActivityUIHelper implements IDialogHelper {
    public static final int PERIOD = 3000;
    static final String TAG = "ActivityUIHelper";
    private Activity mActivity;
    private DialogHelper mDialogHelper = new DialogHelper(this.mActivity);

    public void snackBar(View view, String str, int i, String str2, View.OnClickListener onClickListener) {
    }

    public ActivityUIHelper(Activity activity) {
        this.mActivity = activity;
    }

    public void hideInputMethod() {
        this.mDialogHelper.hideInputMethod();
    }

    public void finish() {
        this.mDialogHelper.dismissProgressDialog();
        this.mDialogHelper.dismissAlertDialog();
        this.mDialogHelper.hideInputMethod();
    }

    public void alert(String str, String str2, String str3, DialogInterface.OnClickListener onClickListener, String str4, DialogInterface.OnClickListener onClickListener2) {
        this.mDialogHelper.alert(str, str2, str3, onClickListener, str4, onClickListener2);
    }

    public void alert(String str, String str2, String str3, DialogInterface.OnClickListener onClickListener, String str4, DialogInterface.OnClickListener onClickListener2, Boolean bool) {
        this.mDialogHelper.alert(str, str2, str3, onClickListener, str4, onClickListener2, bool, (DialogInterface.OnCancelListener) null);
    }

    public void alert(Activity activity, String str, View view, String str2, DialogInterface.OnClickListener onClickListener, String str3, DialogInterface.OnClickListener onClickListener2, Boolean bool, DialogInterface.OnCancelListener onCancelListener) {
        this.mDialogHelper.alert(str, view, str2, onClickListener, str3, onClickListener2, bool, (DialogInterface.OnCancelListener) null);
    }

    public void alertList(String[] strArr, DialogInterface.OnClickListener onClickListener, boolean z) {
        this.mDialogHelper.alertList(strArr, onClickListener, z);
    }

    public void toast(String str, int i) {
        this.mDialogHelper.toast(str, i);
    }

    public void showProgress(String str) {
        this.mDialogHelper.showProgressDialog(str);
    }

    public void showProgress(String str, boolean z, DialogInterface.OnCancelListener onCancelListener) {
        this.mDialogHelper.showProgressDialog(str, z, onCancelListener, true);
    }

    public void alert(Activity activity, String str, String str2, String str3, DialogInterface.OnClickListener onClickListener, String str4, DialogInterface.OnClickListener onClickListener2) {
        alert(str, str2, str3, onClickListener, str4, onClickListener2);
    }

    public void alert(Activity activity, String str, String str2, String str3, DialogInterface.OnClickListener onClickListener, String str4, DialogInterface.OnClickListener onClickListener2, boolean z, boolean z2, DialogInterface.OnCancelListener onCancelListener) {
        alert(str, str2, str3, onClickListener, str4, onClickListener2);
    }

    public void toast(Context context, String str, int i) {
        toast(str, i);
    }

    public void showProgressDialog(Activity activity, String str, boolean z) {
        showProgress(str);
    }

    public void showProgressDialog(Activity activity, String str, boolean z, DialogInterface.OnCancelListener onCancelListener, boolean z2) {
        showProgress(str);
    }

    public void dismissProgressDialog() {
        if (!this.mActivity.isFinishing()) {
            this.mDialogHelper.dismissProgressDialog();
        }
    }

    public void dismissAlertDialog() {
        this.mDialogHelper.dismissAlertDialog();
    }

    public void updateMessage(String str) {
        this.mDialogHelper.updateMessage(str);
    }
}
