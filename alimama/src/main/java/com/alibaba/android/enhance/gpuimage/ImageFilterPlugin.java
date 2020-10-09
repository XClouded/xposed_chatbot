package com.alibaba.android.enhance.gpuimage;

import android.util.Log;
import com.alibaba.android.WeexEnhance;
import com.alibaba.android.enhance.gpuimage.core.WXFilterModule;
import com.alibaba.android.enhance.gpuimage.core.WXImageFilterComponent;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.common.WXException;
import com.taobao.weex.ui.component.WXComponent;

public class ImageFilterPlugin {
    private ImageFilterPlugin() {
    }

    public static void register() {
        try {
            WXSDKEngine.registerModule("filter", WXFilterModule.class);
            WXSDKEngine.registerComponent("image-filter", (Class<? extends WXComponent>) WXImageFilterComponent.class, false);
        } catch (WXException e) {
            Log.e("image-filter", "init failed", e);
        }
    }

    public static WeexEnhance.ImageLoadAdapter getImageAdapter() {
        return WeexEnhance.getImageAdapter();
    }
}
