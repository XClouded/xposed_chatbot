package com.alimama.union.app.personalCenter.model;

import androidx.room.ColumnInfo;

public class GradeThreshold {
    @ColumnInfo(name = "threshold_checkOrderNum")
    private Integer checkOrderNum;
    @ColumnInfo(name = "threshold_minValidOrderNum")
    private Integer minValidOrderNum;
    @ColumnInfo(name = "threshold_totalUv")
    private Integer totalUv;
    @ColumnInfo(name = "threshold_validOrderNum")
    private Integer validOrderNum;

    public Integer getCheckOrderNum() {
        return this.checkOrderNum;
    }

    public void setCheckOrderNum(Integer num) {
        this.checkOrderNum = num;
    }

    public Integer getValidOrderNum() {
        return this.validOrderNum;
    }

    public void setValidOrderNum(Integer num) {
        this.validOrderNum = num;
    }

    public Integer getMinValidOrderNum() {
        return this.minValidOrderNum;
    }

    public void setMinValidOrderNum(Integer num) {
        this.minValidOrderNum = num;
    }

    public Integer getTotalUv() {
        return this.totalUv;
    }

    public void setTotalUv(Integer num) {
        this.totalUv = num;
    }
}
