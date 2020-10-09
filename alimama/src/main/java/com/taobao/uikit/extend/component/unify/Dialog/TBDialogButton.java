package com.taobao.uikit.extend.component.unify.Dialog;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;
import com.taobao.uikit.extend.R;
import com.taobao.uikit.extend.utils.ResourceUtils;

public class TBDialogButton extends TextView {
    private Drawable mDefaultBackground;
    private boolean mStacked = false;
    private Drawable mStackedBackground;
    private int mStackedEndPadding;
    private GravityEnum mStackedGravity;

    public TBDialogButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet, 0, 0);
    }

    public TBDialogButton(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context, attributeSet, i, 0);
    }

    @TargetApi(21)
    public TBDialogButton(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        init(context, attributeSet, i, i2);
    }

    private void init(Context context, AttributeSet attributeSet, int i, int i2) {
        this.mStackedEndPadding = ResourceUtils.getDimensionPixelSize(context, R.dimen.uik_mdDialogFrameMargin);
        this.mStackedGravity = GravityEnum.END;
    }

    /* access modifiers changed from: package-private */
    public void setStacked(boolean z, boolean z2) {
        if (this.mStacked != z || z2) {
            setGravity(z ? this.mStackedGravity.getGravityInt() | 16 : 17);
            if (Build.VERSION.SDK_INT >= 17) {
                setTextAlignment(z ? this.mStackedGravity.getTextAlignment() : 4);
            }
            ResourceUtils.setBackgroundCompat(this, z ? this.mStackedBackground : this.mDefaultBackground);
            if (z) {
                setPadding(this.mStackedEndPadding, getPaddingTop(), this.mStackedEndPadding, getPaddingBottom());
            }
            this.mStacked = z;
        }
    }

    public void setStackedGravity(GravityEnum gravityEnum) {
        this.mStackedGravity = gravityEnum;
    }

    public void setStackedSelector(Drawable drawable) {
        this.mStackedBackground = drawable;
        if (this.mStacked) {
            setStacked(true, true);
        }
    }

    public void setDefaultSelector(Drawable drawable) {
        this.mDefaultBackground = drawable;
        if (!this.mStacked) {
            setStacked(false, true);
        }
    }

    public void setAllCapsCompat(boolean z) {
        if (Build.VERSION.SDK_INT >= 14) {
            setAllCaps(z);
        }
    }
}
