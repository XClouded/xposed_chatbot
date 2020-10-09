package com.taobao.phenix.cache.memory;

import android.content.res.Resources;
import com.taobao.pexode.animate.AnimatedImage;
import com.taobao.phenix.animate.AnimatedImageDrawable;

public class AnimatedCachedImage extends CachedRootImage {
    final AnimatedImage animated;

    public AnimatedCachedImage(AnimatedImage animatedImage, String str, String str2, int i, int i2) {
        super(str, str2, i, i2);
        this.animated = animatedImage;
    }

    /* access modifiers changed from: protected */
    public PassableBitmapDrawable newBitmapDrawable(String str, String str2, int i, int i2, boolean z, Resources resources) {
        return new AnimatedImageDrawable(str, str2, i, i2, this.animated);
    }

    public int getSize() {
        if (this.animated == null) {
            return 0;
        }
        return this.animated.getSizeInBytes();
    }
}
