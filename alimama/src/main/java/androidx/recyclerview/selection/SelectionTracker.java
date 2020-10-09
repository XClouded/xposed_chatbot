package androidx.recyclerview.selection;

import android.content.Context;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Preconditions;
import androidx.recyclerview.selection.BandPredicate;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Set;

public abstract class SelectionTracker<K> {
    public static final String SELECTION_CHANGED_MARKER = "Selection-Changed";

    public static abstract class SelectionObserver<K> {
        public void onItemStateChanged(@NonNull K k, boolean z) {
        }

        public void onSelectionChanged() {
        }

        public void onSelectionRefresh() {
        }

        public void onSelectionRestored() {
        }
    }

    public static abstract class SelectionPredicate<K> {
        public abstract boolean canSelectMultiple();

        public abstract boolean canSetStateAtPosition(int i, boolean z);

        public abstract boolean canSetStateForKey(@NonNull K k, boolean z);
    }

    public abstract void addObserver(SelectionObserver selectionObserver);

    /* access modifiers changed from: package-private */
    public abstract void anchorRange(int i);

    /* access modifiers changed from: package-private */
    public abstract void clearProvisionalSelection();

    public abstract boolean clearSelection();

    public abstract void copySelection(@NonNull MutableSelection<K> mutableSelection);

    public abstract boolean deselect(@NonNull K k);

    /* access modifiers changed from: package-private */
    public abstract void endRange();

    /* access modifiers changed from: package-private */
    public abstract void extendProvisionalRange(int i);

    /* access modifiers changed from: package-private */
    public abstract void extendRange(int i);

    /* access modifiers changed from: package-private */
    public abstract RecyclerView.AdapterDataObserver getAdapterDataObserver();

    public abstract Selection<K> getSelection();

    public abstract boolean hasSelection();

    /* access modifiers changed from: package-private */
    public abstract boolean isRangeActive();

    public abstract boolean isSelected(@Nullable K k);

    /* access modifiers changed from: package-private */
    public abstract void mergeProvisionalSelection();

    public abstract void onRestoreInstanceState(@Nullable Bundle bundle);

    public abstract void onSaveInstanceState(@NonNull Bundle bundle);

    /* access modifiers changed from: protected */
    public abstract void restoreSelection(@NonNull Selection<K> selection);

    public abstract boolean select(@NonNull K k);

    public abstract boolean setItemsSelected(@NonNull Iterable<K> iterable, boolean z);

    /* access modifiers changed from: package-private */
    public abstract void setProvisionalSelection(@NonNull Set<K> set);

    /* access modifiers changed from: package-private */
    public abstract void startRange(int i);

    public static final class Builder<K> {
        private final RecyclerView.Adapter<?> mAdapter;
        private int mBandOverlayId = R.drawable.selection_band_overlay;
        private BandPredicate mBandPredicate;
        private final Context mContext;
        private ItemDetailsLookup<K> mDetailsLookup;
        private FocusDelegate<K> mFocusDelegate = FocusDelegate.dummy();
        private int[] mGestureToolTypes = {1, 0};
        private ItemKeyProvider<K> mKeyProvider;
        private OperationMonitor mMonitor = new OperationMonitor();
        private OnContextClickListener mOnContextClickListener;
        private OnDragInitiatedListener mOnDragInitiatedListener;
        private OnItemActivatedListener<K> mOnItemActivatedListener;
        private int[] mPointerToolTypes;
        final RecyclerView mRecyclerView;
        private final String mSelectionId;
        SelectionPredicate<K> mSelectionPredicate = SelectionPredicates.createSelectAnything();
        private final StorageStrategy<K> mStorage;

        public Builder(@NonNull String str, @NonNull RecyclerView recyclerView, @NonNull ItemKeyProvider<K> itemKeyProvider, @NonNull ItemDetailsLookup<K> itemDetailsLookup, @NonNull StorageStrategy<K> storageStrategy) {
            boolean z = true;
            this.mPointerToolTypes = new int[]{3};
            Preconditions.checkArgument(str != null);
            Preconditions.checkArgument(!str.trim().isEmpty());
            Preconditions.checkArgument(recyclerView != null);
            this.mSelectionId = str;
            this.mRecyclerView = recyclerView;
            this.mContext = recyclerView.getContext();
            this.mAdapter = recyclerView.getAdapter();
            Preconditions.checkArgument(this.mAdapter != null);
            Preconditions.checkArgument(itemKeyProvider != null);
            Preconditions.checkArgument(itemDetailsLookup != null);
            Preconditions.checkArgument(storageStrategy == null ? false : z);
            this.mDetailsLookup = itemDetailsLookup;
            this.mKeyProvider = itemKeyProvider;
            this.mStorage = storageStrategy;
            this.mBandPredicate = new BandPredicate.NonDraggableArea(this.mRecyclerView, itemDetailsLookup);
        }

        public Builder<K> withSelectionPredicate(@NonNull SelectionPredicate<K> selectionPredicate) {
            Preconditions.checkArgument(selectionPredicate != null);
            this.mSelectionPredicate = selectionPredicate;
            return this;
        }

        public Builder<K> withOperationMonitor(@NonNull OperationMonitor operationMonitor) {
            Preconditions.checkArgument(operationMonitor != null);
            this.mMonitor = operationMonitor;
            return this;
        }

        public Builder<K> withFocusDelegate(@NonNull FocusDelegate<K> focusDelegate) {
            Preconditions.checkArgument(focusDelegate != null);
            this.mFocusDelegate = focusDelegate;
            return this;
        }

        public Builder<K> withOnItemActivatedListener(@NonNull OnItemActivatedListener<K> onItemActivatedListener) {
            Preconditions.checkArgument(onItemActivatedListener != null);
            this.mOnItemActivatedListener = onItemActivatedListener;
            return this;
        }

        public Builder<K> withOnContextClickListener(@NonNull OnContextClickListener onContextClickListener) {
            Preconditions.checkArgument(onContextClickListener != null);
            this.mOnContextClickListener = onContextClickListener;
            return this;
        }

        public Builder<K> withOnDragInitiatedListener(@NonNull OnDragInitiatedListener onDragInitiatedListener) {
            Preconditions.checkArgument(onDragInitiatedListener != null);
            this.mOnDragInitiatedListener = onDragInitiatedListener;
            return this;
        }

        public Builder<K> withGestureTooltypes(int... iArr) {
            this.mGestureToolTypes = iArr;
            return this;
        }

        public Builder<K> withBandOverlay(@DrawableRes int i) {
            this.mBandOverlayId = i;
            return this;
        }

        public Builder<K> withBandPredicate(@NonNull BandPredicate bandPredicate) {
            Preconditions.checkArgument(bandPredicate != null);
            this.mBandPredicate = bandPredicate;
            return this;
        }

        public Builder<K> withPointerTooltypes(int... iArr) {
            this.mPointerToolTypes = iArr;
            return this;
        }

        public SelectionTracker<K> build() {
            DefaultSelectionTracker defaultSelectionTracker = new DefaultSelectionTracker(this.mSelectionId, this.mKeyProvider, this.mSelectionPredicate, this.mStorage);
            EventBridge.install(this.mAdapter, defaultSelectionTracker, this.mKeyProvider);
            ViewAutoScroller viewAutoScroller = new ViewAutoScroller(ViewAutoScroller.createScrollHost(this.mRecyclerView));
            GestureRouter gestureRouter = new GestureRouter();
            TouchEventRouter touchEventRouter = new TouchEventRouter(new GestureDetector(this.mContext, gestureRouter));
            final GestureSelectionHelper create = GestureSelectionHelper.create(defaultSelectionTracker, this.mDetailsLookup, this.mRecyclerView, viewAutoScroller, this.mMonitor);
            this.mRecyclerView.addOnItemTouchListener(touchEventRouter);
            this.mOnDragInitiatedListener = this.mOnDragInitiatedListener != null ? this.mOnDragInitiatedListener : new OnDragInitiatedListener() {
                public boolean onDragInitiated(@NonNull MotionEvent motionEvent) {
                    return false;
                }
            };
            this.mOnItemActivatedListener = this.mOnItemActivatedListener != null ? this.mOnItemActivatedListener : new OnItemActivatedListener<K>() {
                public boolean onItemActivated(@NonNull ItemDetailsLookup.ItemDetails<K> itemDetails, @NonNull MotionEvent motionEvent) {
                    return false;
                }
            };
            this.mOnContextClickListener = this.mOnContextClickListener != null ? this.mOnContextClickListener : new OnContextClickListener() {
                public boolean onContextClick(@NonNull MotionEvent motionEvent) {
                    return false;
                }
            };
            ViewAutoScroller viewAutoScroller2 = viewAutoScroller;
            TouchInputHandler touchInputHandler = r1;
            TouchInputHandler touchInputHandler2 = new TouchInputHandler(defaultSelectionTracker, this.mKeyProvider, this.mDetailsLookup, this.mSelectionPredicate, new Runnable() {
                public void run() {
                    if (Builder.this.mSelectionPredicate.canSelectMultiple()) {
                        create.start();
                    }
                }
            }, this.mOnDragInitiatedListener, this.mOnItemActivatedListener, this.mFocusDelegate, new Runnable() {
                public void run() {
                    Builder.this.mRecyclerView.performHapticFeedback(0);
                }
            });
            for (int i : this.mGestureToolTypes) {
                gestureRouter.register(i, touchInputHandler);
                touchEventRouter.register(i, create);
            }
            MouseInputHandler mouseInputHandler = new MouseInputHandler(defaultSelectionTracker, this.mKeyProvider, this.mDetailsLookup, this.mOnContextClickListener, this.mOnItemActivatedListener, this.mFocusDelegate);
            for (int register : this.mPointerToolTypes) {
                gestureRouter.register(register, mouseInputHandler);
            }
            BandSelectionHelper bandSelectionHelper = null;
            if (this.mKeyProvider.hasAccess(0) && this.mSelectionPredicate.canSelectMultiple()) {
                bandSelectionHelper = BandSelectionHelper.create(this.mRecyclerView, viewAutoScroller2, this.mBandOverlayId, this.mKeyProvider, defaultSelectionTracker, this.mSelectionPredicate, this.mBandPredicate, this.mFocusDelegate, this.mMonitor);
            }
            PointerDragEventInterceptor pointerDragEventInterceptor = new PointerDragEventInterceptor(this.mDetailsLookup, this.mOnDragInitiatedListener, bandSelectionHelper);
            for (int register2 : this.mPointerToolTypes) {
                touchEventRouter.register(register2, pointerDragEventInterceptor);
            }
            return defaultSelectionTracker;
        }
    }
}
