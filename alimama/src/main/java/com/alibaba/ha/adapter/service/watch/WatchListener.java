package com.alibaba.ha.adapter.service.watch;

import java.util.Map;

public interface WatchListener {
    Map<String, String> onCatch();

    Map<String, String> onListener(Map<String, Object> map);

    void onWatch(Map<String, Object> map);
}
