package com.taobao.weex;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import com.taobao.weex.WeexFrameRateControl;
import com.taobao.weex.render.WXAbstractRenderContainer;

public class RenderContainer extends WXAbstractRenderContainer implements WeexFrameRateControl.VSyncListener {
    private WeexFrameRateControl mFrameRateControl = new WeexFrameRateControl(this);

    public RenderContainer(Context context) {
        super(context);
    }

    public RenderContainer(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public RenderContainer(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    @TargetApi(21)
    public RenderContainer(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.mFrameRateControl != null) {
            this.mFrameRateControl.start();
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mFrameRateControl != null) {
            this.mFrameRateControl.stop();
        }
    }

    public void dispatchWindowVisibilityChanged(int i) {
        super.dispatchWindowVisibilityChanged(i);
        if (i == 8) {
            if (this.mFrameRateControl != null) {
                this.mFrameRateControl.stop();
            }
        } else if (i == 0 && this.mFrameRateControl != null) {
            this.mFrameRateControl.start();
        }
    }

    public void OnVSync() {
        if (this.mSDKInstance != null && this.mSDKInstance.get() != null) {
            ((WXSDKInstance) this.mSDKInstance.get()).OnVSync();
        }
    }
}
