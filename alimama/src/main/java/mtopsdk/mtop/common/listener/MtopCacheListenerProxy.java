package mtopsdk.mtop.common.listener;

import mtopsdk.mtop.common.MtopCacheEvent;
import mtopsdk.mtop.common.MtopCallback;
import mtopsdk.mtop.common.MtopListener;

public class MtopCacheListenerProxy extends MtopBaseListenerProxy implements MtopCallback.MtopCacheListener {
    private static final String TAG = "mtopsdk.MtopCacheListenerProxy";

    public MtopCacheListenerProxy(MtopListener mtopListener) {
        super(mtopListener);
    }

    public void onCached(MtopCacheEvent mtopCacheEvent, Object obj) {
        if (this.listener instanceof MtopCallback.MtopCacheListener) {
            ((MtopCallback.MtopCacheListener) this.listener).onCached(mtopCacheEvent, obj);
            this.isCached = true;
        }
    }
}
