package com.alibaba.android.umbrella.link;

import androidx.annotation.Nullable;
import com.alibaba.android.umbrella.link.export.UMUserData;
import com.alibaba.android.umbrella.link.util.MapUtils;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

final class LinkLogExtData {
    static final String EXT_DATA_MTOP_RESPONSE_HEADERS = "responseHeaders";
    static final String EXT_DATA_MTOP_RESPONSE_PARAMS = "requestParams";
    static final String EXT_DATA_MTOP_RESPONSE_STR = "responseString";
    static final String EXT_DATA_UI_ARGS = "args";
    private static final String EXT_DATA_USER_DATA = "userData";
    private static final int MAX_EXT_LENGTH = 25600;
    private final Map<String, Object> extMap = new ConcurrentHashMap();

    LinkLogExtData() {
    }

    /* access modifiers changed from: package-private */
    public boolean isEmpty() {
        return this.extMap.isEmpty();
    }

    static LinkLogExtData fromUserData(@Nullable UMUserData uMUserData) {
        LinkLogExtData linkLogExtData = new LinkLogExtData();
        if (uMUserData == null) {
            linkLogExtData.putKV(EXT_DATA_USER_DATA, "");
            return linkLogExtData;
        }
        Map<String, ?> userData = uMUserData.toUserData();
        if (userData == null || userData.isEmpty()) {
            linkLogExtData.putKV(EXT_DATA_USER_DATA, "");
            return linkLogExtData;
        }
        MapUtils.safePutAll(linkLogExtData.extMap, userData);
        return linkLogExtData;
    }

    /* access modifiers changed from: package-private */
    public LinkLogExtData putKV(@Nullable String str, @Nullable Object obj) {
        if (UMStringUtils.isEmpty(str)) {
            return this;
        }
        if (obj == null) {
            this.extMap.put(str, "null value");
            return this;
        }
        this.extMap.put(str, obj);
        return this;
    }

    public Map<String, Object> getExtMap() {
        return this.extMap;
    }

    /* access modifiers changed from: package-private */
    public Map<String, Object> toClipExtMap() {
        Object obj;
        if (this.extMap.containsKey(EXT_DATA_MTOP_RESPONSE_STR) && (obj = this.extMap.get(EXT_DATA_MTOP_RESPONSE_STR)) != null) {
            String valueOf = String.valueOf(obj);
            if (valueOf.length() > MAX_EXT_LENGTH) {
                this.extMap.put(EXT_DATA_MTOP_RESPONSE_STR, valueOf.substring(0, MAX_EXT_LENGTH));
            }
        }
        return this.extMap;
    }
}
