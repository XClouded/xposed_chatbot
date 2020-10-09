package com.alimama.union.app.rxnetwork;

import android.text.TextUtils;
import com.alimamaunion.base.safejson.SafeJSONObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public abstract class RxMtopRequest<T> {
    public static final int AFTER_REQ = 1;
    public static final int BEFOR_REQ = 0;
    private static final String TAG = "RxMtopRequest";
    /* access modifiers changed from: private */
    public boolean isEnableCache = false;
    private boolean isEnablePost = false;
    private ApiInfo mApiInfo;
    /* access modifiers changed from: private */
    public String mCacheKey;
    /* access modifiers changed from: private */
    public int mCacheStrategy = 0;
    /* access modifiers changed from: private */
    public int mCacheTimeSeconds;
    private CompositeSubscription mCompositeSubscription;
    protected Map<String, String> mParams = new HashMap();
    protected RxMtopResult<T> mResultListener;

    public interface RxMtopResult<T> {
        void result(RxMtopResponse<T> rxMtopResponse);
    }

    public abstract T decodeResult(SafeJSONObject safeJSONObject);

    public RxMtopRequest setApiInfo(ApiInfo apiInfo) {
        this.mApiInfo = apiInfo;
        return this;
    }

    public ApiInfo getApiInfo() {
        return this.mApiInfo;
    }

    public RxMtopRequest setParams(Map<String, String> map) {
        if (map != null) {
            this.mParams.putAll(map);
        }
        return this;
    }

    public RxMtopRequest appendParam(String str, String str2) {
        this.mParams.put(str, str2);
        return this;
    }

    public RxMtopRequest appendParam(Map<String, String> map) {
        this.mParams.putAll(map);
        return this;
    }

    public RxMtopRequest setAbSpmParam(String str) {
        return appendParam("abSpm", str);
    }

    public Map<String, String> getParams() {
        return this.mParams;
    }

    public RxMtopRequest setEnableCache(boolean z) {
        this.isEnableCache = z;
        return this;
    }

    public RxMtopRequest setCacheTime(int i) {
        this.mCacheTimeSeconds = i;
        return this;
    }

    public RxMtopRequest setCacheStrategy(int i) {
        if (i == 0 || i == 1) {
            this.mCacheStrategy = i;
            return this;
        }
        throw new RuntimeException();
    }

    public RxMtopRequest setCacheKey(String str) {
        this.mCacheKey = str;
        return this;
    }

    public RxMtopRequest enablePost(boolean z) {
        this.isEnablePost = z;
        return this;
    }

    public boolean isEnablePost() {
        return this.isEnablePost;
    }

    public void sendRequest() {
        sendRequest(this.mResultListener);
    }

    public void sendRequest(RxMtopResult<T> rxMtopResult) {
        this.mResultListener = rxMtopResult;
        Subscription subscribe = innerMtopRequest().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<RxMtopResponse<T>>() {
            public void onCompleted() {
            }

            public void onError(Throwable th) {
            }

            public void onNext(RxMtopResponse<T> rxMtopResponse) {
                if (RxMtopRequest.this.mResultListener != null) {
                    RxMtopRequest.this.mResultListener.result(rxMtopResponse);
                }
            }
        });
        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = new CompositeSubscription();
        }
        this.mCompositeSubscription.add(subscribe);
    }

    private Observable<RxMtopResponse<T>> innerMtopRequest() {
        return Observable.defer(new Func0<Observable<RxMtopResponse<T>>>() {
            public Observable<RxMtopResponse<T>> call() {
                RxMtopResponse rxMtopResponse = new RxMtopResponse();
                if (RxMtopRequest.this.isEnableCache && TextUtils.isEmpty(RxMtopRequest.this.mCacheKey)) {
                    String unused = RxMtopRequest.this.mCacheKey = RxMtopRequest.this.getDefaultCacheKey();
                }
                if (!RxMtopRequest.this.isEnableCache || RxMtopRequest.this.mCacheStrategy != 0) {
                    RxResponse access$600 = RxMtopRequest.this.doRequest();
                    rxMtopResponse.retCode = access$600.retCode;
                    rxMtopResponse.retMsg = access$600.msg;
                    RxMtopRequest.this.handleMtopResponse(access$600, rxMtopResponse);
                    if (access$600.isReqSuccess && RxMtopRequest.this.isEnableCache && rxMtopResponse.isReqSuccess) {
                        RxDiskRequest.putDataToDisk(RxMtopRequest.this.mCacheKey, access$600.data);
                    }
                } else {
                    RxMtopRequest.this.handleMtopResponse(RxDiskRequest.getDataFromDisk(RxMtopRequest.this.mCacheKey, (long) RxMtopRequest.this.mCacheTimeSeconds), rxMtopResponse);
                }
                return Observable.just(rxMtopResponse);
            }
        });
    }

    /* access modifiers changed from: private */
    public String getDefaultCacheKey() {
        if (this.mParams.size() == 0) {
            return this.mApiInfo.getAPIName();
        }
        ArrayList arrayList = new ArrayList(this.mParams.values());
        Collections.sort(arrayList);
        this.mCacheKey = this.mApiInfo.getAPIName() + "#" + TextUtils.join("#", arrayList);
        return this.mCacheKey;
    }

    /* access modifiers changed from: private */
    public void handleMtopResponse(RxResponse rxResponse, RxMtopResponse<T> rxMtopResponse) {
        if (rxResponse == null) {
            rxMtopResponse.isReqSuccess = false;
            return;
        }
        try {
            if (rxResponse.data == null) {
                rxMtopResponse.result = null;
            } else {
                rxMtopResponse.result = decodeResult(new SafeJSONObject(new String(rxResponse.data)));
            }
            rxMtopResponse.isReqSuccess = rxResponse.isReqSuccess;
        } catch (JSONException unused) {
        }
    }

    public void setRxMtopResult(RxMtopResult<T> rxMtopResult) {
        this.mResultListener = rxMtopResult;
    }

    public void unregister() {
        if (this.mCompositeSubscription != null) {
            this.mCompositeSubscription.unsubscribe();
            this.mCompositeSubscription = null;
        }
    }

    /* access modifiers changed from: private */
    public RxResponse doRequest() {
        return RxRequestManager.getInstance().doSyncRequest(this);
    }
}
