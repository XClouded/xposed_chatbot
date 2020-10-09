package com.alibaba.android.umbrella.link.export;

import androidx.annotation.Nullable;
import com.alibaba.android.umbrella.link.UMStringUtils;
import java.util.HashMap;
import java.util.Map;

public final class UMUserData {
    private static final String KEY_MSG = "msg";
    private String msg = "";
    private final Map<String, Object> userArgs = new HashMap();

    private UMUserData() {
    }

    public static UMUserData fromMsg(String str) {
        UMUserData uMUserData = new UMUserData();
        uMUserData.msg = str;
        return uMUserData;
    }

    public static UMUserData fromArg(@Nullable String str, @Nullable Object obj) {
        UMUserData uMUserData = new UMUserData();
        if (UMStringUtils.isEmpty(str) || obj == null) {
            return uMUserData;
        }
        uMUserData.userArgs.put(str, obj);
        return uMUserData;
    }

    public static UMUserData fromMap(@Nullable Map<String, ?> map) {
        UMUserData uMUserData = new UMUserData();
        if (map == null || map.isEmpty()) {
            return uMUserData;
        }
        uMUserData.userArgs.putAll(map);
        return uMUserData;
    }

    public UMUserData putArg(@Nullable String str, @Nullable Object obj) {
        if (UMStringUtils.isEmpty(str) || obj == null) {
            return this;
        }
        this.userArgs.put(str, obj);
        return this;
    }

    public Map<String, ?> toUserData() {
        if (!UMStringUtils.isEmpty(this.msg)) {
            this.userArgs.put("msg", this.msg);
        }
        return this.userArgs;
    }
}
