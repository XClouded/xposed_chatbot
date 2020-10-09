package com.alibaba.android.bindingx.core;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.alibaba.android.bindingx.core.internal.ExpressionPair;
import java.util.Map;

public interface IEventInterceptor {
    void performInterceptIfNeeded(@NonNull String str, @NonNull ExpressionPair expressionPair, @NonNull Map<String, Object> map);

    void setInterceptors(@Nullable Map<String, ExpressionPair> map);
}
