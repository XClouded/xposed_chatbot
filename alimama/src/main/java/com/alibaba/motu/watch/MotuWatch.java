package com.alibaba.motu.watch;

import android.content.Context;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public class MotuWatch {
    private static MotuWatch instance;
    private List<IWatchListener> myWatchListenerList = new ArrayList();

    public boolean enableWatch(Context context, String str, Boolean bool) {
        return true;
    }

    private static synchronized MotuWatch initMotuWatch() {
        MotuWatch motuWatch;
        synchronized (MotuWatch.class) {
            if (instance == null) {
                instance = new MotuWatch();
            }
            motuWatch = instance;
        }
        return motuWatch;
    }

    public static MotuWatch getInstance() {
        if (instance == null) {
            initMotuWatch();
        }
        return instance;
    }

    public List<IWatchListener> getMyWatchListenerList() {
        return this.myWatchListenerList;
    }

    public void setMyWatchListenerList(IWatchListener iWatchListener) {
        try {
            if (this.myWatchListenerList != null) {
                this.myWatchListenerList.add(iWatchListener);
            }
        } catch (Exception e) {
            Log.e(WatchConfig.TAG, "set my watch listener err", e);
        }
    }
}
