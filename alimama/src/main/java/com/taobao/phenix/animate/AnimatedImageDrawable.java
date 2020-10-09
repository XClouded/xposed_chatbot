package com.taobao.phenix.animate;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import com.taobao.pexode.animate.AnimatedImage;
import com.taobao.phenix.cache.memory.PassableBitmapDrawable;
import com.taobao.phenix.chain.DefaultSchedulerSupplier;
import com.taobao.phenix.common.UnitedLog;
import com.taobao.phenix.intf.Phenix;
import com.taobao.rxm.schedule.Scheduler;
import com.taobao.rxm.schedule.SchedulerSupplier;
import com.taobao.weex.el.parse.Operators;
import java.lang.ref.WeakReference;
import java.util.Arrays;

public class AnimatedImageDrawable extends PassableBitmapDrawable {
    private static final int CHOREOGRAPHER_SUBTRACT_FRAME_DELAY = 10;
    private static final int FRAME_DURATION_MS_FOR_MIN = 100;
    private static final int MIN_FRAME_DURATION_MS = 11;
    private static final int TIMEOUT_FOR_DRAW_MS = 1000;
    private static SchedulerSupplier sOneSchedulerSupplier;
    private int[] mAccruedFrameDurations;
    private boolean mAllowPlaying;
    private AnimatedFramesBuffer mAnimatedFramesBuffer;
    private int mCurrAbsFrameNum;
    private int mCurrRelFrameNum;
    private final int mDurationMs;
    private final int mFrameCount;
    private int[] mFrameDurations;
    private final Handler mHandler;
    private final int mImageHeight;
    private final int mImageWidth;
    private final Runnable mInvalidateTask;
    private boolean mIsPaused;
    private int mLastFrameAbsNum;
    private int mLoopCountAtLastPause;
    private AnimatedLoopListener mLoopListener;
    private int mMaxLoopCount;
    private final Runnable mNextFrameTask;
    private long mNextFrameTaskMs;
    private Bitmap mRenderingBitmap;
    private int mStartFrameMustBeRendered;
    private final Runnable mStartTask;
    private long mStartTimeMs;
    private final Runnable mTimeout4DrawTask;
    private boolean mWaitingForUpdate;

    public int getOpacity() {
        return -3;
    }

    private static class WeakFrameTask implements Runnable {
        public static final int INVALIDATE_TYPE = 2;
        public static final int NEXT_TYPE = 1;
        public static final int START_TYPE = 0;
        public static final int TIMEOUT_FOR_DRAW_TYPE = 3;
        private WeakReference<AnimatedImageDrawable> drawableRef;
        private int type;

        public WeakFrameTask(AnimatedImageDrawable animatedImageDrawable, int i) {
            this.drawableRef = new WeakReference<>(animatedImageDrawable);
            this.type = i;
        }

        public void run() {
            AnimatedImageDrawable animatedImageDrawable = (AnimatedImageDrawable) this.drawableRef.get();
            if (animatedImageDrawable != null) {
                switch (this.type) {
                    case 0:
                        animatedImageDrawable.onStart();
                        return;
                    case 1:
                        animatedImageDrawable.onNextFrame();
                        return;
                    case 2:
                        animatedImageDrawable.doInvalidateSelf();
                        return;
                    case 3:
                        animatedImageDrawable.onTimeout4Draw();
                        return;
                    default:
                        return;
                }
            }
        }
    }

    public AnimatedImageDrawable(AnimatedImage animatedImage) {
        this((String) null, (String) null, 0, 0, animatedImage);
    }

    public AnimatedImageDrawable(String str, String str2, int i, int i2, AnimatedImage animatedImage) {
        super(str, str2, i, i2);
        this.mStartTask = new WeakFrameTask(this, 0);
        this.mNextFrameTask = new WeakFrameTask(this, 1);
        this.mInvalidateTask = new WeakFrameTask(this, 2);
        this.mTimeout4DrawTask = new WeakFrameTask(this, 3);
        this.mHandler = new Handler(Looper.getMainLooper());
        this.mImageWidth = animatedImage.getWidth();
        this.mImageHeight = animatedImage.getHeight();
        this.mFrameDurations = animatedImage.getFrameDurations();
        this.mMaxLoopCount = animatedImage.getLoopCount();
        this.mFrameCount = animatedImage.getFrameCount();
        this.mStartFrameMustBeRendered = 0;
        this.mLoopCountAtLastPause = 0;
        this.mNextFrameTaskMs = -1;
        this.mWaitingForUpdate = true;
        this.mAllowPlaying = true;
        this.mDurationMs = accrueAndFixDurations();
        SchedulerSupplier schedulerSupplierUsedInProducer = Phenix.instance().getSchedulerSupplierUsedInProducer();
        if (schedulerSupplierUsedInProducer == null) {
            synchronized (AnimatedImageDrawable.class) {
                if (sOneSchedulerSupplier == null) {
                    sOneSchedulerSupplier = new DefaultSchedulerSupplier((Scheduler) null, 0, 3, 8, 5, 1500, 3, 0, 0);
                }
            }
            schedulerSupplierUsedInProducer = sOneSchedulerSupplier;
        }
        this.mAnimatedFramesBuffer = new AnimatedFramesBuffer(animatedImage, schedulerSupplierUsedInProducer.forDecode(), toString());
    }

    private int accrueAndFixDurations() {
        this.mAccruedFrameDurations = new int[this.mFrameCount];
        int i = 0;
        for (int i2 = 0; i2 < this.mFrameCount; i2++) {
            if (this.mFrameDurations[i2] < 11) {
                this.mFrameDurations[i2] = 100;
            }
            this.mAccruedFrameDurations[i2] = i;
            i += this.mFrameDurations[i2];
        }
        return i;
    }

    /* access modifiers changed from: private */
    public void doInvalidateSelf() {
        this.mWaitingForUpdate = true;
        this.mHandler.removeCallbacks(this.mTimeout4DrawTask);
        this.mHandler.postDelayed(this.mTimeout4DrawTask, 1000);
        invalidateSelf();
    }

    /* access modifiers changed from: package-private */
    public void onStart() {
        if (this.mAllowPlaying) {
            if (this.mIsPaused) {
                this.mStartFrameMustBeRendered = this.mCurrRelFrameNum;
            } else {
                this.mCurrRelFrameNum = 0;
                this.mCurrAbsFrameNum = 0;
                this.mStartFrameMustBeRendered = 0;
            }
            doInvalidateSelf();
        }
    }

    public void start() {
        if (this.mDurationMs != 0 && this.mFrameCount > 1) {
            this.mAllowPlaying = true;
            scheduleSelf(this.mStartTask, SystemClock.uptimeMillis());
        }
    }

    public boolean isPlaying() {
        return this.mAllowPlaying;
    }

    /* access modifiers changed from: package-private */
    public void onNextFrame() {
        this.mNextFrameTaskMs = -1;
        if (this.mAllowPlaying && this.mDurationMs != 0 && this.mFrameCount > 1) {
            computeAndScheduleNextFrame(true, false);
        }
    }

    private int getFrameNumWithTimestamp(int i) {
        int binarySearch = Arrays.binarySearch(this.mAccruedFrameDurations, i);
        return binarySearch < 0 ? ((-binarySearch) - 1) - 1 : binarySearch;
    }

    private void computeAndScheduleNextFrame(boolean z, boolean z2) {
        if (this.mDurationMs != 0) {
            long uptimeMillis = SystemClock.uptimeMillis();
            long j = uptimeMillis - this.mStartTimeMs;
            int i = (int) (j / ((long) this.mDurationMs));
            int i2 = (int) (j % ((long) this.mDurationMs));
            int frameNumWithTimestamp = getFrameNumWithTimestamp(i2);
            boolean z3 = this.mCurrRelFrameNum != frameNumWithTimestamp;
            this.mCurrRelFrameNum = frameNumWithTimestamp;
            this.mCurrAbsFrameNum = (i * this.mFrameCount) + frameNumWithTimestamp;
            if (z) {
                if (z3) {
                    UnitedLog.d("AnimatedImage", "%s schedule next frame changed to %d, drawing=%b, now=%d", this, Integer.valueOf(this.mCurrRelFrameNum), Boolean.valueOf(z2), Long.valueOf(uptimeMillis));
                    doInvalidateSelf();
                    return;
                }
                int i3 = (this.mAccruedFrameDurations[this.mCurrRelFrameNum] + this.mFrameDurations[this.mCurrRelFrameNum]) - i2;
                int i4 = (this.mCurrRelFrameNum + 1) % this.mFrameCount;
                long j2 = ((long) i3) + uptimeMillis + 10;
                if (this.mNextFrameTaskMs == -1 || this.mNextFrameTaskMs > j2) {
                    UnitedLog.d("AnimatedImage", "%s schedule next frame=%d at %d[last:%d], drawing=%b, now=%d", this, Integer.valueOf(i4), Long.valueOf(j2), Long.valueOf(this.mNextFrameTaskMs), Boolean.valueOf(z2), Long.valueOf(uptimeMillis));
                    unscheduleSelf(this.mNextFrameTask);
                    scheduleSelf(this.mNextFrameTask, j2);
                    this.mNextFrameTaskMs = j2;
                }
            }
        }
    }

    public void setMaxLoopCount(int i) {
        this.mMaxLoopCount = i;
    }

    public void setAnimatedLoopListener(AnimatedLoopListener animatedLoopListener) {
        this.mLoopListener = animatedLoopListener;
    }

    private boolean updateRenderingBitmap(int i, int i2) {
        Bitmap cachedBitmapAt = this.mAnimatedFramesBuffer.getCachedBitmapAt(i);
        if (cachedBitmapAt == null) {
            return false;
        }
        if (this.mRenderingBitmap != null) {
            this.mAnimatedFramesBuffer.freeBitmap(this.mRenderingBitmap);
        }
        this.mRenderingBitmap = cachedBitmapAt;
        if (i2 - this.mLastFrameAbsNum > 1) {
            UnitedLog.w("AnimatedImage", "%s dropped %d frames", this, Integer.valueOf((i2 - this.mLastFrameAbsNum) - 1));
        }
        this.mLastFrameAbsNum = i2;
        return true;
    }

    public void draw(Canvas canvas) {
        Runnable runnable;
        int i;
        UnitedLog.d("AnimatedImage", "%s start to draw, waiting=%b, playing=%b", this, Boolean.valueOf(this.mWaitingForUpdate), Boolean.valueOf(this.mAllowPlaying));
        this.mHandler.removeCallbacks(this.mTimeout4DrawTask);
        if (this.mWaitingForUpdate && (this.mAllowPlaying || this.mRenderingBitmap == null)) {
            this.mWaitingForUpdate = false;
            try {
                if (this.mStartFrameMustBeRendered >= 0) {
                    this.mStartTimeMs = SystemClock.uptimeMillis() - ((long) this.mAccruedFrameDurations[this.mStartFrameMustBeRendered]);
                }
                computeAndScheduleNextFrame(false, true);
                int i2 = this.mCurrRelFrameNum;
                int i3 = this.mCurrAbsFrameNum;
                int i4 = this.mLastFrameAbsNum;
                boolean updateRenderingBitmap = updateRenderingBitmap(i2, i3);
                UnitedLog.d("AnimatedImage", "%s drew frame=%d|%d, success=%B", this, Integer.valueOf(i2), Integer.valueOf(i3), Boolean.valueOf(updateRenderingBitmap));
                if (updateRenderingBitmap) {
                    boolean z = this.mStartFrameMustBeRendered == i2;
                    if (z) {
                        this.mStartFrameMustBeRendered = -1;
                    }
                    int i5 = this.mLoopCountAtLastPause + ((i3 + 1) / this.mFrameCount);
                    boolean z2 = i5 != this.mLoopCountAtLastPause + ((i4 + 1) / this.mFrameCount);
                    if (((!(z && this.mLoopCountAtLastPause == 0 && i3 == 0) && !z2) || this.mLoopListener == null || this.mLoopListener.onLoopCompleted(i5, this.mMaxLoopCount)) && (!z2 || this.mMaxLoopCount == 0 || i5 < this.mMaxLoopCount)) {
                        computeAndScheduleNextFrame(true, true);
                    } else {
                        this.mAllowPlaying = false;
                    }
                    if (!this.mAllowPlaying) {
                        doEnd();
                    }
                }
                if (this.mAllowPlaying || this.mRenderingBitmap == null) {
                    if (updateRenderingBitmap) {
                        runnable = null;
                        i = 1;
                    } else {
                        runnable = this.mInvalidateTask;
                        i = 0;
                    }
                    if (this.mAllowPlaying) {
                        this.mAnimatedFramesBuffer.startCacheFrom((i2 + i) % this.mFrameCount, runnable);
                    } else {
                        this.mAnimatedFramesBuffer.startCacheFrom((i2 + i) % this.mFrameCount, 1, runnable);
                    }
                }
            } catch (Throwable th) {
                UnitedLog.e("AnimatedImage", "%s frame render error=%s", this, th);
            }
        }
        if (this.mRenderingBitmap != null) {
            canvas.drawBitmap(this.mRenderingBitmap, (Rect) null, getBounds(), (Paint) null);
        }
    }

    public void pause() {
        pause(true);
    }

    public void pause(boolean z) {
        this.mIsPaused = true;
        this.mAllowPlaying = false;
        if (z) {
            this.mAnimatedFramesBuffer.dropCaches();
        }
        this.mLoopCountAtLastPause += (this.mCurrAbsFrameNum + 1) / this.mFrameCount;
    }

    public void stop() {
        this.mIsPaused = false;
        this.mAllowPlaying = false;
        doEnd();
    }

    private void doEnd() {
        this.mLoopCountAtLastPause = 0;
        this.mAnimatedFramesBuffer.dropCaches();
    }

    /* access modifiers changed from: package-private */
    public void onTimeout4Draw() {
        unscheduleSelf(this.mNextFrameTask);
        this.mNextFrameTaskMs = -1;
        this.mStartFrameMustBeRendered = 0;
        this.mLastFrameAbsNum = 0;
        this.mRenderingBitmap = null;
        doEnd();
        UnitedLog.d("AnimatedImage", "%s timeout for draw, maybe terminate", this);
    }

    public void setAlpha(int i) {
        invalidateSelf();
    }

    public void setColorFilter(ColorFilter colorFilter) {
        invalidateSelf();
    }

    public int getIntrinsicWidth() {
        return this.mImageWidth;
    }

    public int getIntrinsicHeight() {
        return this.mImageHeight;
    }

    public int getFrameCount() {
        return this.mFrameCount;
    }

    public int getDurationMs() {
        return this.mDurationMs;
    }

    public String toString() {
        return "AnimatedImageDrawable(" + Integer.toHexString(hashCode()) + ", key@" + getMemoryCacheKey() + Operators.BRACKET_END_STR;
    }
}
