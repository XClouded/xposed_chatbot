package com.alimama.union.app.infrastructure.image.piccollage;

import androidx.annotation.NonNull;
import java.util.ArrayList;
import java.util.List;

public class CollageModel {
    @NonNull
    private final List<String> collageImageUrls = new ArrayList();
    private Double couponAmount;
    private Double price;
    private Double priceAfterCoupon;
    private String title;
    private String url;

    public CollageModel setTitle(String str) {
        this.title = str;
        return this;
    }

    public CollageModel setUrl(String str) {
        this.url = str;
        return this;
    }

    public CollageModel setPrice(Double d) {
        this.price = d;
        return this;
    }

    public CollageModel setPriceAfterCoupon(Double d) {
        this.priceAfterCoupon = d;
        return this;
    }

    public CollageModel setCouponAmount(Double d) {
        this.couponAmount = d;
        return this;
    }

    public CollageModel setCollageImageUrls(List<String> list) {
        if (list == null) {
            return this;
        }
        this.collageImageUrls.clear();
        this.collageImageUrls.addAll(list);
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    public String getUrl() {
        return this.url;
    }

    public Double getPrice() {
        return this.price;
    }

    public Double getPriceAfterCoupon() {
        return this.priceAfterCoupon;
    }

    public Double getCouponAmount() {
        return this.couponAmount;
    }

    public List<String> getCollageImageUrls() {
        return this.collageImageUrls;
    }
}
