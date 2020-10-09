package androidx.recyclerview.selection;

import android.view.MotionEvent;
import androidx.annotation.NonNull;
import androidx.core.util.Preconditions;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.selection.SelectionTracker;

final class TouchInputHandler<K> extends MotionInputHandler<K> {
    private static final boolean DEBUG = false;
    private static final String TAG = "TouchInputDelegate";
    private final ItemDetailsLookup<K> mDetailsLookup;
    private final Runnable mGestureStarter;
    private final Runnable mHapticPerformer;
    private final OnDragInitiatedListener mOnDragInitiatedListener;
    private final OnItemActivatedListener<K> mOnItemActivatedListener;
    private final SelectionTracker.SelectionPredicate<K> mSelectionPredicate;

    TouchInputHandler(@NonNull SelectionTracker<K> selectionTracker, @NonNull ItemKeyProvider<K> itemKeyProvider, @NonNull ItemDetailsLookup<K> itemDetailsLookup, @NonNull SelectionTracker.SelectionPredicate<K> selectionPredicate, @NonNull Runnable runnable, @NonNull OnDragInitiatedListener onDragInitiatedListener, @NonNull OnItemActivatedListener<K> onItemActivatedListener, @NonNull FocusDelegate<K> focusDelegate, @NonNull Runnable runnable2) {
        super(selectionTracker, itemKeyProvider, focusDelegate);
        boolean z = false;
        Preconditions.checkArgument(itemDetailsLookup != null);
        Preconditions.checkArgument(selectionPredicate != null);
        Preconditions.checkArgument(runnable != null);
        Preconditions.checkArgument(onItemActivatedListener != null);
        Preconditions.checkArgument(onDragInitiatedListener != null);
        Preconditions.checkArgument(runnable2 != null ? true : z);
        this.mDetailsLookup = itemDetailsLookup;
        this.mSelectionPredicate = selectionPredicate;
        this.mGestureStarter = runnable;
        this.mOnItemActivatedListener = onItemActivatedListener;
        this.mOnDragInitiatedListener = onDragInitiatedListener;
        this.mHapticPerformer = runnable2;
    }

    public boolean onSingleTapUp(@NonNull MotionEvent motionEvent) {
        if (!this.mDetailsLookup.overItemWithSelectionKey(motionEvent)) {
            this.mSelectionTracker.clearSelection();
            return false;
        }
        ItemDetailsLookup.ItemDetails<K> itemDetails = this.mDetailsLookup.getItemDetails(motionEvent);
        if (itemDetails == null) {
            return false;
        }
        if (this.mSelectionTracker.hasSelection()) {
            if (isRangeExtension(motionEvent)) {
                extendSelectionRange(itemDetails);
                return true;
            } else if (this.mSelectionTracker.isSelected(itemDetails.getSelectionKey())) {
                this.mSelectionTracker.deselect(itemDetails.getSelectionKey());
                return true;
            } else {
                selectItem(itemDetails);
                return true;
            }
        } else if (itemDetails.inSelectionHotspot(motionEvent)) {
            return selectItem(itemDetails);
        } else {
            return this.mOnItemActivatedListener.onItemActivated(itemDetails, motionEvent);
        }
    }

    public void onLongPress(@NonNull MotionEvent motionEvent) {
        ItemDetailsLookup.ItemDetails<K> itemDetails;
        if (this.mDetailsLookup.overItemWithSelectionKey(motionEvent) && (itemDetails = this.mDetailsLookup.getItemDetails(motionEvent)) != null) {
            boolean z = true;
            if (isRangeExtension(motionEvent)) {
                extendSelectionRange(itemDetails);
            } else if (this.mSelectionTracker.isSelected(itemDetails.getSelectionKey()) || !this.mSelectionPredicate.canSetStateForKey(itemDetails.getSelectionKey(), true)) {
                this.mOnDragInitiatedListener.onDragInitiated(motionEvent);
            } else if (!selectItem(itemDetails)) {
                z = false;
            } else if (this.mSelectionPredicate.canSelectMultiple()) {
                this.mGestureStarter.run();
            }
            if (z) {
                this.mHapticPerformer.run();
            }
        }
    }
}
