package com.alibaba.weex.plugin.gcanvas;

import android.content.Context;
import android.view.Surface;
import com.taobao.gcanvas.GCanvasJNI;

public class GWXNativeView {
    private int mGroupId = 0;
    private long mNativeView = 0;

    private static native long createNativeView(int i);

    private static native void destroyNativeView(int i, long j);

    private static native void nativeOnStart(long j);

    private static native void nativeOnStop(long j);

    private static native void nativeSetDeviceRatio(long j, float f);

    private static native void nativeSurfaceChanged(long j, int i, int i2);

    private static native void nativeSurfaceCreated(long j, Surface surface, int i);

    private static native void nativeSurfaceDestroyed(long j);

    static {
        GCanvasJNI.load();
        System.loadLibrary("GCanvasWeex");
    }

    /* access modifiers changed from: package-private */
    public void create(Context context) {
        this.mGroupId = context.hashCode();
        if (GCanvasNativeGroup.createNativeViewGroup(this.mGroupId)) {
            this.mNativeView = createNativeView(this.mGroupId);
            nativeSetDeviceRatio(this.mNativeView, context.getResources().getDisplayMetrics().density);
            return;
        }
        throw new RuntimeException("create gcanvas native group failed!");
    }

    public void destroy() {
        if (this.mNativeView != 0) {
            destroyNativeView(this.mGroupId, this.mNativeView);
            this.mNativeView = 0;
        }
    }

    public void onSurfaceTextureAvailable(Surface surface, int i) {
        nativeSurfaceCreated(this.mNativeView, surface, i);
    }

    public void onSurfaceTextureSizeChanged(int i, int i2) {
        nativeSurfaceChanged(this.mNativeView, i, i2);
    }

    public void onSurfaceTextureDestroyed() {
        nativeSurfaceDestroyed(this.mNativeView);
    }
}
