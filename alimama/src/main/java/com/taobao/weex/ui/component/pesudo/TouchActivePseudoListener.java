package com.taobao.weex.ui.component.pesudo;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;

public class TouchActivePseudoListener implements View.OnTouchListener {
    private boolean mIsConsumeOnTouch;
    private OnActivePseudoListner mOnActivePseudoListner;

    public TouchActivePseudoListener(OnActivePseudoListner onActivePseudoListner, boolean z) {
        this.mOnActivePseudoListner = onActivePseudoListner;
        this.mIsConsumeOnTouch = z;
    }

    @SuppressLint({"ClickableViewAccessibility"})
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (this.mOnActivePseudoListner != null) {
            if (action == 0 || action == 5) {
                this.mOnActivePseudoListner.updateActivePseudo(true);
            } else if (action == 3 || action == 1 || action == 6) {
                this.mOnActivePseudoListner.updateActivePseudo(false);
            }
        }
        return this.mIsConsumeOnTouch;
    }
}
