package com.taobao.android.dinamicx.view;

import android.content.Context;
import android.graphics.PointF;
import android.util.DisplayMetrics;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

public class DXScrollLinearLayoutManager extends DXLinearLayoutManager {
    /* access modifiers changed from: private */
    public float calculateSpeedPerPixel = 1.0f;
    private float calculateTimeForScrolling = 160.0f;

    public DXScrollLinearLayoutManager(Context context, int i, boolean z) {
        super(context, i, z);
    }

    public void measureChildWithMargins(View view, int i, int i2) {
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
        view.measure(getChildMeasureSpec(getWidth(), getWidthMode(), 0, layoutParams.width, canScrollHorizontally()), getChildMeasureSpec(getHeight(), getHeightMode(), getPaddingTop() + getPaddingBottom() + layoutParams.topMargin + layoutParams.bottomMargin + i2, layoutParams.height, canScrollVertically()));
    }

    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int i) {
        AnonymousClass1 r2 = new LinearSmoothScroller(recyclerView.getContext()) {
            public int calculateDtToFit(int i, int i2, int i3, int i4, int i5) {
                switch (i5) {
                    case -1:
                        return i3 - i;
                    case 0:
                        return i3 - i;
                    case 1:
                        return i4 - i2;
                    default:
                        return 0;
                }
            }

            @Nullable
            public PointF computeScrollVectorForPosition(int i) {
                return DXScrollLinearLayoutManager.this.computeScrollVectorForPosition(i);
            }

            /* access modifiers changed from: protected */
            public float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                return DXScrollLinearLayoutManager.this.calculateSpeedPerPixel;
            }

            /* access modifiers changed from: protected */
            public int calculateTimeForScrolling(int i) {
                return super.calculateTimeForScrolling(i);
            }
        };
        r2.setTargetPosition(i);
        startSmoothScroll(r2);
    }

    public void calculateSpeedPerPixel(int i) {
        if (i == 0) {
            this.calculateSpeedPerPixel = 1.0f;
        } else {
            this.calculateSpeedPerPixel = this.calculateTimeForScrolling / ((float) i);
        }
    }

    public void setCalculateTimeForScrolling(float f) {
        this.calculateTimeForScrolling = f;
    }
}
