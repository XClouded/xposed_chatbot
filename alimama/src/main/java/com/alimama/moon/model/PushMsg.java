package com.alimama.moon.model;

public class PushMsg {
    private String extData;
    private PushMsgExts exts;
    private String img;
    private String messageId;
    private String sound;
    private String taskId;
    private String text;
    private String ticker;
    private String title;
    private String url;

    public String getMessageId() {
        return this.messageId;
    }

    public void setMessageId(String str) {
        this.messageId = str;
    }

    public String getTaskId() {
        return this.taskId;
    }

    public void setTaskId(String str) {
        this.taskId = str;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public String getTicker() {
        return this.ticker == null ? "" : this.ticker;
    }

    public void setTicker(String str) {
        this.ticker = str;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String str) {
        this.text = str;
    }

    public String getUrl() {
        return this.url.toLowerCase();
    }

    public void setUrl(String str) {
        this.url = str;
    }

    public String getImg() {
        return this.img;
    }

    public void setImg(String str) {
        this.img = str;
    }

    public String getSound() {
        return this.sound;
    }

    public void setSound(String str) {
        this.sound = str;
    }

    public PushMsgExts getExts() {
        return this.exts;
    }

    public void setExts(PushMsgExts pushMsgExts) {
        this.exts = pushMsgExts;
    }

    public String getExtData() {
        return this.extData;
    }

    public void setExtData(String str) {
        this.extData = str;
    }

    public String toString() {
        return "title:" + this.title + " ticker:" + this.ticker + " text:" + this.text + " url:" + this.url + " img:" + this.img + " sound:" + this.sound + " exts:" + this.exts.toString();
    }
}
