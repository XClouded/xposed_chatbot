package com.taobao.aipc.utils;

import androidx.core.util.Pair;
import com.taobao.aipc.core.channel.Channel;
import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class IPCCallbackRecycle {
    private static volatile IPCCallbackRecycle sInstance;
    private final ReferenceQueue<Object> mReferenceQueue = new ReferenceQueue<>();
    private final ConcurrentHashMap<PhantomReference<Object>, Pair<String, Integer>> mTimeStamps = new ConcurrentHashMap<>();

    private IPCCallbackRecycle() {
    }

    public static IPCCallbackRecycle getInstance() {
        if (sInstance == null) {
            synchronized (IPCCallbackRecycle.class) {
                if (sInstance == null) {
                    sInstance = new IPCCallbackRecycle();
                }
            }
        }
        return sInstance;
    }

    private void recycle() {
        Pair pair = new Pair(new ArrayList(), new ArrayList());
        synchronized (this.mReferenceQueue) {
            while (true) {
                PhantomReference phantomReference = (PhantomReference) this.mReferenceQueue.poll();
                if (phantomReference != null) {
                    Pair remove = this.mTimeStamps.remove(phantomReference);
                    if (remove != null) {
                        ((ArrayList) pair.first).add(remove.first);
                        ((ArrayList) pair.second).add(remove.second);
                    }
                }
            }
            while (true) {
            }
        }
        if (!((ArrayList) pair.first).isEmpty() && !((ArrayList) pair.second).isEmpty()) {
            Channel.getInstance().recycle((List) pair.first, (ArrayList) pair.second);
        }
    }

    public void register(Object obj, String str, int i) {
        recycle();
        this.mTimeStamps.put(new PhantomReference(obj, this.mReferenceQueue), new Pair(str, Integer.valueOf(i)));
    }
}
