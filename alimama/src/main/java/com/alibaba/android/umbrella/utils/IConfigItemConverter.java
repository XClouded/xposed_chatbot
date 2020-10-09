package com.alibaba.android.umbrella.utils;

import androidx.annotation.Nullable;

public interface IConfigItemConverter<T> {
    @Nullable
    T convert(String str);
}
