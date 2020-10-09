package androidx.recyclerview.selection;

import android.view.MotionEvent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public abstract class ItemDetailsLookup<K> {
    @Nullable
    public abstract ItemDetails<K> getItemDetails(@NonNull MotionEvent motionEvent);

    /* access modifiers changed from: package-private */
    public final boolean overItem(@NonNull MotionEvent motionEvent) {
        return getItemPosition(motionEvent) != -1;
    }

    /* access modifiers changed from: package-private */
    public final boolean overItemWithSelectionKey(@NonNull MotionEvent motionEvent) {
        return overItem(motionEvent) && hasSelectionKey(getItemDetails(motionEvent));
    }

    /* access modifiers changed from: package-private */
    public final boolean inItemDragRegion(@NonNull MotionEvent motionEvent) {
        return overItem(motionEvent) && getItemDetails(motionEvent).inDragRegion(motionEvent);
    }

    /* access modifiers changed from: package-private */
    public final boolean inItemSelectRegion(@NonNull MotionEvent motionEvent) {
        return overItem(motionEvent) && getItemDetails(motionEvent).inSelectionHotspot(motionEvent);
    }

    /* access modifiers changed from: package-private */
    public final int getItemPosition(@NonNull MotionEvent motionEvent) {
        ItemDetails itemDetails = getItemDetails(motionEvent);
        if (itemDetails != null) {
            return itemDetails.getPosition();
        }
        return -1;
    }

    private static boolean hasSelectionKey(@Nullable ItemDetails<?> itemDetails) {
        return (itemDetails == null || itemDetails.getSelectionKey() == null) ? false : true;
    }

    private static boolean hasPosition(@Nullable ItemDetails<?> itemDetails) {
        return (itemDetails == null || itemDetails.getPosition() == -1) ? false : true;
    }

    public static abstract class ItemDetails<K> {
        public abstract int getPosition();

        @Nullable
        public abstract K getSelectionKey();

        public boolean inDragRegion(@NonNull MotionEvent motionEvent) {
            return false;
        }

        public boolean inSelectionHotspot(@NonNull MotionEvent motionEvent) {
            return false;
        }

        public boolean hasSelectionKey() {
            return getSelectionKey() != null;
        }

        public boolean equals(@Nullable Object obj) {
            return (obj instanceof ItemDetails) && isEqualTo((ItemDetails) obj);
        }

        private boolean isEqualTo(@NonNull ItemDetails itemDetails) {
            boolean z;
            Object selectionKey = getSelectionKey();
            if (selectionKey == null) {
                z = itemDetails.getSelectionKey() == null;
            } else {
                z = selectionKey.equals(itemDetails.getSelectionKey());
            }
            if (!z || getPosition() != itemDetails.getPosition()) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            return getPosition() >>> 8;
        }
    }
}
