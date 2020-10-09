package com.alimama.moon.features.home.item;

import com.alimamaunion.base.safejson.SafeJSONArray;
import com.alimamaunion.base.safejson.SafeJSONObject;
import com.alimamaunion.common.listpage.CommonItemInfo;
import com.alimamaunion.common.listpage.CommonRecyclerAdapter;
import org.json.JSONObject;

public class HomeSaleBlockItem extends HomeBaseItem {
    public SaleBlockItem mFirstSaleBlockItem;
    private SafeJSONArray mLastData;

    public HomeSaleBlockItem(String str, int i, JSONObject jSONObject) {
        super(str, i, jSONObject);
        this.mLastData = new SafeJSONObject(jSONObject).optJSONArray("data");
        notifyUpdate();
    }

    public void notifyUpdate(int i, CommonRecyclerAdapter commonRecyclerAdapter, CommonItemInfo commonItemInfo) {
        super.notifyUpdate(i, commonRecyclerAdapter, commonItemInfo);
        notifyUpdate();
    }

    public void notifyUpdate() {
        renderServerData();
    }

    private void renderServerData() {
        if (this.mLastData != null && this.mLastData.length() > 0) {
            this.mFirstSaleBlockItem = new SaleBlockItem(this.mLastData.optJSONObject(0));
        }
    }

    public class SaleBlockItem {
        public String img;
        public String src;

        public SaleBlockItem(SafeJSONObject safeJSONObject) {
            if (safeJSONObject != null) {
                this.img = safeJSONObject.optString("img");
                this.src = safeJSONObject.optString("src");
            }
        }
    }
}
