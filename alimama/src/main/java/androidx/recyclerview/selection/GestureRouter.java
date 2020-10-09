package androidx.recyclerview.selection;

import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Preconditions;

final class GestureRouter<T extends GestureDetector.OnGestureListener & GestureDetector.OnDoubleTapListener> implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {
    private final ToolHandlerRegistry<T> mDelegates;

    GestureRouter(@NonNull T t) {
        Preconditions.checkArgument(t != null);
        this.mDelegates = new ToolHandlerRegistry<>(t);
    }

    GestureRouter() {
        this(new GestureDetector.SimpleOnGestureListener());
    }

    public void register(int i, @Nullable T t) {
        this.mDelegates.set(i, t);
    }

    public boolean onSingleTapConfirmed(@NonNull MotionEvent motionEvent) {
        return ((GestureDetector.OnDoubleTapListener) ((GestureDetector.OnGestureListener) this.mDelegates.get(motionEvent))).onSingleTapConfirmed(motionEvent);
    }

    public boolean onDoubleTap(@NonNull MotionEvent motionEvent) {
        return ((GestureDetector.OnDoubleTapListener) ((GestureDetector.OnGestureListener) this.mDelegates.get(motionEvent))).onDoubleTap(motionEvent);
    }

    public boolean onDoubleTapEvent(@NonNull MotionEvent motionEvent) {
        return ((GestureDetector.OnDoubleTapListener) ((GestureDetector.OnGestureListener) this.mDelegates.get(motionEvent))).onDoubleTapEvent(motionEvent);
    }

    public boolean onDown(@NonNull MotionEvent motionEvent) {
        return ((GestureDetector.OnGestureListener) this.mDelegates.get(motionEvent)).onDown(motionEvent);
    }

    public void onShowPress(@NonNull MotionEvent motionEvent) {
        ((GestureDetector.OnGestureListener) this.mDelegates.get(motionEvent)).onShowPress(motionEvent);
    }

    public boolean onSingleTapUp(@NonNull MotionEvent motionEvent) {
        return ((GestureDetector.OnGestureListener) this.mDelegates.get(motionEvent)).onSingleTapUp(motionEvent);
    }

    public boolean onScroll(@NonNull MotionEvent motionEvent, @NonNull MotionEvent motionEvent2, float f, float f2) {
        return ((GestureDetector.OnGestureListener) this.mDelegates.get(motionEvent2)).onScroll(motionEvent, motionEvent2, f, f2);
    }

    public void onLongPress(@NonNull MotionEvent motionEvent) {
        ((GestureDetector.OnGestureListener) this.mDelegates.get(motionEvent)).onLongPress(motionEvent);
    }

    public boolean onFling(@NonNull MotionEvent motionEvent, @NonNull MotionEvent motionEvent2, float f, float f2) {
        return ((GestureDetector.OnGestureListener) this.mDelegates.get(motionEvent2)).onFling(motionEvent, motionEvent2, f, f2);
    }
}
