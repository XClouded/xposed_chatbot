package com.alimama.moon.features.search;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;
import com.alimama.moon.R;

public class SortCategoryPanel extends FrameLayout implements View.OnClickListener {
    private View mBackground;
    private PopupWindow.OnDismissListener mDismissListener;
    /* access modifiers changed from: private */
    public PopupWindow mPopupWindow;
    /* access modifiers changed from: private */
    public RecyclerView mRecyclerView;

    public SortCategoryPanel(@NonNull Context context) {
        this(context, (AttributeSet) null);
    }

    public SortCategoryPanel(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public SortCategoryPanel(@NonNull Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initViews(context);
    }

    private void initViews(@NonNull Context context) {
        inflate(context, R.layout.merge_sort_category_panel, this);
        this.mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.search_sort_category_divider);
        if (drawable != null) {
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, 1);
            dividerItemDecoration.setDrawable(drawable);
            this.mRecyclerView.addItemDecoration(dividerItemDecoration);
        }
        this.mBackground = findViewById(R.id.bg_view);
        this.mBackground.setOnClickListener(this);
        setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        this.mPopupWindow = new PopupWindow(this, -1, -2);
        this.mPopupWindow.setTouchable(true);
        this.mPopupWindow.setOutsideTouchable(false);
        this.mPopupWindow.setAnimationStyle(16973824);
    }

    public void setOnDismissListener(PopupWindow.OnDismissListener onDismissListener) {
        this.mDismissListener = onDismissListener;
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        this.mRecyclerView.setLayoutManager(layoutManager);
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        this.mRecyclerView.setAdapter(adapter);
    }

    public void setHeight(int i) {
        this.mPopupWindow.setHeight(i);
    }

    public boolean isExpanding() {
        return this.mPopupWindow.isShowing();
    }

    public void toggle(View view) {
        if (this.mPopupWindow.isShowing()) {
            dismiss();
        } else {
            showAsDropdown(view);
        }
    }

    public void onClick(View view) {
        if (view.getId() == R.id.bg_view) {
            dismiss();
        }
    }

    private void showAsDropdown(View view) {
        if (this.mRecyclerView.getHeight() > 0) {
            slideIn();
        } else {
            this.mRecyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                    if (view.getWidth() > 0 && view.getHeight() > 0) {
                        SortCategoryPanel.this.mRecyclerView.removeOnLayoutChangeListener(this);
                        SortCategoryPanel.this.slideIn();
                    }
                }
            });
        }
        this.mBackground.setAlpha(1.0f);
        this.mPopupWindow.showAsDropDown(view);
    }

    /* access modifiers changed from: private */
    public void slideIn() {
        this.mRecyclerView.setY((float) (-this.mRecyclerView.getHeight()));
        this.mRecyclerView.animate().translationY(0.0f).start();
    }

    public void dismiss() {
        this.mDismissListener.onDismiss();
        this.mRecyclerView.animate().translationY((float) (-this.mRecyclerView.getHeight())).withEndAction(new Runnable() {
            public void run() {
                SortCategoryPanel.this.mPopupWindow.dismiss();
            }
        }).start();
        this.mBackground.animate().alpha(0.0f).start();
    }
}
