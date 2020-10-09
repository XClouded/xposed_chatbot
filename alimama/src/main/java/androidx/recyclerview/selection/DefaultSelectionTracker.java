package androidx.recyclerview.selection;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.annotation.VisibleForTesting;
import androidx.core.util.Preconditions;
import androidx.recyclerview.selection.Range;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class DefaultSelectionTracker<K> extends SelectionTracker<K> {
    private static final String EXTRA_SELECTION_PREFIX = "androidx.recyclerview.selection";
    private static final String TAG = "DefaultSelectionTracker";
    private final AdapterObserver mAdapterObserver;
    private final ItemKeyProvider<K> mKeyProvider;
    private final List<SelectionTracker.SelectionObserver> mObservers = new ArrayList(1);
    @Nullable
    private Range mRange;
    private final DefaultSelectionTracker<K>.RangeCallbacks mRangeCallbacks;
    private final Selection<K> mSelection = new Selection<>();
    private final String mSelectionId;
    private final SelectionTracker.SelectionPredicate<K> mSelectionPredicate;
    private final boolean mSingleSelect;
    private final StorageStrategy<K> mStorage;

    public DefaultSelectionTracker(@NonNull String str, @NonNull ItemKeyProvider itemKeyProvider, @NonNull SelectionTracker.SelectionPredicate selectionPredicate, @NonNull StorageStrategy<K> storageStrategy) {
        boolean z = false;
        Preconditions.checkArgument(str != null);
        Preconditions.checkArgument(!str.trim().isEmpty());
        Preconditions.checkArgument(itemKeyProvider != null);
        Preconditions.checkArgument(selectionPredicate != null);
        Preconditions.checkArgument(storageStrategy != null ? true : z);
        this.mSelectionId = str;
        this.mKeyProvider = itemKeyProvider;
        this.mSelectionPredicate = selectionPredicate;
        this.mStorage = storageStrategy;
        this.mRangeCallbacks = new RangeCallbacks();
        this.mSingleSelect = !selectionPredicate.canSelectMultiple();
        this.mAdapterObserver = new AdapterObserver(this);
    }

    public void addObserver(@NonNull SelectionTracker.SelectionObserver selectionObserver) {
        Preconditions.checkArgument(selectionObserver != null);
        this.mObservers.add(selectionObserver);
    }

    public boolean hasSelection() {
        return !this.mSelection.isEmpty();
    }

    public Selection getSelection() {
        return this.mSelection;
    }

    public void copySelection(@NonNull MutableSelection mutableSelection) {
        mutableSelection.copyFrom(this.mSelection);
    }

    public boolean isSelected(@Nullable K k) {
        return this.mSelection.contains(k);
    }

    /* access modifiers changed from: protected */
    public void restoreSelection(@NonNull Selection selection) {
        Preconditions.checkArgument(selection != null);
        setItemsSelectedQuietly(selection.mSelection, true);
        notifySelectionRestored();
    }

    public boolean setItemsSelected(@NonNull Iterable<K> iterable, boolean z) {
        boolean itemsSelectedQuietly = setItemsSelectedQuietly(iterable, z);
        notifySelectionChanged();
        return itemsSelectedQuietly;
    }

    private boolean setItemsSelectedQuietly(@NonNull Iterable<K> iterable, boolean z) {
        boolean z2 = false;
        for (K next : iterable) {
            boolean z3 = true;
            if (!z ? !canSetState(next, false) || !this.mSelection.remove(next) : !canSetState(next, true) || !this.mSelection.add(next)) {
                z3 = false;
            }
            if (z3) {
                notifyItemStateChanged(next, z);
            }
            z2 |= z3;
        }
        return z2;
    }

    public boolean clearSelection() {
        if (!hasSelection()) {
            return false;
        }
        clearProvisionalSelection();
        clearPrimarySelection();
        return true;
    }

    private void clearPrimarySelection() {
        if (hasSelection()) {
            notifySelectionCleared(clearSelectionQuietly());
            notifySelectionChanged();
        }
    }

    private Selection clearSelectionQuietly() {
        this.mRange = null;
        MutableSelection mutableSelection = new MutableSelection();
        if (hasSelection()) {
            copySelection(mutableSelection);
            this.mSelection.clear();
        }
        return mutableSelection;
    }

    public boolean select(@NonNull K k) {
        Preconditions.checkArgument(k != null);
        if (this.mSelection.contains(k) || !canSetState(k, true)) {
            return false;
        }
        if (this.mSingleSelect && hasSelection()) {
            notifySelectionCleared(clearSelectionQuietly());
        }
        this.mSelection.add(k);
        notifyItemStateChanged(k, true);
        notifySelectionChanged();
        return true;
    }

    public boolean deselect(@NonNull K k) {
        Preconditions.checkArgument(k != null);
        if (!this.mSelection.contains(k) || !canSetState(k, false)) {
            return false;
        }
        this.mSelection.remove(k);
        notifyItemStateChanged(k, false);
        notifySelectionChanged();
        if (this.mSelection.isEmpty() && isRangeActive()) {
            endRange();
        }
        return true;
    }

    public void startRange(int i) {
        if (this.mSelection.contains(this.mKeyProvider.getKey(i)) || select(this.mKeyProvider.getKey(i))) {
            anchorRange(i);
        }
    }

    public void extendRange(int i) {
        extendRange(i, 0);
    }

    public void endRange() {
        this.mRange = null;
        clearProvisionalSelection();
    }

    public void anchorRange(int i) {
        Preconditions.checkArgument(i != -1);
        Preconditions.checkArgument(this.mSelection.contains(this.mKeyProvider.getKey(i)));
        this.mRange = new Range(i, this.mRangeCallbacks);
    }

    public void extendProvisionalRange(int i) {
        if (!this.mSingleSelect) {
            Preconditions.checkState(isRangeActive(), "Range start point not set.");
            extendRange(i, 1);
        }
    }

    private void extendRange(int i, int i2) {
        Preconditions.checkState(isRangeActive(), "Range start point not set.");
        this.mRange.extendRange(i, i2);
        notifySelectionChanged();
    }

    public void setProvisionalSelection(@NonNull Set<K> set) {
        if (!this.mSingleSelect) {
            for (Map.Entry next : this.mSelection.setProvisionalSelection(set).entrySet()) {
                notifyItemStateChanged(next.getKey(), ((Boolean) next.getValue()).booleanValue());
            }
            notifySelectionChanged();
        }
    }

    public void mergeProvisionalSelection() {
        this.mSelection.mergeProvisionalSelection();
        notifySelectionChanged();
    }

    public void clearProvisionalSelection() {
        for (K notifyItemStateChanged : this.mSelection.mProvisionalSelection) {
            notifyItemStateChanged(notifyItemStateChanged, false);
        }
        this.mSelection.clearProvisionalSelection();
    }

    public boolean isRangeActive() {
        return this.mRange != null;
    }

    private boolean canSetState(@NonNull K k, boolean z) {
        return this.mSelectionPredicate.canSetStateForKey(k, z);
    }

    /* access modifiers changed from: package-private */
    public RecyclerView.AdapterDataObserver getAdapterDataObserver() {
        return this.mAdapterObserver;
    }

    /* access modifiers changed from: package-private */
    public void onDataSetChanged() {
        this.mSelection.clearProvisionalSelection();
        notifySelectionRefresh();
        Iterator<K> it = this.mSelection.iterator();
        ArrayList<Object> arrayList = null;
        while (it.hasNext()) {
            K next = it.next();
            if (!canSetState(next, true)) {
                if (arrayList == null) {
                    arrayList = new ArrayList<>();
                }
                arrayList.add(next);
            } else {
                for (int size = this.mObservers.size() - 1; size >= 0; size--) {
                    this.mObservers.get(size).onItemStateChanged(next, true);
                }
            }
        }
        if (arrayList != null) {
            for (Object deselect : arrayList) {
                deselect(deselect);
            }
        }
        notifySelectionChanged();
    }

    private void notifyItemStateChanged(@NonNull K k, boolean z) {
        Preconditions.checkArgument(k != null);
        for (int size = this.mObservers.size() - 1; size >= 0; size--) {
            this.mObservers.get(size).onItemStateChanged(k, z);
        }
    }

    private void notifySelectionCleared(@NonNull Selection<K> selection) {
        for (K notifyItemStateChanged : selection.mSelection) {
            notifyItemStateChanged(notifyItemStateChanged, false);
        }
        for (K notifyItemStateChanged2 : selection.mProvisionalSelection) {
            notifyItemStateChanged(notifyItemStateChanged2, false);
        }
    }

    private void notifySelectionChanged() {
        for (int size = this.mObservers.size() - 1; size >= 0; size--) {
            this.mObservers.get(size).onSelectionChanged();
        }
    }

    private void notifySelectionRestored() {
        for (int size = this.mObservers.size() - 1; size >= 0; size--) {
            this.mObservers.get(size).onSelectionRestored();
        }
    }

    private void notifySelectionRefresh() {
        for (int size = this.mObservers.size() - 1; size >= 0; size--) {
            this.mObservers.get(size).onSelectionRefresh();
        }
    }

    private void updateForRange(int i, int i2, boolean z, int i3) {
        switch (i3) {
            case 0:
                updateForRegularRange(i, i2, z);
                return;
            case 1:
                updateForProvisionalRange(i, i2, z);
                return;
            default:
                throw new IllegalArgumentException("Invalid range type: " + i3);
        }
    }

    /* access modifiers changed from: package-private */
    public void updateForRegularRange(int i, int i2, boolean z) {
        Preconditions.checkArgument(i2 >= i);
        while (i <= i2) {
            K key = this.mKeyProvider.getKey(i);
            if (key != null) {
                if (z) {
                    select(key);
                } else {
                    deselect(key);
                }
            }
            i++;
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x003b  */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x003e A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void updateForProvisionalRange(int r5, int r6, boolean r7) {
        /*
            r4 = this;
            r0 = 0
            r1 = 1
            if (r6 < r5) goto L_0x0006
            r2 = 1
            goto L_0x0007
        L_0x0006:
            r2 = 0
        L_0x0007:
            androidx.core.util.Preconditions.checkArgument(r2)
        L_0x000a:
            if (r5 > r6) goto L_0x0041
            androidx.recyclerview.selection.ItemKeyProvider<K> r2 = r4.mKeyProvider
            java.lang.Object r2 = r2.getKey(r5)
            if (r2 != 0) goto L_0x0015
            goto L_0x003e
        L_0x0015:
            if (r7 == 0) goto L_0x0031
            boolean r3 = r4.canSetState(r2, r1)
            if (r3 == 0) goto L_0x002f
            androidx.recyclerview.selection.Selection<K> r3 = r4.mSelection
            java.util.Set<K> r3 = r3.mSelection
            boolean r3 = r3.contains(r2)
            if (r3 != 0) goto L_0x002f
            androidx.recyclerview.selection.Selection<K> r3 = r4.mSelection
            java.util.Set<K> r3 = r3.mProvisionalSelection
            r3.add(r2)
            goto L_0x0038
        L_0x002f:
            r3 = 0
            goto L_0x0039
        L_0x0031:
            androidx.recyclerview.selection.Selection<K> r3 = r4.mSelection
            java.util.Set<K> r3 = r3.mProvisionalSelection
            r3.remove(r2)
        L_0x0038:
            r3 = 1
        L_0x0039:
            if (r3 == 0) goto L_0x003e
            r4.notifyItemStateChanged(r2, r7)
        L_0x003e:
            int r5 = r5 + 1
            goto L_0x000a
        L_0x0041:
            r4.notifySelectionChanged()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.recyclerview.selection.DefaultSelectionTracker.updateForProvisionalRange(int, int, boolean):void");
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public String getInstanceStateKey() {
        return "androidx.recyclerview.selection:" + this.mSelectionId;
    }

    public final void onSaveInstanceState(@NonNull Bundle bundle) {
        if (!this.mSelection.isEmpty()) {
            bundle.putBundle(getInstanceStateKey(), this.mStorage.asBundle(this.mSelection));
        }
    }

    public final void onRestoreInstanceState(@Nullable Bundle bundle) {
        Bundle bundle2;
        Selection<K> asSelection;
        if (bundle != null && (bundle2 = bundle.getBundle(getInstanceStateKey())) != null && (asSelection = this.mStorage.asSelection(bundle2)) != null && !asSelection.isEmpty()) {
            restoreSelection(asSelection);
        }
    }

    private final class RangeCallbacks extends Range.Callbacks {
        RangeCallbacks() {
        }

        /* access modifiers changed from: package-private */
        public void updateForRange(int i, int i2, boolean z, int i3) {
            switch (i3) {
                case 0:
                    DefaultSelectionTracker.this.updateForRegularRange(i, i2, z);
                    return;
                case 1:
                    DefaultSelectionTracker.this.updateForProvisionalRange(i, i2, z);
                    return;
                default:
                    throw new IllegalArgumentException("Invalid range type: " + i3);
            }
        }
    }

    private static final class AdapterObserver extends RecyclerView.AdapterDataObserver {
        private final DefaultSelectionTracker<?> mSelectionTracker;

        AdapterObserver(@NonNull DefaultSelectionTracker<?> defaultSelectionTracker) {
            Preconditions.checkArgument(defaultSelectionTracker != null);
            this.mSelectionTracker = defaultSelectionTracker;
        }

        public void onChanged() {
            this.mSelectionTracker.onDataSetChanged();
        }

        public void onItemRangeChanged(int i, int i2, @Nullable Object obj) {
            if (!SelectionTracker.SELECTION_CHANGED_MARKER.equals(obj)) {
                this.mSelectionTracker.onDataSetChanged();
            }
        }

        public void onItemRangeInserted(int i, int i2) {
            this.mSelectionTracker.endRange();
        }

        public void onItemRangeRemoved(int i, int i2) {
            this.mSelectionTracker.endRange();
        }

        public void onItemRangeMoved(int i, int i2, int i3) {
            this.mSelectionTracker.endRange();
        }
    }
}
