package com.alibaba.taffy.core.util.net;

import java.util.HashMap;
import java.util.Map;

public final class URLParseInfo {
    public String base;
    public Map<String, String> fragment = new HashMap();
    public Map<String, String> param = new HashMap();
}
