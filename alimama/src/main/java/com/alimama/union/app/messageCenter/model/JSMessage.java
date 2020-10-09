package com.alimama.union.app.messageCenter.model;

public class JSMessage {
    private String button;
    private String message;
    private Long msgId;
    private Integer msgType;
    private String title;
    private String url;

    public Integer getMsgType() {
        return this.msgType;
    }

    public void setMsgType(Integer num) {
        this.msgType = num;
    }

    public Long getMsgId() {
        return this.msgId;
    }

    public void setMsgId(Long l) {
        this.msgId = l;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String str) {
        this.message = str;
    }

    public String getButton() {
        return this.button;
    }

    public void setButton(String str) {
        this.button = str;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String str) {
        this.url = str;
    }
}
