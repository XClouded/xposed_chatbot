package com.alimama.moon.features.search;

import android.content.Context;
import android.content.res.Resources;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.alimama.moon.R;
import com.alimama.moon.features.home.item.BaseGoodsItem;
import com.alimama.moon.utils.StringUtil;
import com.alimama.union.app.logger.BusinessMonitorLogger;
import com.alimama.unionwl.base.etaodrawee.EtaoDraweeView;
import com.facebook.drawee.view.SimpleDraweeView;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonItemsAdapter extends RecyclerView.Adapter<CommonItemsViewHolder> {
    /* access modifiers changed from: private */
    public static final Logger logger = LoggerFactory.getLogger((Class<?>) CommonItemsAdapter.class);
    private static final Pattern sIconUrlSizePattern = Pattern.compile("(.+)-(\\d+)-(\\d+)\\.(png|jpeg|jpg|gif)$");
    /* access modifiers changed from: private */
    public ClickInterceptor mClickInterceptor;
    private final int mDefaultTitleIconHeight;
    private final int mDefaultTitleIconWidth;
    private final int mIconTitlePadding;
    private final View.OnClickListener mItemClickListener = new View.OnClickListener() {
        public void onClick(View view) {
            if (!(view.getTag(R.id.searchResults_item_url) instanceof String)) {
                CommonItemsAdapter.logger.error("item url tag not available when share button clicked");
                return;
            }
            String str = (String) view.getTag(R.id.searchResults_item_url);
            String str2 = (String) view.getTag(R.id.searchResults_item_itemId);
            String str3 = (String) view.getTag(R.id.searchResults_item_lensId);
            if (CommonItemsAdapter.this.mClickInterceptor != null) {
                CommonItemsAdapter.this.mClickInterceptor.clickItem(str, str2, str3, view);
            }
        }
    };
    private final List<BaseGoodsItem> mItemList = new ArrayList();
    private final View.OnClickListener mShareClickListener = new View.OnClickListener() {
        public void onClick(View view) {
            if (!(view.getTag(R.id.searchResults_share_url) instanceof String)) {
                CommonItemsAdapter.logger.error("item url tag not available when share button clicked");
                return;
            }
            String str = (String) view.getTag(R.id.searchResults_share_url);
            String str2 = (String) view.getTag(R.id.searchResults_share_itemId);
            String str3 = (String) view.getTag(R.id.searchResults_share_lensId);
            if (CommonItemsAdapter.this.mClickInterceptor != null) {
                CommonItemsAdapter.this.mClickInterceptor.clickShare(str, str2, str3, view);
            }
        }
    };

    public interface ClickInterceptor {
        void clickItem(String str, String str2, String str3, View view);

        void clickShare(String str, String str2, String str3, View view);
    }

    public CommonItemsAdapter(@NonNull Context context, ClickInterceptor clickInterceptor) {
        Resources resources = context.getResources();
        this.mDefaultTitleIconHeight = resources.getDimensionPixelOffset(R.dimen.search_result_title_icon_height);
        this.mDefaultTitleIconWidth = resources.getDimensionPixelOffset(R.dimen.search_result_title_icon_width);
        this.mIconTitlePadding = resources.getDimensionPixelOffset(R.dimen.common_padding_4);
        this.mClickInterceptor = clickInterceptor;
    }

    @NonNull
    public CommonItemsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_result_card_view, viewGroup, false);
        inflate.setOnClickListener(this.mItemClickListener);
        inflate.findViewById(R.id.btn_share).setOnClickListener(this.mShareClickListener);
        return new CommonItemsViewHolder(inflate);
    }

    public void onBindViewHolder(@NonNull CommonItemsViewHolder commonItemsViewHolder, int i) {
        if (i < 0 || i >= this.mItemList.size()) {
            logger.warn("illegal position {} for list with size {}", (Object) Integer.valueOf(i), (Object) Integer.valueOf(this.mItemList.size()));
            return;
        }
        BaseGoodsItem baseGoodsItem = this.mItemList.get(i);
        commonItemsViewHolder.itemView.setTag(R.id.searchResults_item_url, baseGoodsItem.url);
        commonItemsViewHolder.itemView.setTag(R.id.searchResults_item_itemId, baseGoodsItem.itemId);
        commonItemsViewHolder.itemView.setTag(R.id.searchResults_item_lensId, baseGoodsItem.lensId);
        commonItemsViewHolder.mShareButton.setTag(R.id.searchResults_share_url, baseGoodsItem.url);
        commonItemsViewHolder.mShareButton.setTag(R.id.searchResults_share_itemId, baseGoodsItem.itemId);
        commonItemsViewHolder.mShareButton.setTag(R.id.searchResults_share_lensId, baseGoodsItem.lensId);
        if (TextUtils.isEmpty(baseGoodsItem.auctionIconUrl)) {
            commonItemsViewHolder.mTitleIcon.setVisibility(8);
            commonItemsViewHolder.mTitle.setText(Html.fromHtml(baseGoodsItem.itemName));
        } else {
            commonItemsViewHolder.mTitleIcon.setVisibility(0);
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
            ViewGroup.LayoutParams layoutParams = commonItemsViewHolder.mTitleIcon.getLayoutParams();
            layoutParams.width = i2;
            commonItemsViewHolder.mTitleIcon.setLayoutParams(layoutParams);
            commonItemsViewHolder.mTitleIcon.setImageURI(EtaoDraweeView.getRemoteUrl(baseGoodsItem.auctionIconUrl, i2, this.mDefaultTitleIconHeight));
            SpannableString spannableString = new SpannableString(Html.fromHtml(baseGoodsItem.itemName));
            spannableString.setSpan(new FlowTitleLeadingSpan(i2 + this.mIconTitlePadding), 0, spannableString.length(), 33);
            commonItemsViewHolder.mTitle.setText(spannableString);
        }
        commonItemsViewHolder.mImageView.setAnyImageUrl(baseGoodsItem.pic);
        commonItemsViewHolder.mCommissionTextView.setText(StringUtil.getBoldStylePrice(baseGoodsItem.tkCommissionAmount, 11, 12));
        if (TextUtils.isEmpty(baseGoodsItem.tkCommissionAmount)) {
            BusinessMonitorLogger.PriceParamDetect.show("CommonItemsAdapter", "search_result", "item.tkCommissionAmount");
        }
        Resources resources = commonItemsViewHolder.itemView.getResources();
        commonItemsViewHolder.mMonthSellInfoTextView.setText(resources.getString(R.string.sold_info_with_placeholder, new Object[]{StringUtil.sellOutNumStr(baseGoodsItem.monthSellCount)}));
        if (TextUtils.isEmpty(baseGoodsItem.recommendReasons)) {
            commonItemsViewHolder.mRecommendReasonTextView.setVisibility(8);
            commonItemsViewHolder.mTitle.setMaxLines(2);
        } else {
            commonItemsViewHolder.mRecommendReasonTextView.setVisibility(0);
            commonItemsViewHolder.mRecommendReasonTextView.setText(baseGoodsItem.recommendReasons);
            commonItemsViewHolder.mTitle.setMaxLines(1);
        }
        if (!TextUtils.isEmpty(baseGoodsItem.couponAmount)) {
            commonItemsViewHolder.mCouponTextView.setVisibility(0);
            commonItemsViewHolder.mCouponTextView.setText(resources.getString(R.string.coupon_info, new Object[]{baseGoodsItem.couponAmount}));
            commonItemsViewHolder.mPriceInfoPrefixTextView.setText(R.string.price_after_coupon);
            commonItemsViewHolder.mPriceTextView.setText(StringUtil.getBoldStylePriceWithDecimal(StringUtil.deleteDecimalZeroStr(baseGoodsItem.priceAfterCoupon), 12, 18, 12));
            if (TextUtils.isEmpty(baseGoodsItem.priceAfterCoupon)) {
                BusinessMonitorLogger.PriceParamDetect.show("CommonItemsAdapter", "search_result", "item.priceAfterCoupon");
            }
            commonItemsViewHolder.mStrikeThroughPriceTextView.setVisibility(0);
            commonItemsViewHolder.mStrikeThroughPriceTextView.setText(StringUtil.getStrikeThroughPrice(baseGoodsItem.price, 12, 12));
            if (TextUtils.isEmpty(baseGoodsItem.price)) {
                BusinessMonitorLogger.PriceParamDetect.show("CommonItemsAdapter", "search_result", "item.price");
                return;
            }
            return;
        }
        commonItemsViewHolder.mCouponTextView.setVisibility(8);
        commonItemsViewHolder.mPriceInfoPrefixTextView.setVisibility(8);
        commonItemsViewHolder.mPriceTextView.setText(StringUtil.getBoldStylePriceWithDecimal(StringUtil.deleteDecimalZeroStr(baseGoodsItem.priceAfterCoupon), 12, 18, 12));
        if (TextUtils.isEmpty(baseGoodsItem.priceAfterCoupon)) {
            BusinessMonitorLogger.PriceParamDetect.show("CommonItemsAdapter", "search_result", "item.priceAfterCoupon");
        }
        commonItemsViewHolder.mStrikeThroughPriceTextView.setVisibility(4);
    }

    public int getItemCount() {
        return this.mItemList.size();
    }

    public void setData(@NonNull List<BaseGoodsItem> list) {
        this.mItemList.clear();
        this.mItemList.addAll(list);
        notifyDataSetChanged();
    }

    public void appendData(@NonNull List<BaseGoodsItem> list) {
        int size = this.mItemList.size();
        this.mItemList.addAll(list);
        notifyItemRangeInserted(size, list.size());
    }

    public static class CommonItemsViewHolder extends RecyclerView.ViewHolder {
        /* access modifiers changed from: private */
        public TextView mCommissionTextView;
        /* access modifiers changed from: private */
        public TextView mCouponTextView;
        /* access modifiers changed from: private */
        public EtaoDraweeView mImageView;
        /* access modifiers changed from: private */
        public TextView mMonthSellInfoTextView;
        /* access modifiers changed from: private */
        public TextView mPriceInfoPrefixTextView;
        /* access modifiers changed from: private */
        public TextView mPriceTextView;
        /* access modifiers changed from: private */
        public TextView mRecommendReasonTextView;
        /* access modifiers changed from: private */
        public View mShareButton;
        /* access modifiers changed from: private */
        public TextView mStrikeThroughPriceTextView;
        /* access modifiers changed from: private */
        public TextView mTitle;
        /* access modifiers changed from: private */
        public SimpleDraweeView mTitleIcon;

        public CommonItemsViewHolder(View view) {
            super(view);
            this.mTitle = (TextView) view.findViewById(R.id.search_result_title);
            this.mTitleIcon = (SimpleDraweeView) view.findViewById(R.id.search_result_title_icon);
            this.mCouponTextView = (TextView) view.findViewById(R.id.search_result_coupon_text);
            this.mImageView = (EtaoDraweeView) view.findViewById(R.id.search_result_goods_image);
            this.mPriceInfoPrefixTextView = (TextView) view.findViewById(R.id.tv_price_info_prefix);
            this.mPriceTextView = (TextView) view.findViewById(R.id.tv_price_info);
            this.mStrikeThroughPriceTextView = (TextView) view.findViewById(R.id.tv_strike_through_price);
            this.mCommissionTextView = (TextView) view.findViewById(R.id.tv_commission);
            this.mMonthSellInfoTextView = (TextView) view.findViewById(R.id.search_result_sold_text);
            this.mShareButton = view.findViewById(R.id.btn_share);
            this.mRecommendReasonTextView = (TextView) view.findViewById(R.id.recommend_reason_tv);
        }
    }
}
