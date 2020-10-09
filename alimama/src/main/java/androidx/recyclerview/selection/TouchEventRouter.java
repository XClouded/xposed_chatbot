package androidx.recyclerview.selection;

import android.view.GestureDetector;
import android.view.MotionEvent;
import androidx.annotation.NonNull;
import androidx.core.util.Preconditions;
import androidx.recyclerview.widget.RecyclerView;

final class TouchEventRouter implements RecyclerView.OnItemTouchListener {
    private static final String TAG = "TouchEventRouter";
    private final ToolHandlerRegistry<RecyclerView.OnItemTouchListener> mDelegates;
    private final GestureDetector mDetector;

    public void onRequestDisallowInterceptTouchEvent(boolean z) {
    }

    TouchEventRouter(@NonNull GestureDetector gestureDetector, @NonNull RecyclerView.OnItemTouchListener onItemTouchListener) {
        boolean z = false;
        Preconditions.checkArgument(gestureDetector != null);
        Preconditions.checkArgument(onItemTouchListener != null ? true : z);
        this.mDetector = gestureDetector;
        this.mDelegates = new ToolHandlerRegistry<>(onItemTouchListener);
    }

    TouchEventRouter(@NonNull GestureDetector gestureDetector) {
        this(gestureDetector, new RecyclerView.OnItemTouchListener() {
            public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
                return false;
            }

            public void onRequestDisallowInterceptTouchEvent(boolean z) {
            }

            public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void register(int i, @NonNull RecyclerView.OnItemTouchListener onItemTouchListener) {
        Preconditions.checkArgument(onItemTouchListener != null);
        this.mDelegates.set(i, onItemTouchListener);
    }

    public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
        return this.mDelegates.get(motionEvent).onInterceptTouchEvent(recyclerView, motionEvent) | this.mDetector.onTouchEvent(motionEvent);
    }

    public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
        this.mDelegates.get(motionEvent).onTouchEvent(recyclerView, motionEvent);
        this.mDetector.onTouchEvent(motionEvent);
    }
}
