package alimama.com.unwnetwork;

import mtopsdk.mtop.domain.MtopResponse;

public interface MtopRequestManagerListener {
    void onNetworkError(MtopResponse mtopResponse);

    void onOtherFailed(MtopResponse mtopResponse);

    void onSessionValid(MtopResponse mtopResponse);

    void onSuccess(MtopResponse mtopResponse);
}
