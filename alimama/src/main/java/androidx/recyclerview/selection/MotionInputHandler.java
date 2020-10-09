package androidx.recyclerview.selection;

import android.view.GestureDetector;
import android.view.MotionEvent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Preconditions;
import androidx.recyclerview.selection.ItemDetailsLookup;

abstract class MotionInputHandler<K> extends GestureDetector.SimpleOnGestureListener {
    private final FocusDelegate<K> mFocusDelegate;
    private final ItemKeyProvider<K> mKeyProvider;
    protected final SelectionTracker<K> mSelectionTracker;

    MotionInputHandler(@NonNull SelectionTracker<K> selectionTracker, @NonNull ItemKeyProvider<K> itemKeyProvider, @NonNull FocusDelegate<K> focusDelegate) {
        boolean z = false;
        Preconditions.checkArgument(selectionTracker != null);
        Preconditions.checkArgument(itemKeyProvider != null);
        Preconditions.checkArgument(focusDelegate != null ? true : z);
        this.mSelectionTracker = selectionTracker;
        this.mKeyProvider = itemKeyProvider;
        this.mFocusDelegate = focusDelegate;
    }

    /* access modifiers changed from: package-private */
    public final boolean selectItem(@NonNull ItemDetailsLookup.ItemDetails<K> itemDetails) {
        Preconditions.checkArgument(itemDetails != null);
        Preconditions.checkArgument(hasPosition(itemDetails));
        Preconditions.checkArgument(hasSelectionKey(itemDetails));
        if (this.mSelectionTracker.select(itemDetails.getSelectionKey())) {
            this.mSelectionTracker.anchorRange(itemDetails.getPosition());
        }
        if (this.mSelectionTracker.getSelection().size() == 1) {
            this.mFocusDelegate.focusItem(itemDetails);
        } else {
            this.mFocusDelegate.clearFocus();
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public final boolean focusItem(@NonNull ItemDetailsLookup.ItemDetails<K> itemDetails) {
        Preconditions.checkArgument(itemDetails != null);
        Preconditions.checkArgument(hasSelectionKey(itemDetails));
        this.mSelectionTracker.clearSelection();
        this.mFocusDelegate.focusItem(itemDetails);
        return true;
    }

    /* access modifiers changed from: protected */
    public final void extendSelectionRange(@NonNull ItemDetailsLookup.ItemDetails<K> itemDetails) {
        Preconditions.checkState(this.mKeyProvider.hasAccess(0));
        Preconditions.checkArgument(hasPosition(itemDetails));
        Preconditions.checkArgument(hasSelectionKey(itemDetails));
        this.mSelectionTracker.extendRange(itemDetails.getPosition());
        this.mFocusDelegate.focusItem(itemDetails);
    }

    /* access modifiers changed from: package-private */
    public final boolean isRangeExtension(@NonNull MotionEvent motionEvent) {
        if (!MotionEvents.isShiftKeyPressed(motionEvent) || !this.mSelectionTracker.isRangeActive() || !this.mKeyProvider.hasAccess(0)) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public boolean shouldClearSelection(@NonNull MotionEvent motionEvent, @NonNull ItemDetailsLookup.ItemDetails<K> itemDetails) {
        return !MotionEvents.isCtrlKeyPressed(motionEvent) && !itemDetails.inSelectionHotspot(motionEvent) && !this.mSelectionTracker.isSelected(itemDetails.getSelectionKey());
    }

    static boolean hasSelectionKey(@Nullable ItemDetailsLookup.ItemDetails<?> itemDetails) {
        return (itemDetails == null || itemDetails.getSelectionKey() == null) ? false : true;
    }

    static boolean hasPosition(@Nullable ItemDetailsLookup.ItemDetails<?> itemDetails) {
        return (itemDetails == null || itemDetails.getPosition() == -1) ? false : true;
    }
}
