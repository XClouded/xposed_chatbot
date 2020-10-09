package androidx.recyclerview.selection;

import android.graphics.Point;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.core.util.Preconditions;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

final class ViewAutoScroller extends AutoScroller {
    private static final float DEFAULT_SCROLL_THRESHOLD_RATIO = 0.125f;
    private static final int MAX_SCROLL_STEP = 70;
    private static final String TAG = "ViewAutoScroller";
    private final ScrollHost mHost;
    @Nullable
    private Point mLastLocation;
    @Nullable
    private Point mOrigin;
    private boolean mPassedInitialMotionThreshold;
    private final Runnable mRunner;
    private final float mScrollThresholdRatio;

    ViewAutoScroller(@NonNull ScrollHost scrollHost) {
        this(scrollHost, DEFAULT_SCROLL_THRESHOLD_RATIO);
    }

    @VisibleForTesting
    ViewAutoScroller(@NonNull ScrollHost scrollHost, float f) {
        Preconditions.checkArgument(scrollHost != null);
        this.mHost = scrollHost;
        this.mScrollThresholdRatio = f;
        this.mRunner = new Runnable() {
            public void run() {
                ViewAutoScroller.this.runScroll();
            }
        };
    }

    public void reset() {
        this.mHost.removeCallback(this.mRunner);
        this.mOrigin = null;
        this.mLastLocation = null;
        this.mPassedInitialMotionThreshold = false;
    }

    public void scroll(@NonNull Point point) {
        this.mLastLocation = point;
        if (this.mOrigin == null) {
            this.mOrigin = point;
        }
        this.mHost.runAtNextFrame(this.mRunner);
    }

    /* access modifiers changed from: package-private */
    public void runScroll() {
        int i;
        int viewHeight = (int) (((float) this.mHost.getViewHeight()) * this.mScrollThresholdRatio);
        if (this.mLastLocation.y <= viewHeight) {
            i = this.mLastLocation.y - viewHeight;
        } else {
            i = this.mLastLocation.y >= this.mHost.getViewHeight() - viewHeight ? (this.mLastLocation.y - this.mHost.getViewHeight()) + viewHeight : 0;
        }
        if (i != 0) {
            if (this.mPassedInitialMotionThreshold || aboveMotionThreshold(this.mLastLocation)) {
                this.mPassedInitialMotionThreshold = true;
                if (i <= viewHeight) {
                    viewHeight = i;
                }
                this.mHost.scrollBy(computeScrollDistance(viewHeight));
                this.mHost.removeCallback(this.mRunner);
                this.mHost.runAtNextFrame(this.mRunner);
            }
        }
    }

    private boolean aboveMotionThreshold(@NonNull Point point) {
        return Math.abs(this.mOrigin.y - point.y) >= ((int) ((((float) this.mHost.getViewHeight()) * this.mScrollThresholdRatio) * (this.mScrollThresholdRatio * 2.0f)));
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public int computeScrollDistance(int i) {
        int signum = (int) Math.signum((float) i);
        int smoothOutOfBoundsRatio = (int) (((float) (signum * 70)) * smoothOutOfBoundsRatio(Math.min(1.0f, ((float) Math.abs(i)) / ((float) ((int) (((float) this.mHost.getViewHeight()) * this.mScrollThresholdRatio))))));
        return smoothOutOfBoundsRatio != 0 ? smoothOutOfBoundsRatio : signum;
    }

    private float smoothOutOfBoundsRatio(float f) {
        return (float) Math.pow((double) f, 10.0d);
    }

    static abstract class ScrollHost {
        /* access modifiers changed from: package-private */
        public abstract int getViewHeight();

        /* access modifiers changed from: package-private */
        public abstract void removeCallback(@NonNull Runnable runnable);

        /* access modifiers changed from: package-private */
        public abstract void runAtNextFrame(@NonNull Runnable runnable);

        /* access modifiers changed from: package-private */
        public abstract void scrollBy(int i);

        ScrollHost() {
        }
    }

    static ScrollHost createScrollHost(RecyclerView recyclerView) {
        return new RuntimeHost(recyclerView);
    }

    private static final class RuntimeHost extends ScrollHost {
        private final RecyclerView mRecyclerView;

        RuntimeHost(@NonNull RecyclerView recyclerView) {
            this.mRecyclerView = recyclerView;
        }

        /* access modifiers changed from: package-private */
        public void runAtNextFrame(@NonNull Runnable runnable) {
            ViewCompat.postOnAnimation(this.mRecyclerView, runnable);
        }

        /* access modifiers changed from: package-private */
        public void removeCallback(@NonNull Runnable runnable) {
            this.mRecyclerView.removeCallbacks(runnable);
        }

        /* access modifiers changed from: package-private */
        public void scrollBy(int i) {
            this.mRecyclerView.scrollBy(0, i);
        }

        /* access modifiers changed from: package-private */
        public int getViewHeight() {
            return this.mRecyclerView.getHeight();
        }
    }
}
