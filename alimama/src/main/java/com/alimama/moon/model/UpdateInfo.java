package com.alimama.moon.model;

public class UpdateInfo {
    private String info;
    private String md5;
    private String name;
    private String patchSize;
    private String patchUrl;
    private String pri;
    private String size;
    private String url;
    private String version;

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String str) {
        this.url = str;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String str) {
        this.version = str;
    }

    public String getInfo() {
        return this.info;
    }

    public void setInfo(String str) {
        this.info = str;
    }

    public String getSize() {
        return this.size;
    }

    public void setSize(String str) {
        this.size = str;
    }

    public String getPri() {
        return this.pri;
    }

    public void setPri(String str) {
        this.pri = str;
    }

    public String getMd5() {
        return this.md5;
    }

    public void setMd5(String str) {
        this.md5 = str;
    }

    public String getPatchUrl() {
        return this.patchUrl;
    }

    public void setPatchUrl(String str) {
        this.patchUrl = str;
    }

    public String getPatchSize() {
        return this.patchSize;
    }

    public void setPatchSize(String str) {
        this.patchSize = str;
    }
}
