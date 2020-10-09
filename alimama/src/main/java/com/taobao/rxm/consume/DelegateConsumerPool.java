package com.taobao.rxm.consume;

import com.taobao.rxm.common.Releasable;
import com.taobao.rxm.common.RxModel4Phenix;
import com.taobao.rxm.request.RequestContext;
import com.taobao.tcommon.core.Pool;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DelegateConsumerPool<OUT, NEXT_OUT extends Releasable, CONTEXT extends RequestContext> implements Pool<ChainDelegateConsumer<OUT, NEXT_OUT, CONTEXT>> {
    private static final int DEFAULT_MAX_SIZE = 15;
    private final int mMaxSize;
    private final Queue<ChainDelegateConsumer<OUT, NEXT_OUT, CONTEXT>> mRecycledQueue;

    public DelegateConsumerPool() {
        this(15);
    }

    public DelegateConsumerPool(int i) {
        this.mMaxSize = i;
        this.mRecycledQueue = new ConcurrentLinkedQueue();
    }

    public ChainDelegateConsumer<OUT, NEXT_OUT, CONTEXT> offer() {
        if (RxModel4Phenix.isUseRecycle()) {
            return this.mRecycledQueue.poll();
        }
        return null;
    }

    public boolean recycle(ChainDelegateConsumer<OUT, NEXT_OUT, CONTEXT> chainDelegateConsumer) {
        if (chainDelegateConsumer != null) {
            chainDelegateConsumer.reset();
        }
        if (!RxModel4Phenix.isUseRecycle() || this.mRecycledQueue.size() >= this.mMaxSize || !this.mRecycledQueue.offer(chainDelegateConsumer)) {
            return false;
        }
        return true;
    }
}
