package com.alibaba.android.umbrella.utils.converter;

import android.text.TextUtils;
import androidx.annotation.NonNull;
import com.alibaba.android.umbrella.utils.IConfigItemConverter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class StringListConfigConverter implements IConfigItemConverter<List<String>> {
    @NonNull
    public List<String> convert(String str) {
        if (TextUtils.isEmpty(str)) {
            return Collections.emptyList();
        }
        String[] split = str.split(",");
        if (split.length == 0) {
            return Collections.emptyList();
        }
        return Arrays.asList(split);
    }
}
