package com.alimama.union.app.network.response;

import mtopsdk.mtop.domain.BaseOutDo;

public class SharePasswordGetResponse extends BaseOutDo {
    private SharePasswordGetResponseData data;

    public SharePasswordGetResponseData getData() {
        return this.data;
    }

    public void setData(SharePasswordGetResponseData sharePasswordGetResponseData) {
        this.data = sharePasswordGetResponseData;
    }
}
