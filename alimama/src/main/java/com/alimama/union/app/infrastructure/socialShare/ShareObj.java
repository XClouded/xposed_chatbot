package com.alimama.union.app.infrastructure.socialShare;

import java.util.List;

public class ShareObj {
    private Double amount;
    private Double discountCouponPrice;
    private Double discountPrice;
    private String favShopName;
    private Boolean freeShipping;
    private List<String> images;
    private String materialType;
    private String platform;
    private String text;
    private String title;
    private String url;

    public String getPlatform() {
        return this.platform;
    }

    public void setPlatform(String str) {
        this.platform = str;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String str) {
        this.text = str;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String str) {
        this.url = str;
    }

    public List<String> getImages() {
        return this.images;
    }

    public void setImages(List<String> list) {
        this.images = list;
    }

    public String getMaterialType() {
        return this.materialType;
    }

    public void setMaterialType(String str) {
        this.materialType = str;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public Double getDiscountCouponPrice() {
        return this.discountCouponPrice;
    }

    public void setDiscountCouponPrice(Double d) {
        this.discountCouponPrice = d;
    }

    public Double getDiscountPrice() {
        return this.discountPrice;
    }

    public void setDiscountPrice(Double d) {
        this.discountPrice = d;
    }

    public Boolean getFreeShipping() {
        if (this.freeShipping == null) {
            return Boolean.FALSE;
        }
        return this.freeShipping;
    }

    public void setFreeShipping(Boolean bool) {
        this.freeShipping = bool;
    }

    public Double getAmount() {
        return this.amount;
    }

    public void setAmount(Double d) {
        this.amount = d;
    }

    public String getFavShopName() {
        return this.favShopName;
    }

    public void setFavShopName(String str) {
        this.favShopName = str;
    }
}
