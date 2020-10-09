package com.alimama.union.app.network.response;

import mtopsdk.mtop.domain.IMTOPDataObject;

public class MessageReadUpdateResponseData implements IMTOPDataObject {
    private Boolean result;

    public Boolean getResult() {
        return this.result;
    }

    public void setResult(Boolean bool) {
        this.result = bool;
    }
}
