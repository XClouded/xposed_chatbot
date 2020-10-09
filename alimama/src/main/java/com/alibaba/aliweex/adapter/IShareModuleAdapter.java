package com.alibaba.aliweex.adapter;

import android.content.Context;
import com.taobao.weex.bridge.JSCallback;

public interface IShareModuleAdapter {
    void doShare(Context context, String str, JSCallback jSCallback);
}
