package com.alimama.unionwl.uiframe.views.title.moreAction;

public class MoreActionItem {
    private int mIcon;
    private String mTitle;
    private MoreActionClickListener onClickListener;

    public static MoreActionItem createDropDown(int i, String str, MoreActionClickListener moreActionClickListener) {
        MoreActionItem moreActionItem = new MoreActionItem();
        moreActionItem.mIcon = i;
        moreActionItem.mTitle = str;
        moreActionItem.onClickListener = moreActionClickListener;
        return moreActionItem;
    }

    public static MoreActionItem createDropDown(String str, MoreActionClickListener moreActionClickListener) {
        MoreActionItem moreActionItem = new MoreActionItem();
        moreActionItem.mTitle = str;
        moreActionItem.onClickListener = moreActionClickListener;
        return moreActionItem;
    }

    public void onClick(MoreActionViewController moreActionViewController, MoreActionItemView moreActionItemView, MoreActionItem moreActionItem) {
        if (this.onClickListener != null) {
            this.onClickListener.onClick(moreActionViewController, moreActionItemView, moreActionItem);
        }
    }

    public int getIcon() {
        return this.mIcon;
    }

    public String getTitle() {
        return this.mTitle;
    }
}
