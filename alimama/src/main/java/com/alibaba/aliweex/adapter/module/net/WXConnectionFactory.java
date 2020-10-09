package com.alibaba.aliweex.adapter.module.net;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class WXConnectionFactory {
    @Nullable
    public static IWXConnection createDefault(@NonNull Context context) {
        if (context == null) {
            return null;
        }
        return new DefaultWXConnection(context);
    }
}
