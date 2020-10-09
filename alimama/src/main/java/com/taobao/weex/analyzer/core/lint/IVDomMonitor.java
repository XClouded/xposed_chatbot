package com.taobao.weex.analyzer.core.lint;

import androidx.annotation.NonNull;
import com.taobao.weex.WXSDKInstance;

public interface IVDomMonitor {
    void destroy();

    void monitor(@NonNull WXSDKInstance wXSDKInstance);
}
