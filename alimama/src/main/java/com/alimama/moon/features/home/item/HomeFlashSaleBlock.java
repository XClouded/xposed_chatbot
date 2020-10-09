package com.alimama.moon.features.home.item;

import com.alimamaunion.base.safejson.SafeJSONArray;
import com.alimamaunion.base.safejson.SafeJSONObject;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

public class HomeFlashSaleBlock extends HomeBaseItem {
    public List<FlashSaleBlockItem> itemList;
    public String moreSrc;
    public String name;
    public String ruleUrl;

    public HomeFlashSaleBlock(String str, int i) {
        super(str, i);
        this.itemList = new ArrayList();
        this.itemList = new ArrayList();
    }

    public HomeFlashSaleBlock(String str, int i, JSONObject jSONObject) {
        super(str, i, jSONObject);
        this.itemList = new ArrayList();
        try {
            SafeJSONObject optJSONObject = new SafeJSONObject(jSONObject).optJSONArray("data").optJSONObject(0);
            this.name = optJSONObject.optString("name");
            this.ruleUrl = optJSONObject.optString("ruleUrl");
            this.moreSrc = optJSONObject.optString("src");
            SafeJSONArray optJSONArray = optJSONObject.optJSONArray("items");
            for (int i2 = 0; i2 < optJSONArray.length(); i2++) {
                this.itemList.add(new FlashSaleBlockItem(optJSONArray.optJSONObject(i2)));
            }
        } catch (Exception unused) {
        }
    }
}
