package com.taobao.android.dinamicx.view;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import java.util.ArrayList;

public class DXNativePageIndicator extends LinearLayout {
    private final int IMAGE_VIEW_TYPE = 1;
    private int currentIndex = -1;
    private int itemMargin;
    private int itemRoundDiameter;
    final RecycledViewPool recycledPool = new RecycledViewPool();
    private GradientDrawable selectedDrawable;
    private GradientDrawable unselectedDrawable;

    public DXNativePageIndicator(Context context) {
        super(context);
        init();
    }

    public DXNativePageIndicator(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public DXNativePageIndicator(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    private void init() {
        setOrientation(0);
        setGravity(17);
    }

    public void setSelectedDrawable(int i) {
        if (this.selectedDrawable == null) {
            GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setShape(1);
            gradientDrawable.setColor(i);
            gradientDrawable.setSize(this.itemRoundDiameter, this.itemRoundDiameter);
            gradientDrawable.setCornerRadius((float) (this.itemRoundDiameter / 2));
            this.selectedDrawable = gradientDrawable;
            return;
        }
        this.selectedDrawable.setColor(i);
    }

    public void setUnselectedDrawable(int i) {
        if (this.unselectedDrawable == null) {
            GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setShape(1);
            gradientDrawable.setColor(i);
            gradientDrawable.setSize(this.itemRoundDiameter, this.itemRoundDiameter);
            gradientDrawable.setCornerRadius((float) (this.itemRoundDiameter / 2));
            this.unselectedDrawable = gradientDrawable;
            return;
        }
        this.unselectedDrawable.setColor(i);
    }

    public void setSelectedView(int i) {
        if (this.currentIndex != i && i < getChildCount()) {
            if (this.currentIndex != -1) {
                ((ImageView) getChildAt(this.currentIndex)).setImageDrawable(this.unselectedDrawable);
            }
            ((ImageView) getChildAt(i)).setImageDrawable(this.selectedDrawable);
            this.currentIndex = i;
        }
    }

    public void addChildViews(int i, int i2) {
        ImageView imageView;
        if (i < 0) {
            i = 0;
        }
        if (getChildCount() > i) {
            for (int childCount = getChildCount() - 1; childCount >= i; childCount--) {
                recycleView(childCount);
            }
        }
        for (int i3 = 0; i3 < i; i3++) {
            ImageView imageView2 = null;
            if (i3 < getChildCount()) {
                imageView2 = (ImageView) getChildAt(i3);
            }
            if (imageView == null) {
                imageView = (ImageView) this.recycledPool.getRecycledView(1);
                if (imageView == null) {
                    imageView = new ImageView(getContext());
                }
                addView(imageView);
            }
            if (i3 == i2) {
                imageView.setImageDrawable(this.selectedDrawable);
                this.currentIndex = i2;
            } else {
                imageView.setImageDrawable(this.unselectedDrawable);
            }
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(this.itemRoundDiameter, this.itemRoundDiameter);
            if (i3 != i - 1) {
                layoutParams.rightMargin = this.itemMargin;
            } else {
                layoutParams.rightMargin = 0;
            }
            imageView.setLayoutParams(layoutParams);
        }
    }

    public int getCurrentIndex() {
        return this.currentIndex;
    }

    public void setItemRoundDiameter(int i) {
        this.itemRoundDiameter = i;
    }

    public int getItemRoundDiameter() {
        return this.itemRoundDiameter;
    }

    public void setItemMargin(int i) {
        this.itemMargin = i;
    }

    public int getItemMargin() {
        return this.itemMargin;
    }

    public GradientDrawable getSelectedDrawable() {
        return this.selectedDrawable;
    }

    public GradientDrawable getUnselectedDrawable() {
        return this.unselectedDrawable;
    }

    private void recycleView(int i) {
        if (i < super.getChildCount()) {
            View childAt = super.getChildAt(i);
            super.removeViewAt(i);
            this.recycledPool.putRecycledView(1, childAt);
        }
    }

    public static class RecycledViewPool {
        private static final int DEFAULT_MAX_SCRAP = 10;
        private SparseIntArray mMaxScrap = new SparseIntArray();
        private SparseArray<ArrayList<View>> mScrap = new SparseArray<>();

        public void clear() {
            this.mScrap.clear();
        }

        public void setMaxRecycledViews(int i, int i2) {
            this.mMaxScrap.put(i, i2);
            ArrayList arrayList = this.mScrap.get(i);
            if (arrayList != null) {
                while (arrayList.size() > i2) {
                    arrayList.remove(arrayList.size() - 1);
                }
            }
        }

        public View getRecycledView(int i) {
            ArrayList arrayList = this.mScrap.get(i);
            if (arrayList == null || arrayList.isEmpty()) {
                return null;
            }
            int size = arrayList.size() - 1;
            View view = (View) arrayList.get(size);
            arrayList.remove(size);
            return view;
        }

        /* access modifiers changed from: package-private */
        public int size() {
            int i = 0;
            for (int i2 = 0; i2 < this.mScrap.size(); i2++) {
                ArrayList valueAt = this.mScrap.valueAt(i2);
                if (valueAt != null) {
                    i += valueAt.size();
                }
            }
            return i;
        }

        public void putRecycledView(int i, View view) {
            ArrayList<View> scrapHeapForType = getScrapHeapForType(i);
            if (this.mMaxScrap.get(i) > scrapHeapForType.size()) {
                scrapHeapForType.add(view);
            }
        }

        private ArrayList<View> getScrapHeapForType(int i) {
            ArrayList<View> arrayList = this.mScrap.get(i);
            if (arrayList == null) {
                arrayList = new ArrayList<>();
                this.mScrap.put(i, arrayList);
                if (this.mMaxScrap.indexOfKey(i) < 0) {
                    this.mMaxScrap.put(i, 10);
                }
            }
            return arrayList;
        }
    }
}
