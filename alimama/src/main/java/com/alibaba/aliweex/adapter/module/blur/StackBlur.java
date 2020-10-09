package com.alibaba.aliweex.adapter.module.blur;

import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.alibaba.aliweex.utils.BlurTool;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.utils.WXLogUtils;

class StackBlur implements BlurAlgorithm {
    private static final int DEFAULT_MAX_BLUR_RADIUS = 100;
    private static final String TAG = "StackBlur";
    private boolean canModifyBitmap = false;

    StackBlur(boolean z) {
        this.canModifyBitmap = z;
    }

    @Nullable
    public Bitmap blur(Bitmap bitmap, int i) {
        Bitmap bitmap2;
        int max = Math.max(0, Math.min(100, i));
        long j = 0;
        long currentTimeMillis = WXEnvironment.isApkDebugable() ? System.currentTimeMillis() : 0;
        try {
            bitmap2 = BlurTool.stackBlur(bitmap, max, this.canModifyBitmap);
        } catch (Exception e) {
            WXLogUtils.e(TAG, e.getMessage());
            bitmap2 = null;
        }
        if (WXEnvironment.isApkDebugable()) {
            j = System.currentTimeMillis();
        }
        WXLogUtils.d(TAG, "blur time:" + (j - currentTimeMillis) + "ms");
        return bitmap2;
    }

    public boolean canModifyBitmap() {
        return this.canModifyBitmap;
    }

    @NonNull
    public Bitmap.Config getSupportedBitmapConfig() {
        return Bitmap.Config.ARGB_8888;
    }
}
