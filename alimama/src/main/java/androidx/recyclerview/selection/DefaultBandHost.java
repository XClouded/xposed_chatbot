package androidx.recyclerview.selection;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.util.Preconditions;
import androidx.recyclerview.selection.GridModel;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

final class DefaultBandHost<K> extends GridModel.GridHost<K> {
    private static final Rect NILL_RECT = new Rect(0, 0, 0, 0);
    private final Drawable mBand;
    private final ItemKeyProvider<K> mKeyProvider;
    private final RecyclerView mRecyclerView;
    private final SelectionTracker.SelectionPredicate<K> mSelectionPredicate;

    DefaultBandHost(@NonNull RecyclerView recyclerView, @DrawableRes int i, @NonNull ItemKeyProvider<K> itemKeyProvider, @NonNull SelectionTracker.SelectionPredicate<K> selectionPredicate) {
        boolean z = false;
        Preconditions.checkArgument(recyclerView != null);
        this.mRecyclerView = recyclerView;
        this.mBand = this.mRecyclerView.getContext().getResources().getDrawable(i);
        Preconditions.checkArgument(this.mBand != null);
        Preconditions.checkArgument(itemKeyProvider != null);
        Preconditions.checkArgument(selectionPredicate != null ? true : z);
        this.mKeyProvider = itemKeyProvider;
        this.mSelectionPredicate = selectionPredicate;
        this.mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            public void onDrawOver(Canvas canvas, RecyclerView recyclerView, RecyclerView.State state) {
                DefaultBandHost.this.onDrawBand(canvas);
            }
        });
    }

    /* access modifiers changed from: package-private */
    public GridModel<K> createGridModel() {
        return new GridModel<>(this, this.mKeyProvider, this.mSelectionPredicate);
    }

    /* access modifiers changed from: package-private */
    public int getAdapterPositionAt(int i) {
        return this.mRecyclerView.getChildAdapterPosition(this.mRecyclerView.getChildAt(i));
    }

    /* access modifiers changed from: package-private */
    public void addOnScrollListener(@NonNull RecyclerView.OnScrollListener onScrollListener) {
        this.mRecyclerView.addOnScrollListener(onScrollListener);
    }

    /* access modifiers changed from: package-private */
    public void removeOnScrollListener(@NonNull RecyclerView.OnScrollListener onScrollListener) {
        this.mRecyclerView.removeOnScrollListener(onScrollListener);
    }

    /* access modifiers changed from: package-private */
    public Point createAbsolutePoint(@NonNull Point point) {
        return new Point(point.x + this.mRecyclerView.computeHorizontalScrollOffset(), point.y + this.mRecyclerView.computeVerticalScrollOffset());
    }

    /* access modifiers changed from: package-private */
    public Rect getAbsoluteRectForChildViewAt(int i) {
        View childAt = this.mRecyclerView.getChildAt(i);
        Rect rect = new Rect();
        childAt.getHitRect(rect);
        rect.left += this.mRecyclerView.computeHorizontalScrollOffset();
        rect.right += this.mRecyclerView.computeHorizontalScrollOffset();
        rect.top += this.mRecyclerView.computeVerticalScrollOffset();
        rect.bottom += this.mRecyclerView.computeVerticalScrollOffset();
        return rect;
    }

    /* access modifiers changed from: package-private */
    public int getVisibleChildCount() {
        return this.mRecyclerView.getChildCount();
    }

    /* access modifiers changed from: package-private */
    public int getColumnCount() {
        RecyclerView.LayoutManager layoutManager = this.mRecyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            return ((GridLayoutManager) layoutManager).getSpanCount();
        }
        return 1;
    }

    /* access modifiers changed from: package-private */
    public void showBand(@NonNull Rect rect) {
        this.mBand.setBounds(rect);
        this.mRecyclerView.invalidate();
    }

    /* access modifiers changed from: package-private */
    public void hideBand() {
        this.mBand.setBounds(NILL_RECT);
        this.mRecyclerView.invalidate();
    }

    /* access modifiers changed from: package-private */
    public void onDrawBand(@NonNull Canvas canvas) {
        this.mBand.draw(canvas);
    }

    /* access modifiers changed from: package-private */
    public boolean hasView(int i) {
        return this.mRecyclerView.findViewHolderForAdapterPosition(i) != null;
    }
}
