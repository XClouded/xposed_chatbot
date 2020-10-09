package com.taobao.weex.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.MotionEvent;
import androidx.appcompat.widget.SwitchCompat;
import com.taobao.weex.ui.view.gesture.WXGesture;
import com.taobao.weex.ui.view.gesture.WXGestureObservable;

public class WXSwitchView extends SwitchCompat implements WXGestureObservable {
    private WXGesture wxGesture;

    public WXSwitchView(Context context) {
        super(context);
        setShowText(false);
        setGravity(16);
    }

    public void registerGestureListener(WXGesture wXGesture) {
        this.wxGesture = wXGesture;
    }

    public WXGesture getGestureListener() {
        return this.wxGesture;
    }

    @SuppressLint({"ClickableViewAccessibility"})
    public boolean onTouchEvent(MotionEvent motionEvent) {
        boolean onTouchEvent = super.onTouchEvent(motionEvent);
        return this.wxGesture != null ? onTouchEvent | this.wxGesture.onTouch(this, motionEvent) : onTouchEvent;
    }
}
