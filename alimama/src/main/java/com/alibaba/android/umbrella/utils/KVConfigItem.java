package com.alibaba.android.umbrella.utils;

import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.taobao.orange.OrangeConfig;

public class KVConfigItem<T> {
    @NonNull
    private final IConfigItemConverter<T> converter;
    @NonNull
    private final String groupName;
    @NonNull
    private final String initRawValue;
    @NonNull
    private final String key;
    @Nullable
    private String remoteRawValue = null;
    @Nullable
    private T value = null;

    public KVConfigItem(@NonNull String str, @NonNull String str2, @NonNull String str3, @NonNull IConfigItemConverter<T> iConfigItemConverter) {
        this.groupName = str;
        this.key = str2;
        this.initRawValue = str3;
        this.converter = iConfigItemConverter;
        refreshValue();
    }

    public void refreshValue() {
        performSetRawValue(this.initRawValue);
    }

    public void refreshWithDefaultValue(@NonNull String str) {
        if (TextUtils.isEmpty(str)) {
            str = this.initRawValue;
        }
        performSetRawValue(str);
    }

    private void performSetRawValue(@NonNull String str) {
        this.remoteRawValue = OrangeConfig.getInstance().getConfig(this.groupName, this.key, str);
        this.value = null;
    }

    @Nullable
    public final T getValue() {
        if (this.value != null) {
            return this.value;
        }
        this.value = this.converter.convert(this.remoteRawValue);
        return this.value;
    }
}
