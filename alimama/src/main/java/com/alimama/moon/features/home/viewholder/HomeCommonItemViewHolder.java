package com.alimama.moon.features.home.viewholder;

import android.content.res.Resources;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.alimama.moon.R;
import com.alimama.moon.features.home.item.BaseGoodsItem;
import com.alimama.moon.features.home.item.HomeCommonTabItem;
import com.alimama.moon.features.search.FlowTitleLeadingSpan;
import com.alimama.moon.usertrack.UTHelper;
import com.alimama.moon.utils.SpmProcessor;
import com.alimama.moon.utils.StringUtil;
import com.alimama.moon.web.WebPageIntentGenerator;
import com.alimama.union.app.infrastructure.socialShare.social.ShareUtils;
import com.alimama.union.app.logger.BusinessMonitorLogger;
import com.alimama.union.app.pagerouter.MoonComponentManager;
import com.alimama.unionwl.base.etaodrawee.EtaoDraweeView;
import com.alimama.unionwl.utils.LocalDisplay;
import com.alimamaunion.common.listpage.CommonBaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HomeCommonItemViewHolder implements CommonBaseViewHolder<HomeCommonTabItem> {
    /* access modifiers changed from: private */
    public static final Logger logger = LoggerFactory.getLogger((Class<?>) HomeCommonItemViewHolder.class);
    private static final Pattern sIconUrlSizePattern = Pattern.compile("(.+)-(\\d+)-(\\d+)\\.(png|jpeg|jpg|gif)$");
    private TextView mCommissionTextView;
    private TextView mCouponTextView;
    private int mDefaultTitleIconHeight;
    private int mDefaultTitleIconWidth;
    private int mIconTitlePadding;
    private EtaoDraweeView mImageView;
    private final View.OnClickListener mItemClickListener = new View.OnClickListener() {
        public void onClick(View view) {
            if (!(view.getTag(R.id.tabcate_item_itemId) instanceof String) || !(view.getTag(R.id.tabcate_item_url) instanceof String)) {
                HomeCommonItemViewHolder.logger.error("item url or itemId tag not available when share button clicked");
                return;
            }
            UTHelper.HomePage.clickTabCateItem((String) view.getTag(R.id.tabcate_item_itemId));
            String destUrl = SpmProcessor.getDestUrl((String) view.getTag(R.id.tabcate_item_url), UTHelper.HomePage.SPM_CNT_BACKUP, false);
            if (!TextUtils.isEmpty(destUrl)) {
                MoonComponentManager.getInstance().getPageRouter().gotoPage(WebPageIntentGenerator.getItemLandingPage(destUrl));
            }
        }
    };
    private TextView mMonthSellInfoTextView;
    private TextView mPriceInfoPrefixTextView;
    private TextView mPriceTextView;
    private TextView mRecommendReasonTextView;
    private LinearLayout mShareButton;
    private final View.OnClickListener mShareClickListener = new View.OnClickListener() {
        public void onClick(View view) {
            if (!(view.getTag(R.id.tabcate_share_itemId) instanceof String) || !(view.getTag(R.id.tabcate_share_url) instanceof String)) {
                HomeCommonItemViewHolder.logger.error("item url tag or itemId not available when share button clicked");
                return;
            }
            UTHelper.HomePage.clickTabCateShare((String) view.getTag(R.id.tabcate_share_itemId));
            String destUrl = SpmProcessor.getDestUrl((String) view.getTag(R.id.tabcate_share_url), UTHelper.HomePage.SPM_CNT_BACKUP, false);
            if (!TextUtils.isEmpty(destUrl)) {
                ShareUtils.showShare(view.getContext(), destUrl);
            }
        }
    };
    private TextView mStrikeThroughPriceTextView;
    private TextView mTitle;
    private SimpleDraweeView mTitleIcon;
    private View view;

    public View createView(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        this.view = layoutInflater.inflate(R.layout.search_result_card_view, viewGroup, false);
        this.mTitle = (TextView) this.view.findViewById(R.id.search_result_title);
        this.mTitleIcon = (SimpleDraweeView) this.view.findViewById(R.id.search_result_title_icon);
        this.mCouponTextView = (TextView) this.view.findViewById(R.id.search_result_coupon_text);
        this.mImageView = (EtaoDraweeView) this.view.findViewById(R.id.search_result_goods_image);
        this.mPriceInfoPrefixTextView = (TextView) this.view.findViewById(R.id.tv_price_info_prefix);
        this.mPriceTextView = (TextView) this.view.findViewById(R.id.tv_price_info);
        this.mStrikeThroughPriceTextView = (TextView) this.view.findViewById(R.id.tv_strike_through_price);
        this.mCommissionTextView = (TextView) this.view.findViewById(R.id.tv_commission);
        this.mMonthSellInfoTextView = (TextView) this.view.findViewById(R.id.search_result_sold_text);
        this.mRecommendReasonTextView = (TextView) this.view.findViewById(R.id.recommend_reason_tv);
        this.mShareButton = (LinearLayout) this.view.findViewById(R.id.btn_share);
        this.view.setOnClickListener(this.mItemClickListener);
        this.mShareButton.setOnClickListener(this.mShareClickListener);
        Resources resources = viewGroup.getContext().getResources();
        this.mDefaultTitleIconHeight = resources.getDimensionPixelOffset(R.dimen.search_result_title_icon_height);
        this.mDefaultTitleIconWidth = resources.getDimensionPixelOffset(R.dimen.search_result_title_icon_width);
        this.mIconTitlePadding = resources.getDimensionPixelOffset(R.dimen.common_padding_4);
        return this.view;
    }

    public void onBindViewHolder(int i, HomeCommonTabItem homeCommonTabItem) {
        BaseGoodsItem baseGoodsItem = homeCommonTabItem.baseGoodsItem;
        this.view.setTag(R.id.tabcate_item_url, baseGoodsItem.url);
        this.view.setTag(R.id.tabcate_item_itemId, baseGoodsItem.itemId);
        this.mShareButton.setTag(R.id.tabcate_share_url, baseGoodsItem.url);
        this.mShareButton.setTag(R.id.tabcate_share_itemId, baseGoodsItem.itemId);
        if (TextUtils.isEmpty(baseGoodsItem.auctionIconUrl)) {
            this.mTitleIcon.setVisibility(8);
            this.mTitle.setText(Html.fromHtml(baseGoodsItem.itemName));
        } else {
            this.mTitleIcon.setVisibility(0);
            Matcher matcher = sIconUrlSizePattern.matcher(baseGoodsItem.auctionIconUrl);
            int i2 = this.mDefaultTitleIconWidth;
            if (matcher.matches() && matcher.groupCount() > 3) {
                try {
                    int intValue = Integer.valueOf(matcher.group(2)).intValue();
                    int intValue2 = Integer.valueOf(matcher.group(3)).intValue();
                    if (intValue2 != 0) {
                        i2 = (this.mDefaultTitleIconHeight * intValue) / intValue2;
                    }
                } catch (Exception e) {
                    logger.warn("parsing icon url size error", (Throwable) e);
                }
            }
            ViewGroup.LayoutParams layoutParams = this.mTitleIcon.getLayoutParams();
            layoutParams.width = i2;
            this.mTitleIcon.setLayoutParams(layoutParams);
            this.mTitleIcon.setImageURI(EtaoDraweeView.getRemoteUrl(baseGoodsItem.auctionIconUrl, i2, this.mDefaultTitleIconHeight));
            SpannableString spannableString = new SpannableString(Html.fromHtml(baseGoodsItem.itemName));
            spannableString.setSpan(new FlowTitleLeadingSpan(i2 + this.mIconTitlePadding), 0, spannableString.length(), 33);
            this.mTitle.setText(spannableString);
        }
        this.mImageView.setAnyImageUrl(baseGoodsItem.pic);
        if (baseGoodsItem.tkCommissionAmount.length() >= 5) {
            RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(LocalDisplay.dp2px(100.0f), LocalDisplay.dp2px(30.0f));
            layoutParams2.addRule(11);
            layoutParams2.addRule(12);
            layoutParams2.bottomMargin = LocalDisplay.dp2px(12.0f);
            this.mShareButton.setLayoutParams(layoutParams2);
        } else {
            RelativeLayout.LayoutParams layoutParams3 = new RelativeLayout.LayoutParams(LocalDisplay.dp2px(87.0f), LocalDisplay.dp2px(30.0f));
            layoutParams3.addRule(11);
            layoutParams3.addRule(12);
            layoutParams3.bottomMargin = LocalDisplay.dp2px(12.0f);
            this.mShareButton.setLayoutParams(layoutParams3);
        }
        this.mCommissionTextView.setText(StringUtil.getBoldStylePrice(baseGoodsItem.tkCommissionAmount, 10, 14));
        if (TextUtils.isEmpty(baseGoodsItem.tkCommissionAmount)) {
            BusinessMonitorLogger.PriceParamDetect.show("HomeCommonItemViewHolder", "common_item", "item.tkCommissionAmount");
        }
        Resources resources = this.view.getResources();
        this.mMonthSellInfoTextView.setText(resources.getString(R.string.sold_info_with_placeholder, new Object[]{StringUtil.sellOutNumStr(baseGoodsItem.monthSellCount)}));
        if (TextUtils.isEmpty(baseGoodsItem.recommendReasons)) {
            this.mRecommendReasonTextView.setVisibility(8);
            this.mTitle.setMaxLines(2);
        } else {
            this.mRecommendReasonTextView.setVisibility(0);
            this.mRecommendReasonTextView.setText(baseGoodsItem.recommendReasons);
            this.mTitle.setMaxLines(1);
        }
        if (!TextUtils.isEmpty(baseGoodsItem.couponAmount)) {
            this.mCouponTextView.setVisibility(0);
            this.mCouponTextView.setText(resources.getString(R.string.coupon_info, new Object[]{baseGoodsItem.couponAmount}));
            this.mPriceInfoPrefixTextView.setText(R.string.price_after_coupon);
            this.mPriceTextView.setText(StringUtil.getBoldStylePriceWithDecimal(StringUtil.deleteDecimalZeroStr(baseGoodsItem.priceAfterCoupon), 12, 18, 12));
            if (TextUtils.isEmpty(baseGoodsItem.priceAfterCoupon)) {
                BusinessMonitorLogger.PriceParamDetect.show("HomeCommonItemViewHolder", "common_item", "item.priceAfterCoupon");
            }
            this.mStrikeThroughPriceTextView.setVisibility(0);
            this.mStrikeThroughPriceTextView.setText(StringUtil.getStrikeThroughPrice(baseGoodsItem.price, 12, 12));
            return;
        }
        this.mCouponTextView.setVisibility(8);
        this.mPriceInfoPrefixTextView.setVisibility(8);
        this.mPriceTextView.setText(StringUtil.getBoldStylePrice(baseGoodsItem.price, 12, 12));
        if (TextUtils.isEmpty(baseGoodsItem.price)) {
            BusinessMonitorLogger.PriceParamDetect.show("HomeCommonItemViewHolder", "common_item", "item.price");
        }
        this.mStrikeThroughPriceTextView.setVisibility(4);
    }
}
