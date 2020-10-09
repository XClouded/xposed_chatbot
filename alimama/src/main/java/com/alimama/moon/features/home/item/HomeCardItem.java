package com.alimama.moon.features.home.item;

import com.alimamaunion.base.safejson.SafeJSONArray;
import com.alimamaunion.base.safejson.SafeJSONObject;
import com.alimamaunion.common.listpage.CommonBaseItem;
import com.taobao.weex.ui.component.WXEmbed;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

public class HomeCardItem extends CommonBaseItem {
    public String imgUrl;
    public List<BaseGoodsItem> itemList = new ArrayList();
    public String moreUrl;

    public HomeCardItem(String str, int i, JSONObject jSONObject) {
        super(str, i, jSONObject);
        SafeJSONObject safeJSONObject = new SafeJSONObject(jSONObject);
        this.imgUrl = safeJSONObject.optString("img");
        this.moreUrl = safeJSONObject.optString("src");
        SafeJSONArray optJSONArray = safeJSONObject.optJSONArray("items");
        for (int i2 = 0; i2 < optJSONArray.length(); i2++) {
            SafeJSONObject optJSONObject = optJSONArray.optJSONObject(i2);
            BaseGoodsItem baseGoodsItem = new BaseGoodsItem();
            baseGoodsItem.pic = optJSONObject.optString("pic");
            baseGoodsItem.url = optJSONObject.optString("url");
            baseGoodsItem.price = optJSONObject.optString("promotionPrice");
            baseGoodsItem.couponAmount = optJSONObject.optString("couponAmount");
            baseGoodsItem.priceAfterCoupon = optJSONObject.optString("priceAfterCoupon");
            baseGoodsItem.tkCommissionAmount = optJSONObject.optString("tkCommissionAmount");
            baseGoodsItem.itemId = optJSONObject.optString(WXEmbed.ITEM_ID);
            this.itemList.add(baseGoodsItem);
        }
    }
}
