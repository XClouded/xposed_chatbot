package com.alibaba.android.umbrella.link;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public final class UMStringUtils {
    public static boolean isEmpty(@Nullable CharSequence charSequence) {
        return charSequence == null || charSequence.length() == 0;
    }

    public static boolean anyEmpty(@NonNull String... strArr) {
        for (String isEmpty : strArr) {
            if (isEmpty(isEmpty)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNotEmpty(@Nullable CharSequence charSequence) {
        return !isEmpty(charSequence);
    }
}
