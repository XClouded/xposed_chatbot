package com.alimama.moon.features.home.item;

import com.alimamaunion.base.safejson.SafeJSONObject;
import com.alimamaunion.common.listpage.CommonBaseItem;
import com.taobao.weex.ui.component.WXEmbed;
import org.json.JSONObject;

public class HomeCommonTabItem extends CommonBaseItem {
    public BaseGoodsItem baseGoodsItem = new BaseGoodsItem();

    public HomeCommonTabItem(String str, int i, JSONObject jSONObject) {
        super(str, i, jSONObject);
        SafeJSONObject safeJSONObject = new SafeJSONObject(jSONObject);
        this.baseGoodsItem.itemName = safeJSONObject.optString("itemName");
        this.baseGoodsItem.url = safeJSONObject.optString("url");
        this.baseGoodsItem.pic = safeJSONObject.optString("pic");
        this.baseGoodsItem.auctionIconUrl = safeJSONObject.optString("auctionIconUrl");
        this.baseGoodsItem.tkCommissionAmount = safeJSONObject.optString("tkCommissionAmount");
        this.baseGoodsItem.couponAmount = safeJSONObject.optString("couponAmount");
        this.baseGoodsItem.priceAfterCoupon = safeJSONObject.optString("priceAfterCoupon");
        this.baseGoodsItem.price = safeJSONObject.optString("promotionPrice");
        this.baseGoodsItem.monthSellCount = safeJSONObject.optString("monthSellCount");
        this.baseGoodsItem.itemId = safeJSONObject.optString(WXEmbed.ITEM_ID);
        this.baseGoodsItem.recommendReasons = safeJSONObject.optString("recommendReasons");
    }
}
