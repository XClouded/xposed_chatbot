package com.alibaba.aliweex.adapter;

import com.alibaba.aliweex.IConfigAdapter;

public interface IConfigModuleAdapter extends IConfigAdapter {
    void destroy();

    void registerListener(String[] strArr, IConfigModuleListener iConfigModuleListener);

    void unregisterListener(String[] strArr, IConfigModuleListener iConfigModuleListener);
}
