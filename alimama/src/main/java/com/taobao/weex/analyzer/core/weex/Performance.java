package com.taobao.weex.analyzer.core.weex;

import java.util.Map;

public class Performance {
    private Map<String, String> mDimensionMap;
    private Map<String, Double> mMeasureMap;

    public void setMeasureMap(Map<String, Double> map) {
        this.mMeasureMap = map;
    }

    public void setDimensionMap(Map<String, String> map) {
        this.mDimensionMap = map;
    }

    public Map<String, Double> getMeasureMap() {
        return this.mMeasureMap;
    }

    public Map<String, String> getDimensionMap() {
        return this.mDimensionMap;
    }
}
