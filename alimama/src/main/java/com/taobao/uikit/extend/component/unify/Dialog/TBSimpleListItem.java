package com.taobao.uikit.extend.component.unify.Dialog;

public class TBSimpleListItem {
    String mText;
    TBSimpleListItemType mType = TBSimpleListItemType.NORMAL;

    public TBSimpleListItem() {
    }

    public TBSimpleListItem(String str, TBSimpleListItemType tBSimpleListItemType) {
        this.mText = str;
        this.mType = tBSimpleListItemType;
    }

    public String getText() {
        return this.mText;
    }

    public TBSimpleListItemType getType() {
        return this.mType;
    }

    public void setText(String str) {
        this.mText = str;
    }

    public void setType(TBSimpleListItemType tBSimpleListItemType) {
        this.mType = tBSimpleListItemType;
    }
}
