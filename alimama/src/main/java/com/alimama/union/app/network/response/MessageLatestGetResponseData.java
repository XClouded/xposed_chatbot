package com.alimama.union.app.network.response;

import com.alimama.union.app.messageCenter.model.AlertMessage;
import java.util.List;
import mtopsdk.mtop.domain.IMTOPDataObject;

public class MessageLatestGetResponseData implements IMTOPDataObject {
    private List<AlertMessage> result;

    public List<AlertMessage> getResult() {
        return this.result;
    }

    public void setResult(List<AlertMessage> list) {
        this.result = list;
    }
}
