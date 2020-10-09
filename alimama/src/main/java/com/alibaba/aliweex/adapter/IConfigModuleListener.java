package com.alibaba.aliweex.adapter;

import java.util.Map;

public interface IConfigModuleListener {
    void onConfigUpdate(String str, Map<String, String> map);
}
