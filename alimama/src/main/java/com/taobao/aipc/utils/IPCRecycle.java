package com.taobao.aipc.utils;

import com.taobao.aipc.core.channel.Channel;
import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class IPCRecycle {
    private static volatile IPCRecycle sInstance;
    private final ReferenceQueue<Object> mReferenceQueue = new ReferenceQueue<>();
    private final ConcurrentHashMap<PhantomReference<Object>, String> mTimeStamps = new ConcurrentHashMap<>();

    private IPCRecycle() {
    }

    public static IPCRecycle getInstance() {
        if (sInstance == null) {
            synchronized (IPCRecycle.class) {
                if (sInstance == null) {
                    sInstance = new IPCRecycle();
                }
            }
        }
        return sInstance;
    }

    private void recycle() {
        ArrayList arrayList = new ArrayList();
        synchronized (this.mReferenceQueue) {
            while (true) {
                PhantomReference phantomReference = (PhantomReference) this.mReferenceQueue.poll();
                if (phantomReference != null) {
                    String remove = this.mTimeStamps.remove(phantomReference);
                    if (remove != null) {
                        arrayList.add(remove);
                    }
                }
            }
            while (true) {
            }
        }
        if (!arrayList.isEmpty()) {
            Channel.getInstance().recycle(arrayList);
        }
    }

    public void register(Object obj, String str) {
        recycle();
        this.mTimeStamps.put(new PhantomReference(obj, this.mReferenceQueue), str);
    }
}
