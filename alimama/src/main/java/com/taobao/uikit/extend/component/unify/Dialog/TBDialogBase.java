package com.taobao.uikit.extend.component.unify.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;

public class TBDialogBase extends Dialog implements DialogInterface.OnShowListener {
    private DialogInterface.OnShowListener mShowListener;
    protected TBDialogRootLayout view;

    protected TBDialogBase(Context context, int i) {
        super(context, i);
    }

    public View findViewById(int i) {
        return this.view.findViewById(i);
    }

    public final void setOnShowListener(DialogInterface.OnShowListener onShowListener) {
        this.mShowListener = onShowListener;
    }

    /* access modifiers changed from: protected */
    public final void setOnShowListenerInternal() {
        super.setOnShowListener(this);
    }

    /* access modifiers changed from: protected */
    public final void setViewInternal(View view2) {
        super.setContentView(view2);
    }

    /* access modifiers changed from: protected */
    public final void setViewInternal(View view2, ViewGroup.LayoutParams layoutParams) {
        super.setContentView(view2, layoutParams);
    }

    public void onShow(DialogInterface dialogInterface) {
        if (this.mShowListener != null) {
            this.mShowListener.onShow(dialogInterface);
        }
    }

    @Deprecated
    public void setContentView(int i) throws IllegalAccessError {
        throw new IllegalAccessError("setContentView() is not supported in MaterialDialog. Specify a custom view in the Builder instead.");
    }

    @Deprecated
    public void setContentView(View view2) throws IllegalAccessError {
        throw new IllegalAccessError("setContentView() is not supported in MaterialDialog. Specify a custom view in the Builder instead.");
    }

    @Deprecated
    public void setContentView(View view2, ViewGroup.LayoutParams layoutParams) throws IllegalAccessError {
        throw new IllegalAccessError("setContentView() is not supported in MaterialDialog. Specify a custom view in the Builder instead.");
    }
}
