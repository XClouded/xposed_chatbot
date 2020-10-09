package com.alimama.moon.windvane.jsbridge.model;

public class ShowAvatarDialogModel {
    String cancelBtnTitle;
    String msg;
    String okBtnTitle;
    String pictUrl;
    String title;

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String str) {
        this.msg = str;
    }

    public String getPictUrl() {
        return this.pictUrl;
    }

    public void setPictUrl(String str) {
        this.pictUrl = str;
    }

    public String getCancelBtnTitle() {
        return this.cancelBtnTitle;
    }

    public void setCancelBtnTitle(String str) {
        this.cancelBtnTitle = str;
    }

    public String getOkBtnTitle() {
        return this.okBtnTitle;
    }

    public void setOkBtnTitle(String str) {
        this.okBtnTitle = str;
    }
}
