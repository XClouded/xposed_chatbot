package com.taobao.weex.analyzer;

import androidx.annotation.NonNull;

public interface IPermissionHandler {
    boolean isPermissionGranted(@NonNull Config config);
}
