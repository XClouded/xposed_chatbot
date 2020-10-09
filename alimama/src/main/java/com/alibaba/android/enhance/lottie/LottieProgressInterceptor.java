package com.alibaba.android.enhance.lottie;

import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.airbnb.lottie.LottieAnimationView;
import com.alibaba.android.bindingx.core.BindingXPropertyInterceptor;
import com.alibaba.android.bindingx.core.PlatformManager;
import java.util.Map;

public class LottieProgressInterceptor implements BindingXPropertyInterceptor.IPropertyUpdateInterceptor {
    public boolean updateView(@Nullable View view, @NonNull String str, @NonNull Object obj, @NonNull PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator, @NonNull Map<String, Object> map, Object... objArr) {
        if (!"lottie-progress".equals(str) || view == null || !(view instanceof LottieAnimationView)) {
            return false;
        }
        ((LottieAnimationView) view).setProgress((float) ((Double) obj).doubleValue());
        return true;
    }
}
