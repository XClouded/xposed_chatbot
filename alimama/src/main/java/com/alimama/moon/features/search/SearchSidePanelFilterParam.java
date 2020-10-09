package com.alimama.moon.features.search;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class SearchSidePanelFilterParam {
    private static final BigDecimal COMMISSION_MULTIPLIER = new BigDecimal(100);
    @Nullable
    private BigDecimal mMaxCommission;
    @Nullable
    private BigDecimal mMaxPrice;
    @Nullable
    private BigDecimal mMinCommission;
    @Nullable
    private BigDecimal mMinPrice;
    private final List<SearchOptionModel> mSelectedOptions = new ArrayList();

    public List<SearchOptionModel> getSelectedServices() {
        return this.mSelectedOptions;
    }

    public void setMinPrice(@Nullable BigDecimal bigDecimal) {
        this.mMinPrice = bigDecimal;
    }

    public void setMaxPrice(@Nullable BigDecimal bigDecimal) {
        this.mMaxPrice = bigDecimal;
    }

    public void setMinCommission(@Nullable BigDecimal bigDecimal) {
        this.mMinCommission = bigDecimal;
    }

    public void setMaxCommission(@Nullable BigDecimal bigDecimal) {
        this.mMaxCommission = bigDecimal;
    }

    @NonNull
    public String getMinPrice() {
        return this.mMinPrice == null ? "" : this.mMinPrice.toPlainString();
    }

    @NonNull
    public String getMaxPrice() {
        return this.mMaxPrice == null ? "" : this.mMaxPrice.toPlainString();
    }

    @NonNull
    public String getMinCommission() {
        return this.mMinCommission == null ? "" : this.mMinCommission.toPlainString();
    }

    @NonNull
    public String getMaxCommission() {
        return this.mMaxCommission == null ? "" : this.mMaxCommission.toPlainString();
    }

    @NonNull
    public String getMinCommissionParamVal() {
        if (this.mMinCommission == null) {
            return "";
        }
        return this.mMinCommission.multiply(COMMISSION_MULTIPLIER).toPlainString();
    }

    @NonNull
    public String getMaxCommissionParamVal() {
        if (this.mMaxCommission == null) {
            return "";
        }
        return this.mMaxCommission.multiply(COMMISSION_MULTIPLIER).toPlainString();
    }

    public boolean hasFilterOptionsOn() {
        return (this.mMaxCommission == null && this.mMinCommission == null && this.mMaxPrice == null && this.mMinPrice == null && this.mSelectedOptions.isEmpty()) ? false : true;
    }
}
