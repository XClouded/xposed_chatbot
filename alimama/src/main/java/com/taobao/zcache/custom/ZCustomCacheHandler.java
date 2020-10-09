package com.taobao.zcache.custom;

import java.io.InputStream;
import java.util.Map;

public interface ZCustomCacheHandler {
    InputStream loadRequest(String[] strArr, String str, Map<String, String> map, Map<String, String> map2);
}
