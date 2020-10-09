package com.alibaba.ha.adapter.service.watch;

import com.alibaba.motu.watch.MotuWatch;

public class WatchService {
    public void addWatchListener(WatchListener watchListener) {
        MotuWatch.getInstance().setMyWatchListenerList(new WatchListenerAdapter(watchListener));
    }
}
