package com.alimama.unionwl.uiframe.views.title.moreAction;

import android.content.Context;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class MoreActionItemFactory {
    protected Map<Character, MoreActionItem> mMoreActionItems = new LinkedHashMap();

    public abstract MoreActionItem createDefaultItem(Context context);

    public abstract MoreActionItem createItem(String str, Context context);

    public MoreActionItemFactory add(char c, int i, String str, MoreActionClickListener moreActionClickListener) {
        this.mMoreActionItems.put(Character.valueOf(c), MoreActionItem.createDropDown(i, str, moreActionClickListener));
        return this;
    }

    public MoreActionItemFactory add(char c, MoreActionItem moreActionItem) {
        this.mMoreActionItems.put(Character.valueOf(c), moreActionItem);
        return this;
    }

    public Map<Character, MoreActionItem> getItemMap() {
        return this.mMoreActionItems;
    }
}
