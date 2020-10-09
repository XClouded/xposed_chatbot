package com.alimama.moon.features.home.viewholder;

import android.content.res.Resources;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.alimama.moon.R;
import com.alimama.moon.features.home.item.BaseGoodsItem;
import com.alimama.moon.features.home.item.HomeRecommendItem;
import com.alimama.moon.features.search.FlowTitleLeadingSpan;
import com.alimama.moon.usertrack.UTHelper;
import com.alimama.moon.utils.SpmProcessor;
import com.alimama.moon.utils.StringUtil;
import com.alimama.moon.web.WebPageIntentGenerator;
import com.alimama.union.app.infrastructure.socialShare.social.ShareUtils;
import com.alimama.union.app.logger.BusinessMonitorLogger;
import com.alimama.union.app.pagerouter.MoonComponentManager;
import com.alimama.unionwl.base.etaodrawee.EtaoDraweeView;
import com.alimamaunion.common.listpage.CommonBaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HomeRecommendViewHolder implements CommonBaseViewHolder<HomeRecommendItem> {
    /* access modifiers changed from: private */
    public static final Logger logger = LoggerFactory.getLogger((Class<?>) HomeRecommendViewHolder.class);
    private static final Pattern sIconUrlSizePattern = Pattern.compile("(.+)-(\\d+)-(\\d+)\\.(png|jpeg|jpg|gif)$");
    private TextView mCommissionTv;
    private LinearLayout mCouponLl;
    private TextView mCouponPrefix;
    private TextView mCouponTv;
    private int mDefaultTitleIconHeight;
    private int mDefaultTitleIconWidth;
    private SimpleDraweeView mIcon;
    private SimpleDraweeView mIcon1;
    private EtaoDraweeView mImg;
    private final View.OnClickListener mItemClickListener = new View.OnClickListener() {
        public void onClick(View view) {
            if (!(view.getTag(R.id.recommend_item_itemId) instanceof String) || !(view.getTag(R.id.recommend_item_url) instanceof String)) {
                HomeRecommendViewHolder.logger.error("item url or itemId tag not available when share button clicked");
                return;
            }
            HashMap hashMap = new HashMap();
            hashMap.put("item", (String) view.getTag(R.id.recommend_item_itemId));
            hashMap.put("lensId", (String) view.getTag(R.id.recommend_item_lensId));
            UTHelper.HomePage.clickGuessItem(hashMap);
            String destUrl = SpmProcessor.getDestUrl((String) view.getTag(R.id.recommend_item_url), UTHelper.HomePage.SPM_CNT_BACKUP, false);
            if (!TextUtils.isEmpty(destUrl)) {
                MoonComponentManager.getInstance().getPageRouter().gotoPage(WebPageIntentGenerator.getItemLandingPage(destUrl));
            }
        }
    };
    private TextView mPrice1Tv;
    private TextView mPriceAfterCoupon;
    private TextView mPriceTv;
    private TextView mRecommendReason;
    private TextView mSellCountTv;
    private TextView mSellcount1Tv;
    private final View.OnClickListener mShareClickListener = new View.OnClickListener() {
        public void onClick(View view) {
            if (!(view.getTag(R.id.recommend_share_itemId) instanceof String) || !(view.getTag(R.id.recommend_share_url) instanceof String)) {
                HomeRecommendViewHolder.logger.error("item url tag not available when share button clicked");
                return;
            }
            HashMap hashMap = new HashMap();
            hashMap.put("item", (String) view.getTag(R.id.recommend_share_itemId));
            hashMap.put("lensId", (String) view.getTag(R.id.recommend_item_lensId));
            UTHelper.HomePage.clickGuessShare(hashMap);
            String destUrl = SpmProcessor.getDestUrl((String) view.getTag(R.id.recommend_share_url), UTHelper.HomePage.SPM_CNT_BACKUP, false);
            if (!TextUtils.isEmpty(destUrl)) {
                ShareUtils.showShare(view.getContext(), destUrl);
            }
        }
    };
    private View mShareContainer;
    private TextView mTitle;
    private TextView mTitle1;
    private Resources resources;
    private View view;

    public View createView(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        this.resources = viewGroup.getResources();
        this.mDefaultTitleIconHeight = this.resources.getDimensionPixelOffset(R.dimen.search_result_title_icon_height);
        this.mDefaultTitleIconWidth = this.resources.getDimensionPixelOffset(R.dimen.search_result_title_icon_width);
        this.view = layoutInflater.inflate(R.layout.fragment_home_recommend_item, viewGroup, false);
        this.mImg = (EtaoDraweeView) this.view.findViewById(R.id.img_item);
        this.mIcon = (SimpleDraweeView) this.view.findViewById(R.id.img_icon);
        this.mTitle = (TextView) this.view.findViewById(R.id.tv_title);
        this.mCouponTv = (TextView) this.view.findViewById(R.id.recommend_item_coupon_text);
        this.mSellCountTv = (TextView) this.view.findViewById(R.id.tv_sell_count);
        this.mPriceTv = (TextView) this.view.findViewById(R.id.tv_price);
        this.mPriceAfterCoupon = (TextView) this.view.findViewById(R.id.tv_price_after_coupon);
        this.mShareContainer = this.view.findViewById(R.id.share_container);
        this.mCommissionTv = (TextView) this.view.findViewById(R.id.tv_share_money);
        this.mRecommendReason = (TextView) this.view.findViewById(R.id.guess_like_recomment_reason_tv);
        this.mCouponLl = (LinearLayout) this.view.findViewById(R.id.coupon_recommend_ll);
        this.mCouponPrefix = (TextView) this.view.findViewById(R.id.tv_coupon_prefix);
        this.view.setOnClickListener(this.mItemClickListener);
        this.view.findViewById(R.id.share_container).setOnClickListener(this.mShareClickListener);
        return this.view;
    }

    public void onBindViewHolder(int i, HomeRecommendItem homeRecommendItem) {
        BaseGoodsItem baseGoodsItem = homeRecommendItem.baseGoodsItem;
        this.view.setTag(R.id.recommend_item_url, baseGoodsItem.url);
        this.view.setTag(R.id.recommend_item_itemId, baseGoodsItem.itemId);
        this.view.setTag(R.id.recommend_item_lensId, baseGoodsItem.lensId);
        this.mShareContainer.setTag(R.id.recommend_share_url, baseGoodsItem.url);
        this.mShareContainer.setTag(R.id.recommend_share_itemId, baseGoodsItem.itemId);
        this.mShareContainer.setTag(R.id.recommend_item_lensId, baseGoodsItem.lensId);
        this.mImg.setAnyImageUrl(baseGoodsItem.pic);
        setCouponStyle(baseGoodsItem);
        this.mCommissionTv.setText(StringUtil.getBoldStylePrice(baseGoodsItem.tkCommissionAmount, 14, 15));
        if (TextUtils.isEmpty(baseGoodsItem.tkCommissionAmount)) {
            BusinessMonitorLogger.PriceParamDetect.show("HomeRecommendViewHolder", "recommend_item", "baseGoodsItem.tkCommissionAmount");
        }
    }

    private void setCouponStyle(BaseGoodsItem baseGoodsItem) {
        if (!TextUtils.isEmpty(baseGoodsItem.couponAmount) || !TextUtils.isEmpty(baseGoodsItem.recommendReasons)) {
            this.mCouponLl.setVisibility(0);
            this.mTitle.setMaxLines(1);
        } else {
            this.mCouponLl.setVisibility(8);
            this.mTitle.setMaxLines(2);
        }
        if (!TextUtils.isEmpty(baseGoodsItem.couponAmount)) {
            this.mCouponTv.setVisibility(0);
            this.mCouponTv.setText(this.resources.getString(R.string.coupon_info, new Object[]{baseGoodsItem.couponAmount}));
            this.mCouponPrefix.setVisibility(0);
            this.mPriceTv.setVisibility(0);
            if (TextUtils.isEmpty(baseGoodsItem.price)) {
                BusinessMonitorLogger.PriceParamDetect.show("HomeRecommendViewHolder", "recommend_item", "baseGoodsItem.price");
            }
        } else {
            this.mCouponTv.setVisibility(8);
            this.mCouponPrefix.setVisibility(8);
            this.mPriceTv.setVisibility(8);
        }
        if (TextUtils.isEmpty(baseGoodsItem.recommendReasons)) {
            this.mRecommendReason.setVisibility(8);
        } else {
            this.mRecommendReason.setVisibility(0);
            this.mRecommendReason.setText(baseGoodsItem.recommendReasons);
        }
        this.mSellCountTv.setText(this.resources.getString(R.string.sold_info_with_placeholder, new Object[]{StringUtil.sellOutNumStr(baseGoodsItem.monthSellCount)}));
        this.mPriceTv.setText(StringUtil.getStrikeThroughPrice(baseGoodsItem.price, 12, 12));
        setIcon(baseGoodsItem, this.mIcon, this.mTitle);
        this.mPriceAfterCoupon.setText(StringUtil.getBoldStylePriceWithDecimal(StringUtil.deleteDecimalZeroStr(baseGoodsItem.priceAfterCoupon), 12, 18, 12));
        if (TextUtils.isEmpty(baseGoodsItem.priceAfterCoupon)) {
            BusinessMonitorLogger.PriceParamDetect.show("HomeRecommendViewHolder", "recommend_item", "baseGoodsItem.priceAfterCoupon");
        }
    }

    private void setIcon(BaseGoodsItem baseGoodsItem, SimpleDraweeView simpleDraweeView, TextView textView) {
        if (TextUtils.isEmpty(baseGoodsItem.auctionIconUrl)) {
            simpleDraweeView.setVisibility(8);
            textView.setText(Html.fromHtml(baseGoodsItem.itemName));
            return;
        }
        simpleDraweeView.setVisibility(0);
        Matcher matcher = sIconUrlSizePattern.matcher(baseGoodsItem.auctionIconUrl);
        int i = this.mDefaultTitleIconWidth;
        if (matcher.matches() && matcher.groupCount() > 3) {
            try {
                int intValue = Integer.valueOf(matcher.group(2)).intValue();
                int intValue2 = Integer.valueOf(matcher.group(3)).intValue();
                if (intValue2 != 0) {
                    i = (this.mDefaultTitleIconHeight * intValue) / intValue2;
                }
            } catch (Exception e) {
                logger.warn("parsing icon url size error", (Throwable) e);
            }
        }
        ViewGroup.LayoutParams layoutParams = simpleDraweeView.getLayoutParams();
        layoutParams.width = i;
        simpleDraweeView.setLayoutParams(layoutParams);
        simpleDraweeView.setImageURI(EtaoDraweeView.getRemoteUrl(baseGoodsItem.auctionIconUrl, i, this.mDefaultTitleIconHeight));
        if (!TextUtils.isEmpty(baseGoodsItem.couponAmount)) {
            textView.setText(Html.fromHtml(baseGoodsItem.itemName));
            return;
        }
        SpannableString spannableString = new SpannableString(Html.fromHtml(baseGoodsItem.itemName));
        spannableString.setSpan(new FlowTitleLeadingSpan(i + this.resources.getDimensionPixelOffset(R.dimen.common_padding_4)), 0, spannableString.length(), 33);
        textView.setText(spannableString);
    }
}
