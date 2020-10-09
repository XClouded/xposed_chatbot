package androidx.recyclerview.selection;

import androidx.annotation.NonNull;

public final class MutableSelection<K> extends Selection<K> {
    public boolean add(@NonNull K k) {
        return super.add(k);
    }

    public boolean remove(@NonNull K k) {
        return super.remove(k);
    }

    public void copyFrom(@NonNull Selection<K> selection) {
        super.copyFrom(selection);
    }

    public void clear() {
        super.clear();
    }
}
