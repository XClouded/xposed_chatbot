package com.alimama.union.app.infrastructure.image.picPreviewer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import androidx.viewpager.widget.ViewPager;

public class ImageViewPager extends ViewPager {
    public ImageTouchView mCurrentView = null;

    public ImageViewPager(Context context) {
        super(context);
    }

    public ImageViewPager(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void setCurrentView(ImageTouchView imageTouchView) {
        this.mCurrentView = imageTouchView;
    }

    public ImageTouchView getCurrentView() {
        return this.mCurrentView;
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (this.mCurrentView == null) {
            return false;
        }
        if (this.mCurrentView.pagerCanScroll()) {
            return super.onInterceptTouchEvent(motionEvent);
        }
        if (this.mCurrentView.viewCanMove()) {
            return super.onInterceptTouchEvent(motionEvent);
        }
        return false;
    }
}
