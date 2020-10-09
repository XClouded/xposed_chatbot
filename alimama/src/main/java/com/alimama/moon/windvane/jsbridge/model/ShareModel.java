package com.alimama.moon.windvane.jsbridge.model;

import java.util.List;

public class ShareModel {
    List<String> imgList;
    String shareInfo;
    String taoCodeStr;

    public String getShareInfo() {
        return this.shareInfo;
    }

    public void setShareInfo(String str) {
        this.shareInfo = str;
    }

    public List<String> getImgList() {
        return this.imgList;
    }

    public void setImgList(List<String> list) {
        this.imgList = list;
    }

    public String getTaoCodeStr() {
        return this.taoCodeStr;
    }

    public void setTaoCodeStr(String str) {
        this.taoCodeStr = str;
    }
}
