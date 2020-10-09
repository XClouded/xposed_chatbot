package androidx.recyclerview.selection;

import android.view.MotionEvent;
import androidx.annotation.NonNull;
import androidx.recyclerview.selection.ItemDetailsLookup;

public interface OnItemActivatedListener<K> {
    boolean onItemActivated(@NonNull ItemDetailsLookup.ItemDetails<K> itemDetails, @NonNull MotionEvent motionEvent);
}
