package androidx.recyclerview.selection;

import android.view.MotionEvent;
import androidx.annotation.NonNull;

public interface OnContextClickListener {
    boolean onContextClick(@NonNull MotionEvent motionEvent);
}
