package com.alimama.moon.network;

import alimama.com.unwnetwork.MtopRequestManagerListener;
import com.taobao.login4android.Login;
import mtopsdk.mtop.domain.MtopResponse;

public class MtopRequestListener implements MtopRequestManagerListener {
    public void onNetworkError(MtopResponse mtopResponse) {
    }

    public void onOtherFailed(MtopResponse mtopResponse) {
    }

    public void onSuccess(MtopResponse mtopResponse) {
    }

    public void onSessionValid(MtopResponse mtopResponse) {
        Login.logout();
    }
}
