package com.taobao.android.ultron.datamodel;

import mtopsdk.mtop.domain.MtopResponse;

public abstract class AbsRequestCallback implements IRequestCallback {
    public boolean isDealDataOuter(int i, MtopResponse mtopResponse, Object obj) {
        return false;
    }
}
