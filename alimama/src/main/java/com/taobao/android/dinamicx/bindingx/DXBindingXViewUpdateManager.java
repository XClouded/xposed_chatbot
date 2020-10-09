package com.taobao.android.dinamicx.bindingx;

import android.view.View;
import androidx.annotation.NonNull;
import com.alibaba.android.bindingx.core.PlatformManager;
import com.alibaba.android.bindingx.plugin.android.INativeViewUpdater;
import com.alibaba.android.bindingx.plugin.android.NativeViewUpdateService;
import java.util.Map;

public class DXBindingXViewUpdateManager implements INativeViewUpdater {
    public void update(@NonNull View view, @NonNull String str, @NonNull Object obj, @NonNull PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator, @NonNull Map<String, Object> map) {
        INativeViewUpdater findUpdater = NativeViewUpdateService.findUpdater(str);
        if (findUpdater != null) {
            findUpdater.update(view, str, obj, iDeviceResolutionTranslator, map);
        }
    }
}
