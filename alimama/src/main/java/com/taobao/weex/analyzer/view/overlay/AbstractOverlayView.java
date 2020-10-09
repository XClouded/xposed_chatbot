package com.taobao.weex.analyzer.view.overlay;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;
import androidx.annotation.NonNull;

public abstract class AbstractOverlayView implements IOverlayView {
    /* access modifiers changed from: protected */
    public boolean isViewAttached;
    protected Context mContext;
    protected int mGravity = 51;
    protected int mHeight = -2;
    protected View mWholeView;
    protected int mWidth = -2;
    protected WindowManager mWindowManager;
    protected int mX = 0;
    protected int mY = 0;

    /* access modifiers changed from: protected */
    @NonNull
    public abstract View onCreateView();

    /* access modifiers changed from: protected */
    public void onDestroy() {
    }

    /* access modifiers changed from: protected */
    public abstract void onDismiss();

    /* access modifiers changed from: protected */
    public abstract void onShown();

    /* access modifiers changed from: protected */
    public void onViewCreated(@NonNull View view) {
    }

    public AbstractOverlayView(Context context) {
        this.mContext = context;
        this.mWindowManager = (WindowManager) context.getSystemService("window");
    }

    public boolean isViewAttached() {
        return this.isViewAttached;
    }

    public void show() {
        try {
            this.mWholeView = onCreateView();
            onViewCreated(this.mWholeView);
            int i = this.mWidth;
            int i2 = this.mHeight;
            int i3 = 2005;
            if (Build.VERSION.SDK_INT < 19) {
                i3 = 2002;
            }
            if (Build.VERSION.SDK_INT >= 25) {
                i3 = 2002;
            }
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(i, i2, Build.VERSION.SDK_INT >= 26 ? 2038 : i3, 40, -3);
            layoutParams.gravity = this.mGravity;
            layoutParams.x = this.mX;
            layoutParams.y = this.mY;
            this.mWindowManager.addView(this.mWholeView, layoutParams);
            this.isViewAttached = true;
            onShown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dismiss() {
        try {
            if (!(this.mWindowManager == null || this.mWholeView == null || !this.isViewAttached)) {
                this.mWindowManager.removeView(this.mWholeView);
                this.isViewAttached = false;
                onDismiss();
            }
            onDestroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
