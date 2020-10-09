package com.alibaba.android.enhance.gpuimage.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLSurfaceView;
import androidx.annotation.NonNull;
import com.alibaba.android.enhance.gpuimage.core.GLImageFilterRender;

class GLImageFilterView extends GLSurfaceView {
    private GLImageFilterRender mCoreRender;

    GLImageFilterView(Context context) {
        super(context);
        setup();
    }

    private void setup() {
        setEGLContextClientVersion(2);
        setPreserveEGLContextOnPause(true);
        if (getHolder() != null) {
            getHolder().setFormat(-2);
        }
        this.mCoreRender = new GLImageFilterRender(this);
        setRenderer(this.mCoreRender);
        setRenderMode(0);
    }

    /* access modifiers changed from: package-private */
    public boolean isFilterValid(@NonNull ImageFilterConfig imageFilterConfig) {
        return GLImageFilterRender.isFilterValid(imageFilterConfig);
    }

    /* access modifiers changed from: package-private */
    public void applyFilterToBitmap(@NonNull ImageFilterConfig imageFilterConfig, @NonNull Bitmap bitmap, @NonNull GLImageFilterRender.FilterCallback filterCallback) {
        if (this.mCoreRender != null) {
            this.mCoreRender.applyFilter(imageFilterConfig, bitmap, filterCallback);
            requestRender();
        }
    }
}
