package androidx.recyclerview.selection;

import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.core.util.Preconditions;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

final class GestureSelectionHelper implements RecyclerView.OnItemTouchListener {
    private static final String TAG = "GestureSelectionHelper";
    private final ItemDetailsLookup<?> mDetailsLookup;
    private int mLastStartedItemPos = -1;
    private final OperationMonitor mLock;
    private final AutoScroller mScroller;
    private final SelectionTracker<?> mSelectionMgr;
    private boolean mStarted;
    private final ViewDelegate mView;

    static float getInboundY(float f, float f2) {
        if (f2 < 0.0f) {
            return 0.0f;
        }
        return f2 > f ? f : f2;
    }

    public void onRequestDisallowInterceptTouchEvent(boolean z) {
    }

    GestureSelectionHelper(@NonNull SelectionTracker<?> selectionTracker, @NonNull ItemDetailsLookup<?> itemDetailsLookup, @NonNull ViewDelegate viewDelegate, @NonNull AutoScroller autoScroller, @NonNull OperationMonitor operationMonitor) {
        boolean z = false;
        this.mStarted = false;
        Preconditions.checkArgument(selectionTracker != null);
        Preconditions.checkArgument(itemDetailsLookup != null);
        Preconditions.checkArgument(viewDelegate != null);
        Preconditions.checkArgument(autoScroller != null);
        Preconditions.checkArgument(operationMonitor != null ? true : z);
        this.mSelectionMgr = selectionTracker;
        this.mDetailsLookup = itemDetailsLookup;
        this.mView = viewDelegate;
        this.mScroller = autoScroller;
        this.mLock = operationMonitor;
    }

    /* access modifiers changed from: package-private */
    public void start() {
        Preconditions.checkState(!this.mStarted);
        if (this.mLastStartedItemPos == -1) {
            Log.w(TAG, "Illegal state. Can't start without valid mLastStartedItemPos.");
            return;
        }
        Preconditions.checkState(this.mSelectionMgr.isRangeActive());
        this.mLock.checkStopped();
        this.mStarted = true;
        this.mLock.start();
    }

    public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
        MotionEvents.isMouseEvent(motionEvent);
        if (motionEvent.getActionMasked() == 0 && this.mDetailsLookup.getItemDetails(motionEvent) != null) {
            this.mLastStartedItemPos = this.mView.getItemUnder(motionEvent);
        }
        return handleTouch(motionEvent);
    }

    public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
        handleTouch(motionEvent);
    }

    private boolean handleTouch(MotionEvent motionEvent) {
        if (!this.mStarted) {
            return false;
        }
        switch (motionEvent.getActionMasked()) {
            case 1:
                handleUpEvent();
                return true;
            case 2:
                handleMoveEvent(motionEvent);
                return true;
            case 3:
                handleCancelEvent();
                return true;
            default:
                return false;
        }
    }

    private void handleUpEvent() {
        this.mSelectionMgr.mergeProvisionalSelection();
        endSelection();
        if (this.mLastStartedItemPos != -1) {
            this.mSelectionMgr.startRange(this.mLastStartedItemPos);
        }
    }

    private void handleCancelEvent() {
        this.mSelectionMgr.clearProvisionalSelection();
        endSelection();
    }

    private void endSelection() {
        Preconditions.checkState(this.mStarted);
        this.mLastStartedItemPos = -1;
        this.mStarted = false;
        this.mScroller.reset();
        this.mLock.stop();
    }

    private void handleMoveEvent(@NonNull MotionEvent motionEvent) {
        Point origin = MotionEvents.getOrigin(motionEvent);
        int lastGlidedItemPosition = this.mView.getLastGlidedItemPosition(motionEvent);
        if (lastGlidedItemPosition != -1) {
            extendSelection(lastGlidedItemPosition);
        }
        this.mScroller.scroll(origin);
    }

    private void extendSelection(int i) {
        this.mSelectionMgr.extendProvisionalRange(i);
    }

    static GestureSelectionHelper create(@NonNull SelectionTracker<?> selectionTracker, @NonNull ItemDetailsLookup<?> itemDetailsLookup, @NonNull RecyclerView recyclerView, @NonNull AutoScroller autoScroller, @NonNull OperationMonitor operationMonitor) {
        return new GestureSelectionHelper(selectionTracker, itemDetailsLookup, new RecyclerViewDelegate(recyclerView), autoScroller, operationMonitor);
    }

    @VisibleForTesting
    static abstract class ViewDelegate {
        /* access modifiers changed from: package-private */
        public abstract int getHeight();

        /* access modifiers changed from: package-private */
        public abstract int getItemUnder(@NonNull MotionEvent motionEvent);

        /* access modifiers changed from: package-private */
        public abstract int getLastGlidedItemPosition(@NonNull MotionEvent motionEvent);

        ViewDelegate() {
        }
    }

    @VisibleForTesting
    static final class RecyclerViewDelegate extends ViewDelegate {
        private final RecyclerView mRecyclerView;

        RecyclerViewDelegate(@NonNull RecyclerView recyclerView) {
            Preconditions.checkArgument(recyclerView != null);
            this.mRecyclerView = recyclerView;
        }

        /* access modifiers changed from: package-private */
        public int getHeight() {
            return this.mRecyclerView.getHeight();
        }

        /* access modifiers changed from: package-private */
        public int getItemUnder(@NonNull MotionEvent motionEvent) {
            View findChildViewUnder = this.mRecyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
            if (findChildViewUnder != null) {
                return this.mRecyclerView.getChildAdapterPosition(findChildViewUnder);
            }
            return -1;
        }

        /* access modifiers changed from: package-private */
        public int getLastGlidedItemPosition(@NonNull MotionEvent motionEvent) {
            View childAt = this.mRecyclerView.getLayoutManager().getChildAt(this.mRecyclerView.getLayoutManager().getChildCount() - 1);
            boolean isPastLastItem = isPastLastItem(childAt.getTop(), childAt.getLeft(), childAt.getRight(), motionEvent, ViewCompat.getLayoutDirection(this.mRecyclerView));
            float inboundY = GestureSelectionHelper.getInboundY((float) this.mRecyclerView.getHeight(), motionEvent.getY());
            if (isPastLastItem) {
                return this.mRecyclerView.getAdapter().getItemCount() - 1;
            }
            return this.mRecyclerView.getChildAdapterPosition(this.mRecyclerView.findChildViewUnder(motionEvent.getX(), inboundY));
        }

        @VisibleForTesting
        static boolean isPastLastItem(int i, int i2, int i3, @NonNull MotionEvent motionEvent, int i4) {
            if (i4 == 0) {
                return motionEvent.getX() > ((float) i3) && motionEvent.getY() > ((float) i);
            }
            if (motionEvent.getX() >= ((float) i2) || motionEvent.getY() <= ((float) i)) {
                return false;
            }
            return true;
        }
    }
}
