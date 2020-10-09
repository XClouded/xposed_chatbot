package com.alimama.moon.features.home.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.alimama.moon.R;
import com.alimama.moon.features.home.item.Constants;
import com.alimama.moon.features.home.item.FlashSaleBlockItem;
import com.alimama.moon.usertrack.UTHelper;
import com.alimama.moon.utils.SpmProcessor;
import com.alimama.union.app.infrastructure.socialShare.social.ShareUtils;
import com.alimama.union.app.logger.BusinessMonitorLogger;
import com.alimama.unionwl.base.etaodrawee.EtaoDraweeView;

public class HomeFlashSaleItemView extends RelativeLayout {
    private static final String TAG = "HomeFlashSaleItemView";
    /* access modifiers changed from: private */
    public Context mContext;
    private TextView mDiscountInfo;
    private TextView mEarnMoney;
    private TextView mGoodTitle;
    private EtaoDraweeView mHasSnapUpImg;
    private EtaoDraweeView mImg;
    public int mIndex;
    /* access modifiers changed from: private */
    public FlashSaleBlockItem mItem;
    private CommonFlashSaleProgressBar mProgressBar;
    private TextView mRemainNum;
    private TextView mSaleMoney;
    private View mTopView;

    public HomeFlashSaleItemView(Context context, int i) {
        super(context);
        this.mIndex = i;
        initView(context);
    }

    public HomeFlashSaleItemView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView(context);
    }

    public HomeFlashSaleItemView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView(context);
    }

    private void initView(Context context) {
        this.mContext = context;
        this.mTopView = inflate(context, R.layout.home_flash_sale_item_view, this);
        this.mImg = (EtaoDraweeView) this.mTopView.findViewById(R.id.common_limit_rob_item_img);
        this.mHasSnapUpImg = (EtaoDraweeView) this.mTopView.findViewById(R.id.has_snap_up_img);
        this.mGoodTitle = (TextView) this.mTopView.findViewById(R.id.goods_title);
        this.mDiscountInfo = (TextView) this.mTopView.findViewById(R.id.discount_info);
        this.mRemainNum = (TextView) this.mTopView.findViewById(R.id.remain_number);
        this.mSaleMoney = (TextView) this.mTopView.findViewById(R.id.sale_money);
        this.mEarnMoney = (TextView) this.mTopView.findViewById(R.id.earn_money);
        this.mEarnMoney.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                UTHelper.HomePage.clickFlashSaleItemShare(HomeFlashSaleItemView.this.mItem.itemId, HomeFlashSaleItemView.this.mItem.lensId);
                if (HomeFlashSaleItemView.this.mItem != null && HomeFlashSaleItemView.this.mItem.vegasField != null && HomeFlashSaleItemView.this.mItem.vegasField.itemDrawLimitNum != 0) {
                    String destUrl = SpmProcessor.getDestUrl(HomeFlashSaleItemView.this.mItem.srcUrl, UTHelper.HomePage.SPM_CLICK_FLASH_REBATE_EARN_MORE, false);
                    if (!TextUtils.isEmpty(destUrl)) {
                        ShareUtils.showShare(HomeFlashSaleItemView.this.mContext, destUrl);
                    }
                }
            }
        });
        this.mProgressBar = (CommonFlashSaleProgressBar) this.mTopView.findViewById(R.id.common_limit_rob_item_processbar);
    }

    public void render(FlashSaleBlockItem flashSaleBlockItem) {
        if (flashSaleBlockItem != null) {
            this.mItem = flashSaleBlockItem;
            this.mImg.setAnyImageUrl(flashSaleBlockItem.picUrl);
            this.mGoodTitle.setText(flashSaleBlockItem.itemName);
            if (flashSaleBlockItem.vegasField != null) {
                int i = flashSaleBlockItem.vegasField.itemDrawTotalNum;
                String str = flashSaleBlockItem.vegasField.totalRightsFace;
                if (i == 0 || TextUtils.isEmpty(str)) {
                    this.mDiscountInfo.setVisibility(4);
                } else {
                    this.mDiscountInfo.setVisibility(0);
                    this.mDiscountInfo.setText(String.format(getResources().getString(R.string.flash_sale_discount_info), new Object[]{Integer.valueOf(i), str}));
                }
                this.mRemainNum.setText(String.format(getResources().getString(R.string.flash_sale_item_draw_limit_num), new Object[]{Integer.valueOf(flashSaleBlockItem.vegasField.itemDrawLimitNum)}));
                this.mSaleMoney.setText(String.format(getResources().getString(R.string.flash_sale_price_after_all_rights), new Object[]{flashSaleBlockItem.vegasField.priceAfterAllRights}));
                if (TextUtils.isEmpty(flashSaleBlockItem.vegasField.priceAfterAllRights)) {
                    BusinessMonitorLogger.PriceParamDetect.show(TAG, Constants.FLASH_REBATE_TYPE_NAME, "flashSaleBlockItem.vegasField.priceAfterAllRights");
                }
                if (flashSaleBlockItem.vegasField.itemDrawLimitNum > 0) {
                    this.mEarnMoney.setText(String.format(getResources().getString(R.string.flash_sale_subTkCommissionAmount), new Object[]{flashSaleBlockItem.subTkCommissionAmount}));
                    this.mEarnMoney.setBackgroundDrawable(getResources().getDrawable(R.drawable.home_flash_sale_item_earn_sel_bg));
                    this.mEarnMoney.setTextColor(getResources().getColor(R.color.home_flash_sale_earn_money_text_color));
                    if (TextUtils.isEmpty(flashSaleBlockItem.subTkCommissionAmount)) {
                        BusinessMonitorLogger.PriceParamDetect.show(TAG, Constants.FLASH_REBATE_TYPE_NAME, "flashSaleBlockItem.subTkCommissionAmount");
                    }
                    this.mHasSnapUpImg.setVisibility(8);
                } else {
                    this.mEarnMoney.setText(R.string.flash_sale_snap_up);
                    this.mEarnMoney.setTextColor(getResources().getColor(R.color.home_flash_sale_item_remain_number_text_color));
                    this.mEarnMoney.setBackgroundDrawable(getResources().getDrawable(R.drawable.home_flash_sale_item_snap_up_earn_bg));
                    this.mHasSnapUpImg.setVisibility(0);
                    this.mHasSnapUpImg.setBackground(getResources().getDrawable(R.drawable.has_snap_up));
                }
                if (flashSaleBlockItem.vegasField.itemDrawTotalNum != 0) {
                    int i2 = ((flashSaleBlockItem.vegasField.itemDrawTotalNum - flashSaleBlockItem.vegasField.itemDrawLimitNum) * 100) / flashSaleBlockItem.vegasField.itemDrawTotalNum;
                    int i3 = 10;
                    if (i2 >= 10) {
                        i3 = i2;
                    }
                    this.mProgressBar.setCurProgress(i3);
                    return;
                }
                return;
            }
            this.mHasSnapUpImg.setVisibility(8);
        }
    }
}
