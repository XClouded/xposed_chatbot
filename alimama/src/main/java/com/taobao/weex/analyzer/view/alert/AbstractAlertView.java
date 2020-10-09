package com.taobao.weex.analyzer.view.alert;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public abstract class AbstractAlertView extends Dialog implements IAlertView {
    /* access modifiers changed from: protected */
    @LayoutRes
    public abstract int getLayoutResId();

    /* access modifiers changed from: protected */
    @Nullable
    public View onCreateView() {
        return null;
    }

    /* access modifiers changed from: protected */
    public void onDismiss() {
    }

    /* access modifiers changed from: protected */
    public abstract void onInitView(@NonNull Window window);

    /* access modifiers changed from: protected */
    public void onShown() {
    }

    public AbstractAlertView(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        Window window = getWindow();
        if (window != null) {
            window.requestFeature(1);
            int layoutResId = getLayoutResId();
            if (layoutResId != 0) {
                setContentView(layoutResId);
            } else {
                View onCreateView = onCreateView();
                if (onCreateView != null) {
                    setContentView(onCreateView);
                } else {
                    throw new IllegalArgumentException("initialize failed.check if you have call onCreateView or getLayoutResId");
                }
            }
            setCancelable(true);
            setCanceledOnTouchOutside(true);
            if (Build.VERSION.SDK_INT <= 19) {
                window.setType(1000);
            }
            window.setLayout(-1, -1);
            window.setBackgroundDrawable(new ColorDrawable(0));
            onInitView(window);
        }
    }

    public void show() {
        super.show();
        onShown();
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(window.getAttributes());
            layoutParams.width = -1;
            layoutParams.height = -1;
            layoutParams.horizontalMargin = 0.0f;
            layoutParams.verticalMargin = 0.0f;
            window.setAttributes(layoutParams);
        }
    }

    public void dismiss() {
        super.dismiss();
        onDismiss();
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}
