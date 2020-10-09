package com.alimama.moon.features.home.adapter;

import android.content.res.Resources;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.alimama.moon.R;
import com.alimama.moon.features.home.item.BaseGoodsItem;
import com.alimama.moon.usertrack.UTHelper;
import com.alimama.moon.utils.SpmProcessor;
import com.alimama.moon.utils.StringUtil;
import com.alimama.moon.web.WebPageIntentGenerator;
import com.alimama.union.app.infrastructure.socialShare.social.ShareUtils;
import com.alimama.union.app.logger.BusinessMonitorLogger;
import com.alimama.union.app.pagerouter.MoonComponentManager;
import com.alimama.unionwl.base.etaodrawee.EtaoDraweeView;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HomeCardItemAdapter extends RecyclerView.Adapter<ItemViewHolder> {
    /* access modifiers changed from: private */
    public static final Logger logger = LoggerFactory.getLogger((Class<?>) HomeCardItemAdapter.class);
    private final View.OnClickListener mItemClickListener = new View.OnClickListener() {
        public void onClick(View view) {
            if (!(view.getTag(R.id.card_item_itemId) instanceof String) || !(view.getTag(R.id.card_item_url) instanceof String)) {
                HomeCardItemAdapter.logger.error("item url or itemId tag not available when share button clicked");
                return;
            }
            UTHelper.HomePage.clickCardItem((String) view.getTag(R.id.card_item_itemId));
            String destUrl = SpmProcessor.getDestUrl((String) view.getTag(R.id.card_item_url), UTHelper.HomePage.SPM_CNT_BACKUP, false);
            if (!TextUtils.isEmpty(destUrl)) {
                MoonComponentManager.getInstance().getPageRouter().gotoPage(WebPageIntentGenerator.getItemLandingPage(destUrl));
            }
        }
    };
    private List<BaseGoodsItem> mItemList = new ArrayList();
    private final View.OnClickListener mShareClickListener = new View.OnClickListener() {
        public void onClick(View view) {
            if (!(view.getTag(R.id.card_share_itemId) instanceof String) || !(view.getTag(R.id.card_share_url) instanceof String)) {
                HomeCardItemAdapter.logger.error("item url tag or itemId not available when share button clicked");
                return;
            }
            UTHelper.HomePage.clickCardShare((String) view.getTag(R.id.card_share_itemId));
            String destUrl = SpmProcessor.getDestUrl((String) view.getTag(R.id.card_share_url), UTHelper.HomePage.SPM_CNT_BACKUP, false);
            if (!TextUtils.isEmpty(destUrl)) {
                ShareUtils.showShare(view.getContext(), destUrl);
            }
        }
    };

    public void setItemList(List<BaseGoodsItem> list) {
        this.mItemList.clear();
        this.mItemList.addAll(list);
        notifyDataSetChanged();
    }

    public int getItemCount() {
        return this.mItemList.size();
    }

    @NonNull
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_home_card_item, viewGroup, false);
        inflate.setOnClickListener(this.mItemClickListener);
        inflate.findViewById(R.id.share_container).setOnClickListener(this.mShareClickListener);
        return new ItemViewHolder(inflate);
    }

    public void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, int i) {
        if (i < 0 || i >= this.mItemList.size()) {
            logger.warn("illegal position {} for list with size {}", (Object) Integer.valueOf(i), (Object) Integer.valueOf(this.mItemList.size()));
            return;
        }
        BaseGoodsItem baseGoodsItem = this.mItemList.get(i);
        itemViewHolder.itemView.setTag(R.id.card_item_url, baseGoodsItem.url);
        itemViewHolder.itemView.setTag(R.id.card_item_itemId, baseGoodsItem.itemId);
        itemViewHolder.mShareContainer.setTag(R.id.card_share_url, baseGoodsItem.url);
        itemViewHolder.mShareContainer.setTag(R.id.card_share_itemId, baseGoodsItem.itemId);
        Resources resources = itemViewHolder.itemView.getResources();
        String str = (String) itemViewHolder.mItemImg.getTag();
        if (TextUtils.isEmpty(str)) {
            itemViewHolder.mItemImg.setAnyImageUrl(baseGoodsItem.pic);
            itemViewHolder.mItemImg.setTag(baseGoodsItem.pic);
        } else if (!TextUtils.equals(str, baseGoodsItem.pic)) {
            itemViewHolder.mItemImg.setAnyImageUrl(baseGoodsItem.pic);
            itemViewHolder.mItemImg.setTag(baseGoodsItem.pic);
        }
        if (!TextUtils.isEmpty(baseGoodsItem.couponAmount)) {
            itemViewHolder.mCouponTextView.setVisibility(0);
            itemViewHolder.mCouponTextView.setText(resources.getString(R.string.coupon_info, new Object[]{baseGoodsItem.couponAmount}));
            itemViewHolder.mPriceTextView.setText(StringUtil.getBoldStylePrice(baseGoodsItem.priceAfterCoupon, 10, 12));
            if (TextUtils.isEmpty(baseGoodsItem.priceAfterCoupon)) {
                BusinessMonitorLogger.PriceParamDetect.show("HomeCardItemAdapter", "union_sales_card", "item.priceAfterCoupon");
            }
        } else {
            itemViewHolder.mCouponTextView.setVisibility(8);
            itemViewHolder.mCoupon.setVisibility(8);
            if (!TextUtils.isEmpty(baseGoodsItem.price)) {
                itemViewHolder.mPriceTextView.setText(StringUtil.getBoldStylePrice(baseGoodsItem.price, 10, 12));
            }
            if (TextUtils.isEmpty(baseGoodsItem.price)) {
                BusinessMonitorLogger.PriceParamDetect.show("HomeCardItemAdapter", "union_sales_card", "item.price");
            }
        }
        itemViewHolder.mCommissionTextView.setText(StringUtil.getBoldStylePrice(baseGoodsItem.tkCommissionAmount, 12, 13));
        if (TextUtils.isEmpty(baseGoodsItem.tkCommissionAmount)) {
            BusinessMonitorLogger.PriceParamDetect.show("HomeCardItemAdapter", "union_sales_card", "item.tkCommissionAmount");
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        /* access modifiers changed from: private */
        public TextView mCommissionTextView;
        /* access modifiers changed from: private */
        public TextView mCoupon;
        /* access modifiers changed from: private */
        public TextView mCouponTextView;
        /* access modifiers changed from: private */
        public EtaoDraweeView mItemImg;
        /* access modifiers changed from: private */
        public TextView mPriceTextView;
        /* access modifiers changed from: private */
        public View mShareContainer;

        public ItemViewHolder(@NonNull View view) {
            super(view);
            this.mItemImg = (EtaoDraweeView) view.findViewById(R.id.card_item_img);
            this.mCouponTextView = (TextView) view.findViewById(R.id.card_item_coupon_text);
            this.mPriceTextView = (TextView) view.findViewById(R.id.tv_price_info);
            this.mCommissionTextView = (TextView) view.findViewById(R.id.tv_commission);
            this.mShareContainer = view.findViewById(R.id.share_container);
            this.mCoupon = (TextView) view.findViewById(R.id.tv_coupon);
        }
    }
}
