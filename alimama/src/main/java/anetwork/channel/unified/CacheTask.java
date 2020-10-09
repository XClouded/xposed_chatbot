package anetwork.channel.unified;

import android.taobao.windvane.util.WVConstants;
import anet.channel.bytes.ByteArray;
import anet.channel.request.Request;
import anet.channel.statist.RequestStatistic;
import anet.channel.util.ALog;
import anet.channel.util.HttpConstant;
import anetwork.channel.aidl.DefaultFinishEvent;
import anetwork.channel.cache.Cache;
import mtopsdk.common.util.HttpHeaderConstant;

public class CacheTask implements IUnifiedTask {
    private static final String TAG = "anet.CacheTask";
    private Cache cache = null;
    private volatile boolean isCanceled = false;
    private RequestContext rc = null;

    public CacheTask(RequestContext requestContext, Cache cache2) {
        this.rc = requestContext;
        this.cache = cache2;
    }

    public void cancel() {
        this.isCanceled = true;
        this.rc.config.rs.ret = 2;
    }

    public void run() {
        Cache.Entry entry;
        boolean z;
        if (!this.isCanceled) {
            RequestStatistic requestStatistic = this.rc.config.rs;
            if (this.cache != null) {
                String urlString = this.rc.config.getUrlString();
                Request awcnRequest = this.rc.config.getAwcnRequest();
                String str = awcnRequest.getHeaders().get(HttpConstant.CACHE_CONTROL);
                boolean equals = "no-store".equals(str);
                long currentTimeMillis = System.currentTimeMillis();
                if (equals) {
                    this.cache.remove(urlString);
                    z = false;
                    entry = null;
                } else {
                    z = HttpHeaderConstant.NO_CACHE.equals(str);
                    Cache.Entry entry2 = this.cache.get(urlString);
                    if (ALog.isPrintLog(2)) {
                        String str2 = this.rc.seqNum;
                        Object[] objArr = new Object[8];
                        objArr[0] = "hit";
                        objArr[1] = Boolean.valueOf(entry2 != null);
                        objArr[2] = "cost";
                        objArr[3] = Long.valueOf(requestStatistic.cacheTime);
                        objArr[4] = "length";
                        objArr[5] = Integer.valueOf(entry2 != null ? entry2.data.length : 0);
                        objArr[6] = "key";
                        objArr[7] = urlString;
                        ALog.i(TAG, "read cache", str2, objArr);
                    }
                    entry = entry2;
                }
                long currentTimeMillis2 = System.currentTimeMillis();
                requestStatistic.cacheTime = currentTimeMillis2 - currentTimeMillis;
                if (entry == null || z || !entry.isFresh()) {
                    if (!this.isCanceled) {
                        NetworkTask networkTask = new NetworkTask(this.rc, equals ? null : this.cache, entry);
                        this.rc.runningTask = networkTask;
                        networkTask.run();
                    }
                } else if (this.rc.isDone.compareAndSet(false, true)) {
                    this.rc.cancelTimeoutTask();
                    requestStatistic.ret = 1;
                    requestStatistic.statusCode = 200;
                    requestStatistic.msg = "SUCCESS";
                    requestStatistic.protocolType = "cache";
                    requestStatistic.rspEnd = currentTimeMillis2;
                    requestStatistic.processTime = currentTimeMillis2 - requestStatistic.start;
                    if (ALog.isPrintLog(2)) {
                        ALog.i(TAG, "hit fresh cache", this.rc.seqNum, WVConstants.INTENT_EXTRA_URL, this.rc.config.getHttpUrl().urlString());
                    }
                    this.rc.callback.onResponseCode(200, entry.responseHeaders);
                    this.rc.callback.onDataReceiveSize(1, entry.data.length, ByteArray.wrap(entry.data));
                    this.rc.callback.onFinish(new DefaultFinishEvent(200, "SUCCESS", awcnRequest));
                }
            }
        }
    }
}
