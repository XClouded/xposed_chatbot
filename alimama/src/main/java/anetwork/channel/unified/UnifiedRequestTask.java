package anetwork.channel.unified;

import android.os.Looper;
import android.taobao.windvane.util.WVConstants;
import android.text.TextUtils;
import anet.channel.appmonitor.AppMonitor;
import anet.channel.monitor.BandWidthSampler;
import anet.channel.request.FutureCancelable;
import anet.channel.request.Request;
import anet.channel.statist.ExceptionStatistic;
import anet.channel.statist.RequestStatistic;
import anet.channel.thread.ThreadPoolExecutorFactory;
import anet.channel.util.ALog;
import anet.channel.util.ErrorConstant;
import anet.channel.util.HttpUrl;
import anetwork.channel.aidl.DefaultFinishEvent;
import anetwork.channel.cache.Cache;
import anetwork.channel.cache.CacheManager;
import anetwork.channel.config.NetworkConfigCenter;
import anetwork.channel.entity.Repeater;
import anetwork.channel.entity.RequestConfig;
import anetwork.channel.interceptor.Callback;
import anetwork.channel.interceptor.Interceptor;
import anetwork.channel.interceptor.InterceptorManager;
import anetwork.channel.util.RequestConstant;
import com.ta.audid.store.UtdidContentBuilder;
import com.taobao.weex.el.parse.Operators;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

class UnifiedRequestTask {
    private static final String TAG = "anet.UnifiedRequestTask";
    protected RequestContext rc;

    public UnifiedRequestTask(RequestConfig requestConfig, Repeater repeater) {
        repeater.setSeqNo(requestConfig.seqNo);
        this.rc = new RequestContext(requestConfig, repeater);
    }

    class UnifiedRequestChain implements Interceptor.Chain {
        private Callback callback = null;
        private int index = 0;
        private Request request = null;

        UnifiedRequestChain(int i, Request request2, Callback callback2) {
            this.index = i;
            this.request = request2;
            this.callback = callback2;
        }

        public Request request() {
            return this.request;
        }

        public Callback callback() {
            return this.callback;
        }

        public Future proceed(Request request2, Callback callback2) {
            if (UnifiedRequestTask.this.rc.isDone.get()) {
                ALog.i(UnifiedRequestTask.TAG, "request canneled or timeout in processing interceptor", request2.getSeq(), new Object[0]);
                return null;
            } else if (this.index < InterceptorManager.getSize()) {
                return InterceptorManager.getInterceptor(this.index).intercept(new UnifiedRequestChain(this.index + 1, request2, callback2));
            } else {
                UnifiedRequestTask.this.rc.config.setAwcnRequest(request2);
                UnifiedRequestTask.this.rc.callback = callback2;
                Cache cache = NetworkConfigCenter.isHttpCacheEnable() ? CacheManager.getCache(UnifiedRequestTask.this.rc.config.getUrlString(), UnifiedRequestTask.this.rc.config.getHeaders()) : null;
                UnifiedRequestTask.this.rc.runningTask = cache != null ? new CacheTask(UnifiedRequestTask.this.rc, cache) : new NetworkTask(UnifiedRequestTask.this.rc, (Cache) null, (Cache.Entry) null);
                UnifiedRequestTask.this.rc.runningTask.run();
                UnifiedRequestTask.this.commitTimeoutTask();
                return null;
            }
        }
    }

    public Future request() {
        long currentTimeMillis = System.currentTimeMillis();
        this.rc.config.rs.reqServiceTransmissionEnd = currentTimeMillis;
        this.rc.config.rs.start = currentTimeMillis;
        this.rc.config.rs.isReqSync = this.rc.config.isSyncRequest();
        this.rc.config.rs.isReqMain = Looper.myLooper() == Looper.getMainLooper();
        try {
            this.rc.config.rs.netReqStart = Long.valueOf(this.rc.config.getRequestProperty(RequestConstant.KEY_REQ_START)).longValue();
        } catch (Exception unused) {
        }
        String requestProperty = this.rc.config.getRequestProperty(RequestConstant.KEY_TRACE_ID);
        if (!TextUtils.isEmpty(requestProperty)) {
            this.rc.config.rs.traceId = requestProperty;
        }
        ALog.e(TAG, "[traceId:" + requestProperty + Operators.ARRAY_END_STR + "start", this.rc.seqNum, "url", this.rc.config.getUrlString());
        if (NetworkConfigCenter.isUrlInDegradeList(this.rc.config.getHttpUrl())) {
            DegradeTask degradeTask = new DegradeTask(this.rc);
            this.rc.runningTask = degradeTask;
            degradeTask.cancelable = new FutureCancelable(ThreadPoolExecutorFactory.submitBackupTask(new Runnable() {
                public void run() {
                    UnifiedRequestTask.this.rc.runningTask.run();
                }
            }), this.rc.config.getAwcnRequest().getSeq());
            commitTimeoutTask();
            return new FutureResponse(this);
        }
        ThreadPoolExecutorFactory.submitPriorityTask(new Runnable() {
            public void run() {
                new UnifiedRequestChain(0, UnifiedRequestTask.this.rc.config.getAwcnRequest(), UnifiedRequestTask.this.rc.callback).proceed(UnifiedRequestTask.this.rc.config.getAwcnRequest(), UnifiedRequestTask.this.rc.callback);
            }
        }, ThreadPoolExecutorFactory.Priority.HIGH);
        return new FutureResponse(this);
    }

    /* access modifiers changed from: private */
    public void commitTimeoutTask() {
        this.rc.timeoutTask = ThreadPoolExecutorFactory.submitScheduledTask(new Runnable() {
            public void run() {
                if (UnifiedRequestTask.this.rc.isDone.compareAndSet(false, true)) {
                    RequestStatistic requestStatistic = UnifiedRequestTask.this.rc.config.rs;
                    if (requestStatistic.isDone.compareAndSet(false, true)) {
                        requestStatistic.statusCode = ErrorConstant.ERROR_REQUEST_TIME_OUT;
                        requestStatistic.msg = ErrorConstant.getErrMsg(ErrorConstant.ERROR_REQUEST_TIME_OUT);
                        requestStatistic.rspEnd = System.currentTimeMillis();
                        ALog.e(UnifiedRequestTask.TAG, "task time out", UnifiedRequestTask.this.rc.seqNum, UtdidContentBuilder.TYPE_RS, requestStatistic);
                        AppMonitor.getInstance().commitStat(new ExceptionStatistic(ErrorConstant.ERROR_REQUEST_TIME_OUT, (String) null, requestStatistic, (Throwable) null));
                    }
                    UnifiedRequestTask.this.rc.cancelRunningTask();
                    UnifiedRequestTask.this.rc.callback.onFinish(new DefaultFinishEvent((int) ErrorConstant.ERROR_REQUEST_TIME_OUT, (String) null, UnifiedRequestTask.this.rc.config.getAwcnRequest()));
                }
            }
        }, (long) this.rc.config.getWaitTimeout(), TimeUnit.MILLISECONDS);
    }

    /* access modifiers changed from: package-private */
    public void cancelTask() {
        if (this.rc.isDone.compareAndSet(false, true)) {
            HttpUrl httpUrl = this.rc.config.getHttpUrl();
            ALog.e(TAG, "task cancelled", this.rc.seqNum, WVConstants.INTENT_EXTRA_URL, httpUrl.simpleUrlString());
            RequestStatistic requestStatistic = this.rc.config.rs;
            if (requestStatistic.isDone.compareAndSet(false, true)) {
                requestStatistic.ret = 2;
                requestStatistic.statusCode = ErrorConstant.ERROR_REQUEST_CANCEL;
                requestStatistic.msg = ErrorConstant.getErrMsg(ErrorConstant.ERROR_REQUEST_CANCEL);
                requestStatistic.rspEnd = System.currentTimeMillis();
                AppMonitor.getInstance().commitStat(new ExceptionStatistic(ErrorConstant.ERROR_REQUEST_CANCEL, (String) null, requestStatistic, (Throwable) null));
                if (requestStatistic.recDataSize > 102400) {
                    BandWidthSampler.getInstance().onDataReceived(requestStatistic.sendStart, requestStatistic.rspEnd, requestStatistic.recDataSize);
                }
            }
            this.rc.cancelRunningTask();
            this.rc.cancelTimeoutTask();
            this.rc.callback.onFinish(new DefaultFinishEvent((int) ErrorConstant.ERROR_REQUEST_CANCEL, (String) null, this.rc.config.getAwcnRequest()));
        }
    }
}
