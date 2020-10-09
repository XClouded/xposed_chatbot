package com.taobao.phenix.intf.event;

import com.taobao.phenix.intf.PhenixTicket;

public class PhenixEvent {
    protected PhenixTicket ticket;
    String url;

    public PhenixEvent(PhenixTicket phenixTicket) {
        this.ticket = phenixTicket;
    }

    public PhenixEvent(String str, PhenixTicket phenixTicket) {
        this.url = str;
        this.ticket = phenixTicket;
    }

    public PhenixTicket getTicket() {
        return this.ticket;
    }

    public void setTicket(PhenixTicket phenixTicket) {
        this.ticket = phenixTicket;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String str) {
        this.url = str;
    }
}
