package com.ali.user.mobile.filter;

import java.util.Map;

public interface PreHandlerCallback {
    void onFail(int i, Map<String, String> map);

    void onSuccess();
}
