package androidx.recyclerview.selection;

import androidx.annotation.NonNull;
import androidx.recyclerview.selection.SelectionTracker;

public final class SelectionPredicates {
    private SelectionPredicates() {
    }

    public static <K> SelectionTracker.SelectionPredicate<K> createSelectAnything() {
        return new SelectionTracker.SelectionPredicate<K>() {
            public boolean canSelectMultiple() {
                return true;
            }

            public boolean canSetStateAtPosition(int i, boolean z) {
                return true;
            }

            public boolean canSetStateForKey(@NonNull K k, boolean z) {
                return true;
            }
        };
    }

    public static <K> SelectionTracker.SelectionPredicate<K> createSelectSingleAnything() {
        return new SelectionTracker.SelectionPredicate<K>() {
            public boolean canSelectMultiple() {
                return false;
            }

            public boolean canSetStateAtPosition(int i, boolean z) {
                return true;
            }

            public boolean canSetStateForKey(@NonNull K k, boolean z) {
                return true;
            }
        };
    }
}
