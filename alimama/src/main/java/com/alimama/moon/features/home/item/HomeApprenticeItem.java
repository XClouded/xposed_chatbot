package com.alimama.moon.features.home.item;

import com.alimamaunion.base.safejson.SafeJSONObject;
import org.json.JSONObject;

public class HomeApprenticeItem extends HomeCommonTabItem {
    public HomeApprenticeItem(String str, int i, JSONObject jSONObject) {
        super(str, i, jSONObject);
        SafeJSONObject safeJSONObject = new SafeJSONObject(jSONObject);
        this.baseGoodsItem.price = safeJSONObject.optString("price");
    }
}
