package com.alibaba.aliweex.adapter.view;

public class ElevatorItem {
    private int id;
    private boolean isHighLight;
    private boolean isImgShow;
    private String name;
    private int width;

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int i) {
        this.width = i;
    }

    public ElevatorItem(String str) {
        this.name = str;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public boolean getIsHighLight() {
        return this.isHighLight;
    }

    public void setIsHighLight(boolean z) {
        this.isHighLight = z;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int i) {
        this.id = i;
    }

    public boolean getIsImgShow() {
        return this.isImgShow;
    }

    public void setIsImgShow(boolean z) {
        this.isImgShow = z;
    }
}
