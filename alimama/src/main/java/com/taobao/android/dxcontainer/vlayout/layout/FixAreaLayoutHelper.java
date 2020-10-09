package com.taobao.android.dxcontainer.vlayout.layout;

import android.view.View;
import android.view.ViewPropertyAnimator;
import com.taobao.android.dxcontainer.vlayout.LayoutManagerHelper;

public abstract class FixAreaLayoutHelper extends BaseLayoutHelper {
    protected FixAreaAdjuster mAdjuster = FixAreaAdjuster.mDefaultAdjuster;
    protected FixViewAnimatorHelper mFixViewAnimatorHelper;

    public interface FixViewAnimatorHelper {
        ViewPropertyAnimator onGetFixViewAppearAnimator(View view);

        ViewPropertyAnimator onGetFixViewDisappearAnimator(View view);
    }

    public void adjustLayout(int i, int i2, LayoutManagerHelper layoutManagerHelper) {
    }

    public int computeMarginEnd(int i, boolean z, boolean z2, LayoutManagerHelper layoutManagerHelper) {
        return 0;
    }

    public int computeMarginStart(int i, boolean z, boolean z2, LayoutManagerHelper layoutManagerHelper) {
        return 0;
    }

    public int computePaddingEnd(int i, boolean z, boolean z2, LayoutManagerHelper layoutManagerHelper) {
        return 0;
    }

    public int computePaddingStart(int i, boolean z, boolean z2, LayoutManagerHelper layoutManagerHelper) {
        return 0;
    }

    public boolean isFixLayout() {
        return true;
    }

    public void setAdjuster(FixAreaAdjuster fixAreaAdjuster) {
        this.mAdjuster = fixAreaAdjuster;
    }

    public void setFixViewAnimatorHelper(FixViewAnimatorHelper fixViewAnimatorHelper) {
        this.mFixViewAnimatorHelper = fixViewAnimatorHelper;
    }
}
