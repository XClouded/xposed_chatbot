package com.alimama.moon.features.home.viewholder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.alibaba.wireless.security.SecExceptionCode;
import com.alimama.moon.R;
import com.alimama.moon.features.home.item.HomeResourcePlaceItem;
import com.alimama.moon.usertrack.UTHelper;
import com.alimama.union.app.pagerouter.MoonComponentManager;
import com.alimama.unionwl.base.etaodrawee.EtaoDraweeView;
import com.alimama.unionwl.utils.LocalDisplay;
import java.util.ArrayList;
import java.util.List;

public class HomeResourcePlaceViewHolder implements HomeBaseViewHolder<HomeResourcePlaceItem> {
    private static int CLIP_CORNER = LocalDisplay.dp2px(10.0f);
    private final int COL_SIZE = 3;
    private LinearLayout mContainer;
    private int mItemHeight;
    private LinearLayout.LayoutParams mItemParams;
    private int mItemWidth;
    private LinearLayout.LayoutParams mLayoutParams;
    private List<ResourceItemViewRender> mRenderList;
    int marginLeftRight;

    public View createView(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        View inflate = layoutInflater.inflate(R.layout.home_resource_place, (ViewGroup) null);
        this.mContainer = (LinearLayout) inflate.findViewById(R.id.home_resource_place_ll);
        this.marginLeftRight = LocalDisplay.dp2px(12.0f);
        this.mItemWidth = (LocalDisplay.SCREEN_WIDTH_PIXELS - (this.marginLeftRight * 2)) / 3;
        this.mItemHeight = (this.mItemWidth * SecExceptionCode.SEC_ERROR_STA_KEY_NOT_EXISTED) / 234;
        this.mItemParams = new LinearLayout.LayoutParams(this.mItemWidth, this.mItemHeight);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -2);
        layoutParams.leftMargin = this.marginLeftRight;
        layoutParams.rightMargin = this.marginLeftRight;
        inflate.setLayoutParams(layoutParams);
        return inflate;
    }

    public void onBindViewHolder(int i, HomeResourcePlaceItem homeResourcePlaceItem) {
        if (homeResourcePlaceItem != null && homeResourcePlaceItem.resourceItemList != null) {
            int filterResourceItemList = filterResourceItemList(homeResourcePlaceItem.resourceItemList);
            if (filterResourceItemList < 3) {
                this.mContainer.setVisibility(8);
                return;
            }
            this.mContainer.setVisibility(0);
            this.mLayoutParams = new LinearLayout.LayoutParams(-1, -2);
            this.mRenderList = new ArrayList(filterResourceItemList);
            LayoutInflater from = LayoutInflater.from(this.mContainer.getContext());
            this.mContainer.removeAllViews();
            LinearLayout linearLayout = null;
            for (int i2 = 0; i2 < filterResourceItemList; i2++) {
                ResourceItemViewRender resourceItemViewRender = new ResourceItemViewRender();
                View create = resourceItemViewRender.create(from, this.mItemHeight);
                if (i2 % 3 == 0) {
                    linearLayout = new LinearLayout(this.mContainer.getContext());
                    linearLayout.setOrientation(0);
                    this.mContainer.addView(linearLayout, this.mLayoutParams);
                }
                linearLayout.addView(create, this.mItemParams);
                this.mRenderList.add(resourceItemViewRender);
            }
            for (int i3 = 0; i3 < this.mRenderList.size(); i3++) {
                this.mRenderList.get(i3).render(homeResourcePlaceItem.resourceItemList.get(i3));
            }
            clipResourcePlaceCorner(this.mRenderList);
        }
    }

    public void clipResourcePlaceCorner(List<ResourceItemViewRender> list) {
        int size = list.size();
        if (size <= 3) {
            list.get(0).mImgView.setRoundedCorners((float) CLIP_CORNER, 0.0f, 0.0f, (float) CLIP_CORNER);
            list.get(size - 1).mImgView.setRoundedCorners(0.0f, (float) CLIP_CORNER, (float) CLIP_CORNER, 0.0f);
            return;
        }
        list.get(0).mImgView.setRoundedCorners((float) CLIP_CORNER, 0.0f, 0.0f, 0.0f);
        list.get(2).mImgView.setRoundedCorners(0.0f, (float) CLIP_CORNER, 0.0f, 0.0f);
        list.get(size - 3).mImgView.setRoundedCorners(0.0f, 0.0f, 0.0f, (float) CLIP_CORNER);
        list.get(size - 1).mImgView.setRoundedCorners(0.0f, 0.0f, (float) CLIP_CORNER, 0.0f);
    }

    private int filterResourceItemList(List<HomeResourcePlaceItem.HomeResourceItem> list) {
        int size = list.size();
        if (size <= 3 || size % 3 == 0) {
            return size;
        }
        while (size % 3 != 0) {
            list.remove(size - 1);
            size = list.size();
        }
        return size;
    }

    private static class ResourceItemViewRender {
        EtaoDraweeView mImgView;
        View mView;

        private ResourceItemViewRender() {
        }

        public View create(LayoutInflater layoutInflater, int i) {
            View inflate = layoutInflater.inflate(R.layout.home_resource_place_item_view, (ViewGroup) null);
            this.mView = inflate;
            this.mImgView = (EtaoDraweeView) inflate.findViewById(R.id.home_resource_place_item_view_img);
            this.mImgView.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
            return inflate;
        }

        public void render(final HomeResourcePlaceItem.HomeResourceItem homeResourceItem) {
            this.mImgView.setAnyImageUrl(homeResourceItem.mImgUrl);
            this.mView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    UTHelper.HomePage.clickResourcePlaceItem();
                    MoonComponentManager.getInstance().getPageRouter().gotoPage(homeResourceItem.mJumpUrl);
                }
            });
        }
    }
}
