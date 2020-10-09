package androidx.recyclerview.selection;

import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.core.util.Preconditions;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class BandPredicate {
    public abstract boolean canInitiate(MotionEvent motionEvent);

    static boolean hasSupportedLayoutManager(@NonNull RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        return (layoutManager instanceof GridLayoutManager) || (layoutManager instanceof LinearLayoutManager);
    }

    public static final class EmptyArea extends BandPredicate {
        private final RecyclerView mRecyclerView;

        public EmptyArea(@NonNull RecyclerView recyclerView) {
            Preconditions.checkArgument(recyclerView != null);
            this.mRecyclerView = recyclerView;
        }

        public boolean canInitiate(@NonNull MotionEvent motionEvent) {
            if (!hasSupportedLayoutManager(this.mRecyclerView) || this.mRecyclerView.hasPendingAdapterUpdates()) {
                return false;
            }
            View findChildViewUnder = this.mRecyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
            if ((findChildViewUnder != null ? this.mRecyclerView.getChildAdapterPosition(findChildViewUnder) : -1) == -1) {
                return true;
            }
            return false;
        }
    }

    public static final class NonDraggableArea extends BandPredicate {
        private final ItemDetailsLookup mDetailsLookup;
        private final RecyclerView mRecyclerView;

        public NonDraggableArea(@NonNull RecyclerView recyclerView, @NonNull ItemDetailsLookup itemDetailsLookup) {
            boolean z = false;
            Preconditions.checkArgument(recyclerView != null);
            Preconditions.checkArgument(itemDetailsLookup != null ? true : z);
            this.mRecyclerView = recyclerView;
            this.mDetailsLookup = itemDetailsLookup;
        }

        public boolean canInitiate(@NonNull MotionEvent motionEvent) {
            if (!hasSupportedLayoutManager(this.mRecyclerView) || this.mRecyclerView.hasPendingAdapterUpdates()) {
                return false;
            }
            ItemDetailsLookup.ItemDetails itemDetails = this.mDetailsLookup.getItemDetails(motionEvent);
            if (itemDetails == null || !itemDetails.inDragRegion(motionEvent)) {
                return true;
            }
            return false;
        }
    }
}
