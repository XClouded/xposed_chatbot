package com.alibaba.android.prefetchx.adapter;

import com.alibaba.android.prefetchx.core.jsmodule.JSModulePojo;

public interface AssetAdapter {
    String getAssetFromZCache(String str);

    JSModulePojo getJSModuleFromFile(String str);

    String getStringToFile(String str);

    void putJSModuleToFile(String str, JSModulePojo jSModulePojo);

    void putStringToFile(String str, String str2);

    void removeJSModule(String str);
}
