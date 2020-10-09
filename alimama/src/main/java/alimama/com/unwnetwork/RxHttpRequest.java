package alimama.com.unwnetwork;

import alimama.com.unwbase.UNWManager;
import alimama.com.unwbase.interfaces.IRxRequestManager;
import com.alimamaunion.base.safejson.SafeJSONObject;
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

public abstract class RxHttpRequest<T> {
    /* access modifiers changed from: private */
    public boolean isNeedOriData;
    private boolean isPost;
    private CompositeSubscription mCompositeSubscription;
    protected Map<String, String> mParams = new HashMap();
    /* access modifiers changed from: private */
    public RxHttpResult<T> mRxHttpResult;
    /* access modifiers changed from: private */
    public String mUrl;

    public interface RxHttpResult<T> {
        void result(RxHttpResponse<T> rxHttpResponse);
    }

    public abstract T decodeResult(RxResponse rxResponse);

    public RxHttpRequest setReqUrl(String str) {
        this.mUrl = str;
        return this;
    }

    public RxHttpRequest enablePost() {
        this.isPost = true;
        return this;
    }

    public boolean isPost() {
        return this.isPost;
    }

    public RxHttpRequest setNeedOriData(boolean z) {
        this.isNeedOriData = z;
        return this;
    }

    public RxHttpRequest setParams(Map<String, String> map) {
        this.mParams.putAll(map);
        return this;
    }

    public RxHttpRequest appendParam(String str, String str2) {
        this.mParams.put(str, str2);
        return this;
    }

    public Map<String, String> getParams() {
        return this.mParams;
    }

    public String getUrl() {
        return this.mUrl;
    }

    public RxHttpRequest setRxHttpResult(RxHttpResult<T> rxHttpResult) {
        this.mRxHttpResult = rxHttpResult;
        return this;
    }

    public void sendRequest() {
        sendRequest(this.mRxHttpResult);
    }

    public void sendRequest(RxHttpResult<T> rxHttpResult) {
        this.mRxHttpResult = rxHttpResult;
        Subscription subscribe = innerHttpRequest().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<RxHttpResponse<T>>() {
            public void onCompleted() {
            }

            public void onError(Throwable th) {
            }

            public void onNext(RxHttpResponse<T> rxHttpResponse) {
                if (RxHttpRequest.this.mRxHttpResult != null) {
                    RxHttpRequest.this.mRxHttpResult.result(rxHttpResponse);
                }
            }
        });
        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = new CompositeSubscription();
        }
        this.mCompositeSubscription.add(subscribe);
    }

    /* access modifiers changed from: protected */
    public Observable<RxHttpResponse<T>> innerHttpRequest() {
        return Observable.defer(new Func0<Observable<RxHttpResponse<T>>>() {
            public Observable<RxHttpResponse<T>> call() {
                RxHttpResponse rxHttpResponse = new RxHttpResponse();
                RxResponse access$100 = RxHttpRequest.this.doRequest();
                rxHttpResponse.isReqSuccess = false;
                if (access$100.isReqSuccess) {
                    access$100.url = RxHttpRequest.this.mUrl;
                    if (RxHttpRequest.this.isNeedOriData) {
                        T decodeResult = RxHttpRequest.this.decodeResult(access$100);
                        rxHttpResponse.isReqSuccess = true;
                        rxHttpResponse.result = decodeResult;
                        return Observable.just(rxHttpResponse);
                    }
                    try {
                        access$100.obj = new SafeJSONObject(new String(access$100.oriData));
                        T decodeResult2 = RxHttpRequest.this.decodeResult(access$100);
                        rxHttpResponse.isReqSuccess = true;
                        rxHttpResponse.result = decodeResult2;
                        return Observable.just(rxHttpResponse);
                    } catch (JSONException unused) {
                    }
                }
                rxHttpResponse.isReqSuccess = false;
                return Observable.just(rxHttpResponse);
            }
        });
    }

    public void unregister() {
        if (this.mCompositeSubscription != null) {
            this.mCompositeSubscription.unsubscribe();
            this.mCompositeSubscription = null;
        }
    }

    /* access modifiers changed from: private */
    public RxResponse doRequest() {
        IRxRequestManager iRxRequestManager = (IRxRequestManager) UNWManager.getInstance().getService(IRxRequestManager.class);
        if (iRxRequestManager == null || !(iRxRequestManager instanceof RxRequestManager)) {
            return null;
        }
        return ((RxRequestManager) iRxRequestManager).doSyncHttpRequest(this);
    }
}
