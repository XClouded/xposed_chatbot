package com.alimama.moon.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BackToTopView extends AppCompatImageView {
    private static final int MAX_SMOOTH_SCROLL_ITEM_DIFF = 10;
    private static final String TAG = "BackToTopView";
    /* access modifiers changed from: private */
    @Nullable
    public RecyclerView mBoundRecyclerView;
    /* access modifiers changed from: private */
    @Nullable
    public LinearLayoutManager mLayoutManager;
    private final RecyclerView.OnScrollListener mPageScrollListener;

    public BackToTopView(Context context) {
        this(context, (AttributeSet) null);
    }

    public BackToTopView(Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public BackToTopView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mPageScrollListener = new RecyclerView.OnScrollListener() {
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int i) {
                super.onScrollStateChanged(recyclerView, i);
                if (BackToTopView.this.mBoundRecyclerView != null && (BackToTopView.this.mLayoutManager instanceof LinearLayoutManager)) {
                    if (i != 0 && i != 2) {
                        return;
                    }
                    if (BackToTopView.this.mLayoutManager.findFirstVisibleItemPosition() > 0) {
                        BackToTopView.this.setVisibility(0);
                    } else {
                        BackToTopView.this.setVisibility(8);
                    }
                }
            }

            public void onScrolled(@NonNull RecyclerView recyclerView, int i, int i2) {
                super.onScrolled(recyclerView, i, i2);
            }
        };
        initViews(context);
    }

    private void initViews(@NonNull Context context) {
        setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (BackToTopView.this.mBoundRecyclerView != null && BackToTopView.this.mLayoutManager != null) {
                    if (BackToTopView.this.mLayoutManager.findFirstVisibleItemPosition() > 10) {
                        BackToTopView.this.mBoundRecyclerView.scrollToPosition(10);
                        BackToTopView.this.mBoundRecyclerView.post(new Runnable() {
                            public void run() {
                                BackToTopView.this.mBoundRecyclerView.smoothScrollToPosition(0);
                            }
                        });
                        return;
                    }
                    BackToTopView.this.mBoundRecyclerView.smoothScrollToPosition(0);
                }
            }
        });
    }

    public void bindRecyclerView(@NonNull RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (!(layoutManager instanceof LinearLayoutManager)) {
            Log.e(TAG, "only LinearLayoutManager is supported for now");
            return;
        }
        this.mBoundRecyclerView = recyclerView;
        this.mLayoutManager = (LinearLayoutManager) layoutManager;
        this.mBoundRecyclerView.addOnScrollListener(this.mPageScrollListener);
    }
}
