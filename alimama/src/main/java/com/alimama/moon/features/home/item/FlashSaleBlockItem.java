package com.alimama.moon.features.home.item;

import com.alimamaunion.base.safejson.SafeJSONObject;
import com.taobao.weex.ui.component.WXEmbed;

public class FlashSaleBlockItem {
    public String itemId;
    public String itemName;
    public String lensId;
    public String picUrl;
    public String srcUrl;
    public String subTkCommissionAmount;
    public VegasFieldItem vegasField;

    public FlashSaleBlockItem(SafeJSONObject safeJSONObject) {
        try {
            this.itemName = safeJSONObject.optString("itemName");
            this.picUrl = safeJSONObject.optString("pic");
            this.srcUrl = safeJSONObject.optString("url");
            this.subTkCommissionAmount = safeJSONObject.optString("subTkCommissionAmount");
            this.itemId = safeJSONObject.optString(WXEmbed.ITEM_ID);
            this.lensId = safeJSONObject.optString("lensId");
            if (safeJSONObject.has("vegasFields")) {
                this.vegasField = new VegasFieldItem(safeJSONObject.optJSONObject("vegasFields"));
            } else {
                this.vegasField = null;
            }
        } catch (Exception unused) {
        }
    }

    public class VegasFieldItem {
        public int itemDrawLimitNum;
        public int itemDrawTotalNum;
        public String priceAfterAllRights;
        public String totalRightsFace;

        public VegasFieldItem(SafeJSONObject safeJSONObject) {
            this.itemDrawLimitNum = safeJSONObject.optInt("itemDrawLimitNum");
            this.totalRightsFace = safeJSONObject.optString("totalRightsFace");
            this.priceAfterAllRights = safeJSONObject.optString("priceAfterAllRights");
            this.itemDrawTotalNum = safeJSONObject.optInt("itemDrawTotalNum");
        }
    }
}
