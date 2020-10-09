package com.alibaba.android.enhance.gpuimage.core;

import android.graphics.Bitmap;
import android.media.effect.Effect;
import android.media.effect.EffectContext;
import android.media.effect.EffectFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.text.TextUtils;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.alibaba.android.enhance.gpuimage.utils.Utils;
import com.uc.crashsdk.export.LogType;
import java.util.Iterator;
import java.util.Map;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

class GLImageFilterRender implements GLSurfaceView.Renderer {
    private static final String TAG = "GLImageFilterRender";
    private Bitmap mBitmap = null;
    private Effect mEffect;
    private EffectContext mEffectContext;
    private FilterCallback mFilterCallback;
    private ImageFilterConfig mFilterConfig;
    private GLSurfaceView mHostRef;
    private int mImageHeight;
    private int mImageWidth;
    private boolean mInitialized = false;
    private TextureRenderer mTexRenderer = new TextureRenderer();
    private int[] mTextures = new int[2];

    interface FilterCallback {
        void onFailed(String str);

        void onSuccess();
    }

    public void onSurfaceCreated(GL10 gl10, EGLConfig eGLConfig) {
    }

    GLImageFilterRender(GLSurfaceView gLSurfaceView) {
        this.mHostRef = gLSurfaceView;
    }

    public void onSurfaceChanged(GL10 gl10, int i, int i2) {
        if (this.mTexRenderer != null) {
            this.mTexRenderer.updateViewSize(i, i2);
        }
        this.mImageWidth = i;
        this.mImageHeight = i2;
    }

    public void onDrawFrame(GL10 gl10) {
        try {
            onDrawFrameInternal();
            try {
                recycle();
            } catch (Exception e) {
                Log.e(TAG, "release | internal error", e);
            }
        } catch (Exception e2) {
            Log.e(TAG, "internal error", e2);
            if (this.mFilterCallback != null) {
                this.mFilterCallback.onFailed("internal error");
            }
            recycle();
        } catch (Throwable th) {
            try {
                recycle();
            } catch (Exception e3) {
                Log.e(TAG, "release | internal error", e3);
            }
            throw th;
        }
    }

    private void onDrawFrameInternal() {
        if (!hasValidSurface()) {
            Log.e(TAG, "surface invalid");
            return;
        }
        drawBackground();
        if (isFilterValid(this.mFilterConfig)) {
            if (!this.mInitialized) {
                this.mEffectContext = EffectContext.createWithCurrentGlContext();
                this.mTexRenderer.init();
                this.mInitialized = true;
            }
            if (this.mEffect != null) {
                this.mEffect.release();
            }
            this.mEffect = initEffectByName();
            loadTextures();
            if (this.mEffect != null) {
                Benchmark.markPureFilterStart();
                this.mEffect.apply(this.mTextures[0], this.mImageWidth, this.mImageHeight, this.mTextures[1]);
                Benchmark.markPureFilterEnd();
                this.mTexRenderer.renderTexture(this.mTextures[1]);
            } else {
                this.mTexRenderer.renderTexture(this.mTextures[0]);
            }
            if (this.mFilterCallback != null) {
                this.mFilterCallback.onSuccess();
            }
            Benchmark.markTaskEnd();
            Benchmark.printResult();
        }
    }

    static boolean isFilterValid(ImageFilterConfig imageFilterConfig) {
        if (imageFilterConfig == null) {
            return false;
        }
        String str = imageFilterConfig.name;
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        try {
            if (str.equals("raw") || EffectFactory.isEffectSupported(str)) {
                return true;
            }
            return false;
        } catch (Throwable unused) {
            return false;
        }
    }

    private void loadTextures() {
        if (this.mBitmap != null && !this.mBitmap.isRecycled()) {
            GLES20.glGenTextures(2, this.mTextures, 0);
            Bitmap bitmap = this.mBitmap;
            this.mImageWidth = this.mImageWidth <= 0 ? bitmap.getWidth() : this.mImageWidth;
            this.mImageHeight = this.mImageHeight <= 0 ? bitmap.getHeight() : this.mImageHeight;
            this.mTexRenderer.updateTextureSize(this.mImageWidth, this.mImageHeight);
            GLES20.glBindTexture(3553, this.mTextures[0]);
            GLUtils.texImage2D(3553, 0, bitmap, 0);
            GLToolbox.initTexParams();
        }
    }

    @Nullable
    private Effect initEffectByName() {
        Effect effect;
        Throwable th;
        Map.Entry next;
        if (this.mFilterConfig == null || TextUtils.isEmpty(this.mFilterConfig.name) || this.mFilterConfig.name.equals("raw")) {
            return null;
        }
        try {
            effect = this.mEffectContext.getFactory().createEffect(this.mFilterConfig.name);
            try {
                Map<String, Object> map = this.mFilterConfig.props;
                if (map != null && !map.isEmpty()) {
                    Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
                    while (it.hasNext()) {
                        next = it.next();
                        Utils.setEffectParameter(effect, this.mFilterConfig.name, (String) next.getKey(), next.getValue());
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "unknown parameter: " + ((String) next.getKey()), e);
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (Throwable th3) {
            th = th3;
            effect = null;
            Log.e("image-filter", "effect not support", th);
            if (this.mFilterCallback != null) {
                this.mFilterCallback.onFailed("effect not support");
            }
            return effect;
        }
        return effect;
    }

    /* access modifiers changed from: package-private */
    public void applyFilter(@NonNull ImageFilterConfig imageFilterConfig, @NonNull Bitmap bitmap, FilterCallback filterCallback) {
        this.mFilterConfig = imageFilterConfig;
        if (this.mBitmap != null) {
            this.mBitmap = null;
        }
        this.mBitmap = bitmap;
        this.mFilterCallback = filterCallback;
    }

    /* access modifiers changed from: package-private */
    public void recycle() {
        if (this.mEffectContext != null) {
            this.mEffectContext.release();
            this.mInitialized = false;
            this.mEffectContext = null;
            GLES20.glDeleteTextures(2, this.mTextures, 0);
        }
    }

    /* access modifiers changed from: package-private */
    public void tearDown() {
        if (this.mTexRenderer != null) {
            this.mTexRenderer.tearDown();
        }
    }

    private void drawBackground() {
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
        GLToolbox.checkGlError("glClearColor");
        GLES20.glClear(LogType.UNEXP_RESTART);
        GLToolbox.checkGlError("glClear");
    }

    private boolean hasValidSurface() {
        if (this.mHostRef == null) {
            return false;
        }
        try {
            return this.mHostRef.getHolder().getSurface().isValid();
        } catch (Exception unused) {
            return false;
        }
    }
}
