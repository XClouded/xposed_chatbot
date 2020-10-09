package com.alimama.moon.model;

public class PushMsgExts {
    private String target;
    private String url;

    public String getTarget() {
        return this.target;
    }

    public void setTarget(String str) {
        this.target = str;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String str) {
        this.url = str;
    }

    public String toString() {
        return "PushMsgExts{target='" + this.target + '\'' + ", url='" + this.url + '\'' + '}';
    }
}
