package com.alimama.union.app.personalCenter.model;

public class MineItemData {
    private boolean hidden;
    private String iconUrl;
    private boolean isEmpty;
    private String itemName;
    private String itemType;
    private String jumpUrl;

    public String getIconUrl() {
        return this.iconUrl;
    }

    public void setIconUrl(String str) {
        this.iconUrl = str;
    }

    public String getItemName() {
        return this.itemName;
    }

    public void setItemName(String str) {
        this.itemName = str;
    }

    public String getJumpUrl() {
        return this.jumpUrl;
    }

    public void setJumpUrl(String str) {
        this.jumpUrl = str;
    }

    public boolean isEmpty() {
        return this.isEmpty;
    }

    public void setIsEmpty(boolean z) {
        this.isEmpty = z;
    }

    public String getItemType() {
        return this.itemType;
    }

    public void setItemType(String str) {
        this.itemType = str;
    }

    public boolean isHidden() {
        return this.hidden;
    }

    public void setHidden(boolean z) {
        this.hidden = z;
    }
}
