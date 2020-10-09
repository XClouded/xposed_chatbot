package com.alibaba.ha.adapter.service.watch;

import com.alibaba.motu.watch.IWatchListener;
import java.util.Map;

public class WatchListenerAdapter implements IWatchListener {
    private WatchListener watchListener = null;

    public WatchListenerAdapter(WatchListener watchListener2) {
        this.watchListener = watchListener2;
    }

    public void onWatch(Map<String, Object> map) {
        if (this.watchListener != null) {
            this.watchListener.onWatch(map);
        }
    }

    public Map<String, String> onCatch() {
        if (this.watchListener != null) {
            return this.watchListener.onCatch();
        }
        return null;
    }

    public Map<String, String> onListener(Map<String, Object> map) {
        if (this.watchListener != null) {
            return this.watchListener.onListener(map);
        }
        return null;
    }
}
