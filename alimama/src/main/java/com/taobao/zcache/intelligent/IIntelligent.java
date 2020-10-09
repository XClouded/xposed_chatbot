package com.taobao.zcache.intelligent;

import java.util.Map;

public interface IIntelligent {
    void commitFirstVisit(Map<String, String> map, Map<String, Double> map2, String str, boolean z);

    void commitUpdate(Map<String, String> map, Map<String, Double> map2);

    boolean isTriggerred();
}
