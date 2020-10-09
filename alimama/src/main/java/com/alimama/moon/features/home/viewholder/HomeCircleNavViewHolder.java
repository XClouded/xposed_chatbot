package com.alimama.moon.features.home.viewholder;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import com.alibaba.wireless.security.SecExceptionCode;
import com.alimama.moon.R;
import com.alimama.moon.features.home.item.HomeCircleNavItem;
import com.alimama.moon.features.home.theme.HomeThemeDataManager;
import com.alimama.moon.usertrack.UTHelper;
import com.alimama.union.app.pagerouter.MoonComponentManager;
import com.alimama.unionwl.base.etaodrawee.EtaoDraweeView;
import com.alimama.unionwl.utils.LocalDisplay;
import java.util.ArrayList;
import java.util.List;

public class HomeCircleNavViewHolder implements HomeBaseViewHolder<HomeCircleNavItem> {
    private final int COL_SIZE = 5;
    /* access modifiers changed from: private */
    public EtaoDraweeView mCircleNavBg;
    /* access modifiers changed from: private */
    public LinearLayout mContainer;
    private Context mContext;
    private int mImgHeight;
    private LinearLayout.LayoutParams mItemParams;
    private int mItemWidth;
    private int mLastLen = 0;
    private ViewGroup.LayoutParams mLayoutParams;
    private List<ItemViewRender> mRenderList;

    public View createView(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        View inflate = layoutInflater.inflate(R.layout.home_views_circle_nav, (ViewGroup) null);
        this.mContext = viewGroup.getContext();
        this.mContainer = (LinearLayout) inflate.findViewById(R.id.home_views_circle_nav_container);
        this.mCircleNavBg = (EtaoDraweeView) inflate.findViewById(R.id.home_views_circle_nav_bg_image);
        int dp2px = LocalDisplay.dp2px(12.0f);
        this.mContainer.setPadding(dp2px, LocalDisplay.dp2px(9.0f), dp2px, 0);
        this.mItemWidth = (LocalDisplay.SCREEN_WIDTH_PIXELS - (dp2px * 2)) / 5;
        this.mImgHeight = (this.mItemWidth * 76) / SecExceptionCode.SEC_ERROR_INIT_LOW_VERSION_DATA;
        this.mItemParams = new LinearLayout.LayoutParams(this.mItemWidth, -2);
        return inflate;
    }

    public void onBindViewHolder(int i, HomeCircleNavItem homeCircleNavItem) {
        int filterResourceItemList = filterResourceItemList(homeCircleNavItem.itemList);
        if (filterResourceItemList < 5) {
            this.mContainer.setVisibility(8);
            return;
        }
        int i2 = 0;
        this.mContainer.setVisibility(0);
        if (this.mRenderList == null || this.mLastLen != filterResourceItemList) {
            this.mLastLen = filterResourceItemList;
            this.mLayoutParams = new LinearLayout.LayoutParams(-1, -2);
            this.mRenderList = new ArrayList(filterResourceItemList);
            LayoutInflater from = LayoutInflater.from(this.mContainer.getContext());
            this.mContainer.removeAllViews();
            LinearLayout linearLayout = null;
            for (int i3 = 0; i3 < filterResourceItemList; i3++) {
                ItemViewRender itemViewRender = new ItemViewRender();
                View create = itemViewRender.create(from, this.mImgHeight);
                if (i3 % 5 == 0) {
                    linearLayout = new LinearLayout(this.mContainer.getContext());
                    linearLayout.setOrientation(0);
                    this.mContainer.addView(linearLayout, this.mLayoutParams);
                }
                linearLayout.addView(create, this.mItemParams);
                this.mRenderList.add(itemViewRender);
            }
        }
        while (i2 < this.mRenderList.size() && i2 < filterResourceItemList) {
            this.mRenderList.get(i2).render(homeCircleNavItem.itemList.get(i2), i2);
            i2++;
        }
        updateCircleNavBg();
    }

    private void updateCircleNavBg() {
        try {
            int i = 0;
            if (HomeThemeDataManager.getInstance().isSwitchConfigCenterTheme()) {
                this.mCircleNavBg.setVisibility(0);
                this.mCircleNavBg.setAnyImageUrl(HomeThemeDataManager.getInstance().themeDataItem.circleNavImg);
                while (i < this.mRenderList.size()) {
                    this.mRenderList.get(i).mNameTextView.setTextColor(Color.parseColor(HomeThemeDataManager.getInstance().themeDataItem.circleNavTextColor));
                    i++;
                }
                this.mContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    public void onGlobalLayout() {
                        HomeCircleNavViewHolder.this.mCircleNavBg.setLayoutParams(new FrameLayout.LayoutParams(-1, HomeCircleNavViewHolder.this.mContainer.getMeasuredHeight() + LocalDisplay.dp2px(9.0f)));
                        HomeCircleNavViewHolder.this.mContainer.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                });
                return;
            }
            this.mCircleNavBg.setVisibility(8);
            while (i < this.mRenderList.size()) {
                this.mRenderList.get(i).mNameTextView.setTextColor(ContextCompat.getColor(this.mContext, R.color.common_black));
                i++;
            }
        } catch (Exception unused) {
        }
    }

    private int filterResourceItemList(List<HomeCircleNavItem.Item> list) {
        int size = list.size();
        if (size <= 5 || size % 5 == 0) {
            return size;
        }
        while (size % 5 != 0) {
            list.remove(size - 1);
            size = list.size();
        }
        return size;
    }

    public static class ItemViewRender {
        EtaoDraweeView mLogoImageView;
        TextView mNameTextView;
        View mView;

        public View create(LayoutInflater layoutInflater, int i) {
            View inflate = layoutInflater.inflate(R.layout.home_views_circle_nav_item, (ViewGroup) null);
            this.mView = inflate;
            this.mLogoImageView = (EtaoDraweeView) inflate.findViewById(R.id.home_views_circle_nav_item_image);
            this.mNameTextView = (TextView) inflate.findViewById(R.id.home_views_circle_nav_item_text_view);
            this.mLogoImageView.setLayoutParams(new LinearLayout.LayoutParams(i, i));
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
            layoutParams.topMargin = LocalDisplay.dp2px(7.0f);
            this.mNameTextView.setLayoutParams(layoutParams);
            return inflate;
        }

        /* access modifiers changed from: package-private */
        public void render(final HomeCircleNavItem.Item item, final int i) {
            this.mNameTextView.setText(item.name);
            this.mLogoImageView.setAnyImageUrl(item.img);
            this.mView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    UTHelper.HomePage.clickCircleItem(i);
                    MoonComponentManager.getInstance().getPageRouter().gotoPage(item.src);
                }
            });
        }
    }
}
