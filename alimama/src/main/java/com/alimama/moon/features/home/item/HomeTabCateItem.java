package com.alimama.moon.features.home.item;

public class HomeTabCateItem {
    private String floorId;
    private String qieId;
    private String spm;
    private String title;
    private String type;

    public HomeTabCateItem() {
    }

    public HomeTabCateItem(String str, String str2, String str3) {
        this.title = str;
        this.type = str2;
        this.floorId = str3;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String str) {
        this.type = str;
    }

    public String getFloorId() {
        return this.floorId;
    }

    public void setFloorId(String str) {
        this.floorId = str;
    }

    public String getQieId() {
        return this.qieId;
    }

    public void setQieId(String str) {
        this.qieId = str;
    }

    public String getSpm() {
        return this.spm;
    }

    public void setSpm(String str) {
        this.spm = str;
    }
}
