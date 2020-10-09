package com.alimama.moon.windvane.jsbridge.model;

import java.util.List;

public class PictureBrowserModel {
    List<String> imgList;
    String pos;

    public String getPos() {
        return this.pos;
    }

    public void setPos(String str) {
        this.pos = str;
    }

    public List<String> getImgList() {
        return this.imgList;
    }

    public void setImgList(List<String> list) {
        this.imgList = list;
    }
}
