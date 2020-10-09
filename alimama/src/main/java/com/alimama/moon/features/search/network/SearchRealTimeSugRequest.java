package com.alimama.moon.features.search.network;

import android.net.Uri;
import com.alimama.union.app.rxnetwork.RxHttpRequest;
import com.alimama.union.app.rxnetwork.RxHttpResponse;
import com.alimama.union.app.rxnetwork.RxResponse;
import org.greenrobot.eventbus.EventBus;

public class SearchRealTimeSugRequest extends RxHttpRequest<SearchRealTimeSugResponse> implements RxHttpRequest.RxHttpResult<SearchRealTimeSugResponse> {
    private static final String SEARCH_REALTIME_SUG_API = "https://suggest.taobao.com/sug?code=utf-8&extras=1";

    public SearchRealTimeSugRequest(String str) {
        Uri.Builder buildUpon = Uri.parse(SEARCH_REALTIME_SUG_API).buildUpon();
        buildUpon.appendQueryParameter("q", str);
        setReqUrl(buildUpon.toString());
        setRxHttpResult(this);
    }

    public SearchRealTimeSugResponse decodeResult(RxResponse rxResponse) {
        return new SearchRealTimeSugResponse(rxResponse.obj);
    }

    public void result(RxHttpResponse<SearchRealTimeSugResponse> rxHttpResponse) {
        SearchRealTimeSugEvent searchRealTimeSugEvent = new SearchRealTimeSugEvent();
        searchRealTimeSugEvent.isSuccess = rxHttpResponse.isReqSuccess;
        searchRealTimeSugEvent.dataResult = (SearchRealTimeSugResponse) rxHttpResponse.result;
        EventBus.getDefault().post(searchRealTimeSugEvent);
    }
}
