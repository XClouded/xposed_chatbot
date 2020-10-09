package com.alimama.moon.config.model;

public class MidH5TabModel {
    private String highlightedImageUrl;
    private String imageUrl;
    private String isSwitchMidH5Tab;
    private String schema;
    private String spm;
    private String title;
    private String type;

    public String getType() {
        return this.type;
    }

    public void setType(String str) {
        this.type = str;
    }

    public String getIsSwitchMidH5Tab() {
        return this.isSwitchMidH5Tab;
    }

    public void setIsSwitchMidH5Tab(String str) {
        this.isSwitchMidH5Tab = str;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public String getSchema() {
        return this.schema;
    }

    public void setSchema(String str) {
        this.schema = str;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setImageUrl(String str) {
        this.imageUrl = str;
    }

    public String getHighlightedImageUrl() {
        return this.highlightedImageUrl;
    }

    public void setHighlightedImageUrl(String str) {
        this.highlightedImageUrl = str;
    }

    public String getSpm() {
        return this.spm;
    }

    public void setSpm(String str) {
        this.spm = str;
    }

    public String toString() {
        return "MidH5TabModel{isSwitchMidH5Tab='" + this.isSwitchMidH5Tab + '\'' + ", title='" + this.title + '\'' + ", schema='" + this.schema + '\'' + ", imageUrl='" + this.imageUrl + '\'' + ", highlightedImageUrl='" + this.highlightedImageUrl + '\'' + ", spm='" + this.spm + '\'' + ", type='" + this.type + '\'' + '}';
    }
}
