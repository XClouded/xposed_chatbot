package com.alimama.moon.features.home.viewholder;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.alimama.moon.R;
import com.alimama.moon.features.home.item.FlashSaleBlockItem;
import com.alimama.moon.features.home.item.HomeFlashSaleBlock;
import com.alimama.moon.features.home.view.HomeCountDownView;
import com.alimama.moon.features.home.view.HomeFlashSaleItemView;
import com.alimama.moon.usertrack.UTHelper;
import com.alimama.moon.utils.SpmProcessor;
import com.alimama.moon.web.WebPageIntentGenerator;
import com.alimama.union.app.pagerouter.MoonComponentManager;
import com.alimama.unionwl.utils.LocalDisplay;
import java.util.List;

public class HomeFlashSaleViewHolder implements HomeBaseViewHolder<HomeFlashSaleBlock> {
    private HomeCountDownView mCountDown;
    private ImageView mHelpImg;
    private LinearLayout mItemContainer;
    private TextView mMoreTv;
    private RelativeLayout mTitleContainRl;
    private TextView mTopBarTitleTv;
    private View mView;
    int marginLeftRight;

    public View createView(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        View inflate = layoutInflater.inflate(R.layout.home_flash_sale_layout, (ViewGroup) null);
        this.mView = inflate;
        this.mTitleContainRl = (RelativeLayout) inflate.findViewById(R.id.container);
        this.mCountDown = (HomeCountDownView) inflate.findViewById(R.id.countdown);
        this.mItemContainer = (LinearLayout) inflate.findViewById(R.id.item_container);
        this.mTopBarTitleTv = (TextView) inflate.findViewById(R.id.first_title_tv);
        this.mMoreTv = (TextView) inflate.findViewById(R.id.more_tv);
        this.mHelpImg = (ImageView) inflate.findViewById(R.id.flash_sale_help_img);
        this.marginLeftRight = LocalDisplay.dp2px(12.0f);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -2);
        layoutParams.leftMargin = this.marginLeftRight;
        layoutParams.rightMargin = this.marginLeftRight;
        inflate.setLayoutParams(layoutParams);
        return inflate;
    }

    public void onBindViewHolder(int i, final HomeFlashSaleBlock homeFlashSaleBlock) {
        if (homeFlashSaleBlock.itemList.size() == 0) {
            updateViewLayout(0);
            this.mTitleContainRl.setVisibility(8);
            this.mItemContainer.setVisibility(8);
            return;
        }
        updateViewLayout(9);
        this.mTitleContainRl.setVisibility(0);
        this.mItemContainer.setVisibility(0);
        this.mTopBarTitleTv.setText(homeFlashSaleBlock.name);
        this.mHelpImg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                MoonComponentManager.getInstance().getPageRouter().gotoPage(homeFlashSaleBlock.ruleUrl);
            }
        });
        this.mMoreTv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                UTHelper.HomePage.clickFlashSaleMore();
                MoonComponentManager.getInstance().getPageRouter().gotoPage(homeFlashSaleBlock.moreSrc);
            }
        });
        this.mItemContainer.removeAllViews();
        List<FlashSaleBlockItem> list = homeFlashSaleBlock.itemList;
        int size = list.size();
        for (int i2 = 0; i2 < size; i2++) {
            final FlashSaleBlockItem flashSaleBlockItem = list.get(i2);
            HomeFlashSaleItemView homeFlashSaleItemView = new HomeFlashSaleItemView(this.mItemContainer.getContext(), i2);
            homeFlashSaleItemView.render(flashSaleBlockItem);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
            homeFlashSaleItemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    UTHelper.HomePage.clickFlashSaleItem(flashSaleBlockItem.itemId, flashSaleBlockItem.lensId);
                    if (flashSaleBlockItem.vegasField != null && flashSaleBlockItem.vegasField.itemDrawLimitNum > 0) {
                        HomeFlashSaleViewHolder.this.goToItemlandingPage(flashSaleBlockItem.srcUrl);
                    }
                }
            });
            this.mItemContainer.addView(homeFlashSaleItemView, layoutParams);
        }
    }

    private void updateViewLayout(int i) {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -2);
        layoutParams.leftMargin = this.marginLeftRight;
        layoutParams.rightMargin = this.marginLeftRight;
        layoutParams.topMargin = LocalDisplay.dp2px((float) i);
        this.mView.setLayoutParams(layoutParams);
    }

    /* access modifiers changed from: private */
    public void goToItemlandingPage(String str) {
        String destUrl = SpmProcessor.getDestUrl(str, UTHelper.HomePage.SPM_CNT_BACKUP, false);
        if (!TextUtils.isEmpty(destUrl)) {
            MoonComponentManager.getInstance().getPageRouter().gotoPage(WebPageIntentGenerator.getRebateItemLandingPage(destUrl));
        }
    }
}
