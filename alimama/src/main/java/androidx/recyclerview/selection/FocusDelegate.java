package androidx.recyclerview.selection;

import androidx.annotation.NonNull;
import androidx.recyclerview.selection.ItemDetailsLookup;

public abstract class FocusDelegate<K> {
    public abstract void clearFocus();

    public abstract void focusItem(@NonNull ItemDetailsLookup.ItemDetails<K> itemDetails);

    public abstract int getFocusedPosition();

    public abstract boolean hasFocusedItem();

    static <K> FocusDelegate<K> dummy() {
        return new FocusDelegate<K>() {
            public void clearFocus() {
            }

            public void focusItem(@NonNull ItemDetailsLookup.ItemDetails<K> itemDetails) {
            }

            public int getFocusedPosition() {
                return -1;
            }

            public boolean hasFocusedItem() {
                return false;
            }
        };
    }
}
