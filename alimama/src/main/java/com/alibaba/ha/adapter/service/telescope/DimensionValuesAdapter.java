package com.alibaba.ha.adapter.service.telescope;

import java.util.HashMap;
import java.util.Map;

public class DimensionValuesAdapter {
    public Map<String, String> dimensionValues = new HashMap();

    public void setValue(String str, String str2) {
        this.dimensionValues.put(str, str2);
    }
}
