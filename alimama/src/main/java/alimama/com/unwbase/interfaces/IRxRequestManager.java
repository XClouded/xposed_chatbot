package alimama.com.unwbase.interfaces;

import alimama.com.unwbase.net.RxHttpRequest;
import alimama.com.unwbase.net.RxMtopRequest;
import alimama.com.unwbase.net.RxResponse;

public interface IRxRequestManager extends IInitAction {
    RxResponse doSyncHttpRequest(RxHttpRequest rxHttpRequest);

    RxResponse doSyncRequest(RxMtopRequest rxMtopRequest);
}
