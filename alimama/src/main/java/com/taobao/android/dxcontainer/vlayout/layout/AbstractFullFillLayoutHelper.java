package com.taobao.android.dxcontainer.vlayout.layout;

import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.taobao.android.dxcontainer.vlayout.LayoutManagerHelper;
import com.taobao.android.dxcontainer.vlayout.Range;
import com.taobao.android.dxcontainer.vlayout.VirtualLayoutManager;

public abstract class AbstractFullFillLayoutHelper extends BaseLayoutHelper {
    private static final String TAG = "FullFillLayoutHelper";
    protected boolean hasFooter = false;
    protected boolean hasHeader = false;
    protected boolean mLayoutWithAnchor = false;
    private LayoutManagerHelper mTempLayoutHelper;

    /* access modifiers changed from: protected */
    public boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return true;
    }

    /* access modifiers changed from: protected */
    public void doLayoutView(RecyclerView.Recycler recycler, RecyclerView.State state, VirtualLayoutManager.LayoutStateWrapper layoutStateWrapper, LayoutChunkResult layoutChunkResult, LayoutManagerHelper layoutManagerHelper) {
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
    }

    /* access modifiers changed from: protected */
    public final void setMeasuredDimension(int i, int i2) {
    }

    /* access modifiers changed from: protected */
    public int getAllChildren(View[] viewArr, RecyclerView.Recycler recycler, VirtualLayoutManager.LayoutStateWrapper layoutStateWrapper, LayoutChunkResult layoutChunkResult, LayoutManagerHelper layoutManagerHelper) {
        View nextView;
        int i = 0;
        boolean z = true;
        if (layoutStateWrapper.getItemDirection() != 1) {
            z = false;
        }
        int intValue = ((Integer) (z ? getRange().getLower() : getRange().getUpper())).intValue();
        int currentPosition = layoutStateWrapper.getCurrentPosition();
        if (!z ? currentPosition > intValue : currentPosition > intValue) {
            Log.w(TAG, "Please handle strange order views carefully");
        }
        int i2 = 0;
        while (i2 < viewArr.length && !isOutOfRange(layoutStateWrapper.getCurrentPosition()) && (nextView = nextView(recycler, layoutStateWrapper, layoutManagerHelper, layoutChunkResult)) != null) {
            viewArr[i2] = nextView;
            ViewGroup.LayoutParams layoutParams = nextView.getLayoutParams();
            if (layoutParams == null) {
                nextView.setLayoutParams(generateDefaultLayoutParams());
            } else if (!checkLayoutParams(layoutParams)) {
                nextView.setLayoutParams(generateLayoutParams(layoutParams));
            }
            i2++;
        }
        if (i2 > 0 && !z) {
            for (int i3 = i2 - 1; i < i3; i3--) {
                View view = viewArr[i];
                viewArr[i] = viewArr[i3];
                viewArr[i3] = view;
                i++;
            }
        }
        return i2;
    }

    public void layoutViews(RecyclerView.Recycler recycler, RecyclerView.State state, VirtualLayoutManager.LayoutStateWrapper layoutStateWrapper, LayoutChunkResult layoutChunkResult, LayoutManagerHelper layoutManagerHelper) {
        this.mTempLayoutHelper = layoutManagerHelper;
        doLayoutView(recycler, state, layoutStateWrapper, layoutChunkResult, layoutManagerHelper);
        this.mTempLayoutHelper = null;
    }

    public void checkAnchorInfo(RecyclerView.State state, VirtualLayoutManager.AnchorInfoWrapper anchorInfoWrapper, LayoutManagerHelper layoutManagerHelper) {
        if (anchorInfoWrapper.layoutFromEnd) {
            if (!this.hasFooter) {
                anchorInfoWrapper.position = getRange().getUpper().intValue();
            }
        } else if (!this.hasHeader) {
            anchorInfoWrapper.position = getRange().getLower().intValue();
        }
        this.mLayoutWithAnchor = true;
    }

    public void afterLayout(RecyclerView.Recycler recycler, RecyclerView.State state, int i, int i2, int i3, LayoutManagerHelper layoutManagerHelper) {
        super.afterLayout(recycler, state, i, i2, i3, layoutManagerHelper);
        this.mLayoutWithAnchor = false;
    }

    public int computeAlignOffset(int i, boolean z, boolean z2, LayoutManagerHelper layoutManagerHelper) {
        boolean z3 = true;
        if (layoutManagerHelper.getOrientation() != 1) {
            z3 = false;
        }
        if (z3) {
            if (z) {
                return this.mMarginBottom + this.mPaddingBottom;
            }
            return (-this.mMarginTop) - this.mPaddingTop;
        } else if (z) {
            return this.mMarginRight + this.mPaddingRight;
        } else {
            return (-this.mMarginLeft) - this.mPaddingLeft;
        }
    }

    /* access modifiers changed from: protected */
    public ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new ViewGroup.LayoutParams(-1, -2);
    }

    /* access modifiers changed from: protected */
    public ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return new ViewGroup.LayoutParams(layoutParams);
    }

    public boolean isRecyclable(int i, int i2, int i3, LayoutManagerHelper layoutManagerHelper, boolean z) {
        Range<Integer> range = getRange();
        if (!range.contains(Integer.valueOf(i))) {
            Log.w(TAG, "Child item not match");
            return true;
        } else if (this.hasHeader && i == getRange().getLower().intValue()) {
            return true;
        } else {
            if (this.hasFooter && i == getRange().getUpper().intValue()) {
                return true;
            }
            return Range.create(Integer.valueOf(i2), Integer.valueOf(i3)).contains(Range.create(Integer.valueOf(range.getLower().intValue() + (this.hasHeader ? 1 : 0)), Integer.valueOf(range.getUpper().intValue() - (this.hasFooter ? 1 : 0))));
        }
    }

    public void setHasHeader(boolean z) {
        this.hasHeader = z;
    }

    public void setHasFooter(boolean z) {
        this.hasFooter = z;
    }

    /* access modifiers changed from: protected */
    public void calculateRect(int i, Rect rect, VirtualLayoutManager.LayoutStateWrapper layoutStateWrapper, LayoutManagerHelper layoutManagerHelper) {
        int i2 = 0;
        if (layoutManagerHelper.getOrientation() == 1) {
            rect.left = layoutManagerHelper.getPaddingLeft() + this.mMarginLeft + this.mPaddingLeft;
            rect.right = ((layoutManagerHelper.getContentWidth() - layoutManagerHelper.getPaddingRight()) - this.mMarginRight) - this.mPaddingRight;
            if (layoutStateWrapper.getLayoutDirection() == -1) {
                int offset = layoutStateWrapper.getOffset();
                if (!this.mLayoutWithAnchor && !this.hasFooter) {
                    i2 = this.mMarginBottom + this.mPaddingBottom;
                }
                rect.bottom = offset - i2;
                rect.top = rect.bottom - i;
                return;
            }
            int offset2 = layoutStateWrapper.getOffset();
            if (!this.mLayoutWithAnchor && !this.hasHeader) {
                i2 = this.mMarginTop + this.mPaddingTop;
            }
            rect.top = offset2 + i2;
            rect.bottom = rect.top + i;
            return;
        }
        rect.top = layoutManagerHelper.getPaddingTop() + this.mMarginTop + this.mPaddingTop;
        rect.bottom = ((layoutManagerHelper.getContentHeight() - layoutManagerHelper.getPaddingBottom()) - this.mMarginBottom) - this.mPaddingBottom;
        if (layoutStateWrapper.getLayoutDirection() == -1) {
            int offset3 = layoutStateWrapper.getOffset();
            if (!this.mLayoutWithAnchor && !this.hasFooter) {
                i2 = this.mMarginRight + this.mPaddingRight;
            }
            rect.right = offset3 - i2;
            rect.left = rect.right - i;
            return;
        }
        int offset4 = layoutStateWrapper.getOffset();
        if (!this.mLayoutWithAnchor && !this.hasHeader) {
            i2 = this.mMarginLeft + this.mPaddingLeft;
        }
        rect.left = offset4 + i2;
        rect.right = rect.left + i;
    }
}
