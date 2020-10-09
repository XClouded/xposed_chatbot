package com.alibaba.android.prefetchx.core.data;

import android.app.Activity;
import android.content.Context;
import com.taobao.weex.bridge.JSCallback;

public class SupportNative {
    public String prefetchData(Activity activity, String str) {
        return PFMtop.getInstance().prefetch((Context) activity, str);
    }

    public String getResult(String str, JSCallback jSCallback) {
        return StorageWeex.getInstance().read(str);
    }

    public void removeResult(String str) {
        StorageWeex.getInstance().remove(str);
    }
}
