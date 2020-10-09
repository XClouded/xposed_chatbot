package com.taobao.phenix.intf;

import com.taobao.rxm.request.RequestContext;

public class PhenixTicket implements IPhenixTicket {
    boolean done = false;
    private RequestContext mRequestContext;
    protected String url = "";

    public PhenixTicket(RequestContext requestContext) {
        this.mRequestContext = requestContext;
    }

    public void setUrl(String str) {
        this.url = str;
    }

    public boolean isDone() {
        return this.done;
    }

    public void setDone(boolean z) {
        this.done = z;
        if (z) {
            this.mRequestContext = null;
        }
    }

    public boolean theSame(String str) {
        return this.url != null && this.url.compareToIgnoreCase(str) == 0;
    }

    public void setRequestContext(RequestContext requestContext) {
        this.mRequestContext = requestContext;
    }

    public boolean isDownloading() {
        RequestContext requestContext = this.mRequestContext;
        return requestContext != null && !requestContext.isCancelledInMultiplex();
    }

    public boolean cancel() {
        RequestContext requestContext;
        synchronized (this) {
            requestContext = this.mRequestContext;
            this.mRequestContext = null;
        }
        if (requestContext == null) {
            return false;
        }
        requestContext.cancel();
        return false;
    }
}
