package com.alimama.moon.features.home;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.alimama.moon.features.home.item.Constants;
import com.alimama.unionwl.utils.LocalDisplay;
import com.alimamaunion.common.listpage.CommonItemInfo;
import com.alimamaunion.common.listpage.CommonRecyclerAdapter;

public class HomeItemDecoration extends RecyclerView.ItemDecoration {
    private CommonRecyclerAdapter mAdapter;
    private int mMargin12Dp = LocalDisplay.dp2px(12.0f);
    private int mMargin3Dp = LocalDisplay.dp2px(3.0f);
    private int mMargin6Dp = LocalDisplay.dp2px(6.0f);
    private int mMargin9Dp = LocalDisplay.dp2px(9.0f);

    public void onDraw(@NonNull Canvas canvas, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.State state) {
    }

    public HomeItemDecoration(CommonRecyclerAdapter commonRecyclerAdapter) {
        this.mAdapter = commonRecyclerAdapter;
    }

    public void getItemOffsets(@NonNull Rect rect, @NonNull View view, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.State state) {
        int i;
        int i2;
        super.getItemOffsets(rect, view, recyclerView, state);
        int childAdapterPosition = recyclerView.getChildAdapterPosition(view);
        int i3 = this.mMargin9Dp;
        String findViewTypeStr = CommonItemInfo.findViewTypeStr(this.mAdapter.getItemViewType(childAdapterPosition));
        if (!TextUtils.isEmpty(findViewTypeStr) && TextUtils.equals(findViewTypeStr, "sales_card")) {
            i3 = this.mMargin9Dp;
        }
        if (TextUtils.isEmpty(findViewTypeStr) || !TextUtils.equals(findViewTypeStr, "common_item")) {
            i2 = 0;
            i = 0;
        } else {
            int i4 = this.mMargin9Dp;
            i = this.mMargin9Dp;
            i2 = i4;
            i3 = 0;
        }
        if (!TextUtils.isEmpty(findViewTypeStr) && (TextUtils.equals(findViewTypeStr, Constants.BANNER_TYPE_NAME) || TextUtils.equals(findViewTypeStr, Constants.FLASH_REBATE_TYPE_NAME) || TextUtils.equals(findViewTypeStr, Constants.CIRCLE_POINT_TYPE_NAME) || TextUtils.equals(findViewTypeStr, Constants.SALE_BLOCK_TYPE_NAME))) {
            i3 = 0;
        }
        int spanIndex = ((GridLayoutManager.LayoutParams) view.getLayoutParams()).getSpanIndex();
        if (!TextUtils.isEmpty(findViewTypeStr) && TextUtils.equals(findViewTypeStr, "recommend_item")) {
            i3 = this.mMargin6Dp;
            if (spanIndex == 0) {
                i2 = this.mMargin9Dp;
                i = this.mMargin3Dp;
            } else {
                i = this.mMargin9Dp;
                i2 = this.mMargin3Dp;
            }
        }
        if (!TextUtils.isEmpty(findViewTypeStr) && TextUtils.equals(findViewTypeStr, Constants.APPRENTICE_TYPE_NAME)) {
            i3 = this.mMargin6Dp;
            i2 = this.mMargin12Dp;
            i = this.mMargin12Dp;
        }
        if (!TextUtils.isEmpty(findViewTypeStr) && TextUtils.equals(findViewTypeStr, Constants.APPRENTICE_TITLE_TYPE_NAME)) {
            i3 = this.mMargin12Dp;
            i2 = this.mMargin12Dp;
            i = this.mMargin12Dp;
        }
        rect.set(i2, i3, i, 0);
    }
}
