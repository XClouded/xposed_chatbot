package com.alibaba.android.bindingx.plugin.android;

import android.content.Context;
import androidx.annotation.NonNull;
import com.alibaba.android.bindingx.core.PlatformManager;
import com.alibaba.android.bindingx.core.internal.AbstractScrollEventHandler;
import com.alibaba.android.bindingx.core.internal.BindingXConstants;

public class BindingXScrollHandler extends AbstractScrollEventHandler implements PlatformManager.ScrollListener {
    private static final String STATE_SCROLL_END = "scrollEnd";
    private static final String STATE_SCROLL_START = "scrollStart";
    private int mLastDx = 0;
    private int mLastDy = 0;
    private int mTx = 0;
    private int mTy = 0;

    private boolean isSameDirection(int i, int i2) {
        if (i == i2) {
            return true;
        }
        if (i <= 0 || i2 <= 0) {
            return i < 0 && i2 < 0;
        }
        return true;
    }

    public void onActivityPause() {
    }

    public void onActivityResume() {
    }

    public void onStart(@NonNull String str, @NonNull String str2) {
    }

    public BindingXScrollHandler(Context context, PlatformManager platformManager, Object... objArr) {
        super(context, platformManager, objArr);
    }

    public boolean onCreate(@NonNull String str, @NonNull String str2) {
        PlatformManager.IScrollFactory scrollFactory = this.mPlatformManager.getScrollFactory();
        if (scrollFactory == null) {
            return false;
        }
        scrollFactory.addScrollListenerWith(str, this);
        return true;
    }

    public boolean onDisable(@NonNull String str, @NonNull String str2) {
        PlatformManager.IScrollFactory scrollFactory = this.mPlatformManager.getScrollFactory();
        if (scrollFactory == null) {
            return false;
        }
        scrollFactory.removeScrollListenerWith(str, this);
        return super.onDisable(str, str2);
    }

    public void onScrolled(float f, float f2) {
        boolean z;
        int i;
        int i2;
        int i3;
        int i4;
        float f3 = f;
        float f4 = f2;
        int i5 = (int) (f3 - ((float) this.mContentOffsetX));
        int i6 = (int) (f4 - ((float) this.mContentOffsetY));
        this.mContentOffsetX = (int) f3;
        this.mContentOffsetY = (int) f4;
        if (i5 != 0 || i6 != 0) {
            if (!isSameDirection(i6, this.mLastDy)) {
                this.mTy = this.mContentOffsetY;
                z = true;
            } else {
                z = false;
            }
            if (!isSameDirection(i5, this.mLastDx)) {
                this.mTx = this.mContentOffsetX;
                z = true;
            }
            int i7 = this.mContentOffsetX - this.mTx;
            int i8 = this.mContentOffsetY - this.mTy;
            this.mLastDx = i5;
            this.mLastDy = i6;
            if (z) {
                int i9 = i7;
                i = i5;
                double d = (double) i6;
                i4 = i8;
                i3 = i6;
                i2 = i9;
                super.fireEventByState(BindingXConstants.STATE_TURNING, (double) this.mContentOffsetX, (double) this.mContentOffsetY, (double) i5, d, (double) i7, (double) i8, new Object[0]);
            } else {
                i4 = i8;
                i3 = i6;
                i2 = i7;
                i = i5;
            }
            super.handleScrollEvent(this.mContentOffsetX, this.mContentOffsetY, i, i3, i2, i4);
        }
    }

    public void onScrollStart() {
        super.fireEventByState("scrollStart", 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, new Object[0]);
    }

    public void onScrollEnd(float f, float f2) {
        super.fireEventByState("scrollEnd", (double) f, (double) f2, 0.0d, 0.0d, 0.0d, 0.0d, new Object[0]);
    }
}
