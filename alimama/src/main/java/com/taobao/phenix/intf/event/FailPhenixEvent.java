package com.taobao.phenix.intf.event;

import com.taobao.phenix.intf.PhenixTicket;

public class FailPhenixEvent extends PhenixEvent {
    int httpCode;
    String httpMessage;
    int resultCode;

    public FailPhenixEvent(PhenixTicket phenixTicket) {
        super(phenixTicket);
    }

    public int getResultCode() {
        return this.resultCode;
    }

    public void setResultCode(int i) {
        this.resultCode = i;
    }

    public int getHttpCode() {
        return this.httpCode;
    }

    public String getHttpMessage() {
        return this.httpMessage;
    }

    public void setHttpCode(int i) {
        this.httpCode = i;
    }

    public void setHttpMessage(String str) {
        this.httpMessage = str;
    }
}
