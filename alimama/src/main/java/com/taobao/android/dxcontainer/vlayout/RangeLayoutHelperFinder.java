package com.taobao.android.dxcontainer.vlayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class RangeLayoutHelperFinder extends LayoutHelperFinder {
    @NonNull
    private Comparator<LayoutHelperItem> mLayoutHelperItemComparator = new Comparator<LayoutHelperItem>() {
        public int compare(LayoutHelperItem layoutHelperItem, LayoutHelperItem layoutHelperItem2) {
            return layoutHelperItem.getStartPosition() - layoutHelperItem2.getStartPosition();
        }
    };
    @NonNull
    private List<LayoutHelperItem> mLayoutHelperItems = new LinkedList();
    @NonNull
    private List<LayoutHelper> mLayoutHelpers = new LinkedList();
    @NonNull
    private List<LayoutHelper> mReverseLayoutHelpers = new LinkedList();
    private LayoutHelperItem[] mSortedLayoutHelpers = null;

    /* access modifiers changed from: protected */
    public List<LayoutHelper> reverse() {
        return this.mReverseLayoutHelpers;
    }

    public void setLayouts(@Nullable List<LayoutHelper> list) {
        this.mLayoutHelpers.clear();
        this.mReverseLayoutHelpers.clear();
        this.mLayoutHelperItems.clear();
        if (list != null) {
            ListIterator<LayoutHelper> listIterator = list.listIterator();
            while (listIterator.hasNext()) {
                LayoutHelper next = listIterator.next();
                this.mLayoutHelpers.add(next);
                this.mLayoutHelperItems.add(new LayoutHelperItem(next));
            }
            while (listIterator.hasPrevious()) {
                this.mReverseLayoutHelpers.add(listIterator.previous());
            }
            this.mSortedLayoutHelpers = (LayoutHelperItem[]) this.mLayoutHelperItems.toArray(new LayoutHelperItem[this.mLayoutHelperItems.size()]);
            Arrays.sort(this.mSortedLayoutHelpers, this.mLayoutHelperItemComparator);
        }
    }

    /* access modifiers changed from: protected */
    @NonNull
    public List<LayoutHelper> getLayoutHelpers() {
        return this.mLayoutHelpers;
    }

    @Nullable
    public LayoutHelper getLayoutHelper(int i) {
        LayoutHelperItem layoutHelperItem;
        if (this.mSortedLayoutHelpers == null || this.mSortedLayoutHelpers.length == 0) {
            return null;
        }
        int i2 = 0;
        int length = this.mSortedLayoutHelpers.length - 1;
        while (true) {
            if (i2 > length) {
                layoutHelperItem = null;
                break;
            }
            int i3 = (i2 + length) / 2;
            layoutHelperItem = this.mSortedLayoutHelpers[i3];
            if (layoutHelperItem.getStartPosition() <= i) {
                if (layoutHelperItem.getEndPosition() >= i) {
                    if (layoutHelperItem.getStartPosition() <= i && layoutHelperItem.getEndPosition() >= i) {
                        break;
                    }
                } else {
                    i2 = i3 + 1;
                }
            } else {
                length = i3 - 1;
            }
        }
        if (layoutHelperItem == null) {
            return null;
        }
        return layoutHelperItem.layoutHelper;
    }

    static class LayoutHelperItem {
        LayoutHelper layoutHelper;

        LayoutHelperItem(LayoutHelper layoutHelper2) {
            this.layoutHelper = layoutHelper2;
        }

        public int getStartPosition() {
            return this.layoutHelper.getRange().getLower().intValue();
        }

        public int getEndPosition() {
            return this.layoutHelper.getRange().getUpper().intValue();
        }
    }
}
