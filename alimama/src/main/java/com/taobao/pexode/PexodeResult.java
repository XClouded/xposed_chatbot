package com.taobao.pexode;

import android.graphics.Bitmap;
import android.os.Build;
import com.taobao.pexode.animate.AnimatedImage;
import com.taobao.weex.el.parse.Operators;

public class PexodeResult {
    public AnimatedImage animated;
    public Bitmap bitmap;

    public static PexodeResult wrap(Bitmap bitmap2) {
        if (bitmap2 == null) {
            return null;
        }
        PexodeResult pexodeResult = new PexodeResult();
        pexodeResult.bitmap = bitmap2;
        if (Build.VERSION.SDK_INT > 23) {
            pexodeResult.bitmap.prepareToDraw();
        }
        return pexodeResult;
    }

    public static PexodeResult wrap(AnimatedImage animatedImage) {
        if (animatedImage == null) {
            return null;
        }
        PexodeResult pexodeResult = new PexodeResult();
        pexodeResult.animated = animatedImage;
        return pexodeResult;
    }

    public String toString() {
        return "PexodeResult(bitmap=" + this.bitmap + ", animated=" + this.animated + Operators.BRACKET_END_STR;
    }
}
