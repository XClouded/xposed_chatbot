package com.alimama.moon.features.home.viewholder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.alimama.moon.R;
import com.alimama.moon.features.home.item.HomeSaleBlockItem;
import com.alimama.moon.features.home.theme.HomeThemeDataManager;
import com.alimama.moon.usertrack.UTHelper;
import com.alimama.union.app.pagerouter.MoonComponentManager;
import com.alimama.unionwl.base.etaodrawee.EtaoDraweeView;
import com.alimama.unionwl.utils.LocalDisplay;

public class HomeSaleBlockViewHolder implements HomeBaseViewHolder<HomeSaleBlockItem> {
    private static int CLIP_CORNER = LocalDisplay.dp2px(10.0f);
    private int height;
    private EtaoDraweeView mCommodityBg;
    private EtaoDraweeView mSaleBlockBg;
    private int marginLeftRight;

    public View createView(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        View inflate = layoutInflater.inflate(R.layout.home_sale_block_layout, viewGroup, false);
        this.mCommodityBg = (EtaoDraweeView) inflate.findViewById(R.id.commodity_bg);
        this.mSaleBlockBg = (EtaoDraweeView) inflate.findViewById(R.id.sale_block_bg);
        this.mCommodityBg.setRoundedCorners((float) CLIP_CORNER, (float) CLIP_CORNER, (float) CLIP_CORNER, (float) CLIP_CORNER);
        this.marginLeftRight = LocalDisplay.dp2px(12.0f);
        this.height = ((LocalDisplay.SCREEN_WIDTH_PIXELS - (this.marginLeftRight * 2)) * 156) / 702;
        return inflate;
    }

    public void onBindViewHolder(int i, final HomeSaleBlockItem homeSaleBlockItem) {
        updateSaleBlockBg();
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, this.height);
        layoutParams.leftMargin = this.marginLeftRight;
        layoutParams.rightMargin = this.marginLeftRight;
        layoutParams.topMargin = LocalDisplay.dp2px(9.0f);
        this.mCommodityBg.setLayoutParams(layoutParams);
        if (homeSaleBlockItem.mFirstSaleBlockItem != null) {
            this.mCommodityBg.setAnyImageUrl(homeSaleBlockItem.mFirstSaleBlockItem.img);
            this.mCommodityBg.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    UTHelper.HomePage.clickSaleBlock();
                    MoonComponentManager.getInstance().getPageRouter().gotoPage(homeSaleBlockItem.mFirstSaleBlockItem.src);
                }
            });
        }
    }

    private void updateSaleBlockBg() {
        try {
            if (HomeThemeDataManager.getInstance().isSwitchConfigCenterTheme()) {
                this.mSaleBlockBg.setVisibility(0);
                this.mSaleBlockBg.setAnyImageUrl(HomeThemeDataManager.getInstance().themeDataItem.saleBlockImg);
                return;
            }
            this.mSaleBlockBg.setVisibility(8);
        } catch (Exception unused) {
        }
    }
}
