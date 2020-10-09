package com.ali.alihadeviceevaluator.network;

import mtopsdk.mtop.domain.BaseOutDo;

class MtopTaobaoHaQueryResponse extends BaseOutDo {
    private MtopTaobaoHaQueryResponseData data;

    MtopTaobaoHaQueryResponse() {
    }

    public MtopTaobaoHaQueryResponseData getData() {
        return this.data;
    }

    public void setData(MtopTaobaoHaQueryResponseData mtopTaobaoHaQueryResponseData) {
        this.data = mtopTaobaoHaQueryResponseData;
    }
}
