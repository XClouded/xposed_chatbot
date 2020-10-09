package androidx.recyclerview.selection;

import android.view.MotionEvent;
import androidx.annotation.NonNull;

public interface OnDragInitiatedListener {
    boolean onDragInitiated(@NonNull MotionEvent motionEvent);
}
