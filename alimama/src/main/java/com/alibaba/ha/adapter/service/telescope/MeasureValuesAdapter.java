package com.alibaba.ha.adapter.service.telescope;

import java.util.HashMap;
import java.util.Map;

public class MeasureValuesAdapter {
    public Map<String, Double> measureValues = new HashMap();

    public void setValue(String str, double d) {
        this.measureValues.put(str, Double.valueOf(d));
    }
}
