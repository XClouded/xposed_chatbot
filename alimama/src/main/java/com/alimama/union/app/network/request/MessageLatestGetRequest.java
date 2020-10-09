package com.alimama.union.app.network.request;

import mtopsdk.mtop.domain.IMTOPDataObject;

public class MessageLatestGetRequest implements IMTOPDataObject {
    private String API_NAME = "mtop.alimama.moon.provider.message.latest.get";
    private boolean NEED_ECODE = true;
    private boolean NEED_SESSION = true;
    private String VERSION = "1.0";
    private long status = 2;

    public String getAPI_NAME() {
        return this.API_NAME;
    }

    public void setAPI_NAME(String str) {
        this.API_NAME = str;
    }

    public String getVERSION() {
        return this.VERSION;
    }

    public void setVERSION(String str) {
        this.VERSION = str;
    }

    public boolean isNEED_ECODE() {
        return this.NEED_ECODE;
    }

    public void setNEED_ECODE(boolean z) {
        this.NEED_ECODE = z;
    }

    public boolean isNEED_SESSION() {
        return this.NEED_SESSION;
    }

    public void setNEED_SESSION(boolean z) {
        this.NEED_SESSION = z;
    }

    public long getStatus() {
        return this.status;
    }

    public void setStatus(long j) {
        this.status = j;
    }
}
