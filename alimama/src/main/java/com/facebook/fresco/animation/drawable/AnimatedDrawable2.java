package com.facebook.fresco.animation.drawable;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import com.facebook.common.logging.FLog;
import com.facebook.drawable.base.DrawableWithCaches;
import com.facebook.drawee.drawable.DrawableProperties;
import com.facebook.fresco.animation.backend.AnimationBackend;
import com.facebook.fresco.animation.frame.DropFramesFrameScheduler;
import com.facebook.fresco.animation.frame.FrameScheduler;
import javax.annotation.Nullable;

public class AnimatedDrawable2 extends Drawable implements Animatable, DrawableWithCaches {
    private static final int DEFAULT_FRAME_SCHEDULING_DELAY_MS = 8;
    private static final int DEFAULT_FRAME_SCHEDULING_OFFSET_MS = 0;
    private static final AnimationListener NO_OP_LISTENER = new BaseAnimationListener();
    private static final Class<?> TAG = AnimatedDrawable2.class;
    @Nullable
    private AnimationBackend mAnimationBackend;
    private volatile AnimationListener mAnimationListener;
    @Nullable
    private volatile DrawListener mDrawListener;
    @Nullable
    private DrawableProperties mDrawableProperties;
    private int mDroppedFrames;
    private long mExpectedRenderTimeMs;
    @Nullable
    private FrameScheduler mFrameScheduler;
    private long mFrameSchedulingDelayMs;
    private long mFrameSchedulingOffsetMs;
    /* access modifiers changed from: private */
    public final Runnable mInvalidateRunnable;
    private volatile boolean mIsRunning;
    private int mLastDrawnFrameNumber;
    private long mLastFrameAnimationTimeMs;
    private long mStartTimeMs;

    public interface DrawListener {
        void onDraw(AnimatedDrawable2 animatedDrawable2, FrameScheduler frameScheduler, int i, boolean z, boolean z2, long j, long j2, long j3, long j4, long j5, long j6, long j7);
    }

    public int getOpacity() {
        return -3;
    }

    public AnimatedDrawable2() {
        this((AnimationBackend) null);
    }

    public AnimatedDrawable2(@Nullable AnimationBackend animationBackend) {
        this.mFrameSchedulingDelayMs = 8;
        this.mFrameSchedulingOffsetMs = 0;
        this.mAnimationListener = NO_OP_LISTENER;
        this.mDrawListener = null;
        this.mInvalidateRunnable = new Runnable() {
            public void run() {
                AnimatedDrawable2.this.unscheduleSelf(AnimatedDrawable2.this.mInvalidateRunnable);
                AnimatedDrawable2.this.invalidateSelf();
            }
        };
        this.mAnimationBackend = animationBackend;
        this.mFrameScheduler = createSchedulerForBackendAndDelayMethod(this.mAnimationBackend);
    }

    public int getIntrinsicWidth() {
        if (this.mAnimationBackend == null) {
            return super.getIntrinsicWidth();
        }
        return this.mAnimationBackend.getIntrinsicWidth();
    }

    public int getIntrinsicHeight() {
        if (this.mAnimationBackend == null) {
            return super.getIntrinsicHeight();
        }
        return this.mAnimationBackend.getIntrinsicHeight();
    }

    public void start() {
        if (!this.mIsRunning && this.mAnimationBackend != null && this.mAnimationBackend.getFrameCount() > 1) {
            this.mIsRunning = true;
            this.mStartTimeMs = now();
            this.mExpectedRenderTimeMs = this.mStartTimeMs;
            this.mLastFrameAnimationTimeMs = -1;
            this.mLastDrawnFrameNumber = -1;
            invalidateSelf();
            this.mAnimationListener.onAnimationStart(this);
        }
    }

    public void stop() {
        if (this.mIsRunning) {
            this.mIsRunning = false;
            this.mStartTimeMs = 0;
            this.mExpectedRenderTimeMs = this.mStartTimeMs;
            this.mLastFrameAnimationTimeMs = -1;
            this.mLastDrawnFrameNumber = -1;
            unscheduleSelf(this.mInvalidateRunnable);
            this.mAnimationListener.onAnimationStop(this);
        }
    }

    public boolean isRunning() {
        return this.mIsRunning;
    }

    /* access modifiers changed from: protected */
    public void onBoundsChange(Rect rect) {
        super.onBoundsChange(rect);
        if (this.mAnimationBackend != null) {
            this.mAnimationBackend.setBounds(rect);
        }
    }

    public void draw(Canvas canvas) {
        long max;
        long j;
        long j2;
        long j3;
        if (this.mAnimationBackend == null || this.mFrameScheduler == null) {
            return;
        }
        long now = now();
        if (this.mIsRunning) {
            max = (now - this.mStartTimeMs) + this.mFrameSchedulingOffsetMs;
        } else {
            max = Math.max(this.mLastFrameAnimationTimeMs, 0);
        }
        long j4 = max;
        int frameNumberToRender = this.mFrameScheduler.getFrameNumberToRender(j4, this.mLastFrameAnimationTimeMs);
        if (frameNumberToRender == -1) {
            frameNumberToRender = this.mAnimationBackend.getFrameCount() - 1;
            this.mAnimationListener.onAnimationStop(this);
            this.mIsRunning = false;
        } else if (frameNumberToRender == 0 && this.mLastDrawnFrameNumber != -1 && now >= this.mExpectedRenderTimeMs) {
            this.mAnimationListener.onAnimationRepeat(this);
        }
        int i = frameNumberToRender;
        boolean drawFrame = this.mAnimationBackend.drawFrame(this, canvas, i);
        if (drawFrame) {
            this.mAnimationListener.onAnimationFrame(this, i);
            this.mLastDrawnFrameNumber = i;
        }
        if (!drawFrame) {
            onFrameDropped();
        }
        long now2 = now();
        if (this.mIsRunning) {
            long targetRenderTimeForNextFrameMs = this.mFrameScheduler.getTargetRenderTimeForNextFrameMs(now2 - this.mStartTimeMs);
            if (targetRenderTimeForNextFrameMs != -1) {
                long j5 = this.mFrameSchedulingDelayMs + targetRenderTimeForNextFrameMs;
                scheduleNextFrame(j5);
                j = j5;
            } else {
                j = -1;
            }
            j2 = targetRenderTimeForNextFrameMs;
        } else {
            j2 = -1;
            j = -1;
        }
        if (this.mDrawListener != null) {
            this.mDrawListener.onDraw(this, this.mFrameScheduler, i, drawFrame, this.mIsRunning, this.mStartTimeMs, j4, this.mLastFrameAnimationTimeMs, now, now2, j2, j);
            j3 = j4;
        } else {
            j3 = j4;
        }
        this.mLastFrameAnimationTimeMs = j3;
    }

    public void setAlpha(int i) {
        if (this.mDrawableProperties == null) {
            this.mDrawableProperties = new DrawableProperties();
        }
        this.mDrawableProperties.setAlpha(i);
        if (this.mAnimationBackend != null) {
            this.mAnimationBackend.setAlpha(i);
        }
    }

    public void setColorFilter(ColorFilter colorFilter) {
        if (this.mDrawableProperties == null) {
            this.mDrawableProperties = new DrawableProperties();
        }
        this.mDrawableProperties.setColorFilter(colorFilter);
        if (this.mAnimationBackend != null) {
            this.mAnimationBackend.setColorFilter(colorFilter);
        }
    }

    public void setAnimationBackend(@Nullable AnimationBackend animationBackend) {
        this.mAnimationBackend = animationBackend;
        if (this.mAnimationBackend != null) {
            this.mFrameScheduler = new DropFramesFrameScheduler(this.mAnimationBackend);
            this.mAnimationBackend.setBounds(getBounds());
            if (this.mDrawableProperties != null) {
                this.mDrawableProperties.applyTo(this);
            }
        }
        this.mFrameScheduler = createSchedulerForBackendAndDelayMethod(this.mAnimationBackend);
        stop();
    }

    @Nullable
    public AnimationBackend getAnimationBackend() {
        return this.mAnimationBackend;
    }

    public long getDroppedFrames() {
        return (long) this.mDroppedFrames;
    }

    public long getStartTimeMs() {
        return this.mStartTimeMs;
    }

    public boolean isInfiniteAnimation() {
        return this.mFrameScheduler != null && this.mFrameScheduler.isInfiniteAnimation();
    }

    public void jumpToFrame(int i) {
        if (this.mAnimationBackend != null && this.mFrameScheduler != null) {
            this.mLastFrameAnimationTimeMs = this.mFrameScheduler.getTargetRenderTimeMs(i);
            this.mStartTimeMs = now() - this.mLastFrameAnimationTimeMs;
            this.mExpectedRenderTimeMs = this.mStartTimeMs;
            invalidateSelf();
        }
    }

    public long getLoopDurationMs() {
        if (this.mAnimationBackend == null) {
            return 0;
        }
        if (this.mFrameScheduler != null) {
            return this.mFrameScheduler.getLoopDurationMs();
        }
        int i = 0;
        for (int i2 = 0; i2 < this.mAnimationBackend.getFrameCount(); i2++) {
            i += this.mAnimationBackend.getFrameDurationMs(i2);
        }
        return (long) i;
    }

    public int getFrameCount() {
        if (this.mAnimationBackend == null) {
            return 0;
        }
        return this.mAnimationBackend.getFrameCount();
    }

    public int getLoopCount() {
        if (this.mAnimationBackend == null) {
            return 0;
        }
        return this.mAnimationBackend.getLoopCount();
    }

    public void setFrameSchedulingDelayMs(long j) {
        this.mFrameSchedulingDelayMs = j;
    }

    public void setFrameSchedulingOffsetMs(long j) {
        this.mFrameSchedulingOffsetMs = j;
    }

    public void setAnimationListener(@Nullable AnimationListener animationListener) {
        if (animationListener == null) {
            animationListener = NO_OP_LISTENER;
        }
        this.mAnimationListener = animationListener;
    }

    public void setDrawListener(@Nullable DrawListener drawListener) {
        this.mDrawListener = drawListener;
    }

    private void scheduleNextFrame(long j) {
        this.mExpectedRenderTimeMs = this.mStartTimeMs + j;
        scheduleSelf(this.mInvalidateRunnable, this.mExpectedRenderTimeMs);
    }

    private void onFrameDropped() {
        this.mDroppedFrames++;
        if (FLog.isLoggable(2)) {
            FLog.v(TAG, "Dropped a frame. Count: %s", (Object) Integer.valueOf(this.mDroppedFrames));
        }
    }

    private long now() {
        return SystemClock.uptimeMillis();
    }

    @Nullable
    private static FrameScheduler createSchedulerForBackendAndDelayMethod(@Nullable AnimationBackend animationBackend) {
        if (animationBackend == null) {
            return null;
        }
        return new DropFramesFrameScheduler(animationBackend);
    }

    /* access modifiers changed from: protected */
    public boolean onLevelChange(int i) {
        if (this.mIsRunning) {
            return false;
        }
        long j = (long) i;
        if (this.mLastFrameAnimationTimeMs == j) {
            return false;
        }
        this.mLastFrameAnimationTimeMs = j;
        invalidateSelf();
        return true;
    }

    public void dropCaches() {
        if (this.mAnimationBackend != null) {
            this.mAnimationBackend.clear();
        }
    }
}
