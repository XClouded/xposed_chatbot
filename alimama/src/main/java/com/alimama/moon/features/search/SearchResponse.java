package com.alimama.moon.features.search;

import com.alimama.moon.features.home.item.BaseGoodsItem;
import com.alimamaunion.base.safejson.SafeJSONArray;
import com.alimamaunion.base.safejson.SafeJSONObject;
import com.taobao.weex.ui.component.WXEmbed;
import java.util.ArrayList;
import java.util.List;

public class SearchResponse {
    private final List<BaseGoodsItem> resultList = new ArrayList();
    private int totalCount;

    public static SearchResponse fromJson(SafeJSONObject safeJSONObject) {
        SafeJSONObject optJSONObject = safeJSONObject.optJSONObject("recommend");
        SafeJSONArray optJSONArray = optJSONObject.optJSONArray("resultList");
        SearchResponse searchResponse = new SearchResponse();
        searchResponse.totalCount = optJSONObject.optInt("totalCount");
        for (int i = 0; i < optJSONArray.length(); i++) {
            SafeJSONObject optJSONObject2 = optJSONArray.optJSONObject(i);
            BaseGoodsItem baseGoodsItem = new BaseGoodsItem();
            baseGoodsItem.url = optJSONObject2.optString("url");
            baseGoodsItem.pic = optJSONObject2.optString("pic");
            baseGoodsItem.itemName = optJSONObject2.optString("itemName");
            baseGoodsItem.auctionIconUrl = optJSONObject2.optString("auctionIconUrl");
            baseGoodsItem.priceAfterCoupon = optJSONObject2.optString("priceAfterCoupon");
            baseGoodsItem.price = optJSONObject2.optString("price");
            baseGoodsItem.couponAmount = optJSONObject2.optString("couponAmount");
            baseGoodsItem.calTkCommission = optJSONObject2.optString("calTkCommission");
            baseGoodsItem.tkCommissionAmount = optJSONObject2.optString("tkCommissionAmount");
            baseGoodsItem.monthSellCount = optJSONObject2.optString("monthSellCount");
            baseGoodsItem.lensId = optJSONObject2.optString("lensId");
            baseGoodsItem.itemId = optJSONObject2.optString(WXEmbed.ITEM_ID);
            searchResponse.resultList.add(baseGoodsItem);
        }
        return searchResponse;
    }

    public int getTotalCount() {
        return this.totalCount;
    }

    public List<BaseGoodsItem> getResultList() {
        return this.resultList;
    }
}
