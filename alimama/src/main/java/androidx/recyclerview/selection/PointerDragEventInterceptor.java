package androidx.recyclerview.selection;

import android.view.MotionEvent;
import androidx.annotation.Nullable;
import androidx.core.util.Preconditions;
import androidx.recyclerview.widget.RecyclerView;

final class PointerDragEventInterceptor implements RecyclerView.OnItemTouchListener {
    @Nullable
    private RecyclerView.OnItemTouchListener mDelegate;
    private final OnDragInitiatedListener mDragListener;
    private final ItemDetailsLookup mEventDetailsLookup;

    PointerDragEventInterceptor(ItemDetailsLookup itemDetailsLookup, OnDragInitiatedListener onDragInitiatedListener, @Nullable RecyclerView.OnItemTouchListener onItemTouchListener) {
        boolean z = false;
        Preconditions.checkArgument(itemDetailsLookup != null);
        Preconditions.checkArgument(onDragInitiatedListener != null ? true : z);
        this.mEventDetailsLookup = itemDetailsLookup;
        this.mDragListener = onDragInitiatedListener;
        this.mDelegate = onItemTouchListener;
    }

    public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
        if (MotionEvents.isPointerDragEvent(motionEvent) && this.mEventDetailsLookup.inItemDragRegion(motionEvent)) {
            return this.mDragListener.onDragInitiated(motionEvent);
        }
        if (this.mDelegate != null) {
            return this.mDelegate.onInterceptTouchEvent(recyclerView, motionEvent);
        }
        return false;
    }

    public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
        if (this.mDelegate != null) {
            this.mDelegate.onTouchEvent(recyclerView, motionEvent);
        }
    }

    public void onRequestDisallowInterceptTouchEvent(boolean z) {
        if (this.mDelegate != null) {
            this.mDelegate.onRequestDisallowInterceptTouchEvent(z);
        }
    }
}
