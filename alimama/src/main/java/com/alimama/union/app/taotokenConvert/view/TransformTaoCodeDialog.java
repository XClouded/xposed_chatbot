package com.alimama.union.app.taotokenConvert.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.alimama.moon.R;
import com.alimama.moon.usertrack.UTHelper;
import com.alimama.moon.utils.Helper;
import com.alimama.moon.utils.SpmProcessor;
import com.alimama.moon.utils.StringUtil;
import com.alimama.moon.web.WebPageIntentGenerator;
import com.alimama.union.app.infrastructure.socialShare.social.ShareUtils;
import com.alimama.union.app.network.response.TaoCodeItemInfo;
import com.alimama.union.app.pagerouter.MoonComponentManager;
import com.alimama.unionwl.base.etaodrawee.EtaoDraweeView;
import com.taobao.login4android.Login;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransformTaoCodeDialog extends Dialog implements DialogInterface.OnShowListener, DialogInterface.OnDismissListener, View.OnClickListener {
    private static final String CLICK_CONVERT_SUGGEST_DETAIL_CONTROL_NAME = "click_convertsuggest_detail";
    private static final String CLICK_CONVERT_TAO_DETAIL_CONTROL_NAME = "click_converttao_detail";
    private static final String CLICK_CONVERT_TAO_SHARE_CONTROL_NAME = "click_converttao_share";
    private static final String CLICK_EARN_SHARE_CONTROL_NAME = "click_convertsuggest_share";
    private static final String CLICK_MORE_CONTROL_NAME = "click_convertsuggest_more";
    private static final String CONVERT_SUGGEST_SHOW_CONTROL_NAME = "convertsuggest_show";
    private static final String CONVERT_TAO_SHOW_CONTROL_NAME = "converttao_show";
    private static final String RMB_SYMBOL = "¥ ";
    public static final String SPM_CLOSE_CONTROL_NAME = "clickclose";
    private static final String SPM_DETAIL_CONTROL_NAME = "clickdetail";
    public static final String SPM_RULES_CONTROL_NAME = "clickrules";
    public static final String SPM_SEARCH_TITLE_CONTROL_NAME = "clicksearchtitle";
    private static final String SPM_SHARE_CONTROL_NAME = "clickshare";
    public static final String SPM_SIMILAR_CONTROL_NAME = "clicksimilar";
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) TransformTaoCodeDialog.class);
    private TextView commissionTextView = ((TextView) findViewById(R.id.tv_commission));
    private TextView contentTextView = ((TextView) findViewById(R.id.tv_content));
    private TextView couponInfoText = ((TextView) findViewById(R.id.tv_coupon_info));
    private EtaoDraweeView itemImageView = ((EtaoDraweeView) findViewById(R.id.image_view));
    @Nullable
    private TaoCodeItemInfo itemInfo;
    private TextView mPrivacyMarketCouponTv = ((TextView) findViewById(R.id.privacy_market_coupon_tv));
    private TextView mPrivacyMarketEarnTv = ((TextView) findViewById(R.id.privacy_market_earn_tv));
    private LinearLayout mPrivacyMarketLl = ((LinearLayout) findViewById(R.id.privacy_market_ll));
    private TextView mPrivacyMarketPriceAfterAllRightsTv = ((TextView) findViewById(R.id.privacy_market_price_after_all_rights));
    private TextView mPrivacyMarketPriceTv = ((TextView) findViewById(R.id.privacy_market_price));
    private TextView mPrivacyMarketTaoBonusTv = ((TextView) findViewById(R.id.privacy_market_tao_bonus_tv));
    private TextView mPrivacyMarketTitleTv = ((TextView) findViewById(R.id.privacy_market_title_tv));
    private EtaoDraweeView mSimilarRecommendItemImg = ((EtaoDraweeView) findViewById(R.id.similar_recommend_image_view));
    private TextView mSimilarRecommendItemPriceAfterAllRightsTv = ((TextView) findViewById(R.id.similar_recommend_price_after_all_rights));
    private TextView mSimilarRecommendItemPriceTv = ((TextView) findViewById(R.id.similar_recommend_price));
    private TextView mSimilarRecommendItemTitleTv = ((TextView) findViewById(R.id.similar_recommend_item_title));
    private LinearLayout mSimilarRecommendLl = ((LinearLayout) findViewById(R.id.similar_recommend_ll));
    private TextView mSimilarRecommendSubtitleTv = ((TextView) findViewById(R.id.similar_recommend_subtitle_tv));
    private TextView mSimilarRecommendTitleTv = ((TextView) findViewById(R.id.similar_recommend_title_tv));
    private TextView mSimilarRecommentItemEarnTv = ((TextView) findViewById(R.id.similar_recommend_earn_tv));
    private TextView priceInfoPrefix = ((TextView) findViewById(R.id.tv_price_info_prefix));
    private TextView priceInfoText = ((TextView) findViewById(R.id.tv_price_info));
    @Nullable
    private String sharedContent;
    private TextView strikeThroughPriceText = ((TextView) findViewById(R.id.tv_strike_through_price));
    private TextView titleTextView = ((TextView) findViewById(R.id.tv_title));

    public TransformTaoCodeDialog(@NonNull Context context) {
        super(context, R.style.common_dialog_style);
        setContentView(R.layout.dialog_transform_taocode);
        findViewById(R.id.btn_negative).setOnClickListener(this);
        findViewById(R.id.btn_positive).setOnClickListener(this);
        findViewById(R.id.item_info_panel).setOnClickListener(this);
        findViewById(R.id.btn_close).setOnClickListener(this);
        findViewById(R.id.more_rl).setOnClickListener(this);
        findViewById(R.id.similar_recommend_earn_tv).setOnClickListener(this);
        findViewById(R.id.similar_recommend_item_rl).setOnClickListener(this);
        findViewById(R.id.privacy_market_content).setOnClickListener(this);
        findViewById(R.id.privacy_market_earn_tv).setOnClickListener(this);
        setOnShowListener(this);
        setOnDismissListener(this);
        setCanceledOnTouchOutside(false);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_negative:
            case R.id.item_info_panel:
                UTHelper.sendControlHit(UTHelper.UT_TAO_CODE_DIALOG_PAGE_NAME, SPM_DETAIL_CONTROL_NAME);
                String itemDetailPageUrl = getItemDetailPageUrl();
                if (!TextUtils.isEmpty(itemDetailPageUrl)) {
                    MoonComponentManager.getInstance().getPageRouter().gotoPage(itemDetailPageUrl);
                    break;
                } else {
                    logger.error("detail page url not available when navigate to detail page");
                    return;
                }
            case R.id.btn_positive:
                UTHelper.sendControlHit(UTHelper.UT_TAO_CODE_DIALOG_PAGE_NAME, SPM_SHARE_CONTROL_NAME);
                if (this.itemInfo != null && !TextUtils.isEmpty(this.itemInfo.getRawUrl())) {
                    ShareUtils.showShare(getContext(), SpmProcessor.appendSpm(this.itemInfo.getRawUrl(), UTHelper.TaoCodeDialog.SPM_VAL_TO_SHARE));
                    break;
                } else {
                    logger.error("item rawUrl not available when navigate to share page");
                    return;
                }
            case R.id.more_rl:
                UTHelper.sendControlHit(UTHelper.UT_TAO_CODE_DIALOG_PAGE_NAME, CLICK_MORE_CONTROL_NAME);
                MoonComponentManager.getInstance().getPageRouter().gotoPage(Uri.parse(this.itemInfo.taoTokenMarketInfoDTO.itemListUrl).buildUpon().appendQueryParameter("itemid", this.itemInfo.getItemId()).appendQueryParameter("userId", Login.getUserId()).build().toString());
                break;
            case R.id.similar_recommend_item_rl:
                HashMap hashMap = new HashMap();
                hashMap.put("args", UTHelper.HomePage.SPM_CLICK_FLASH_REBATE_EARN_MORE);
                UTHelper.sendControlHit(UTHelper.UT_TAO_CODE_DIALOG_PAGE_NAME, CLICK_CONVERT_SUGGEST_DETAIL_CONTROL_NAME, hashMap);
                String destUrl = SpmProcessor.getDestUrl(this.itemInfo.taoTokenMarketInfoDTO.taoTokenMarketItemDTO.url, UTHelper.HomePage.SPM_CNT_BACKUP, false);
                if (!TextUtils.isEmpty(destUrl)) {
                    MoonComponentManager.getInstance().getPageRouter().gotoPage(WebPageIntentGenerator.getRebateItemLandingPage(destUrl));
                    break;
                } else {
                    return;
                }
            case R.id.similar_recommend_earn_tv:
                HashMap hashMap2 = new HashMap();
                hashMap2.put("args", UTHelper.HomePage.SPM_CLICK_FLASH_REBATE_EARN_MORE);
                UTHelper.sendControlHit(UTHelper.UT_TAO_CODE_DIALOG_PAGE_NAME, CLICK_EARN_SHARE_CONTROL_NAME, hashMap2);
                String destUrl2 = SpmProcessor.getDestUrl(this.itemInfo.taoTokenMarketInfoDTO.taoTokenMarketItemDTO.url, UTHelper.HomePage.SPM_CLICK_FLASH_REBATE_EARN_MORE, false);
                if (!TextUtils.isEmpty(destUrl2)) {
                    ShareUtils.showShare(getContext(), destUrl2);
                    break;
                } else {
                    return;
                }
            case R.id.privacy_market_content:
                HashMap hashMap3 = new HashMap();
                hashMap3.put("args", UTHelper.HomePage.SPM_CLICK_FLASH_REBATE_EARN_MORE);
                UTHelper.sendControlHit(UTHelper.UT_TAO_CODE_DIALOG_PAGE_NAME, CLICK_CONVERT_TAO_DETAIL_CONTROL_NAME, hashMap3);
                String destUrl3 = SpmProcessor.getDestUrl(this.itemInfo.taoTokenMarketInfoDTO.taoTokenMarketItemDTO.url, UTHelper.HomePage.SPM_CNT_BACKUP, false);
                if (!TextUtils.isEmpty(destUrl3)) {
                    MoonComponentManager.getInstance().getPageRouter().gotoPage(WebPageIntentGenerator.getRebateItemLandingPage(destUrl3));
                    break;
                } else {
                    return;
                }
            case R.id.privacy_market_earn_tv:
                HashMap hashMap4 = new HashMap();
                hashMap4.put("args", UTHelper.HomePage.SPM_CLICK_FLASH_REBATE_EARN_MORE);
                UTHelper.sendControlHit(UTHelper.UT_TAO_CODE_DIALOG_PAGE_NAME, CLICK_CONVERT_TAO_SHARE_CONTROL_NAME, hashMap4);
                String destUrl4 = SpmProcessor.getDestUrl(this.itemInfo.taoTokenMarketInfoDTO.taoTokenMarketItemDTO.url, UTHelper.HomePage.SPM_CLICK_FLASH_REBATE_EARN_MORE, false);
                if (!TextUtils.isEmpty(destUrl4)) {
                    ShareUtils.showShare(getContext(), destUrl4);
                    break;
                } else {
                    return;
                }
        }
        dismiss();
    }

    public void onShow(DialogInterface dialogInterface) {
        SpmProcessor.pageAppear(this, UTHelper.UT_TAO_CODE_DIALOG_PAGE_NAME);
    }

    public void onDismiss(DialogInterface dialogInterface) {
        SpmProcessor.pageDisappear(this, UTHelper.TaoCodeDialog.SPM_CNT);
    }

    public void show(String str, TaoCodeItemInfo taoCodeItemInfo) {
        if (TextUtils.isEmpty(str) || taoCodeItemInfo == null) {
            logger.warn("can't display empty content {} or item info {}", (Object) str, (Object) taoCodeItemInfo);
            return;
        }
        this.sharedContent = str;
        this.itemInfo = taoCodeItemInfo;
        this.contentTextView.setText(taoCodeItemInfo.getTitle());
        this.commissionTextView.setText(getBoldStylePrice(StringUtil.twoDecimalStr(taoCodeItemInfo.getMoney())));
        this.itemImageView.setAnyImageUrl(taoCodeItemInfo.getPictUrl());
        this.titleTextView.setText(taoCodeItemInfo.isSharedFromUser() ? R.string.tao_code_item_dialog_title_from_user : R.string.tao_code_item_dialog_title);
        if (taoCodeItemInfo.getCouponAmount() != null && taoCodeItemInfo.getCouponAmount().doubleValue() > 0.0d) {
            this.couponInfoText.setVisibility(0);
            this.couponInfoText.setText(getContext().getString(R.string.coupon_info, new Object[]{StringUtil.intValueStr(taoCodeItemInfo.getCouponAmount())}));
            this.priceInfoPrefix.setText(R.string.price_after_coupon);
            this.priceInfoText.setText(getBoldStylePrice(StringUtil.twoDecimalStr(taoCodeItemInfo.getAfterCouponAmount())));
            displayStrikeThroughPrice(taoCodeItemInfo);
        } else {
            this.couponInfoText.setVisibility(4);
            this.priceInfoPrefix.setText(R.string.current_price);
            this.priceInfoText.setText(getBoldStylePrice(StringUtil.twoDecimalStr(taoCodeItemInfo.getPrice())));
            this.strikeThroughPriceText.setVisibility(4);
        }
        if (!taoCodeItemInfo.isTaoTokenMarketInfoDTONull()) {
            showSimilarRecommendView(taoCodeItemInfo);
            showPrivacyMarketView(taoCodeItemInfo);
        } else {
            this.mSimilarRecommendLl.setVisibility(8);
            this.mPrivacyMarketLl.setVisibility(8);
        }
        show();
    }

    private void showSimilarRecommendView(TaoCodeItemInfo taoCodeItemInfo) {
        if (taoCodeItemInfo.taoTokenMarketInfoDTO == null) {
            this.mSimilarRecommendLl.setVisibility(8);
            this.mPrivacyMarketLl.setVisibility(8);
        } else if (!taoCodeItemInfo.taoTokenMarketInfoDTO.itemMatch) {
            this.mSimilarRecommendLl.setVisibility(0);
            this.mPrivacyMarketLl.setVisibility(8);
            UTHelper.sendControlHit(UTHelper.UT_TAO_CODE_DIALOG_PAGE_NAME, CONVERT_SUGGEST_SHOW_CONTROL_NAME);
            this.mSimilarRecommendTitleTv.setText(taoCodeItemInfo.taoTokenMarketInfoDTO.title);
            this.mSimilarRecommendSubtitleTv.setText(taoCodeItemInfo.taoTokenMarketInfoDTO.subtitle);
            if (taoCodeItemInfo.taoTokenMarketInfoDTO.taoTokenMarketItemDTO != null) {
                this.mSimilarRecommendItemImg.setAnyImageUrl(taoCodeItemInfo.taoTokenMarketInfoDTO.taoTokenMarketItemDTO.pictUrl);
                this.mSimilarRecommendItemTitleTv.setText(taoCodeItemInfo.taoTokenMarketInfoDTO.taoTokenMarketItemDTO.title);
                this.mSimilarRecommendItemPriceAfterAllRightsTv.setText(getBoldStylePrice(StringUtil.twoDecimalStr(taoCodeItemInfo.taoTokenMarketInfoDTO.taoTokenMarketItemDTO.priceAfterAllRights)));
                SpannableStringBuilder displayedPrice = getDisplayedPrice(taoCodeItemInfo.taoTokenMarketInfoDTO.taoTokenMarketItemDTO.price);
                displayedPrice.setSpan(new StrikethroughSpan(), RMB_SYMBOL.length(), displayedPrice.length(), 33);
                this.mSimilarRecommendItemPriceTv.setText(displayedPrice);
                TextView textView = this.mSimilarRecommentItemEarnTv;
                textView.setText("赚 " + getBoldStylePrice(StringUtil.twoDecimalStr(taoCodeItemInfo.taoTokenMarketInfoDTO.taoTokenMarketItemDTO.commissionAmount)));
            }
        }
    }

    private void showPrivacyMarketView(TaoCodeItemInfo taoCodeItemInfo) {
        if (taoCodeItemInfo.taoTokenMarketInfoDTO == null) {
            this.mSimilarRecommendLl.setVisibility(8);
            this.mPrivacyMarketLl.setVisibility(8);
        } else if (taoCodeItemInfo.taoTokenMarketInfoDTO.itemMatch) {
            this.mSimilarRecommendLl.setVisibility(8);
            this.mPrivacyMarketLl.setVisibility(0);
            UTHelper.sendControlHit(UTHelper.UT_TAO_CODE_DIALOG_PAGE_NAME, CONVERT_TAO_SHOW_CONTROL_NAME);
            this.mPrivacyMarketTitleTv.setText(taoCodeItemInfo.taoTokenMarketInfoDTO.title);
            if (TextUtils.isEmpty(taoCodeItemInfo.taoTokenMarketInfoDTO.taoTokenMarketItemDTO.couponAmount) || TextUtils.equals("0", taoCodeItemInfo.taoTokenMarketInfoDTO.taoTokenMarketItemDTO.couponAmount)) {
                this.mPrivacyMarketCouponTv.setVisibility(8);
            } else {
                this.mPrivacyMarketCouponTv.setVisibility(0);
                this.mPrivacyMarketCouponTv.setText(getContext().getString(R.string.coupon_info, new Object[]{taoCodeItemInfo.taoTokenMarketInfoDTO.taoTokenMarketItemDTO.couponAmount}));
            }
            if (TextUtils.isEmpty(taoCodeItemInfo.taoTokenMarketInfoDTO.taoTokenMarketItemDTO.rightsFace) || TextUtils.equals("0", taoCodeItemInfo.taoTokenMarketInfoDTO.taoTokenMarketItemDTO.rightsFace)) {
                this.mPrivacyMarketTaoBonusTv.setVisibility(8);
            } else {
                this.mPrivacyMarketTaoBonusTv.setVisibility(0);
                this.mPrivacyMarketTaoBonusTv.setText(getContext().getString(R.string.tao_bonus_coupon_info, new Object[]{taoCodeItemInfo.taoTokenMarketInfoDTO.taoTokenMarketItemDTO.rightsFace}));
            }
            this.mPrivacyMarketPriceAfterAllRightsTv.setText(getBoldStylePrice(StringUtil.twoDecimalStr(taoCodeItemInfo.taoTokenMarketInfoDTO.taoTokenMarketItemDTO.priceAfterAllRights)));
            SpannableStringBuilder displayedPrice = getDisplayedPrice(taoCodeItemInfo.taoTokenMarketInfoDTO.taoTokenMarketItemDTO.price);
            displayedPrice.setSpan(new StrikethroughSpan(), RMB_SYMBOL.length(), displayedPrice.length(), 33);
            this.mPrivacyMarketPriceTv.setText(displayedPrice);
            TextView textView = this.mPrivacyMarketEarnTv;
            textView.setText("赚 " + getBoldStylePrice(StringUtil.twoDecimalStr(taoCodeItemInfo.taoTokenMarketInfoDTO.taoTokenMarketItemDTO.commissionAmount)));
        }
    }

    private void displayStrikeThroughPrice(TaoCodeItemInfo taoCodeItemInfo) {
        this.strikeThroughPriceText.setVisibility(0);
        SpannableStringBuilder displayedPrice = getDisplayedPrice(StringUtil.twoDecimalStr(taoCodeItemInfo.getPrice()));
        displayedPrice.setSpan(new StrikethroughSpan(), RMB_SYMBOL.length(), displayedPrice.length(), 33);
        this.strikeThroughPriceText.setText(displayedPrice);
    }

    private SpannableStringBuilder getDisplayedPrice(@NonNull String str) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(RMB_SYMBOL);
        spannableStringBuilder.append(str);
        spannableStringBuilder.setSpan(new AbsoluteSizeSpan(10, true), 0, RMB_SYMBOL.length(), 33);
        spannableStringBuilder.setSpan(new AbsoluteSizeSpan(14, true), RMB_SYMBOL.length(), spannableStringBuilder.length(), 33);
        return spannableStringBuilder;
    }

    private SpannableStringBuilder getBoldStylePrice(@NonNull String str) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(RMB_SYMBOL);
        spannableStringBuilder.append(str);
        spannableStringBuilder.setSpan(new AbsoluteSizeSpan(10, true), 0, RMB_SYMBOL.length(), 33);
        spannableStringBuilder.setSpan(new AbsoluteSizeSpan(14, true), RMB_SYMBOL.length(), spannableStringBuilder.length(), 33);
        spannableStringBuilder.setSpan(new StyleSpan(1), RMB_SYMBOL.length(), spannableStringBuilder.length(), 33);
        return spannableStringBuilder;
    }

    @Nullable
    private String getItemDetailPageUrl() {
        if (this.itemInfo == null) {
            return null;
        }
        String uri = Helper.appendQueryParameter(Helper.appendQueryParameter(Uri.parse(this.itemInfo.getRawUrl()), "spm", UTHelper.TaoCodeDialog.SPM_VAL_TO_DETAILS), "sharewords", this.sharedContent).toString();
        if (TextUtils.isEmpty(uri)) {
            return null;
        }
        return WebPageIntentGenerator.getItemLandingPage(uri);
    }
}
