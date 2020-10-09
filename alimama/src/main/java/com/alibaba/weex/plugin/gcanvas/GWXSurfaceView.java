package com.alibaba.weex.plugin.gcanvas;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.MotionEvent;
import com.taobao.gcanvas.surface.GTextureView;
import com.taobao.gcanvas.util.GLog;
import com.taobao.weex.ui.view.gesture.WXGesture;
import com.taobao.weex.ui.view.gesture.WXGestureObservable;

public class GWXSurfaceView extends GTextureView implements WXGestureObservable {
    private WXGCanvasLigntningComponent wxComponent;
    private WXGesture wxGesture;

    public GWXSurfaceView(Context context, WXGCanvasLigntningComponent wXGCanvasLigntningComponent) {
        super(context, wXGCanvasLigntningComponent.getRef());
        this.wxComponent = wXGCanvasLigntningComponent;
    }

    public void registerGestureListener(WXGesture wXGesture) {
        this.wxGesture = wXGesture;
    }

    public WXGesture getGestureListener() {
        return this.wxGesture;
    }

    /* JADX WARNING: type inference failed for: r2v0, types: [com.alibaba.weex.plugin.gcanvas.GWXSurfaceView, android.view.View, com.taobao.gcanvas.surface.GTextureView] */
    public boolean onTouchEvent(MotionEvent motionEvent) {
        boolean onTouchEvent = GWXSurfaceView.super.onTouchEvent(motionEvent);
        return this.wxGesture != null ? onTouchEvent | this.wxGesture.onTouch(this, motionEvent) : onTouchEvent;
    }

    public void setBackgroundDrawable(Drawable drawable) {
        if (Build.VERSION.SDK_INT < 24 && drawable != null) {
            GWXSurfaceView.super.setBackgroundDrawable(drawable);
        }
    }

    public void sendEvent() {
        if (this.wxComponent != null) {
            GLog.d("start to send event in GWXSurfaceView");
            this.wxComponent.sendEvent();
        }
    }
}
