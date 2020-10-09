package com.alibaba.ha.adapter.service.crash;

import java.util.Map;

public interface JavaCrashListener {
    Map<String, Object> onCrashCaught(Thread thread, Throwable th);
}
