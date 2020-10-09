package com.taobao.uikit.extend.feature.features;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.taobao.uikit.feature.callback.AdapterCallback;
import com.taobao.uikit.feature.features.AbsFeature;
import com.taobao.uikit.feature.view.TImageView;

public class SmoothScrollFeature extends AbsFeature<ListView> implements AdapterCallback, AbsListView.OnScrollListener {
    private int mLastVisibleItem = -1;
    /* access modifiers changed from: private */
    public int mScrollState = 0;

    public void constructor(Context context, AttributeSet attributeSet, int i) {
    }

    @Deprecated
    public void setImageViewTag(ImageView imageView, Object obj) {
    }

    public void setHost(ListView listView) {
        super.setHost(listView);
        listView.setOnScrollListener(this);
    }

    public ListAdapter wrapAdapter(ListAdapter listAdapter) {
        return (listAdapter == null || (listAdapter instanceof SmoothAdapter)) ? listAdapter : new SmoothAdapter(listAdapter);
    }

    public void onScrollStateChanged(AbsListView absListView, int i) {
        this.mScrollState = i;
        if (i == 0) {
            resume(absListView);
        }
    }

    public void onScroll(AbsListView absListView, int i, int i2, int i3) {
        if (this.mLastVisibleItem != i) {
            if (i == 0) {
                View childAt = absListView.getChildAt(0);
                if (childAt == null || childAt.getTop() == 0) {
                    resume(absListView);
                }
            } else if (i3 == i2 + i) {
                resume(absListView);
            }
            this.mLastVisibleItem = i;
        }
    }

    public int getScrollState() {
        return this.mScrollState;
    }

    class SmoothAdapter extends BaseAdapter {
        private ListAdapter mDelegateAdapter;

        public SmoothAdapter(ListAdapter listAdapter) {
            this.mDelegateAdapter = listAdapter;
        }

        public int getCount() {
            return this.mDelegateAdapter.getCount();
        }

        public Object getItem(int i) {
            return this.mDelegateAdapter.getItem(i);
        }

        public long getItemId(int i) {
            return this.mDelegateAdapter.getItemId(i);
        }

        public boolean areAllItemsEnabled() {
            return this.mDelegateAdapter.areAllItemsEnabled();
        }

        public int getItemViewType(int i) {
            return this.mDelegateAdapter.getItemViewType(i);
        }

        public int getViewTypeCount() {
            return this.mDelegateAdapter.getViewTypeCount();
        }

        public boolean hasStableIds() {
            return this.mDelegateAdapter.hasStableIds();
        }

        public boolean isEmpty() {
            return this.mDelegateAdapter.isEmpty();
        }

        public boolean isEnabled(int i) {
            return this.mDelegateAdapter.isEnabled(i);
        }

        public final View getView(int i, View view, ViewGroup viewGroup) {
            View view2 = this.mDelegateAdapter.getView(i, view, viewGroup);
            if (2 != SmoothScrollFeature.this.mScrollState) {
                SmoothScrollFeature.this.resume(view2);
            } else {
                SmoothScrollFeature.this.pause(view2);
            }
            return view2;
        }

        public void notifyDataSetChanged() {
            if (this.mDelegateAdapter instanceof BaseAdapter) {
                ((BaseAdapter) this.mDelegateAdapter).notifyDataSetChanged();
            }
        }

        public void notifyDataSetInvalidated() {
            if (this.mDelegateAdapter instanceof BaseAdapter) {
                ((BaseAdapter) this.mDelegateAdapter).notifyDataSetInvalidated();
            }
        }

        public void registerDataSetObserver(DataSetObserver dataSetObserver) {
            this.mDelegateAdapter.registerDataSetObserver(dataSetObserver);
        }

        public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {
            this.mDelegateAdapter.unregisterDataSetObserver(dataSetObserver);
        }
    }

    private ImageLoadFeature getImageLoadFeature(TImageView tImageView) {
        return (ImageLoadFeature) tImageView.findFeature(ImageLoadFeature.class);
    }

    /* access modifiers changed from: private */
    public void resume(View view) {
        ImageLoadFeature imageLoadFeature;
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            int childCount = viewGroup.getChildCount();
            for (int i = 0; i < childCount; i++) {
                resume(viewGroup.getChildAt(i));
            }
        } else if ((view instanceof TImageView) && (imageLoadFeature = getImageLoadFeature((TImageView) view)) != null) {
            imageLoadFeature.resume();
        }
    }

    /* access modifiers changed from: private */
    public void pause(View view) {
        ImageLoadFeature imageLoadFeature;
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            int childCount = viewGroup.getChildCount();
            for (int i = 0; i < childCount; i++) {
                pause(viewGroup.getChildAt(i));
            }
        } else if ((view instanceof TImageView) && (imageLoadFeature = getImageLoadFeature((TImageView) view)) != null) {
            imageLoadFeature.pause();
        }
    }
}
