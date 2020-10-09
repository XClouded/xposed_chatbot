package com.taobao.android;

import com.taobao.phenix.intf.PhenixTicket;

public class PhenixTicketAdapter implements AliImageTicketInterface {
    private final PhenixTicket mPhenixTicket;

    public PhenixTicketAdapter(PhenixTicket phenixTicket) {
        this.mPhenixTicket = phenixTicket;
    }

    public void setUrl(String str) {
        this.mPhenixTicket.setUrl(str);
    }

    public boolean isDone() {
        return this.mPhenixTicket.isDone();
    }

    public void setDone(boolean z) {
        this.mPhenixTicket.setDone(z);
    }

    public boolean theSame(String str) {
        return this.mPhenixTicket.theSame(str);
    }

    public boolean isDownloading() {
        return this.mPhenixTicket.isDownloading();
    }

    public boolean cancel() {
        return this.mPhenixTicket.cancel();
    }
}
