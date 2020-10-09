package androidx.recyclerview.selection;

import android.util.SparseArray;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import java.util.HashMap;
import java.util.Map;

public final class StableIdKeyProvider extends ItemKeyProvider<Long> {
    private final Map<Long, Integer> mKeyToPosition = new HashMap();
    private final SparseArray<Long> mPositionToKey = new SparseArray<>();
    private final RecyclerView mRecyclerView;

    public StableIdKeyProvider(@NonNull RecyclerView recyclerView) {
        super(1);
        this.mRecyclerView = recyclerView;
        this.mRecyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            public void onChildViewAttachedToWindow(View view) {
                StableIdKeyProvider.this.onAttached(view);
            }

            public void onChildViewDetachedFromWindow(View view) {
                StableIdKeyProvider.this.onDetached(view);
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void onAttached(@NonNull View view) {
        RecyclerView.ViewHolder findContainingViewHolder = this.mRecyclerView.findContainingViewHolder(view);
        int adapterPosition = findContainingViewHolder.getAdapterPosition();
        long itemId = findContainingViewHolder.getItemId();
        if (adapterPosition != -1 && itemId != -1) {
            this.mPositionToKey.put(adapterPosition, Long.valueOf(itemId));
            this.mKeyToPosition.put(Long.valueOf(itemId), Integer.valueOf(adapterPosition));
        }
    }

    /* access modifiers changed from: package-private */
    public void onDetached(@NonNull View view) {
        RecyclerView.ViewHolder findContainingViewHolder = this.mRecyclerView.findContainingViewHolder(view);
        int adapterPosition = findContainingViewHolder.getAdapterPosition();
        long itemId = findContainingViewHolder.getItemId();
        if (adapterPosition != -1 && itemId != -1) {
            this.mPositionToKey.delete(adapterPosition);
            this.mKeyToPosition.remove(Long.valueOf(itemId));
        }
    }

    @Nullable
    public Long getKey(int i) {
        return this.mPositionToKey.get(i, (Object) null);
    }

    public int getPosition(@NonNull Long l) {
        if (this.mKeyToPosition.containsKey(l)) {
            return this.mKeyToPosition.get(l).intValue();
        }
        return -1;
    }
}
