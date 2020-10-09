package com.taobao.pexode.animate;

public class AnimatedDrawableFrameInfo {
    public final int frameNumber;
    public final int height;
    public final BlendMode mBlendMode;
    public final DisposalMode mDisposalMode;
    public final int width;
    public final int xOffset;
    public final int yOffset;

    public enum BlendMode {
        BLEND_WITH_PREVIOUS,
        NO_BLEND
    }

    public enum DisposalMode {
        DISPOSE_DO_NOT,
        DISPOSE_TO_BACKGROUND,
        DISPOSE_TO_PREVIOUS
    }

    public AnimatedDrawableFrameInfo(int i, int i2, int i3, int i4, int i5, BlendMode blendMode, DisposalMode disposalMode) {
        this.frameNumber = i;
        this.xOffset = i2;
        this.yOffset = i3;
        this.width = i4;
        this.height = i5;
        this.mBlendMode = blendMode;
        this.mDisposalMode = disposalMode;
    }
}
