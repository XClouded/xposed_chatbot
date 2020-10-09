package com.rd.animation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ValueAnimation {
    private ColorAnimation colorAnimation;
    private DropAnimation dropAnimation;
    private FillAnimation fillAnimation;
    private ScaleAnimation scaleAnimation;
    private SlideAnimation slideAnimation;
    private SwapAnimation swapAnimation;
    private ThinWormAnimation thinWormAnimation;
    private UpdateListener updateListener;
    private WormAnimation wormAnimation;

    public interface UpdateListener {
        void onColorAnimationUpdated(int i, int i2);

        void onDropAnimationUpdated(int i, int i2, int i3);

        void onFillAnimationUpdated(int i, int i2, int i3, int i4, int i5, int i6);

        void onScaleAnimationUpdated(int i, int i2, int i3, int i4);

        void onSlideAnimationUpdated(int i);

        void onSwapAnimationUpdated(int i);

        void onThinWormAnimationUpdated(int i, int i2, int i3);

        void onWormAnimationUpdated(int i, int i2);
    }

    public ValueAnimation(@Nullable UpdateListener updateListener2) {
        this.updateListener = updateListener2;
    }

    @NonNull
    public ColorAnimation color() {
        if (this.colorAnimation == null) {
            this.colorAnimation = new ColorAnimation(this.updateListener);
        }
        return this.colorAnimation;
    }

    @NonNull
    public ScaleAnimation scale() {
        if (this.scaleAnimation == null) {
            this.scaleAnimation = new ScaleAnimation(this.updateListener);
        }
        return this.scaleAnimation;
    }

    @NonNull
    public WormAnimation worm() {
        if (this.wormAnimation == null) {
            this.wormAnimation = new WormAnimation(this.updateListener);
        }
        return this.wormAnimation;
    }

    @NonNull
    public SlideAnimation slide() {
        if (this.slideAnimation == null) {
            this.slideAnimation = new SlideAnimation(this.updateListener);
        }
        return this.slideAnimation;
    }

    @NonNull
    public FillAnimation fill() {
        if (this.fillAnimation == null) {
            this.fillAnimation = new FillAnimation(this.updateListener);
        }
        return this.fillAnimation;
    }

    @NonNull
    public ThinWormAnimation thinWorm() {
        if (this.thinWormAnimation == null) {
            this.thinWormAnimation = new ThinWormAnimation(this.updateListener);
        }
        return this.thinWormAnimation;
    }

    @NonNull
    public DropAnimation drop() {
        if (this.dropAnimation == null) {
            this.dropAnimation = new DropAnimation(this.updateListener);
        }
        return this.dropAnimation;
    }

    @NonNull
    public SwapAnimation swap() {
        if (this.swapAnimation == null) {
            this.swapAnimation = new SwapAnimation(this.updateListener);
        }
        return this.swapAnimation;
    }
}
