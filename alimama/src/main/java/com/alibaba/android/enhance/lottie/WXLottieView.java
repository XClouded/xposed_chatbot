package com.alibaba.android.enhance.lottie;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import androidx.annotation.Nullable;
import com.airbnb.lottie.LottieAnimationView;
import com.taobao.weex.ui.view.gesture.WXGesture;
import com.taobao.weex.ui.view.gesture.WXGestureObservable;

public class WXLottieView extends LottieAnimationView implements WXGestureObservable {
    private WXGesture wxGesture;

    public WXLottieView(Context context) {
        super(context);
    }

    public WXLottieView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public WXLottieView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public void registerGestureListener(@Nullable WXGesture wXGesture) {
        this.wxGesture = wXGesture;
    }

    public WXGesture getGestureListener() {
        return this.wxGesture;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        boolean onTouchEvent = super.onTouchEvent(motionEvent);
        return this.wxGesture != null ? onTouchEvent | this.wxGesture.onTouch(this, motionEvent) : onTouchEvent;
    }
}
