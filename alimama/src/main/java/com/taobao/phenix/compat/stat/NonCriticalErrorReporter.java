package com.taobao.phenix.compat.stat;

import java.util.Map;

public interface NonCriticalErrorReporter {
    void onNonCriticalErrorHappen(String str, Throwable th, Map<String, Object> map);
}
