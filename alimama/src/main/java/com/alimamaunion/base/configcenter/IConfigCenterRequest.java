package com.alimamaunion.base.configcenter;

import java.util.Map;

public interface IConfigCenterRequest {
    void doRequest(Map<String, String> map, String str, EtaoConfigCenter etaoConfigCenter);
}
