package com.alimama.moon.view.autoscrollviewpager;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

public class CustomDurationScroller extends Scroller {
    private double scrollFactor = 1.0d;

    public CustomDurationScroller(Context context) {
        super(context);
    }

    public CustomDurationScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    public void setScrollDurationFactor(double d) {
        this.scrollFactor = d;
    }

    public void startScroll(int i, int i2, int i3, int i4, int i5) {
        double d = (double) i5;
        double d2 = this.scrollFactor;
        Double.isNaN(d);
        super.startScroll(i, i2, i3, i4, (int) (d * d2));
    }
}
