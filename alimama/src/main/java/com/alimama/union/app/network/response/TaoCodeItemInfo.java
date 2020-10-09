package com.alimama.union.app.network.response;

import android.text.TextUtils;
import com.alimamaunion.base.safejson.SafeJSONObject;
import com.taobao.weex.ui.component.WXEmbed;
import mtopsdk.mtop.domain.IMTOPDataObject;

public class TaoCodeItemInfo implements IMTOPDataObject {
    private Double afterCouponAmount;
    private Double couponAmount;
    private String itemId;
    private Double money;
    private String msg;
    private String own = "0";
    private String pictUrl;
    private Double price;
    private String rawUrl;
    private String srcPid;
    public TaoTokenMarketInfoDTO taoTokenMarketInfoDTO;
    private String title;

    public TaoCodeItemInfo fromJson(SafeJSONObject safeJSONObject) {
        this.itemId = safeJSONObject.optString(WXEmbed.ITEM_ID);
        this.money = Double.valueOf(safeJSONObject.optDouble("money"));
        this.price = Double.valueOf(safeJSONObject.optDouble("price"));
        this.couponAmount = Double.valueOf(safeJSONObject.optDouble("couponAmount"));
        this.afterCouponAmount = Double.valueOf(safeJSONObject.optDouble("afterCouponAmount"));
        this.pictUrl = safeJSONObject.optString("pictUrl");
        this.rawUrl = safeJSONObject.getWrappedJsonObject().isNull("rawUrl") ? null : safeJSONObject.optString("rawUrl");
        this.title = safeJSONObject.optString("title");
        this.srcPid = safeJSONObject.optString("srcPid");
        this.own = safeJSONObject.optString("own", "0");
        this.msg = safeJSONObject.optString("msg");
        SafeJSONObject optJSONObject = safeJSONObject.optJSONObject("taoTokenMarketInfoDTO");
        if (optJSONObject != null) {
            TaoTokenMarketInfoDTO taoTokenMarketInfoDTO2 = new TaoTokenMarketInfoDTO();
            taoTokenMarketInfoDTO2.itemMatch = TextUtils.equals(optJSONObject.optString("itemMatch"), "true");
            taoTokenMarketInfoDTO2.title = optJSONObject.optString("title");
            taoTokenMarketInfoDTO2.subtitle = optJSONObject.optString("subtitle");
            taoTokenMarketInfoDTO2.itemListUrl = optJSONObject.optString("itemListUrl");
            SafeJSONObject optJSONObject2 = optJSONObject.optJSONObject("taoTokenMarketItemDTO");
            if (optJSONObject2 != null) {
                TaoTokenMarketItemDTO taoTokenMarketItemDTO = new TaoTokenMarketItemDTO();
                taoTokenMarketItemDTO.commissionAmount = optJSONObject2.optString("commissionAmount");
                taoTokenMarketItemDTO.pictUrl = optJSONObject2.optString("pictUrl");
                taoTokenMarketItemDTO.price = optJSONObject2.optString("price");
                taoTokenMarketItemDTO.priceAfterAllRights = optJSONObject2.optString("priceAfterAllRights");
                taoTokenMarketItemDTO.title = optJSONObject2.optString("title");
                taoTokenMarketItemDTO.url = optJSONObject2.optString("url");
                taoTokenMarketItemDTO.rightsFace = optJSONObject2.optString("rightsFace");
                taoTokenMarketItemDTO.couponAmount = optJSONObject2.optString("couponAmount");
                taoTokenMarketInfoDTO2.taoTokenMarketItemDTO = taoTokenMarketItemDTO;
            }
            this.taoTokenMarketInfoDTO = taoTokenMarketInfoDTO2;
        }
        return this;
    }

    public boolean isTaoTokenMarketInfoDTONull() {
        if (this.taoTokenMarketInfoDTO == null || this.taoTokenMarketInfoDTO.taoTokenMarketItemDTO == null) {
            return true;
        }
        if (!this.taoTokenMarketInfoDTO.isTaoTokenMarketInfoNull() || !this.taoTokenMarketInfoDTO.taoTokenMarketItemDTO.isTaoTokenMarketItemNull()) {
            return false;
        }
        return true;
    }

    public String getItemId() {
        return this.itemId;
    }

    public void setItemId(String str) {
        this.itemId = str;
    }

    public Double getMoney() {
        return this.money;
    }

    public void setMoney(Double d) {
        this.money = d;
    }

    public String getPictUrl() {
        return this.pictUrl;
    }

    public void setPictUrl(String str) {
        this.pictUrl = str;
    }

    public String getRawUrl() {
        return this.rawUrl;
    }

    public void setRawUrl(String str) {
        this.rawUrl = str;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public String getSrcPid() {
        return this.srcPid;
    }

    public void setSrcPid(String str) {
        this.srcPid = str;
    }

    public String getOwn() {
        return this.own;
    }

    public void setOwn(String str) {
        this.own = str;
    }

    public void setPrice(Double d) {
        this.price = d;
    }

    public Double getPrice() {
        return this.price;
    }

    public void setCouponAmount(Double d) {
        this.couponAmount = d;
    }

    public void setAfterCouponAmount(Double d) {
        this.afterCouponAmount = d;
    }

    public Double getCouponAmount() {
        return this.couponAmount;
    }

    public Double getAfterCouponAmount() {
        return this.afterCouponAmount;
    }

    public boolean isSharedFromUser() {
        return "1".equals(this.own);
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String str) {
        this.msg = str;
    }

    public class TaoTokenMarketInfoDTO {
        public String itemListUrl;
        public boolean itemMatch;
        public String subtitle;
        public TaoTokenMarketItemDTO taoTokenMarketItemDTO;
        public String title;

        public TaoTokenMarketInfoDTO() {
        }

        public boolean isTaoTokenMarketInfoNull() {
            return TextUtils.isEmpty(this.title) && TextUtils.isEmpty(this.subtitle) && TextUtils.isEmpty(this.itemListUrl);
        }
    }

    public class TaoTokenMarketItemDTO {
        public String commissionAmount;
        public String couponAmount;
        public String pictUrl;
        public String price;
        public String priceAfterAllRights;
        public String rightsFace;
        public String title;
        public String url;

        public TaoTokenMarketItemDTO() {
        }

        public boolean isTaoTokenMarketItemNull() {
            return TextUtils.isEmpty(this.commissionAmount) && TextUtils.isEmpty(this.price) && TextUtils.isEmpty(this.priceAfterAllRights) && TextUtils.isEmpty(this.couponAmount);
        }
    }
}
