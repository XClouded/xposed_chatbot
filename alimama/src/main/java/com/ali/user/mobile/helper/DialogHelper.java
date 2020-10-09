package com.ali.user.mobile.helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import com.ali.user.mobile.log.TLogAdapter;
import com.ali.user.mobile.ui.widget.AUProgressDialog;

public class DialogHelper {
    protected static final String TAG = "login.DialogHelper";
    /* access modifiers changed from: private */
    public Activity mActivity;
    /* access modifiers changed from: private */
    public AlertDialog mAlertDialog;
    /* access modifiers changed from: private */
    public AlertDialog mProgressDialog;
    private Toast mToast;

    public DialogHelper(Activity activity) {
        this.mActivity = activity;
    }

    public void alert(String str, String str2, String str3, DialogInterface.OnClickListener onClickListener, String str4, DialogInterface.OnClickListener onClickListener2) {
        alert(str, str2, str3, onClickListener, str4, onClickListener2, (Boolean) false, (DialogInterface.OnCancelListener) null);
    }

    public void alert(String str, String str2, String str3, DialogInterface.OnClickListener onClickListener, String str4, DialogInterface.OnClickListener onClickListener2, Boolean bool, DialogInterface.OnCancelListener onCancelListener) {
        dismissAlertDialog();
        final String str5 = str;
        final String str6 = str2;
        final String str7 = str3;
        final DialogInterface.OnClickListener onClickListener3 = onClickListener;
        final String str8 = str4;
        final DialogInterface.OnClickListener onClickListener4 = onClickListener2;
        final Boolean bool2 = bool;
        final DialogInterface.OnCancelListener onCancelListener2 = onCancelListener;
        this.mActivity.runOnUiThread(new Runnable() {
            public void run() {
                if (DialogHelper.this.mActivity != null && !DialogHelper.this.mActivity.isFinishing()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(DialogHelper.this.mActivity, 16973939));
                    if (!TextUtils.isEmpty(str5)) {
                        builder.setTitle(str5);
                    }
                    if (!TextUtils.isEmpty(str6)) {
                        builder.setMessage(str6);
                    }
                    if (!TextUtils.isEmpty(str7)) {
                        builder.setPositiveButton(str7, onClickListener3);
                    }
                    if (!TextUtils.isEmpty(str8)) {
                        builder.setNegativeButton(str8, onClickListener4);
                    }
                    try {
                        AlertDialog unused = DialogHelper.this.mAlertDialog = builder.show();
                        DialogHelper.this.mAlertDialog.setCanceledOnTouchOutside(bool2.booleanValue());
                        DialogHelper.this.mAlertDialog.setCancelable(bool2.booleanValue());
                        DialogHelper.this.mAlertDialog.setOnCancelListener(onCancelListener2);
                        DialogHelper.this.mAlertDialog.getButton(-1).setTextColor(Color.parseColor("#FF5000"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void alert(String str, View view, String str2, DialogInterface.OnClickListener onClickListener, String str3, DialogInterface.OnClickListener onClickListener2, Boolean bool, DialogInterface.OnCancelListener onCancelListener) {
        dismissAlertDialog();
        final String str4 = str;
        final View view2 = view;
        final String str5 = str2;
        final DialogInterface.OnClickListener onClickListener3 = onClickListener;
        final String str6 = str3;
        final DialogInterface.OnClickListener onClickListener4 = onClickListener2;
        final Boolean bool2 = bool;
        final DialogInterface.OnCancelListener onCancelListener2 = onCancelListener;
        this.mActivity.runOnUiThread(new Runnable() {
            public void run() {
                if (DialogHelper.this.mActivity != null && !DialogHelper.this.mActivity.isFinishing()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(DialogHelper.this.mActivity, 16973939));
                    if (!TextUtils.isEmpty(str4)) {
                        builder.setTitle(str4);
                    }
                    if (view2 != null) {
                        builder.setView(view2);
                    }
                    if (!TextUtils.isEmpty(str5)) {
                        builder.setPositiveButton(str5, onClickListener3);
                    }
                    if (!TextUtils.isEmpty(str6)) {
                        builder.setNegativeButton(str6, onClickListener4);
                    }
                    try {
                        AlertDialog unused = DialogHelper.this.mAlertDialog = builder.show();
                        DialogHelper.this.mAlertDialog.setCanceledOnTouchOutside(bool2.booleanValue());
                        DialogHelper.this.mAlertDialog.setCancelable(bool2.booleanValue());
                        DialogHelper.this.mAlertDialog.setOnCancelListener(onCancelListener2);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void alertList(final String[] strArr, final DialogInterface.OnClickListener onClickListener, final boolean z) {
        dismissAlertDialog();
        this.mActivity.runOnUiThread(new Runnable() {
            public void run() {
                if (DialogHelper.this.mActivity != null && !DialogHelper.this.mActivity.isFinishing()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(DialogHelper.this.mActivity, 16973939));
                    builder.setItems(strArr, onClickListener);
                    AlertDialog unused = DialogHelper.this.mAlertDialog = builder.show();
                    DialogHelper.this.mAlertDialog.setCanceledOnTouchOutside(z);
                    DialogHelper.this.mAlertDialog.setCancelable(z);
                }
            }
        });
    }

    public void toast(final String str, final int i) {
        if (this.mActivity != null && !this.mActivity.isFinishing()) {
            this.mActivity.runOnUiThread(new Runnable() {
                public void run() {
                    try {
                        Toast.makeText(DialogHelper.this.mActivity, str, i).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public void hideInputMethod() {
        if (this.mActivity != null && this.mActivity.getCurrentFocus() != null) {
            ((InputMethodManager) this.mActivity.getSystemService("input_method")).hideSoftInputFromWindow(this.mActivity.getCurrentFocus().getWindowToken(), 2);
        }
    }

    public void showProgressDialog(boolean z, String str) {
        showProgressDialog(str, true, (DialogInterface.OnCancelListener) null, z);
    }

    public void showProgressDialog(String str) {
        showProgressDialog(str, true, (DialogInterface.OnCancelListener) null, true);
    }

    public void showProgressDialog(String str, boolean z, DialogInterface.OnCancelListener onCancelListener, boolean z2) {
        if (this.mProgressDialog == null || !this.mProgressDialog.isShowing()) {
            final String str2 = str;
            final boolean z3 = z2;
            final boolean z4 = z;
            final DialogInterface.OnCancelListener onCancelListener2 = onCancelListener;
            this.mActivity.runOnUiThread(new Runnable() {
                public void run() {
                    if (DialogHelper.this.mActivity != null && !DialogHelper.this.mActivity.isFinishing()) {
                        AlertDialog unused = DialogHelper.this.mProgressDialog = new AUProgressDialog(DialogHelper.this.mActivity);
                        DialogHelper.this.mProgressDialog.setMessage(str2);
                        ((AUProgressDialog) DialogHelper.this.mProgressDialog).setProgressVisiable(z3);
                        DialogHelper.this.mProgressDialog.setCancelable(z4);
                        DialogHelper.this.mProgressDialog.setOnCancelListener(onCancelListener2);
                        try {
                            DialogHelper.this.mProgressDialog.show();
                        } catch (Exception unused2) {
                        }
                        DialogHelper.this.mProgressDialog.setCanceledOnTouchOutside(false);
                    }
                }
            });
        }
    }

    public void dismissProgressDialog() {
        if (this.mProgressDialog != null && this.mProgressDialog.isShowing()) {
            this.mActivity.runOnUiThread(new Runnable() {
                public void run() {
                    if (DialogHelper.this.mProgressDialog != null && DialogHelper.this.mProgressDialog.isShowing()) {
                        try {
                            DialogHelper.this.mProgressDialog.dismiss();
                        } catch (Exception e) {
                            TLogAdapter.w(DialogHelper.TAG, "dismissProgressDialog", e);
                        } catch (Throwable th) {
                            AlertDialog unused = DialogHelper.this.mProgressDialog = null;
                            throw th;
                        }
                        AlertDialog unused2 = DialogHelper.this.mProgressDialog = null;
                    }
                }
            });
        }
    }

    public void updateMessage(final String str) {
        if (this.mAlertDialog != null) {
            this.mActivity.runOnUiThread(new Runnable() {
                public void run() {
                    try {
                        DialogHelper.this.mAlertDialog.setMessage(str);
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                }
            });
        }
    }

    public void dismissAlertDialog() {
        if (this.mAlertDialog != null && this.mAlertDialog.isShowing()) {
            this.mActivity.runOnUiThread(new Runnable() {
                public void run() {
                    if (DialogHelper.this.mAlertDialog != null && DialogHelper.this.mAlertDialog.isShowing()) {
                        try {
                            DialogHelper.this.mAlertDialog.dismiss();
                        } catch (Exception e) {
                            TLogAdapter.w(DialogHelper.TAG, "dismissProgressDialog", e);
                        } catch (Throwable th) {
                            AlertDialog unused = DialogHelper.this.mAlertDialog = null;
                            throw th;
                        }
                        AlertDialog unused2 = DialogHelper.this.mAlertDialog = null;
                    }
                }
            });
        }
    }
}
