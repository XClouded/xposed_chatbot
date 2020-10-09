package com.alibaba.android.prefetchx.core.image;

import com.taobao.uikit.feature.features.FeatureFactory;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImageConfigDO implements Serializable {
    private static final long serialVersionUID = 2364807330758417984L;
    public List<String> constantImageUrl = new ArrayList();
    public int count = 5;
    public int denominator = 1;
    public List<String> keyList = new ArrayList();
    public Map<String, Integer> keySize = new HashMap();
    public String urlKey;
    public int viewport = FeatureFactory.PRIORITY_ABOVE_NORMAL;

    public String getUrlKey() {
        return this.urlKey;
    }

    public void setUrlKey(String str) {
        this.urlKey = str;
    }

    public int getCount() {
        return Math.max(this.count, 0);
    }

    public void setCount(int i) {
        this.count = i;
    }

    public int getDenominator() {
        return Math.max(this.denominator, 1);
    }

    public void setDenominator(int i) {
        this.denominator = i;
    }

    public List<String> getKeyList() {
        return this.keyList;
    }

    public void setKeyList(List<String> list) {
        this.keyList = list;
    }

    public Map<String, Integer> getKeySize() {
        return this.keySize;
    }

    public void setKeySize(Map<String, Integer> map) {
        this.keySize = map;
    }

    public List<String> getConstantImageUrl() {
        return this.constantImageUrl;
    }

    public void setConstantImageUrl(List<String> list) {
        this.constantImageUrl = list;
    }

    public int getViewport() {
        return this.viewport;
    }

    public void setViewport(int i) {
        this.viewport = i;
    }
}
