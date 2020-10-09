package com.alimama.union.app.network.response;

import mtopsdk.mtop.domain.BaseOutDo;

public class ItemcouponTaotokenTaotokeninfoResponse extends BaseOutDo {
    private TaoCodeItemInfo data;

    public TaoCodeItemInfo getData() {
        return this.data;
    }

    public void setData(TaoCodeItemInfo taoCodeItemInfo) {
        this.data = taoCodeItemInfo;
    }
}
