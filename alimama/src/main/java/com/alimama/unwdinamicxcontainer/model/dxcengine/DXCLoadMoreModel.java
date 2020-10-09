package com.alimama.unwdinamicxcontainer.model.dxcengine;

public class DXCLoadMoreModel {
    private String failedText;
    private String loadingText;
    private String noMoreText;
    private String normalText;

    public DXCLoadMoreModel(String str, String str2, String str3, String str4) {
        this.normalText = str;
        this.loadingText = str2;
        this.failedText = str3;
        this.noMoreText = str4;
    }

    public String getNormalText() {
        return this.normalText;
    }

    public void setNormalText(String str) {
        this.normalText = str;
    }

    public String getLoadingText() {
        return this.loadingText;
    }

    public void setLoadingText(String str) {
        this.loadingText = str;
    }

    public String getFailedText() {
        return this.failedText;
    }

    public void setFailedText(String str) {
        this.failedText = str;
    }

    public String getNoMoreText() {
        return this.noMoreText;
    }

    public void setNoMoreText(String str) {
        this.noMoreText = str;
    }
}
