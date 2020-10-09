package com.alimama.union.app.aalogin.model;

import androidx.room.ColumnInfo;

public class Threshold {
    @ColumnInfo(name = "threshold_checkOrderNum")
    private Integer checkOrderNum;
    @ColumnInfo(name = "threshold_minOrderNum")
    private Integer minOrderNum;
    @ColumnInfo(name = "threshold_totalUV")
    private Integer totalUV;
    @ColumnInfo(name = "threshold_validOrderNum")
    private Integer validOrderNum;

    public Integer getMinOrderNum() {
        return this.minOrderNum;
    }

    public void setMinOrderNum(Integer num) {
        this.minOrderNum = num;
    }

    public Integer getValidOrderNum() {
        return this.validOrderNum;
    }

    public void setValidOrderNum(Integer num) {
        this.validOrderNum = num;
    }

    public Integer getCheckOrderNum() {
        return this.checkOrderNum;
    }

    public void setCheckOrderNum(Integer num) {
        this.checkOrderNum = num;
    }

    public Integer getTotalUV() {
        return this.totalUV;
    }

    public void setTotalUV(Integer num) {
        this.totalUV = num;
    }
}
