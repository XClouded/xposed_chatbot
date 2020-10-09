package com.alimama.moon.features.search;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Layout;
import android.text.style.LeadingMarginSpan;

public class FlowTitleLeadingSpan implements LeadingMarginSpan.LeadingMarginSpan2 {
    private int mLeadingMargin;

    public void drawLeadingMargin(Canvas canvas, Paint paint, int i, int i2, int i3, int i4, int i5, CharSequence charSequence, int i6, int i7, boolean z, Layout layout) {
    }

    public int getLeadingMarginLineCount() {
        return 1;
    }

    public FlowTitleLeadingSpan(int i) {
        this.mLeadingMargin = i;
    }

    public int getLeadingMargin(boolean z) {
        if (z) {
            return this.mLeadingMargin;
        }
        return 0;
    }
}
