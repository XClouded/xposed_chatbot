package com.alibaba.motu.watch;

import java.util.Map;

public interface IWatchListener {
    Map<String, String> onCatch();

    Map<String, String> onListener(Map<String, Object> map);

    void onWatch(Map<String, Object> map);
}
