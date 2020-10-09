package com.alimama.union.app.network.request;

import mtopsdk.mtop.domain.IMTOPDataObject;

public class SharePasswordGetRequest implements IMTOPDataObject {
    private String API_NAME = "mtop.taobao.sharepassword.querypassword";
    private boolean NEED_ECODE = false;
    private boolean NEED_SESSION = true;
    private String VERSION = "1.0";
    private String passwordContent = null;
    private String passwordType = null;

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

    public String getPasswordContent() {
        return this.passwordContent;
    }

    public void setPasswordContent(String str) {
        this.passwordContent = str;
    }

    public String getPasswordType() {
        return this.passwordType;
    }

    public void setPasswordType(String str) {
        this.passwordType = str;
    }
}
